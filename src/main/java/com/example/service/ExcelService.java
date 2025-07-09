package com.example.service;

import com.example.entity.AreaCode;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel服务类
 */
public class ExcelService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);
    
    /**
     * 从Excel文件读取区域代码数据
     */
    public List<AreaCode> getAreaCodesFromExcel(String filePath) {
        List<AreaCode> areaCodes = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0); // 获取第一个工作表
            
            // 跳过标题行，从第二行开始读取
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                
                // 读取A列（区域代码）和B列（区域名称）
                String areaCode = getCellValueAsString(row.getCell(0));
                String areaName = getCellValueAsString(row.getCell(1));
                

                
                if (areaCode != null && !areaCode.trim().isEmpty()) {
                    AreaCode areaCodeEntity = new AreaCode(areaCode.trim(), areaName);
                    areaCodes.add(areaCodeEntity);
                }
            }
            
            logger.info("从Excel文件读取到 {} 条区域代码数据", areaCodes.size());
            
        } catch (IOException e) {
            logger.error("读取Excel文件失败: {}", filePath, e);
            throw new RuntimeException("Excel文件读取失败", e);
        }
        
        return areaCodes;
    }
    
    /**
     * 获取单元格值作为字符串
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // 如果是数字，转换为字符串（避免科学计数法）
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 处理数字格式的区域代码
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    /**
     * 处理编码问题
     */
    private String handleEncoding(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }
        
        try {
            // 尝试处理可能的编码问题
            byte[] bytes = text.getBytes(StandardCharsets.ISO_8859_1);
            String decoded = new String(bytes, StandardCharsets.UTF_8);
            
            // 如果解码后的文本包含乱码字符，尝试其他编码
            if (containsGarbledText(decoded)) {
                // 尝试GBK编码
                bytes = text.getBytes("GBK");
                decoded = new String(bytes, StandardCharsets.UTF_8);
                
                if (containsGarbledText(decoded)) {
                    // 如果还是乱码，返回原文本
                    logger.warn("无法正确解码文本，使用原文本: {}", text);
                    return text;
                }
            }
            
            return decoded;
            
        } catch (Exception e) {
            logger.warn("编码转换失败，使用原文本: {}", text, e);
            return text;
        }
    }
    
    /**
     * 检查是否包含乱码字符
     */
    private boolean containsGarbledText(String text) {
        if (text == null) {
            return false;
        }
        
        // 检查是否包含常见的乱码字符
        return text.contains("") || text.contains("?") || text.contains("");
    }
} 