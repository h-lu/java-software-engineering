# Week 01：抽象层次与软件工程史

> "软件工程的终极目标，是控制复杂度。"
> — Grady Booch

2026 年初，GitHub Copilot 宣布用户数突破 2600 万，AI 正在编写普通开发者 46% 的代码。90% 的财富 100 强公司都在使用 AI 编程助手，80-85% 的开发者已将 AI 工具纳入日常工作流程。

但数据也揭示了隐忧：AI 生成代码的 Bug 率比人类代码高 1.7 倍，26.6% 的 AI 生成程序会产生不正确的输出。代码质量正在"悄悄变差"。

这引出了我们这门课的核心问题：**AI 能帮你快速写出代码，但它能帮你理解"什么是好软件"吗？**

答案是：比以往任何时候都更需要。AI 能快速生成"能跑"的代码，但它不会帮你理解抽象层次、不会做架构决策、更不会把控长期可维护性。这些工程思维必须掌握在你自己手中。AI 是倍增器，不是替代者——你理解得越深，AI 帮你做得越好。

本周我们从软件工程的演进史讲起，理解抽象层次的提升如何改变软件开发。然后你会写出第一个 Java 程序——一个简单的个人名片生成器。在这个过程中，你会体会 Java 和 Python 的思维差异，理解为什么大型项目偏爱静态类型，并建立"工程化"的基本认知。

---

## 学习目标

完成本周学习后，你将能够：
1. 理解软件工程三个黄金时代的演进（从汇编到对象到 AI）
2. 解释 Python（动态类型）和 Java（静态类型）的思维差异
3. 写出第一个可运行的 Java 程序
4. 配置完整的开发环境（JDK 17、IntelliJ、Maven、Git）
5. 为团队项目建立 Git 仓库并完成首次提交

<!--
贯穿案例：个人名片生成器（BusinessCardGenerator）
- 第 1 节：从软件工程演进 → 案例从一个"硬编码输出姓名"的 Hello World 开始
- 第 2 节：静态类型思维 → 案例演进到"变量化存储个人信息"，理解类型声明的作用
- 第 3 节：第一个完整程序 → 案例演进到"用户交互式输入"，使用 Scanner
- 第 4 节：程序是可改进的 → 案例演进到"格式化输出名片"，体验 Java 严格带来的好处
- 第 5 节：工程化起步 → 案例演进到"可维护版本"，理解为什么工程规范重要

最终成果：一个能读取用户输入、格式化输出个人信息的控制台程序，读者能说出"Java 的严格在哪里帮了我"

认知负荷预算：
- 本周新概念（5 个，预算上限 6 个）：
  1. 静态类型（static typing）
  2. 变量声明（variable declaration）
  3. 基本数据类型（primitive types）
  4. Scanner 输入
  5. 锚点（anchor）- 工程概念
- 结论：✅ 在预算内

回顾桥设计：Week 01 豁免（第一周）

CampusFlow 本周推进：
- 上周状态：无（课程起点）
- 本周改进：团队组建 + 选题确定 + Git 仓库初始化 + README 建立
- 涉及的本周概念：锚点（在 README 中建立项目锚点）、Git 基础
- 建议落盘位置：项目仓库的 README.md 和 docs/ADR-001-项目立项.md

角色出场规划：
- 小北：在第 2 节中因"忘记声明类型"而报错，引出静态类型的概念
- 老潘：在第 4 节中对比"Python 的灵活 vs Java 的严格"，从工程角度解释为什么需要严格
- 阿码：在第 5 节中提问"为什么不能用 AI 直接生成项目"，引出工程思维的重要性

AI 小专栏 #1（放在第 2 节之后）：
- 主题：AI 改变了什么，没改变什么
- 连接点：呼应第 2 节的"静态类型思维"——AI 能帮你写代码，但不能帮你理解类型系统的价值
- 建议搜索词："AI programming assistant adoption 2026"、"software development trends 2026"

AI 小专栏 #2（放在第 4 节之后）：
- 主题：用 AI 学习 Java（但要小心）
- 连接点：呼应第 4 节的"程序改进"——AI 可以生成代码，但审查和验证必须由你来做
- 建议搜索词："GitHub Copilot Java 2026"、"AI code review statistics 2026"
-->

---

## 1. 软件工程的三个黄金时代

> **本节导航**（第一次读可以跳到这里）
>
> 如果你对"软件工程史"不太感兴趣，只想快速上手 Java，可以这样读：
> - **快速路径**：直接跳到"从 Hello World 开始"，先写出第一个程序
> - **完整路径**：读完本节，理解"为什么 Java 要这样设计"
>
> 本节的核心问题：有了 AI，我们为什么还要学这些历史？

---

老潘在第一节课上就问了这个问题："有了 AI，我们为什么还要学软件工程史？"

底下一片沉默。阿码小声说："历史课总是很无聊……"

"不是让你们背年份，"老潘笑了，"而是让你们理解：**技术演进从来不是乱来的，每个时代都在解决上一个时代的问题。**"

他打开了一张图——三个时代的演进。

### 从机器指令到对象

**第一个时代：机器语言时代**。程序员直接和硬件对话，用 0 和 1 编写指令。每行代码都在告诉 CPU"把这个值移到那个寄存器"——这是最低的抽象层次。你写一个"打印名字"的功能，需要几十行指令，而且稍微改一点需求就要重写大半。

老潘问："如果让你用机器语言写一个电商网站，你愿意吗？"

"疯了。"小北摇头，"写个首页都要写半年。"

"所以出现了**第二个时代：高级语言时代**。"老潘继续说，"Fortran、C、Pascal——你写 `print(name)`，编译器把它翻译成机器指令。抽象层次提升了，你不用关心寄存器、内存地址，你关心的是'我要做什么'。"

但这还不够。程序越来越大，几个人的项目变成几十人的团队，代码从几千行变成几十万行。**函数式的抽象不够用了**——你需要一种更贴近现实世界的思维方式：把"学生"、"课程"、"选课记录"当成一个个"对象"，每个对象有自己的属性和行为。

这就是**第三个时代：面向对象时代**。Java 就是在这个背景下诞生的（1995年）。它不是第一个面向对象语言（Smalltalk 更早），但它把"封装、继承、多态"这套思维方式带进了主流工程。

### 现在的第四个时代

"那现在是什么时代？"小北举手。

老潘指了指屏幕上的 AI 图标："**AI 辅助时代**。但记住：AI 提升的不是抽象层次，是开发速度。你仍然需要理解对象、理解架构、理解质量——AI 只是帮你写代码更快，不是帮你思考更深。"

他顿了顿："如果你不理解抽象层次，AI 给你的代码就是一堆'能跑但不知道为什么能跑'的碎片。三个月后你自己都看不懂。"

阿码突然明白了："所以我们要学软件工程史，是为了知道'为什么这么设计'，而不是只知道'怎么写'。"

"对。"老潘说，"历史会重复。不懂对象时代的设计模式，你会用 AI 生成出一堆面条代码；不懂工程化的质量门禁，你会让 AI 把 bug 加速带进生产环境。"

### 从 Hello World 开始

理解了抽象层次的演进，我们来写第一个 Java 程序——一个最简单的"个人名片生成器"。

先从最低的抽象层次开始：硬编码输出。

```java
// 文件：HelloWorld.java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("       张三的个人名片");
        System.out.println("================================");
        System.out.println("职位：软件工程师");
        System.out.println("邮箱：zhangsan@example.com");
        System.out.println("电话：138-0000-0000");
        System.out.println("================================");
    }
}
```

这段代码是最原始的版本：所有信息都写死在代码里。每次想生成不同的名片，你都要修改源代码、重新编译、运行。在机器语言时代，这已经是很"高级"的抽象了——但今天我们知道，它不够灵活。

下一步：让信息可以被替换，而不是写死。

---

## 2. Python 不会猜你想说什么——理解静态类型

小北从 Python 转过来，习惯了 `name = "张三"` 这种写法。他顺手在 Java 里写了类似的代码：

```java
// 小北的代码（❌ 编译不通过）
public class BusinessCard {
    public static void main(String[] args) {
        name = "张三";
        job = "软件工程师";
        email = "zhangsan@example.com";

        System.out.println("姓名：" + name);
        System.out.println("职位：" + job);
    }
}
```

他自信满满地点了"运行"——结果 IDE 立刻用红线把这行代码围了起来。

> **错误提示**：
> ```
> Error: cannot find symbol
>   symbol:   variable name
>   location: class BusinessCard
> ```

"我都还没运行，它怎么就骂我？"小北看着报错，一头雾水。"Python 里不都这么写吗？"

### Java 和 Python 的本质差异

这是静态类型（static typing）和动态类型的第一次正面碰撞。

在 Python 里，变量像便利贴——你随时可以把它贴到任何值上。解释器会在运行时"猜"你想做什么：`name = "张三"`，Python 看到"这是个字符串"，就把 `name` 标记成字符串类型。你后面写 `name = 123`，Python 也会接受——它只是默默把便利贴贴到了新的值上。

但 Java 不猜。**Java 要求你在声明变量时就明确告诉编译器：这个变量将来会装什么类型的数据。**

正确的写法是这样：

```java
// 文件：VariableDemo.java
public class VariableDemo {
    public static void main(String[] args) {
        // 必须先声明类型
        String name = "张三";
        String job = "软件工程师";
        String email = "zhangsan@example.com";

        System.out.println("姓名：" + name);
        System.out.println("职位：" + job);
        System.out.println("邮箱：" + email);
    }
}
```

你看，`String name = "张三";` 这行代码在说：
- `String`：这个变量会存储字符串类型的数据
- `name`：变量名
- `=`：把右边的值赋给左边的变量
- `"张三"`：具体的字符串值

**这不是"麻烦"，这是契约。** 你和编译器签了个合同："我保证 `name` 这个变量永远只存字符串，如果我在后面试图把数字塞进去，你就拦住我。"

### 类型声明的好处

为了让你更直观地理解两种语言的区别，我们来看一个对比表格：

| 场景 | Python | Java |
|------|--------|------|
| **变量声明** | `name = "张三"`（无需声明类型） | `String name = "张三";`（必须声明类型） |
| **类型检查时机** | 运行时才检查 | 编译期就检查 |
| **类型错误示例** | `age = "25"` 然后 `age + 10` 得到 `"2510"`（字符串拼接） | `String age = "25";` 然后 `age + 10` 编译报错 |
| **灵活度** | 高（变量可随时换类型） | 低（一旦声明就不能改） |
| **安全性** | 低（类型错误到运行时才发现） | 高（类型错误编译期就发现） |
| **适合场景** | 快速原型、脚本工具、个人项目 | 大型项目、团队协作、长期维护 |

小北还是不理解："为什么不能像 Python 那样灵活？"

老潘用一个场景回答了他：

> 假设你在写一个电商系统，Python 里你有这样的代码：
> ```python
> discount = 0.8
> price = 100
> final_price = price * discount  # 80.0
> ```
>
> 三个月后，你的同事接手这个项目，不小心写了一行：
> ```python
> discount = "8折"  # 他以为是字符串形式
> price = 100
> final_price = price * discount  # ❌ 报错：can't multiply sequence by non-int of type 'str'
> ```
>
> 这个 bug 要到**运行时**才会爆出来——可能已经上线了，用户正在下单，突然整个订单系统崩溃。

"但在 Java 里，"老潘继续说，"你同事写 `discount = "8折";` 的瞬间，IDE 就会报错。代码根本跑不起来。"

这就是静态类型的价值：**把错误从运行时提前到编译期**。你宁可多写几个字符的类型声明，也不愿意半夜三点被电话叫起来修复线上 bug。

### 基本数据类型

Java 把数据类型分成两类：

**1. 基本类型（primitive types）**：直接存储值，像小盒子
- `int`：整数，如 `123`
- `double`：浮点数，如 `3.14`
- `boolean`：布尔值，只有 `true` 或 `false`
- `char`：单个字符，如 `'A'`

**2. 引用类型**：存储对象的地址，像标签
- `String`：字符串（注意 `S` 大写）
- 数组、类、接口等

我们的名片生成器现在可以存储更多类型的信息了：

```java
// 文件：BusinessCard.java
public class BusinessCard {
    public static void main(String[] args) {
        String name = "张三";
        String job = "软件工程师";
        String email = "zhangsan@example.com";
        int age = 28;              // 新增：年龄
        double yearsOfExp = 5.5;   // 新增：工作经验

        System.out.println("================================");
        System.out.println("       " + name + "的个人名片");
        System.out.println("================================");
        System.out.println("职位：" + job);
        System.out.println("年龄：" + age + "岁");
        System.out.println("工作经验：" + yearsOfExp + "年");
        System.out.println("邮箱：" + email);
        System.out.println("================================");
    }
}
```

你现在理解了：类型声明不是 Java 在找麻烦，而是它在帮你提前发现问题。程序越大、团队越多人，这个"提前发现"的价值就越明显。

---

> **AI 时代小专栏：AI 改变了什么，没改变什么**
>
> 2026 年的 GitHub Copilot 数据显示：开发者每周平均节省 3.6 小时，生产力提升约 1.5-2 倍。看起来 AI 正在让编码变得更高效——但你可能注意到了一个悖论：代码变多了，但代码质量并没有同步提升。
>
> AI 改变的是"编码速度"，不是"工程思维"。它能在几秒钟内生成一段 `Scanner` 读取输入的代码，但它不会帮你思考：
> - 这个变量应该用什么类型？
> - 如果用户输入的不是数字怎么办？
> - 这段代码三个月后还能看懂吗？
>
> 更关键的是，AI 生成的代码往往"能跑但不够严谨"。它可能会用 `Object` 类型代替具体的类型声明，忽略类型安全的好处。如果你不理解静态类型的价值，你就无法判断 AI 给你的代码是不是"工程上合理的"。
>
> 所以本周你学的类型声明、编译期检查——这些看似"麻烦"的约束，恰恰是你审查 AI 代码的标尺。**AI 能帮你写代码，但你必须具备判断"什么是好代码"的能力。**
>
> 参考（访问日期：2026-02-10）：
> - [GitHub Copilot Usage Data Statistics For 2026 - Tenet](https://www.wearetenet.com/blog/github-copilot-usage-data-statistics)
> - [AI Coding Assistants 2026: GitHub Copilot, ChatGPT, and Why - Programming Helper](https://www.programming-helper.com/tech/ai-coding-assistants-2026-github-copilot-chatgpt-developer-productivity-python)

---

## 3. 程序是活的——跟用户聊起来

上一节的名片生成器有个问题：每次想生成不同的名片，都要修改代码、重新编译。这太麻烦了。

"能不能让用户自己输入信息？"阿码问，"像 Python 的 `input()` 那样？"

Java 里有对应的工具：`Scanner` 类。

### Scanner 基本用法

`Scanner` 是 Java 标准库里专门用来读取输入的工具。你可以用它从键盘读取用户输入、从文件读取内容、甚至从字符串里解析数据。

先看最简单的例子：

```java
// 文件：ScannerDemo.java
import java.util.Scanner;  // 必须导入 Scanner 类

public class ScannerDemo {
    public static void main(String[] args) {
        // 创建 Scanner 对象，从"标准输入"（键盘）读取
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入你的名字：");
        String name = scanner.nextLine();  // 读取一整行

        System.out.print("请输入你的职位：");
        String job = scanner.nextLine();

        System.out.print("请输入你的年龄：");
        int age = scanner.nextInt();  // 读取一个整数

        System.out.println("\n=== 你的名片 ===");
        System.out.println("姓名：" + name);
        System.out.println("职位：" + job);
        System.out.println("年龄：" + age + "岁");

        scanner.close();  // 记得关闭 Scanner
    }
}
```

运行这段代码，你会看到：

```
请输入你的名字：李四
请输入你的职位：产品经理
请输入你的年龄：30

=== 你的名片 ===
姓名：李四
职位：产品经理
年龄：30岁
```

### 理解输入流

`Scanner scanner = new Scanner(System.in);` 这行代码做了什么？

- `System.in`：代表"标准输入流"（通常是键盘）
- `new Scanner(...)`：创建一个 Scanner 对象，包装这个输入流
- `scanner`：变量名，你可以用它来读取输入

你可以把 Scanner 想象成一个"翻译官"：键盘敲进来的是一串字符流（`"李四\n产品经理\n30\n"`），Scanner 帮你把它"翻译"成你需要的类型——字符串、整数、浮点数等。

### 常见方法

| 方法 | 读取内容 | 示例 |
|------|---------|------|
| `nextLine()` | 一整行字符串 | `"Hello World"` |
| `next()` | 下一个单词（以空格分隔） | `"Hello"` |
| `nextInt()` | 整数 | `123` |
| `nextDouble()` | 浮点数 | `3.14` |
| `nextBoolean()` | 布尔值 | `true` |

### 小北踩过的坑

小北写了一个读取输入的程序，结果遇到了奇怪的问题：

```java
// 小北的代码（有问题）
Scanner scanner = new Scanner(System.in);

System.out.print("请输入年龄：");
int age = scanner.nextInt();

System.out.print("请输入姓名：");
String name = scanner.nextLine();  // ❌ 这里直接跳过了！

System.out.println("姓名：" + name + "，年龄：" + age);
```

运行结果：

```
请输入年龄：25
请输入姓名：姓名：，年龄：25
```

"为什么 `name` 没让我输入就跳过去了？"小北困惑了。

老潘走过来，画了张图：

> 把输入流想象成一条**流水线**。你敲键盘时，字符是一个接一个"流"进来的。
>
> 当你输入 `25` 然后按回车，流水线上实际是 `"25\n"`（三个字符：数字 2、数字 5、换行符）。
>
> `nextInt()` 就像是一个**只舀水的勺子**——它把 `25` 舀走了，但把 `\n` 留在杯底。轮到 `nextLine()` 时，它看到杯底还有个换行符，就以为"哦，这一行已经结束了"，直接返回空字符串。

**问题根源**：`nextInt()` 只读取数字，**不读取后面的换行符**。这个换行符留在缓冲区里，被下一个 `nextLine()` 误认为是"空行"。

**修复方法有两种**：

**方法 1：统一用 `nextLine()`，然后手动转换**（推荐）

```java
// 修复后的代码
System.out.print("请输入年龄：");
int age = Integer.parseInt(scanner.nextLine());  // 先读整行（包括换行符），再转成整数

System.out.print("请输入姓名：");
String name = scanner.nextLine();  // ✅ 现在正常工作了
```

**方法 2：用"吃掉换行符"的技巧**

```java
// 另一种修复方式
System.out.print("请输入年龄：");
int age = scanner.nextInt();
scanner.nextLine();  // 吃掉 nextInt() 遗留的换行符

System.out.print("请输入姓名：");
String name = scanner.nextLine();  // ✅ 也能工作
```

老潘建议用方法 1。"统一用 `nextLine()` 更少出错。你以后会学到，`nextInt()` 还有其他问题——比如输入的不是数字，程序会直接崩溃。用 `nextLine()` + `parseInt()`，你有机会处理异常情况。"

### 完整的名片生成器

现在我们的名片生成器可以读取用户输入了：

```java
// 文件：BusinessCardInteractive.java
import java.util.Scanner;

public class BusinessCardInteractive {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 名片生成器 ===");
        System.out.println();

        System.out.print("请输入姓名：");
        String name = scanner.nextLine();

        System.out.print("请输入职位：");
        String job = scanner.nextLine();

        System.out.print("请输入邮箱：");
        String email = scanner.nextLine();

        System.out.print("请输入年龄：");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("请输入工作年限（可以是小数）：");
        double yearsOfExp = Double.parseDouble(scanner.nextLine());

        // 格式化输出
        System.out.println();
        System.out.println("================================");
        System.out.println("       " + name + " 的个人名片");
        System.out.println("================================");
        System.out.println("职位：" + job);
        System.out.println("年龄：" + age + "岁");
        System.out.println("工作经验：" + yearsOfExp + "年");
        System.out.println("邮箱：" + email);
        System.out.println("================================");

        scanner.close();
    }
}
```

这个程序已经能"活"起来了——你可以用它生成任意人的名片，不用修改代码。但还有一个问题：如果用户输入的不是数字怎么办？比如年龄输入了"二十八"而不是 `28`？

### 边界情况：当用户不按套路出牌

真实的程序需要处理各种"意外"输入。让我们看看常见的边界情况：

**情况 1：输入的不是数字**
```java
System.out.print("请输入年龄：");
String ageInput = scanner.nextLine();  // 用户输入"二十八"
int age = Integer.parseInt(ageInput);  // ❌ 报错：NumberFormatException
```

程序会崩溃并抛出 `NumberFormatException`。Week 03 我们会讲如何用 `try-catch` 捕获这个异常并给用户友好的提示。

**情况 2：输入是空字符串**
```java
System.out.print("请输入姓名：");
String name = scanner.nextLine();  // 用户直接按回车
// name = ""（空字符串）
```

空字符串"不算错"，但可能导致后续输出格式不完整。你可以在输出前检查：
```java
if (name.trim().isEmpty()) {
    System.out.println("（未提供姓名）");
} else {
    System.out.println("姓名：" + name);
}
```

**情况 3：输入包含前后空格**
```java
System.out.print("请输入姓名：");
String name = scanner.nextLine();  // 用户输入"  张三  "
// 输出：姓名：  张三  （两边有难看的空格）
```

用 `trim()` 去掉前后空格：
```java
String name = scanner.nextLine().trim();  // "张三"
```

**情况 4：数值超出合理范围**
```java
int age = Integer.parseInt(scanner.nextLine());  // 用户输入"200"
// 程序不会报错，但"200岁"显然不合理
```

你需要添加业务验证：
```java
if (age < 0 || age > 120) {
    System.out.println("年龄必须在 0-120 之间");
    // 这里可以重新读取输入
}
```

这涉及到**异常处理**，我们会在 Week 03 详细讲。现在你只需要知道：Java 的严格在这里也会帮你——程序会崩溃并给出清晰的报错，而不是像某些语言那样"静默失败"然后产生错误的结果。

---

## 4. "这太严格了吧"——从 Python 的宽容到 Java 的严谨

小北开始有点怀念 Python 了。

"Java 太麻烦了，"他抱怨道，"声明类型、写分号、关闭 Scanner……Python 两行搞定的事情，Java 要写十行。"

老潘听到了，走过来问："你觉得 Python 和 Java 的区别是什么？"

"Python 更灵活、更简洁。"小北说。

"没错。"老潘点点头，"但简洁不等于高效。来，我给你看个场景。"

### 两个版本的对比

老潘打开两个文件，一个是 Python 版，一个是 Java 版：

**Python 版本**：
```python
# 名片生成器（Python）
name = input("请输入姓名：")
age = input("请输入年龄：")
print(f"{name} 的年龄是 {age}")

# 如果后面有人写了这样：
age = age + 10  # 本意是年龄加 10
print(f"十年后 {name} 的年龄是 {age}")
```

运行结果：
```
请输入姓名：张三
请输入年龄：25
张三 的年龄是 25
十年后 张三 的年龄是 2510  # ❌ 字符串拼接！
```

"你看，"老潘指着输出，"`input()` 返回的是字符串。当你写 `age + 10`，Python 不会报错，它会把 `10` 转成字符串然后拼接。这个 bug 很隐蔽，可能到上线才发现。"

**Java 版本**：
```java
// 名片生成器（Java）
String name = scanner.nextLine();
String age = scanner.nextLine();  // 假设你错误地用 String 存储

int ageAfter10Years = age + 10;  // ❌ 编译不通过
```

```
Error: bad operand types for binary operator '+'
  first type:  String
  second type: int
```

"Java 在编译期就拦住了你，"老潘说，"你必须显式地把字符串转成整数：`Integer.parseInt(age) + 10`。多打几个字，但避免了 bug。"

### 编译期 vs 运行时

这个对比背后是两种哲学：

| 维度 | Python | Java |
|------|--------|------|
| 类型检查 | 运行时 | 编译期 |
| 错误发现 | 程序运行后才报错 | 编译时就报错 |
| 灵活性 | 高（一个变量可以随时换类型） | 低（一旦声明类型就不能改） |
| 安全性 | 低（类型错误要到运行时才发现） | 高（类型错误编译期就发现） |
| 适合场景 | 快速原型、脚本工具 | 大型项目、团队协作 |

"所以记住这句话，"老潘说，"**Python 是快速原型，Java 是长期维护。**"

小北似懂非懂："那什么时候用 Python，什么时候用 Java？"

"看场景。"老潘给出了一个简单的判断：
- 如果你在写一个**小工具**（几百行代码、一个人维护、用几次就丢），用 Python
- 如果你在写一个**系统**（几万行代码、团队协作、要维护好几年），用 Java

### 类型安全的好处

阿码举手了："但我还是觉得 Java 太严格了。能不能举个例子，说明'严格'真的有用？"

老潘想了想，讲了他几年前遇到的一个真实 bug：

> 那是个电商系统，Python 写的。有个函数计算折扣：
> ```python
> def calculate_price(original_price, discount):
>     return original_price * discount
> ```
>
> 调用的时候，有人传错了：
> ```python
> price = calculate_price(100, "8折")  # 本意是传 0.8
> ```
>
> Python 不会报错，它会返回 `"8折8折8折...（100次）"`。这个 bug 在测试环境没发现（因为测试数据都是正确的折扣值），结果上线后，有用户输入了"8折"而不是 `0.8`，整个价格计算就乱了。

"如果是 Java 呢？"阿码问。

"Java 根本编译不过。"老潘笑了，"你要么传 `double` 类型的折扣，要么编译器直接骂你。你不会有机会把这种错误带上线。"

### 格式化输出

回到我们的名片生成器。Java 的严格在这里也会帮到你——它提供了格式化输出的工具，让你精确控制显示格式：

```java
// 文件：FormattedCard.java
import java.util.Scanner;

public class FormattedCard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入姓名：");
        String name = scanner.nextLine();

        System.out.print("请输入职位：");
        String job = scanner.nextLine();

        System.out.print("请输入年龄：");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("请输入工作年限：");
        double yearsOfExp = Double.parseDouble(scanner.nextLine());

        // 使用 printf 格式化输出（类似 C 的 printf）
        System.out.println();
        System.out.println("╔═══════════════════════════════════╗");
        System.out.printf("║      %s 的个人名片%n", name);
        System.out.println("╠═══════════════════════════════════╣");
        System.out.printf("║ 职位：%-20s ║%n", job);  // %-20s 左对齐，占 20 字符
        System.out.printf("║ 年龄：%d 岁%15s ║%n", age, "");  // %d 整数
        System.out.printf("║ 工作经验：%.1f 年%12s ║%n", yearsOfExp, "");  // %.1f 保留一位小数
        System.out.println("╚═══════════════════════════════════╝");

        scanner.close();
    }
}
```

运行结果：
```
请输入姓名：王五
请输入职位：数据分析师
请输入年龄：27
请输入工作年限：4.5

╔═══════════════════════════════════╗
║      王五 的个人名片              ║
╠═══════════════════════════════════╣
║ 职位：数据分析师                 ║
║ 年龄：27 岁                      ║
║ 工作经验：4.5 年                 ║
╚═══════════════════════════════════╝
```

`printf` 的格式化语法：
- `%s`：字符串
- `%d`：整数
- `%.1f`：浮点数（`.1` 表示保留一位小数）
- `%n`：换行符（跨平台）
- `%-20s`：左对齐，占 20 字符宽度

这些格式化字符串如果写错了（比如用 `%d` 去格式化字符串），编译器会在运行时报错——这比"静默输出错误结果"要安全得多。

---

> **AI 时代小专栏：用 AI 学习 Java（但要小心）**
>
> 本周你学到了 `Scanner` 的一个经典陷阱：`nextInt()` 会遗留换行符，导致后续的 `nextLine()` 直接跳过。这是新手常犯的错误，也是 AI 生成代码时**最容易遗漏的边界情况**。
>
> 2026 年的代码质量报告显示了一个令人担忧的趋势：AI 生成代码的 Bug 率比人类代码高 1.7 倍，26.6% 的 AI 生成程序会产生不正确的输出。更严重的是，60% 的故障是"无声的逻辑失败"——程序跑起来了，但结果是错的。
>
> 为什么会这样？因为 AI 擅长"生成语法正确的代码"，但不擅长"理解所有边界情况"。当你让 AI 生成一个读取输入的代码，它可能直接写出 `scanner.nextInt()`，而不会提醒你"等等，如果后面还有 `nextLine()`，你需要先吃掉换行符"。
>
> 这就是为什么你需要**先理解 Java 的严格，再用 AI 倍增你的效率**：
> - 你需要能读懂 Java 的编译错误，而不是依赖 AI 来调试
> - 你需要知道"可能的坑"在哪里（比如换行符问题），才能审查 AI 给的代码
> - 你需要能用类型系统思考，才能判断 AI 生成的代码是否安全
>
> 所以本周的"严格"不是在为难你——它是在给你一个**能审查 AI 代码的标尺**。AI 能帮你生成代码，但你必须负责验证它、测试它、改进它。
>
> 参考（访问日期：2026-02-10）：
> - [State of Code Developer Survey 2026 - Sonar](https://www.sonarsource.com/resources/state-of-code-2026/)
> - [AI vs Human Code Generation Report - CodeRabbit](https://coderabbit.ai/blog/ai-vs-human-code-generation-2026/)
> - [AI-Generated Code Quality Metrics and Statistics for 2026 - SecondTalent](https://secondtalent.com/blog/ai-code-quality-2026/)

---

## 5. 工程不是写完代码——从"能跑"到"可维护"

我们的名片生成器现在已经能运行了。但老潘看了代码，摇摇头：

"这代码能跑，但三个月后你自己都看不懂了。"

### 代码可读性的重要性

小北不服："怎么会看不懂？我自己写的啊。"

老潘让他做个实验：打开你三个月前写的 Python 脚本，不看注释，告诉我这段代码在做什么。

小北打开了一个文件，盯着看了五分钟，尴尬地说："……好像是个爬虫？但这个变量 `x` 是干嘛的来着……"

"这就是问题。"老潘说，"**代码写一次，会被读十次。** 包括你未来的自己、你的同事、接手你项目的人。工程关注的是长期可维护性，不是'今天能跑'。"

### 命名规范

老潘指着小北的代码：

```java
// 小北的代码（❌ 命名不清晰）
String s1 = scanner.nextLine();
String s2 = scanner.nextLine();
int x = Integer.parseInt(scanner.nextLine());

System.out.println(s1);
System.out.println(s2);
System.out.println(x);
```

"s1、s2、x——这些名字你能记住它们是什么吗？"老潘问。

"……好像 s1 是姓名，s2 是职位？"小北也不确定了。

**好的命名应该"自解释"**：

```java
// ✅ 改进后的代码
String name = scanner.nextLine();
String jobTitle = scanner.nextLine();
int age = Integer.parseInt(scanner.nextLine());

System.out.println("姓名：" + name);
System.out.println("职位：" + jobTitle);
System.out.println("年龄：" + age);
```

现在你不需要猜——变量名告诉你它在做什么。

Java 的命名规范（记住这些，IDE 会帮你检查）：
- **类名**：PascalCase（首字母大写），如 `BusinessCard`
- **方法名和变量名**：camelCase（首字母小写），如 `printCard`、`userName`
- **常量**：UPPER_SNAKE_CASE（全大写），如 `MAX_SIZE`

### 注释的时机

小北现在学会了写注释，但他有点走火入魔：

```java
// 小北的代码（❌ 过度注释）
// 创建一个 Scanner 对象
Scanner scanner = new Scanner(System.in);

// 打印提示信息
System.out.print("请输入姓名：");

// 读取用户输入的姓名
String name = scanner.nextLine();

// 打印姓名
System.out.println("姓名：" + name);
```

"这些注释没用，"老潘说，"**注释应该解释'为什么'，不是'是什么'。** 代码本身已经告诉你它在做什么了，不需要翻译成中文。"

好的注释长这样：

```java
// ✅ 好的注释
// 使用 nextLine() 而不是 nextInt() 读取年龄，
// 避免 nextInt() 遗留换行符导致后续读取问题
System.out.print("请输入年龄：");
int age = Integer.parseInt(scanner.nextLine());

// 格式化输出使用中文全角边框（╔═══╗）以匹配中文字符宽度
System.out.println("╔═══════════════════════════════════╗");
```

注意：注释解释了**为什么这么做**（避免换行符问题、匹配中文字符宽度），而不是翻译代码。

### 代码结构

阿码问："那代码应该怎么组织？我想把'生成名片'和'读取输入'分开。"

"好问题。"老潘点头，"这就是'函数'（Java 里叫方法）的作用。"

我们重构一下代码。在重构之前，先理解一个核心思想：

> **当程序需要处理多个相关的信息时，可以创建一个"类"来封装它们。**

比如，"姓名、职位、邮箱、年龄、工作年限"这些信息都是描述"一个用户"的。与其在 main 方法里传来传去五个变量，不如把它们打包成一个"用户信息"对象——这就是类的作用。

重构后的代码：

```java
// 文件：BusinessCardGenerator.java
import java.util.Scanner;

/**
 * 个人名片生成器。
 *
 * <p>读取用户输入的个人信息，生成格式化的名片输出。
 * 支持姓名、职位、年龄、工作经验、邮箱等信息。
 */
public class BusinessCardGenerator {

    /** 名片边框宽度
     *
     * private：只有这个类能访问（外部看不见）
     * static：属于类本身，不是某个对象（所有实例共享）
     * final：一旦赋值就不能再改（它是常量）
     * 这种命名（全大写下划线分隔）是 Java 常量的规范
     */
    private static final int CARD_WIDTH = 33;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 读取用户输入，封装成 UserInfo 对象
        UserInfo user = readUserInfo(scanner);

        // 生成并打印名片
        printBusinessCard(user);

        scanner.close();
    }

    /**
     * 读取用户输入的个人信息。
     *
     * @param scanner Scanner 对象，用于读取输入
     * @return 包含用户信息的对象
     */
    private static UserInfo readUserInfo(Scanner scanner) {
        System.out.println("=== 名片生成器 ===");
        System.out.println();

        System.out.print("请输入姓名：");
        String name = scanner.nextLine();

        System.out.print("请输入职位：");
        String jobTitle = scanner.nextLine();

        System.out.print("请输入邮箱：");
        String email = scanner.nextLine();

        System.out.print("请输入年龄：");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("请输入工作年限：");
        double yearsOfExp = Double.parseDouble(scanner.nextLine());

        return new UserInfo(name, jobTitle, email, age, yearsOfExp);
    }

    /**
     * 打印格式化的名片。
     *
     * @param user 用户信息
     */
    private static void printBusinessCard(UserInfo user) {
        System.out.println();
        printBorder();
        printTitle(user.getName());
        printBorder();
        printField("职位", user.getJobTitle());
        printField("年龄", user.getAge() + "岁");
        printField("工作经验", user.getYearsOfExp() + "年");
        printField("邮箱", user.getEmail());
        printBorder();
    }

    /** 打印名片边框 */
    private static void printBorder() {
        System.out.println("╔═══════════════════════════════════╗");
    }

    /** 打印名片标题 */
    private static void printTitle(String name) {
        System.out.printf("║      %s 的个人名片%14s ║%n", name, "");
    }

    /** 打印单个字段 */
    private static void printField(String label, String value) {
        System.out.printf("║ %s：%-24s ║%n", label, value);
    }
}

/**
 * 用户信息类。
 *
 * 所有字段都是 private final：
 * <ul>
 *   <li><b>private</b>：封装——只有这个类内部能直接访问，外部必须通过 getter 方法。
 *       这样做的好处是：将来如果需要修改数据存储方式（比如从变量改为从数据库读取），
 *       外部代码完全不受影响，因为它们只通过 getter 方法访问数据。</li>
 *   <li><b>final</b>：不可变性——一旦在构造函数中赋值，就不能再修改。
 *       这保证了数据不会被意外改动。想象一下：如果你的程序有个"用户年龄"字段，
 *       某个地方不小心写了 user.age = -1，有了 final 保护，这种错误根本编译不过。</li>
 * </ul>
 *
 * <p>为什么不用普通的变量声明（如 public String name）？</p>
 * <ul>
 *   <li>public 字段可以被任何代码直接修改——你不知道哪里改了它、为什么改了它</li>
 *   <li>private + final 让对象在创建后就是"只读"的——这在多线程环境下尤其重要</li>
 *   <li>未来如果需要加验证逻辑（比如年龄不能小于0），只需要在构造函数里加一次判断</li>
 * </ul>
 */
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

    // Getter 方法
    // 注意：没有 setter 方法——因为字段是 final 的，不允许修改
    public String getName() { return name; }
    public String getJobTitle() { return jobTitle; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
    public double getYearsOfExp() { return yearsOfExp; }
}
```

"等等，"小北打断老潘，"这么多代码……不就打印个名片吗？"

"你今天觉得多，"老潘说，"下个月你回来改这个程序，想在名片上加个'电话'字段——你会发现只需要在 `UserInfo` 类里加一行，`printField()` 会自动处理。这就是工程化：**把可能会变化的部分隔离出来，让修改成本最小化。**"

### 工程思维 vs AI 生成

阿码举手了："那为什么不用 AI 直接生成整个项目？我试过让 AI 生成一个学生管理系统，它一下就生成了几百行代码。"

"AI 能生成代码，但它不能帮你做架构决策。"老潘回答，"比如：
- AI 不知道你的团队有几个人，它可能生成一个过重的设计（2个人根本维护不了）
- AI 不知道你的业务会怎么变化，它可能生成一个难以扩展的结构
- AI 不会写测试，不会写文档，不会做 Code Review

**AI 是工具，你不是工具的使用者——你是工程的负责人。**"

老潘顿了顿："记住这句话：**AI 生成的代码，你要负责审查、测试、维护。** 如果你自己不理解代码在做什么，AI 帮你写的越多，你未来的坑越大。"

### 从"能跑"到"可维护"

总结一下，我们本周学完了"从 Hello World 到工程化"的第一步：

| 维度 | Week 01 初学者 | 工程化思维 |
|------|---------------|-----------|
| 目标 | 代码能运行 | 代码可维护 |
| 变量命名 | `x`、`s1`、`temp` | `userName`、`cardWidth` |
| 注释 | 不写或过度注释 | 解释"为什么" |
| 代码结构 | 全塞在 `main()` 里 | 拆成方法、职责分明 |
| 错误处理 | 碰到再说 | 提前防御、编译期检查 |

这不是要你"必须写出完美代码"——而是要你**开始有意识地思考**：我写的代码，三个月后还能看懂吗？别人能接手吗？改一个需求要动多少地方？

这些问题，就是软件工程的起点。

---

## CampusFlow 进度

本周是项目起点，你需要完成团队组建和项目初始化。

### 1. 组建团队（2-3 人）

找 2-3 个同学组队，确定团队角色分工：
- **后端开发**：负责领域模型、业务逻辑、数据层
- **AI 前端审查**：负责 AI 生成的前端代码审查和测试
- **首席架构师**：轮换担任，负责撰写 ADR 和做架构决策

### 2. 选择选题

从 `shared/book_project.md` 的选题池中选择一个，或自拟选题。确保：
- 功能范围可控（16 周能完成）
- 有明确的差异化设计
- 团队成员都感兴趣

**常见选题示例**：
- TaskFlow（个人任务管理）
- NoteStream（笔记工具）
- BookHub（图书管理）
- RoomReserve（会议室预约）

### 3. 初始化 Git 仓库

```bash
# 创建项目目录
mkdir campusflow-yourteam
cd campusflow-yourteam

# 初始化 Git 仓库
git init

# 创建初始 README
echo "# CampusFlow: [你的项目名]" > README.md
echo "" >> README.md
echo "## 团队成员" >> README.md
echo "- 姓名1：负责后端 + 首席架构师（第1轮）" >> README.md
echo "- 姓名2：负责后端 + AI 前端审查" >> README.md
echo "" >> README.md
echo "## 项目概述" >> README.md
echo "一句话描述你的项目。" >> README.md

# 首次提交
git add README.md
git commit -m "feat: 项目立项 - [项目名]"
```

### 4. 建立项目 README（完整版）

README 是项目的"门面"，应该包含：

```markdown
# CampusFlow: [项目名]

## 团队成员
- [姓名1]：[角色1]
- [姓名2]：[角色2]
- [姓名3]：[角色3]

## 项目概述
[一句话描述你的项目]

## 核心功能
- 功能1：[描述]
- 功能2：[描述]
- 功能3：[描述]

## 技术栈
- 后端：Java 17 + Javalin + SQLite + JDBC
- 前端：AI 生成（HTML + CSS + JavaScript）
- 测试：JUnit 5
- 构建工具：Maven

## 差异化设计
[说明你的项目与同类项目的区别]

## 进度计划
- Week 01-04：项目启动 + 领域模型 + CLI 版
- Week 05-08：后端工程化 + 测试 + 持久化
- Week 09-12：REST API + AI 前端
- Week 13-16：集成测试 + 文档 + 发布
```

### 5. 验证

```bash
# 检查 Git 历史是否有首次提交
git log --oneline

# 检查 README 是否完整
cat README.md
```

你应该看到：
- `git log` 输出至少一次提交
- `README.md` 包含项目基本信息

### 6. 项目锚点

在 README 中记录你的"项目锚点"（anchor）——这是你在 Week 01 学到的工程概念：

```markdown
## 项目锚点

### 主张
我们选择 [Javalin] 而不是 [Spring Boot] 作为 Web 框架，因为：
- Javalin 更轻量，学习曲线更平缓
- 适合小团队快速迭代
- 不牺牲工程化能力（仍能做分层架构）

### 验证方式
- [ ] Week 09 能用 Javalin 启动第一个 REST API
- [ ] Week 12 前后端联调成功
- [ ] 性能满足需求（具体指标待定）
```

**锚点**（anchor）是一个工程概念：把"主张"与"可验证的证据"绑定在一起。你不用在 Week 01 就证明 Javalin 比 Spring Boot 好，但你写下"我们选择 X 因为 Y"，并在后续几周验证这个选择是否正确。

如果后续发现 Javalin 不够用，你再写一个锚点记录"我们为什么迁移到 Spring Boot"。这就是工程决策的演进——不是拍脑袋，而是有记录、可追溯。

---

## Git 本周要点

### 必会命令

```bash
# 初始化仓库
git init

# 查看状态
git status

# 添加文件
git add <file>
git add .  # 添加所有

# 提交
git commit -m "描述性消息"

# 查看历史
git log --oneline
```

### Commit Message 规范

使用 `feat:` / `fix:` 前缀，方便后期追溯：

```bash
git commit -m "feat: 项目立项 - TaskFlow 任务管理系统"
git commit -m "feat: 完成 Task 类的领域模型设计"
git commit -m "fix: 修复 Scanner nextInt() 的换行符问题"
```

### 常见坑

1. **忘记 `git add` 就提交** → 什么都不会发生（Git 会提示"nothing to commit"）
2. **commit message 写得太随意** → `git commit -m "update"` 这样的消息毫无意义，后期无法追溯
3. **把敏感信息（密码、token）提交** → 永远留在历史里（后续教如何清理，但最好一开始就避免）

---

## 本周小结（供下周参考）

本周我们建立了几个关键认知：

1. **软件工程的演进是抽象层次的不断提升**——从机器指令到对象再到 AI 辅助。每个时代都在解决上一个时代的问题。AI 是倍增器，不是替代者。

2. **Python 的灵活适合快速原型，Java 的严格适合长期维护**。类型声明不是"麻烦"，而是你和编译器的契约——把错误从运行时提前到编译期。

3. **"能跑"和"可维护"是两回事**。工程关注后者：命名规范、注释解释"为什么"而不是"是什么"、代码结构清晰可扩展。

4. **锚点（anchor）是工程决策的基础**。把"主张"与"可验证的证据"绑定在一起，让架构决策可追溯、可讨论、可演进。

下周我们将进入**类设计与领域建模**，你会学到如何用类来抽象现实世界的概念（比如 Task、User、Project），并写出第一份 ADR（架构决策记录）。你会发现：Java 的类不只是"代码的组织方式"，它是你理解问题域的工具。

---

## Definition of Done（学生自测清单）

### 知识理解
- [ ] 能用自己的话解释软件工程三个黄金时代的核心差异
- [ ] 能说出静态类型和动态类型的至少 3 个区别
- [ ] 能解释为什么大型项目偏向静态类型

### 代码能力
- [ ] 能从零写出包含 Scanner 输入的完整 Java 程序
- [ ] 能正确声明和使用基本数据类型（int, double, String, boolean）
- [ ] 能解释 IDE 报错信息并修复常见问题

### 环境配置
- [ ] JDK 17 安装成功（`java -version` 显示正确）
- [ ] IntelliJ IDEA 能创建并运行 Java 项目
- [ ] Maven 能构建项目（`mvn -v` 显示正确）
- [ ] Git 配置完成（`git config --list` 包含 user.name 和 user.email）

### 团队项目
- [ ] 团队组建完成（2-3 人）
- [ ] 选题确定并记录在 README
- [ ] Git 仓库初始化完成
- [ ] 首次提交成功且 commit message 符合规范
