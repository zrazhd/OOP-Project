package enums;

public enum DegreeType {
    BACHELOR,
    MASTER,
    PHD;

    @Override
    public String toString() {
        switch (this) {
            case BACHELOR: return "Bachelor";
            case MASTER: return "Master";
            case PHD: return "PhD";
            default: return name();
        }
    }
}
