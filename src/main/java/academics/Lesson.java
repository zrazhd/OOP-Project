package academics;

import enums.LessonType;
import users.Teacher;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Represents the lesson in the system.
 */
public class Lesson implements Serializable {

    private LessonType type;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private Teacher teacher;

    /**
     * Constructor for Lesson.
     * @param type parameter value.
     * @param day parameter value.
     * @param startTime parameter value.
     * @param endTime parameter value.
     * @param room parameter value.
     * @param teacher parameter value.
     */
    public Lesson(LessonType type, DayOfWeek day, LocalTime startTime, LocalTime endTime, String room, Teacher teacher) {
        this.type = type;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.teacher = teacher;
    }

    // Getters and Setters
    /**
     * Gets the type.
     * @return LessonType
     */
    public LessonType getType() { return type; }
    public void setType(LessonType type) { this.type = type; }
    /**
     * Gets the day.
     * @return DayOfWeek
     */
    public DayOfWeek getDay() { return day; }
    public void setDay(DayOfWeek day) { this.day = day; }
    /**
     * Gets the start time.
     * @return LocalTime
     */
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    /**
     * Gets the end time.
     * @return LocalTime
     */
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    /**
     * Gets the room.
     * @return String
     */
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    /**
     * Gets the teacher.
     * @return Teacher
     */
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    @Override
    public String toString() {
        return type + " | " + day + " " + startTime + "-" + endTime + " | Room: " + room + " | " + teacher.getFullName();
    }
}
