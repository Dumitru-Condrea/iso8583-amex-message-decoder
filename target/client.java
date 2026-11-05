
package your.package.tgs;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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

    public TgsUpdateResult updateIfRequired(
            String tokenOwnerId,
            CardBrand brand,
            TgsConfigurationScope scope,
            JsonNode request,
            Supplier<ApiResponse> updateAction) {

        Objects.requireNonNull(tokenOwnerId, "tokenOwnerId");
        Objects.requireNonNull(brand, "brand");
        Objects.requireNonNull(scope, "scope");
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(updateAction, "updateAction");

        // Read current configuration.
        JsonNode current = mongoService.getEmvConfiguration(
                tokenOwnerId,
                brand
        );

        // Skip unnecessary TGS updates.
        if (TgsConfigurationComparator.matches(
                current,
                request,
                scope)) {

            return TgsUpdateResult.SKIPPED;
        }

        // Execute existing Gateway/TGS request.
        ApiResponse response = updateAction.get();

        if (response == null || !response.isSuccessful()) {

            throw new IllegalStateException(
                    "TGS configuration update failed"
                            + (response == null
                            ? ": response is null"
                            : ". HTTP status: "
                            + response.getStatusCode())
            );
        }

        // Wait for configuration propagation.
        waitForConfiguration();

        // Read actual configuration after update.
        JsonNode updated = mongoService.getEmvConfiguration(
                tokenOwnerId,
                brand
        );

        // Validate persisted configuration.
        if (!TgsConfigurationComparator.matches(
                updated,
                request,
                scope)) {

            throw new IllegalStateException(
                    "TGS configuration was not applied "
                            + "after " + waitSeconds + " seconds. "
                            + "Token owner: " + tokenOwnerId
                            + ", brand: " + brand
                            + ", scope: " + scope
            );
        }

        return TgsUpdateResult.UPDATED;
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
                    "Interrupted while waiting for "
                            + "TGS configuration update",
                    e
            );
        }
    }
}
