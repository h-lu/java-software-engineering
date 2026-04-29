# Week 11 Starter Code：Quality Gate

这个 starter 用于 Week 11 quality gate 作业。它故意没有提供完整的 SpotBugs 或 JaCoCo 配置；你需要自己添加插件、运行工具、分析结果，并改进测试。

## 运行命令

```bash
cd chapters/week_11/starter_code
mvn test
```

添加所需插件后，再运行：

```bash
mvn spotbugs:check
mvn test jacoco:report
```

## 你需要编辑的文件

- `pom.xml`：添加 SpotBugs 和 JaCoCo plugin 配置。
- `src/main/java/com/campusflow/quality/CodeWithIssues.java`：运行 SpotBugs 后，检查并修复至少三个真实问题。
- `src/main/java/com/campusflow/quality/TaskStatusCalculator.java`：用 JaCoCo 找出未覆盖分支并补测试。
- `src/test/java/com/campusflow/quality/TaskStatusCalculatorTest.java`：根据覆盖率报告扩展测试。
- `TECHNICAL_DEBT.md`：把 starter rows 替换成你自己的 prioritized backlog。
- `TEST_SUMMARY.md`：记录 SpotBugs 和 JaCoCo 命令、发现、前后结果。你也可以按作业拆成 `fixes.md`、`new_tests.md`、`coverage_analysis.md` 三个文件；如果不拆分，`TEST_SUMMARY.md` 需要包含同名小节，方便评分时定位。

## 待办清单

- [ ] 在 `pom.xml` 中配置 SpotBugs。
- [ ] 运行 SpotBugs，并把输出保存为 `spotbugs_report.txt`。
- [ ] 修复至少三个 high 或 medium priority issues。
- [ ] 在 `pom.xml` 中配置 JaCoCo。
- [ ] 生成并查看覆盖率报告。
- [ ] 为未覆盖分支至少增加三个测试。
- [ ] 用至少十个优先级明确的条目更新 `TECHNICAL_DEBT.md`。
- [ ] 进阶/选做：定义你的 quality gate thresholds，并解释取舍。

内置测试只是 smoke test，不足以达到作业要求的覆盖率目标。
