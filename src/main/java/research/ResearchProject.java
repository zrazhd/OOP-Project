package research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the research project in the system.
 */
public class ResearchProject implements Serializable {

    private String topic;
    private List<Researcher> participants;
    private List<ResearchPaper> publishedPapers;

    /**
     * Constructor for ResearchProject.
     * @param topic parameter value.
     */
    public ResearchProject(String topic) {
        this.topic = topic;
        this.participants = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    /**
     * addParticipant.
     * @param person parameter value.
     */
    public void addParticipant(Researcher person) {
        if (person == null) return;
        if (!participants.contains(person)) {
            participants.add(person);
        }
    }

    /**
     * addPaper.
     * @param paper parameter value.
     */
    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
    }

    // Getters
    /**
     * Gets the topic.
     * @return String
     */
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    /**
     * Gets the participants.
     * @return List&lt;Researcher&gt;
     */
    public List<Researcher> getParticipants() { return participants; }
    /**
     * Gets the published papers.
     * @return List&lt;ResearchPaper&gt;
     */
    public List<ResearchPaper> getPublishedPapers() { return publishedPapers; }

    @Override
    public String toString() {
        return "ResearchProject: " + topic + " | Participants: " + participants.size() + " | Papers: " + publishedPapers.size();
    }
}
