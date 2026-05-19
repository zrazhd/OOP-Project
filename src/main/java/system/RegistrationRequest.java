package system;

import academics.Course;
import users.Student;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a student's request to register for a course.
 * Flow: Student creates request → Manager approves/rejects.
 */
public class RegistrationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Status { PENDING, APPROVED, REJECTED }

    private static int counter = 0;

    private final int id;
    private final Student student;
    private final Course course;
    private final LocalDateTime createdAt;
    private Status status;
    private String managerComment;

    /**
     * Constructor for RegistrationRequest.
     * @param student parameter value.
     * @param course parameter value.
     */
    public RegistrationRequest(Student student, Course course) {
        this.id = ++counter;
        this.student = student;
        this.course = course;
        this.createdAt = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    /**
     * Gets the id.
     * @return int
     */
    public int getId()                  { return id; }
    /**
     * Gets the student.
     * @return Student
     */
    public Student getStudent()         { return student; }
    /**
     * Gets the course.
     * @return Course
     */
    public Course getCourse()           { return course; }
    /**
     * Gets the status.
     * @return Status
     */
    public Status getStatus()           { return status; }
    /**
     * Gets the created at.
     * @return LocalDateTime
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Gets the manager comment.
     * @return String
     */
    public String getManagerComment()   { return managerComment; }

    /**
     * approve.
     * @param comment parameter value.
     */
    public void approve(String comment) {
        this.status = Status.APPROVED;
        this.managerComment = comment;
    }

    /**
     * reject.
     * @param comment parameter value.
     */
    public void reject(String comment) {
        this.status = Status.REJECTED;
        this.managerComment = comment;
    }

    /**
     * Checks if pending.
     * @return boolean
     */
    public boolean isPending() {
        return status == Status.PENDING;
    }

    @Override
    public String toString() {
        String statusIcon = switch (status) {
            case PENDING  -> "⏳";
            case APPROVED -> "✅";
            case REJECTED -> "❌";
        };
        return String.format("%s #%d │ %s → %s (%s) │ %s",
                statusIcon, id, student.getFullName(),
                course.getCourseId(), course.getName(),
                createdAt.toLocalDate());
    }
}
