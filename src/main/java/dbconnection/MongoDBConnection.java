package dbconnection;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ServerAddress;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

import com.mongodb.MongoCredential;


public class MongoDBConnection {
    private static volatile MongoClient mongoClientInstance;
    private static volatile MongoDatabase mongoDatabaseInstance;

    private static final String MONGO_HOST_KEY = "MONGO_HOST";
    private static final String MONGO_PORT_KEY = "MONGO_PORT";
    private static final String MONGO_DATABASE_KEY = "MONGO_DATABASE";

    private static final String MONGO_USERNAME_KEY = "MONGO_USER";
    private static final String MONGO_PASSWORD_KEY = "MONGO_PASSWORD";
    private static final String MONGO_AUTH_DATABASE = "admin";

    private MongoDBConnection() {}

    public static MongoClient getMongoClientInstance() {
        MongoClient result = mongoClientInstance;
        if (result != null) {
            return result;
        }

        synchronized (MongoDBConnection.class) {
            if (mongoClientInstance == null) {
                EnvLoader envLoader = EnvLoader.getInstance();

                String mongoHost = envLoader.getEnv(MONGO_HOST_KEY);
                String mongoPortStr = envLoader.getEnv(MONGO_PORT_KEY);
                String mongoUser = envLoader.getEnv(MONGO_USERNAME_KEY);
                String mongoPassword = envLoader.getEnv(MONGO_PASSWORD_KEY);

                if (mongoHost == null || mongoHost.isEmpty()) {
                    throw new IllegalStateException("MongoDB Host ('" + MONGO_HOST_KEY + "') not found or is empty in .env.");
                }
                if (mongoPortStr == null || mongoPortStr.isEmpty()) {
                    throw new IllegalStateException("MongoDB Port ('" + MONGO_PORT_KEY + "') not found or is empty in .env.");
                }
                if (mongoUser == null || mongoUser.isEmpty() || mongoPassword == null || mongoPassword.isEmpty()) {
                    throw new IllegalStateException("MongoDB Username or Password ('" + MONGO_USERNAME_KEY + "'/'" + MONGO_PASSWORD_KEY + "') not found or is empty in .env.");
                }

                int mongoPort;
                try {
                    mongoPort = Integer.parseInt(mongoPortStr);
                } catch (NumberFormatException e) {
                    throw new IllegalStateException("MongoDB Port ('" + MONGO_PORT_KEY + "') is not a valid integer in .env.", e);
                }

                MongoCredential credential = MongoCredential.createCredential(mongoUser, MONGO_AUTH_DATABASE, mongoPassword.toCharArray());

                CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();

                CodecRegistry customCodecRegistry = fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(pojoCodecProvider),
                        fromCodecs(new LocalDateTimeCodec())
                );

                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Collections.singletonList(new ServerAddress(mongoHost, mongoPort))))
                        .credential(credential)
                        .codecRegistry(customCodecRegistry)
                        .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(10, TimeUnit.SECONDS)
                                .maxSize(100)
                                .minSize(10))
                        .applyToSocketSettings(builder -> builder.connectTimeout(2, TimeUnit.SECONDS)
                                .readTimeout(5, TimeUnit.SECONDS))
                        .build();

                System.out.println("Attempting to connect to MongoDB at: " + mongoHost + ":" + mongoPort);

                try {
                    mongoClientInstance = MongoClients.create(settings);
                    System.out.println("✅ MongoDB MongoClient initialized.");
                } catch (Exception e) {
                    System.err.println("❌ Error creating MongoDB MongoClient with settings: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Failed to create MongoDB MongoClient", e);
                }
            }
            return mongoClientInstance;
        }
    }

    public static synchronized MongoDatabase getDatabaseInstance() {
        if (mongoDatabaseInstance == null) {
            MongoClient client = getMongoClientInstance();

            EnvLoader envLoader = EnvLoader.getInstance();
            String mongoDatabaseName = envLoader.getEnv(MONGO_DATABASE_KEY);

            if (mongoDatabaseName == null || mongoDatabaseName.isEmpty()) {
                throw new IllegalStateException("MongoDB database name ('" + MONGO_DATABASE_KEY + "') not found or is empty in .env.");
            }

            try {
                mongoDatabaseInstance = client.getDatabase(mongoDatabaseName);
                System.out.println("✅ Connected to MongoDB database: " + mongoDatabaseName);
            } catch (Exception e) {
                System.err.println("❌ Error getting MongoDB database: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to get MongoDB database", e);
            }
        }
        return mongoDatabaseInstance;
    }

    public static void closeConnection() {
        if (mongoClientInstance != null) {
            try {
                mongoClientInstance.close();
                mongoClientInstance = null;
                mongoDatabaseInstance = null;
                System.out.println("✅ MongoDB connection closed.");
            } catch (Exception e) {
                System.err.println("Error closing MongoDB connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}