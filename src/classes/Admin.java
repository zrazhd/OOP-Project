package classes;

import enums.Language;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Admin extends Employee implements Comparable<Admin> {
    private static final long serialVersionUID = 1L;

    private static final Map<String, User> userRegistry = new LinkedHashMap<>();

    private static final List<LogEntry> actionLog = new ArrayList<>();

    public static class LogEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String actorId;
        private final String actorName;
        private final String action;
        private final LocalDateTime timestamp;

        public LogEntry(String actorId, String actorName, String action) {
            this.actorId = actorId;
            this.actorName = actorName;
            this.action = action;
            this.timestamp = LocalDateTime.now();
        }

        public String getActorId() { return actorId; }
        public String getActorName() { return actorName; }
        public String getAction() { return action; }
        public LocalDateTime getTimestamp() { return timestamp; }

        @Override
        public String toString() {
            return "[" + timestamp.toString().replace('T', ' ').substring(0, 19) + "] "
                    + actorName + " (" + actorId + "): " + action;
        }
    }


    public Admin(String userId, String firstName, String lastName, String email,
                 String password, String department) {
        super(userId, firstName, lastName, email, password, department);
    }

    public static void log(User actor, String action) {
        actionLog.add(new LogEntry(actor.getUserId(), actor.getFullName(), action));
    }

    public static void log(String actorId, String actorName, String action) {
        actionLog.add(new LogEntry(actorId, actorName, action));
    }


    public void addUser(User user) {
        if (userRegistry.containsKey(user.getUserId())) {
            System.out.println("User with ID " + user.getUserId() + " already exists.");
            return;
        }
        userRegistry.put(user.getUserId(), user);
        log(this, "Added user: " + user.getFullName() + " [" + user.getUserId() + "]");
        System.out.println("User added: " + user.getFullName());
    }

    public boolean removeUser(String userId) {
        User removed = userRegistry.remove(userId);
        if (removed != null) {
            log(this, "Removed user: " + removed.getFullName() + " [" + userId + "]");
            System.out.println("User removed: " + removed.getFullName());
            return true;
        }
        System.out.println("User with ID " + userId + " not found.");
        return false;
    }

    public void updateUserEmail(String userId, String newEmail) {
        User user = findUserById(userId);
        if (user == null) return;
        String old = user.getEmail();
        user.setEmail(newEmail);
        log(this, "Updated email of " + user.getFullName() + " from " + old + " to " + newEmail);
        System.out.println("Email updated for " + user.getFullName());
    }

    public void resetUserPassword(String userId, String newPassword) {
        User user = findUserById(userId);
        if (user == null) return;
        user.setPassword(newPassword);
        log(this, "Reset password for user: " + user.getFullName() + " [" + userId + "]");
        System.out.println("Password reset for " + user.getFullName());
    }

    public void updateUserLanguage(String userId, Language language) {
        User user = findUserById(userId);
        if (user == null) return;
        user.switchLanguage(language);
        log(this, "Changed language of " + user.getFullName() + " to " + language);
        System.out.println("Language updated for " + user.getFullName());
    }

    public User findUserById(String userId) {
        User user = userRegistry.get(userId);
        if (user == null) {
            System.out.println("User not found: " + userId);
        }
        return user;
    }

    public List<User> searchUsersByName(String namePart) {
        String lower = namePart.toLowerCase();
        return userRegistry.values().stream()
                .filter(u -> u.getFullName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public <T extends User> List<T> getAllUsersOfType(Class<T> type) {
        return userRegistry.values().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public static Map<String, User> getUserRegistry() {
        return Collections.unmodifiableMap(userRegistry);
    }

    public List<LogEntry> viewAllLogs() {
        log(this, "Viewed all system logs");
        return Collections.unmodifiableList(actionLog);
    }

    public List<LogEntry> viewLogsForUser(String userId) {
        log(this, "Viewed logs for user ID: " + userId);
        return actionLog.stream()
                .filter(e -> e.getActorId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<LogEntry> viewRecentLogs(int count) {
        int size = actionLog.size();
        return Collections.unmodifiableList(
                actionLog.subList(Math.max(0, size - count), size)
        );
    }

    public String generateLogReport(int lastN) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== SYSTEM LOG REPORT ==========\n");
        sb.append("Generated by: ").append(getFullName()).append("\n");
        sb.append("Showing last ").append(lastN).append(" entries of ").append(actionLog.size()).append(" total:\n");
        sb.append("---------------------------------------\n");
        List<LogEntry> recent = viewRecentLogs(lastN);
        recent.forEach(e -> sb.append(e).append("\n"));
        sb.append("=======================================\n");
        return sb.toString();
    }

    public void saveUsersToFile(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(new LinkedHashMap<>(userRegistry));
            log(this, "Saved user registry to: " + filePath);
            System.out.println("User registry saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save user registry: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadUsersFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Map<String, User> loaded = (Map<String, User>) ois.readObject();
            userRegistry.putAll(loaded);
            log(this, "Loaded user registry from: " + filePath);
            System.out.println("Loaded " + loaded.size() + " users from " + filePath);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load user registry: " + e.getMessage());
        }
    }

    @Override
    public int compareTo(Admin other) {
        return this.getFullName().compareToIgnoreCase(other.getFullName());
    }

    @Override
    public String toString() {
        return super.toString() + " | ADMIN | Registry size: " + userRegistry.size();
    }
}