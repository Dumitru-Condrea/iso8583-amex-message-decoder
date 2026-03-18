package amex.iso8583.schema.mti;

import amex.iso8583.engine.header.MessageHeaderProfile;
import amex.iso8583.engine.mapping.Amex1430Mapper;
import amex.iso8583.engine.mapping.MessageMapper;
import amex.iso8583.model.mti.Amex1430Message;
import amex.iso8583.schema.definition.FieldDefinition;
import amex.iso8583.schema.definition.MessageDefinition;

import java.util.Collections;
import java.util.Map;

public class Amex1430Definition implements MessageDefinition<Amex1430Message> {

    private final MessageMapper<Amex1430Message> mapper = new Amex1430Mapper();

    @Override
    public String getMti() {
        return "1430";
    }

    @Override
    public MessageHeaderProfile getHeaderProfile() {
        return MessageHeaderProfile.amexDefault();
    }

    @Override
    public Map<Integer, FieldDefinition> getFieldDefinitions() {
        return Collections.emptyMap();
    }

    @Override
    public MessageMapper<Amex1430Message> getMapper() {
        return mapper;
    }
}
