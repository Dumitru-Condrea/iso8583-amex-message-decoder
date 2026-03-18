package amex.iso8583.schema.definition;

import amex.iso8583.schema.common.DataEncoding;
import amex.iso8583.schema.common.FieldPresence;
import amex.iso8583.schema.common.FieldValueFormat;
import amex.iso8583.schema.common.LengthEncoding;
import amex.iso8583.schema.common.LengthType;
import amex.iso8583.schema.common.LengthUnit;
import lombok.Getter;

import java.util.Objects;

@Getter
public class SubfieldDefinition {

    private final int subfieldNumber;
    private final String name;
    private final FieldPresence presence;
    private final DataEncoding dataEncoding;
    private final FieldValueFormat valueFormat;
    private final LengthType lengthType;
    private final LengthEncoding lengthEncoding;
    private final LengthUnit lengthUnit;
    private final Integer fixedLength;
    private final Integer maxLength;

    protected SubfieldDefinition(Builder builder) {
        this.subfieldNumber = builder.subfieldNumber;
        this.name = Objects.requireNonNull(builder.name, "name must not be null");
        this.presence = Objects.requireNonNull(builder.presence, "presence must not be null");
        this.dataEncoding = Objects.requireNonNull(builder.dataEncoding, "dataEncoding must not be null");
        this.valueFormat = Objects.requireNonNull(builder.valueFormat, "valueFormat must not be null");
        this.lengthType = Objects.requireNonNull(builder.lengthType, "lengthType must not be null");
        this.lengthEncoding = Objects.requireNonNull(builder.lengthEncoding, "lengthEncoding must not be null");
        this.lengthUnit = Objects.requireNonNull(builder.lengthUnit, "lengthUnit must not be null");
        this.fixedLength = builder.fixedLength;
        this.maxLength = builder.maxLength;

        validate();
    }

    private void validate() {
        if (lengthType == LengthType.FIXED && fixedLength == null) {
            throw new IllegalArgumentException("fixedLength must be set for FIXED subfield: " + name);
        }
        if (lengthType != LengthType.FIXED && maxLength == null) {
            throw new IllegalArgumentException("maxLength must be set for variable subfield: " + name);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int subfieldNumber;
        private String name;
        private FieldPresence presence = FieldPresence.OPTIONAL;
        private DataEncoding dataEncoding;
        private FieldValueFormat valueFormat;
        private LengthType lengthType = LengthType.FIXED;
        private LengthEncoding lengthEncoding = LengthEncoding.NONE;
        private LengthUnit lengthUnit;
        private Integer fixedLength;
        private Integer maxLength;

        public Builder subfieldNumber(int subfieldNumber) {
            this.subfieldNumber = subfieldNumber;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder presence(FieldPresence presence) {
            this.presence = presence;
            return this;
        }

        public Builder dataEncoding(DataEncoding dataEncoding) {
            this.dataEncoding = dataEncoding;
            return this;
        }

        public Builder valueFormat(FieldValueFormat valueFormat) {
            this.valueFormat = valueFormat;
            return this;
        }

        public Builder lengthType(LengthType lengthType) {
            this.lengthType = lengthType;
            return this;
        }

        public Builder lengthEncoding(LengthEncoding lengthEncoding) {
            this.lengthEncoding = lengthEncoding;
            return this;
        }

        public Builder lengthUnit(LengthUnit lengthUnit) {
            this.lengthUnit = lengthUnit;
            return this;
        }

        public Builder fixedLength(int fixedLength) {
            this.fixedLength = fixedLength;
            return this;
        }

        public Builder maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public SubfieldDefinition build() {
            return new SubfieldDefinition(this);
        }
    }
}
