package app;

import enums.CitationFormat;
import enums.NewsType;
import research.*;
import system.Lang;
import system.News;
import users.GraduateStudent;
import users.Teacher;
import users.User;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ResearcherMenu {

    private final Researcher researcher;
    private final String ownerName;
    private final Database database;
    private final Scanner scanner;

    public ResearcherMenu(Teacher teacher, Database database, Scanner scanner) {
        this.researcher  = teacher;
        this.ownerName   = teacher.getFullName();
        this.database    = database;
        this.scanner     = scanner;
    }

    public ResearcherMenu(GraduateStudent gs, Database database, Scanner scanner) {
        this.researcher  = gs;
        this.ownerName   = gs.getFullName();
        this.database    = database;
        this.scanner     = scanner;
    }

    public void show() {
        while (true) {
            Lang.header(Lang.get("res_menu") + " — " + ownerName);
            Lang.menuItem(1, "res_add");
            Lang.menuItem(2, "res_view");
            Lang.menuItem(3, "res_cite");
            Lang.menuItem(4, "res_hindex");
            Lang.menuItem(5, "res_join");
            Lang.menuItem(6, "res_projects");
            Lang.menuItem(7, "Add paper to a project", true);
            Lang.menuItem(8, "res_all_papers");
            Lang.menuItem(9, "res_top");
            Lang.menuItem(10, "res_publish");
            Lang.menuItem(11, "res_subscribe");
            Lang.menuItem(12, "stu_journals");
            if (researcher instanceof GraduateStudent) {
                System.out.println("\n  --- Graduate Student ---");
                Lang.menuItem(13, "Add diploma project", true);
                Lang.menuItem(14, "View diploma projects", true);
                Lang.menuItem(15, "Set supervisor", true);
            }
            Lang.menuBack();
            Lang.separator();
            Lang.prompt();

            switch (readInt()) {
                case 1  -> addPaper();
                case 2  -> viewMyPapersSorted();
                case 3  -> getCitation();
                case 4  -> printHIndex();
                case 5  -> joinProject();
                case 6  -> viewMyProjects();
                case 7  -> addPaperToProject();
                case 8  -> viewAllUniversityPapers();
                case 9  -> topCitedResearchers();
                case 10 -> publishToJournal();
                case 11 -> subscribeToJournal();
                case 12 -> viewJournals();
                case 13 -> { if (researcher instanceof GraduateStudent gs) addDiplomaProject(gs); else Lang.err(Lang.get("invalid")); }
                case 14 -> { if (researcher instanceof GraduateStudent gs) viewDiplomaProjects(gs); else Lang.err(Lang.get("invalid")); }
                case 15 -> { if (researcher instanceof GraduateStudent gs) setSupervisor(gs); else Lang.err(Lang.get("invalid")); }
                case 0  -> { return; }
                default -> Lang.err(Lang.get("invalid"));
            }
        }
    }

    private void addPaper() {
        System.out.print(Lang.get("title") + ": "); String title = scanner.nextLine().trim();
        System.out.print("Journal: "); String journal = scanner.nextLine().trim();
        System.out.print("Number of pages: "); int pages = readInt();
        System.out.print("Year (e.g. 2024): "); int year = readInt();
        System.out.print("Month (1-12): "); int month = readInt();
        System.out.print("Citations: "); int citations = readInt();
        System.out.print("DOI (leave blank to auto-generate): "); String doi = scanner.nextLine().trim();
        if (doi.isEmpty()) doi = title.toLowerCase().replace(' ', '-') + "-" + year;

        ResearchPaper paper = new ResearchPaper(title, List.of(ownerName), journal, pages, LocalDate.of(year, Math.max(1, Math.min(12, month)), 1), doi, citations);
        researcher.addResearchPaper(paper);
        database.log(ownerName, "ADD_PAPER", "Added paper: " + title);
        Lang.ok("Paper added: " + paper);
    }

    private void viewMyPapersSorted() {
        if (researcher.getResearchPapers().isEmpty()) { Lang.info(Lang.get("empty")); return; }
        researcher.printPapers(pickComparator());
    }

    private void getCitation() {
        List<ResearchPaper> papers = researcher.getResearchPapers();
        if (papers.isEmpty()) { Lang.info(Lang.get("empty")); return; }
        printNumberedPapers(papers);
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= papers.size()) { Lang.err(Lang.get("invalid")); return; }

        System.out.println("Format: 1=Plain Text  2=BibTeX");
        System.out.print(Lang.get("choice") + ": ");
        CitationFormat fmt = readInt() == 2 ? CitationFormat.BIBTEX : CitationFormat.PLAIN_TEXT;
        System.out.println("\n" + papers.get(idx).getCitation(fmt));
    }

    private void printHIndex() {
        System.out.println(Lang.get("res_hindex") + ": " + researcher.calculateHIndex());
    }

    private void joinProject() {
        System.out.print("Project topic: "); String topic = scanner.nextLine().trim();
        try {
            researcher.joinResearchProject(new ResearchProject(topic));
            database.log(ownerName, "JOIN_PROJECT", "Joined project: " + topic);
            Lang.ok(Lang.get("success"));
        } catch (NotResearcherException e) { Lang.err(e.getMessage()); }
    }

    private void viewMyProjects() {
        List<ResearchProject> projects = researcher.getResearchProjects();
        if (projects.isEmpty()) { Lang.info(Lang.get("empty")); return; }
        for (int i = 0; i < projects.size(); i++) System.out.println((i + 1) + ". " + projects.get(i));
    }

    private void addPaperToProject() {
        List<ResearchProject> projects = researcher.getResearchProjects();
        if (projects.isEmpty()) { Lang.info(Lang.get("empty")); return; }
        for (int i = 0; i < projects.size(); i++) System.out.println((i + 1) + ". " + projects.get(i).getTopic());
        System.out.print("Select project: "); int pIdx = readInt() - 1;
        if (pIdx < 0 || pIdx >= projects.size()) { Lang.err(Lang.get("invalid")); return; }

        List<ResearchPaper> papers = researcher.getResearchPapers();
        if (papers.isEmpty()) { Lang.info(Lang.get("empty")); return; }
        printNumberedPapers(papers);
        System.out.print("Select paper: "); int paperIdx = readInt() - 1;
        if (paperIdx < 0 || paperIdx >= papers.size()) { Lang.err(Lang.get("invalid")); return; }

        projects.get(pIdx).addPaper(papers.get(paperIdx));
        database.log(ownerName, "PROJECT_ADD_PAPER", "Added paper to project " + projects.get(pIdx).getTopic());
        Lang.ok(Lang.get("success"));
    }

    private void viewAllUniversityPapers() {
        List<ResearchPaper> all = database.getAllResearchPapers();
        if (all.isEmpty()) { Lang.info(Lang.get("empty")); return; }
        all.sort(pickComparator());
        System.out.println("\n=== All University Research Papers ===");
        all.forEach(p -> System.out.println("  " + p));
    }

    private void topCitedResearchers() {
        List<Researcher> researchers = database.getAllResearchers();
        if (researchers.isEmpty()) { Lang.info(Lang.get("empty")); return; }
        researchers.sort(Comparator.comparingInt(Researcher::calculateHIndex).reversed());
        System.out.println("\n=== Top Cited Researchers (by h-index) ===");
        int rank = 1;
        for (Researcher r : researchers) {
            String name = (r instanceof User u) ? u.getFullName() : r.toString();
            System.out.printf("%2d. %-30s  h-index: %d   papers: %d%n", rank++, name, r.calculateHIndex(), r.getResearchPapers().size());
        }
    }

    private void publishToJournal() {
        List<ResearchPaper> papers = researcher.getResearchPapers();
        List<ResearchJournal> journals = database.getJournals();
        if (papers.isEmpty() || journals.isEmpty()) { Lang.info(Lang.get("empty")); return; }

        printNumberedPapers(papers);
        System.out.print("Select paper: "); int pIdx = readInt() - 1;
        if (pIdx < 0 || pIdx >= papers.size()) { Lang.err(Lang.get("invalid")); return; }

        for (int i = 0; i < journals.size(); i++) System.out.println((i + 1) + ". " + journals.get(i).getName());
        System.out.print("Select journal: "); int jIdx = readInt() - 1;
        if (jIdx < 0 || jIdx >= journals.size()) { Lang.err(Lang.get("invalid")); return; }

        ResearchJournal journal = journals.get(jIdx);
        ResearchPaper paper = papers.get(pIdx);
        journal.publishPaper(paper);

        if (researcher instanceof User user) {
            News announcement = new News("New Publication: " + paper.getTitle(),
                    user.getFullName() + " published a new paper \"" + paper.getTitle() + "\" in " + journal.getName() + ".",
                    NewsType.RESEARCH, (users.Employee) user);
            database.addNews(announcement);
            database.log(ownerName, "PUBLISH_PAPER", "Published paper " + paper.getTitle() + " in " + journal.getName());
            Lang.ok("Published & Announcement created");
        }
    }

    private void subscribeToJournal() {
        List<ResearchJournal> journals = database.getJournals();
        if (journals.isEmpty()) { Lang.info(Lang.get("empty")); return; }
        for (int i = 0; i < journals.size(); i++) System.out.println((i + 1) + ". " + journals.get(i));
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < journals.size() && researcher instanceof User user) {
            journals.get(idx).subscribe(user);
            database.log(ownerName, "SUBSCRIBE_JOURNAL", "Subscribed to " + journals.get(idx).getName());
        } else Lang.err(Lang.get("invalid"));
    }

    private void viewJournals() {
        database.getJournals().forEach(j -> System.out.println("  " + j));
    }

    private void addDiplomaProject(GraduateStudent gs) {
        System.out.print("Title: "); String title = scanner.nextLine().trim();
        gs.addDiplomaProject(new ResearchPaper(title, List.of(ownerName), "Diploma", 1, LocalDate.now(), title.toLowerCase().replace(' ', '-') + "-diploma", 0));
        database.log(ownerName, "ADD_DIPLOMA", "Added diploma project: " + title);
        Lang.ok(Lang.get("success"));
    }
    private void viewDiplomaProjects(GraduateStudent gs) { gs.getDiplomaProjects().forEach(System.out::println); }
    private void setSupervisor(GraduateStudent gs) {
        List<Teacher> teachers = database.getTeachers();
        System.out.println("\n--- Available Supervisors (Researchers) ---");
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).isResearcher()) System.out.printf("%d. %-28s  h-index: %d%n", i + 1, teachers.get(i).getFullName(), teachers.get(i).calculateHIndex());
        }
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < teachers.size()) {
            try {
                gs.setSupervisor(teachers.get(idx));
                database.log(ownerName, "SET_SUPERVISOR", "Set supervisor to " + teachers.get(idx).getFullName());
                Lang.ok(Lang.get("success"));
            } catch (Exception e) { Lang.err(e.getMessage()); }
        } else Lang.err(Lang.get("invalid"));
    }

    private Comparator<ResearchPaper> pickComparator() {
        System.out.println("Sort by: 1=Date  2=Citations (desc)  3=Pages (desc)");
        System.out.print(Lang.get("choice") + ": ");
        return switch (readInt()) {
            case 2  -> ResearchPaper.byCitations();
            case 3  -> ResearchPaper.byPages();
            default -> ResearchPaper.byDate();
        };
    }
    private void printNumberedPapers(List<ResearchPaper> papers) { for (int i = 0; i < papers.size(); i++) System.out.println((i + 1) + ". " + papers.get(i)); }
    private int readInt() { try { return Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) { return -1; } }
}
