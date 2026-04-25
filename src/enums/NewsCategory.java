package enums;

public enum NewsCategory {
    ACADEMICS,
    RESEARCH,
    EVENTS,
    ANNOUNCEMENTS;

    @Override
    public String toString() {
        switch (this) {
            case ACADEMICS: return "Academics";
            case RESEARCH: return "Research";
            case EVENTS: return "Events";
            case ANNOUNCEMENTS: return "Announcements";
            default: return name();
        }
    }
}