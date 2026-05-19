package app;

import academics.Course;
import enums.UrgencyLevel;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import research.NotResearcherException;
import research.ResearchPaper;
import research.ResearchProject;
import users.Complaint;
import users.Student;
import users.Teacher;

public class TeacherMenu {

    private final Teacher teacher;
    private final Database db;
    private final Scanner scanner;

    public TeacherMenu(Teacher teacher) {
        this.teacher = teacher;
        this.db = Database.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Teacher Menu ===");
            System.out.println("Welcome, " + teacher.getFullName() + "!");
            System.out.println("Position: " + teacher.getPosition() + " | School: " + teacher.getSchool());
            System.out.println("Rating: " + String.format("%.1f", teacher.getAverageRating()));
            System.out.println("--------------------");
            System.out.println("1. View my courses");
            System.out.println("2. View students in a course");
            System.out.println("3. Put marks for a student");
            System.out.println("4. Send complaint about a student");

            if (teacher.isResearcher()) {
                System.out.println("--- Research ---");
                System.out.println("5. Add research paper");
                System.out.println("6. View my research papers");
                System.out.println("7. Join research project");
                System.out.println("8. View my h-index");
                System.out.println("9. Print papers sorted by citations");
            }

            System.out.println("0. Logout");
            System.out.print("Choose: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> viewCourses();
                case "2" -> viewStudentsInCourse();
                case "3" -> putMarks();
                case "4" -> sendComplaint();
                case "5" -> { if (teacher.isResearcher()) addResearchPaper(); else unknown(); }
                case "6" -> { if (teacher.isResearcher()) viewResearchPapers(); else unknown(); }
                case "7" -> { if (teacher.isResearcher()) joinResearchProject(); else unknown(); }
                case "8" -> { if (teacher.isResearcher()) viewHIndex(); else unknown(); }
                case "9" -> { if (teacher.isResearcher()) printPapersSorted(); else unknown(); }
                case "0" -> {
                    System.out.println("Logging out...");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ── 1. View my courses ────────────────────────────────────────────────────

    private void viewCourses() {
        List<Course> courses = teacher.viewCourses();
        if (courses.isEmpty()) {
            System.out.println("You have no assigned courses.");
            return;
        }
        System.out.println("\n=== Your Courses ===");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
    }

    // ── 2. View students in a course ─────────────────────────────────────────

    private void viewStudentsInCourse() {
        Course course = pickCourse();
        if (course == null) return;

        List<Student> students = teacher.viewStudents(course);
        if (students.isEmpty()) {
            System.out.println("No students enrolled in " + course.getName());
            return;
        }
        System.out.println("\n=== Students in " + course.getName() + " ===");
        for (int i = 0; i < students.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, students.get(i).getFullName());
        }
    }

    // ── 3. Put marks ──────────────────────────────────────────────────────────

    private void putMarks() {
        Course course = pickCourse();
        if (course == null) return;

        List<Student> students = teacher.viewStudents(course);
        if (students.isEmpty()) {
            System.out.println("No students in this course.");
            return;
        }

        System.out.println("Select student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getFullName());
        }
        Student student = pickFromList(students);
        if (student == null) return;

        try {
            System.out.print("Attestation 1 (0-30): ");
            double att1 = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Attestation 2 (0-30): ");
            double att2 = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Final exam (0-40): ");
            double finalExam = Double.parseDouble(scanner.nextLine().trim());

            teacher.putMark(student, course, att1, att2, finalExam);
            System.out.println("Marks saved for " + student.getFullName());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numbers only.");
        }
    }

    // ── 4. Send complaint ─────────────────────────────────────────────────────

    private void sendComplaint() {
        List<Student> allStudents = db.getStudents();
        if (allStudents.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }

        System.out.println("Select student to complain about:");
        for (int i = 0; i < allStudents.size(); i++) {
            System.out.println((i + 1) + ". " + allStudents.get(i).getFullName());
        }
        Student student = pickFromList(allStudents);
        if (student == null) return;

        System.out.println("Select urgency level:");
        UrgencyLevel[] levels = UrgencyLevel.values();
        for (int i = 0; i < levels.length; i++) {
            System.out.println((i + 1) + ". " + levels[i]);
        }
        UrgencyLevel urgency = null;
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < levels.length) {
                urgency = levels[idx];
            }
        } catch (NumberFormatException ignored) {}

        if (urgency == null) {
            System.out.println("Invalid urgency level.");
            return;
        }

        System.out.print("Reason: ");
        String reason = scanner.nextLine().trim();

        Complaint complaint = teacher.sendComplaint(student, urgency, reason);
        System.out.println("Complaint submitted: " + complaint);
    }

    // ── 5. Add research paper ─────────────────────────────────────────────────

    private void addResearchPaper() {
        System.out.print("Paper title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Authors (comma-separated): ");
        String[] authorsArr = scanner.nextLine().trim().split(",");
        List<String> authors = new java.util.ArrayList<>();
        for (String a : authorsArr) authors.add(a.trim());

        System.out.print("Journal name: ");
        String journal = scanner.nextLine().trim();

        System.out.print("Pages (number): ");
        int pages = 0;
        try {
            pages = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, defaulting to 0.");
        }

        System.out.print("Date published (YYYY-MM-DD): ");
        java.time.LocalDate date = java.time.LocalDate.now();
        try {
            date = java.time.LocalDate.parse(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid date, using today.");
        }

        System.out.print("DOI (or leave blank): ");
        String doi = scanner.nextLine().trim();

        System.out.print("Citations: ");
        int citations = 0;
        try {
            citations = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, defaulting to 0.");
        }

        ResearchPaper paper = new ResearchPaper(title, authors, journal, pages, date, doi, citations);
        teacher.addResearchPaper(paper);
        System.out.println("Research paper added: " + title);
    }

    // ── 6. View research papers ───────────────────────────────────────────────

    private void viewResearchPapers() {
        List<ResearchPaper> papers = teacher.getResearchPapers();
        if (papers.isEmpty()) {
            System.out.println("You have no research papers yet.");
            return;
        }
        System.out.println("\n=== Your Research Papers ===");
        for (int i = 0; i < papers.size(); i++) {
            System.out.println((i + 1) + ". " + papers.get(i));
        }
    }

    // ── 7. Join research project ──────────────────────────────────────────────

    private void joinResearchProject() {
        System.out.print("Enter research project name to join: ");
        String projectName = scanner.nextLine().trim();
        // In a real system you'd pick from a list in the DB;
        // here we create a stub project and let the teacher join it.
        ResearchProject project = new ResearchProject(projectName);
        try {
            teacher.joinResearchProject(project);
            System.out.println("Joined project: " + projectName);
        } catch (NotResearcherException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ── 8. View h-index ───────────────────────────────────────────────────────

    private void viewHIndex() {
        int h = teacher.calculateHIndex();
        System.out.println("Your h-index: " + h);
    }

    // ── 9. Print papers sorted by citations ───────────────────────────────────

    private void printPapersSorted() {
        teacher.printPapers(Comparator.comparingInt(ResearchPaper::getCitations).reversed());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Course pickCourse() {
        List<Course> courses = teacher.viewCourses();
        if (courses.isEmpty()) {
            System.out.println("You have no assigned courses.");
            return null;
        }
        System.out.println("Select course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
        return pickFromList(courses);
    }

    private <T> T pickFromList(List<T> list) {
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < list.size()) return list.get(idx);
        } catch (NumberFormatException ignored) {}
        System.out.println("Invalid selection.");
        return null;
    }

    private void unknown() {
        System.out.println("Option not available for your role.");
    }
}