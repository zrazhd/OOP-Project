package academics;

import enums.CourseType;
import enums.School;
import users.Student;
import users.Teacher;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the course in the system.
 */
public class Course implements Serializable {

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

    /**
     * Constructor for Course.
     * @param courseId parameter value.
     * @param name parameter value.
     * @param credits parameter value.
     * @param courseType parameter value.
     * @param school parameter value.
     * @param yearOfStudy parameter value.
     */
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

    /**
     * addLectureTeacher.
     * @param teacher parameter value.
     */
    public void addLectureTeacher(Teacher teacher) {
        if (!lectureTeachers.contains(teacher)) {
            lectureTeachers.add(teacher);
        }
    }

    /**
     * addPracticeTeacher.
     * @param teacher parameter value.
     */
    public void addPracticeTeacher(Teacher teacher) {
        if (!practiceTeachers.contains(teacher)) {
            practiceTeachers.add(teacher);
        }
    }

    /**
     * enrollStudent.
     * @param student parameter value.
     * @return boolean
     */
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

    /**
     * removeStudent.
     * @param student parameter value.
     */
    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    /**
     * addLesson.
     * @param lesson parameter value.
     */
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    /**
     * Gets the all teachers.
     * @return List&lt;Teacher&gt;
     */
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
    /**
     * Gets the course id.
     * @return String
     */
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    /**
     * Gets the name.
     * @return String
     */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    /**
     * Gets the credits.
     * @return int
     */
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    /**
     * Gets the course type.
     * @return CourseType
     */
    public CourseType getCourseType() { return courseType; }
    public void setCourseType(CourseType courseType) { this.courseType = courseType; }
    /**
     * Gets the school.
     * @return School
     */
    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }
    /**
     * Gets the year of study.
     * @return int
     */
    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    /**
     * Gets the lecture teachers.
     * @return List&lt;Teacher&gt;
     */
    public List<Teacher> getLectureTeachers() { return lectureTeachers; }
    /**
     * Gets the practice teachers.
     * @return List&lt;Teacher&gt;
     */
    public List<Teacher> getPracticeTeachers() { return practiceTeachers; }
    /**
     * Gets the enrolled students.
     * @return List&lt;Student&gt;
     */
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    /**
     * Gets the lessons.
     * @return List&lt;Lesson&gt;
     */
    public List<Lesson> getLessons() { return lessons; }
    /**
     * Gets the max students.
     * @return int
     */
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
