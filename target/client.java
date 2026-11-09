public static boolean containsEnabledToggle(
        @NonNull JsonNode request,
        @NonNull TgsConfigurationScope scope) {

    if (scope == TgsConfigurationScope.FULL) {
        return containsEnabledToggle(
                request,
                TgsConfigurationScope.SETTINGS
        ) || containsEnabledToggle(
                request,
                TgsConfigurationScope.WALLETS
        );
    }

    return containsTrue(
            request.path(scope.getRequestField())
    );
}

private static boolean containsTrue(JsonNode node) {

    if (node.isBoolean()) {
        return node.booleanValue();
    }

    if (node.isContainerNode()) {
        for (JsonNode child : node) {
            if (containsTrue(child)) {
                return true;
            }
        }
    }

    return false;
}
