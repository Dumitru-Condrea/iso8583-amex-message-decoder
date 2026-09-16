package your.package.api.config;

import lombok.Getter;

@Getter
public enum ServiceType {

    EMV("emv", "api.port.main"),
    GATEWAY("gateway", "api.port.gateway"),

    EMV_AUTH("emv-auth", "api.port.auth"),
    GATEWAY_AUTH("gateway-auth", "api.port.auth");

    private final String service;
    private final String portProperty;

    ServiceType(
            String service,
            String portProperty) {

        this.service = service;
        this.portProperty = portProperty;
    }
}
