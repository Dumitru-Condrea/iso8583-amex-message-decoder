package amex.iso8583.engine.mapping;

import amex.iso8583.engine.core.DecodedMessage;
import amex.iso8583.model.mti.Amex1420Message;

public class Amex1420Mapper implements MessageMapper<Amex1420Message> {

    @Override
    public Amex1420Message map(DecodedMessage decodedMessage) {
        throw new UnsupportedOperationException("Amex1420Mapper is not implemented yet");
    }
}