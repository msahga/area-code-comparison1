package com.example.update;

import com.example.config.DatabaseConfig;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AreaCodeUpdateApp {
    public static void main(String[] args) {
        String excelPath = "E:\\test1\\区域代码对比结果.xlsx";
        String outputPath = "E:\\test1\\数据库导出结果.xlsx";
        ExcelUpdateService updateService = new ExcelUpdateService();
        updateService.updateAreaCodesFromExcel(excelPath);

        DatabaseExportService exportService = new DatabaseExportService();
        exportService.exportAreaCodesToExcel(outputPath);
        System.out.println("全部完成！");
    }

    public static class ExcelUpdateService {
        /**
         * 读取Excel并批量更新数据库中区域代码（只更新匹配状态为区域代码错误的行，且只替换前6位）
         */
        public void updateAreaCodesFromExcel(String excelPath) {
            try (FileInputStream fis = new FileInputStream(excelPath);
                 Workbook workbook = new XSSFWorkbook(fis);
                 Connection conn = DatabaseConfig.getConnection()) {

                Sheet sheet = workbook.getSheetAt(0);
                String updateSql = "UPDATE wms_area_code SET area_code = CONCAT(?, SUBSTRING(area_code, 7)) WHERE area_code LIKE ?";
                PreparedStatement ps = conn.prepareStatement(updateSql);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    String dbAreaCode = getCellValueAsString(row.getCell(0)); // 数据库区域代码
                    String excelAreaCode = getCellValueAsString(row.getCell(2)); // Excel区域代码
                    String matchStatus = getCellValueAsString(row.getCell(4)); // 匹配状态
                    if ("区域代码错误".equals(matchStatus) && dbAreaCode.length() >= 6 && excelAreaCode.length() >= 6) {
                        String newPrefix = excelAreaCode.substring(0, 6);
                        String oldPrefix = dbAreaCode.substring(0, 6);
                        ps.setString(1, newPrefix);
                        ps.setString(2, oldPrefix + "%");
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getCellValueAsString(Cell cell) {
            if (cell == null) return "";
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case FORMULA:
                    return cell.getCellFormula();
                default:
                    return "";
            }
        }
    }
}