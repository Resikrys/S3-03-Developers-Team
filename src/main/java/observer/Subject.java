package observer;

import java.util.List;

public interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers(NotificationEvent event);
    List<Observer> getObservers();
}
