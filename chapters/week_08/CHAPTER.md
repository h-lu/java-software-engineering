# Week 08：让代码活得更久——设计模式与代码重构

> "代码是写给人看的，只是顺便让机器执行。"
> —— Harold Abelson

2025-2026 年，AI 辅助重构工具正在重塑代码维护的方式。GitHub Copilot 的 Agent Mode 已经可以自主处理 issue、生成代码并提交 PR；Cursor 和 Claude Code 也能识别代码坏味道并建议改进方案。但这也带来了新的问题：当 AI 建议"提取方法"或"引入策略模式"时，工程师如何判断这个建议是否合理？

重构不仅仅是代码改动，更是对系统行为的保持承诺。AI 可以帮你执行重构，但决定是否重构、选择哪种手法、判断重构是否成功——这些判断必须是人做出的。本周，你将学习如何识别代码坏味道、应用经典重构手法，并理解设计模式背后的设计原则——这些能力让你在 AI 时代依然保持对代码的主导权。

---

## 前情提要

上周你完成了 CampusFlow 的持久化改造——数据从内存迁移到了 SQLite，Repository 层通过 JDBC 实现了完整的 CRUD 操作。你的代码现在能"记住"东西了。

但 JDBC 代码写起来相当啰嗦：每个操作都要处理 `Connection`、`PreparedStatement`、`ResultSet`，异常处理也重复出现。更重要的是，随着功能增加，你的 `TaskService` 类可能已经膨胀到几百行——它既要处理业务逻辑，又要协调数据访问，还要处理各种边界情况。

这不是你的错，这是代码自然生长的结果。就像花园如果不修剪就会杂草丛生，代码如果不维护就会逐渐腐烂。但优秀的工程师知道：代码需要定期"修剪"。本周我们将学习如何让代码保持整洁、可维护——通过识别代码坏味道、应用重构手法、引入设计模式。

---

## 学习目标

完成本周学习后，你将能够：

1. 识别常见的代码坏味道（上帝类、重复代码、长方法等）并理解其危害
2. 应用基本重构手法（提取方法、移动方法、引入参数对象等）改进代码结构
3. 理解并应用策略模式、工厂模式解决特定设计问题
4. 使用 SpotBugs 或 PMD 进行静态代码分析，发现潜在问题
5. 理解技术债的概念，建立可持续的代码维护意识
6. 为 CampusFlow 编写 ADR-004，记录架构演进决策

---

<!--
贯穿案例设计：【CampusFlow 代码质量大改造】
- 第 1 节：从一个有"坏味道"的 CampusFlow 代码开始（上帝类、重复代码、长方法）
- 第 2 节：识别代码坏味道，理解重构的价值
- 第 3 节：应用提取方法、移动方法等基础重构手法
- 第 4 节：引入策略模式解决条件分支过多的问题
- 第 5 节：使用静态分析工具（SpotBugs/PMD）发现隐藏问题
- 第 6 节：CampusFlow 进度——编写 ADR-004，记录架构演进决策
最终成果：一个经过重构、代码质量显著提升的 CampusFlow，附带 ADR-004 文档

认知负荷预算检查：
- 本周新概念（系统化工程阶段上限 6 个）：
  1. 代码坏味道（Code Smells）
  2. 重构（Refactoring）
  3. 策略模式（Strategy Pattern）
  4. 工厂模式（Factory Pattern）- 简要介绍
  5. 静态代码分析（Static Analysis）
  6. 圈复杂度（Cyclomatic Complexity）
- 结论：✅ 正好 6 个，在预算内

回顾桥设计（至少 2 个）：
- [Repository 模式]（来自 week_05/07）：在第 3-4 节，通过重构 Repository 层来复现
- [SOLID 原则]（来自 week_02）：在第 2-4 节，通过代码坏味道与改进方案来复现
- [单元测试]（来自 week_06）：在第 3 节，强调重构必须有测试保护
- [try-with-resources]（来自 week_07）：在第 3 节，作为重复代码的例子进行重构

AI 小专栏规划：
专栏 1（放在第 2 节之后，前段）：
- 主题：AI 辅助重构建议评估——如何批判性地审视 AI 提出的重构方案
- 连接点：与第 2 节代码坏味道识别呼应
- 建议搜索词："AI refactoring tools 2025 2026", "GitHub Copilot code review refactoring", "LLM code smell detection accuracy"

专栏 2（放在第 4 节之后，中段）：
- 主题：AI 时代的设计模式——LLM 是否能正确理解和应用设计模式
- 连接点：与第 4 节策略模式/工厂模式呼应
- 建议搜索词："LLM design pattern application 2025 2026", "AI generated design patterns quality", "ChatGPT strategy pattern implementation"

CampusFlow 本周推进：
- 上周状态：CLI 版功能完整，SQLite 持久化完成，Repository 层有完整单元测试
- 本周改进：代码重构与质量提升，编写 ADR-004（架构演进决策）
- 涉及的本周概念：代码坏味道识别、重构手法、设计模式、静态分析、技术债管理
- 建议示例文件：examples/08_campusflow_refactoring.java, examples/08_campusflow_strategy.java

角色出场规划：
- 小北：在第 1 节面对自己写的"上帝类"感到困惑；在第 3 节重构时担心"改出 bug"
- 阿码：在第 2 节追问"AI 能不能自动重构？"；在第 4 节尝试用 AI 生成策略模式代码并讨论其质量
- 老潘：在第 1 节点评代码坏味道的生产环境风险；在第 5 节介绍静态分析工具在团队中的使用；在第 6 节指导 ADR-004 的撰写
-->

## 1. 你的代码有"味道"吗？

小北打开两周前写的 `TaskService`，准备添加一个新功能。他滚动着屏幕，眉头越皱越紧。

"我当时为什么这么写？"他盯着那个 300 多行的类，感觉自己像是在看陌生人写的代码。

这个类里有创建任务的逻辑、有查询任务的逻辑、有计算逾期费用的逻辑，甚至还有直接操作数据库的 JDBC 代码。所有东西混在一起，像一锅煮过头的粥。

老潘路过，瞥了一眼屏幕："上帝类？"

"什么？"

"**上帝类**（God Class）——一个类什么都管，就像上帝一样全知全能。"老潘摇摇头，"这是典型的代码坏味道。在生产环境里，这种代码维护起来是噩梦。你改一个地方，莫名其妙另一个地方就崩了。"

小北想起 Week 02 学过 SOLID 原则，其中单一职责原则（SRP）就说一个类应该只有一个改变的理由。但看着眼前的 `TaskService`，它至少有五个改变的理由：任务状态变更、费用计算规则调整、数据库结构变化、输入验证逻辑更新、报表格式修改……

"这代码能跑吗？"老潘问。

"能跑，测试都过了……"

"能跑的代码不一定是好代码。"老潘说，"来，一起看看这个'有味道'的代码到底问题在哪。"

```java
// 文件：TaskServiceBefore.java（代码坏味道版本）
public class TaskService {
    private final String dbUrl = "jdbc:sqlite:campusflow.db";
    private final List<Task> cache = new ArrayList<>();

    // 300+ 行的上帝类：业务逻辑 + 数据访问 + 输入验证 + 费用计算

    public void createTask(String title, String description, String priority) {
        // 输入验证
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (priority == null || (!priority.equals("high") && !priority.equals("medium") && !priority.equals("low"))) {
            throw new IllegalArgumentException("优先级必须是 high/medium/low");
        }

        // 生成 ID
        String id = UUID.randomUUID().toString();

        // 数据库操作（直接写在 Service 里！）
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO tasks (id, title, description, priority, status) VALUES (?, ?, ?, ?, ?)")) {

            pstmt.setString(1, id);
            pstmt.setString(2, title);
            pstmt.setString(3, description);
            pstmt.setString(4, priority);
            pstmt.setString(5, "pending");
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("创建任务失败", e);
        }

        // 更新缓存
        Task task = new Task(id, title, description, priority, "pending");
        cache.add(task);

        // 发送通知（又一项职责！）
        System.out.println("任务已创建: " + title);
    }

    public double calculateOverdueFee(String taskId, int daysOverdue) {
        // 从数据库读取任务
        Task task = null;
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tasks WHERE id = ?")) {
            pstmt.setString(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                task = new Task(rs.getString("id"), rs.getString("title"),
                              rs.getString("description"), rs.getString("priority"),
                              rs.getString("status"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询失败", e);
        }

        if (task == null) return 0.0;

        // 复杂的费用计算逻辑
        double baseFee = 10.0;
        String priority = task.getPriority();
        double multiplier = 1.0;

        if (priority.equals("high")) {
            multiplier = 3.0;
        } else if (priority.equals("medium")) {
            multiplier = 2.0;
        } else if (priority.equals("low")) {
            multiplier = 1.0;
        }

        // 更多条件判断……
        if (daysOverdue > 30) {
            multiplier *= 1.5;
        } else if (daysOverdue > 7) {
            multiplier *= 1.2;
        }

        return baseFee * daysOverdue * multiplier;
    }

    // 还有 200+ 行的其他方法……
}
```

小北越看越心虚。老潘指着屏幕："看出几个问题？"

"呃……代码太长了？"

"不只是长。"老潘逐一点出，"这里有四个明显的**代码坏味道**（Code Smells）：

**上帝类**——一个类承担了太多职责。它既要处理业务逻辑，又要直接操作数据库，还要管理缓存、发送通知。Week 02 学的单一职责原则被抛到了九霄云外。

**重复代码**——数据库连接的创建代码在每个方法里都重复出现。`DriverManager.getConnection(dbUrl)` 出现了多少次？如果数据库 URL 要改，得改多少处？

**长方法**——`createTask` 和 `calculateOverdueFee` 都太长了，做了太多事情。一个方法应该只做一件事，并且把它做好。

**紧耦合**——Service 层直接依赖 JDBC 细节。如果以后要换成 MySQL 或者 MongoDB，这个类得重写多少？

小北咽了口唾沫："那……怎么办？重写？"

"不用重写。"老潘说，"在 Week 06 你学会了写测试，那是安全网。现在你需要学会**重构**——在不改变行为的前提下改进设计。"

小北点点头。他意识到，写出让程序能跑的代码只是第一步。让代码能被**维护**——被自己维护、被队友维护、被三个月后的自己维护——才是工程师的真正功力。

---

## 2. 重构：在不改变行为的前提下改进设计

"重构？"小北问，"就是重写代码吗？"

"不完全是。"老潘说，"**重构**（Refactoring）是在不改变代码外在行为的前提下，改进其内部结构。用户看不出变化，但开发者能看出变化。"

阿码在旁边插话："那 AI 能不能自动重构？我听说 GitHub Copilot 能建议代码优化。"

"能，但有前提。"老潘说，"重构的黄金法则是：**先写测试，再改代码**。没有测试保护的重构，就像在高空走钢丝没有安全网。Week 06 学的单元测试，现在派上用场了。"

这呼应了 Week 06 学的单元测试。重构之前，你必须确保有一套可靠的测试，能够验证代码的行为在重构前后保持一致。

"那我怎么开始？"小北问。

"先给现在的代码写测试。"老潘说，"测试通过后，你才有胆子改代码。"

让我们看看如何为 `TaskService` 建立测试保护：

```java
// 文件：TaskServiceTest.java（重构前的测试保护）
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskServiceTest {
    private TaskService service;

    @BeforeEach
    void setUp() {
        service = new TaskService();
        // 使用内存数据库或测试数据库初始化
    }

    @Test
    void shouldCreateTaskWithValidInput() {
        assertDoesNotThrow(() -> {
            service.createTask("测试任务", "测试描述", "high");
        });
    }

    @Test
    void shouldThrowExceptionWhenTitleIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.createTask("", "描述", "high");
        });
    }

    @Test
    void shouldCalculateOverdueFeeForHighPriority() {
        // 先创建一个高优先级任务
        service.createTask("高优先级任务", "描述", "high");

        // 假设任务 ID 已知（实际测试需要调整）
        double fee = service.calculateOverdueFee("some-id", 10);

        // 高优先级基础费率 3.0，10 天逾期，基础费 10
        // 10 * 10 * 3.0 = 300，超过 7 天乘以 1.2
        assertTrue(fee > 0);
    }

    @Test
    void shouldCalculateOverdueFeeForLowPriority() {
        service.createTask("低优先级任务", "描述", "low");

        double fee = service.calculateOverdueFee("some-id", 5);

        // 低优先级基础费率 1.0
        assertTrue(fee > 0);
    }

    // 更多测试用例……
}
```

小北运行测试——全绿。他松了口气："现在可以重构了？"

"对。重构的流程是这样的："老潘在白板上画了一个循环：

```
1. 确保测试通过（绿灯）
2. 识别坏味道
3. 应用重构手法（小步前进）
4. 运行测试（确保还是绿灯）
5. 重复
```

"关键是**小步前进**。每次只做一个小改动，然后跑测试。如果测试失败，回滚——你刚才改的不多，很容易定位问题。"

小北想起 Week 03 学的防御式编程。重构也是一种防御——防御代码腐烂，防御技术债累积。

"那我从哪开始？"他问。

"最简单的开始：提取方法。把那个长长的 `calculateOverdueFee` 拆成小块。"

---

> **AI 时代小专栏：AI 辅助重构建议评估**
>
> 2025-2026 年，AI 重构工具已经从简单的代码补全进化为能自主执行任务的 Agent。GitHub Copilot 的 Agent Mode 可以接收 issue 并自动生成代码和 PR；Cursor 和 Claude Code 也具备类似的自主重构能力。这些工具能帮你完成变量重命名、提取方法、死代码消除等操作，甚至能处理跨文件的重构。
>
> 但别急着把方向盘完全交给 AI。2025 年的一项研究显示，GPT-4 在代码坏味道检测上的精确度只有 0.79，召回率更是低至 0.41——这意味着它可能漏掉近六成的坏味道，或者把正常代码误判为有问题。
>
> 面对 AI 的重构建议，建议你按这个框架评估：
> 1. **它是否改变了行为？** 重构的第一原则是不改变外在行为，AI 有时会"优化"时意外改变逻辑
> 2. **它是否降低了复杂度？** 用静态分析工具验证圈复杂度是否真的下降
> 3. **它是否通过了所有测试？** 重构后必须跑一遍测试套件
>
> 企业级实践强调"原子化转换"——每次重构的改动控制在 200 行以内，审查时间能减少 60%。AI 可以帮你生成这些小的、专注的 PR，但最终的合并按钮，必须是人按下的。
>
> 参考（访问日期：2026-02-12）：
> - https://github.com/orgs/community/discussions/186361
> - https://www.cs.ru.nl/bachelors-theses/2025/Robert_Blaauwendraad___1084960___Evaluating_Large_Language_Models_for_Code_Smell_Detection.pdf
> - https://www.byteable.ai/blog/top-ai-code-refactoring-tools-for-tackling-technical-debt-in-2026

---

## 3. 基础重构手法实战

小北深吸一口气，开始他的第一次重构。他选择最简单的**提取方法**（Extract Method）作为开始。

原来的 `calculateOverdueFee` 方法做了太多事：既要从数据库读取任务，又要计算费用，还要处理各种优先级和逾期天数的条件。这违反了单一职责原则。

```java
// 重构前：一个方法做了太多事
public double calculateOverdueFee(String taskId, int daysOverdue) {
    // 1. 数据库查询（重复代码！）
    Task task = null;
    try (Connection conn = DriverManager.getConnection(dbUrl);
         PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tasks WHERE id = ?")) {
        pstmt.setString(1, taskId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            task = new Task(rs.getString("id"), rs.getString("title"),
                          rs.getString("description"), rs.getString("priority"),
                          rs.getString("status"));
        }
    } catch (SQLException e) {
        throw new RuntimeException("查询失败", e);
    }

    if (task == null) return 0.0;

    // 2. 费用计算（应该独立出来）
    double baseFee = 10.0;
    String priority = task.getPriority();
    double multiplier = 1.0;

    if (priority.equals("high")) {
        multiplier = 3.0;
    } else if (priority.equals("medium")) {
        multiplier = 2.0;
    } else if (priority.equals("low")) {
        multiplier = 1.0;
    }

    if (daysOverdue > 30) {
        multiplier *= 1.5;
    } else if (daysOverdue > 7) {
        multiplier *= 1.2;
    }

    return baseFee * daysOverdue * multiplier;
}
```

第一步，把费用计算逻辑提取出来：

```java
// 重构后：提取方法
public double calculateOverdueFee(String taskId, int daysOverdue) {
    Task task = findTaskById(taskId);  // 提取查询逻辑
    if (task == null) return 0.0;

    return calculateFee(task.getPriority(), daysOverdue);  // 提取计算逻辑
}

// 新提取的方法：查询任务
private Task findTaskById(String taskId) {
    try (Connection conn = DriverManager.getConnection(dbUrl);
         PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM tasks WHERE id = ?")) {
        pstmt.setString(1, taskId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Task(rs.getString("id"), rs.getString("title"),
                          rs.getString("description"), rs.getString("priority"),
                          rs.getString("status"));
        }
    } catch (SQLException e) {
        throw new RuntimeException("查询失败", e);
    }
    return null;
}

// 新提取的方法：计算费用
private double calculateFee(String priority, int daysOverdue) {
    double baseFee = 10.0;
    double multiplier = getPriorityMultiplier(priority);
    multiplier = applyOverdueMultiplier(multiplier, daysOverdue);
    return baseFee * daysOverdue * multiplier;
}

// 再提取：获取优先级倍率
private double getPriorityMultiplier(String priority) {
    return switch (priority) {
        case "high" -> 3.0;
        case "medium" -> 2.0;
        case "low" -> 1.0;
        default -> 1.0;
    };
}

// 再提取：应用逾期倍率
private double applyOverdueMultiplier(double multiplier, int daysOverdue) {
    if (daysOverdue > 30) {
        return multiplier * 1.5;
    } else if (daysOverdue > 7) {
        return multiplier * 1.2;
    }
    return multiplier;
}
```

小北跑了一遍测试——全绿。他松了口气："还好没改坏……"

"你看，"老潘说，"现在的代码读起来像散文。`calculateOverdueFee` 方法只有三行，一眼就能看出它在做什么：查找任务，然后计算费用。"

"而且我用 Java 14 的 `switch` 表达式替代了冗长的 `if-else` 链，"小北补充道，"代码简洁多了。"

"别急着庆祝，"老潘说，"还有更大的问题——重复代码。Week 07 学过的 `try-with-resources` 在每个方法里都重复出现，数据库连接逻辑散落在各处。"

"那怎么办？"

"**移动方法**（Move Method）——把数据库操作移到专门的 Repository 类。这也是回顾 Week 07 的 Repository 模式。"

```java
// 文件：JdbcTaskRepository.java（Week 07 的 Repository 模式）
public class JdbcTaskRepository implements TaskRepository {
    private final String url;

    public JdbcTaskRepository(String url) {
        this.url = url;
    }

    @Override
    public Optional<Task> findById(String id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapToTask(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询任务失败", e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Task task) {
        String sql = """
            INSERT INTO tasks (id, title, description, priority, status)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
                title = excluded.title,
                description = excluded.description,
                priority = excluded.priority,
                status = excluded.status
            """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getPriority());
            pstmt.setString(5, task.getStatus());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("保存任务失败", e);
        }
    }

    private Task mapToTask(ResultSet rs) throws SQLException {
        return new Task(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("priority"),
            rs.getString("status")
        );
    }
}
```

现在 `TaskService` 可以专注于业务逻辑，把数据访问委托给 Repository：

```java
// 重构后的 TaskService：只负责业务逻辑
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public double calculateOverdueFee(String taskId, int daysOverdue) {
        Task task = taskRepository.findById(taskId)
            .orElse(null);
        if (task == null) return 0.0;

        return calculateFee(task.getPriority(), daysOverdue);
    }

    // 其他业务方法……
}
```

阿码在旁边看着，突然问："这些重构手法 AI 能自动做吗？"

"部分可以。"老潘说，"现代 IDE（IntelliJ IDEA、VS Code）都有重构快捷键，提取方法、重命名变量、移动方法都能一键完成。AI 辅助工具也能建议重构方案。但关键是——**你要知道为什么要重构，以及选择哪种手法**。"

小北点点头。他想起 Week 02 学的开闭原则（Open/Closed Principle）。现在的设计让 `TaskService` 对扩展开放（可以换不同的 Repository 实现），对修改关闭（业务逻辑不需要因为存储方式改变而修改）。

重构不是目的，而是手段。目的是让代码更容易理解、更容易修改、更少 bug。

---

## 4. 设计模式：策略模式与工厂模式

经过几轮重构，`TaskService` 已经瘦了不少。但还有一个问题——费用计算逻辑里有一堆条件判断，看着就头大：

```java
private double getPriorityMultiplier(String priority) {
    return switch (priority) {
        case "high" -> 3.0;
        case "medium" -> 2.0;
        case "low" -> 1.0;
        default -> 1.0;
    };
}
```

如果以后要加新的优先级类型，或者不同的计费规则（比如学生优惠、会员折扣），这个 `switch` 语句会像滚雪球一样越来越长。

"这就是**策略模式**（Strategy Pattern）的用武之地。"老潘说，"把不同的算法封装成独立的类，让它们可以互相替换。这样加新规则时，只需要添加新类，不用改现有代码。"

策略模式的核心思想是：**定义一系列算法，把它们一个个封装起来，并且使它们可以互相替换**。

阿码凑过来看："这不就是把 switch 拆开吗？"

"不只是拆开。"老潘说，"关键是每个策略都是独立的、可替换的。加新策略不用改现有代码——这就是 Week 02 说的开闭原则。"

首先，定义一个策略接口：

```java
// 文件：FeeCalculationStrategy.java
public interface FeeCalculationStrategy {
    double calculateFee(int daysOverdue);
    boolean supports(String priority);
}
```

然后，为每种优先级实现具体的策略：

```java
// 文件：HighPriorityFeeStrategy.java
public class HighPriorityFeeStrategy implements FeeCalculationStrategy {
    @Override
    public double calculateFee(int daysOverdue) {
        double baseFee = 10.0;
        double multiplier = 3.0;
        multiplier = applyOverdueMultiplier(multiplier, daysOverdue);
        return baseFee * daysOverdue * multiplier;
    }

    @Override
    public boolean supports(String priority) {
        return "high".equals(priority);
    }

    private double applyOverdueMultiplier(double multiplier, int daysOverdue) {
        if (daysOverdue > 30) {
            return multiplier * 1.5;
        } else if (daysOverdue > 7) {
            return multiplier * 1.2;
        }
        return multiplier;
    }
}

// 其他策略类结构类似，这里省略完整实现
// MediumPriorityFeeStrategy: multiplier = 2.0
// LowPriorityFeeStrategy: multiplier = 1.0
```

现在，`TaskService` 不再需要知道具体的计算逻辑：

```java
// 重构后的 TaskService 使用策略模式
public class TaskService {
    private final TaskRepository taskRepository;
    private final List<FeeCalculationStrategy> feeStrategies;

    public TaskService(TaskRepository taskRepository, List<FeeCalculationStrategy> feeStrategies) {
        this.taskRepository = taskRepository;
        this.feeStrategies = feeStrategies;
    }

    public double calculateOverdueFee(String taskId, int daysOverdue) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("任务不存在: " + taskId));

        FeeCalculationStrategy strategy = feeStrategies.stream()
            .filter(s -> s.supports(task.getPriority()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("不支持的优先级: " + task.getPriority()));

        return strategy.calculateFee(daysOverdue);
    }
}
```

小北看着代码，突然问："每个策略类都要写这么多吗？感觉结构都差不多。"

"其实结构确实差不多，"老潘说，"关键是每个都独立，符合单一职责。你可以让 AI 生成模板，但核心逻辑得自己填。"

阿码在旁边插话："这样确实清爽多了。但如果我要加一个新策略，还得手动创建实例然后传给 `TaskService`？每次都要改客户端代码？"

"好问题。这时候可以配合**工厂模式**（Factory Pattern）来创建策略对象。"老潘说，"工厂把对象创建的复杂性封装起来，客户端只需要说'我要一个策略'，不用关心具体怎么创建。"

```java
// 文件：FeeStrategyFactory.java
public class FeeStrategyFactory {
    public static List<FeeCalculationStrategy> createDefaultStrategies() {
        return List.of(
            new HighPriorityFeeStrategy(),
            new MediumPriorityFeeStrategy(),
            new LowPriorityFeeStrategy()
        );
    }

    public static FeeCalculationStrategy createStrategy(String priority) {
        return switch (priority) {
            case "high" -> new HighPriorityFeeStrategy();
            case "medium" -> new MediumPriorityFeeStrategy();
            case "low" -> new LowPriorityFeeStrategy();
            default -> throw new IllegalArgumentException("不支持的优先级: " + priority);
        };
    }
}
```

使用工厂后，客户端代码变得更简单：

```java
// 客户端代码
TaskRepository repository = new JdbcTaskRepository("jdbc:sqlite:campusflow.db");
List<FeeCalculationStrategy> strategies = FeeStrategyFactory.createDefaultStrategies();
TaskService service = new TaskService(repository, strategies);
```

"策略模式的好处是什么？"小北问。

"三点："老潘伸出手指，"第一，**消除条件判断**——不再有一长串 if-else 或 switch。第二，**易于扩展**——加新策略只需实现接口，不用改现有代码。第三，**单一职责**——每个策略类只负责一种计算逻辑。"

这正是 Week 02 学过的开闭原则的体现：对扩展开放（可以添加新策略），对修改关闭（不需要修改 `TaskService`）。

阿码突然说："我试试让 AI 生成策略模式的代码。"

几分钟后，阿码拿着 AI 生成的代码回来："它生成了，但……"

"但什么？"

"它把所有的策略逻辑放在一个类里，用 if-else 区分。这不对吧？"

老潘看了一眼："AI 理解了'策略'这个词，但没理解策略模式的真正意图。它给的是'策略'的字面实现，不是设计模式的意图实现。"

"所以 AI 也会犯错？"

"当然。AI 是基于统计模式生成代码，它不知道你的设计意图。**理解设计模式的'为什么'比记住'怎么做'更重要**。"

---

> **AI 时代小专栏：AI 时代的设计模式**
>
> 阿码用 AI 生成策略模式代码却得到一坨 if-else——这不是个例。2025 年的研究表明，LLM 虽然能"说出"设计模式的名字，却不一定能准确理解其设计意图。
>
> 好消息是，研究人员正在解决这个问题。MaintainCoder 等多智能体系统专门处理设计模式选择，相比基线模型可提升代码可维护性指标 14-30%。"设计模式注入"（Design-Pattern Injection）技术结合显式模式库与生成模型，让 AI 生成的代码能遵循经过验证的模板。UML Forge 项目甚至展示了 LLM 可以作为"自适应编译器"，接收 UML 设计作为输入，生成符合设计模式的 Java 代码。
>
> 但即便如此，人机协作循环（Human-in-the-Loop）仍能将设计模式应用的成功率提高 35%。为什么？因为设计模式的精髓不在于"类怎么组织"，而在于"为什么这样组织"。
>
> AI 擅长的是：根据需求自动选择合适的设计模式、通过 RAG 框架确保实现一致性、将 UML 图转换为模式化的代码骨架。
>
> AI 不擅长的是：理解业务上下文中"何时该打破模式"、判断某个模式是否过度设计、识别模式应用中的"代码异味"。
>
> 所以，当你让 AI 帮你实现策略模式时，别忘了问它："为什么这里用策略模式而不是简单的 switch 语句？"如果它回答不出个所以然，那你可能得到了一个"看起来对"但"想错了"的实现——就像阿码遇到的那样。
>
> 参考（访问日期：2026-02-12）：
> - https://ceur-ws.org/Vol-4122/paper16.pdf
> - https://kaynes.com/top-llms-for-coding/
> - https://www.softwareseni.com/spec-driven-development-in-2025-the-complete-guide-to-using-ai-to-write-production-code

---

## 5. 静态代码分析：让工具帮你找问题

重构完成后，小北想看看代码质量到底提升了多少。老潘推荐他使用**静态代码分析**（Static Analysis）工具。

"静态分析就是在不运行代码的情况下，分析代码的结构、找出潜在问题。"老潘说，"SpotBugs 和 PMD 是 Java 社区最常用的两个工具。"

在 `pom.xml` 中添加 SpotBugs 插件：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.spotbugs</groupId>
            <artifactId>spotbugs-maven-plugin</artifactId>
            <version>4.8.3.0</version>
            <configuration>
                <effort>Max</effort>
                <threshold>Low</threshold>
            </configuration>
        </plugin>
    </plugins>
</build>
```

运行分析：

```bash
mvn spotbugs:check
```

SpotBugs 会扫描字节码，找出常见的 bug 模式，比如：
- 空指针 dereference
- 资源泄漏
- 死锁风险
- 不正确的 equals/hashCode 实现

PMD 则更关注代码风格和潜在问题：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-pmd-plugin</artifactId>
    <version>3.21.2</version>
    <configuration>
        <rulesets>
            <ruleset>/rulesets/java/basic.xml</ruleset>
            <ruleset>/rulesets/java/design.xml</ruleset>
        </rulesets>
    </configuration>
</plugin>
```

"除了找 bug，静态分析还能计算**圈复杂度**（Cyclomatic Complexity）。"老潘说，"这是一个度量指标，表示代码路径的复杂程度。"

圈复杂度的计算公式很简单：**1 + 条件语句的数量**。一个方法的圈复杂度越高，测试和维护的难度就越大。

```java
// 圈复杂度 = 1（基础）+ 3（if语句）= 4
public void example(int a, int b, int c) {
    if (a > 0) {           // +1
        doSomething();
    }
    if (b > 0) {           // +1
        doSomethingElse();
    }
    if (c > 0) {           // +1
        doAnotherThing();
    }
}

// 圈复杂度 = 1 + 4 = 5（switch 每个 case 算一个分支）
public int getPriorityValue(String priority) {
    return switch (priority) {  // +1
        case "high" -> 3;       // +1
        case "medium" -> 2;     // +1
        case "low" -> 1;        // +1
        default -> 0;           // +1
    };
}
```

业界通常建议：方法的圈复杂度应该控制在 10 以下，最好不超过 5。

小北运行了分析工具，对比了重构前后的报告：

```
重构前 TaskService：
- 圈复杂度：47（严重超标）
- 代码行数：312 行
- SpotBugs 警告：3 个（潜在空指针、资源泄漏）

重构后 TaskService：
- 圈复杂度：8（良好）
- 代码行数：45 行
- SpotBugs 警告：0 个
```

"数据不会骗人。"老潘说，"重构的效果是客观的。"

"在公司里，静态分析是 CI/CD 流程的一部分。"老潘补充道，"代码合入前必须通过静态分析门禁，圈复杂度超标或发现高危 bug 模式都会阻止合并。"

小北想起 Week 06 学的测试门禁。静态分析是另一道质量防线——它不需要运行代码，就能发现潜在问题。

---

## 6. 技术债管理与可持续开发

重构完成后，小北感觉轻松多了。但他也意识到一个问题："为什么代码会变成这样？我一开始写得挺好的啊。"

"那我们欠了多少技术债？"小北问老潘，"有没有个账本？"

"好问题。"老潘说，"技术债就像信用卡账单，你得知道欠了多少，利息是多少。"

"这就是**技术债**（Technical Debt）。"老潘说，"Week 02 提过这个概念，现在我们来深入聊聊。"

技术债不是坏事——它是权衡的结果。为了赶 deadline，你可能会选择快速但粗糙的实现；为了验证想法，你可能会写临时代码。这些都是合理的决策。

问题在于：**你借了债，有没有还的计划？**

```
技术债 = 快速交付的短期收益 - 长期维护成本
```

技术债分几种类型：

**故意借的债**——"我知道这不好，但我们需要这周上线。下周重构。"这种债是战略性的，关键是要有还款计划。

**不小心借的债**——"我不知道这样写会有问题。"缺乏经验导致的，随着学习会逐渐减少。

**腐烂的债**——代码随着时间推移自然老化，原来的好设计不再适应新需求。就像再新鲜的水果放久了也会烂。

"CampusFlow 里有哪些技术债？"老潘问。

小北想了想：
- "我们用 SQLite，但以后可能要迁移到 PostgreSQL。"
- "费用计算的硬编码规则需要改成可配置的。"
- "CLI 界面以后要换成 Web 界面。"

"好。那你打算什么时候还这些债？"

"呃……"

"这就是问题所在。"老潘说，"技术债需要管理。我们团队的做法是："

1. **记录技术债**——在代码里用 TODO/FIXME 标记，或者在项目管理工具里建卡
2. **评估影响**——这债不还，会有什么后果？
3. **制定计划**——每个迭代预留 20% 时间还债
4. **重构时机**——修改代码时，顺手改进（童子军规则：离开营地时要比来时更干净）

阿码在旁边问："能不能借新还旧？比如先欠着，等以后有空再重构？"

"这叫'技术债展期'，利息会滚起来的。"老潘摇摇头。

"那什么时候该重构，什么时候该忍着？"阿码追问。

"Martin Fowler 有个'法则'："老潘说，"**三次法则**——第一次做某件事时，直接做；第二次做类似的事时，虽然会重复，但还是直接做；第三次再做类似的事时，就该重构了。"

"另外，重构的最佳时机是：**添加新功能之前**。先让代码对新功能友好，再添加功能。"

小北点点头。他意识到，重构不是一次性的"大扫除"，而是持续的过程。就像打扫房间——每天花 10 分钟整理，比一个月来一次彻底打扫要轻松得多。

"还有，"老潘补充道，"重构必须配合版本控制。每个重构手法单独提交，写清楚提交信息：`refactor: extract method calculateOverdueFee()`。这样出问题可以随时回滚。"

---

## CampusFlow 进度：ADR-004 架构演进决策

经过本周的重构，CampusFlow 的代码质量有了显著提升。现在，作为本周的首席架构师，你需要编写 ADR-004，记录这次架构演进决策。

```markdown
# ADR-004: 引入策略模式优化费用计算逻辑

## 状态
已接受（2026-02-12）

## 背景
随着 CampusFlow 功能增加，TaskService 类逐渐膨胀到 300+ 行，承担了过多职责：
- 任务 CRUD 操作
- 费用计算逻辑（包含复杂的条件判断）
- 直接操作数据库（JDBC 代码）

这违反了单一职责原则（SRP），导致：
1. 代码难以理解和维护
2. 修改费用计算规则时可能影响其他功能
3. 单元测试困难（需要模拟数据库）

## 决策
我们决定：
1. 将数据访问逻辑提取到 Repository 层（已完成于 Week 07）
2. 使用策略模式（Strategy Pattern）重构费用计算逻辑
3. 引入工厂模式（Factory Pattern）管理策略对象的创建
4. 集成 SpotBugs 静态分析工具作为质量门禁

## 后果

### 正面
- TaskService 从 312 行减少到 45 行，圈复杂度从 47 降至 8
- 费用计算逻辑现在独立于业务逻辑，易于扩展新规则
- 代码通过所有静态分析检查，无 SpotBugs 警告
- 单元测试覆盖率保持在 80% 以上

### 负面
- 引入了新的接口和类，增加了项目的文件数量
- 团队需要学习策略模式的概念
- 短期内增加了开发工作量

## 替代方案

### 替代方案 1：保持现状
- 优点：无需改动
- 缺点：技术债持续累积，维护成本越来越高
- 结论：拒绝

### 替代方案 2：使用 if-else 链 + 配置表
- 优点：简单直接
- 缺点：仍然需要修改代码来添加新规则，不符合开闭原则
- 结论：拒绝

## 参考
- Week 02: SOLID 原则与单一职责
- Week 08: 策略模式与重构手法
- 《重构：改善既有代码的设计》Martin Fowler
```

老潘看了这份 ADR，点点头："写得不错。ADR 的关键不是写得多漂亮，而是**记录决策的上下文和权衡**。三个月后，当别人（或你自己）问'为什么当初这么设计'时，ADR 能给出答案。"

"这也是 Week 02 学的——架构决策必须被记录和审查。"

小北看着重构后的代码，感到一种久违的清爽。代码还是那些代码，但结构清晰了，职责分明了，未来的修改也会变得容易。

工具能帮我们发现问题，但解决问题的意愿和计划，就涉及到技术债管理的艺术了。

---

## Git 本周要点

重构提交有一些特别的讲究：

**原子性提交**：每个重构手法单独提交。不要把"提取方法"和"重命名变量"混在一个提交里，出问题不好回滚。

**提交信息规范**：用 `refactor:` 前缀，描述清楚做了什么。例如：
- `refactor: extract method calculateOverdueFee()`
- `refactor: move database logic to JdbcTaskRepository`
- `refactor: introduce FeeCalculationStrategy interface`

**重构前后对比**：`git diff` 是重构的好伙伴。改动太大？拆成几次提交。改动不清晰？可能是重构手法选得不对。

**安全网**：重构前确保所有测试通过并提交；重构后测试通过再提交。这样 `git bisect` 能在出问题的时候快速定位。

---

## 本周小结（供下周参考）

本周你学会了让代码"活得更久"的技能。

从识别代码坏味道开始，你学会了用"上帝类"、"重复代码"、"长方法"这些术语描述代码的问题。然后你实践了重构手法——提取方法、移动方法、引入策略模式——在不改变行为的前提下改进设计。静态分析工具给了你一个客观的度量：圈复杂度从 47 降到 8，代码行数从 312 行减到 45 行，这些数字证明了重构的价值。

更重要的是，你理解了技术债的本质。它不是罪恶，而是权衡——关键是要有还款计划。ADR-004 记录了你的架构决策，让三个月后的你能理解今天为什么选择策略模式。

小北的 TaskService 从一锅"煮过头的粥"变成了职责清晰的结构。阿码尝试了用 AI 生成策略模式代码，也学会了批判性地评估 AI 建议。老潘的"三次法则"和"童子军规则"会成为你日后工作的指南。

但 CLI 版本的 CampusFlow 已经走到了尽头。下周，我们将进入 AI 时代工程阶段——学习 REST API 设计，把你的应用变成真正的 Web 服务。

---

## Definition of Done（学生自测清单）

- [ ] 我能识别至少 3 种代码坏味道（上帝类、重复代码、长方法）
- [ ] 我能应用提取方法、移动方法等基础重构手法改进代码
- [ ] 我能解释策略模式的适用场景，并在代码中应用
- [ ] 我能使用 SpotBugs 或 PMD 对代码进行静态分析
- [ ] 我能解释圈复杂度的含义，并知道如何降低它
- [ ] 我能解释技术债的概念，识别项目中的技术债
- [ ] 我完成了 CampusFlow 的代码重构，测试全部通过
- [ ] 我编写了 ADR-004，记录了架构演进决策
