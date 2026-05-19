package app;

import enums.RequestStatus;
import system.TechRequest;
import users.Employee;
import users.Message;
import users.TechSupportSpecialist;

import java.util.List;
import java.util.Scanner;

/**
 * Console menu for TechSupportSpecialist users.
 * Covers: viewing new/all requests, accepting, rejecting, marking as done,
 * filtering by status, summary dashboard, and messaging.
 */
public class TechSupportMenu {

    private final TechSupportSpecialist specialist;
    private final Database database;
    private final Scanner scanner;

    public TechSupportMenu(TechSupportSpecialist specialist, Database database, Scanner scanner) {
        this.specialist = specialist;
        this.database = database;
        this.scanner = scanner;
    }

    public void show() {
        while (true) {
            System.out.println("\n====== TECH SUPPORT MENU (" + specialist.getFullName() + ") ======");
            System.out.println("1. View NEW requests (marks them as VIEWED)");
            System.out.println("2. View ALL assigned requests");
            System.out.println("3. Accept a request");
            System.out.println("4. Reject a request");
            System.out.println("5. Mark request as DONE");
            System.out.println("6. Filter requests by status");
            System.out.println("7. My summary dashboard");
            System.out.println("8. Send message to employee");
            System.out.println("9. View inbox");
            System.out.println("0. Logout");
            System.out.println("==========================================");
            System.out.print("Choice: ");

            switch (readInt()) {
                case 1 -> viewNewRequests();
                case 2 -> viewAllRequests();
                case 3 -> acceptRequest();
                case 4 -> rejectRequest();
                case 5 -> markDone();
                case 6 -> filterByStatus();
                case 7 -> specialist.printRequestSummary();
                case 8 -> sendMessage();
                case 9 -> viewInbox();
                case 0 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ─── 1. View NEW requests ────────────────────────────────────────────────────

    private void viewNewRequests() {
        List<TechRequest> newReqs = specialist.viewNewRequests();
        if (newReqs.isEmpty()) {
            System.out.println("No new requests.");
        } else {
            System.out.println("\n--- New Requests (now marked VIEWED) ---");
            printRequests(newReqs);
        }
    }

    // ─── 2. View ALL requests ─────────────────────────────────────────────────────

    private void viewAllRequests() {
        List<TechRequest> all = specialist.viewAllRequests();
        if (all.isEmpty()) {
            System.out.println("No requests assigned to you.");
            return;
        }
        System.out.println("\n--- All Assigned Requests ---");
        printNumberedRequests(all);
    }

    // ─── 3. Accept ───────────────────────────────────────────────────────────────

    private void acceptRequest() {
        List<TechRequest> all = specialist.viewAllRequests();
        if (all.isEmpty()) {
            System.out.println("No requests assigned.");
            return;
        }
        TechRequest req = pickRequest(all, "accept");
        if (req != null) {
            specialist.acceptRequest(req);
        }
    }

    // ─── 4. Reject ───────────────────────────────────────────────────────────────

    private void rejectRequest() {
        List<TechRequest> all = specialist.viewAllRequests();
        if (all.isEmpty()) {
            System.out.println("No requests assigned.");
            return;
        }
        TechRequest req = pickRequest(all, "reject");
        if (req != null) {
            System.out.print("Rejection reason: ");
            String reason = readLine();
            specialist.rejectRequest(req, reason);
        }
    }

    // ─── 5. Mark DONE ────────────────────────────────────────────────────────────

    private void markDone() {
        List<TechRequest> accepted = specialist.viewRequestsByStatus(RequestStatus.ACCEPTED);
        if (accepted.isEmpty()) {
            System.out.println("No ACCEPTED requests to mark as done.");
            return;
        }
        System.out.println("\n--- ACCEPTED Requests ---");
        printNumberedRequests(accepted);
        System.out.print("Enter number to mark as DONE: ");
        int idx = readInt();
        if (idx < 1 || idx > accepted.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        System.out.print("Resolution note: ");
        String note = readLine();
        specialist.markAsDone(accepted.get(idx - 1), note);
    }

    // ─── 6. Filter by status ─────────────────────────────────────────────────────

    private void filterByStatus() {
        System.out.println("\nAvailable statuses: NEW, VIEWED, ACCEPTED, REJECTED, DONE");
        System.out.print("Enter status: ");
        RequestStatus status;
        try {
            status = RequestStatus.valueOf(readLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Unknown status.");
            return;
        }
        List<TechRequest> filtered = specialist.viewRequestsByStatus(status);
        if (filtered.isEmpty()) {
            System.out.println("No requests with status: " + status);
        } else {
            System.out.println("\n--- Requests with status " + status + " ---");
            printRequests(filtered);
        }
    }

    // ─── 8. Send message ─────────────────────────────────────────────────────────

    private void sendMessage() {
        List<Employee> employees = database.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        System.out.println("\n--- Employees ---");
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            System.out.println((i + 1) + ". " + e.getFullName() + " (" + e.getClass().getSimpleName() + ")");
        }
        System.out.print("Select employee: ");
        int idx = readInt() - 1;
        if (idx < 0 || idx >= employees.size()) {
            System.out.println("Invalid.");
            return;
        }
        System.out.print("Message: ");
        String text = readLine();
        specialist.sendMessage(employees.get(idx), text);
        System.out.println("Message sent.");
    }

    // ─── 9. View inbox ───────────────────────────────────────────────────────────

    private void viewInbox() {
        List<Message> inbox = specialist.getInbox();
        if (inbox.isEmpty()) {
            System.out.println("Inbox is empty.");
            return;
        }
        System.out.println("\n=== Inbox (" + inbox.size() + " messages) ===");
        for (Message msg : inbox) {
            System.out.println("  " + msg);
        }
    }

    // ─── helpers ─────────────────────────────────────────────────────────────────

    private TechRequest pickRequest(List<TechRequest> requests, String action) {
        printNumberedRequests(requests);
        System.out.print("Enter number to " + action + ": ");
        int idx = readInt();
        if (idx < 1 || idx > requests.size()) {
            System.out.println("Invalid selection.");
            return null;
        }
        return requests.get(idx - 1);
    }

    private void printRequests(List<TechRequest> requests) {
        for (TechRequest r : requests) {
            System.out.println("  " + r);
        }
    }

    private void printNumberedRequests(List<TechRequest> requests) {
        for (int i = 0; i < requests.size(); i++) {
            System.out.println((i + 1) + ". " + requests.get(i));
        }
    }

    private String readLine() {
        return scanner.nextLine().trim();
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
