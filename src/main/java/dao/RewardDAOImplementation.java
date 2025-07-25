package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.ReplaceOptions;
import dbconnection.MongoDBConnection;
import model.Reward;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RewardDAOImplementation implements RewardDAO {

    private final MongoCollection<Reward> rewardsCollection;
    private static final String REWARDS_COLLECTION_NAME = "rewards";

    static {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);
    }

    public RewardDAOImplementation() {
        MongoDatabase database = MongoDBConnection.getDatabaseInstance();
        this.rewardsCollection = database.getCollection(REWARDS_COLLECTION_NAME, Reward.class);
    }

    @Override
    public void createReward(Reward reward) {
        try {
            rewardsCollection.insertOne(reward);
            System.out.println("✅ Reward created successfully for player: " + reward.getPlayerName() + " (ID: " + reward.getId() + ")");
        } catch (Exception e) {
            System.err.println("❌ Error creating reward: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Reward> getRewardById(ObjectId id) {
        try {
            return Optional.ofNullable(rewardsCollection.find(eq("_id", id)).first());
        } catch (Exception e) {
            System.err.println("❌ Error getting reward by ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Reward> getAllRewards() {
        List<Reward> rewards = new ArrayList<>();
        try {
            rewardsCollection.find().into(rewards);
        } catch (Exception e) {
            System.err.println("❌ Error getting all rewards: " + e.getMessage());
            e.printStackTrace();
        }
        return rewards;
    }

    @Override
    public List<Reward> getRewardsByPlayerName(String playerName) {
        List<Reward> rewards = new ArrayList<>();
        try {
            rewardsCollection.find(Filters.regex("playerName", playerName, "i")).into(rewards);
        } catch (Exception e) {
            System.err.println("❌ Error getting rewards by player name '" + playerName + "': " + e.getMessage());
            e.printStackTrace();
        }
        return rewards;
    }

    @Override
    public void updateReward(Reward reward) {
        if (reward.getId() == null) {
            System.err.println("❌ Cannot update reward: Reward ID is missing.");
            return;
        }
        try {
            rewardsCollection.replaceOne(eq("_id", reward.getId()), reward, new ReplaceOptions().upsert(false));
            System.out.println("✅ Reward updated successfully for ID: " + reward.getId());
        } catch (Exception e) {
            System.err.println("❌ Error updating reward with ID " + reward.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReward(ObjectId id) {
        try {
            rewardsCollection.deleteOne(eq("_id", id));
            System.out.println("✅ Reward deleted successfully for ID: " + id);
        } catch (Exception e) {
            System.err.println("❌ Error deleting reward with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}