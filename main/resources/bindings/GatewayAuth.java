import java.util.UUID;

public final class GatewayAuthService extends AuthService {

    private static final String IDENTITY_REQUEST =
            "requests/auth/gateway-identity.json";

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

        ObjectNode body = (ObjectNode) JsonUtils.readResource(
                IDENTITY_REQUEST
        );

        body.put(
                "identityName",
                "gateway-" + generateSubUuid() + "-client"
        );

        return body;
    }

    private String generateSubUuid() {
        return UUID.randomUUID()
                .toString()
                .substring(0, 10);
    }
}
