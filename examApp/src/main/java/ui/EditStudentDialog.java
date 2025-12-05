package ui;

import logic.Student;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Диалоговое окно для редактирования данных студента.
 * Позволяет изменить ФИО и оценку существующего студента.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class EditStudentDialog extends JDialog {
    private JTextField fullNameField;      // Поле для ФИО
    private JComboBox<String> scoreCombo;  // Выбор оценки
    private JLabel resultLabel;            // Метка результата
    private JButton saveButton;            // Кнопка "Сохранить"
    private JButton cancelButton;          // Кнопка "Отменить"
    private boolean approved = false;      // Флаг подтверждения
    private Student student;               // Редактируемый студент

    /**
     * Создает диалоговое окно для редактирования студента.
     *
     * @param parent        - родительское окно
     * @param studentToEdit - студент для редактирования
     */
    public EditStudentDialog(JFrame parent, Student studentToEdit) {
        super(parent, "Редактировать студента", true);
        this.student = studentToEdit;
        initialize();
        setupLayout();
        setupListeners();
        pack();
        setLocationRelativeTo(parent); // Центрирование
    }

    /**
     * Инициализирует компоненты диалога.
     */
    private void initialize() {
        // Поле для редактирования ФИО
        fullNameField = new JTextField(25);
        fullNameField.setText(student.getFullName());

        // Комбобокс с оценками (0-10 или "Не указана")
        scoreCombo = new JComboBox<>();
        scoreCombo.addItem("Не указана"); // Вариант без оценки
        for (int i = 0; i <= 10; i++) {
            scoreCombo.addItem(String.valueOf(i)); // Оценки 0-10
        }

        // Устанавливаем текущую оценку студента
        if (student.hasGrade()) {
            scoreCombo.setSelectedItem(String.valueOf(student.getScore()));
        } else {
            scoreCombo.setSelectedIndex(0); // "Не указана"
        }

        // Метка для отображения результата
        resultLabel = new JLabel();
        resultLabel.setFont(new Font("Arial", Font.BOLD, 12));
        updateResultLabel(); // Обновляем результат

        // Кнопки управления
        saveButton = new JButton("Сохранить");
        cancelButton = new JButton("Отменить");

        // Устанавливаем фокус и выделяем весь текст
        fullNameField.requestFocusInWindow();
        fullNameField.selectAll();
    }

    /**
     * Настраивает расположение компонентов.
     */
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Поле ФИО
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("ФИО студента:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(fullNameField, gbc);

        // Выбор оценки
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Оценка (0-10):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(scoreCombo, gbc);

        // Результат
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Результат:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(resultLabel, gbc);

        // Правила оценивания
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        JLabel rulesLabel = new JLabel("<html><small>Критерии: 0-3 - не сдал, 4-10 - сдал. Оценка может быть не указана.</small></html>");
        rulesLabel.setForeground(Color.GRAY);
        mainPanel.add(rulesLabel, gbc);

        // Панель с кнопками
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    /**
     * Настраивает обработчики событий.
     */
    private void setupListeners() {
        // При изменении оценки обновляем результат
        scoreCombo.addActionListener(e -> updateResultLabel());

        // Кнопка "Сохранить"
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                approved = true;
                updateStudent();
                dispose(); // Закрываем окно
            }
        });

        // Кнопка "Отменить"
        cancelButton.addActionListener(e -> dispose());

        // Enter в поле ФИО = нажатие кнопки "Сохранить"
        fullNameField.addActionListener(e -> saveButton.doClick());
    }

    /**
     * Обновляет метку результата в зависимости от выбранной оценки.
     */
    private void updateResultLabel() {
        String selected = (String) scoreCombo.getSelectedItem();

        if ("Не указана".equals(selected)) {
            resultLabel.setText("Ожидает оценки");
            resultLabel.setForeground(Color.GRAY);
        } else {
            try {
                int score = Integer.parseInt(selected);
                if (score >= 4) {
                    resultLabel.setText("Сдал");
                    resultLabel.setForeground(Color.GREEN.darker());
                } else {
                    resultLabel.setText("Не сдал");
                    resultLabel.setForeground(Color.RED);
                }
            } catch (NumberFormatException e) {
                resultLabel.setText("Ошибка оценки");
                resultLabel.setForeground(Color.ORANGE);
            }
        }
    }

    /**
     * Проверяет корректность введенных данных.
     *
     * @return true - если данные корректны
     */
    private boolean validateInput() {
        if (fullNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите ФИО студента", "Ошибка", JOptionPane.ERROR_MESSAGE);
            fullNameField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Обновляет данные студента на основе введенных значений.
     */
    private void updateStudent() {
        String fullName = fullNameField.getText().trim();
        String selectedScore = (String) scoreCombo.getSelectedItem();

        if ("Не указана".equals(selectedScore)) {
            // Устанавливаем студента без оценки
            student.setFullName(fullName);
            student.setScore(-1); // -1 означает "оценка не указана"
        } else {
            try {
                int score = Integer.parseInt(selectedScore);
                student.setFullName(fullName);
                student.setScore(score);
            } catch (NumberFormatException e) {
                student.setFullName(fullName);
                student.setScore(-1); // По умолчанию без оценки
            }
        }
    }

    /**
     * Проверяет, было ли окно подтверждено.
     *
     * @return true - если нажата "Сохранить"
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * Возвращает отредактированного студента.
     *
     * @return Student - объект студента
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Статический метод для отображения диалога редактирования.
     *
     * @param parent        - родительское окно
     * @param studentToEdit - студент для редактирования
     * @return Student - отредактированный студент или null при отмене
     */
    public static Student showDialog(JFrame parent, Student studentToEdit) {
        EditStudentDialog dialog = new EditStudentDialog(parent, studentToEdit);
        dialog.setVisible(true);
        return dialog.isApproved() ? dialog.getStudent() : null;
    }
}