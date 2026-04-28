#!/bin/bash

# Week 02 测试运行脚本

echo "================================"
echo "Week 02: 类设计与领域建模"
echo "测试运行脚本"
echo "================================"
echo ""

# 进入脚本所在目录
cd "$(dirname "$0")"

echo "运行所有测试..."
echo ""

mvn clean test

echo ""
echo "================================"
echo "测试完成！"
echo "================================"
echo ""
echo "当前 starter 只包含烟雾测试："
echo "  mvn test -Dtest=StarterSmokeTest"
echo ""
echo "完成作业后，请添加自己的 Book/BookCollection 测试。"
echo ""
