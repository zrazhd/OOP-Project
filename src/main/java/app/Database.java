package app;

import academics.Course;
import research.ResearchJournal;
import research.ResearchPaper;
import research.Researcher;
import system.News;
import system.TechRequest;
import system.RegistrationRequest;
import system.EmployeeRequest;
import system.LogEntry;
import users.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton database — central data store for the university system.
 * Supports serialization (save/load) for data persistence.
 * Design patterns: Singleton.
 */
public class Database implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Database instance;

    private List<User> users = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();
    private List<TechRequest> techRequests = new ArrayList<>();
    private List<Complaint> complaints = new ArrayList<>();
    private List<ResearchJournal> journals = new ArrayList<>();
    private List<RegistrationRequest> registrationRequests = new ArrayList<>();
    private List<EmployeeRequest> employeeRequests = new ArrayList<>();
    private List<StudentOrganization> organizations = new ArrayList<>();
    private List<LogEntry> logs = new ArrayList<>();

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // ─── Serialization (Data Storage pattern) ────────────────────────────────────

    private static final String DATA_FILE = "university_data.ser";

    public void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println("[Database] Data saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("[Database] Save failed: " + e.getMessage());
        }
    }

    public static Database load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Database loaded = (Database) ois.readObject();
            if (loaded.users == null) loaded.users = new ArrayList<>();
            if (loaded.courses == null) loaded.courses = new ArrayList<>();
            if (loaded.newsList == null) loaded.newsList = new ArrayList<>();
            if (loaded.techRequests == null) loaded.techRequests = new ArrayList<>();
            if (loaded.complaints == null) loaded.complaints = new ArrayList<>();
            if (loaded.journals == null) loaded.journals = new ArrayList<>();
            if (loaded.registrationRequests == null) loaded.registrationRequests = new ArrayList<>();
            if (loaded.employeeRequests == null) loaded.employeeRequests = new ArrayList<>();
            if (loaded.organizations == null) loaded.organizations = new ArrayList<>();
            if (loaded.logs == null) loaded.logs = new ArrayList<>();
            instance = loaded;
            System.out.println("[Database] Data loaded from " + DATA_FILE);
            return loaded;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[Database] Load failed: " + e.getMessage());
            return null;
        }
    }

    // ─── User CRUD ───────────────────────────────────────────────────────────────

    public boolean addUser(User user) {
        if (user == null || findUserById(user.getUserId()) != null || findUserByEmail(user.getEmail()) != null) {
            return false;
        }
        users.add(user);
        return true;
    }

    public boolean removeUser(String userId) {
        return users.removeIf(u -> u.getUserId().equals(userId));
    }

    public User login(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public User findUserById(String userId) {
        return users.stream()
                .filter(user -> user.getUserId().equalsIgnoreCase(userId))
                .findFirst()
                .orElse(null);
    }

    public User findUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // ─── Course CRUD ─────────────────────────────────────────────────────────────

    public boolean addCourse(Course course) {
        if (course == null) {
            return false;
        }
        String courseId = course.getCourseId();
        if (courseId == null || courseId.isBlank() || findCourseById(courseId) != null) {
            return false;
        }
        courses.add(course);
        return true;
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public Course findCourseById(String courseId) {
        return courses.stream()
                .filter(course -> course.getCourseId().equalsIgnoreCase(courseId))
                .findFirst()
                .orElse(null);
    }

    // ─── News ────────────────────────────────────────────────────────────────────

    public boolean addNews(News news) {
        if (news == null || newsList.contains(news)) {
            return false;
        }
        newsList.add(news);
        return true;
    }

    public List<News> getNewsList() {
        return Collections.unmodifiableList(newsList);
    }

    // ─── Tech Requests ───────────────────────────────────────────────────────────

    public boolean addTechRequest(TechRequest request) {
        if (request == null || techRequests.contains(request)) {
            return false;
        }
        techRequests.add(request);
        return true;
    }

    public List<TechRequest> getTechRequests() {
        return Collections.unmodifiableList(techRequests);
    }

    // ─── Complaints ──────────────────────────────────────────────────────────────

    public void addComplaint(Complaint complaint) {
        complaints.add(complaint);
    }

    public List<Complaint> getComplaints() {
        return Collections.unmodifiableList(complaints);
    }

    // ─── Research Journals ───────────────────────────────────────────────────────

    public void addJournal(ResearchJournal journal) {
        if (!journals.contains(journal)) {
            journals.add(journal);
        }
    }

    public List<ResearchJournal> getJournals() {
        return Collections.unmodifiableList(journals);
    }

    // ─── Registration Requests ───────────────────────────────────────────────────
    public void addRegistrationRequest(RegistrationRequest req) { registrationRequests.add(req); }
    public List<RegistrationRequest> getRegistrationRequests() { return Collections.unmodifiableList(registrationRequests); }

    // ─── Employee Requests ───────────────────────────────────────────────────────
    public void addEmployeeRequest(EmployeeRequest req) { employeeRequests.add(req); }
    public List<EmployeeRequest> getEmployeeRequests() { return Collections.unmodifiableList(employeeRequests); }

    // ─── Student Organizations ───────────────────────────────────────────────────
    public void addOrganization(StudentOrganization org) { organizations.add(org); }
    public List<StudentOrganization> getOrganizations() { return Collections.unmodifiableList(organizations); }
    public StudentOrganization findOrganizationByName(String name) {
        return organizations.stream().filter(o -> o.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    // ─── System Logs ─────────────────────────────────────────────────────────────
    public void addLog(LogEntry log) { logs.add(log); }
    public void log(String actorName, String action, String details) {
        logs.add(new LogEntry(actorName, action, details, java.time.LocalDateTime.now()));
    }
    public List<LogEntry> getLogs() { return Collections.unmodifiableList(logs); }

    // ─── Filtered queries ────────────────────────────────────────────────────────

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Student student) {
                students.add(student);
            }
        }
        return students;
    }

    public List<Teacher> getTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Teacher teacher) {
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Employee emp) {
                employees.add(emp);
            }
        }
        return employees;
    }

    /**
     * Collect every ResearchPaper from every Researcher in the system.
     */
    public List<ResearchPaper> getAllResearchPapers() {
        List<ResearchPaper> all = new ArrayList<>();
        for (Researcher r : getAllResearchers()) {
            for (ResearchPaper p : r.getResearchPapers()) {
                if (!all.contains(p)) {
                    all.add(p);
                }
            }
        }
        return all;
    }

    /**
     * Return every user who implements Researcher.
     */
    public List<Researcher> getAllResearchers() {
        List<Researcher> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Teacher t && t.isResearcher()) {
                result.add(t);
            } else if (u instanceof GraduateStudent gs) {
                result.add(gs);
            }
        }
        return result;
    }
}