package com.example.service;

import com.example.entity.ComparisonResult;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel输出服务类
 */
public class ExcelOutputService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelOutputService.class);
    
    /**
     * 将对比结果保存到Excel文件
     */
    public void saveComparisonResultsToExcel(List<ComparisonResult> results, String outputPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("对比结果");
            
            // 创建标题行样式
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "数据库区域代码", "数据库区域名称", 
                "Excel区域代码", "Excel区域名称", 
                "匹配状态", "备注", "实时行政区域代码"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 创建数据行样式
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle matchStyle = createMatchStyle(workbook);
            CellStyle unmatchStyle = createUnmatchStyle(workbook);
            CellStyle realTimeStyle = createRealTimeStyle(workbook);
            
            // 填充数据
            for (int i = 0; i < results.size(); i++) {
                ComparisonResult result = results.get(i);
                Row row = sheet.createRow(i + 1);
                
                // 数据库区域代码
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(result.getDbAreaCode());
                cell0.setCellStyle(dataStyle);
                
                // 数据库区域名称
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(result.getDbAreaName());
                cell1.setCellStyle(dataStyle);
                
                // Excel区域代码
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(result.getExcelAreaCode());
                cell2.setCellStyle(dataStyle);
                
                // Excel区域名称
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(result.getExcelAreaName());
                cell3.setCellStyle(dataStyle);
                
                // 匹配状态
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(result.getMatchStatus());
                cell4.setCellStyle(result.isMatch() ? matchStyle : unmatchStyle);
                
                // 备注
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(result.getRemark());
                cell5.setCellStyle(dataStyle);
                
                // 实时行政区域代码 (G列)
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(result.getRealTimeAreaCode());
                cell6.setCellStyle(realTimeStyle);
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 保存文件
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
            }
            
            logger.info("对比结果已保存到: {}", outputPath);
            
        } catch (IOException e) {
            logger.error("保存Excel文件失败: {}", outputPath, e);
            throw new RuntimeException("Excel文件保存失败", e);
        }
    }
    
    /**
     * 创建标题行样式
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    /**
     * 创建数据行样式
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    /**
     * 创建匹配状态样式（绿色背景）
     */
    private CellStyle createMatchStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    
    /**
     * 创建不匹配状态样式（红色背景）
     */
    private CellStyle createUnmatchStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    
    /**
     * 创建实时行政区域代码样式（蓝色背景）
     */
    private CellStyle createRealTimeStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
    
    /**
     * 生成输出文件名
     */
    public String generateOutputFileName() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "区域代码对比结果_" + timestamp + ".xlsx";
    }
} 