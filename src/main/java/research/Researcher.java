package research;

import java.util.Comparator;
import java.util.List;

/**
 * Interface for researcher.
 */
public interface Researcher {

    List<ResearchPaper> getResearchPapers();

    List<ResearchProject> getResearchProjects();

    void addResearchPaper(ResearchPaper paper);

    void joinResearchProject(ResearchProject project) throws NotResearcherException;

    int calculateHIndex();

    void printPapers(Comparator<ResearchPaper> comparator);
}
