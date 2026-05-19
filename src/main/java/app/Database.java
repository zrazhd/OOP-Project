package app;

import academics.Course;
import enums.CourseType;
import system.News;
import system.TechRequest;
import users.Student;
import users.Teacher;
import users.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {
    private static Database instance;

    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<News> newsList = new ArrayList<>();
    private final List<TechRequest> techRequests = new ArrayList<>();

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public boolean addUser(User user) {
        if (user == null || findUserById(user.getUserId()) != null || findUserByEmail(user.getEmail()) != null) {
            return false;
        }
        users.add(user);
        return true;
    }

    public boolean addCourse(Course course) {
        if (course == null || findCourseById(course.getCourseId()) != null) {
            return false;
        }
        courses.add(course);
        return true;
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

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public boolean addNews(News news) {
        if (news == null || newsList.contains(news)) {
            return false;
        }
        newsList.add(news);
        return true;
    }

    public boolean addTechRequest(TechRequest request) {
        if (request == null || techRequests.contains(request)) {
            return false;
        }
        techRequests.add(request);
        return true;
    }

    public List<News> getNewsList() {
        return Collections.unmodifiableList(newsList);
    }

    public List<TechRequest> getTechRequests() {
        return Collections.unmodifiableList(techRequests);
    }

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

    public Course findCourseById(String courseId) {
        return courses.stream()
                .filter(course -> course.getCourseId().equalsIgnoreCase(courseId))
                .findFirst()
                .orElse(null);
    }
}