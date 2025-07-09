@echo off
echo 依赖库下载脚本
echo ================================

REM 创建lib目录
if not exist "lib" mkdir "lib"

echo 正在下载依赖库...

REM 下载Apache POI
echo 下载 poi-5.2.3.jar...
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/apache/poi/poi/5.2.3/poi-5.2.3.jar' -OutFile 'lib\poi-5.2.3.jar'}"

echo 下载 poi-ooxml-5.2.3.jar...
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml/5.2.3/poi-ooxml-5.2.3.jar' -OutFile 'lib\poi-ooxml-5.2.3.jar'}"

REM 下载MySQL连接器
echo 下载 mysql-connector-java-8.0.33.jar...
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar' -OutFile 'lib\mysql-connector-java-8.0.33.jar'}"

REM 下载SLF4J
echo 下载 slf4j-api-1.7.36.jar...
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar' -OutFile 'lib\slf4j-api-1.7.36.jar'}"

REM 下载Logback
echo 下载 logback-classic-1.2.12.jar...
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/ch/qos/logback/logback-classic/1.2.12/logback-classic-1.2.12.jar' -OutFile 'lib\logback-classic-1.2.12.jar'}"

echo 下载 logback-core-1.2.12.jar...
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.2.12/logback-core-1.2.12.jar' -OutFile 'lib\logback-core-1.2.12.jar'}"

echo 下载完成！
echo 现在可以运行 compile.bat 来编译项目
pause 