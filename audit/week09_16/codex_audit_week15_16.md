# Week 15-16 课程作业审稿报告（Codex 分片）

审查范围：`chapters/week_15` 到 `chapters/week_16`

审查口径：只审查，不修改课程文件；逐周核对 `ASSIGNMENT.md`、`starter_code/src/main`、`starter_code/src/test`、`pom.xml`、`README.md` 的类名、包名、方法签名、文件路径、测试入口、依赖和构建产物。

## 测试运行结果

| Week | 命令 | 结果 | 备注 |
| --- | --- | --- | --- |
| week_15 | `cd chapters/week_15/starter_code && mvn -B -ntp test` | 通过 | `Week15StarterSmokeTest` 2 个测试通过。运行环境为 Java 21.0.10、Maven 3.8.6；README 写 Maven 3.9+，但当前构建未依赖 3.9 特性。 |
| week_16 | `cd chapters/week_16/starter_code && mvn -B -ntp test` | 通过 | `Week16StarterSmokeTest` 2 个测试通过；Surefire 生成了 `target/surefire-reports/2026-04-28T21-51-27_935-jvmRun1.dumpstream` 警告文件，但未导致失败。 |

补充验证：我在 `/tmp` 复制 week_15/week_16 的 starter_code，并把 Markdown 模板中的 `TODO` / `待办` 替换为模拟完成内容后重新运行 `mvn -B -ntp test`。两周都失败：week_15 失败在 `Week15StarterSmokeTest.java:24`，报 `presentation_script.md should contain 学生 TODO/待办项`；week_16 失败在 `Week16StarterSmokeTest.java:24`，报 `01_showcase_feedback_summary.md should contain 学生 TODO/待办项`。这证明当前 smoke test 会在学生真正完成并清理占位词后反向失败。

## week_15

### 一致性判断

整体结构很小：主类 `com.campusflow.ShowcaseReadinessCheck`、测试类 `com.campusflow.Week15StarterSmokeTest`、JUnit 依赖和 Maven 测试入口一致，初始 starter 包能编译并通过测试。`ASSIGNMENT.md` 明确 Week 15 不要求新增 Java 功能，starter 也只是模板与检查器，这一点一致。

主要问题是测试目标和学生目标冲突：smoke test 把“模板仍包含 TODO/待办”作为通过条件，而学生完成作业后合理行为正是删除或替换这些占位词。另外，README 引导学生编辑 `slides_outline.md` / `poster_checklist.md`，但作业最终要求提交 `slides.pptx` 或 `slides.html`、`poster.pdf` 或 `poster.html`，两者缺少清晰映射。

### P0

无。

### P1

1. smoke test 会在学生完成模板后反向失败。
   - 证据：`starter_code/src/test/java/com/campusflow/Week15StarterSmokeTest.java:23-24` 读取每个必需模板并断言内容仍包含 `TODO` 或 `待办`。
   - 风险：学生按作业完成 `presentation_script.md`、`qa_prep.md` 等文件并清理占位词后，`mvn test` 失败，形成“完成越充分越失败”的反馈。
   - 建议：改 `starter_code`。学生侧 smoke test 只应检查文件存在、可读取、关键章节/表格仍在；如果需要检查初始模板含占位词，应移到教师侧生成校验，不放在学生运行的 `mvn test`。

2. README 的编辑文件与作业最终提交物没有明确对应关系。
   - 证据：`ASSIGNMENT.md` 要求 `slides.pptx` / `slides.html` 和 `poster.pdf` / `poster.html`；`starter_code/README.md` 的“需要编辑的文件”列的是 `slides_outline.md`、`poster_checklist.md`，没有提醒这些只是规划材料，不等同最终 PPT/海报。
   - 风险：学生按 README 完成 starter 文件后，可能缺少真正评分需要的 PPT 和海报成品。
   - 建议：优先改 `starter_code/README.md`，把 `slides_outline.md` / `poster_checklist.md` 标为准备辅助文件，并明确最终仍要生成 `slides.pptx`/`slides.html` 和 `poster.pdf`/`poster.html`；也可在 `ASSIGNMENT.md` 中补一句 starter 模板与最终提交物的映射。

### P2

1. `showcase_practice.md` 是可选加分项，但被 `ShowcaseReadinessCheck.requiredFiles()` 作为 required file。
   - 证据：`ASSIGNMENT.md` 中跨组展示演练是进阶作业；`ShowcaseReadinessCheck.java:10-16` 将 `showcase_practice.md` 放入 `REQUIRED_FILES`。
   - 风险：如果学生认为可选项可以删除或不提交，测试/检查器语义会与作业语义冲突。
   - 建议：改 `starter_code`，将 required/optional 分开，或者在 README 写明“文件保留，未做可写未参加”。

2. `prepare_demo_data.sh` 出现在 README 待编辑列表，但不在作业提交要求中。
   - 证据：`starter_code/README.md` 列出 `prepare_demo_data.sh`；`ASSIGNMENT.md` 提交文件命名没有该脚本。
   - 风险：学生可能误以为脚本也是必交物。
   - 建议：改 `starter_code/README.md`，标注为“可选演示辅助脚本，不作为评分提交物”。

3. `starter_code/target/` 构建产物存在于工作区。
   - 证据：审查开始前已存在 `chapters/week_15/starter_code/target/surefire-reports/...`；`git ls-files` 未显示跟踪，根 `.gitignore` 已有 `target/`。
   - 风险：当前不是已跟踪文件问题，但发布打包或人工复制时容易带入构建产物。
   - 建议：不改课程源码；发布前清理 `chapters/week_15/starter_code/target/`。

### 建议改 ASSIGNMENT 还是 starter_code

优先改 `starter_code`：修正 smoke test 的断言方向，拆分 required/optional 文件，并更新 README 对最终交付物的说明。`ASSIGNMENT.md` 只需补充 starter 模板与最终提交文件的映射即可。

## week_16

### 一致性判断

主类 `com.campusflow.Week16ReadinessCheck`、测试类 `com.campusflow.Week16StarterSmokeTest`、JUnit 依赖和 Maven 测试入口一致，初始 starter 包通过测试。`ASSIGNMENT.md` 的主题是展示反馈、工程复盘、知识地图、职业规划，starter Markdown 模板也覆盖了这四类内容。

主要问题同样是 smoke test 反向约束学生必须保留占位词；其次是 starter 的 `.md` 模板和作业最终 zip/PDF/图片目录结构之间缺少交付映射。学生按 README 填完 Markdown，不一定知道哪些内容还要导出为 PDF、图片或放入 `01_展示/`、`02_复盘/` 等目录。

### P0

无。

### P1

1. smoke test 会在学生完成模板后反向失败。
   - 证据：`starter_code/src/test/java/com/campusflow/Week16StarterSmokeTest.java:23-24` 断言每个模板文件仍包含 `TODO` 或 `待办`。
   - 风险：学生把 `01_showcase_feedback_summary.md`、`02_retrospective_report.md` 等模板写成最终稿并删除占位词后，`mvn test` 失败。
   - 建议：改 `starter_code`。学生侧 smoke test 应验证文件存在、标题/必要章节存在、内容非空，而不是要求保留占位词。

2. starter README 没有把 Markdown 模板映射到最终提交 zip 结构。
   - 证据：`ASSIGNMENT.md` 要求 `week_16_学号_姓名.zip` 下分 `01_展示/`、`02_复盘/`、`03_知识地图/`、`04_职业规划/`，且多处要求 PDF 或图片；`starter_code/README.md` 只列出 5 个 `.md` 文件需要编辑。
   - 风险：学生按 README 完成 Markdown 后，可能缺少导出的 PDF/图片和最终目录结构，人工评分或平台提交时不合格。
   - 建议：优先改 `starter_code/README.md` 和 `submission_checklist.md`，明确每个 `.md` 模板对应哪个最终目录、是否需要导出 PDF/图片；也可在 `ASSIGNMENT.md` 的提交要求中补充“starter Markdown 可作为源文件，最终需按目录导出/整理”。

3. 反馈汇总格式前后不完全一致。
   - 证据：任务 1.4 允许“反馈汇总表（Markdown 或 PDF）”，但最终 zip 示例写 `反馈汇总.pdf`；starter 提供的是 `01_showcase_feedback_summary.md`。
   - 风险：学生提交 Markdown 是否合格不够明确。
   - 建议：改 `ASSIGNMENT.md`，在最终 zip 树中写 `反馈汇总.md 或 反馈汇总.pdf`；或改 README 要求导出为 PDF。

### P2

1. 能力雷达图、知识地图、学习计划等最终成品在 starter 中被合并为 Markdown 小节。
   - 证据：作业要求能力雷达图为 PDF 或图片、知识地图为 PDF 或图片、6 个月学习计划为 PDF 或表格；starter 分别放在 `02_retrospective_report.md`、`03_knowledge_map.md`、`04_career_plan.md` 中。
   - 风险：学生可能只写文字说明，未产出可视化图表或独立计划表。
   - 建议：改 `starter_code/README.md` / `submission_checklist.md`，明确这些 Markdown 是源稿，最终还要导出图表或表格成品。

2. `starter_code/target/` 构建产物存在于工作区。
   - 证据：审查开始前已存在 `chapters/week_16/starter_code/target/surefire-reports/...`；本次测试又生成了一个 Surefire dumpstream 警告文件；`git ls-files` 未显示跟踪，根 `.gitignore` 已有 `target/`。
   - 风险：当前不是已跟踪文件问题，但发布打包或人工复制时容易带入构建产物。
   - 建议：不改课程源码；发布前清理 `chapters/week_16/starter_code/target/`。

### 建议改 ASSIGNMENT 还是 starter_code

优先改 `starter_code`：修正 smoke test、补 README/清单的最终交付映射。`ASSIGNMENT.md` 需要同步修正反馈汇总格式，并说明 starter Markdown 与最终 zip/PDF/图片之间的关系。

## 必须修改的文件清单

我认为发布前必须修改：

- `chapters/week_15/starter_code/src/test/java/com/campusflow/Week15StarterSmokeTest.java`：移除“必须保留 TODO/待办”的反向断言，改为检查文件存在、结构完整、内容非空。
- `chapters/week_15/starter_code/README.md`：说明 `slides_outline.md` / `poster_checklist.md` 是准备辅助文件，不替代最终 `slides.pptx`/`slides.html` 和 `poster.pdf`/`poster.html`；标注 `prepare_demo_data.sh` 为可选辅助脚本。
- `chapters/week_15/starter_code/src/main/java/com/campusflow/ShowcaseReadinessCheck.java`：拆分必做与可选文件，避免把 `showcase_practice.md` 表述为 required。
- `chapters/week_16/starter_code/src/test/java/com/campusflow/Week16StarterSmokeTest.java`：移除“必须保留 TODO/待办”的反向断言，改为检查文件存在、结构完整、内容非空。
- `chapters/week_16/starter_code/README.md`：补充 5 个 Markdown 模板到最终 zip 目录、PDF/图片/表格成品的映射。
- `chapters/week_16/starter_code/submission_checklist.md`：明确哪些 Markdown 可直接提交，哪些需要导出 PDF、图片或表格。
- `chapters/week_16/ASSIGNMENT.md`：统一反馈汇总的 Markdown/PDF 口径，并说明 starter Markdown 可作为源文件但最终需按提交结构整理。

发布前还应清理但不属于课程内容修改：

- `chapters/week_15/starter_code/target/`
- `chapters/week_16/starter_code/target/`
