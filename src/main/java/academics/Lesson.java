package academics;

import enums.LessonType;
import users.Teacher;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class Lesson implements Serializable {

    private LessonType type;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String room;
    private Teacher teacher;

    public Lesson(LessonType type, DayOfWeek day, LocalTime startTime, LocalTime endTime, String room, Teacher teacher) {
        this.type = type;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.teacher = teacher;
    }

    // Getters and Setters
    public LessonType getType() { return type; }
    public void setType(LessonType type) { this.type = type; }
    public DayOfWeek getDay() { return day; }
    public void setDay(DayOfWeek day) { this.day = day; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public Teacher getTeacher() { return teacher; }
    public void setTeacher(Teacher teacher) { this.teacher = teacher; }

    @Override
    public String toString() {
        return type + " | " + day + " " + startTime + "-" + endTime + " | Room: " + room + " | " + teacher.getFullName();
    }
}
