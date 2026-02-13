# CampusFlow 架构决策索引

本文档汇总了 CampusFlow 项目的所有架构决策记录（ADR）。

## 什么是 ADR？

ADR（Architecture Decision Record）是架构决策记录——记录重要的技术决策、背景、后果。它不是"设计文档"，而是"决策日志"，帮助团队回顾"为什么这样设计"。

## ADR 列表

| ADR | 标题 | 日期 | 状态 | 影响 |
|-----|------|------|------|------|
| [ADR-001](adr/001-domain-model.md) | 领域模型设计 | 2026-01-15 | 已采纳 | 高 |
| [ADR-002](adr/002-data-storage.md) | 数据存储方案 | 2026-02-01 | 已采纳 | 高 |
| [ADR-003](adr/003-api-design.md) | REST API 设计 | 2026-02-15 | 已采纳 | 中 |
| [ADR-004](adr/004-architecture-evolution.md) | 架构演进 | 2026-02-28 | 已采纳 | 高 |

## 决策摘要

### ADR-001：领域模型设计

**决策**：采用贫血模型（Anemic Domain Model）

**核心内容**：
- Task 作为数据实体（POJO），业务逻辑在 Service 层
- 领域模型：Task（id, title, description, dueDate, completed）
- 验证逻辑：Service 层负责（标题长度、日期有效性）

**背景**：Week 02 时团队规模小（2 人），项目复杂度低，优先考虑简单性。

**优点**：
- 简单、易理解
- 适合小团队快速迭代
- 业务逻辑集中在 Service 层，容易定位

**缺点**：
- 业务逻辑分散，项目大时难以维护
- 领域模型变成"数据容器"，缺少业务行为
- 违反面向对象设计原则（行为应该在对象内部）

**后果**：
- 如果项目演变成大型系统（>10 人团队），需考虑演进为充血模型（Rich Domain Model）
- 当前阶段（学习项目）保持贫血模型

**相关 ADR**：影响 ADR-002（数据存储方案）、ADR-003（API 设计）

---

### ADR-002：数据存储方案

**决策**：使用 SQLite + JDBC

**核心内容**：
- SQLite：零配置、单文件数据库
- JDBC：手动管理连接、使用 try-with-resources
- Repository 模式：封装数据访问逻辑

**技术栈**：
```java
// 数据库连接
Connection conn = DriverManager.getConnection("jdbc:sqlite:campusflow.db");

// 资源管理
try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    // 执行查询
}
```

**背景**：Week 07 时选择，优先考虑简单性而非性能。

**优点**：
- 零配置：无需安装数据库服务器
- 单文件：数据库文件可以直接备份、迁移
- 学习成本低：JDBC 是 Java 数据访问的基础
- 适合小规模应用（<1000 QPS）

**缺点**：
- 并发性能有限：写入并发度低
- 不支持多服务器部署：单机数据库
- 功能有限：没有存储过程、触发器等高级特性

**后果**：
- 如果并发量超过 100 QPS，需迁移到 PostgreSQL/MySQL
- 如果需要多服务器部署，需引入独立的数据库服务器

**撤销条件**：
- 并发 QPS > 100
- 需要多服务器部署
- 需要事务的高级特性（如分布式事务）

**相关 ADR**：受 ADR-001 影响，影响 ADR-003（API 设计）

---

### ADR-003：REST API 设计

**决策**：采用标准 RESTful 风格

**核心内容**：
- 资源导向：`/tasks`、`/tasks/{id}`
- HTTP 方法语义：GET（读取）、POST（创建）、PUT（完整更新）、PATCH（部分更新）、DELETE（删除）
- JSON 格式：使用 Gson 序列化
- 状态码规范：200（成功）、400（客户端错误）、404（不存在）、500（服务器错误）

**API 端点示例**：
```
GET    /tasks              - 获取所有任务
GET    /tasks/{id}         - 获取单个任务
POST   /tasks              - 创建任务
PUT    /tasks/{id}         - 完整更新任务
PATCH  /tasks/{id}         - 部分更新任务
DELETE /tasks/{id}         - 删除任务
POST   /tasks/{id}/complete - 标记完成（业务功能）
```

**背景**：Week 09 时设计，与前端团队约定契约。

**优点**：
- 标准化：符合 RESTful 最佳实践
- 前后端分离：前端可以独立开发
- 可缓存：GET 请求可以被缓存
- 易于测试：每个端点独立测试

**缺点**：
- 可能不是最优性能：对于特定场景，GraphQL 更灵活
- 需要多次请求：获取关联数据需要多次请求

**后果**：
- 如果需要 GraphQL 或 gRPC，需重新设计 API
- 如果需要实时推送，需引入 WebSocket

**撤销条件**：
- 前端需求复杂，需要 GraphQL 的灵活性
- 需要实时通信（WebSocket）

**相关 ADR**：受 ADR-001、ADR-002 影响

---

### ADR-004：架构演进

**决策**：不引入 Spring Boot

**核心内容**：
- 继续使用 Javalin（极简框架）
- 理由：学习 Java 核心技术，不依赖框架魔法
- 代价：需要手动配置依赖注入、事务管理

**当前架构**：
```
Javalin (Web 框架)
    ↓
Controller 层 (HTTP 请求处理)
    ↓
Service 层 (业务逻辑)
    ↓
Repository 层 (数据访问)
    ↓
SQLite (数据库)
```

**背景**：Week 12 时讨论，团队认为"理解原理"比"快速开发"更重要。

**优点**：
- 学习 Java 核心技术（JDBC、Servlet 原理）
- 不依赖框架"黑盒"魔法
- 代码透明，容易调试
- 适合教学项目

**缺点**：
- 开发效率低：需要手动配置很多内容
- 缺少生态：没有 Spring 的丰富组件（Security、Data、Cloud）
- 生产环境风险：缺少企业级特性（监控、配置中心）

**后果**：
- 如果项目需要进入生产环境，需重新评估 Spring Boot
- 团队需要学习 Spring Boot 才能进入企业开发

**撤销条件**：
- 项目进入生产环境
- 需要企业级特性（监控、配置中心、分布式）
- 团队规模扩大，需要标准框架

**相关 ADR**：受前面所有 ADR 影响，这是对整体架构的总结

---

## 决策依赖图

```
ADR-001（领域模型：贫血模型）
    ↓ 影响
ADR-002（数据存储：SQLite + JDBC）
    ↓ 影响
ADR-003（API 设计：RESTful）
    ↓ 影响
ADR-004（架构演进：不引入 Spring Boot）
```

**解释**：
1. ADR-001 选择贫血模型 → Task 是简单 POJO → 数据访问简单（ADR-002）
2. ADR-002 选择 SQLite → 小规模数据库 → RESTful API 足够（ADR-003）
3. ADR-003 选择 RESTful → API 设计清晰 → 不需要 Spring Boot（ADR-004）

**涟漪效应**：Week 02 的选择影响了后续所有架构决策。

## 待决策事项

以下是未来可能需要决策的问题，提前记录下来避免"临时抱佛脚"。

### 高优先级

- [ ] **是否引入缓存层（Redis）？**
  - 背景：如果任务量超过 10000，查询可能变慢
  - 触发条件：平均响应时间 > 500ms
  - 建议：使用 Redis 缓存热点数据（如最近访问的任务）

- [ ] **是否需要消息队列（异步任务）？**
  - 背景：如果有耗时操作（如发送通知、生成报告），需要异步处理
  - 触发条件：API 响应时间 > 2s
  - 建议：使用 RabbitMQ 或 Kafka

### 中优先级

- [ ] **是否迁移到 PostgreSQL？**
  - 背景：如果并发量超过 100 QPS
  - 触发条件：数据库写入延迟 > 100ms
  - 建议：迁移到 PostgreSQL，引入连接池（HikariCP）

- [ ] **是否引入 Spring Boot？**
  - 背景：项目进入生产环境，需要企业级特性
  - 触发条件：团队规模 > 5 人
  - 建议：逐步迁移，先用 Spring Boot 替换 Javalin

### 低优先级

- [ ] **是否使用 GraphQL 替代 REST？**
  - 背景：前端需要灵活的数据查询
  - 触发条件：前端抱怨 API 请求次数太多
  - 建议：评估 GraphQL 的收益和成本

- [ ] **是否引入微服务架构？**
  - 背景：项目变得庞大，单体难以维护
  - 触发条件：代码量 > 50000 行
  - 建议：拆分为 Task Service、User Service、Notification Service

## 决策模板

如果需要新增 ADR，请使用以下模板：

```markdown
# ADR-XXX: [决策标题]

## 状态
- 已采纳 / 已撤销 / 已替代

## 背景
描述为什么需要这个决策。

## 决策
说明选择了什么方案。

## 理由
解释为什么选择这个方案，而不是其他方案。

## 后果
说明这个决策带来的影响（正面和负面）。

## 相关 ADR
列出相关的架构决策。
```

## 参考

- [ADR 模板和最佳实践](https://adr.github.io/)
- [ThoughtWorks: 架构决策记录](https://www.thoughtworks.com/radar/techniques/architecture-decision-records)
- [Microsoft: 架构决策记录](https://docs.microsoft.com/en-us/azure/architecture/patterns/decision-records)

---

**最后更新**：2026-02-12
**维护者**：CampusFlow 团队
