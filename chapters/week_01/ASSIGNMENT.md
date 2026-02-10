# Week 01 作业：抽象层次与软件工程史

> **提交截止**：Week 02 上课前
> **提交方式**：将代码提交到团队 Git 仓库 + 提交作业报告到教学平台

---

## 作业概述

本周作业有三个目标：
1. **环境配置**：搭建完整的 Java 开发环境（JDK 17、IntelliJ、Maven、Git）
2. **编码练习**：完成一个从 Hello World 演进到个人名片生成器的练习
3. **团队项目启动**：组建团队、确定选题、初始化 Git 仓库

如果你在作业中遇到困难，可以参考 `starter_code/` 目录中的起步代码（但不要照抄）。

---

## 基础练习（必做）

### 练习 1：环境配置验证

**目标**：确保所有工具都能正常运行。

**步骤**：

1. **验证 JDK 安装**
   ```bash
   java -version
   ```
   你应该看到类似 `openjdk version "17.x.x"` 的输出。

2. **验证 Maven 安装**
   ```bash
   mvn -v
   ```
   你应该看到 Maven 版本信息和 Java 版本。

3. **验证 Git 配置**
   ```bash
   git config user.name
   git config user.email
   ```
   如果没有配置，执行：
   ```bash
   git config --global user.name "你的名字"
   git config --global user.email "你的邮箱"
   ```

**验收标准**：
- [ ] 上述三个命令都能正常输出
- [ ] 能在 IntelliJ 中创建新的 Java 项目

**常见错误**：
- `java: command not found`：JDK 没有正确安装或没有添加到 PATH
- `mvn: command not found`：Maven 没有正确安装

---

### 练习 2：Hello World 程序

**目标**：写出第一个可运行的 Java 程序，理解 Java 的基本结构。

**要求**：

创建一个 `HelloWorld.java` 文件，输出以下内容：

```
================================
       张三的个人名片
================================
职位：软件工程师
邮箱：zhangsan@example.com
电话：138-0000-0000
================================
```

**输入示例**：
无需输入（硬编码输出）

**输出示例**：
```
================================
       张三的个人名片
================================
职位：软件工程师
邮箱：zhangsan@example.com
电话：138-0000-0000
================================
```

**验收标准**：
- [ ] 代码能编译通过
- [ ] 运行后输出与示例一致
- [ ] 理解 `public class`、`public static void main`、`System.out.println` 的作用

**提示**：
- Java 的文件名必须与类名一致
- 使用 `System.out.println()` 输出每行
- `main` 方法是 Java 程序的入口点

**常见错误**：
- 文件名与类名不一致（如文件叫 `hello.java` 但类是 `HelloWorld`）
- 忘记写分号 `;`
- `main` 方法签名写错（漏了 `String[] args`）

---

### 练习 3：变量声明练习

**目标**：理解静态类型的变量声明，与 Python 做对比。

**要求**：

修改 Hello World，使用变量存储个人信息：

```java
// 伪代码提示
String name = ...;
String job = ...;
String email = ...;

System.out.println("================================");
System.out.println("       " + name + "的个人名片");
// ...
```

**输入示例**：
无

**输出示例**：
```
================================
       张三的个人名片
================================
职位：软件工程师
邮箱：zhangsan@example.com
电话：138-0000-0000
================================
```

**验收标准**：
- [ ] 使用 `String` 类型声明变量
- [ ] 理解为什么 `String name = "张三"` 必须声明类型
- [ ] 能用自己的话解释"静态类型"与"动态类型"的区别

**思考题**（不提交）：
- 如果写 `int name = "张三"` 会发生什么？为什么？
- Python 里 `name = 123` 和 `name = "abc"` 都是合法的，Java 里为什么不行？

---

### 练习 4：Scanner 输入练习

**目标**：让程序能读取用户输入，理解 Scanner 的基本用法。

**要求**：

创建 `VariableDemo.java`，读取用户输入的姓名、职位和年龄，然后输出。

**输入示例**：
```
请输入你的名字：李四
请输入你的职位：产品经理
请输入你的年龄：30
```

**输出示例**：
```
=== 你的名片 ===
姓名：李四
职位：产品经理
年龄：30岁
```

**验收标准**：
- [ ] 能正确读取用户输入
- [ ] 能处理整数输入（年龄）
- [ ] 记得关闭 Scanner（`scanner.close()`）

**提示**：
- 使用 `scanner.nextLine()` 读取字符串
- 使用 `Integer.parseInt(scanner.nextLine())` 读取整数（避免换行符问题）
- 为什么不用 `scanner.nextInt()`？因为 `nextInt()` 会遗留换行符，导致后续的 `nextLine()` 跳过

**常见错误**：
- 混用 `nextInt()` 和 `nextLine()` 导致输入跳过
- 忘记 `import java.util.Scanner;`
- 输入提示后没有给用户输入的机会（程序直接结束）

---

## 进阶练习（必做）

### 练习 5：完整的名片生成器

**目标**：将所有知识点整合，完成一个功能完整的名片生成器。

**要求**：

创建 `BusinessCard.java`，读取用户输入的以下信息：
- 姓名（字符串）
- 职位（字符串）
- 邮箱（字符串）
- 年龄（整数）
- 工作年限（浮点数）

然后输出格式化的名片。

**输入示例**：
```
请输入姓名：王五
请输入职位：数据分析师
请输入邮箱：wangwu@example.com
请输入年龄：27
请输入工作年限：4.5
```

**输出示例**：
```
================================
       王五 的个人名片
================================
职位：数据分析师
年龄：27岁
工作经验：4.5年
邮箱：wangwu@example.com
================================
```

**验收标准**：
- [ ] 能正确读取 5 个字段
- [ ] 使用了正确的数据类型（String、int、double）
- [ ] 输出格式与示例一致
- [ ] 代码有适当的变量命名（不要用 `s1`、`x` 这种无意义命名）

**加分项**（可选）：
- [ ] 使用 `printf` 格式化输出，让边框对齐
- [ ] 添加输入验证（如年龄不能为负数）
- [ ] 支持生成多张名片（循环）

**常见错误**：
- 变量命名不清晰（如 `a`、`b`、`c`）
- 直接使用 `scanner.nextDouble()` 导致后续输入问题
- 忘记处理字符串拼接的空格

---

### 练习 6：类型安全练习

**目标**：体会静态类型的好处，理解为什么大型项目需要类型系统。

**要求**：

创建 `TypeComparison.java`，对比两种实现方式：

**版本 1**（像 Python 一样，全部用 String 存储）：
```java
// 伪代码
String age = scanner.nextLine();  // "27"
int ageAfter10Years = age + 10;   // ❌ 编译不通过
```

**版本 2**（使用正确的类型）：
```java
// 伪代码
int age = Integer.parseInt(scanner.nextLine());  // 27
int ageAfter10Years = age + 10;                  // ✅ 正确
```

**思考题**（写入作业报告）：
1. 为什么版本 1 编译不通过？这到底是"麻烦"还是"保护"？
2. 如果是 Python，`age = "27"; age + 10` 会发生什么？哪个更安全？
3. 想象一个电商系统的价格计算场景，如果价格被错误地存成字符串，会有什么后果？

**验收标准**：
- [ ] 理解为什么版本 1 编译不通过
- [ ] 能用自己的话解释"类型安全"的价值
- [ ] 完成思考题

---

## 挑战练习（可选）

### 练习 7：添加输入验证

**目标**：学会防御式编程，处理异常输入。

**要求**：

修改名片生成器，添加以下验证：
1. 姓名不能为空
2. 年龄必须是正整数（1-120）
3. 工作年限不能为负数
4. 邮箱必须包含 `@` 符号

如果输入不合法，提示用户重新输入。

**输入示例**（错误场景）：
```
请输入姓名：
姓名不能为空，请重新输入：李四

请输入年龄：-5
年龄必须是 1-120 之间的整数，请重新输入：25
```

**提示**：
- 使用 `while` 循环实现"重新输入"
- 使用 `String.isEmpty()` 检查空字符串
- 使用 `String.contains("@")` 检查邮箱格式
- 对于数字输入验证，建议使用正则表达式（如 `input.matches("\\d+")`）判断是否为数字
- 更完善的 `try-catch` 异常处理方式会在 **Week 03** 详细讲解，本周作业不要求掌握异常处理

**代码示例**（年龄验证）：

```java
// 年龄验证示例（Week 03 会讲更完善的异常处理版本）
Scanner scanner = new Scanner(System.in);

int age = 0;
boolean validInput = false;

while (!validInput) {
    System.out.print("请输入年龄：");
    String input = scanner.nextLine();

    // 简单验证：检查是否为数字（用 if 判断，避免使用 try-catch）
    if (input.matches("\\d+")) {  // 正则表达式：全是数字
        age = Integer.parseInt(input);
        if (age >= 1 && age <= 120) {
            validInput = true;  // 输入合法，退出循环
        } else {
            System.out.println("年龄必须是 1-120 之间的整数，请重新输入。");
        }
    } else {
        System.out.println("输入的不是有效整数，请重新输入。");
    }
}

System.out.println("年龄：" + age + "岁");
```

> **提示**：上面用正则表达式 `input.matches("\\d+")` 判断是否全是数字，这是 Week 01 级别的简化方案。
>
> 更完善的版本需要用 `try-catch` 捕获 `NumberFormatException`——那是 **Week 03 的内容**。如果你现在就想了解，可以预习，但本周作业不要求掌握异常处理。

**输入示例**：
```
请输入年龄：abc
输入的不是有效整数，请重新输入。
请输入年龄：-5
年龄必须是 1-120 之间的整数，请重新输入。
请输入年龄：25
年龄：25岁
```

**验收标准**：
- [ ] 能检测并处理至少 2 种无效输入
- [ ] 用户体验友好（清晰的错误提示）
- [ ] 程序不会因为无效输入而崩溃

---

### 练习 8：支持多张名片

**目标**：理解循环和集合的基本用法。

**要求**：

允许用户连续生成多张名片，直到输入 "quit" 为止。

**输入示例**：
```
=== 名片生成器 ===
请输入姓名（输入 quit 退出）：张三
...（生成名片）
请输入姓名（输入 quit 退出）：李四
...（生成名片）
请输入姓名（输入 quit 退出）：quit
再见！
```

**验收标准**：
- [ ] 使用 `while` 循环实现
- [ ] 能正确处理退出指令
- [ ] 每次生成的名片都正确显示

**加分项**（可选）：
- [ ] 将生成的名片存储在 `ArrayList` 中（预习 Week 05）
- [ ] 支持查询已生成的名片（按姓名搜索）

---

## 团队项目启动（必做）

### 练习 9：团队组建与选题

**目标**：组建 2-3 人的团队，确定贯穿项目（CampusFlow）的选题。

**要求**：

1. **组建团队**
   - 找 2-3 个同学组队
   - 确定角色分工：
     - **后端开发**：负责领域模型、业务逻辑、数据层
     - **AI 前端审查**：负责 AI 生成的前端代码审查和测试
     - **首席架构师**：轮换担任，负责撰写 ADR 和做架构决策

2. **选择选题**
   - 从以下选题中选择一个，或自拟选题：
     - TaskFlow（个人任务管理）
     - NoteStream（笔记工具）
     - BookHub（图书管理）
     - RoomReserve（会议室预约）
   - 确保：
     - 功能范围可控（16 周能完成）
     - 有明确的差异化设计
     - 团队成员都感兴趣

**验收标准**：
- [ ] 团队组建完成（2-3 人）
- [ ] 选题确定并记录在 README
- [ ] 角色分工明确

---

### 练习 10：初始化 Git 仓库

**目标**：建立项目的 Git 仓库，完成首次提交。

**要求**：

1. **初始化仓库**
   ```bash
   mkdir campusflow-yourteam
   cd campusflow-yourteam
   git init
   ```

2. **创建 README.md**

   ```markdown
   # CampusFlow: [你的项目名]

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
   ```

3. **完成首次提交**

   ```bash
   git add README.md
   git commit -m "feat: 项目立项 - [项目名]"
   ```

4. **验证**

   ```bash
   git log --oneline
   ```

**验收标准**：
- [ ] Git 仓库初始化成功
- [ ] README.md 包含项目基本信息
- [ ] 首次提交的 commit message 符合规范（`feat: ...`）
- [ ] `git log` 能看到提交记录

**常见错误**：
- 忘记 `git add` 就提交（Git 提示 "nothing to commit"）
- commit message 写得太随意（如 `update`、`fix bug`）
- 把项目放在错误的位置（应该放在团队共享的位置，如 GitHub/GitLab）

---

### 练习 11：建立项目锚点

**目标**：理解"锚点"（anchor）的概念，记录项目的技术决策。

**要求**：

在 README.md 中添加"项目锚点"部分：

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

**思考题**（写入作业报告）：
1. 什么是"锚点"？它和"拍脑袋做决定"有什么区别？
2. 为什么要在 Week 01 就记录这个锚点，而不是 Week 09 再决定？
3. 如果后续发现 Javalin 不够用，应该怎么办？

**验收标准**：
- [ ] README 中包含"项目锚点"部分
- [ ] 清楚地表达了"主张"和"验证方式"
- [ ] 完成思考题

---

## AI 协作练习（可选）

> **Week 01 属于 AI 融合的"观察期"，AI 练习设为可选。**
> **本周重点是理解原理，不要过度依赖 AI。**

### 练习 12：用 AI 解释报错信息

**场景**：

你在写 Java 代码时遇到了编译错误：

```java
VariableDemo.java:5: error: cannot find symbol
        name = "张三";
        ^
  symbol:   variable name
  location: class VariableDemo
```

**任务**：

1. 将这段报错信息复制给 AI 工具（如 ChatGPT、Claude）
2. 问 AI："这个报错是什么意思？怎么修复？"
3. **重要**：不要直接复制 AI 的代码，尝试理解它的解释
4. 自己修复代码，验证是否正确

**思考题**（写入作业报告）：
1. AI 的解释准确吗？有没有遗漏什么？
2. 如果没有 AI，你会怎么自己解决这个报错？
3. AI 能帮你"快速解决问题"，但它能帮你"理解为什么会出错"吗？

**验收标准**：
- [ ] 成功修复了编译错误
- [ ] 理解错误的原因（不是只知道"怎么改"）
- [ ] 完成思考题

---

### 练习 13：审查 AI 生成的代码（预习）

**场景**：

你让 AI 生成一个名片生成器的代码，它给出了以下实现：

```java
// AI 生成的代码（故意包含问题）
import java.util.Scanner;

public class BusinessCard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入姓名：");
        String name = scanner.next();  // 问题1：可能读取不完整

        System.out.print("请输入年龄：");
        int age = scanner.nextInt();   // 问题2：遗留换行符

        System.out.print("请输入邮箱：");
        String email = scanner.nextLine();  // 问题3：会直接跳过

        System.out.println("姓名：" + name);
        System.out.println("年龄：" + age);
        System.out.println("邮箱：" + email);

        // 问题4：忘记关闭 scanner
    }
}
```

**任务**：

审查这段代码，找出所有问题：
- [ ] 代码能运行吗？如果不能，为什么？
- [ ] 变量命名清晰吗？
- [ ] 有没有缺少错误处理的地方？
- [ ] 边界情况处理了吗？（如输入空字符串）
- [ ] 你能写一个让它失败的测试吗？

**提交**：
修复后的代码 + 你发现了哪些问题的简短说明。

**提示**：
- `scanner.next()` 只读取下一个单词（以空格分隔），不是整行
- `scanner.nextInt()` 会遗留换行符
- 应该用 `scanner.nextLine()` 或 `Integer.parseInt(scanner.nextLine())`
- Scanner 需要关闭（`scanner.close()`）

---

## 作业报告要求

### 提交内容

1. **代码部分**
   - 将所有 Java 代码提交到团队 Git 仓库
   - 代码结构清晰，有适当的注释
   - 变量命名符合 Java 规范

2. **报告部分**（提交到教学平台）
   - 包含以下内容：
     1. 环境配置截图（3 个命令的输出）
     2. 练习 6 的思考题答案
     3. 练习 11 的思考题答案
     4. 团队项目信息（成员、选题、GitHub 仓库链接）
     5. 如果完成了 AI 协作练习，包含思考题答案

### 报告模板

```markdown
# Week 01 作业报告

## 环境配置
[粘贴 java -version、mvn -v、git config 输出]

## 练习 6：类型安全思考题
1. ...
2. ...
3. ...

## 练习 11：项目锚点思考题
1. ...
2. ...
3. ...

## 团队项目
- 团队成员：...
- 选题：...
- GitHub 仓库：...

## AI 协作练习（如果完成）
[思考题答案]
```

---

## 验收清单

### 基础练习（30 分）
- [ ] 环境配置完成（3 个命令都能正常运行）
- [ ] HelloWorld.java 能编译运行
- [ ] 变量声明练习完成
- [ ] Scanner 输入练习完成

### 进阶练习（30 分）
- [ ] 完整的名片生成器能正确运行
- [ ] 使用了正确的数据类型
- [ ] 变量命名清晰
- [ ] 类型安全思考题完成

### 代码质量（20 分）
- [ ] 代码能编译通过
- [ ] 变量命名符合 Java 规范
- [ ] 有适当的注释
- [ ] 代码结构清晰

### 团队项目（20 分）
- [ ] 团队组建完成
- [ ] 选题确定
- [ ] Git 仓库初始化成功
- [ ] README 包含项目锚点
- [ ] 项目锚点思考题完成

### AI 练习（加分项）
- [ ] 完成 AI 解释报错练习
- [ ] 完成 AI 代码审查练习

---

## 常见错误汇总

### 编译错误
1. **文件名与类名不一致**
   - 错误：`class HelloWorld` 在 `hello.java` 文件中
   - 修复：文件名改为 `HelloWorld.java`

2. **找不到符号**
   - 错误：`cannot find symbol: variable name`
   - 原因：忘记声明变量类型
   - 修复：`String name = ...;`

3. **缺少分号**
   - 错误：`';' expected`
   - 修复：在语句末尾加 `;`

### 运行时错误
1. **输入跳过**
   - 现象：`nextInt()` 后的 `nextLine()` 直接跳过
   - 原因：`nextInt()` 不读取换行符
   - 修复：统一用 `nextLine()` 或加一个 `nextLine()` 吃掉换行符

2. **类型转换错误**
   - 现象：输入 "abc" 给 `Integer.parseInt()` 导致程序崩溃
   - 原因：输入不是有效整数
   - 修复：本周可以用 `input.matches("\\d+")` 先检查是否为数字，Week 03 会讲更完善的异常处理

### Git 错误
1. **没有提交内容**
   - 错误：`nothing to commit`
   - 原因：忘记 `git add`
   - 修复：先 `git add .` 再提交

2. **Commit message 太随意**
   - 不推荐：`git commit -m "update"`
   - 推荐：`git commit -m "feat: 完成名片生成器"`

---

## 学习资源

### 官方文档
- [Java 17 文档](https://docs.oracle.com/en/java/javase/17/)
- [Maven 入门指南](https://maven.apache.org/guides/getting-started/)
- [Git 官方教程](https://git-scm.com/doc)

### 推荐阅读
- [On Java 8](https://onjava8.com/)（Thinking in Java 作者的最新版）
- [Effective Java (3rd Edition)](https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/)（进阶）

### 视频教程
- [Java 编程基础（B站）](https://www.bilibili.com/)
- [Git 入门教程](https://www.bilibili.com/)

---

**最后提醒**：本周重点是"理解原理"，不要因为代码写不出来就沮丧。多看报错信息，多问"为什么"，慢慢你会发现 Java 的"严格"是在帮你。
