# Week 03 评分标准

## 评分维度概览

| 维度 | 占比 | 说明 |
|------|------|------|
| **功能正确性** | 35% | 异常是否正确捕获/抛出，输入验证是否完整 |
| **代码质量** | 25% | 防御式编程实践，异常处理策略合理性 |
| **测试覆盖** | 20% | 边界情况处理，异常场景测试 |
| **文档完整** | 20% | FMEA 分析质量，异常处理策略文档 |

---

## 详细评分标准

### 一、功能正确性（35 分）

#### 1.1 异常处理正确性（15 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| try-catch 使用正确 | 5 分 | 正确捕获特定异常，不是笼统的 Exception |
| finally 或 try-with-resources | 4 分 | 资源正确关闭，无资源泄漏 |
| 异常信息清晰 | 3 分 | 错误信息具体，帮助定位问题 |
| 异常传播合理 | 3 分 | 不吞异常，不过度捕获 |

**示例**（好的异常处理）：
```java
// ✅ 好的做法
try (Scanner scanner = new Scanner(file)) {
    while (scanner.hasNextLine()) {
        process(scanner.nextLine());
    }
} catch (FileNotFoundException e) {
    System.err.println("文件不存在: " + filename);
    return Collections.emptyList();
} catch (IOException e) {
    throw new TaskFileException("读取文件失败: " + filename, e);
}
```

**示例**（不好的异常处理）：
```java
// ❌ 不好的做法
try {
    Scanner scanner = new Scanner(file);
    // 处理...
} catch (Exception e) {  // 捕获太宽泛
    // 什么都不做——吞异常！
}
```

**评分细则**：
- **13-15 分**：异常处理完善，资源管理正确，无资源泄漏
- **10-12 分**：基本正确，个别地方可以改进
- **6-9 分**：有异常处理，但存在问题（吞异常、资源未关闭）
- **0-5 分**：异常处理不正确或缺失

---

#### 1.2 自定义异常设计（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 继承层次合理 | 4 分 | 有基础异常类，具体异常继承合理 |
| 异常类型选择正确 | 3 分 | 受检异常 vs 运行时异常选择合理 |
| 构造方法完整 | 3 分 | 提供消息和 cause 的构造方法 |

**示例**（好的自定义异常）：
```java
// ✅ 好的设计
public class CampusFlowException extends Exception {
    public CampusFlowException(String message) {
        super(message);
    }

    public CampusFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class TaskNotFoundException extends CampusFlowException {
    public TaskNotFoundException(String title) {
        super("找不到任务: " + title);
    }
}
```

**评分细则**：
- **9-10 分**：异常层次清晰，类型选择合理，构造方法完整
- **7-8 分**：基本合理，个别地方可以改进
- **4-6 分**：异常设计有问题（如全部用运行时异常）
- **0-3 分**：自定义异常缺失或不合理

---

#### 1.3 输入验证完整性（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 空值检查 | 3 分 | 对 null 参数有检查 |
| 格式验证 | 3 分 | 日期、优先级等格式验证 |
| 边界检查 | 2 分 | 长度、范围等边界验证 |
| 错误信息清晰 | 2 分 | 验证失败时给出具体错误信息 |

**示例**（好的输入验证）：
```java
// ✅ 好的验证
public void setTitle(String title) {
    if (title == null) {
        throw new InvalidTaskDataException("title", "不能为 null");
    }
    if (title.trim().isEmpty()) {
        throw new InvalidTaskDataException("title", "不能为空");
    }
    if (title.length() > 100) {
        throw new InvalidTaskDataException("title", "不能超过100字符");
    }
    this.title = title.trim();
}
```

**评分细则**：
- **9-10 分**：验证完整，覆盖 null、空值、格式、边界
- **7-8 分**：基本验证齐全，个别边界情况遗漏
- **4-6 分**：有验证，但不完整（如只检查 null）
- **0-3 分**：缺少输入验证

---

### 二、代码质量（25 分）

#### 2.1 防御式编程实践（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 尽早失败 | 4 分 | 在方法入口验证参数，不让错误扩散 |
| 白名单思维 | 3 分 | 定义"什么是合法的"而非"什么是非法的" |
| 断言使用 | 3 分 | 对"不应该发生"的情况使用 assert |

**示例**（好的防御式编程）：
```java
// ✅ 尽早失败
public void addTask(Task task) {
    if (task == null) {
        throw new IllegalArgumentException("task 不能为 null");
    }
    if (findTask(task.getTitle()) != null) {
        throw new DuplicateTaskException(task.getTitle());
    }
    tasks.add(task);
}

// ✅ 断言记录假设
public void completeTask(String title) {
    Task task = findTask(title);
    assert task != null : "任务应该存在";
    task.markCompleted();
}
```

**评分细则**：
- **9-10 分**：防御式编程应用到位，参数验证、断言使用合理
- **7-8 分**：基本应用，个别地方可以改进
- **4-6 分**：有防御式编程意识，但应用不完整
- **0-3 分**：缺少防御式编程实践

---

#### 2.2 异常处理策略（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 受检 vs 运行时选择 | 4 分 | 根据场景正确选择异常类型 |
| 异常转换 | 3 分 | 底层异常正确转换为领域异常 |
| 不滥用异常 | 3 分 | 异常用于异常情况，不用于流程控制 |

**示例**（好的策略）：
```java
// ✅ 受检异常：调用者可以恢复
try {
    List<Task> tasks = repository.loadFromFile(filename);
} catch (TaskFileException e) {
    // 提示用户选择其他文件
}

// ✅ 运行时异常：编程错误
public void setTitle(String title) {
    if (title == null) {
        throw new IllegalArgumentException("title 不能为 null");
    }
}

// ✅ 异常转换
try {
    // IO 操作
} catch (IOException e) {
    throw new TaskFileException("读取失败", e);  // 保留 cause
}
```

**评分细则**：
- **9-10 分**：异常策略清晰，类型选择合理，转换正确
- **7-8 分**：基本合理，个别选择可以讨论
- **4-6 分**：策略有问题（如全部用运行时异常）
- **0-3 分**：没有明确的异常策略

---

#### 2.3 代码风格（5 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 命名规范 | 2 分 | 异常类名以 Exception 结尾，清晰表达异常类型 |
| 代码格式 | 2 分 | try-catch-finally 格式规范 |
| 注释 | 1 分 | 复杂异常处理有注释说明 |

**评分细则**：
- **5 分**：风格完全符合规范
- **4 分**：基本符合，小问题
- **2-3 分**：有风格问题
- **0-1 分**：风格混乱

---

### 三、测试覆盖（20 分）

#### 3.1 异常场景测试（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 正常异常测试 | 4 分 | 测试异常是否正确抛出 |
| 边界情况测试 | 3 分 | 测试 null、空值、极值等边界 |
| 异常信息验证 | 3 分 | 验证异常信息是否正确 |

**示例**（好的异常测试）：
```java
@Test
public void testSetNullTitle() {
    Task task = new Task();
    InvalidTaskDataException exception = assertThrows(
        InvalidTaskDataException.class,
        () -> task.setTitle(null)
    );
    assertEquals("任务数据无效 [title]: 不能为 null", exception.getMessage());
}

@Test
public void testLoadNonExistentFile() {
    TaskFileLoader loader = new TaskFileLoader();
    List<String> tasks = loader.loadTasksFromFile("not_exist.txt");
    assertTrue(tasks.isEmpty());
}
```

**评分细则**：
- **9-10 分**：异常测试覆盖全面，包括边界和信息验证
- **7-8 分**：有基本异常测试，边界情况可以补充
- **4-6 分**：测试很少，只覆盖正常异常
- **0-3 分**：缺少异常测试

---

#### 3.2 测试质量（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 断言合理 | 4 分 | 使用 assertThrows 验证异常类型 |
| 测试命名 | 3 分 | 方法名清晰描述测试场景 |
| 测试独立 | 3 分 | 每个测试独立，不依赖其他测试 |

**示例**（不好的测试）：
```java
// ❌ 没有断言异常类型
@Test
public void testInvalidTitle() {
    Task task = new Task();
    task.setTitle("");  // 会抛出异常，但没有断言
}

// ❌ 命名不清晰
@Test
public void test1() {  // test1 是什么？
    // ...
}
```

**评分细则**：
- **9-10 分**：测试质量高，断言合理，命名清晰
- **7-8 分**：基本合理，个别可以改进
- **4-6 分**：测试质量一般
- **0-3 分**：测试质量差

---

### 四、文档完整性（20 分）

#### 4.1 FMEA 分析质量（12 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 故障点识别 | 4 分 | 至少 5 个故障点，覆盖主要功能模块 |
| RPN 评估 | 3 分 | 严重程度/发生频率/检测难度评估合理 |
| 预防/处理策略 | 3 分 | 每个故障点都有对应的代码实现策略 |
| 与代码对应 | 2 分 | FMEA 中的策略在代码中有体现 |

**示例**（好的 FMEA）：
```markdown
| 功能模块 | 潜在故障 | 影响 | S | O | D | RPN | 预防措施 | 处理策略 |
|---------|---------|------|---|---|---|-----|---------|---------|
| 任务创建 | 空标题 | 数据无效 | 5 | 3 | 2 | 30 | setter 验证 | 抛出 InvalidTaskDataException |
| 文件加载 | 文件不存在 | 程序崩溃 | 8 | 6 | 2 | 96 | try-catch | 捕获异常，返回空列表 |
```

**评分细则**：
- **11-12 分**：FMEA 分析全面，RPN 评估合理，与代码对应
- **8-10 分**：基本完整，个别地方可以改进
- **5-7 分**：FMEA 流于形式，缺少量化评估
- **0-4 分**：FMEA 缺失或质量差

---

#### 4.2 异常处理策略文档（8 分，挑战题）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 分类原则清晰 | 3 分 | 明确说明何时用受检/运行时异常 |
| 转换规则 | 2 分 | 说明底层异常如何转换 |
| 恢复策略 | 2 分 | 不同异常的恢复方式 |
| 与领域模型结合 | 1 分 | 异常处理如何融入领域模型 |

**评分细则**：
- **7-8 分**：策略文档清晰完整，有设计决策说明
- **5-6 分**：基本完整，个别地方可以补充
- **3-4 分**：文档简单，缺少深度
- **0-2 分**：文档缺失或质量差

---

## 进阶题与挑战题（额外加分）

### 进阶题（+20 分）

| 任务 | 分数 | 评分标准 |
|------|------|---------|
| 任务 5：完整异常处理层 | 10 分 | 异常转换正确，统一处理逻辑，错误恢复策略合理 |
| 任务 6：try-with-resources | 10 分 | 资源管理正确，多资源处理，异常处理完整 |

**评分细则**：
- **9-10 分**：完全实现，设计合理
- **7-8 分**：基本实现，部分功能可以改进
- **4-6 分**：实现不完整或有明显问题
- **0-3 分**：尝试了但未能完成

---

### 挑战题（+20 分）

| 任务 | 分数 | 评分标准 |
|------|------|---------|
| 任务 7：异常处理策略设计 | 20 分 | 策略清晰，与代码一致，有深度思考 |

**评分细则**：
- **18-20 分**：策略文档全面，设计决策清晰，与代码实现一致
- **15-17 分**：策略文档完整，有设计思考
- **10-14 分**：有策略文档，但深度不足
- **5-9 分**：尝试了但质量不高
- **0-4 分**：未完成

---

## 总分计算

### 基础分（100 分）
- 功能正确性：35 分
- 代码质量：25 分
- 测试覆盖：20 分
- 文档完整性：20 分

### 额外加分（最多 +40 分）
- 进阶题：+20 分
- 挑战题：+20 分

### 最终等级
- **A（90-100 分）**：基础分 90+，或基础分 80+ + 至少一个进阶题
- **B（80-89 分）**：基础分 80+
- **C（70-79 分）**：基础分 70+
- **D（60-69 分）**：基础分 60+
- **F（<60 分）**：基础分 <60

---

## 常见扣分项

| 问题 | 扣分 | 说明 |
|------|------|------|
| 吞异常 | -15 分 | catch 块什么都不做 |
| 资源泄漏 | -10 分 | 文件/连接未关闭 |
| 捕获过于宽泛 | -8 分 | 捕获 Exception 或 Throwable |
| 缺少输入验证 | -10 分 | setter 没有参数检查 |
| FMEA 流于形式 | -8 分 | 没有量化评估或与代码不对应 |
| 自定义异常不合理 | -8 分 | 异常层次混乱或类型选择错误 |
| 测试覆盖不足 | -5 分 | 只有正常情况测试 |
| 命名不规范 | -3 分 | 异常类命名不符合规范 |

---

## 快速自查清单

提交前请检查：

### 异常处理
- [ ] 使用 try-catch-finally 或 try-with-resources 处理文件操作
- [ ] 资源在任何情况下都被正确关闭
- [ ] 不捕获过于宽泛的异常（Exception/Throwable）
- [ ] 不吞异常（catch 块至少打印错误信息）
- [ ] 自定义异常继承层次合理

### 输入验证
- [ ] 所有 setter 都有参数验证
- [ ] 验证 null、空值、格式、边界
- [ ] 验证失败时抛出具体异常
- [ ] 错误信息清晰，指出字段和问题

### FMEA
- [ ] 至少 5 个故障点
- [ ] 有 RPN 评估
- [ ] 每个故障点都有代码实现
- [ ] 按 RPN 排序，优先处理高风险

### 测试
- [ ] 测试异常是否正确抛出
- [ ] 测试边界情况（null、空值、极值）
- [ ] 使用 assertThrows 验证异常类型
- [ ] 测试命名清晰

### 提交
- [ ] 代码在 `src/main/java/edu/campusflow/` 目录下
- [ ] FMEA 在 `docs/fmea.md`
- [ ] 异常类在 `exception` 包中
- [ ] Git commit message 清晰
