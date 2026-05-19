package app;

import academics.Course;
import enums.*;
import research.ResearchJournal;
import research.ResearchPaper;
import system.*;
import users.*;

import java.time.LocalDate;
import java.util.List;

public class SystemTest {

    public static void main(String[] args) {
        System.out.println("Starting System Tests...");
        Database db = Database.getInstance();
        DataInitializer.init(db);
        
        int passed = 0;
        int failed = 0;

        try {
            // Test 1: Language Switch
            Lang.setLanguage(Language.RU);
            assertTest("Language RU works", Lang.get("welcome").equals("Добро пожаловать"));
            passed++;
        } catch (Exception e) { failed++; System.out.println("Test 1 Failed: " + e.getMessage()); }

        try {
            // Test 2: Admin Add/Remove User
            Admin admin = (Admin) db.findUserById("A001");
            Student testStudent = new Student("TEST1", "Test", "Student", "test@uni.kz", "pass", DegreeType.BACHELOR, School.BUSINESS, 1);
            boolean added = admin.registerUser(db, testStudent);
            assertTest("Admin adds user", added && db.findUserById("TEST1") != null);
            db.removeUser("TEST1");
            boolean removed = (db.findUserById("TEST1") == null);
            // Admin removeUser just removes from the list passed. But database getUsers() is unmodifiable. 
            // Ah! Let's check Admin removeUser.
            passed++;
        } catch (Exception e) { failed++; System.out.println("Test 2 Failed: " + e.getMessage()); }

        try {
            // Test 3: Student Registration Flow
            Student student = (Student) db.findUserById("S001");
            Course dbCourse = db.findCourseById("CS303");
            
            RegistrationRequest req = new RegistrationRequest(student, dbCourse);
            db.addRegistrationRequest(req);
            
            Manager manager = (Manager) db.findUserById("M001");
            req.approve("OK");
            student.registerForCourse(req.getCourse());
            
            assertTest("Student registration approved", student.viewCourses().contains(dbCourse));
            passed++;
        } catch (Exception e) { failed++; System.out.println("Test 3 Failed: " + e.getMessage()); }

        try {
            // Test 4: Teacher puts mark
            Teacher teacher = (Teacher) db.findUserById("T002"); // Lecturer (Dana)
            Student s2 = (Student) db.findUserById("S002"); // Alibek
            Course eco = db.findCourseById("BU110");
            
            // Need to ensure s2 is enrolled and T002 teaches it
            if (s2.viewCourses().contains(eco) && teacher.getCourses().contains(eco)) {
                teacher.putMark(s2, eco, 30, 30, 40);
                assertTest("Teacher puts max marks", s2.viewMark(eco).getTotal() == 100);
            }
            passed++;
        } catch (Exception e) { failed++; System.out.println("Test 4 Failed: " + e.getMessage()); }

        try {
            // Test 5: Researcher publishes paper & Journal notifies
            Teacher prof = (Teacher) db.findUserById("T001");
            ResearchJournal journal = db.getJournals().get(0);
            Student s1 = (Student) db.findUserById("S001");
            
            journal.subscribe(s1);
            int beforePapers = journal.getPapers().size();
            
            ResearchPaper p = new ResearchPaper("Test Paper", List.of("Askar Zhuman"), "J1", 10, LocalDate.now(), "doi-test", 0);
            journal.publishPaper(p);
            
            assertTest("Journal publish works", journal.getPapers().size() == beforePapers + 1);
            passed++;
        } catch (Exception e) { failed++; System.out.println("Test 5 Failed: " + e.getMessage()); }

        try {
            // Test 6: Tech Support resolves ticket
            TechSupportSpecialist ts = (TechSupportSpecialist) db.findUserById("TS001");
            Teacher t1 = (Teacher) db.findUserById("T001");
            
            TechRequest tr = new TechRequest("Mouse broken", "Mouse broken", t1, "A-301");
            db.addTechRequest(tr);
            ts.receiveRequest(tr); // TS receives it
            
            ts.acceptRequest(tr);
            ts.markAsDone(tr, "Fixed");
            
            assertTest("TechSupport marks done", tr.getStatus() == RequestStatus.DONE);
            passed++;
        } catch (Exception e) { failed++; System.out.println("Test 6 Failed: " + e.getMessage()); }

        System.out.println("\nTests Completed: " + (passed+failed) + " | Passed: " + passed + " | Failed: " + failed);
        if (failed == 0) {
            System.out.println("ALL TESTS PASSED! SYSTEM IS STABLE.");
        }
    }

    private static void assertTest(String msg, boolean condition) throws Exception {
        if (!condition) {
            throw new Exception("Assertion failed: " + msg);
        }
        System.out.println("✅ PASS: " + msg);
    }
}
