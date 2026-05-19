package system;

import enums.NewsType;
import users.Employee;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Represents the news in the system.
 */
public class News implements Serializable {

    private static int counter = 1;

    private int newsId;
    private String title;
    private String content;
    private NewsType type;
    private Employee author;
    private LocalDateTime publishedAt;
    private boolean pinned;
    private List<Comment> comments;

    /**
     * Constructor for News.
     * @param title parameter value.
     * @param content parameter value.
     * @param type parameter value.
     * @param author parameter value.
     */
    public News(String title, String content, NewsType type, Employee author) {
        this.newsId = counter++;
        this.title = title;
        this.content = content;
        this.type = type;
        this.author = author;
        this.publishedAt = LocalDateTime.now();
        // Research news is always pinned per spec
        this.pinned = type.isPinned();
        this.comments = new ArrayList<>();
    }


    /**
     * addComment.
     * @param comment parameter value.
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }


    /**
     * removeComment.
     * @param comment parameter value.
     */
    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    /**
     * Gets the comments.
     * @return List&lt;Comment&gt;
     */
    public List<Comment> getComments() {
        return comments;
    }

    // Getters and Setters
    /**
     * Gets the news id.
     * @return int
     */
    public int getNewsId() { return newsId; }
    /**
     * Gets the title.
     * @return String
     */
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    /**
     * Gets the content.
     * @return String
     */
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    /**
     * Gets the type.
     * @return NewsType
     */
    public NewsType getType() { return type; }
    /**
     * Sets the type.
     * @param type parameter value.
     */
    public void setType(NewsType type) {
        this.type = type;
        this.pinned = type.isPinned();
    }
    /**
     * Gets the author.
     * @return Employee
     */
    public Employee getAuthor() { return author; }
    /**
     * Gets the published at.
     * @return LocalDateTime
     */
    public LocalDateTime getPublishedAt() { return publishedAt; }
    /**
     * Checks if pinned.
     * @return boolean
     */
    public boolean isPinned() { return pinned; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return newsId == news.newsId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsId);
    }

    @Override
    public String toString() {
        return (pinned ? "[PINNED] " : "") + "[" + type + "] #" + newsId
                + " \"" + title + "\" by " + author.getFullName()
                + " @ " + publishedAt.toLocalDate();
    }
}