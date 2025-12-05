package ui;

import logic.ExamRecord;
import logic.Student;
import javax.swing.*;
import java.awt.*;

/**
 * Класс для формирования печатной формы экзаменационной ведомости.
 * Создает текстовое представление данных для печати или просмотра.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class PrintTemplate {

    /**
     * Формирует и отображает ведомость для печати.
     *
     * @param record - данные экзаменационной ведомости
     * @param email  - email для отправки результатов
     */
    public static void printRecord(ExamRecord record, String email) {
        // Текстовое поле для отображения ведомости
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Моноширинный шрифт

        // Формируем содержимое ведомости
        StringBuilder sb = new StringBuilder();
        createHeader(sb, record, email);
        createTableHeader(sb);
        createStudentData(sb, record);
        createSummary(sb, record);

        // Устанавливаем текст в поле
        textArea.setText(sb.toString());

        // Помещаем в прокручиваемую панель
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // Отображаем диалоговое окно
        JOptionPane.showMessageDialog(null, scrollPane, "Ведомость для печати", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Создает заголовок ведомости.
     */
    private static void createHeader(StringBuilder sb, ExamRecord record, String email) {
        sb.append("ВЕДОМОСТЬ ДЛЯ ПРОВЕДЕНИЯ АТТЕСТАЦИИ\n");
        sb.append("===================================\n");
        sb.append("Предмет: ").append(record.getSubject()).append("\n");
        sb.append("Дата: ").append(record.getDate()).append("\n");
        sb.append("Email для отправки: ").append(email).append("\n\n");
    }

    /**
     * Создает заголовок таблицы с данными студентов.
     */
    private static void createTableHeader(StringBuilder sb) {
        sb.append(String.format("%-30s %-8s %-10s\n",
                "ФИО студента", "Оценка", "Результат"));
        sb.append("------------------------------------------------\n");
    }

    /**
     * Создает строки с данными студентов.
     */
    private static void createStudentData(StringBuilder sb, ExamRecord record) {
        for (Student student : record.getStudents()) {
            String grade = student.hasGrade() ? String.valueOf(student.getScore()) : "Нет оценки";
            sb.append(String.format("%-30s %-8s %-10s\n",
                    student.getFullName(),
                    grade,
                    student.getResultText()));
        }
    }

    /**
     * Создает раздел с итоговой статистикой.
     */
    private static void createSummary(StringBuilder sb, ExamRecord record) {
        sb.append("\nИТОГИ:\n");
        sb.append("Всего студентов: ").append(record.getTotalStudents()).append("\n");
        sb.append("С оценкой: ").append(record.getStudentsWithGrade()).append("\n");
        sb.append("Без оценки: ").append(record.getStudentsWithoutGrade()).append("\n");
        sb.append("Сдали: ").append(record.getPassedCount()).append("\n");
        sb.append("Не сдали: ").append(record.getFailedCount()).append("\n");
    }
}