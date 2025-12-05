package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

/**
 * Редактор ячеек таблицы для выбора оценок.
 * Использует выпадающий список с предопределенными значениями.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class GradeComboBoxEditor extends DefaultCellEditor {
    private JComboBox<String> comboBox; // Выпадающий список с оценками

    /**
     * Создает редактор ячеек с выпадающим списком оценок.
     */
    public GradeComboBoxEditor() {
        super(new JComboBox<>());
        this.comboBox = (JComboBox<String>) getComponent();

        // Добавляем варианты оценок в логическом порядке
        String[] grades = {"незачет", "зачет", "2", "3", "4", "5"};
        for (String grade : grades) {
            comboBox.addItem(grade);
        }

        // Запрещаем ручной ввод - только выбор из списка
        comboBox.setEditable(false);
    }

    /**
     * Возвращает компонент для редактирования ячейки.
     *
     * @param table      - таблица
     * @param value      - текущее значение ячейки
     * @param isSelected - выбрана ли ячейка
     * @param row        - номер строки
     * @param column     - номер столбца
     * @return Component - компонент для редактирования
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        // Устанавливаем текущее значение в комбобокс
        comboBox.setSelectedItem(value);
        return comboBox;
    }

    /**
     * Возвращает значение после редактирования.
     *
     * @return Object - выбранное значение
     */
    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }
}