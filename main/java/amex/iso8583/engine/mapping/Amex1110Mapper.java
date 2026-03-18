package amex.iso8583.engine.mapping;

import amex.iso8583.engine.core.DecodedMessage;
import amex.iso8583.model.mti.Amex1110Message;

public class Amex1110Mapper implements MessageMapper<Amex1110Message> {

    @Override
    public Amex1110Message map(DecodedMessage decodedMessage) {
        throw new UnsupportedOperationException("Amex1110Mapper is not implemented yet");
    }
}
