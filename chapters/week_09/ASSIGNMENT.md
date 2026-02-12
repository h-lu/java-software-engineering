# Week 09 作业：将 CampusFlow 改造为 Web 服务

## 作业目标

将 Week 08 重构后的 CampusFlow CLI 版改造为 REST API Web 服务，学习使用 Javalin 框架实现后端服务。

## 基础作业（必做）

### 1. 创建 Javalin Web 服务（25分）

搭建基础的 Javalin 项目结构：

- [ ] 配置 Maven `pom.xml`，添加 Javalin 6.x 和 Jackson 依赖
- [ ] 创建 `App` 类作为应用程序入口
- [ ] 配置 Javalin 实例，启用开发日志插件
- [ ] 设置服务器端口（默认 7070）

验收标准：
```bash
mvn compile exec:java
# 服务启动，访问 http://localhost:7070/health 返回 200
```

### 2. 实现 RESTful API 端点（40分）

实现以下端点：

| 方法 | 端点 | 功能 | 状态码 |
|------|------|------|--------|
| GET | `/tasks` | 获取所有任务 | 200 OK |
| GET | `/tasks/{id}` | 获取单个任务 | 200 OK / 404 Not Found |
| POST | `/tasks` | 创建任务 | 201 Created / 400 Bad Request |
| PUT | `/tasks/{id}` | 更新任务 | 200 OK / 404 Not Found |
| DELETE | `/tasks/{id}` | 删除任务 | 204 No Content / 404 Not Found |

要求：
- [ ] 使用 JSON 作为请求/响应格式
- [ ] 复用 Week 08 的 Repository 层
- [ ] 实现请求验证（标题不能为空等）
- [ ] 使用合适的状态码

### 3. 实现统一异常处理（15分）

- [ ] 创建自定义异常类（`NotFoundException`、`ValidationException`）
- [ ] 实现全局异常处理器
- [ ] 返回标准错误响应格式：

```json
{
  "error": "ValidationException",
  "message": "标题不能为空",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/tasks"
}
```

### 4. 编写 ADR-003（10分）

记录 API 设计决策，包含：
- [ ] 背景：为什么选择 REST 而非其他风格
- [ ] 决策：URI 设计、HTTP 方法选择、状态码约定
- [ ] 后果：正面和负面影响
- [ ] 替代方案：考虑过 GraphQL/gRPC 吗？

### 5. API 测试（10分）

- [ ] 使用 curl 或 HTTP 客户端测试所有端点
- [ ] 记录测试用例和结果
- [ ] 至少包含一个错误情况测试

## 进阶作业（选做，+20分）

选择至少 2 项完成：

### A. 查询参数过滤（10分）
实现查询参数支持：
- `GET /tasks?status=pending` - 按状态过滤
- `GET /tasks?priority=high` - 按优先级过滤
- `GET /tasks?search=关键词` - 按标题搜索

### B. 分页支持（10分）
实现分页：
- `GET /tasks?limit=10&offset=0` - 分页参数
- 响应包含分页元数据（总数量、当前页等）

### C. API 版本控制（10分）
实现版本控制：
- 使用路径版本（`/v1/tasks`）
- 或 Header 版本（`Accept-Version: v1`）

### D. 逾期费用计算端点（10分）
复用 Week 08 的策略模式，实现：
- `GET /tasks/{id}/overdue-fee?days=5` - 计算逾期费用

## 挑战作业（选做，+10分）

### E. 简单认证（10分）
实现基于 Token 的简单认证：
- `POST /login` - 返回 token
- 其他端点需要 `Authorization: Bearer <token>` Header

### F. OpenAPI 规范（10分）
- 编写 `openapi.yaml` 描述你的 API
- 或使用注解生成（如 SpringDoc，但 Javalin 需手动编写）

## AI 协作练习（可选）

### 练习 1：AI 辅助 API 设计
1. 向 AI 描述你的需求（"设计一个任务管理系统的 REST API"）
2. 记录 AI 的建议（端点设计、URI 命名、状态码选择）
3. 使用以下检查清单评估 AI 建议：
   - [ ] 是否使用名词而非动词？
   - [ ] 是否使用复数形式？
   - [ ] 状态码是否正确？
   - [ ] 是否有嵌套资源？
4. 记录 AI 建议的优点和需要改进的地方

### 练习 2：AI 代码审查
1. 使用 AI 审查你的 API 实现代码
2. 记录 AI 发现的问题（如果有）
3. 判断哪些问题是真的需要修复的

## 提交要求

### 代码提交
```bash
# 创建 week-09 分支
git checkout -b week-09

# 提交代码
git add .
git commit -m "feat: week 09 REST API implementation"

# 推送分支
git push origin week-09
```

### 提交内容清单
- [ ] 完整可运行的代码（`mvn test` 通过）
- [ ] `docs/ADR-003.md` 架构决策记录
- [ ] `docs/API_TESTING.md` API 测试记录（curl 命令和响应）
- [ ] （可选）`docs/AI_COLLABORATION.md` AI 协作练习记录

### 提交格式
```
week-09/
├── pom.xml
├── src/
│   ├── main/java/com/campusflow/...
│   └── test/java/com/campusflow/...
└── docs/
    ├── ADR-003.md
    ├── API_TESTING.md
    └── AI_COLLABORATION.md (可选)
```

## 评分标准

| 维度 | 占比 | 说明 |
|------|------|------|
| 功能完整性 | 40% | 所有基础功能实现，测试通过 |
| RESTful 规范 | 30% | URI 设计、HTTP 方法、状态码使用正确 |
| 代码质量 | 20% | 结构清晰，异常处理完善，有测试 |
| 文档与 ADR | 10% | ADR-003 完整，测试记录清晰 |

## 截止时间

本周作业截止：2026-02-19 23:59

## 参考资源

- 教材 Week 09 章节
- Javalin 官方文档：https://javalin.io/documentation
- REST API 教程：https://restfulapi.net/
- 示例代码：`chapters/week_09/examples/`

## 常见问题

**Q: 我需要保留 Week 08 的所有代码吗？**
A: 保留 Repository 层和领域模型，将 CLI 界面替换为 HTTP 控制器。

**Q: 可以使用内存数据库吗？**
A: 可以，本周重点在 API 设计，可以使用内存存储（`InMemoryTaskRepository`）。

**Q: 如何处理 CORS？**
A: Javalin 内置 CORS 插件：`config.bundledPlugins.enableCors(cors -> cors.addRule(...))`
