package enums;

public enum ManagerType {
    OR,          // Office of Registrar
    DEPARTMENT,
    DEAN_OFFICE,
    FINANCE;

    @Override
    public String toString() {
        switch (this) {
            case OR: return "Office of Registrar";
            case DEPARTMENT: return "Department";
            case DEAN_OFFICE: return "Dean's Office";
            case FINANCE: return "Finance";
            default: return name();
        }
    }
}
