package ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * Кастомный редактор ячеек для выбора оценки.
 * Использует комбобокс для выбора оценки (0-10 или "Нет оценки").
 *
 * @version 3.0
 * @author Маленков Станислав Владимирович
 */
public class GradeCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox<String> comboBox;
    private Object currentValue;

    public GradeCellEditor() {
        comboBox = new JComboBox<>();

        // Добавляем варианты оценок
        comboBox.addItem("Нет оценки");
        for (int i = 0; i <= 10; i++) {
            comboBox.addItem(String.valueOf(i));
        }

        // Настройка внешнего вида
        comboBox.setEditable(false);
        comboBox.setBackground(Color.WHITE);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 12));

        // При выборе значения завершаем редактирование
        comboBox.addActionListener(e -> {
            fireEditingStopped();
        });

        // Завершаем редактирование при потере фокуса
        comboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                fireEditingStopped();
            }
        });

        // Завершаем редактирование по нажатию Enter
        comboBox.registerKeyboardAction(e -> {
            fireEditingStopped();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Отменяем редактирование по нажатию Escape
        comboBox.registerKeyboardAction(e -> {
            fireEditingCanceled();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        // Сохраняем текущее значение
        currentValue = value;

        // Устанавливаем выбранный элемент в комбобокс
        if (value instanceof Integer) {
            comboBox.setSelectedItem(String.valueOf(value));
        } else if (value instanceof String) {
            comboBox.setSelectedItem(value);
        } else {
            comboBox.setSelectedItem("Нет оценки");
        }

        // Выделяем текст для удобства
        comboBox.requestFocus();

        return comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        Object selected = comboBox.getSelectedItem();

        if (selected == null || "Нет оценки".equals(selected)) {
            return "Нет оценки";
        }

        try {
            return Integer.parseInt(selected.toString());
        } catch (NumberFormatException e) {
            return "Нет оценки";
        }
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject e) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }
}