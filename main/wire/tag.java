package your.package.utils;

import io.cucumber.java.Scenario;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScenarioTagUtils {

    private static final Pattern WIREMOCK_ACTIVE_TAG =
            Pattern.compile(
                    "^@wiremock\\.active:(true|false)$",
                    Pattern.CASE_INSENSITIVE
            );

    public static Optional<Boolean> getWiremockActive(
            Scenario scenario) {

        return scenario
                .getSourceTagNames()
                .stream()
                .map(
                        WIREMOCK_ACTIVE_TAG::matcher
                )
                .filter(
                        Matcher::matches
                )
                .map(matcher ->
                        Boolean.parseBoolean(
                                matcher.group(1)
                        )
                )
                .findFirst();
    }
}
