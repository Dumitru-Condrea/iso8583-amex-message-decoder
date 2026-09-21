import java.util.UUID;

public final class EmvAuthService extends AuthService {

    private static final String IDENTITY_REQUEST =
            "requests/auth/emv-identity.json";

    public EmvAuthService(RestClient restClient) {
        super(
                new AuthClient(
                        restClient,
                        ServiceUrls.EMV_AUTH
                )
        );
    }

    @Override
    protected String getMasterUsername() {
        return ApiConfig.getEmvMasterUsername();
    }

    @Override
    protected String getMasterPassword() {
        return ApiConfig.getEmvMasterPassword();
    }

    @Override
    protected JsonNode createIdentityBody() {

        ObjectNode body = (ObjectNode) JsonUtils.readResource(
                IDENTITY_REQUEST
        );

        body.put(
                "identityName",
                "id-for-emv-" + generateSubUuid()
        );

        return body;
    }

    private String generateSubUuid() {
        return UUID.randomUUID()
                .toString()
                .substring(0, 10);
    }
}
