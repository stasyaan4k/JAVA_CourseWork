package ui;

import logic.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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
        JMenuItem editStudentItem = new JMenuItem("Редактировать студента...");
        JMenuItem deleteStudentItem = new JMenuItem("Удалить студента");
        JMenuItem calculateItem = new JMenuItem("Рассчитать итоги");
        JMenuItem clearItem = new JMenuItem("Очистить всё");

        editStudentItem.setAccelerator(KeyStroke.getKeyStroke("F2"));
        deleteStudentItem.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        calculateItem.setAccelerator(KeyStroke.getKeyStroke("F9"));
        clearItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift D"));

        editStudentItem.addActionListener(e -> editStudent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "edit")));
        deleteStudentItem.addActionListener(e -> removeStudent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "remove")));
        calculateItem.addActionListener(e -> calculateResults(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "calc")));
        clearItem.addActionListener(e -> clearData(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "clear")));

        editMenu.add(editStudentItem);
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
        subjectField = new JTextField(" ");
        panel.add(subjectField);

        // Вторая строка: Дата аттестации
        panel.add(new JLabel("Дата аттестации:"));
        dateField = new JTextField(" ");
        panel.add(dateField);

        // Третья строка: Email
        panel.add(new JLabel("Email для отправки:"));
        emailField = new JTextField(" ");
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

        // Добавляем обработчик двойного клика для редактирования
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Двойной клик
                    editStudent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "doubleclick"));
                }
            }
        });

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
        JButton editBtn = createActionButton("Редактировать", this::editStudent, new Color(70, 130, 180));
        JButton removeBtn = createActionButton("Удалить", this::removeStudent, new Color(70, 130, 180));
        JButton calcBtn = createActionButton("Рассчитать итоги", this::calculateResults, new Color(70, 130, 180));
        JButton clearBtn = createActionButton("Очистить", this::clearData, new Color(70, 130, 180));

        // Увеличиваем кнопки
        addBtn.setPreferredSize(new Dimension(180, 35));
        editBtn.setPreferredSize(new Dimension(140, 35));
        removeBtn.setPreferredSize(new Dimension(140, 35));
        calcBtn.setPreferredSize(new Dimension(180, 35));
        clearBtn.setPreferredSize(new Dimension(140, 35));

        panel.add(addBtn);
        panel.add(editBtn);
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
                "1. ДОБАВЛЕНИЕ И РЕДАКТИРОВАНИЕ СТУДЕНТОВ\n" +
                "   • Нажмите кнопку 'Добавить студента' или 'Редактировать'\n" +
                "   • Введите ФИО и оценку (0-10, может быть не указана)\n" +
                "   • Критерии: 0-3 - не сдал, 4-10 - сдал\n" +
                "   • Для быстрого редактирования: двойной клик по строке или F2\n\n" +
                "2. РАБОТА С ТАБЛИЦЕЙ\n" +
                "   • Для удаления выделите строку и нажмите 'Удалить' или Delete\n" +
                "   • Для редактирования: выделите строку и нажмите F2 или двойной клик\n" +
                "   • Для сортировки нажмите на заголовок столбца\n\n" +
                "3. РАСЧЕТ ИТОГОВ\n" +
                "   • Нажмите 'Рассчитать итоги' или F9 для получения статистики\n" +
                "   • Отображается общее количество, процент сдачи\n\n" +
                "4. ФАЙЛОВЫЕ ОПЕРАЦИИ\n" +
                "   • Файл → Открыть... (Ctrl+O): загрузить ведомость из файла XLSX\n" +
                "   • Файл → Сохранить (Ctrl+S): сохранить текущую ведомость в XLSX\n" +
                "   • Файл → Сохранить как...: сохранить под новым именем\n" +
                "   • Файл → Экспорт → В Excel...: дублирует функцию сохранения\n" +
                "   • Файл → Экспорт → Отправить по email: отправить результаты\n\n" +
                "5. ПЕЧАТЬ\n" +
                "   • Файл → Печать → Печать ведомости (Ctrl+P)\n" +
                "   • Формирует печатную форму ведомости\n\n" +
                "ГОРЯЧИЕ КЛАВИШИ:\n" +
                "• Ctrl+O - Открыть файл XLSX\n" +
                "• Ctrl+S - Сохранить файл XLSX\n" +
                "• Ctrl+N - Новая ведомость\n" +
                "• F2 - Редактировать студента\n" +
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

    // Методы для работы с файлами XLSX
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() ||
                        f.getName().toLowerCase().endsWith(".xlsx") ||
                        f.getName().toLowerCase().endsWith(".xls");
            }

            @Override
            public String getDescription() {
                return "Файлы Excel (*.xlsx, *.xls)";
            }
        });

        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            loadFromExcelFile(file);
        }
    }

    // Изменяем метод loadFromExcelFile:
    private void loadFromExcelFile(File file) {
        try {
            ExcelImportResult result = ExcelExportService.importFromExcel(file.getAbsolutePath());

            if (result.hasData()) {
                // Обновляем интерфейс с загруженными данными
                String subject = result.getSubject();
                String date = result.getDate();
                String email = result.getEmail();

                // Устанавливаем значения по умолчанию, если данные отсутствуют
                if (subject == null || subject.trim().isEmpty()) {
                    subject = "Шаблон (можно использовать для любого предмета)";
                }
                if (date == null || date.trim().isEmpty()) {
                    date = "05.12.2025";
                }
                if (email == null || email.trim().isEmpty()) {
                    email = "teacher.bntu@gmail.com";
                }

                subjectField.setText(subject);
                dateField.setText(date);
                emailField.setText(email);

                // Очищаем таблицу и добавляем студентов
                tableModel.clear();
                for (Student student : result.getStudents()) {
                    tableModel.addStudent(student);
                }

                currentFile = file;
                updateExamRecordFromTable();
                updateStatus("Файл Excel загружен: " + file.getName());
            } else {
                showWarningDialog("Не удалось загрузить данные из файла Excel");
            }

        } catch (Exception e) {
            showErrorDialog("Ошибка при загрузке файла Excel: " + e.getMessage());
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            saveToExcelFile(currentFile);
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() ||
                        f.getName().toLowerCase().endsWith(".xlsx") ||
                        f.getName().toLowerCase().endsWith(".xls");
            }

            @Override
            public String getDescription() {
                return "Файлы Excel (*.xlsx, *.xls)";
            }
        });

        // Предлагаем имя файла по умолчанию
        String defaultName = ExcelExportService.generateFileName(examRecord.getSubject());
        fileChooser.setSelectedFile(new File(defaultName));

        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Добавляем расширение, если его нет
            if (!file.getName().toLowerCase().endsWith(".xlsx") && !file.getName().toLowerCase().endsWith(".xls")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }
            saveToExcelFile(file);
        }
    }

    // Изменяем метод saveToExcelFile:
    private void saveToExcelFile(File file) {
        updateExamRecordFromTable();
        boolean success = ExcelExportService.exportToExcel(examRecord, emailField.getText(), file.getAbsolutePath());

        if (success) {
            currentFile = file;
            updateStatus("Файл сохранен в Excel: " + file.getName());
        } else {
            showErrorDialog("Не удалось сохранить файл в формате Excel");
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

    private void editStudent(ActionEvent e) {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = studentTable.convertRowIndexToModel(selectedRow);
            Student student = tableModel.getStudent(modelRow);
            if (student != null) {
                // Создаем копию студента для редактирования
                Student studentCopy = new Student(student.getFullName(), student.getScore());

                // Открываем диалог редактирования с текущими данными студента
                Student editedStudent = EditStudentDialog.showDialog(frame, studentCopy);
                if (editedStudent != null) {
                    // Обновляем студента в модели
                    tableModel.updateStudent(modelRow, editedStudent);
                    updateExamRecordFromTable();
                    updateStatus("Данные студента обновлены: " + editedStudent.getFullName());
                }
            }
        } else {
            showWarningDialog("Выберите студента для редактирования");
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

        PrintTemplate.printRecord(examRecord, emailField.getText());
        updateStatus("Сформирована ведомость для печати");
    }

    private void exportToExcel(ActionEvent e) {
        // Просто вызываем сохранение как
        saveFileAs();
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
            subjectField.setText("Программирование на Java");
            dateField.setText("15.12.2025");
            emailField.setText("teacher@bntu.by");
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

    // В методе updateStatus добавляем информацию о студентах без оценки:
    private void updateStatus(String message) {
        statusLabel.setText(" " + message + " | Студентов: " + tableModel.getRowCount() +
                " | С оценкой: " + examRecord.getStudentsWithGrade() +
                " | Без оценки: " + examRecord.getStudentsWithoutGrade() +
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