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
public class FieldDefinition {

    private final int number;
    private final String name;
    private final FieldPresence presence;
    private final DataEncoding dataEncoding;
    private final FieldValueFormat valueFormat;
    private final LengthType lengthType;
    private final LengthEncoding lengthEncoding;
    private final LengthUnit lengthUnit;
    private final Integer fixedLength;
    private final Integer maxLength;
    private final FieldDefinitionAdjuster adjuster;

    protected FieldDefinition(Builder<?> builder) {
        this.number = builder.number;
        this.name = Objects.requireNonNull(builder.name, "name must not be null");
        this.presence = Objects.requireNonNull(builder.presence, "presence must not be null");
        this.dataEncoding = Objects.requireNonNull(builder.dataEncoding, "dataEncoding must not be null");
        this.valueFormat = Objects.requireNonNull(builder.valueFormat, "valueFormat must not be null");
        this.lengthType = Objects.requireNonNull(builder.lengthType, "lengthType must not be null");
        this.lengthEncoding = Objects.requireNonNull(builder.lengthEncoding, "lengthEncoding must not be null");
        this.lengthUnit = Objects.requireNonNull(builder.lengthUnit, "lengthUnit must not be null");
        this.fixedLength = builder.fixedLength;
        this.maxLength = builder.maxLength;
        this.adjuster = builder.adjuster == null ? NoOpFieldDefinitionAdjuster.INSTANCE : builder.adjuster;

        validate();
    }

    private void validate() {
        if (number < 0) {
            throw new IllegalArgumentException("field number must be >= 0");
        }
        if (lengthType == LengthType.FIXED && fixedLength == null) {
            throw new IllegalArgumentException("fixedLength must be set for FIXED field: " + name);
        }
        if (lengthType != LengthType.FIXED && maxLength == null) {
            throw new IllegalArgumentException("maxLength must be set for variable field: " + name);
        }
    }

    public static Builder<?> builder() {
        return new BuilderImpl();
    }

    public static abstract class Builder<T extends Builder<T>> {
        protected int number;
        protected String name;
        protected FieldPresence presence = FieldPresence.OPTIONAL;
        protected DataEncoding dataEncoding;
        protected FieldValueFormat valueFormat;
        protected LengthType lengthType = LengthType.FIXED;
        protected LengthEncoding lengthEncoding = LengthEncoding.NONE;
        protected LengthUnit lengthUnit;
        protected Integer fixedLength;
        protected Integer maxLength;
        protected FieldDefinitionAdjuster adjuster = NoOpFieldDefinitionAdjuster.INSTANCE;

        protected abstract T self();

        public T number(int number) {
            this.number = number;
            return self();
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T presence(FieldPresence presence) {
            this.presence = presence;
            return self();
        }

        public T dataEncoding(DataEncoding dataEncoding) {
            this.dataEncoding = dataEncoding;
            return self();
        }

        public T valueFormat(FieldValueFormat valueFormat) {
            this.valueFormat = valueFormat;
            return self();
        }

        public T lengthType(LengthType lengthType) {
            this.lengthType = lengthType;
            return self();
        }

        public T lengthEncoding(LengthEncoding lengthEncoding) {
            this.lengthEncoding = lengthEncoding;
            return self();
        }

        public T lengthUnit(LengthUnit lengthUnit) {
            this.lengthUnit = lengthUnit;
            return self();
        }

        public T fixedLength(int fixedLength) {
            this.fixedLength = fixedLength;
            return self();
        }

        public T maxLength(int maxLength) {
            this.maxLength = maxLength;
            return self();
        }

        public T adjuster(FieldDefinitionAdjuster adjuster) {
            this.adjuster = adjuster;
            return self();
        }

        public FieldDefinition build() {
            return new FieldDefinition(this);
        }
    }

    private static final class BuilderImpl extends Builder<BuilderImpl> {
        @Override
        protected BuilderImpl self() {
            return this;
        }
    }
}
