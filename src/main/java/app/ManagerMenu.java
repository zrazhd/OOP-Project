package app;

import academics.Course;
import enums.NewsType;
import enums.School;
import system.Comment;
import system.News;
import system.OfficialMessage;
import users.*;

import java.util.List;
import java.util.Scanner;

/**
 * Console menu for Manager users.
 * Covers: course management, teacher assignments, student registration approval,
 * reports, news, official messages, messaging.
 */
public class ManagerMenu {

    private final Manager manager;
    private final Database db;
    private final Scanner scanner;

    public ManagerMenu(Manager manager, Database db, Scanner scanner) {
        this.manager = manager;
        this.db = db;
        this.scanner = scanner;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("Welcome, " + manager.getFullName() + "!");
            System.out.println("Type: " + manager.getManagerType());
            System.out.println("--------------------");
            System.out.println("1.  Assign lecture teacher to course");
            System.out.println("2.  Assign practice teacher to course");
            System.out.println("3.  Approve student registration for course");
            System.out.println("4.  Generate course report");
            System.out.println("5.  Generate school report");
            System.out.println("6.  View students sorted by GPA");
            System.out.println("7.  View students sorted by name");
            System.out.println("8.  View teachers sorted by rating");
            System.out.println("9.  Create news");
            System.out.println("10. View all news");
            System.out.println("11. Delete news");
            System.out.println("12. Send message to employee");
            System.out.println("13. View inbox");
            System.out.println("14. Send official message");
            System.out.println("0.  Logout");
            System.out.print("Choose: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1"  -> assignLectureTeacher();
                case "2"  -> assignPracticeTeacher();
                case "3"  -> approveStudentRegistration();
                case "4"  -> generateCourseReport();
                case "5"  -> generateSchoolReport();
                case "6"  -> viewStudentsByGpa();
                case "7"  -> viewStudentsByName();
                case "8"  -> viewTeachersByRating();
                case "9"  -> createNews();
                case "10" -> viewAllNews();
                case "11" -> deleteNews();
                case "12" -> sendMessage();
                case "13" -> viewInbox();
                case "14" -> sendOfficialMessage();
                case "0"  -> {
                    System.out.println("Logging out...");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // 1. Assign lecture teacher 

    private void assignLectureTeacher() {
        Course course = pickCourse();
        if (course == null) return;
        Teacher teacher = pickTeacher();
        if (teacher == null) return;
        manager.assignLectureTeacher(course, teacher);
    }

    // 2. Assign practice teacher 

    private void assignPracticeTeacher() {
        Course course = pickCourse();
        if (course == null) return;
        Teacher teacher = pickTeacher();
        if (teacher == null) return;
        manager.assignPracticeTeacher(course, teacher);
    }

    // 3. Approve student registration 

    private void approveStudentRegistration() {
        Student student = pickStudent();
        if (student == null) return;
        Course course = pickCourse();
        if (course == null) return;
        manager.approveStudentRegistration(student, course);
    }

    // 4. Generate course report 

    private void generateCourseReport() {
        Course course = pickCourse();
        if (course == null) return;
        String report = manager.generateCourseReport(course);
        System.out.println(report);
    }

    // 5. Generate school report 

    private void generateSchoolReport() {
        System.out.println("Select school:");
        School[] schools = School.values();
        for (int i = 0; i < schools.length; i++) {
            System.out.println((i + 1) + ". " + schools[i]);
        }
        School school = null;
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < schools.length) school = schools[idx];
        } catch (NumberFormatException ignored) {}

        if (school == null) {
            System.out.println("Invalid selection.");
            return;
        }

        List<Student> allStudents = db.getStudents();
        String report = manager.generateSchoolReport(allStudents, school);
        System.out.println(report);
    }

    // 6. Students sorted by GPA 

    private void viewStudentsByGpa() {
        List<Student> sorted = manager.sortStudentsByGpa(db.getStudents());
        printStudentList("Students by GPA (descending)", sorted);
    }

    // 7. Students sorted by name 

    private void viewStudentsByName() {
        List<Student> sorted = manager.sortStudentsByName(db.getStudents());
        printStudentList("Students by Name", sorted);
    }

    // 8. Teachers sorted by rating 

    private void viewTeachersByRating() {
        List<Teacher> sorted = manager.sortTeachersByRating(db.getTeachers());
        if (sorted.isEmpty()) {
            System.out.println("No teachers found.");
            return;
        }
        System.out.println("\n=== Teachers by Rating (descending) ===");
        for (int i = 0; i < sorted.size(); i++) {
            Teacher t = sorted.get(i);
            System.out.printf("  %d. %-28s  Rating: %.1f%n",
                    i + 1, t.getFullName(), t.getAverageRating());
        }
    }

    // 9. Create news 

    private void createNews() {
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Content: ");
        String content = scanner.nextLine().trim();

        System.out.println("Select news type:");
        NewsType[] types = NewsType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }
        NewsType type = types[0];
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx >= 0 && idx < types.length) type = types[idx];
        } catch (NumberFormatException ignored) {}

        News news = manager.createNews(title, content, type);
        db.addNews(news);
        System.out.println("News created: " + news.getTitle());
    }

    // 10. View all news 

    private void viewAllNews() {
        List<News> newsList = db.getNewsList();
        if (newsList.isEmpty()) {
            System.out.println("No news yet.");
            return;
        }
        System.out.println("\n=== University News ===");
        for (int i = 0; i < newsList.size(); i++) {
            News n = newsList.get(i);
            String display = (i + 1) + ". " + n.toString();
            // Research news highlighted in ANSI yellow
            if (n.isPinned()) {
                display = "\033[1;33m" + display + "\033[0m";
            }
            System.out.println(display);
            if (!n.getComments().isEmpty()) {
                for (system.Comment c : n.getComments()) {
                    System.out.println("     💬 " + c);
                }
            }
        }
    }

    // 11. Delete news 

    private void deleteNews() {
        List<News> newsList = manager.getNewsSorted();
        if (newsList.isEmpty()) {
            System.out.println("No news to delete.");
            return;
        }
        System.out.println("Select news to delete:");
        for (int i = 0; i < newsList.size(); i++) {
            System.out.println((i + 1) + ". " + newsList.get(i).getTitle());
        }
        News news = pickFromList(newsList);
        if (news == null) return;
        manager.deleteNews(news);
        System.out.println("News deleted.");
    }

    // 12. Send message to employee

    private void sendMessage() {
        List<Employee> employees = db.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees.");
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
        manager.sendMessage(receiver, text);
        System.out.println("Message sent to " + receiver.getFullName());
    }

    // 13. View inbox

    private void viewInbox() {
        List<Message> inbox = manager.getInbox();
        if (inbox.isEmpty()) {
            System.out.println("Inbox is empty.");
            return;
        }
        System.out.println("\n=== Inbox (" + inbox.size() + " messages) ===");
        for (Message msg : inbox) {
            System.out.println("  " + msg);
        }
    }

    // 14. Send official message

    private void sendOfficialMessage() {
        System.out.print("Subject: ");
        String subject = scanner.nextLine().trim();
        System.out.print("Body: ");
        String body = scanner.nextLine().trim();

        System.out.println("Send to: 1=Employee  2=Department");
        System.out.print("Choice: ");
        int sendTo = readInt();

        OfficialMessage message;
        if (sendTo == 1) {
            List<Employee> employees = db.getEmployees();
            for (int i = 0; i < employees.size(); i++) {
                System.out.println((i + 1) + ". " + employees.get(i).getFullName());
            }
            Employee recipient = pickFromList(employees);
            if (recipient == null) return;
            message = new OfficialMessage(subject, body, manager, recipient, true, manager.getManagerType().toString());
        } else {
            System.out.print("Department name: ");
            String dept = scanner.nextLine().trim();
            message = new OfficialMessage(subject, body, manager, dept, true, manager.getManagerType().toString());
        }
        System.out.println("Official message sent:\n" + message);
    }

    // Helpers 

    private Course pickCourse() {
        List<Course> courses = db.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses in the system.");
            return null;
        }
        System.out.println("Select course:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
        return pickFromList(courses);
    }

    private Teacher pickTeacher() {
        List<Teacher> teachers = db.getTeachers();
        if (teachers.isEmpty()) {
            System.out.println("No teachers in the system.");
            return null;
        }
        System.out.println("Select teacher:");
        for (int i = 0; i < teachers.size(); i++) {
            System.out.println((i + 1) + ". " + teachers.get(i).getFullName()
                    + " (" + teachers.get(i).getPosition() + ")");
        }
        return pickFromList(teachers);
    }

    private Student pickStudent() {
        List<Student> students = db.getStudents();
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return null;
        }
        System.out.println("Select student:");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i).getFullName());
        }
        return pickFromList(students);
    }

    private void printStudentList(String header, List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n=== " + header + " ===");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("  %d. %-28s  GPA: %.2f%n",
                    i + 1, s.getFullName(), s.calculateGPA());
        }
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