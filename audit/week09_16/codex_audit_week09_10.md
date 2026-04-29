# Week 09-10 作业一致性审稿报告

审查范围：`chapters/week_09` 到 `chapters/week_10`。

审查对象：`ASSIGNMENT.md`、`starter_code/src/main`、`starter_code/src/test`、`starter_code/pom.xml`、`starter_code/README.md`，并检查 `target/` 构建产物状态。

优先级约定：
- P0：会导致学生按作业要求完成后，`mvn test` 或核心验收反向失败。
- P1：作业说明、starter、测试或运行入口不一致，会明显误导实现。
- P2：不阻塞完成，但会增加困惑、评审噪音或发布卫生风险。

## Week 09

### 一致性判断

部分一致。`starter_code` 的包名为 `com.campusflow`，入口类为 `App`，源码路径和测试路径符合 Maven 标准目录；当前 starter 可编译，`mvn -B -ntp test` 通过。但测试仍断言占位实现，和 `ASSIGNMENT.md` 要求学生替换为真实 Javalin REST API 的目标冲突。

Maven 结果：
- 命令：`cd chapters/week_09/starter_code && mvn -B -ntp test`
- 结果：BUILD SUCCESS，`Tests run: 2, Failures: 0, Errors: 0, Skipped: 0`
- 备注：Surefire 报告中出现 forked JVM native stream warning，并生成了 `target/surefire-reports/2026-04-28T21-51-31_364-jvmRun1.dumpstream`，不影响测试通过。

### P0

1. `AppSmokeTest` 会惩罚完成后的真实实现。
   - 证据：`src/test/java/com/campusflow/AppSmokeTest.java:13-15` 断言 `App.tasksPlaceholderJson().contains("待办")`。
   - 冲突：`ASSIGNMENT.md:24-40` 要求实现真实 `/tasks` CRUD；`README.md:31` 也要求把占位 server 替换成 Javalin routes。
   - 风险：学生按要求删除或替换 `tasksPlaceholderJson()` 后，测试会编译失败或断言失败；如果保留“待办”占位以通过测试，又没有真正完成作业。
   - 建议修改：改 `starter_code/src/test/java/com/campusflow/AppSmokeTest.java`。删除对占位文案的断言，改为不依赖 TODO helper 的 smoke test，例如只验证 `App` 可加载，或改成完成作业后也成立的 `/health`/route wiring 测试。若希望保留 starter 阶段测试，则在 README 明确要求学生提交前必须替换该测试。

### P1

1. 作业验收命令和 starter `pom.xml` 不匹配。
   - 证据：`ASSIGNMENT.md:18-21` 写 `mvn compile exec:java`；`pom.xml:60-77` 只有 compiler 和 surefire，没有配置 `exec-maven-plugin` 和 `mainClass`；`README.md:18-25` 改用 `mvn compile` 加 `java -cp target/classes com.campusflow.App`。
   - 风险：学生照 ASSIGNMENT 跑验收命令可能无法启动服务，和 README 给出的入口不一致。
   - 建议修改：优先改 `starter_code/pom.xml`，加入 `exec-maven-plugin` 并配置 `com.campusflow.App`；或改 `ASSIGNMENT.md`，统一为 README 中的 `java -cp` 启动方式。

2. `/tasks` 响应 envelope 在不同文件中不一致。
   - 证据：`ASSIGNMENT.md:28-34` 只列 CRUD 状态码，没有说明 `GET /tasks` 返回 `{data, total}`；但 `TaskController.java:14-16` 注释要求返回 `{"data": [...], "total": n}`，Week 10 又把 `{data: [...], total: N}` 当作 API 契约。
   - 风险：学生实现成裸数组也符合 Week 09 作业表述，但 Week 10 前端按 `{data,total}` 读取会失败。
   - 建议修改：改 `ASSIGNMENT.md`，明确 Week 09 的 `GET /tasks` 响应结构；或者降低 Week 10 对 envelope 的硬依赖。

### P2

1. `target/` 构建产物存在于工作区，但当前未被 Git 跟踪。
   - 证据：存在 `chapters/week_09/starter_code/target/surefire-reports/...`；`git ls-files 'chapters/week_09/starter_code/target/*'` 无输出；`.gitignore:10` 的 `target/` 生效。
   - 风险：不会被 Git 提交，但 release 打包若不清理工作区，可能混入构建产物。
   - 建议修改：不需要改课程文件；release 前执行清理或确保打包只取 Git tracked files。

## Week 10

### 一致性判断

不一致风险较高。当前 Maven starter 可编译，包名、入口类和测试路径符合 Maven 标准目录；但 `ASSIGNMENT.md` 把 Week 09 未必存在的后端 API 当成已实现，并要求通过 CORS 联调任务列表。`starter_code/src/main` 只有 `/health`，没有 `/tasks`，README 又让学生“先启动 Week 09 backend，或扩展这个 starter”，导致后端归属和验收入口不清。

Maven 结果：
- 命令：`cd chapters/week_10/starter_code && mvn -B -ntp test`
- 结果：BUILD SUCCESS，`Tests run: 1, Failures: 0, Errors: 0, Skipped: 0`

### P0

1. Week 10 API 参考声明了 Week 09 不要求实现的接口，学生按 Week 09 完成后前端 smoke test 会失败。
   - 证据：`ASSIGNMENT.md:35-46` 要求或声明 `PATCH /tasks/{id}`、`GET /stats`、`POST /tasks/{id}/complete`，并写“后端已实现”；Week 09 `ASSIGNMENT.md:28-34` 只要求 `GET/POST/PUT/DELETE /tasks` 这组基础 CRUD，没有 `PATCH`、`/stats`、`/complete`。
   - 风险：学生照 Week 10 prompt 生成“标记完成”或统计面板，会调用不存在的端点；这不是学生实现错误，而是跨周契约错误。
   - 建议修改：改 `ASSIGNMENT.md`。把 Week 10 API 参考收敛到 Week 09 必做接口，或明确 Week 10 需要新增哪些后端接口并把它们列为本周后端任务。不要写“后端已实现”，除非 Week 09 作业同步要求这些接口。

2. CORS 验收使用的后端不明确，且 Week 10 starter backend 不能支撑任务列表联调。
   - 证据：`ASSIGNMENT.md:235-277` 要求修改 `src/main/java/com/campusflow/App.java` 并验证任务列表正常加载；Week 10 `App.java:19-26` 只注册 `/health`；`README.md:20` 又说手动测试 frontend 时先启动 Week 09 backend，或扩展这个 starter。
   - 风险：学生如果只给 Week 10 starter 加 CORS，`frontend/index.html` 调 `/tasks` 会 404；如果改 Week 09 backend，则 Week 10 提交物清单中的 `src/main/java/com/campusflow/App.java` 又容易指向错误目录。
   - 建议修改：改 `ASSIGNMENT.md` 和 `starter_code/README.md` 二选一统一口径。推荐写明“本周前端联调使用 Week 09 backend，在 Week 09 的 App.java 中开启 CORS”；若要 Week 10 独立提交，则 starter 必须复制或实现最小 `/tasks` API。

### P1

1. `AppSmokeTest` 依赖实现细节 helper，学生替换为 Javalin 后容易反向失败。
   - 证据：`src/test/java/com/campusflow/AppSmokeTest.java:8-10` 调用 `App.healthJson()`；`README.md:29-30` 要求把占位 server 替换为 CORS 配置；`ASSIGNMENT.md:244-256` 给的是 Javalin `Javalin.create(...).start(7070)` 风格。
   - 风险：学生写出正常的 Javalin `main` 后，如果删除 `healthJson()`，测试会编译失败；保留这个 helper 不是作业要求。
   - 建议修改：改 `starter_code/src/test/java/com/campusflow/AppSmokeTest.java`，不要依赖 `healthJson()` 这种占位 helper。可改成检查 `App.class` 可加载，或提供明确的可保留公共 factory 方法并在 README/ASSIGNMENT 中写入要求。

2. 作业验收命令和 `pom.xml` 不匹配。
   - 证据：`ASSIGNMENT.md:266-267` 写 `mvn -q compile exec:java`；`pom.xml:60-77` 没有 `exec-maven-plugin` 和 `mainClass`。
   - 风险：学生照验收命令启动后端时可能失败。
   - 建议修改：优先改 `starter_code/pom.xml`，配置 `exec-maven-plugin`；或改 `ASSIGNMENT.md`，统一为 README 的 `java -cp target/classes com.campusflow.App`。

3. CORS 示例可能缺少依赖上下文。
   - 证据：`ASSIGNMENT.md:244-256` 直接给出 Javalin CORS 代码；`pom.xml:50-57` 只有 JUnit，并仅注释“如果使用这个 backend 添加 Javalin”。
   - 风险：初学者复制 CORS 示例后会遇到 `io.javalin...` import 缺失，且不清楚需要同时加哪些依赖。
   - 建议修改：改 `ASSIGNMENT.md` 或 `README.md`，在 CORS 任务旁明确需要 Javalin 依赖；更好是改 `starter_code/pom.xml` 预置 Javalin，减少非核心阻塞。

### P2

1. `target/` 构建产物存在于工作区，但当前未被 Git 跟踪。
   - 证据：存在 `chapters/week_10/starter_code/target/surefire-reports/...`；`git ls-files 'chapters/week_10/starter_code/target/*'` 无输出；`.gitignore:10` 的 `target/` 生效。
   - 风险：不会被 Git 提交，但 release 打包若不清理工作区，可能混入构建产物。
   - 建议修改：不需要改课程文件；release 前清理或确保打包只取 Git tracked files。

## 必须修改的文件清单

我认为 release 前必须修改：

1. `chapters/week_09/starter_code/src/test/java/com/campusflow/AppSmokeTest.java`
   - 移除对 `tasksPlaceholderJson()` 和“待办”占位内容的断言。

2. `chapters/week_10/ASSIGNMENT.md`
   - 对齐 Week 09 实际 API 契约，移除或改写 `PATCH /tasks/{id}`、`GET /stats`、`POST /tasks/{id}/complete` 的“已实现”表述。
   - 明确 CORS 联调用的是 Week 09 backend 还是 Week 10 starter backend。

3. `chapters/week_10/starter_code/README.md`
   - 与 Week 10 ASSIGNMENT 统一后端归属说明。

4. `chapters/week_10/starter_code/src/test/java/com/campusflow/AppSmokeTest.java`
   - 移除对 `App.healthJson()` helper 的实现细节依赖，避免学生替换为 Javalin 后测试编译失败。

建议同步修改，但可按教学取舍决定：

5. `chapters/week_09/starter_code/pom.xml`
   - 如果保留 `mvn compile exec:java` 验收方式，则配置 `exec-maven-plugin`。

6. `chapters/week_10/starter_code/pom.xml`
   - 如果保留 `mvn -q compile exec:java` 验收方式，则配置 `exec-maven-plugin`；如果要求在 Week 10 starter 内实现 CORS，则预置 Javalin 依赖。

7. `chapters/week_09/ASSIGNMENT.md`
   - 明确 `GET /tasks` 响应是否必须为 `{data, total}`，避免 Week 10 前端契约漂移。
