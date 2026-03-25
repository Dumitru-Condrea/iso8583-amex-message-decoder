package amex.iso8583.schema.definition;

import amex.iso8583.api.DecodeOptions;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class NoOpFieldDefinitionAdjuster implements FieldDefinitionAdjuster {
    public static final NoOpFieldDefinitionAdjuster INSTANCE = new NoOpFieldDefinitionAdjuster();

    @Override
    public FieldDefinition adjust(FieldDefinition fieldDefinition, DecodeOptions options) {
        return fieldDefinition;
    }
}