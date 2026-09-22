public final class LogSanitizer {

    private LogSanitizer() {
    }

    public static String sanitize(String value) {

        if (value == null) {
            return null;
        }

        return value
                .replaceAll(
                        "(\"access_token\"\\s*:\\s*\")[^\"]*\"",
                        "$1*****\""
                )
                .replaceAll(
                        "(\"accessKeySecret\"\\s*:\\s*\")[^\"]*\"",
                        "$1*****\""
                )
                .replaceAll(
                        "(\"accessKeyId\"\\s*:\\s*\")[^\"]*\"",
                        "$1*****\""
                )
                .replaceAll(
                        "(\"password\"\\s*:\\s*\")[^\"]*\"",
                        "$1*****\""
                );
    }
}
