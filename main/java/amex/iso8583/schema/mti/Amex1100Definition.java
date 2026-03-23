package amex.iso8583.schema.mti;

import amex.iso8583.engine.header.MessageHeaderProfile;
import amex.iso8583.engine.mapping.Amex1100Mapper;
import amex.iso8583.engine.mapping.MessageMapper;
import amex.iso8583.model.mti.Amex1100Message;
import amex.iso8583.schema.common.DataEncoding;
import amex.iso8583.schema.common.FieldPresence;
import amex.iso8583.schema.common.FieldValueFormat;
import amex.iso8583.schema.common.LengthEncoding;
import amex.iso8583.schema.common.LengthType;
import amex.iso8583.schema.common.LengthUnit;
import amex.iso8583.schema.definition.CompositeFieldDefinition;
import amex.iso8583.schema.definition.FieldDefinition;
import amex.iso8583.schema.definition.MessageDefinition;
import amex.iso8583.schema.definition.SubfieldDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Amex1100Definition implements MessageDefinition<Amex1100Message> {

    private final MessageMapper<Amex1100Message> mapper = new Amex1100Mapper();
    private final Map<Integer, FieldDefinition> fieldDefinitions;

    public Amex1100Definition() {
        this.fieldDefinitions = Collections.unmodifiableMap(buildFields());
    }

    @Override
    public String getMti() {
        return "1100";
    }

    @Override
    public MessageHeaderProfile getHeaderProfile() {
        return MessageHeaderProfile.amexDefault();
    }

    @Override
    public Map<Integer, FieldDefinition> getFieldDefinitions() {
        return fieldDefinitions;
    }

    @Override
    public MessageMapper<Amex1100Message> getMapper() {
        return mapper;
    }

    private Map<Integer, FieldDefinition> buildFields() {
        Map<Integer, FieldDefinition> map = new LinkedHashMap<>();

        map.put(2, FieldDefinition.builder()
                .number(2)
                .name("PAN")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.MASKED_PAN)
                .lengthType(LengthType.LLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.CHARS)
                .maxLength(21)
                .build());

        map.put(3, FieldDefinition.builder()
                .number(3)
                .name("PROCESSING_CODE")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(6)
                .build());

        map.put(4, FieldDefinition.builder()
                .number(4)
                .name("AMOUNT_TRANSACTION")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(12)
                .build());

        map.put(11, FieldDefinition.builder()
                .number(11)
                .name("STAN")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(6)
                .build());

        map.put(12, FieldDefinition.builder()
                .number(12)
                .name("DATE_TIME_LOCAL_TRANSACTION")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(12)
                .build());

        map.put(14, FieldDefinition.builder()
                .number(14)
                .name("DATE_EXPIRATION")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.ASCII)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(4)
                .build());

        map.put(19, FieldDefinition.builder()
                .number(19)
                .name("COUNTRY_CODE_ACQUIRING_INSTITUTION")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(3)
                .build());

        map.put(22, CompositeFieldDefinition.builder()
                .number(22)
                .name("POS_DATA_CODE")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(12)
                .subfields(posDataCodeSubfields())
                .build());

        map.put(24, FieldDefinition.builder()
                .number(24)
                .name("FUNCTION_CODE")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(3)
                .build());

        map.put(25, FieldDefinition.builder()
                .number(25)
                .name("MESSAGE_REASON_CODE")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(4)
                .build());

        map.put(26, FieldDefinition.builder()
                .number(26)
                .name("CARD_ACCEPTOR_BUSINESS_CODE")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(4)
                .build());

        map.put(35, FieldDefinition.builder()
                .number(35)
                .name("TRACK2")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.ASCII)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.LLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.CHARS)
                .maxLength(37)
                .build());

        map.put(37, FieldDefinition.builder()
                .number(37)
                .name("RETRIEVAL_REFERENCE_NUMBER")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(12)
                .build());

        map.put(41, FieldDefinition.builder()
                .number(41)
                .name("TERMID")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(8)
                .build());

        map.put(42, FieldDefinition.builder()
                .number(42)
                .name("CARD_ACCEPTOR_ID_CODE")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(15)
                .build());

        map.put(43, FieldDefinition.builder()
                .number(43)
                .name("ACCEPTORDATA")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.LLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.CHARS)
                .maxLength(101)
                .build());

        map.put(49, FieldDefinition.builder()
                .number(49)
                .name("CURRENCY_CODE_TRANSACTION")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(3)
                .build());

        map.put(52, FieldDefinition.builder()
                .number(52)
                .name("PINBLOCK")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(8)
                .build());

        map.put(53, CompositeFieldDefinition.builder()
                .number(53)
                .name("SECURITY_RELATED_CONTROL_INFORMATION")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.LLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.CHARS)
                .maxLength(17)
                .subfields(securityRelatedControlInformationSubfields())
                .build());

        map.put(55, CompositeFieldDefinition.builder()
                .number(55)
                .name("ICC_RELATED_DATA")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.LLLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.BYTES)
                .maxLength(256)
                .subfields(iccRelatedDataSubfields())
                .build());

        map.put(60, CompositeFieldDefinition.builder()
                .number(60)
                .name("NATIONAL_USE_DATA")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.LLLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.CHARS)
                .maxLength(141)
                .subfields(nationalUseDataSubFields())
                .build());

        map.put(61, CompositeFieldDefinition.builder()
                .number(61)
                .name("AMEX_SAFE_KEY_DATA")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.LLLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.CHARS)
                .maxLength(54)
                .subfields(amexSafeKeyDataSubFields())
                .build());

        map.put(63, CompositeFieldDefinition.builder()
                .number(63)
                .name("ADDRESS_VERIFICATION")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.LLLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.CHARS)
                .maxLength(205)
                .subfields(addressVerificationSubFields())
                .build());


        map.put(112, CompositeFieldDefinition.builder()
                .number(112)
                .name("PAYMENT_ACCOUNT_DATA")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.LLLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.CHARS)
                .maxLength(79)
                .subfields(paymentAccountDataSubFields())
                .build());

        map.put(113, CompositeFieldDefinition.builder()
                .number(113)
                .name("ACCEPTANCE_ENVIRONMENT_DATA")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.LLLVAR)
                .lengthEncoding(LengthEncoding.EBCDIC)
                .lengthUnit(LengthUnit.CHARS)
                .maxLength(15)
                .subfields(acceptanceEnvironmentDataSubFields())
                .build());

        return map;
    }

    private List<SubfieldDefinition> posDataCodeSubfields() {
        List<SubfieldDefinition> list = new ArrayList<>();

        BiFunction<Integer, String, SubfieldDefinition> subfieldWithName = (id, name) ->
                SubfieldDefinition.builder()
                        .subfieldNumber(id)
                        .name(name)
                        .presence(FieldPresence.OPTIONAL)
                        .dataEncoding(DataEncoding.EBCDIC)
                        .valueFormat(FieldValueFormat.TEXT)
                        .lengthType(LengthType.FIXED)
                        .lengthEncoding(LengthEncoding.NONE)
                        .lengthUnit(LengthUnit.CHARS)
                        .fixedLength(1)
                        .build();

        list.add(subfieldWithName.apply(1, "POS_DATA_CODE.CARD_DATA_INPUT_CAPABILITY"));
        list.add(subfieldWithName.apply(2, "POS_DATA_CODE.CARDHOLDER_AUTHENTICATION_CAPABILITIES"));
        list.add(subfieldWithName.apply(3, "POS_DATA_CODE.CARD_CAPTURE_CAPABILITY"));
        list.add(subfieldWithName.apply(4, "POS_DATA_CODE.OPERATION_ENVIRONMENT"));
        list.add(subfieldWithName.apply(5, "POS_DATA_CODE.CARDHOLDER_PRESENT"));
        list.add(subfieldWithName.apply(6, "POS_DATA_CODE.CARD_PRESENT"));
        list.add(subfieldWithName.apply(7, "POS_DATA_CODE.CARD_DATA_INPUT_MODE"));
        list.add(subfieldWithName.apply(8, "POS_DATA_CODE.CARD_MEMBER_AUTHENTICATION_METHOD"));
        list.add(subfieldWithName.apply(9, "POS_DATA_CODE.CARD_MEMBER_AUTHENTICATION_ENTITY"));
        list.add(subfieldWithName.apply(10, "POS_DATA_CODE.CARD_DATA_OUTPUT_CAPABILITY"));
        list.add(subfieldWithName.apply(11, "POS_DATA_CODE.TERMINAL_OUTPUT_CAPABILITY"));
        list.add(subfieldWithName.apply(12, "POS_DATA_CODE.PIN_CAPTURE_CAPABILITY"));

        return list;
    }

    private List<SubfieldDefinition> securityRelatedControlInformationSubfields() {
        List<SubfieldDefinition> list = new ArrayList<>();

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(0)
                .name("SECURITY_RELATED_CONTROL_INFORMATION.CID_CODE")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthEncoding(LengthEncoding.NONE)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(4)
                .build()
        );

        return list;
    }

    private List<SubfieldDefinition> iccRelatedDataSubfields() {
        List<SubfieldDefinition> list = new ArrayList<>();

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-1)
                .name("ICC_RELATED_DATA.ICC_HEADER_VERSION_NAME")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(4)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(0)
                .name("ICC_RELATED_DATA.ICC_HEADER_VERSION_NUMBER")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BCD)
                .valueFormat(FieldValueFormat.NUMERIC_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.DIGITS)
                .fixedLength(4)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(1)
                .name("ICC_RELATED_DATA.APPLICATION_CRYPTOGRAM")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(8)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(2)
                .name("ICC_RELATED_DATA.ISSUER_APPLICATION_DATA")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.LVAR)
                .lengthEncoding(LengthEncoding.BCD)
                .lengthUnit(LengthUnit.BYTES)
                .maxLength(33)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(3)
                .name("ICC_RELATED_DATA.UNPREDICTABLE_NUMBER")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(4)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(4)
                .name("ICC_RELATED_DATA.APPLICATION_TRANSACTION_COUNT")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(5)
                .name("ICC_RELATED_DATA.TERMINAL_VERIFICATION_RESULTS")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(5)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(6)
                .name("ICC_RELATED_DATA.TRANSACTION_DATE")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BCD)
                .valueFormat(FieldValueFormat.NUMERIC_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.DIGITS)
                .fixedLength(6)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(7)
                .name("ICC_RELATED_DATA.TRANSACTION_TYPE")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(1)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(8)
                .name("ICC_RELATED_DATA.AMOUNT_AUTHORIZED")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BCD)
                .valueFormat(FieldValueFormat.NUMERIC_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.DIGITS)
                .fixedLength(12)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(9)
                .name("ICC_RELATED_DATA.TRANSACTION_CURRENCY_CODE")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(10)
                .name("ICC_RELATED_DATA.TERMINAL_COUNTRY_CODE")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthEncoding(LengthEncoding.NONE)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(11)
                .name("ICC_RELATED_DATA.APPLICATION_INTERCHANGE_PROFILE")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(12)
                .name("ICC_RELATED_DATA.OTHER_AMOUNT")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BCD)
                .valueFormat(FieldValueFormat.NUMERIC_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.DIGITS)
                .fixedLength(12)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(13)
                .name("ICC_RELATED_DATA.APPLICATION_PAN_SEQUENCE_NUMBER")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(1)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(14)
                .name("ICC_RELATED_DATA.CRYPTOGRAM_INFORMATION_DATA")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.BYTES)
                .fixedLength(1)
                .build()
        );

        return list;
    }

    private List<SubfieldDefinition> nationalUseDataSubFields() {
        List<SubfieldDefinition> list = new ArrayList<>();

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-2)
                .name("NATIONAL_USE_DATA.PRIMARY_ID")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-1)
                .name("NATIONAL_USE_DATA.SECONDARY_ID")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(3)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(0)
                .name("NATIONAL_USE_DATA.BITMAP_IDENTIFIER")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(4)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(1)
                .name("NATIONAL_USE_DATA.SELLER_ID")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(20)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(2)
                .name("NATIONAL_USE_DATA.OTID")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(15)
                .build()
        );

        return list;
    }

    private List<SubfieldDefinition> amexSafeKeyDataSubFields() {
        List<SubfieldDefinition> list = new ArrayList<>();

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-2)
                .name("AMEX_SAFE_KEY_DATA.PRIMARY_ID")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-1)
                .name("AMEX_SAFE_KEY_DATA.SECONDARY_ID")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(3)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(0)
                .name("AMEX_SAFE_KEY_DATA.ECI")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(1)
                .name("AMEX_SAFE_KEY_DATA.AEVV_ID")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(4)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(2)
                .name("AMEX_SAFE_KEY_DATA.AEVV")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(20)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(3)
                .name("AMEX_SAFE_KEY_DATA.XID")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.ASCII)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(3)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(4)
                .name("AMEX_SAFE_KEY_DATA.TRANSACTION_ID_VALUE")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.HEX_STRING)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(20)
                .build()
        );

        return list;
    }

    private List<SubfieldDefinition> addressVerificationSubFields() {
        List<SubfieldDefinition> list = new ArrayList<>();

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(1)
                .name("ADDRESS_VERIFICATION.SERVICE_IDENTIFIER")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(2)
                .name("ADDRESS_VERIFICATION.REQUEST_TYPE_IDENTIFIER")
                .presence(FieldPresence.MANDATORY)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(3)
                .name("ADDRESS_VERIFICATION.CARDMEMBER_BILLING_POSTAL_CODE")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(9)
                .build()
        );


        list.add(SubfieldDefinition.builder()
                .subfieldNumber(4)
                .name("ADDRESS_VERIFICATION.CARDMEMBER_BILLING_ADDRESS")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(20)
                .build()
        );

        return list;
    }

    private List<SubfieldDefinition> paymentAccountDataSubFields() {
        List<SubfieldDefinition> list = new ArrayList<>();

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-2)
                .name("PAYMENT_ACCOUNT_DATA.PRIMARY_ID")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-1)
                .name("PAYMENT_ACCOUNT_DATA.SECONDARY_ID")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(3)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(0)
                .name("PAYMENT_ACCOUNT_DATA.BITMAP_IDENTIFIER")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(4)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(1)
                .name("PAYMENT_ACCOUNT_DATA.PAYMENT_ACCOUNT_REFERENCE")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(35)
                .build()
        );

        return list;
    }

    private List<SubfieldDefinition> acceptanceEnvironmentDataSubFields() {
        List<SubfieldDefinition> list = new ArrayList<>();

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-3)
                .name("ACCEPTANCE_ENVIRONMENT_DATA.PRIMARY_ID")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(2)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-2)
                .name("ACCEPTANCE_ENVIRONMENT_DATA.SECONDARY_ID")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(3)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(-1)
                .name("ACCEPTANCE_ENVIRONMENT_DATA.INITIATING_PARTY_INDICATOR")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(1)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(0)
                .name("ACCEPTANCE_ENVIRONMENT_DATA.BITMAP_IDENTIFIER")
                .presence(FieldPresence.OPTIONAL)
                .dataEncoding(DataEncoding.BINARY)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(4)
                .build()
        );

        list.add(SubfieldDefinition.builder()
                .subfieldNumber(1)
                .name("ACCEPTANCE_ENVIRONMENT_DATA.AUTHENTICATION_OUTAGE_INDICATOR")
                .presence(FieldPresence.CONDITIONAL)
                .dataEncoding(DataEncoding.EBCDIC)
                .valueFormat(FieldValueFormat.TEXT)
                .lengthType(LengthType.FIXED)
                .lengthUnit(LengthUnit.CHARS)
                .fixedLength(1)
                .build()
        );

        return list;
    }
}
