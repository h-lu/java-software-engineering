# Week 09 Starter Code：Javalin REST API

这是 Week 09 作业的小型待办骨架：把 CampusFlow 从 CLI 程序改造成 Javalin REST API。

## 包含内容

- 一个 Java 21 Maven 项目，已接入 JUnit 5。
- 可运行的 `App`，包含 `GET /health`。
- 任务相关占位路由暂时返回 `501 Not Implemented`。
- 最小版 domain、repository、service、controller 和 exception 类，里面保留了待办标记。
- smoke tests 用来确认 starter 可以编译，server 基本 wiring 可用。
- 注释会提示你在哪里加入 Javalin 6.x 和 Jackson 的真实实现。

## 运行命令

```bash
cd chapters/week_09/starter_code
mvn test
mvn compile
java -cp target/classes com.campusflow.App
curl http://localhost:7070/health
curl http://localhost:7070/tasks
```

## 你需要编辑的文件

- `pom.xml`：添加 Javalin 6.x 和 Jackson。
- `src/main/java/com/campusflow/App.java`：把占位 server 替换成 Javalin routes 和全局错误处理。
- `src/main/java/com/campusflow/controller/TaskController.java`：把 HTTP 请求转换为 service 调用。
- `src/main/java/com/campusflow/service/TaskService.java`：实现校验和业务操作。
- `src/main/java/com/campusflow/repository/InMemoryTaskRepository.java`：实现内存持久化。
- `src/main/java/com/campusflow/model/Task.java`：根据 Week 08 的模型补充所需字段或行为。
- `docs/ADR-003.md`：在你的提交中创建该文件，说明 REST API 设计决策。
- `docs/API_TESTING.md`：创建该文件，记录 curl 或 HTTP client 测试。

## 待办清单

- [ ] 实现 `GET /tasks`。
- [ ] 实现 `GET /tasks/{id}`，任务不存在时返回 `404`。
- [ ] 实现 `POST /tasks`，包含请求校验和 `201`。
- [ ] 实现 `PUT /tasks/{id}`。
- [ ] 实现 `DELETE /tasks/{id}`，成功时返回 `204`。
- [ ] 为 validation 和 not-found 错误返回统一 JSON error body。
- [ ] 为完成后的 routes 增加有意义的 unit 或 integration tests。
- [ ] 编写 ADR-003 和 API testing notes。

starter tests 故意只做 smoke test。实现作业时，请替换或扩展它们。
