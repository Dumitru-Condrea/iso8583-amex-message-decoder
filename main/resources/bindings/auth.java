public final class AuthClient {

    private static final String TOKEN_ENDPOINT = "/oauth/token";

    private final RestClient restClient;
    private final String baseUrl;

    public AuthClient(RestClient restClient, String baseUrl) {
        this.restClient = restClient;
        this.baseUrl = baseUrl;
    }

    public AuthToken getToken(String username, String password) {

        ApiRequest request = ApiRequest.builder()
                .basicAuth(username, password)
                .multipart("grant_type", "client_credentials")
                .build();

        ApiResponse response = restClient.post(
                baseUrl + TOKEN_ENDPOINT,
                request
        );

        validateTokenResponse(response);

        String accessToken = response
                .json()
                .path("access_token")
                .asText();

        long expiresIn = response
                .json()
                .path("expires_in")
                .asLong();

        return new AuthToken(
                accessToken,
                expiresIn
        );
    }

    private void validateTokenResponse(ApiResponse response) {

        if (response.getStatusCode() != 200) {
            throw new IllegalStateException(
                    "Failed to retrieve auth token. Status code: "
                            + response.getStatusCode()
                            + ", response: "
                            + response.getBody()
            );
        }

        if (!response.json().hasNonNull("access_token")) {
            throw new IllegalStateException(
                    "Authentication response does not contain access_token"
            );
        }
    }
}
