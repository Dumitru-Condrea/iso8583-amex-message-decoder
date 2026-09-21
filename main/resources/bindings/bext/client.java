package your.package.api;

import your.package.api.auth.service.EmvAuthService;
import your.package.api.auth.service.GatewayAuthService;
import your.package.api.client.RestClient;
import your.package.api.service.EmvService;
import your.package.api.service.GatewayService;

public final class ApiClients {

    private final RestClient restClient;

    private final EmvAuthService emvAuthService;
    private final GatewayAuthService gatewayAuthService;

    private final EmvService emvService;
    private final GatewayService gatewayService;

    private ApiClients() {

        this.restClient = new RestClient();

        this.emvAuthService =
                new EmvAuthService(restClient);

        this.gatewayAuthService =
                new GatewayAuthService(restClient);

        this.emvService =
                new EmvService(
                        restClient,
                        emvAuthService
                );

        this.gatewayService =
                new GatewayService(
                        restClient,
                        gatewayAuthService
                );
    }

    public static EmvService emv() {
        return Holder.INSTANCE.emvService;
    }

    public static GatewayService gateway() {
        return Holder.INSTANCE.gatewayService;
    }

    private static class Holder {
        private static final ApiClients INSTANCE =
                new ApiClients();
    }
}
