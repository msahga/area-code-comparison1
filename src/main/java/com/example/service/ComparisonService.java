package com.example.service;

import com.example.entity.AreaCode;
import com.example.entity.ComparisonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 对比服务类
 */
public class ComparisonService {

    private static final Logger logger = LoggerFactory.getLogger(ComparisonService.class);
    private RealTimeAreaCodeService realTimeAreaCodeService;

    public ComparisonService() {
        this.realTimeAreaCodeService = new RealTimeAreaCodeService();
    }

    public List<ComparisonResult> compareAreaCodes(List<AreaCode> dbAreaCodes, List<AreaCode> excelAreaCodes) {
        List<ComparisonResult> results = new ArrayList<>();

        // 1. 构建数据库数据映射，便于查找
        Map<String, AreaCode> dbMap = new HashMap<>();
        Map<String, List<AreaCode>> dbNameMap = new HashMap<>();
        for (AreaCode dbAreaCode : dbAreaCodes) {
            String key = getComparisonKey(dbAreaCode);
            dbMap.put(key, dbAreaCode);

            // 区域名称前6位映射
            String namePrefix = getPrefix(dbAreaCode.getAreaName(), 6);
            dbNameMap.computeIfAbsent(namePrefix, k -> new ArrayList<>()).add(dbAreaCode);
        }

        Set<String> excelKeySet = new HashSet<>();
        Set<String> matchedDbKeys = new HashSet<>(); // 记录已匹配或区域代码错误的dbKey
        for (AreaCode excelAreaCode : excelAreaCodes) {
            String excelKey = getComparisonKey(excelAreaCode);
            excelKeySet.add(excelKey);
            AreaCode matchedDbAreaCode = dbMap.get(excelKey);

            // 调用新版方法，传入D列名称和区域代码
            String realTimeCode = getRealTimeAreaCode(excelAreaCode.getAreaName(), excelAreaCode.getAreaCode());

            if (matchedDbAreaCode != null) {
                // 完全匹配
                ComparisonResult result = new ComparisonResult(
                        matchedDbAreaCode.getAreaCode(),
                        matchedDbAreaCode.getAreaName(),
                        excelAreaCode.getAreaCode(),
                        excelAreaCode.getAreaName()
                );
                result.setMatch(true);
                result.setMatchStatus("匹配");
                result.setRemark("数据库和Excel数据完全一致");
                result.setRealTimeAreaCode(realTimeCode); // 放入G列
                results.add(result);
                matchedDbKeys.add(excelKey);
            } else {
                // 匹配区域名称前6位
                String namePrefix = getPrefix(excelAreaCode.getAreaName(), 6);
                List<AreaCode> dbNameList = dbNameMap.get(namePrefix);
                boolean codeError = false;
                if (dbNameList != null) {
                    for (AreaCode dbArea : dbNameList) {
                        // 区域名称前6位相同，但区域代码不同
                        if (!getPrefix(dbArea.getAreaCode(), 6).equals(getPrefix(excelAreaCode.getAreaCode(), 6))) {
                            ComparisonResult result = new ComparisonResult(
                                    dbArea.getAreaCode(),
                                    dbArea.getAreaName(),
                                    excelAreaCode.getAreaCode(),
                                    excelAreaCode.getAreaName()
                            );
                            result.setMatch(false);
                            result.setMatchStatus("区域代码错误");
                            result.setRemark("区域名称相同但区域代码不一致，请人工核查");
                            result.setRealTimeAreaCode(realTimeCode); // 放入G列
                            results.add(result);
                            matchedDbKeys.add(getComparisonKey(dbArea));
                            codeError = true;
                        }
                    }
                }
                if (!codeError) {
                    // 数据库中未找到对应的区域代码
                    ComparisonResult result = new ComparisonResult(
                            "",
                            "",
                            excelAreaCode.getAreaCode(),
                            excelAreaCode.getAreaName()
                    );
                    result.setMatch(false);
                    result.setMatchStatus("不匹配");
                    result.setRemark("在数据库中未找到对应的区域代码");
                    result.setRealTimeAreaCode(realTimeCode); // 放入G列
                    results.add(result);
                }
            }
        }

        // 补充数据库有但Excel没有的（只生成Excel中完全没有的，排除已匹配和区域代码错误的）
        for (AreaCode dbAreaCode : dbAreaCodes) {
            String dbKey = getComparisonKey(dbAreaCode);
            if (!excelKeySet.contains(dbKey) && !matchedDbKeys.contains(dbKey)) {
                ComparisonResult result = new ComparisonResult(
                        dbAreaCode.getAreaCode(),
                        dbAreaCode.getAreaName(),
                        "",
                        ""
                );
                result.setMatch(false);
                result.setMatchStatus("不匹配");
                result.setRemark("在Excel中未找到对应的区域代码");
                result.setRealTimeAreaCode(""); // G列为空
                results.add(result);
            }
        }

        logger.info("对比完成，共生成 {} 条对比结果", results.size());
        return results;
    }

    /**
     * 获取实时行政区域代码
     * @param areaName 区域名称
     * @param areaCode 区域代码
     * @return 实时行政区域代码
     */
    private String getRealTimeAreaCode(String areaName, String areaCode) {
        try {
            return realTimeAreaCodeService.getRealTimeAreaCode(areaName, areaCode);
        } catch (Exception e) {
            logger.error("获取实时行政区域代码失败: areaName={}, areaCode={}", areaName, areaCode, e);
            return "获取失败";
        }
    }

    /**
     * 生成对比用的键值
     * 取area_code的前6位和area_name的前6位
     */
    private String getComparisonKey(AreaCode areaCode) {
        String areaCodePrefix = getPrefix(areaCode.getAreaCode(), 6);
        String areaNamePrefix = getPrefix(areaCode.getAreaName(), 6);
        return areaCodePrefix + "|" + areaNamePrefix;
    }

    /**
     * 获取字符串的前N位
     */
    private String getPrefix(String text, int length) {
        if (text == null) {
            return "";
        }
        if (text.length() <= length) {
            return text;
        }
        return text.substring(0, length);
    }

    /**
     * 生成对比统计信息
     */
    public void generateStatistics(List<ComparisonResult> results) {
        int totalCount = results.size();
        int matchCount = 0;
        int unmatchCount = 0;
        int codeErrorCount = 0;

        for (ComparisonResult result : results) {
            if ("匹配".equals(result.getMatchStatus())) {
                matchCount++;
            } else if ("区域代码错误".equals(result.getMatchStatus())) {
                codeErrorCount++;
            } else {
                unmatchCount++;
            }
        }

        logger.info("=== 对比统计信息 ===");
        logger.info("总记录数: {}", totalCount);
        logger.info("匹配记录数: {}", matchCount);
        logger.info("区域代码错误数: {}", codeErrorCount);
        logger.info("不匹配记录数: {}", unmatchCount);
        logger.info("匹配率: {:.2f}%", totalCount == 0 ? 0 : (double) matchCount / totalCount * 100);
        logger.info("==================");
    }
}