package system;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Represents the log entry in the system.
 */
public class LogEntry implements Serializable {

    private final String actorName;
    private final String action;
    private final String details;
    private final LocalDateTime timestamp;

    /**
     * Constructor for LogEntry.
     * @param actorName parameter value.
     * @param action parameter value.
     * @param details parameter value.
     * @param timestamp parameter value.
     */
    public LogEntry(String actorName, String action, String details, LocalDateTime timestamp) {
        this.actorName = actorName;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
    }

    /**
     * Gets the actor name.
     * @return String
     */
    public String getActorName() { return actorName; }
    /**
     * Gets the action.
     * @return String
     */
    public String getAction() { return action; }
    /**
     * Gets the details.
     * @return String
     */
    public String getDetails() { return details; }
    /**
     * Gets the timestamp.
     * @return LocalDateTime
     */
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] [" + action + "] " + actorName + ": " + details;
    }
}