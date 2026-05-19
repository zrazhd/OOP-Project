package users;

import enums.UrgencyLevel;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the complaint in the system.
 */
public class Complaint implements Serializable {

    private Teacher teacher;
    private Student student;
    private UrgencyLevel urgency;
    private String reason;
    private LocalDateTime date;

    /**
     * Constructor for Complaint.
     * @param teacher parameter value.
     * @param student parameter value.
     * @param urgency parameter value.
     * @param reason parameter value.
     */
    public Complaint(Teacher teacher, Student student, UrgencyLevel urgency, String reason) {
        this.teacher = teacher;
        this.student = student;
        this.urgency = urgency;
        this.reason = reason;
        this.date = LocalDateTime.now();
    }

    /**
     * Gets the teacher.
     * @return Teacher
     */
    public Teacher getTeacher() { return teacher; }
    /**
     * Gets the student.
     * @return Student
     */
    public Student getStudent() { return student; }
    /**
     * Gets the urgency.
     * @return UrgencyLevel
     */
    public UrgencyLevel getUrgency() { return urgency; }
    /**
     * Gets the reason.
     * @return String
     */
    public String getReason() { return reason; }
    /**
     * Gets the date.
     * @return LocalDateTime
     */
    public LocalDateTime getDate() { return date; }

    @Override
    public String toString() {
        return "[" + urgency + "] " + teacher.getFullName() + " -> " + student.getFullName() + ": " + reason + " (" + date.toLocalDate() + ")";
    }
}
