package logic;

public class GradeCalculator {

    public static String calculateResults(ExamRecord record) {
        int total = record.getTotalStudents();
        int withGrade = record.getStudentsWithGrade();
        int passed = record.getPassedCount();
        int failed = record.getFailedCount();
        int withoutGrade = record.getStudentsWithoutGrade();

        double percentage = withGrade > 0 ? (passed * 100.0 / withGrade) : 0;

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