package system;

import users.User;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Represents the comment in the system.
 */
public class Comment implements Serializable {

    private User author;
    private String text;
    private LocalDateTime createdAt;

    /**
     * Constructor for Comment.
     * @param author parameter value.
     * @param text parameter value.
     */
    public Comment(User author, String text) {
        this.author = author;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Gets the author.
     * @return User
     */
    public User getAuthor() { return author; }
    /**
     * Gets the text.
     * @return String
     */
    public String getText() { return text; }
    /**
     * Gets the created at.
     * @return LocalDateTime
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "[" + createdAt.toLocalDate() + "] " + author.getFullName() + ": " + text;
    }
}