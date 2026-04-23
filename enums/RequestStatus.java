package enums;

public enum RequestStatus {
    NEW,
    VIEWED,
    ACCEPTED,
    REJECTED,
    DONE;

    @Override
    public String toString() {
        switch (this) {
            case NEW: return "New";
            case VIEWED: return "Viewed";
            case ACCEPTED: return "Accepted";
            case REJECTED: return "Rejected";
            case DONE: return "Done";
            default: return name();
        }
    }
}
