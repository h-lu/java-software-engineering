# Codex 审稿报告：week13_14

审查范围：`chapters/week_13` 到 `chapters/week_14`

审查对象：`ASSIGNMENT.md`、`starter_code/src/main`、`starter_code/src/test`、`starter_code/pom.xml`、`starter_code/README.md`

执行原则：只审查，不修改课程文件。

## Maven 测试记录

| Week | 命令 | 结果 | 备注 |
|---|---|---|---|
| week_13 | `mvn -B -ntp test` | PASS | `Tests run: 2, Failures: 0, Errors: 0, Skipped: 0`；当前 starter 无 `src/main/java`，Maven 输出 `No sources to compile`。 |
| week_14 | `mvn -B -ntp test` | PASS | `Tests run: 2, Failures: 0, Errors: 0, Skipped: 0`；Surefire 报告有 `Corrupted channel by directly writing to native stream` warning，但未导致失败。 |

## week_13

一致性判断：部分一致。starter 包能通过当前 smoke test，OpenAPI/README/ADR 模板与作业主题一致；但测试逻辑与学生完成作业后的状态相反，且作业中的部分路径与 starter 实际结构不一致。

### P0

- `starter_code/src/test/java/com/campusflow/Week13StarterSmokeTest.java` 要求 `openapi.yaml`、`PROJECT_README_TODO.md`、`docs/ADR_INDEX.md`、`docs/ADR/001-domain-model.md` 仍包含 `TODO` 或 `待办`。学生按 `ASSIGNMENT.md` 完成作业后会替换这些标记，smoke test 会反向失败。
  - 建议改：`starter_code/src/test/java/com/campusflow/Week13StarterSmokeTest.java`
  - 建议方向：测试应检查必需文件存在、OpenAPI 可解析、README 包含快速开始/验证步骤、ADR 链接存在，而不是检查 TODO 是否保留。

### P1

- `ASSIGNMENT.md` 的 ADR 示例链接使用 `adr/001-domain-model.md`，starter 实际目录是 `docs/ADR/001-domain-model.md`。Linux 文件系统区分大小写，学生照抄作业示例会造成链接完整性检查失败。
  - 建议改：`ASSIGNMENT.md`
  - 建议方向：统一为 `ADR/001-domain-model.md`，或把 starter 目录改成小写 `adr/`；更小改动是修正文档示例。

- `ASSIGNMENT.md` 参考资源写“可以参考 `starter_code/src/main/java/` 中的示例代码”，但 week_13 starter 没有 `src/main/java`，只有文档模板和测试。
  - 建议改：`ASSIGNMENT.md`
  - 建议方向：改为参考 `starter_code/openapi.yaml`、`PROJECT_README_TODO.md`、`docs/ADR_INDEX.md` 或 `examples/`。

### P2

- `ASSIGNMENT.md` 必交文件是根目录 `README.md`，starter README 让学生先改 `PROJECT_README_TODO.md` 再复制到项目根目录 `README.md`。这可以理解，但两个 README 的角色容易混淆。
  - 建议改：`starter_code/README.md` 或 `ASSIGNMENT.md`
  - 建议方向：明确 `starter_code/README.md` 是起步包说明，不是提交物；`PROJECT_README_TODO.md` 完成后应重命名或复制为提交物 `README.md`。

- `starter_code/target` 当前存在构建产物。它们被 `.gitignore` 的 `target/` 忽略，且未被 Git 跟踪，但发布教材包或手动打包时仍可能混入。
  - 建议改：不改课程源码；发布前清理工作区构建产物。

## week_14

一致性判断：存在阻塞级不一致。当前 starter 能通过 smoke test，但测试、README、作业正文对文件名、`Config` 类 API、JAR 名称和配置端口的要求不完全一致；学生按作业正文完成后很可能导致 smoke test 编译失败或断言失败。

### P0

- `ASSIGNMENT.md` 示例要求 `Config` 使用构造器和 JavaBean 风格方法：`new Config()`、`getDbPath()`、`getPort()`、`getEnv()`；starter 代码和测试使用 `Config.loadFor("dev")`、`env()`、`port()`、`databasePath()`。学生如果按作业正文实现，会使 `Week14StarterSmokeTest` 编译失败。
  - 建议改：优先改 `starter_code/src/main/java/com/campusflow/config/Config.java` 与 `starter_code/src/test/java/com/campusflow/Week14StarterSmokeTest.java`
  - 建议方向：让 starter API 与作业正文一致，或在作业正文明确 starter 采用 `loadFor/env/port/databasePath` 签名并同步示例。

- `Week14StarterSmokeTest` 要求 `README.md`、`CHANGELOG_TODO.md`、`DEPLOYMENT_TODO.md`、`Version.java`、`Config.java` 保留 `TODO` 或 `待办`。学生按要求完成作业后会删除 TODO，测试会反向失败。
  - 建议改：`starter_code/src/test/java/com/campusflow/Week14StarterSmokeTest.java`
  - 建议方向：测试应检查完成态行为，例如 `Config` 支持 `dev/prod`、`${PORT:8080}` 占位符、`Version.parse/compareTo/next*` 行为，以及 README/CHANGELOG/DEPLOYMENT 的最终文件存在。

- `ASSIGNMENT.md` 必交文件是 `CHANGELOG.md` 和 `DEPLOYMENT.md`，starter 提供并测试的是 `CHANGELOG_TODO.md` 和 `DEPLOYMENT_TODO.md`。学生若按必交清单改名，smoke test 会找不到 TODO 文件；若不改名，又与提交清单不一致。
  - 建议改：`starter_code` 文件名、`starter_code/README.md`、`Week14StarterSmokeTest`
  - 建议方向：starter 直接提供最终文件名 `CHANGELOG.md`、`DEPLOYMENT.md`，或测试同时接受最终文件名并不要求 TODO。

### P1

- `starter_code/README.md` 说完成 shade TODO 后运行 `java -jar target/campusflow-week14-starter-0.1.0-SNAPSHOT.jar`，但 `ASSIGNMENT.md` 全文使用 `target/campusflow-1.0.0.jar`，并要求发布 `v1.0.0`。
  - 建议改：`starter_code/README.md` 或 `starter_code/pom.xml`
  - 建议方向：如果作业目标是 `campusflow-1.0.0.jar`，starter 应提示学生同步改 `artifactId/version/finalName`，或 README 使用作业统一的 JAR 名称。

- `ASSIGNMENT.md` 的开发配置示例是 `server.port=7070`，starter 的 `config-dev.properties` 是 `server.port=8080`。同时作业验证 JAR 时访问 `http://localhost:7070/api/tasks`，Docker 示例和 prod 默认端口又使用 8080。
  - 建议改：`ASSIGNMENT.md` 或 `starter_code/src/main/resources/config-dev.properties`
  - 建议方向：统一开发端口，并在 README、配置文件、验证命令中保持一致。

- `starter_code/README.md` 和测试要求实现 `src/main/java/com/campusflow/util/Version.java`，但 `ASSIGNMENT.md` 的必交文件和建议目录树没有列出 `Version.java`，任务 1 也没有明确要求写 SemVer 工具类。
  - 建议改：`ASSIGNMENT.md` 或 `starter_code/README.md`/测试
  - 建议方向：若希望学生实现 `Version`，在任务 1 加入类名、包名、方法签名和测试入口；否则移除 starter 对 `Version.java` 的强要求。

### P2

- `starter_code/pom.xml` 当前只含 JUnit 和 shade TODO，`mvn test` 可通过，但无法验证作业核心的可执行 JAR。当前 smoke test 没有覆盖 `maven-shade-plugin`、Manifest 主类、签名文件排除。
  - 建议改：`starter_code/src/test` 或新增构建验证说明
  - 建议方向：保留轻量 smoke test 的同时，增加面向完成态的验证脚本或 README 命令，避免学生误以为 `mvn test` 通过就完成发布任务。

- `starter_code/target` 当前存在构建产物。它们被 `.gitignore` 的 `target/` 忽略，且未被 Git 跟踪，但发布教材包或手动打包时仍可能混入。
  - 建议改：不改课程源码；发布前清理工作区构建产物。

## 必须修改的文件清单

我认为进入 release 前必须修改：

- `chapters/week_13/starter_code/src/test/java/com/campusflow/Week13StarterSmokeTest.java`
- `chapters/week_13/ASSIGNMENT.md`
- `chapters/week_14/starter_code/src/test/java/com/campusflow/Week14StarterSmokeTest.java`
- `chapters/week_14/starter_code/src/main/java/com/campusflow/config/Config.java`
- `chapters/week_14/ASSIGNMENT.md`
- `chapters/week_14/starter_code/README.md`
- `chapters/week_14/starter_code/CHANGELOG_TODO.md`
- `chapters/week_14/starter_code/DEPLOYMENT_TODO.md`
