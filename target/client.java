
public enum TgsConfigurationScope {

    SETTINGS("settings", "emvSettings"),

    WALLETS("wallets", "digitalWalletsSettings"),

    FULL(null, null);

    private final String mongoField;
    private final String requestField;

    TgsConfigurationScope(
            String mongoField,
            String requestField) {

        this.mongoField = mongoField;
        this.requestField = requestField;
    }

    public String getMongoField() {
        return mongoField;
    }

    public String getRequestField() {
        return requestField;
    }
}








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

        Objects.requireNonNull(mongoConfig, "Mongo config must not be null");
        Objects.requireNonNull(tgsRequest, "TGS request must not be null");
        Objects.requireNonNull(scope, "Configuration scope must not be null");

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

        if (!actual.isObject() || !expected.isObject()
                || expected.size() == 0) {
            return false;
        }

        return containsExpected(actual, expected);
    }

    private static boolean containsExpected(
            JsonNode actual,
            JsonNode expected) {

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

                if (actualValue.isMissingNode()
                        || !containsExpected(
                                actualValue,
                                field.getValue()
                        )) {
                    return false;
                }
            }

            return true;
        }

        if (expected.isArray()) {
            return actual.equals(expected);
        }

        return actual.equals(expected);
    }
}
