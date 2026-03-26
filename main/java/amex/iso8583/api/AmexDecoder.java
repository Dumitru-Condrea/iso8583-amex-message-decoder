package amex.iso8583.api;

import amex.iso8583.engine.core.ByteCursor;
import amex.iso8583.engine.core.DecodedField;
import amex.iso8583.engine.core.DecodedFieldMeta;
import amex.iso8583.engine.core.DecodedMessage;
import amex.iso8583.engine.core.DecodedSubfield;
import amex.iso8583.engine.field.CompositeFieldDecoder;
import amex.iso8583.engine.field.PrimitiveFieldDecoder;
import amex.iso8583.engine.header.BitmapSet;
import amex.iso8583.engine.header.HeaderDecoder;
import amex.iso8583.engine.header.MessageHeaderProfile;
import amex.iso8583.engine.mapping.MessageMapper;
import amex.iso8583.engine.value.LengthReader;
import amex.iso8583.engine.value.ValueDecoder;
import amex.iso8583.exception.DecodeException;
import amex.iso8583.exception.UnknownFieldDefinitionException;
import amex.iso8583.model.common.AmexMessage;
import amex.iso8583.schema.definition.CompositeFieldDefinition;
import amex.iso8583.schema.definition.FieldDefinition;
import amex.iso8583.schema.definition.MessageDefinition;
import amex.iso8583.schema.registry.MessageDefinitionsRegistry;

import java.util.LinkedHashMap;
import java.util.Map;

public class AmexDecoder {

    private final MessageDefinitionsRegistry registry;
    private final ValueDecoder valueDecoder;
    private final LengthReader lengthReader;
    private final HeaderDecoder headerDecoder;
    private final PrimitiveFieldDecoder primitiveFieldDecoder;
    private final CompositeFieldDecoder compositeFieldDecoder;

    public AmexDecoder(MessageDefinitionsRegistry registry) {
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null");
        }

        this.registry = registry;
        this.valueDecoder = new ValueDecoder();
        this.lengthReader = new LengthReader(valueDecoder);
        this.headerDecoder = new HeaderDecoder(valueDecoder);
        this.primitiveFieldDecoder = new PrimitiveFieldDecoder(valueDecoder, lengthReader);
        this.compositeFieldDecoder = new CompositeFieldDecoder(valueDecoder, lengthReader);
    }

    public AmexDecoder() {
        this(MessageDefinitionsRegistry.defaultRegistry());
    }

    public DecodedAmexResult<? extends AmexMessage> decode(byte[] rawMessage) {
        return decode(rawMessage, DecodeOptions.empty());
    }

    public DecodedAmexResult<? extends AmexMessage> decode(byte[] rawMessage, DecodeOptions options) {
        if (rawMessage == null) {
            throw new IllegalArgumentException("rawMessage must not be null");
        }
        if (options == null) {
            options = DecodeOptions.empty();
        }

        ByteCursor cursor = new ByteCursor(rawMessage);

        String mti = headerDecoder.readMti(
                cursor,
                MessageHeaderProfile.amexDefault()
        );

        MessageDefinition<? extends AmexMessage> definition = registry.getRequired(mti);

        return decodeInternal(rawMessage, definition, options);
    }

    public <T extends AmexMessage> DecodedAmexResult<T> decode(
            byte[] rawMessage,
            MessageDefinition<T> definition,
            DecodeOptions options
    ) {
        if (rawMessage == null) {
            throw new IllegalArgumentException("rawMessage must not be null");
        }
        if (definition == null) {
            throw new IllegalArgumentException("definition must not be null");
        }
        if (options == null) {
            options = DecodeOptions.empty();
        }

        return decodeInternal(rawMessage, definition, options);
    }

    private <T extends AmexMessage> DecodedAmexResult<T> decodeInternal(
            byte[] rawMessage,
            MessageDefinition<T> definition,
            DecodeOptions options
    ) {
        ByteCursor cursor = new ByteCursor(rawMessage);

        String mti = headerDecoder.readMti(cursor, definition.getHeaderProfile());
        BitmapSet bitmapSet = headerDecoder.readBitmaps(cursor, definition.getHeaderProfile());

        Map<Integer, DecodedField> decodedFields = new LinkedHashMap<>();
        Map<String, String> flatValues = new LinkedHashMap<>();
        Map<Integer, DecodedFieldMeta> fieldMeta = new LinkedHashMap<>();
        Map<Integer, FieldDefinition> effectiveFieldDefinitions = new LinkedHashMap<>();

        flatValues.put("MESSAGE_TYPE_IDENTIFIER", mti);
        flatValues.put("PRIMARY_BITMAP", bitmapSet.getPrimaryHex());

        if (bitmapSet.hasSecondaryBitmap()) {
            flatValues.put("SECONDARY_BITMAP", bitmapSet.getSecondaryHex());
        }

        for (Integer fieldNumber : bitmapSet.getActiveFields()) {
            FieldDefinition baseDefinition = definition.findFieldDefinition(fieldNumber).orElse(null);

            if (baseDefinition == null) {
                throw new UnknownFieldDefinitionException(
                        "No field definition found for field " + fieldNumber + " in MTI " + mti,
                        mti,
                        fieldNumber,
                        cursor.position(),
                        null
                );
            }

            FieldDefinition effectiveDefinition = applyAdjuster(baseDefinition, options);
            effectiveFieldDefinitions.put(fieldNumber, effectiveDefinition);

            DecodedField decodedField = decodeField(cursor, effectiveDefinition, options);

            decodedFields.put(fieldNumber, decodedField);
            if (decodedField.getMeta() != null) {
                fieldMeta.put(fieldNumber, decodedField.getMeta());
            }

            addToFlatValues(decodedField, flatValues);
        }

        DecodedMessage decodedMessage = new DecodedMessage(
                mti,
                bitmapSet,
                decodedFields,
                flatValues
        );

        T typedMessage = mapTypedMessage(definition.getMapper(), decodedMessage);

        return new DecodedAmexResult<>(typedMessage, flatValues, fieldMeta, effectiveFieldDefinitions);
    }

    private FieldDefinition applyAdjuster(FieldDefinition baseDefinition, DecodeOptions options) {
        if (baseDefinition == null) {
            return null;
        }

        if (baseDefinition.getAdjuster() == null) {
            return baseDefinition;
        }

        FieldDefinition adjusted = baseDefinition.getAdjuster().adjust(baseDefinition, options);
        return adjusted == null ? baseDefinition : adjusted;
    }

    private DecodedField decodeField(ByteCursor cursor, FieldDefinition fieldDefinition, DecodeOptions options) {
        if (fieldDefinition instanceof CompositeFieldDefinition) {
            return compositeFieldDecoder.decode(cursor, fieldDefinition, options);
        }
        return primitiveFieldDecoder.decode(cursor, fieldDefinition, options);
    }

    private void addToFlatValues(DecodedField decodedField, Map<String, String> flatValues) {
        flatValues.put(decodedField.getFieldName(), decodedField.getDisplayValue());

        if (decodedField.hasSubfields()) {
            for (DecodedSubfield subfield : decodedField.getSubfields()) {
                flatValues.put(subfield.getSubfieldName(), subfield.getDisplayValue());
            }
        }
    }

    private <T extends AmexMessage> T mapTypedMessage(MessageMapper<T> mapper, DecodedMessage decodedMessage) {
        try {
            return mapper.map(decodedMessage);
        } catch (Exception e) {
            throw new DecodeException(
                    "Failed to map decoded message to typed message for MTI " + decodedMessage.getMti(),
                    decodedMessage.getMti(),
                    null,
                    null,
                    null,
                    e
            );
        }
    }
}
