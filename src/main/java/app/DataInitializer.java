package app;

import academics.Course;
import enums.CourseType;
import enums.DegreeType;
import enums.School;
import enums.TeacherPosition;
import research.ResearchPaper;
import users.GraduateStudent;
import users.Student;
import users.Teacher;

import java.time.LocalDate;
import java.util.List;

public final class DataInitializer {
    private DataInitializer() {
    }

    public static void init(Database database) {
        Teacher teacher = new Teacher(
                "T001", "Askar", "Zhuman", "askar@uni.kz", "pass123",
                "Computer Science", TeacherPosition.PROFESSOR, School.IT_AND_ENGINEERING
        );
        teacher.addResearchPaper(new ResearchPaper(
                "Deep Learning for Education",
                List.of("Askar Zhuman"),
                "KBTU Journal", 14, LocalDate.of(2021, 5, 1), "10.1000/dle.2021", 9
        ));
        teacher.addResearchPaper(new ResearchPaper(
                "AI in Assessment",
                List.of("Askar Zhuman"),
                "KBTU Journal", 10, LocalDate.of(2022, 6, 1), "10.1000/aia.2022", 8
        ));
        teacher.addResearchPaper(new ResearchPaper(
                "Learning Analytics",
                List.of("Askar Zhuman"),
                "KBTU Journal", 12, LocalDate.of(2023, 4, 1), "10.1000/la.2023", 7
        ));

        Student student = new Student(
                "S001", "Aruzhan", "Nurgali", "aruzhan@uni.kz", "pass123",
                DegreeType.BACHELOR, School.IT_AND_ENGINEERING, 2
        );

        GraduateStudent graduateStudent = new GraduateStudent(
                "G001", "Nursultan", "Ospanov", "nursultan@uni.kz", "pass123",
                DegreeType.MASTER, School.IT_AND_ENGINEERING, 1
        );
        try {
            graduateStudent.setSupervisor(teacher);
        } catch (Exception ignored) {
        }

        Course course = new Course(
                "CS201", "Object-Oriented Programming", 5,
                CourseType.MAJOR, School.IT_AND_ENGINEERING, 2
        );
        course.addLectureTeacher(teacher);
        teacher.addCourse(course);

        database.addUser(teacher);
        database.addUser(student);
        database.addUser(graduateStudent);
        database.addCourse(course);
    }
}