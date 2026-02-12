# week_02：类设计与领域建模

> "设计一个类，就是在设计一个小世界。"
> — Grady Booch

2026 年初，GitHub Copilot 用户数突破 2000 万，AI 编程工具已被 80% 以上的开发者纳入日常工作流程。但一个令人担忧的趋势正在浮现：Forrester 预测，到 2026 年，**75% 的技术领导者将面临中度到严重的技术债**——比 2025 年的 50% 飙升 25 个百分点。

更关键的是，2025 年的代码质量研究显示了一个悖论：**53% 的开发者报告 AI 工具增加了他们的技术债**，而非减少。AI 生成的代码"看起来能跑，但缺乏架构判断"——这是 Ox Security 研究报告的结论。MIT 研究者甚至将 AI 代码称为"**高息债务**"：它提供即时的开发速度，但以未来维护负担的复利作为代价。

这引出了 Week 02 的核心问题：**AI 能帮你快速写出一个类，但它能帮你理解"什么是好的类设计"吗？**

答案是：比以往任何时候都更需要。类是 Java 程序的基本抽象单元，是你理解问题域的工具。如果只让 AI 生成而你自己不理解，你会得到一堆"语法正确但设计混乱"的碎片代码。本周你将学习如何从问题域中识别类、如何设计封装良好的类、如何用 SOLID 原则评估设计质量——并写出第一份 ADR（架构决策记录）。

---

## 学习目标

完成本周学习后，你将能够：
1. 使用"名词提取法"从需求文档中识别核心类
2. 设计封装良好的类（private 字段 + public 方法）
3. 理解 SOLID 原则的基本思想（单一职责、开闭原则）
4. 撰写第一份 ADR（领域模型设计决策）
5. 为 CampusFlow 项目设计核心领域模型

> **Definition of Done (DoD)**：完成本章学习的标准包括实现一个符合 SOLID 原则的任务管理系统、编写 ADR-001 文档、并通过代码审查验证设计质量。

<!--
贯穿案例：任务管理器（TaskManager）
- 第 1 节：从一个"什么都能做但很乱"的单一类开始，发现职责混乱的问题
- 第 2 节：识别核心类（Task、TaskManager、User），理解名词提取法
- 第 3 节：封装数据（private 字段 + getter/setter），理解为什么要隐藏数据
- 第 4 节：应用单一职责原则，拆分"数据类"和"管理类"
- 第 5 节：写 ADR-001 记录设计决策，理解概念完整性

最终成果：一个有清晰职责划分的 TaskManager 系统，包含 Task 类（数据）、TaskManager 类（管理）、TaskValidator 类（验证），以及对应的 ADR-001 文档

认知负荷预算：
- 本周新概念（5 个，预算上限 6 个）：
  1. 类定义（class definition）- 从 Week 01 的 UserInfo 延伸，系统化讲解
  2. 封装（encapsulation）- private/public 访问控制的意义
  3. SOLID 原则（SOLID principles）- 重点讲单一职责和开闭原则
  4. 领域模型（domain model）- 如何从问题域抽象出类
  5. ADR（Architecture Decision Record）- 架构决策记录方法
- 结论：✅ 在预算内

回顾桥设计（至少 2 个）：
- [静态类型]（来自 week_01）：在第 2 节，通过"类的字段类型声明"再次使用静态类型思维
- [变量声明]（来自 week_01）：在第 3 节，通过"类的字段声明"强化变量声明的理解
- [Scanner 输入]（来自 week_01）：在第 5 节的交互式菜单中再次使用

CampusFlow 本周推进：
- 上周状态：团队组建 + 选题确定 + Git 仓库初始化
- 本周改进：设计核心领域模型（根据选题确定核心类如 Task/Note/Book），写出第一份 ADR
- 涉及的本周概念：领域模型、类定义、封装、ADR
- 建议落盘位置：docs/adr/001-领域模型设计.md + src/main/java/edu/campusflow/domain/

角色出场规划：
- 小北：在第 1 节中写了一个"什么都能做的 God Class"，引出职责混乱的问题
- 阿码：在第 3 节中追问"为什么要 private，不能全用 public 吗"，引出封装的意义
- 老潘：在第 4 节中对比"AI 生成的代码 vs 有设计思想的代码"，从工程角度解释单一职责原则

AI 小专栏 #1（放在第 2 节之后）：
- 主题：AI 能生成类代码，但不能做设计决策
- 连接点：呼应第 2 节的"名词提取法"——AI 可以根据你描述的类生成代码，但它不会帮你思考"这个类应该有哪些职责"
- 建议搜索词："AI code generation class design 2026"、"AI generated code technical debt 2025"

AI 小专栏 #2（放在第 4 节之后）：
- 主题：SOLID 原则在 AI 时代仍然重要
- 连接点：呼应第 4 节的"单一职责原则"——AI 生成的代码往往违反 SOLID，你需要有能力审查和改进
- 建议搜索词："SOLID principles AI code review 2026"、"AI generated code quality 2025"
-->

---

## 前情提要

上周你学会了 Java 的基本语法——静态类型声明、变量定义、Scanner 输入，并用它们写出了一个可运行的个人名片生成器。你可能觉得 Java"很严格"：必须先声明类型、必须用 getter 方法访问数据、编译器会在你运行前就报错。

但这也是 Java 的价值所在——它把错误从"运行时崩溃"提前到了"编译期拦截"。

本周我们进入类设计的核心。你会看到：**类不只是"代码的组织方式"，而是你理解问题域的工具**。一个设计良好的类，能帮你把复杂的现实问题抽象成清晰的模型；一个设计糟糕的类，会变成"上帝类"——什么都能做，但什么都做不好。

---

## 1. 一个"什么都能做"的类——为什么会出问题

小北刚开始做任务管理器时，写了一个"功能齐全"的 `Task` 类。他觉得把所有相关功能都放在一起会很方便——创建、验证、保存、显示、邮件通知，统统塞进一个类里。

```java
// 小北的代码（有问题）
public class Task {
    // 数据字段
    public String title;
    public String description;
    public boolean completed;
    public String priority;  // "高"、"中"、"低"

    // 数据验证
    public boolean isValid() {
        if (title == null || title.trim().isEmpty()) return false;
        if (priority == null ||
            (!priority.equals("高") && !priority.equals("中") && !priority.equals("低"))) {
            return false;
        }
        return true;
    }

    // 文件读写
    public void saveToFile(String filename) {
        // ... 写文件的代码
    }

    public void loadFromFile(String filename) {
        // ... 读文件的代码
    }

    // 显示输出
    public void print() {
        System.out.println("任务：" + title);
        System.out.println("状态：" + (completed ? "已完成" : "进行中"));
    }

    // 邮件通知
    public void sendEmailNotification(String emailAddress) {
        // ... 发邮件的代码
    }

    // ... 还有 20 多个方法
}
```

"这不挺好的吗？"小北说，"所有功能都在一个类里，想找什么很容易。"

老潘看了代码，眉头紧锁："你现在觉得挺好，三个月后再看——这个类有 500 行、30 个方法。你想加一个新功能，都不知道该插在哪个位置。"

### 什么是"上帝类"（God Class）

**上帝类**是一个反模式：一个类承担了太多职责，像个"什么都管"的上帝。它看起来方便——所有东西都在一个地方——但维护成本极高。

| 特征 | 好的类设计 | 上帝类 |
|------|----------|--------|
| 职责数量 | 1-2 个相关职责 | 5+ 个不相关职责 |
| 方法数量 | 5-10 个 | 20+ 个 |
| 代码行数 | 100-200 行 | 500+ 行 |
| 可测试性 | 容易（职责清晰） | 困难（依赖太多） |
| 可维护性 | 高（改一个功能只影响一个地方） | 低（改一个功能可能影响多个地方） |

### 职责过多的代价

阿码不理解："职责多了有什么坏处？程序能跑啊。"

老潘用一个场景回答了他：

> 假设你想改"任务优先级"的存储方式——从"高/中/低"改成数字（1/2/3）。
>
> 如果你的设计是"上帝类"：
> - 你需要在 `Task` 类里改 5 个地方（验证、保存、加载、显示、通知）
> - 你可能在其他类里也有直接访问 `priority` 字段的代码
> - 你改完要测试 20 个方法才能确保没改坏
>
> 如果你的设计是"职责分离"：
> - `Task` 只负责存储数据（改一个字段声明）
> - `TaskValidator` 负责验证（改一个方法）
> - `TaskRepository` 负责保存（改一个方法）
> - 其他类不直接访问 `priority`，而是通过 getter
> - 你改完只需要测试 3 个类

"这就是**单一职责原则**的核心思想，"老潘说，"**一个类应该只有一个引起它变化的原因。**"

还记得上周我们写的 `UserInfo` 类吗？回头看那个类，你会发现它其实已经很好地体现了"职责分离"：

```java
// Week 01 的 UserInfo 类（✅ 好的设计）
class UserInfo {
    private final String name;
    private final String jobTitle;
    private final String email;
    private final int age;
    private final double yearsOfExp;

    public UserInfo(String name, String jobTitle, String email,
                    int age, double yearsOfExp) {
        this.name = name;
        this.jobTitle = jobTitle;
        this.email = email;
        this.age = age;
        this.yearsOfExp = yearsOfExp;
    }

    // 只提供 getter，没有 setter（不可变对象）
    public String getName() { return name; }
    public String getJobTitle() { return jobTitle; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
    public double getYearsOfExp() { return yearsOfExp; }
}
```

看，`UserInfo` 只负责存储数据——它不负责验证、不负责显示、不负责持久化。这些职责被分离到了其他类：`BusinessCardGenerator` 负责显示，验证逻辑应该在专门的 `Validator` 类里（虽然 Week 01 我们没写，但思路是清晰的）。

"所以设计良好的类不是'代码更少'，是'职责更清晰'。"老潘说，"我们先把小北的代码'拆开'——但怎么拆？这需要先理解'什么是类'。"

老潘停顿了一下，补充道："但光理解还不够。**你得把设计决策记下来**——否则三个月后你接手自己的代码，会不知道当初为什么这样设计。这就是我们本周要学的 ADR（架构决策记录）。先记住这个词，第 5 节会详细讲。"

---

## 2. 类是抽象的工具——从问题域识别类

阿码问："类是凭空想出来的吗？我怎么知道需要哪些类？"

"类不是你想出来的，是从问题域里**识别**出来的。"老潘打开白板。

### 名词提取法

**名词提取法**是识别类的第一步：

> **步骤 1：写需求文档**
>
> TaskManager 的核心需求：
> - 用户可以创建任务（Task）
> - 每个任务有标题、描述、截止日期、优先级、完成状态
> - 任务可以被标记为已完成
> - 任务可以按优先级排序
> - 系统可以过滤出已过期的任务

"等等，"小北打断，"这些需求我已经写好了，但我不知道怎么转成代码。"

"继续看，"老潘说。

> **步骤 2：圈出名词**
>
> 用户（User）、任务（Task）、标题、描述、截止日期、优先级、完成状态、系统

> **步骤 3：区分核心名词和属性**
>
> | 名词 | 类型 | 理由 |
> |------|------|------|
> | Task | **核心类** | 有独立存在价值，有属性和行为 |
> | User | **核心类** | 有独立存在价值（虽然 Week 02 暂不实现） |
> | 标题 | Task 的属性 | 描述 Task 的一个特征 |
> | 描述 | Task 的属性 | 描述 Task 的一个特征 |
> | 截止日期 | Task 的属性 | 描述 Task 的一个特征 |
> | 优先级 | Task 的属性 | 描述 Task 的一个特征 |
> | 完成状态 | Task 的属性 | 描述 Task 的一个特征 |
> | 系统 | **不是类** | 是"TaskManager"的通俗说法 |

> **步骤 4：识别"管理者"类**
>
> 有些类不存储数据，而是"管理"其他类。比如：
> - `TaskManager`：管理任务的增删改查
> - `TaskValidator`：验证任务数据是否合法
> - `TaskRepository`：负责任务的持久化（存储）

小北想起来了："这就像上周的 `UserInfo` 和 `BusinessCardGenerator`！"

"对！"老潘说，"`UserInfo` 是数据类，`BusinessCardGenerator` 是管理类（负责显示）。它们的职责分得很清楚。"

### 类与对象的关系

上周你见过 `UserInfo` 类了，但我们没有系统地讲"类"和"对象"的区别。

**类**是模板，**对象**是实例。

| 类 | 对象 |
|----|----|
| `Task`（模板） | `task1`、`task2`、`task3`（具体实例） |
| "人类"的基因 | "张三"、"李四"（具体的人） |
| 一次可复用 | 可以创建无数个对象 |

还记得 Week 01 的**静态类型**思维吗？在声明变量时，你必须告诉编译器变量的类型。创建对象时也是如此：

```java
// 定义类（模板）
public class Task {
    private String title;
    private boolean completed;

    public Task(String title) {
        this.title = title;  // this.title 指向当前对象的 title 字段
        this.completed = false;
    }

    public String getTitle() {
        return title;  // 相当于 this.title
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }
}

// 创建对象（实例）
Task task1 = new Task("写作业");  // 第一个 Task 对象
Task task2 = new Task("复习Java"); // 第二个 Task 对象
Task task3 = new Task("做项目");   // 第三个 Task 对象

// 每个对象有自己独立的 title 和 completed
task1.markCompleted();  // task1 完成了
// task2.completed 仍然是 false
// task3.completed 仍然是 false
```

看，`task1.markCompleted()` 只影响 `task1` 的状态，不会影响 `task2` 和 `task3`。这就是对象的价值——**每个对象有自己独立的状态**。

### 实体类 vs 服务类

从需求中识别的类可以分为两类：

**1. 实体类（Entity Class）**：存储数据，有状态
- 代表问题域中的"事物"
- 例如：`Task`、`User`、`Project`、`Note`
- 特征：有字段（属性）、有 getter/setter

**2. 服务类（Service Class）**：提供行为，无状态
- 代表"动作"或"管理器"
- 例如：`TaskManager`、`TaskValidator`、`EmailService`
- 特征：主要是方法，字段很少

| 类型 | 示例 | 字段 | 方法 |
|------|------|------|------|
| 实体类 | `Task` | title, completed, priority | getTitle(), markCompleted() |
| 服务类 | `TaskManager` | 可能有一个 Task 列表 | addTask(), filterByPriority() |

阿码举手了："那 `TaskManager` 为什么不直接把任务存成 `ArrayList`？为什么要单独一个类？"

"好问题。"老潘说，"这就是**'封装变化'**。如果你直接用 `ArrayList`，将来你想换存储方式（比如换成数据库），所有用到 `ArrayList` 的地方都要改。但如果封装成 `TaskManager`，你只需要改这个类内部——其他代码完全不受影响。"

这就是**抽象的价值**：用类把"可能会变化的部分"隔离起来。

### 从需求到类的实践

现在用名词提取法重新设计 TaskManager。注意到这里我们再次使用了 Week 01 的**静态类型**声明——每个字段都必须明确类型。就像你上周写 `UserInfo` 时，必须指定 `name` 是 `String`、`age` 是 `int` 一样，类设计同样需要明确的类型思维。

还记得 Week 01 用 `Scanner` 从控制台读取用户输入吗？

```java
Scanner scanner = new Scanner(System.in);
String name = scanner.nextLine();  // 读取用户名
```

类设计也有类似的"输入"——不是从键盘，而是从需求中"读取"信息。名词提取法就是一种从需求文档中"读取"类名的工具。

"我试了一下让 AI 生成类代码，"阿码插话说，"我让它'写一个 Task 类'，它真的生成了完整的代码——字段、getter、setter、构造方法，一应俱全。但我运行的时候才发现，它把 `priority` 生成了 `String` 类型，而我后来想按优先级排序，结果字符串排序和数字不一样——'高'在字典序里反而比'低'靠后。"

"这就是问题所在，"老潘说，"AI 能根据你描述的类生成代码，但它**不会帮你思考'这个字段应该是什么类型'**。你得自己决定：优先级是用数字（1/2/3）还是字符串（高/中/低），还是枚举（Week 08 会讲）。类型设计影响后续所有逻辑，AI 没法替你做这个决策。"

```java
// 文件：Task.java（实体类）
public class Task {
    // 字段声明：必须指定类型（Week 01 的知识）
    private String title;
    private String description;
    private String dueDate;
    private String priority; // "高"、"中"、"低"
    private boolean completed;

    public Task(String title) {
        this.title = title;
        this.completed = false;
        this.priority = "中"; // 默认优先级
    }

    // Getter 和 Setter（下一节讲为什么需要）
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }

    // ... 其他 getter/setter
}

// 文件：TaskManager.java（服务类）
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks = new ArrayList<>();

    // 添加任务
    public void addTask(Task task) {
        tasks.add(task);
    }

    // 标记任务完成
    public void markCompleted(String taskTitle) {
        for (Task task : tasks) {
            if (task.getTitle().equals(taskTitle)) {
                task.markCompleted();
                break;
            }
        }
    }

    // 过滤未完成的任务
    public List<Task> getIncompleteTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                result.add(task);
            }
        }
        return result;
    }
}
```

"看，"老潘说，"现在 `Task` 只负责存储数据，`TaskManager` 负责管理任务。职责分明了。"

---

> **AI 时代小专栏：AI 能生成类代码，但不能做设计决策**
>
> 2025 年的代码质量研究显示了一个令人担忧的趋势：AI 生成代码中，62% 存在"职责不清晰"的问题——类和方法的边界模糊，三个月后没人敢改。
>
> AI 擅长什么？当你告诉它"创建一个 Task 类，有 title、description、completed 字段"，它能在几秒钟内生成完整的类代码，包括 getter、setter、构造方法。
>
> 但 AI 不擅长什么？
> - 它不会帮你思考"这个类应该有哪些职责"（它可能把验证、持久化、UI 都塞进一个类）
> - 它不知道你的业务会如何变化（所以它不会提前考虑"将来可能需要支持子任务"）
> - 它不会写 ADR（设计决策记录）
>
> 更关键的是，研究表明 **53% 的开发者报告 AI 工具增加了他们的技术债**，而 **67% 的开发者报告花更多时间调试 AI 生成的代码**。MIT 研究者甚至将 AI 代码称为"**高息债务**"：它提供即时的开发速度，但以未来维护负担的复利作为代价。开发者直接接受 AI 给的设计，却不理解"为什么这样设计"。
>
> 所以本周你学的"名词提取法"、"职责分离"——这些看似"慢"的思考，恰恰是你**审查 AI 代码、判断设计质量**的标尺。AI 能帮你生成类代码，但你必须负责：
> - 判断 AI 给的类职责是否清晰
> - 决定是否需要拆分或合并类
> - 记录你的设计决策（ADR）
> - 在业务变化时重构设计
>
> 参考（访问日期：2026-02-11）：
> - [The Revenge of QA: How AI Code Generation Is Exposing Decades of Process Debt](https://itrevolution.com/articles/the-revenge-of-qa-how-ai-code-generation-is-exposing-decades-of-process-debt/)
> - [How to Apply SOLID Principles in AI Development Using Prompt Engineering](https://www.syncfusion.com/blogs/post/solid-principles-ai-development)

---

## 3. 封装——小北的三次试错

小北看完老潘的讲解，觉得自己已经懂了。他决定亲手写一个 `Task` 类——不用 `private`，全部用 `public`，这样"写起来快"。

### 第一次尝试：全部 public

```java
// 小北的代码（❌ 所有字段都是 public）
public class Task {
    public String title;
    public boolean completed;
    public int priority; // 1=高, 2=中, 3=低
}
```

他兴冲冲地写了个测试：

```java
Task task = new Task();
task.title = "写作业";
task.completed = true;
task.priority = 5; // 手滑，写成了 5
```

程序运行了，没有报错。但三天后，当小北想按优先级排序时，发现 `priority = 5` 根本不在他预期的 1-3 范围内。这个 bug 藏了三天，影响了十几个任务的数据。

"为什么 Java 不报错？"小北困惑地问。

"因为 `public` 字段没有任何保护，"老潘说，"外部代码可以赋任何值，Java 编译器不会检查。"

### 第二次尝试：加验证，但用 public

小北不甘心："那我每次赋值前自己检查不就行了？"

```java
// 小北改进后的代码（还是有问题）
public class Task {
    public String title;
    public boolean completed;
    public int priority;

    public void setPrioritySafe(int p) {
        if (p < 1 || p > 3) {
            throw new IllegalArgumentException("优先级必须是 1-3");
        }
        this.priority = p;
    }
}
```

"这样总行了吧？"

"不行，"老潘摇头，"你还是可以直接 `task.priority = 5`，绕过你的 `setPrioritySafe` 方法。而且——"老潘指着屏幕，"你的同事阿码不知道有 `setPrioritySafe` 这个方法，他直接用了 `task.priority = 2`，你的检查就形同虚设。"

小北愣住了。他意识到：**只要字段是 public，就无法强制所有人走验证逻辑**。

### 第三次尝试：终于对了

"那怎么办？"小北问。

"把字段藏起来，只暴露你控制的方法。"老潘写下：

```java
// ✅ 正确的封装
public class Task {
    private String title;      // private：外部无法直接访问
    private boolean completed;
    private int priority;

    public Task(String title) {
        this.title = title;
        this.completed = false;
        this.priority = 2; // 默认"中"
    }

    // 外部只能通过这个方法修改 priority
    public void setPriority(int priority) {
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级必须是 1-3");
        }
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    // ... 其他 getter/setter
}
```

"现在，"老潘解释，"`task.priority = 5` 编译都过不了——因为 `priority` 是 `private`。外部代码**只能**通过 `setPriority(5)`，而这个方法会抛出异常，立即提醒你'这里有 bug'。"

小北恍然大悟："所以封装不是'麻烦'，是'保护'——保护数据不被破坏。"

"对。而且还有一个更深层的价值。"老潘说。

### 封装的真正价值：你可以"撒谎"

"等等，"阿码凑过来，"什么叫'撒谎'？"

老潘笑了："意思是——**封装让你可以在内部偷偷改变实现，而外部完全不知道**。

假设你最初设计 `Task` 的 `completed` 字段是 `boolean`：

```java
// 初始设计
public class Task {
    private boolean completed; // true=已完成, false=未完成

    public boolean isCompleted() {
        return completed;
    }
}
```

三个月后，产品经理说："我们需要支持'进行中'的状态。"

如果你用封装：

```java
// 改进后的设计（内部实现变了，但外部代码不变）
public class Task {
    private enum Status { NOT_STARTED, IN_PROGRESS, COMPLETED }
    private Status status = Status.NOT_STARTED;

    public boolean isCompleted() {  // ✅ 方法签名不变
        return status == Status.COMPLETED;
    }

    public boolean isInProgress() {  // 新增方法
        return status == Status.IN_PROGRESS;
    }

    public void markCompleted() {
        this.status = Status.COMPLETED;
    }

    public void markInProgress() {  // 新增方法
        this.status = Status.IN_PROGRESS;
    }
}
```

"看，"老潘说，"内部从 `boolean` 改成了 `enum`，但 `isCompleted()` 方法还在，返回类型也没变。所有调用 `task.isCompleted()` 的外部代码**完全不用改**——它们根本不知道你内部换了实现。"

"这就是封装的价值，"老潘总结，"**内部实现可以自由变化，只要公共接口不变，外部代码完全不受影响。** 你可以'撒谎'——告诉外部'我还是那个 boolean'，实际上内部已经变成了复杂的状态机。"

### 完整的封装示例

```java
// 文件：Task.java（完整的封装示例）
public class Task {
    // 字段：全部 private
    private String title;
    private String description;
    private int priority; // 1=高, 2=中, 3=低
    private boolean completed;

    // 构造方法
    public Task(String title) {
        this.title = title;
        this.priority = 2; // 默认"中"
        this.completed = false;
    }

    // Getter：返回字段值
    public String getTitle() {
        return title;
    }

    // Setter：设置字段值（可以加验证）
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        this.title = title;
    }

    // boolean 的 getter 命名习惯：is 开头
    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {  // 更语义化的方法名
        this.completed = true;
    }

    // 带验证的 setter
    public void setPriority(int priority) {
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级必须是 1-3");
        }
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    // 可以没有 setter（只读字段）
    public String getDescription() {
        return description;
    }
}
```

小北看明白了："所以封装不是'麻烦'，是'保护'——保护数据不被破坏，保护代码不受内部变化影响。"

"对。"老潘说，"而且记住：**封装不是目的，是手段。目的是让代码更容易维护。**"

### 阿码的疑问

"那 getter/setter 这么多重复代码，能不能用 AI 生成？"

"可以，"老潘点头，"但你必须：
1. 理解为什么要写 getter/setter（不是'模板'，是'保护数据'）
2. 知道哪些字段需要 setter（如果字段不应该被外部修改，就不要提供 setter）
3. 在 setter 里加验证逻辑（AI 生成的代码通常没有验证）

AI 能帮你生成代码骨架，但你负责填充逻辑。"

---

## 4. SOLID 原则入门——单一职责与开闭原则

"小北，你看过我上个月写的代码吗？"老潘打开一个老项目，"这个类有 1200 行。"

小北凑过去一看——一个 `TaskProcessor` 类，里面有保存、验证、发送邮件、生成报表、格式化输出……"天哪，这真的是一个人写的吗？"

"这就是我当时的'杰作'，"老潘苦笑，"后来产品要加一个'短信通知'功能。你以为简单吧？就加一个方法嘛。结果我改了这个类，测试了 12 个功能，有 3 个被影响坏了。修了一周，还引了新 bug。"

阿码问："为什么会这样？"

"因为这个类**职责太多**。每次改动都可能影响其他功能。这就是为什么我们需要 **SOLID 原则**——它不是'考试要背'的，是'避免掉坑'的经验总结。"

老潘顿了顿："这五个字母，代表五个从无数次失败中总结出来的教训。今天我们先讲最基础的两个——单一职责原则（SRP）和开闭原则（OCP）。贪多嚼不烂。"

### 一个反直觉的事实

"这里有个反直觉的事实，"老潘在白板上写下一行字：

> **"一个类承担的职责越少，它越容易被复用。"**

"不对吧？"小北疑惑，"职责多不是说明功能强大吗？"

"恰恰相反，"老潘说，"想象你写了一个'超级任务类'——它能保存、验证、发送邮件、生成图表。现在你想在新项目里复用'保存任务'的功能——你必须把整个类搬过去，连同你不需要的邮件、图表功能一起。这就是'依赖了不需要的东西'。"

"但如果你的 `Task` 类只负责存储数据，`TaskSaver` 类只负责保存——你可以在任何地方复用 `Task` 类，不需要带上保存、邮件等额外功能。"

"职责单一 → 依赖少 → 复用容易。这就是 SRP 的核心价值。"

### SOLID 是什么（快速了解）

SOLID 是五个设计原则的首字母缩写：

| 原则 | 名称 | 一句话记住 |
|------|------|------------|
| **S** | Single Responsibility Principle（单一职责原则） | 一个类只做一件事 |
| **O** | Open/Closed Principle（开闭原则） | 加功能不改旧代码 |
| **L** | Liskov Substitution Principle（里氏替换原则） | 子类能替换父类 |
| **I** | Interface Segregation Principle（接口隔离原则） | 接口要小而专一 |
| **D** | Dependency Inversion Principle（依赖倒置原则） | 依赖抽象不依赖具体 |

**本周重点**：S（单一职责）和 O（开闭原则）。其他三个后续深入。

### 单一职责原则（SRP）

**定义**：一个类应该只有一个引起它变化的原因。

"还是有点抽象，"阿码说。

老潘指着小北之前写的代码："你看，你的 Task 类有 5 个'引起变化的原因'——"

1. 任务的数据结构变了（比如新增一个字段）→ 需要修改 Task
2. 验证规则变了（比如优先级从 3 档改成 5 档）→ 需要修改 Task
3. 存储方式变了（比如从文件改成数据库）→ 需要修改 Task
4. 显示格式变了（比如从文本改成 JSON）→ 需要修改 Task
5. 通知方式变了（比如从邮件改成短信）→ 需要修改 Task

"这就是**职责过多**——每次需求变，你都要改 Task 类。改的次数多了，bug 就多了。"

### 拆分小北的"上帝类"

现在把小北的 Task 类拆成多个职责单一的类：

**拆分前（上帝类）**：
```java
// ❌ 职责过多
public class Task {
    // 职责 1：存储数据
    public String title;
    public boolean completed;

    // 职责 2：验证数据
    public boolean isValid() { /* ... */ }

    // 职责 3：文件存储
    public void saveToFile(String filename) { /* ... */ }

    // 职责 4：显示
    public void print() { /* ... */ }

    // 职责 5：邮件通知
    public void sendEmailNotification(String email) { /* ... */ }
}
```

**拆分后（单一职责）**：
```java
// ✅ 职责 1：存储数据
public class Task {
    private String title;
    private boolean completed;

    public Task(String title) {
        this.title = title;
        this.completed = false;
    }

    public String getTitle() { return title; }
    public boolean isCompleted() { return completed; }
    public void markCompleted() { this.completed = true; }
}

// ✅ 职责 2：验证数据
public class TaskValidator {
    public static boolean isValid(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}

// ✅ 职责 3：文件存储
public class TaskRepository {
    public void save(Task task, String filename) {
        // ... 写文件
    }

    public Task load(String filename) {
        // ... 读文件
        return null;
    }
}

// ✅ 职责 4：显示
public class TaskPrinter {
    public void print(Task task) {
        System.out.println("任务：" + task.getTitle());
        System.out.println("状态：" + (task.isCompleted() ? "已完成" : "进行中"));
    }

    public void printAsJson(Task task) {
        System.out.println("{ \"title\": \"" + task.getTitle() + "\" }");
    }
}

// ✅ 职责 5：邮件通知
public class EmailNotifier {
    public void sendTaskNotification(Task task, String emailAddress) {
        // ... 发邮件
    }
}
```

现在每个类只有一个职责：
- `Task`：存储任务数据
- `TaskValidator`：验证任务是否合法
- `TaskRepository`：负责持久化
- `TaskPrinter`：负责显示
- `EmailNotifier`：负责邮件通知

### 如何判断类是否违反 SRP

老潘给了三个简单的问题：

**问题 1：这个类有哪些"变化的原因"？**
- 如果你能说出 2 个以上的原因（比如"数据结构变"、"验证规则变"、"存储方式变"），说明职责过多。

**问题 2：能不能用一句话描述这个类的职责？**
- 如果你的描述是"它负责 A，还有 B，还有 C……"，说明职责过多。
- 好的描述应该是"它负责……"（一个完整的句子，一个责任）。

**问题 3：如果需求变了，需要改几个类？**
- 如果一个需求变，你需要改 3 个类，说明职责没分好。
- 理想情况是：一个需求变，只需要改 1 个类。

### 开闭原则（OCP）——不用继承也能理解

**定义**：对扩展开放，对修改关闭。

"这个怎么理解？"小北问。

老潘用 `TaskPrinter` 举例：

```java
// ❌ 不符合开闭原则：每次要新格式，都要修改这个类
public class TaskPrinter {
    public void print(Task task, String format) {
        if (format.equals("text")) {
            System.out.println("任务：" + task.getTitle());
        } else if (format.equals("json")) {
            System.out.println("{ \"title\": \"" + task.getTitle() + "\" }");
        } else if (format.equals("xml")) {
            // 每次要支持新格式，都要加一个 else if
            System.out.println("<task><title>" + task.getTitle() + "</title></task>");
        }
    }
}
```

"这个设计的问题：每次要支持新格式，你都要修改 `TaskPrinter` 类——这就叫'对修改不关闭'。"

"那怎么办？"小北问，"不用继承能做到吗？"

"能。"老潘说，"开闭原则的核心思想不是'必须用继承'，而是'**通过某种方式扩展功能，而不是修改现有代码**'。"

```java
// ✅ 符合开闭原则：通过组合而非继承来扩展
// 不需要抽象类，不需要继承，Week 02 的知识就能理解

// 1. 定义一个"格式器"接口（用接口，不是抽象类）
interface TaskFormatter {
    String format(Task task);
}

// 2. 文本格式实现
class TextFormatter implements TaskFormatter {
    public String format(Task task) {
        return "任务：" + task.getTitle();
    }
}

// 3. JSON 格式实现
class JsonFormatter implements TaskFormatter {
    public String format(Task task) {
        return "{ \"title\": \"" + task.getTitle() + "\" }";
    }
}

// 4. 打印器不关心具体格式，只依赖接口
class TaskPrinter {
    public void print(Task task, TaskFormatter formatter) {
        System.out.println(formatter.format(task));
    }
}

// 使用
Task task = new Task("写作业");
TaskPrinter printer = new TaskPrinter();

// 想要文本格式？用 TextFormatter
printer.print(task, new TextFormatter());

// 想要 JSON 格式？用 JsonFormatter
printer.print(task, new JsonFormatter());

// 想要 XML 格式？新建一个类，不用改 TaskPrinter
class XmlFormatter implements TaskFormatter {
    public String format(Task task) {
        return "<task><title>" + task.getTitle() + "</title></task>";
    }
}
printer.print(task, new XmlFormatter());
```

"看，"老潘说，"要支持新格式，只需要**新建一个类**（`XmlFormatter`），不用修改 `TaskPrinter`——这就是'对修改关闭'。"

"这里用到的 `interface` 是 Week 02 的新知识，"老潘补充，"它比抽象类更轻量，更适合这种'定义行为契约'的场景。你不用理解继承的复杂规则，只需要知道：接口定义了'能做什么'，具体类实现'怎么做'。"

### AI 生成的代码往往违反 SOLID

阿码举手了："那为什么我让 AI 生成的代码，经常是'上帝类'？"

"因为 AI 擅长生成'能跑的代码'，不擅长做'设计决策'。"老潘说。

他打开一份 AI 生成的代码：

```java
// AI 生成的代码（❌ 职责过多）
public class TaskService {
    private List<Task> tasks = new ArrayList<>();

    // 数据验证
    public boolean isValid(Task task) { /* ... */ }

    // 数据存储
    public void saveToFile(String filename) { /* ... */ }

    // 数据显示
    public void print(Task task) { /* ... */ }

    // 邮件通知
    public void sendEmail(Task task, String email) { /* ... */ }

    // ... 20 多个方法
}
```

"AI 为什么这样生成？因为它的训练数据里有大量这样的代码——快速原型、个人项目、写完就丢的脚本。但在工程上，这种代码三个月后就变成'遗留代码'，没人敢动。"

"所以你必须**审查 AI 生成的代码**，用 SOLID 原则判断：
- 这个类的职责清晰吗？
- 如果需求变了，需要改几个地方？
- 有没有把不相关的职责塞在一起？

**AI 能生成代码，但你负责设计质量。**"

---

> **AI 时代小专栏：SOLID 原则在 AI 时代仍然重要**
>
> 2025 年的研究报告显示了一个矛盾：开发者用 AI 写代码更快了（平均节省 55% 时间），但代码质量反而下降了——60% 的 AI 生成代码存在"职责不清晰"的问题。
>
> 更关键的是，研究表明 **AI 生成的代码中，代码重复量增加了 8 倍**。Syncfusion 2025 年的报告指出："AI 生成的代码往往缺乏结构，导致系统脆弱"。这些"能跑但设计混乱"的代码会快速累积成技术债：当需求变化时（而需求总是在变化），修改 AI 生成的代码比修改手工设计的代码**更困难**——因为你不理解当初 AI 为什么这样生成，也不知道改一个地方会不会影响其他地方。
>
> SOLID 原则在 AI 时代的价值是什么？
>
> **它不是让你"背定义"，而是给你"审查 AI 代码的标尺"。**
>
> | SOLID 原则 | 审查 AI 代码时的问题 |
> |-----------|-------------------|
> | S（单一职责） | 这个类是不是承担了太多不相关的职责？ |
> | O（开闭原则） | 如果需求变了，这个类需要修改吗？能通过扩展实现吗？ |
> | L（里氏替换） | AI 生成的子类真的能替换父类吗？ |
> | I（接口隔离） | 接口是不是太大？强迫类实现不需要的方法？ |
> | D（依赖倒置） | 类是不是依赖具体实现而不是抽象？ |
>
> 2025 年的工程实践也印证了这一点：**SOLID 原则仍然是构建可维护软件的"黄金标准"**。LinkedIn 2026 年的一篇文章指出："清晰的边界防止复制粘贴逻辑变成永久性代码"、"依赖倒置防止快速修复硬连线到系统中"。在 AI 时代，它们的价值不是减少了，而是增加了——因为它们是你判断"AI 生成的代码是否值得接受"的依据。
>
> 所以本周你学的 SOLID，不是"过时的理论"，而是**AI 时代的核心技能**。AI 能帮你写代码，但你负责：
> - 判断 AI 给的设计是否合理
> - 拆分或重构 AI 生成的"上帝类"
> - 在需求变化时演进设计
>
> 参考（访问日期：2026-02-11）：
> - [The Revenge of QA: How AI Code Generation Is Exposing Decades of Process Debt](https://itrevolution.com/articles/the-revenge-of-qa-how-ai-code-generation-is-exposing-decades-of-process-debt/)
> - [How to Apply SOLID Principles in AI Development Using Prompt Engineering](https://www.syncfusion.com/blogs/post/solid-principles-ai-development)
> - [SOLID Principles Remain Crucial in AI-Assisted Coding - LinkedIn](https://www.linkedin.com/posts/kyleszives_solid-principles-and-clean-code-are-cooked-activity-7422722714949304320-No9H)

---

## 5. ADR——小北的第一份设计文档

小北不理解："设计就设计呗，为什么要写文档？代码不就是最好的文档吗？"

老潘叹了口气："来，我给你讲个真实的故事。"

### 一个真实的故事

"两年前，我接手一个项目。我看到代码里有个 `TaskService` 类，里面有个方法叫 `validateAndSaveAndNotify()`——对，三个动词连在一起。"

"我觉得这方法名太长，就拆成了三个方法：`validate()`、`save()`、`notify()`。"

"结果上线后，系统崩了。"

"为什么？"小北问。

"因为**当初设计这个方法的人有他的原因**——这三个操作必须在同一个事务里执行，否则会出现'保存成功但通知失败'的异常状态。我把它们拆开，破坏了这个约束。"

"但代码里没有任何注释说明这一点，也没有文档记录这个设计决策。当初设计的人已经离职了，没人知道为什么要这样写。"

"所以记住：**代码告诉你'怎么做'，ADR 告诉你'为什么做'。**"

### 什么是 ADR

**ADR**（Architecture Decision Record，架构决策记录）是一个轻量级文档，记录重要的架构决策及其背景。

ADR 的核心思想：

| 内容 | 说明 |
|------|------|
| **我们要做什么** | 决策的内容 |
| **为什么要这样做** | 背景和动机 |
| **为什么不那样做** | 考虑过的其他方案 |
| **有什么后果** | 这个决策的影响 |
| **怎么验证** | 后续如何验证这个决策是否正确 |

### 小北写的第一份 ADR（被老潘打回来了）

小北听完老潘的故事，决定自己写一份 ADR。他花了半小时，写完后得意地给老潘看：

```markdown
# ADR-001: TaskManager 设计

## 决策
我们设计了 Task 类和 TaskManager 类。

## 理由
这样设计比较好，符合 SOLID 原则。

## 替代方案
无。
```

老潘看完后，沉默了几秒。

"小北，你知道这份 ADR 的问题在哪吗？"

"啊？有什么问题？"

"问题太多了。"老潘指着屏幕：

"第一，'比较好'——什么叫比较好？好在哪里？具体解决了什么问题？"

"第二，'符合 SOLID 原则'——哪个原则？怎么符合的？"

"第三，'无'替代方案——你真的没有考虑过其他方案吗？比如全部用 public 字段？比如把所有功能塞进一个类？"

"第四，也是最重要的——**这份 ADR 没有记录你的思考过程**。三个月后你再看，你根本不知道当初为什么这样设计。"

小北脸红了："那……应该怎么写？"

### ADR 的基本结构

老潘拿出一份模板：

```markdown
# ADR-001: 领域模型设计决策

## 状态
已采纳 / 已废弃 / 已替代

## 背景
我们正在开发 TaskManager 任务管理系统。需要设计核心的领域模型，包括任务、用户、项目管理等实体。

## 决策
我们选择将系统拆分为以下核心类：

### 实体类
- **Task**：存储任务的标题、描述、优先级、完成状态
- **User**：存储用户信息（暂不实现，预留扩展）

### 管理类
- **TaskManager**：管理任务的增删改查
- **TaskValidator**：验证任务数据合法性
- **TaskRepository**：负责任务持久化（接口设计，Week 07 实现）

### 设计原则
- 遵循单一职责原则（SRP）：每个类只负责一个职责
- 遵循开闭原则（OCP）：通过接口扩展功能，而不是修改现有代码

## 理由
1. **职责分离**：将数据存储、验证、管理、持久化分离，便于维护和测试
2. **可扩展性**：TaskRepository 设计为接口，将来可以切换存储方式（内存 → 文件 → 数据库）
3. **符合 SOLID**：避免"上帝类"，每个类职责单一

## 替代方案
### 方案 A：单一 Task 类（上帝类）
- **优点**：简单快速
- **缺点**：难以维护，职责过多
- **结论**：不采纳

### 方案 B：使用继承（Task 基类 + HighPriorityTask 子类）
- **优点**：可以表达"不同类型任务"的差异
- **缺点**：过度设计，目前需求不需要
- **结论**：暂不采纳，将来如果需要再考虑

## 后果
### 正面影响
- 代码结构清晰，易于理解和维护
- 每个类可以独立测试
- 未来扩展容易（比如切换存储方式）

### 负面影响
- 类的数量增多，初期开发时间增加
- 需要理解多个类之间的关系

### 风险
- 如果团队对 SOLID 原则理解不足，可能过度设计
- 缓解措施：保持设计简单，避免为了模式而模式

## 验证方式
- [ ] Week 04：完成 TaskManager 的 CLI 版本，验证类设计是否合理
- [ ] Week 07：实现 TaskRepository 的持久化，验证接口设计是否便于扩展
- [ ] Week 12：代码审查，检查是否违反了单一职责原则

## 相关决策
- ADR-002（Week 04）：数据存储方案决策
- ADR-003（Week 06）：API 设计决策

## 参考
- SOLID Principles (Robert C. Martin)
- Domain-Driven Design (Eric Evans)
```

"看，"老潘说，"这份 ADR 的关键不是'写了什么'，而是'记录了思考过程'：
- 你考虑过哪些方案？
- 为什么选 A 不选 B？
- 有什么风险和缓解措施？
- 将来怎么验证这个决策是否正确？"

### ADR 的最佳实践

老潘给了几个写 ADR 的建议：

**1. 保持轻量**
- ADR 不是学术论文，不需要长篇大论
- 一页 Markdown 足够记录一个决策
- 重点写"为什么"和"替代方案"

**2. 记录决策过程，不只是结果**
- 不要只写"我们选择了方案 A"
- 要写"为什么选 A 而不是 B"——这是最有价值的信息

**3. ADR 是活的文档**
- 决策不是永久的，可以变更
- 如果后来发现 ADR-001 的决策有问题，写 ADR-005 记录"为什么废弃 ADR-001"
- ADR 的历史记录比"最新的正确答案"更有价值

**4. 由首席架构师负责**
- ADR 必须由人写，不能用 AI 代劳
- 首席架构师负责撰写，但团队参与讨论
- **ADR 记录的是团队的集体决策，不是某个人的想法**

### ADR 和锚点的关系

小北想起来了："Week 01 我们学过'锚点'（anchor），它和 ADR 是什么关系？"

"锚点是 ADR 的核心思想，"老潘说。

Week 01 的"锚点"定义：

> 把"主张"与"可验证证据"绑定在一起的记录。

ADR 就是锚点的**具体实践**：

| 锚点要素 | ADR 中的对应 |
|---------|-------------|
| 主张 | "决策"部分 |
| 理由 | "理由"部分 |
| 替代方案 | "替代方案"部分 |
| 可验证证据 | "验证方式"部分 |

"所以 ADR 不是'额外的工作'，而是**把你在脑子里想的设计决策显式化、可追溯**。"

### 小北修改后的 ADR

小北重新写了一份：

```markdown
# ADR-001: TaskManager 领域模型设计

## 状态
已采纳

## 背景
CampusFlow 项目小组选择了"TaskFlow"（个人任务管理）作为选题。需要设计核心的领域模型，支撑任务创建、管理、持久化等功能。

## 决策

### 核心类设计
```
┌─────────────────┐
│    TaskManager  │ (管理类)
├─────────────────┤
│ - tasks: List   │
│ + addTask()     │
│ + markComplete()│
│ + filter()      │
└────────┬────────┘
         │ 使用
         ▼
┌─────────────────┐
│      Task       │ (实体类)
├─────────────────┤
│ - title: String │
│ - completed:    │
│   boolean       │
│ + getTitle()    │
│ + markComplete()│
└─────────────────┘
```

### 职责分配
- **Task**：只负责存储任务数据（封装）
- **TaskManager**：负责管理任务的增删改查（服务）
- **TaskValidator**：负责验证数据合法性（验证）
- **TaskPrinter**：负责格式化输出（显示）

### 设计约束
- 所有 Task 字段都是 `private final`（不可变）
- 外部访问必须通过 getter 方法
- Task 对象创建后不能修改（除了 completed 状态）

## 理由
1. **单一职责**：每个类只负责一件事，便于理解和测试
2. **封装保护**：private 字段防止数据被意外破坏
3. **不可变性**：Task 创建后不能修改，避免并发问题（Week 09 会讲到）
4. **可扩展性**：将来可以轻松添加新功能（比如 TaskRepository 持久化）

## 替代方案

### 方案 A：全部 public 字段
- **优点**：代码少，写起来快
- **缺点**：数据不安全，无法控制变化
- **结论**：不采纳——违反封装原则

### 方案 B：使用 Lombok 自动生成 getter/setter
- **优点**：减少样板代码
- **缺点**：隐藏了设计意图，新人不易理解
- **结论**：暂不采纳——教学阶段手写更有助于理解

## 后果
- 正面：代码结构清晰，职责分明，易于维护
- 负面：类数量增多，初期开发时间增加约 30%
- 风险：团队可能过度设计——缓解：每个功能需求对应一个类

## 验证方式
- [ ] Week 02：TaskManager 能正常运行（交互式菜单）
- [ ] Week 04：代码审查，检查是否符合单一职责原则
- [ ] Week 07：添加 TaskRepository 时，验证 Task 类设计是否便于持久化

## 首席架构师
[你的名字] - 第 1 轮（Week 02-03）

## 记录日期
2026-02-11
```

"这次好多了，"老潘点头，"这份 ADR 记录了你的设计决策。三个月后，你或你的同事接手这个项目，看到 ADR 就会明白'为什么这样设计'——不会像我当年那样，自作聪明地改坏代码。"

### ADR 不能用 AI 代劳

阿码问："那 ADR 能用 AI 生成吗？"

老潘严肃地说："**ADR 必须由人来写，不能用 AI 代劳。**"

"为什么？"

"因为 ADR 记录的是**你的团队的决策过程**：
- 为什么你选择了方案 A 而不是 B？
- 你的团队讨论过哪些问题？
- 你的项目有哪些特殊约束？

AI 可以帮你润色语言、帮你检查格式，但**不能帮你做决策**。如果你用 AI 生成 ADR，你会得到一堆'看起来正确但和你项目无关'的文字——这没有意义。"

"而且，"老潘补充道，"写 ADR 的过程本身就是'深度思考'。当你被迫用文字解释'为什么这样设计'时，你会发现自己思维的漏洞——这是 AI 代劳不了的。"

---

## CampusFlow 进度

本周是项目设计阶段，你需要完成核心领域模型设计和第一份 ADR。

### 1. 识别核心类

根据你的选题，使用"名词提取法"识别核心类：

**示例：TaskFlow（个人任务管理）**
- 核心实体：Task（任务）、Category（分类）、Tag（标签）
- 管理类：TaskManager、TaskFilter
- 接口：TaskRepository（Week 07 实现）

**示例：NoteStream（笔记工具）**
- 核心实体：Note（笔记）、Folder（文件夹）、Tag（标签）
- 管理类：NoteManager、SearchEngine
- 接口：NoteRepository（Week 07 实现）

**示例：BookHub（图书管理）**
- 核心实体：Book（图书）、BorrowRecord（借阅记录）、User（用户）
- 管理类：BookManager、BorrowManager
- 接口：BookRepository（Week 07 实现）

### 2. 设计类结构

在 `src/main/java/edu/campusflow/domain/` 目录下创建领域模型：

```bash
# 创建目录
mkdir -p src/main/java/edu/campusflow/domain

# 创建核心类文件
touch src/main/java/edu/campusflow/domain/Task.java
touch src/main/java/edu/campusflow/domain/TaskManager.java
touch src/main/java/edu/campusflow/domain/TaskValidator.java
touch src/main/java/edu/campusflow/domain/TaskPrinter.java
```

### 3. 编写 ADR-001

在 `docs/` 目录下创建第一份 ADR：

```bash
# 创建目录
mkdir -p docs/adr

# 创建 ADR-001
touch docs/adr/001-领域模型设计.md
```

ADR 模板：

```markdown
# ADR-001: [你的项目名] 领域模型设计

## 状态
已采纳

## 背景
[一句话描述你的项目是什么]

## 决策
[核心类设计图 + 职责分配]

## 理由
[为什么这样设计？引用 SOLID 原则]

## 替代方案
[考虑过的其他方案]

## 后果
[正面影响、负面影响、风险]

## 验证方式
- [ ] Week 04：[如何验证这个设计]
- [ ] Week 07：[如何验证可扩展性]

## 首席架构师
[你的名字] - 第 1 轮（Week 02-03）

## 记录日期
2026-02-11
```

### 4. 代码示例

Task.java：
```java
package edu.campusflow.domain;

public class Task {
    private final String title;
    private final String description;
    private boolean completed;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        this.completed = true;
    }
}
```

TaskManager.java：
```java
package edu.campusflow.domain;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void markCompleted(String title) {
        for (Task task : tasks) {
            if (task.getTitle().equals(title)) {
                task.markCompleted();
                break;
            }
        }
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getIncompleteTasks() {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                result.add(task);
            }
        }
        return result;
    }
}
```

### 5. 验证

```bash
# 编译检查
javac src/main/java/edu/campusflow/domain/*.java

# 运行测试（如果已编写）
java -cp src/main/java edu.campusflow.domain.TaskManager
```

你应该看到：
- 代码编译通过
- 类职责清晰（Task 存储数据，TaskManager 管理任务）
- ADR-001 文档完整

---

## Git 本周要点

### 必会命令

```bash
# 查看当前分支
git branch

# 创建新分支（用于实验性开发）
git branch feature-task-model
git checkout feature-task-model

# 合并分支
git checkout main
git merge feature-task-model

# 删除已合并的分支
git branch -d feature-task-model
```

### Commit Message 规范

本周的 commit 示例：

```bash
git add docs/adr/001-领域模型设计.md
git commit -m "docs: 完成 ADR-001 领域模型设计"

git add src/main/java/edu/campusflow/domain/
git commit -m "feat: 实现 Task 和 TaskManager 核心类"
```

### 常见坑

1. **忘记写 ADR 就写代码** → 代码能跑，但三个月后没人知道为什么这样设计
2. **ADR 用 AI 生成** → 得到"正确的废话"，没有记录真实决策过程
3. **类职责不清晰就动手写** → 写着写着变成"上帝类"，后期难以维护

---

## 本周小结（供下周参考）

本周我们从"类设计混乱"的问题出发，建立了一套完整的设计思维。

首先，类不是凭空想出来的——它是从问题域中识别出来的（名词提取法）。实体类存储数据，服务类提供行为，两者的职责要分清楚。上周你写的 `UserInfo` 就是一个很好的实体类示例，而 `BusinessCardGenerator` 是服务类。

封装是保护数据的第一道防线。`private` 字段 + getter/setter 不是"模板代码"，而是把"外部可以怎么访问"和"内部怎么存储"解耦开来。内部实现可以自由变化（比如从 `boolean completed` 改成 `enum Status`），只要公共接口不变，外部代码完全不受影响。这又体现了 Week 01 学到的静态类型思维的价值——编译器会在你访问不存在的字段时立即报错。

单一职责原则（SRP）是避免"上帝类"的关键。一个类应该只有一个引起它变化的原因。小北的 Task 类有 5 个职责，所以每次需求变都要改它——这就像一个什么都会的人，但什么都不精。拆分后，`Task` 只管数据，`TaskValidator` 管验证，`TaskRepository` 管存储——改验证规则时只需要动 `TaskValidator`，不会影响 `Task`。

ADR 则把这些"为什么这样设计"的决策显式化、可追溯。代码告诉你"怎么做"，ADR 告诉你"为什么做"。老潘的故事已经说明了这一点：没有 ADR，你可能会自作聪明地改坏代码。而且 ADR 必须由人来写——它记录的是你团队的决策过程，不是某个通用答案。

最后，AI 能生成类代码，但不能做设计决策。本周你学的 SOLID 原则、名词提取法、封装思想——这些是"审查 AI 代码、判断设计质量"的标尺。AI 擅长生成"能跑的代码"，但你负责判断设计是否合理、是否需要重构。

下周我们将进入**异常处理与防御式编程**。你会发现：好的代码不是"不出错"，而是"出错时有策略"。

---

## CampusFlow 项目进度

本周你为 CampusFlow 项目设计了**核心领域模型**，这是整个系统的骨架。一个良好的领域模型能让后续开发事半功倍。

### 核心类设计

**1. Task（任务实体）**
- 职责：存储任务数据（标题、描述、优先级、截止日期）
- 设计：private 字段 + public getter/setter，封装内部状态
- 验证：setter 方法拒绝非法输入（如空标题）

**2. TaskManager（任务管理器）**
- 职责：管理任务的增删改查
- 设计：单一职责——只负责任务管理，不负责存储和显示
- 依赖：使用 TaskValidator 验证任务数据

**3. TaskValidator（验证器）**
- 职责：验证任务数据是否合法
- 设计：独立的验证逻辑，可复用

### 代码片段

```java
// Task 实体类
public class Task {
    private String title;
    private String description;
    private String priority;  // HIGH, MEDIUM, LOW
    private String dueDate;
    private boolean completed;

    // getter/setter with validation
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    // ... 其他 getter/setter
}

// TaskManager 服务类
public class TaskManager {
    private final List<Task> tasks;
    private final TaskValidator validator;

    public boolean addTask(Task task) {
        if (!validator.isValid(task)) {
            return false;
        }
        tasks.add(task);
        return true;
    }

    // ... 其他管理方法
}
```

### ADR-001：领域模型设计

本周你还撰写了 ADR-001，记录了核心类的设计决策：
- 为什么 Task 和 TaskManager 分离？
- 为什么使用 String 存储优先级而不是枚举？（本周先用简单类型，Week 08 引入枚举后重构）
- 为什么 TaskValidator 要独立出来？

这些决策记录将帮助你在后续周次中保持设计一致性。

### 未来展望

- Week 03：为 CampusFlow 添加异常处理，当用户输入不合法时能优雅反馈
- Week 04：构建 CLI 交互界面，实现任务的命令行操作
- Week 07：添加数据持久化，将任务保存到数据库

---

## Definition of Done（学生自测清单）

### 知识理解
- [ ] 能用自己的话解释"上帝类"的问题
- [ ] 能说出封装的三个核心原则
- [ ] 能解释单一职责原则（SRP）的核心思想
- [ ] 能说明 ADR 的作用和基本结构

### 设计能力
- [ ] 能用"名词提取法"从需求中识别核心类
- [ ] 能区分"实体类"和"服务类"
- [ ] 能判断一个类是否违反 SRP
- [ ] 能为一个类设计合理的 getter/setter

### 代码能力
- [ ] 能从零设计并实现一个封装良好的类
- [ ] 能将"上帝类"拆分成多个职责单一的类
- [ ] 能编写符合规范的 ADR 文档

### 团队项目
- [ ] 核心领域模型设计完成（Task/Note/Book 等核心类）
- [ ] ADR-001 提交到 Git 仓库
- [ ] 代码职责清晰，没有"上帝类"
- [ ] 首席架构师第 1 轮完成
