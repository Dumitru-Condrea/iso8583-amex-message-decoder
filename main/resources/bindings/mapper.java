public final class ApiResponseMapper {

    private ApiResponseMapper() {
    }

    public static ApiResponse from(Response response) {

        Map<String, List<String>> headers = response
                .headers()
                .asList()
                .stream()
                .collect(Collectors.groupingBy(
                        Header::getName,
                        Collectors.mapping(
                                Header::getValue,
                                Collectors.toList()
                        )
                ));

        return new ApiResponse(
                response.statusCode(),
                response.asString(),
                headers
        );
    }
}
