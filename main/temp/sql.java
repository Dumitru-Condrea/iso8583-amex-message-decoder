public static String replaceSqlValues(
        String sql,
        String placeholder,
        Object... values
) {
    if (sql == null || placeholder == null) {
        throw new IllegalArgumentException(
                "SQL and placeholder cannot be null"
        );
    }

    if (!sql.contains(placeholder)) {
        throw new IllegalArgumentException(
                "SQL placeholder not found: " + placeholder
        );
    }

    if (values == null || values.length == 0) {
        throw new IllegalArgumentException(
                "SQL values cannot be empty"
        );
    }

    String replacement = Arrays.stream(values)
            .map(value -> {
                if (value == null) {
                    return "NULL";
                }

                if (value instanceof String) {
                    return "'" +
                            ((String) value).replace("'", "''") +
                            "'";
                }

                if (value instanceof Enum<?>) {
                    return "'" +
                            ((Enum<?>) value).name() +
                            "'";
                }

                if (value instanceof Number) {
                    return value.toString();
                }

                if (value instanceof Boolean) {
                    return (Boolean) value ? "1" : "0";
                }

                throw new IllegalArgumentException(
                        "Unsupported SQL value type: "
                                + value.getClass().getName()
                );
            })
            .collect(Collectors.joining(", "));

    return sql.replace(placeholder, replacement);
}
