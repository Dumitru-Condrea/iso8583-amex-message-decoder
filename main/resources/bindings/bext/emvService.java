public final class EmvService {

    private final RestClient restClient;
    private final EmvAuthService authService;

    public EmvService(
            RestClient restClient,
            EmvAuthService authService) {

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
                ServiceUrls.EMV.url(endpoint),
                request
        );
    }
}
