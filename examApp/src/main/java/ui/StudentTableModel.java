package ui;

import logic.Student;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StudentTableModel extends AbstractTableModel {
    private List<Student> students;
    private final String[] columnNames = {"№", "ФИО студента", "Оценка", "Результат"};

    public StudentTableModel() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
        fireTableRowsInserted(students.size() - 1, students.size() - 1);
    }

    public void removeStudent(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            students.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public Student getStudent(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < students.size()) {
            return students.get(rowIndex);
        }
        return null;
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students); // Возвращаем копию для безопасности
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
            case 0: return rowIndex + 1;
            case 1: return student.getFullName();
            case 2: return student.getScore();
            case 3: return student.getResultText();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0 || columnIndex == 2) {
            return Integer.class;
        }
        return String.class;
    }

    public void clear() {
        int size = students.size();
        if (size > 0) {
            students.clear();
            fireTableRowsDeleted(0, size - 1);
        }
    }
}