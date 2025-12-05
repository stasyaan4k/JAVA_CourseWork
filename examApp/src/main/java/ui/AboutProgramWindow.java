package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Диалоговое окно "Сведения о программе".
 * Содержит информацию о назначении, возможностях и требованиях программы.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class AboutProgramWindow extends JDialog {

    /**
     * Создает окно с информацией о программе.
     *
     * @param parent - родительское окно
     */
    public AboutProgramWindow(JFrame parent) {
        super(parent, "Сведения о программе", true);
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
        JLabel titleLabel = createTitleLabel();

        // Изображение (заглушка)
        JLabel imageLabel = createImageLabel();

        // Описание программы
        JScrollPane descriptionScroll = createDescriptionArea();

        // Системные требования
        JPanel requirementsPanel = createRequirementsPanel();

        // Кнопка закрытия
        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());

        // Собираем интерфейс
        assembleInterface(mainPanel, titleLabel, imageLabel,
                descriptionScroll, requirementsPanel, closeButton);

        add(mainPanel);
    }

    /**
     * Создает заголовок окна.
     */
    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Программа 'Экзаменационная ведомость'", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        return titleLabel;
    }

    /**
     * Создает метку с изображением программы.
     */
    private JLabel createImageLabel() {
        JLabel imageLabel = new JLabel(new ImageIcon("program_image.jpg"));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return imageLabel;
    }

    /**
     * Создает текстовое поле с описанием программы.
     */
    private JScrollPane createDescriptionArea() {
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("НАЗНАЧЕНИЕ ПРОГРАММЫ\n\n" +
                "Программа 'Экзаменационная ведомость' предназначена для автоматизации процесса " +
                "формирования и ведения электронных ведомостей успеваемости студентов.\n\n" +
                "ОСНОВНЫЕ ВОЗМОЖНОСТИ:\n" +
                "1. Ведение базы данных студентов\n" +
                "2. Ввод и редактирование оценок\n" +
                "3. Автоматическое определение результатов сдачи\n" +
                "4. Формирование статистики успеваемости\n" +
                "5. Экспорт данных в формат Excel\n" +
                "6. Печать ведомостей\n" +
                "7. Отправка результатов по email\n\n" +
                "ТЕХНИЧЕСКИЕ ХАРАКТЕРИСТИКИ:\n" +
                "• Язык программирования: Java 8+\n" +
                "• Графическая библиотека: Swing\n" +
                "• Формат данных: XML/Excel\n" +
                "• Архитектура: MVC\n" +
                "• Системные требования: Windows/Linux/Mac, JRE 1.8+\n\n" +
                "МЕТОДИЧЕСКОЕ ОБЕСПЕЧЕНИЕ:\n" +
                "Программа разработана в соответствии с требованиями курсовой работы " +
                "по дисциплине 'Программирование на языке Java' БНТУ.");
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setPreferredSize(new Dimension(500, 350));

        return descriptionScroll;
    }

    /**
     * Создает панель с системными требованиями.
     */
    private JPanel createRequirementsPanel() {
        JPanel requirementsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        requirementsPanel.setBorder(BorderFactory.createTitledBorder("Системные требования"));
        requirementsPanel.add(new JLabel("• Операционная система: Windows 7/8/10/11, Linux, macOS"));
        requirementsPanel.add(new JLabel("• Процессор: Intel Core i3 или эквивалент"));
        requirementsPanel.add(new JLabel("• Оперативная память: 2 ГБ"));
        requirementsPanel.add(new JLabel("• Свободное место на диске: 50 МБ"));
        requirementsPanel.add(new JLabel("• Java Runtime Environment 1.8 или выше"));

        return requirementsPanel;
    }

    /**
     * Собирает все компоненты в интерфейс.
     */
    private void assembleInterface(JPanel mainPanel, JLabel titleLabel,
                                   JLabel imageLabel, JScrollPane descriptionScroll,
                                   JPanel requirementsPanel, JButton closeButton) {
        JPanel topPanel = new JPanel();
        topPanel.add(titleLabel);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.add(imageLabel, BorderLayout.NORTH);
        centerPanel.add(descriptionScroll, BorderLayout.CENTER);
        centerPanel.add(requirementsPanel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);

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
        AboutProgramWindow dialog = new AboutProgramWindow(parent);
        dialog.setVisible(true);
    }
}