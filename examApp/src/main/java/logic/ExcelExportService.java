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

/**
 * Сервис для экспорта и импорта данных в формате Excel.
 * Использует библиотеку Apache POI для работы с файлами .xlsx и .xls.
 *
 * @version 1.0
 * @author Маленков Станислав Владимирович
 */
public class ExcelExportService {

    /**
     * Экспортирует данные ведомости в файл Excel.
     *
     * @param record   - данные экзамена
     * @param email    - email для отправки
     * @param filePath - путь для сохранения
     * @return boolean - true если успешно
     */
    public static boolean exportToExcel(ExamRecord record, String email, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ведомость зачета");

            // Создаем метаданные
            createMetadataRows(sheet, record, email);
            sheet.createRow(3); // Пустая строка

            // Создаем таблицу
            createHeaderRow(workbook, sheet, 4);
            fillStudentData(sheet, record, 5);
            createSummaryRows(sheet, record);

            autoSizeColumns(sheet);
            return saveWorkbook(workbook, filePath);

        } catch (IOException e) {
            showErrorDialog("Ошибка при сохранении файла: " + e.getMessage());
            return false;
        }
    }

    /**
     * Создает строки с метаданными (предмет, дата, email).
     */
    private static void createMetadataRows(Sheet sheet, ExamRecord record, String email) {
        CellStyle labelStyle = createLabelStyle(sheet.getWorkbook());
        CellStyle valueStyle = createValueStyle(sheet.getWorkbook());

        // Строка 0: Предмет
        Row subjectRow = sheet.createRow(0);
        subjectRow.createCell(0).setCellValue("Предмет:");
        subjectRow.createCell(1).setCellValue(record.getSubject());
        subjectRow.getCell(0).setCellStyle(labelStyle);
        subjectRow.getCell(1).setCellStyle(valueStyle);

        // Строка 1: Дата
        Row dateRow = sheet.createRow(1);
        dateRow.createCell(0).setCellValue("Дата аттестации:");
        dateRow.createCell(1).setCellValue(record.getDate());
        dateRow.getCell(0).setCellStyle(labelStyle);
        dateRow.getCell(1).setCellStyle(valueStyle);

        // Строка 2: Email
        Row emailRow = sheet.createRow(2);
        emailRow.createCell(0).setCellValue("Email для отправки:");
        emailRow.createCell(1).setCellValue(email);
        emailRow.getCell(0).setCellStyle(labelStyle);
        emailRow.getCell(1).setCellStyle(valueStyle);
    }

    /**
     * Создает стиль для меток (жирный шрифт).
     */
    private static CellStyle createLabelStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    /**
     * Создает стиль для значений (желтый фон).
     */
    private static CellStyle createValueStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    /**
     * Создает заголовок таблицы.
     */
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

    /**
     * Создает стиль для заголовка таблицы.
     */
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

    /**
     * Заполняет данные студентов в таблицу.
     */
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

    /**
     * Создает строки с итоговой статистикой.
     */
    private static void createSummaryRows(Sheet sheet, ExamRecord record) {
        int lastRow = sheet.getLastRowNum();
        CellStyle summaryStyle = createSummaryStyle(sheet.getWorkbook());

        sheet.createRow(lastRow + 2).createCell(0).setCellValue("ИТОГИ:");
        sheet.getRow(lastRow + 2).getCell(0).setCellStyle(summaryStyle);

        sheet.createRow(lastRow + 3).createCell(0).setCellValue("Всего студентов: " + record.getTotalStudents());
        sheet.createRow(lastRow + 4).createCell(0).setCellValue("С оценкой: " + record.getStudentsWithGrade());
        sheet.createRow(lastRow + 5).createCell(0).setCellValue("Без оценки: " + record.getStudentsWithoutGrade());
        sheet.createRow(lastRow + 6).createCell(0).setCellValue("Сдали: " + record.getPassedCount());
        sheet.createRow(lastRow + 7).createCell(0).setCellValue("Не сдали: " + record.getFailedCount());
    }

    /**
     * Создает стиль для итоговой строки.
     */
    private static CellStyle createSummaryStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        return style;
    }

    /**
     * Автоматически подгоняет ширину колонок.
     */
    private static void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Сохраняет книгу Excel в файл.
     */
    private static boolean saveWorkbook(Workbook workbook, String filePath) throws IOException {
        try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(filePath)) {
            workbook.write(fileOut);
            return true;
        }
    }

    /**
     * Импортирует данные из файла Excel.
     *
     * @param filePath - путь к файлу Excel
     * @return ExcelImportResult - результат импорта
     */
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

            Sheet sheet = workbook.getSheetAt(0); // Первый лист

            // Читаем метаданные
            readMetadata(sheet, result);

            // Ищем начало таблицы студентов
            int tableStartRow = findTableStart(sheet);
            if (tableStartRow == -1) {
                tableStartRow = findDataStart(sheet);
            }

            // Читаем студентов
            if (tableStartRow != -1) {
                for (int i = tableStartRow; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    if (isSummaryRow(row) || isEmptyRow(row)) {
                        continue; // Пропускаем итоги и пустые строки
                    }

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

    /**
     * Читает метаданные из файла Excel.
     */
    private static void readMetadata(Sheet sheet, ExcelImportResult result) {
        String subject = "", date = "", email = "";

        for (int i = 0; i <= Math.min(10, sheet.getLastRowNum()); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell firstCell = row.getCell(0);
            if (firstCell == null) continue;

            String cellValue = getCellValueAsString(firstCell).toLowerCase();

            if (cellValue.contains("предмет:")) {
                subject = getCellValueAsString(row.getCell(1));
            } else if (cellValue.contains("дата") && cellValue.contains("аттестации:")) {
                date = getCellValueAsString(row.getCell(1));
            } else if (cellValue.contains("email") || cellValue.contains("почта")) {
                email = getCellValueAsString(row.getCell(1));
            }
        }

        result.setSubject(subject);
        result.setDate(date);
        result.setEmail(email);
    }

    /**
     * Находит начало таблицы студентов по заголовку.
     */
    private static int findTableStart(Sheet sheet) {
        for (int i = 0; i <= Math.min(20, sheet.getLastRowNum()); i++) {
            Row row = sheet.getRow(i);
            if (row != null && isTableHeader(row)) {
                return i + 1; // Строка после заголовка
            }
        }
        return -1;
    }

    /**
     * Проверяет, является ли строка заголовком таблицы.
     */
    private static boolean isTableHeader(Row row) {
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

        return matchCount >= 2; // Хотя бы 2 совпадения
    }

    /**
     * Находит начало данных по содержимому.
     */
    private static int findDataStart(Sheet sheet) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell nameCell = row.getCell(1); // Колонка B
            if (nameCell != null) {
                String name = getCellValueAsString(nameCell).trim();
                if (!name.isEmpty() && !name.matches("\\d+") &&
                        !name.toLowerCase().contains("фио") &&
                        !name.toLowerCase().contains("итог") &&
                        name.length() > 2) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Проверяет, является ли строка итоговой.
     */
    private static boolean isSummaryRow(Row row) {
        if (row == null) return false;

        Cell firstCell = row.getCell(0);
        if (firstCell != null) {
            String value = getCellValueAsString(firstCell).toLowerCase();
            return value.contains("итог") || value.contains("всего") ||
                    value.contains("сдали") || value.contains("не сдали");
        }
        return false;
    }

    /**
     * Проверяет, является ли строка пустой.
     */
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

    /**
     * Читает данные студента из строки Excel.
     */
    private static Student readStudentFromRow(Row row) {
        Cell nameCell = row.getCell(1); // ФИО в колонке B
        if (nameCell == null) {
            nameCell = row.getCell(0); // Пробуем колонку A
            if (nameCell == null) return null;
        }

        String fullName = getCellValueAsString(nameCell).trim();
        if (fullName.isEmpty() || fullName.matches("\\d+") ||
                fullName.toLowerCase().contains("фио") ||
                fullName.toLowerCase().contains("итог")) {
            return null;
        }

        // Ищем оценку в колонках 2-3
        int score = -1;
        for (int i = 2; i <= 3; i++) {
            Cell scoreCell = row.getCell(i);
            if (scoreCell != null) {
                String scoreText = getCellValueAsString(scoreCell).trim();
                if (!scoreText.isEmpty() && !scoreText.equalsIgnoreCase("нет оценки")) {
                    try {
                        score = Integer.parseInt(scoreText);
                        if (score >= 0 && score <= 10) {
                            break; // Нашли валидную оценку
                        }
                    } catch (NumberFormatException e) {
                        // Пропускаем, пробуем следующую колонку
                    }
                }
            }
        }
        return new Student(fullName, score);
    }

    /**
     * Преобразует значение ячейки в строку.
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
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

    /**
     * Показывает диалог с ошибкой.
     */
    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Генерирует имя файла на основе предмета и времени.
     *
     * @param subject - название предмета
     * @return String - имя файла
     */
    public static String generateFileName(String subject) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        String cleanSubject = subject.replaceAll("[^a-zA-Z0-9а-яА-Я]", "_");
        return "ведомость_" + cleanSubject + "_" + timestamp + ".xlsx";
    }
}