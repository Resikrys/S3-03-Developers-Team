package observer;

import model.Player;

public interface Observer {
    void update(NotificationEvent event);
    Player getPlayer();
}
