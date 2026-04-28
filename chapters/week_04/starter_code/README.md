# Week 04 Starter Code：任务统计与 Code Review

这是 Week 04 作业的独立 Maven 起步项目。它只提供任务统计功能的骨架、ADR 模板和烟雾测试，不包含完整实现。

## 运行命令

```bash
cd chapters/week_04/starter_code
mvn test
```

## 项目结构

```text
src/main/java/edu/campusflow/
├── domain/
│   ├── Task.java
│   └── TaskStats.java
└── manager/
    └── TaskManager.java
docs/adr/
└── 002-数据存储方案.md
```

## 你需要完成的 TODO

- `TaskStats`：补全构造方法约束、getter 和 `toString`。
- `TaskManager.getStatistics()`：统计总任务数、已完成数、待完成数。
- `TaskManager.countByPriority()`：按优先级统计数量，避免 `counts.get(priority) + 1` 的 NPE。
- `docs/adr/002-数据存储方案.md`：由团队自己完成数据存储决策。
- 在作业报告中记录 feature branch、PR 描述、自我审查和至少 3 条 Code Review 反馈。

`mvn test` 只验证骨架可编译。你完成 TODO 后应补充覆盖空列表、重复优先级、已完成任务等场景的测试。
