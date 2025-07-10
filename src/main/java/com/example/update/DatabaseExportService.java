package com.example.update;

import com.example.config.DatabaseConfig;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseExportService {
    /**
     * 导出数据库wms_area_code表的parent_id、area_code、area_name、area_type、level到Excel
     */
    public void exportAreaCodesToExcel(String outputPath) {
        String[] headers = {"parent_id", "area_code", "area_name", "area_type", "level"};
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT parent_id, area_code, area_name, area_type, level FROM wms_area_code");
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("导出数据");
            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            int rowIdx = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowIdx++);
                for (int i = 0; i < headers.length; i++) {
                    row.createCell(i).setCellValue(rs.getString(headers[i]));
                }
            }
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 