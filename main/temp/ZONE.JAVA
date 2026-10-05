public static ZoneId getDatabaseZone(
        Connection connection
) throws SQLException {

    try (PreparedStatement statement =
                 connection.prepareStatement(DB_TIMEZONE);
         ResultSet resultSet =
                 statement.executeQuery()) {

        if (!resultSet.next()) {
            throw new IllegalStateException(
                    "Unable to retrieve database timezone"
            );
        }

        String dbTimezone = resultSet.getString("DBTIMEZONE");

        return toZoneId(dbTimezone);
    }
}

private static ZoneId toZoneId(String timezone) {
    if (timezone == null || timezone.trim().isEmpty()) {
        throw new IllegalStateException(
                "Database timezone is empty"
        );
    }

    return ZoneId.of(timezone.trim());
}
