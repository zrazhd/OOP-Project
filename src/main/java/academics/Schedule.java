package academics;

import enums.LessonType;
import enums.Semester;
import users.Teacher;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;


/**
 * Represents the schedule in the system.
 */
public class Schedule implements Serializable {

    private int academicYear;
    private Semester semester;

    private Map<String, List<Lesson>> roomSchedule;
    private Map<String, List<Lesson>> teacherSchedule;
    private List<Lesson> allLessons;

    /**
     * Constructor for Schedule.
     * @param academicYear parameter value.
     * @param semester parameter value.
     */
    public Schedule(int academicYear, Semester semester) {
        this.academicYear = academicYear;
        this.semester = semester;
        this.roomSchedule = new HashMap<>();
        this.teacherSchedule = new HashMap<>();
        this.allLessons = new ArrayList<>();
    }

    public boolean scheduleLesson(Course course, LessonType type,
                                   DayOfWeek day, LocalTime start, LocalTime end,
                                   String room, Teacher teacher) {
        if (hasRoomConflict(room, day, start, end)) {
            System.out.println("[Schedule] CONFLICT: Room " + room
                    + " is already booked on " + day + " " + start + "-" + end);
            return false;
        }
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

    /**
     * hasRoomConflict.
     * @param room parameter value.
     * @param day parameter value.
     * @param start parameter value.
     * @param end parameter value.
     * @return boolean
     */
    private boolean hasRoomConflict(String room, DayOfWeek day, LocalTime start, LocalTime end) {
        List<Lesson> booked = roomSchedule.getOrDefault(room, Collections.emptyList());
        return booked.stream().anyMatch(l -> l.getDay() == day && overlaps(l, start, end));
    }

    /**
     * hasTeacherConflict.
     * @param teacher parameter value.
     * @param day parameter value.
     * @param start parameter value.
     * @param end parameter value.
     * @return boolean
     */
    private boolean hasTeacherConflict(Teacher teacher, DayOfWeek day, LocalTime start, LocalTime end) {
        List<Lesson> booked = teacherSchedule.getOrDefault(teacher.getUserId(), Collections.emptyList());
        return booked.stream().anyMatch(l -> l.getDay() == day && overlaps(l, start, end));
    }

    /**
     * overlaps.
     * @param lesson parameter value.
     * @param start parameter value.
     * @param end parameter value.
     * @return boolean
     */
    private boolean overlaps(Lesson lesson, LocalTime start, LocalTime end) {
        return lesson.getStartTime().isBefore(end) && lesson.getEndTime().isAfter(start);
    }

    /**
     * printSchedule.
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
     * printRoomSchedule.
     * @param room parameter value.
     */
    public void printRoomSchedule(String room) {
        System.out.println("--- Room " + room + " schedule ---");
        roomSchedule.getOrDefault(room, Collections.emptyList()).stream()
                .sorted(Comparator.comparing(Lesson::getDay).thenComparing(Lesson::getStartTime))
                .forEach(l -> System.out.println("  " + l));
    }

    /**
     * printTeacherSchedule.
     * @param teacher parameter value.
     */
    public void printTeacherSchedule(Teacher teacher) {
        System.out.println("--- Schedule for " + teacher.getFullName() + " ---");
        teacherSchedule.getOrDefault(teacher.getUserId(), Collections.emptyList()).stream()
                .sorted(Comparator.comparing(Lesson::getDay).thenComparing(Lesson::getStartTime))
                .forEach(l -> System.out.println("  " + l));
    }

    /**
     * Gets the academic year.
     * @return int
     */
    public int getAcademicYear() { return academicYear; }
    /**
     * Gets the semester.
     * @return Semester
     */
    public Semester getSemester() { return semester; }
    /**
     * Gets the all lessons.
     * @return List&lt;Lesson&gt;
     */
    public List<Lesson> getAllLessons() { return Collections.unmodifiableList(allLessons); }
}