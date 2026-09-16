package your.package.api.client;

import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ApiResponseMapper {

    private ApiResponseMapper() {
    }

    public static ApiResponse from(
            Response response) {

        Map<String, List<String>> headers =
                response
                        .headers()
                        .asList()
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Header::getName,
                                        Collectors.mapping(
                                                Header::getValue,
                                                Collectors.toList()
                                        )
                                )
                        );

        return new ApiResponse(
                response.statusCode(),
                response.asString(),
                headers
        );
    }
}
