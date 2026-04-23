package enums;

public enum GradeLetter {
    A(4.0, 95),
    A_MINUS(3.67, 90),
    B_PLUS(3.33, 85),
    B(3.0, 80),
    B_MINUS(2.67, 75),
    C_PLUS(2.33, 70),
    C(2.0, 65),
    C_MINUS(1.67, 60),
    D_PLUS(1.33, 55),
    D(1.0, 50),
    F(0.0, 0);

    private final double gpaValue;
    private final int minPercentage;

    GradeLetter(double gpaValue, int minPercentage) {
        this.gpaValue = gpaValue;
        this.minPercentage = minPercentage;
    }

    public double getGpaValue() {
        return gpaValue;
    }

    public int getMinPercentage() {
        return minPercentage;
    }

    public static GradeLetter fromPercentage(double percentage) {
        if (percentage >= 95) return A;
        if (percentage >= 90) return A_MINUS;
        if (percentage >= 85) return B_PLUS;
        if (percentage >= 80) return B;
        if (percentage >= 75) return B_MINUS;
        if (percentage >= 70) return C_PLUS;
        if (percentage >= 65) return C;
        if (percentage >= 60) return C_MINUS;
        if (percentage >= 55) return D_PLUS;
        if (percentage >= 50) return D;
        return F;
    }

    @Override
    public String toString() {
        return name().replace("_MINUS", "-").replace("_PLUS", "+");
    }
}
