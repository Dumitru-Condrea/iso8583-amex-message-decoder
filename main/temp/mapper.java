@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JdbcUtils {

    public static <T> List<T> toList(
            ResultSet resultSet,
            ResultSetMapper<T> mapper
    ) throws SQLException {

        List<T> result = new ArrayList<>();

        while (resultSet.next()) {
            result.add(mapper.map(resultSet));
        }

        return result;
    }
}
