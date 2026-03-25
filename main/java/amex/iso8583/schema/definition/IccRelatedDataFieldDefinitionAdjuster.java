package amex.iso8583.schema.definition;

import amex.iso8583.api.DecodeOptionKeys;
import amex.iso8583.api.DecodeOptions;
import amex.iso8583.schema.common.*;

import java.util.ArrayList;
import java.util.List;

public class IccRelatedDataFieldDefinitionAdjuster implements FieldDefinitionAdjuster {

    @Override
    public FieldDefinition adjust(FieldDefinition fieldDefinition, DecodeOptions options) {
        if (!(fieldDefinition instanceof CompositeFieldDefinition)) {
            return fieldDefinition;
        }

        if (options == null) {
            return fieldDefinition;
        }

        CompositeFieldDefinition composite = (CompositeFieldDefinition) fieldDefinition;

        boolean simplified = getBoolean(options, DecodeOptionKeys.DE55_SIMPLIFIED_EMV);
        String variant = getString(options, DecodeOptionKeys.DE55_VARIANT);

        if (!simplified && isBlank(variant)) {
            return fieldDefinition;
        }

        List<SubfieldDefinition> subfields = new ArrayList<>(composite.getSubfields());

        if (simplified) {
            subfields = removeSubfieldByName(subfields, "ICC_RELATED_DATA.ISSUER_APPLICATION_DATA");
            subfields = removeSubfieldByName(subfields, "ICC_RELATED_DATA.OTHER_AMOUNT");
        }

        if ("SIMPLIFIED".equalsIgnoreCase(variant)) {
            subfields = renameSubfield(
                    subfields,
                    "ICC_RELATED_DATA.APPLICATION_CRYPTOGRAM",
                    "ICC_RELATED_DATA.APPLICATION_CRYPTOGRAM_SIMPLIFIED"
            );

            subfields = changeSubfieldMaxLength(
                    subfields,
                    "ICC_RELATED_DATA.ISSUER_APPLICATION_DATA",
                    16
            );
        }

        if ("LEGACY".equalsIgnoreCase(variant)) {
            subfields = replaceSubfield(
                    subfields,
                    "ICC_RELATED_DATA.ISSUER_APPLICATION_DATA",
                    SubfieldDefinition.builder()
                            .subfieldNumber(2)
                            .name("ICC_RELATED_DATA.ISSUER_APPLICATION_DATA")
                            .presence(FieldPresence.OPTIONAL)
                            .dataEncoding(DataEncoding.HEX)
                            .valueFormat(FieldValueFormat.HEX_STRING)
                            .lengthType(LengthType.LLVAR)
                            .lengthEncoding(LengthEncoding.BCD)
                            .lengthUnit(LengthUnit.BYTES)
                            .maxLength(40)
                            .build()
            );
        }

        if ("WITH_EXTRA_TAG".equalsIgnoreCase(variant)) {
            subfields = addSubfieldAfter(
                    subfields,
                    "ICC_RELATED_DATA.CRYPTOGRAM_INFORMATION_DATA",
                    SubfieldDefinition.builder()
                            .subfieldNumber(15)
                            .name("ICC_RELATED_DATA.EXTRA_ISSUER_CONTEXT")
                            .presence(FieldPresence.OPTIONAL)
                            .dataEncoding(DataEncoding.HEX)
                            .valueFormat(FieldValueFormat.HEX_STRING)
                            .lengthType(LengthType.FIXED)
                            .lengthEncoding(LengthEncoding.NONE)
                            .lengthUnit(LengthUnit.BYTES)
                            .fixedLength(4)
                            .build()
            );
        }

        return copyCompositeWithSubfields(composite, subfields);
    }

    private CompositeFieldDefinition copyCompositeWithSubfields(
            CompositeFieldDefinition source,
            List<SubfieldDefinition> subfields
    ) {
        CompositeFieldDefinition.Builder builder = CompositeFieldDefinition.builder()
                .number(source.getNumber())
                .name(source.getName())
                .presence(source.getPresence())
                .dataEncoding(source.getDataEncoding())
                .valueFormat(source.getValueFormat())
                .lengthType(source.getLengthType())
                .lengthEncoding(source.getLengthEncoding())
                .lengthUnit(source.getLengthUnit())
                .adjuster(source.getAdjuster())
                .subfields(subfields);

        if (source.getFixedLength() != null) {
            builder.fixedLength(source.getFixedLength());
        }
        if (source.getMaxLength() != null) {
            builder.maxLength(source.getMaxLength());
        }

        return builder.build();
    }

    private List<SubfieldDefinition> removeSubfieldByName(List<SubfieldDefinition> source, String name) {
        List<SubfieldDefinition> out = new ArrayList<>();
        for (SubfieldDefinition subfield : source) {
            if (!name.equals(subfield.getName())) {
                out.add(subfield);
            }
        }
        return out;
    }

    private List<SubfieldDefinition> renameSubfield(List<SubfieldDefinition> source, String oldName, String newName) {
        List<SubfieldDefinition> out = new ArrayList<>();

        for (SubfieldDefinition subfield : source) {
            if (oldName.equals(subfield.getName())) {
                out.add(copySubfieldWithName(subfield, newName));
            } else {
                out.add(subfield);
            }
        }

        return out;
    }

    private List<SubfieldDefinition> changeSubfieldMaxLength(List<SubfieldDefinition> source, String name, int maxLength) {
        List<SubfieldDefinition> out = new ArrayList<>();

        for (SubfieldDefinition subfield : source) {
            if (name.equals(subfield.getName())) {
                out.add(copySubfieldWithMaxLength(subfield, maxLength));
            } else {
                out.add(subfield);
            }
        }

        return out;
    }

    private List<SubfieldDefinition> replaceSubfield(
            List<SubfieldDefinition> source,
            String name,
            SubfieldDefinition replacement
    ) {
        List<SubfieldDefinition> out = new ArrayList<>();

        for (SubfieldDefinition subfield : source) {
            if (name.equals(subfield.getName())) {
                out.add(replacement);
            } else {
                out.add(subfield);
            }
        }

        return out;
    }

    private List<SubfieldDefinition> addSubfieldAfter(
            List<SubfieldDefinition> source,
            String afterName,
            SubfieldDefinition newSubfield
    ) {
        List<SubfieldDefinition> out = new ArrayList<>();

        for (SubfieldDefinition subfield : source) {
            out.add(subfield);
            if (afterName.equals(subfield.getName())) {
                out.add(newSubfield);
            }
        }

        return out;
    }

    private SubfieldDefinition copySubfieldWithName(SubfieldDefinition source, String newName) {
        SubfieldDefinition.Builder builder = SubfieldDefinition.builder()
                .subfieldNumber(source.getSubfieldNumber())
                .name(newName)
                .presence(source.getPresence())
                .dataEncoding(source.getDataEncoding())
                .valueFormat(source.getValueFormat())
                .lengthType(source.getLengthType())
                .lengthEncoding(source.getLengthEncoding())
                .lengthUnit(source.getLengthUnit());

        if (source.getFixedLength() != null) {
            builder.fixedLength(source.getFixedLength());
        }
        if (source.getMaxLength() != null) {
            builder.maxLength(source.getMaxLength());
        }

        return builder.build();
    }

    private SubfieldDefinition copySubfieldWithMaxLength(SubfieldDefinition source, int maxLength) {
        SubfieldDefinition.Builder builder = SubfieldDefinition.builder()
                .subfieldNumber(source.getSubfieldNumber())
                .name(source.getName())
                .presence(source.getPresence())
                .dataEncoding(source.getDataEncoding())
                .valueFormat(source.getValueFormat())
                .lengthType(source.getLengthType())
                .lengthEncoding(source.getLengthEncoding())
                .lengthUnit(source.getLengthUnit());

        if (source.getFixedLength() != null) {
            builder.fixedLength(source.getFixedLength());
        }
        builder.maxLength(maxLength);

        return builder.build();
    }

    private boolean getBoolean(DecodeOptions options, String key) {
        Object value = options.get(key);
        return value instanceof Boolean && (Boolean) value;
    }

    private String getString(DecodeOptions options, String key) {
        Object value = options.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
