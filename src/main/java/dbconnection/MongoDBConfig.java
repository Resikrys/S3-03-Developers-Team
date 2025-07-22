////Si ho carrega tot desde ENVLoader aquest arxiu és innecessari!! --> Ara mateix no funciona
//package dbconnection;
//
//import io.github.cdimascio.dotenv.Dotenv;
//
//public class MongoDBConfig {
//    private static final Dotenv dotenv = Dotenv.load();
//    private static final String DB_HOST = dotenv.get("MONGO_HOST");
//    private static final String DB_PORT = dotenv.get("MONGO_PORT");
//    private static final String DB_DATABASE = dotenv.get("MONGO_DATABASE");
//    private static final String DB_USER = dotenv.get("MONGO_USER");
//    private static final String DB_PASSWORD = dotenv.get("MONGO_PASSWORD");
//
//    private static final String CONNECTION_STRING = String.format(
//            "mongodb://%s:%s@%s:%s/%s",
////            "mongodb://%s:%s/%s",
////            DB_HOST, DB_PORT, DB_DATABASE
//            DB_USER, DB_PASSWORD, DB_HOST, DB_PORT, DB_DATABASE
//    );
//
//    public static String getString() {
//        return CONNECTION_STRING;
//    }
//    public static String getDbDatabase(){
//        return DB_DATABASE;
//    }
//}
