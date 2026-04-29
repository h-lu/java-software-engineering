# Week 11-12 课程作业审稿报告（Codex 分片）

审查范围：`chapters/week_11` 到 `chapters/week_12`

审查口径：只审查，不修改课程文件；逐周核对 `ASSIGNMENT.md`、`starter_code/src/main`、`starter_code/src/test`、`pom.xml`、`README.md` 的类名、包名、方法签名、文件路径、测试入口、依赖和构建产物。

## 测试运行结果

| Week | 命令 | 结果 | 备注 |
| --- | --- | --- | --- |
| week_11 | `cd chapters/week_11/starter_code && mvn -B -ntp test` | 通过 | `TaskStatusCalculatorTest` 1 个测试通过；Surefire 生成了 `target/surefire-reports/2026-04-28T21-51-01_249-jvmRun1.dumpstream` 警告文件。 |
| week_12 | `cd chapters/week_12/starter_code && mvn -B -ntp test` | 通过 | `AppSmokeTest` 1 个测试通过。 |

## week_11

### 一致性判断

整体可作为 Week 11 starter 使用：包名 `com.campusflow.quality`、主类 `CodeWithIssues` / `TaskStatusCalculator`、测试入口 `TaskStatusCalculatorTest` 与 README 指向一致；初始 smoke test 通过。作业要求学生补 SpotBugs、JaCoCo、测试和技术债，starter 的 TODO 也能支撑这些任务。

主要不一致集中在“提交物命名/承载文件”：`ASSIGNMENT.md` 要求独立提交 `fixes.md`、`new_tests.md`、`coverage_analysis.md` 等，starter README 却引导学生更新 `TEST_SUMMARY.md` 记录 SpotBugs/JaCoCo 命令、发现和前后结果。学生如果按 README 做，可能在评分时缺必交文件。

### P0

无。

### P1

1. `ASSIGNMENT.md` 与 starter README 的提交物命名不一致，可能导致学生按 README 完成但缺 `fixes.md` / `new_tests.md`。
   - 证据：`ASSIGNMENT.md` 要求 `fixes.md`、`new_tests.md`、`coverage_analysis.md`；`starter_code/README.md` 要求更新 `TEST_SUMMARY.md`。
   - 风险：非代码失败，学生完成内容但文件名不符合评分脚本或人工 checklist。
   - 建议：优先改 `ASSIGNMENT.md`，明确允许把 fixes/new tests/coverage analysis 写入 `TEST_SUMMARY.md` 的指定小节；或者改 starter README，列出 `fixes.md`、`new_tests.md`、`coverage_analysis.md` 这些独立文件。

2. `quality gate thresholds` 在 README 待办中像必做项，但 `ASSIGNMENT.md` 中 `quality-gate.json` 是进阶选做。
   - 证据：README 待办包含“定义你的 quality gate thresholds”；作业中“设计质量门禁配置”在进阶作业。
   - 风险：学生误以为基础作业必须完成质量门禁配置。
   - 建议：改 starter README，把该项标注为“进阶/选做”，或改 `ASSIGNMENT.md` 将轻量 threshold 说明纳入基础作业。

### P2

1. `starter_code/target/` 构建产物存在于工作区。
   - 证据：`chapters/week_11/starter_code/target/...` 下有 class、maven-status、surefire reports；`git ls-files` 未显示已跟踪，根 `.gitignore` 已忽略 `target/`。
   - 风险：若人工打包或绕过 ignore，可能把构建产物带入提交。
   - 建议：不改课程源码；发布前清理 `chapters/week_11/starter_code/target/`。

### 建议改 ASSIGNMENT 还是 starter_code

优先改 `ASSIGNMENT.md` 的提交物说明，统一“独立文件”与 `TEST_SUMMARY.md` 的关系；其次微调 starter README 的选做标注。starter Java 代码和 smoke test 当前不需要修改。

## week_12

### 一致性判断

初始 Maven smoke test 可通过，主包名 `com.campusflow` 及 `model/repository/service` 分层清晰。作业主线要求 Javalin + HttpClient + Mockito，starter README 也要求学生添加 Javalin 和 Mockito。

但 week 12 存在多处会让学生“照说明做仍可能失败或困惑”的一致性问题：Javalin 依赖没有在 `ASSIGNMENT.md` 的依赖任务中显式要求；starter 当前实现是 JDK `HttpServer` 而 README 说能证明 Javalin server；端口在 assignment 示例中是 8080，README/App 使用 7070；进阶 OpenAPI 示例路径是 `/tasks`，基础任务要求 `/api/tasks`。

### P0

无。

### P1

1. `ASSIGNMENT.md` 要求使用 Javalin，但只显式要求添加 Mockito 依赖，未给 Javalin 依赖配置。
   - 证据：任务 1 示例直接使用 `Javalin app`；任务 2.1 才列出 Mockito 依赖；`pom.xml` 只有 TODO 注释，没有 Javalin 依赖。
   - 风险：学生按任务 1 创建 `TaskApiIntegrationTest.java` 后会编译失败，除非从 README 或自行搜索补依赖。
   - 建议：改 `ASSIGNMENT.md`，在任务 1.1 增加 Javalin dependency 片段和版本；或改 starter `pom.xml` 预置 Javalin 依赖，只让学生实现路由。

2. 端口说明不一致：作业示例和 Bug Bash 示例使用 `8080`，starter README/App 使用 `7070`。
   - 证据：`ASSIGNMENT.md` 多处 `http://localhost:8080/api/tasks`；`starter_code/App.java` 默认 `PORT = 7070`；README curl 也是 7070。
   - 风险：学生运行 README 的服务再复制 assignment 的 curl/test，会请求错端口；smoke test 本身不覆盖 HTTP 入口，无法提前暴露。
   - 建议：优先改 `ASSIGNMENT.md` 和 README 统一端口，最好推荐测试中使用随机端口 `start(0)`，手动运行统一用 7070 或 8080 之一。

3. 进阶 OpenAPI 示例路径与基础 API 路径不一致。
   - 证据：基础任务全程使用 `/api/tasks`；OpenAPI 示例写 `paths: /tasks`。
   - 风险：学生按进阶示例写契约测试，会与基础集成测试的 API 契约冲突。
   - 建议：改 `ASSIGNMENT.md`，把 OpenAPI 示例路径改为 `/api/tasks`。

### P2

1. README 对当前 starter 的描述不准确。
   - 证据：README 写“证明 Javalin server 可以启动”，但 `App.java` 使用 `com.sun.net.httpserver.HttpServer`，`AppSmokeTest` 只断言 `App.healthJson()` 字符串，未启动 server。
   - 风险：学生误判当前 smoke test 覆盖了服务启动。
   - 建议：改 starter README，说明当前只是 JDK HttpServer 占位和 health payload smoke test；或改 starter_code 预置 Javalin。

2. 作业与 README 对测试文件路径的明确程度不同。
   - 证据：`ASSIGNMENT.md` 只说创建 `TaskApiIntegrationTest.java` / `TaskServiceMockTest.java`；README 指向 `src/test/java/com/campusflow/integration/TaskApiIntegrationTest.java` 和 `src/test/java/com/campusflow/mock/TaskServiceMockTest.java`。
   - 风险：学生可能把测试放在默认包或不一致包路径，Maven 仍可能跑，但风格和评分定位不统一。
   - 建议：改 `ASSIGNMENT.md`，补充推荐包名和完整路径。

3. `starter_code/target/` 构建产物存在于工作区。
   - 证据：`chapters/week_12/starter_code/target/...` 下有 class、maven-status、surefire reports；`git ls-files` 未显示已跟踪，根 `.gitignore` 已忽略 `target/`。
   - 风险：若人工打包或绕过 ignore，可能把构建产物带入提交。
   - 建议：不改课程源码；发布前清理 `chapters/week_12/starter_code/target/`。

### 建议改 ASSIGNMENT 还是 starter_code

优先改 `ASSIGNMENT.md`：补 Javalin 依赖、统一端口、修正 OpenAPI 路径、补完整测试路径。若希望降低学生起步成本，可以改 starter_code：在 `pom.xml` 预置 Javalin，或者把 README 描述改成当前 JDK HttpServer 占位的真实状态。

## 必须修改的文件清单

我认为发布前必须修改：

- `chapters/week_11/ASSIGNMENT.md`：统一 `fixes.md` / `new_tests.md` / `coverage_analysis.md` 与 `TEST_SUMMARY.md` 的提交物口径。
- `chapters/week_11/starter_code/README.md`：同步 Week 11 提交物命名，并标注 quality gate thresholds 是否选做。
- `chapters/week_12/ASSIGNMENT.md`：补 Javalin 依赖要求，统一端口，修正 OpenAPI 路径为 `/api/tasks`，补测试文件路径。
- `chapters/week_12/starter_code/README.md`：修正“Javalin server 已可启动”的描述，或与 starter 实现同步。

发布前还应清理但不属于课程内容修改：

- `chapters/week_11/starter_code/target/`
- `chapters/week_12/starter_code/target/`
