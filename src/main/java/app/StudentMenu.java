package app;

import academics.Course;
import research.NotResearcherException;
import research.ResearchPaper;
import research.ResearchProject;
import users.GraduateStudent;
import users.Student;
import users.Teacher;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class StudentMenu {
    private final Student student;
    private final Database database;
    private final Scanner scanner;

    public StudentMenu(Student student, Database database, Scanner scanner) {
        this.student = student;
        this.database = database;
        this.scanner = scanner;
    }

    public void show() {
        while (true) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1. View my courses");
            System.out.println("2. Register for course");
            System.out.println("3. View marks");
            System.out.println("4. View transcript");
            System.out.println("5. Rate teacher");
            System.out.println("6. View teacher info");
            if (student instanceof GraduateStudent) {
                System.out.println("7. Add research paper");
                System.out.println("8. View research papers");
                System.out.println("9. Join research project");
                System.out.println("10. View h-index");
                System.out.println("11. Set supervisor");
                System.out.println("12. Add diploma project");
            }
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> viewMyCourses();
                case 2 -> registerForCourse();
                case 3 -> viewMarks();
                case 4 -> System.out.println(student.getTranscript());
                case 5 -> rateTeacher();
                case 6 -> viewTeacherInfo();
                case 7 -> { if (student instanceof GraduateStudent gs) addResearchPaper(gs); else invalid(); }
                case 8 -> { if (student instanceof GraduateStudent gs) viewResearchPapers(gs); else invalid(); }
                case 9 -> { if (student instanceof GraduateStudent gs) joinResearchProject(gs); else invalid(); }
                case 10 -> { if (student instanceof GraduateStudent gs) System.out.println("h-index = " + gs.calculateHIndex()); else invalid(); }
                case 11 -> { if (student instanceof GraduateStudent gs) setSupervisor(gs); else invalid(); }
                case 12 -> { if (student instanceof GraduateStudent gs) addDiplomaProject(gs); else invalid(); }
                case 0 -> { System.out.println("Logging out..."); return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void viewMyCourses() {
        List<Course> courses = student.viewCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses yet.");
            return;
        }
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    private void registerForCourse() {
        for (Course course : database.getCourses()) {
            System.out.println(course.getCourseId() + " - " + course.getName());
        }
        System.out.print("Enter course id: ");
        Course selected = database.findCourseById(scanner.nextLine().trim());
        if (selected == null) {
            System.out.println("Course not found.");
            return;
        }
        try {
            student.registerForCourse(selected);
            System.out.println("Registered successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewMarks() {
        if (student.viewAllMarks().isEmpty()) {
            System.out.println("No marks yet.");
            return;
        }
        student.viewAllMarks().forEach((course, mark) ->
                System.out.println(course.getCourseId() + " -> " + mark.getTotal() + " (" + mark.getGradeLetter() + ")"));
    }

    private void rateTeacher() {
        List<Teacher> teachers = database.getTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            Teacher teacher = teachers.get(i);
            System.out.println((i + 1) + ". " + teacher.getFullName());
        }
        System.out.print("Choose teacher number: ");
        int index = readInt();
        if (index < 1 || index > teachers.size()) {
            System.out.println("Invalid teacher.");
            return;
        }
        System.out.print("Rating 1-10: ");
        int rating = readInt();
        teachers.get(index - 1).addRating(rating);
    }

    private void viewTeacherInfo() {
        for (Course course : student.viewCourses()) {
            System.out.println(student.viewTeacherInfo(course));
        }
    }

    private void addResearchPaper(GraduateStudent gs) {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Journal: ");
        String journal = scanner.nextLine();
        System.out.print("Pages: ");
        int pages = readInt();
        System.out.print("Year: ");
        int year = readInt();
        System.out.print("Citations: ");
        int citations = readInt();
        gs.addResearchPaper(new ResearchPaper(
                title,
                List.of(gs.getFullName()),
                journal,
                pages,
                LocalDate.of(year, 1, 1),
                title.toLowerCase().replace(' ', '-') + "-" + year,
                citations
        ));
    }

    private void viewResearchPapers(GraduateStudent gs) {
        if (gs.getResearchPapers().isEmpty()) {
            System.out.println("No papers yet.");
            return;
        }
        gs.getResearchPapers().stream()
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);
    }

    private void joinResearchProject(GraduateStudent gs) {
        System.out.print("Project topic: ");
        String topic = scanner.nextLine();
        gs.joinResearchProject(new ResearchProject(topic));
    }

    private void setSupervisor(GraduateStudent gs) {
        List<Teacher> teachers = database.getTeachers();
        for (int i = 0; i < teachers.size(); i++) {
            Teacher teacher = teachers.get(i);
            System.out.println((i + 1) + ". " + teacher.getFullName() + " (h-index: " + teacher.calculateHIndex() + ")");
        }
        System.out.print("Choose supervisor: ");
        int index = readInt();
        if (index < 1 || index > teachers.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        try {
            gs.setSupervisor(teachers.get(index - 1));
            System.out.println("Supervisor set.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addDiplomaProject(GraduateStudent gs) {
        System.out.print("Diploma project title: ");
        String title = scanner.nextLine();
        gs.addDiplomaProject(new ResearchPaper(
                title,
                List.of(gs.getFullName()),
                "Diploma Project",
                1,
                LocalDate.now(),
                title.toLowerCase().replace(' ', '-') + "-diploma",
                0
        ));
    }

    private void invalid() {
        System.out.println("This option is available only for Graduate Students.");
    }

    private int readInt() {
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}