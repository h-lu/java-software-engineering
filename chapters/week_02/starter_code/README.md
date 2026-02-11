# Week 02: 类设计与领域建模 - Starter Code

## 项目概述

本周的作业是设计一个**任务管理系统（TaskManager）**，核心目标是学习：

1. **类设计基础** - 如何从问题域识别类（名词提取法）
2. **封装原则** - 为什么使用 private 字段 + public getter/setter
3. **单一职责原则（SRP）** - 避免"上帝类"（God Class）
4. **领域模型** - 实体类 vs 服务类

## 项目结构

```
starter_code/
├── pom.xml                                    # Maven 配置
├── README.md                                  # 本文件
└── src/
    ├── main/java/com/week02/
    │   ├── Task.java                          # 实体类（存储任务数据）
    │   └── TaskManager.java                   # 服务类（管理任务）
    └── test/java/com/week02/
        ├── TaskTest.java                      # Task 类测试
        ├── TaskManagerTest.java               # TaskManager 类测试
        ├── EncapsulationTest.java             # 封装原则测试
        └── SolidPrinciplesTest.java           # SOLID 原则测试
```

## 核心类设计

### Task 类（实体类）

**职责**：存储任务的数据（标题、描述、优先级、完成状态）

**设计原则**：
- 封装：所有字段都是 `private`
- 单一职责：只负责存储数据，不负责验证、持久化、显示
- 不可变性：某些字段（如 ID）可以是 `final`

**方法**：
- `getTitle()`, `setDescription()`, `getPriority()` - Getter
- `setTitle()`, `setPriority()` - Setter（带验证）
- `markCompleted()` - 业务方法

### TaskManager 类（服务类）

**职责**：管理任务的增删改查

**设计原则**：
- 封装：内部的 `tasks` 列表是 `private`
- 单一职责：只负责管理，不负责存储数据本身
- 防御性拷贝：`getAllTasks()` 返回列表的拷贝

**方法**：
- `addTask(Task)` - 添加任务
- `markCompleted(String)` - 标记任务完成
- `getAllTasks()` - 获取所有任务
- `getIncompleteTasks()` - 获取未完成任务
- `getTasksByPriority(int)` - 按优先级获取任务

## 测试说明

### TaskTest.java

测试 Task 类的：
- 对象创建和初始化
- Getter/Setter 功能
- 数据验证（setter 拒绝非法输入）
- 对象独立性（每个对象有独立状态）

### TaskManagerTest.java

测试 TaskManager 类的：
- 任务管理功能（添加、标记完成、过滤）
- 边界情况（空列表、重复任务、不存在的任务）
- 防御式编程（处理 null 输入）

### EncapsulationTest.java

测试封装原则：
- 字段是 `private` 的（外部不能直接访问）
- 数据验证在 setter 中进行
- 内部实现可以变化而不影响外部代码

### SolidPrinciplesTest.java

测试 SOLID 原则：
- 单一职责原则（SRP）- 每个类只有一个职责
- 开闭原则（OCP）- 对扩展开放，对修改关闭
- 如何识别"上帝类"反模式

## 如何运行测试

```bash
# 进入项目目录
cd chapters/week_02/starter_code

# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=TaskTest
mvn test -Dtest=TaskManagerTest
mvn test -Dtest=EncapsulationTest
mvn test -Dtest=SolidPrinciplesTest

# 查看测试报告（可选）
mvn surefire-report:report
```

## 作业验收标准

- [ ] 所有测试通过（`mvn test` 成功）
- [ ] Task 类封装良好（private 字段 + public getter/setter）
- [ ] TaskManager 类职责单一（只管理，不存储数据）
- [ ] 代码有清晰的注释，说明设计决策
- [ ] 理解并能解释"为什么这样设计"

## 本周学习目标检查

完成本周作业后，你应该能够：

- [ ] 用"名词提取法"从需求中识别核心类
- [ ] 区分"实体类"和"服务类"
- [ ] 判断一个类是否违反 SRP
- [ ] 为一个类设计合理的 getter/setter
- [ ] 理解封装的价值（保护数据、控制变化）
- [ ] 理解单一职责原则（SRP）的核心思想

## 扩展练习（可选）

1. **TaskValidator 类**：创建一个验证器类，负责验证任务数据的合法性
2. **TaskPrinter 类**：创建一个打印器类，负责格式化输出任务
3. **TaskRepository 接口**：设计一个持久化接口（Week 07 实现）
4. **改进 Task 类**：添加更多字段（如 dueDate, tags），考虑不可变性

## 常见问题

### Q: 为什么要有 getter/setter？直接用 public 字段不是更简单吗？

A: 封装的好处：
1. **数据验证**：可以在 setter 里验证输入
2. **控制变化**：内部实现可以改变，不影响外部代码
3. **调试**：可以在方法里加日志，追踪修改

### Q: 什么时候用 final 字段？

A: 如果对象创建后字段不应该改变，就用 `final`：
- 不可变对象（Immutable Object）
- 防止意外修改

### Q: 为什么要返回防御性拷贝？

A: 防止外部代码修改内部状态：
```java
// 如果直接返回内部列表：
public List<Task> getAllTasks() { return tasks; } // ❌
// 外部代码可以：manager.getAllTasks().clear();

// 返回拷贝：
public List<Task> getAllTasks() { return new ArrayList<>(tasks); } // ✅
// 外部代码修改不影响 TaskManager
```

### Q: 单一职责原则有什么好处？

A:
1. **易维护**：一个需求变化，只改一个类
2. **易测试**：每个类职责单一，测试简单
3. **可重用**：职责单一的类更容易在其他地方使用

## 参考资料

- CHAPTER.md - 本章核心内容
- ASSIGNMENT.md - 作业要求
- [Effective Java (3rd Edition)](https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/)
- [Clean Code by Robert C. Martin](https://www.oreilly.com/library/view/clean-code-a/9780136083238/)
