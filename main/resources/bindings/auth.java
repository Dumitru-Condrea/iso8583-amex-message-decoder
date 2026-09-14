public final class AuthClient {

    private final RestClient restClient;
    private final String baseUrl;

    public AuthClient(
            RestClient restClient,
            String baseUrl) {

        this.restClient = restClient;
        this.baseUrl = baseUrl;
    }

    public ApiResponse getToken(
            String username,
            String password) {

        ApiRequest request = ApiRequest.request()
                .basicAuth(username, password)
                .multipart(
                        "grant_type",
                        "client_credentials"
                );

        return restClient.post(
                baseUrl + "/oauth/token",
                request
        );
    }
}
