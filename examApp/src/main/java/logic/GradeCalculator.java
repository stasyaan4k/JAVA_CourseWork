package logic;

public class GradeCalculator {

    public static String calculateResults(ExamRecord record) {
        int total = record.getTotalStudents();
        int passed = record.getPassedCount();
        int failed = record.getFailedCount();
        double percentage = total > 0 ? (passed * 100.0 / total) : 0;

        return String.format(
                "ИТОГИ АТТЕСТАЦИИ:\n" +
                        "Предмет: %s\n" +
                        "Дата: %s\n" +
                        "Всего студентов: %d\n" +
                        "Сдали: %d\n" +
                        "Не сдали: %d\n" +
                        "Процент сдачи: %.1f%%",
                record.getSubject(), record.getDate(), total, passed, failed, percentage
        );
    }
}