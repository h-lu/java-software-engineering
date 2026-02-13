#!/bin/bash

# CampusFlow 演示数据准备脚本
# Week 15: 项目集市与展示准备

echo "========================================="
echo "CampusFlow - 演示数据准备脚本"
echo "========================================="
echo ""
echo "此脚本将创建 5-10 个演示任务，用于展示。"
echo ""

# 检查服务器是否运行
echo "检查 CampusFlow 服务器..."
if ! curl -s http://localhost:8080/api/tasks > /dev/null 2>&1; then
    echo "错误：CampusFlow 服务器未运行"
    echo "请先运行 ./start.sh 启动服务器"
    exit 1
fi

echo "服务器运行正常"
echo ""

# 创建演示任务
echo "创建演示任务..."
echo ""

# 任务 1：展示脚本设计
echo "创建任务 1: 完成展示脚本设计"
curl -s -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "完成展示脚本设计",
    "description": "设计 10 分钟的 CampusFlow 演示脚本，包括开场、主体、结尾",
    "dueDate": "2026-02-20",
    "completed": false
  }' | jq '.'

# 任务 2：PPT 制作
echo ""
echo "创建任务 2: 制作展示 PPT"
curl -s -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "制作展示 PPT",
    "description": "设计 12-15 页 PPT，包括问题背景、技术选型、功能演示、工程实践",
    "dueDate": "2026-02-20",
    "completed": false
  }' | jq '.'

# 任务 3：海报设计
echo ""
echo "创建任务 3: 设计项目集市海报"
curl -s -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "设计项目集市海报",
    "description": "使用 HTML/CSS 设计 A1 尺寸海报，包含项目介绍、技术栈、二维码",
    "dueDate": "2026-02-20",
    "completed": true
  }' | jq '.'

# 任务 4：二维码生成
echo ""
echo "创建任务 4: 生成项目二维码"
curl -s -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "生成项目二维码",
    "description": "为 GitHub 仓库、在线演示、API 文档生成二维码",
    "dueDate": "2026-02-20",
    "completed": true
  }' | jq '.'

# 任务 5：演练问答
echo ""
echo "创建任务 5: 准备问答环节"
curl -s -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "准备问答环节",
    "description": "准备 10-15 个常见问题的答案，包括技术选型、架构设计、未来规划",
    "dueDate": "2026-02-20",
    "completed": false
  }' | jq '.'

# 任务 6：跨组演练
echo ""
echo "创建任务 6: 跨组展示演练"
curl -s -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "跨组展示演练",
    "description": "邀请 2-3 个团队观看演练，收集反馈并改进",
    "dueDate": "2026-02-20",
    "completed": false
  }' | jq '.'

# 任务 7：Bug Bash 修复
echo ""
echo "创建任务 7: 修复 Bug Bash 发现的问题"
curl -s -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "修复 Bug Bash 发现的问题",
    "description": "修复其他组发现的 3 个 bug：标题截断、删除确认、移动端适配",
    "dueDate": "2026-02-20",
    "completed": true
  }' | jq '.'

# 任务 8：AI 前端审查
echo ""
echo "创建任务 8: 完成 AI 生成前端的代码审查"
curl -s -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "完成 AI 生成前端的代码审查",
    "description": "使用审查检查清单评估 AI 生成的前端代码，修复空输入、CSS 美化等问题",
    "dueDate": "2026-02-20",
    "completed": true
  }' | jq '.'

echo ""
echo "========================================="
echo "演示数据创建完成！"
echo "========================================="
echo ""
echo "已创建 8 个演示任务："
echo "- 5 个进行中的任务（用于演示标记完成）"
echo "- 3 个已完成的任务（用于演示过滤功能）"
echo ""
echo "演示流程建议："
echo "1. 展示所有任务（GET /api/tasks）"
echo "2. 创建新任务（POST /api/tasks）"
echo "3. 标记任务完成（PUT /api/tasks/{id}/complete）"
echo "4. 过滤已完成的任务（GET /api/tasks?status=completed）"
echo "5. 删除任务（DELETE /api/tasks/{id}）"
echo ""
echo "访问 http://localhost:8080 查看演示界面"
