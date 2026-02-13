# CampusFlow

> 一个简洁的校园任务管理系统，支持创建任务、设置截止日期、标记完成。

## 快速开始（5 分钟）

### 前置要求
- Java 17+
- Maven 3.6+

### 1. 克隆仓库
```bash
git clone https://github.com/your-org/campusflow.git
cd campusflow
```

### 2. 编译并运行
```bash
mvn compile exec:java
```

### 3. 访问应用
- 打开浏览器访问 http://localhost:7070
- API 文档：http://localhost:7070/api-docs.yaml

### 验证安装
发送测试请求：
```bash
curl http://localhost:7070/health
# 预期输出：{"service":"CampusFlow","version":"2.4.0","status":"UP",...}
```

## 功能特性

- ✅ 创建任务、编辑任务、删除任务
- ✅ 设置截止日期、标记完成
- ✅ 任务搜索和过滤
- ✅ RESTful API + OpenAPI 文档
- ✅ 逾期费用计算

## 使用指南

### 创建任务
```bash
curl -X POST http://localhost:7070/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"学习集成测试","completed":false,"dueDate":"2026-12-31"}'
```

### 获取所有任务
```bash
curl http://localhost:7070/tasks
```

## 常见问题

**Q: 数据库在哪里？**
A: 本版本使用内存数据库，数据在应用重启后会丢失。

**Q: 如何修改端口号？**
A: 编辑 `src/main/java/com/campusflow/App.java`，修改 `PORT` 常量。

## 架构设计

本项目采用三层架构：
- **Controller 层**：处理 HTTP 请求
- **Service 层**：业务逻辑
- **Repository 层**：数据访问

详细的架构决策请参考 [docs/ADR](docs/ADR)。

## 许可证

MIT License
