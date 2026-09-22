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










private static final Set<String> SENSITIVE_FIELDS =
        new HashSet<>(Arrays.asList(
                "access_token",
                "accessKeySecret",
                "accessKeyId",
                "password"
        ));














private String sanitizeJson(Object value) {

    if (value == null) {
        return null;
    }

    try {
        JsonNode node;

        if (value instanceof JsonNode) {
            node = ((JsonNode) value).deepCopy();

        } else if (value instanceof String) {
            node = JsonUtils.read((String) value);

        } else {
            node = JsonUtils.toJsonNode(value);
        }

        maskSensitiveFields(node);

        return JsonUtils.toPrettyJson(node);

    } catch (RuntimeException e) {
        return String.valueOf(value);
    }
}














private void maskSensitiveFields(JsonNode node) {

    if (node.isObject()) {

        ObjectNode objectNode = (ObjectNode) node;

        Iterator<Map.Entry<String, JsonNode>> fields =
                objectNode.fields();

        while (fields.hasNext()) {

            Map.Entry<String, JsonNode> field =
                    fields.next();

            if (SENSITIVE_FIELDS.contains(field.getKey())) {

                objectNode.put(
                        field.getKey(),
                        "*****"
                );

            } else {
                maskSensitiveFields(field.getValue());
            }
        }

    } else if (node.isArray()) {

        for (JsonNode child : node) {
            maskSensitiveFields(child);
        }
    }
}








private static final DateTimeFormatter TIMESTAMP_FORMAT =
        DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss.SSS"
        );








private void printToConsole(String message) {

    System.out.println(
            "Timestamp: "
                    + LocalDateTime.now()
                    .format(TIMESTAMP_FORMAT)
    );

    System.out.println(message);
}

