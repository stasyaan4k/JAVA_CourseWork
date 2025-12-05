package ui;

import logic.ExamRecord;
import logic.Student;
import javax.swing.*;
import java.awt.*;

public class PrintTemplate {

    public static void printRecord(ExamRecord record, String email) {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        sb.append("ВЕДОМОСТЬ ДЛЯ ПРОВЕДЕНИЯ АТТЕСТАЦИИ\n");
        sb.append("===================================\n");
        sb.append("Предмет: ").append(record.getSubject()).append("\n");
        sb.append("Дата: ").append(record.getDate()).append("\n");
        sb.append("Email для отправки: ").append(email).append("\n\n");

        sb.append(String.format("%-30s %-8s %-10s\n",
                "ФИО студента", "Оценка", "Результат"));
        sb.append("------------------------------------------------\n");

        for (Student student : record.getStudents()) {
            String grade = student.hasGrade() ? String.valueOf(student.getScore()) : "Нет оценки";
            sb.append(String.format("%-30s %-8s %-10s\n",
                    student.getFullName(),
                    grade,
                    student.getResultText()));
        }

        sb.append("\nИТОГИ:\n");
        sb.append("Всего студентов: ").append(record.getTotalStudents()).append("\n");
        sb.append("С оценкой: ").append(record.getStudentsWithGrade()).append("\n");
        sb.append("Без оценки: ").append(record.getStudentsWithoutGrade()).append("\n");
        sb.append("Сдали: ").append(record.getPassedCount()).append("\n");
        sb.append("Не сдали: ").append(record.getFailedCount()).append("\n");

        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(null, scrollPane, "Ведомость для печати", JOptionPane.PLAIN_MESSAGE);
    }
}