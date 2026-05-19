package research;

import users.User;
import java.io.Serializable;
import java.util.*;


/**
 * Represents the research journal in the system.
 */
public class ResearchJournal implements Serializable {

    private String name;
    private String issn;
    private int foundedYear;
    private List<ResearchPaper> papers;
    private List<User> subscribers;

    /**
     * Constructor for ResearchJournal.
     * @param name parameter value.
     * @param issn parameter value.
     * @param foundedYear parameter value.
     */
    public ResearchJournal(String name, String issn, int foundedYear) {
        this.name = name;
        this.issn = issn;
        this.foundedYear = foundedYear;
        this.papers = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }


    /**
     * subscribe.
     * @param user parameter value.
     */
    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
            System.out.println(user.getFullName() + " subscribed to \"" + name + "\"");
        }
    }

    /**
     * unsubscribe.
     * @param user parameter value.
     */
    public void unsubscribe(User user) {
        subscribers.remove(user);
        System.out.println(user.getFullName() + " unsubscribed from \"" + name + "\"");
    }


    
    /**
     * publishPaper.
     * @param paper parameter value.
     */
    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        System.out.println("[" + name + "] New paper published: \"" + paper.getTitle() + "\"");
        notifySubscribers(paper);
    }

    /**
     * notifySubscribers.
     * @param paper parameter value.
     */
    private void notifySubscribers(ResearchPaper paper) {
        for (User subscriber : subscribers) {
            System.out.println("  → " + subscriber.getFullName()
                    + ": new paper in \"" + name + "\" — " + paper.getTitle());
        }
    }

    //Getters and Setters

    /**
     * Gets the name.
     * @return String
     */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    /**
     * Gets the issn.
     * @return String
     */
    public String getIssn() { return issn; }
    /**
     * Gets the founded year.
     * @return int
     */
    public int getFoundedYear() { return foundedYear; }
    /**
     * Gets the papers.
     * @return List&lt;ResearchPaper&gt;
     */
    public List<ResearchPaper> getPapers() { return Collections.unmodifiableList(papers); }
    /**
     * Gets the subscribers.
     * @return List&lt;User&gt;
     */
    public List<User> getSubscribers() { return Collections.unmodifiableList(subscribers); }

    @Override
    public String toString() {
        return "\"" + name + "\" (ISSN: " + issn + ", founded " + foundedYear + ")"
                + " | Papers: " + papers.size()
                + " | Subscribers: " + subscribers.size();
    }
}