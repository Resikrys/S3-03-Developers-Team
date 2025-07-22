package menu;

import manager.RewardManager;
import util.InputHelper;
import java.sql.SQLException; // Catch SQLException just in case, though MongoDB won't throw it directly here

public class RewardMenu {
    private final RewardManager rewardManager;
    private final InputHelper inputHelper;

    public RewardMenu(InputHelper inputHelper) {
        this.inputHelper = inputHelper;
        this.rewardManager = new RewardManager(inputHelper);
    }

    public void showMenu() {
        int option;
        do {
            printMenu();
            option = inputHelper.readInt("Choose an option: ");

            try {
                handleOption(option);
            } catch (Exception e) { // Catch all exceptions for robustness in menu level
                System.err.println("❌ An error occurred during the Reward operation: " + e.getMessage());
                e.printStackTrace(); // For debugging
            }
        } while (option != 0);
    }

    private void printMenu() {
        System.out.println("\n--- Reward Management ---");
        System.out.println("1. Create Reward");
        System.out.println("2. Search Reward by ID");
        System.out.println("3. List All Rewards");
        System.out.println("4. Search Rewards by Player Name");
        System.out.println("5. Update Reward");
        System.out.println("6. Delete Reward");
        System.out.println("0. Back to Main Menu");
        System.out.print("Select an option: ");
    }

    private void handleOption(int option) {
        switch (option) {
            case 1 -> rewardManager.createReward();
            case 2 -> rewardManager.getRewardById();
            case 3 -> rewardManager.getAllRewards();
            case 4 -> rewardManager.getRewardsByPlayerName();
            case 5 -> rewardManager.updateReward();
            case 6 -> rewardManager.deleteReward();
            case 0 -> System.out.println("Returning to main menu.");
            default -> System.out.println("Invalid option.");
        }
    }
}
