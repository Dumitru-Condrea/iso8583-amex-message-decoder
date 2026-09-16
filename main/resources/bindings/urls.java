package your.package.api.config;

public final class ServiceUrls {

    public static final ServiceUrl EMV =
            ServiceUrlBuilder.build(ServiceType.EMV);

    public static final ServiceUrl GATEWAY =
            ServiceUrlBuilder.build(ServiceType.GATEWAY);

    public static final ServiceUrl EMV_AUTH =
            ServiceUrlBuilder.build(ServiceType.EMV_AUTH);

    public static final ServiceUrl GATEWAY_AUTH =
            ServiceUrlBuilder.build(ServiceType.GATEWAY_AUTH);

    private ServiceUrls() {
    }
}
