package your.package.api.auth;

import your.package.api.client.ApiRequest;
import your.package.api.client.ApiResponse;
import your.package.api.client.RestClient;

public final class AuthClient {

    private static final String TOKEN_ENDPOINT = "/oauth/token";

    private final RestClient restClient;
    private final String baseUrl;

    public AuthClient(RestClient restClient, String baseUrl) {
        this.restClient = restClient;
        this.baseUrl = baseUrl;
    }

    public ApiResponse getToken(String username, String password) {

        ApiRequest request = ApiRequest.request()
                .basicAuth(username, password)
                .multipart("grant_type", "client_credentials");

        return restClient.post(
                baseUrl + TOKEN_ENDPOINT,
                request
        );
    }
}
