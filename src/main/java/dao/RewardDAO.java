package dao;

import model.Reward;
import org.bson.types.ObjectId; // If you want to search by ObjectId
import java.util.List;
import java.util.Optional;

public interface RewardDAO {
    void createReward(Reward reward);
    Optional<Reward> getRewardById(ObjectId id); // Search by MongoDB's unique ID
    List<Reward> getAllRewards();
    List<Reward> getRewardsByPlayerName(String playerName); // Custom search
    void updateReward(Reward reward); // Updates an existing reward (assumes ID is set)
    void deleteReward(ObjectId id); // Deletes by ID
}
