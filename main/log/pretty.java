public static String toPrettyJson(Object value) {

    if (value == null) {
        return null;
    }

    try {
        JsonNode node;

        if (value instanceof JsonNode) {
            node = (JsonNode) value;

        } else if (value instanceof String) {
            String stringValue = (String) value;

            try {
                node = MAPPER.readTree(stringValue);
            } catch (JsonProcessingException e) {
                return stringValue;
            }

        } else {
            node = MAPPER.valueToTree(value);
        }

        return MAPPER
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(node);

    } catch (JsonProcessingException e) {
        return String.valueOf(value);
    }
}
