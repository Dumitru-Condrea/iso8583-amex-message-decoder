package amex.iso8583.engine.header;

import amex.iso8583.engine.core.ByteCursor;
import amex.iso8583.engine.value.ValueDecoder;
import amex.iso8583.schema.common.DataEncoding;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class HeaderDecoder {

    private final ValueDecoder valueDecoder;

    public String readMti(ByteCursor cursor, MessageHeaderProfile profile) {
        validateProfile(profile);

        byte[] mtiBytes = cursor.readBytes(4);

        if (profile.getMtiEncoding() == DataEncoding.EBCDIC) {
            return valueDecoder.decodeEbcdic(mtiBytes);
        }

        if (profile.getMtiEncoding() == DataEncoding.ASCII) {
            return valueDecoder.decodeAscii(mtiBytes);
        }

        throw new IllegalArgumentException("Unsupported MTI encoding: " + profile.getMtiEncoding());
    }

    public BitmapSet readBitmaps(ByteCursor cursor, MessageHeaderProfile profile) {
        validateProfile(profile);

        if (profile.getBitmapEncoding() != DataEncoding.BINARY) {
            throw new IllegalArgumentException("Only binary bitmap is currently supported");
        }

        byte[] primary = readBitmapBytes(cursor, 8);
        String primaryHex = valueDecoder.toHex(primary);

        boolean hasSecondary = isBitSet(primary, 1);
        byte[] secondary = null;
        String secondaryHex = null;

        if (hasSecondary) {
            if (!profile.isSecondaryBitmapSupported()) {
                throw new IllegalArgumentException("Secondary bitmap detected but not allowed by profile");
            }

            secondary = readBitmapBytes(cursor, 8);
            secondaryHex = valueDecoder.toHex(secondary);
        }

        List<Integer> activeFields = collectActiveFields(primary, secondary);
        return new BitmapSet(primaryHex, secondaryHex, activeFields);
    }

    private byte[] readBitmapBytes(ByteCursor cursor, int logicalByteCount) {
        byte[] bitmap = new byte[logicalByteCount];
        int out = 0;

        while (out < logicalByteCount) {
            if (isEscapedHexByte(cursor)) {
                bitmap[out++] = readEscapedHexByte(cursor);
            } else {
                bitmap[out++] = cursor.readByte();
            }
        }

        return bitmap;
    }

    private boolean isEscapedHexByte(ByteCursor cursor) {
        if (cursor.remaining() < 4) {
            return false;
        }

        byte b0 = cursor.peek(0);
        byte b1 = cursor.peek(1);
        byte b2 = cursor.peek(2);
        byte b3 = cursor.peek(3);

        return b0 == '{'
                && b3 == '}'
                && isHexByte(b1)
                && isHexByte(b2);
    }

    private byte readEscapedHexByte(ByteCursor cursor) {
        cursor.readByte();
        byte h1 = cursor.readByte();
        byte h2 = cursor.readByte();
        cursor.readByte();

        int hi = Character.digit((char) h1, 16);
        int lo = Character.digit((char) h2, 16);

        if (hi < 0 || lo < 0) {
            throw new IllegalArgumentException("Invalid escaped bitmap byte");
        }

        return (byte) ((hi << 4) | lo);
    }

    private boolean isHexByte(byte value) {
        char c = (char) value;
        return (c >= '0' && c <= '9')
                || (c >= 'A' && c <= 'F')
                || (c >= 'a' && c <= 'f');
    }

    private List<Integer> collectActiveFields(byte[] primary, byte[] secondary) {
        List<Integer> fields = new ArrayList<>();
        collectFieldsFromBitmap(primary, 1, fields);

        if (secondary != null) {
            collectFieldsFromBitmap(secondary, 65, fields);
        }

        fields.remove(Integer.valueOf(1));

        return fields;
    }

    private void collectFieldsFromBitmap(byte[] bitmap, int baseFieldNumber, List<Integer> out) {
        for (int byteIndex = 0; byteIndex < bitmap.length; byteIndex++) {
            int unsigned = bitmap[byteIndex] & 0xFF;

            for (int bitIndex = 0; bitIndex < 8; bitIndex++) {
                int mask = 1 << (7 - bitIndex);
                if ((unsigned & mask) != 0) {
                    int fieldNumber = baseFieldNumber + (byteIndex * 8) + bitIndex;
                    out.add(fieldNumber);
                }
            }
        }
    }

    private boolean isBitSet(byte[] bitmap, int fieldNumber) {
        int zeroBased = fieldNumber - 1;
        int byteIndex = zeroBased / 8;
        int bitIndex = zeroBased % 8;
        int mask = 1 << (7 - bitIndex);

        return (bitmap[byteIndex] & mask) != 0;
    }

    private void validateProfile(MessageHeaderProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("profile must not be null");
        }
    }
}