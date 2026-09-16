package your.package.api.client;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public final class RestClient {

    public ApiResponse get(
            String url,
            ApiRequest request) {

        return execute(
                Method.GET,
                url,
                request
        );
    }

    public ApiResponse post(
            String url,
            ApiRequest request) {

        return execute(
                Method.POST,
                url,
                request
        );
    }

    public ApiResponse put(
            String url,
            ApiRequest request) {

        return execute(
                Method.PUT,
                url,
                request
        );
    }

    public ApiResponse delete(
            String url,
            ApiRequest request) {

        return execute(
                Method.DELETE,
                url,
                request
        );
    }

    private ApiResponse execute(
            Method method,
            String url,
            ApiRequest request) {

        RequestSpecification specification =
                RestAssured.given();

        apply(
                specification,
                request
        );

        Response response =
                specification.request(
                        method,
                        url
                );

        return ApiResponseMapper.from(response);
    }

    private void apply(
            RequestSpecification specification,
            ApiRequest request) {

        if (!request.getHeaders().isEmpty()) {
            specification.headers(
                    request.getHeaders()
            );
        }

        if (!request.getQueryParams().isEmpty()) {
            specification.queryParams(
                    request.getQueryParams()
            );
        }

        if (!request.getFormParams().isEmpty()) {
            specification.formParams(
                    request.getFormParams()
            );
        }

        if (!request.getMultipartParams().isEmpty()) {
            request
                    .getMultipartParams()
                    .forEach(
                            specification::multiPart
                    );
        }

        if (request.getBody() != null) {
            specification
                    .contentType(ContentType.JSON)
                    .body(request.getBody());
        }

        if (request.getUsername() != null) {
            specification
                    .auth()
                    .preemptive()
                    .basic(
                            request.getUsername(),
                            request.getPassword()
                    );
        }

        if (request.getBearerToken() != null) {
            specification.header(
                    "Authorization",
                    "Bearer "
                            + request.getBearerToken()
            );
        }
    }
}
