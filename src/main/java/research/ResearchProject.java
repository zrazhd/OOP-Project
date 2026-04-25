package research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String topic;
    private List<Researcher> participants;
    private List<ResearchPaper> publishedPapers;

    public ResearchProject(String topic) {
        this.topic = topic;
        this.participants = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    public void addParticipant(Object person) throws NotResearcherException {
        if (!(person instanceof Researcher)) {
            throw new NotResearcherException("Only Researchers can join a ResearchProject. " + person + " is not a Researcher.");
        }
        participants.add((Researcher) person);
    }

    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
    }

    // Getters
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public List<Researcher> getParticipants() { return participants; }
    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }

    @Override
    public String toString() {
        return "ResearchProject: " + topic + " | Participants: " + participants.size() + " | Papers: " + publishedPapers.size();
    }
}
