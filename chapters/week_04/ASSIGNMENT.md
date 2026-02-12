# Week 04 作业：团队协作与 Code Review

> "代码是写给人看的，只是顺便让机器执行。"
> —— Harold Abelson

---

## 作业概述

本周作业分为三层：基础、进阶、挑战。你至少需要完成基础题才能通过本周考核。进阶题和挑战题是可选的，但强烈推荐尝试——它们能帮你真正掌握"在团队中高效协作"的能力。

**本周核心目标**：
1. 使用 Git Feature Branch 工作流进行团队协作开发
2. 创建规范的 Pull Request，使用审查清单系统化评估代码质量
3. 给出建设性的 Code Review 反馈，处理技术分歧
4. 撰写 ADR-002（数据存储方案决策）
5. 在团队环境中应用已学的 SOLID 原则和异常处理策略

---

## 基础题（必做）

### 任务 1：创建 Feature Branch 并完成开发

**场景**：小北要为 CampusFlow 添加"任务统计功能"（统计总任务数、已完成数、待完成数）。按照团队规范，他需要在 feature 分支上开发。

**任务**：

1. **创建功能分支**
   ```bash
   # 从最新的 main 分支创建
   git checkout main
   git pull origin main
   git checkout -b feature/task-stats
   ```

2. **实现任务统计功能**

   在 `TaskManager` 类中添加以下方法：

   ```java
   // 文件：TaskManager.java
   public class TaskManager {
       private List<Task> tasks = new ArrayList<>();

       /**
        * 获取任务统计信息
        * @return TaskStats 包含总任务数、已完成数、待完成数
        */
       public TaskStats getStatistics() {
           // 实现统计逻辑
       }

       /**
        * 按优先级统计任务数量
        * @return Map<优先级, 任务数>
        */
       public Map<String, Integer> countByPriority() {
           // 实现统计逻辑
       }
   }
   ```

3. **创建 TaskStats 类**

   ```java
   // 文件：TaskStats.java
   public class TaskStats {
       private int totalCount;
       private int completedCount;
       private int pendingCount;

       // 构造方法、getter、toString
   }
   ```

4. **提交到 feature 分支**
   ```bash
   git add .
   git commit -m "feat: 添加任务统计功能"
   git push -u origin feature/task-stats
   ```

**要求**：
- 分支命名符合规范：`feature/功能描述`
- 提交信息符合规范：`feat: 描述`
- 代码包含适当的注释
- 包含基本的输入验证（防御式编程）

**输入示例**：
```java
TaskManager manager = new TaskManager();
manager.addTask(new Task("任务1", "2025-03-01", 1));
manager.addTask(new Task("任务2", "2025-03-02", 2));
manager.markCompleted("任务1");

TaskStats stats = manager.getStatistics();
System.out.println(stats);
```

**输出示例**：
```
TaskStats{totalCount=2, completedCount=1, pendingCount=1}

按优先级统计：
高=1, 中=1
```

**提示**：
- 使用 `git log --oneline --graph` 查看分支历史
- 确保 main 分支始终可发布（不要在 main 上直接开发）
- 参考 CHAPTER.md 第 2 节的 Feature Branch 工作流

---

### 任务 2：创建 Pull Request

**场景**：小北完成了任务统计功能，现在需要创建 Pull Request，邀请队友审查代码。

**任务**：

1. **在 GitHub/GitLab 上创建 PR**
   - 标题：`feat: 添加任务统计功能`
   - 描述使用以下模板：

   ```markdown
   ## 改动内容
   添加任务统计功能，支持查看总体统计和按优先级统计。

   ### 新增类
   - `TaskStats`：统计信息的数据类
   - `TaskManager.getStatistics()`：获取总体统计
   - `TaskManager.countByPriority()`：按优先级统计

   ### 代码示例
   ```java
   TaskStats stats = manager.getStatistics();
   System.out.println("总任务数：" + stats.getTotalCount());
   ```

   ## 测试方式
   1. 运行 `TaskManagerTest` 中的测试用例
   2. 手动测试：在 CLI 中创建任务后查看统计

   ## 审查清单
   - [ ] 代码符合 Java 命名规范
   - [ ] 新增方法有适当的注释
   - [ ] 异常处理完善（考虑空列表情况）
   - [ ] 单元测试覆盖新增功能
   - [ ] 符合单一职责原则
   ```

2. **模拟审查过程**

   由于这是个人作业，你需要：
   - 自己审查自己的代码（使用审查清单）
   - 在 PR 描述中记录发现的 2-3 个问题
   - 修复问题后更新 PR

**要求**：
- PR 标题清晰描述改动内容
- PR 描述包含：做了什么、为什么做、如何测试
- 包含审查清单（Review Checklist）
- 记录自我审查发现的问题

**提交物**：
- PR 链接或截图（如果是本地 Git，提供 `git log` 输出）
- 自我审查记录（发现了哪些问题、如何修复）

**提示**：
- 好的 PR 描述能让审查者快速理解改动意图
- 审查清单确保不遗漏重要检查项
- 参考 CHAPTER.md 第 3 节的 PR 创建指南

---

### 任务 3：使用审查清单进行 Code Review

**场景**：阿码收到了小北的 PR，需要使用审查清单系统化地审查代码。

**任务**：

1. **使用以下审查清单审查代码**：

   ```markdown
   ## Code Review 检查清单

   ### 设计层面
   - [ ] 这个改动是否符合整体架构？
   - [ ] 类和方法的职责是否清晰？（SRP 检查）
   - [ ] 是否有更好的设计方案？

   ### 代码层面
   - [ ] 是否有明显的 bug？（空指针、数组越界等）
   - [ ] 异常处理是否完善？（Week 03 知识）
   - [ ] 输入验证是否到位？（防御式编程）
   - [ ] 是否有重复代码？

   ### 可维护性
   - [ ] 命名是否清晰？（方法名、变量名、类名）
   - [ ] 是否有适当的注释？（解释"为什么"，不是"做什么"）
   - [ ] 代码是否容易理解？

   ### 测试
   - [ ] 是否有单元测试？
   - [ ] 测试是否覆盖了边界情况？
   - [ ] 测试是否独立、可重复？
   ```

2. **记录审查意见**

   模拟审查以下代码（假设这是 PR 中的代码）：

   ```java
   // 需要审查的代码
   public class TaskManager {
       private List<Task> tasks = new ArrayList<>();

       public TaskStats getStatistics() {
           int total = tasks.size();
           int completed = 0;
           for (Task task : tasks) {
               if (task.isCompleted()) {
                   completed++;
               }
           }
           return new TaskStats(total, completed, total - completed);
       }

       public Map<String, Integer> countByPriority() {
           Map<String, Integer> counts = new HashMap<>();
           for (Task task : tasks) {
               String priority = task.getPriority();
               counts.put(priority, counts.get(priority) + 1);  // 潜在 NPE
           }
           return counts;
       }
   }
   ```

   找出至少 3 个问题，并以建设性方式提出反馈。

**要求**：
- 使用审查清单逐项检查
- 找出至少 3 个代码问题
- 反馈要建设性（解释原因、提供选项）
- 记录审查过程

**输出示例**（审查意见）：
```markdown
## 审查意见

### 问题 1：潜在空指针异常
**位置**：`countByPriority` 方法第 12 行
**问题**：`counts.get(priority)` 可能返回 null，导致 NPE
**建议**：使用 `getOrDefault` 或先检查 containsKey

### 问题 2：缺少空列表检查
**位置**：`getStatistics` 方法
**问题**：当 tasks 为空列表时，返回的统计信息是否合理？
**建议**：添加注释说明空列表的行为，或添加前置检查

### 问题 3：缺少输入验证
**位置**：类整体
**问题**：如果 tasks 为 null，所有方法都会抛出 NPE
**建议**：在构造函数和 setter 中添加防御式编程检查
```

**提示**：
- Code Review 不只是"挑错"，是"一起让代码更好"
- 对事不对人，解释原因，提供选项
- 参考 CHAPTER.md 第 4 节的反馈技巧

---

### 任务 4：撰写 ADR-002（数据存储方案决策）

**场景**：CampusFlow 团队需要决定数据存储方案。小北作为首席架构师，需要撰写 ADR-002 记录决策过程。

**任务**：

撰写 `docs/adr/002-数据存储方案.md`，包含以下内容：

```markdown
# ADR-002: 数据存储方案决策

## 状态
已采纳 / 提案中 / 已废弃

## 背景
CampusFlow 目前使用内存存储（ArrayList），程序关闭后数据丢失。
需要选择一种持久化方案。

## 决策
我们选择 ______ 作为数据存储方案。

## 理由
### 为什么选这个方案
1. ...
2. ...

### 为什么不选其他方案
1. ...
2. ...

## 替代方案
### 方案 A：内存存储（现状）
- 优点：...
- 缺点：...

### 方案 B：文件存储（JSON/CSV）
- 优点：...
- 缺点：...

### 方案 C：SQLite 数据库
- 优点：...
- 缺点：...

## 后果
### 正面影响
- ...

### 负面影响
- ...

## 验证方式
- [ ] Week 05：完成 FileTaskRepository 实现
- [ ] Week 06：完成 SQLiteTaskRepository 实现

## 相关决策
- ADR-001：领域模型设计

## 首席架构师
你的名字 - 第 X 轮

## 记录日期
YYYY-MM-DD
```

**要求**：
- 对比至少 3 种方案（内存、文件、数据库）
- 给出明确的决策和充分的理由
- 分析正面和负面影响
- 列出验证方式（检查清单）
- **必须由人写，不能用 AI 代劳决策内容**

**提示**：
- ADR 记录的是决策过程，不只是结果
- 写 ADR 的过程就是深度思考的过程
- 参考 CHAPTER.md 第 6 节的 ADR-002 示例

---

## 进阶题（选做）

### 任务 5：处理审查反馈并改进代码

**场景**：小北收到了阿码的审查意见，需要根据反馈改进代码。

**任务**：

1. **根据以下审查意见修改代码**：

   ```markdown
   ## 审查意见

   1. **NPE 风险**：`countByPriority` 中使用 `counts.get(priority) + 1` 会 NPE
   2. **职责分离**：`TaskManager` 既管理任务又做统计，是否违反 SRP？
   3. **异常处理**：当 tasks 为 null 时，方法会抛出 NPE
   4. **测试覆盖**：缺少空列表和 null 的测试用例
   ```

2. **修改代码并提交**

   ```bash
   # 修改代码...
   git add .
   git commit -m "refactor: 根据 Code Review 意见优化代码"
   git push origin feature/task-stats
   ```

3. **在 PR 中回复审查意见**

   ```markdown
   ## 修改记录

   - [x] 修复 NPE 风险：使用 `getOrDefault`
   - [x] 添加 null 检查：在构造函数中防御式编程
   - [x] 补充测试：添加空列表和 null 测试用例
   - [ ] 职责分离：保持现状，但添加 TODO 注释，计划 Week 05 重构
   ```

**要求**：
- 修复所有标记为"必须修复"的问题
- 对"建议性"意见给出回应（接受或解释为什么不接受）
- 保持建设性的沟通态度

**提示**：
- 不是所有审查意见都必须接受
- 如果不同意某个意见，礼貌地解释原因
- 参考 CHAPTER.md 第 5 节的分歧处理技巧

---

### 任务 6：模拟多人协作场景

**场景**：你和队友同时修改了同一个文件，产生了冲突。

**任务**：

1. **模拟冲突场景**

   ```bash
   # 创建两个分支
   git checkout -b feature/add-validation
   git checkout -b feature/add-logging

   # 在两个分支上修改 TaskManager.java 的同一个方法
   # feature/add-validation：在构造函数中添加验证
   # feature/add-logging：在构造函数中添加日志
   ```

2. **解决合并冲突**

   ```bash
   # 先合并 feature/add-validation
   git checkout main
   git merge feature/add-validation

   # 再合并 feature/add-logging（会产生冲突）
   git merge feature/add-logging

   # 手动解决冲突
   ```

3. **记录冲突解决过程**

   写出：
   - 冲突是如何产生的
   - 冲突标记是什么样的
   - 你是如何解决的
   - 如何避免类似冲突

**要求**：
- 展示冲突前后的代码
- 展示冲突标记（`<<<<<<<` `=======` `>>>>>>>`）
- 解释解决思路
- 总结避免冲突的最佳实践

**提示**：
- 冲突是团队协作的常态
- 使用 `git log --oneline --graph` 查看分支历史
- 参考 CHAPTER.md 第 2 节的冲突处理指南

---

## 挑战题（可选）

### 任务 7：建立团队代码规范

**场景**：CampusFlow 团队需要建立正式的代码规范文档。

**任务**：

撰写 `docs/code-style.md`，包含：

1. **Git 工作流规范**
   - 分支命名规范
   - Commit Message 规范
   - PR 创建规范

2. **Code Review 规范**
   - 审查清单
   - 反馈准则
   - 合并标准（需要几个 approve）

3. **Java 代码规范**
   - 命名规范
   - 注释规范
   - 异常处理规范

4. **AI 使用规范**（Week 04+ 要求）
   - 哪些场景可以用 AI
   - 如何标注 AI 辅助代码
   - AI 生成代码的审查要求

**要求**：
- 规范要具体、可执行
- 包含示例（好的 vs 不好的）
- 与团队实际情况相符

---

## AI 协作练习（可选）

> **Week 04 处于"识别期"**：学会审视 AI 代码，加入 AI 生成代码审查练习。

### 任务：审查 AI 生成的代码

下面这段代码是某个 AI 工具生成的"任务统计功能"实现：

```java
public class TaskStatsService {
    private List<Task> tasks;

    public TaskStatsService(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Map<String, Object> getFullStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", tasks.size());
        stats.put("completed", tasks.stream().filter(Task::isCompleted).count());
        stats.put("pending", tasks.size() - (Long) stats.get("completed"));

        // 按优先级统计
        Map<String, Integer> byPriority = new HashMap<>();
        for (Task task : tasks) {
            String p = task.getPriority();
            byPriority.put(p, byPriority.get(p) + 1);  // 问题在这里
        }
        stats.put("byPriority", byPriority);

        // 计算完成率
        double completionRate = (Long) stats.get("completed") / (double) tasks.size();
        stats.put("completionRate", completionRate * 100 + "%");

        return stats;
    }
}
```

**请审查这段代码**：

- [ ] 代码能运行吗？有哪些潜在问题？
- [ ] 变量命名清晰吗？类型选择合理吗？
- [ ] 有没有缺少错误处理的地方？
- [ ] 边界情况处理了吗？（空列表、null 等）
- [ ] 你能写一个让它失败的测试吗？
- [ ] 与你自己写的 TaskStats 相比，优缺点是什么？

**提交**：
1. 修复后的代码
2. 你发现了哪些问题的简短说明（至少 3 个）
3. 你对"AI 生成代码"的总体评价

**提示**：
- AI 擅长生成代码骨架，但常遗漏边界情况
- AI 可能使用不合适的类型（如 `Map<String, Object>`）
- 审查 AI 代码和审查人类代码的标准是一样的

---

## 提交要求

### 必交内容（基础题）

1. **Git 协作记录**：
   - feature 分支创建记录（`git log` 输出）
   - PR 描述（Markdown 格式）
   - 自我审查记录

2. **源代码**：
   - `src/main/java/edu/campusflow/domain/TaskStats.java`
   - `src/main/java/edu/campusflow/manager/TaskManager.java`（更新后的版本）

3. **ADR 文档**：
   - `docs/adr/002-数据存储方案.md`

4. **审查记录**：
   - 审查清单使用情况
   - 发现的问题和反馈

### 选交内容（进阶/挑战）

1. **代码修改记录**：
   - 根据审查意见修改后的代码
   - 修改说明

2. **冲突解决记录**（任务 6）：
   - 冲突场景描述
   - 解决过程记录

3. **团队规范文档**（任务 7）：
   - `docs/code-style.md`

4. **AI 审查报告**（AI 协作练习）：
   - 修复后的代码
   - 问题发现和评价

### 提交格式

```bash
# Git 提交
git add docs/adr/002-数据存储方案.md
git add src/main/java/edu/campusflow/
git commit -m "feat: 完成 Week 04 基础题 - 团队协作与 Code Review"

# 如果做了进阶题
git add docs/code-style.md
git commit -m "docs: 添加团队代码规范"
```

---

## 常见错误与避坑指南

### 错误 1：直接在 main 分支开发

**问题**：违反 Feature Branch 工作流，可能污染主线。

**正确做法**：
```bash
# 创建 feature 分支
git checkout -b feature/xxx

# 开发完成后通过 PR 合并
```

---

### 错误 2：PR 描述过于简单

**问题**：审查者无法理解改动意图。

**不好的描述**：
```markdown
添加统计功能
```

**好的描述**：
```markdown
## 改动内容
添加任务统计功能，支持查看总体统计和按优先级统计。

### 新增方法
- `getStatistics()`：返回 TaskStats 对象
- `countByPriority()`：返回 Map<优先级, 数量>

## 测试方式
运行 TaskManagerTest 中的测试用例
```

---

### 错误 3：Code Review 只关注风格

**问题**：只评论"这里少了个空格"，忽略了设计问题。

**正确做法**：
- 关注设计：是否符合 SRP？
- 关注健壮性：异常处理是否完善？
- 关注可维护性：命名是否清晰？
- 风格问题可以用工具（如 Checkstyle）自动化

---

### 错误 4：ADR 流于形式

**问题**：只写决策结果，不写决策过程。

**不好的 ADR**：
```markdown
## 决策
使用 SQLite

## 理由
因为它好。
```

**好的 ADR**：
```markdown
## 决策
使用 SQLite 作为数据存储方案。

## 理由
1. **持久化需求**：内存存储数据会丢失，不符合真实使用场景
2. **查询能力**：SQLite 支持 SQL，便于实现复杂筛选
3. **轻量级**：无需单独安装数据库服务器
4. **可扩展性**：将来可迁移到 PostgreSQL/MySQL
```

---

### 错误 5：用 AI 代写 ADR

**问题**：ADR 记录的是团队的决策过程，AI 不知道你的业务约束。

**正确做法**：
- AI 可以帮你整理格式、检查语法
- 决策内容必须由人写
- 写 ADR 的过程就是深度思考的过程

---

## 评分标准

详细评分标准见 `RUBRIC.md`。

简要说明：
- **基础题**：60 分 - 完成任务 1-4，Git 工作流正确，ADR 完整
- **进阶题**：+20 分 - 完成任务 5 或 6，能处理审查反馈或解决冲突
- **挑战题**：+20 分 - 完成任务 7，团队规范完整
- **AI 协作**：+10 分（额外）- 完成 AI 代码审查练习

---

## 学习资源

- **本周章节**：`chapters/week_04/CHAPTER.md`
- **起步代码**：`starter_code/`（如果你遇到困难）
- **风格指南**：`shared/style_guide.md`
- **Git 工作流参考**：`shared/gitea_workflow.md`

---

## 起步代码使用说明

如果你遇到困难，可以参考 `starter_code/` 目录中的起步代码：

```bash
# 起步代码位置
starter_code/
├── src/main/java/edu/campusflow/domain/     # TaskStats 模板
├── src/main/java/edu/campusflow/manager/    # TaskManager 扩展模板
└── docs/adr/                                # ADR 模板
```

**使用建议**：
1. 先自己尝试完成任务
2. 遇到困难时参考起步代码的**思路**，不要直接复制
3. 起步代码可能故意留有一些问题，你需要发现并修复

---

## 截止日期

**提交截止**：本周日 23:59

**延迟提交**：每晚一天扣 10% 分数，最多延迟 3 天
