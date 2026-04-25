package classes;

import enums.RequestStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class SupportRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int idCounter = 1;

    private final int requestId;
    private final Employee requester;
    private final String description;
    private RequestStatus status;
    private final LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    private String resolution; // notes added when DONE / REJECTED

    public SupportRequest(Employee requester, String description) {
        this.requestId = idCounter++;
        this.requester = requester;
        this.description = description;
        this.status = RequestStatus.NEW;
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = this.submittedAt;
        this.resolution = "";
    }

    public int getRequestId() { return requestId; }
    public Employee getRequester() { return requester; }
    public String getDescription() { return description; }
    public RequestStatus getStatus() { return status; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getResolution() { return resolution; }

    void setStatus(RequestStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public String toString() {
        return String.format("[#%d | %s] \"%s\" by %s — submitted %s%s",
                requestId, status, description, requester.getFullName(),
                submittedAt.toLocalDate(),
                resolution.isBlank() ? "" : " | Note: " + resolution);
    }
}

public class TechSupportSpecialist extends Employee implements Comparable<TechSupportSpecialist> {
    private static final long serialVersionUID = 1L;

    private static final List<SupportRequest> allRequests = new ArrayList<>();

    private List<SupportRequest> assignedRequests;

    public TechSupportSpecialist(String userId, String firstName, String lastName,
                                  String email, String password, String department) {
        super(userId, firstName, lastName, email, password, department);
        this.assignedRequests = new ArrayList<>();
    }

    // ===== Static helper: anyone can submit a request =====

    /**
     * Any employee can submit a support request into the global queue.
     *
     * @param requester   the employee submitting the request
     * @param description description of the issue (e.g., "Fix projector in Room 305")
     * @return the created request (so the requester can track its ID)
     */
    public static SupportRequest submitRequest(Employee requester, String description) {
        SupportRequest req = new SupportRequest(requester, description);
        allRequests.add(req);
        System.out.println("Support request #" + req.getRequestId() + " submitted: " + description);
        return req;
    }

    // ===== Specialist actions =====

    public List<SupportRequest> viewNewRequests() {
        List<SupportRequest> newOnes = allRequests.stream()
                .filter(r -> r.getStatus() == RequestStatus.NEW)
                .collect(Collectors.toList());

        newOnes.forEach(r -> {
            r.setStatus(RequestStatus.VIEWED);
            if (!assignedRequests.contains(r)) assignedRequests.add(r);
        });

        System.out.println(getFullName() + " viewed " + newOnes.size() + " new request(s).");
        return Collections.unmodifiableList(newOnes);
    }

    public void acceptRequest(SupportRequest request) {
        requireStatus(request, RequestStatus.VIEWED, RequestStatus.NEW);
        request.setStatus(RequestStatus.ACCEPTED);
        if (!assignedRequests.contains(request)) assignedRequests.add(request);
        System.out.println(getFullName() + " accepted request #" + request.getRequestId());
    }

    public void rejectRequest(SupportRequest request, String reason) {
        requireStatus(request, RequestStatus.VIEWED, RequestStatus.NEW, RequestStatus.ACCEPTED);
        request.setStatus(RequestStatus.REJECTED);
        request.setResolution(reason);
        System.out.println(getFullName() + " rejected request #" + request.getRequestId() + ": " + reason);
    }

    public void markAsDone(SupportRequest request, String resolutionNotes) {
        requireStatus(request, RequestStatus.ACCEPTED);
        request.setStatus(RequestStatus.DONE);
        request.setResolution(resolutionNotes);
        System.out.println(getFullName() + " completed request #" + request.getRequestId()
                + ". Notes: " + resolutionNotes);
    }

    public List<SupportRequest> getAssignedRequests() {
        return Collections.unmodifiableList(assignedRequests);
    }

    public List<SupportRequest> getRequestsByStatus(RequestStatus status) {
        return assignedRequests.stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public static String generateSystemReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== TECH SUPPORT SYSTEM REPORT =====\n");
        sb.append(String.format("Total requests: %d%n", allRequests.size()));
        for (RequestStatus s : RequestStatus.values()) {
            long count = allRequests.stream().filter(r -> r.getStatus() == s).count();
            sb.append(String.format("  %-10s : %d%n", s, count));
        }
        sb.append("--------------------------------------\n");
        allRequests.stream()
                .sorted(Comparator.comparing(SupportRequest::getSubmittedAt).reversed())
                .forEach(r -> sb.append(r).append("\n"));
        sb.append("======================================\n");
        return sb.toString();
    }


    @SafeVarargs
    private static <T> void requireStatus(SupportRequest request, T... allowed) {
        boolean ok = false;
        for (T s : allowed) {
            if (request.getStatus() == s) { ok = true; break; }
        }
        if (!ok) {
            System.out.println("Warning: request #" + request.getRequestId()
                    + " is in status " + request.getStatus() + " — action may not apply.");
        }
    }

    @Override
    public int compareTo(TechSupportSpecialist other) {
        // Sort by number of completed requests (most productive first)
        long myDone = assignedRequests.stream().filter(r -> r.getStatus() == RequestStatus.DONE).count();
        long theirDone = other.assignedRequests.stream().filter(r -> r.getStatus() == RequestStatus.DONE).count();
        return Long.compare(theirDone, myDone);
    }

    @Override
    public String toString() {
        long done = assignedRequests.stream().filter(r -> r.getStatus() == RequestStatus.DONE).count();
        return super.toString() + " | TechSupport | Assigned: " + assignedRequests.size()
                + " | Resolved: " + done;
    }
}