# Week 03：异常处理与防御式编程

> "程序必须首先正确，然后才谈得上效率。"
> — C.A.R. Hoare

2024 年 7 月，CrowdStrike 的一次软件更新导致全球超过 850 万台 Windows 设备崩溃，机场停摆、医院系统瘫痪、银行无法交易，造成的经济损失估计超过 50 亿美元。事后调查发现，根本原因是异常处理代码中的一个边界条件判断失误——当遇到特定格式的数据时，系统没有优雅地降级，而是直接崩溃。

这个事件再次证明：**错误处理不是"锦上添花"，是工程责任**。在 AI 时代，我们用 AI 生成代码的速度越来越快，但 AI 往往遗漏边界情况——空输入、异常值、资源不可用。本周你将学习 Java 的异常处理机制，理解"受检异常"与"运行时异常"的区别，掌握防御式编程的原则，并学会用 FMEA（故障模式与影响分析）思维预判系统可能失败的方式。

---

## 学习目标

完成本周学习后，你将能够：
1. 区分受检异常（checked exception）与运行时异常（runtime exception），并知道何时使用哪种
2. 使用 try-catch-finally 结构优雅地处理异常
3. 应用防御式编程原则，在代码边界处进行输入验证
4. 使用 FMEA 思维分析代码的潜在故障点
5. 对比 Python 的"宽容"与 Java 的"严格"，理解两种哲学的工程含义

---

<!--
贯穿案例：CampusFlow CLI 的异常处理增强
- 第 1 节：展示没有异常处理的代码如何崩溃（用户输入非法日期、文件不存在等场景）
- 第 2 节：用 try-catch 捕获异常，让程序不崩溃但用户体验仍差
- 第 3 节：区分受检异常 vs 运行时异常，理解 Java 的设计哲学
- 第 4 节：应用防御式编程，在边界处验证输入，预防异常发生
- 第 5 节：用 FMEA 分析 CampusFlow 的潜在故障点，建立错误处理策略

最终成果：一个能优雅处理各种异常情况的 CampusFlow CLI 版本，包含完整的异常处理策略文档

认知负荷预算：
- 本周新概念（4 个，预算上限 4 个）：
  1. 受检异常（checked exception）
  2. 运行时异常（runtime exception）
  3. try-catch-finally 结构
  4. 防御式编程（defensive programming）
- 结论：✅ 在预算内

回顾桥设计（至少 2 个）：
- [类定义]（来自 week_02）：在第 1 节，通过"Task 类的方法抛出异常"再次使用类设计知识
- [封装]（来自 week_02）：在第 4 节，通过"在 setter 中验证输入"强化封装的价值
- [Scanner 输入]（来自 week_01）：在第 2 节，通过"处理用户输入异常"再次使用 Scanner

CampusFlow 本周推进：
- 上周状态：核心领域模型设计完成（Task、TaskManager 等类），ADR-001 已提交
- 本周改进：为所有核心类添加异常处理，确保用户输入非法数据时程序不崩溃
- 涉及的本周概念：受检异常、运行时异常、try-catch-finally、防御式编程
- 建议落盘位置：src/main/java/edu/campusflow/exception/ + 核心业务类的异常处理

角色出场规划：
- 小北：在第 1 节中运行程序时遇到 FileNotFoundException，不理解为什么 Java "强迫"他处理这个异常
- 阿码：在第 3 节中追问"为什么有些异常要强制处理，有些不用"，引出受检异常 vs 运行时异常的深层设计哲学
- 老潘：在第 5 节中用 FMEA 分析 CampusFlow 的故障点，给出工程视角的错误处理策略

AI 小专栏 #1（放在第 2 节之后）：
- 主题：AI 生成代码常遗漏异常处理
- 连接点：呼应第 2 节的 try-catch 基础——AI 生成的代码往往只有"happy path"，没有错误处理
- 建议搜索词："AI generated code error handling 2026" "AI code boundary cases missing 2025"

AI 小专栏 #2（放在第 4 节之后）：
- 主题：防御式编程在 AI 时代的价值
- 连接点：呼应第 4 节的防御式编程——AI 不会替你考虑"用户会怎么搞砸这个程序"
- 建议搜索词："defensive programming AI generated code 2026" "input validation security 2025"
-->

---

## 前情提要

上周你设计了 CampusFlow 的核心领域模型——Task、TaskManager 等类，理解了封装的价值和单一职责原则。你的代码结构已经清晰，但还有一个致命弱点：**它假设所有输入都是合法的**。

现实中，用户会输入非法日期（比如"2025-13-45"），文件可能不存在，网络可能断开。上周的代码遇到这些情况会直接崩溃。本周我们要给 CampusFlow 穿上"防弹衣"——让它在异常情况下依然能优雅地处理，而不是直接崩溃。

---

## 1. 当程序崩溃时——异常是什么

<!--
**Bloom 层次**：理解
**学习目标**：理解异常的本质，感受没有异常处理的代码问题
**贯穿案例推进**：展示 CampusFlow 在没有异常处理时的崩溃场景（文件不存在、日期解析失败等）
**建议示例文件**：01_crash_demo.java
**叙事入口**：从"上周的代码跑得好好的，直到..."的场景开头
**角色出场**：小北运行程序时遇到 FileNotFoundException，困惑为什么程序"说崩就崩"
**回顾桥**：[类定义]（week_02）：通过 Task 类的方法调用引出异常抛出点
-->

小北上周写的 TaskManager 跑得很好。他能添加任务、标记完成、筛选未完成任务——一切都很顺利。直到今天，他想给程序加一个"从文件加载任务"的功能。

"这应该很简单，"小北心想，"用 Scanner 读取文件，和上周从控制台读取差不多..."

```java
// 文件：TaskFileLoader.java
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TaskFileLoader {

    public void loadTasksFromFile(String filename) {
        // 小北想：用 Scanner 读取文件，和上周从控制台读取应该差不多
        File file = new File(filename);
        Scanner scanner = new Scanner(file);  // ← 这里出问题了？编译器直接报错

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("读取到: " + line);
        }
        scanner.close();
    }
}
```

小北自信满满地按下编译键——然后 Java 编译器给了他一记闷棍：

```
TaskFileLoader.java:10: 错误: 未报告的异常错误 FileNotFoundException; 必须对其进行捕获或声明以便抛出
        Scanner scanner = new Scanner(file);
                          ^
1 个错误
```

"什么？"小北盯着屏幕，"我只是想读取一个文件，为什么编译都不让过？"

### 异常是 Java 的错误处理机制

小北习惯了 Python 的方式——代码能编译，运行时才报错。但 Java 不一样：**有些错误，编译器强迫你在写代码时就考虑**。

这就是**异常**（Exception）——Java 用来处理"程序运行时可能发生的意外情况"的机制。当 `new Scanner(file)` 执行时，如果文件不存在，Java 会抛出一个 `FileNotFoundException`。这个异常如果不处理，程序就会崩溃。

```
Exception in thread "main" java.io.FileNotFoundException: tasks.txt (系统找不到指定的文件。)
    at java.io.FileInputStream.open0(Native Method)
    at java.io.FileInputStream.open(FileInputStream.java:211)
    at java.util.Scanner.<init>(Scanner.java:610)
    at TaskFileLoader.loadTasksFromFile(TaskFileLoader.java:10)
```

看到那一堆红色的堆栈跟踪（stack trace）了吗？这就是程序崩溃时的"死亡现场"。它告诉你：
- 发生了什么异常（`FileNotFoundException`）
- 在哪里发生的（`TaskFileLoader.java:10`）
- 调用链是什么（谁调了谁，最后调到出问题的代码）

### 为什么 Java 要这样设计

小北不解："Python 就不会强迫我处理这些，为什么 Java 这么严格？"

阿码在旁边插话："我查了一下，这叫**受检异常**（checked exception）。Java 设计者认为，文件不存在是'可预见的错误'，程序员必须在代码中显式处理。"

"但这样很麻烦啊，"小北抱怨，"我只是想快速试一下功能..."

老潘听到了，走过来："你觉得麻烦，是因为你还没被'运行时崩溃'教育过。"

他讲了一个故事：

> 三年前，我们有个生产系统，半夜崩溃了。原因是日志文件目录被运维误删，程序写入日志时抛出异常，没人处理，整个服务挂掉。
>
> 如果那是 Java 代码，编译器会强迫开发者考虑'文件可能写失败'的情况——也许在编译期就能发现问题。但那是 Python 代码，"能跑"，直到那个凌晨 3 点真的跑不了。

"Java 的严格不是在为难你，"老潘说，"是在保护你。**编译期能发现的错误，比运行期崩溃要便宜得多**——前者改一行代码，后者可能要赔客户钱。"

### 异常的本质

让我们退一步，理解异常到底是什么。

**异常是一个对象**。当程序遇到无法继续执行的情况时，Java 会创建一个异常对象，里面包含：
- 错误类型（`FileNotFoundException`、`NullPointerException` 等）
- 错误信息（"系统找不到指定的文件"）
- 发生位置（堆栈跟踪）

这个对象被"抛出"（throw），沿着调用链向上传递，直到有人"捕获"（catch）它，或者程序崩溃。

```
方法 A 调用 方法 B
    方法 B 调用 方法 C
        方法 C 中抛出异常
    异常向上传播到 方法 B
异常向上传播到 方法 A
如果 A 也不处理 → 程序崩溃
```

小北终于理解了："所以异常是 Java 的'安全气囊'——出问题时，它不会让程序默默崩溃，而是大声喊出来，强迫我处理。"

"对，"老潘点头，"但光'喊出来'不够，你得学会'接住它'。这就是下一节的内容。"

---

## 2. 捕获异常——让程序不崩溃

<!--
**Bloom 层次**：应用
**学习目标**：掌握 try-catch-finally 的基本用法
**贯穿案例推进**：给 CampusFlow 添加 try-catch，让程序不崩溃但用户体验仍粗糙
**建议示例文件**：02_try_catch_basic.java
**叙事入口**："程序不崩溃只是第一步，用户需要知道发生了什么"
**角色出场**：阿码尝试各种边界输入，发现 try-catch 能捕获异常但信息不够友好
**回顾桥**：[Scanner 输入]（week_01）：处理用户输入时的 NumberFormatException
-->

小北现在明白了：Java 强迫他处理 `FileNotFoundException`。但怎么处理？

答案是 **try-catch** 结构——Java 捕获异常的核心机制。

```java
// 文件：TaskFileLoader.java（改进版）
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TaskFileLoader {

    public void loadTasksFromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("读取到: " + line);
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("错误：找不到文件 '" + filename + "'");
        }
    }
}
```

现在代码能编译了。运行一下：

```
错误：找不到文件 'tasks.txt'
```

程序没有崩溃，而是优雅地打印了一条错误信息，然后继续执行。

### try-catch 的工作原理

`try` 块里放"可能抛出异常的代码"，`catch` 块里放"异常发生时的处理逻辑"。

执行流程是这样的：
1. 进入 `try` 块，正常执行
2. 如果一切顺利，`catch` 块被跳过
3. 如果在 `try` 块中抛出异常，立即跳转到匹配的 `catch` 块
4. 执行 `catch` 块中的代码，然后继续执行 `catch` 块之后的代码

```java
try {
    // 步骤 1：尝试执行
    // 如果这里抛出异常 → 跳到 catch
    // 如果正常完成 → 跳过 catch
} catch (异常类型 变量名) {
    // 步骤 2：异常处理
}
// 步骤 3：继续执行
```

### 阿码的边界测试

阿码看到小北的代码，眼睛里闪过一丝狡黠。他决定做个"破坏性测试"。

"如果用户输入的文件名是空字符串呢？"阿码问。

小北试了一下——程序输出"错误：找不到文件 ''"，没有崩溃。小北松了口气。

"那如果文件名是 null 呢？"

"不会吧，谁会传 null..."小北嘟囔着，但还是试了一下：

```
Exception in thread "main" java.lang.NullPointerException
    at java.io.File.<init>(File.java:278)
```

"又崩溃了！"小北叫起来。

"因为你只捕获了 `FileNotFoundException`，但 `new File(null)` 抛出的是 `NullPointerException`，"阿码耸耸肩，"用户永远比你想象的更有创意。"

小北翻了个白眼："好吧，我得捕获多种异常。"

### 多 catch 块

Java 允许你有多个 `catch` 块，处理不同类型的异常：

```java
public void loadTasksFromFile(String filename) {
    try {
        if (filename == null) {
            throw new IllegalArgumentException("文件名不能为 null");
        }

        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("读取到: " + line);
        }
        scanner.close();

    } catch (FileNotFoundException e) {
        System.out.println("错误：找不到文件 '" + filename + "'");

    } catch (IllegalArgumentException e) {
        System.out.println("错误：" + e.getMessage());
    }
}
```

注意：**子类异常的 catch 块要放在父类之前**。如果先 catch `Exception`（所有异常的父类），后面更具体的 catch 块就永远不会被执行。

### finally——无论如何都要执行

阿码又发现了一个问题："如果 `scanner.close()` 之前抛出异常，文件句柄就没关闭。"

小北不解："但在 catch 块里程序不继续执行了吗？"

"如果异常发生在读取过程中呢？比如文件读了一半磁盘坏了。"阿码说，"你需要 `finally` 块——**无论是否发生异常，都会执行**。"

```java
public void loadTasksFromFile(String filename) {
    Scanner scanner = null;

    try {
        File file = new File(filename);
        scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("读取到: " + line);
        }

    } catch (FileNotFoundException e) {
        System.out.println("错误：找不到文件 '" + filename + "'");

    } finally {
        // 无论是否发生异常，都会执行这里
        if (scanner != null) {
            scanner.close();
            System.out.println("文件已关闭");
        }
    }
}
```

`finally` 块的典型用途是**资源清理**：关闭文件、关闭数据库连接、释放锁等。

### 回顾 Week 01 的 Scanner

还记得 Week 01 我们用 `Scanner` 读取用户输入吗？

```java
Scanner scanner = new Scanner(System.in);
System.out.print("请输入年龄：");
int age = scanner.nextInt();  // 如果用户输入"abc"？
```

如果用户输入的不是数字，`nextInt()` 会抛出 `InputMismatchException`。当时我们没有处理，程序会直接崩溃。

现在你知道怎么处理了：

```java
Scanner scanner = new Scanner(System.in);
System.out.print("请输入年龄：");

try {
    int age = scanner.nextInt();
    System.out.println("你的年龄是：" + age);
} catch (InputMismatchException e) {
    System.out.println("错误：请输入有效的数字");
    scanner.next();  // 清除错误的输入
}
```

这就是异常处理的价值：**把"程序崩溃"变成"用户友好的错误提示"**。

但小北还是觉得哪里不对："为什么有些异常 Java 强迫我处理（像 `FileNotFoundException`），有些却不用（像 `NullPointerException`）？"

"好问题，"老潘正好路过，"这就是下一节要讲的——受检异常和运行时异常的区别。"

---

> **AI 时代小专栏：AI 生成代码常遗漏异常处理**
>
> 阿码最近迷上了用 AI 助手生成代码。"你看，"他得意地给小北看一段刚生成的 Java 方法，"几秒钟就写好了文件读取功能！"
>
> 小北扫了一眼："等等，这段代码只有 'happy path'——如果文件不存在怎么办？如果读取到一半磁盘坏了怎么办？"
>
> 阿码愣了一下。确实，AI 生成的代码看起来完美无缺，但仔细检查就会发现它遗漏了所有错误处理。
>
> 这不是个例。根据 Stack Overflow 2026 年 1 月的研究，AI 生成的 PR 遗漏错误检查的可能性几乎是人类程序员的两倍——空指针检查、提前返回、防御性编码都是 AI 的盲区。[Stack Overflow 研究](https://stackoverflow.blog/2026/01/28/are-bugs-and-incidents-inevitable-with-ai-coding-agents/)
>
> 更惊人的数据来自 LinearB 2026 基准测试：AI 生成代码的 PR 接受率只有 32.7%，而人工 PR 是 84.4%。30-40% 的 AI 代码需要重大返工，最常见的原因就是"缺乏错误处理"和"在边界情况下会崩溃"。[LinearB 基准测试](https://www.secondtalent.com/resources/ai-generated-code-quality-metrics-and-statistics-for-2026/)
>
> AI 最容易遗漏的错误模式包括：
> - **缺少空检查**：假设对象永远不会为 null
> - **不完整的异常处理**：要么没有 try-catch，要么捕获过于宽泛
> - **缺少边界检查**：数组越界、整数溢出、空输入
> - **资源清理缺口**：错误路径中未关闭的连接
>
> 所以你刚学的 try-catch-finally 不是"过时的语法"——它是你审查 AI 代码、补充边界情况的核心能力。AI 能帮你写 80% 的代码，但剩下 20% 的错误处理，需要你自己来判断。
>
> 参考（访问日期：2026-02-11）：
> - [Stack Overflow 2026年1月研究](https://stackoverflow.blog/2026/01/28/are-bugs-and-incidents-inevitable-with-ai-coding-agents/)
> - [LinearB 2026 基准测试](https://www.secondtalent.com/resources/ai-generated-code-quality-metrics-and-statistics-for-2026/)

---

## 3. 受检异常 vs 运行时异常——Java 的严格哲学

<!--
**Bloom 层次**：分析
**学习目标**：区分两种异常类型，理解 Java 的设计哲学
**贯穿案例推进**：分析 CampusFlow 中哪些异常应该是受检的，哪些应该是运行时的
**建议示例文件**：03_checked_vs_unchecked.java
**叙事入口**："为什么有些异常 Java 强迫你处理，有些不用？"
**角色出场**：阿码追问"为什么有些异常要强制处理，有些不用"，引出深层设计哲学
**回顾桥**：[静态类型]（week_01）：对比 Java 的"编译期严格"与 Python 的"运行时宽容"
-->

阿码的疑问触及了 Java 异常体系的核心。

Java 把所有异常分成两大类。

**受检异常（Checked Exception）**是编译器强迫你处理或声明的那一类。它们继承自 `Exception`（但不包括 `RuntimeException`），像 `IOException`、`SQLException`、`FileNotFoundException` 都属于这一类。这类异常代表"可预见的、应该恢复的"错误——文件可能不存在，网络可能断开，这些不是程序员的错，但必须在代码中考虑。

**运行时异常（Runtime Exception）**则不需要强制处理。它们继承自 `RuntimeException`，像 `NullPointerException`、`IllegalArgumentException`、`IndexOutOfBoundsException` 都是典型代表。这类异常代表"编程错误"——它们通常不应该被捕获，而应该通过修复代码来避免。

用继承树来看会更清晰：

```
Throwable
├── Error（系统级错误，不处理）
└── Exception
    ├── RuntimeException（运行时异常，不强制处理）
    └── 其他 Exception（受检异常，强制处理）
```

### 为什么这样设计

阿码追问："这背后的设计逻辑是什么？为什么 `FileNotFoundException` 要强制处理，而 `NullPointerException` 不用？"

老潘坐下来，没有直接回答，而是反问："你觉得'文件不存在'和'代码里有个 null'，哪个是程序员的错？"

阿码想了想："null 是程序员的错，文件不存在...可能是用户删了文件？"

"完全正确。"老潘点头，"这就是 Java 的设计哲学——"

> **受检异常代表"外部不确定性"**。
>
> 文件是否存在、网络是否通畅、数据库是否可用——这些不是你能控制的。Java 强迫你考虑这些情况，是因为**生产环境中它们真的会发生**。
>
> **运行时异常代表"编程错误"**。
>
> 空指针、数组越界、非法参数——这些是你的代码有 bug。你不应该捕获它们然后继续运行，而应该**修复代码**，让 bug 不发生。

"所以，"小北总结，"受检异常是'提醒我考虑外部风险'，运行时异常是'告诉我代码有 bug'？"

"完全正确，"老潘点头，"这是 Java 的**工程哲学**：把'外部风险'和'内部错误'分开，用编译器强制你处理前者，用崩溃提醒你修复后者。"

### 两种哲学的对比

这又让我们回到了 Week 01 的话题：Java 的"严格" vs Python 的"宽容"。

| 维度 | Java | Python |
|------|------|--------|
| 类型检查 | 编译期 | 运行期 |
| 异常处理 | 受检异常强制处理 | 不区分，统一运行期处理 |
| 错误发现 | 越早越好 | 灵活优先 |
| 适用场景 | 大型系统、长期维护 | 快速原型、脚本工具 |

"Python 也有它的道理，"老潘说，"快速原型时，你确实不想被编译器打断思路。但当你要构建一个需要维护 5 年、10 年的系统时，Java 的严格性就是优势。"

### 实践：为 CampusFlow 选择异常类型

现在让我们为 CampusFlow 设计异常策略。

**场景 1：任务文件不存在**
```java
// 受检异常——调用者必须处理
public void loadFromFile(String filename) throws FileNotFoundException {
    // 文件是否存在是外部因素，调用者应该决定如何处理
}
```

**场景 2：任务标题为空**
```java
// 运行时异常——编程错误，不应该发生
public void setTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
        throw new IllegalArgumentException("标题不能为空");
    }
    this.title = title;
}
```

**场景 3：任务找不到**
```java
// 受检异常还是运行时异常？这取决于业务语义

// 方案 A：受检异常——强制调用者处理
public Task findTask(String title) throws TaskNotFoundException {
    // 如果任务不存在是"正常情况"，用受检异常
}

// 方案 B：运行时异常——编程错误
public Task getTask(String title) {
    Task task = findInList(title);
    if (task == null) {
        throw new TaskNotFoundRuntimeException(title);
    }
    return task;
}
```

阿码问："怎么决定用哪种？"

老潘给了一个简单的判断标准：

> **如果调用者可以合理地恢复，用受检异常。**
> 例如：文件不存在时，调用者可以提示用户选择其他文件。
>
> **如果调用者无法恢复，用运行时异常。**
> 例如：空指针、数组越界——这些是 bug，调用者除了崩溃别无选择。

### 一个反直觉的事实

小北突然想到一个问题："等等，那 `Integer.parseInt()` 抛出的 `NumberFormatException` 为什么是运行时异常？"

他指着 Week 01 的代码："用户输入 'abc' 想转成数字，这不是'外部不确定性'吗？为什么不设计成受检异常，强迫我处理？"

老潘笑了："问得好。这确实是 Java 设计中最常被争论的决定之一。"

"官方理由是：如果 `NumberFormatException` 是受检异常，那每次调用 `parseInt()` 都要 try-catch，代码会臃肿不堪。而且，**在大多数场景下，你确实无法'恢复'——用户输入了垃圾，你能怎么办？**"

"但这里有个坑，"老潘压低声音，"很多人因此忽略了输入验证。他们觉得'反正会抛异常，让上层处理'——结果上层也这么想，最后没人处理。"

阿码恍然大悟："所以这就是为什么我们学了防御式编程！与其依赖异常，不如在调用 `parseInt()` 之前先验证输入格式。"

"完全正确，"老潘点头，"`NumberFormatException` 是最后一道防线，不是第一道。好的代码应该先用正则表达式验证 '是不是数字'，再调用 `parseInt()`。这样你甚至不会走到异常那一步。"

小北感叹："原来异常类型的选择背后有这么多权衡..."

### 自定义异常

CampusFlow 应该有自己的异常类，而不是到处抛 `Exception`。

```java
// 基础领域异常（受检）
public class CampusFlowException extends Exception {
    public CampusFlowException(String message) {
        super(message);
    }

    public CampusFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}

// 具体业务异常
public class TaskNotFoundException extends CampusFlowException {
    public TaskNotFoundException(String taskTitle) {
        super("找不到任务: " + taskTitle);
    }
}

public class InvalidTaskDataException extends CampusFlowException {
    public InvalidTaskDataException(String field, String reason) {
        super("任务数据无效 [" + field + "]: " + reason);
    }
}
```

自定义异常的好处：
1. **语义清晰**：`TaskNotFoundException` 比 `Exception` 更明确
2. **便于捕获**：可以只捕获特定类型的异常
3. **统一处理**：所有业务异常继承自 `CampusFlowException`，便于上层统一处理

小北现在理解了 Java 的异常哲学："所以异常类型不是随便选的，它反映了'这个错误是谁的责任、能否恢复'。"

"对，"老潘说，"但最好的异常处理是**不让异常发生**。这就是防御式编程——下一节的内容。"

---

> **AI 时代小专栏：防御式编程在 AI 时代的价值**
>
> "用户会输入什么？"老潘问阿码。
>
> "正常的数据吧..."阿码犹豫了一下。
>
> "错。用户会输入**一切**——包括 AI 生成的垃圾数据。"
>
> 老潘讲了一个真实案例：某团队用 AI 生成了一批测试数据导入系统，结果 AI 生成的日期格式混用了 "2025-01-15" 和 "01/15/2025" 两种格式。系统没有严格验证输入，导致数据库里一半数据是脏数据，清理花了整整一周。
>
> 这正是防御式编程在 AI 时代变得更重要的原因。根据 2025 年的安全研究，**45% 的 AI 生成代码**未能通过安全测试，会引入 OWASP Top 10 漏洞。代码弱点占数据泄露事件的 40% 以上。[Secure Coding 2025 最佳实践](https://cygnostic.io/4-best-practices-for-secure-code-development-in-2025/)
>
> 为什么 AI 特别容易在安全上翻车？
>
> 首先，AI 训练数据里有大量"能跑就行"的示例代码，这些代码往往缺少输入验证。其次，AI 不理解你的业务上下文——它不知道"任务标题"在你的系统里有什么约束。
>
> 防御式编程的三层防线在 AI 时代尤其重要：
>
> **第一层：客户端验证**——给用户即时反馈，但不要相信它（可被绕过）。
>
> **第二层：服务端/API 验证**——真正的安全基础。研究显示，API 网关在执行环境前阻止了 96.7% 的注入攻击。
>
> **第三层：白名单思维**——定义"什么是合法的"，而不是"什么是非法的"。OWASP 明确推荐白名单（允许列表）而非黑名单，因为攻击者总能找到绕过黑名单的方法。
>
> 老潘最后说："AI 能生成代码，但不会替你承担安全责任。当你用 AI 生成 setter 方法时，记得问自己——如果 AI 传进来一个 10MB 的字符串，我的程序会崩吗？"
>
> 这就是你刚学的防御式编程的真正价值：**AI 生成代码，你来守住边界**。
>
> 参考（访问日期：2026-02-11）：
> - [Secure Coding: 2025 最佳实践](https://cygnostic.io/4-best-practices-for-secure-code-development-in-2025/)
> - [Secure Coding 2026 指南](https://www.securityjourney.com/post/best-practices-for-secure-coding)

---

## 4. 防御式编程——在错误发生前阻止它

<!--
**Bloom 层次**：应用
**学习目标**：应用防御式编程原则，在边界处验证输入
**贯穿案例推进**：在 CampusFlow 的 setter 和构造函数中添加输入验证，预防异常发生
**建议示例文件**：04_defensive_programming.java
**叙事入口**："最好的异常处理是不让异常发生"
**角色出场**：老潘点评"用户永远会做你想不到的事情"，给出工程视角
**回顾桥**：[封装]（week_02）：在 setter 中验证输入，强化封装的价值
-->

老潘看了一眼小北的代码，摇摇头："你在 catch 块里处理了很多异常，但有没有想过——**很多异常根本不应该发生**？"

"什么意思？"小北问。

"你看这段代码："

```java
// 小北的代码（问题版）
public void addTask(Task task) {
    try {
        taskManager.add(task);
    } catch (NullPointerException e) {
        System.out.println("错误：任务不能为 null");
    }
}
```

"你在捕获 `NullPointerException`，但更好的做法是**不让 null 传进来**。"

### 防御式编程的核心思想

**防御式编程**（Defensive Programming）的核心是：**在代码边界处验证输入，把非法数据挡在系统外面**。

```java
// 改进版：在入口处防御
public void addTask(Task task) {
    if (task == null) {
        throw new IllegalArgumentException("任务不能为 null");
    }
    taskManager.add(task);
}
```

这样，`NullPointerException` 就不会在 `taskManager.add()` 内部发生了。

老潘说："记住这句话：**用户永远会做你想不到的事情**。"

他顿了顿，眼神变得严肃："去年，我亲历了一场'字符灾难'。一个用户在备注字段粘贴了一篇完整的 Wikipedia 文章——整整 12 万字符。系统没做长度限制，数据库字段是 VARCHAR(255)，结果数据被截断，但程序继续运行，把半截任务记录写进了下游的结算系统。"

"然后呢？"小北问。

"然后？"老潘苦笑，"结算金额算错了，客户投诉，我们三个工程师通宵回滚数据。就因为少写了一行 `if (title.length() > 100)`。**防御式编程不是洁癖，是保险丝**——它熔断的时候，你可能会骂它碍事；但它要是没了，烧掉的就是整栋房子。"

> 用户会输入空字符串。
> 用户会输入 10000 个字符的标题。
> 用户会输入 "2025-13-45" 这样的日期。
> 用户会在要求数字的地方输入 "abc"。
>
> 不要信任任何外部输入。验证它，或者拒绝它。

### 在封装中实现防御

还记得 Week 02 学的**封装**吗？`private` 字段 + `public` getter/setter 不只是"规范"，更是**防御的阵地**。

```java
public class Task {
    private String title;
    private String dueDate;
    private int priority;  // 1=高, 2=中, 3=低

    // 在 setter 中防御
    public void setTitle(String title) {
        // 防御 1：不能为空
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        // 防御 2：长度限制
        if (title.length() > 100) {
            throw new IllegalArgumentException("标题不能超过 100 字符");
        }
        // 防御 3：去除首尾空格
        this.title = title.trim();
    }

    public void setDueDate(String dueDate) {
        // 防御：日期格式验证
        if (!isValidDateFormat(dueDate)) {
            throw new IllegalArgumentException("日期格式无效，应为 YYYY-MM-DD");
        }
        this.dueDate = dueDate;
    }

    public void setPriority(int priority) {
        // 防御：枚举值验证
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级必须是 1（高）、2（中）或 3（低）");
        }
        this.priority = priority;
    }

    private boolean isValidDateFormat(String date) {
        // 简单的日期格式验证
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}
```

这就是**封装的价值**：类的内部状态由类自己保护，外部无法破坏。

### 防御式编程的原则

老潘总结了几个防御式编程的原则。

**尽早失败（Fail Fast）**的意思是：不要等错误扩散到系统深处才报错，在入口就检查。想象一下，如果 `task` 是 null，你在第 10 行才发现和在入口处就发现，哪个更容易调试？显然是后者。

```java
// ❌ 不好的做法：让错误扩散
public void processTask(Task task) {
    // 假设 task 不为 null，继续处理
    String title = task.getTitle();  // 如果 task 为 null，这里才崩溃
    // ... 更多代码
}

// ✅ 好的做法：尽早检查
public void processTask(Task task) {
    if (task == null) {
        throw new IllegalArgumentException("task 不能为 null");
    }
    // 现在可以安全地假设 task 不为 null
    String title = task.getTitle();
}
```

**验证所有输入**——任何来自外部的数据都要验证：用户输入、文件内容、网络响应、数据库记录。不要假设它们会符合预期。

```java
public class TaskManager {
    public void addTask(Task task) {
        // 防御 null
        if (task == null) {
            throw new IllegalArgumentException("任务不能为 null");
        }

        // 防御重复
        if (findTask(task.getTitle()) != null) {
            throw new IllegalArgumentException("任务 '" + task.getTitle() + "' 已存在");
        }

        tasks.add(task);
    }
}
```

**使用断言记录假设**——对于"不应该发生"的情况，使用断言（assert）记录你的假设。这不仅是防御，也是文档——告诉阅读代码的人"到这里，task 一定不为 null"。

```java
public void completeTask(String title) {
    Task task = findTask(title);
    // 断言：根据前置条件，task 不应该为 null
    assert task != null : "任务 '" + title + "' 应该存在";

    task.markCompleted();
}
```

注意：断言默认是关闭的（运行时需要加 `-ea` 参数开启），所以**不要用断言替代输入验证**。

### 防御 vs 异常处理

防御式编程和异常处理不是对立的，而是**互补的**：

| 防御式编程 | 异常处理 |
|-----------|---------|
| 预防错误发生 | 处理无法预防的错误 |
| 在边界处验证输入 | 在异常发生时恢复 |
| 抛出运行时异常（编程错误） | 捕获受检异常（外部风险） |
| 目标是"不让异常发生" | 目标是"异常发生时优雅降级" |

小北现在明白了："所以最佳实践是——先用防御式编程阻止可预见的错误，再用异常处理应对真正的意外？"

"完全正确，"老潘说，"但怎么知道'哪些错误是可预见的'？这需要系统性的风险思维。这就是下一节的 FMEA。"

---

## 5. FMEA 思维——系统性地预判故障

<!--
**Bloom 层次**：评价
**学习目标**：使用 FMEA 方法分析代码的潜在故障点
**贯穿案例推进**：用 FMEA 表格分析 CampusFlow 的故障模式，建立错误处理策略
**建议示例文件**：05_fmea_analysis.md（文档而非代码）
**叙事入口**："不要等用户报告 bug，提前想象所有可能出错的地方"
**角色出场**：老潘带领团队做 FMEA 分析，展示工程化的风险思维
**回顾桥**：[单一职责原则]（week_02）：异常处理也是类的职责之一
-->

老潘把团队召集到一起，没有直接讲 FMEA，而是先讲了一个故事。

"三年前，我带的一个项目，上线前我觉得代码已经很完善了——该有 try-catch 的地方都有，该验证的输入都验证了。"老潘顿了顿，"结果上线第一天，数据库连接池耗尽，整个系统瘫痪了四个小时。"

小北问："是代码有 bug 吗？"

"不是 bug，"老潘摇头，"是我们**根本没想过这个问题**。当时每个数据库操作都有 try-catch，但我们假设'数据库永远可用'。没人问一句：如果连接池满了怎么办？如果连接超时怎么办？"

阿码问："后来怎么解决的？"

"加了一个连接池监控和熔断机制。但问题是，这个故障本可以提前预判。"老潘说，"**被动地处理 bug，不如主动地预判风险**。"

他打开白板，写下三个字母：**FMEA**。

"这不是马后炮，"老潘说，"这是 NASA 在阿波罗登月前用的方法，是波音造飞机前用的方法。现在，我们用它在写代码前就想清楚'哪里可能出错'。"

### 为什么需要 FMEA

小北有些困惑："我们已经学了 try-catch 和防御式编程，为什么还要 FMEA？"

老潘在白板上画了两个圈："try-catch 是'事后处理'——异常发生了，你怎么应对。防御式编程是'事中拦截'——在边界处挡住坏数据。但 FMEA 是'事前预判'——**在写第一行代码前，就想清楚哪里可能出错**。

"问题是，"老潘继续说，"你怎么知道该在哪里加 try-catch？该验证哪些输入？如果没有系统性的方法，你只会处理'已经遇到过的 bug'，而遗漏'还没发生但可能发生的问题'。就像我当年的连接池故障——不是代码写错了，是**根本没想到**。

"FMEA 就是帮你'想到'的系统化方法。"

### 什么是 FMEA

**FMEA**（Failure Mode and Effects Analysis，故障模式与影响分析）是一种系统化的风险管理方法。它起源于航空航天工程，现在广泛应用于软件开发。

FMEA 的核心问题是：
1. **什么可能出错？**（故障模式）
2. **出错后会发生什么？**（影响）
3. **怎么预防？**（预防措施）
4. **怎么检测？**（检测方法）

### CampusFlow 的 FMEA 分析

"好，"老潘说，"现在我们来分析 CampusFlow。假设明天就要上线了，什么可能出错？"

小北想了想："用户输入空标题？"

"对，还有呢？"老潘在白板上写。

阿码补充："文件被删了？日期格式不对？"

"很好，"老潘继续引导，"还有更隐蔽的——如果任务标题是 10000 个字符呢？如果文件存在但格式是乱码呢？"

老潘把这些整理成 FMEA 表格：

```
┌─────────────┬─────────────────┬──────────┬─────────────┬─────────────┐
│  功能模块   │    潜在故障     │   影响   │   预防措施  │   处理策略  │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  任务创建   │  空标题         │  数据无  │  setter 验证 │ 抛出运行时  │
│             │                 │  效      │             │ 异常        │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  任务创建   │  标题超长       │  显示错  │  setter 验证 │ 抛出运行时  │
│             │  (>100字符)     │  乱      │             │ 异常        │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  任务查询   │  任务不存在     │  NPE 或  │  防御式检查  │ 返回 Optional│
│             │                 │  逻辑错误│             │ 或抛异常    │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  文件加载   │  文件不存在     │  程序崩  │  前置检查   │ 捕获受检异常 │
│             │                 │  溃      │             │ 提示用户    │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  文件加载   │  文件格式错误   │  解析失  │  格式验证   │ 捕获异常，  │
│             │                 │  败      │             │ 记录错误行  │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  日期解析   │  非法日期格式   │  程序崩  │  正则验证   │ 抛出运行时  │
│             │  (2025-13-45)   │  溃      │             │ 异常        │
└─────────────┴─────────────────┴──────────┴─────────────┴─────────────┘
```

"看，"老潘说，"用 FMEA，你不是等 bug 出现再修，而是**提前想象所有可能出错的地方**，并制定策略。"

### FMEA 的优先级：RPN 计算

"这么多潜在故障，都要处理吗？"小北问。

"当然不是，"老潘说，"要排优先级。FMEA 用 **RPN**（Risk Priority Number，风险优先级数）来排序。"

他在白板上写下公式：

```
RPN = 严重程度(S) × 发生频率(O) × 检测难度(D)
```

"每个维度 1-10 分。我来举个例子——"

老潘指着表格中的第一行："'任务标题为空'这个故障。严重程度算中等吧，空标题不会导致系统崩溃，但数据确实无效，给 5 分。发生频率呢？如果用户界面没做校验，偶尔会发生，给 3 分。检测难度？很容易，setter 里一检查就能发现，给 2 分。"

他在白板上算道："5 × 3 × 2 = **30**。RPN 是 30，优先级较低。"

"再看这个——'数据库连接池耗尽'。"老潘的表情严肃起来，"严重程度最高（10），系统瘫痪。发生频率看似低（2），毕竟正常情况不会耗尽。但检测难度呢？最难检测（8）——等你发现系统瘫了，问题已经发生了，而且你根本不知道连接池是什么时候开始泄漏的。"

"10 × 2 × 8 = **160**。"

小北倒吸一口凉气："160 对比 30... 差了五倍多！"

"对，"老潘点头，"这就是为什么我当年那个项目瘫痪了四个小时。如果做了 FMEA，连接池耗尽这个风险会被标记为高优先级，上线前就会有监控和熔断机制。"

"RPN 高的要优先处理，"老潘总结，"不要平均用力。先处理 RPN > 100 的高风险项，再处理 50-100 的中风险，低风险的可以暂时记录但不急于实现。"

阿码举手问："那如果两个故障的 RPN 一样呢？"

"优先处理严重程度高的，"老潘回答，"一个 S=8、O=5、D=5（RPN=200）的故障，比一个 S=5、O=8、D=5（也是 RPN=200）的更危险。因为严重程度是'一旦发生就挽回不了'的维度。"

### 从 FMEA 到代码

FMEA 不是文档工作，它直接指导代码实现。

```java
public class TaskService {

    // FMEA 识别：任务不存在 → 返回 Optional 而不是 null
    public Optional<Task> findTask(String title) {
        return tasks.stream()
            .filter(t -> t.getTitle().equals(title))
            .findFirst();
    }

    // FMEA 识别：文件加载失败 → 捕获异常，优雅降级
    public List<Task> loadTasksFromFile(String filename) {
        List<Task> loadedTasks = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filename))) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine();
                try {
                    Task task = parseTask(line);
                    loadedTasks.add(task);
                } catch (IllegalArgumentException e) {
                    // FMEA 策略：记录错误，继续处理其他行
                    System.err.println("第 " + lineNumber + " 行解析失败: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            // FMEA 策略：文件不存在不是致命错误，返回空列表
            System.out.println("提示：文件 '" + filename + "' 不存在，将创建新文件");
        }

        return loadedTasks;
    }

    private Task parseTask(String line) {
        // 解析逻辑，可能抛出 IllegalArgumentException
        // ...
    }
}
```

### FMEA 与单一职责原则

还记得 Week 02 学的**单一职责原则**吗？异常处理也是类的职责之一。

```java
// ❌ 不好的做法：TaskManager 什么都管
public class TaskManager {
    public void addTask(Task task) {
        // 验证
        // 存储
        // 日志
        // 异常处理
    }
}

// ✅ 好的做法：职责分离
public class TaskValidator {
    public void validate(Task task) { /* FMEA 预防层 */ }
}

public class TaskRepository {
    public void save(Task task) throws IOException { /* FMEA 处理层 */ }
}

public class TaskLogger {
    public void logError(String message) { /* FMEA 检测层 */ }
}
```

"FMEA 和 SOLID 是相通的，"老潘说，"**风险预防是职责，应该被封装在专门的类中**。"

小北现在理解了："FMEA 不是额外的文档工作，是**工程化的风险思维**。它帮我在写代码前就想清楚'哪里可能出错、怎么处理'。"

"对，"老潘收起白板笔，最后说道，"而且记住：**AI 不会替你考虑这些**。AI 生成代码时不会自动做 FMEA 分析，它只会生成'happy path'。这是你作为工程师的核心价值——不是写代码的速度，而是预判风险的能力。"

阿码在一旁默默点头，把白板上的 RPN 公式拍了下来。"

---

## CampusFlow 进度

还记得上周的 CampusFlow 吗？如果你在菜单选择时输入"abc"而不是数字，或者在添加任务时输入一个非法日期——它会直接崩溃，带着一堆红色的堆栈跟踪信息。

本周，CampusFlow 穿上了"防弹衣"。现在它能优雅地处理各种异常输入，给用户清晰的错误提示，而不是粗暴地崩溃。

### 异常类设计

为 CampusFlow 定义领域异常：

```java
// 基础领域异常
public class CampusFlowException extends Exception {
    public CampusFlowException(String message) {
        super(message);
    }
}

// 具体业务异常
public class TaskNotFoundException extends CampusFlowException {
    public TaskNotFoundException(String taskTitle) {
        super("找不到任务: " + taskTitle);
    }
}

public class InvalidTaskDataException extends CampusFlowException {
    public InvalidTaskDataException(String field, String reason) {
        super("任务数据无效 [" + field + "]: " + reason);
    }
}
```

### 防御式输入验证

在 Task 类中添加输入验证：

```java
public class Task {
    private String title;
    private String dueDate;

    public void setTitle(String title) throws InvalidTaskDataException {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidTaskDataException("title", "标题不能为空");
        }
        if (title.length() > 100) {
            throw new InvalidTaskDataException("title", "标题不能超过100字符");
        }
        this.title = title.trim();
    }

    // 类似地验证其他字段...
}
```

### FMEA 分析实践

为 CampusFlow 做简化版 FMEA：

```
┌─────────────┬─────────────────┬──────────┬─────────────┬─────────────┐
│  功能模块   │    潜在故障     │   影响   │   预防措施  │   处理策略  │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  任务创建   │  空标题         │  数据无  │  setter 验证 │ 抛出运行时  │
│             │                 │  效      │             │ 异常        │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  任务查询   │  任务不存在     │  NPE 或  │  防御式检查  │ 返回 Optional│
│             │                 │  逻辑错误│             │ 或抛异常    │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  文件加载   │  文件不存在     │  程序崩  │  前置检查   │ 捕获受检异常 │
│             │                 │  溃      │             │ 提示用户    │
├─────────────┼─────────────────┼──────────┼─────────────┼─────────────┤
│  日期解析   │  非法日期格式   │  程序崩  │  正则验证   │ 抛出运行时  │
│             │  (2025-13-45)   │  溃      │             │ 异常        │
└─────────────┴─────────────────┴──────────┴─────────────┴─────────────┘
```

### 验证清单

- [ ] 所有用户输入点都有验证
- [ ] 自定义异常继承层次清晰
- [ ] try-catch 不吞异常（至少记录日志）
- [ ] FMEA 文档提交到 docs/fmea.md

---

## Git 本周要点

### 必会命令

异常处理相关的代码经常需要调试和回滚。这几个命令会帮到你：

```bash
# 查看提交历史（找 bug 时很有用）
git log --oneline -10

# 查看某文件的修改历史
git log -p src/main/java/Task.java

# 创建修复分支
git checkout -b fix/exception-handling

# 合并回主分支
git checkout main
git merge fix/exception-handling
```

### Commit Message 规范

本周的 commit 示例：

```bash
git add src/main/java/exception/
git commit -m "feat: 添加领域异常类体系"

git add src/main/java/domain/Task.java
git commit -m "feat: Task 类添加防御式输入验证"

git add docs/fmea.md
git commit -m "docs: 添加 FMEA 故障分析文档"
```

### 常见坑

**吞异常**是最隐蔽的错误——catch 块里什么都不做，问题被隐藏了，程序继续运行，但数据可能已经损坏。

**过度使用受检异常**会让代码变得臃肿。如果一个方法 throws 了 5 个不同的受检异常，调用者会被迫写一大段 catch 块。考虑用自定义异常包装，或者把某些异常转成运行时异常。

**忽略 FMEA**是思维层面的问题。只处理已发生的 bug，不预防潜在问题，会让你永远处于"救火"状态。花 30 分钟做 FMEA 分析，可能省下几小时的调试时间。

---

## 本周小结（供下周参考）

本周我们从"程序崩溃"的问题出发，建立了完整的异常处理思维。

异常是 Java 处理错误的机制。没有异常处理的代码就像没有安全气囊的汽车——一出事就完蛋。小北在第 1 节遇到的 FileNotFoundException 让你感受到：异常处理不是可选的，是必需的。

try-catch-finally 是捕获异常的基本工具。但仅仅让程序不崩溃还不够——用户需要知道发生了什么。阿码在第 2 节的实验让你明白：异常处理的目标是**优雅降级**，不是**隐藏问题**。

Java 的受检异常 vs 运行时异常设计体现了它的工程哲学：**在编译期就强制你考虑错误情况**。这与 Python 的"宽容"形成对比——Python 让你在运行时发现问题，Java 强迫你在写代码时就思考。两种哲学各有优劣，但理解 Java 的严格性有助于你写出更健壮的代码。

防御式编程是"预防胜于治疗"。在边界处验证输入（setter、构造函数、API 入口），不让非法数据进入系统内部。这再次体现了 Week 02 学的**封装**价值——类的内部状态由类自己保护。

FMEA 则是一种系统化的风险思维。不要等用户报告 bug，提前想象所有可能出错的地方，并制定应对策略。老潘在第 5 节的分析展示了工程化的风险管理方法。

最后，AI 生成代码常遗漏异常处理。它擅长生成"happy path"，但不擅长考虑"用户会怎么搞砸这个程序"。本周你学的异常处理思维，是你**审查 AI 代码、补充边界情况**的重要能力。

下周我们将进入**团队协作与 Code Review**——软件是团队运动，代码审查是保障质量的关键环节。

---

## Definition of Done（学生自测清单）

### 知识理解

完成本周学习后，你应该能用自己的话解释：受检异常和运行时异常的根本区别是什么？try-catch-finally 各自在异常处理中扮演什么角色？防御式编程的"尽早失败"原则是什么意思？FMEA 分析的基本步骤有哪些？

### 代码能力

检查你的代码是否满足：能正确使用 try-catch-finally 结构处理文件读取等场景的异常；为 CampusFlow 定义了合理的自定义异常类层次；在 Task 等核心类的 setter 和构造函数中添加了输入验证；能判断一个场景应该用受检异常还是运行时异常。

### 设计能力

确认你能：为 CampusFlow 设计完整的异常类继承体系；用 FMEA 方法分析至少 5 个潜在故障点并制定应对策略；制定合理的错误处理策略（明白不是每个异常都需要捕获）。

### 团队项目

提交前请检查：CampusFlow 的所有核心类都添加了输入验证；自定义异常类体系已完成并放在 exception 包中；FMEA 分析文档已提交到 docs/fmea.md；程序能优雅处理各种非法用户输入而不会崩溃。
