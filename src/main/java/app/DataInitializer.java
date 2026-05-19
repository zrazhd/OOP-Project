package app;

import academics.Course;
import academics.Lesson;
import academics.Schedule;
import enums.*;
import research.ResearchJournal;
import research.ResearchPaper;
import research.ResearchProject;
import system.Comment;
import system.News;
import system.OfficialMessage;
import system.TechRequest;
import users.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Initializes the database with sample data for demonstration.
 * Creates all required entity types: Users, Courses, Research, News, etc.
 */
public final class DataInitializer {
    private DataInitializer() {
    }

    public static void init(Database database) {

        // ═══ ADMIN ═══════════════════════════════════════════════════════════════
        Admin admin = new Admin(
                "A001", "System", "Admin", "admin@uni.kz", "admin123",
                "Administration"
        );

        // ═══ TEACHERS ════════════════════════════════════════════════════════════
        Teacher professor = new Teacher(
                "T001", "Askar", "Zhuman", "askar@uni.kz", "pass123",
                "Computer Science", TeacherPosition.PROFESSOR, School.IT_AND_ENGINEERING
        );
        // Professor is automatically a researcher, add papers for h-index >= 3
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

        Teacher seniorLecturer = new Teacher(
                "T003", "Bolat", "Omarov", "bolat@uni.kz", "pass123",
                "Physics", TeacherPosition.SENIOR_LECTURER, School.ENERGY_AND_OIL_GAS
        );
        // Senior lecturer who is also a researcher
        seniorLecturer.becomeResearcher();
        seniorLecturer.addResearchPaper(new ResearchPaper(
                "Renewable Energy Optimization",
                List.of("Bolat Omarov"),
                "Energy Journal", 20, LocalDate.of(2020, 3, 15), "10.1000/reo.2020", 15
        ));
        seniorLecturer.addResearchPaper(new ResearchPaper(
                "Solar Panel Efficiency",
                List.of("Bolat Omarov"),
                "Green Tech Review", 18, LocalDate.of(2022, 7, 1), "10.1000/spe.2022", 10
        ));

        Teacher tutor = new Teacher(
                "T004", "Gulnara", "Tursun", "gulnara@uni.kz", "pass123",
                "Computer Science", TeacherPosition.TUTOR, School.IT_AND_ENGINEERING
        );

        // ═══ MANAGER ═════════════════════════════════════════════════════════════
        Manager registrar = new Manager(
                "M001", "Aigerim", "Nazar", "aigerim@uni.kz", "pass123",
                "Registrar Office", ManagerType.OR
        );

        Manager deanOffice = new Manager(
                "M002", "Kairat", "Serik", "kairat@uni.kz", "pass123",
                "Dean Office", ManagerType.DEAN_OFFICE
        );

        // ═══ TECH SUPPORT ════════════════════════════════════════════════════════
        TechSupportSpecialist techSupport = new TechSupportSpecialist(
                "TS001", "Arman", "Ilyas", "arman@uni.kz", "pass123",
                "IT Support", "Software"
        );

        // ═══ STUDENTS (Bachelor) ═════════════════════════════════════════════════
        Student student1 = new Student(
                "S001", "Aruzhan", "Nurgali", "aruzhan@uni.kz", "pass123",
                DegreeType.BACHELOR, School.IT_AND_ENGINEERING, 2
        );
        Student student2 = new Student(
                "S002", "Alibek", "Sultan", "alibek@uni.kz", "pass123",
                DegreeType.BACHELOR, School.BUSINESS, 1
        );
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

        // ═══ GRADUATE STUDENTS ═══════════════════════════════════════════════════
        GraduateStudent graduateStudent1 = new GraduateStudent(
                "G001", "Nursultan", "Ospanov", "nursultan@uni.kz", "pass123",
                DegreeType.MASTER, School.IT_AND_ENGINEERING, 1
        );
        GraduateStudent graduateStudent2 = new GraduateStudent(
                "G002", "Moldir", "Sadyk", "moldir@uni.kz", "pass123",
                DegreeType.PHD, School.APPLIED_MATHEMATICS, 2
        );

        // Set supervisors (requires h-index >= 3)
        try {
            graduateStudent1.setSupervisor(professor);
            graduateStudent2.setSupervisor(professor);
        } catch (Exception e) {
            System.out.println("[Init] Supervisor error: " + e.getMessage());
        }

        // ═══ COURSES ═════════════════════════════════════════════════════════════
        Course oop = new Course("CS201", "Object-Oriented Programming", 5,
                CourseType.MAJOR, School.IT_AND_ENGINEERING, 2);
        Course ds = new Course("CS202", "Data Structures", 5,
                CourseType.MAJOR, School.IT_AND_ENGINEERING, 2);
        Course db = new Course("CS303", "Database Systems", 5,
                CourseType.MAJOR, School.IT_AND_ENGINEERING, 3);
        Course se = new Course("CS304", "Software Engineering", 4,
                CourseType.MAJOR, School.IT_AND_ENGINEERING, 3);
        Course math = new Course("MA101", "Calculus I", 4,
                CourseType.MINOR, School.APPLIED_MATHEMATICS, 1);
        Course eco = new Course("BU110", "Introduction to Economics", 3,
                CourseType.FREE_ELECTIVE, School.BUSINESS, 1);
        // Oil & Gas school course (can be free elective for SITE students)
        Course oilgas = new Course("OG201", "Petroleum Engineering Basics", 3,
                CourseType.FREE_ELECTIVE, School.ENERGY_AND_OIL_GAS, 2);

        // ═══ ASSIGN TEACHERS TO COURSES ══════════════════════════════════════════
        oop.addLectureTeacher(professor);
        oop.addPracticeTeacher(lecturer);
        oop.addPracticeTeacher(tutor);
        ds.addLectureTeacher(professor);
        ds.addPracticeTeacher(lecturer);
        db.addLectureTeacher(lecturer);
        se.addLectureTeacher(professor);
        math.addLectureTeacher(lecturer);
        eco.addLectureTeacher(lecturer);
        oilgas.addLectureTeacher(seniorLecturer);

        professor.addCourse(oop);
        professor.addCourse(ds);
        professor.addCourse(se);
        lecturer.addCourse(oop);
        lecturer.addCourse(ds);
        lecturer.addCourse(db);
        lecturer.addCourse(math);
        lecturer.addCourse(eco);
        seniorLecturer.addCourse(oilgas);
        tutor.addCourse(oop);

        // ═══ SCHEDULE (with conflict detection) ══════════════════════════════════
        Schedule schedule = new Schedule(2026, Semester.SPRING);
        schedule.scheduleLesson(oop, LessonType.LECTURE, DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 30), "A-301", professor);
        schedule.scheduleLesson(oop, LessonType.PRACTICE, DayOfWeek.WEDNESDAY,
                LocalTime.of(11, 0), LocalTime.of(12, 30), "B-204", lecturer);
        schedule.scheduleLesson(ds, LessonType.LECTURE, DayOfWeek.TUESDAY,
                LocalTime.of(10, 0), LocalTime.of(11, 30), "A-302", professor);
        schedule.scheduleLesson(db, LessonType.LECTURE, DayOfWeek.THURSDAY,
                LocalTime.of(13, 0), LocalTime.of(14, 30), "C-110", lecturer);
        schedule.scheduleLesson(se, LessonType.LECTURE, DayOfWeek.FRIDAY,
                LocalTime.of(15, 0), LocalTime.of(16, 30), "D-115", professor);
        schedule.scheduleLesson(math, LessonType.LECTURE, DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(9, 30), "M-101", lecturer);
        schedule.scheduleLesson(eco, LessonType.LECTURE, DayOfWeek.TUESDAY,
                LocalTime.of(14, 0), LocalTime.of(15, 30), "E-12", lecturer);
        schedule.scheduleLesson(oilgas, LessonType.LECTURE, DayOfWeek.WEDNESDAY,
                LocalTime.of(9, 0), LocalTime.of(10, 30), "G-201", seniorLecturer);

        System.out.println("\n--- Full Schedule ---");
        schedule.printSchedule();

        // ═══ STUDENT ENROLLMENT & MARKS ══════════════════════════════════════════
        try {
            student1.registerForCourse(oop);
            student1.registerForCourse(ds);
            student1.registerForCourse(math);
            student2.registerForCourse(eco);
            student2.registerForCourse(math);
            graduateStudent1.registerForCourse(oop);
            graduateStudent1.registerForCourse(db);
            graduateStudent2.registerForCourse(se);

            s_zrazhevskiy.registerForCourse(oop);
            s_zrazhevskiy.registerForCourse(ds);
            s_gatiyatullin.registerForCourse(db);
            s_gatiyatullin.registerForCourse(se);
            s_yesentai.registerForCourse(eco);
            s_yesentai.registerForCourse(math);
            s_kuanysh.registerForCourse(oop);
            s_kuanysh.registerForCourse(db);

            // SITE student takes Oil&Gas course as free elective
            s_zrazhevskiy.registerForCourse(oilgas);
        } catch (Exception e) {
            System.out.println("[Init] Enrollment error: " + e.getMessage());
        }

        // Put marks
        professor.putMark(student1, oop, 27, 26, 34);
        professor.putMark(student1, ds, 25, 24, 30);
        lecturer.putMark(student2, eco, 20, 21, 28);
        professor.putMark(s_zrazhevskiy, oop, 28, 27, 35);
        professor.putMark(s_zrazhevskiy, ds, 24, 25, 32);
        lecturer.putMark(s_gatiyatullin, db, 22, 23, 30);
        professor.putMark(s_gatiyatullin, se, 26, 25, 33);
        lecturer.putMark(s_yesentai, eco, 18, 20, 27);
        lecturer.putMark(s_yesentai, math, 20, 22, 30);
        professor.putMark(s_kuanysh, oop, 25, 24, 31);
        lecturer.putMark(s_kuanysh, db, 23, 22, 29);

        // Student ratings for teachers
        student1.rateTeacher(professor, 9);
        student1.rateTeacher(lecturer, 7);
        s_zrazhevskiy.rateTeacher(professor, 10);
        s_gatiyatullin.rateTeacher(professor, 8);
        s_kuanysh.rateTeacher(lecturer, 6);

        // ═══ RESEARCH ════════════════════════════════════════════════════════════
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

        // Add diploma project for graduate student
        graduateStudent1.addDiplomaProject(new ResearchPaper(
                "Machine Learning in University Education",
                List.of("Nursultan Ospanov"),
                "Master Thesis", 80, LocalDate.of(2025, 5, 1), "thesis-ospanov-2025", 0
        ));

        // Research Projects
        ResearchProject aiProject = new ResearchProject("AI in Learning Analytics");
        try {
            professor.joinResearchProject(aiProject);
        } catch (Exception ignored) {}
        graduateStudent1.joinResearchProject(aiProject);

        ResearchProject adaptiveProject = new ResearchProject("Adaptive Curriculum Design");
        graduateStudent2.joinResearchProject(adaptiveProject);

        // ═══ RESEARCH JOURNALS (Observer pattern) ════════════════════════════════
        ResearchJournal kbtuJournal = new ResearchJournal(
                "KBTU Computer Science Journal", "1234-5678", 2015
        );
        ResearchJournal energyJournal = new ResearchJournal(
                "Central Asian Energy Review", "2345-6789", 2018
        );

        // Subscribe some users
        kbtuJournal.subscribe(student1);
        kbtuJournal.subscribe(graduateStudent1);
        kbtuJournal.subscribe(professor);
        energyJournal.subscribe(seniorLecturer);
        energyJournal.subscribe(s_zrazhevskiy);

        // Publish a paper to journal (triggers Observer notifications)
        kbtuJournal.publishPaper(new ResearchPaper(
                "Advances in Compiler Design",
                List.of("Askar Zhuman"),
                "KBTU CS Journal", 15, LocalDate.of(2025, 9, 1), "10.1000/acd.2025", 3
        ));

        // ═══ NEWS WITH COMMENTS ══════════════════════════════════════════════════
        News researchNews = new News(
                "Research Symposium 2026",
                "Students and staff are invited to submit papers for the annual symposium.",
                NewsType.RESEARCH,
                registrar
        );
        researchNews.addComment(new Comment(student1, "Looks very interesting!"));
        researchNews.addComment(new Comment(graduateStudent1, "Will submit my paper."));

        News academicNews = new News(
                "Spring Registration Window",
                "Registration for spring semester opens next week.",
                NewsType.ANNOUNCEMENT,
                registrar
        );
        academicNews.addComment(new Comment(student2, "When exactly?"));

        News generalNews = new News(
                "Library Hours Extended",
                "The library will be open until midnight during exam period.",
                NewsType.GENERAL,
                deanOffice
        );

        // ═══ OFFICIAL MESSAGES ═══════════════════════════════════════════════════
        OfficialMessage officialMsg1 = new OfficialMessage(
                "Exam Room Change",
                "Final exam for CS201 moved to room A-501. Please inform students.",
                registrar, professor, true, "Dean"
        );
        OfficialMessage officialMsg2 = new OfficialMessage(
                "Faculty Meeting",
                "Mandatory faculty meeting on Friday at 14:00 in Conference Room B.",
                deanOffice, "Computer Science", true, "Dean"
        );
        System.out.println("\n--- Official Messages ---");
        System.out.println(officialMsg1);
        System.out.println(officialMsg2);

        // ═══ TECH SUPPORT REQUESTS ═══════════════════════════════════════════════
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

        TechRequest request3 = new TechRequest(
                "Broken chair in C-110",
                "Two chairs in room C-110 are broken.",
                seniorLecturer,
                "C-110"
        );
        techSupport.receiveRequest(request3);

        // ═══ COMPLAINTS ══════════════════════════════════════════════════════════
        Complaint complaint1 = professor.sendComplaint(student2, UrgencyLevel.LOW, "Frequently absent from lectures");
        Complaint complaint2 = lecturer.sendComplaint(s_yesentai, UrgencyLevel.MEDIUM, "Plagiarism in homework");

        // ═══ EMPLOYEE MESSAGES ═══════════════════════════════════════════════════
        professor.sendMessage(lecturer, "Can you cover my OOP practice session on Wednesday?");
        lecturer.sendMessage(professor, "Sure, I'll handle it.");
        registrar.sendMessage(professor, "Please submit final grades by Friday.");

        // ═══ REGISTER ALL INTO DATABASE ══════════════════════════════════════════
        admin.registerUser(database, admin);
        admin.registerUser(database, professor);
        admin.registerUser(database, lecturer);
        admin.registerUser(database, seniorLecturer);
        admin.registerUser(database, tutor);
        admin.registerUser(database, registrar);
        admin.registerUser(database, deanOffice);
        admin.registerUser(database, techSupport);
        admin.registerUser(database, student1);
        admin.registerUser(database, student2);
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
        admin.registerCourse(database, oilgas);

        database.addNews(researchNews);
        database.addNews(academicNews);
        database.addNews(generalNews);
        database.addTechRequest(request1);
        database.addTechRequest(request2);
        database.addTechRequest(request3);
        database.addComplaint(complaint1);
        database.addComplaint(complaint2);
        database.addJournal(kbtuJournal);
        database.addJournal(energyJournal);

        System.out.println("\n[DataInitializer] ✓ All sample data loaded successfully.");
        System.out.println("[DataInitializer] Users: " + database.getUsers().size()
                + " | Courses: " + database.getCourses().size()
                + " | Journals: " + database.getJournals().size());
    }
}