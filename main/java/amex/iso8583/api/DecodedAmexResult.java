package amex.iso8583.api;

import amex.iso8583.engine.core.DecodedFieldMeta;
import amex.iso8583.model.common.AmexMessage;
import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class DecodedAmexResult<T extends AmexMessage> {

    private final T message;
    private final Map<String, String> flatValues;
    private final Map<Integer, DecodedFieldMeta> fieldMeta;

    public DecodedAmexResult(
            T message,
            Map<String, String> flatValues,
            Map<Integer, DecodedFieldMeta> fieldMeta
    ) {

        if (message == null) {
            throw new IllegalArgumentException("message must not be null");
        }

        this.message = message;
        this.flatValues = Collections.unmodifiableMap(new LinkedHashMap<>(flatValues));
        this.fieldMeta = Collections.unmodifiableMap(new LinkedHashMap<>(fieldMeta));
    }
}
