package users;

import enums.RequestStatus;
import system.TechRequest;
import java.util.*;


public class TechSupportSpecialist extends Employee {

    private List<TechRequest> assignedRequests;
    private List<TechRequest> completedRequests;
    private String specialization; // e.g. "Hardware", "Network", "Software"

    public TechSupportSpecialist(String userId, String firstName, String lastName,
                                  String email, String password, String department,
                                  String specialization) {
        super(userId, firstName, lastName, email, password, department);
        this.specialization = specialization;
        this.assignedRequests = new ArrayList<>();
        this.completedRequests = new ArrayList<>();
    }

    /**
     * Assign a request to this specialist.
     * Called by Admin or Manager when routing a request.
     */
    public void receiveRequest(TechRequest request) {
        assignedRequests.add(request);
        System.out.println("[TechSupport] " + getFullName() + " received request: " + request.getTitle());
    }

    /**
     * View all new/unresolved requests. Automatically marks them as VIEWED.
     */
    public List<TechRequest> viewNewRequests() {
        List<TechRequest> newRequests = new ArrayList<>();
        for (TechRequest req : assignedRequests) {
            if (req.getStatus() == RequestStatus.NEW) {
                req.setStatus(RequestStatus.VIEWED);
                newRequests.add(req);
                System.out.println("[TechSupport] Viewed request #" + req.getRequestId()
                        + ": \"" + req.getTitle() + "\" → status set to VIEWED");
            }
        }
        return newRequests;
    }

    /**
     * View all requests regardless of status.
     */
    public List<TechRequest> viewAllRequests() {
        return Collections.unmodifiableList(assignedRequests);
    }

    /**
     * Accept a request — specialist will work on it.
     */
    public void acceptRequest(TechRequest request) {
        if (!assignedRequests.contains(request)) {
            System.out.println("[TechSupport] Request not assigned to you.");
            return;
        }
        if (request.getStatus() == RequestStatus.NEW || request.getStatus() == RequestStatus.VIEWED) {
            request.setStatus(RequestStatus.ACCEPTED);
            System.out.println("[TechSupport] Request #" + request.getRequestId()
                    + " ACCEPTED by " + getFullName());
        } else {
            System.out.println("[TechSupport] Cannot accept request in status: " + request.getStatus());
        }
    }

    /**
     * Reject a request with a reason.
     */
    public void rejectRequest(TechRequest request, String reason) {
        if (!assignedRequests.contains(request)) {
            System.out.println("[TechSupport] Request not assigned to you.");
            return;
        }
        request.setStatus(RequestStatus.REJECTED);
        request.setResolutionNote("Rejected: " + reason);
        System.out.println("[TechSupport] Request #" + request.getRequestId()
                + " REJECTED by " + getFullName() + ". Reason: " + reason);
    }

    /**
     * Mark a request as done, optionally leaving a resolution note.
     */
    public void markAsDone(TechRequest request, String resolutionNote) {
        if (!assignedRequests.contains(request)) {
            System.out.println("[TechSupport] Request not assigned to you.");
            return;
        }
        if (request.getStatus() != RequestStatus.ACCEPTED) {
            System.out.println("[TechSupport] Request must be ACCEPTED before marking as DONE.");
            return;
        }
        request.setStatus(RequestStatus.DONE);
        request.setResolutionNote(resolutionNote);
        assignedRequests.remove(request);
        completedRequests.add(request);
        System.out.println("[TechSupport] Request #" + request.getRequestId()
                + " marked as DONE. Note: " + resolutionNote);
    }

    /**
     * View requests filtered by status.
     */
    public List<TechRequest> viewRequestsByStatus(RequestStatus status) {
        List<TechRequest> result = new ArrayList<>();
        for (TechRequest req : assignedRequests) {
            if (req.getStatus() == status) {
                result.add(req);
            }
        }
        return result;
    }

    /**
     * Print a summary of all assigned requests.
     */
    public void printRequestSummary() {
        System.out.println("===== TECH REQUEST SUMMARY for " + getFullName() + " =====");
        System.out.println("Specialization: " + specialization);
        System.out.println("Active requests: " + assignedRequests.size());
        System.out.println("Completed requests: " + completedRequests.size());
        for (TechRequest req : assignedRequests) {
            System.out.println("  " + req);
        }
        System.out.println("=====================================================");
    }

    // Getters
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public List<TechRequest> getAssignedRequests() { return assignedRequests; }
    public List<TechRequest> getCompletedRequests() { return completedRequests; }

    @Override
    public String toString() {
        return super.toString() + " | Tech Support [" + specialization + "]";
    }
}