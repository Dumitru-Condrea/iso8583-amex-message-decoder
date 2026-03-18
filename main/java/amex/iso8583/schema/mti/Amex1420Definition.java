package amex.iso8583.schema.mti;

import amex.iso8583.engine.header.MessageHeaderProfile;
import amex.iso8583.engine.mapping.Amex1420Mapper;
import amex.iso8583.engine.mapping.MessageMapper;
import amex.iso8583.model.mti.Amex1420Message;
import amex.iso8583.schema.definition.FieldDefinition;
import amex.iso8583.schema.definition.MessageDefinition;

import java.util.Collections;
import java.util.Map;

public class Amex1420Definition implements MessageDefinition<Amex1420Message> {

    private final MessageMapper<Amex1420Message> mapper = new Amex1420Mapper();

    @Override
    public String getMti() {
        return "1420";
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
    public MessageMapper<Amex1420Message> getMapper() {
        return mapper;
    }
}
