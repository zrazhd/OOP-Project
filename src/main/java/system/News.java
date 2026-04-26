package system;

import enums.NewsType;
import users.Employee;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * News represents a university news article.
 * News with NewsType.RESEARCH is automatically pinned (appears first).
 * Any user can add comments.
 * Implements Observer pattern: notifies subscribers via NewsPublisher.
 */
public class News implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 1;

    private int newsId;
    private String title;
    private String content;
    private NewsType type;
    private Employee author;
    private LocalDateTime publishedAt;
    private boolean pinned;
    private List<Comment> comments;

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
     * Add a comment to this news article.
     * Any user (by name/role) can comment.
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    /**
     * Remove a comment (Admin/Manager privilege).
     */
    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    // Getters and Setters
    public int getNewsId() { return newsId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public NewsType getType() { return type; }
    public void setType(NewsType type) {
        this.type = type;
        this.pinned = type.isPinned();
    }
    public Employee getAuthor() { return author; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
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