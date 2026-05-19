package app;

import enums.Language;
import system.Lang;
import users.*;

import java.util.Scanner;

public class Main {
    private static Database database;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // 1. Setup Language
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     KBTU University System v2.0      ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("Choose language / Тілді таңдаңыз / Выберите язык:");
        System.out.println("1. English (EN)\n2. Қазақша (KZ)\n3. Русский (RU)");
        System.out.print("Choice: ");
        String langChoice = scanner.nextLine().trim();
        if (langChoice.equals("2")) Lang.setLanguage(Language.KZ);
        else if (langChoice.equals("3")) Lang.setLanguage(Language.RU);
        else Lang.setLanguage(Language.EN);

        // 2. Load Database
        database = Database.load();
        if (database == null) {
            database = Database.getInstance();
            DataInitializer.init(database);
            System.out.println("[System] Initialized with sample data.");
        }

        // 3. Main Loop
        while (true) {
            Lang.header(Lang.get("sys_title"));
            Lang.menuItem(1, "login");
            Lang.menuExit();
            Lang.separator();
            Lang.prompt();

            String choice = scanner.nextLine().trim();
            if (choice.equals("0")) {
                Lang.info(Lang.get("saving"));
                database.save();
                System.out.println("Goodbye!");
                break;
            } else if (choice.equals("1")) {
                loginFlow();
            } else {
                Lang.err(Lang.get("invalid"));
            }
        }
        scanner.close();
    }

    private static void loginFlow() {
        System.out.print(Lang.get("email") + ": ");
        String email = scanner.nextLine().trim();
        System.out.print(Lang.get("password") + ": ");
        String password = scanner.nextLine().trim();

        User user = database.login(email, password);
        if (user != null) {
            Lang.ok(Lang.get("welcome") + ", " + user.getFullName() + "!");
            database.log(user.getFullName(), "LOGIN", "User logged in");
            routeUser(user);
            database.log(user.getFullName(), "LOGOUT", "User logged out");
            Lang.info(user.getFullName() + " " + Lang.get("logout").toLowerCase());
        } else {
            Lang.err(Lang.get("login_fail"));
        }
    }

    private static void routeUser(User user) {
        if (user instanceof Admin admin) {
            new AdminMenu(admin, database, scanner).show();
        } else if (user instanceof Teacher teacher) {
            new TeacherMenu(teacher, database, scanner).show();
        } else if (user instanceof Manager manager) {
            new ManagerMenu(manager, database, scanner).show();
        } else if (user instanceof TechSupportSpecialist tech) {
            new TechSupportMenu(tech, database, scanner).show();
        } else if (user instanceof Student student) {
            new StudentMenu(student, database, scanner).show();
        } else {
            Lang.err("Unknown role for user: " + user.getFullName());
        }
    }
}