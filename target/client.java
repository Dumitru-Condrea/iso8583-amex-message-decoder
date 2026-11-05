
package your.package.db.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public final class MongoDbClient implements AutoCloseable {

    private static final String URI_KEY =
            "database.mongo.uri.tokenisation";

    private static final String USERNAME_KEY =
            "database.mongo.username.tokenisation";

    private static final String PASSWORD_KEY =
            "database.mongo.password.tokenisation";

    private static final String DATABASE_KEY =
            "database.mongo.database.tokenisation";

    private static final String COLLECTION_KEY =
            "database.mongo.collection.tokenisation";

    private static final int MIN_POOL_SIZE = 0;
    private static final int MAX_POOL_SIZE = 5;

    private static final long MAX_IDLE_SECONDS = 60;
    private static final long MAX_WAIT_SECONDS = 10;

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    private MongoDbClient() {

        ConnectionString connectionString =
                new ConnectionString(buildMongoUri());

        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .applyToConnectionPoolSettings(builder ->
                                builder
                                        .minSize(MIN_POOL_SIZE)
                                        .maxSize(MAX_POOL_SIZE)
                                        .maxConnectionIdleTime(
                                                MAX_IDLE_SECONDS,
                                                TimeUnit.SECONDS
                                        )
                                        .maxWaitTime(
                                                MAX_WAIT_SECONDS,
                                                TimeUnit.SECONDS
                                        )
                        )
                        .build();

        this.mongoClient =
                MongoClients.create(settings);

        this.database =
                mongoClient.getDatabase(
                        requiredProperty(DATABASE_KEY)
                );

        this.collection =
                database.getCollection(
                        requiredProperty(COLLECTION_KEY)
                );
    }

    public static MongoDbClient getInstance() {
        return Holder.INSTANCE;
    }

    public MongoDatabase database() {
        return database;
    }

    public MongoCollection<Document> collection() {
        return collection;
    }

    public void ping() {
        database.runCommand(
                new Document("ping", 1)
        );
    }

    private static String buildMongoUri() {

        String template =
                requiredProperty(URI_KEY);

        String username =
                requiredProperty(USERNAME_KEY);

        String password =
                requiredProperty(PASSWORD_KEY);

        if (!template.contains("{username}")
                || !template.contains("{password}")) {

            throw new IllegalArgumentException(
                    "MongoDB URI must contain "
                            + "{username} and {password} placeholders"
            );
        }

        return template
                .replace("{username}", encode(username))
                .replace("{password}", encode(password));
    }

    private static String requiredProperty(
            String key) {

        String value = Config.get(key);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Missing MongoDB property: " + key
            );
        }

        return value;
    }

    private static String encode(
            String value) {

        try {
            return URLEncoder.encode(value, "UTF-8")
                    .replace("+", "%20");

        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(
                    "Failed to encode MongoDB credentials",
                    e
            );
        }
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    private static final class Holder {

        private static final MongoDbClient INSTANCE =
                new MongoDbClient();
    }
}
