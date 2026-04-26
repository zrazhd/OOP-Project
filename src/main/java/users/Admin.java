package users;

import system.LogEntry;
import java.time.LocalDateTime;
import java.util.*;

public class Admin extends Employee {

    private List<LogEntry> systemLogs;

    public Admin(String userId, String firstName, String lastName,
                 String email, String password, String department) {
        super(userId, firstName, lastName, email, password, department);
        this.systemLogs = new ArrayList<>();
    }


    public void addUser(List<User> userList, User user) {
        userList.add(user);
        log("ADD_USER", "Added: " + user.getFullName());
        System.out.println("[Admin] User added: " + user.getFullName());
    }

    public void removeUser(List<User> userList, String userId) {
        User found = findById(userList, userId);
        if (found != null) {
            userList.remove(found);
            log("REMOVE_USER", "Removed: " + found.getFullName());
            System.out.println("[Admin] Removed: " + found.getFullName());
        } else {
            System.out.println("[Admin] User not found: " + userId);
        }
    }

    public void updateEmail(List<User> userList, String userId, String newEmail) {
        User user = findById(userList, userId);
        if (user != null) {
            String old = user.getEmail();
            user.setEmail(newEmail);
            log("UPDATE_EMAIL", userId + ": " + old + " → " + newEmail);
            System.out.println("[Admin] Email updated for " + user.getFullName());
        } else {
            System.out.println("[Admin] User not found: " + userId);
        }
    }

    public void resetPassword(List<User> userList, String userId, String newPassword) {
        User user = findById(userList, userId);
        if (user != null) {
            user.setPassword(newPassword);
            log("RESET_PASSWORD", "Password reset for: " + userId);
            System.out.println("[Admin] Password reset for " + user.getFullName());
        } else {
            System.out.println("[Admin] User not found: " + userId);
        }
    }

    public User findById(List<User> userList, String userId) {
        for (User u : userList) {
            if (u.getUserId().equals(userId)) return u;
        }
        return null;
    }

    // ===== Logging =====

    public void log(String action, String details) {
        systemLogs.add(new LogEntry(getFullName(), action, details, LocalDateTime.now()));
    }

    public void printAllLogs() {
        System.out.println("====== SYSTEM LOGS (" + systemLogs.size() + ") ======");
        for (LogEntry e : systemLogs) System.out.println(e);
        System.out.println("======================================");
    }

    public List<LogEntry> getLogs() {
        return Collections.unmodifiableList(systemLogs);
    }

    public List<LogEntry> getLogsByAction(String action) {
        List<LogEntry> result = new ArrayList<>();
        for (LogEntry e : systemLogs) {
            if (e.getAction().equalsIgnoreCase(action)) result.add(e);
        }
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + " | Admin";
    }
}