package app;

import academics.Course;
import research.ResearchJournal;
import research.ResearchPaper;
import research.ResearchProject;
import system.Comment;
import system.Lang;
import system.News;
import system.RegistrationRequest;
import users.GraduateStudent;
import users.Student;
import users.StudentOrganization;

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
            Lang.header(Lang.get("stu_menu") + " — " + student.getFullName());
            Lang.menuItem(1, "stu_courses");
            Lang.menuItem(2, "stu_register");
            Lang.menuItem(3, "stu_drop");
            Lang.menuItem(4, "stu_marks");
            Lang.menuItem(5, "stu_transcript");
            Lang.menuItem(6, "stu_rate");
            Lang.menuItem(7, "stu_teacher_info");
            Lang.menuItem(8, "stu_news");
            Lang.menuItem(9, "stu_comment");
            Lang.menuItem(10, "stu_journal_sub");
            Lang.menuItem(11, "stu_journals");
            Lang.menuItem(12, "stu_orgs");
            Lang.menuItem(13, "stu_join_org");
            Lang.menuItem(14, "stu_leave_org");
            Lang.menuItem(15, "stu_pending");

            if (student instanceof GraduateStudent) {
                System.out.println("\n  --- Graduate Student ---");
                Lang.menuItem(16, "res_add");
                Lang.menuItem(17, "res_view");
                Lang.menuItem(18, "res_join");
                Lang.menuItem(19, "res_hindex");
                Lang.menuItem(20, "Set supervisor", true);
                Lang.menuItem(21, "Add diploma project", true);
                Lang.menuItem(22, "View diploma projects", true);
            }
            Lang.menuBack();
            Lang.separator();
            Lang.prompt();

            int choice = readInt();
            switch (choice) {
                case 1  -> viewMyCourses();
                case 2  -> requestCourseRegistration();
                case 3  -> dropCourse();
                case 4  -> viewMarks();
                case 5  -> System.out.println(student.getTranscript());
                case 6  -> rateTeacher();
                case 7  -> viewTeacherInfo();
                case 8  -> viewNews();
                case 9  -> commentOnNews();
                case 10 -> subscribeToJournal();
                case 11 -> viewJournals();
                case 12 -> viewOrganizations();
                case 13 -> joinOrganization();
                case 14 -> leaveOrganization();
                case 15 -> viewPendingRequests();
                case 16 -> { if (student instanceof GraduateStudent gs) addResearchPaper(gs); else Lang.err(Lang.get("invalid")); }
                case 17 -> { if (student instanceof GraduateStudent gs) viewResearchPapers(gs); else Lang.err(Lang.get("invalid")); }
                case 18 -> { if (student instanceof GraduateStudent gs) joinResearchProject(gs); else Lang.err(Lang.get("invalid")); }
                case 19 -> { if (student instanceof GraduateStudent gs) System.out.println("h-index = " + gs.calculateHIndex()); else Lang.err(Lang.get("invalid")); }
                case 20 -> { if (student instanceof GraduateStudent gs) new ResearcherMenu(gs, database, scanner).show(); else Lang.err(Lang.get("invalid")); }
                case 21 -> { if (student instanceof GraduateStudent gs) addDiplomaProject(gs); else Lang.err(Lang.get("invalid")); }
                case 22 -> { if (student instanceof GraduateStudent gs) viewDiplomaProjects(gs); else Lang.err(Lang.get("invalid")); }
                case 0  -> { return; }
                default -> Lang.err(Lang.get("invalid"));
            }
        }
    }

    private void viewMyCourses() {
        List<Course> courses = student.viewCourses();
        if (courses.isEmpty()) {
            Lang.info(Lang.get("stu_no_courses"));
            return;
        }
        for (Course course : courses) {
            System.out.println("  " + course);
        }
    }

    private void requestCourseRegistration() {
        List<Course> all = database.getCourses();
        for (int i = 0; i < all.size(); i++) {
            System.out.println((i + 1) + ". " + all.get(i).getCourseId() + " - " + all.get(i).getName());
        }
        System.out.print(Lang.get("select") + " (1-" + all.size() + "): ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= all.size()) {
            Lang.err(Lang.get("invalid"));
            return;
        }
        Course selected = all.get(idx);
        
        // Check if already requested
        for (RegistrationRequest req : database.getRegistrationRequests()) {
            if (req.getStudent().equals(student) && req.getCourse().equals(selected) && req.isPending()) {
                Lang.err("You already have a pending request for this course.");
                return;
            }
        }

        RegistrationRequest req = new RegistrationRequest(student, selected);
        database.addRegistrationRequest(req);
        database.log(student.getFullName(), "COURSE_REG_REQUEST", "Requested to register for " + selected.getCourseId());
        Lang.ok("Registration request sent to Manager.");
    }

    private void dropCourse() {
        List<Course> courses = student.viewCourses();
        if (courses.isEmpty()) {
            Lang.info(Lang.get("stu_no_courses"));
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i));
        }
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= courses.size()) {
            Lang.err(Lang.get("invalid"));
            return;
        }
        Course c = courses.get(idx);
        student.dropCourse(c);
        database.log(student.getFullName(), "DROP_COURSE", "Dropped course " + c.getCourseId());
        Lang.ok("Course dropped.");
    }

    private void viewMarks() {
        if (student.viewAllMarks().isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        student.viewAllMarks().forEach((course, mark) ->
                System.out.println("  " + course.getCourseId() + " " + course.getName()
                        + " -> " + mark.getTotal() + " (" + mark.getGradeLetter() + ")"));
    }

    private void rateTeacher() {
        System.out.print("Teacher ID: ");
        String tId = scanner.nextLine().trim();
        users.User u = database.findUserById(tId);
        if (u instanceof users.Teacher t) {
            System.out.print("Rating 1-10: ");
            int r = readInt();
            try {
                student.rateTeacher(t, r);
                database.log(student.getFullName(), "RATE_TEACHER", "Rated " + t.getFullName() + " a " + r);
                Lang.ok(Lang.get("success"));
            } catch (Exception e) {
                Lang.err(e.getMessage());
            }
        } else {
            Lang.err(Lang.get("not_found"));
        }
    }

    private void viewTeacherInfo() {
        for (Course course : student.viewCourses()) {
            System.out.println(student.viewTeacherInfo(course));
        }
    }

    private void viewNews() {
        List<News> news = database.getNewsList();
        if (news.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        System.out.println("\n=== UNIVERSITY NEWS ===");
        for (int i = 0; i < news.size(); i++) {
            News n = news.get(i);
            String display = (i + 1) + ". " + n.toString() + "\n  " + n.getContent();
            if (n.isPinned()) display = "\033[1;33m" + display + "\033[0m";
            System.out.println(display);
            for (Comment c : n.getComments()) {
                System.out.println("     💬 " + c);
            }
        }
    }

    private void commentOnNews() {
        viewNews();
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        List<News> news = database.getNewsList();
        if (idx >= 0 && idx < news.size()) {
            System.out.print(Lang.get("message") + ": ");
            String text = scanner.nextLine().trim();
            news.get(idx).addComment(new Comment(student, text));
            database.log(student.getFullName(), "COMMENT_NEWS", "Commented on news #" + news.get(idx).getNewsId());
            Lang.ok(Lang.get("success"));
        } else {
            Lang.err(Lang.get("invalid"));
        }
    }

    private void viewOrganizations() {
        List<String> orgs = student.getOrganizations();
        if (orgs.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        System.out.println("\n--- " + Lang.get("stu_orgs") + " ---");
        for (String o : orgs) {
            System.out.println("  " + o + " (" + student.getOrganizationRoles().get(o) + ")");
        }
    }

    private void joinOrganization() {
        List<StudentOrganization> orgs = database.getOrganizations();
        if (orgs.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        for (int i = 0; i < orgs.size(); i++) {
            System.out.println((i + 1) + ". " + orgs.get(i).getName());
        }
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < orgs.size()) {
            StudentOrganization o = orgs.get(idx);
            if (o.join(student)) {
                database.log(student.getFullName(), "JOIN_ORG", "Joined organization " + o.getName());
            }
        } else {
            Lang.err(Lang.get("invalid"));
        }
    }

    private void leaveOrganization() {
        List<String> orgs = student.getOrganizations();
        if (orgs.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        for (int i = 0; i < orgs.size(); i++) {
            System.out.println((i + 1) + ". " + orgs.get(i));
        }
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < orgs.size()) {
            String name = orgs.get(idx);
            StudentOrganization o = database.findOrganizationByName(name);
            if (o != null && o.leave(student)) {
                database.log(student.getFullName(), "LEAVE_ORG", "Left organization " + name);
            }
        } else {
            Lang.err(Lang.get("invalid"));
        }
    }

    private void viewPendingRequests() {
        boolean found = false;
        System.out.println("\n--- My Requests ---");
        for (RegistrationRequest req : database.getRegistrationRequests()) {
            if (req.getStudent().equals(student)) {
                System.out.println("  " + req);
                if (req.getManagerComment() != null) {
                    System.out.println("    └ Comment: " + req.getManagerComment());
                }
                found = true;
            }
        }
        if (!found) Lang.info(Lang.get("empty"));
    }

    private void subscribeToJournal() {
        List<ResearchJournal> journals = database.getJournals();
        if (journals.isEmpty()) {
            Lang.info(Lang.get("empty"));
            return;
        }
        for (int i = 0; i < journals.size(); i++) {
            System.out.println((i + 1) + ". " + journals.get(i).getName());
        }
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < journals.size()) {
            journals.get(idx).subscribe(student);
            database.log(student.getFullName(), "SUBSCRIBE_JOURNAL", "Subscribed to " + journals.get(idx).getName());
        }
    }

    private void viewJournals() {
        for (ResearchJournal j : database.getJournals()) {
            System.out.println("  " + j);
        }
    }

    // Graduate Student specific pass-throughs
    private void addResearchPaper(GraduateStudent gs) {
        System.out.print("Title: "); String title = scanner.nextLine();
        gs.addResearchPaper(new ResearchPaper(title, List.of(gs.getFullName()), "Journal", 1, LocalDate.now(), "doi", 0));
        database.log(gs.getFullName(), "ADD_PAPER", "Added paper " + title);
        Lang.ok(Lang.get("success"));
    }
    private void viewResearchPapers(GraduateStudent gs) {
        gs.getResearchPapers().forEach(p -> System.out.println("  " + p));
    }
    private void joinResearchProject(GraduateStudent gs) {
        System.out.print("Project topic: "); String topic = scanner.nextLine();
        gs.joinResearchProject(new ResearchProject(topic));
        database.log(gs.getFullName(), "JOIN_PROJECT", "Joined project " + topic);
        Lang.ok(Lang.get("success"));
    }
    private void addDiplomaProject(GraduateStudent gs) {
        System.out.print("Diploma project title: "); String title = scanner.nextLine();
        gs.addDiplomaProject(new ResearchPaper(title, List.of(gs.getFullName()), "Diploma", 1, LocalDate.now(), "doi", 0));
        database.log(gs.getFullName(), "ADD_DIPLOMA", "Added diploma project " + title);
        Lang.ok(Lang.get("success"));
    }
    private void viewDiplomaProjects(GraduateStudent gs) {
        gs.getDiplomaProjects().forEach(System.out::println);
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}