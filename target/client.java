
package your.package.db.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class MongoDbClient implements AutoCloseable {

    private static final String URI_KEY =
            "database.mongo.uri.tokenisation";

    private static final String USERNAME_KEY =
            "database.mongo.username.tokenisation";

    private static final String PASSWORD_KEY =
            "database.mongo.password.tokenisation";

    private final MongoClient mongoClient;

    private MongoDbClient() {

        this.mongoClient = MongoClients.create(
                buildMongoUri()
        );
    }

    public static MongoDbClient getInstance() {
        return Holder.INSTANCE;
    }

    public MongoDatabase database(String name) {
        return mongoClient.getDatabase(name);
    }

    public MongoCollection<Document> collection(
            String database,
            String collection) {

        return database(database)
                .getCollection(collection);
    }

    private static String buildMongoUri() {

        String template = Config.get(URI_KEY);
        String username = Config.get(USERNAME_KEY);
        String password = Config.get(PASSWORD_KEY);

        if (template == null
                || username == null
                || password == null) {

            throw new IllegalStateException(
                    "MongoDB connection properties are missing"
            );
        }

        if (!template.contains("{username}")
                || !template.contains("{password}")) {

            throw new IllegalArgumentException(
                    "MongoDB URI must contain "
                            + "{username} and {password}"
            );
        }

        return template
                .replace("{username}", encode(username))
                .replace("{password}", encode(password));
    }

    private static String encode(String value) {

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
