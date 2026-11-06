
package your.package.tgs;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TgsConfigurationComparator {

    public static List<String> compare(
            @NonNull JsonNode mongoConfig,
            @NonNull JsonNode tgsRequest,
            @NonNull TgsConfigurationScope scope) {

        List<String> differences = new ArrayList<>();

        if (scope == TgsConfigurationScope.FULL) {

            compareSection(
                    mongoConfig,
                    tgsRequest,
                    TgsConfigurationScope.SETTINGS,
                    differences
            );

            compareSection(
                    mongoConfig,
                    tgsRequest,
                    TgsConfigurationScope.WALLETS,
                    differences
            );

        } else {

            compareSection(
                    mongoConfig,
                    tgsRequest,
                    scope,
                    differences
            );
        }

        return differences;
    }

    public static boolean matches(
            @NonNull JsonNode mongoConfig,
            @NonNull JsonNode tgsRequest,
            @NonNull TgsConfigurationScope scope) {

        return compare(
                mongoConfig,
                tgsRequest,
                scope
        ).isEmpty();
    }

    private static void compareSection(
            JsonNode mongoConfig,
            JsonNode tgsRequest,
            TgsConfigurationScope scope,
            List<String> differences) {

        JsonNode actual =
                mongoConfig.path(scope.getMongoField());

        JsonNode expected =
                tgsRequest.path(scope.getRequestField());

        String path = scope.getMongoField();

        if (!expected.isObject() || expected.size() == 0) {

            differences.add(
                    path + "\n"
                            + "   Expected : non-empty JSON object"
                            + "\n"
                            + "   Actual   : missing or empty request section"
            );

            return;
        }

        compareNodes(
                actual,
                expected,
                path,
                differences
        );
    }

    private static void compareNodes(
            JsonNode actual,
            JsonNode expected,
            String path,
            List<String> differences) {

        if (expected.isObject()) {

            if (!actual.isObject()) {

                addDifference(
                        differences,
                        path,
                        expected,
                        actual
                );

                return;
            }

            Iterator<Map.Entry<String, JsonNode>> fields =
                    expected.fields();

            while (fields.hasNext()) {

                Map.Entry<String, JsonNode> field =
                        fields.next();

                compareNodes(
                        actual.path(field.getKey()),
                        field.getValue(),
                        path + "." + field.getKey(),
                        differences
                );
            }

            return;
        }

        if (actual.isMissingNode()
                || !actual.equals(expected)) {

            addDifference(
                    differences,
                    path,
                    expected,
                    actual
            );
        }
    }

    private static void addDifference(
            List<String> differences,
            String path,
            JsonNode expected,
            JsonNode actual) {

        differences.add(
                path
                        + "\n   Expected : "
                        + format(expected)
                        + "\n   Actual   : "
                        + format(actual)
        );
    }

    private static String format(JsonNode node) {

        if (node == null || node.isMissingNode()) {
            return "<missing>";
        }

        return node.toString();
    }
}





package your.package.tgs;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class TgsConfigurationValidator {

    @NonNull
    private final MongoConfigurationService mongoService;

    public boolean matches(
            @NonNull String tokenOwnerId,
            @NonNull CardBrand brand,
            @NonNull TgsConfigurationScope scope,
            @NonNull JsonNode expectedRequest) {

        return compare(
                tokenOwnerId,
                brand,
                scope,
                expectedRequest
        ).isEmpty();
    }

    public List<String> compare(
            @NonNull String tokenOwnerId,
            @NonNull CardBrand brand,
            @NonNull TgsConfigurationScope scope,
            @NonNull JsonNode expectedRequest) {

        JsonNode actualConfig =
                mongoService.getEmvConfiguration(
                        tokenOwnerId,
                        brand
                );

        return TgsConfigurationComparator.compare(
                actualConfig,
                expectedRequest,
                scope
        );
    }

    public void verify(
            @NonNull String tokenOwnerId,
            @NonNull CardBrand brand,
            @NonNull TgsConfigurationScope scope,
            @NonNull JsonNode expectedRequest) {

        List<String> differences = compare(
                tokenOwnerId,
                brand,
                scope,
                expectedRequest
        );

        if (differences.isEmpty()) {
            return;
        }

        String details = differences.stream()
                .map(diff -> " - " + diff)
                .collect(Collectors.joining("\n\n"));

        throw new AssertionError(
                "TGS configuration validation failed"
                        + "\nToken owner : " + tokenOwnerId
                        + "\nBrand       : " + brand
                        + "\nScope       : " + scope
                        + "\n\nMismatched fields ("
                        + differences.size() + "):"
                        + "\n\n" + details
        );
    }
}
