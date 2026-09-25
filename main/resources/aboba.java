default ObjectNode with(Consumer<ObjectNode> modifier) {

    ObjectNode request = asObjectNode();

    modifier.accept(request);

    return request;
}





