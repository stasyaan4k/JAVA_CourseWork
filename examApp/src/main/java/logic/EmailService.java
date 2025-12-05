package logic;

import javax.swing.JOptionPane;

/**
 * Сервис для отправки результатов экзамена по email.
 * Имитирует отправку через диалоговое окно.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class EmailService {

    /**
     * Отправляет результаты экзамена на указанный email.
     * Выводит статистику в диалоговом окне.
     *
     * @param email  - email получателя
     * @param record - данные экзаменационной ведомости
     */
    public static void sendResults(String email, ExamRecord record) {
        // Получаем статистику успеваемости
        String results = GradeCalculator.calculateResults(record);

        // Формируем сообщение
        String message = "Результаты зачета отправлены на email: " + email + "\n\n" + results;

        // Показываем диалоговое окно (имитация отправки)
        JOptionPane.showMessageDialog(null, message, "Отправка email", JOptionPane.INFORMATION_MESSAGE);
    }
}