package amex.iso8583.engine.field;

import amex.iso8583.api.DecodeOptions;
import amex.iso8583.engine.core.ByteCursor;
import amex.iso8583.engine.core.DecodedField;
import amex.iso8583.engine.core.DecodedFieldMeta;
import amex.iso8583.engine.value.LengthReader;
import amex.iso8583.engine.value.ValueDecoder;
import amex.iso8583.exception.DecodeException;
import amex.iso8583.exception.InvalidLengthException;
import amex.iso8583.schema.common.DataEncoding;
import amex.iso8583.schema.common.FieldValueFormat;
import amex.iso8583.schema.common.LengthType;
import amex.iso8583.schema.common.LengthUnit;
import amex.iso8583.schema.definition.FieldDefinition;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

@RequiredArgsConstructor
public class PrimitiveFieldDecoder implements FieldDecoder {

    private final ValueDecoder valueDecoder;
    private final LengthReader lengthReader;

    @Override
    public DecodedField decode(ByteCursor cursor, FieldDefinition fieldDefinition, DecodeOptions options) {
        if (cursor == null) {
            throw new IllegalArgumentException("cursor must not be null");
        }
        if (fieldDefinition == null) {
            throw new IllegalArgumentException("fieldDefinition must not be null");
        }

        int startOffset = cursor.position();

        try {
            int logicalLength = resolveLogicalLength(cursor, fieldDefinition);
            validateLogicalLength(fieldDefinition, logicalLength);

            int byteLength = resolveByteLength(logicalLength, fieldDefinition.getLengthUnit());
            byte[] raw = cursor.readBytes(byteLength);

            String displayValue = decodeDisplayValue(raw, fieldDefinition, logicalLength);

            int endOffset = cursor.position();
            DecodedFieldMeta meta = new DecodedFieldMeta(
                    fieldDefinition.getNumber(),
                    fieldDefinition.getName(),
                    startOffset,
                    endOffset,
                    byteLength,
                    valueDecoder.toHex(raw)
            );

            return new DecodedField(
                    fieldDefinition.getNumber(),
                    fieldDefinition.getName(),
                    displayValue,
                    Collections.emptyList(),
                    meta
            );

        } catch (DecodeException e) {
            throw e;
        } catch (Exception e) {
            throw new DecodeException(
                    "Failed to decode primitive field: " + fieldDefinition.getName(),
                    null,
                    fieldDefinition.getNumber(),
                    fieldDefinition.getName(),
                    startOffset,
                    e
            );
        }
    }

    private int resolveLogicalLength(ByteCursor cursor, FieldDefinition fieldDefinition) {
        if (fieldDefinition.getLengthType() == LengthType.FIXED) {
            return fieldDefinition.getFixedLength();
        }

        Integer logicalLength = lengthReader.readLength(
                cursor,
                fieldDefinition.getLengthType(),
                fieldDefinition.getLengthEncoding()
        );

        if (logicalLength == null) {
            throw new InvalidLengthException(
                    "Variable field length was not resolved: " + fieldDefinition.getName(),
                    null,
                    fieldDefinition.getNumber(),
                    fieldDefinition.getName(),
                    cursor.position(),
                    null
            );
        }

        return logicalLength;
    }

    private void validateLogicalLength(FieldDefinition fieldDefinition, int logicalLength) {
        if (logicalLength < 0) {
            throw new InvalidLengthException(
                    "Negative field length: " + fieldDefinition.getName(),
                    null,
                    fieldDefinition.getNumber(),
                    fieldDefinition.getName(),
                    null,
                    null
            );
        }

        if (fieldDefinition.getLengthType() != LengthType.FIXED && fieldDefinition.getMaxLength() != null) {
            if (logicalLength > fieldDefinition.getMaxLength()) {
                throw new InvalidLengthException(
                        "Field length exceeds max length: " + fieldDefinition.getName()
                                + ", actual=" + logicalLength
                                + ", max=" + fieldDefinition.getMaxLength(),
                        null,
                        fieldDefinition.getNumber(),
                        fieldDefinition.getName(),
                        null,
                        null
                );
            }
        }
    }

    private int resolveByteLength(int logicalLength, LengthUnit lengthUnit) {
        return lengthReader.toByteLength(logicalLength, lengthUnit);
    }

    private String decodeDisplayValue(byte[] raw, FieldDefinition fieldDefinition, int logicalLength) {
        DataEncoding dataEncoding = fieldDefinition.getDataEncoding();
        FieldValueFormat valueFormat = fieldDefinition.getValueFormat();

        String decoded;

        if (valueFormat == FieldValueFormat.HEX_STRING) {
            decoded = valueDecoder.toHex(raw);
            return valueDecoder.normalizeDisplaySpaces(decoded);
        }

        if (valueFormat == FieldValueFormat.MASKED_PAN) {
            decoded = valueDecoder.decodeMaskedPan(raw);
            return valueDecoder.normalizeDisplaySpaces(decoded);
        }

        switch (dataEncoding) {
            case EBCDIC:
                decoded = valueDecoder.decodeEbcdic(raw);
                break;
            case ASCII:
                decoded = valueDecoder.decodeAscii(raw);
                break;
            case BCD:
                decoded = valueDecoder.decodeBcd(raw, logicalLength);
                break;
            case BINARY:
                decoded = valueDecoder.decodeBinaryAsHex(raw);
                break;
            case HEX:
                decoded = valueDecoder.decodeHexBytesAsHex(raw);
                break;
            default:
                throw new IllegalArgumentException("Unsupported data encoding: " + dataEncoding);
        }

        return valueDecoder.normalizeDisplaySpaces(decoded);
    }
}