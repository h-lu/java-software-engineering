# Week 03 作业：异常处理与防御式编程

> "错误处理不是'锦上添花'，是工程责任。"
> —— 老潘

---

## 作业概述

本周作业分为三层：基础、进阶、挑战。你至少需要完成基础题才能通过本周考核。进阶题和挑战题是可选的，但强烈推荐尝试——它们能帮你真正掌握"让程序在异常情况下依然健壮"的能力。

**本周核心目标**：
1. 区分受检异常与运行时异常，并正确选择使用场景
2. 使用 try-catch-finally 结构优雅地处理异常
3. 应用防御式编程原则，在代码边界处进行输入验证
4. 使用 FMEA 思维分析代码的潜在故障点

---

## 基础题（必做）

### 任务 1：为文件读取添加异常处理

**场景**：小北写了一个从文件加载任务列表的功能，但没有处理异常，程序经常崩溃。

**任务**：

为以下代码添加完整的异常处理：

```java
// 需要改进的代码（文件：TaskFileLoader.java）
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class TaskFileLoader {

    public List<String> loadTasksFromFile(String filename) {
        List<String> tasks = new ArrayList<>();
        File file = new File(filename);
        Scanner scanner = new Scanner(file);  // 可能抛出 FileNotFoundException

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            tasks.add(line);
        }
        scanner.close();

        return tasks;
    }
}
```

**要求**：
1. 使用 try-catch-finally 结构处理 `FileNotFoundException`
2. 处理 `filename` 为 null 的情况（抛出 `IllegalArgumentException`）
3. 确保 `scanner` 在任何情况下都被正确关闭
4. 提供友好的错误信息（不要只打印堆栈跟踪）

**输入示例**：
```java
TaskFileLoader loader = new TaskFileLoader();

// 情况 1：文件存在
List<String> tasks = loader.loadTasksFromFile("tasks.txt");
// 输出：成功读取 3 行任务

// 情况 2：文件不存在
List<String> tasks = loader.loadTasksFromFile("not_exist.txt");
// 输出：错误：找不到文件 'not_exist.txt'

// 情况 3：filename 为 null
List<String> tasks = loader.loadTasksFromFile(null);
// 抛出：IllegalArgumentException: 文件名不能为 null
```

**输出示例**：
```
成功读取 3 行任务
任务列表：[买牛奶, 写作业, 去健身房]

错误：找不到文件 'not_exist.txt'
返回空列表

Exception in thread "main" java.lang.IllegalArgumentException: 文件名不能为 null
```

**提示**：
- 使用 `try-catch-finally` 或 `try-with-resources`（Java 7+）
- `finally` 块中检查 `scanner != null` 再关闭
- 错误信息要具体，帮助用户理解问题

---

### 任务 2：实现自定义异常类

**场景**：CampusFlow 需要一套自己的异常体系，而不是到处抛 `Exception` 或 `RuntimeException`。

**任务**：

为 CampusFlow 设计并实现自定义异常类层次：

1. **基础异常类**（受检异常）：
   - `CampusFlowException` 继承 `Exception`
   - 提供带消息和带原因的构造方法

2. **具体业务异常**：
   - `TaskNotFoundException`：当查找的任务不存在时抛出
   - `InvalidTaskDataException`：当任务数据不合法时抛出（如空标题、无效日期）
   - `DuplicateTaskException`：当添加重复任务时抛出

**输入示例**：
```java
// 使用自定义异常
public class TaskManager {
    public Task findTask(String title) throws TaskNotFoundException {
        // 查找逻辑...
        if (notFound) {
            throw new TaskNotFoundException(title);
        }
    }

    public void addTask(Task task) throws InvalidTaskDataException, DuplicateTaskException {
        // 验证逻辑...
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new InvalidTaskDataException("title", "标题不能为空");
        }
        // 重复检查...
        if (exists) {
            throw new DuplicateTaskException(task.getTitle());
        }
    }
}
```

**输出示例**：
```
TaskNotFoundException: 找不到任务: 买牛奶

InvalidTaskDataException: 任务数据无效 [title]: 标题不能为空

DuplicateTaskException: 任务 '买牛奶' 已存在
```

**要求**：
- 所有自定义异常必须提供有意义的错误信息
- 异常类放在 `exception` 包中
- 考虑哪些应该是受检异常，哪些应该是运行时异常

**提示**：
- 业务异常通常设计为受检异常（调用者必须处理）
- 编程错误（如 null 参数）可以用运行时异常
- 参考 CHAPTER.md 第 3 节的异常选择原则

---

### 任务 3：在 Task 类中添加防御式输入验证

**场景**：上周你设计了 `Task` 类，但没有做输入验证。用户可能输入空标题、未来日期、负数优先级等非法数据。

**任务**：

为 `Task` 类的 setter 方法添加防御式输入验证：

```java
public class Task {
    private String title;
    private String dueDate;  // 格式：YYYY-MM-DD
    private int priority;    // 1=高, 2=中, 3=低
    private boolean completed;

    // 需要添加验证的 setter
    public void setTitle(String title) { /* ... */ }
    public void setDueDate(String dueDate) { /* ... */ }
    public void setPriority(int priority) { /* ... */ }
}
```

**验证规则**：

| 字段 | 验证规则 | 失败时抛出 |
|------|----------|-----------|
| title | 不能为 null 或空字符串；长度 1-100 字符 | `InvalidTaskDataException` |
| dueDate | 格式必须为 YYYY-MM-DD；年份不能早于 2000 | `InvalidTaskDataException` |
| priority | 必须是 1、2 或 3 | `InvalidTaskDataException` |

**输入示例**：
```java
Task task = new Task();

task.setTitle("买牛奶");  // 成功

task.setTitle("");  // 抛出 InvalidTaskDataException: 任务数据无效 [title]: 标题不能为空

task.setTitle(null);  // 抛出 InvalidTaskDataException: 任务数据无效 [title]: 标题不能为 null

task.setTitle("a".repeat(101));  // 抛出 InvalidTaskDataException: 任务数据无效 [title]: 标题不能超过100字符

task.setDueDate("2025-13-45");  // 抛出 InvalidTaskDataException: 任务数据无效 [dueDate]: 日期格式无效

task.setPriority(5);  // 抛出 InvalidTaskDataException: 任务数据无效 [priority]: 优先级必须是1、2或3
```

**输出示例**：
```
标题设置成功：买牛奶

InvalidTaskDataException: 任务数据无效 [title]: 标题不能为空

InvalidTaskDataException: 任务数据无效 [dueDate]: 日期格式无效，应为 YYYY-MM-DD
```

**要求**：
- 每个 setter 都要有完整的参数验证
- 验证失败时抛出 `InvalidTaskDataException`
- 错误信息要具体，指出哪个字段、什么问题
- 考虑使用正则表达式验证日期格式

**提示**：
- 使用 `title != null && !title.trim().isEmpty()` 检查空字符串
- 日期格式验证：`date.matches("\\d{4}-\\d{2}-\\d{2}")`
- 这是防御式编程的实践——"尽早失败"，不让非法数据进入系统

---

### 任务 4：编写 FMEA 分析文档

**场景**：老潘要求团队用 FMEA 方法分析 CampusFlow 的潜在故障点。

**任务**：

为 CampusFlow 编写简化版 FMEA 分析文档 `docs/fmea.md`，至少分析 5 个潜在故障点。

**FMEA 表格模板**：

```markdown
| 功能模块 | 潜在故障 | 影响 | 严重程度(1-10) | 发生频率(1-10) | 检测难度(1-10) | RPN | 预防措施 | 处理策略 |
|---------|---------|------|---------------|---------------|---------------|-----|---------|---------|
| 任务创建 | 空标题 | 数据无效 | 5 | 3 | 2 | 30 | setter 验证 | 抛出异常 |
| ... | ... | ... | ... | ... | ... | ... | ... | ... |
```

**必须分析的故障点**：
1. 任务标题为空或超长
2. 任务文件不存在或无法读取
3. 任务文件格式错误（解析失败）
4. 查找不存在的任务
5. 添加重复任务

**输入示例**（FMEA 文档片段）：
```markdown
## CampusFlow FMEA 分析

### 1. 任务创建模块

| 潜在故障 | 影响 | S | O | D | RPN | 预防/处理 |
|---------|------|---|---|---|-----|----------|
| 空标题 | 数据无效 | 5 | 3 | 2 | 30 | setter 验证，尽早失败 |
| 标题超长(>100字符) | 显示混乱 | 4 | 2 | 2 | 16 | setter 验证，限制长度 |

### 2. 文件操作模块

| 潜在故障 | 影响 | S | O | D | RPN | 预防/处理 |
|---------|------|---|---|---|-----|----------|
| 文件不存在 | 程序崩溃 | 8 | 6 | 2 | 96 | try-catch，优雅降级 |
| 文件格式错误 | 解析失败 | 6 | 4 | 3 | 72 | 逐行解析，错误行记录 |
```

**输出示例**：
- 完整的 FMEA 文档（1-2 页）
- 至少 5 个故障点的分析
- 每个故障点都有对应的代码实现（参考任务 1-3）

**要求**：
- 使用 RPN（Risk Priority Number）= 严重程度 × 发生频率 × 检测难度
- 按 RPN 排序，优先处理高风险项
- 每个故障点都要有对应的代码实现策略

**提示**：
- FMEA 不是文档工作，是风险思维训练
- 参考 CHAPTER.md 第 5 节的 FMEA 示例
- 思考"用户会怎么搞砸这个程序"

---

## 进阶题（选做）

### 任务 5：为 CampusFlow 添加完整的异常处理层

**场景**：现在 CampusFlow 有了自定义异常，但还没有统一的异常处理策略。

**任务**：

为 CampusFlow 设计并实现完整的异常处理层：

1. **异常转换层**：将底层异常（如 `IOException`）转换为领域异常
2. **统一异常处理**：在 CLI 层统一捕获和显示异常
3. **错误恢复策略**：不同异常的不同恢复方式

**输入示例**：
```java
// 异常转换
public class TaskRepository {
    public List<Task> loadFromFile(String filename) throws CampusFlowException {
        try {
            // 文件读取逻辑...
        } catch (FileNotFoundException e) {
            throw new TaskFileException("无法加载任务文件: " + filename, e);
        } catch (IOException e) {
            throw new TaskFileException("读取任务文件时出错: " + filename, e);
        }
    }
}

// 统一异常处理
public class TaskCLI {
    public void run() {
        while (true) {
            try {
                processCommand();
            } catch (InvalidTaskDataException e) {
                System.out.println("输入错误：" + e.getMessage());
                System.out.println("请检查输入后重试");
            } catch (TaskNotFoundException e) {
                System.out.println("未找到：" + e.getMessage());
                System.out.println("使用 'list' 查看所有任务");
            } catch (CampusFlowException e) {
                System.out.println("系统错误：" + e.getMessage());
                // 记录日志...
            }
        }
    }
}
```

**输出示例**：
```
=== CampusFlow 任务管理 ===
1. 添加任务
2. 查看任务
3. 标记完成
4. 退出

请选择：1
输入任务标题：
输入错误：任务数据无效 [title]: 标题不能为空
请检查输入后重试

请选择：2
未找到：找不到任务: 不存在的任务
使用 'list' 查看所有任务
```

**要求**：
- 所有底层异常都要转换为领域异常
- CLI 层有统一的异常处理逻辑
- 错误信息对用户友好
- 考虑添加日志记录（可选）

---

### 任务 6：资源管理与 try-with-resources

**场景**：CampusFlow 需要同时管理多个资源（文件、数据库连接等），要确保资源正确关闭。

**任务**：

使用 `try-with-resources` 改进文件操作代码：

1. 实现 `TaskRepository` 的 `saveToFile` 方法
2. 同时打开多个资源（如读取旧文件、写入新文件）
3. 确保所有资源在任何情况下都被关闭

**输入示例**：
```java
public class TaskRepository {
    public void saveToFile(String filename, List<Task> tasks) throws TaskFileException {
        // 使用 try-with-resources 确保 PrintWriter 被关闭
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Task task : tasks) {
                writer.println(formatTask(task));
            }
        } catch (IOException e) {
            throw new TaskFileException("保存任务文件失败: " + filename, e);
        }
    }

    public void backupAndSave(String filename, List<Task> tasks) throws TaskFileException {
        // 同时管理多个资源：读取旧文件作为备份，写入新文件
        try (Scanner reader = new Scanner(new File(filename + ".bak"));
             PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // 备份逻辑...
            // 保存逻辑...
        } catch (IOException e) {
            throw new TaskFileException("备份或保存失败", e);
        }
    }
}
```

**输出示例**：
```
任务保存成功，共 5 条任务

备份创建成功：tasks.txt.bak
任务保存成功：tasks.txt
```

**要求**：
- 使用 `try-with-resources` 语法（Java 7+）
- 展示同时管理多个资源
- 异常处理要完整

**提示**：
- `try-with-resources` 自动调用 `close()`，不需要 finally 块
- 多个资源用分号分隔：`try (Resource1 r1 = ...; Resource2 r2 = ...)`
- 资源必须实现 `AutoCloseable` 接口

---

## 挑战题（可选）

### 任务 7：异常处理策略设计

**场景**：老潘问了一个问题："CampusFlow 的异常处理策略应该是什么？什么时候用受检异常，什么时候用运行时异常？"

**任务**：

为 CampusFlow 设计完整的异常处理策略文档 `docs/exception-strategy.md`，包含：

1. **异常分类原则**：
   - 什么情况下使用受检异常
   - 什么情况下使用运行时异常
   - 什么情况下不应该捕获异常

2. **异常转换规则**：
   - 底层异常如何转换为领域异常
   - 何时保留原始异常（cause）
   - 何时包装为新的异常类型

3. **错误恢复策略**：
   - 不同异常类型的恢复方式
   - 重试策略（如网络超时）
   - 优雅降级策略

4. **与 Week 02 设计的结合**：
   - 异常处理如何融入领域模型
   - 每个类的异常职责

**输入示例**（策略文档片段）：
```markdown
## CampusFlow 异常处理策略

### 1. 异常分类原则

**受检异常（必须处理）**：
- 外部资源不可用：文件不存在、网络断开
- 业务规则违反：任务不存在、重复添加
- 原因：调用者可以合理恢复（选择其他文件、提示用户）

**运行时异常（不强制处理）**：
- 编程错误：null 参数、数组越界
- 前置条件违反：空标题（应在 UI 层验证）
- 原因：调用者无法恢复，应该修复代码

### 2. 异常转换规则

```java
// 底层异常 → 领域异常
try {
    // IO 操作
} catch (IOException e) {
    // 保留原始异常作为 cause
    throw new TaskFileException("操作失败", e);
}
```

### 3. 错误恢复策略

| 异常类型 | 恢复策略 | 示例 |
|---------|---------|------|
| TaskNotFoundException | 提示用户，建议操作 | "任务不存在，使用 list 查看所有任务" |
| TaskFileException | 优雅降级，使用默认值 | 文件不存在时返回空列表 |
| InvalidTaskDataException | 提示具体错误，允许重试 | "标题不能为空，请重新输入" |
```

**输出示例**：
- 完整的策略文档（2-3 页）
- 与代码实现对应（参考任务 1-6）
- 有具体的设计决策和理由

**要求**：
- 策略要与代码实现一致
- 引用本周学到的异常处理原则
- 考虑可维护性和用户体验

---

## 提交要求

### 必交内容（基础题）

1. **源代码**：
   - `src/main/java/edu/campusflow/exception/CampusFlowException.java`
   - `src/main/java/edu/campusflow/exception/TaskNotFoundException.java`
   - `src/main/java/edu/campusflow/exception/InvalidTaskDataException.java`
   - `src/main/java/edu/campusflow/exception/DuplicateTaskException.java`
   - `src/main/java/edu/campusflow/util/TaskFileLoader.java`（任务 1）
   - `src/main/java/edu/campusflow/domain/Task.java`（添加验证后的版本）

2. **FMEA 文档**：
   - `docs/fmea.md`

3. **测试代码**：
   - 至少 3 个测试方法，验证异常处理和输入验证

### 选交内容（进阶/挑战）

1. **进阶题代码**：
   - `src/main/java/edu/campusflow/repository/TaskRepository.java`（任务 5、6）
   - `src/main/java/edu/campusflow/cli/TaskCLI.java`（任务 5）

2. **挑战题策略文档**（任务 7）：
   - `docs/exception-strategy.md`

### 提交格式

```bash
# Git 提交
git add src/main/java/edu/campusflow/exception/
git add src/main/java/edu/campusflow/util/TaskFileLoader.java
git add src/main/java/edu/campusflow/domain/Task.java
git add docs/fmea.md
git commit -m "feat: 完成 Week 03 基础题 - 异常处理与防御式编程"

# 如果做了进阶题
git add src/main/java/edu/campusflow/repository/
git add src/main/java/edu/campusflow/cli/
git commit -m "feat: 完成 Week 03 进阶题 - 完整异常处理层"
```

---

## 常见错误与避坑指南

### 错误 1：吞异常

**问题**：catch 块里什么都不做，问题被隐藏了
```java
// ❌ 不好的做法
try {
    file.read();
} catch (IOException e) {
    // 什么都不做——异常被吞掉了！
}
```

**正确做法**：
- 至少记录日志或打印错误信息
- 如果确实要忽略，注释说明原因
```java
// ✅ 好的做法
try {
    file.read();
} catch (IOException e) {
    System.err.println("读取文件失败: " + e.getMessage());
    // 或者重新抛出
    throw new TaskFileException("读取失败", e);
}
```

---

### 错误 2：过度使用受检异常

**问题**：每个方法都 throws 一堆异常，调用者被迫写大量 catch 块
```java
// ❌ 过度使用受检异常
public void method() throws ExceptionA, ExceptionB, ExceptionC, ExceptionD {
    // ...
}
```

**正确做法**：
- 用自定义异常包装多个底层异常
- 考虑用运行时异常表示编程错误
```java
// ✅ 好的做法
public void method() throws CampusFlowException {
    try {
        // 可能抛出多种异常
    } catch (IOException | SQLException e) {
        throw new CampusFlowException("操作失败", e);
    }
}
```

---

### 错误 3：忽略资源关闭

**问题**：在异常发生时资源没有关闭
```java
// ❌ 不好的做法
Scanner scanner = new Scanner(file);
// 如果这里抛出异常，scanner 不会关闭
process(scanner);
scanner.close();
```

**正确做法**：
- 使用 `try-with-resources`
- 或在 `finally` 块中关闭
```java
// ✅ 好的做法（try-with-resources）
try (Scanner scanner = new Scanner(file)) {
    process(scanner);
}  // scanner 自动关闭

// ✅ 好的做法（finally）
Scanner scanner = null;
try {
    scanner = new Scanner(file);
    process(scanner);
} finally {
    if (scanner != null) {
        scanner.close();
    }
}
```

---

### 错误 4：捕获过于宽泛的异常

**问题**：捕获 `Exception` 或 `Throwable`，隐藏了真正的问题
```java
// ❌ 不好的做法
try {
    // 一些操作
} catch (Exception e) {  // 太宽泛了！
    // 所有异常都被捕获，包括编程错误
}
```

**正确做法**：
- 捕获具体的异常类型
- 只有顶层方法才捕获 `Exception` 作为兜底
```java
// ✅ 好的做法
try {
    // 文件操作
} catch (FileNotFoundException e) {
    // 处理文件不存在
} catch (IOException e) {
    // 处理其他 IO 错误
}
```

---

### 错误 5：FMEA 流于形式

**问题**：FMEA 表格填写随意，没有指导代码实现
```markdown
| 故障 | 影响 | 处理 |
|-----|------|------|
| 出错 | 不好 | 处理 |
```

**正确做法**：
- 具体描述故障场景
- 量化评估（S/O/D）
- 每个故障点都要有对应的代码实现

---

## 评分标准

详细评分标准见 `RUBRIC.md`。

简要说明：
- **基础题**：60 分 - 完成任务 1-4，异常处理正确，FMEA 文档完整
- **进阶题**：+20 分 - 完成任务 5 或 6，异常处理层设计合理
- **挑战题**：+20 分 - 完成任务 7，异常处理策略有深度

---

## 学习资源

- **本周章节**：`chapters/week_03/CHAPTER.md`
- **起步代码**：`starter_code/src/main/java/`（如果你遇到困难）
- **风格指南**：`shared/style_guide.md`
- **AI 辅助**：可以用 AI 生成异常类骨架，但必须自己审查和修改

---

## 起步代码使用说明

如果你遇到困难，可以参考 `starter_code/` 目录中的起步代码：

```bash
# 起步代码位置
starter_code/
├── src/main/java/edu/campusflow/exception/     # 异常类模板
├── src/main/java/edu/campusflow/util/          # TaskFileLoader 模板
└── src/main/java/edu/campusflow/domain/        # Task 类验证模板
```

**使用建议**：
1. 先自己尝试完成任务
2. 遇到困难时参考起步代码的**思路**，不要直接复制
3. 起步代码可能故意留有一些问题，你需要发现并修复

---

## 截止日期

**提交截止**：本周日 23:59

**延迟提交**：每晚一天扣 10% 分数，最多延迟 3 天
