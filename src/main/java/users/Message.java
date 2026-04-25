package users;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private Employee sender;
    private Employee receiver;
    private String text;
    private LocalDateTime date;
    private boolean isOfficial;

    public Message(Employee sender, Employee receiver, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.date = LocalDateTime.now();
        this.isOfficial = false;
    }

    public Message(Employee sender, Employee receiver, String text, boolean isOfficial) {
        this(sender, receiver, text);
        this.isOfficial = isOfficial;
    }

    public Employee getSender() {
        return sender;
    }

    public Employee getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    @Override
    public String toString() {
        return "[" + date.toLocalDate() + "] " + sender.getFullName() + " -> " + receiver.getFullName() + ": " + text;
    }
}
