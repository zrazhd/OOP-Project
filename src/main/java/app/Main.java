package app;

import academics.Course;
import enums.CourseType;
import enums.DegreeType;
import enums.School;
import enums.TeacherPosition;
import exceptions.CreditLimitExceededException;
import exceptions.TooManyFailsException;
import research.ResearchPaper;
import users.GraduateStudent;
import users.Student;
import users.Teacher;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;

/**
 * Simple runnable demo entrypoint.
 */
public class Main {

    public static void main(String[] args) {
                Database database = Database.getInstance();
                DataInitializer.init(database);
                Scanner scanner = new Scanner(System.in);

                System.out.println("=== KBTU University System ===");
                System.out.print("Choose language (EN/KZ/RU): ");
                String language = scanner.nextLine().trim();
                System.out.println("Language selected: " + language);

                while (true) {
                        System.out.println("\n--- Login ---");
                        System.out.print("Email: ");
                        String email = scanner.nextLine().trim();
                        System.out.print("Password: ");
                        String password = scanner.nextLine().trim();

                        var user = database.login(email, password);
                        if (user == null) {
                                System.out.println("Invalid email or password.");
                                continue;
                        }

                        System.out.println("Welcome, " + user.getFullName());
                        if (user instanceof Student student) {
                                new StudentMenu(student, database, scanner).show();
                        } else {
                                System.out.println("This demo currently supports student accounts in the menu.");
                        }
                }
    }
}