# Week 10 Starter Code：AI Frontend + 官方 Backend Base

这个 starter 支持 Week 10 作业：用 AI 生成 CampusFlow frontend，人工审查、修复，再通过 CORS 接入 backend。

本目录现在包含一个**官方 Week 10 backend base**。它相当于一份干净、可运行的 Week 09 结果包，避免每个小组因为 Week 09 作业完成度不同而卡在前后端联调上。

## 包含内容

- 一个 Java 21 + Maven + Javalin 6.x backend。
- 已实现并可直接运行的 CampusFlow task API：
  - `GET /health`
  - `GET /tasks`，返回 `{data: [...], total: N}`
  - `GET /tasks/{id}`
  - `POST /tasks`
  - `PUT /tasks/{id}`
  - `DELETE /tasks/{id}`
- 开发环境 CORS 已开启，允许浏览器直接从 `frontend/index.html` 调用 `http://localhost:7070`。
- 一个待学生完成的 frontend 工作区：保存 AI 原始输出、人工审查、修复后的前端。
- 单元测试和 HTTP smoke test，用来确认 backend base 可编译、可运行、API contract 可用。

## 快速运行

```bash
cd chapters/week_10/starter_code
mvn test
mvn compile exec:java
```

另开一个终端测试 API：

```bash
curl http://localhost:7070/health
curl http://localhost:7070/tasks
curl -X POST http://localhost:7070/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"审查 AI 代码","description":"检查幻觉 API","dueDate":"2026-05-11"}'
curl http://localhost:7070/tasks
```

### Windows 中文说明

这个 backend 会按 UTF-8 返回 JSON，并且兼容 Windows 命令行可能发出的 GBK/GB18030 中文请求体。

如果你在 Windows `cmd` 或 PowerShell 里直接看 `curl` 输出仍然乱码，通常是终端显示编码问题，不是 API 数据坏了。建议先执行：

```bat
chcp 65001
```

或者在 PowerShell / 浏览器 DevTools 里查看响应。发送请求时也推荐显式声明 UTF-8：

```bash
curl -X POST http://localhost:7070/tasks \
  -H "Content-Type: application/json; charset=utf-8" \
  -d '{"title":"中文任务","description":"检查 Windows 中文","dueDate":"2026-05-11"}'
```

然后在浏览器中打开：

```text
chapters/week_10/starter_code/frontend/index.html
```

## API Contract

基础 URL：`http://localhost:7070`

| 方法 | 路径 | 说明 |
|---|---|---|
| `GET` | `/health` | 健康检查 |
| `GET` | `/tasks` | 获取任务列表，返回 `{data, total}` |
| `GET` | `/tasks/{id}` | 获取单个任务 |
| `POST` | `/tasks` | 创建任务 |
| `PUT` | `/tasks/{id}` | 更新任务，包括标记完成 |
| `DELETE` | `/tasks/{id}` | 删除任务 |

任务对象示例：

```json
{
  "id": "uuid-string",
  "title": "审查 AI 代码",
  "description": "检查 XSS、空状态和幻觉 API",
  "dueDate": "2026-05-11",
  "completed": false,
  "overdueDays": 0,
  "createdAt": "2026-05-11T02:00:00Z"
}
```

创建任务请求体：

```json
{
  "title": "审查 AI 代码",
  "description": "检查 XSS、空状态和幻觉 API",
  "dueDate": "2026-05-11"
}
```

标记完成请使用 `PUT /tasks/{id}`，提交更新后的任务字段。基础作业不要假设存在 `PATCH /tasks/{id}`、`POST /tasks/{id}/complete` 或 `GET /stats`。

`src/test/java/com/campusflow/contract/CorsContractTemplateTest.java` 提供了禁用的 CORS 验收模板。选择 standalone backend 方案时，完成 `/tasks` 和 CORS 后移除 `@Disabled`。

## 你需要编辑的文件

前端与 AI 协作产物：

- `PROMPT.md`：写下生成第一版 frontend 使用的 prompt。
- `AI_TOOL.md`：记录 AI tool 和 model version。
- `frontend/ai_generated.html`：保存未经修改的 AI 原始输出。
- `REVIEW.md`：完成 checklist，至少记录三类问题。
- `frontend/index.html`：实现你审查并修复后的 frontend。

通常不需要改 backend base。只有当你要挑战额外后端功能时，才修改：

- `src/main/java/com/campusflow/App.java`
- `src/main/java/com/campusflow/model/Task.java`
- `src/main/java/com/campusflow/service/TaskService.java`
- `src/main/java/com/campusflow/repository/InMemoryTaskRepository.java`

## 待办清单

- [ ] 写一个包含 role、task、constraints、output format 的 prompt。
- [ ] 保存未经修改的 AI generated frontend。
- [ ] 审查 XSS、empty states、loading/error states、hallucinated APIs。
- [ ] 修复 frontend，处理用户内容时不要使用不安全的 `innerHTML`。
- [ ] 至少增加两个 UX improvements。
- [ ] 启动本目录提供的 backend base，并验证 frontend 能通过 CORS 调用 `/tasks`。
- [ ] 按提交要求保存截图和说明。

完成实现后，请至少运行：

```bash
mvn test
```
