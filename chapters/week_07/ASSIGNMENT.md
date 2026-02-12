# Week 07 作业：让数据活下来——JDBC 持久化实践

> "内存是易失的，磁盘是永恒的。"
> —— 数据库工程师的谚语

---

## 作业概述

本周作业将带领你把 Week 05-06 的内存 Repository 迁移到 JDBC + SQLite 实现。你的 CampusFlow 数据将不再"随风而逝"，程序重启后依然完好无损。

| 项目 | 说明 |
|------|------|
| 预估耗时 | 6-8 小时（基础题 3-4 小时，进阶/挑战 3-4 小时） |
| 截止日期 | 本周日 23:59 |
| 提交方式 | Git 仓库链接 + 测试运行截图 |

---

## 学习目标

完成本周作业后，你将能够：

1. 配置 JDBC 连接 SQLite 数据库，理解 JDBC URL 和驱动概念
2. 使用 `try-with-resources` 正确管理数据库连接资源
3. 使用 `PreparedStatement` 实现安全的 CRUD 操作（防止 SQL 注入）
4. 设计数据库 schema 并编写初始化脚本
5. 使用 H2 内存数据库编写 Repository 单元测试
6. 将内存 Repository 迁移到 JDBC 实现

---

## 基础题（必做）

### 任务 1：JDBC 连接与数据库初始化（基础）

**场景**：小北第一次接触 JDBC，他需要先学会建立数据库连接，并创建表结构。

**任务**：

完成以下 JDBC 基础组件的实现：

```java
// DatabaseConfig.java - 数据库配置类
public class DatabaseConfig {
    // TODO 1: 定义数据库 URL 常量（jdbc:sqlite:campusflow.db）

    // TODO 2: 实现获取连接的方法，使用 try-with-resources 的调用方负责关闭
    public static Connection getConnection() throws SQLException {
        // 你的代码
    }
}
```

```java
// DatabaseInitializer.java - 数据库初始化类
public class DatabaseInitializer {
    private final String url;

    public DatabaseInitializer(String url) {
        this.url = url;
    }

    // TODO 3: 实现初始化方法，执行 schema.sql 创建表
    public void initialize() {
        // 你的代码：读取 schema.sql 并执行
    }
}
```

```sql
-- schema.sql - 任务表结构
-- TODO 4: 编写创建 tasks 表的 SQL
-- 字段：id (TEXT PRIMARY KEY), title (TEXT NOT NULL),
--       description (TEXT), status (TEXT), created_at (TEXT)
```

**要求**：

1. **DatabaseConfig**：
   - 定义常量 `DB_URL = "jdbc:sqlite:campusflow.db"`
   - `getConnection()` 返回 `DriverManager.getConnection(DB_URL)`
   - 处理 `ClassNotFoundException`（如果驱动未加载）

2. **DatabaseInitializer**：
   - 使用 `try-with-resources` 管理 Connection 和 Statement
   - 从 classpath 读取 `schema.sql` 文件
   - 支持按分号分割多条 SQL 语句执行

3. **schema.sql**：
   - 使用 `CREATE TABLE IF NOT EXISTS` 避免重复创建报错
   - 为 `status` 字段添加 `CHECK` 约束（'pending', 'in_progress', 'done'）
   - 添加合适的索引优化查询

**输入/输出示例**：

```java
// 测试连接
Connection conn = DatabaseConfig.getConnection();
System.out.println("连接成功！");
conn.close();

// 初始化数据库
DatabaseInitializer initializer = new DatabaseInitializer("jdbc:sqlite:campusflow.db");
initializer.initialize();
// 输出：数据库初始化成功
// 目录下生成 campusflow.db 文件
```

**边界情况处理**：

```java
// schema.sql 不存在
initializer.initialize();
// 抛出：RuntimeException: 读取 schema.sql 失败

// SQL 语法错误
// 抛出：RuntimeException: 执行 SQL 失败: [具体错误信息]
```

**提示**：
- 使用 `getClass().getClassLoader().getResourceAsStream()` 读取 classpath 资源
- SQL 文件中的注释行（-- 开头）需要跳过
- 使用 `String.split(";")` 分割多条语句

---

### 任务 2：JdbcTaskRepository 实现（核心）

**场景**：现在需要把 Week 05 的内存版 `TaskRepository` 改造成 JDBC 版。

**任务**：

实现完整的 JDBC 版 Repository：

```java
// JdbcTaskRepository.java
public class JdbcTaskRepository implements TaskRepository {
    private final String url;

    public JdbcTaskRepository(String url) {
        this.url = url;
    }

    // TODO 1: 保存任务（插入或更新）
    @Override
    public void save(Task task) {
        // 防御式编程：参数校验
        // 使用 PreparedStatement 执行 INSERT ... ON CONFLICT UPDATE
    }

    // TODO 2: 根据 ID 查询任务
    @Override
    public Optional<Task> findById(String id) {
        // 使用 PreparedStatement 执行 SELECT
        // 使用 ResultSet 映射到 Task 对象
    }

    // TODO 3: 查询所有任务
    @Override
    public List<Task> findAll() {
        // 使用 SELECT * FROM tasks ORDER BY created_at DESC
        // 返回 List<Task>
    }

    // TODO 4: 删除任务
    @Override
    public void delete(String id) {
        // 使用 PreparedStatement 执行 DELETE
    }

    // TODO 5: 根据状态查询任务
    public List<Task> findByStatus(String status) {
        // 带参数的 SELECT 查询
    }

    // 辅助方法：将 ResultSet 映射为 Task
    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        // 你的代码
    }
}
```

**要求**：

1. **save**：
   - 检查 `task` 和 `task.getId()` 不为 null
   - 使用 `INSERT INTO ... ON CONFLICT(id) DO UPDATE ...` 实现"存在则更新，不存在则插入"
   - 使用 `PreparedStatement` 设置参数，禁止字符串拼接 SQL

2. **findById**：
   - 检查 `id` 不为 null 或空
   - 使用 `PreparedStatement` 防止 SQL 注入
   - 返回 `Optional<Task>`，找不到返回 `Optional.empty()`

3. **findAll**：
   - 按 `created_at` 降序排列
   - 返回 `ArrayList<Task>`

4. **delete**：
   - 检查 `id` 不为 null 或空
   - 使用 `PreparedStatement` 执行删除

5. **findByStatus**：
   - 带参数查询，只返回指定状态的任务
   - 使用 `PreparedStatement` 设置状态参数

**输入/输出示例**：

```java
// 创建 Repository
TaskRepository repo = new JdbcTaskRepository("jdbc:sqlite:campusflow.db");

// 保存任务
Task task = new Task("task-001", "完成 Week 07 作业", "实现 JDBC Repository", "pending");
repo.save(task);
// 成功，无输出

// 查询任务
Optional<Task> found = repo.findById("task-001");
found.ifPresent(t -> System.out.println(t.getTitle()));
// 输出：完成 Week 07 作业

// 查询所有任务
List<Task> all = repo.findAll();
// 输出：[完成 Week 07 作业, 买牛奶, ...]（按创建时间倒序）

// 更新任务
task.setStatus("done");
repo.save(task);
// 数据库中任务状态更新为 done

// 删除任务
repo.delete("task-001");
// 任务从数据库中删除
```

**边界情况处理**：

```java
// 保存 null 任务
repo.save(null);
// 抛出：IllegalArgumentException: Task 不能为 null

// 查询不存在的任务
Optional<Task> notFound = repo.findById("不存在的ID");
// 返回：Optional.empty()

// 使用字符串拼接 SQL（错误示范）
// ❌ 永远不要这样做：
// String sql = "SELECT * FROM tasks WHERE id = '" + id + "'";
// 这会导致 SQL 注入漏洞！
```

**提示**：
- 使用 `try (Connection conn = ...; PreparedStatement pstmt = ...)` 自动关闭资源
- `ResultSet` 也需要在 try-with-resources 中管理
- 使用 `rs.getString("column_name")` 获取字段值
- SQLite 的布尔值用 INTEGER 存储（0/1），使用 `rs.getBoolean()` 自动转换

---

### 任务 3：Repository 单元测试（使用 H2）

**场景**：小北想知道如何测试数据库访问代码，但不想污染真实数据库。

**任务**：

使用 H2 内存数据库编写 `JdbcTaskRepository` 的单元测试：

```java
// JdbcTaskRepositoryTest.java
public class JdbcTaskRepositoryTest {

    private JdbcTaskRepository repository;
    private static final String TEST_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    @BeforeEach
    void setUp() {
        // TODO 1: 每个测试前初始化 H2 数据库和 Repository
    }

    @Test
    void shouldSaveAndFindTask() {
        // TODO 2: 测试保存和查询
    }

    @Test
    void shouldReturnEmptyWhenTaskNotFound() {
        // TODO 3: 测试查询不存在的任务
    }

    @Test
    void shouldUpdateExistingTask() {
        // TODO 4: 测试更新已存在的任务
    }

    @Test
    void shouldDeleteTask() {
        // TODO 5: 测试删除任务
    }

    @Test
    void shouldFindByStatus() {
        // TODO 6: 测试按状态查询
    }

    @Test
    void shouldThrowExceptionWhenSavingNullTask() {
        // TODO 7: 测试保存 null 任务时抛出异常
    }

    @Test
    void shouldFindAllInDescendingOrder() {
        // TODO 8: 测试查询结果按创建时间倒序排列
    }
}
```

**要求**：

1. **setUp**：
   - 使用 `@BeforeEach` 确保每个测试前数据库状态干净
   - 初始化 H2 内存数据库（URL: `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1`）
   - 执行 schema.sql 创建表结构
   - 创建 `JdbcTaskRepository` 实例

2. **测试覆盖**：
   - 正常 CRUD 操作
   - 查询不存在的记录
   - 更新已存在的记录
   - 按状态筛选
   - 边界情况（null 参数、空字符串）
   - 异常场景

3. **测试隔离**：
   - 每个测试使用独立的数据库状态
   - 测试之间不相互影响

**pom.xml 依赖**：

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.224</version>
    <scope>test</scope>
</dependency>
```

**输入/输出示例**：

```java
@Test
void shouldSaveAndFindTask() {
    // 准备
    Task task = new Task("T1", "测试任务", "描述", "pending");

    // 执行
    repository.save(task);
    Optional<Task> found = repository.findById("T1");

    // 验证
    assertTrue(found.isPresent());
    assertEquals("测试任务", found.get().getTitle());
    assertEquals("pending", found.get().getStatus());
}
```

**提示**：
- H2 兼容大部分标准 SQL，但 SQLite 有一些特有语法（如 `ON CONFLICT`）在 H2 中可能不同
- 测试 schema 可以简化，不需要完全与生产环境一致
- 使用 `assertThrows` 验证异常类型和消息

---

## 进阶题（选做）

### 任务 4：数据迁移工具

**场景**：阿码问："如果我已经有很多内存中的数据，怎么迁移到数据库？"

**任务**：

编写一个数据迁移工具，将内存 Repository 的数据迁移到 JDBC Repository：

```java
// DataMigrationTool.java
public class DataMigrationTool {

    // TODO: 实现从内存到数据库的迁移
    public static void migrate(InMemoryTaskRepository source, JdbcTaskRepository target) {
        // 1. 从 source 读取所有任务
        // 2. 逐个保存到 target
        // 3. 记录迁移日志（成功数、失败数）
    }
}
```

**要求**：
- 处理迁移过程中的异常（某个任务失败不中断整个迁移）
- 记录迁移统计信息（总任务数、成功数、失败数）
- 支持事务：要么全部成功，要么全部回滚

---

### 任务 5：连接池调研

**场景**：老潘说："生产环境都用连接池，你知道为什么吗？"

**任务**：

1. 调研 HikariCP 连接池的基本用法
2. 编写对比测试：比较 `DriverManager` 和 HikariCP 的性能差异
3. 撰写简要报告（500 字以内），说明：
   - 连接池的核心优势
   - 什么场景下必须使用连接池
   - CampusFlow 目前是否需要连接池

---

## 挑战题（可选）

### 任务 6：多表关联与 JOIN 查询

**场景**：CampusFlow 需要支持用户和任务的关联——每个任务属于一个用户。

**任务**：

1. 设计 `users` 表和 `tasks` 表的一对多关系
2. 实现 `UserRepository` 和关联查询
3. 使用 JOIN 查询获取用户及其所有任务

```java
// 查询示例：获取用户及其所有任务
public User findUserWithTasks(String userId) {
    String sql = """
        SELECT u.*, t.id as task_id, t.title as task_title
        FROM users u
        LEFT JOIN tasks t ON u.id = t.user_id
        WHERE u.id = ?
        """;
    // 实现代码...
}
```

---

### 任务 7：架构决策记录（ADR）

**场景**：老潘要求记录数据持久化方案的决策过程。

**任务**：

编写 ADR-002：数据持久化方案决策，说明：

1. 为什么选择 SQLite（而不是 MySQL/PostgreSQL/H2）
2. 为什么使用 JDBC（而不是 JPA/MyBatis）
3. 数据迁移策略
4. 未来可能的技术演进路径

---

## AI 协作练习（可选）

根据 AI 融合路径，Week 07 属于"协作期"，你可以使用 AI 辅助完成以下任务，并提交审查报告。

### 练习：AI 辅助 SQL 生成与审查

下面这段 SQL 是某个 AI 工具为 CampusFlow 生成的 schema：

```sql
CREATE TABLE tasks (
    id TEXT,
    title VARCHAR(255),
    description TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP
);

CREATE INDEX idx_status ON tasks(status);
```

**请审查这段 SQL**：

- [ ] 主键定义正确吗？
- [ ] 字段类型选择合理吗？（SQLite 有 VARCHAR 吗？）
- [ ] 有 NOT NULL 约束吗？
- [ ] 有 CHECK 约束验证状态值吗？
- [ ] 索引创建有必要吗？
- [ ] 如果 title 很长，VARCHAR(255) 会截断吗？

**提交**：修复后的 schema.sql + 你发现了哪些问题的简短说明。

---

## 提交要求

### 必交内容（基础题）

1. **源代码**：
   - `src/main/java/edu/campusflow/config/DatabaseConfig.java`（任务 1）
   - `src/main/java/edu/campusflow/config/DatabaseInitializer.java`（任务 1）
   - `src/main/resources/schema.sql`（任务 1）
   - `src/main/java/edu/campusflow/repository/JdbcTaskRepository.java`（任务 2）
   - `src/main/java/edu/campusflow/repository/TaskRepository.java`（接口）

2. **测试代码**：
   - `src/test/java/edu/campusflow/repository/JdbcTaskRepositoryTest.java`（任务 3）

3. **测试运行截图**：显示所有测试通过

4. **README.md**：说明如何运行项目

### 选交内容（进阶/挑战）

1. **进阶题代码**：
   - `src/main/java/edu/campusflow/migration/DataMigrationTool.java`（任务 4）
   - `docs/research/connection-pool-report.md`（任务 5）

2. **挑战题**：
   - `src/main/java/edu/campusflow/repository/UserRepository.java`（任务 6）
   - `docs/adr/ADR-002-persistence.md`（任务 7）

3. **AI 协作练习**（如完成）：
   - `docs/ai-review-sql.md`

### 提交格式

```bash
# Git 提交
git add src/main/java/edu/campusflow/config/
git add src/main/java/edu/campusflow/repository/
git add src/main/resources/
git add src/test/java/edu/campusflow/repository/
git commit -m "feat: 完成 Week 07 基础题 - JDBC 持久化"

# 如果做了进阶题
git add src/main/java/edu/campusflow/migration/
git commit -m "feat: 完成 Week 07 进阶题 - 数据迁移工具"

# 如果做了挑战题
git add docs/adr/
git commit -m "docs: 添加 ADR-002 数据持久化方案决策"
```

---

## 常见错误与避坑指南

### 错误 1：忘记关闭数据库连接

**问题**：连接泄漏导致"too many connections"错误

```java
// ❌ 不好的做法
Connection conn = DriverManager.getConnection(url);
// 使用连接...
// 如果这里抛出异常，连接就不会关闭
conn.close();

// ✅ 好的做法：try-with-resources
try (Connection conn = DriverManager.getConnection(url)) {
    // 使用连接...
} // 自动关闭，即使抛出异常
```

---

### 错误 2：字符串拼接 SQL（SQL 注入）

**问题**：用户输入可能破坏 SQL 结构，导致数据泄露或丢失

```java
// ❌ 不好的做法：字符串拼接
String sql = "SELECT * FROM tasks WHERE id = '" + id + "'";
// 如果 id = "' OR '1'='1"，会返回所有记录！

// ✅ 好的做法：使用 PreparedStatement
String sql = "SELECT * FROM tasks WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, id);  // 自动转义特殊字符
```

---

### 错误 3：忽略 SQLException

**问题**：直接打印或吞掉异常，调用方无法知道操作失败

```java
// ❌ 不好的做法
try {
    pstmt.executeUpdate();
} catch (SQLException e) {
    e.printStackTrace();  // 只是打印，调用方以为成功了
}

// ✅ 好的做法：转换为运行时异常抛出
try {
    pstmt.executeUpdate();
} catch (SQLException e) {
    throw new RuntimeException("保存任务失败: " + e.getMessage(), e);
}
```

---

### 错误 4：ResultSet 没有关闭

**问题**：ResultSet 也占用数据库资源，需要及时释放

```java
// ❌ 不好的做法
ResultSet rs = pstmt.executeQuery();
// 处理结果...
// 忘记关闭 rs

// ✅ 好的做法：try-with-resources 嵌套
try (Connection conn = ...;
     PreparedStatement pstmt = ...;
     ResultSet rs = pstmt.executeQuery()) {
    while (rs.next()) {
        // 处理结果...
    }
} // 全部自动关闭
```

---

### 错误 5：没有参数校验

**问题**：null 参数导致难以调试的数据库错误

```java
// ❌ 不好的做法
public void save(Task task) {
    String sql = "INSERT INTO tasks (id, title) VALUES (?, ?)";
    // 如果 task 为 null，task.getId() 会抛出 NullPointerException
}

// ✅ 好的做法：防御式编程
public void save(Task task) {
    if (task == null) {
        throw new IllegalArgumentException("Task 不能为 null");
    }
    if (task.getId() == null || task.getId().isBlank()) {
        throw new IllegalArgumentException("Task ID 不能为空");
    }
    // 继续执行...
}
```

---

### 错误 6：提交数据库文件到 Git

**问题**：数据库文件通常很大且经常变化，不应该提交到版本控制

```gitignore
# .gitignore
*.db
*.sqlite
*.sqlite3

# 但 schema 和迁移脚本要提交
!schema.sql
!data.sql
```

---

## 评分标准

详细评分标准见 `RUBRIC.md`。

简要说明：
- **基础题**：60 分 - 完成任务 1-3，JDBC 连接正确，CRUD 实现完整，测试覆盖 70%+
- **进阶题**：+20 分 - 完成任务 4 或 5，数据迁移或连接池调研有深度
- **挑战题**：+20 分 - 完成任务 6 或 7，多表关联或 ADR 有深度

---

## 学习资源

- **本周章节**：`chapters/week_07/CHAPTER.md`
- **起步代码**：`starter_code/src/main/java/`（如果你遇到困难）
- **风格指南**：`shared/style_guide.md`
- **SQLite 官方文档**：https://www.sqlite.org/lang.html
- **JDBC 官方教程**：https://docs.oracle.com/javase/tutorial/jdbc/

---

## 起步代码使用说明

如果你遇到困难，可以参考 `starter_code/` 目录中的起步代码：

```bash
# 起步代码位置
starter_code/
├── src/main/java/edu/campusflow/config/        # 数据库配置
├── src/main/java/edu/campusflow/repository/    # Repository 层
└── src/main/resources/                         # schema.sql
```

**使用建议**：
1. 先自己尝试完成任务
2. 遇到困难时参考起步代码的**思路**，不要直接复制
3. 起步代码可能故意留有一些问题，你需要发现并修复

---

## 截止日期

**提交截止**：本周日 23:59

**延迟提交**：每晚一天扣 10% 分数，最多延迟 3 天
