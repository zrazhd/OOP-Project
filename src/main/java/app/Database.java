package app;

import academics.Course;
import enums.CourseType;
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

    private Database() {
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addCourse(Course course) {
        courses.add(course);
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

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
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