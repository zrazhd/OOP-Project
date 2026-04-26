package users;

import academics.Course;
import academics.Mark;
import enums.DegreeType;
import enums.School;
import exceptions.CreditLimitExceededException;
import exceptions.TooManyFailsException;
import java.util.*;

public class Student extends User implements Comparable<Student> {
    private static final long serialVersionUID = 1L;

    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS = 3;

    private DegreeType degreeType;
    private School school;
    private int yearOfStudy;
    private Map<Course, Mark> marks;
    private List<Course> enrolledCourses;
    private int totalCredits;
    private int failCount;
    private List<String> organizations;
    private Map<String, String> organizationRoles; // orgName -> "MEMBER" or "HEAD"

    public Student(String userId, String firstName, String lastName, String email, String password,
                   DegreeType degreeType, School school, int yearOfStudy) {
        super(userId, firstName, lastName, email, password);
        this.degreeType = degreeType;
        this.school = school;
        this.yearOfStudy = yearOfStudy;
        this.marks = new HashMap<>();
        this.enrolledCourses = new ArrayList<>();
        this.totalCredits = 0;
        this.failCount = 0;
        this.organizations = new ArrayList<>();
        this.organizationRoles = new HashMap<>();
    }

    /**
     * Register for a course with credit limit and fail count checks.
     */
    public void registerForCourse(Course course) throws CreditLimitExceededException, TooManyFailsException {
        if (failCount >= MAX_FAILS) {
            throw new TooManyFailsException(getFullName() + " has failed " + failCount + " times. Cannot register for more courses.");
        }
        if (totalCredits + course.getCredits() > MAX_CREDITS) {
            throw new CreditLimitExceededException(getFullName() + " would exceed " + MAX_CREDITS + " credits. Current: " + totalCredits + ", trying to add: " + course.getCredits());
        }
        if (enrolledCourses.contains(course)) {
            System.out.println("Already enrolled in " + course.getName());
            return;
        }
        enrolledCourses.add(course);
        course.enrollStudent(this);
        marks.put(course, new Mark(course));
        totalCredits += course.getCredits();
    }

    /**
     * Drop a course.
     */
    public void dropCourse(Course course) {
        if (enrolledCourses.remove(course)) {
            marks.remove(course);
            course.removeStudent(this);
            totalCredits -= course.getCredits();
        }
    }

    /**
     * View all courses the student is enrolled in.
     */
    public List<Course> viewCourses() {
        return Collections.unmodifiableList(enrolledCourses);
    }

    /**
     * View mark for a specific course.
     */
    public Mark viewMark(Course course) {
        return marks.get(course);
    }

    /**
     * View all marks.
     */
    public Map<Course, Mark> viewAllMarks() {
        return Collections.unmodifiableMap(marks);
    }

    /**
     * Get the full transcript.
     */
    public String getTranscript() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TRANSCRIPT ===\n");
        sb.append("Student: ").append(getFullName()).append(" (").append(getUserId()).append(")\n");
        sb.append("School: ").append(school).append("\n");
        sb.append("Degree: ").append(degreeType).append(" | Year: ").append(yearOfStudy).append("\n");
        sb.append("GPA: ").append(String.format("%.2f", calculateGPA())).append("\n");
        sb.append("------------------\n");
        for (Map.Entry<Course, Mark> entry : marks.entrySet()) {
            Course c = entry.getKey();
            Mark m = entry.getValue();
            sb.append(c.getCourseId()).append(" ").append(c.getName())
              .append(" (").append(c.getCredits()).append(" cr.) - ")
              .append(m.getTotal()).append(" ").append(m.getGradeLetter()).append("\n");
        }
        sb.append("==================\n");
        return sb.toString();
    }

    /**
     * Calculate cumulative GPA.
     */
    public double calculateGPA() {
        if (marks.isEmpty()) return 0.0;
        double totalPoints = 0;
        int totalCr = 0;
        for (Map.Entry<Course, Mark> entry : marks.entrySet()) {
            int cr = entry.getKey().getCredits();
            double gpa = entry.getValue().getGpaValue();
            totalPoints += gpa * cr;
            totalCr += cr;
        }
        return totalCr == 0 ? 0.0 : totalPoints / totalCr;
    }

    /**
     * Record a fail (called when a mark < 50 is finalized).
     */
    public void recordFail() {
        failCount++;
    }

    /**
     * View info about a teacher of a specific course.
     */
    public String viewTeacherInfo(Course course) {
        StringBuilder sb = new StringBuilder();
        sb.append("Teachers for ").append(course.getName()).append(":\n");
        sb.append("Lecture: ");
        for (Teacher t : course.getLectureTeachers()) {
            sb.append(t.getFullName()).append(" (").append(t.getPosition()).append(") ");
        }
        sb.append("\nPractice: ");
        for (Teacher t : course.getPracticeTeachers()) {
            sb.append(t.getFullName()).append(" (").append(t.getPosition()).append(") ");
        }
        return sb.toString();
    }

    /**
     * Rate a teacher (1-10 scale).
     */
    public void rateTeacher(Teacher teacher, int rating) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }
        teacher.addRating(rating);
    }

    /**
     * Join a student organization as member or head.
     */
    public void joinOrganization(String orgName, boolean isHead) {
        if (!organizations.contains(orgName)) {
            organizations.add(orgName);
            organizationRoles.put(orgName, isHead ? "HEAD" : "MEMBER");
        }
    }

    public void leaveOrganization(String orgName) {
        organizations.remove(orgName);
        organizationRoles.remove(orgName);
    }

    /**
     * Sort comparison by GPA (descending).
     */
    @Override
    public int compareTo(Student other) {
        return Double.compare(other.calculateGPA(), this.calculateGPA());
    }

    // Getters and Setters
    public DegreeType getDegreeType() { return degreeType; }
    public void setDegreeType(DegreeType degreeType) { this.degreeType = degreeType; }
    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }
    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    public int getTotalCredits() { return totalCredits; }
    public int getFailCount() { return failCount; }
    public List<String> getOrganizations() { return organizations; }
    public Map<String, String> getOrganizationRoles() { return organizationRoles; }

    @Override
    public String toString() {
        return super.toString() + " | " + degreeType + " | " + school + " | Year " + yearOfStudy + " | GPA: " + String.format("%.2f", calculateGPA());
    }
}
