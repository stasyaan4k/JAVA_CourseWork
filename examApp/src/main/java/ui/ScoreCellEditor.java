package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Редактор ячеек таблицы для ввода числовых оценок.
 * Разрешает ввод только цифр и обрабатывает ошибки ввода.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class ScoreCellEditor extends DefaultCellEditor {
    private JTextField textField; // Текстовое поле для ввода

    /**
     * Создает редактор ячеек для числовых оценок.
     */
    public ScoreCellEditor() {
        super(new JTextField());
        this.textField = (JTextField) getComponent();

        // Добавляем обработчик клавиш для фильтрации ввода
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Разрешаем только цифры и управляющие клавиши
                if (!Character.isDigit(c) &&
                        c != KeyEvent.VK_BACK_SPACE &&
                        c != KeyEvent.VK_DELETE) {
                    e.consume(); // Блокируем ввод
                }
            }
        });
    }

    /**
     * Возвращает компонент для редактирования ячейки.
     *
     * @param table      - таблица
     * @param value      - текущее значение ячейки
     * @param isSelected - выбрана ли ячейка
     * @param row        - номер строки
     * @param column     - номер столбца
     * @return Component - текстовое поле для ввода
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        // Устанавливаем текущее значение или "0" по умолчанию
        textField.setText(value != null ? value.toString() : "0");
        return textField;
    }

    /**
     * Возвращает значение после редактирования.
     *
     * @return Object - целочисленное значение или 0 при ошибке
     */
    @Override
    public Object getCellEditorValue() {
        try {
            // Пробуем преобразовать в число
            return Integer.parseInt(textField.getText());
        } catch (NumberFormatException e) {
            // При ошибке возвращаем 0
            return 0;
        }
    }
}