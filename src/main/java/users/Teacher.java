package users;

import academics.Course;
import academics.Mark;
import enums.TeacherPosition;
import enums.UrgencyLevel;
import enums.School;
import research.*;
import java.util.*;

public class Teacher extends Employee implements Comparable<Teacher> {

    private TeacherPosition position;
    private School school;
    private List<Course> courses;
    private List<Integer> ratings;

    // Researcher fields (null if not a researcher)
    private boolean isResearcher;
    private List<ResearchPaper> researchPapers;
    private List<ResearchProject> researchProjects;

    public Teacher(String userId, String firstName, String lastName, String email, String password,
                   String department, TeacherPosition position, School school) {
        super(userId, firstName, lastName, email, password, department);
        this.position = position;
        this.school = school;
        this.courses = new ArrayList<>();
        this.ratings = new ArrayList<>();

        if (position == TeacherPosition.PROFESSOR) {
            this.isResearcher = true;
        } else {
            this.isResearcher = false;
        }
        this.researchPapers = new ArrayList<>();
        this.researchProjects = new ArrayList<>();
    }

    public List<Course> viewCourses() {
        return Collections.unmodifiableList(courses);
    }

    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    public void putMark(Student student, Course course, double att1, double att2, double finalExam) {
        if (!courses.contains(course)) {
            System.out.println("You don't teach " + course.getName());
            return;
        }
        Mark mark = student.viewMark(course);
        if (mark == null) {
            System.out.println(student.getFullName() + " is not enrolled in " + course.getName());
            return;
        }
        mark.setFirstAttestation(att1);
        mark.setSecondAttestation(att2);
        mark.setFinalExam(finalExam);

        if (!mark.isPassed()) {
            student.recordFail();
        }
    }

    public List<Student> viewStudents(Course course) {
        if (!courses.contains(course)) {
            System.out.println("You don't teach " + course.getName());
            return Collections.emptyList();
        }
        return course.getEnrolledStudents();
    }

    public Complaint sendComplaint(Student student, UrgencyLevel urgency, String reason) {
        Complaint complaint = new Complaint(this, student, urgency, reason);
        System.out.println("Complaint sent about " + student.getFullName() + " with urgency " + urgency);
        return complaint;
    }

    public void addRating(int rating) {
        ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) return 0.0;
        int sum = 0;
        for (int r : ratings) {
            sum += r;
        }
        return (double) sum / ratings.size();
    }


    public void becomeResearcher() {
        this.isResearcher = true;
    }

    public boolean isResearcher() {
        return isResearcher;
    }

    public void addResearchPaper(ResearchPaper paper) {
        if (!isResearcher) {
            System.out.println(getFullName() + " is not a researcher.");
            return;
        }
        if (!researchPapers.contains(paper)) {
            researchPapers.add(paper);
        }
    }

    public void joinResearchProject(ResearchProject project) throws NotResearcherException {
        if (!isResearcher) {
            throw new NotResearcherException(getFullName() + " is not a researcher and cannot join a research project.");
        }
        if (!researchProjects.contains(project)) {
            researchProjects.add(project);
            project.addParticipant(this);
        }
    }

    public int calculateHIndex() {
        List<Integer> citationCounts = new ArrayList<>();
        for (ResearchPaper paper : researchPapers) {
            citationCounts.add(paper.getCitations());
        }
        citationCounts.sort(Collections.reverseOrder());

        int h = 0;
        for (int i = 0; i < citationCounts.size(); i++) {
            if (citationCounts.get(i) >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
    }

    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(comparator);
        System.out.println("=== Research Papers of " + getFullName() + " ===");
        for (ResearchPaper paper : sorted) {
            System.out.println("  " + paper);
        }
    }

    public List<ResearchPaper> getResearchPapers() {
        return researchPapers;
    }

    public List<ResearchProject> getResearchProjects() {
        return researchProjects;
    }

    // Sort teachers by average rating (descending).
    @Override
    public int compareTo(Teacher other) {
        return Double.compare(other.getAverageRating(), this.getAverageRating());
    }

    // Getters and Setters
    public TeacherPosition getPosition() { return position; }
    public void setPosition(TeacherPosition position) { this.position = position; }
    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }
    public List<Course> getCourses() { return courses; }
    public List<Integer> getRatings() { return ratings; }

    @Override
    public String toString() {
        return super.toString() + " | " + position + " | " + school +
               " | Rating: " + String.format("%.1f", getAverageRating()) +
               (isResearcher ? " | h-index: " + calculateHIndex() : "");
    }
}
