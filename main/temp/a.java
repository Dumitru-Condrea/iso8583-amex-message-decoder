public static String getString(
        ResultSet resultSet,
        String column
) throws SQLException {

    if (!hasColumn(resultSet, column)) {
        return null;
    }

    return resultSet.getString(column);
}

public static boolean hasColumn(
        ResultSet resultSet,
        String column
) throws SQLException {

    ResultSetMetaData metaData = resultSet.getMetaData();

    for (int i = 1; i <= metaData.getColumnCount(); i++) {
        if (column.equalsIgnoreCase(metaData.getColumnLabel(i))) {
            return true;
        }
    }

    return false;
}
