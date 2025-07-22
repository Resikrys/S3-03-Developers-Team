// init-mongo.js

// Connect to the database specified in MONGO_INITDB_DATABASE
// This is implied when the script runs in the context of the container's initdb.
// However, explicitly specifying ensures clarity.
// If you want to connect as the root user first, you'd do:
// db.getSiblingDB("admin").auth("escaperoom_mongo_user", "12345");
// then switch to your database.
// But for this init script, simply targeting the database is usually enough
// because the Docker entrypoint handles the initial user and database context.

db = db.getSiblingDB('escapeRoomMongo'); // Explicitly switch to your desired database

// Create a dummy collection and insert a dummy document
// This forces the database and collection to exist
db.rewards.insertOne({
    "__comment": "This is a dummy document to ensure 'escapeRoomMongo' database and 'rewards' collection are created on startup.",
    "creationDate": new Date()
});

// You can add more setup here if needed, e.g., creating other collections, indexes, or roles
// db.anotherCollection.createIndex({ "field": 1 });
// db.createCollection("anotherEmptyCollection");

print("Database 'escapeRoomMongo' and 'rewards' collection initialized with a dummy document.");