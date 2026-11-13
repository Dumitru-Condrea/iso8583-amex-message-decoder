private record SummaryRow(
        String leftLabel,
        Object leftValue,
        String rightLabel,
        Object rightValue
) {}

private String requestSummaryWithColumns(CommonRequest request) {
    var rows = List.of(
            new SummaryRow("GatewayMerchantId", request.getGatewayMerchantId(),
                    "Cryptogram", request.getCryptogram()),
            new SummaryRow("Method", request.getAuthMethod(),
                    "Eci", request.getEci()),
            new SummaryRow("PAN", request.getPan(), "", ""),
            new SummaryRow("Year", request.getExpiryYear(), "", ""),
            new SummaryRow("Month", request.getExpiryMonth(), "", "")
    );

    int labelWidth = rows.stream()
            .mapToInt(row -> row.leftLabel().length())
            .max()
            .orElse(0);

    int valueWidth = rows.stream()
            .mapToInt(row -> value(row.leftValue()).length())
            .max()
            .orElse(0);

    int rightLabelWidth = rows.stream()
            .mapToInt(row -> row.rightLabel().length())
            .max()
            .orElse(0);

    var result = new StringBuilder("Request:\n");

    for (var row : rows) {
        result.append(String.format(
                "  %-" + labelWidth + "s : %-" + valueWidth + "s | %-" +
                        rightLabelWidth + "s : %s%n",
                row.leftLabel(),
                value(row.leftValue()),
                row.rightLabel(),
                value(row.rightValue())
        ));
    }

    return result.toString();
}

private String value(Object value) {
    return value == null ? "" : value.toString();
}
