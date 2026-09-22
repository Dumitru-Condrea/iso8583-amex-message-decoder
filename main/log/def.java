@Slf4j
public final class DefaultApiLogListener
        implements ApiLogListener {

    @Override
    public void onRequest(
            String method,
            String url,
            ApiRequest request) {

        String message = buildRequestLog(
                method,
                url,
                request
        );

        info(message);
    }

    @Override
    public void onResponse(
            String method,
            String url,
            ApiResponse response,
            long durationMs) {

        String message = buildResponseLog(
                method,
                url,
                response,
                durationMs
        );

        info(message);
    }

    @Override
    public void onError(
            String method,
            String url,
            Throwable error,
            long durationMs) {

        String message =
                "========== API ERROR ==========\n"
                        + "Method : " + method + "\n"
                        + "URL    : " + url + "\n"
                        + "Time   : " + durationMs + " ms\n"
                        + "Error  : " + error.getMessage() + "\n"
                        + "==============================";

        System.err.println(message);
        error.printStackTrace(System.err);

        log.error(message, error);
    }

    private String buildRequestLog(
            String method,
            String url,
            ApiRequest request) {

        StringBuilder builder = new StringBuilder();

        builder.append("========== API REQUEST ==========\n");
        builder.append("Method : ").append(method).append('\n');
        builder.append("URL    : ").append(url).append('\n');

        if (!request.getHeaders().isEmpty()) {
            builder.append("Headers: ")
                    .append(request.getHeaders())
                    .append('\n');
        }

        if (!request.getQueryParams().isEmpty()) {
            builder.append("Query  : ")
                    .append(request.getQueryParams())
                    .append('\n');
        }

        if (!request.getFormParams().isEmpty()) {
            builder.append("Form   : ")
                    .append(request.getFormParams())
                    .append('\n');
        }

        if (!request.getMultipartParams().isEmpty()) {
            builder.append("Multipart: ")
                    .append(request.getMultipartParams())
                    .append('\n');
        }

        if (request.getUsername() != null) {
            builder.append("Auth   : Basic *****\n");
        }

        if (request.getBearerToken() != null) {
            builder.append("Auth   : Bearer *****\n");
        }

        if (request.getBody() != null) {
            builder.append("Body   : ")
                    .append(
                            LogSanitizer.sanitize(
                                    String.valueOf(request.getBody())
                            )
                    )
                    .append('\n');
        }

        builder.append("=================================");

        return builder.toString();
    }

    private String buildResponseLog(
            String method,
            String url,
            ApiResponse response,
            long durationMs) {

        StringBuilder builder = new StringBuilder();

        builder.append("========= API RESPONSE ==========\n");
        builder.append("Method : ").append(method).append('\n');
        builder.append("URL    : ").append(url).append('\n');
        builder.append("Status : ")
                .append(response.getStatusCode())
                .append('\n');
        builder.append("Time   : ")
                .append(durationMs)
                .append(" ms\n");

        if (!response.getHeaders().isEmpty()) {
            builder.append("Headers: ")
                    .append(response.getHeaders())
                    .append('\n');
        }

        if (response.getBody() != null
                && !response.getBody().isEmpty()) {

            builder.append("Body   : ")
                    .append(
                            LogSanitizer.sanitize(
                                    response.getBody()
                            )
                    )
                    .append('\n');
        }

        builder.append("=================================");

        return builder.toString();
    }

    private void info(String message) {
        System.out.println(message);
        log.info(message);
    }
}
