package users;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the message in the system.
 */
public class Message implements Serializable {

    private Employee sender;
    private Employee receiver;
    private String text;
    private LocalDateTime date;
    private boolean isOfficial;

    /**
     * Constructor for Message.
     * @param sender parameter value.
     * @param receiver parameter value.
     * @param text parameter value.
     */
    public Message(Employee sender, Employee receiver, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.date = LocalDateTime.now();
        this.isOfficial = false;
    }

    /**
     * Constructor for Message.
     * @param sender parameter value.
     * @param receiver parameter value.
     * @param text parameter value.
     * @param isOfficial parameter value.
     */
    public Message(Employee sender, Employee receiver, String text, boolean isOfficial) {
        this(sender, receiver, text);
        this.isOfficial = isOfficial;
    }

    /**
     * Gets the sender.
     * @return Employee
     */
    public Employee getSender() {
        return sender;
    }

    /**
     * Gets the receiver.
     * @return Employee
     */
    public Employee getReceiver() {
        return receiver;
    }

    /**
     * Gets the text.
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the date.
     * @return LocalDateTime
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Checks if official.
     * @return boolean
     */
    public boolean isOfficial() {
        return isOfficial;
    }

    @Override
    public String toString() {
        return "[" + date.toLocalDate() + "] " + sender.getFullName() + " -> " + receiver.getFullName() + ": " + text;
    }
}
