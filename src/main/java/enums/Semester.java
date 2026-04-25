package enums;

public enum Semester {
    FALL,
    SPRING,
    SUMMER;

    @Override
    public String toString() {
        switch (this) {
            case FALL: return "Fall";
            case SPRING: return "Spring";
            case SUMMER: return "Summer";
            default: return name();
        }
    }
}
