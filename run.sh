#!/bin/bash

echo "区域代码对比工具启动脚本"
echo "================================"

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo "错误：未找到Java环境，请先安装Java 8或更高版本"
    exit 1
fi

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误：未找到Maven环境，请先安装Maven 3.6或更高版本"
    exit 1
fi

echo "正在编译项目..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "编译失败，请检查项目配置"
    exit 1
fi

echo "正在运行程序..."
mvn exec:java -Dexec.mainClass="com.example.AreaCodeComparisonApp"

if [ $? -ne 0 ]; then
    echo "程序运行失败，请检查配置和数据库连接"
    exit 1
fi

echo "程序执行完成！" 