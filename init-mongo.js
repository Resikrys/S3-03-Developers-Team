
db = db.getSiblingDB('escapeRoomMongo');

db.rewards.insertOne({
    "__comment": "This is a dummy document to ensure 'escapeRoomMongo' database and 'rewards' collection are created on startup.",
    "creationDate": new Date()
});

print("Database 'escapeRoomMongo' and 'rewards' collection initialized with a dummy document.");