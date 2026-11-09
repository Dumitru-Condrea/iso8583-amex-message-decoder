private static @NonNull ApiResponse updateAndVerify(
        @NonNull String brand,
        @NonNull String tokenOwnerId,
        @NonNull ObjectNode request,
        @NonNull TgsConfigurationScope scope) {

    // Read MongoDB and compare current configuration.
    boolean configurationMatches =
            TgsConfigurationValidator.matches(
                    tokenOwnerId,
                    brand,
                    scope,
                    request
            );

    // Determine whether waiting is necessary.
    boolean requiresWait =
            !configurationMatches
                    && TgsConfigurationComparator.containsEnabledToggle(
                            request,
                            scope
                    );

    // Existing cache operations.
    assertThat(GATEWAY_ACTUATOR().deleteCaches()
            .isSuccessful()).isTrue();

    assertThat(GATEWAY_ACTUATOR().getCaches()
            .isSuccessful()).isTrue();

    // Always execute TGS request.
    ApiResponse response = GATEWAY()
            .changeTokenOwnerState(tokenOwnerId, request);

    assertThat(response.isSuccessful())
            .isTrue();

    // Wait only when configuration differs and true is requested.
    if (requiresWait) {
        waitForConfiguration();
    }

    // Always verify actual MongoDB configuration.
    TgsConfigurationValidator.verify(
            tokenOwnerId,
            brand,
            scope,
            request
    );

    return response;
}
