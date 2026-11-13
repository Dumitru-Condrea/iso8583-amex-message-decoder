private void printRequestSummary(@NotNull CommonRequest request) {
    if ("PAN_ONLY".equals(request.getAuthMethod())) {
        System.out.println(requestSummaryWithColumns(request));
        return;
    }

    // Оставь здесь существующий вывод для остальных методов
    System.out.println(requestSummary(request));
}

private String requestSummaryWithColumns(CommonRequest request) {
    var crypto = request.getCryptogram();
    var eci = request.getEci();

    return """
            Request:
            %s
            %s
            %s
            %s
            %s
            """.formatted(
            row("GatewayMerchantId", request.getGatewayMerchantId(), "Cryptogram", crypto),
            row("Method", request.getAuthMethod(), "Eci", eci),
            row("PAN", request.getPan(), "", ""),
            row("Year", request.getExpiryYear(), "", ""),
            row("Month", request.getExpiryMonth(), "", "")
    );
}

private String row(
        String leftLabel,
        Object leftValue,
        String rightLabel,
        Object rightValue
) {
    return String.format(
            "  %-18s : %-16s | %-10s : %s",
            leftLabel,
            leftValue == null ? "" : leftValue,
            rightLabel,
            rightValue == null ? "" : rightValue
    );
}
