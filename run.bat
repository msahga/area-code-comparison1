@echo off
echo 区域代码对比工具启动脚本
echo ================================

REM 检查Java是否安装
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到Java环境，请先安装Java 8或更高版本
    pause
    exit /b 1
)

REM 检查Maven是否安装
mvn -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到Maven环境，请先安装Maven 3.6或更高版本
    pause
    exit /b 1
)

echo 正在编译项目...
call mvn clean compile

if errorlevel 1 (
    echo 编译失败，请检查项目配置
    pause
    exit /b 1
)

echo 正在运行程序...
call mvn exec:java -Dexec.mainClass="com.example.AreaCodeComparisonApp"

if errorlevel 1 (
    echo 程序运行失败，请检查配置和数据库连接
    pause
    exit /b 1
)

echo 程序执行完成！
pause 