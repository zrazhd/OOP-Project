package system;

import users.Employee;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A request from an Employee (sick leave, vacation, equipment, etc.).
 * Manager can view and process these.
 */
public class EmployeeRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum RequestType { SICK_LEAVE, VACATION, EQUIPMENT, REFERENCE, OTHER }
    public enum Status { PENDING, APPROVED, REJECTED }

    private static int counter = 0;

    private final int id;
    private final Employee sender;
    private final RequestType type;
    private final String description;
    private final LocalDateTime createdAt;
    private Status status;
    private String managerResponse;

    /**
     * Constructor for EmployeeRequest.
     * @param sender parameter value.
     * @param type parameter value.
     * @param description parameter value.
     */
    public EmployeeRequest(Employee sender, RequestType type, String description) {
        this.id = ++counter;
        this.sender = sender;
        this.type = type;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    /**
     * Gets the id.
     * @return int
     */
    public int getId()                  { return id; }
    /**
     * Gets the sender.
     * @return Employee
     */
    public Employee getSender()         { return sender; }
    /**
     * Gets the type.
     * @return RequestType
     */
    public RequestType getType()        { return type; }
    /**
     * Gets the description.
     * @return String
     */
    public String getDescription()      { return description; }
    /**
     * Gets the status.
     * @return Status
     */
    public Status getStatus()           { return status; }
    /**
     * Gets the created at.
     * @return LocalDateTime
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * approve.
     * @param response parameter value.
     */
    public void approve(String response) {
        this.status = Status.APPROVED;
        this.managerResponse = response;
    }

    /**
     * reject.
     * @param response parameter value.
     */
    public void reject(String response) {
        this.status = Status.REJECTED;
        this.managerResponse = response;
    }

    @Override
    public String toString() {
        String statusIcon = switch (status) {
            case PENDING  -> "⏳";
            case APPROVED -> "✅";
            case REJECTED -> "❌";
        };
        return String.format("%s #%d │ %-20s │ %-12s │ %s │ %s",
                statusIcon, id, sender.getFullName(), type, description, createdAt.toLocalDate());
    }
}
