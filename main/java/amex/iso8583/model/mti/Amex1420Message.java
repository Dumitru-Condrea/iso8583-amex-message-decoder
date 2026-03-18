package amex.iso8583.model.mti;

import amex.iso8583.model.common.AmexMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Amex1420Message implements AmexMessage {
    private final String mti;
}
