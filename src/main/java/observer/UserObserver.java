package observer;

import model.Player; // Keep import

public class UserObserver implements Observer {
    private final Player player;

    public UserObserver(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getId (){ // Useful for identification, e.g., in lists
        return player.getId();
    }

    @Override
    public void update(NotificationEvent event) {
        String notificationMessage = "";
        switch (event.getType()) {
            case ROOM_CREATED:
                notificationMessage = "🎉 New Room '" + event.getDescription() + "' (ID: " + event.getEntityId() + ") has been created!";
                break;
            case ROOM_UPDATED:
                notificationMessage = "🔄 Room '" + event.getDescription() + "' (ID: " + event.getEntityId() + ") has been updated.";
                break;
            case ROOM_DELETED:
                notificationMessage = "🗑️ Room '" + event.getDescription() + "' (ID: " + event.getEntityId() + ") has been deleted.";
                break;
            case DECORATION_CREATED:
                notificationMessage = "✨ New Decorative Object '" + event.getDescription() + "' (ID: " + event.getEntityId() + ") has been added.";
                break;
            case DECORATION_UPDATED:
                notificationMessage = "🔄 Decorative Object '" + event.getDescription() + "' (ID: " + event.getEntityId() + ") has been updated.";
                break;
            case DECORATION_DELETED:
                notificationMessage = "🗑️ Decorative Object '" + event.getDescription() + "' (ID: " + event.getEntityId() + ") has been removed.";
                break;
            case CLUE_CREATED:
                notificationMessage = "🕵️‍♀️ New Clue '" + event.getDescription() + "' (ID: " + event.getEntityId() + ") has been added.";
                break;
            case CLUE_UPDATED:
                notificationMessage = "🔄 Clue '" + event.getDescription() + "' (ID: " + event.getEntityId() + ") has been updated.";
                break;
            case CLUE_DELETED:
                notificationMessage = "🗑️ Clue '" + event.getDescription() + "' (ID: " + event.getEntityId() + ") has been removed.";
                break;
            default:
                notificationMessage = "An unknown update occurred for " + event.getEntityName() + " (ID: " + event.getEntityId() + "): " + event.getDescription();
                break;
        }
        player.addNotification(notificationMessage);
        System.out.printf(">>> Notification for %s (%s): %s%n",
                player.getName(), player.getEmail(), notificationMessage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserObserver that = (UserObserver) o;
        return player.getId() == that.player.getId();
    }

    @Override
    public int hashCode() { // Crucial for correct `attach` and `detach`
        return Integer.hashCode(player.getId());
    }

    @Override
    public String toString() {
        return "UserObserver(Player ID: " + player.getId() + ", Name: " + player.getName() + ")";
    }
}
