package enums;

public enum NewsType {
    RESEARCH,
    GENERAL,
    ANNOUNCEMENT;

    public boolean isPinned() {
        return this == RESEARCH;
    }

    @Override
    public String toString() {
        switch (this) {
            case RESEARCH: return "Research";
            case GENERAL: return "General";
            case ANNOUNCEMENT: return "Announcement";
            default: return name();
        }
    }
}
