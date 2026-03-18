package amex.iso8583.schema.registry;

import amex.iso8583.exception.UnknownMessageDefinitionException;
import amex.iso8583.model.common.AmexMessage;
import amex.iso8583.schema.definition.MessageDefinition;
import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class MessageDefinitionsRegistry {

    private final Map<String, MessageDefinition<? extends AmexMessage>> definitionsByMti;

    public MessageDefinitionsRegistry(Map<String, MessageDefinition<? extends AmexMessage>> definitionsByMti) {
        if (definitionsByMti == null) {
            throw new IllegalArgumentException("definitionsByMti must not be null");
        }
        this.definitionsByMti = Collections.unmodifiableMap(new LinkedHashMap<>(definitionsByMti));
    }

    public MessageDefinition<? extends AmexMessage> getRequired(String mti) {
        MessageDefinition<? extends AmexMessage> definition = definitionsByMti.get(mti);
        if (definition == null) {
            throw new UnknownMessageDefinitionException(
                    "No message definition registered for MTI: " + mti,
                    mti,
                    null
            );
        }
        return definition;
    }

    public static MessageDefinitionsRegistry defaultRegistry() {
        Map<String, MessageDefinition<? extends AmexMessage>> definitions = new LinkedHashMap<>();

        definitions.put("1100", new amex.iso8583.schema.mti.Amex1100Definition());
        definitions.put("1110", new amex.iso8583.schema.mti.Amex1110Definition());
        definitions.put("1420", new amex.iso8583.schema.mti.Amex1420Definition());
        definitions.put("1430", new amex.iso8583.schema.mti.Amex1430Definition());

        return new MessageDefinitionsRegistry(definitions);
    }
}
