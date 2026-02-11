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
echo "查看详细测试报告："
echo "  mvn surefire-report:report"
echo ""
echo "运行特定测试类："
echo "  mvn test -Dtest=TaskTest"
echo "  mvn test -Dtest=TaskManagerTest"
echo "  mvn test -Dtest=EncapsulationTest"
echo "  mvn test -Dtest=SolidPrinciplesTest"
echo ""
