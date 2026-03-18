package amex.iso8583.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PosDataCode {

    private final String cardDataInputCapability;
    private final String cardholderAuthenticationCapabilities;
    private final String cardCaptureCapability;
    private final String operationEnvironment;
    private final String cardholderPresent;
    private final String cardPresent;
    private final String cardDataInputMode;
    private final String cardMemberAuthenticationMethod;
    private final String cardMemberAuthenticationEntity;
    private final String cardDataOutputCapability;
    private final String terminalOutputCapability;
    private final String pinCaptureCapability;
}
