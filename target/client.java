
package your.package.tgs;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public final class TgsConfigurationComparator {

    private TgsConfigurationComparator() {
    }

    public static boolean matches(
            JsonNode mongoConfig,
            JsonNode tgsRequest,
            TgsConfigurationScope scope) {

        Objects.requireNonNull(
                mongoConfig,
                "Mongo configuration must not be null"
        );

        Objects.requireNonNull(
                tgsRequest,
                "TGS request must not be null"
        );

        Objects.requireNonNull(
                scope,
                "Configuration scope must not be null"
        );

        if (scope == TgsConfigurationScope.FULL) {

            return matchesSection(
                    mongoConfig,
                    tgsRequest,
                    TgsConfigurationScope.SETTINGS
            ) && matchesSection(
                    mongoConfig,
                    tgsRequest,
                    TgsConfigurationScope.WALLETS
            );
        }

        return matchesSection(
                mongoConfig,
                tgsRequest,
                scope
        );
    }

    private static boolean matchesSection(
            JsonNode mongoConfig,
            JsonNode tgsRequest,
            TgsConfigurationScope scope) {

        JsonNode actual =
                mongoConfig.path(scope.getMongoField());

        JsonNode expected =
                tgsRequest.path(scope.getRequestField());

        // A missing, invalid or empty expected
        // section must never pass validation.
        if (!expected.isObject()
                || expected.size() == 0) {
            return false;
        }

        return containsExpected(actual, expected);
    }

    private static boolean containsExpected(
            JsonNode actual,
            JsonNode expected) {

        if (actual.isMissingNode()) {
            return false;
        }

        if (expected.isObject()) {

            if (!actual.isObject()) {
                return false;
            }

            Iterator<Map.Entry<String, JsonNode>> fields =
                    expected.fields();

            while (fields.hasNext()) {

                Map.Entry<String, JsonNode> field =
                        fields.next();

                JsonNode actualValue =
                        actual.path(field.getKey());

                if (!containsExpected(
                        actualValue,
                        field.getValue()
                )) {
                    return false;
                }
            }

            return true;
        }

        // Arrays and scalar values are compared exactly.
        return actual.equals(expected);
    }
}




package your.package.tgs;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

// Import your existing classes:
// MongoConfigurationService
// CardBrand

public final class TgsConfigurationValidator {

    private final MongoConfigurationService mongoService;

    public TgsConfigurationValidator(
            MongoConfigurationService mongoService) {

        this.mongoService = Objects.requireNonNull(
                mongoService,
                "MongoConfigurationService must not be null"
        );
    }

    public boolean matches(
            String tokenOwnerId,
            CardBrand brand,
            TgsConfigurationScope scope,
            JsonNode expectedRequest) {

        Objects.requireNonNull(tokenOwnerId, "tokenOwnerId");
        Objects.requireNonNull(brand, "brand");
        Objects.requireNonNull(scope, "scope");
        Objects.requireNonNull(expectedRequest, "expectedRequest");

        JsonNode actualConfig =
                mongoService.getEmvConfiguration(
                        tokenOwnerId,
                        brand
                );

        return TgsConfigurationComparator.matches(
                actualConfig,
                expectedRequest,
                scope
        );
    }

    public void verify(
            String tokenOwnerId,
            CardBrand brand,
            TgsConfigurationScope scope,
            JsonNode expectedRequest) {

        if (!matches(
                tokenOwnerId,
                brand,
                scope,
                expectedRequest
        )) {

            throw new AssertionError(
                    "TGS configuration does not match the expected state."
                            + "\nToken owner: " + tokenOwnerId
                            + "\nBrand: " + brand
                            + "\nScope: " + scope
            );
        }
    }
}
