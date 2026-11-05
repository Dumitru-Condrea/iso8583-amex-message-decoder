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
