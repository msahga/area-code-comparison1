package com.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

/**
 * 实时行政区域代码服务类
 * 通过高德地图API获取最新的行政区域代码
 */
public class RealTimeAreaCodeService {
    
    private static final Logger logger = LoggerFactory.getLogger(RealTimeAreaCodeService.class);
    private static final String AMAP_API_BASE_URL = "https://restapi.amap.com/v3";
    private static final String DISTRICT_API_URL = AMAP_API_BASE_URL + "/config/district";
    
    private String apiKey;
    private ObjectMapper objectMapper;
    
    private static final Map<String, String> AREA_CODE_PREFIX_TO_PROVINCE = new HashMap<>();
    static {
        AREA_CODE_PREFIX_TO_PROVINCE.put("110", "北京市");
        AREA_CODE_PREFIX_TO_PROVINCE.put("120", "天津市");
        AREA_CODE_PREFIX_TO_PROVINCE.put("130", "河北省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("140", "山西省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("150", "内蒙古自治区");
        AREA_CODE_PREFIX_TO_PROVINCE.put("210", "辽宁省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("220", "吉林省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("230", "黑龙江省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("310", "上海市");
        AREA_CODE_PREFIX_TO_PROVINCE.put("320", "江苏省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("330", "浙江省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("340", "安徽省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("350", "福建省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("360", "江西省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("370", "山东省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("410", "河南省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("420", "湖北省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("430", "湖南省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("440", "广东省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("450", "广西壮族自治区");
        AREA_CODE_PREFIX_TO_PROVINCE.put("460", "海南省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("500", "重庆市");
        AREA_CODE_PREFIX_TO_PROVINCE.put("510", "四川省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("520", "贵州省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("530", "云南省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("540", "西藏自治区");
        AREA_CODE_PREFIX_TO_PROVINCE.put("610", "陕西省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("620", "甘肃省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("630", "青海省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("640", "宁夏回族自治区");
        AREA_CODE_PREFIX_TO_PROVINCE.put("650", "新疆维吾尔自治区");
        AREA_CODE_PREFIX_TO_PROVINCE.put("710", "台湾省");
        AREA_CODE_PREFIX_TO_PROVINCE.put("810", "香港特别行政区");
        AREA_CODE_PREFIX_TO_PROVINCE.put("820", "澳门特别行政区");
    }

    public RealTimeAreaCodeService() {
        this.objectMapper = new ObjectMapper();
        loadApiKey();
        // 自动检测key有效性
       
    }
    
    /**
     * 加载高德地图API密钥
     */
    private void loadApiKey() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
                this.apiKey = properties.getProperty("amap.api.key");
                if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
                    logger.warn("未配置高德地图API密钥");
                }
            }
        } catch (IOException e) {
            logger.error("加载配置文件失败", e);
        }
    }
    
    /**
     * 优化后的方法，需传入区域名称和区域代码
     * @param areaName 区域名称
     * @param areaCode 区域代码
     * @return 行政区域代码，如果未找到则返回null
     */
    public String getRealTimeAreaCode(String areaName, String areaCode) {
        if (areaName == null || areaName.trim().isEmpty()) {
            return null;
        }
        if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
            logger.error("高德API密钥未配置，无法查询区域代码");
            return null;
        }
        try {
            // 1. 直接查
            String code = queryAmap(areaName.trim());
            if (code != null) return code;
            // 2. 只在区域代码前3位有对应省/市前缀时才补全
            if (areaCode != null && areaCode.length() >= 3) {
                String prefix = areaCode.substring(0, 3);
                String province = AREA_CODE_PREFIX_TO_PROVINCE.get(prefix);
                if (province != null) {
                    code = queryAmap(province + areaName.trim());
                    if (code != null) {
                        logger.info("高德API智能补全地名查到区域代码: {} + {} -> {}", province, areaName.trim(), code);
                        return code;
                    }
                }
            }
            logger.warn("高德API未查到区域代码，areaName={}, areaCode={}", areaName, areaCode);
            return null;
        } catch (Exception e) {
            logger.error("获取实时行政区域代码失败: areaName={}, areaCode={}", areaName, areaCode, e);
            return null;
        }
    }

    private String queryAmap(String areaName) throws IOException {
        String encodedAreaName = URLEncoder.encode(areaName, StandardCharsets.UTF_8.toString());
        String requestUrl = String.format("%s?key=%s&keywords=%s&subdistrict=0&extensions=base",
                DISTRICT_API_URL, apiKey, encodedAreaName);
        logger.debug("请求高德API: {}", requestUrl);
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);
        try (InputStream inputStream = connection.getInputStream()) {
            JsonNode response = objectMapper.readTree(inputStream);
            logger.info("高德API返回内容: {}", response.toString());
            String status = response.path("status").asText();
            if (!"1".equals(status)) {
                String info = response.path("info").asText();
                logger.warn("高德API请求失败: {}", info);
                return null;
            }
            JsonNode districts = response.path("districts");
            if (districts.isArray() && districts.size() > 0) {
                JsonNode firstDistrict = districts.get(0);
                String adcode = firstDistrict.path("adcode").asText();
                if (adcode != null && !adcode.isEmpty()) {
                    logger.debug("找到区域代码: {} -> {}", areaName, adcode);
                    return adcode;
                }
            }
            logger.debug("未找到区域代码: {}", areaName);
            return null;
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * 根据区域代码获取实时行政区域代码
     * @param areaCode 区域代码
     * @return 实时行政区域代码，如果未找到则返回null
     */
    public String getRealTimeAreaCodeByCode(String areaCode) {
        if (areaCode == null || areaCode.trim().isEmpty()) {
            return null;
        }
        if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
            logger.error("高德API密钥未配置，无法查询区域代码");
            return null;
        }
        try {
            // 构建请求URL
            String requestUrl = String.format("%s?key=%s&keywords=%s&subdistrict=0&extensions=base",
                    DISTRICT_API_URL, apiKey, areaCode.trim());
            
            logger.debug("请求高德API: {}", requestUrl);
            
            // 发送HTTP请求
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            
            // 读取响应
            try (InputStream inputStream = connection.getInputStream()) {
                JsonNode response = objectMapper.readTree(inputStream);
                
                // 检查API响应状态
                String status = response.path("status").asText();
                if (!"1".equals(status)) {
                    String info = response.path("info").asText();
                    logger.warn("高德API请求失败: {}", info);
                    return null;
                }
                
                // 解析区域信息
                JsonNode districts = response.path("districts");
                if (districts.isArray() && districts.size() > 0) {
                    JsonNode firstDistrict = districts.get(0);
                    String adcode = firstDistrict.path("adcode").asText();
                    
                    if (adcode != null && !adcode.isEmpty()) {
                        logger.debug("找到实时区域代码: {} -> {}", areaCode, adcode);
                        return adcode;
                    }
                }
                
                logger.debug("未找到实时区域代码: {}", areaCode);
                return null;
                
            } finally {
                connection.disconnect();
            }
            
        } catch (Exception e) {
            logger.error("获取实时行政区域代码失败: {}", areaCode, e);
            return null;
        }
    }
    
    /**
     * 批量获取实时行政区域代码
     * @param areaNames 区域名称列表
     * @return 区域名称到区域代码的映射
     */
    public java.util.Map<String, String> getRealTimeAreaCodes(java.util.List<String> areaNames) {
        java.util.Map<String, String> result = new java.util.HashMap<>();
        
        for (String areaName : areaNames) {
            String areaCode = getRealTimeAreaCode(areaName, null);
            if (areaCode != null) {
                result.put(areaName, areaCode);
            }
            // 添加延迟避免API限制
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return result;
    }
} 