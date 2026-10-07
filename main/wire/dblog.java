package your.package.db.logging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DbLogger {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss.SSS"
            );

    public static void select(
            String sql,
            int rowsFound) {

        String message =
                "========== DB SELECT ==========\n"
                        + "Query:\n"
                        + sql
                        + "\n\n"
                        + "Rows found    : "
                        + rowsFound
                        + "\n"
                        + "==============================";

        info(message);
    }

    public static void update(
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

    public static void error(
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

    private static void info(
            String message) {

        System.out.println(
                timestamp()
                        + "\n"
                        + message
        );

        log.info(message);
    }

    private static void error(
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

    private static String timestamp() {

        return "timestamp: "
                + LocalDateTime.now()
                .format(TIMESTAMP_FORMAT);
    }
}
