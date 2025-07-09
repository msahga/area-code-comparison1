package com.example.entity;

/**
 * 对比结果实体类
 */
public class ComparisonResult {
    private String dbAreaCode;
    private String dbAreaName;
    private String excelAreaCode;
    private String excelAreaName;
    private boolean isMatch;
    private String matchStatus;
    private String remark;
    private String realTimeAreaCode; // 新增：实时行政区域代码
    
    // 构造函数
    public ComparisonResult() {}
    
    public ComparisonResult(String dbAreaCode, String dbAreaName, String excelAreaCode, String excelAreaName) {
        this.dbAreaCode = dbAreaCode;
        this.dbAreaName = dbAreaName;
        this.excelAreaCode = excelAreaCode;
        this.excelAreaName = excelAreaName;
    }
    
    // Getter和Setter方法
    public String getDbAreaCode() {
        return dbAreaCode;
    }
    
    public void setDbAreaCode(String dbAreaCode) {
        this.dbAreaCode = dbAreaCode;
    }
    
    public String getDbAreaName() {
        return dbAreaName;
    }
    
    public void setDbAreaName(String dbAreaName) {
        this.dbAreaName = dbAreaName;
    }
    
    public String getExcelAreaCode() {
        return excelAreaCode;
    }
    
    public void setExcelAreaCode(String excelAreaCode) {
        this.excelAreaCode = excelAreaCode;
    }
    
    public String getExcelAreaName() {
        return excelAreaName;
    }
    
    public void setExcelAreaName(String excelAreaName) {
        this.excelAreaName = excelAreaName;
    }
    
    public boolean isMatch() {
        return isMatch;
    }
    
    public void setMatch(boolean match) {
        isMatch = match;
    }
    
    public String getMatchStatus() {
        return matchStatus;
    }
    
    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    // 新增：实时行政区域代码的getter和setter
    public String getRealTimeAreaCode() {
        return realTimeAreaCode;
    }
    
    public void setRealTimeAreaCode(String realTimeAreaCode) {
        this.realTimeAreaCode = realTimeAreaCode;
    }
    
    @Override
    public String toString() {
        return "ComparisonResult{" +
                "dbAreaCode='" + dbAreaCode + '\'' +
                ", dbAreaName='" + dbAreaName + '\'' +
                ", excelAreaCode='" + excelAreaCode + '\'' +
                ", excelAreaName='" + excelAreaName + '\'' +
                ", isMatch=" + isMatch +
                ", matchStatus='" + matchStatus + '\'' +
                ", remark='" + remark + '\'' +
                ", realTimeAreaCode='" + realTimeAreaCode + '\'' +
                '}';
    }
} 