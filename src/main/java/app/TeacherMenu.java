package app;

import academics.Course;
import enums.UrgencyLevel;
import system.Comment;
import system.Lang;
import system.News;
import users.Employee;
import users.Message;
import users.Student;
import users.Teacher;

import java.util.List;
import java.util.Scanner;

public class TeacherMenu {
    private final Teacher teacher;
    private final Database database;
    private final Scanner scanner;

    public TeacherMenu(Teacher teacher, Database database, Scanner scanner) {
        this.teacher = teacher;
        this.database = database;
        this.scanner = scanner;
    }

    public void show() {
        while (true) {
            Lang.header(Lang.get("tch_menu") + " — " + teacher.getFullName());
            Lang.menuItem(1, "tch_courses");
            Lang.menuItem(2, "tch_students");
            Lang.menuItem(3, "tch_marks");
            Lang.menuItem(4, "tch_complaint");
            Lang.menuItem(5, "tch_send_msg");
            Lang.menuItem(6, "tch_inbox");
            Lang.menuItem(7, "tch_news");
            Lang.menuItem(8, "tch_comment");
            if (teacher.isResearcher()) {
                System.out.println("\n  --- Researcher Options ---");
                Lang.menuItem(9, "res_menu", true);
            }
            Lang.menuExit();
            Lang.separator();
            Lang.prompt();

            int choice = readInt();
            switch (choice) {
                case 1 -> viewMyCourses();
                case 2 -> viewStudents();
                case 3 -> putMarks();
                case 4 -> sendComplaint();
                case 5 -> sendEmployeeMessage();
                case 6 -> viewInbox();
                case 7 -> viewNews();
                case 8 -> commentOnNews();
                case 9 -> { if (teacher.isResearcher()) new ResearcherMenu(teacher, database, scanner).show(); else Lang.err(Lang.get("invalid")); }
                case 0 -> { return; }
                default -> Lang.err(Lang.get("invalid"));
            }
        }
    }

    private void viewMyCourses() {
        List<Course> courses = teacher.getCourses();
        if (courses.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        System.out.println("\n--- " + Lang.get("tch_courses") + " ---");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }
    }

    private void viewStudents() {
        Course c = pickCourse();
        if (c != null) {
            List<Student> students = c.getEnrolledStudents();
            if (students.isEmpty()) {
                Lang.info(Lang.get("empty"));
            } else {
                students.forEach(s -> System.out.println("  " + s.getFullName() + " - " + s.getUserId()));
            }
        }
    }

    private void putMarks() {
        Course c = pickCourse();
        if (c == null) return;
        List<Student> students = c.getEnrolledStudents();
        if (students.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getFullName());
        }
        System.out.print(Lang.get("select") + ": ");
        int sIdx = readInt() - 1;
        if (sIdx >= 0 && sIdx < students.size()) {
            Student s = students.get(sIdx);
            System.out.print("Attestation 1 (0-30): "); double a1 = readDouble();
            System.out.print("Attestation 2 (0-30): "); double a2 = readDouble();
            System.out.print("Final Exam (0-40): ");    double fe = readDouble();
            teacher.putMark(s, c, a1, a2, fe);
            database.log(teacher.getFullName(), "PUT_MARK", "Rated " + s.getFullName() + " in " + c.getCourseId());
            Lang.ok(Lang.get("success"));
        } else {
            Lang.err(Lang.get("invalid"));
        }
    }

    private void sendComplaint() {
        System.out.print("Student ID: ");
        String sid = scanner.nextLine().trim();
        users.User u = database.findUserById(sid);
        if (u instanceof Student s) {
            System.out.print("Urgency (LOW, MEDIUM, HIGH): ");
            try {
                UrgencyLevel urg = UrgencyLevel.valueOf(scanner.nextLine().trim().toUpperCase());
                System.out.print("Reason: ");
                String reason = scanner.nextLine().trim();
                teacher.sendComplaint(s, urg, reason);
                database.log(teacher.getFullName(), "COMPLAINT", "Filed complaint against " + s.getFullName() + " [" + urg + "]");
                Lang.ok(Lang.get("success"));
            } catch (Exception e) {
                Lang.err("Invalid urgency level");
            }
        } else {
            Lang.err(Lang.get("not_found"));
        }
    }

    private void sendEmployeeMessage() {
        List<Employee> emps = database.getEmployees();
        for (int i = 0; i < emps.size(); i++) {
            System.out.println((i + 1) + ". " + emps.get(i).getFullName() + " (" + emps.get(i).getClass().getSimpleName() + ")");
        }
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < emps.size()) {
            System.out.print(Lang.get("message") + ": ");
            String text = scanner.nextLine().trim();
            teacher.sendMessage(emps.get(idx), text);
            database.log(teacher.getFullName(), "SEND_MSG", "Sent message to " + emps.get(idx).getFullName());
            Lang.ok(Lang.get("sent"));
        } else {
            Lang.err(Lang.get("invalid"));
        }
    }

    private void viewInbox() {
        List<Message> inbox = teacher.getInbox();
        if (inbox.isEmpty()) Lang.info(Lang.get("empty"));
        else inbox.forEach(m -> System.out.println("  " + m));
    }

    private void viewNews() {
        List<News> news = database.getNewsList();
        if (news.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        System.out.println("\n=== UNIVERSITY NEWS ===");
        for (int i = 0; i < news.size(); i++) {
            News n = news.get(i);
            String display = (i + 1) + ". " + n.toString() + "\n  " + n.getContent();
            if (n.isPinned()) display = "\033[1;33m" + display + "\033[0m";
            System.out.println(display);
        }
    }

    private void commentOnNews() {
        viewNews();
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        List<News> news = database.getNewsList();
        if (idx >= 0 && idx < news.size()) {
            System.out.print(Lang.get("message") + ": ");
            String text = scanner.nextLine().trim();
            news.get(idx).addComment(new Comment(teacher, text));
            database.log(teacher.getFullName(), "COMMENT_NEWS", "Commented on news #" + news.get(idx).getNewsId());
            Lang.ok(Lang.get("success"));
        } else {
            Lang.err(Lang.get("invalid"));
        }
    }

    private Course pickCourse() {
        List<Course> courses = teacher.getCourses();
        if (courses.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return null;
        }
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getName());
        }
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < courses.size()) return courses.get(idx);
        Lang.err(Lang.get("invalid"));
        return null;
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) { return -1; }
    }
    private double readDouble() {
        try { return Double.parseDouble(scanner.nextLine().trim()); } catch (Exception e) { return -1.0; }
    }
}