package system;

import enums.RequestStatus;
import users.Employee;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Represents the tech request in the system.
 */
public class TechRequest implements Serializable {

    private static int counter = 1;

    private int requestId;
    private String title;
    private String description;
    private Employee requester;
    private String location;          // room / building
    private RequestStatus status;
    private String resolutionNote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean signedByDean;     // official requests must be signed

    /**
     * Constructor for TechRequest.
     * @param title parameter value.
     * @param description parameter value.
     * @param requester parameter value.
     * @param location parameter value.
     */
    public TechRequest(String title, String description, Employee requester, String location) {
        this.requestId = counter++;
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.location = location;
        this.status = RequestStatus.NEW;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.signedByDean = false;
    }

  
    /**
     * signByDean.
     */
    public void signByDean() {
        this.signedByDean = true;
        touch();
        System.out.println("[TechRequest] Request #" + requestId + " signed by dean.");
    }

    /**
     * Sets the status.
     * @param status parameter value.
     */
    public void setStatus(RequestStatus status) {
        this.status = status;
        touch();
    }

    /**
     * touch.
     */
    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    /**
     * Gets the request id.
     * @return int
     */
    public int getRequestId() { return requestId; }
    /**
     * Gets the title.
     * @return String
     */
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    /**
     * Gets the description.
     * @return String
     */
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    /**
     * Gets the requester.
     * @return Employee
     */
    public Employee getRequester() { return requester; }
    /**
     * Gets the location.
     * @return String
     */
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    /**
     * Gets the status.
     * @return RequestStatus
     */
    public RequestStatus getStatus() { return status; }
    /**
     * Gets the resolution note.
     * @return String
     */
    public String getResolutionNote() { return resolutionNote; }
    public void setResolutionNote(String resolutionNote) { this.resolutionNote = resolutionNote; touch(); }
    /**
     * Gets the created at.
     * @return LocalDateTime
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Gets the updated at.
     * @return LocalDateTime
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Checks if signed by dean.
     * @return boolean
     */
    public boolean isSignedByDean() { return signedByDean; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechRequest that = (TechRequest) o;
        return requestId == that.requestId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }

    @Override
    public String toString() {
        return "Request #" + requestId + " [" + status + "]"
                + (signedByDean ? " ✓Signed" : "")
                + " \"" + title + "\" by " + requester.getFullName()
                + " @ " + location
                + (resolutionNote != null ? " | Note: " + resolutionNote : "");
    }
}