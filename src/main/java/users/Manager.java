package users;

import academics.Course;
import academics.Mark;
import enums.ManagerType;
import enums.NewsType;
import enums.School;
import system.News;
import system.TechRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the manager in the system.
 */
public class Manager extends Employee {

    private ManagerType managerType;
    private List<Course> managedCourses;
    private List<News> newsList;

    public Manager(String userId, String firstName, String lastName,
                   String email, String password, String department,
                   ManagerType managerType) {
        super(userId, firstName, lastName, email, password, department);
        this.managerType = managerType;
        this.managedCourses = new ArrayList<>();
        this.newsList = new ArrayList<>();
    }


    /**
     * addCourseForRegistration.
     * @param course parameter value.
     */
    public void addCourseForRegistration(Course course) {
        if (!managedCourses.contains(course)) {
            managedCourses.add(course);
            System.out.println("[Manager] Course added: " + course.getName());
        }
    }

    /**
     * removeCourseFromRegistration.
     * @param course parameter value.
     */
    public void removeCourseFromRegistration(Course course) {
        managedCourses.remove(course);
        System.out.println("[Manager] Course removed: " + course.getName());
    }

    /**
     * assignLectureTeacher.
     * @param course parameter value.
     * @param teacher parameter value.
     */
    public void assignLectureTeacher(Course course, Teacher teacher) {
        course.addLectureTeacher(teacher);
        teacher.addCourse(course);
        System.out.println("[Manager] " + teacher.getFullName()
                + " → lecture teacher for " + course.getName());
    }

    /**
     * assignPracticeTeacher.
     * @param course parameter value.
     * @param teacher parameter value.
     */
    public void assignPracticeTeacher(Course course, Teacher teacher) {
        course.addPracticeTeacher(teacher);
        teacher.addCourse(course);
        System.out.println("[Manager] " + teacher.getFullName()
                + " → practice teacher for " + course.getName());
    }

    /**
     * approveStudentRegistration.
     * @param student parameter value.
     * @param course parameter value.
     */
    public void approveStudentRegistration(Student student, Course course) {
        try {
            student.registerForCourse(course);
            System.out.println("[Manager] Approved: "
                    + student.getFullName() + " → " + course.getName());
        } catch (Exception e) {
            System.out.println("[Manager] Cannot approve: " + e.getMessage());
        }
    }


    /**
     * generateCourseReport.
     * @param course parameter value.
     * @return String
     */
    public String generateCourseReport(Course course) {
        StringBuilder sb = new StringBuilder();
        sb.append("====== COURSE REPORT: ").append(course.getName()).append(" ======\n");
        sb.append("Course ID: ").append(course.getCourseId()).append("\n");
        sb.append("Credits: ").append(course.getCredits()).append("\n");
        sb.append("Enrolled: ").append(course.getEnrolledStudents().size()).append("\n");
        sb.append("---------------------------------------\n");

        int passed = 0, failed = 0;
        double totalGpa = 0;
        for (Student s : course.getEnrolledStudents()) {
            Mark m = s.viewMark(course);
            if (m != null) {
                sb.append(String.format("  %-28s  %5.1f  %s%n",
                        s.getFullName(), m.getTotal(), m.getGradeLetter()));
                if (m.isPassed()) passed++; else failed++;
                totalGpa += s.calculateGPA();
            }
        }

        int total = course.getEnrolledStudents().size();
        sb.append("---------------------------------------\n");
        sb.append("Passed: ").append(passed).append(" | Failed: ").append(failed).append("\n");
        if (total > 0)
            sb.append(String.format("Avg GPA: %.2f%n", totalGpa / total));
        sb.append("=======================================\n");
        return sb.toString();
    }

    /**
     * generateSchoolReport.
     * @param students parameter value.
     * @param school parameter value.
     * @return String
     */
    public String generateSchoolReport(List<Student> students, School school) {
        List<Student> filtered = students.stream()
                .filter(s -> s.getSchool() == school)
                .sorted()  // Student implements Comparable by GPA desc
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("====== SCHOOL REPORT: ").append(school).append(" ======\n");
        sb.append(String.format("%-4s  %-28s  %s%n", "Rank", "Name", "GPA"));
        sb.append("------------------------------------------\n");
        int rank = 1;
        for (Student s : filtered) {
            sb.append(String.format("%-4d  %-28s  %.2f%n",
                    rank++, s.getFullName(), s.calculateGPA()));
        }
        return sb.toString();
    }

    // Sorting utilities 

    /**
     * sortStudentsByName.
     * @param students parameter value.
     * @return List&lt;Student&gt;
     */
    public List<Student> sortStudentsByName(List<Student> students) {
        return students.stream()
                .sorted(Comparator.comparing(User::getFullName))
                .collect(Collectors.toList());
    }

    /**
     * sortStudentsByGpa.
     * @param students parameter value.
     * @return List&lt;Student&gt;
     */
    public List<Student> sortStudentsByGpa(List<Student> students) {
        return students.stream().sorted().collect(Collectors.toList());
    }

    /**
     * sortTeachersByRating.
     * @param teachers parameter value.
     * @return List&lt;Teacher&gt;
     */
    public List<Teacher> sortTeachersByRating(List<Teacher> teachers) {
        return teachers.stream().sorted().collect(Collectors.toList());
    }


    /**
     * createNews.
     * @param title parameter value.
     * @param content parameter value.
     * @param type parameter value.
     * @return News
     */
    public News createNews(String title, String content, NewsType type) {
        News news = new News(title, content, type, this);
        newsList.add(news);
        System.out.println("[Manager] News created: " + title);
        return news;
    }

    /**
     * deleteNews.
     * @param news parameter value.
     */
    public void deleteNews(News news) {
        newsList.remove(news);
    }

    /**
     * Gets the news sorted.
     * @return List&lt;News&gt;
     */
    public List<News> getNewsSorted() {
        return newsList.stream()
                .sorted(Comparator.comparing(News::isPinned).reversed()
                        .thenComparing(Comparator.comparing(News::getPublishedAt).reversed()))
                .collect(Collectors.toList());
    }


    /**
     * viewSignedRequests.
     * @param allRequests parameter value.
     * @return List&lt;TechRequest&gt;
     */
    public List<TechRequest> viewSignedRequests(List<TechRequest> allRequests) {
        return allRequests.stream()
                .filter(TechRequest::isSignedByDean)
                .collect(Collectors.toList());
    }

    // Getters 

    /**
     * Gets the manager type.
     * @return ManagerType
     */
    public ManagerType getManagerType() { return managerType; }
    public void setManagerType(ManagerType t) { this.managerType = t; }
    /**
     * Gets the managed courses.
     * @return List&lt;Course&gt;
     */
    public List<Course> getManagedCourses() { return managedCourses; }
    /**
     * Gets the news list.
     * @return List&lt;News&gt;
     */
    public List<News> getNewsList() { return newsList; }

    @Override
    public String toString() {
        return super.toString() + " | Manager [" + managerType + "]";
    }
}