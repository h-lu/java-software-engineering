# Week 12 Starter Code：Integration Tests, Mockito, Bug Bash

这个 starter 支持 Week 12：integration tests、Mockito-based tests、contract checks 和 Bug Bash 报告。

API 实现故意很小。它只能证明 Javalin server 可以启动，task API routes 仍是待办占位。你的作业是实现足够的行为，写出有意义的 integration tests，并记录 Bug Bash 发现。

## 运行命令

```bash
mvn test
mvn compile
java -cp target/classes com.campusflow.App
curl http://localhost:7070/health
curl http://localhost:7070/api/tasks
```

## 你需要编辑的文件

- `pom.xml`：为真实作业添加 Javalin 和 Mockito。
- `src/main/java/com/campusflow/App.java`：把占位 server 替换成 integration tests 需要的 Javalin API routes。
- `src/main/java/com/campusflow/service/TaskService.java`：补充 task operations 的业务行为。
- `src/main/java/com/campusflow/repository/InMemoryTaskRepository.java`：提供一个内存版 test repository。
- `src/test/java/com/campusflow/integration/TaskApiIntegrationTest.java`：创建真实 HTTP tests。
- `src/test/java/com/campusflow/mock/TaskServiceMockTest.java`：创建 Mockito tests。
- `BUG_BASH_REPORT.md`：记录 Bug Bash 中发现的至少两个 bug。
- `ROOT_CAUSE_ANALYSIS.md`：对一个 bug 做超出表面修复的分析。

## 待办清单

- [ ] 在 integration tests 中启动真实 Javalin server。
- [ ] 测试 `GET /api/tasks`。
- [ ] 测试 `POST /api/tasks`。
- [ ] 至少测试一个错误场景。
- [ ] 为预期 JSON 字段名增加 contract test。
- [ ] 至少增加两个 Mockito tests 和一个 Spy test。
- [ ] 完成 Bug Bash report。
- [ ] 完成一个 bug 的 root cause analysis。

内置 smoke test 只验证 server setup，不算作作业要求的 integration test suite。
