package logic;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private int score; // 0-10 баллов
    private boolean passed;

    public Student(String fullName, int score) {
        this.fullName = fullName;
        this.score = score;
        this.passed = calculatePassed();
    }

    private boolean calculatePassed() {
        // Сдал если оценка >= 4
        return score >= 4;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        this.passed = calculatePassed();
    }

    public boolean isPassed() {
        return passed;
    }

    public String getResultText() {
        return passed ? "Сдал" : "Не сдал";
    }
}