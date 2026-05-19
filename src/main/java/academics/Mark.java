package academics;

import enums.GradeLetter;
import java.io.Serializable;

/**
 * Represents the mark in the system.
 */
public class Mark implements Serializable {

    private double firstAttestation;   // max 30
    private double secondAttestation;  // max 30
    private double finalExam;          // max 40
    private Course course;

    /**
     * Constructor for Mark.
     * @param course parameter value.
     */
    public Mark(Course course) {
        this.course = course;
        this.firstAttestation = 0;
        this.secondAttestation = 0;
        this.finalExam = 0;
    }

    /**
     * Gets the total.
     * @return double
     */
    public double getTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }

    /**
     * Gets the grade letter.
     * @return GradeLetter
     */
    public GradeLetter getGradeLetter() {
        return GradeLetter.fromPercentage(getTotal());
    }

    /**
     * Gets the gpa value.
     * @return double
     */
    public double getGpaValue() {
        return getGradeLetter().getGpaValue();
    }

    /**
     * Checks if passed.
     * @return boolean
     */
    public boolean isPassed() {
        return getTotal() >= 50;
    }

    // Getters and Setters
    /**
     * Gets the first attestation.
     * @return double
     */
    public double getFirstAttestation() { return firstAttestation; }

    /**
     * Sets the first attestation.
     * @param firstAttestation parameter value.
     */
    public void setFirstAttestation(double firstAttestation) {
        if (firstAttestation < 0 || firstAttestation > 30) {
            throw new IllegalArgumentException("First attestation must be between 0 and 30");
        }
        this.firstAttestation = firstAttestation;
    }

    /**
     * Gets the second attestation.
     * @return double
     */
    public double getSecondAttestation() { return secondAttestation; }

    /**
     * Sets the second attestation.
     * @param secondAttestation parameter value.
     */
    public void setSecondAttestation(double secondAttestation) {
        if (secondAttestation < 0 || secondAttestation > 30) {
            throw new IllegalArgumentException("Second attestation must be between 0 and 30");
        }
        this.secondAttestation = secondAttestation;
    }

    /**
     * Gets the final exam.
     * @return double
     */
    public double getFinalExam() { return finalExam; }

    /**
     * Sets the final exam.
     * @param finalExam parameter value.
     */
    public void setFinalExam(double finalExam) {
        if (finalExam < 0 || finalExam > 40) {
            throw new IllegalArgumentException("Final exam must be between 0 and 40");
        }
        this.finalExam = finalExam;
    }

    /**
     * Gets the course.
     * @return Course
     */
    public Course getCourse() { return course; }

    @Override
    public String toString() {
        return course.getName() + ": " + getTotal() + " (" + getGradeLetter() + ")";
    }
}
