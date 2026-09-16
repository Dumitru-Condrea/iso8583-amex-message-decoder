package your.package.api.config;

public final class ServiceUrlBuilder {

    private static final String SERVICE_PLACEHOLDER = "{service}";
    private static final String NUMBER_PLACEHOLDER = "{number}";

    private ServiceUrlBuilder() {
    }

    public static ServiceUrl build(ServiceType type) {

        String baseUrl = ApiConfig.get("api.base-url");

        String instanceNumber =
                ApiConfig.getAwsInstanceNumber();

        String port =
                ApiConfig.get(type.getPortProperty());

        String url = baseUrl
                .replace(
                        SERVICE_PLACEHOLDER,
                        type.getService()
                )
                .replace(
                        NUMBER_PLACEHOLDER,
                        instanceNumber
                );

        if (port != null && !port.trim().isEmpty()) {
            url = url + ":" + port;
        }

        return new ServiceUrl(url);
    }
}
