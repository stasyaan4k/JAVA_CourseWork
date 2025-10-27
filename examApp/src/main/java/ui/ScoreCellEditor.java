package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ScoreCellEditor extends DefaultCellEditor {
    private JTextField textField;

    public ScoreCellEditor() {
        super(new JTextField());
        this.textField = (JTextField) getComponent();

        // Разрешаем только цифры
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        textField.setText(value != null ? value.toString() : "0");
        return textField;
    }

    @Override
    public Object getCellEditorValue() {
        try {
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            return 0; // Значение по умолчанию при ошибке
        }
    }
}