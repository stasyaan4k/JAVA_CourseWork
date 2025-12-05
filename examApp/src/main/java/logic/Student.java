package logic;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private int score; // 0-10 баллов, -1 означает "оценка не указана"
    private boolean passed;
    private boolean hasGrade; // Флаг наличия оценки

    public Student(String fullName, int score) {
        this.fullName = fullName;
        this.score = score;
        this.hasGrade = (score >= 0 && score <= 10);
        this.passed = calculatePassed();
    }

    private boolean calculatePassed() {
        // Если оценка не указана, считаем что не сдал
        if (!hasGrade) {
            return false;
        }
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
        this.hasGrade = (score >= 0 && score <= 10);
        this.passed = calculatePassed();
    }

    public boolean isPassed() {
        return passed;
    }

    public boolean hasGrade() {
        return hasGrade;
    }

    public String getResultText() {
        if (!hasGrade) {
            return "Ожидает оценки";
        }
        return passed ? "Сдал" : "Не сдал";
    }

    public String getGradeText() {
        if (!hasGrade) {
            return "Нет оценки";
        }
        return String.valueOf(score);
    }
}