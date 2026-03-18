package amex.iso8583.schema.definition;

import amex.iso8583.engine.header.MessageHeaderProfile;
import amex.iso8583.engine.mapping.MessageMapper;
import amex.iso8583.model.common.AmexMessage;

import java.util.Map;
import java.util.Optional;

public interface MessageDefinition<T extends AmexMessage> {
    String getMti();
    MessageHeaderProfile getHeaderProfile();
    Map<Integer, FieldDefinition> getFieldDefinitions();
    MessageMapper<T> getMapper();

    default Optional<FieldDefinition> findFieldDefinition(int fieldNumber) {
        return Optional.ofNullable(getFieldDefinitions().get(fieldNumber));
    }
}
