#!/usr/bin/env bash
set -euo pipefail

API_BASE_URL="${API_BASE_URL:-http://localhost:8080}"

echo "待办： Review this script before running it."
echo "It assumes your CampusFlow API accepts POST ${API_BASE_URL}/api/tasks."
echo "Update endpoint paths and JSON fields to match your real API."

curl -X POST "${API_BASE_URL}/api/tasks" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "待办： 演示任务标题",
    "description": "待办： 演示任务描述",
    "completed": false
  }'
