package amex.iso8583.util;

import amex.iso8583.model.field.PrimaryAccountNumber;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserUtils {

    public static LocalDateTime parseDateTime(@NonNull String value) {
        if (!value.matches("\\d{12}")) {
            throw new IllegalArgumentException(
                    "Invalid datetime format: expected 12 digits in format ddMMyyHHmmss, got: " + value
            );
        }

        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("ddMMyyHHmmss"));
    }

    public static PrimaryAccountNumber parseMaskedPanValue(@NonNull String value) {
        Matcher matcher = Pattern.compile("^(\\d+)\\*+(\\d+)$").matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid masked value format: expected digits-asterisks-digits, got: " + value
            );
        }

        return new PrimaryAccountNumber(
                value,
                matcher.group(1),
                matcher.group(2)
        );
    }
}