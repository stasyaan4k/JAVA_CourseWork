package ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * Редактор ячеек для редактирования ФИО студентов.
 * Использует текстовое поле для ввода.
 *
 * @version 3.0
 * @author Маленков Станислав Владимирович
 */
public class NameCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JTextField textField;
    private Object currentValue;

    public NameCellEditor() {
        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));

        // Завершаем редактирование по нажатию Enter
        textField.addActionListener(e -> {
            fireEditingStopped();
        });

        // Завершаем редактирование при потере фокуса
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!e.isTemporary()) {
                    fireEditingStopped();
                }
            }
        });

        // Завершаем редактирование по нажатию Enter (дополнительно)
        textField.registerKeyboardAction(e -> {
            fireEditingStopped();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        // Отменяем редактирование по нажатию Escape
        textField.registerKeyboardAction(e -> {
            fireEditingCanceled();
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        currentValue = value;
        textField.setText(value != null ? value.toString() : "");
        textField.selectAll(); // Выделяем весь текст для удобства редактирования
        textField.requestFocus();
        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        String text = textField.getText().trim();
        return text.isEmpty() ? "Новый студент" : text;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return ((MouseEvent) e).getClickCount() >= 1;
        }
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