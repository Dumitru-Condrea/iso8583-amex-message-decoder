package amex.iso8583.engine.field;

import amex.iso8583.api.DecodeOptions;
import amex.iso8583.engine.core.ByteCursor;
import amex.iso8583.engine.core.DecodedField;
import amex.iso8583.engine.core.DecodedFieldMeta;
import amex.iso8583.engine.core.DecodedSubfield;
import amex.iso8583.engine.value.LengthReader;
import amex.iso8583.engine.value.ValueDecoder;
import amex.iso8583.exception.CompositeDecodeException;
import amex.iso8583.exception.DecodeException;
import amex.iso8583.exception.InvalidLengthException;
import amex.iso8583.schema.common.DataEncoding;
import amex.iso8583.schema.common.FieldValueFormat;
import amex.iso8583.schema.common.LengthType;
import amex.iso8583.schema.common.LengthUnit;
import amex.iso8583.schema.definition.CompositeFieldDefinition;
import amex.iso8583.schema.definition.FieldDefinition;
import amex.iso8583.schema.definition.SubfieldDefinition;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CompositeFieldDecoder implements FieldDecoder {

    private final ValueDecoder valueDecoder;
    private final LengthReader lengthReader;

    @Override
    public DecodedField decode(ByteCursor cursor, FieldDefinition fieldDefinition, DecodeOptions options) {
        if (!(fieldDefinition instanceof CompositeFieldDefinition)) {
            throw new IllegalArgumentException("CompositeFieldDecoder requires CompositeFieldDefinition");
        }

        CompositeFieldDefinition compositeDefinition = (CompositeFieldDefinition) fieldDefinition;
        int parentStartOffset = cursor.position();

        try {
            int parentLogicalLength = resolveParentLogicalLength(cursor, compositeDefinition);
            validateParentLogicalLength(compositeDefinition, parentLogicalLength);

            int parentByteLength = resolveByteLength(parentLogicalLength, compositeDefinition.getLengthUnit());
            byte[] parentRaw = cursor.readBytes(parentByteLength);

            ByteCursor subCursor = new ByteCursor(parentRaw);
            List<DecodedSubfield> decodedSubfields = decodeSubfields(subCursor, compositeDefinition);

            String parentDisplayValue = buildParentDisplayValue(decodedSubfields);

            int parentEndOffset = cursor.position();
            DecodedFieldMeta parentMeta = new DecodedFieldMeta(
                    compositeDefinition.getNumber(),
                    compositeDefinition.getName(),
                    parentStartOffset,
                    parentEndOffset,
                    parentByteLength,
                    valueDecoder.toHex(parentRaw)
            );

            return new DecodedField(
                    compositeDefinition.getNumber(),
                    compositeDefinition.getName(),
                    parentDisplayValue,
                    decodedSubfields,
                    parentMeta
            );

        } catch (DecodeException e) {
            throw e;
        } catch (Exception e) {
            throw new CompositeDecodeException(
                    "Failed to decode composite field: " + compositeDefinition.getName(),
                    null,
                    compositeDefinition.getNumber(),
                    compositeDefinition.getName(),
                    parentStartOffset,
                    null,
                    e
            );
        }
    }

    private List<DecodedSubfield> decodeSubfields(ByteCursor subCursor, CompositeFieldDefinition compositeDefinition) {
        List<DecodedSubfield> result = new ArrayList<DecodedSubfield>();

        for (SubfieldDefinition subfieldDefinition : compositeDefinition.getSubfields()) {
            int subfieldStartOffset = subCursor.position();

            try {
                int logicalLength = resolveSubfieldLogicalLength(subCursor, subfieldDefinition);
                validateSubfieldLogicalLength(subfieldDefinition, logicalLength);

                int byteLength = resolveByteLength(logicalLength, subfieldDefinition.getLengthUnit());
                byte[] raw = subCursor.readBytes(byteLength);

                String displayValue = decodeDisplayValue(
                        raw,
                        subfieldDefinition.getDataEncoding(),
                        subfieldDefinition.getValueFormat(),
                        logicalLength
                );

                int subfieldEndOffset = subCursor.position();
                DecodedFieldMeta meta = new DecodedFieldMeta(
                        subfieldDefinition.getSubfieldNumber(),
                        subfieldDefinition.getName(),
                        subfieldStartOffset,
                        subfieldEndOffset,
                        byteLength,
                        valueDecoder.toHex(raw)
                );

                result.add(new DecodedSubfield(
                        subfieldDefinition.getSubfieldNumber(),
                        subfieldDefinition.getName(),
                        displayValue,
                        meta
                ));

            } catch (Exception e) {
                throw new CompositeDecodeException(
                        "Failed to decode subfield: " + subfieldDefinition.getName(),
                        null,
                        compositeDefinition.getNumber(),
                        compositeDefinition.getName(),
                        subfieldStartOffset,
                        result,
                        e
                );
            }
        }

        if (subCursor.hasRemaining()) {
            throw new CompositeDecodeException(
                    "Composite field not fully consumed: " + compositeDefinition.getName(),
                    null,
                    compositeDefinition.getNumber(),
                    compositeDefinition.getName(),
                    subCursor.position(),
                    result,
                    null
            );
        }

        return result;
    }

    private String buildParentDisplayValue(List<DecodedSubfield> decodedSubfields) {
        StringBuilder sb = new StringBuilder();

        for (DecodedSubfield subfield : decodedSubfields) {
            if (subfield.getDisplayValue() != null) {
                sb.append(subfield.getDisplayValue());
            }
        }

        return sb.toString();
    }

    private int resolveParentLogicalLength(ByteCursor cursor, CompositeFieldDefinition definition) {
        if (definition.getLengthType() == LengthType.FIXED) {
            return definition.getFixedLength();
        }

        Integer logicalLength = lengthReader.readLength(
                cursor,
                definition.getLengthType(),
                definition.getLengthEncoding()
        );

        if (logicalLength == null) {
            throw new InvalidLengthException(
                    "Composite field length was not resolved: " + definition.getName(),
                    null,
                    definition.getNumber(),
                    definition.getName(),
                    cursor.position(),
                    null
            );
        }

        return logicalLength;
    }

    private int resolveSubfieldLogicalLength(ByteCursor cursor, SubfieldDefinition definition) {
        if (definition.getLengthType() == LengthType.FIXED) {
            return definition.getFixedLength();
        }

        Integer logicalLength = lengthReader.readLength(
                cursor,
                definition.getLengthType(),
                definition.getLengthEncoding()
        );

        if (logicalLength == null) {
            throw new InvalidLengthException(
                    "Composite subfield length was not resolved: " + definition.getName(),
                    null,
                    null,
                    definition.getName(),
                    cursor.position(),
                    null
            );
        }

        return logicalLength;
    }

    private void validateParentLogicalLength(CompositeFieldDefinition definition, int logicalLength) {
        if (logicalLength < 0) {
            throw new InvalidLengthException(
                    "Negative composite field length: " + definition.getName(),
                    null,
                    definition.getNumber(),
                    definition.getName(),
                    null,
                    null
            );
        }

        if (definition.getLengthType() != LengthType.FIXED && definition.getMaxLength() != null) {
            if (logicalLength > definition.getMaxLength()) {
                throw new InvalidLengthException(
                        "Composite field length exceeds max length: " + definition.getName()
                                + ", actual=" + logicalLength
                                + ", max=" + definition.getMaxLength(),
                        null,
                        definition.getNumber(),
                        definition.getName(),
                        null,
                        null
                );
            }
        }
    }

    private void validateSubfieldLogicalLength(SubfieldDefinition definition, int logicalLength) {
        if (logicalLength < 0) {
            throw new InvalidLengthException(
                    "Negative subfield length: " + definition.getName(),
                    null,
                    null,
                    definition.getName(),
                    null,
                    null
            );
        }

        if (definition.getLengthType() != LengthType.FIXED && definition.getMaxLength() != null) {
            if (logicalLength > definition.getMaxLength()) {
                throw new InvalidLengthException(
                        "Subfield length exceeds max length: " + definition.getName()
                                + ", actual=" + logicalLength
                                + ", max=" + definition.getMaxLength(),
                        null,
                        null,
                        definition.getName(),
                        null,
                        null
                );
            }
        }
    }

    private int resolveByteLength(int logicalLength, LengthUnit lengthUnit) {
        return lengthReader.toByteLength(logicalLength, lengthUnit);
    }

    private String decodeDisplayValue(
            byte[] raw,
            DataEncoding dataEncoding,
            FieldValueFormat valueFormat,
            int logicalLength
    ) {
        String decoded;

        if (valueFormat == FieldValueFormat.HEX_STRING) {
            decoded = valueDecoder.toHex(raw);
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