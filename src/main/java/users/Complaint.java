package users;

import enums.UrgencyLevel;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    private Teacher teacher;
    private Student student;
    private UrgencyLevel urgency;
    private String reason;
    private LocalDateTime date;

    public Complaint(Teacher teacher, Student student, UrgencyLevel urgency, String reason) {
        this.teacher = teacher;
        this.student = student;
        this.urgency = urgency;
        this.reason = reason;
        this.date = LocalDateTime.now();
    }

    public Teacher getTeacher() { return teacher; }
    public Student getStudent() { return student; }
    public UrgencyLevel getUrgency() { return urgency; }
    public String getReason() { return reason; }
    public LocalDateTime getDate() { return date; }

    @Override
    public String toString() {
        return "[" + urgency + "] " + teacher.getFullName() + " -> " + student.getFullName() + ": " + reason + " (" + date.toLocalDate() + ")";
    }
}
