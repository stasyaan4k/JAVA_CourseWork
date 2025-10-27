package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class GradeComboBoxEditor extends DefaultCellEditor {
    private JComboBox<String> comboBox;

    public GradeComboBoxEditor() {
        super(new JComboBox<>());
        this.comboBox = (JComboBox<String>) getComponent();

        // Варианты оценок в логическом порядке
        String[] grades = {"незачет", "зачет", "2", "3", "4", "5"};
        for (String grade : grades) {
            comboBox.addItem(grade);
        }

        // Настраиваем комбобокс для удобства использования
        comboBox.setEditable(false); // Запрещаем ручной ввод, только выбор из списка
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        comboBox.setSelectedItem(value);
        return comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }
}