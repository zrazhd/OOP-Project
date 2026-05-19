package app;

import academics.Course;
import enums.DegreeType;
import enums.ManagerType;
import enums.School;
import enums.TeacherPosition;
import system.Lang;
import system.LogEntry;
import users.*;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {

    private final Admin admin;
    private final Database database;
    private final Scanner scanner;

    public AdminMenu(Admin admin, Database database, Scanner scanner) {
        this.admin = admin;
        this.database = database;
        this.scanner = scanner;
    }

    public void show() {
        while (true) {
            Lang.header(Lang.get("admin_menu") + " — " + admin.getFullName());
            Lang.menuItem(1, "admin_users");
            Lang.menuItem(2, "admin_add");
            Lang.menuItem(3, "admin_remove");
            Lang.menuItem(4, "admin_update");
            Lang.menuItem(5, "admin_reset");
            Lang.menuItem(6, "admin_search");
            Lang.menuItem(7, "admin_logs");
            Lang.menuItem(8, "admin_filter");
            Lang.menuItem(9, "admin_courses");
            Lang.menuExit();
            Lang.separator();
            Lang.prompt();

            int choice = readInt();
            switch (choice) {
                case 1 -> viewAllUsers();
                case 2 -> addUser();
                case 3 -> removeUser();
                case 4 -> updateEmail();
                case 5 -> resetPassword();
                case 6 -> searchUser();
                case 7 -> viewGlobalLogs();
                case 8 -> filterLogsByAction();
                case 9 -> viewAllCourses();
                case 0 -> {
                    Lang.info(Lang.get("logout") + "...");
                    return;
                }
                default -> Lang.err(Lang.get("invalid"));
            }
        }
    }

    private void viewAllUsers() {
        List<User> users = database.getUsers();
        if (users.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        System.out.println("\n--- " + Lang.get("admin_users") + " (" + users.size() + ") ---");
        for (User u : users) {
            System.out.println("  " + u);
        }
    }

    private void addUser() {
        System.out.println("\nSelect user type:");
        System.out.println("1. Student\n2. Graduate Student\n3. Teacher\n4. Manager\n5. Tech Support\n6. Admin");
        Lang.prompt();
        int type = readInt();

        System.out.print("ID: "); String id = readLine();
        if (database.findUserById(id) != null) {
            Lang.err("User ID already exists.");
            return;
        }

        System.out.print("First Name: "); String fn = readLine();
        System.out.print("Last Name: "); String ln = readLine();
        System.out.print("Email: "); String email = readLine();
        System.out.print("Password: "); String pass = readLine();

        User newUser = null;
        try {
            switch (type) {
                case 1 -> {
                    System.out.print("School (e.g. IT_AND_ENGINEERING, BUSINESS): ");
                    School school = School.valueOf(readLine().toUpperCase());
                    System.out.print("Year of study (1-4): ");
                    int year = readInt();
                    newUser = new Student(id, fn, ln, email, pass, DegreeType.BACHELOR, school, year);
                }
                case 2 -> {
                    System.out.print("Degree (MASTER, PHD): ");
                    DegreeType degree = DegreeType.valueOf(readLine().toUpperCase());
                    System.out.print("School (e.g. IT_AND_ENGINEERING, BUSINESS): ");
                    School school = School.valueOf(readLine().toUpperCase());
                    System.out.print("Year of study (1-3): ");
                    int year = readInt();
                    newUser = new GraduateStudent(id, fn, ln, email, pass, degree, school, year);
                }
                case 3 -> {
                    System.out.print("Department: ");
                    String dept = readLine();
                    System.out.print("Position (PROFESSOR, SENIOR_LECTURER, LECTURER, TUTOR): ");
                    TeacherPosition pos = TeacherPosition.valueOf(readLine().toUpperCase());
                    System.out.print("School: ");
                    School school = School.valueOf(readLine().toUpperCase());
                    newUser = new Teacher(id, fn, ln, email, pass, dept, pos, school);
                }
                case 4 -> {
                    System.out.print("Department: ");
                    String dept = readLine();
                    System.out.print("Manager Type (OR, DEAN_OFFICE): ");
                    ManagerType mType = ManagerType.valueOf(readLine().toUpperCase());
                    newUser = new Manager(id, fn, ln, email, pass, dept, mType);
                }
                case 5 -> {
                    System.out.print("Department: ");
                    String dept = readLine();
                    System.out.print("Specialization: ");
                    String spec = readLine();
                    newUser = new TechSupportSpecialist(id, fn, ln, email, pass, dept, spec);
                }
                case 6 -> {
                    System.out.print("Department: ");
                    String dept = readLine();
                    newUser = new Admin(id, fn, ln, email, pass, dept);
                }
                default -> { Lang.err("Invalid user type."); return; }
            }
        } catch (IllegalArgumentException e) {
            Lang.err("Invalid enum value provided.");
            return;
        }

        if (database.addUser(newUser)) {
            database.log(admin.getFullName(), "ADD_USER", "Added user " + id + " (" + fn + " " + ln + ")");
            Lang.ok(Lang.get("success"));
        } else {
            Lang.err("Failed to add user.");
        }
    }

    private void removeUser() {
        System.out.print("Enter ID of user to remove: ");
        String userId = readLine();
        User found = database.findUserById(userId);
        if (found == null) {
            Lang.err(Lang.get("not_found"));
            return;
        }
        System.out.print(Lang.get("confirm") + " ");
        if (!readLine().equalsIgnoreCase(Lang.get("yes"))) {
            Lang.info(Lang.get("cancel"));
            return;
        }
        if (database.removeUser(userId)) {
            database.log(admin.getFullName(), "REMOVE_USER", "Removed user " + userId + " (" + found.getFullName() + ")");
            Lang.ok(Lang.get("success"));
        }
    }

    private void updateEmail() {
        System.out.print("Enter User ID: ");
        String userId = readLine();
        User found = database.findUserById(userId);
        if (found == null) {
            Lang.err(Lang.get("not_found"));
            return;
        }
        System.out.print("New Email: ");
        String newEmail = readLine();
        String oldEmail = found.getEmail();
        found.setEmail(newEmail);
        database.log(admin.getFullName(), "UPDATE_EMAIL", "User " + userId + ": " + oldEmail + " -> " + newEmail);
        Lang.ok(Lang.get("success"));
    }

    private void resetPassword() {
        System.out.print("Enter User ID: ");
        String userId = readLine();
        User found = database.findUserById(userId);
        if (found == null) {
            Lang.err(Lang.get("not_found"));
            return;
        }
        System.out.print("New Password: ");
        String newPass = readLine();
        found.setPassword(newPass);
        database.log(admin.getFullName(), "RESET_PASSWORD", "Reset password for " + userId);
        Lang.ok(Lang.get("success"));
    }

    private void searchUser() {
        System.out.print("Enter User ID: ");
        String userId = readLine();
        User found = database.findUserById(userId);
        if (found != null) {
            System.out.println("  " + found);
        } else {
            Lang.err(Lang.get("not_found"));
        }
    }

    private void viewGlobalLogs() {
        List<LogEntry> logs = database.getLogs();
        if (logs.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        System.out.println("\n=== SYSTEM LOGS (" + logs.size() + ") ===");
        for (LogEntry e : logs) {
            System.out.println("  " + e);
        }
    }

    private void filterLogsByAction() {
        System.out.print("Action to filter (e.g. LOGIN, ADD_USER): ");
        String action = readLine().toUpperCase();
        List<LogEntry> logs = database.getLogs();
        long count = 0;
        System.out.println("\n=== SYSTEM LOGS: " + action + " ===");
        for (LogEntry e : logs) {
            if (e.getAction().equalsIgnoreCase(action)) {
                System.out.println("  " + e);
                count++;
            }
        }
        if (count == 0) Lang.info(Lang.get("empty"));
    }

    private void viewAllCourses() {
        List<Course> courses = database.getCourses();
        if (courses.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        System.out.println("\n--- COURSES (" + courses.size() + ") ---");
        for (Course c : courses) {
            System.out.println("  " + c);
        }
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
