package dao;

import model.Reward;
import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

public interface RewardDAO {
    void createReward(Reward reward);
    Optional<Reward> getRewardById(ObjectId id);
    List<Reward> getAllRewards();
    List<Reward> getRewardsByPlayerName(String playerName);
    void updateReward(Reward reward);
    void deleteReward(ObjectId id);
}