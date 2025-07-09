package com.example.entity;

/**
 * 区域代码实体类
 */
public class AreaCode {
    private Long id;
    private Long parentId;
    private String areaCode;
    private String areaName;
    private String areaType;
    private Integer level;
    private Boolean available;
    private Float sortOrder;
    private String remark;
    private String delFlag;
    private Long createDept;
    private Long createBy;
    private String createTime;
    private Long updateBy;
    private String updateTime;
    private String tenantId;
    
    // 构造函数
    public AreaCode() {}
    
    public AreaCode(String areaCode, String areaName) {
        this.areaCode = areaCode;
        this.areaName = areaName;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public String getAreaCode() {
        return areaCode;
    }
    
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    
    public String getAreaName() {
        return areaName;
    }
    
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    
    public String getAreaType() {
        return areaType;
    }
    
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    public Boolean getAvailable() {
        return available;
    }
    
    public void setAvailable(Boolean available) {
        this.available = available;
    }
    
    public Float getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(Float sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getDelFlag() {
        return delFlag;
    }
    
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    
    public Long getCreateDept() {
        return createDept;
    }
    
    public void setCreateDept(Long createDept) {
        this.createDept = createDept;
    }
    
    public Long getCreateBy() {
        return createBy;
    }
    
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    public Long getUpdateBy() {
        return updateBy;
    }
    
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
    
    public String getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String toString() {
        return "AreaCode{" +
                "id=" + id +
                ", areaCode='" + areaCode + '\'' +
                ", areaName='" + areaName + '\'' +
                '}';
    }
} 