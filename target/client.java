public Document findDocument(String tokenOwnerId) {

    Document document = collection
            .find(Filters.eq("tokenOwnerId", tokenOwnerId))
            .first();

    if (document == null) {
        throw new IllegalStateException(
                "MongoDB document not found for tokenOwnerId: "
                        + tokenOwnerId
        );
    }

    return document;
}


public JsonNode findEmvConfig(
        String tokenOwnerId,
        CardBrand brand) {

    Document document = findDocument(tokenOwnerId);

    Document emvConfig =
            document.get("emvConfig", Document.class);

    if (emvConfig == null) {
        throw new IllegalStateException(
                "emvConfig not found for tokenOwnerId: "
                        + tokenOwnerId
        );
    }

    Document brandConfig =
            emvConfig.get(brand.name(), Document.class);

    if (brandConfig == null) {
        throw new IllegalStateException(
                "EMV configuration not found for brand "
                        + brand
                        + " and tokenOwnerId "
                        + tokenOwnerId
        );
    }

    return JsonUtils.read(brandConfig.toJson());
}
