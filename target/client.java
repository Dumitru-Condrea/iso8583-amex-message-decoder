public static boolean requiresActivationWait(
        @NonNull JsonNode mongoConfig,
        @NonNull JsonNode request,
        @NonNull TgsConfigurationScope scope) {

    switch (scope) {

        case SETTINGS:
            return requiresSettingsWait(mongoConfig, request);

        case WALLETS:
            return requiresWalletsWait(mongoConfig, request);

        case FULL:
            return requiresSettingsWait(mongoConfig, request)
                    || requiresWalletsWait(mongoConfig, request);

        default:
            throw new IllegalArgumentException(
                    "Unsupported TGS configuration scope: " + scope
            );
    }
}




private static boolean requiresSettingsWait(
        JsonNode mongoConfig,
        JsonNode request) {

    JsonNode actual = mongoConfig.path("settings");
    JsonNode expected = request.path("emvSettings");

    // Main switch explicitly disabled:
    // no activation wait, regardless of dependent toggles.
    if (expected.has("emvTokenisationEnabled")
            && !expected.path("emvTokenisationEnabled").asBoolean()) {
        return false;
    }

    return isEnabling(
            actual,
            expected,
            "emvTokenisationEnabled"
    ) || isEnabling(
            actual,
            expected,
            "exposeTokenToMerchant"
    );
}



private static boolean requiresWalletsWait(
        JsonNode mongoConfig,
        JsonNode request) {

    JsonNode actual = mongoConfig.path("wallets");
    JsonNode expected = request.path("digitalWalletsSettings");

    return isEnabling(
            actual,
            expected,
            "googlePayEnabled"
    ) || isEnabling(
            actual,
            expected,
            "applePayEnabled"
    );
}


private static boolean isEnabling(
        JsonNode actual,
        JsonNode expected,
        String field) {

    JsonNode requested = expected.path(field);

    if (!requested.isBoolean() || !requested.booleanValue()) {
        return false;
    }

    JsonNode current = actual.path(field);

    // Only an explicit false -> true transition
    // requires activation waiting.
    return current.isBoolean() && !current.booleanValue();
}
