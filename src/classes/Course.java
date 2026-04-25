package classes;

import enums.CourseType;
import enums.School;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseId;
    private String name;
    private int credits;
    private CourseType courseType;
    private School school;
    private int yearOfStudy;

    private List<Teacher> lectureTeachers;
    private List<Teacher> practiceTeachers;
    private List<Student> enrolledStudents;
    private List<Lesson> lessons;
    private int maxStudents;

    public Course(String courseId, String name, int credits, CourseType courseType, School school, int yearOfStudy) {
        this.courseId = courseId;
        this.name = name;
        this.credits = credits;
        this.courseType = courseType;
        this.school = school;
        this.yearOfStudy = yearOfStudy;
        this.lectureTeachers = new ArrayList<>();
        this.practiceTeachers = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.maxStudents = 40;
    }

    public void addLectureTeacher(Teacher teacher) {
        if (!lectureTeachers.contains(teacher)) {
            lectureTeachers.add(teacher);
        }
    }

    public void addPracticeTeacher(Teacher teacher) {
        if (!practiceTeachers.contains(teacher)) {
            practiceTeachers.add(teacher);
        }
    }

    public boolean enrollStudent(Student student) {
        if (enrolledStudents.size() >= maxStudents) {
            System.out.println("Course " + name + " is full.");
            return false;
        }
        if (enrolledStudents.contains(student)) {
            System.out.println(student.getFullName() + " is already enrolled in " + name);
            return false;
        }
        enrolledStudents.add(student);
        return true;
    }

    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public List<Teacher> getAllTeachers() {
        List<Teacher> all = new ArrayList<>(lectureTeachers);
        for (Teacher t : practiceTeachers) {
            if (!all.contains(t)) {
                all.add(t);
            }
        }
        return all;
    }

    // Getters and Setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public CourseType getCourseType() { return courseType; }
    public void setCourseType(CourseType courseType) { this.courseType = courseType; }
    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }
    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    public List<Teacher> getLectureTeachers() { return lectureTeachers; }
    public List<Teacher> getPracticeTeachers() { return practiceTeachers; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    public List<Lesson> getLessons() { return lessons; }
    public int getMaxStudents() { return maxStudents; }
    public void setMaxStudents(int maxStudents) { this.maxStudents = maxStudents; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseId, course.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }

    @Override
    public String toString() {
        return courseId + " - " + name + " (" + credits + " credits, " + courseType + ")";
    }
}
