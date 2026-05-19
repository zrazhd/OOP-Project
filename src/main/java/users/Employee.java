package users;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the employee in the system.
 */
public abstract class Employee extends User {

    private double salary;
    private String department;
    private List<Message> inbox;
    private List<Message> sentMessages;

    /**
     * Constructor for Employee.
     * @param userId parameter value.
     * @param firstName parameter value.
     * @param lastName parameter value.
     * @param email parameter value.
     * @param password parameter value.
     * @param department parameter value.
     */
    public Employee(String userId, String firstName, String lastName, String email, String password, String department) {
        super(userId, firstName, lastName, email, password);
        this.department = department;
        this.inbox = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
    }

    /**
     * sendMessage.
     * @param receiver parameter value.
     * @param text parameter value.
     */
    public void sendMessage(Employee receiver, String text) {
        Message message = new Message(this, receiver, text);
        this.sentMessages.add(message);
        receiver.receiveMessage(message);
    }

    /**
     * receiveMessage.
     * @param message parameter value.
     */
    public void receiveMessage(Message message) {
        this.inbox.add(message);
    }

    /**
     * Gets the inbox.
     * @return List&lt;Message&gt;
     */
    public List<Message> getInbox() {
        return inbox;
    }

    /**
     * Gets the sent messages.
     * @return List&lt;Message&gt;
     */
    public List<Message> getSentMessages() {
        return sentMessages;
    }

    // Getters and Setters
    /**
     * Gets the salary.
     * @return double
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Sets the salary.
     * @param salary parameter value.
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Gets the department.
     * @return String
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department.
     * @param department parameter value.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return super.toString() + " | Dept: " + department;
    }
}
