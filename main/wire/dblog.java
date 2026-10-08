public static boolean isConnectionValid(Connection connection) {
    try {
        return connection != null && connection.isValid(3);
    } catch (SQLException e) {
        return false;
    }
}
