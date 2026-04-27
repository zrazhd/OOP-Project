package academics;

import enums.GradeLetter;
import java.io.Serializable;

public class Mark implements Serializable {

    private double firstAttestation;   // max 30
    private double secondAttestation;  // max 30
    private double finalExam;          // max 40
    private Course course;

    public Mark(Course course) {
        this.course = course;
        this.firstAttestation = 0;
        this.secondAttestation = 0;
        this.finalExam = 0;
    }

    public double getTotal() {
        return firstAttestation + secondAttestation + finalExam;
    }

    public GradeLetter getGradeLetter() {
        return GradeLetter.fromPercentage(getTotal());
    }

    public double getGpaValue() {
        return getGradeLetter().getGpaValue();
    }

    public boolean isPassed() {
        return getTotal() >= 50;
    }

    // Getters and Setters
    public double getFirstAttestation() { return firstAttestation; }

    public void setFirstAttestation(double firstAttestation) {
        if (firstAttestation < 0 || firstAttestation > 30) {
            throw new IllegalArgumentException("First attestation must be between 0 and 30");
        }
        this.firstAttestation = firstAttestation;
    }

    public double getSecondAttestation() { return secondAttestation; }

    public void setSecondAttestation(double secondAttestation) {
        if (secondAttestation < 0 || secondAttestation > 30) {
            throw new IllegalArgumentException("Second attestation must be between 0 and 30");
        }
        this.secondAttestation = secondAttestation;
    }

    public double getFinalExam() { return finalExam; }

    public void setFinalExam(double finalExam) {
        if (finalExam < 0 || finalExam > 40) {
            throw new IllegalArgumentException("Final exam must be between 0 and 40");
        }
        this.finalExam = finalExam;
    }

    public Course getCourse() { return course; }

    @Override
    public String toString() {
        return course.getName() + ": " + getTotal() + " (" + getGradeLetter() + ")";
    }
}
