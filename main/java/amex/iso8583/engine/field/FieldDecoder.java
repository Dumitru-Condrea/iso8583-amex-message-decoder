package amex.iso8583.engine.field;

import amex.iso8583.api.DecodeOptions;
import amex.iso8583.engine.core.ByteCursor;
import amex.iso8583.engine.core.DecodedField;
import amex.iso8583.schema.definition.FieldDefinition;

public interface FieldDecoder {

    DecodedField decode(ByteCursor cursor, FieldDefinition fieldDefinition, DecodeOptions options);
}
