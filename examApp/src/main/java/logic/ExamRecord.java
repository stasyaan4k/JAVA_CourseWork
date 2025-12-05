package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения экзаменационной ведомости.
 * Содержит информацию о предмете, дате и списке студентов.
 * Поддерживает сериализацию для сохранения в файл.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class ExamRecord implements Serializable {
    private static final long serialVersionUID = 1L; // Для сериализации

    private String subject;           // Название предмета
    private String date;              // Дата проведения
    private List<Student> students;   // Список студентов

    /**
     * Создает новую экзаменационную ведомость.
     *
     * @param subject - название предмета
     * @param date    - дата проведения
     */
    public ExamRecord(String subject, String date) {
        this.subject = subject;
        this.date = date;
        this.students = new ArrayList<>(); // Инициализация пустого списка
    }

    /* Основные операции со студентами */

    /**
     * Добавляет студента в ведомость.
     *
     * @param student - добавляемый студент
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Удаляет студента по индексу.
     *
     * @param index - индекс студента в списке
     */
    public void removeStudent(int index) {
        if (index >= 0 && index < students.size()) {
            students.remove(index);
        }
    }

    /**
     * Возвращает копию списка студентов.
     *
     * @return List<Student> - список студентов
     */
    public List<Student> getStudents() {
        return new ArrayList<>(students); // Защитная копия
    }

    /* Статистические методы */

    /**
     * Возвращает общее количество студентов.
     *
     * @return int - количество студентов
     */
    public int getTotalStudents() {
        return students.size();
    }

    /**
     * Подсчитывает студентов с указанной оценкой.
     *
     * @return int - количество студентов с оценкой
     */
    public int getStudentsWithGrade() {
        return (int) students.stream().filter(Student::hasGrade).count();
    }

    /**
     * Подсчитывает сдавших студентов.
     * Учитываются только студенты с оценкой.
     *
     * @return int - количество сдавших
     */
    public int getPassedCount() {
        return (int) students.stream()
                .filter(Student::hasGrade)
                .filter(Student::isPassed)
                .count();
    }

    /**
     * Подсчитывает не сдавших студентов.
     * Учитываются только студенты с оценкой.
     *
     * @return int - количество не сдавших
     */
    public int getFailedCount() {
        return (int) students.stream()
                .filter(Student::hasGrade)
                .filter(s -> !s.isPassed())
                .count();
    }

    /**
     * Подсчитывает студентов без оценки.
     *
     * @return int - количество студентов без оценки
     */
    public int getStudentsWithoutGrade() {
        return (int) students.stream()
                .filter(s -> !s.hasGrade())
                .count();
    }

    /* Геттеры и сеттеры */

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}