package logic;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExcelExportService {

    public static boolean exportToExcel(ExamRecord record, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ведомость зачета");

            createHeaderRow(workbook, sheet);
            fillStudentData(sheet, record);
            createSummaryRows(sheet, record);
            autoSizeColumns(sheet);

            return saveWorkbook(workbook, filePath);

        } catch (IOException e) {
            showErrorDialog("Ошибка при сохранении файла: " + e.getMessage());
            return false;
        }
    }

    private static void createHeaderRow(Workbook workbook, Sheet sheet) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        Row headerRow = sheet.createRow(0);

        String[] headers = {"№", "ФИО студента", "Оценка", "Результат"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    private static void fillStudentData(Sheet sheet, ExamRecord record) {
        int rowNum = 1;
        for (Student student : record.getStudents()) {
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(rowNum); // №
            row.createCell(1).setCellValue(student.getFullName()); // ФИО
            row.createCell(2).setCellValue(student.getScore()); // Оценка
            row.createCell(3).setCellValue(student.isPassed() ? "Сдал" : "Не сдал"); // Результат
            rowNum++;
        }
    }

    private static void createSummaryRows(Sheet sheet, ExamRecord record) {
        int lastRow = sheet.getLastRowNum();

        Row summaryRow1 = sheet.createRow(lastRow + 2);
        summaryRow1.createCell(0).setCellValue("ИТОГИ:");

        Row summaryRow2 = sheet.createRow(lastRow + 3);
        summaryRow2.createCell(0).setCellValue("Всего студентов: " + record.getTotalStudents());

        Row summaryRow3 = sheet.createRow(lastRow + 4);
        summaryRow3.createCell(0).setCellValue("Сдали: " + record.getPassedCount());

        Row summaryRow4 = sheet.createRow(lastRow + 5);
        summaryRow4.createCell(0).setCellValue("Не сдали: " + record.getFailedCount());
    }

    private static void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 4; i++) { // Теперь 4 колонки вместо 5
            sheet.autoSizeColumn(i);
        }
    }

    private static boolean saveWorkbook(Workbook workbook, String filePath) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            return true;
        }
    }

    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static String generateFileName(String subject) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        String cleanSubject = subject.replaceAll("[^a-zA-Z0-9а-яА-Я]", "_");
        return "ведомость_" + cleanSubject + "_" + timestamp + ".xlsx";
    }
}