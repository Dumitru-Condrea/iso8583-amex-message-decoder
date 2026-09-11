public final class RestClient {

    public ApiResponse get(String url) {
        return execute(
                RestAssured
                        .given()
                        .get(url)
        );
    }

    public ApiResponse post(String url, Object body) {
        return execute(
                RestAssured
                        .given()
                        .contentType(ContentType.JSON)
                        .body(body)
                        .post(url)
        );
    }

    private ApiResponse execute(Response response) {
        return ApiResponseMapper.from(response);
    }
}
