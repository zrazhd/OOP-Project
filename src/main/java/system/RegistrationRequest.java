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

    public RegistrationRequest(Student student, Course course) {
        this.id = ++counter;
        this.student = student;
        this.course = course;
        this.createdAt = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    public int getId()                  { return id; }
    public Student getStudent()         { return student; }
    public Course getCourse()           { return course; }
    public Status getStatus()           { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getManagerComment()   { return managerComment; }

    public void approve(String comment) {
        this.status = Status.APPROVED;
        this.managerComment = comment;
    }

    public void reject(String comment) {
        this.status = Status.REJECTED;
        this.managerComment = comment;
    }

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
