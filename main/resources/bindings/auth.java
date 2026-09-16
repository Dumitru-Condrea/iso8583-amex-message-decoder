package your.package.api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import your.package.api.client.ApiRequest;
import your.package.api.client.ApiResponse;
import your.package.api.client.RestClient;
import your.package.api.config.ServiceUrl;

public final class AuthClient {

    private static final String TOKEN_ENDPOINT =
            "/oauth/token";

    private final RestClient restClient;
    private final ServiceUrl serviceUrl;

    public AuthClient(
            RestClient restClient,
            ServiceUrl serviceUrl) {

        this.restClient = restClient;
        this.serviceUrl = serviceUrl;
    }

    public AuthToken getToken(
            String username,
            String password) {

        ApiRequest request =
                ApiRequest.builder()
                        .basicAuth(
                                username,
                                password
                        )
                        .multipart(
                                "grant_type",
                                "client_credentials"
                        )
                        .build();

        ApiResponse response =
                restClient.post(
                        serviceUrl.url(
                                TOKEN_ENDPOINT
                        ),
                        request
                );

        JsonNode body =
                response.json();

        validateTokenResponse(
                response,
                body
        );

        return new AuthToken(
                body.path(
                        "access_token"
                ).asText(),

                body.path(
                        "expires_in"
                ).asLong()
        );
    }

    private void validateTokenResponse(
            ApiResponse response,
            JsonNode body) {

        if (response.getStatusCode() != 200) {

            throw new IllegalStateException(
                    "Failed to retrieve auth token. "
                            + "Status code: "
                            + response.getStatusCode()
                            + ", response: "
                            + response.getBody()
            );
        }

        if (!body.hasNonNull("access_token")
                || body
                .path("access_token")
                .asText()
                .isEmpty()) {

            throw new IllegalStateException(
                    "Authentication response "
                            + "does not contain access_token"
            );
        }
    }
}
