package amex.iso8583.engine.mapping;

import amex.iso8583.engine.core.DecodedMessage;
import amex.iso8583.model.mti.Amex1430Message;

public class Amex1430Mapper implements MessageMapper<Amex1430Message> {

    @Override
    public Amex1430Message map(DecodedMessage decodedMessage) {
        throw new UnsupportedOperationException("Amex1430Mapper is not implemented yet");
    }
}
