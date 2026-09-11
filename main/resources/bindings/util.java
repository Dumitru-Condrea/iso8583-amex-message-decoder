public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static JsonNode read(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Failed to parse JSON",
                    e
            );
        }
    }

    public static <T> T read(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Failed to deserialize JSON to "
                            + type.getSimpleName(),
                    e
            );
        }
    }
}
