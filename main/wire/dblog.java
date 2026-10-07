package your.package.db.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DbLogger {

    private static final Logger LOG =
            LoggerFactory.getLogger(DbLogger.class);

    public void query(
            String sql,
            long durationMs,
            int rows,
            Object... params) {

        StringBuilder builder =
                new StringBuilder();

        builder.append("========== DB QUERY ==========\n")
                .append("SQL:\n")
                .append(sql)
                .append('\n');

        appendParameters(
                builder,
                params
        );

        builder.append("\nTime : ")
                .append(durationMs)
                .append(" ms\n")
                .append("Rows : ")
                .append(rows)
                .append('\n')
                .append("==============================");

        write(
                builder.toString()
        );
    }

    public void update(
            String sql,
            long durationMs,
            int affectedRows,
            Object... params) {

        StringBuilder builder =
                new StringBuilder();

        builder.append("========== DB UPDATE =========\n")
                .append("SQL:\n")
                .append(sql)
                .append('\n');

        appendParameters(
                builder,
                params
        );

        builder.append("\nTime          : ")
                .append(durationMs)
                .append(" ms\n")
                .append("Affected rows : ")
                .append(affectedRows)
                .append('\n')
                .append("==============================");

        write(
                builder.toString()
        );
    }

    public void error(
            String sql,
            long durationMs,
            Exception exception,
            Object... params) {

        StringBuilder builder =
                new StringBuilder();

        builder.append("========== DB ERROR ==========\n")
                .append("SQL:\n")
                .append(sql)
                .append('\n');

        appendParameters(
                builder,
                params
        );

        builder.append("\nTime    : ")
                .append(durationMs)
                .append(" ms\n")
                .append("Error   : ")
                .append(
                        exception
                                .getClass()
                                .getSimpleName()
                )
                .append('\n')
                .append("Message : ")
                .append(
                        exception.getMessage()
                )
                .append('\n')
                .append("==============================");

        write(
                builder.toString()
        );
    }

    private void appendParameters(
            StringBuilder builder,
            Object... params) {

        if (params == null
                || params.length == 0) {
            return;
        }

        builder.append("\nParameters:\n");

        for (int i = 0; i < params.length; i++) {

            builder.append("  ")
                    .append(i + 1)
                    .append(" : ")
                    .append(
                            formatValue(
                                    params[i]
                            )
                    )
                    .append('\n');
        }
    }

    private String formatValue(
            Object value) {

        if (value == null) {
            return "null";
        }

        return String.valueOf(value);
    }

    private void write(
            String message) {

        String timestamp =
                new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss.SSS"
                )
                        .format(
                                new Date()
                        );

        System.out.println(
                "timestamp: "
                        + timestamp
                        + "\n\n"
                        + message
        );

        LOG.info(
                message
        );
    }
}
