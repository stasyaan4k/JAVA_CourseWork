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

        // Заголовок с информацией об университете
        JLabel titleLabel = new JLabel("Белорусский национальный технический университет", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // Подзаголовок
        JLabel subtitleLabel = new JLabel("Факультет информационных технологий и робототехники", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        // Кафедра
        JLabel departmentLabel = new JLabel("Кафедра программного обеспечения информационных систем и технологий", SwingConstants.CENTER);
        departmentLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));

        // Название программы
        JLabel programLabel = new JLabel("Программа: 'Экзаменационная ведомость'", SwingConstants.CENTER);
        programLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
        programLabel.setForeground(Color.BLUE);

        // Информация о студенте (нужно заполнить своими данными)
        JPanel studentPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Исполнитель"));
        studentPanel.add(new JLabel("ФИО студента:"));
        studentPanel.add(new JLabel("[Ваше ФИО]"));
        studentPanel.add(new JLabel("Группа:"));
        studentPanel.add(new JLabel("[Ваша группа]"));
        studentPanel.add(new JLabel("Преподаватель:"));
        studentPanel.add(new JLabel("В.В. Сидорик"));

        // Кнопка для перехода к программе
        JButton startButton = new JButton("Начать работу");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);

        // Добавляем все компоненты
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(titleLabel);
        centerPanel.add(subtitleLabel);
        centerPanel.add(departmentLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(programLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(studentPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(startButton);

        content.add(centerPanel, BorderLayout.CENTER);

        // Таймер для автоматического закрытия
        Timer timer = new Timer(DISPLAY_TIME, e -> dispose());
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
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    public static void showSplash() {
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);
    }
}