package system;

import enums.RequestStatus;
import users.Employee;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


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

  
    public void signByDean() {
        this.signedByDean = true;
        touch();
        System.out.println("[TechRequest] Request #" + requestId + " signed by dean.");
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
        touch();
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getRequestId() { return requestId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Employee getRequester() { return requester; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public RequestStatus getStatus() { return status; }
    public String getResolutionNote() { return resolutionNote; }
    public void setResolutionNote(String resolutionNote) { this.resolutionNote = resolutionNote; touch(); }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
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