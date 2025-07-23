// src/main/java/dbconnection/MongoDBConnection.java
package dbconnection;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.ServerAddress; // For specifying host and port in builder

// NEW: For Connection Pool and Socket Settings (good practice)
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SocketSettings;

// NEW IMPORTS FOR CODECS
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import org.bson.codecs.jsr310.LocalDateTimeCodec; // <-- CORRECTED IMPORT FOR LOCALDATETIMECODEC
import java.util.concurrent.TimeUnit;

import dbconnection.EnvLoader;

import java.time.Duration; // <-- NEW IMPORT FOR DURATION
import java.util.Collections; // For Collections.singletonList


public class MongoDBConnection {
    private static volatile MongoClient mongoClientInstance;
    private static volatile MongoDatabase mongoDatabaseInstance;

    private static final String MONGO_HOST_KEY = "MONGO_HOST";
    private static final String MONGO_PORT_KEY = "MONGO_PORT";
    private static final String MONGO_DATABASE_KEY = "MONGO_DATABASE";

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

                if (mongoHost == null || mongoHost.isEmpty()) {
                    throw new IllegalStateException("MongoDB Host ('" + MONGO_HOST_KEY + "') not found or is empty in .env.");
                }
                if (mongoPortStr == null || mongoPortStr.isEmpty()) {
                    throw new IllegalStateException("MongoDB Port ('" + MONGO_PORT_KEY + "') not found or is empty in .env.");
                }

                int mongoPort;
                try {
                    mongoPort = Integer.parseInt(mongoPortStr);
                } catch (NumberFormatException e) {
                    throw new IllegalStateException("MongoDB Port ('" + MONGO_PORT_KEY + "') is not a valid integer in .env.", e);
                }

                // --- CODEC REGISTRY ---
                CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();

                CodecRegistry customCodecRegistry = fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(pojoCodecProvider),
                        fromCodecs(new LocalDateTimeCodec())
                );
                // --- END CODEC REGISTRY ---

                // Build MongoClientSettings
                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyToClusterSettings(builder ->
                                builder.hosts(Collections.singletonList(new ServerAddress(mongoHost, mongoPort))))
                        .codecRegistry(customCodecRegistry) // REGISTER THE CODEC REGISTRY HERE
                        .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(10, TimeUnit.SECONDS) // This should now resolve
                                .maxSize(100)
                                .minSize(10))
                        .applyToSocketSettings(builder -> builder.connectTimeout(2, TimeUnit.SECONDS) // This should now resolve
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