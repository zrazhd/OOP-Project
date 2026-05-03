package research;

import users.User;
import java.io.Serializable;
import java.util.*;


public class ResearchJournal implements Serializable {

    private String name;
    private String issn;
    private int foundedYear;
    private List<ResearchPaper> papers;
    private List<User> subscribers;

    public ResearchJournal(String name, String issn, int foundedYear) {
        this.name = name;
        this.issn = issn;
        this.foundedYear = foundedYear;
        this.papers = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }


    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
            System.out.println(user.getFullName() + " subscribed to \"" + name + "\"");
        }
    }

    public void unsubscribe(User user) {
        subscribers.remove(user);
        System.out.println(user.getFullName() + " unsubscribed from \"" + name + "\"");
    }


    
    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        System.out.println("[" + name + "] New paper published: \"" + paper.getTitle() + "\"");
        notifySubscribers(paper);
    }

    private void notifySubscribers(ResearchPaper paper) {
        for (User subscriber : subscribers) {
            System.out.println("  → " + subscriber.getFullName()
                    + ": new paper in \"" + name + "\" — " + paper.getTitle());
        }
    }

    //Getters and Setters

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIssn() { return issn; }
    public int getFoundedYear() { return foundedYear; }
    public List<ResearchPaper> getPapers() { return Collections.unmodifiableList(papers); }
    public List<User> getSubscribers() { return Collections.unmodifiableList(subscribers); }

    @Override
    public String toString() {
        return "\"" + name + "\" (ISSN: " + issn + ", founded " + foundedYear + ")"
                + " | Papers: " + papers.size()
                + " | Subscribers: " + subscribers.size();
    }
}