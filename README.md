# 区域代码对比工具

这是一个Java程序，用于对比数据库中的区域代码数据与Excel文件中的数据，并将对比结果保存到新的Excel文件中。

## 功能特性

1. **数据库数据读取**: 从MySQL数据库的`wms_area_code`表读取区域代码和区域名称
2. **Excel数据读取**: 从Excel文件的A列和B列读取区域代码和区域名称
3. **智能对比**: 取数据库和Excel中区域代码和区域名称的前6位进行对比
4. **编码处理**: 自动处理中文乱码问题
5. **结果输出**: 将对比结果保存到格式化的Excel文件中
6. **统计信息**: 生成详细的对比统计信息

## 项目结构

```
src/main/java/com/example/
├── AreaCodeComparisonApp.java    # 主程序入口
├── config/
│   └── DatabaseConfig.java       # 数据库配置
├── entity/
│   ├── AreaCode.java            # 区域代码实体类
│   └── ComparisonResult.java    # 对比结果实体类
└── service/
    ├── DatabaseService.java     # 数据库服务
    ├── ExcelService.java        # Excel读取服务
    ├── ComparisonService.java   # 对比服务
    └── ExcelOutputService.java  # Excel输出服务
```

## 环境要求

- Java 8 或更高版本
- MySQL 数据库
- Excel文件（.xlsx格式）

**可选：**
- Maven 3.6 或更高版本（用于依赖管理）

## 配置说明

### 1. 数据库配置

编辑 `src/main/java/com/example/config/DatabaseConfig.java` 文件，修改以下配置：

```java
private static final String URL = "jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

### 2. Excel文件路径

在 `AreaCodeComparisonApp.java` 中修改Excel文件路径：

```java
String excelFilePath = "E:\\test1\\行政区域代码.xlsx";
```

## 使用方法

### 方法一：使用Maven（推荐）

#### 1. 编译项目

```bash
mvn clean compile
```

#### 2. 运行程序

```bash
mvn exec:java -Dexec.mainClass="com.example.AreaCodeComparisonApp"
```

### 方法二：不使用Maven

#### 1. 下载依赖库

创建 `lib` 目录，并下载以下JAR文件：
- poi-5.2.3.jar
- poi-ooxml-5.2.3.jar
- mysql-connector-java-8.0.33.jar
- slf4j-api-1.7.36.jar
- logback-classic-1.2.12.jar
- logback-core-1.2.12.jar

#### 2. 编译项目

```bash
# Windows
compile.bat

# Linux/Mac
chmod +x compile.sh
./compile.sh
```

#### 3. 运行程序

```bash
# Windows
run-simple.bat

# Linux/Mac
chmod +x run-simple.sh
./run-simple.sh
```

### 3. 查看结果

程序执行完成后，会在指定目录生成对比结果Excel文件，文件名格式为：
`区域代码对比结果_yyyyMMdd_HHmmss.xlsx`

## 对比规则

1. **区域代码对比**: 取数据库和Excel中区域代码的前6位进行比较
2. **区域名称对比**: 取数据库和Excel中区域名称的前6位进行比较
3. **匹配条件**: 区域代码和区域名称的前6位都必须完全一致才算匹配
4. **编码处理**: 自动处理中文编码问题，支持UTF-8和GBK编码

## 输出格式

生成的Excel文件包含以下列：

| 列名 | 说明 |
|------|------|
| 数据库区域代码 | 数据库中的完整区域代码 |
| 数据库区域名称 | 数据库中的完整区域名称 |
| Excel区域代码 | Excel中的完整区域代码 |
| Excel区域名称 | Excel中的完整区域名称 |
| 匹配状态 | 匹配/不匹配 |
| 备注 | 详细说明 |

## 日志文件

程序运行时会生成日志文件：
- 控制台输出：实时显示程序执行状态
- 文件日志：`logs/area-code-comparison.log`

## 注意事项

1. 确保数据库连接正常，且`wms_area_code`表存在
2. 确保Excel文件路径正确，且文件格式为.xlsx
3. 程序会自动处理中文编码问题，但建议Excel文件使用UTF-8编码
4. 对比结果文件会覆盖同名文件，请注意备份

## 故障排除

### 常见问题

1. **数据库连接失败**
   - 检查数据库配置是否正确
   - 确认数据库服务是否启动
   - 检查网络连接

2. **Excel文件读取失败**
   - 确认文件路径是否正确
   - 检查文件是否被其他程序占用
   - 确认文件格式为.xlsx

3. **中文乱码问题**
   - 程序已内置编码处理逻辑
   - 如果仍有问题，请检查Excel文件的编码格式

## 版本历史

- v1.0.0: 初始版本，支持基本的对比功能 