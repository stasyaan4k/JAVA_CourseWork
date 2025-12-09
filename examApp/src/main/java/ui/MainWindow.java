package ui;

import logic.*;
import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Главное окно приложения для управления экзаменационной ведомостью.
 *
 * @version 3.2
 * @author Маленков Станислав Владимирович
 */
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
    private File originalFile;
    private boolean isDataModified = false;

    public MainWindow() {
        initialize();
    }

    private void initialize() {
        examRecord = new ExamRecord("Программирование на Java", "15.12.2025");
        tableModel = new StudentTableModel();
        tableModel.setMainWindow(this);

        createMainWindow();
        setupUI();
        updateWindowTitle();

        frame.setVisible(true);
    }

    private void createMainWindow() {
        frame = new JFrame("Ведомость для проведения аттестации");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmExit();
            }
        });
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
    }

    private void setupUI() {
        frame.setJMenuBar(createMenuBar());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(createInputPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createTablePanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createButtonPanel());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(createStatusBar());

        frame.add(mainPanel);
    }

    private void updateWindowTitle() {
        String title = "Ведомость для проведения аттестации";

        if (currentFile != null) {
            title += " - " + currentFile.getName();
        }

        if (isDataModified) {
            title += " *";
        }

        frame.setTitle(title);
    }

    protected void markDataModified() {
        if (!isDataModified) {
            isDataModified = true;
            updateWindowTitle();
        }
    }

    private void markDataSaved() {
        isDataModified = false;
        updateWindowTitle();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Меню Файл
        JMenu fileMenu = new JMenu("Файл");
        fileMenu.setMnemonic('Ф');

        JMenuItem newRecordItem = new JMenuItem("Новая ведомость");
        newRecordItem.setMnemonic('Н');
        newRecordItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newRecordItem.addActionListener(e -> clearAllData());

        JMenuItem openItem = new JMenuItem("Открыть...");
        openItem.setMnemonic('О');
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(e -> openFile());

        JMenuItem saveItem = new JMenuItem("Сохранить");
        saveItem.setMnemonic('С');
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> saveFile());

        JMenuItem saveAsItem = new JMenuItem("Сохранить как...");
        saveAsItem.setMnemonic('а');
        saveAsItem.addActionListener(e -> saveFileAs());

        JMenu exportMenu = new JMenu("Экспорт");
        exportMenu.setMnemonic('Э');

        JMenuItem exportExcelItem = new JMenuItem("В Excel...");
        exportExcelItem.addActionListener(e -> exportToExcel());

        JMenuItem exportEmailItem = new JMenuItem("Отправить по email...");
        exportEmailItem.addActionListener(e -> sendEmail());

        exportMenu.add(exportExcelItem);
        exportMenu.add(exportEmailItem);

        JMenuItem printItem = new JMenuItem("Печать ведомости...");
        printItem.setMnemonic('П');
        printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        printItem.addActionListener(e -> printRecord());

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.setMnemonic('ы');
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        exitItem.addActionListener(e -> confirmExit());

        // Собираем меню Файл
        fileMenu.add(newRecordItem);
        fileMenu.addSeparator();
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exportMenu);
        fileMenu.add(printItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Меню Правка
        JMenu editMenu = new JMenu("Правка");
        editMenu.setMnemonic('П');

        JMenuItem deleteStudentItem = new JMenuItem("Удалить студента");
        deleteStudentItem.setMnemonic('У');
        deleteStudentItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteStudentItem.addActionListener(e -> removeStudent());

        JMenuItem calculateItem = new JMenuItem("Рассчитать итоги");
        calculateItem.setMnemonic('Р');
        calculateItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        calculateItem.addActionListener(e -> calculateResults());

        JMenuItem clearItem = new JMenuItem("Очистить всё");
        clearItem.setMnemonic('О');
        clearItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        clearItem.addActionListener(e -> clearAllData());

        editMenu.add(deleteStudentItem);
        editMenu.addSeparator();
        editMenu.add(calculateItem);
        editMenu.add(clearItem);

        // Меню Вид
        JMenu viewMenu = new JMenu("Вид");
        viewMenu.setMnemonic('В');

        JCheckBoxMenuItem showGridItem = new JCheckBoxMenuItem("Показывать сетку таблицы", true);
        showGridItem.addActionListener(e -> studentTable.setShowGrid(showGridItem.isSelected()));

        JMenu fontSizeMenu = new JMenu("Размер шрифта таблицы");
        fontSizeMenu.setMnemonic('Р');

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

        viewMenu.add(showGridItem);
        viewMenu.addSeparator();
        viewMenu.add(fontSizeMenu);

        // Меню Справка
        JMenu helpMenu = new JMenu("Справка");
        helpMenu.setMnemonic('С');

        JMenuItem aboutAuthorItem = new JMenuItem("Сведения об авторе");
        aboutAuthorItem.setMnemonic('а');
        aboutAuthorItem.addActionListener(e -> AboutAuthorWindow.showDialog(frame));

        JMenuItem aboutProgramItem = new JMenuItem("Сведения о программе");
        aboutProgramItem.setMnemonic('п');
        aboutProgramItem.addActionListener(e -> AboutProgramWindow.showDialog(frame));

        JMenuItem helpItem = new JMenuItem("Справка");
        helpItem.setMnemonic('С');
        helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
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

        panel.add(new JLabel("Предмет:"));
        subjectField = new JTextField("Программирование на Java");
        subjectField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { markDataModified(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { markDataModified(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { markDataModified(); }
        });
        panel.add(subjectField);

        panel.add(new JLabel("Дата аттестации:"));
        dateField = new JTextField("15.12.2025");
        dateField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { markDataModified(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { markDataModified(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { markDataModified(); }
        });
        panel.add(dateField);

        panel.add(new JLabel("Email для отправки:"));
        emailField = new JTextField("teacher@bntu.by");
        emailField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { markDataModified(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { markDataModified(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { markDataModified(); }
        });
        panel.add(emailField);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Список студентов"));
        panel.setPreferredSize(new Dimension(1100, 350));

        studentTable = new JTable(tableModel);
        setupTableForInlineEditing();

        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setPreferredSize(new Dimension(1100, 300));
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupTableForInlineEditing() {
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentTable.setRowHeight(25);
        studentTable.setShowGrid(true);
        studentTable.setGridColor(new Color(220, 220, 220));

        studentTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(500);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        studentTable.setAutoCreateRowSorter(true);

        TableColumn nameColumn = studentTable.getColumnModel().getColumn(1);
        nameColumn.setCellEditor(new NameCellEditor());

        TableColumn gradeColumn = studentTable.getColumnModel().getColumn(2);
        gradeColumn.setCellEditor(new GradeCellEditor());

        studentTable.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Основные действия"));
        panel.setMaximumSize(new Dimension(1150, 60));

        JButton addBtn = createActionButton("Добавить студента", e -> addStudent(), new Color(70, 130, 180));
        JButton removeBtn = createActionButton("Удалить", e -> removeStudent(), new Color(70, 130, 180));
        JButton calcBtn = createActionButton("Рассчитать итоги", e -> calculateResults(), new Color(70, 130, 180));
        JButton clearBtn = createActionButton("Очистить", e -> clearData(), new Color(70, 130, 180));

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

        JLabel counterLabel = new JLabel(" Студентов: 0 ");
        counterLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        counterLabel.setBackground(new Color(220, 220, 220));
        counterLabel.setOpaque(true);

        panel.add(statusLabel, BorderLayout.CENTER);
        panel.add(counterLabel, BorderLayout.EAST);

        return panel;
    }

    private void setTableFontSize(int size) {
        Font currentFont = studentTable.getFont();
        studentTable.setFont(new Font(currentFont.getName(), currentFont.getStyle(), size));
        studentTable.setRowHeight(size + 10);
        studentTable.repaint();
    }

    private void showHelp() {
        String helpText = "КРАТКАЯ СПРАВКА\n\n" +
                "1. ДОБАВЛЕНИЕ И РЕДАКТИРОВАНИЕ СТУДЕНТОВ\n" +
                "   • Нажмите кнопку 'Добавить студента' для добавления\n" +
                "   • Для редактирования: кликните по ячейке с ФИО или оценкой\n" +
                "   • Изменения сохраняются автоматически\n" +
                "   • Критерии: 0-3 - не сдал, 4-10 - сдал\n\n" +
                "2. РАБОТА С ТАБЛИЦЕЙ\n" +
                "   • Для удаления выделите строку и нажмите 'Удалить' или Delete\n" +
                "   • Для редактирования: кликните на ячейку и вводите данные\n" +
                "   • Для сортировки нажмите на заголовок столбца\n" +
                "   • Оценка выбирается из выпадающего списка\n\n" +
                "3. ФАЙЛОВЫЕ ОПЕРАЦИИ\n" +
                "   • Файл → Открыть... (Ctrl+O): загрузить ведомость из файла XLSX\n" +
                "   • Файл → Сохранить (Ctrl+S): сохранить текущую ведомость в XLSX\n" +
                "   • Файл → Сохранить как...: создать копию файла\n" +
                "   • * в заголовке окна: есть несохраненные изменения\n\n" +
                "ГОРЯЧИЕ КЛАВИШИ:\n" +
                "• Ctrl+O - Открыть файл XLSX\n" +
                "• Ctrl+S - Сохранить файл XLSX\n" +
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

    private void openFile() {
        if (isDataModified) {
            int choice = JOptionPane.showConfirmDialog(frame,
                    "Есть несохраненные изменения. Сохранить перед открытием нового файла?",
                    "Несохраненные изменения",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                saveFile();
                if (isDataModified) return;
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xlsx") ||
                        f.getName().toLowerCase().endsWith(".xls");
            }

            @Override
            public String getDescription() {
                return "Файлы Excel (*.xlsx, *.xls)";
            }
        });

        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            loadFromExcelFile(file);
        }
    }

    private void loadFromExcelFile(File file) {
        try {
            ExcelImportResult result = ExcelExportService.importFromExcel(file.getAbsolutePath());

            if (result.hasData()) {
                String subject = result.getSubject();
                String date = result.getDate();
                String email = result.getEmail();

                if (subject == null || subject.trim().isEmpty()) subject = "Программирование на Java";
                if (date == null || date.trim().isEmpty()) date = "15.12.2025";
                if (email == null || email.trim().isEmpty()) email = "teacher@bntu.by";

                subjectField.setText(subject);
                dateField.setText(date);
                emailField.setText(email);

                tableModel.clear();
                for (Student student : result.getStudents()) {
                    tableModel.addStudent(student);
                }

                currentFile = file;
                originalFile = file;
                isDataModified = false;
                updateWindowTitle();
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
            saveToExcelFile(currentFile, true);
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xlsx") ||
                        f.getName().toLowerCase().endsWith(".xls");
            }

            @Override
            public String getDescription() {
                return "Файлы Excel (*.xlsx, *.xls)";
            }
        });

        String defaultName = ExcelExportService.generateFileName(examRecord.getSubject());
        fileChooser.setSelectedFile(new File(defaultName));

        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();

            if (!path.toLowerCase().endsWith(".xlsx") && !path.toLowerCase().endsWith(".xls")) {
                selectedFile = new File(path + ".xlsx");
            }

            saveToExcelFile(selectedFile, false);
        }
    }

    private void saveToExcelFile(File file, boolean updateCurrentFile) {
        updateExamRecordFromTable();
        boolean success = ExcelExportService.exportToExcel(examRecord, emailField.getText(), file.getAbsolutePath());

        if (success) {
            if (updateCurrentFile) currentFile = file;
            markDataSaved();
            updateStatus("Файл сохранен: " + file.getName() + (updateCurrentFile ? " (текущий)" : " (копия)"));
        } else {
            showErrorDialog("Не удалось сохранить файл в формате Excel");
        }
    }

    private void addStudent() {
        Student student = new Student("Новый студент", -1);
        tableModel.addStudent(student);
        markDataModified();
        updateExamRecordFromTable();
        updateStatus("Добавлен новый студент. Отредактируйте данные в таблице.");

        int lastRow = tableModel.getRowCount() - 1;
        studentTable.setRowSelectionInterval(lastRow, lastRow);
        studentTable.scrollRectToVisible(studentTable.getCellRect(lastRow, 0, true));
    }

    private void removeStudent() {
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
                    markDataModified();
                    updateExamRecordFromTable();
                    updateStatus("Студент удален");
                }
            }
        } else {
            showWarningDialog("Выберите студента для удаления");
        }
    }

    private void calculateResults() {
        updateExamRecordFromTable();
        String results = GradeCalculator.calculateResults(examRecord);
        JOptionPane.showMessageDialog(frame, results, "Итоги аттестации", JOptionPane.INFORMATION_MESSAGE);
        updateStatus("Рассчитаны итоги аттестации");
    }

    private void sendEmail() {
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

    private void printRecord() {
        updateExamRecordFromTable();
        if (examRecord.getTotalStudents() == 0) {
            showWarningDialog("Нет данных для печати");
            return;
        }

        // Используем простой текстовый вывод
        PrintTemplate.printRecord(examRecord, emailField.getText());
        updateStatus("Сформирована ведомость для печати");
    }

    private void exportToExcel() {
        saveFileAs();
    }

    private void clearData() {
        if (tableModel.getRowCount() == 0) {
            showInfoDialog("Таблица уже пуста");
            return;
        }

        int result = JOptionPane.showConfirmDialog(frame,
                "Очистить всю таблицу студентов? Это действие нельзя отменить.",
                "Подтверждение очистки", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            tableModel.clear();
            markDataModified();
            updateExamRecordFromTable();
            updateStatus("Таблица очищена");
        }
    }

    private void clearAllData() {
        if (tableModel.getRowCount() == 0 &&
                subjectField.getText().equals("Программирование на Java") &&
                dateField.getText().equals("15.12.2025") &&
                emailField.getText().equals("teacher@bntu.by")) {
            return;
        }

        int result = JOptionPane.showConfirmDialog(frame,
                "Очистить все данные (таблицу и параметры)? Это действие нельзя отменить.",
                "Подтверждение очистки", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            tableModel.clear();
            subjectField.setText("Программирование на Java");
            dateField.setText("15.12.2025");
            emailField.setText("teacher@bntu.by");
            currentFile = null;
            originalFile = null;
            isDataModified = true;
            updateExamRecordFromTable();
            updateWindowTitle();
            updateStatus("Все данные очищены");
        }
    }

    private void confirmExit() {
        if (isDataModified) {
            int choice = JOptionPane.showConfirmDialog(frame,
                    "Есть несохраненные изменения. Сохранить перед выходом?",
                    "Подтверждение выхода",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                saveFile();
                if (!isDataModified) System.exit(0);
            } else if (choice == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
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
}