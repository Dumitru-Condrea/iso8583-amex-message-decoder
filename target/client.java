TGS configuration activation detected. Waiting 60 seconds for changes to take effect before validation.

        public static boolean requiresActivationWait(
        @NonNull JsonNode mongoConfig,
        @NonNull JsonNode request,
        @NonNull TgsConfigurationScope scope) {

    JsonNode tokenisationEnabled = request
            .path("emvSettings")
            .path("emvTokenisationEnabled");

    // Explicitly disabling tokenisation overrides all other toggles.
    if (tokenisationEnabled.isBoolean()
            && !tokenisationEnabled.booleanValue()) {
        return false;
    }

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
                    "Unsupported configuration scope: " + scope
            );
    }
}
