package users;

import java.util.ArrayList;
import java.util.List;

public abstract class Employee extends User {

    private double salary;
    private String department;
    private List<Message> inbox;
    private List<Message> sentMessages;

    public Employee(String userId, String firstName, String lastName, String email, String password, String department) {
        super(userId, firstName, lastName, email, password);
        this.department = department;
        this.inbox = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
    }

    public void sendMessage(Employee receiver, String text) {
        Message message = new Message(this, receiver, text);
        this.sentMessages.add(message);
        receiver.receiveMessage(message);
    }

    public void receiveMessage(Message message) {
        this.inbox.add(message);
    }

    public List<Message> getInbox() {
        return inbox;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    // Getters and Setters
    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return super.toString() + " | Dept: " + department;
    }
}
