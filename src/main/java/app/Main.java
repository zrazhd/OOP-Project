package app;

import enums.Language;
import users.*;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Entry point for the KBTU University System.
 * All users access the system via authentication.
 * Each role is routed to the appropriate dedicated Menu class.
 */
public class Main {

    public static void main(String[] args) {
        // Try to load persisted data, fall back to fresh init
        Database database = Database.load();
        if (database == null) {
            database = Database.getInstance();
            DataInitializer.init(database);
            System.out.println("[System] Initialized with sample data.");
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     KBTU University System v2.0      ║");
        System.out.println("╚══════════════════════════════════════╝");

        // Language selection
        System.out.println("Choose language / Тілді таңдаңыз / Выберите язык:");
        System.out.println("1. English (EN)");
        System.out.println("2. Қазақша (KZ)");
        System.out.println("3. Русский (RU)");
        System.out.print("Choice: ");
        Language selectedLanguage = Language.EN;
        try {
            int langChoice = Integer.parseInt(scanner.nextLine().trim());
            selectedLanguage = switch (langChoice) {
                case 2 -> Language.KZ;
                case 3 -> Language.RU;
                default -> Language.EN;
            };
        } catch (Exception e) {
            // default EN
        }
        System.out.println("Language: " + selectedLanguage);

        try {
            while (true) {
                System.out.println("\n--- Login ---");
                System.out.print("Email: ");
                String email = scanner.nextLine().trim();

                if (email.equalsIgnoreCase("exit") || email.equalsIgnoreCase("quit")) {
                    System.out.println("Saving data and exiting...");
                    database.save();
                    break;
                }

                System.out.print("Password: ");
                String password = scanner.nextLine().trim();

                User user = database.login(email, password);
                if (user == null) {
                    System.out.println("Invalid email or password. Try again.");
                    continue;
                }

                user.switchLanguage(selectedLanguage);
                System.out.println("\nWelcome, " + user.getFullName()
                        + " (" + user.getClass().getSimpleName() + ")");

                // Route to the appropriate menu based on user role
                routeToMenu(user, database, scanner);

                // Save after each session logout
                database.save();
            }
        } catch (NoSuchElementException eof) {
            System.out.println("Input closed. Saving and exiting...");
            database.save();
        }
    }

    /**
     * Routes the authenticated user to their role-specific menu.
     * Demonstrates polymorphism and instanceof pattern matching.
     */
    private static void routeToMenu(User user, Database database, Scanner scanner) {
        if (user instanceof Admin admin) {
            new AdminMenu(admin, database, scanner).show();

        } else if (user instanceof GraduateStudent gs) {
            // GraduateStudent gets both Student menu + Researcher menu access
            showGraduateStudentMenu(gs, database, scanner);

        } else if (user instanceof Student student) {
            new StudentMenu(student, database, scanner).show();

        } else if (user instanceof Teacher teacher) {
            // Teacher gets TeacherMenu, with ResearcherMenu submenu if researcher
            showTeacherMenu(teacher, database, scanner);

        } else if (user instanceof Manager manager) {
            new ManagerMenu(manager, database, scanner).show();

        } else if (user instanceof TechSupportSpecialist techSupport) {
            new TechSupportMenu(techSupport, database, scanner).show();

        } else {
            System.out.println("No menu available for role: " + user.getClass().getSimpleName());
        }
    }

    /**
     * Teacher menu that also offers ResearcherMenu submenu.
     */
    private static void showTeacherMenu(Teacher teacher, Database database, Scanner scanner) {
        while (true) {
            System.out.println("\n=== TEACHER MAIN MENU for " + teacher.getFullName() + " ===");
            System.out.println("1. Teaching functions");
            if (teacher.isResearcher()) {
                System.out.println("2. Research functions");
            } else {
                System.out.println("2. Become a researcher");
            }
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int choice = readInt(scanner);
            switch (choice) {
                case 1 -> new TeacherMenu(teacher, database, scanner).show();
                case 2 -> {
                    if (teacher.isResearcher()) {
                        new ResearcherMenu(teacher, database, scanner).show();
                    } else {
                        teacher.becomeResearcher();
                        System.out.println("You are now a researcher! Access research menu next time.");
                    }
                }
                case 0 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Graduate student gets Student menu + Research menu.
     */
    private static void showGraduateStudentMenu(GraduateStudent gs, Database database, Scanner scanner) {
        while (true) {
            System.out.println("\n=== GRADUATE STUDENT MAIN MENU for " + gs.getFullName() + " ===");
            System.out.println("1. Student functions");
            System.out.println("2. Research functions");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int choice = readInt(scanner);
            switch (choice) {
                case 1 -> new StudentMenu(gs, database, scanner).show();
                case 2 -> new ResearcherMenu(gs, database, scanner).show();
                case 0 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static int readInt(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}