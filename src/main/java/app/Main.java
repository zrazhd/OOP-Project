package app;

import academics.Course;
import enums.CourseType;
import enums.DegreeType;
import enums.ManagerType;
import enums.School;
import enums.TeacherPosition;
import research.ResearchPaper;
import research.ResearchProject;
import users.Admin;
import users.GraduateStudent;
import users.Manager;
import users.Student;
import users.Teacher;
import users.TechSupportSpecialist;
import users.User;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

        public static void main(String[] args) {
                Database database = Database.getInstance();
                DataInitializer.init(database);
                Scanner scanner = new Scanner(System.in);

                System.out.println("=== KBTU University System ===");
                System.out.print("Choose language (EN/KZ/RU): ");
                String language = scanner.nextLine().trim();
                System.out.println("Language selected: " + language);

                try {
                        while (true) {
                                System.out.println("\n--- Login ---");
                                System.out.print("Email: ");
                                String email = scanner.nextLine().trim();
                                System.out.print("Password: ");
                                String password = scanner.nextLine().trim();

                                User user = database.login(email, password);
                                if (user == null) {
                                        System.out.println("Invalid email or password.");
                                        continue;
                                }

                                System.out.println("Welcome, " + user.getFullName() + " (" + user.getClass().getSimpleName() + ")");

                                if (user instanceof Admin admin) {
                                        adminMenu(admin, database, scanner);
                                } else if (user instanceof GraduateStudent graduateStudent) {
                                        new StudentMenu(graduateStudent, database, scanner).show();
                                } else if (user instanceof Student student) {
                                        new StudentMenu(student, database, scanner).show();
                                } else if (user instanceof Teacher teacher) {
                                        teacherMenu(teacher, database, scanner);
                                } else if (user instanceof Manager manager) {
                                        managerMenu(manager, database, scanner);
                                } else if (user instanceof TechSupportSpecialist techSupport) {
                                        techMenu(techSupport, scanner);
                                } else {
                                        System.out.println("No interactive menu implemented for role: " + user.getClass().getSimpleName());
                                }
                        }
                } catch (NoSuchElementException eof) {
                        System.out.println("Input closed. Exiting...");
                }
        }

        private static void adminMenu(Admin admin, Database database, Scanner scanner) {
                while (true) {
                        System.out.println("\n=== Admin Menu for " + admin.getFullName() + " ===");
                        System.out.println("1. Add student");
                        System.out.println("2. Add graduate student");
                        System.out.println("3. Add teacher");
                        System.out.println("4. Add admin");
                        System.out.println("5. Add manager");
                        System.out.println("6. Add tech support specialist");
                        System.out.println("7. Add course");
                        System.out.println("8. List users");
                        System.out.println("9. List courses");
                        System.out.println("0. Logout");
                        System.out.print("Choice: ");

                        int choice = readInt(scanner);
                        switch (choice) {
                                case 1 -> admin.registerUser(database, createStudent(scanner));
                                case 2 -> admin.registerUser(database, createGraduateStudent(scanner));
                                case 3 -> admin.registerUser(database, createTeacher(scanner));
                                case 4 -> admin.registerUser(database, createAdmin(scanner));
                                case 5 -> admin.registerUser(database, createManager(scanner));
                                case 6 -> admin.registerUser(database, createTechSupport(scanner));
                                case 7 -> admin.registerCourse(database, createCourse(scanner));
                                case 8 -> database.getUsers().forEach(System.out::println);
                                case 9 -> database.getCourses().forEach(System.out::println);
                                case 0 -> {
                                        System.out.println("Admin logout.");
                                        return;
                                }
                                default -> System.out.println("Invalid option");
                        }
                }
        }

        private static Student createStudent(Scanner scanner) {
                String[] common = readCommonUserFields(scanner);
                DegreeType degreeType = chooseEnum("Degree", DegreeType.values(), scanner);
                School school = chooseEnum("School", School.values(), scanner);
                System.out.print("Year of study: ");
                int year = readInt(scanner);
                return new Student(common[0], common[1], common[2], common[3], common[4], degreeType, school, year);
        }

        private static GraduateStudent createGraduateStudent(Scanner scanner) {
                String[] common = readCommonUserFields(scanner);
                System.out.println("Only MASTER or PHD is allowed for graduate students.");
                DegreeType degreeType = chooseEnum("Degree", DegreeType.values(), scanner);
                if (degreeType != DegreeType.MASTER && degreeType != DegreeType.PHD) {
                        throw new IllegalArgumentException("Graduate student must have MASTER or PHD degree type.");
                }
                School school = chooseEnum("School", School.values(), scanner);
                System.out.print("Year of study: ");
                int year = readInt(scanner);
                return new GraduateStudent(common[0], common[1], common[2], common[3], common[4], degreeType, school, year);
        }

        private static Teacher createTeacher(Scanner scanner) {
                String[] common = readCommonUserFields(scanner);
                System.out.print("Department: ");
                String department = scanner.nextLine().trim();
                TeacherPosition position = chooseEnum("Teacher position", TeacherPosition.values(), scanner);
                School school = chooseEnum("School", School.values(), scanner);
                return new Teacher(common[0], common[1], common[2], common[3], common[4], department, position, school);
        }

        private static Admin createAdmin(Scanner scanner) {
                String[] common = readCommonUserFields(scanner);
                System.out.print("Department: ");
                String department = scanner.nextLine().trim();
                return new Admin(common[0], common[1], common[2], common[3], common[4], department);
        }

        private static Manager createManager(Scanner scanner) {
                String[] common = readCommonUserFields(scanner);
                System.out.print("Department: ");
                String department = scanner.nextLine().trim();
                ManagerType managerType = chooseEnum("Manager type", ManagerType.values(), scanner);
                return new Manager(common[0], common[1], common[2], common[3], common[4], department, managerType);
        }

        private static TechSupportSpecialist createTechSupport(Scanner scanner) {
                String[] common = readCommonUserFields(scanner);
                System.out.print("Department: ");
                String department = scanner.nextLine().trim();
                System.out.print("Specialization: ");
                String specialization = scanner.nextLine().trim();
                return new TechSupportSpecialist(common[0], common[1], common[2], common[3], common[4], department, specialization);
        }

        private static Course createCourse(Scanner scanner) {
                System.out.print("Course id: ");
                String id = scanner.nextLine().trim();
                System.out.print("Name: ");
                String name = scanner.nextLine().trim();
                System.out.print("Credits: ");
                int credits = readInt(scanner);
                CourseType courseType = chooseEnum("Course type", CourseType.values(), scanner);
                School school = chooseEnum("School", School.values(), scanner);
                System.out.print("Year of study: ");
                int year = readInt(scanner);
                return new Course(id, name, credits, courseType, school, year);
        }

        private static void teacherMenu(Teacher teacher, Database database, Scanner scanner) {
                while (true) {
                        System.out.println("\n=== Teacher Menu for " + teacher.getFullName() + " ===");
                        System.out.println("1. View my courses");
                        System.out.println("2. View students in a course");
                        System.out.println("3. Put mark");
                        System.out.println("4. Become researcher");
                        System.out.println("5. Add research paper");
                        System.out.println("6. Join research project");
                        System.out.println("7. View H-index");
                        System.out.println("0. Logout");
                        System.out.print("Choice: ");

                        int choice = readInt(scanner);
                        switch (choice) {
                                case 1 -> teacher.viewCourses().forEach(System.out::println);
                                case 2 -> {
                                        Course course = chooseCourse(database, scanner);
                                        if (course != null) {
                                                course.getEnrolledStudents().forEach(s -> System.out.println(s.getFullName()));
                                        }
                                }
                                case 3 -> putMarkFlow(teacher, database, scanner);
                                case 4 -> {
                                        teacher.becomeResearcher();
                                        System.out.println("You are now marked as researcher.");
                                }
                                case 5 -> addResearchPaperFlow(teacher, scanner);
                                case 6 -> joinResearchProjectFlow(teacher, scanner);
                                case 7 -> System.out.println("h-index = " + teacher.calculateHIndex());
                                case 0 -> {
                                        System.out.println("Teacher logout.");
                                        return;
                                }
                                default -> System.out.println("Invalid option");
                        }
                }
        }

        private static void managerMenu(Manager manager, Database database, Scanner scanner) {
                while (true) {
                        System.out.println("\n=== Manager Menu for " + manager.getFullName() + " ===");
                        System.out.println("1. List courses");
                        System.out.println("2. Create course");
                        System.out.println("3. Generate course report");
                        System.out.println("0. Logout");
                        System.out.print("Choice: ");

                        int choice = readInt(scanner);
                        switch (choice) {
                                case 1 -> database.getCourses().forEach(System.out::println);
                                case 2 -> {
                                        Course course = createCourse(scanner);
                                        if (database.addCourse(course)) {
                                                System.out.println("Course added: " + course);
                                        } else {
                                                System.out.println("Course not added: duplicate id or invalid data.");
                                        }
                                }
                                case 3 -> {
                                        Course course = chooseCourse(database, scanner);
                                        if (course != null) {
                                                System.out.println(manager.generateCourseReport(course));
                                        }
                                }
                                case 0 -> {
                                        System.out.println("Manager logout.");
                                        return;
                                }
                                default -> System.out.println("Invalid option");
                        }
                }
        }

        private static void techMenu(TechSupportSpecialist techSupport, Scanner scanner) {
                while (true) {
                        System.out.println("\n=== Tech Support Menu for " + techSupport.getFullName() + " ===");
                        System.out.println("1. View assigned requests");
                        System.out.println("2. Print summary");
                        System.out.println("0. Logout");
                        System.out.print("Choice: ");

                        int choice = readInt(scanner);
                        switch (choice) {
                                case 1 -> techSupport.viewAllRequests().forEach(System.out::println);
                                case 2 -> techSupport.printRequestSummary();
                                case 0 -> {
                                        System.out.println("Tech logout.");
                                        return;
                                }
                                default -> System.out.println("Invalid option");
                        }
                }
        }

        private static void putMarkFlow(Teacher teacher, Database database, Scanner scanner) {
                Course course = chooseCourse(database, scanner);
                if (course == null) {
                        return;
                }
                List<Student> students = course.getEnrolledStudents();
                if (students.isEmpty()) {
                        System.out.println("No students enrolled in this course.");
                        return;
                }
                for (int i = 0; i < students.size(); i++) {
                        System.out.println((i + 1) + ". " + students.get(i).getFullName());
                }
                System.out.print("Student number: ");
                int index = readInt(scanner) - 1;
                if (index < 0 || index >= students.size()) {
                        System.out.println("Invalid student.");
                        return;
                }
                Student student = students.get(index);
                System.out.print("Att1: ");
                double att1 = readDouble(scanner);
                System.out.print("Att2: ");
                double att2 = readDouble(scanner);
                System.out.print("Final: ");
                double finalExam = readDouble(scanner);
                teacher.putMark(student, course, att1, att2, finalExam);
        }

        private static void addResearchPaperFlow(Teacher teacher, Scanner scanner) {
                System.out.print("Title: ");
                String title = scanner.nextLine().trim();
                System.out.print("Journal: ");
                String journal = scanner.nextLine().trim();
                System.out.print("Pages: ");
                int pages = readInt(scanner);
                System.out.print("Year: ");
                int year = readInt(scanner);
                System.out.print("Citations: ");
                int citations = readInt(scanner);
                teacher.addResearchPaper(new ResearchPaper(
                                title,
                                List.of(teacher.getFullName()),
                                journal,
                                pages,
                                LocalDate.of(year, 1, 1),
                                title.toLowerCase().replace(' ', '-') + "-" + year,
                                citations
                ));
        }

        private static void joinResearchProjectFlow(Teacher teacher, Scanner scanner) {
                System.out.print("Project topic: ");
                String topic = scanner.nextLine().trim();
                try {
                        teacher.joinResearchProject(new ResearchProject(topic));
                        System.out.println("Joined project.");
                } catch (Exception e) {
                        System.out.println("Cannot join project: " + e.getMessage());
                }
        }

        private static Course chooseCourse(Database database, Scanner scanner) {
                if (database.getCourses().isEmpty()) {
                        System.out.println("No courses in database.");
                        return null;
                }
                for (Course course : database.getCourses()) {
                        System.out.println(course.getCourseId() + " - " + course.getName());
                }
                System.out.print("Enter course id: ");
                return database.findCourseById(scanner.nextLine().trim());
        }

        private static String[] readCommonUserFields(Scanner scanner) {
                System.out.print("User id: ");
                String userId = scanner.nextLine().trim();
                System.out.print("First name: ");
                String firstName = scanner.nextLine().trim();
                System.out.print("Last name: ");
                String lastName = scanner.nextLine().trim();
                System.out.print("Email: ");
                String email = scanner.nextLine().trim();
                System.out.print("Password: ");
                String password = scanner.nextLine().trim();
                return new String[] { userId, firstName, lastName, email, password };
        }

        private static <E extends Enum<E>> E chooseEnum(String label, E[] values, Scanner scanner) {
                System.out.println(label + ":");
                for (int i = 0; i < values.length; i++) {
                        System.out.println((i + 1) + ". " + values[i]);
                }
                System.out.print("Choose number: ");
                int index = readInt(scanner) - 1;
                if (index < 0 || index >= values.length) {
                        throw new IllegalArgumentException("Invalid " + label.toLowerCase() + " selection.");
                }
                return values[index];
        }

        private static int readInt(Scanner scanner) {
                try {
                        return Integer.parseInt(scanner.nextLine().trim());
                } catch (Exception e) {
                        return -1;
                }
        }

        private static double readDouble(Scanner scanner) {
                try {
                        return Double.parseDouble(scanner.nextLine().trim());
                } catch (Exception e) {
                        return 0.0;
                }
        }
}