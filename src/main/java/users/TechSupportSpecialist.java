package users;

import enums.RequestStatus;
import system.TechRequest;
import java.util.*;


/**
 * Represents the tech support specialist in the system.
 */
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
     * receiveRequest.
     * @param request parameter value.
     */
    public void receiveRequest(TechRequest request) {
        assignedRequests.add(request);
        System.out.println("[TechSupport] " + getFullName() + " received request: " + request.getTitle());
    }

    /**
     * viewNewRequests.
     * @return List&lt;TechRequest&gt;
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
     * viewAllRequests.
     * @return List&lt;TechRequest&gt;
     */
    public List<TechRequest> viewAllRequests() {
        return Collections.unmodifiableList(assignedRequests);
    }

    /**
     * acceptRequest.
     * @param request parameter value.
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
     * rejectRequest.
     * @param request parameter value.
     * @param reason parameter value.
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
     * markAsDone.
     * @param request parameter value.
     * @param resolutionNote parameter value.
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
     * viewRequestsByStatus.
     * @param status parameter value.
     * @return List&lt;TechRequest&gt;
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
     * printRequestSummary.
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
    /**
     * Gets the specialization.
     * @return String
     */
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    /**
     * Gets the assigned requests.
     * @return List&lt;TechRequest&gt;
     */
    public List<TechRequest> getAssignedRequests() { return assignedRequests; }
    /**
     * Gets the completed requests.
     * @return List&lt;TechRequest&gt;
     */
    public List<TechRequest> getCompletedRequests() { return completedRequests; }

    @Override
    public String toString() {
        return super.toString() + " | Tech Support [" + specialization + "]";
    }
}