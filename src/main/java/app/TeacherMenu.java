package app;

import academics.Course;
import enums.CitationFormat;
import enums.UrgencyLevel;
import research.ResearchPaper;
import system.Comment;
import system.News;
import users.*;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for Teacher users.
 * Covers: viewing courses, students, putting marks, sending complaints,
 * sending messages, viewing/commenting news.
 */
public class TeacherMenu {

    private final Teacher teacher;
    private final Database db;
    private final Scanner scanner;

    public TeacherMenu(Teacher teacher, Database db, Scanner scanner) {
        this.teacher = teacher;
        this.db = db;
        this.scanner = scanner;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Teacher Menu ===");
            System.out.println("Welcome, " + teacher.getFullName() + "!");
            System.out.println("Position: " + teacher.getPosition() + " | School: " + teacher.getSchool());
            System.out.println("Rating: " + String.format("%.1f", teacher.getAverageRating()));
            System.out.println("--------------------");
            System.out.println("1.  View my courses");
            System.out.println("2.  View students in a course");
            System.out.println("3.  Put marks for a student");
            System.out.println("4.  Send complaint about a student");
            System.out.println("5.  Send message to an employee");
            System.out.println("6.  View inbox");
            System.out.println("7.  View news");
            System.out.println("8.  Add comment to news");
            System.out.println("0.  Back");
            System.out.print("Choose: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1"  -> viewCourses();
                case "2"  -> viewStudentsInCourse();
                case "3"  -> putMarks();
                case "4"  -> sendComplaint();
                case "5"  -> sendMessage();
                case "6"  -> viewInbox();
                case "7"  -> viewNews();
                case "8"  -> commentOnNews();
                case "0"  -> {
                    System.out.println("Returning...");
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
            Student s = students.get(i);
            System.out.printf("  %d. %-28s GPA: %.2f%n", i + 1, s.getFullName(), s.calculateGPA());
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
        db.addComplaint(complaint);
        System.out.println("Complaint submitted: " + complaint);
    }

    // ── 5. Send message to employee ───────────────────────────────────────────

    private void sendMessage() {
        List<Employee> employees = db.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees in the system.");
            return;
        }
        System.out.println("\n--- Employees ---");
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            System.out.println((i + 1) + ". " + e.getFullName() + " (" + e.getClass().getSimpleName() + ")");
        }
        Employee receiver = pickFromList(employees);
        if (receiver == null) return;

        System.out.print("Message: ");
        String text = scanner.nextLine().trim();
        teacher.sendMessage(receiver, text);
        System.out.println("Message sent to " + receiver.getFullName());
    }

    // ── 6. View inbox ─────────────────────────────────────────────────────────

    private void viewInbox() {
        List<Message> inbox = teacher.getInbox();
        if (inbox.isEmpty()) {
            System.out.println("Inbox is empty.");
            return;
        }
        System.out.println("\n=== Inbox (" + inbox.size() + " messages) ===");
        for (Message msg : inbox) {
            System.out.println("  " + msg);
        }
    }

    // ── 7. View news ──────────────────────────────────────────────────────────

    private void viewNews() {
        List<News> news = db.getNewsList();
        if (news.isEmpty()) {
            System.out.println("No news.");
            return;
        }
        System.out.println("\n=== University News ===");
        for (int i = 0; i < news.size(); i++) {
            News n = news.get(i);
            String display = (i + 1) + ". " + n.toString();
            // Research news highlighted in color (ANSI yellow)
            if (n.isPinned()) {
                display = "\033[1;33m" + display + "\033[0m";
            }
            System.out.println(display);
            // Show comments
            if (!n.getComments().isEmpty()) {
                for (Comment c : n.getComments()) {
                    System.out.println("     💬 " + c);
                }
            }
        }
    }

    // ── 8. Comment on news ────────────────────────────────────────────────────

    private void commentOnNews() {
        List<News> news = db.getNewsList();
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
        news.get(idx).addComment(new Comment(teacher, text));
        System.out.println("Comment added.");
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

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}