public static void deleteTokensByOwnerId(
        @NonNull Connection connection,
        @NonNull String tokenOwnerId
) throws SQLException {

    String query =
            loadQueryTemplate(DELETE_TOKENS_BY_OWNER_ID_SQL);

    try (CallableStatement statement =
                 connection.prepareCall(query)) {

        int parameterIndex = 1;

        statement.setString(parameterIndex++, tokenOwnerId);

        int cursorIndex = parameterIndex;

        statement.registerOutParameter(
                cursorIndex,
                OracleTypes.REF_CURSOR
        );

        connection.setAutoCommit(false);

        try {
            statement.execute();

            var deletedIds =
                    readDeletedTokenIds(statement, cursorIndex);

            connection.commit();

            info(String.format(
                    "Deleted TOKEN_ID values for TOKEN_OWNER_ID '%s': %s",
                    tokenOwnerId,
                    deletedIds
            ));

        } catch (SQLException exception) {
            connection.rollback();
            throw exception;

        } finally {
            connection.setAutoCommit(true);
        }
    }
}