public final class ApiResponse {

    private final int statusCode;
    private final String body;
    private final Map<String, List<String>> headers;

    public ApiResponse(
            int statusCode,
            String body,
            Map<String, List<String>> headers) {

        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public int statusCode() {
        return statusCode;
    }

    public String body() {
        return body;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public String header(String name) {
        List<String> values = headers.get(name);

        return values == null || values.isEmpty()
                ? null
                : values.get(0);
    }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    public JsonNode json() {
        return JsonUtils.read(body);
    }

    public <T> T as(Class<T> type) {
        return JsonUtils.read(body, type);
    }
}
