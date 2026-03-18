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

        byte[] primary = cursor.readBytes(8);
        String primaryHex = valueDecoder.toHex(primary);

        boolean hasSecondary = isBitSet(primary, 1);
        byte[] secondary = null;
        String secondaryHex = null;

        if (hasSecondary) {
            if (!profile.isSecondaryBitmapSupported()) {
                throw new IllegalArgumentException("Secondary bitmap detected but not allowed by profile");
            }

            secondary = cursor.readBytes(8);
            secondaryHex = valueDecoder.toHex(secondary);
        }

        List<Integer> activeFields = collectActiveFields(primary, secondary);
        return new BitmapSet(primaryHex, secondaryHex, activeFields);
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