package enums;

public enum CourseType {
    MAJOR,
    MINOR,
    FREE_ELECTIVE;

    @Override
    public String toString() {
        switch (this) {
            case MAJOR: return "Major";
            case MINOR: return "Minor";
            case FREE_ELECTIVE: return "Free Elective";
            default: return name();
        }
    }
}
