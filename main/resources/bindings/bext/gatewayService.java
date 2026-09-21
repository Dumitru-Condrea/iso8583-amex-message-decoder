public final class GatewayService {

    private final RestClient restClient;
    private final GatewayAuthService authService;

    public GatewayService(
            RestClient restClient,
            GatewayAuthService authService) {

        this.restClient = restClient;
        this.authService = authService;
    }

    public ApiResponse post(
            String endpoint,
            Object body) {

        ApiRequest request = ApiRequest.builder()
                .bearerAuth(authService.getToken())
                .body(body)
                .build();

        return restClient.post(
                ServiceUrls.GATEWAY.url(endpoint),
                request
        );
    }
}
