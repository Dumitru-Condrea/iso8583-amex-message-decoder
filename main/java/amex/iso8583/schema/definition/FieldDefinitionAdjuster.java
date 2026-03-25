package amex.iso8583.schema.definition;

import amex.iso8583.api.DecodeOptions;

public interface FieldDefinitionAdjuster {
    FieldDefinition adjust(FieldDefinition fieldDefinition, DecodeOptions options);
}