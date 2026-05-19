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

    public StudentOrganization(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName()             { return name; }
    public String getDescription()      { return description; }
    public List<Student> getMembers()   { return members; }
    public int getMemberCount()         { return members.size(); }

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
