package amex.iso8583.schema.mti;

import amex.iso8583.engine.header.MessageHeaderProfile;
import amex.iso8583.engine.mapping.Amex1110Mapper;
import amex.iso8583.engine.mapping.MessageMapper;
import amex.iso8583.model.mti.Amex1110Message;
import amex.iso8583.schema.definition.FieldDefinition;
import amex.iso8583.schema.definition.MessageDefinition;

import java.util.Collections;
import java.util.Map;

public class Amex1110Definition implements MessageDefinition<Amex1110Message> {

    private final MessageMapper<Amex1110Message> mapper = new Amex1110Mapper();

    @Override
    public String getMti() {
        return "1110";
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
    public MessageMapper<Amex1110Message> getMapper() {
        return mapper;
    }
}
