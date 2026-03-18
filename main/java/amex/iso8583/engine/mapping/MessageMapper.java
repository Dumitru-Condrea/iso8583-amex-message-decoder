package amex.iso8583.engine.mapping;

import amex.iso8583.engine.core.DecodedMessage;
import amex.iso8583.model.common.AmexMessage;

public interface MessageMapper<T extends AmexMessage> {
    T map(DecodedMessage decodedMessage);
}