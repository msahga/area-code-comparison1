package com.example.service;

import com.example.config.DatabaseConfig;
import com.example.entity.AreaCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库服务类
 */
public class DatabaseService {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    
    /**
     * 从数据库读取区域代码数据
     */
    public List<AreaCode> getAreaCodesFromDatabase() {
        List<AreaCode> areaCodes = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = DatabaseConfig.getConnection();
            String sql = "SELECT area_code, area_name FROM wms_area_code WHERE del_flag = '0' AND available = 1";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                String areaCode = resultSet.getString("area_code");
                String areaName = resultSet.getString("area_name");
                

                
                AreaCode areaCodeEntity = new AreaCode(areaCode, areaName);
                areaCodes.add(areaCodeEntity);
            }
            
            logger.info("从数据库读取到 {} 条区域代码数据", areaCodes.size());
            
        } catch (SQLException e) {
            logger.error("从数据库读取区域代码数据失败", e);
            throw new RuntimeException("数据库查询失败", e);
        } finally {
            closeResources(connection, statement, resultSet);
        }
        
        return areaCodes;
    }
    
    /**
     * 处理编码问题
     */
    private String handleEncoding(String text) {
        if (text == null) {
            return "";
        }
        
        try {
            // 尝试处理可能的编码问题
            byte[] bytes = text.getBytes("ISO-8859-1");
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            // 如果转换失败，返回原文本
            logger.warn("编码转换失败，使用原文本: {}", text);
            return text;
        }
    }
    
    /**
     * 关闭数据库资源
     */
    private void closeResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error("关闭ResultSet失败", e);
            }
        }
        
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("关闭PreparedStatement失败", e);
            }
        }
        
        if (connection != null) {
            DatabaseConfig.closeConnection(connection);
        }
    }
} 