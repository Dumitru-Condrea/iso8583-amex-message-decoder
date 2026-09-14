public final class ApiRequest {

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, Object> queryParams = new HashMap<>();
    private final Map<String, Object> formParams = new HashMap<>();

    private Object body;

    private String username;
    private String password;
    private String bearerToken;

    private ApiRequest() {
    }

    public static ApiRequest request() {
        return new ApiRequest();
    }

    public ApiRequest header(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public ApiRequest queryParam(String name, Object value) {
        queryParams.put(name, value);
        return this;
    }

    public ApiRequest formParam(String name, Object value) {
        formParams.put(name, value);
        return this;
    }

    public ApiRequest body(Object body) {
        this.body = body;
        return this;
    }

    public ApiRequest basicAuth(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public ApiRequest bearerAuth(String token) {
        this.bearerToken = token;
        return this;
    }

    Map<String, String> headers() {
        return headers;
    }

    Map<String, Object> queryParams() {
        return queryParams;
    }

    Map<String, Object> formParams() {
        return formParams;
    }

    Object body() {
        return body;
    }

    String username() {
        return username;
    }

    String password() {
        return password;
    }

    String bearerToken() {
        return bearerToken;
    }
}
