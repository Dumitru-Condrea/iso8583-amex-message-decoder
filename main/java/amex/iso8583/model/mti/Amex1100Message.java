package amex.iso8583.model.mti;

import amex.iso8583.model.common.AmexMessage;
import amex.iso8583.model.field.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Amex1100Message implements AmexMessage {

    private final String mti;
    private final String pan;
    private final String processingCode;
    private final String amountTransaction;
    private final String stan;
    private final String dateTimeLocalTransaction;
    private final String dateExpiration;
    private final String countryCodeAcquiringInstitution;
    private final PosDataCode posDataCode;
    private final String functionCode;
    private final String messageReasonCode;
    private final String cardAcceptorBusinessCode;
    private final String track2;
    private final String retrievalReferenceNumber;
    private final String termId;
    private final String cardAcceptorIdCode;
    private final String acceptorData;
    private final String currencyCodeTransaction;
    private final String pinBlock;
    private final SecurityRelatedControlInformation securityRelatedControlInformation;
    private final IccRelatedData iccRelatedData;
    private final NationalUseData nationalUseData;
    private final AmexSafeKeyData amexSafeKeyData;
    private final AddressVerificationData addressVerificationData;
    private final PaymentAccountData paymentAccountData;
    private final AcceptanceEnvironmentData acceptanceEnvironmentData;
}