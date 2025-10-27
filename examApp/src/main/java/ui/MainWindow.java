package ui;

import logic.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class MainWindow {
    private JFrame frame;
    private JTable studentTable;
    private StudentTableModel tableModel;
    private ExamRecord examRecord;
    private JTextField emailField;
    private JTextField subjectField;
    private JTextField dateField;
    private JLabel statusLabel;

    public MainWindow() {
        initialize();
    }

    private void initialize() {
        examRecord = new ExamRecord("Программирование на Java", "15.12.2025");
        tableModel = new StudentTableModel();

        createMainWindow();
        setupUI();

        frame.setVisible(true);
    }

    private void createMainWindow() {
        frame = new JFrame("Ведомость для проведения аттестации");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
    }

    private void setupUI() {
        // Основная панель с вертикальным расположением
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Добавляем все компоненты
        mainPanel.add(createInputPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createTablePanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createButtonPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(createStatusBar());

        frame.add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5)); // 3 строки, 2 колонки
        panel.setBorder(BorderFactory.createTitledBorder("Параметры аттестации"));
        panel.setMaximumSize(new Dimension(1150, 120));

        // Первая строка: Предмет
        panel.add(new JLabel("Предмет:"));
        subjectField = new JTextField("Программирование на Java");
        panel.add(subjectField);

        // Вторая строка: Дата аттестации
        panel.add(new JLabel("Дата аттестации:"));
        dateField = new JTextField("15.12.2025");
        panel.add(dateField);

        // Третья строка: Email
        panel.add(new JLabel("Email для отправки:"));
        emailField = new JTextField("teacher@bntu.by");
        panel.add(emailField);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Список студентов"));
        panel.setPreferredSize(new Dimension(1100, 350));

        studentTable = new JTable(tableModel);

        // Настраиваем ширину колонок
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(500);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        studentTable.setAutoCreateRowSorter(true);

        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setPreferredSize(new Dimension(1100, 300));
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Действия"));
        panel.setMaximumSize(new Dimension(1150, 60));

        // Создаем ВСЕ кнопки в одном ряду
        JButton addBtn = createActionButton("Добавить студента", this::addStudent, new Color(70, 130, 180));
        JButton removeBtn = createActionButton("Удалить", this::removeStudent, new Color(220, 80, 60));
        JButton calcBtn = createActionButton("Рассчитать итоги", this::calculateResults, new Color(60, 179, 113));
        JButton emailBtn = createActionButton("Отправить email", this::sendEmail, new Color(106, 90, 205));
        JButton printBtn = createActionButton("Печать", this::printRecord, new Color(205, 133, 63));
        JButton excelBtn = createActionButton("Экспорт в Excel", this::exportToExcel, new Color(46, 139, 87));
        JButton clearBtn = createActionButton("Очистить все", this::clearData, new Color(178, 34, 34));

        // Добавляем ВСЕ кнопки в один ряд
        panel.add(addBtn);
        panel.add(removeBtn);
        panel.add(calcBtn);
        panel.add(emailBtn);
        panel.add(printBtn);
        panel.add(excelBtn);
        panel.add(clearBtn);

        return panel;
    }

    private JButton createActionButton(String text, java.awt.event.ActionListener listener, Color color) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(140, 35));
        return button;
    }

    private JPanel createStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(1150, 30));

        statusLabel = new JLabel(" Готов к работе. Нажмите 'Добавить студента' для начала работы.");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setBackground(new Color(240, 240, 240));
        statusLabel.setOpaque(true);

        panel.add(statusLabel, BorderLayout.CENTER);
        return panel;
    }

    // Остальные методы без изменений
    private void addStudent(ActionEvent e) {
        Student student = AddStudentDialog.showDialog(frame);
        if (student != null) {
            tableModel.addStudent(student);
            updateExamRecordFromTable();
            updateStatus("Добавлен студент: " + student.getFullName() + " (оценка: " + student.getScore() + ")");
        }
    }

    private void removeStudent(ActionEvent e) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = studentTable.convertRowIndexToModel(selectedRow);
            Student student = tableModel.getStudent(modelRow);
            if (student != null) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Удалить студента: " + student.getFullName() + "?",
                        "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeStudent(modelRow);
                    updateExamRecordFromTable();
                    updateStatus("Студент удален");
                }
            }
        } else {
            showWarningDialog("Выберите студента для удаления");
        }
    }

    private void calculateResults(ActionEvent e) {
        updateExamRecordFromTable();
        String results = GradeCalculator.calculateResults(examRecord);
        JOptionPane.showMessageDialog(frame, results, "Итоги аттестации", JOptionPane.INFORMATION_MESSAGE);
        updateStatus("Рассчитаны итоги аттестации");
    }

    private void sendEmail(ActionEvent e) {
        updateExamRecordFromTable();
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showErrorDialog("Введите email адрес");
            return;
        }

        if (examRecord.getTotalStudents() == 0) {
            showWarningDialog("Нет данных для отправки");
            return;
        }

        EmailService.sendResults(email, examRecord);
        updateStatus("Результаты отправлены на email: " + email);
    }

    private void printRecord(ActionEvent e) {
        updateExamRecordFromTable();
        if (examRecord.getTotalStudents() == 0) {
            showWarningDialog("Нет данных для печати");
            return;
        }

        PrintTemplate.printRecord(examRecord);
        updateStatus("Сформирована ведомость для печати");
    }

    private void exportToExcel(ActionEvent e) {
        updateExamRecordFromTable();
        if (examRecord.getTotalStudents() == 0) {
            showWarningDialog("Нет данных для экспорта");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        String fileName = ExcelExportService.generateFileName(examRecord.getSubject());
        fileChooser.setSelectedFile(new File(fileName));

        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            boolean success = ExcelExportService.exportToExcel(examRecord, file.getAbsolutePath());

            if (success) {
                updateStatus("Ведомость сохранена в Excel: " + file.getName());
            }
        }
    }

    private void clearData(ActionEvent e) {
        if (tableModel.getRowCount() == 0) {
            showInfoDialog("Таблица уже пуста");
            return;
        }

        int result = JOptionPane.showConfirmDialog(frame,
                "Очистить всю таблицу студентов? Это действие нельзя отменить.",
                "Подтверждение очистки", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            tableModel.clear();
            updateExamRecordFromTable();
            updateStatus("Таблица очищена");
        }
    }

    private void updateExamRecordFromTable() {
        examRecord.setSubject(subjectField.getText());
        examRecord.setDate(dateField.getText());

        ExamRecord newRecord = new ExamRecord(subjectField.getText(), dateField.getText());
        for (Student student : tableModel.getStudents()) {
            newRecord.addStudent(student);
        }
        examRecord = newRecord;
    }

    private void updateStatus(String message) {
        statusLabel.setText(" " + message + " | Студентов: " + tableModel.getRowCount() +
                " | Сдали: " + examRecord.getPassedCount() +
                " | Не сдали: " + examRecord.getFailedCount());
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(frame, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(frame, message, "Предупреждение", JOptionPane.WARNING_MESSAGE);
    }

    private void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(frame, message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow();
        });
    }
}