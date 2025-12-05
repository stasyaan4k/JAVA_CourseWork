package logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для хранения результатов импорта из Excel.
 * Содержит данные о предмете, дате, email и списке студентов.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class ExcelImportResult {
    private String subject;           // Название предмета
    private String date;              // Дата аттестации
    private String email;             // Email для отправки
    private List<Student> students;   // Список студентов

    /**
     * Создает пустой результат импорта.
     */
    public ExcelImportResult() {
        this.students = new ArrayList<>();
    }

    /**
     * Создает результат импорта с данными.
     *
     * @param subject  - название предмета
     * @param date     - дата аттестации
     * @param email    - email для отправки
     * @param students - список студентов
     */
    public ExcelImportResult(String subject, String date, String email, List<Student> students) {
        this.subject = subject;
        this.date = date;
        this.email = email;
        this.students = students;
    }

    /* Геттеры и сеттеры */

    /**
     * Возвращает название предмета.
     *
     * @return String - название предмета
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Устанавливает название предмета.
     *
     * @param subject - новое название предмета
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Возвращает дату аттестации.
     *
     * @return String - дата аттестации
     */
    public String getDate() {
        return date;
    }

    /**
     * Устанавливает дату аттестации.
     *
     * @param date - новая дата аттестации
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Возвращает email для отправки.
     *
     * @return String - email адрес
     */
    public String getEmail() {
        return email;
    }

    /**
     * Устанавливает email для отправки.
     *
     * @param email - новый email адрес
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Возвращает список студентов.
     *
     * @return List<Student> - список студентов
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Устанавливает список студентов.
     *
     * @param students - новый список студентов
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /**
     * Проверяет, есть ли данные в результате.
     *
     * @return boolean - true если есть данные, false если нет
     */
    public boolean hasData() {
        return students != null && !students.isEmpty();
    }
}