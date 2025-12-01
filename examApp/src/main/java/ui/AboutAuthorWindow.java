package ui;

import javax.swing.*;
import java.awt.*;

public class AboutAuthorWindow extends JDialog {

    public AboutAuthorWindow(JFrame parent) {
        super(parent, "Сведения об авторе", true);
        initialize();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initialize() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Заголовок
        JLabel titleLabel = new JLabel("Информация об авторе программы", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 102, 204));

        // Создаем панель с фото (заглушка)
        JPanel photoPanel = new JPanel(new BorderLayout());
        JLabel photoLabel = new JLabel("ФОТО", SwingConstants.CENTER);
        photoLabel.setPreferredSize(new Dimension(150, 200));
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        photoLabel.setBackground(new Color(240, 240, 240));
        photoLabel.setOpaque(true);
        photoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        photoPanel.add(photoLabel, BorderLayout.CENTER);
        photoPanel.add(new JLabel("Фотография автора", SwingConstants.CENTER), BorderLayout.SOUTH);

        // Информация об авторе
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("ФИО студента:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("[Ваше ФИО]"), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Номер группы:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("[Ваша группа]"), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Факультет:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("ФИТР"), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Специальность:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("Программное обеспечение информационных технологий"), gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Преподаватель:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("В.В. Сидорик"), gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        infoPanel.add(new JLabel("Год разработки:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("2025"), gbc);

        // Достижения и навыки
        JTextArea achievementsArea = new JTextArea(6, 30);
        achievementsArea.setText("Достижения и навыки:\n\n" +
                "• Разработка программ на Java\n" +
                "• Создание GUI приложений с использованием Swing\n" +
                "• Работа с базами данных\n" +
                "• Разработка алгоритмов обработки данных\n" +
                "• Тестирование и отладка программного обеспечения");
        achievementsArea.setEditable(false);
        achievementsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        achievementsArea.setBorder(BorderFactory.createTitledBorder("Профессиональные навыки"));

        // Контактная информация
        JPanel contactPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        contactPanel.setBorder(BorderFactory.createTitledBorder("Контактная информация"));
        contactPanel.add(new JLabel("Email: student@edu.bntu.by"));
        contactPanel.add(new JLabel("Телефон: +375 (29) XXX-XX-XX"));
        contactPanel.add(new JLabel("Место учебы: БНТУ, г. Минск"));

        // Кнопка закрытия
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Закрыть");

        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(closeButton);

        // Собираем интерфейс
        JPanel topPanel = new JPanel();
        topPanel.add(titleLabel);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.add(photoPanel, BorderLayout.WEST);
        centerPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(new JScrollPane(achievementsArea), BorderLayout.NORTH);
        bottomPanel.add(contactPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setSize(600, 550);
    }

    public static void showDialog(JFrame parent) {
        AboutAuthorWindow dialog = new AboutAuthorWindow(parent);
        dialog.setVisible(true);
    }
}