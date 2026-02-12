# 示例：代码审查清单（Code Review Checklist）

本示例展示 CampusFlow 团队使用的代码审查清单，系统化评估代码质量。

---

## 审查清单模板

审查者在审查 Pull Request 时，对照以下清单逐项检查：

---

### 设计层面

- [ ] **单一职责原则（SRP）**
  - 每个类是否只负责一件事？
  - 如果类的方法超过 7 个，考虑是否需要拆分
  - 示例：`TaskManager` 是否承载了太多职责？

- [ ] **开闭原则（OCP）**
  - 新增功能是否需要修改现有代码？
  - 是否可以通过扩展（而非修改）来添加功能？

- [ ] **接口隔离**
  - 依赖的是接口还是具体实现？
  - 示例：`TaskManager` 应该依赖 `TaskRepository` 接口

- [ ] **架构一致性**
  - 这个改动是否符合整体架构设计？
  - 是否与 ADR 中的决策一致？

---

### 代码层面

- [ ] **空指针风险（NPE）**
  - 方法参数是否为 null 做了检查？
  - 链式调用是否有 NPE 风险？
  ```java
  // ❌ 有风险
  if (task.getPriority().equals("高")) { }

  // ✅ 安全
  if ("高".equals(task.getPriority())) { }
  // 或
  if (priority == null) { throw new IllegalArgumentException(); }
  ```

- [ ] **异常处理**
  - 是否使用了合适的异常类型？
  - 异常信息是否清晰？
  - 是否记录了异常日志？

- [ ] **输入验证**
  - 公共方法是否验证了输入参数？
  - 非法输入是否被优雅处理？
  ```java
  // ✅ 好的做法
  public void addTask(Task task) {
      if (task == null) {
          throw new IllegalArgumentException("任务不能为 null");
      }
      if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
          throw new IllegalArgumentException("任务标题不能为空");
      }
      // ...
  }
  ```

- [ ] **魔法数字**
  - 是否有未命名的常量？
  ```java
  // ❌ 不好的做法
  if (priority == 3) { }

  // ✅ 好的做法
  private static final int PRIORITY_HIGH = 3;
  if (priority == PRIORITY_HIGH) { }
  ```

- [ ] **重复代码**
  - 是否有可以提取的重复逻辑？
  - 是否遵循 DRY 原则？

---

### 可维护性

- [ ] **命名清晰**
  - 类名、方法名、变量名是否清晰表达意图？
  - 是否遵循 Java 命名规范？
  ```java
  // ❌ 不清晰
  public void process() { }
  int n;

  // ✅ 清晰
  public void filterTasksByPriority() { }
  int taskCount;
  ```

- [ ] **注释质量**
  - 注释解释"为什么"，不是"做什么"
  - 公共 API 是否有 Javadoc？
  ```java
  // ❌ 无用的注释
  // 增加计数器
  count++;

  // ✅ 有价值的注释
  // 计数器需要线程安全，使用 AtomicInteger
  private AtomicInteger counter = new AtomicInteger(0);
  ```

- [ ] **代码复杂度**
  - 方法是否过长（超过 30 行）？
  - 嵌套是否过深（超过 3 层）？
  - 是否可以使用卫语句简化？

---

### 测试

- [ ] **单元测试覆盖**
  - 新增功能是否有对应的单元测试？
  - 测试是否独立、可重复？

- [ ] **边界情况**
  - 是否测试了空列表、null、空字符串？
  - 是否测试了最大值/最小值？

- [ ] **异常路径**
  - 是否测试了异常抛出？
  - 是否验证了异常信息？

---

## 审查反馈示例

### 好的反馈

```
如果 `priority` 为 null，第 5 行会抛出 NPE。
建议添加前置检查，或者考虑使用 Objects.equals() 来避免空指针。
你觉得哪种方式更适合这个场景？
```

**特点**：
- 解释问题原因（NPE 风险）
- 提供选项
- 用提问而非指责

### 不好的反馈

```
这里会 NPE，你代码写错了。
```

**问题**：
- 语气指责
- 没有解释原因
- 没有提供解决方案

---

## 使用说明

1. **审查前**：阅读 PR 描述，了解改动背景
2. **审查中**：对照清单逐项检查，在 PR 下添加评论
3. **审查后**：勾选已检查项，给出 Approve/Request Changes 结论

---

## 审查清单的演进

这个清单不是一成不变的。团队应该定期回顾：

- 哪些检查项经常被遗漏？
- 哪些检查项过于冗余？
- 最近出现了什么新问题需要加入清单？

CampusFlow 团队每月回顾一次审查清单，持续改进。
