package users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student organization that students can join/leave.
 */
public class StudentOrganization implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String description;
    private final List<Student> members = new ArrayList<>();

    /**
     * Constructor for StudentOrganization.
     * @param name parameter value.
     * @param description parameter value.
     */
    public StudentOrganization(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Gets the name.
     * @return String
     */
    public String getName()             { return name; }
    /**
     * Gets the description.
     * @return String
     */
    public String getDescription()      { return description; }
    /**
     * Gets the members.
     * @return List&lt;Student&gt;
     */
    public List<Student> getMembers()   { return members; }
    /**
     * Gets the member count.
     * @return int
     */
    public int getMemberCount()         { return members.size(); }

    /**
     * join.
     * @param student parameter value.
     * @return boolean
     */
    public boolean join(Student student) {
        if (members.contains(student)) {
            System.out.println("  ⚠ " + student.getFullName() + " is already a member of " + name);
            return false;
        }
        members.add(student);
        student.joinOrganization(name, false);
        System.out.println("  ✓ " + student.getFullName() + " joined " + name);
        return true;
    }

    /**
     * leave.
     * @param student parameter value.
     * @return boolean
     */
    public boolean leave(Student student) {
        if (!members.contains(student)) {
            System.out.println("  ⚠ " + student.getFullName() + " is not a member of " + name);
            return false;
        }
        members.remove(student);
        student.leaveOrganization(name);
        System.out.println("  ✓ " + student.getFullName() + " left " + name);
        return true;
    }

    @Override
    public String toString() {
        return String.format("🏛 %-25s │ Members: %d │ %s", name, members.size(), description);
    }
}
