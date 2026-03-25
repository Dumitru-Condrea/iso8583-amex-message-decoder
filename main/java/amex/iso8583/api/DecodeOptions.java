package amex.iso8583.api;

import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public final class DecodeOptions {

    private final Map<String, Object> values;

    private DecodeOptions(Map<String, Object> values) {
        this.values = Collections.unmodifiableMap(values);
    }

    public static DecodeOptions empty() {
        return new DecodeOptions(new LinkedHashMap<>());
    }

    public DecodeOptions with(String key, Object value) {
        LinkedHashMap<String, Object> copy = new LinkedHashMap<>(this.values);
        copy.put(key, value);
        return new DecodeOptions(copy);
    }

    public Object get(String key) {
        return values.get(key);
    }

    public boolean has(String key) {
        return values.containsKey(key);
    }

    public Map<String, Object> asMap() {
        return values;
    }
}

