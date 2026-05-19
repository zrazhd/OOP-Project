package app;

import academics.Course;
import academics.Lesson;
import academics.Mark;
import enums.CourseType;
import enums.DegreeType;
import enums.LessonType;
import enums.ManagerType;
import enums.NewsType;
import enums.School;
import enums.TeacherPosition;
import enums.UrgencyLevel;
import research.ResearchPaper;
import research.ResearchProject;
import users.Admin;
import users.GraduateStudent;
import users.Manager;
import users.Student;
import users.Teacher;
import users.TechSupportSpecialist;

import system.Comment;
import system.News;
import system.TechRequest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public final class DataInitializer {
    private DataInitializer() {
    }

    public static void init(Database database) {
        Admin admin = new Admin(
                "A001", "System", "Admin", "admin@uni.kz", "admin123",
                "Administration"
        );

    Teacher professor = new Teacher(
        "T001", "Askar", "Zhuman", "askar@uni.kz", "pass123",
        "Computer Science", TeacherPosition.PROFESSOR, School.IT_AND_ENGINEERING
    );
    professor.addResearchPaper(new ResearchPaper(
        "Deep Learning for Education",
        List.of("Askar Zhuman"),
        "KBTU Journal", 14, LocalDate.of(2021, 5, 1), "10.1000/dle.2021", 9
    ));
    professor.addResearchPaper(new ResearchPaper(
        "AI in Assessment",
        List.of("Askar Zhuman"),
        "KBTU Journal", 10, LocalDate.of(2022, 6, 1), "10.1000/aia.2022", 8
    ));
    professor.addResearchPaper(new ResearchPaper(
        "Learning Analytics",
        List.of("Askar Zhuman"),
        "KBTU Journal", 12, LocalDate.of(2023, 4, 1), "10.1000/la.2023", 7
    ));

    Teacher lecturer = new Teacher(
        "T002", "Dana", "Kassym", "dana@uni.kz", "pass123",
        "Mathematics", TeacherPosition.LECTURER, School.APPLIED_MATHEMATICS
    );

    Manager registrar = new Manager(
        "M001", "Aigerim", "Nazar", "aigerim@uni.kz", "pass123",
        "Registrar Office", ManagerType.OR
    );

    TechSupportSpecialist techSupport = new TechSupportSpecialist(
        "TS001", "Arman", "Ilyas", "arman@uni.kz", "pass123",
        "IT Support", "Software"
    );

    Student student1 = new Student(
        "S001", "Aruzhan", "Nurgali", "aruzhan@uni.kz", "pass123",
        DegreeType.BACHELOR, School.IT_AND_ENGINEERING, 2
    );
    Student student2 = new Student(
        "S002", "Alibek", "Sultan", "alibek@uni.kz", "pass123",
        DegreeType.BACHELOR, School.BUSINESS, 1
    );

    GraduateStudent graduateStudent1 = new GraduateStudent(
        "G001", "Nursultan", "Ospanov", "nursultan@uni.kz", "pass123",
        DegreeType.MASTER, School.IT_AND_ENGINEERING, 1
    );
    GraduateStudent graduateStudent2 = new GraduateStudent(
        "G002", "Moldir", "Sadyk", "moldir@uni.kz", "pass123",
        DegreeType.PHD, School.APPLIED_MATHEMATICS, 2
    );

    try {
        graduateStudent1.setSupervisor(professor);
        graduateStudent2.setSupervisor(professor);
    } catch (Exception ignored) {
    }

    Course oop = new Course("CS201", "Object-Oriented Programming", 5, CourseType.MAJOR, School.IT_AND_ENGINEERING, 2);
    Course ds = new Course("CS202", "Data Structures", 5, CourseType.MAJOR, School.IT_AND_ENGINEERING, 2);
    Course db = new Course("CS303", "Database Systems", 5, CourseType.MAJOR, School.IT_AND_ENGINEERING, 3);
    Course se = new Course("CS304", "Software Engineering", 4, CourseType.MAJOR, School.IT_AND_ENGINEERING, 3);
    Course math = new Course("MA101", "Calculus I", 4, CourseType.MINOR, School.APPLIED_MATHEMATICS, 1);
    Course eco = new Course("BU110", "Introduction to Economics", 3, CourseType.FREE_ELECTIVE, School.BUSINESS, 1);

    oop.addLectureTeacher(professor);
    oop.addPracticeTeacher(lecturer);
    ds.addLectureTeacher(professor);
    ds.addPracticeTeacher(lecturer);
    db.addLectureTeacher(lecturer);
    se.addLectureTeacher(professor);
    math.addLectureTeacher(lecturer);
    eco.addLectureTeacher(lecturer);

    professor.addCourse(oop);
    professor.addCourse(ds);
    professor.addCourse(se);
    lecturer.addCourse(oop);
    lecturer.addCourse(ds);
    lecturer.addCourse(db);
    lecturer.addCourse(math);
    lecturer.addCourse(eco);

    oop.addLesson(new Lesson(LessonType.LECTURE, DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 30), "A-301", professor));
    oop.addLesson(new Lesson(LessonType.PRACTICE, DayOfWeek.WEDNESDAY, LocalTime.of(11, 0), LocalTime.of(12, 30), "B-204", lecturer));
    ds.addLesson(new Lesson(LessonType.LECTURE, DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(11, 30), "A-302", professor));
    db.addLesson(new Lesson(LessonType.LECTURE, DayOfWeek.THURSDAY, LocalTime.of(13, 0), LocalTime.of(14, 30), "C-110", lecturer));
    se.addLesson(new Lesson(LessonType.LECTURE, DayOfWeek.FRIDAY, LocalTime.of(15, 0), LocalTime.of(16, 30), "D-115", professor));
    math.addLesson(new Lesson(LessonType.LECTURE, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(9, 30), "M-101", lecturer));
    eco.addLesson(new Lesson(LessonType.LECTURE, DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(15, 30), "E-12", lecturer));

    try {
        student1.registerForCourse(oop);
        student1.registerForCourse(ds);
        student1.registerForCourse(math);
        student2.registerForCourse(eco);
        student2.registerForCourse(math);
        graduateStudent1.registerForCourse(oop);
        graduateStudent1.registerForCourse(db);
        graduateStudent2.registerForCourse(se);
    } catch (Exception ignored) {
    }

    professor.putMark(student1, oop, 27, 26, 34);
    professor.putMark(student1, ds, 25, 24, 30);
    lecturer.putMark(student2, eco, 20, 21, 28);

    professor.addResearchPaper(new ResearchPaper(
        "Software Quality Metrics",
        List.of("Askar Zhuman", "Nursultan Ospanov"),
        "International CS Review", 16, LocalDate.of(2024, 1, 15), "10.1000/sqm.2024", 11
    ));
    graduateStudent1.addResearchPaper(new ResearchPaper(
        "Student Engagement Analytics",
        List.of("Nursultan Ospanov"),
        "KBTU Graduate Journal", 9, LocalDate.of(2024, 3, 10), "10.1000/sea.2024", 4
    ));
    graduateStudent2.addResearchPaper(new ResearchPaper(
        "Optimization of Neural Network Training",
        List.of("Moldir Sadyk"),
        "PhD Research Notes", 18, LocalDate.of(2023, 11, 20), "10.1000/onn.2023", 6
    ));

    ResearchProject aiProject = new ResearchProject("AI in Learning Analytics");
        try {
            professor.joinResearchProject(aiProject);
        } catch (Exception ignored) {
        }
    graduateStudent1.joinResearchProject(aiProject);
    graduateStudent2.joinResearchProject(new ResearchProject("Adaptive Curriculum Design"));

    News researchNews = new News(
        "Research Symposium 2026",
        "Students and staff are invited to submit papers for the annual symposium.",
        NewsType.RESEARCH,
        registrar
    );
    researchNews.addComment(new Comment(student1, "Looks interesting"));

    News academicNews = new News(
        "Spring Registration Window",
        "Registration for spring semester opens next week.",
        NewsType.ANNOUNCEMENT,
        registrar
    );

    TechRequest request1 = new TechRequest(
        "Projector issue",
        "The projector in room A-301 is flickering.",
        professor,
        "A-301"
    );
    request1.signByDean();
    techSupport.receiveRequest(request1);
    techSupport.acceptRequest(request1);
    techSupport.markAsDone(request1, "Bulb replaced and device tested.");

    TechRequest request2 = new TechRequest(
        "Wi-Fi access",
        "Lab B-204 needs stronger Wi-Fi coverage.",
        lecturer,
        "B-204"
    );
    techSupport.receiveRequest(request2);
    techSupport.viewNewRequests();

    admin.registerUser(database, professor);
    admin.registerUser(database, lecturer);
    admin.registerUser(database, registrar);
    admin.registerUser(database, techSupport);
    admin.registerUser(database, student1);
    admin.registerUser(database, student2);
    // Additional test students requested
    Student s_zrazhevskiy = new Student(
        "S003", "Denis", "Zrazhevskiy", "d_zrazhevskiy@kbtu.kz", "pass123",
        DegreeType.BACHELOR, School.IT_AND_ENGINEERING, 2
    );
    Student s_gatiyatullin = new Student(
        "S004", "Ibragim", "Gatiyatullin", "i_gatiyatullin@kbtu.kz", "pass123",
        DegreeType.BACHELOR, School.IT_AND_ENGINEERING, 3
    );
    Student s_yesentai = new Student(
        "S005", "Adil", "Yesentai", "a_yessentay@kbtu.kz", "pass123",
        DegreeType.BACHELOR, School.BUSINESS, 1
    );
    Student s_kuanysh = new Student(
        "S006", "Kuanysh", "Zhanibek", "z_kuanysh@kbtu.kz", "pass123",
        DegreeType.BACHELOR, School.IT_AND_ENGINEERING, 2
    );

    try {
        s_zrazhevskiy.registerForCourse(oop);
        s_zrazhevskiy.registerForCourse(ds);
        professor.putMark(s_zrazhevskiy, oop, 28, 27, 35);
        professor.putMark(s_zrazhevskiy, ds, 24, 25, 32);

        s_gatiyatullin.registerForCourse(db);
        s_gatiyatullin.registerForCourse(se);
        lecturer.putMark(s_gatiyatullin, db, 22, 23, 30);
        professor.putMark(s_gatiyatullin, se, 26, 25, 33);

        s_yesentai.registerForCourse(eco);
        s_yesentai.registerForCourse(math);
        lecturer.putMark(s_yesentai, eco, 18, 20, 27);
        lecturer.putMark(s_yesentai, math, 20, 22, 30);

        s_kuanysh.registerForCourse(oop);
        s_kuanysh.registerForCourse(db);
        professor.putMark(s_kuanysh, oop, 25, 24, 31);
        lecturer.putMark(s_kuanysh, db, 23, 22, 29);
    } catch (Exception ignored) {
    }

    // add to system
    admin.registerUser(database, s_zrazhevskiy);
    admin.registerUser(database, s_gatiyatullin);
    admin.registerUser(database, s_yesentai);
    admin.registerUser(database, s_kuanysh);
    admin.registerUser(database, graduateStudent1);
    admin.registerUser(database, graduateStudent2);

    admin.registerCourse(database, oop);
    admin.registerCourse(database, ds);
    admin.registerCourse(database, db);
    admin.registerCourse(database, se);
    admin.registerCourse(database, math);
    admin.registerCourse(database, eco);

    database.addNews(researchNews);
    database.addNews(academicNews);
    database.addTechRequest(request1);
    database.addTechRequest(request2);

    database.addUser(admin);
    }
}