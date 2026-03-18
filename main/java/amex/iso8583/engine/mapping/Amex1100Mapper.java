package amex.iso8583.engine.mapping;

import amex.iso8583.engine.core.DecodedMessage;
import amex.iso8583.model.field.*;
import amex.iso8583.model.mti.Amex1100Message;

public class Amex1100Mapper implements MessageMapper<Amex1100Message> {

    @Override
    public Amex1100Message map(DecodedMessage decodedMessage) {
        if (decodedMessage == null) {
            throw new IllegalArgumentException("decodedMessage must not be null");
        }

        return new Amex1100Message(
                decodedMessage.getMti(),
                decodedMessage.getFlatValue("PAN"),
                decodedMessage.getFlatValue("PROCESSING_CODE"),
                decodedMessage.getFlatValue("AMOUNT_TRANSACTION"),
                decodedMessage.getFlatValue("STAN"),
                decodedMessage.getFlatValue("DATE_TIME_LOCAL_TRANSACTION"),
                decodedMessage.getFlatValue("DATE_EXPIRATION"),
                decodedMessage.getFlatValue("COUNTRY_CODE_ACQUIRING_INSTITUTION"),
                mapPosDataCode(decodedMessage),
                decodedMessage.getFlatValue("FUNCTION_CODE"),
                decodedMessage.getFlatValue("MESSAGE_REASON_CODE"),
                decodedMessage.getFlatValue("CARD_ACCEPTOR_BUSINESS_CODE"),
                decodedMessage.getFlatValue("TRACK2"),
                decodedMessage.getFlatValue("RETRIEVAL_REFERENCE_NUMBER"),
                decodedMessage.getFlatValue("TERMID"),
                decodedMessage.getFlatValue("CARD_ACCEPTOR_ID_CODE"),
                decodedMessage.getFlatValue("ACCEPTORDATA"),
                decodedMessage.getFlatValue("CURRENCY_CODE_TRANSACTION"),
                decodedMessage.getFlatValue("PINBLOCK"),
                mapSecurityRelatedControlInformation(decodedMessage),
                mapIccRelatedData(decodedMessage),
                mapNationalUseData(decodedMessage),
                mapSafeKeyData(decodedMessage),
                mapAddressVerificationData(decodedMessage),
                mapPaymentAccountData(decodedMessage),
                mapAcceptanceEnvironmentData(decodedMessage)
        );
    }

    private PosDataCode mapPosDataCode(DecodedMessage decodedMessage) {
        if (decodedMessage.getFlatValue("POS_DATA_CODE") == null) {
            return null;
        }

        return new PosDataCode(
                decodedMessage.getFlatValue("POS_DATA_CODE.CARD_DATA_INPUT_CAPABILITY"),
                decodedMessage.getFlatValue("POS_DATA_CODE.CARDHOLDER_AUTHENTICATION_CAPABILITIES"),
                decodedMessage.getFlatValue("POS_DATA_CODE.CARD_CAPTURE_CAPABILITY"),
                decodedMessage.getFlatValue("POS_DATA_CODE.OPERATION_ENVIRONMENT"),
                decodedMessage.getFlatValue("POS_DATA_CODE.CARDHOLDER_PRESENT"),
                decodedMessage.getFlatValue("POS_DATA_CODE.CARD_PRESENT"),
                decodedMessage.getFlatValue("POS_DATA_CODE.CARD_DATA_INPUT_MODE"),
                decodedMessage.getFlatValue("POS_DATA_CODE.CARD_MEMBER_AUTHENTICATION_METHOD"),
                decodedMessage.getFlatValue("POS_DATA_CODE.CARD_MEMBER_AUTHENTICATION_ENTITY"),
                decodedMessage.getFlatValue("POS_DATA_CODE.CARD_DATA_OUTPUT_CAPABILITY"),
                decodedMessage.getFlatValue("POS_DATA_CODE.TERMINAL_OUTPUT_CAPABILITY"),
                decodedMessage.getFlatValue("POS_DATA_CODE.PIN_CAPTURE_CAPABILITY")
        );
    }

    private SecurityRelatedControlInformation mapSecurityRelatedControlInformation(DecodedMessage decodedMessage) {
        if (decodedMessage.getFlatValue("SECURITY_RELATED_CONTROL_INFORMATION") == null) {
            return null;
        }

        return new SecurityRelatedControlInformation(
                decodedMessage.getFlatValue("SECURITY_RELATED_CONTROL_INFORMATION.CID_CODE")
        );
    }

    private IccRelatedData mapIccRelatedData(DecodedMessage decodedMessage) {
        if (decodedMessage.getFlatValue("ICC_RELATED_DATA") == null) {
            return null;
        }

        return new IccRelatedData(
                decodedMessage.getFlatValue("ICC_RELATED_DATA.ICC_HEADER_VERSION_NAME"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.ICC_HEADER_VERSION_NUMBER"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.APPLICATION_CRYPTOGRAM"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.ISSUER_APPLICATION_DATA"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.UNPREDICTABLE_NUMBER"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.APPLICATION_TRANSACTION_COUNT"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.TERMINAL_VERIFICATION_RESULTS"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.TRANSACTION_DATE"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.TRANSACTION_TYPE"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.AMOUNT_AUTHORIZED"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.TRANSACTION_CURRENCY_CODE"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.TERMINAL_COUNTRY_CODE"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.APPLICATION_INTERCHANGE_PROFILE"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.OTHER_AMOUNT"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.APPLICATION_PAN_SEQUENCE_NUMBER"),
                decodedMessage.getFlatValue("ICC_RELATED_DATA.CRYPTOGRAM_INFORMATION_DATA")
        );
    }

    private NationalUseData mapNationalUseData(DecodedMessage decodedMessage) {
        if (decodedMessage.getFlatValue("NATIONAL_USE_DATA") == null) {
            return null;
        }

        return new NationalUseData(
                decodedMessage.getFlatValue("NATIONAL_USE_DATA.PRIMARY_ID"),
                decodedMessage.getFlatValue("NATIONAL_USE_DATA.SECONDARY_ID"),
                decodedMessage.getFlatValue("NATIONAL_USE_DATA.BITMAP_IDENTIFIER"),
                decodedMessage.getFlatValue("NATIONAL_USE_DATA.SELLER_ID"),
                decodedMessage.getFlatValue("NATIONAL_USE_DATA.OTID")
        );
    }

    private AmexSafeKeyData mapSafeKeyData(DecodedMessage decodedMessage) {
        if (decodedMessage.getFlatValue("AMEX_SAFE_KEY_DATA") == null) {
            return null;
        }

        return new AmexSafeKeyData(
                decodedMessage.getFlatValue("AMEX_SAFE_KEY_DATA.PRIMARY_ID"),
                decodedMessage.getFlatValue("AMEX_SAFE_KEY_DATA.SECONDARY_ID"),
                decodedMessage.getFlatValue("AMEX_SAFE_KEY_DATA.ECI"),
                decodedMessage.getFlatValue("AMEX_SAFE_KEY_DATA.AEVV_ID"),
                decodedMessage.getFlatValue("AMEX_SAFE_KEY_DATA.AEVV"),
                decodedMessage.getFlatValue("AMEX_SAFE_KEY_DATA.XID"),
                decodedMessage.getFlatValue("AMEX_SAFE_KEY_DATA.TRANSACTION_ID_VALUE")
        );
    }

    private AddressVerificationData mapAddressVerificationData(DecodedMessage decodedMessage) {
        if (decodedMessage.getFlatValue("ADDRESS_VERIFICATION") == null) {
            return null;
        }

        return new AddressVerificationData(
                decodedMessage.getFlatValue("ADDRESS_VERIFICATION.SERVICE_IDENTIFIER"),
                decodedMessage.getFlatValue("ADDRESS_VERIFICATION.REQUEST_TYPE_IDENTIFIER"),
                decodedMessage.getFlatValue("ADDRESS_VERIFICATION.CARDMEMBER_BILLING_POSTAL_CODE"),
                decodedMessage.getFlatValue("ADDRESS_VERIFICATION.CARDMEMBER_BILLING_ADDRESS")
        );
    }

    private PaymentAccountData mapPaymentAccountData(DecodedMessage decodedMessage) {
        if (decodedMessage.getFlatValue("PAYMENT_ACCOUNT_DATA") == null) {
            return null;
        }

        return new PaymentAccountData(
                decodedMessage.getFlatValue("PAYMENT_ACCOUNT_DATA.PRIMARY_ID"),
                decodedMessage.getFlatValue("PAYMENT_ACCOUNT_DATA.SECONDARY_ID"),
                decodedMessage.getFlatValue("PAYMENT_ACCOUNT_DATA.BITMAP_IDENTIFIER"),
                decodedMessage.getFlatValue("PAYMENT_ACCOUNT_DATA.PAYMENT_ACCOUNT_REFERENCE")
        );
    }

    private AcceptanceEnvironmentData mapAcceptanceEnvironmentData(DecodedMessage decodedMessage) {
        if (decodedMessage.getFlatValue("PAYMENT_ACCOUNT_DATA") == null) {
            return null;
        }

        return new AcceptanceEnvironmentData(
                decodedMessage.getFlatValue("ACCEPTANCE_ENVIRONMENT_DATA.PRIMARY_ID"),
                decodedMessage.getFlatValue("ACCEPTANCE_ENVIRONMENT_DATA.SECONDARY_ID"),
                decodedMessage.getFlatValue("ACCEPTANCE_ENVIRONMENT_DATA.INITIATING_PARTY_INDICATOR"),
                decodedMessage.getFlatValue("ACCEPTANCE_ENVIRONMENT_DATA.BITMAP_IDENTIFIER"),
                decodedMessage.getFlatValue("ACCEPTANCE_ENVIRONMENT_DATA.AUTHENTICATION_OUTAGE_INDICATOR")
        );
    }
}
