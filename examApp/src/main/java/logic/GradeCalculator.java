package logic;

/**
 * Класс для расчета статистики по экзаменационной ведомости.
 * Вычисляет основные показатели успеваемости студентов.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class GradeCalculator {

    /**
     * Рассчитывает итоговую статистику по ведомости.
     *
     * @param record - экзаменационная ведомость
     * @return String - форматированная строка с результатами
     */
    public static String calculateResults(ExamRecord record) {
        // Получаем основные показатели
        int total = record.getTotalStudents();
        int withGrade = record.getStudentsWithGrade();
        int passed = record.getPassedCount();
        int failed = record.getFailedCount();
        int withoutGrade = record.getStudentsWithoutGrade();

        // Рассчитываем процент сдачи
        double percentage = withGrade > 0 ? (passed * 100.0 / withGrade) : 0;

        // Форматируем результаты
        return String.format(
                "ИТОГИ АТТЕСТАЦИИ:\n" +
                        "Предмет: %s\n" +
                        "Дата: %s\n" +
                        "Всего студентов: %d\n" +
                        "С оценкой: %d\n" +
                        "Без оценки: %d\n" +
                        "Сдали: %d\n" +
                        "Не сдали: %d\n" +
                        "Процент сдачи (от имеющих оценку): %.1f%%",
                record.getSubject(), record.getDate(), total, withGrade,
                withoutGrade, passed, failed, percentage
        );
    }
}