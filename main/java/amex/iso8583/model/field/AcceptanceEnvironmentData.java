package amex.iso8583.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcceptanceEnvironmentData {

    private final String primaryId;
    private final String secondaryId;
    private final String initiatingPartyIndicator;
    private final String bitmapIdentifier;
    private final String authenticationOutageIndicator;
}