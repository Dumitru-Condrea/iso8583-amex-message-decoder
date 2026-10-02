public static <T> List<T> select(
            Connection connection,
            String query,
            ResultSetMapper<T> mapper
    ) throws SQLException {

        try (PreparedStatement statement =
                     connection.prepareStatement(query);
             ResultSet resultSet =
                     statement.executeQuery()) {

            return toList(resultSet, mapper);
        }
    }

    public static int update(
            Connection connection,
            String query
    ) throws SQLException {

        boolean autoCommit = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            int affectedRows;

            try (PreparedStatement statement =
                         connection.prepareStatement(query)) {

                affectedRows = statement.executeUpdate();
            }

            connection.commit();

            return affectedRows;

        } catch (SQLException exception) {
            connection.rollback();
            throw exception;

        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }
