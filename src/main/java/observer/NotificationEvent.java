package observer;

public class NotificationEvent {
    private final EventType type;
    private final String entityName; // e.g., "Room", "Decoration", "Clue"
    private final int entityId;      // ID of the entity that changed
    private final String description; // A human-readable description of the change

    public NotificationEvent(EventType type, String entityName, int entityId, String description) {
        this.type = type;
        this.entityName = entityName;
        this.entityId = entityId;
        this.description = description;
    }

    public EventType getType() {
        return type;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getEntityId() {
        return entityId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "NotificationEvent{" +
                "type=" + type +
                ", entityName='" + entityName + '\'' +
                ", entityId=" + entityId +
                ", description='" + description + '\'' +
                '}';
    }
}