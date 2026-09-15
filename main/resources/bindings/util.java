public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static JsonNode jsonNode(Object... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException(
                    "Arguments must be passed as key-value pairs"
            );
        }

        ObjectNode node = MAPPER.createObjectNode();

        for (int i = 0; i < values.length; i += 2) {
            String key = String.valueOf(values[i]);
            Object value = values[i + 1];

            node.replace(key, MAPPER.valueToTree(value));
        }

        return node;
    }
}
