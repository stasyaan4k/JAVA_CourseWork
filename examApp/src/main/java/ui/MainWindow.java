package ui;

import logic.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class MainWindow {
    private JFrame frame;
    private JTable studentTable;
    private StudentTableModel tableModel;
    private ExamRecord examRecord;
    private JTextField emailField;
    private JTextField subjectField;
    private JTextField dateField;
    private JLabel statusLabel;
    private File currentFile;

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
        // Создаем главное меню
        frame.setJMenuBar(createMenuBar());

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

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Меню Файл
        JMenu fileMenu = new JMenu("Файл");

        // Пункт "Новая ведомость"
        JMenuItem newRecordItem = new JMenuItem("Новая ведомость");

        newRecordItem.addActionListener(e -> clearAllData());
        newRecordItem.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));

        // Пункты меню Файл
        JMenuItem openItem = new JMenuItem("Открыть...");
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openItem.addActionListener(e -> openFile());

        JMenuItem saveItem = new JMenuItem("Сохранить");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveItem.addActionListener(e -> saveFile());

        JMenuItem saveAsItem = new JMenuItem("Сохранить как...");
        saveAsItem.addActionListener(e -> saveFileAs());

        // Подменю "Экспорт"
        JMenu exportMenu = new JMenu("Экспорт");
        JMenuItem exportExcelItem = new JMenuItem("В Excel...");
        JMenuItem exportEmailItem = new JMenuItem("Отправить по email...");

        exportExcelItem.addActionListener(e -> exportToExcel(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "export")));
        exportEmailItem.addActionListener(e -> sendEmail(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "email")));

        exportMenu.add(exportExcelItem);
        exportMenu.add(exportEmailItem);

        // Подменю "Печать"
        JMenu printMenu = new JMenu("Печать");
        JMenuItem printItem = new JMenuItem("Печать ведомости...");

        printItem.addActionListener(e -> printRecord(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "print")));
        printItem.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));

        printMenu.add(printItem);

        // Разделитель
        JSeparator separator = new JSeparator();

        // Выход
        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> System.exit(0));
        exitItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));

        // Собираем меню Файл
        fileMenu.add(newRecordItem);
        fileMenu.addSeparator();
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exportMenu);
        fileMenu.add(printMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Меню Правка
        JMenu editMenu = new JMenu("Правка");
        JMenuItem deleteStudentItem = new JMenuItem("Удалить студента");
        JMenuItem calculateItem = new JMenuItem("Рассчитать итоги");
        JMenuItem clearItem = new JMenuItem("Очистить всё");

        deleteStudentItem.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        calculateItem.setAccelerator(KeyStroke.getKeyStroke("F9"));
        clearItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift D"));

        deleteStudentItem.addActionListener(e -> removeStudent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "remove")));
        calculateItem.addActionListener(e -> calculateResults(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "calc")));
        clearItem.addActionListener(e -> clearData(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clear")));

        editMenu.add(deleteStudentItem);
        editMenu.addSeparator();
        editMenu.add(calculateItem);
        editMenu.add(clearItem);

        // Меню Вид (упрощенное - только размер шрифта)
        JMenu viewMenu = new JMenu("Вид");
        JCheckBoxMenuItem showGridItem = new JCheckBoxMenuItem("Показывать сетку таблицы", true);

        // Подменю "Размер шрифта таблицы"
        JMenu fontSizeMenu = new JMenu("Размер шрифта таблицы");

        ButtonGroup fontSizeGroup = new ButtonGroup();
        JRadioButtonMenuItem fontSizeSmall = new JRadioButtonMenuItem("Мелкий (10pt)");
        JRadioButtonMenuItem fontSizeMedium = new JRadioButtonMenuItem("Средний (12pt)", true);
        JRadioButtonMenuItem fontSizeLarge = new JRadioButtonMenuItem("Крупный (14pt)");

        fontSizeGroup.add(fontSizeSmall);
        fontSizeGroup.add(fontSizeMedium);
        fontSizeGroup.add(fontSizeLarge);

        fontSizeSmall.addActionListener(e -> setTableFontSize(10));
        fontSizeMedium.addActionListener(e -> setTableFontSize(12));
        fontSizeLarge.addActionListener(e -> setTableFontSize(14));

        fontSizeMenu.add(fontSizeSmall);
        fontSizeMenu.add(fontSizeMedium);
        fontSizeMenu.add(fontSizeLarge);

        // Обработчик для сетки
        showGridItem.addActionListener(e -> studentTable.setShowGrid(showGridItem.isSelected()));

        viewMenu.add(showGridItem);
        viewMenu.addSeparator();
        viewMenu.add(fontSizeMenu);

        // Меню Справка
        JMenu helpMenu = new JMenu("Справка");
        JMenuItem aboutAuthorItem = new JMenuItem("Сведения об авторе");
        JMenuItem aboutProgramItem = new JMenuItem("Сведения о программе");
        JMenuItem helpItem = new JMenuItem("Справка");

        aboutAuthorItem.addActionListener(e -> AboutAuthorWindow.showDialog(frame));
        aboutProgramItem.addActionListener(e -> AboutProgramWindow.showDialog(frame));
        helpItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
        helpItem.addActionListener(e -> showHelp());

        helpMenu.add(aboutAuthorItem);
        helpMenu.add(aboutProgramItem);
        helpMenu.addSeparator();
        helpMenu.add(helpItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
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

        // Настройка внешнего вида таблицы по умолчанию
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentTable.setRowHeight(25);
        studentTable.setShowGrid(true);
        studentTable.setGridColor(new Color(220, 220, 220));

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
        panel.setBorder(BorderFactory.createTitledBorder("Основные действия"));
        panel.setMaximumSize(new Dimension(1150, 60));

        // Оставляем только основные кнопки
        JButton addBtn = createActionButton("Добавить студента", this::addStudent, new Color(70, 130, 180));
        JButton removeBtn = createActionButton("Удалить", this::removeStudent, new Color(220, 80, 60));
        JButton calcBtn = createActionButton("Рассчитать итоги", this::calculateResults, new Color(60, 179, 113));
        JButton clearBtn = createActionButton("Очистить", this::clearData, new Color(178, 34, 34));

        // Увеличиваем кнопки
        addBtn.setPreferredSize(new Dimension(180, 35));
        removeBtn.setPreferredSize(new Dimension(140, 35));
        calcBtn.setPreferredSize(new Dimension(180, 35));
        clearBtn.setPreferredSize(new Dimension(140, 35));

        panel.add(addBtn);
        panel.add(removeBtn);
        panel.add(calcBtn);
        panel.add(clearBtn);

        return panel;
    }

    private JButton createActionButton(String text, java.awt.event.ActionListener listener, Color color) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }

    private JPanel createStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(1150, 30));

        statusLabel = new JLabel(" Готов к работе. Нажмите 'Добавить студента' для начала работы.");
        statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        statusLabel.setBackground(new Color(240, 240, 240));
        statusLabel.setOpaque(true);

        // Добавляем индикатор количества студентов
        JLabel counterLabel = new JLabel(" Студентов: 0 ");
        counterLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        counterLabel.setBackground(new Color(220, 220, 220));
        counterLabel.setOpaque(true);

        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(counterLabel, BorderLayout.EAST);

        return panel;
    }

    // Методы для работы с внешним видом таблицы
    private void setTableFontSize(int size) {
        Font currentFont = studentTable.getFont();
        studentTable.setFont(new Font(currentFont.getName(), currentFont.getStyle(), size));
        studentTable.setRowHeight(size + 10);
        studentTable.repaint();
    }

    private void showHelp() {
        String helpText = "КРАТКАЯ СПРАВКА\n\n" +
                "1. ДОБАВЛЕНИЕ СТУДЕНТОВ\n" +
                "   • Нажмите кнопку 'Добавить студента'\n" +
                "   • Введите ФИО и оценку (0-10)\n" +
                "   • Критерии: 0-3 - не сдал, 4-10 - сдал\n\n" +
                "2. РАБОТА С ТАБЛИЦЕЙ\n" +
                "   • Для удаления выделите строку и нажмите 'Удалить'\n" +
                "   • Для сортировки нажмите на заголовок столбца\n\n" +
                "3. РАСЧЕТ ИТОГОВ\n" +
                "   • Нажмите 'Рассчитать итоги' для получения статистики\n" +
                "   • Отображается общее количество, процент сдачи\n\n" +
                "4. ФАЙЛОВЫЕ ОПЕРАЦИИ\n" +
                "   • Файл → Открыть... (Ctrl+O): загрузить ведомость из файла\n" +
                "   • Файл → Сохранить (Ctrl+S): сохранить текущую ведомость\n" +
                "   • Файл → Сохранить как...: сохранить под новым именем\n" +
                "   • Файл → Экспорт → В Excel: сохранить в Excel\n" +
                "   • Файл → Экспорт → Отправить по email: отправить результаты\n\n" +
                "5. ПЕЧАТЬ\n" +
                "   • Файл → Печать → Печать ведомости (Ctrl+P)\n" +
                "   • Формирует печатную форму ведомости\n\n" +
                "ГОРЯЧИЕ КЛАВИШИ:\n" +
                "• Ctrl+O - Открыть файл\n" +
                "• Ctrl+S - Сохранить файл\n" +
                "• Ctrl+N - Новая ведомость\n" +
                "• Delete - Удалить студента\n" +
                "• F9 - Рассчитать итоги\n" +
                "• Ctrl+P - Печать\n" +
                "• F1 - Эта справка";

        JTextArea helpArea = new JTextArea(helpText);
        helpArea.setEditable(false);
        helpArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        helpArea.setBackground(new Color(248, 248, 248));

        JScrollPane scrollPane = new JScrollPane(helpArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(frame, scrollPane, "Справка по программе", JOptionPane.INFORMATION_MESSAGE);
    }

    // Методы для работы с файлами
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".exam");
            }

            @Override
            public String getDescription() {
                return "Файлы ведомостей (*.exam)";
            }
        });

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            loadFromFile(file);
        }
    }

    private void loadFromFile(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            // Читаем данные из файла
            String subject = (String) ois.readObject();
            String date = (String) ois.readObject();
            String email = (String) ois.readObject();
            List<Student> students = (List<Student>) ois.readObject();

            // Обновляем интерфейс
            subjectField.setText(subject);
            dateField.setText(date);
            emailField.setText(email);

            // Очищаем таблицу и добавляем студентов
            tableModel.clear();
            for (Student student : students) {
                tableModel.addStudent(student);
            }

            currentFile = file;
            updateExamRecordFromTable();
            updateStatus("Файл загружен: " + file.getName());

        } catch (Exception e) {
            showErrorDialog("Ошибка при загрузке файла: " + e.getMessage());
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            saveToFile(currentFile);
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".exam");
            }

            @Override
            public String getDescription() {
                return "Файлы ведомостей (*.exam)";
            }
        });

        // Предлагаем имя файла по умолчанию
        String defaultName = "ведомость_" + subjectField.getText().replaceAll("[^a-zA-Zа-яА-Я0-9]", "_") + ".exam";
        fileChooser.setSelectedFile(new File(defaultName));

        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Добавляем расширение, если его нет
            if (!file.getName().toLowerCase().endsWith(".exam")) {
                file = new File(file.getAbsolutePath() + ".exam");
            }
            saveToFile(file);
        }
    }

    private void saveToFile(File file) {
        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(file))) {
            updateExamRecordFromTable();

            // Сохраняем данные
            oos.writeObject(examRecord.getSubject());
            oos.writeObject(examRecord.getDate());
            oos.writeObject(emailField.getText());
            oos.writeObject(tableModel.getStudents());

            currentFile = file;
            updateStatus("Файл сохранен: " + file.getName());

        } catch (Exception e) {
            showErrorDialog("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    // Методы-обработчики
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

    private void clearAllData() {
        int result = JOptionPane.showConfirmDialog(frame,
                "Очистить все данные (таблицу и параметры)? Это действие нельзя отменить.",
                "Подтверждение очистки", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            tableModel.clear();
            subjectField.setText("");
            dateField.setText("");
            emailField.setText("");
            currentFile = null;
            updateExamRecordFromTable();
            updateStatus("Все данные очищены");
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

        // Обновляем счетчик в статусной строке
        Component[] components = ((JPanel)frame.getContentPane().getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComps = ((JPanel)comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JLabel && ((JLabel)subComp).getText().startsWith(" Студентов:")) {
                        ((JLabel)subComp).setText(" Студентов: " + tableModel.getRowCount() + " ");
                    }
                }
            }
        }
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