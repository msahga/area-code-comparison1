@echo off
echo 区域代码对比工具运行脚本
echo ================================

REM 检查Java是否安装
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到Java环境，请先安装Java 8或更高版本
    pause
    exit /b 1
)

REM 检查是否已编译
if not exist "target\classes\com\example\AreaCodeComparisonApp.class" (
    echo 错误：程序未编译，请先运行 compile.bat
    pause
    exit /b 1
)

REM 检查lib目录
if not exist "lib" (
    echo 错误：lib目录不存在，请先运行 compile.bat
    pause
    exit /b 1
)

echo 正在运行程序...

REM 构建classpath
set CLASSPATH=target\classes
for %%f in (lib\*.jar) do set CLASSPATH=!CLASSPATH!;%%f

REM 运行程序
java -cp "%CLASSPATH%" com.example.AreaCodeComparisonApp

if errorlevel 1 (
    echo 程序运行失败，请检查配置和数据库连接
    pause
    exit /b 1
)

echo 程序执行完成！
pause 