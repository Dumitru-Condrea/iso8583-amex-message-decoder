package your.package.db.logging;

import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public final class DbLogger {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss.SSS"
            );

    public void select(
            String sql,
            ResultSet resultSet) {

        String message =
                "========== DB SELECT ==========\n"
                        + "Query:\n"
                        + sql
                        + "\n\n"
                        + "Rows found : "
                        + getRowCount(resultSet)
                        + "\n"
                        + "==============================";

        info(message);
    }

    public void update(
            String sql,
            int rowsAffected) {

        String message =
                "========== DB UPDATE ==========\n"
                        + "Query:\n"
                        + sql
                        + "\n\n"
                        + "Rows affected : "
                        + rowsAffected
                        + "\n"
                        + "==============================";

        info(message);
    }

    public void error(
            String sql,
            Exception exception) {

        String message =
                "=========== DB ERROR ==========\n"
                        + "Query:\n"
                        + sql
                        + "\n\n"
                        + "Error   : "
                        + exception.getClass().getSimpleName()
                        + "\n"
                        + "Message : "
                        + exception.getMessage()
                        + "\n"
                        + "==============================";

        error(
                message,
                exception
        );
    }

    private int getRowCount(
            ResultSet resultSet) {

        if (resultSet == null) {
            return 0;
        }

        try {

            int currentRow =
                    resultSet.getRow();

            boolean beforeFirst =
                    resultSet.isBeforeFirst();

            boolean afterLast =
                    resultSet.isAfterLast();

            resultSet.last();

            int count =
                    resultSet.getRow();

            if (beforeFirst) {

                resultSet.beforeFirst();

            } else if (afterLast) {

                resultSet.afterLast();

            } else if (currentRow > 0) {

                resultSet.absolute(
                        currentRow
                );
            }

            return count;

        } catch (SQLException e) {

            throw new IllegalStateException(
                    "Failed to count ResultSet rows",
                    e
            );
        }
    }

    private void info(
            String message) {

        System.out.println(
                timestamp()
                        + "\n"
                        + message
        );

        log.info(message);
    }

    private void error(
            String message,
            Exception exception) {

        System.err.println(
                timestamp()
                        + "\n"
                        + message
        );

        log.error(
                message,
                exception
        );
    }

    private String timestamp() {

        return "timestamp: "
                + LocalDateTime.now()
                .format(TIMESTAMP_FORMAT);
    }
}
