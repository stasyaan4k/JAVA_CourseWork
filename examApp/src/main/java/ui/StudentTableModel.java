package ui;

import logic.Student;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель данных для таблицы студентов.
 * Поддерживает inline-редактирование ячеек.
 *
 * @version 3.0
 * @author Маленков Станислав Владимирович
 */
public class StudentTableModel extends AbstractTableModel {
    private List<Student> students;
    private final String[] columnNames = {"№", "ФИО студента", "Оценка", "Результат"};

    // Ссылка на главное окно для уведомления об изменениях
    private MainWindow mainWindow;

    public StudentTableModel() {
        this.students = new ArrayList<>();
    }

    /**
     * Устанавливает ссылку на главное окно.
     */
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void addStudent(Student student) {
        students.add(student);
        fireTableRowsInserted(students.size() - 1, students.size() - 1);
        notifyDataModified();
    }

    public void removeStudent(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            students.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
            notifyDataModified();
        }
    }

    public void updateStudent(int rowIndex, Student student) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            students.set(rowIndex, student);
            fireTableRowsUpdated(rowIndex, rowIndex);
            notifyDataModified();
        }
    }

    public Student getStudent(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            return students.get(rowIndex);
        }
        return null;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch (columnIndex) {
            case 0: return rowIndex + 1; // № (не редактируемый)
            case 1: return student.getFullName(); // ФИО
            case 2: return student.hasGrade() ? student.getScore() : "Нет оценки"; // Оценка
            case 3: return student.getResultText(); // Результат (не редактируемый)
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Разрешаем редактирование только столбцов 1 (ФИО) и 2 (Оценка)
        return columnIndex == 1 || columnIndex == 2;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);

        if (value == null) return;

        switch (columnIndex) {
            case 1: // Редактирование ФИО
                String newName = value.toString().trim();
                if (!newName.isEmpty() && !newName.equals(student.getFullName())) {
                    student.setFullName(newName);
                    fireTableCellUpdated(rowIndex, columnIndex);
                    // Автоматически обновляем результат
                    fireTableCellUpdated(rowIndex, 3);
                    notifyDataModified();
                }
                break;

            case 2: // Редактирование оценки
                try {
                    if (value instanceof Integer) {
                        int score = (Integer) value;
                        if (score >= 0 && score <= 10 && score != student.getScore()) {
                            student.setScore(score);
                            fireTableCellUpdated(rowIndex, columnIndex);
                            fireTableCellUpdated(rowIndex, 3);
                            notifyDataModified();
                        }
                    } else if (value instanceof String) {
                        String strValue = ((String) value).trim();
                        if (strValue.equalsIgnoreCase("нет оценки") || strValue.isEmpty()) {
                            if (student.hasGrade()) {
                                student.setScore(-1);
                                fireTableCellUpdated(rowIndex, columnIndex);
                                fireTableCellUpdated(rowIndex, 3);
                                notifyDataModified();
                            }
                        } else {
                            int score = Integer.parseInt(strValue);
                            if (score >= 0 && score <= 10 && score != student.getScore()) {
                                student.setScore(score);
                                fireTableCellUpdated(rowIndex, columnIndex);
                                fireTableCellUpdated(rowIndex, 3);
                                notifyDataModified();
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    // Неверный формат числа - игнорируем
                }
                break;
        }
    }

    /**
     * Уведомляет главное окно об изменении данных.
     */
    private void notifyDataModified() {
        if (mainWindow != null) {
            // Используем рефлексию для вызова protected метода
            try {
                java.lang.reflect.Method method = MainWindow.class.getDeclaredMethod("markDataModified");
                method.setAccessible(true);
                method.invoke(mainWindow);
            } catch (Exception e) {
                // Игнорируем, если не получилось вызвать
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Integer.class;
        } else if (columnIndex == 2) {
            return Object.class; // Может быть Integer или String
        }
        return String.class;
    }

    public void clear() {
        int size = students.size();
        if (size > 0) {
            students.clear();
            fireTableRowsDeleted(0, size - 1);
            notifyDataModified();
        }
    }
}