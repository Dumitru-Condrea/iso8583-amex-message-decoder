public interface ApiLogListener {

    void onRequest(
            String method,
            String url,
            ApiRequest request
    );

    void onResponse(
            String method,
            String url,
            ApiResponse response,
            long durationMs
    );

    void onError(
            String method,
            String url,
            Throwable error,
            long durationMs
    );
}
