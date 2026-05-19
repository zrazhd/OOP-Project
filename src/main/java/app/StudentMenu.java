package app;

import academics.Course;
import research.ResearchJournal;
import research.ResearchPaper;
import research.ResearchProject;
import system.Comment;
import system.News;
import users.GraduateStudent;
import users.Student;
import users.Teacher;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for Student users (and GraduateStudent).
 * Covers: courses, marks, transcript, teacher rating, news, journal subscriptions.
 */
public class StudentMenu {
    private final Student student;
    private final Database database;
    private final Scanner scanner;

    public StudentMenu(Student student, Database database, Scanner scanner) {
        this.student = student;
        this.database = database;
        this.scanner = scanner;
    }

    public void show() {
        while (true) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1.  View my courses");
            System.out.println("2.  Register for course");
            System.out.println("3.  Drop course");
            System.out.println("4.  View marks");
            System.out.println("5.  View transcript");
            System.out.println("6.  Rate teacher");
            System.out.println("7.  View teacher info");
            System.out.println("8.  View news");
            System.out.println("9.  Comment on news");
            System.out.println("10. Subscribe to research journal");
            System.out.println("11. View research journals");
            if (student instanceof GraduateStudent) {
                System.out.println("--- Graduate Student ---");
                System.out.println("12. Add research paper");
                System.out.println("13. View research papers");
                System.out.println("14. Join research project");
                System.out.println("15. View h-index");
                System.out.println("16. Set supervisor");
                System.out.println("17. Add diploma project");
                System.out.println("18. View diploma projects");
            }
            System.out.println("0.  Back");
            System.out.print("Choice: ");

            int choice = readInt();
            switch (choice) {
                case 1  -> viewMyCourses();
                case 2  -> registerForCourse();
                case 3  -> dropCourse();
                case 4  -> viewMarks();
                case 5  -> System.out.println(student.getTranscript());
                case 6  -> rateTeacher();
                case 7  -> viewTeacherInfo();
                case 8  -> viewNews();
                case 9  -> commentOnNews();
                case 10 -> subscribeToJournal();
                case 11 -> viewJournals();
                case 12 -> { if (student instanceof GraduateStudent gs) addResearchPaper(gs); else invalid(); }
                case 13 -> { if (student instanceof GraduateStudent gs) viewResearchPapers(gs); else invalid(); }
                case 14 -> { if (student instanceof GraduateStudent gs) joinResearchProject(gs); else invalid(); }
                case 15 -> { if (student instanceof GraduateStudent gs) System.out.println("h-index = " + gs.calculateHIndex()); else invalid(); }
                case 16 -> { if (student instanceof GraduateStudent gs) setSupervisor(gs); else invalid(); }
                case 17 -> { if (student instanceof GraduateStudent gs) addDiplomaProject(gs); else invalid(); }
                case 18 -> { if (student instanceof GraduateStudent gs) viewDiplomaProjects(gs); else invalid(); }
                case 0  -> { System.out.println("Returning..."); return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void viewMyCourses() {
        List<Course> courses = student.viewCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses yet.");
            return;
        }
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    private void registerForCourse() {
        for (Course course : database.getCourses()) {
            System.out.println(course.getCourseId() + " - " + course.getName() + " (" + course.getCourseType() + ", " + course.getSchool() + ")");
        }
        System.out.print("Enter course id: ");
        Course selected = database.findCourseById(scanner.nextLine().trim());
        if (selected == null) {
            System.out.println("Course not found.");
            return;
        }
        try {
            student.registerForCourse(selected);
            System.out.println("Registered successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void dropCourse() {
        List<Course> courses = student.viewCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses to drop.");
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
        System.out.print("Select course to drop: ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= courses.size()) {
            System.out.println("Invalid.");
            return;
        }
        student.dropCourse(courses.get(idx));
        System.out.println("Course dropped.");
    }

    private void viewMarks() {
        if (student.viewAllMarks().isEmpty()) {
            System.out.println("No marks yet.");
            return;
        }
        student.viewAllMarks().forEach((course, mark) ->
                System.out.println(course.getCourseId() + " " + course.getName()
                        + " -> " + mark.getTotal() + " (" + mark.getGradeLetter() + ")"));
    }

    private void rateTeacher() {
        List<Teacher> teachers = database.getTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            Teacher teacher = teachers.get(i);
            System.out.println((i + 1) + ". " + teacher.getFullName() + " (" + teacher.getPosition() + ") Rating: " + String.format("%.1f", teacher.getAverageRating()));
        }
        System.out.print("Choose teacher number: ");
        int index = readInt();
        if (index < 1 || index > teachers.size()) {
            System.out.println("Invalid teacher.");
            return;
        }
        System.out.print("Rating 1-10: ");
        int rating = readInt();
        try {
            student.rateTeacher(teachers.get(index - 1), rating);
            System.out.println("Rating submitted.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewTeacherInfo() {
        for (Course course : student.viewCourses()) {
            System.out.println(student.viewTeacherInfo(course));
        }
    }

    // ── News ──────────────────────────────────────────────────────────────────

    private void viewNews() {
        List<News> news = database.getNewsList();
        if (news.isEmpty()) {
            System.out.println("No news.");
            return;
        }
        System.out.println("\n=== University News ===");
        for (int i = 0; i < news.size(); i++) {
            News n = news.get(i);
            String display = (i + 1) + ". " + n.toString();
            // Research news highlighted in ANSI yellow
            if (n.isPinned()) {
                display = "\033[1;33m" + display + "\033[0m";
            }
            System.out.println(display);
            if (!n.getComments().isEmpty()) {
                for (Comment c : n.getComments()) {
                    System.out.println("     💬 " + c);
                }
            }
        }
    }

    private void commentOnNews() {
        List<News> news = database.getNewsList();
        if (news.isEmpty()) {
            System.out.println("No news to comment on.");
            return;
        }
        for (int i = 0; i < news.size(); i++) {
            System.out.println((i + 1) + ". " + news.get(i).getTitle());
        }
        System.out.print("Select news number: ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= news.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        System.out.print("Your comment: ");
        String text = scanner.nextLine().trim();
        news.get(idx).addComment(new Comment(student, text));
        System.out.println("Comment added.");
    }

    // ── Journal subscriptions (Observer pattern) ──────────────────────────────

    private void subscribeToJournal() {
        List<ResearchJournal> journals = database.getJournals();
        if (journals.isEmpty()) {
            System.out.println("No research journals available.");
            return;
        }
        for (int i = 0; i < journals.size(); i++) {
            System.out.println((i + 1) + ". " + journals.get(i));
        }
        System.out.print("Subscribe to journal number (or 0 to cancel): ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= journals.size()) {
            System.out.println("Cancelled.");
            return;
        }
        journals.get(idx).subscribe(student);
    }

    private void viewJournals() {
        List<ResearchJournal> journals = database.getJournals();
        if (journals.isEmpty()) {
            System.out.println("No research journals.");
            return;
        }
        System.out.println("\n=== Research Journals ===");
        for (ResearchJournal j : journals) {
            System.out.println("  " + j);
        }
    }

    // ── Graduate Student features ─────────────────────────────────────────────

    private void addResearchPaper(GraduateStudent gs) {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Journal: ");
        String journal = scanner.nextLine();
        System.out.print("Pages: ");
        int pages = readInt();
        System.out.print("Year: ");
        int year = readInt();
        System.out.print("Citations: ");
        int citations = readInt();
        gs.addResearchPaper(new ResearchPaper(
                title,
                List.of(gs.getFullName()),
                journal,
                pages,
                LocalDate.of(year, 1, 1),
                title.toLowerCase().replace(' ', '-') + "-" + year,
                citations
        ));
        System.out.println("Paper added.");
    }

    private void viewResearchPapers(GraduateStudent gs) {
        if (gs.getResearchPapers().isEmpty()) {
            System.out.println("No papers yet.");
            return;
        }
        gs.getResearchPapers().stream()
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);
    }

    private void joinResearchProject(GraduateStudent gs) {
        System.out.print("Project topic: ");
        String topic = scanner.nextLine();
        gs.joinResearchProject(new ResearchProject(topic));
        System.out.println("Joined project.");
    }

    private void setSupervisor(GraduateStudent gs) {
        List<Teacher> teachers = database.getTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            Teacher teacher = teachers.get(i);
            System.out.println((i + 1) + ". " + teacher.getFullName()
                    + " (h-index: " + teacher.calculateHIndex()
                    + ", " + teacher.getPosition() + ")");
        }
        System.out.print("Choose supervisor: ");
        int index = readInt();
        if (index < 1 || index > teachers.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        try {
            gs.setSupervisor(teachers.get(index - 1));
            System.out.println("Supervisor set: " + teachers.get(index - 1).getFullName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addDiplomaProject(GraduateStudent gs) {
        System.out.print("Diploma project title: ");
        String title = scanner.nextLine();
        gs.addDiplomaProject(new ResearchPaper(
                title,
                List.of(gs.getFullName()),
                "Diploma Project",
                1,
                LocalDate.now(),
                title.toLowerCase().replace(' ', '-') + "-diploma",
                0
        ));
        System.out.println("Diploma project added.");
    }

    private void viewDiplomaProjects(GraduateStudent gs) {
        List<ResearchPaper> diplomas = gs.getDiplomaProjects();
        if (diplomas.isEmpty()) {
            System.out.println("No diploma projects yet.");
            return;
        }
        System.out.println("\n--- Diploma Projects ---");
        diplomas.forEach(System.out::println);
    }

    private void invalid() {
        System.out.println("This option is available only for Graduate Students.");
    }

    private int readInt() {
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}