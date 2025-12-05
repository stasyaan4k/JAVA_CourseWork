package logic;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcelExportService {

    public static boolean exportToExcel(ExamRecord record, String email, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ведомость зачета");

            // Создаем строки с метаданными
            createMetadataRows(sheet, record, email);

            // Пустая строка для разделения
            sheet.createRow(3);

            // Создаем заголовок таблицы
            createHeaderRow(workbook, sheet, 4);

            // Заполняем данные студентов
            fillStudentData(sheet, record, 5);

            // Создаем строки с итогами
            createSummaryRows(sheet, record);

            // Автоматически подгоняем ширину колонок
            autoSizeColumns(sheet);

            return saveWorkbook(workbook, filePath);

        } catch (IOException e) {
            showErrorDialog("Ошибка при сохранении файла: " + e.getMessage());
            return false;
        }
    }

    private static void createMetadataRows(Sheet sheet, ExamRecord record, String email) {
        CellStyle labelStyle = createLabelStyle(sheet.getWorkbook());
        CellStyle valueStyle = createValueStyle(sheet.getWorkbook());

        // Строка 0: Предмет
        Row subjectRow = sheet.createRow(0);
        Cell subjectLabel = subjectRow.createCell(0);
        subjectLabel.setCellValue("Предмет:");
        subjectLabel.setCellStyle(labelStyle);

        Cell subjectValue = subjectRow.createCell(1);
        subjectValue.setCellValue(record.getSubject());
        subjectValue.setCellStyle(valueStyle);

        // Строка 1: Дата аттестации
        Row dateRow = sheet.createRow(1);
        Cell dateLabel = dateRow.createCell(0);
        dateLabel.setCellValue("Дата аттестации:");
        dateLabel.setCellStyle(labelStyle);

        Cell dateValue = dateRow.createCell(1);
        dateValue.setCellValue(record.getDate());
        dateValue.setCellStyle(valueStyle);

        // Строка 2: Email
        Row emailRow = sheet.createRow(2);
        Cell emailLabel = emailRow.createCell(0);
        emailLabel.setCellValue("Email для отправки:");
        emailLabel.setCellStyle(labelStyle);

        Cell emailValue = emailRow.createCell(1);
        emailValue.setCellValue(email);
        emailValue.setCellStyle(valueStyle);
    }

    private static CellStyle createLabelStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private static CellStyle createValueStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private static void createHeaderRow(Workbook workbook, Sheet sheet, int rowNum) {
        CellStyle headerStyle = createHeaderStyle(workbook);
        Row headerRow = sheet.createRow(rowNum);

        String[] headers = {"№", "ФИО студента", "Оценка (0-10)", "Результат"};
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

        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);

        return style;
    }

    private static void fillStudentData(Sheet sheet, ExamRecord record, int startRow) {
        int rowNum = startRow;
        for (Student student : record.getStudents()) {
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(rowNum - startRow + 1); // №
            row.createCell(1).setCellValue(student.getFullName()); // ФИО

            // Оценка
            if (student.hasGrade()) {
                row.createCell(2).setCellValue(student.getScore());
            } else {
                row.createCell(2).setCellValue("Нет оценки");
            }

            // Результат
            row.createCell(3).setCellValue(student.getResultText());
            rowNum++;
        }
    }

    private static void createSummaryRows(Sheet sheet, ExamRecord record) {
        int lastRow = sheet.getLastRowNum();

        Row summaryRow1 = sheet.createRow(lastRow + 2);
        summaryRow1.createCell(0).setCellValue("ИТОГИ:");
        summaryRow1.getCell(0).setCellStyle(createSummaryStyle(sheet.getWorkbook()));

        Row summaryRow2 = sheet.createRow(lastRow + 3);
        summaryRow2.createCell(0).setCellValue("Всего студентов: " + record.getTotalStudents());

        Row summaryRow3 = sheet.createRow(lastRow + 4);
        summaryRow3.createCell(0).setCellValue("С оценкой: " + record.getStudentsWithGrade());

        Row summaryRow4 = sheet.createRow(lastRow + 5);
        summaryRow4.createCell(0).setCellValue("Без оценки: " + record.getStudentsWithoutGrade());

        Row summaryRow5 = sheet.createRow(lastRow + 6);
        summaryRow5.createCell(0).setCellValue("Сдали: " + record.getPassedCount());

        Row summaryRow6 = sheet.createRow(lastRow + 7);
        summaryRow6.createCell(0).setCellValue("Не сдали: " + record.getFailedCount());
    }

    private static CellStyle createSummaryStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        return style;
    }

    private static void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private static boolean saveWorkbook(Workbook workbook, String filePath) throws IOException {
        try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(filePath)) {
            workbook.write(fileOut);
            return true;
        }
    }

    public static ExcelImportResult importFromExcel(String filePath) {
        ExcelImportResult result = new ExcelImportResult();
        List<Student> students = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook;

            // Определяем формат файла
            if (filePath.toLowerCase().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (filePath.toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                showErrorDialog("Неподдерживаемый формат файла");
                return result;
            }

            Sheet sheet = workbook.getSheetAt(0); // Берем первый лист

            // Читаем метаданные
            String subject = "";
            String date = "";
            String email = "";

            // Ищем строки с метаданными по содержимому
            for (int i = 0; i <= Math.min(10, sheet.getLastRowNum()); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell firstCell = row.getCell(0);
                if (firstCell == null) continue;

                String cellValue = getCellValueAsString(firstCell).toLowerCase();

                if (cellValue.contains("предмет:")) {
                    Cell valueCell = row.getCell(1);
                    if (valueCell != null) {
                        subject = getCellValueAsString(valueCell);
                    }
                } else if (cellValue.contains("дата") && cellValue.contains("аттестации:")) {
                    Cell valueCell = row.getCell(1);
                    if (valueCell != null) {
                        date = getCellValueAsString(valueCell);
                    }
                } else if (cellValue.contains("email") || cellValue.contains("почта")) {
                    Cell valueCell = row.getCell(1);
                    if (valueCell != null) {
                        email = getCellValueAsString(valueCell);
                    }
                }
            }

            result.setSubject(subject);
            result.setDate(date);
            result.setEmail(email);

            // Ищем начало таблицы студентов
            int tableStartRow = findTableStart(sheet);

            if (tableStartRow == -1) {
                // Если не нашли заголовок таблицы, ищем данные по структуре
                tableStartRow = findDataStart(sheet);
            }

            // Читаем студентов
            if (tableStartRow != -1) {
                for (int i = tableStartRow; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    // Пропускаем пустые строки и строки с итогами
                    if (isSummaryRow(row) || isEmptyRow(row)) {
                        continue;
                    }

                    // Пытаемся прочитать студента
                    Student student = readStudentFromRow(row);
                    if (student != null) {
                        students.add(student);
                    }
                }
            }

            result.setStudents(students);
            workbook.close();

        } catch (Exception e) {
            showErrorDialog("Ошибка при чтении файла Excel: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    private static int findTableStart(Sheet sheet) {
        // Ищем заголовок таблицы
        for (int i = 0; i <= Math.min(20, sheet.getLastRowNum()); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // Проверяем, является ли строка заголовком таблицы
            if (isTableHeader(row)) {
                return i + 1; // Следующая строка после заголовка
            }
        }
        return -1;
    }

    private static boolean isTableHeader(Row row) {
        // Проверяем, содержит ли строка заголовки таблицы
        String[] expectedHeaders = {"№", "фио", "оценка", "результат"};
        int matchCount = 0;

        for (int i = 0; i < Math.min(4, row.getLastCellNum()); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String cellValue = getCellValueAsString(cell).toLowerCase();
                for (String header : expectedHeaders) {
                    if (cellValue.contains(header)) {
                        matchCount++;
                        break;
                    }
                }
            }
        }

        return matchCount >= 2; // Если найдено хотя бы 2 совпадения
    }

    private static int findDataStart(Sheet sheet) {
        // Если не нашли заголовок, ищем начало данных
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // Ищем строку, где есть ФИО (обычно во второй колонке)
            Cell nameCell = row.getCell(1); // Колонка B
            if (nameCell != null) {
                String name = getCellValueAsString(nameCell).trim();
                // Проверяем, что это похоже на ФИО (не пустое, не номер, не заголовок)
                if (!name.isEmpty() &&
                        !name.matches("\\d+") &&
                        !name.toLowerCase().contains("фио") &&
                        !name.toLowerCase().contains("итог") &&
                        name.length() > 2) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean isSummaryRow(Row row) {
        if (row == null) return false;

        // Проверяем первую ячейку на наличие итоговой информации
        Cell firstCell = row.getCell(0);
        if (firstCell != null) {
            String value = getCellValueAsString(firstCell).toLowerCase();
            return value.contains("итог") ||
                    value.contains("всего") ||
                    value.contains("сдали") ||
                    value.contains("не сдали");
        }
        return false;
    }

    private static boolean isEmptyRow(Row row) {
        if (row == null) return true;

        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                String value = getCellValueAsString(cell).trim();
                if (!value.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Student readStudentFromRow(Row row) {
        // Ожидаемая структура:
        // Колонка 0: № (может быть пустой)
        // Колонка 1: ФИО
        // Колонка 2: Оценка (может быть пустой)
        // Колонка 3: Результат (может быть пустой)

        Cell nameCell = row.getCell(1); // ФИО
        if (nameCell == null) {
            // Пробуем другие колонки
            nameCell = row.getCell(0);
            if (nameCell == null) return null;
        }

        String fullName = getCellValueAsString(nameCell).trim();
        if (fullName.isEmpty() ||
                fullName.matches("\\d+") || // Не номер
                fullName.toLowerCase().contains("фио") || // Не заголовок
                fullName.toLowerCase().contains("итог")) { // Не итоги
            return null;
        }

        // Ищем оценку
        int score = -1;

        // Пробуем разные колонки для оценки
        for (int i = 2; i <= 3; i++) {
            Cell scoreCell = row.getCell(i);
            if (scoreCell != null) {
                String scoreText = getCellValueAsString(scoreCell).trim();
                if (!scoreText.isEmpty() && !scoreText.equalsIgnoreCase("нет оценки")) {
                    try {
                        score = Integer.parseInt(scoreText);
                        if (score >= 0 && score <= 10) {
                            break; // Нашли валидную оценку
                        } else {
                            score = -1; // Невалидная оценка
                        }
                    } catch (NumberFormatException e) {
                        // Не число, пробуем следующую колонку
                    }
                }
            }
        }
        return new Student(fullName, score);
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Проверяем, целое ли число
                    double value = cell.getNumericCellValue();
                    if (value == Math.floor(value)) {
                        return String.valueOf((int) value);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return getCellValueAsString(cell);
                } catch (Exception e) {
                    return cell.getCellFormula();
                }
            default:
                return "";
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