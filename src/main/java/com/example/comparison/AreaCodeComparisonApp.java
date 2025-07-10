package com.example.comparison;

import com.example.entity.AreaCode;
import com.example.entity.ComparisonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

/**
 * 区域代码对比主程序
 */
public class AreaCodeComparisonApp {

    private static final Logger logger = LoggerFactory.getLogger(AreaCodeComparisonApp.class);

    public static void main(String[] args) {
        try {
            logger.info("开始执行区域代码对比程序...");

            // 加载配置
            Properties properties = loadProperties();

            // 初始化服务
            DatabaseService databaseService = new DatabaseService();
            ExcelService excelService = new ExcelService();
            ComparisonService comparisonService = new ComparisonService();
            ExcelOutputService excelOutputService = new ExcelOutputService();

            // 1. 从数据库读取数据
            logger.info("正在从数据库读取区域代码数据...");
            List<AreaCode> dbAreaCodes = databaseService.getAreaCodesFromDatabase();

            // 2. 从Excel文件读取数据
            logger.info("正在从Excel文件读取区域代码数据...");
            String excelFilePath = properties.getProperty("excel.input.path", "E:\\test1\\行政区域代码.xlsx");
            List<AreaCode> excelAreaCodes = excelService.getAreaCodesFromExcel(excelFilePath);

            // 3. 执行对比
            logger.info("正在执行数据对比...");
            List<ComparisonResult> comparisonResults = comparisonService.compareAreaCodes(dbAreaCodes, excelAreaCodes);

            // 4. 生成统计信息
            comparisonService.generateStatistics(comparisonResults);

            // 5. 保存结果到Excel
            logger.info("正在保存对比结果到Excel文件...");
            String outputFileName = excelOutputService.generateOutputFileName();
            String outputPath = properties.getProperty("excel.output.path", "E:\\test1") + "\\" + outputFileName;
            excelOutputService.saveComparisonResultsToExcel(comparisonResults, outputPath);

            logger.info("区域代码对比程序执行完成！");
            logger.info("输出文件路径: {}", outputPath);

        } catch (Exception e) {
            logger.error("程序执行失败", e);
            System.exit(1);
        }
    }

    /**
     * 加载配置文件
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = AreaCodeComparisonApp.class.getClassLoader().getResourceAsStream("application.properties");
             Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            if (input != null) {
                properties.load(reader);
                logger.info("成功加载配置文件");
            } else {
                logger.warn("未找到配置文件，使用默认配置");
            }
        } catch (IOException e) {
            logger.warn("加载配置文件失败，使用默认配置", e);
        }
        return properties;
    }
} 