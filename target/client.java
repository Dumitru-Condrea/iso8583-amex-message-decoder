import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class TgsConfigurationManager {

    private static final String WAIT_SECONDS_KEY =
            "tgs.config.wait-seconds";

    private final MongoConfigurationService mongoService;
    private final long waitSeconds;

    public TgsConfigurationManager(
            MongoConfigurationService mongoService) {

        this.mongoService = Objects.requireNonNull(
                mongoService,
                "MongoConfigurationService must not be null"
        );

        this.waitSeconds = Long.parseLong(
                Config.get(WAIT_SECONDS_KEY)
        );

        if (waitSeconds < 0) {
            throw new IllegalArgumentException(
                    "TGS configuration wait cannot be negative"
            );
        }
    }

    public void updateConfiguration(
            String tokenOwnerId,
            CardBrand brand,
            TgsConfigurationScope scope,
            JsonNode request,
            Runnable updateAction) {

        Objects.requireNonNull(tokenOwnerId, "tokenOwnerId");
        Objects.requireNonNull(brand, "brand");
        Objects.requireNonNull(scope, "scope");
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(updateAction, "updateAction");

        // Compare request with current MongoDB configuration.
        boolean alreadyMatches = matchesConfiguration(
                tokenOwnerId,
                brand,
                scope,
                request
        );

        // Always execute TGS POST.
        updateAction.run();

        // Skip waiting if configuration already matched.
        if (alreadyMatches) {
            return;
        }

        // Wait for configuration propagation.
        waitForConfiguration();

        // Compare the same request with MongoDB again.
        if (!matchesConfiguration(
                tokenOwnerId,
                brand,
                scope,
                request
        )) {
            throw new IllegalStateException(
                    "TGS configuration was not applied after "
                            + waitSeconds
                            + " seconds. Token owner: "
                            + tokenOwnerId
                            + ", brand: "
                            + brand
                            + ", scope: "
                            + scope
            );
        }
    }

    private boolean matchesConfiguration(
            String tokenOwnerId,
            CardBrand brand,
            TgsConfigurationScope scope,
            JsonNode request) {

        JsonNode mongoConfig =
                mongoService.getEmvConfiguration(
                        tokenOwnerId,
                        brand
                );

        return TgsConfigurationComparator.matches(
                mongoConfig,
                request,
                scope
        );
    }

    private void waitForConfiguration() {

        if (waitSeconds == 0) {
            return;
        }

        try {
            TimeUnit.SECONDS.sleep(waitSeconds);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Interrupted while waiting for TGS configuration",
                    e
            );
        }
    }
}
