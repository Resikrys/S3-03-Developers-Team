package manager;

import dao.RewardDAO;
import dao.RewardDAOImplementation;
import model.Reward;
import util.InputHelper;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public class RewardManager {
    private final RewardDAO rewardDao;
    private final InputHelper inputHelper;

    public RewardManager(InputHelper inputHelper) {
        this.inputHelper = inputHelper;
        this.rewardDao = new RewardDAOImplementation();
    }

    public void createReward() {
        System.out.println("\n--- Create New Reward ---");
        String playerName = inputHelper.readString("Enter player name: ");
        String escapeRoomName = inputHelper.readString("Enter escape room name: ");
        String certificateMessage = inputHelper.readString("Enter certificate message: ");

        boolean hasSpecialReward = inputHelper.readBoolean("Does this reward have a special message? (yes/no): ");
        Reward newReward;
        if (hasSpecialReward) {
            String specialMessage = inputHelper.readString("Enter special message: ");
            newReward = new Reward(playerName, escapeRoomName, certificateMessage, specialMessage);
        } else {
            newReward = new Reward(playerName, escapeRoomName, certificateMessage);
        }

        rewardDao.createReward(newReward);
    }

    public void getRewardById() {
        System.out.println("\n--- Search Reward by ID ---");
        String idString = inputHelper.readString("Enter Reward ID (e.g., 65e8a7b0c1d2e3f4a5b6c7d8): ");
        try {
            ObjectId id = new ObjectId(idString);
            Optional<Reward> reward = rewardDao.getRewardById(id);
            if (reward.isPresent()) {
                System.out.println("🎯 Found Reward: " + reward.get());
            } else {
                System.out.println("⚠️ No reward found with ID: " + idString);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid Reward ID format. Please enter a valid 24-character hexadecimal string.");
        }
    }

    public void getAllRewards() {
        System.out.println("\n--- All Rewards ---");
        List<Reward> rewards = rewardDao.getAllRewards();
        if (rewards.isEmpty()) {
            System.out.println("📭 No rewards available.");
        } else {
            rewards.forEach(System.out::println);
        }
    }

    public void getRewardsByPlayerName() {
        System.out.println("\n--- Search Rewards by Player Name ---");
        String playerName = inputHelper.readString("Enter player name to search: ");
        List<Reward> rewards = rewardDao.getRewardsByPlayerName(playerName);
        if (rewards.isEmpty()) {
            System.out.println("📭 No rewards found for player: " + playerName);
        } else {
            System.out.println("--- Rewards for Player: " + playerName + " ---");
            rewards.forEach(System.out::println);
        }
    }

    public void updateReward() {
        System.out.println("\n--- Update Reward ---");
        String idString = inputHelper.readString("Enter ID of the reward to update: ");
        try {
            ObjectId id = new ObjectId(idString);
            Optional<Reward> existingRewardOptional = rewardDao.getRewardById(id);

            if (existingRewardOptional.isPresent()) {
                Reward existingReward = existingRewardOptional.get();
                System.out.println("Current Reward details: " + existingReward);

                String newPlayerName = inputHelper.readString("New player name (current: " + existingReward.getPlayerName() + "): ");
                String newEscapeRoomName = inputHelper.readString("New escape room name (current: " + existingReward.getEscapeRoomName() + "): ");
                String newCertificateMessage = inputHelper.readString("New certificate message (current: " + existingReward.getCertificateMessage() + "): ");

                String currentSpecialRewardDisplay = (existingReward.getSpecialRewardDetails() != null && !existingReward.getSpecialRewardDetails().isEmpty()) ?
                        existingReward.getSpecialRewardDetails() : "None";

                boolean updateSpecialReward = inputHelper.readBoolean("Update special message? (yes/no, current: " + currentSpecialRewardDisplay + "): ");

                if (updateSpecialReward) {
                    String newSpecialMessage = inputHelper.readString("Enter new special message (leave empty to clear): ");
                    existingReward.setSpecialRewardDetails(newSpecialMessage.isEmpty() ? null : newSpecialMessage);
                } else {
                }


                existingReward.setPlayerName(newPlayerName);
                existingReward.setEscapeRoomName(newEscapeRoomName);
                existingReward.setCertificateMessage(newCertificateMessage);

                rewardDao.updateReward(existingReward);
            } else {
                System.out.println("⚠️ No reward found with ID: " + idString);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid Reward ID format. Please enter a valid 24-character hexadecimal string.");
        }
    }

    public void deleteReward() {
        System.out.println("\n--- Delete Reward ---");
        String idString = inputHelper.readString("Enter ID of the reward to delete: ");
        try {
            ObjectId id = new ObjectId(idString);
            Optional<Reward> existingReward = rewardDao.getRewardById(id);
            if (existingReward.isPresent()) {
                rewardDao.deleteReward(id);
            } else {
                System.out.println("⚠️ No reward found with ID: " + idString);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid Reward ID format. Please enter a valid 24-character hexadecimal string.");
        }
    }
}
