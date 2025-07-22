// src/main/java/dbconnection/MongoDBConnection.java
package dbconnection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
//import dbconnection.EnvLoader; // Assuming EnvLoader is in the 'util' package -> innecessari xq està al mateix package i ja s'importa

public class MongoDBConnection {
    private static volatile MongoClient mongoClientInstance;
    private static volatile MongoDatabase mongoDatabaseInstance;

//    private static final String MONGO_URI_KEY = "mongodb.uri";
//    private static final String MONGO_DATABASE_KEY = "mongodb.database";
private static final String MONGO_HOST_KEY = "MONGO_HOST"; // NEW
    private static final String MONGO_PORT_KEY = "MONGO_PORT"; // NEW
    private static final String MONGO_DATABASE_KEY = "MONGO_DATABASE";

    // Private constructor to enforce Singleton pattern
    private MongoDBConnection() {}

    /**
     * Initializes and returns the singleton MongoClient instance.
     * Uses double-checked locking for thread safety.
     * @return The MongoClient instance.
     * @throws RuntimeException if connection fails or configuration is missing.
     */
    public static MongoClient getMongoClientInstance() {
        MongoClient result = mongoClientInstance;
        if (result != null) {
            return result;
        }

        synchronized (MongoDBConnection.class) {
            if (mongoClientInstance == null) {
                EnvLoader envLoader = EnvLoader.getInstance();

                // Get host and port separately
                String mongoHost = envLoader.getEnv(MONGO_HOST_KEY);
                String mongoPort = envLoader.getEnv(MONGO_PORT_KEY);

                if (mongoHost == null || mongoHost.isEmpty()) {
                    throw new IllegalStateException("MongoDB Host ('" + MONGO_HOST_KEY + "') not found or is empty in .env.");
                }
                if (mongoPort == null || mongoPort.isEmpty()) {
                    throw new IllegalStateException("MongoDB Port ('" + MONGO_PORT_KEY + "') not found or is empty in .env.");
                }

                // Construct the URI using the separate values
                // Use a default URI pattern, excluding user/pass for now as it's typically in the docker-compose init
                String mongoUri = "mongodb://" + mongoHost + ":" + mongoPort;
                System.out.println("Attempting to connect to MongoDB URI: " + mongoUri); // For debugging

                try {
                    mongoClientInstance = MongoClients.create(mongoUri);
                    System.out.println("✅ MongoDB MongoClient initialized.");
                } catch (Exception e) {
                    System.err.println("❌ Error creating MongoDB MongoClient with URI '" + mongoUri + "': " + e.getMessage());
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
            String mongoDatabaseName = envLoader.getEnv(MONGO_DATABASE_KEY); // This is correct, as MONGO_DATABASE is literal

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