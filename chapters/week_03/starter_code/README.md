# Week 03 Starter Code：异常处理与防御式编程

这是 Week 03 作业的独立 Maven 起步项目。它把原来的单文件参考实现替换为标准 Maven 布局，并只保留学生可继续完成的 TODO 骨架。

## 运行命令

```bash
cd chapters/week_03/starter_code
mvn test
```

## 你需要编辑的文件

- `src/main/java/edu/campusflow/exception/`：补全 CampusFlow 自定义异常体系。
- `src/main/java/edu/campusflow/domain/Task.java`：在 setter 中加入防御式输入验证。
- `src/main/java/edu/campusflow/util/TaskFileLoader.java`：为文件读取加入异常处理和资源关闭策略。
- `docs/fmea.md`：完成至少 5 个故障点的 FMEA 分析。
- `src/test/java/edu/campusflow/StarterSmokeTest.java`：完成作业后可以扩展为真实验收测试。

## TODO 清单

- TODO 1：`TaskFileLoader.loadTasksFromFile` 处理 `null` 文件名、文件不存在、资源关闭。
- TODO 2：自定义异常消息要具体，能帮助调用者定位字段或任务名。
- TODO 3：`Task` 的标题、日期、优先级验证应与 `ASSIGNMENT.md` 表格一致。
- TODO 4：FMEA 文档中的 RPN 需要按 `严重程度 × 发生频率 × 检测难度` 计算。

`mvn test` 只验证骨架可编译，不代表作业完成。
