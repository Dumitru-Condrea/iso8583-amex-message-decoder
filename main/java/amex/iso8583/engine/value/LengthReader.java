package amex.iso8583.engine.value;

import amex.iso8583.engine.core.ByteCursor;
import amex.iso8583.schema.common.LengthEncoding;
import amex.iso8583.schema.common.LengthType;
import amex.iso8583.schema.common.LengthUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LengthReader {

    private final ValueDecoder valueDecoder;

    public Integer readLength(ByteCursor cursor, LengthType lengthType, LengthEncoding lengthEncoding) {
        if (lengthType == null) {
            throw new IllegalArgumentException("lengthType must not be null");
        }

        if (lengthEncoding == null) {
            throw new IllegalArgumentException("lengthEncoding must not be null");
        }

        switch (lengthType) {
            case FIXED:
                return null;
            case LVAR:
                return parseLength(cursor.readBytes(1), lengthEncoding);
            case LLVAR:
                return parseLength(cursor.readBytes(2), lengthEncoding);
            case LLLVAR:
                return parseLength(cursor.readBytes(3), lengthEncoding);
            default:
                throw new IllegalArgumentException("Unsupported length type: " + lengthType);
        }
    }

    public int toByteLength(int logicalLength, LengthUnit lengthUnit) {
        if (logicalLength < 0) {
            throw new IllegalArgumentException("logicalLength must be >= 0");
        }
        if (lengthUnit == null) {
            throw new IllegalArgumentException("lengthUnit must not be null");
        }

        switch (lengthUnit) {
            case BYTES:
            case CHARS:
                return logicalLength;
            case DIGITS:
                return (logicalLength + 1) / 2;
            default:
                throw new IllegalArgumentException("Unsupported length unit: " + lengthUnit);
        }
    }

    private int parseLength(byte[] rawLength, LengthEncoding lengthEncoding) {
        String value;

        switch (lengthEncoding) {
            case ASCII:
                value = valueDecoder.decodeAscii(rawLength);
                break;
            case EBCDIC:
                value = valueDecoder.decodeEbcdic(rawLength);
                break;
            case BCD:
                value = valueDecoder.decodeBcd(rawLength, rawLength.length * 2);
                break;
            case NONE:
                throw new IllegalArgumentException("LengthEncoding.NONE is not valid for variable length fields");
            default:
                throw new IllegalArgumentException("Unsupported length encoding: " + lengthEncoding);
        }

        return Integer.parseInt(value.trim());
    }
}