package app;

import enums.RequestStatus;
import system.Lang;
import system.TechRequest;
import users.Employee;
import users.Message;
import users.TechSupportSpecialist;

import java.util.List;
import java.util.Scanner;

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
            Lang.header(Lang.get("tech_menu") + " — " + specialist.getFullName());
            Lang.menuItem(1, "tech_new");
            Lang.menuItem(2, "tech_all");
            Lang.menuItem(3, "tech_accept");
            Lang.menuItem(4, "tech_reject");
            Lang.menuItem(5, "tech_done");
            Lang.menuItem(6, "tech_filter");
            Lang.menuItem(7, "tech_summary");
            Lang.menuItem(8, "mgr_send_msg");
            Lang.menuItem(9, "mgr_inbox");
            Lang.menuExit();
            Lang.separator();
            Lang.prompt();

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
                case 0 -> { return; }
                default -> Lang.err(Lang.get("invalid"));
            }
        }
    }

    private void viewNewRequests() {
        List<TechRequest> newReqs = specialist.viewNewRequests();
        if (newReqs.isEmpty()) Lang.info(Lang.get("empty"));
        else newReqs.forEach(r -> System.out.println("  " + r));
        database.log(specialist.getFullName(), "TECH_VIEW_NEW", "Viewed new requests");
    }

    private void viewAllRequests() {
        List<TechRequest> all = specialist.viewAllRequests();
        if (all.isEmpty()) Lang.info(Lang.get("empty"));
        else printNumberedRequests(all);
    }

    private void acceptRequest() {
        TechRequest req = pickRequest(specialist.viewAllRequests(), "accept");
        if (req != null) {
            specialist.acceptRequest(req);
            database.log(specialist.getFullName(), "TECH_ACCEPT", "Accepted request #" + req.getRequestId());
            Lang.ok(Lang.get("success"));
        }
    }

    private void rejectRequest() {
        TechRequest req = pickRequest(specialist.viewAllRequests(), "reject");
        if (req != null) {
            System.out.print("Reason: ");
            specialist.rejectRequest(req, scanner.nextLine().trim());
            database.log(specialist.getFullName(), "TECH_REJECT", "Rejected request #" + req.getRequestId());
            Lang.ok(Lang.get("success"));
        }
    }

    private void markDone() {
        List<TechRequest> accepted = specialist.viewRequestsByStatus(RequestStatus.ACCEPTED);
        TechRequest req = pickRequest(accepted, "mark DONE");
        if (req != null) {
            System.out.print("Resolution note: ");
            specialist.markAsDone(req, scanner.nextLine().trim());
            database.log(specialist.getFullName(), "TECH_DONE", "Finished request #" + req.getRequestId());
            Lang.ok(Lang.get("success"));
        }
    }

    private void filterByStatus() {
        System.out.print("Status (NEW, VIEWED, ACCEPTED, REJECTED, DONE): ");
        try {
            RequestStatus s = RequestStatus.valueOf(scanner.nextLine().trim().toUpperCase());
            List<TechRequest> filtered = specialist.viewRequestsByStatus(s);
            if (filtered.isEmpty()) Lang.info(Lang.get("empty"));
            else filtered.forEach(r -> System.out.println("  " + r));
        } catch (Exception e) { Lang.err("Invalid status."); }
    }

    private void sendMessage() {
        List<Employee> emps = database.getEmployees();
        for (int i = 0; i < emps.size(); i++) System.out.println((i + 1) + ". " + emps.get(i).getFullName());
        System.out.print(Lang.get("select") + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < emps.size()) {
            System.out.print(Lang.get("message") + ": ");
            specialist.sendMessage(emps.get(idx), scanner.nextLine().trim());
            database.log(specialist.getFullName(), "SEND_MSG", "Sent message to " + emps.get(idx).getFullName());
            Lang.ok(Lang.get("sent"));
        } else Lang.err(Lang.get("invalid"));
    }

    private void viewInbox() {
        List<Message> inbox = specialist.getInbox();
        if (inbox.isEmpty()) Lang.info(Lang.get("empty"));
        else inbox.forEach(m -> System.out.println("  " + m));
    }

    private TechRequest pickRequest(List<TechRequest> requests, String action) {
        if (requests.isEmpty()) { Lang.info(Lang.get("empty")); return null; }
        printNumberedRequests(requests);
        System.out.print("Select number to " + action + ": ");
        int idx = readInt() - 1;
        if (idx >= 0 && idx < requests.size()) return requests.get(idx);
        Lang.err(Lang.get("invalid")); return null;
    }

    private void printNumberedRequests(List<TechRequest> requests) {
        for (int i = 0; i < requests.size(); i++) System.out.println((i + 1) + ". " + requests.get(i));
    }
    private int readInt() { try { return Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) { return -1; } }
}
