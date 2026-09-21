package your.package.api.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import your.package.api.auth.client.AuthClient;
import your.package.api.client.RestClient;
import your.package.api.config.ApiConfig;
import your.package.api.config.ServiceUrls;
import your.package.api.json.JsonUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public final class GatewayAuthService extends AuthService {

    public GatewayAuthService(RestClient restClient) {
        super(
                new AuthClient(
                        restClient,
                        ServiceUrls.GATEWAY_AUTH
                )
        );
    }

    @Override
    protected String getMasterUsername() {
        return ApiConfig.getGatewayMasterUsername();
    }

    @Override
    protected String getMasterPassword() {
        return ApiConfig.getGatewayMasterPassword();
    }

    @Override
    protected JsonNode createIdentityBody() {

        String subUuid = UUID.randomUUID()
                .toString()
                .substring(0, 10);

        ObjectNode body =
                JsonUtils.objectNode();

        body.put(
                "identityName",
                "gateway-" + subUuid + "-client"
        );

        body.put(
                "description",
                "Sample Description"
        );

        body.set(
                "scopes",
                JsonUtils.toJsonNode(
                        Arrays.asList(
                                "tokgw.create",
                                "tokgw.read",
                                "tokgw.update",
                                "tokgw.delete"
                        )
                )
        );

        body.set(
                "resources",
                JsonUtils.toJsonNode(
                        Collections.singletonList(
                                "http://test-framework/"
                        )
                )
        );

        body.set(
                "additionalInfo",
                JsonUtils.objectNode()
        );

        return body;
    }
}
