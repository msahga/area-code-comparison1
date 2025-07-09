@echo off
echo 区域代码对比工具编译脚本
echo ================================

REM 检查Java是否安装
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到Java环境，请先安装Java 8或更高版本
    pause
    exit /b 1
)

REM 创建输出目录
if not exist "target\classes" mkdir "target\classes"

REM 创建lib目录
if not exist "lib" mkdir "lib"

echo 正在下载依赖库...
echo 请手动下载以下JAR文件到lib目录：
echo 1. poi-5.2.3.jar
echo 2. poi-ooxml-5.2.3.jar
echo 3. mysql-connector-java-8.0.33.jar
echo 4. slf4j-api-1.7.36.jar
echo 5. logback-classic-1.2.12.jar
echo 6. logback-core-1.2.12.jar
echo.
echo 下载地址：
echo https://mvnrepository.com/artifact/org.apache.poi/poi/5.2.3
echo https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml/5.2.3
echo https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.33
echo https://mvnrepository.com/artifact/org.slf4j/slf4j-api/1.7.36
echo https://mvnrepository.com/artifact/ch.qos.logback/logback-classic/1.2.12
echo https://mvnrepository.com/artifact/ch.qos.logback/logback-core/1.2.12
echo.

REM 检查lib目录中是否有JAR文件
set JAR_COUNT=0
for %%f in (lib\*.jar) do set /a JAR_COUNT+=1

if %JAR_COUNT% LSS 6 (
    echo 错误：lib目录中缺少必要的JAR文件
    echo 请先下载上述JAR文件到lib目录
    pause
    exit /b 1
)

echo 正在编译Java源文件...

REM 构建classpath
set CLASSPATH=target\classes
for %%f in (lib\*.jar) do set CLASSPATH=!CLASSPATH!;%%f

REM 编译Java文件
javac -cp "%CLASSPATH%" -d target\classes -encoding UTF-8 src\main\java\com\example\*.java src\main\java\com\example\config\*.java src\main\java\com\example\entity\*.java src\main\java\com\example\service\*.java

if errorlevel 1 (
    echo 编译失败，请检查源代码
    pause
    exit /b 1
)

REM 复制资源文件
if exist "src\main\resources" (
    xcopy "src\main\resources\*" "target\classes\" /E /Y >nul
)

echo 编译完成！
echo 现在可以运行 run-simple.bat 来执行程序
pause 