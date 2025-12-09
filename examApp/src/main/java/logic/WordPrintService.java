package logic;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Упрощенный сервис для печати ведомости в текстовый файл
 * Используется, если не установлены библиотеки Apache POI
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class WordPrintService {

    /**
     * Создает текстовый файл с ведомостью
     */
    public static boolean printToWord(ExamRecord record, String email) {
        try {
            // Генерируем имя файла
            String fileName = generateFileName(record.getSubject());

            // Создаем содержимое файла
            StringBuilder content = new StringBuilder();

            // Заголовок
            content.append("ВЕДОМОСТЬ ДЛЯ ПРОВЕДЕНИЯ АТТЕСТАЦИИ\n");
            content.append("===================================\n\n");
            content.append("Предмет: ").append(record.getSubject()).append("\n");
            content.append("Дата аттестации: ").append(record.getDate()).append("\n");
            content.append("Email для отправки: ").append(email).append("\n\n");

            // Таблица студентов
            content.append("СПИСОК СТУДЕНТОВ:\n");
            content.append("=================================================================\n");
            content.append(String.format("%-5s %-35s %-10s %-15s\n",
                    "№", "ФИО студента", "Оценка", "Результат"));
            content.append("=================================================================\n");

            int counter = 1;
            for (Student student : record.getStudents()) {
                String grade = student.hasGrade() ? String.valueOf(student.getScore()) : "Нет оценки";
                content.append(String.format("%-5d %-35s %-10s %-15s\n",
                        counter++,
                        student.getFullName(),
                        grade,
                        student.getResultText()));
            }

            // Статистика
            content.append("\n\nИТОГИ:\n");
            content.append("=================================================================\n");
            content.append("Всего студентов: ").append(record.getTotalStudents()).append("\n");
            content.append("С оценкой: ").append(record.getStudentsWithGrade()).append("\n");
            content.append("Без оценки: ").append(record.getStudentsWithoutGrade()).append("\n");
            content.append("Сдали: ").append(record.getPassedCount()).append("\n");
            content.append("Не сдали: ").append(record.getFailedCount()).append("\n");

            // Проценты
            int withGrade = record.getStudentsWithGrade();
            int passed = record.getPassedCount();
            if (withGrade > 0) {
                double percentage = (passed * 100.0) / withGrade;
                content.append(String.format("Процент сдачи (от имеющих оценку): %.1f%%\n", percentage));
            }

            content.append("\n\n");
            content.append("Преподаватель: ___________________\n");
            content.append("Дата: ").append(LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))).append("\n");

            // Сохраняем файл
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(content.toString());
            }

            // Показываем сообщение
            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Ведомость успешно сохранена в файл: " + fileName + "\n\nХотите открыть файл?",
                    "Печать завершена",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                openFile(fileName);
            }

            return true;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Ошибка при создании файла: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Генерирует имя файла
     */
    private static String generateFileName(String subject) {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        String cleanSubject = subject.replaceAll("[^a-zA-Z0-9а-яА-Я\\s]", "_");
        return "ведомость_" + cleanSubject + "_" + timestamp + ".txt";
    }

    /**
     * Открывает файл в связанном приложении
     */
    private static void openFile(String fileName) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(new java.io.File(fileName));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Не удалось открыть файл автоматически. Файл сохранен по пути: " +
                            new java.io.File(fileName).getAbsolutePath(),
                    "Информация",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}