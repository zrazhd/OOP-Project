package system;

import java.io.Serializable;
import java.time.LocalDateTime;


public class LogEntry implements Serializable {

    private final String actorName;
    private final String action;
    private final String details;
    private final LocalDateTime timestamp;

    public LogEntry(String actorName, String action, String details, LocalDateTime timestamp) {
        this.actorName = actorName;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getActorName() { return actorName; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] [" + action + "] " + actorName + ": " + details;
    }
}