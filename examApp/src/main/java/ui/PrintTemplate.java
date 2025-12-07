package ui;

import logic.ExamRecord;
import logic.Student;
import javax.swing.*;
import java.awt.*;

public class PrintTemplate {

    public static void printRecord(ExamRecord record, String email) {
        // Создаем текстовое представление
        StringBuilder sb = new StringBuilder();

        sb.append("ВЕДОМОСТЬ ДЛЯ ПРОВЕДЕНИЯ АТТЕСТАЦИИ\n");
        sb.append("===================================\n\n");
        sb.append("Предмет: ").append(record.getSubject()).append("\n");
        sb.append("Дата: ").append(record.getDate()).append("\n");
        sb.append("Email: ").append(email).append("\n\n");

        sb.append("СПИСОК СТУДЕНТОВ:\n");
        sb.append("=========================================\n");

        int counter = 1;
        for (Student student : record.getStudents()) {
            String grade = student.hasGrade() ? String.valueOf(student.getScore()) : "Нет оценки";
            sb.append(String.format("%2d. %-30s %-10s %-10s\n",
                    counter++,
                    student.getFullName(),
                    grade,
                    student.getResultText()));
        }

        sb.append("\nСТАТИСТИКА:\n");
        sb.append("=========================================\n");
        sb.append("Всего студентов: ").append(record.getTotalStudents()).append("\n");
        sb.append("С оценкой: ").append(record.getStudentsWithGrade()).append("\n");
        sb.append("Без оценки: ").append(record.getStudentsWithoutGrade()).append("\n");
        sb.append("Сдали: ").append(record.getPassedCount()).append("\n");
        sb.append("Не сдали: ").append(record.getFailedCount()).append("\n");

        // Показываем в диалоговом окне
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(null, scrollPane,
                "Печать ведомости", JOptionPane.INFORMATION_MESSAGE);
    }
}