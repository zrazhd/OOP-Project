package users;

import enums.DegreeType;
import enums.School;
import research.*;
import java.util.*;

public class GraduateStudent extends Student implements Researcher {

    private Researcher researcherSupervisor;
    private List<ResearchPaper> researchPapers;
    private List<ResearchProject> researchProjects;
    private List<ResearchPaper> diplomaProjects;

    public GraduateStudent(String userId, String firstName, String lastName, String email, String password,
                           DegreeType degreeType, School school, int yearOfStudy) {
        super(userId, firstName, lastName, email, password, degreeType, school, yearOfStudy);
        if (degreeType != DegreeType.MASTER && degreeType != DegreeType.PHD) {
            throw new IllegalArgumentException("GraduateStudent must be MASTER or PhD");
        }
        this.researchPapers = new ArrayList<>();
        this.researchProjects = new ArrayList<>();
        this.diplomaProjects = new ArrayList<>();
    }

    /**
     * Assign a research supervisor. Throws exception if supervisor's h-index < 3.
     */
    public void setSupervisor(Researcher supervisor) throws InvalidSupervisorException {
        if (supervisor.calculateHIndex() < 3) {
            throw new InvalidSupervisorException(
                "Cannot assign supervisor with h-index " + supervisor.calculateHIndex() + ". Minimum required h-index is 3."
            );
        }
        this.researcherSupervisor = supervisor;
    }

    public Researcher getSupervisor() {
        return researcherSupervisor;
    }

    /**
     * Add a research paper as a diploma project.
     */
    public void addDiplomaProject(ResearchPaper paper) {
        if (!diplomaProjects.contains(paper)) {
            diplomaProjects.add(paper);
        }
        if (!researchPapers.contains(paper)) {
            researchPapers.add(paper);
        }
    }

    public List<ResearchPaper> getDiplomaProjects() {
        return Collections.unmodifiableList(diplomaProjects);
    }

    // ===== Researcher interface implementation =====

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return researchPapers;
    }

    @Override
    public List<ResearchProject> getResearchProjects() {
        return researchProjects;
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        if (!researchPapers.contains(paper)) {
            researchPapers.add(paper);
        }
    }

    @Override
    public void joinResearchProject(ResearchProject project) {
        if (!researchProjects.contains(project)) {
            researchProjects.add(project);
            try {
                project.addParticipant(this);
            } catch (NotResearcherException e) {
                // Should never happen since GraduateStudent implements Researcher
                System.err.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    @Override
    public int calculateHIndex() {
        List<Integer> citationCounts = new ArrayList<>();
        for (ResearchPaper paper : researchPapers) {
            citationCounts.add(paper.getCitations());
        }
        citationCounts.sort(Collections.reverseOrder());

        int h = 0;
        for (int i = 0; i < citationCounts.size(); i++) {
            if (citationCounts.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(comparator);
        System.out.println("=== Research Papers of " + getFullName() + " ===");
        for (ResearchPaper paper : sorted) {
            System.out.println("  " + paper);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " | h-index: " + calculateHIndex() + " | Diploma projects: " + diplomaProjects.size();
    }
}
