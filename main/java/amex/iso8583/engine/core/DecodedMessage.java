package amex.iso8583.engine.core;

import amex.iso8583.engine.header.BitmapSet;
import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class DecodedMessage {

    private final String mti;
    private final BitmapSet bitmapSet;
    private final Map<Integer, DecodedField> fields;
    private final Map<String, String> flatValues;

    public DecodedMessage(
            String mti,
            BitmapSet bitmapSet,
            Map<Integer, DecodedField> fields,
            Map<String, String> flatValues
    ) {
        if (mti == null) {
            throw new IllegalArgumentException("mti must not be null");
        }
        if (bitmapSet == null) {
            throw new IllegalArgumentException("bitmapSet must not be null");
        }
        if (fields == null) {
            throw new IllegalArgumentException("fields must not be null");
        }
        if (flatValues == null) {
            throw new IllegalArgumentException("flatValues must not be null");
        }

        this.mti = mti;
        this.bitmapSet = bitmapSet;
        this.fields = Collections.unmodifiableMap(new LinkedHashMap<>(fields));
        this.flatValues = Collections.unmodifiableMap(new LinkedHashMap<>(flatValues));
    }

    public DecodedField getField(int fieldNumber) {
        return fields.get(fieldNumber);
    }

    public String getFlatValue(String fieldName) {
        return flatValues.get(fieldName);
    }

    public boolean hasField(int fieldNumber) {
        return fields.containsKey(fieldNumber);
    }
}