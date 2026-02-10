# Week 01：抽象层次与软件工程史 —— 从 Python 到 Java

> "The only way to learn a new programming language is by writing programs in it."
> — Dennis Ritchie, C 语言之父

2025 年，AI 编程工具已经无处不在。GitHub Copilot 的月活开发者突破 1500 万，Claude、Cursor、Windsurf 等工具让写代码的效率成倍提升。有人说"学编程已经没用了"——但事实恰恰相反。就像自动驾驶时代你仍然需要理解交通规则，AI 写代码的时代，**读懂代码、做出正确的设计决策**反而更重要了。

本周，你将从 Python 迁移到 Java，写下第一行 Java 代码。这不仅是语法的转换，更是思维方式的升级——从动态类型的"快速原型"思维，到静态类型的"工程严谨"思维。

---

## 学习目标

完成本周学习后，你将能够：

1. 理解软件工程的三个黄金时代，认识到抽象层次的不断提升
2. 对比 Python（动态类型）与 Java（静态类型）的思维差异
3. 配置 Java 开发环境（JDK 17、IntelliJ IDEA、Maven、Git）
4. 编写并运行第一个 Java 程序（Hello World）
5. 使用 `System.out.println()` 输出信息，理解 Java 的输入输出
6. 使用 `Scanner` 获取用户输入，编写简单的交互式程序
7. 声明变量并理解 Java 的基本数据类型
8. 使用 Git 进行版本控制的基础操作

---

<!--
贯穿案例：CampusFlow 项目启动

演进路线：
- 第 1 节（环境配置）：搭建 Java 开发环境，配置 IntelliJ IDEA
- 第 2 节（Hello World）：编写第一个 Java 程序，理解类和方法
- 第 3 节（变量与类型）：理解 Java 的静态类型系统
- 第 4 节（用户输入）：使用 Scanner 进行交互
- 第 5 节（团队组建）：Git 协作基础，团队项目启动

最终成果：配置完成的开发环境 + 团队 Git 仓库 + 第一个可运行的 Java 程序
-->

<!--
认知负荷预算：
- 本周新概念（6 个，预算上限 6 个）：
  1. JDK / JRE / JVM（Java 运行时环境）
  2. 静态类型 vs 动态类型
  3. 类（Class）和主方法（main）
  4. System.out.println（输出）
  5. Scanner（输入）
  6. Git 基础操作
- 结论：✅ 在预算内

回顾桥设计（Week 01 为起点周，豁免回顾桥要求，但需为后续周铺垫）
-->

<!--
角色出场规划：
- 小北（第 2 节）：在写第一个 Java 程序时困惑于"为什么需要写这么多代码才能打印一行字"
- 小北（第 3 节）：在类型声明上出错（String name = 123），引出静态类型的概念
- 阿码（第 3 节）：追问"Java 为什么要强制声明类型？Python 就不需要"
- 老潘（第 4 节）：给出工程建议——"第一行代码要写得能让明天的自己看懂"
- 老潘（第 5 节）：解释 Git 在团队协作中的重要性

总出场次数：4 次（3 个不同角色），满足每章至少 2 次的要求
-->

<!--
AI 小专栏规划：

专栏 #1（放在第 2-3 节之间）：
- 主题：AI 编程工具概览与选择
- 连接点：与第 2 节 "第一个程序" 呼应——AI 可以生成代码，但你需要先理解代码
- 建议搜索词："GitHub Copilot statistics 2025", "Claude Code vs Cursor 2025"

专栏 #2（放在第 4-5 节之间）：
- 主题：AI 时代为什么还需要学 Java 基础
- 连接点：与静态类型和工程思维呼应——基础是驾驭 AI 的前提
- 建议搜索词："AI generated code debugging", "static typing benefits software engineering"
-->

## 1. 软件工程的三个黄金时代

在写下第一行代码之前，让我们先站在历史的高度，理解软件工程是如何演进的。

Grady Booch（UML 创始人之一）曾提出一个观点：**软件工程经历了三个黄金时代**。

### 第一黄金时代：算法时代（1960s-1980s）

在这个时代，软件工程的核心问题是**算法**。Fortran、COBOL、C 语言主宰了这个时代。程序员的核心能力是写出高效的算法，让有限的计算资源发挥最大价值。

这个时代的代表是 Donald Knuth 的《计算机程序设计艺术》——算法就是一切。

### 第二黄金时代：面向对象时代（1990s-2010s）

随着软件规模的增长，人们发现单纯优化算法已经不够了。**如何组织代码、如何管理复杂度**成为了核心问题。

C++、Java、C# 崛起，面向对象编程（OOP）成为了主流。UML 被发明出来用于设计软件架构。这个时代的核心问题是**概念完整性**——如何让大型软件系统的各个部分协调一致地工作。

Java 就是这个时代的产物。1995 年，Sun 公司发布 Java，提出"一次编写，到处运行"的口号。Java 的静态类型系统、严格的语法规范，都是为了管理大型项目的复杂度。

### 第三黄金时代：AI 时代（现在）

我们正在经历第三个黄金时代。AI 工具（Copilot、Claude、Cursor）让代码生成变得前所未有的高效。但这带来了一个新问题：**当 AI 可以生成代码时，程序员的价值在哪里？**

答案在 Booch 的论文里：**架构决策、概念完整性维护、质量把控**——这些必须由人来做。AI 是强大的工具，但工具需要人来驾驭。

> **关键洞察**：抽象层次在不断提升。从算法到架构，再到 AI 辅助，每一层都在让程序员站得更高、看得更远。但地基依然重要——如果你不理解底层的代码，你就无法做出正确的架构决策。

---

## 2. 从 Python 到 Java：思维转换

你已经学过 Python，习惯了它的简洁和灵活。现在，让我们看看 Java 有什么不同。

### Python：动态类型的"快速原型"思维

```python
name = "李小明"      # 不需要声明类型
age = 25             # 随时可以改
print(f"你好，{name}！明年你{age + 1}岁")
```

Python 的哲学是"简单胜于复杂"。你不需要告诉 Python `name` 是字符串，`age` 是整数——它在运行时会自动推断。

这种灵活性非常适合快速原型。你可以几分钟内写出一个能工作的程序。

### Java：静态类型的"工程严谨"思维

```java
public class Hello {
    public static void main(String[] args) {
        String name = "李小明";   // 必须声明类型
        int age = 25;            // 类型不能随意改
        System.out.println("你好，" + name + "！明年你" + (age + 1) + "岁");
    }
}
```

小北看着这段代码，皱起了眉头："等等，为什么需要写这么多代码才能打印一行字？Python 只要一行 `print` 就够了啊。"

这就是 Java 的"代价"——为了获得工程上的严谨，你需要写更多的"样板代码"。

但老潘在旁边说了一句："小北，你知道为什么企业级项目大多用 Java 吗？"

### 静态类型的价值

Java 的**静态类型系统**（Static Typing）意味着：

1. **编译期检查**：很多错误在运行前就能被发现
   ```java
   String name = "李小明";
   name = 123;  // ❌ 编译错误！不能把整数赋给字符串变量
   ```

2. ** IDE 支持**：因为类型是明确的，IDE 可以给出准确的代码补全和重构建议

3. **可读性和维护性**：六个月后再看这段代码，你马上知道 `name` 是字符串，`age` 是整数

4. **团队协作**：在大型团队中，明确的类型规范减少了沟通成本

阿码举手问道："那 Python 的动态类型就不好吗？"

"不是不好，是适用场景不同，"老潘解释道，"Python 适合快速原型、数据分析、脚本编写。Java 适合大型系统、长期维护的企业软件。就像跑车和卡车——跑车快但载不了多少货，卡车慢一点但能运很重的东西。"

> **核心认知**：Python 教你"如何快速写出代码"，Java 教你"如何写出能长期维护的代码"。两种思维都是优秀工程师需要的。

---

## 3. 搭建开发环境

在开始写代码之前，我们需要配置开发环境。Java 的开发环境比 Python 复杂一些，但这也是学习工程化思维的一部分。

### 安装 JDK 17

JDK（Java Development Kit）是 Java 开发工具包，包含了编译器、运行时环境等必需工具。

**下载地址**：https://www.oracle.com/java/technologies/downloads/#java17

或使用包管理器：
```bash
# macOS (Homebrew)
brew install openjdk@17

# Ubuntu/Debian
sudo apt-get install openjdk-17-jdk

# Windows (choco)
choco install openjdk17
```

验证安装：
```bash
java -version
javac -version
```

应该显示 `17.0.x` 版本。

### 安装 IntelliJ IDEA

IntelliJ IDEA 是 Java 开发最常用的 IDE，有强大的代码补全、重构和调试功能。

**下载地址**：https://www.jetbrains.com/idea/download/

社区版（Community Edition）免费且功能足够学习使用。

### 安装 Maven

Maven 是 Java 项目的构建工具，管理依赖、编译代码、运行测试。

```bash
# macOS
brew install maven

# Ubuntu/Debian
sudo apt-get install maven

# 验证
mvn -version
```

### 配置 Git

你应该已经有 Git 基础了，让我们确保配置正确：

```bash
git config --global user.name "你的名字"
git config --global user.email "your.email@example.com"
```

---

> **AI 时代小专栏：AI 编程工具概览**
>
> 你正在配置 Java 开发环境的同时，AI 编程工具正在以惊人的速度改变这个行业。
>
> 2025 年，GitHub Copilot 已成为超过 1500 万开发者的标配工具。据研究显示，使用 Copilot 的开发者完成任务的速度平均提升 55%。Cursor 和 Windsurf 等新兴工具则更进一步，提供了更智能的代码理解和重构能力。
>
> 目前主流的 AI 编程工具包括：
> - **GitHub Copilot**（$10/月）：代码补全能力强，与 IDE 集成好
> - **Claude Code**（$20/月）：适合复杂任务，架构设计能力强
> - **Cursor**（$20/月）：用户体验好，适合前端开发
> - **Codeium**（免费版可用）：免费的 Copilot 替代品
>
> 但这里有个关键：**AI 可以帮你生成代码，甚至能一口气写出整个程序。可如果你连 Java 的基本语法都不清楚，你怎么知道 AI 生成的代码对不对？怎么判断它有没有用错类型、遗漏异常处理？**
>
> 所以你正在学的 Java 基础，是理解一切 AI 生成代码的前提。先学会走路，再借助交通工具——这才是和 AI 协作的正确姿势。
>
> 参考：
> - GitHub Copilot 官方数据（访问日期：2025-02-10）
> - "The State of AI Coding 2025" 行业报告

---

## 4. 你的第一行 Java 代码

环境配置好了，现在写下你的第一行 Java 代码。

在 IntelliJ IDEA 中创建一个新项目：

1. File → New → Project
2. 选择 "Java"，确保 SDK 是 17
3. 项目名称：`CampusFlow`
4. 点击 "Create"

创建文件 `src/HelloWorld.java`：

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

点击运行按钮（绿色的三角形），你会在控制台看到：

```
Hello, World!
```

### 代码解析

小北看着这段代码，忍不住问："为什么需要这么多行才能打印一句话？"

让我们逐行理解：

```java
public class HelloWorld {           // 定义一个名为 HelloWorld 的类
    public static void main(String[] args) {  // 主方法，程序从这里开始执行
        System.out.println("Hello, World!");  // 打印输出
    }
}
```

- **`public class HelloWorld`**：在 Java 中，所有代码都必须放在类（class）里面。`HelloWorld` 是类名，通常与文件名一致。

- **`public static void main(String[] args)`**：这是程序的入口点。Java 虚拟机（JVM）会从这里开始执行你的程序。
  - `public`：公开的，可以被外部调用
  - `static`：静态的，不需要创建对象就能调用
  - `void`：返回值类型，表示这个方法不返回任何值
  - `main`：方法名，Java 规定的入口方法名
  - `String[] args`：命令行参数

- **`System.out.println()`**：打印输出并换行。`System.out` 是标准输出流，`println` 是 "print line" 的缩写。

### 与 Python 的对比

| Python | Java | 说明 |
|--------|------|------|
| `print("Hello")` | `System.out.println("Hello")` | Java 更冗长，但功能更强 |
| 直接运行文件 | 需要先编译 | Java 是编译型语言 |
| 动态类型 | 静态类型 | 需要声明变量类型 |
| 缩进表示代码块 | 大括号 `{}` 表示代码块 | 风格不同 |

---

## 5. 变量与数据类型

现在让我们学习如何在 Java 中存储和使用数据。

### 声明变量

在 Java 中，声明变量需要指定类型：

```java
public class VariablesDemo {
    public static void main(String[] args) {
        String name = "李小明";      // 字符串类型
        int age = 25;                // 整数类型
        double height = 1.75;        // 浮点数类型
        boolean isStudent = true;    // 布尔类型
        
        System.out.println("姓名：" + name);
        System.out.println("年龄：" + age);
        System.out.println("身高：" + height + " 米");
        System.out.println("是否学生：" + isStudent);
    }
}
```

### 基本数据类型

Java 有两类数据类型：

**基本类型**（Primitive Types）：
- `int`：整数（4 字节）
- `long`：长整数（8 字节）
- `double`：双精度浮点数（8 字节）
- `float`：单精度浮点数（4 字节）
- `boolean`：布尔值（true/false）
- `char`：单个字符（2 字节）
- `byte`、`short`：更小的整数类型

**引用类型**（Reference Types）：
- `String`：字符串（类类型）
- 数组、类等自定义类型

### 类型转换

小北试着写了一段代码：

```java
int age = 25;
String ageString = age;  // ❌ 编译错误！
```

"为什么不能直接把整数赋值给字符串？"小北困惑地问。

阿码在旁边解释："Java 是静态类型的，不同类型之间不能直接赋值。你需要显式转换："

```java
int age = 25;
String ageString = String.valueOf(age);  // ✅ 正确
// 或者
String ageString2 = Integer.toString(age);  // ✅ 也可以
```

这就是静态类型的严谨之处——它迫使你明确知道自己在做什么类型转换。

---

## 6. 用户输入与交互

程序如果只是输出信息，那就太无聊了。让我们学习如何获取用户输入。

Java 使用 `Scanner` 类来处理输入：

```java
import java.util.Scanner;

public class UserInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("请输入你的姓名：");
        String name = scanner.nextLine();
        
        System.out.print("请输入你的年龄：");
        int age = scanner.nextInt();
        
        System.out.println("你好，" + name + "！");
        System.out.println("明年你就 " + (age + 1) + " 岁了！");
        
        scanner.close();  // 记得关闭 Scanner
    }
}
```

### 代码解析

- **`import java.util.Scanner`**：导入 Scanner 类。Java 的标准库组织在 `java.util` 等包中，使用前需要导入。

- **`Scanner scanner = new Scanner(System.in)`**：创建 Scanner 对象，`System.in` 代表标准输入（键盘）。

- **`scanner.nextLine()`**：读取一行字符串输入。

- **`scanner.nextInt()`**：读取一个整数输入。

- **`scanner.close()`**：关闭 Scanner，释放资源。这是良好的编程习惯。

### 一个常见陷阱

小北遇到了一个奇怪的问题：

```java
System.out.print("请输入年龄：");
int age = scanner.nextInt();

System.out.print("请输入姓名：");
String name = scanner.nextLine();  // 这里直接跳过了！
```

问题出在哪？`nextInt()` 只读取数字，不会消费行尾的回车符。剩下的回车被 `nextLine()` 读到了，所以看起来像是跳过了输入。

解决方法：在 `nextInt()` 后加一个 `nextLine()` 吃掉回车：

```java
int age = scanner.nextInt();
scanner.nextLine();  // 吃掉换行符
String name = scanner.nextLine();
```

> 老潘点评："这种'坑'在 Java 里很常见。Python 的 `input()` 更友好，但 Java 让你更清楚底层发生了什么。这就是工程思维——了解工具的行为细节，才能写出可靠的代码。"

---

> **AI 时代小专栏：为什么还需要学 Java 基础？**
>
> 你可能会想：既然 AI 可以帮我写代码，为什么还要花时间去理解类型系统、`Scanner` 的 quirks、以及各种语法细节？
>
> 让我给你讲一个真实的故事。
>
> 2024 年底，一个创业团队用 AI 生成了一套 Java 后端系统。代码看起来完美，功能也都实现了。但上线一周后，系统开始频繁崩溃。
>
> 问题出在哪？AI 生成的代码没有正确处理数据库连接关闭。在高并发场景下，连接池被耗尽，系统就挂了。
>
> 团队里的高级工程师花了两天时间排查，最后发现是一个 `try-finally` 块被 AI 遗漏了。如果团队里没有人真正理解 Java 的资源管理机制，这个问题可能永远找不到。
>
> **AI 可以生成代码，但 AI 不会为你的生产事故负责。**
>
> 当你理解了 Java 的静态类型系统，你就能判断 AI 生成的类型转换是否安全；当你理解了资源管理，你就能发现 AI 遗漏的 `close()` 调用；当你理解了异常处理，你就能补全 AI 没有考虑的边界情况。
>
> 这就是本课程的核心理念：**AI 是倍增器，不是替代者**。学会 Java 基础，不是为了和 AI 竞争写代码的速度，而是为了在 AI 生成代码后，能够审查它、改进它、对最终质量负责。
>
> Linus Torvalds（Linux 创始人）曾说："LLMs 将帮助我们写出更好的软件，更快。但编译器才是 1000 倍提升，AI 只是再加 10-100 倍。" 工具在进化，但底层的工程原理从未改变。

---

## 7. 团队组建与 Git 基础

本周的最后一部分，我们来组建团队并建立项目仓库。

### 团队组建

- 每 2-3 人一组
- 确定团队名称
- 确定项目选题（从 CampusFlow 选题池中选择）
- 确定第一轮首席架构师（负责 Week 02-03）

### 创建 Git 仓库

1. 在 GitHub 上创建一个新的仓库，命名为 `campusflow-[你的项目名]`

2. 克隆到本地：
```bash
git clone https://github.com/your-team/campusflow-yourproject.git
cd campusflow-yourproject
```

3. 创建初始项目结构：
```bash
mkdir -p src/main/java/com/campusflow
mkdir -p src/test/java
```

4. 创建 `.gitignore` 文件：
```
# IntelliJ
.idea/
*.iml

# Maven
target/

# Compiled files
*.class
```

5. 提交初始代码：
```bash
git add .
git commit -m "Initial commit: project setup"
git push origin main
```

### Git 基础操作回顾

```bash
# 查看状态
git status

# 添加文件到暂存区
git add filename.java
# 或添加所有修改
git add .

# 提交更改
git commit -m "描述这次修改的内容"

# 推送到远程仓库
git push

# 拉取最新代码
git pull
```

---

## 本周小结（供下周参考）

本周是 Java 软件工程课程的起点。我们建立了从 Python 到 Java 的思维桥梁，理解了静态类型系统的价值，配置了完整的开发环境，写下了第一行 Java 代码，并启动了团队项目。

**核心概念已建立**：
- Java 的静态类型系统 vs Python 的动态类型
- 类（Class）是 Java 的基本组织单位
- 所有代码必须从 `main` 方法开始执行
- `System.out.println` 用于输出，`Scanner` 用于输入
- Git 是团队协作的基础工具

**CampusFlow 项目进度**：
- ✅ 团队组建完成
- ✅ 开发环境配置完成
- ✅ Git 仓库建立
- ⏳ 下周：领域建模与第一份 ADR

**为下周铺垫**：
- 预习"面向对象"的基本概念（类、对象、封装）
- 思考你的项目需要哪些核心实体（如 Task、User、Project 等）
- 准备好讨论领域模型设计

---

## 练习与思考

### 基础练习

1. **个人信息卡片**：编写一个 Java 程序，使用 `Scanner` 获取用户的姓名、年龄、专业，然后格式化输出一张个人信息卡片。

2. **简易计算器**：编写程序接收两个数字和一个运算符（+、-、*、/），输出计算结果。

3. **类型转换实验**：尝试将 `int` 转换为 `String`，将 `String` 转换为 `int`（使用 `Integer.parseInt()`），观察什么情况下会出错。

### 思考题

1. 为什么 Java 要强制声明变量类型？这种"繁琐"带来了什么好处？

2. 对比 Python 和 Java 的错误提示，Java 的编译错误信息是否更有帮助？为什么？

3. 在团队项目中，静态类型系统如何帮助协作？

### 预习任务

1. 阅读 CampusFlow 项目设计文档（`shared/book_project.md`）
2. 与你的团队讨论选题，初步确定项目方向
3. 确认第一轮首席架构师

---

> **Week 01 完成标记**：恭喜！你已经迈出了 Java 软件工程学习的第一步。从下周开始，我们将深入面向对象设计和 ADR（架构决策记录）的世界。
