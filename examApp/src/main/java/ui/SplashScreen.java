package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Окно-заставка при запуске программы.
 * Отображает информацию об университете, авторе и программе.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class SplashScreen extends JWindow {
    private static final int DISPLAY_TIME = 60000; // Время показа: 60 секунд

    /**
     * Создает окно-заставку.
     */
    public SplashScreen() {
        initialize();
    }

    /**
     * Инициализирует компоненты окна-заставки.
     */
    private void initialize() {
        // Основной контейнер с белым фоном
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Центральная панель с вертикальным расположением
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Добавляем все компоненты в центр
        addComponentsToCenterPanel(centerPanel);

        content.add(centerPanel, BorderLayout.CENTER);

        // Настраиваем таймер для автоматического закрытия
        setupAutoCloseTimer();

        // Устанавливаем содержимое окна
        setContentPane(content);
        setSize(650, 500);
        setLocationRelativeTo(null); // Центрирование
    }

    /**
     * Добавляет компоненты на центральную панель.
     */
    private void addComponentsToCenterPanel(JPanel centerPanel) {
        // Заголовок с информацией об университете
        JLabel titleLabel = createLabel("Белорусский национальный технический университет",
                new Font("Times New Roman", Font.BOLD, 18), 0, 0, 15, 0);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Подзаголовок - факультет
        JLabel subtitleLabel = createLabel("Факультет информационных технологий и робототехники",
                new Font("Times New Roman", Font.PLAIN, 14), 0, 0, 5, 0);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Кафедра
        JLabel departmentLabel = createLabel("Кафедра программного обеспечения информационных систем и технологий",
                new Font("Times New Roman", Font.PLAIN, 14), 0, 0, 30, 0);
        departmentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Название программы
        JLabel programLabel = createLabel("Программа: 'Экзаменационная ведомость'",
                new Font("Times New Roman", Font.BOLD, 16), new Color(0, 0, 139), 0, 0, 25, 0);
        programLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Панель с информацией о студенте
        JPanel studentPanel = createStudentPanel();

        // Панель с кнопкой запуска
        JPanel buttonPanel = createButtonPanel();

        // Инструкция для пользователя
        JLabel instructionLabel = createLabel("(Нажмите кнопку или кликните в любое место окна)",
                new Font("Arial", Font.ITALIC, 11), Color.GRAY, 0, 0, 0, 0);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Добавляем все компоненты в центральную панель
        centerPanel.add(titleLabel);
        centerPanel.add(subtitleLabel);
        centerPanel.add(departmentLabel);
        centerPanel.add(programLabel);
        centerPanel.add(studentPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(instructionLabel);
    }

    /**
     * Создает метку с заданными параметрами.
     */
    private JLabel createLabel(String text, Font font, int top, int left, int bottom, int right) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
        return label;
    }

    /**
     * Создает метку с заданными параметрами и цветом.
     */
    private JLabel createLabel(String text, Font font, Color color, int top, int left, int bottom, int right) {
        JLabel label = createLabel(text, font, top, left, bottom, right);
        label.setForeground(color);
        return label;
    }

    /**
     * Создает панель с информацией о студенте.
     */
    private JPanel createStudentPanel() {
        JPanel studentPanel = new JPanel(new GridBagLayout());
        studentPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "Исполнитель"
        ));
        studentPanel.setBackground(Color.WHITE);
        studentPanel.setMaximumSize(new Dimension(450, 130));
        studentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);

        // ФИО студента
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        studentPanel.add(new JLabel("ФИО студента:"), gbc);

        gbc.gridx = 1;
        JLabel nameLabel = new JLabel("Маленков Станислав Владимирович");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentPanel.add(nameLabel, gbc);

        // Группа
        gbc.gridx = 0; gbc.gridy = 1;
        studentPanel.add(new JLabel("Группа:"), gbc);

        gbc.gridx = 1;
        JLabel groupLabel = new JLabel("10702423");
        groupLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentPanel.add(groupLabel, gbc);

        // Преподаватель
        gbc.gridx = 0; gbc.gridy = 2;
        studentPanel.add(new JLabel("Преподаватель:"), gbc);

        gbc.gridx = 1;
        JLabel teacherLabel = new JLabel("В.В. Сидорик");
        teacherLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentPanel.add(teacherLabel, gbc);

        return studentPanel;
    }

    /**
     * Создает панель с кнопкой запуска программы.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));

        JButton startButton = createStartButton();
        buttonPanel.add(startButton);

        return buttonPanel;
    }

    /**
     * Создает стилизованную кнопку запуска.
     */
    private JButton createStartButton() {
        JButton startButton = new JButton("Начать работу");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 100, 150), 2),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Эффект при наведении курсора
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(90, 150, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(70, 130, 180));
            }
        });

        // Обработчик нажатия кнопки
        startButton.addActionListener(e -> startMainApplication());

        return startButton;
    }

    /**
     * Настраивает таймер для автоматического закрытия заставки.
     */
    private void setupAutoCloseTimer() {
        Timer timer = new Timer(DISPLAY_TIME, e -> closeApplication());
        timer.setRepeats(false);
        timer.start();

        // Обработчик клика по окну
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                timer.stop();
                startMainApplication();
            }
        });
    }

    /**
     * Запускает главное окно и закрывает заставку.
     */
    private void startMainApplication() {
        dispose(); // Закрываем заставку
        SwingUtilities.invokeLater(() -> new MainWindow()); // Запускаем главное окно
    }

    /**
     * Закрывает приложение (вызывается по таймеру).
     */
    private void closeApplication() {
        dispose(); // Закрываем заставку
        System.exit(0); // Завершаем программу
    }

    /**
     * Статический метод для отображения заставки.
     */
    public static void showSplash() {
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);
    }
}