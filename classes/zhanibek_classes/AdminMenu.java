package app;

import enums.DegreeType;
import enums.School;
import enums.TeacherPosition;
import system.LogEntry;
import users.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console menu for Admin users.
 * Provides: user management (add/remove/update), log viewing.
 */
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
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. View all users");
            System.out.println("2. Add user");
            System.out.println("3. Remove user");
            System.out.println("4. Update user email");
            System.out.println("5. Reset user password");
            System.out.println("6. Search user by ID");
            System.out.println("7. View system logs");
            System.out.println("8. Filter logs by action");
            System.out.println("0. Logout");
            System.out.println("=================================");
            System.out.print("Choice: ");

            switch (readInt()) {
                case 1 -> viewAllUsers();
                case 2 -> addUser();
                case 3 -> removeUser();
                case 4 -> updateEmail();
                case 5 -> resetPassword();
                case 6 -> searchUser();
                case 7 -> admin.printAllLogs();
                case 8 -> filterLogs();
                case 0 -> {
                    admin.log("LOGOUT", admin.getFullName() + " logged out");
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ─── 1. View all users ──────────────────────────────────────────────────────

    private void viewAllUsers() {
        List<User> users = new ArrayList<>(database.getUsers());
        if (users.isEmpty()) {
            System.out.println("No users in the system.");
            return;
        }
        System.out.println("\n--- All Users (" + users.size() + ") ---");
        int i = 1;
        for (User u : users) {
            System.out.println(i++ + ". " + u);
        }
    }

    // ─── 2. Add user ─────────────────────────────────────────────────────────────

    private void addUser() {
        System.out.println("\n--- Add User ---");
        System.out.println("Select type:");
        System.out.println("1. Student");
        System.out.println("2. Teacher");
        System.out.println("3. Admin");
        System.out.print("Choice: ");
        int type = readInt();

        System.out.print("User ID: ");
        String id = readLine();
        System.out.print("First name: ");
        String first = readLine();
        System.out.print("Last name: ");
        String last = readLine();
        System.out.print("Email: ");
        String email = readLine();
        System.out.print("Password: ");
        String password = readLine();

        User newUser = null;

        switch (type) {
            case 1 -> {
                System.out.print("Department/School (IT_AND_ENGINEERING / BUSINESS / SCIENCE): ");
                School school = parseSchool(readLine());
                System.out.print("Degree (BACHELOR / MASTER / PHD): ");
                DegreeType degree = parseDegree(readLine());
                System.out.print("Year of study: ");
                int year = readInt();
                if (degree == DegreeType.MASTER || degree == DegreeType.PHD) {
                    newUser = new GraduateStudent(id, first, last, email, password, degree, school, year);
                } else {
                    newUser = new Student(id, first, last, email, password, degree, school, year);
                }
            }
            case 2 -> {
                System.out.print("Department: ");
                String dept = readLine();
                System.out.print("Position (PROFESSOR / SENIOR_LECTURER / LECTURER / TUTOR): ");
                TeacherPosition pos = parsePosition(readLine());
                System.out.print("School (IT_AND_ENGINEERING / BUSINESS / SCIENCE): ");
                School school = parseSchool(readLine());
                newUser = new Teacher(id, first, last, email, password, dept, pos, school);
            }
            case 3 -> {
                System.out.print("Department: ");
                String dept = readLine();
                newUser = new Admin(id, first, last, email, password, dept);
            }
            default -> {
                System.out.println("Invalid type.");
                return;
            }
        }

        // addUser in Admin logs internally
        admin.addUser(new ArrayList<>(database.getUsers()), newUser);
        // We also add directly to the database (Admin.addUser operates on the passed list,
        // so we add to the source-of-truth as well)
        database.addUser(newUser);
        admin.log("ADD_USER_DB", "Persisted user " + newUser.getUserId() + " to database");
    }

    // ─── 3. Remove user ──────────────────────────────────────────────────────────

    private void removeUser() {
        System.out.print("\nEnter user ID to remove: ");
        String userId = readLine();
        // We work on a mutable copy and mirror changes
        List<User> mutableUsers = new ArrayList<>(database.getUsers());
        User found = admin.findById(mutableUsers, userId);
        if (found == null) {
            System.out.println("User not found: " + userId);
            return;
        }
        System.out.print("Confirm removal of " + found.getFullName() + "? (yes/no): ");
        if (!readLine().equalsIgnoreCase("yes")) {
            System.out.println("Cancelled.");
            return;
        }
        database.removeUser(userId);
        admin.log("REMOVE_USER", "Removed user " + userId + " (" + found.getFullName() + ")");
        System.out.println("User removed successfully.");
    }

    // ─── 4. Update email ─────────────────────────────────────────────────────────

    private void updateEmail() {
        System.out.print("\nEnter user ID: ");
        String userId = readLine();
        User user = findUserInDb(userId);
        if (user == null) return;

        System.out.println("Current email: " + user.getEmail());
        System.out.print("New email: ");
        String newEmail = readLine();

        List<User> mutableUsers = new ArrayList<>(database.getUsers());
        admin.updateEmail(mutableUsers, userId, newEmail);
        // reflect on the actual object (already mutated via reference)
        admin.log("UPDATE_EMAIL_CONFIRMED", userId + " → " + newEmail);
    }

    // ─── 5. Reset password ───────────────────────────────────────────────────────

    private void resetPassword() {
        System.out.print("\nEnter user ID: ");
        String userId = readLine();
        User user = findUserInDb(userId);
        if (user == null) return;

        System.out.print("New password: ");
        String newPass = readLine();

        List<User> mutableUsers = new ArrayList<>(database.getUsers());
        admin.resetPassword(mutableUsers, userId, newPass);
    }

    // ─── 6. Search user ──────────────────────────────────────────────────────────

    private void searchUser() {
        System.out.print("\nEnter user ID: ");
        String userId = readLine();
        User user = findUserInDb(userId);
        if (user != null) {
            System.out.println("\nFound:");
            System.out.println(user);
        }
    }

    // ─── 8. Filter logs ──────────────────────────────────────────────────────────

    private void filterLogs() {
        System.out.println("Available actions: ADD_USER, REMOVE_USER, UPDATE_EMAIL, RESET_PASSWORD, LOGOUT");
        System.out.print("Enter action to filter: ");
        String action = readLine();
        List<LogEntry> filtered = admin.getLogsByAction(action);
        if (filtered.isEmpty()) {
            System.out.println("No logs for action: " + action);
        } else {
            filtered.forEach(System.out::println);
        }
    }

    // ─── helpers ─────────────────────────────────────────────────────────────────

    private User findUserInDb(String userId) {
        for (User u : database.getUsers()) {
            if (u.getUserId().equals(userId)) return u;
        }
        System.out.println("User not found: " + userId);
        return null;
    }

    private School parseSchool(String input) {
        try {
            return School.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown school, defaulting to IT_AND_ENGINEERING.");
            return School.IT_AND_ENGINEERING;
        }
    }

    private DegreeType parseDegree(String input) {
        try {
            return DegreeType.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown degree, defaulting to BACHELOR.");
            return DegreeType.BACHELOR;
        }
    }

    private TeacherPosition parsePosition(String input) {
        try {
            return TeacherPosition.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown position, defaulting to LECTURER.");
            return TeacherPosition.LECTURER;
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
