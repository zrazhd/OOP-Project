package app;

import enums.CitationFormat;
import research.*;
import users.GraduateStudent;
import users.Teacher;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Shared researcher sub-menu, callable from both TeacherMenu and StudentMenu
 * when the user is a Researcher (Teacher who isResearcher(), or GraduateStudent).
 *
 * Usage:
 *   new ResearcherMenu(teacher, database, scanner).show();
 *   new ResearcherMenu(gradStudent, database, scanner).show();
 */
public class ResearcherMenu {

    private final Researcher researcher;   // the logged-in user as Researcher
    private final String ownerName;        // for display
    private final Database database;
    private final Scanner scanner;

    /** Constructor for a Teacher who is also a Researcher. */
    public ResearcherMenu(Teacher teacher, Database database, Scanner scanner) {
        this.researcher  = teacher;
        this.ownerName   = teacher.getFullName();
        this.database    = database;
        this.scanner     = scanner;
    }

    /** Constructor for a GraduateStudent who is always a Researcher. */
    public ResearcherMenu(GraduateStudent gs, Database database, Scanner scanner) {
        this.researcher  = gs;
        this.ownerName   = gs.getFullName();
        this.database    = database;
        this.scanner     = scanner;
    }

    public void show() {
        while (true) {
            System.out.println("\n=== RESEARCHER MENU — " + ownerName + " ===");
            System.out.println("1.  Add research paper");
            System.out.println("2.  View my papers (sorted)");
            System.out.println("3.  Get citation for a paper");
            System.out.println("4.  My h-index");
            System.out.println("5.  Join research project");
            System.out.println("6.  View my research projects");
            System.out.println("7.  Add paper to a project");
            System.out.println("8.  View ALL university papers (sorted)");
            System.out.println("9.  Top cited researchers in university");
            if (researcher instanceof GraduateStudent) {
                System.out.println("10. Add diploma project");
                System.out.println("11. View diploma projects");
                System.out.println("12. Set supervisor");
            }
            System.out.println("0.  Back");
            System.out.println("=========================================");
            System.out.print("Choice: ");

            int choice = readInt();
            switch (choice) {
                case 1  -> addPaper();
                case 2  -> viewMyPapersSorted();
                case 3  -> getCitation();
                case 4  -> printHIndex();
                case 5  -> joinProject();
                case 6  -> viewMyProjects();
                case 7  -> addPaperToProject();
                case 8  -> viewAllUniversityPapers();
                case 9  -> topCitedResearchers();
                case 10 -> { if (researcher instanceof GraduateStudent gs) addDiplomaProject(gs); else back(); }
                case 11 -> { if (researcher instanceof GraduateStudent gs) viewDiplomaProjects(gs); else back(); }
                case 12 -> { if (researcher instanceof GraduateStudent gs) setSupervisor(gs); else back(); }
                case 0  -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ─── 1. Add paper ────────────────────────────────────────────────────────────

    private void addPaper() {
        System.out.print("Title: ");
        String title = readLine();
        System.out.print("Journal: ");
        String journal = readLine();
        System.out.print("Number of pages: ");
        int pages = readInt();
        System.out.print("Year (e.g. 2024): ");
        int year = readInt();
        System.out.print("Month (1-12): ");
        int month = readInt();
        System.out.print("Citations: ");
        int citations = readInt();
        System.out.print("DOI (leave blank to auto-generate): ");
        String doi = readLine();
        if (doi.isEmpty()) {
            doi = title.toLowerCase().replace(' ', '-') + "-" + year;
        }

        ResearchPaper paper = new ResearchPaper(
                title,
                List.of(ownerName),
                journal,
                pages,
                LocalDate.of(year, Math.max(1, Math.min(12, month)), 1),
                doi,
                citations
        );
        researcher.addResearchPaper(paper);
        System.out.println("Paper added: " + paper);
    }

    // ─── 2. View my papers sorted ────────────────────────────────────────────────

    private void viewMyPapersSorted() {
        if (researcher.getResearchPapers().isEmpty()) {
            System.out.println("You have no research papers yet.");
            return;
        }
        Comparator<ResearchPaper> comp = pickComparator();
        researcher.printPapers(comp);
    }

    // ─── 3. Get citation ─────────────────────────────────────────────────────────

    private void getCitation() {
        List<ResearchPaper> papers = researcher.getResearchPapers();
        if (papers.isEmpty()) {
            System.out.println("No papers.");
            return;
        }
        printNumberedPapers(papers);
        System.out.print("Select paper number: ");
        int idx = readInt();
        if (idx < 1 || idx > papers.size()) { System.out.println("Invalid."); return; }

        System.out.println("Format: 1=Plain Text  2=BibTeX");
        System.out.print("Choice: ");
        CitationFormat fmt = readInt() == 2 ? CitationFormat.BIBTEX : CitationFormat.PLAIN_TEXT;
        System.out.println("\n" + papers.get(idx - 1).getCitation(fmt));
    }

    // ─── 4. h-index ──────────────────────────────────────────────────────────────

    private void printHIndex() {
        System.out.println("Your h-index: " + researcher.calculateHIndex());
    }

    // ─── 5. Join project ─────────────────────────────────────────────────────────

    private void joinProject() {
        System.out.print("Project topic: ");
        String topic = readLine();
        ResearchProject project = new ResearchProject(topic);
        try {
            researcher.joinResearchProject(project);
            System.out.println("Joined project: " + topic);
        } catch (NotResearcherException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ─── 6. View my projects ─────────────────────────────────────────────────────

    private void viewMyProjects() {
        List<ResearchProject> projects = researcher.getResearchProjects();
        if (projects.isEmpty()) {
            System.out.println("No research projects.");
            return;
        }
        System.out.println("\n--- My Research Projects ---");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i + 1) + ". " + projects.get(i));
        }
    }

    // ─── 7. Add paper to project ─────────────────────────────────────────────────

    private void addPaperToProject() {
        List<ResearchProject> projects = researcher.getResearchProjects();
        if (projects.isEmpty()) { System.out.println("No projects."); return; }

        System.out.println("--- My Projects ---");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println((i + 1) + ". " + projects.get(i).getTopic());
        }
        System.out.print("Select project: ");
        int pIdx = readInt();
        if (pIdx < 1 || pIdx > projects.size()) { System.out.println("Invalid."); return; }

        List<ResearchPaper> papers = researcher.getResearchPapers();
        if (papers.isEmpty()) { System.out.println("No papers to add."); return; }

        printNumberedPapers(papers);
        System.out.print("Select paper: ");
        int paperIdx = readInt();
        if (paperIdx < 1 || paperIdx > papers.size()) { System.out.println("Invalid."); return; }

        projects.get(pIdx - 1).addPaper(papers.get(paperIdx - 1));
        System.out.println("Paper added to project.");
    }

    // ─── 8. All university papers ────────────────────────────────────────────────

    private void viewAllUniversityPapers() {
        List<ResearchPaper> all = database.getAllResearchPapers();
        if (all.isEmpty()) { System.out.println("No research papers in the university."); return; }

        Comparator<ResearchPaper> comp = pickComparator();
        all.sort(comp);

        System.out.println("\n=== All University Research Papers ===");
        for (ResearchPaper p : all) {
            System.out.println("  " + p);
        }
    }

    // ─── 9. Top cited researchers ────────────────────────────────────────────────

    private void topCitedResearchers() {
        List<Researcher> researchers = database.getAllResearchers();
        if (researchers.isEmpty()) { System.out.println("No researchers found."); return; }

        researchers.sort(Comparator.comparingInt(Researcher::calculateHIndex).reversed());

        System.out.println("\n=== Top Cited Researchers (by h-index) ===");
        int rank = 1;
        for (Researcher r : researchers) {
            String name = (r instanceof users.User u) ? u.getFullName() : r.toString();
            System.out.printf("%2d. %-30s  h-index: %d   papers: %d%n",
                    rank++, name, r.calculateHIndex(), r.getResearchPapers().size());
        }
    }

    // ─── 10. Add diploma project (GraduateStudent only) ──────────────────────────

    private void addDiplomaProject(GraduateStudent gs) {
        System.out.print("Diploma project title: ");
        String title = readLine();
        ResearchPaper diploma = new ResearchPaper(
                title,
                List.of(ownerName),
                "Diploma Project",
                1,
                LocalDate.now(),
                title.toLowerCase().replace(' ', '-') + "-diploma",
                0
        );
        gs.addDiplomaProject(diploma);
        System.out.println("Diploma project added.");
    }

    // ─── 11. View diploma projects ───────────────────────────────────────────────

    private void viewDiplomaProjects(GraduateStudent gs) {
        List<ResearchPaper> diplomas = gs.getDiplomaProjects();
        if (diplomas.isEmpty()) { System.out.println("No diploma projects yet."); return; }
        System.out.println("\n--- Diploma Projects ---");
        diplomas.forEach(System.out::println);
    }

    // ─── 12. Set supervisor ───────────────────────────────────────────────────────

    private void setSupervisor(GraduateStudent gs) {
        List<Teacher> teachers = database.getTeachers();
        if (teachers.isEmpty()) { System.out.println("No teachers available."); return; }

        System.out.println("\n--- Available Supervisors (Researchers) ---");
        int i = 1;
        for (Teacher t : teachers) {
            if (t.isResearcher()) {
                System.out.printf("%d. %-28s  h-index: %d%n", i, t.getFullName(), t.calculateHIndex());
            }
            i++;
        }

        System.out.print("Enter teacher number: ");
        int idx = readInt();
        if (idx < 1 || idx > teachers.size()) { System.out.println("Invalid."); return; }
        try {
            gs.setSupervisor(teachers.get(idx - 1));
            System.out.println("Supervisor set: " + teachers.get(idx - 1).getFullName());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ─── utilities ───────────────────────────────────────────────────────────────

    /**
     * Let the user pick a sort order for papers.
     */
    private Comparator<ResearchPaper> pickComparator() {
        System.out.println("Sort by: 1=Date  2=Citations (desc)  3=Pages (desc)");
        System.out.print("Choice: ");
        return switch (readInt()) {
            case 2  -> ResearchPaper.byCitations();
            case 3  -> ResearchPaper.byPages();
            default -> ResearchPaper.byDate();
        };
    }

    private void printNumberedPapers(List<ResearchPaper> papers) {
        for (int i = 0; i < papers.size(); i++) {
            System.out.println((i + 1) + ". " + papers.get(i));
        }
    }

    private void back() {
        System.out.println("Option not available for your role.");
    }

    private String readLine() {
        return scanner.nextLine().trim();
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
