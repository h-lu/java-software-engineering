# CampusFlow

> 一个简洁的校园任务管理系统，支持创建任务、设置截止日期、标记完成。

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen)](https://github.com/your-org/campusflow/actions)

## 快速开始（5 分钟）

### 前置要求

- Java 17+ ([下载](https://adoptium.net/))
- Maven 3.6+ ([下载](https://maven.apache.org/download.cgi))

### 1. 克隆仓库

```bash
git clone https://github.com/your-org/campusflow.git
cd campusflow
```

### 2. 启动服务器

```bash
mvn compile exec:java
```

首次启动会自动创建测试数据。

### 3. 访问应用

- Web 界面：http://localhost:7070
- API 文档：http://localhost:7070/swagger-ui（暂未启用）
- 健康检查：http://localhost:7070/health

### 验证安装

发送测试请求：

```bash
curl http://localhost:7070/health
# 预期输出：{"service":"CampusFlow","version":"2.3.0","status":"UP"}
```

## 功能特性

### 任务管理

- 创建任务、编辑任务、删除任务
- 设置截止日期、标记完成
- 任务搜索和过滤
- 软删除（删除任务可恢复）

### API 设计

- RESTful 风格 API
- JSON 数据格式
- 标准错误响应
- CORS 支持（开发模式）

### 质量保障

- 单元测试覆盖率 70%+
- 集成测试（端到端 API 测试）
- 契约测试（API 契约验证）
- 静态分析（SpotBugs）

## 项目结构

```
campusflow/
├── src/main/java/              # Java 源代码
│   └── com/campusflow/
│       ├── model/              # 领域模型（Task）
│       ├── dto/                # 数据传输对象（TaskRequest）
│       ├── repository/         # 数据访问层
│       ├── service/            # 业务逻辑层
│       ├── controller/         # HTTP 控制器
│       └── exception/         # 自定义异常
├── src/test/java/             # 测试代码
├── src/main/resources/        # 配置文件
├── docs/                      # 架构决策记录（ADR）
├── openapi.yaml              # API 文档
└── README.md                  # 本文件
```

## 使用指南

### API 端点

#### 健康检查

```bash
curl http://localhost:7070/health
```

#### 获取所有任务

```bash
curl http://localhost:7070/tasks
```

#### 创建任务

```bash
curl -X POST http://localhost:7070/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "学习集成测试",
    "description": "编写 CampusFlow 的集成测试",
    "dueDate": "2026-02-20"
  }'
```

#### 标记任务完成

```bash
curl -X POST http://localhost:7070/tasks/{id}/complete
```

#### 更新任务（部分字段）

```bash
curl -X PATCH http://localhost:7070/tasks/{id} \
  -H "Content-Type: application/json" \
  -d '{"completed": true}'
```

#### 删除任务

```bash
curl -X DELETE http://localhost:7070/tasks/{id}
```

### 获取统计信息

```bash
curl http://localhost:7070/stats
# 预期输出：{"total":3,"completed":1,"pending":2,"overdue":0}
```

## 开发指南

### 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试
mvn test -Dtest=TaskApiIntegrationTest

# 运行集成测试
mvn test -Dtest=*IntegrationTest
```

### 代码覆盖率

```bash
mvn jacoco:report
# 报告在 target/site/jacoco/index.html
open target/site/jacoco/index.html
```

### 静态分析

```bash
mvn spotbugs:check
# 报告在 target/site/spotbugs.html
open target/site/spotbugs.html
```

### 完整质量检查

```bash
mvn verify
# 包含：测试 + 覆盖率检查 + 静态分析
```

### 清理构建

```bash
mvn clean install
```

## 常见问题

### Q: 数据库在哪里？

**A:** 当前版本使用内存存储（`InMemoryTaskRepository`），数据在服务器重启后会丢失。

Week 07 后会引入 SQLite 持久化，数据库文件在项目根目录（`campusflow.db`），首次运行自动创建。

### Q: 如何修改端口号？

**A:** 编辑 `src/main/java/com/campusflow/App.java`，修改 `PORT` 常量：

```java
private static final int PORT = 8080; // 改成你想要的端口
```

### Q: 任务标题有长度限制吗？

**A:** 是的，标题最大 100 字符。这是业务规则，不是技术限制。

### Q: 为什么选择 Javalin 而不是 Spring Boot？

**A:** 参考架构决策记录 [ADR-004](docs/ADR/004-architecture-evolution.md)。

简要理由：课程目标是学习 Java 核心技术，不依赖框架魔法。Javalin 轻量、简洁，适合学习。

### Q: CORS 配置是什么？

**A:** CORS（跨域资源共享）允许前端从不同端口访问后端 API。

当前配置：允许任何 Origin（开发模式）。生产环境需要限制具体域名。

详见 [ADR-003](docs/ADR/003-api-design.md)。

### Q: 如何添加新的 API 端点？

**A:** 步骤如下：

1. 在 `TaskController` 中添加处理方法
2. 在 `App.createApp()` 中注册路由
3. 更新 `openapi.yaml` 文档
4. 编写单元测试和集成测试
5. 运行 `mvn verify` 确保质量门禁通过

### Q: 测试失败了怎么办？

**A:**

1. 先看测试失败原因（断言失败？异常？）
2. 检查日志输出
3. 使用 `mvn test -X` 查看详细调试信息
4. 如果是集成测试失败，检查服务器是否正常启动

## 架构设计

### 三层架构

```
┌─────────────────────────────────────┐
│   Controller 层 (HTTP 请求处理)      │
├─────────────────────────────────────┤
│   Service 层 (业务逻辑)              │
├─────────────────────────────────────┤
│   Repository 层 (数据访问)           │
└─────────────────────────────────────┘
```

### 依赖关系

- Controller → Service → Repository
- 单向依赖，避免循环依赖

### 设计模式

- **Repository 模式**：封装数据访问逻辑
- **DTO 模式**：数据传输对象，隔离领域模型
- **依赖注入**：手动组装依赖（不使用框架）

详细的架构决策请参考 [docs/ADR](docs/ADR)。

## 质量门禁

项目配置了以下质量门禁：

| 门禁 | 工具 | 标准 |
|------|------|------|
| 测试覆盖率 | JaCoCo | >= 70% |
| 静态分析 | SpotBugs | 高优先级问题 = 0 |
| 集成测试 | JUnit 5 | 全部通过 |

运行 `mvn verify` 自动检查所有门禁。

## 贡献指南

欢迎贡献！请：

1. Fork 本仓库
2. 创建特性分支（`git checkout -b feature/amazing-feature`）
3. 提交更改（`git commit -m 'Add amazing feature'`）
4. 推送到分支（`git push origin feature/amazing-feature`）
5. 创建 Pull Request

### 代码规范

- 遵循 Google Java Style Guide
- 方法最大 50 行
- 类最大 300 行
- 每个公共方法必须有 Javadoc

### 提交信息规范

使用 [Conventional Commits](https://www.conventionalcommits.org/)：

```
<type>(<scope>): <subject>

<body>

<footer>
```

类型：
- `feat`: 新功能
- `fix`: Bug 修复
- `docs`: 文档更新
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具相关

示例：

```
feat(tasks): add task search by keyword

Implement keyword search that searches in both title and description.
Add integration tests to verify the search functionality.

Closes #123
```

## 版本历史

### v2.3.0 (Week 12)

- 集成测试支持
- 契约测试示例
- 测试替身（Mock/Stub）
- 质量门禁配置

### v2.0.0 (Week 09)

- REST API 设计
- Javalin 框架集成
- CORS 配置

### v1.0.0 (Week 01)

- 初始版本（CLI）
- 核心领域模型
- 内存存储

## 许可证

[MIT License](LICENSE)

## 联系方式

- 项目主页：https://github.com/your-org/campusflow
- 问题反馈：https://github.com/your-org/campusflow/issues
- 文档：https://docs.campusflow.example.com

---

## 附录：Git 错误恢复

### 如果 push 失败

```bash
# 先 pull 最新代码
git pull --rebase origin main

# 解决冲突后
git push origin main
```

### 如果提交信息写错了

```bash
# 修改最近一次提交
git commit --amend -m "正确的提交信息"

# 如果已经 push，需要强制推送（谨慎使用！）
git push --force-with-lease origin main
```

### 如果需要撤销提交

```bash
# 撤销最近一次提交（保留更改）
git reset --soft HEAD~1

# 撤销最近一次提交（丢弃更改）
git reset --hard HEAD~1

# 如果已经 push，需要强制推送（谨慎使用！）
git push --force-with-lease origin main
```
