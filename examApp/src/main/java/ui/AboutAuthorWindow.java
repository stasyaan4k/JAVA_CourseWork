package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Диалоговое окно "Сведения об авторе".
 * Содержит информацию о разработчике программы.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class AboutAuthorWindow extends JDialog {

    /**
     * Создает окно с информацией об авторе.
     *
     * @param parent - родительское окно
     */
    public AboutAuthorWindow(JFrame parent) {
        super(parent, "Сведения об авторе", true);
        initialize();
        pack();
        setLocationRelativeTo(parent); // Центрирование
    }

    /**
     * Инициализирует компоненты окна.
     */
    private void initialize() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Заголовок окна
        JLabel titleLabel = new JLabel("Информация об авторе программы", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 102, 204));

        // Панель с фотографией (заглушка)
        JPanel photoPanel = createPhotoPanel();

        // Панель с информацией об авторе
        JPanel infoPanel = createInfoPanel();

        // Достижения и навыки
        JTextArea achievementsArea = createAchievementsArea();

        // Контактная информация
        JPanel contactPanel = createContactPanel();

        // Кнопка закрытия
        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());

        // Собираем интерфейс
        assembleInterface(mainPanel, titleLabel, photoPanel, infoPanel,
                achievementsArea, contactPanel, closeButton);

        add(mainPanel);
        setSize(600, 550);
    }

    /**
     * Создает панель с фотографией автора.
     */
    private JPanel createPhotoPanel() {
        JPanel photoPanel = new JPanel(new BorderLayout());
        JLabel photoLabel = new JLabel("ФОТО", SwingConstants.CENTER);
        photoLabel.setPreferredSize(new Dimension(150, 200));
        photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        photoLabel.setBackground(new Color(240, 240, 240));
        photoLabel.setOpaque(true);
        photoLabel.setFont(new Font("Arial", Font.BOLD, 14));

        photoPanel.add(photoLabel, BorderLayout.CENTER);
        photoPanel.add(new JLabel("Фотография автора", SwingConstants.CENTER), BorderLayout.SOUTH);

        return photoPanel;
    }

    /**
     * Создает панель с информацией об авторе.
     */
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ФИО студента
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("ФИО студента:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("[Ваше ФИО]"), gbc);

        // Номер группы
        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Номер группы:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("[Ваша группа]"), gbc);

        // Факультет
        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Факультет:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("ФИТР"), gbc);

        // Специальность
        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Специальность:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("Программное обеспечение информационных технологий"), gbc);

        // Преподаватель
        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Преподаватель:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("В.В. Сидорик"), gbc);

        // Год разработки
        gbc.gridx = 0; gbc.gridy = 5;
        infoPanel.add(new JLabel("Год разработки:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel("2025"), gbc);

        return infoPanel;
    }

    /**
     * Создает текстовое поле с достижениями.
     */
    private JTextArea createAchievementsArea() {
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

        return achievementsArea;
    }

    /**
     * Создает панель с контактной информацией.
     */
    private JPanel createContactPanel() {
        JPanel contactPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        contactPanel.setBorder(BorderFactory.createTitledBorder("Контактная информация"));
        contactPanel.add(new JLabel("Email: student@edu.bntu.by"));
        contactPanel.add(new JLabel("Телефон: +375 (29) XXX-XX-XX"));
        contactPanel.add(new JLabel("Место учебы: БНТУ, г. Минск"));

        return contactPanel;
    }

    /**
     * Собирает все компоненты в интерфейс.
     */
    private void assembleInterface(JPanel mainPanel, JLabel titleLabel,
                                   JPanel photoPanel, JPanel infoPanel,
                                   JTextArea achievementsArea, JPanel contactPanel,
                                   JButton closeButton) {
        JPanel topPanel = new JPanel();
        topPanel.add(titleLabel);

        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.add(photoPanel, BorderLayout.WEST);
        centerPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(new JScrollPane(achievementsArea), BorderLayout.NORTH);
        bottomPanel.add(contactPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Отображает диалоговое окно.
     *
     * @param parent - родительское окно
     */
    public static void showDialog(JFrame parent) {
        AboutAuthorWindow dialog = new AboutAuthorWindow(parent);
        dialog.setVisible(true);
    }
}