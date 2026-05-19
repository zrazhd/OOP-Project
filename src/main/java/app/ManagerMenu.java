package app;

import academics.Course;
import enums.NewsType;
import enums.School;
import system.*;
import users.Employee;
import users.Manager;
import users.Message;
import users.Student;
import users.Teacher;

import java.util.List;
import java.util.Scanner;

public class ManagerMenu {
    private final Manager manager;
    private final Database database;
    private final Scanner scanner;

    public ManagerMenu(Manager manager, Database database, Scanner scanner) {
        this.manager = manager;
        this.database = database;
        this.scanner = scanner;
    }

    public void show() {
        while (true) {
            Lang.header(Lang.get("mgr_menu") + " — " + manager.getFullName() + " (" + manager.getManagerType() + ")");
            Lang.menuItem(1, "mgr_assign_course");
            Lang.menuItem(2, "mgr_approve");
            Lang.menuItem(3, "mgr_report");
            Lang.menuItem(4, "mgr_school_rep");
            Lang.menuItem(5, "mgr_stu_gpa");
            Lang.menuItem(6, "mgr_stu_name");
            Lang.menuItem(7, "mgr_tch_rate");
            Lang.menuItem(8, "mgr_news_create");
            Lang.menuItem(9, "mgr_news_view");
            Lang.menuItem(10, "mgr_send_msg");
            Lang.menuItem(11, "mgr_inbox");
            Lang.menuItem(12, "mgr_official");
            Lang.menuItem(13, "mgr_emp_req");
            Lang.menuExit();
            Lang.separator();
            Lang.prompt();

            int choice = readInt();
            switch (choice) {
                case 1  -> assignCourseToTeacher();
                case 2  -> processRegistrations();
                case 3  -> generateCourseReport();
                case 4  -> generateSchoolReport();
                case 5  -> viewStudentsSortedGpa();
                case 6  -> viewStudentsSortedName();
                case 7  -> viewTeachersSortedRating();
                case 8  -> createNews();
                case 9  -> viewNews();
                case 10 -> sendEmployeeMessage();
                case 11 -> viewInbox();
                case 12 -> sendOfficialMessage();
                case 13 -> viewEmployeeRequests();
                case 0  -> { return; }
                default -> Lang.err(Lang.get("invalid"));
            }
        }
    }

    private void assignCourseToTeacher() {
        List<Course> courses = database.getCourses();
        List<Teacher> teachers = database.getTeachers();
        if (courses.isEmpty() || teachers.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        for (int i = 0; i < courses.size(); i++) System.out.println((i + 1) + ". " + courses.get(i).getCourseId() + " - " + courses.get(i).getName());
        System.out.print(Lang.get("select") + " Course: ");
        int cIdx = readInt() - 1;
        if (cIdx < 0 || cIdx >= courses.size()) { Lang.err(Lang.get("invalid")); return; }
        
        for (int i = 0; i < teachers.size(); i++) System.out.println((i + 1) + ". " + teachers.get(i).getFullName() + " (" + teachers.get(i).getPosition() + ")");
        System.out.print(Lang.get("select") + " Teacher: ");
        int tIdx = readInt() - 1;
        if (tIdx < 0 || tIdx >= teachers.size()) { Lang.err(Lang.get("invalid")); return; }
        
        System.out.print("Type (1 = LECTURE, 2 = PRACTICE): ");
        int type = readInt();
        if (type == 1) {
            manager.assignLectureTeacher(courses.get(cIdx), teachers.get(tIdx));
            database.log(manager.getFullName(), "ASSIGN_COURSE", "Assigned LECTURE " + courses.get(cIdx).getCourseId() + " to " + teachers.get(tIdx).getFullName());
        } else {
            manager.assignPracticeTeacher(courses.get(cIdx), teachers.get(tIdx));
            database.log(manager.getFullName(), "ASSIGN_COURSE", "Assigned PRACTICE " + courses.get(cIdx).getCourseId() + " to " + teachers.get(tIdx).getFullName());
        }
        Lang.ok(Lang.get("success"));
    }

    private void processRegistrations() {
        List<RegistrationRequest> reqs = database.getRegistrationRequests();
        boolean found = false;
        for (RegistrationRequest r : reqs) {
            if (r.isPending()) {
                System.out.println("  " + r);
                System.out.print("Approve? (yes/no/skip): ");
                String ans = scanner.nextLine().trim().toLowerCase();
                if (ans.equals("yes")) {
                    try {
                        r.getStudent().registerForCourse(r.getCourse());
                        r.approve("Approved by " + manager.getFullName());
                        database.log(manager.getFullName(), "APPROVE_REG", "Approved " + r.getStudent().getFullName() + " for " + r.getCourse().getCourseId());
                        Lang.ok(Lang.get("success"));
                    } catch (Exception e) {
                        r.reject(e.getMessage());
                        Lang.err("Failed: " + e.getMessage());
                    }
                } else if (ans.equals("no")) {
                    System.out.print("Reason: ");
                    r.reject(scanner.nextLine().trim());
                    database.log(manager.getFullName(), "REJECT_REG", "Rejected " + r.getStudent().getFullName() + " for " + r.getCourse().getCourseId());
                    Lang.ok("Rejected");
                }
                found = true;
            }
        }
        if (!found) Lang.info(Lang.get("empty"));
    }

    private void generateCourseReport() {
        List<Course> courses = database.getCourses();
        for (int i = 0; i < courses.size(); i++) System.out.println((i + 1) + ". " + courses.get(i).getName());
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < courses.size()) {
            System.out.println(manager.generateCourseReport(courses.get(idx)));
            database.log(manager.getFullName(), "REPORT", "Generated Course Report for " + courses.get(idx).getCourseId());
        } else Lang.err(Lang.get("invalid"));
    }

    private void generateSchoolReport() {
        System.out.print("School (e.g. IT_AND_ENGINEERING, BUSINESS): ");
        try {
            School s = School.valueOf(scanner.nextLine().trim().toUpperCase());
            System.out.println(manager.generateSchoolReport(database.getStudents(), s));
            database.log(manager.getFullName(), "REPORT", "Generated School Report for " + s);
        } catch (Exception e) { Lang.err("Invalid school."); }
    }

    private void viewStudentsSortedGpa() {
        List<Student> students = manager.sortStudentsByGpa(database.getStudents());
        students.forEach(s -> System.out.printf("  %-25s | GPA: %.2f%n", s.getFullName(), s.calculateGPA()));
    }

    private void viewStudentsSortedName() {
        List<Student> students = manager.sortStudentsByName(database.getStudents());
        students.forEach(s -> System.out.printf("  %-25s | GPA: %.2f%n", s.getFullName(), s.calculateGPA()));
    }

    private void viewTeachersSortedRating() {
        List<Teacher> teachers = manager.sortTeachersByRating(database.getTeachers());
        teachers.forEach(t -> System.out.printf("  %-25s | Rating: %.1f%n", t.getFullName(), t.getAverageRating()));
    }

    private void createNews() {
        System.out.print(Lang.get("title") + ": "); String t = scanner.nextLine().trim();
        System.out.print(Lang.get("content") + ": "); String c = scanner.nextLine().trim();
        System.out.print("Type (GENERAL, RESEARCH, EVENT, ANNOUNCEMENT): ");
        try {
            NewsType type = NewsType.valueOf(scanner.nextLine().trim().toUpperCase());
            database.addNews(manager.createNews(t, c, type));
            database.log(manager.getFullName(), "CREATE_NEWS", "Created news: " + t);
            Lang.ok(Lang.get("success"));
        } catch (Exception e) { Lang.err("Invalid news type"); }
    }

    private void viewNews() {
        List<News> news = manager.getNewsSorted(); // wait, News belongs to Database now
        if (database.getNewsList().isEmpty()) { Lang.info(Lang.get("empty")); return; }
        for (News n : database.getNewsList()) {
            System.out.println("  " + n.toString());
        }
    }

    private void sendEmployeeMessage() {
        List<Employee> emps = database.getEmployees();
        for (int i = 0; i < emps.size(); i++) System.out.println((i + 1) + ". " + emps.get(i).getFullName());
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < emps.size()) {
            System.out.print(Lang.get("message") + ": ");
            manager.sendMessage(emps.get(idx), scanner.nextLine().trim());
            database.log(manager.getFullName(), "SEND_MSG", "Sent message to " + emps.get(idx).getFullName());
            Lang.ok(Lang.get("sent"));
        } else Lang.err(Lang.get("invalid"));
    }

    private void viewInbox() {
        List<Message> inbox = manager.getInbox();
        if (inbox.isEmpty()) Lang.info(Lang.get("empty"));
        else inbox.forEach(m -> System.out.println("  " + m));
    }

    private void sendOfficialMessage() {
        System.out.print("To (1 = Person, 2 = Department): ");
        int t = readInt();
        System.out.print("Subject: "); String subj = scanner.nextLine();
        System.out.print("Body: "); String body = scanner.nextLine();
        System.out.print("Signatory Title (e.g. Dean): "); String sig = scanner.nextLine();
        
        OfficialMessage om;
        if (t == 1) {
            System.out.print("Employee ID: ");
            users.User u = database.findUserById(scanner.nextLine().trim());
            if (u instanceof Employee e) om = new OfficialMessage(subj, body, manager, e, true, sig);
            else { Lang.err(Lang.get("not_found")); return; }
        } else {
            System.out.print("Department Name: ");
            om = new OfficialMessage(subj, body, manager, scanner.nextLine().trim(), true, sig);
        }
        System.out.println("\n" + om);
        database.log(manager.getFullName(), "OFFICIAL_MSG", "Sent official message: " + subj);
        Lang.ok(Lang.get("sent"));
    }

    private void viewEmployeeRequests() {
        List<EmployeeRequest> reqs = database.getEmployeeRequests();
        if (reqs.isEmpty()) { Lang.info(Lang.get("empty")); return; }
        for (EmployeeRequest r : reqs) {
            System.out.println("  " + r);
        }
    }

    private int readInt() {
        try { return Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) { return -1; }
    }
}