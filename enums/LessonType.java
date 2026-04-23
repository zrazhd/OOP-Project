package enums;

public enum LessonType {
    LECTURE,
    PRACTICE;

    @Override
    public String toString() {
        switch (this) {
            case LECTURE: return "Lecture";
            case PRACTICE: return "Practice";
            default: return name();
        }
    }
}
