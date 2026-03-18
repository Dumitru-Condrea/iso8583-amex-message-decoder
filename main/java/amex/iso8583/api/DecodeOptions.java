package amex.iso8583.api;

import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class DecodeOptions {

    private final Map<String, Object> flags;

    public DecodeOptions(Map<String, Object> flags) {
        this.flags = Collections.unmodifiableMap(
                new LinkedHashMap<String, Object>(
                        flags == null ? Collections.<String, Object>emptyMap() : flags
                )
        );
    }

    public static DecodeOptions empty() {
        return new DecodeOptions(Collections.<String, Object>emptyMap());
    }

    public boolean hasFlag(String key) {
        return flags.containsKey(key);
    }

    public Object get(String key) {
        return flags.get(key);
    }

    public String getString(String key) {
        Object value = flags.get(key);
        return value == null ? null : String.valueOf(value);
    }

    public boolean getBoolean(String key) {
        Object value = flags.get(key);

        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return false;
    }

    public Integer getInteger(String key) {
        Object value = flags.get(key);

        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            return Integer.valueOf((String) value);
        }
        return null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Map<String, Object> flags = new LinkedHashMap<String, Object>();

        public Builder flag(String key, Object value) {
            flags.put(key, value);
            return this;
        }

        public DecodeOptions build() {
            return new DecodeOptions(flags);
        }
    }
}

