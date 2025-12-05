package ui;

import logic.Student;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель данных для таблицы студентов.
 * Предоставляет данные для отображения в JTable.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class StudentTableModel extends AbstractTableModel {
    private List<Student> students;                 // Список студентов
    private final String[] columnNames = {"№", "ФИО студента", "Оценка", "Результат"}; // Заголовки столбцов

    /**
     * Создает пустую модель таблицы.
     */
    public StudentTableModel() {
        this.students = new ArrayList<>();
    }

    /**
     * Добавляет студента в таблицу.
     *
     * @param student - добавляемый студент
     */
    public void addStudent(Student student) {
        students.add(student);
        fireTableRowsInserted(students.size() - 1, students.size() - 1);
    }

    /**
     * Удаляет студента по индексу строки.
     *
     * @param rowIndex - индекс строки
     */
    public void removeStudent(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            students.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    /**
     * Обновляет данные студента.
     *
     * @param rowIndex - индекс строки
     * @param student  - новые данные студента
     */
    public void updateStudent(int rowIndex, Student student) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            students.set(rowIndex, student);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    /**
     * Возвращает студента по индексу строки.
     *
     * @param rowIndex - индекс строки
     * @return Student - объект студента
     */
    public Student getStudent(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            return students.get(rowIndex);
        }
        return null;
    }

    /**
     * Возвращает копию списка студентов.
     *
     * @return List<Student> - список студентов
     */
    public List<Student> getStudents() {
        return new ArrayList<>(students); // Защитная копия
    }

    /* Методы интерфейса TableModel */

    /**
     * Возвращает количество строк в таблице.
     */
    @Override
    public int getRowCount() {
        return students.size();
    }

    /**
     * Возвращает количество столбцов в таблице.
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Возвращает название столбца.
     *
     * @param column - индекс столбца
     * @return String - название столбца
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Возвращает значение ячейки таблицы.
     *
     * @param rowIndex    - индекс строки
     * @param columnIndex - индекс столбца
     * @return Object - значение ячейки
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch (columnIndex) {
            case 0: return rowIndex + 1; // № по порядку
            case 1: return student.getFullName(); // ФИО
            case 2: return student.hasGrade() ? student.getScore() : "Нет оценки"; // Оценка
            case 3: return student.getResultText(); // Результат
            default: return null;
        }
    }

    /**
     * Определяет, можно ли редактировать ячейку.
     * В данной реализации все ячейки не редактируемые.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Запрещаем редактирование
    }

    /**
     * Возвращает класс данных в столбце.
     *
     * @param columnIndex - индекс столбца
     * @return Class - класс данных столбца
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Integer.class; // № - целое число
        } else if (columnIndex == 2) {
            return Object.class; // Оценка - может быть Integer или String
        }
        return String.class; // Остальные столбцы - строки
    }

    /**
     * Очищает таблицу (удаляет всех студентов).
     */
    public void clear() {
        int size = students.size();
        if (size > 0) {
            students.clear();
            fireTableRowsDeleted(0, size - 1); // Уведомляем об удалении всех строк
        }
    }
}