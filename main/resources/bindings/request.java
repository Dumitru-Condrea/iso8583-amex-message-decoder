import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class ApiRequest {

    private final Map<String, String> headers;
    private final Map<String, Object> queryParams;
    private final Map<String, Object> formParams;
    private final Map<String, Object> multipartParams;

    private final Object body;

    private final String username;
    private final String password;
    private final String bearerToken;

    private ApiRequest(Builder builder) {
        this.headers = new HashMap<>(builder.headers);
        this.queryParams = new HashMap<>(builder.queryParams);
        this.formParams = new HashMap<>(builder.formParams);
        this.multipartParams = new HashMap<>(builder.multipartParams);

        this.body = builder.body;

        this.username = builder.username;
        this.password = builder.password;
        this.bearerToken = builder.bearerToken;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static final class Builder {

        private final Map<String, String> headers;
        private final Map<String, Object> queryParams;
        private final Map<String, Object> formParams;
        private final Map<String, Object> multipartParams;

        private Object body;

        private String username;
        private String password;
        private String bearerToken;

        private Builder() {
            this.headers = new HashMap<>();
            this.queryParams = new HashMap<>();
            this.formParams = new HashMap<>();
            this.multipartParams = new HashMap<>();
        }

        private Builder(ApiRequest request) {
            this.headers = new HashMap<>(request.headers);
            this.queryParams = new HashMap<>(request.queryParams);
            this.formParams = new HashMap<>(request.formParams);
            this.multipartParams = new HashMap<>(request.multipartParams);

            this.body = request.body;

            this.username = request.username;
            this.password = request.password;
            this.bearerToken = request.bearerToken;
        }

        public Builder header(
                String name,
                String value) {

            headers.put(name, value);
            return this;
        }

        public Builder queryParam(
                String name,
                Object value) {

            queryParams.put(name, value);
            return this;
        }

        public Builder formParam(
                String name,
                Object value) {

            formParams.put(name, value);
            return this;
        }

        public Builder multipart(
                String name,
                Object value) {

            multipartParams.put(name, value);
            return this;
        }

        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        public Builder basicAuth(
                String username,
                String password) {

            requireNotNull(
                    username,
                    "Username must not be null"
            );

            requireNotNull(
                    password,
                    "Password must not be null"
            );

            this.username = username;
            this.password = password;
            this.bearerToken = null;

            return this;
        }

        public Builder bearerAuth(String token) {

            requireNotNull(
                    token,
                    "Bearer token must not be null"
            );

            this.bearerToken = token;
            this.username = null;
            this.password = null;

            return this;
        }

        public ApiRequest build() {
            return new ApiRequest(this);
        }

        private static void requireNotNull(
                Object value,
                String message) {

            if (value == null) {
                throw new IllegalArgumentException(message);
            }
        }
    }
}
