package app;

import academics.Course;
import research.ResearchPaper;
import research.Researcher;
import users.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton database.
 * Patched to add: removeUser(), getAllResearchPapers(), getAllResearchers()
 * — required by AdminMenu and ResearcherMenu.
 */
public class Database {
    private static Database instance;

    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();

    private Database() {}

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // ─── Basic CRUD ──────────────────────────────────────────────────────────────

    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Remove a user by ID. Used by AdminMenu.
     * @return true if found and removed, false otherwise.
     */
    public boolean removeUser(String userId) {
        return users.removeIf(u -> u.getUserId().equals(userId));
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    // ─── Auth ────────────────────────────────────────────────────────────────────

    public User login(String email, String password) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    // ─── Queries ─────────────────────────────────────────────────────────────────

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public List<Student> getStudents() {
        List<Student> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Student s) result.add(s);
        }
        return result;
    }

    public List<Teacher> getTeachers() {
        List<Teacher> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Teacher t) result.add(t);
        }
        return result;
    }

    public Course findCourseById(String courseId) {
        return courses.stream()
                .filter(c -> c.getCourseId().equalsIgnoreCase(courseId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Collect every ResearchPaper from every Researcher in the system.
     * Used by ResearcherMenu option 8 (all university papers).
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
     * Used by ResearcherMenu option 9 (top cited researchers).
     */
    public List<Researcher> getAllResearchers() {
        List<Researcher> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Teacher t && t.isResearcher()) {
                result.add(t);
            } else if (u instanceof GraduateStudent gs) {
                result.add(gs);
            } else if (u instanceof Researcher r) {
                result.add(r);
            }
        }
        return result;
    }
}
