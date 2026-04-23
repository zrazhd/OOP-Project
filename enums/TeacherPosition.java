package enums;

public enum TeacherPosition {
    TUTOR,
    LECTURER,
    SENIOR_LECTURER,
    PROFESSOR;

    @Override
    public String toString() {
        switch (this) {
            case TUTOR: return "Tutor";
            case LECTURER: return "Lecturer";
            case SENIOR_LECTURER: return "Senior Lecturer";
            case PROFESSOR: return "Professor";
            default: return name();
        }
    }
}
