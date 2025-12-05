package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SplashScreen extends JWindow {
    private static final int DISPLAY_TIME = 60000; // 60 секунд

    public SplashScreen() {
        initialize();
    }

    private void initialize() {
        // Основной контейнер
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Панель с основным содержимым
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // Заголовок с информацией об университете
        JLabel titleLabel = new JLabel("Белорусский национальный технический университет", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Подзаголовок
        JLabel subtitleLabel = new JLabel("Факультет информационных технологий и робототехники", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        // Кафедра
        JLabel departmentLabel = new JLabel("Кафедра программного обеспечения информационных систем и технологий", SwingConstants.CENTER);
        departmentLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        departmentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        departmentLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Название программы
        JLabel programLabel = new JLabel("Программа: 'Экзаменационная ведомость'", SwingConstants.CENTER);
        programLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        programLabel.setForeground(new Color(0, 0, 139)); // Темно-синий
        programLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        programLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        // Информация о студенте
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

        // Первая строка: ФИО студента
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        studentPanel.add(new JLabel("ФИО студента:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel nameLabel = new JLabel("Маленков Станислав Владимирович");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentPanel.add(nameLabel, gbc);

        // Вторая строка: Группа
        gbc.gridx = 0; gbc.gridy = 1;
        studentPanel.add(new JLabel("Группа:"), gbc);

        gbc.gridx = 1;
        JLabel groupLabel = new JLabel("10702423");
        groupLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentPanel.add(groupLabel, gbc);

        // Третья строка: Преподаватель
        gbc.gridx = 0; gbc.gridy = 2;
        studentPanel.add(new JLabel("Преподаватель:"), gbc);

        gbc.gridx = 1;
        JLabel teacherLabel = new JLabel("В.В. Сидорик");
        teacherLabel.setFont(new Font("Arial", Font.BOLD, 12));
        studentPanel.add(teacherLabel, gbc);

        // Кнопка для перехода к программе
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0));

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

        // Добавляем эффект при наведении
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

        buttonPanel.add(startButton);

        // Инструкция
        JLabel instructionLabel = new JLabel("(Нажмите кнопку или кликните в любое место окна)", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructionLabel.setForeground(Color.GRAY);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Добавляем все компоненты
        centerPanel.add(titleLabel);
        centerPanel.add(subtitleLabel);
        centerPanel.add(departmentLabel);
        centerPanel.add(programLabel);
        centerPanel.add(studentPanel);
        centerPanel.add(buttonPanel);
        centerPanel.add(instructionLabel);

        content.add(centerPanel, BorderLayout.CENTER);

        // Таймер для автоматического закрытия
        Timer timer = new Timer(DISPLAY_TIME, e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new MainWindow());
        });
        timer.setRepeats(false);
        timer.start();

        // Обработчик кнопки
        startButton.addActionListener(e -> {
            timer.stop();
            dispose();
            SwingUtilities.invokeLater(() -> new MainWindow());
        });

        // Обработчик клика по окну
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                timer.stop();
                dispose();
                SwingUtilities.invokeLater(() -> new MainWindow());
            }
        });

        setContentPane(content);
        setSize(650, 500);
        setLocationRelativeTo(null);
    }

    public static void showSplash() {
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);
    }
}