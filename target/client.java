package your.package.db.mongo;

import com.fasterxml.jackson.databind.JsonNode;
import org.bson.Document;

public final class MongoConfigurationService {

    private final MongoDbClient mongoDbClient;

    public MongoConfigurationService(
            MongoDbClient mongoDbClient) {

        this.mongoDbClient = mongoDbClient;
    }

    public JsonNode getEmvConfiguration(
            String tokenOwnerId,
            CardBrand brand) {

        Document document =
                mongoDbClient.findByTokenOwnerId(tokenOwnerId);

        Document emvConfig =
                document.get("emvConfig", Document.class);

        if (emvConfig == null) {
            throw new IllegalStateException(
                    "MongoDB emvConfig not found for tokenOwnerId: "
                            + tokenOwnerId
            );
        }

        Document brandConfig =
                emvConfig.get(brand.name(), Document.class);

        if (brandConfig == null) {
            throw new IllegalStateException(
                    "MongoDB EMV configuration not found for brand "
                            + brand
                            + " and tokenOwnerId "
                            + tokenOwnerId
            );
        }

        return JsonUtils.read(brandConfig.toJson());
    }
}
