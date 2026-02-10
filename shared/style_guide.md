# Java 代码风格指南

## 命名规范

### 类名
- PascalCase（帕斯卡命名法）
- 名词或名词短语

```java
// ✅ 好的命名
public class TaskController { }
public class UserRepository { }
public class DatabaseConfig { }

// ❌ 不好的命名
public class task_controller { }  // 下划线
public class T { }                // 太简短
```

### 方法名
- camelCase（驼峰命名法）
- 动词或动词短语

```java
// ✅ 好的命名
public void saveTask() { }
public User findById(int id) { }
public boolean isValid() { }

// ❌ 不好的命名
public void savetask() { }   // 没有驼峰
public String name() { }      // 不是动词
```

### 变量名
- camelCase
- 有意义的描述

```java
// ✅ 好的命名
int taskCount;
String userName;
List<Task> completedTasks;

// ❌ 不好的命名
int n;           // 无意义
String s;        // 无意义
```

### 常量
- UPPER_SNAKE_CASE（全大写下划线分隔）

```java
public static final int MAX_SIZE = 100;
public static final String DEFAULT_ENCODING = "UTF-8";
```

## 代码格式

### 缩进
- 4 个空格（不要 Tab）

### 大括号
- K&R 风格：左大括号不换行

```java
public class Example {
    public void method() {
        if (condition) {
            // ...
        } else {
            // ...
        }
    }
}
```

### 行长度
- 每行不超过 100 个字符

### 空行
- 方法之间空一行
- 逻辑段落之间空一行

## 注释规范

### Javadoc（类和方法）

```java
/**
 * 表示一个任务实体。
 * 
 * <p>任务有标题、描述、截止日期和完成状态。
 * 支持标记完成和更新信息。
 * 
 * @author 作者名
 * @version 1.0
 */
public class Task {
    
    /**
     * 标记任务为已完成。
     * 
     * <p>如果任务已经是完成状态，则不做任何操作。
     * 
     * @return true 如果状态发生变化，false 如果已经是完成状态
     */
    public boolean markCompleted() {
        // ...
    }
}
```

### 行内注释

```java
// 计算总价（含税费）
double total = price * (1 + TAX_RATE);

// 简单注释可以放在行尾
int count = 0;  // 已处理的任务数
```

## 最佳实践

### 类组织顺序

```java
public class Example {
    // 1. 常量
    public static final int MAX = 100;
    
    // 2. 静态变量
    private static int counter = 0;
    
    // 3. 实例变量
    private String name;
    private int age;
    
    // 4. 构造方法
    public Example(String name) {
        this.name = name;
    }
    
    // 5. 公共方法
    public void doSomething() { }
    
    // 6. 私有方法
    private void helper() { }
}
```

### 避免魔法数字

```java
// ❌ 不好的做法
if (status == 3) { }

// ✅ 好的做法
public static final int STATUS_COMPLETED = 3;
if (status == STATUS_COMPLETED) { }
```

### 尽早返回

```java
// ❌ 嵌套太深
public void process(String input) {
    if (input != null) {
        if (!input.isEmpty()) {
            // 实际逻辑
        }
    }
}

// ✅ 尽早返回
public void process(String input) {
    if (input == null || input.isEmpty()) {
        return;
    }
    // 实际逻辑
}
```

## 教材特定约定

### 代码示例格式

```java
// 文件：Task.java
public class Task {
    private String title;
    private boolean completed;
    
    public Task(String title) {
        this.title = title;
        this.completed = false;
    }
    
    // Getter 和 Setter 省略...
}
```

### 渐进式代码展示

```java
// ===== 版本 1：基础实现 =====
public void save(Task task) {
    // 简单实现
}

// ===== 版本 2：添加异常处理 =====
public void save(Task task) {
    try {
        // 带异常处理的实现
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

// ===== 版本 3：完整实现 =====
public void save(Task task) {
    // 带事务、日志的完整实现
}
```

### 角色对话中的代码

```java
// 小北的代码（有问题）
public class TaskManager {
    public List<Task> tasks = new ArrayList<>();  // ❌ 公开字段
}

// 老潘的改进建议
public class TaskManager {
    private List<Task> tasks = new ArrayList<>();  // ✅ 私有字段
    
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);  // 返回副本
    }
}
```
