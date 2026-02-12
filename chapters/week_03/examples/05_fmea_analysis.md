# FMEA 分析文档：CampusFlow 异常处理

## 概述

本文档使用 FMEA（Failure Mode and Effects Analysis，故障模式与影响分析）方法，系统性地分析 CampusFlow 项目可能遇到的故障点，并制定相应的预防和处理策略。

---

## 什么是 FMEA

FMEA 是一种系统化的风险管理方法，起源于航空航天工程，现在广泛应用于软件开发。它帮助我们：

1. **识别故障模式**：什么可能出错？
2. **评估影响**：出错后会发生什么？
3. **制定预防措施**：怎么防止故障发生？
4. **设计检测方法**：怎么发现故障？

---

## CampusFlow FMEA 分析表

### 核心功能模块故障分析

| 功能模块 | 潜在故障 | 影响 | 严重程度(S) | 发生频率(O) | 检测难度(D) | RPN | 预防措施 | 处理策略 |
|---------|---------|------|-----------|-----------|-----------|-----|---------|---------|
| 任务创建 | 空标题 | 数据无效 | 5 | 3 | 2 | 30 | setter 验证 | 抛出运行时异常 |
| 任务创建 | 标题超长(>100字符) | 显示错乱 | 4 | 2 | 2 | 16 | setter 验证 | 抛出运行时异常 |
| 任务创建 | 非法日期格式 | 解析失败 | 6 | 4 | 3 | 72 | 正则验证 | 抛出运行时异常 |
| 任务创建 | 无效优先级 | 逻辑错误 | 5 | 2 | 2 | 20 | 枚举值验证 | 抛出运行时异常 |
| 任务查询 | 任务不存在 | NPE 或逻辑错误 | 7 | 5 | 4 | 140 | 防御式检查 | 返回 Optional 或抛异常 |
| 任务查询 | null 查询条件 | NPE | 8 | 3 | 2 | 48 | 入口验证 | 抛出运行时异常 |
| 文件加载 | 文件不存在 | 程序崩溃 | 8 | 6 | 2 | 96 | 前置检查 | 捕获受检异常，提示用户 |
| 文件加载 | 文件格式错误 | 解析失败 | 7 | 4 | 3 | 84 | 格式验证 | 捕获异常，记录错误行 |
| 文件加载 | 文件权限不足 | 读取失败 | 6 | 2 | 4 | 48 | 权限检查 | 捕获异常，提示用户 |
| 文件保存 | 磁盘空间不足 | 数据丢失 | 10 | 2 | 5 | 100 | 前置检查 | 捕获异常，提示用户 |
| 文件保存 | 文件被占用 | 写入失败 | 6 | 3 | 3 | 54 | 重试机制 | 捕获异常，提示用户 |
| 用户输入 | 非法数字格式 | 程序崩溃 | 7 | 5 | 2 | 70 | 类型检查 | try-catch，提示重输 |
| 用户输入 | 空输入 | 逻辑错误 | 5 | 4 | 2 | 40 | 空值检查 | 提示重新输入 |
| 数据操作 | 重复添加任务 | 数据冗余 | 4 | 4 | 3 | 48 | 重复检查 | 抛出运行时异常 |
| 数据操作 | 并发修改 | 数据不一致 | 8 | 2 | 8 | 128 | 同步机制 | 记录日志，重试 |

> **RPN（Risk Priority Number）= 严重程度(S) × 发生频率(O) × 检测难度(D)**
>
> RPN 越高，风险越大，需要优先处理。

---

## 高风险项详细分析（RPN > 80）

### 1. 任务不存在时返回 null（RPN = 140）

**风险描述**：调用者可能忘记检查 null，导致 NullPointerException 在系统深处发生。

**处理策略**：
```java
// 方案 A：返回 Optional（推荐）
public Optional<Task> findTask(String title) {
    return tasks.stream()
        .filter(t -> t.getTitle().equals(title))
        .findFirst();
}

// 方案 B：抛出受检异常
public Task findTaskChecked(String title) throws TaskNotFoundException {
    // ...
}

// 方案 C：抛出运行时异常（编程错误场景）
public Task getTask(String title) {
    Task task = findInList(title);
    if (task == null) {
        throw new TaskNotFoundRuntimeException(title);
    }
    return task;
}
```

### 2. 磁盘空间不足导致数据丢失（RPN = 100）

**风险描述**：保存任务时磁盘空间不足，可能导致数据丢失或文件损坏。

**处理策略**：
```java
public void saveTasks(List<Task> tasks, String filename) {
    // 1. 先写入临时文件
    File tempFile = new File(filename + ".tmp");
    try (FileWriter writer = new FileWriter(tempFile)) {
        for (Task task : tasks) {
            writer.write(taskToString(task) + "\n");
        }
        // 2. 成功后再替换原文件
        tempFile.renameTo(new File(filename));
    } catch (IOException e) {
        tempFile.delete();  // 清理临时文件
        throw new TaskSaveException("保存失败: " + e.getMessage(), e);
    }
}
```

### 3. 文件不存在（RPN = 96）

**风险描述**：用户指定的文件不存在，程序崩溃。

**处理策略**：
```java
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
                // 记录错误，继续处理其他行
                System.err.println("第 " + lineNumber + " 行解析失败: " + e.getMessage());
            }
        }
    } catch (FileNotFoundException e) {
        // 文件不存在不是致命错误，返回空列表
        System.out.println("提示：文件 '" + filename + "' 不存在，将创建新文件");
    }

    return loadedTasks;
}
```

### 4. 文件格式错误（RPN = 84）

**风险描述**：文件内容格式不正确，解析失败。

**处理策略**：
- 逐行解析，单条记录失败不影响其他记录
- 记录错误行号和内容，便于用户修复
- 提供格式说明文档

### 5. 非法日期格式（RPN = 72）

**风险描述**：用户输入 "2025-13-45" 等非法日期。

**处理策略**：
```java
public void setDueDate(String dueDate) {
    if (!dueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
        throw new IllegalArgumentException("日期格式无效，应为 YYYY-MM-DD");
    }
    // 进一步验证日期值
    if (!isValidDate(dueDate)) {
        throw new IllegalArgumentException("日期值无效");
    }
    this.dueDate = dueDate;
}
```

---

## 异常类设计

基于 FMEA 分析，定义以下异常类层次结构：

```
CampusFlowException（受检异常 - 基础领域异常）
├── TaskNotFoundException（任务不存在）
├── InvalidTaskDataException（任务数据无效）
└── TaskIOException（任务 IO 错误）
    ├── TaskSaveException（保存失败）
    └── TaskLoadException（加载失败）

RuntimeException
└── TaskNotFoundRuntimeException（编程错误场景）
```

### 异常类代码

```java
/**
 * CampusFlow 基础领域异常
 */
public class CampusFlowException extends Exception {
    public CampusFlowException(String message) {
        super(message);
    }

    public CampusFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * 任务不存在异常
 */
public class TaskNotFoundException extends CampusFlowException {
    public TaskNotFoundException(String taskTitle) {
        super("找不到任务: " + taskTitle);
    }
}

/**
 * 任务数据无效异常
 */
public class InvalidTaskDataException extends CampusFlowException {
    public InvalidTaskDataException(String field, String reason) {
        super("任务数据无效 [" + field + "]: " + reason);
    }
}

/**
 * 任务 IO 异常
 */
public class TaskIOException extends CampusFlowException {
    public TaskIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---

## 防御式编程检查清单

### 输入验证点

- [ ] 所有用户输入点都有验证
- [ ] 所有 setter 方法都有参数验证
- [ ] 构造函数参数都有验证
- [ ] 文件路径在使用前验证
- [ ] 数组/列表索引访问前验证边界

### 异常处理检查

- [ ] 所有受检异常都被处理或声明
- [ ] try-catch 不吞异常（至少记录日志）
- [ ] finally 块正确关闭资源
- [ ] 自定义异常继承层次清晰
- [ ] 异常消息对用户友好

### 代码审查点

- [ ] 没有裸 catch (Exception e) {}
- [ ] 没有空的 catch 块
- [ ] 没有捕获 RuntimeException 后静默处理
- [ ] 资源关闭在 finally 或 try-with-resources 中

---

## 从 FMEA 到代码实现

FMEA 不是文档工作，它直接指导代码实现：

| FMEA 要素 | 代码实现 |
|----------|---------|
| 预防措施 | 防御式编程、输入验证 |
| 处理策略 | try-catch-finally、异常转换 |
| 检测方法 | 日志记录、监控告警 |
| 严重程度 | 决定异常类型（受检 vs 运行时）|

---

## 总结

FMEA 是一种**工程化的风险思维**。它帮助我们在写代码前就想清楚：

1. **哪里可能出错**（故障模式）
2. **出错后怎么办**（处理策略）
3. **怎么防止出错**（预防措施）

**关键原则**：
- 高风险项（RPN > 80）优先处理
- 先用防御式编程预防可预见的错误
- 再用异常处理应对真正的意外
- AI 不会替你考虑这些，这是工程师的核心价值
