package com.projektER.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
@Service
public class ExcelManipulator {

    public ExcelManipulator() {
        super();
    }

    public File transfer(int sourceRowIndex, int targetRowIndex, String filePath) throws IOException {
        List<List<String>> rows = readExcel(filePath);
        return unscrambleRows(sourceRowIndex, targetRowIndex, rows);
    }

    private List<List<String>> readExcel(String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(getCellValueAsString(cell));
                }
                data.add(rowData);
            }
        }
        return data;
    }

    private String getCellValueAsString(Cell cell) {
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    private File unscrambleRows(int sourceIndex, int targetIndex, List<List<String>> rows) throws IOException {
        validateIndices(sourceIndex, targetIndex, rows.size());
        
        // Swap rows
        List<String> temp = rows.get(sourceIndex);
        rows.set(sourceIndex, rows.get(targetIndex));
        rows.set(targetIndex, temp);

        return writeExcel(rows);
    }

    private void validateIndices(int source, int target, int rowCount) {
        if (source < 0 || target < 0 || source >= rowCount || target >= rowCount) {
            throw new IllegalArgumentException("Row indices out of bounds");
        }
    }

    private File writeExcel(List<List<String>> rows) throws IOException {
        Path outputPath = Files.createTempFile("myapp-", ".xlsx");
        
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(outputPath.toFile())) {

            Sheet sheet = workbook.createSheet("Processed Data");
            int rowNum = 0;

            for (List<String> rowData : rows) {
                Row row = sheet.createRow(rowNum++);
                int cellNum = 0;
                for (String value : rowData) {
                    row.createCell(cellNum++).setCellValue(value);
                }
            }

            workbook.write(fos);
        }
        return outputPath.toFile();
    }
}