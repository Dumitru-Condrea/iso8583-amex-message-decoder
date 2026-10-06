package your.package.api.wiremock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import your.package.api.auth.service.GatewayAuthService;
import your.package.api.client.ApiRequest;
import your.package.api.client.ApiResponse;
import your.package.api.client.RestClient;
import your.package.api.json.JsonUtils;

public final class WireMockClient {

    private static final String PROPERTY_NAME =
            "wiremock.active";

    private static final String STATE_PATH =
            "/app/mocks/wiremockActive";

    private static final int OK =
            200;

    private final RestClient restClient;

    private final GatewayAuthService gatewayAuthService;

    public WireMockClient(
            RestClient restClient,
            GatewayAuthService gatewayAuthService) {

        this.restClient =
                restClient;

        this.gatewayAuthService =
                gatewayAuthService;
    }

    /**
     * Refreshes environment first and then
     * retrieves the actual WireMock state.
     *
     * REFRESH -> GET
     */
    public boolean getState() {

        refresh();

        ApiResponse response =
                getStateRequest();

        return extractState(response);
    }

    /**
     * Updates WireMock state and refreshes
     * the environment afterwards.
     *
     * SET -> REFRESH
     */
    public void updateState(
            boolean active) {

        setStateRequest(active);

        refresh();
    }

    private ApiResponse getStateRequest() {

        ApiRequest request =
                baseRequest()
                        .build();

        ApiResponse response =
                restClient.get(
                        WireMockActuatorEndpoint.GET_STATE,
                        request
                );

        validateOk(
                response,
                "Failed to retrieve WireMock state"
        );

        return response;
    }

    private void setStateRequest(
            boolean active) {

        ObjectNode body =
                JsonUtils.objectNode();

        body.put(
                "name",
                PROPERTY_NAME
        );

        body.put(
                "value",
                active
        );

        ApiRequest request =
                baseRequest()
                        .body(body)
                        .build();

        ApiResponse response =
                restClient.post(
                        WireMockActuatorEndpoint.SET_STATE,
                        request
                );

        validateOk(
                response,
                "Failed to update WireMock state"
        );
    }

    private void refresh() {

        ApiRequest request =
                baseRequest()
                        .build();

        ApiResponse response =
                restClient.post(
                        WireMockActuatorEndpoint.REFRESH,
                        request
                );

        validateOk(
                response,
                "Failed to refresh Actuator environment"
        );
    }

    private ApiRequest.Builder baseRequest() {

        return ApiRequest.builder()
                .bearerAuth(
                        gatewayAuthService.getToken()
                )
                .skipContextTracking();
    }

    private boolean extractState(
            ApiResponse response) {

        JsonNode stateNode =
                response
                        .json()
                        .at(STATE_PATH);

        if (stateNode.isMissingNode()
                || stateNode.isNull()) {

            throw new IllegalStateException(
                    "WireMock state is missing "
                            + "in Actuator response. "
                            + "Expected path: "
                            + STATE_PATH
            );
        }

        String state =
                stateNode.asText();

        if (!isBoolean(state)) {

            throw new IllegalStateException(
                    "Unexpected WireMock state: "
                            + state
            );
        }

        return Boolean.parseBoolean(state);
    }

    private boolean isBoolean(
            String value) {

        return "true".equalsIgnoreCase(value)
                || "false".equalsIgnoreCase(value);
    }

    private void validateOk(
            ApiResponse response,
            String message) {

        if (response.getStatusCode() != OK) {

            throw new IllegalStateException(
                    message
                            + ". Expected status: "
                            + OK
                            + ", actual status: "
                            + response.getStatusCode()
                            + ", response: "
                            + response.getBody()
            );
        }
    }
}
