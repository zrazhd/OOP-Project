package academics;

import enums.LessonType;
import enums.Semester;
import users.Teacher;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

/**
 * Schedule manages the weekly timetable for courses.
 * Handles room assignments and checks for conflicts (room load, teacher availability).
 *
 * Bonus feature: Schedule generation with conflict checking.
 */
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private int academicYear;
    private Semester semester;

    // room -> list of lessons booked in that room
    private Map<String, List<Lesson>> roomSchedule;
    // teacher -> list of lessons they teach
    private Map<String, List<Lesson>> teacherSchedule;
    // all lessons
    private List<Lesson> allLessons;

    public Schedule(int academicYear, Semester semester) {
        this.academicYear = academicYear;
        this.semester = semester;
        this.roomSchedule = new HashMap<>();
        this.teacherSchedule = new HashMap<>();
        this.allLessons = new ArrayList<>();
    }

    /**
     * Attempt to schedule a lesson. Returns false if there is a conflict
     * (room already booked or teacher already teaching at that time).
     */
    public boolean scheduleLesson(Course course, LessonType type,
                                   DayOfWeek day, LocalTime start, LocalTime end,
                                   String room, Teacher teacher) {
        // Check room conflict
        if (hasRoomConflict(room, day, start, end)) {
            System.out.println("[Schedule] CONFLICT: Room " + room
                    + " is already booked on " + day + " " + start + "-" + end);
            return false;
        }
        // Check teacher conflict
        if (hasTeacherConflict(teacher, day, start, end)) {
            System.out.println("[Schedule] CONFLICT: " + teacher.getFullName()
                    + " already has a lesson on " + day + " " + start + "-" + end);
            return false;
        }

        Lesson lesson = new Lesson(type, day, start, end, room, teacher);
        course.addLesson(lesson);
        allLessons.add(lesson);

        roomSchedule.computeIfAbsent(room, k -> new ArrayList<>()).add(lesson);
        teacherSchedule.computeIfAbsent(teacher.getUserId(), k -> new ArrayList<>()).add(lesson);

        System.out.println("[Schedule] Scheduled: " + type + " for " + course.getName()
                + " | " + day + " " + start + "-" + end + " | Room: " + room
                + " | " + teacher.getFullName());
        return true;
    }

    private boolean hasRoomConflict(String room, DayOfWeek day, LocalTime start, LocalTime end) {
        List<Lesson> booked = roomSchedule.getOrDefault(room, Collections.emptyList());
        return booked.stream().anyMatch(l -> l.getDay() == day && overlaps(l, start, end));
    }

    private boolean hasTeacherConflict(Teacher teacher, DayOfWeek day, LocalTime start, LocalTime end) {
        List<Lesson> booked = teacherSchedule.getOrDefault(teacher.getUserId(), Collections.emptyList());
        return booked.stream().anyMatch(l -> l.getDay() == day && overlaps(l, start, end));
    }

    private boolean overlaps(Lesson lesson, LocalTime start, LocalTime end) {
        return lesson.getStartTime().isBefore(end) && lesson.getEndTime().isAfter(start);
    }

    /**
     * Print the full schedule, sorted by day and start time.
     */
    public void printSchedule() {
        System.out.println("====== SCHEDULE " + semester + " " + academicYear + " ======");
        allLessons.stream()
                .sorted(Comparator.comparing(Lesson::getDay)
                        .thenComparing(Lesson::getStartTime))
                .forEach(l -> System.out.println("  " + l));
        System.out.println("======================================");
    }

    /**
     * Print schedule for a specific room.
     */
    public void printRoomSchedule(String room) {
        System.out.println("--- Room " + room + " schedule ---");
        roomSchedule.getOrDefault(room, Collections.emptyList()).stream()
                .sorted(Comparator.comparing(Lesson::getDay).thenComparing(Lesson::getStartTime))
                .forEach(l -> System.out.println("  " + l));
    }

    /**
     * Print schedule for a specific teacher.
     */
    public void printTeacherSchedule(Teacher teacher) {
        System.out.println("--- Schedule for " + teacher.getFullName() + " ---");
        teacherSchedule.getOrDefault(teacher.getUserId(), Collections.emptyList()).stream()
                .sorted(Comparator.comparing(Lesson::getDay).thenComparing(Lesson::getStartTime))
                .forEach(l -> System.out.println("  " + l));
    }

    public int getAcademicYear() { return academicYear; }
    public Semester getSemester() { return semester; }
    public List<Lesson> getAllLessons() { return Collections.unmodifiableList(allLessons); }
}