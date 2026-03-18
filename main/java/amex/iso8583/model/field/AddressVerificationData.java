package amex.iso8583.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddressVerificationData {

    private final String serviceIdentifier;
    private final String requestTypeIdentifier;
    private final String cardmemberBillingPostalCode;
    private final String cardmemberBillingAddress;
}
