package your.package.api.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import your.package.api.auth.client.AuthClient;
import your.package.api.auth.model.AuthToken;
import your.package.api.auth.model.Credentials;

public abstract class AuthService {

    private final AuthClient authClient;

    private AuthToken cachedToken;

    protected AuthService(AuthClient authClient) {
        this.authClient = authClient;
    }

    public String getToken() {

        if (cachedToken == null || !cachedToken.isValid()) {
            cachedToken = authenticate();
        }

        return cachedToken.getValue();
    }

    private AuthToken authenticate() {

        // STEP 1
        AuthToken masterToken = authClient.getToken(
                getMasterUsername(),
                getMasterPassword()
        );

        // STEP 2
        Credentials identity = authClient.createIdentity(
                masterToken.getValue(),
                createIdentityBody()
        );

        // STEP 3
        return authClient.getToken(
                identity.getUsername(),
                identity.getPassword()
        );
    }

    protected abstract String getMasterUsername();

    protected abstract String getMasterPassword();

    protected abstract JsonNode createIdentityBody();
}
