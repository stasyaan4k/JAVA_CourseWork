package ui;

import logic.Student;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddStudentDialog extends JDialog {
    private JTextField fullNameField;
    private JComboBox<Integer> scoreCombo;
    private JLabel resultLabel;
    private JButton okButton;
    private JButton cancelButton;
    private boolean approved = false;
    private Student student;

    public AddStudentDialog(JFrame parent) {
        super(parent, "Добавить студента", true);
        initialize();
        setupLayout();
        setupListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initialize() {
        fullNameField = new JTextField(25);

        // Оценки от 0 до 10
        scoreCombo = new JComboBox<>();
        for (int i = 0; i <= 10; i++) {
            scoreCombo.addItem(i);
        }
        scoreCombo.setSelectedItem(4); // По умолчанию 4

        resultLabel = new JLabel("Сдал");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 12));
        updateResultLabel();

        okButton = new JButton("Добавить");
        cancelButton = new JButton("Отмена");

        fullNameField.requestFocusInWindow();
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ФИО
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("ФИО студента:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(fullNameField, gbc);

        // Оценка
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Оценка (0-10):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(scoreCombo, gbc);

        // Результат
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Результат:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        mainPanel.add(resultLabel, gbc);

        // Правила
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        JLabel rulesLabel = new JLabel("<html><small>Критерии: 0-3 - не сдал, 4-10 - сдал</small></html>");
        rulesLabel.setForeground(Color.GRAY);
        mainPanel.add(rulesLabel, gbc);

        // Кнопки
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void setupListeners() {
        // Обработчик смены оценки
        scoreCombo.addActionListener(e -> updateResultLabel());

        // ОК кнопка
        okButton.addActionListener(e -> {
            if (validateInput()) {
                approved = true;
                createStudent();
                dispose();
            }
        });

        // Отмена
        cancelButton.addActionListener(e -> dispose());

        // Enter в поле ФИО - добавление
        fullNameField.addActionListener(e -> okButton.doClick());
    }

    private void updateResultLabel() {
        int score = (Integer) scoreCombo.getSelectedItem();
        if (score >= 4) {
            resultLabel.setText("Сдал");
            resultLabel.setForeground(Color.GREEN.darker());
        } else {
            resultLabel.setText("Не сдал");
            resultLabel.setForeground(Color.RED);
        }
    }

    private boolean validateInput() {
        // Проверка ФИО
        if (fullNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите ФИО студента", "Ошибка", JOptionPane.ERROR_MESSAGE);
            fullNameField.requestFocus();
            return false;
        }

        return true;
    }

    private void createStudent() {
        String fullName = fullNameField.getText().trim();
        int score = (Integer) scoreCombo.getSelectedItem();

        student = new Student(fullName, score);
    }

    public boolean isApproved() {
        return approved;
    }

    public Student getStudent() {
        return student;
    }

    public static Student showDialog(JFrame parent) {
        AddStudentDialog dialog = new AddStudentDialog(parent);
        dialog.setVisible(true);
        return dialog.isApproved() ? dialog.getStudent() : null;
    }
}