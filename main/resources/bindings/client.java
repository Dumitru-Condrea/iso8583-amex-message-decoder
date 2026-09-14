public final class RestClient {

    public ApiResponse get(String url, ApiRequest request) {
        return execute(Method.GET, url, request);
    }

    public ApiResponse post(String url, ApiRequest request) {
        return execute(Method.POST, url, request);
    }

    public ApiResponse put(String url, ApiRequest request) {
        return execute(Method.PUT, url, request);
    }

    public ApiResponse delete(String url, ApiRequest request) {
        return execute(Method.DELETE, url, request);
    }

    private ApiResponse execute(
            Method method,
            String url,
            ApiRequest request) {

        RequestSpecification specification =
                RestAssured.given();

        apply(specification, request);

        Response response =
                specification.request(method, url);

        return ApiResponseMapper.from(response);
    }

   private void apply(
        RequestSpecification specification,
        ApiRequest request) {

    if (!request.headers().isEmpty()) {
        specification.headers(request.headers());
    }

    if (!request.queryParams().isEmpty()) {
        specification.queryParams(request.queryParams());
    }

    if (!request.formParams().isEmpty()) {
        specification.formParams(request.formParams());
    }

    if (!request.multipartParams().isEmpty()) {
        request.multipartParams()
                .forEach(specification::multiPart);
    }

    if (request.body() != null) {
        specification
                .contentType(ContentType.JSON)
                .body(request.body());
    }

    if (request.username() != null) {
        specification
                .auth()
                .preemptive()
                .basic(
                        request.username(),
                        request.password()
                );
    }

    if (request.bearerToken() != null) {
        specification
                .auth()
                .oauth2(request.bearerToken());
    }
}
