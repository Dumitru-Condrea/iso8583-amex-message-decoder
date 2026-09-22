private ApiResponse execute(
        Method method,
        String url,
        ApiRequest request) {

    logListener.onRequest(
            method.name(),
            url,
            request
    );

    RequestSpecification specification =
            RestAssured.given();

    apply(specification, request);

    long startedAt = System.currentTimeMillis();

    try {
        Response response =
                specification.request(method, url);

        long durationMs =
                System.currentTimeMillis() - startedAt;

        ApiResponse apiResponse =
                ApiResponseMapper.from(response);

        logListener.onResponse(
                method.name(),
                url,
                apiResponse,
                durationMs
        );

        return apiResponse;

    } catch (RuntimeException e) {

        long durationMs =
                System.currentTimeMillis() - startedAt;

        logListener.onError(
                method.name(),
                url,
                e,
                durationMs
        );

        throw e;
    }
}
