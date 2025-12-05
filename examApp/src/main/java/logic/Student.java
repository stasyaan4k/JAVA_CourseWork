package logic;

import java.io.Serializable;

/**
 * Класс, представляющий студента.
 * Хранит информацию о ФИО, оценке и результате аттестации.
 * Поддерживает сериализацию.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L; // Для сериализации

    private String fullName;      // ФИО студента
    private int score;            // Оценка (0-10, -1 = не указана)
    private boolean passed;       // Результат сдачи
    private boolean hasGrade;     // Флаг наличия оценки

    /**
     * Создает нового студента.
     *
     * @param fullName - ФИО студента
     * @param score    - оценка (0-10, -1 если не указана)
     */
    public Student(String fullName, int score) {
        this.fullName = fullName;
        this.score = score;
        this.hasGrade = (score >= 0 && score <= 10);
        this.passed = calculatePassed(); // Определяем результат
    }

    /**
     * Рассчитывает результат сдачи.
     *
     * @return boolean - true если сдал (оценка >= 4)
     */
    private boolean calculatePassed() {
        if (!hasGrade) {
            return false; // Без оценки - не сдал
        }
        return score >= 4; // Сдал если оценка >= 4
    }

    /* Геттеры и сеттеры */

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getScore() {
        return score;
    }

    /**
     * Устанавливает оценку и пересчитывает результат.
     */
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

    /**
     * Возвращает текстовое представление результата.
     *
     * @return String - "Сдал", "Не сдал" или "Ожидает оценки"
     */
    public String getResultText() {
        if (!hasGrade) {
            return "Ожидает оценки";
        }
        return passed ? "Сдал" : "Не сдал";
    }

    /**
     * Возвращает текстовое представление оценки.
     *
     * @return String - числовая оценка или "Нет оценки"
     */
    public String getGradeText() {
        if (!hasGrade) {
            return "Нет оценки";
        }
        return String.valueOf(score);
    }
}