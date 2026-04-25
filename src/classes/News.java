package classes;

import enums.NewsCategory;
import java.io.Serializable;
import java.time.*;
import java.util.*;

public class News implements Serializable, Comparable<News> {
    private static final long serialVersionUID = 1L;

    // ===== Fields =====
    private final int newsId;
    private static int idCounter = 1;

    private String title;
    private String content;
    private NewsCategory category;
    private final User author;          
    private final LocalDateTime publishedAt;
    private boolean isPinned;           
    private final List<Comment> comments;

    private static final Map<NewsCategory, List<User>> subscribers = new EnumMap<>(NewsCategory.class);

    public static class Comment implements Serializable {
        private static final long serialVersionUID = 1L;
        private final User author;
        private final String text;
        private final LocalDateTime postedAt;

        public Comment(User author, String text) {
            this.author = author;
            this.text = text;
            this.postedAt = LocalDateTime.now();
        }

        public User getAuthor() { return author; }
        public String getText() { return text; }
        public LocalDateTime getPostedAt() { return postedAt; }

        @Override
        public String toString() {
            return "[" + postedAt.toLocalDate() + "] " + author.getFullName() + ": " + text;
        }
    }

    public News(String title, String content, NewsCategory category, User author) {
        this.newsId = idCounter++;
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.publishedAt = LocalDateTime.now();
        this.isPinned = (category == NewsCategory.RESEARCH);
        this.comments = new ArrayList<>();

        notifySubscribers(this);
    }

    public static News createPaperAnnouncement(Researcher researcher, ResearchPaper paper, User systemAuthor) {
        String title = "New Research Publication: \"" + paper.getTitle() + "\"";
        String content = "Researcher " + researcherName(researcher)
                + " has published a new paper titled \"" + paper.getTitle() + "\""
                + " in " + paper.getJournal()
                + " (" + paper.getDatePublished().getYear() + ")."
                + " Citations so far: " + paper.getCitations() + "."
                + " DOI: " + paper.getDoi();
        return new News(title, content, NewsCategory.RESEARCH, systemAuthor);
    }

    public static News createTopCitedResearcherAnnouncement(Researcher topResearcher,
                                                             int totalCitations,
                                                             User systemAuthor) {
        String name = researcherName(topResearcher);
        String title = "Top Cited Researcher: " + name;
        String content = name + " has been recognized as the top cited researcher in the university"
                + " with a total of " + totalCitations + " citations"
                + " and an h-index of " + topResearcher.calculateHIndex() + "."
                + " Congratulations!";
        return new News(title, content, NewsCategory.RESEARCH, systemAuthor);
    }

    public static void subscribe(User user, NewsCategory category) {
        subscribers.computeIfAbsent(category, k -> new ArrayList<>()).add(user);
        System.out.println(user.getFullName() + " subscribed to " + category + " news.");
    }

    public static void unsubscribe(User user, NewsCategory category) {
        List<User> list = subscribers.get(category);
        if (list != null) {
            list.remove(user);
            System.out.println(user.getFullName() + " unsubscribed from " + category + " news.");
        }
    }

    private static void notifySubscribers(News news) {
        List<User> targets = subscribers.getOrDefault(news.category, Collections.emptyList());
        if (targets.isEmpty()) return;
        System.out.println("=== NOTIFICATION: New " + news.category + " news posted ===");
        System.out.println("  Title   : " + news.title);
        System.out.println("  Summary : " + news.content.substring(0, Math.min(100, news.content.length())) + "...");
        System.out.println("  Notified " + targets.size() + " subscriber(s).");
    }

    public void addComment(User author, String text) {
        comments.add(new Comment(author, text));
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    @Override
    public int compareTo(News other) {
        if (this.isPinned != other.isPinned) {
            return this.isPinned ? -1 : 1; 
        }
        return other.publishedAt.compareTo(this.publishedAt);
    }

    // ===== Getters =====

    public int getNewsId() { return newsId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public NewsCategory getCategory() { return category; }

    public void setCategory(NewsCategory category) {
        this.category = category;
        if (category == NewsCategory.RESEARCH) this.isPinned = true;
    }

    public User getAuthor() { return author; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public boolean isPinned() { return isPinned; }
    public void setPinned(boolean pinned) { this.isPinned = pinned; }

    public static Map<NewsCategory, List<User>> getSubscribers() {
        return Collections.unmodifiableMap(subscribers);
    }


    private static String researcherName(Researcher r) {
        if (r instanceof User) return ((User) r).getFullName();
        return r.toString();
    }

    @Override
    public String toString() {
        return String.format("[#%d%s | %s | %s] %s  (%d comment%s)",
                newsId,
                isPinned ? " 📌" : "",
                category,
                publishedAt.toLocalDate(),
                title,
                comments.size(),
                comments.size() == 1 ? "" : "s");
    }
}