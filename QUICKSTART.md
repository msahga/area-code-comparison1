# 快速开始指南

## 第一步：环境准备

1. **安装Java 8或更高版本**
   ```bash
   java -version
   ```

2. **安装Maven 3.6或更高版本**
   ```bash
   mvn -version
   ```

3. **准备MySQL数据库**
   - 确保数据库服务正在运行
   - 确保`wms_area_code`表存在

## 第二步：配置程序

1. **复制配置文件**
   ```bash
   cp application.properties.example src/main/resources/application.properties
   ```

2. **编辑配置文件**
   打开 `src/main/resources/application.properties` 文件，修改以下配置：
   
   ```properties
   # 数据库配置
   database.url=jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
   database.username=your_username
   database.password=your_password
   
   # Excel文件路径
   excel.input.path=E:\\test1\\行政区域代码.xlsx
   excel.output.path=E:\\test1
   ```

## 第三步：运行程序

### Windows用户
双击运行 `run.bat` 文件

### Linux/Mac用户
```bash
chmod +x run.sh
./run.sh
```

### 手动运行
```bash
# 编译项目
mvn clean compile

# 运行程序
mvn exec:java -Dexec.mainClass="com.example.AreaCodeComparisonApp"
```

## 第四步：查看结果

程序执行完成后，会在配置的输出目录生成对比结果文件：
- 文件名格式：`区域代码对比结果_yyyyMMdd_HHmmss.xlsx`
- 包含详细的对比信息和统计结果

## 常见问题

### 1. 数据库连接失败
- 检查数据库服务是否启动
- 确认数据库连接信息是否正确
- 检查网络连接

### 2. Excel文件读取失败
- 确认文件路径是否正确
- 检查文件是否被其他程序占用
- 确认文件格式为.xlsx

### 3. 中文乱码问题
- 程序已内置编码处理逻辑
- 如果仍有问题，请检查Excel文件的编码格式

## 输出文件说明

生成的Excel文件包含以下列：
- **数据库区域代码**：数据库中的完整区域代码
- **数据库区域名称**：数据库中的完整区域名称
- **Excel区域代码**：Excel中的完整区域代码
- **Excel区域名称**：Excel中的完整区域名称
- **匹配状态**：匹配/不匹配（带颜色标识）
- **备注**：详细说明

## 对比规则

- 取数据库和Excel中区域代码的前6位进行比较
- 取数据库和Excel中区域名称的前6位进行比较
- 区域代码和区域名称的前6位都必须完全一致才算匹配 