package your.package.api.config;

import lombok.Getter;

@Getter
public final class ServiceUrl {

    private final String baseUrl;

    ServiceUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String url(String endpoint) {

        if (endpoint == null || endpoint.isEmpty()) {
            return baseUrl;
        }

        if (endpoint.startsWith("/")) {
            return baseUrl + endpoint;
        }

        return baseUrl + "/" + endpoint;
    }

    @Override
    public String toString() {
        return baseUrl;
    }
}
