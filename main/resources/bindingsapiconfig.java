package your.package.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApiConfig {

    private static final String CONFIG_FILE = "api.properties";

    private static final Properties PROPERTIES = loadProperties();

    private ApiConfig() {
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);

        if (isBlank(value)) {
            throw new IllegalStateException(
                    "Configuration property is missing: " + key
            );
        }

        return value;
    }

    public static String getAwsInstanceNumber() {
        String value = System.getProperty("awsInstanceNumber");

        if (isBlank(value)) {
            throw new IllegalStateException(
                    "System property 'awsInstanceNumber' is missing"
            );
        }

        return value;
    }

    public static String getEmvMasterUsername() {
        return get("ids.emv.master.username");
    }

    public static String getEmvMasterPassword() {
        return get("ids.emv.master.password");
    }

    public static String getGatewayMasterUsername() {
        return get("ids.gateway.master.username");
    }

    public static String getGatewayMasterPassword() {
        return get("ids.gateway.master.password");
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = ApiConfig.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Configuration file not found: " + CONFIG_FILE
                );
            }

            properties.load(inputStream);

            return properties;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to load configuration file: " + CONFIG_FILE,
                    e
            );
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
