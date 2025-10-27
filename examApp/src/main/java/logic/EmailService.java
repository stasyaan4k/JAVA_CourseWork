package logic;

import javax.swing.JOptionPane;

public class EmailService {

    public static void sendResults(String email, ExamRecord record) {
        String results = GradeCalculator.calculateResults(record);
        String message = "Результаты зачета отправлены на email: " + email + "\n\n" + results;

        JOptionPane.showMessageDialog(null, message, "Отправка email", JOptionPane.INFORMATION_MESSAGE);
    }
}