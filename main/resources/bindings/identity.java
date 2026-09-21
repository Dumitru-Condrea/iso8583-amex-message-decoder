private void validateIdentityResponse(
            ApiResponse response,
            JsonNode body) {

        if (response.getStatusCode() != 201) {
            throw new IllegalStateException(
                    "Failed to create identity. Status: "
                            + response.getStatusCode()
                            + ", response: "
                            + response.getBody()
            );
        }

        if (!body.hasNonNull("accessKeyId")
                || body.path("accessKeyId").asText().isEmpty()
                || !body.hasNonNull("accessKeySecret")
                || body.path("accessKeySecret").asText().isEmpty()) {

            throw new IllegalStateException(
                    "Identity response does not contain valid credentials"
            );
        }
    }
