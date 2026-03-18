package amex.iso8583.model.field;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IccRelatedData {

    private final String iccHeaderVersionName;
    private final String iccHeaderVersionNumber;
    private final String applicationCryptogram;
    private final String issuerApplicationData;
    private final String unpredictableNumber;
    private final String applicationTransactionCount;
    private final String terminalVerificationResults;
    private final String transactionDate;
    private final String transactionType;
    private final String amountAuthorized;
    private final String transactionCurrencyCode;
    private final String terminalCountryCode;
    private final String applicationInterchangeProfile;
    private final String otherAmount;
    private final String applicationPanSequenceNumber;
    private final String cryptogramInformationData;
}
