# Week 07 评分标准

## 评分维度概览

| 维度 | 占比 | 说明 |
|------|------|------|
| **功能完整性** | 30% | JDBC 连接、CRUD 实现、资源管理 |
| **代码质量** | 25% | try-with-resources、PreparedStatement、异常处理 |
| **测试覆盖** | 25% | H2 测试、边界情况、异常测试 |
| **工程规范** | 20% | 代码风格、注释、提交信息 |

---

## 详细评分标准

### 一、功能完整性（30 分）

#### 1.1 JDBC 连接与配置（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 驱动配置正确 | 3 分 | pom.xml 中正确添加 sqlite-jdbc 依赖 |
| 连接获取正确 | 4 分 | 使用 DriverManager.getConnection()，URL 格式正确 |
| 初始化脚本执行 | 3 分 | DatabaseInitializer 能正确读取并执行 schema.sql |

**示例**（好的 JDBC 配置）：
```java
// ✅ 好的做法：配置集中管理
public class DatabaseConfig {
    public static final String DB_URL = "jdbc:sqlite:campusflow.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
```

**示例**（不好的 JDBC 配置）：
```java
// ❌ 不好的做法：URL 硬编码在各处
Connection conn = DriverManager.getConnection("jdbc:sqlite:mydb.db");
```

**评分细则**：
- **9-10 分**：JDBC 配置完整，驱动正确，URL 规范
- **7-8 分**：基本正确，个别配置可以改进
- **4-6 分**：能连接数据库，但配置有问题
- **0-3 分**：无法连接数据库或配置缺失

---

#### 1.2 CRUD 实现完整性（12 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| CREATE (save) | 3 分 | 正确实现 INSERT，支持插入或更新 |
| READ (findById/findAll) | 3 分 | 正确实现 SELECT，能映射 ResultSet |
| UPDATE (save) | 3 分 | 正确实现 UPDATE 或 ON CONFLICT UPDATE |
| DELETE (delete) | 3 分 | 正确实现 DELETE |

**示例**（好的 CRUD 实现）：
```java
// ✅ 好的做法：使用 PreparedStatement，参数化查询
public void save(Task task) {
    String sql = """
        INSERT INTO tasks (id, title, description, status, created_at)
        VALUES (?, ?, ?, ?, ?)
        ON CONFLICT(id) DO UPDATE SET
            title = excluded.title,
            description = excluded.description,
            status = excluded.status
        """;

    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, task.getId());
        pstmt.setString(2, task.getTitle());
        // ...
        pstmt.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException("保存任务失败: " + e.getMessage(), e);
    }
}
```

**示例**（不好的 CRUD 实现）：
```java
// ❌ 不好的做法：字符串拼接 SQL
public void save(Task task) {
    String sql = "INSERT INTO tasks VALUES ('" + task.getId() + "', '" + task.getTitle() + "')";
    // SQL 注入风险！
}
```

**评分细则**：
- **11-12 分**：CRUD 完整实现，使用 PreparedStatement
- **8-10 分**：基本实现，个别操作可以改进
- **5-7 分**：实现不完整或有明显问题
- **0-4 分**：CRUD 实现缺失或错误

---

#### 1.3 资源管理（8 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| try-with-resources 使用 | 4 分 | Connection、Statement、ResultSet 都使用 try-with-resources |
| 资源关闭顺序 | 2 分 | 按 ResultSet -> Statement -> Connection 顺序关闭 |
| 异常时资源释放 | 2 分 | 异常情况下资源也能正确释放 |

**示例**（好的资源管理）：
```java
// ✅ 好的做法：多层 try-with-resources
try (Connection conn = DatabaseConfig.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql);
     ResultSet rs = pstmt.executeQuery()) {
    while (rs.next()) {
        // 处理结果
    }
} // 自动按 rs -> pstmt -> conn 顺序关闭
```

**示例**（不好的资源管理）：
```java
// ❌ 不好的做法：手动关闭，容易遗漏
Connection conn = null;
PreparedStatement pstmt = null;
ResultSet rs = null;
try {
    conn = DatabaseConfig.getConnection();
    pstmt = conn.prepareStatement(sql);
    rs = pstmt.executeQuery();
    // ...
} finally {
    rs.close();  // 如果 rs 为 null，会抛出 NullPointerException
    pstmt.close();
    conn.close();
}
```

**评分细则**：
- **7-8 分**：资源管理完善，全部使用 try-with-resources
- **5-6 分**：基本正确，个别资源管理可以改进
- **3-4 分**：有资源管理，但存在问题
- **0-2 分**：资源管理缺失，有泄漏风险

---

### 二、代码质量（25 分）

#### 2.1 SQL 安全性（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| PreparedStatement 使用 | 5 分 | 所有带参数的查询都使用 PreparedStatement，无字符串拼接 |
| SQL 注入防护 | 3 分 | 用户输入通过参数设置，不直接拼接到 SQL |
| 输入验证 | 2 分 | 方法入口验证参数合法性（null、空字符串等） |

**示例**（好的 SQL 安全）：
```java
// ✅ 好的做法：参数化查询
public Optional<Task> findById(String id) {
    if (id == null || id.isBlank()) {
        return Optional.empty();
    }
    String sql = "SELECT * FROM tasks WHERE id = ?";
    try (Connection conn = DatabaseConfig.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, id);  // 安全设置参数
        // ...
    }
}
```

**示例**（不好的 SQL 安全）：
```java
// ❌ 不好的做法：字符串拼接，存在 SQL 注入
public List<Task> findByTitle(String title) {
    String sql = "SELECT * FROM tasks WHERE title LIKE '%" + title + "%'";
    // 如果 title = "' OR '1'='1"，会返回所有记录！
}
```

**评分细则**：
- **9-10 分**：SQL 安全完善，全部使用 PreparedStatement
- **7-8 分**：基本安全，个别查询可以改进
- **4-6 分**：有 SQL 安全意识，但应用不完整
- **0-3 分**：存在 SQL 注入风险

---

#### 2.2 异常处理（8 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| SQLException 处理 | 3 分 | 捕获 SQLException 并转换为有意义的运行时异常 |
| 异常信息清晰 | 3 分 | 异常消息包含上下文信息（如操作类型、实体 ID） |
| 异常链保留 | 2 分 | 使用 `new RuntimeException(msg, cause)` 保留原始异常 |

**示例**（好的异常处理）：
```java
// ✅ 好的做法：有意义的异常信息
try {
    pstmt.executeUpdate();
} catch (SQLException e) {
    throw new RuntimeException("保存任务失败 [id=" + task.getId() + "]: " + e.getMessage(), e);
}
```

**示例**（不好的异常处理）：
```java
// ❌ 不好的做法：吞掉异常或信息不足
try {
    pstmt.executeUpdate();
} catch (SQLException e) {
    e.printStackTrace();  // 调用方不知道失败了
}

// ❌ 不好的做法：丢失原始异常
catch (SQLException e) {
    throw new RuntimeException("数据库错误");  // 丢失了 e，无法追踪根因
}
```

**评分细则**：
- **7-8 分**：异常处理完善，信息清晰，保留异常链
- **5-6 分**：基本正确，个别异常处理可以改进
- **3-4 分**：有异常处理，但信息不足
- **0-2 分**：异常处理缺失或不当

---

#### 2.3 代码风格（7 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 命名规范 | 3 分 | 类名、方法名、变量名符合 Java 规范 |
| SQL 可读性 | 2 分 | SQL 语句格式清晰，使用文本块或适当换行 |
| 注释 | 2 分 | 复杂逻辑有注释说明 |

**示例**（好的 SQL 可读性）：
```java
// ✅ 好的做法：使用文本块，清晰可读
String sql = """
    SELECT t.id, t.title, t.status, u.name as user_name
    FROM tasks t
    JOIN users u ON t.user_id = u.id
    WHERE t.status = ?
    ORDER BY t.created_at DESC
    """;
```

**评分细则**：
- **6-7 分**：风格完全符合规范，SQL 清晰可读
- **4-5 分**：基本符合，小问题
- **2-3 分**：有风格问题
- **0-1 分**：风格混乱

---

### 三、测试覆盖（25 分）

#### 3.1 Repository 测试完整性（15 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| H2 内存数据库使用 | 4 分 | 正确使用 H2 内存数据库进行测试 |
| CRUD 测试覆盖 | 5 分 | save、findById、findAll、delete 都有测试 |
| 边界情况测试 | 4 分 | null 参数、空字符串、不存在记录等 |
| 异常测试 | 2 分 | 验证异常抛出（assertThrows） |

**示例**（好的 Repository 测试）：
```java
@Test
void shouldSaveAndFindTask() {
    Task task = new Task("T1", "测试", "描述", "pending");
    repository.save(task);

    Optional<Task> found = repository.findById("T1");
    assertTrue(found.isPresent());
    assertEquals("测试", found.get().getTitle());
}

@Test
void shouldThrowExceptionWhenSavingNullTask() {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> repository.save(null)
    );
    assertEquals("Task 不能为 null", exception.getMessage());
}
```

**评分细则**：
- **13-15 分**：测试覆盖全面，包括正常和异常情况
- **10-12 分**：测试基本完整，可以补充更多边界情况
- **6-9 分**：有测试，但覆盖不完整
- **0-5 分**：测试缺失或质量差

---

#### 3.2 测试质量（10 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 测试隔离 | 4 分 | 每个测试独立，使用 @BeforeEach 初始化干净状态 |
| 断言合理 | 3 分 | 使用合适的断言方法，验证关键结果 |
| 测试命名 | 3 分 | 方法名清晰描述测试场景（should...When...） |

**示例**（好的测试命名）：
```java
// ✅ 好的命名
@Test
void shouldReturnEmptyOptionalWhenTaskNotFound() { }

@Test
void shouldUpdateExistingTaskWhenIdExists() { }

// ❌ 不好的命名
@Test
void test1() { }

@Test
void testFind() { }
```

**评分细则**：
- **9-10 分**：测试质量高，隔离性好，命名清晰
- **7-8 分**：基本合理，个别可以改进
- **4-6 分**：测试质量一般
- **0-3 分**：测试质量差

---

### 四、工程规范（20 分）

#### 4.1 Git 提交规范（6 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 提交信息 | 3 分 | 使用规范格式（feat:, test:, docs: 等） |
| 提交粒度 | 3 分 | 小步提交，每个提交有明确目的 |

**示例**（好的提交信息）：
```bash
feat: 实现 JDBC 数据库连接和初始化

test: 添加 JdbcTaskRepository 单元测试

fix: 修复 ResultSet 未关闭的资源泄漏问题
```

**评分细则**：
- **5-6 分**：提交规范，信息清晰，粒度合适
- **3-4 分**：基本规范，个别可以改进
- **1-2 分**：提交信息不够清晰
- **0 分**：提交混乱

---

#### 4.2 代码组织（7 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 包结构 | 3 分 | 包名有意义（config, repository, domain 等） |
| 类职责 | 2 分 | 每个类职责单一 |
| 接口与实现分离 | 2 分 | TaskRepository 接口与 JdbcTaskRepository 实现分离 |

**评分细则**：
- **6-7 分**：代码组织良好，结构清晰
- **4-5 分**：基本合理，个别可以改进
- **2-3 分**：组织有问题
- **0-1 分**：组织混乱

---

#### 4.3 文档完整（7 分）

| 评分项 | 分数 | 标准 |
|--------|------|------|
| README | 3 分 | 有 README 说明如何运行项目 |
| 代码注释 | 2 分 | 复杂 SQL 和逻辑有注释 |
| schema 文档 | 2 分 | schema.sql 有注释说明表结构 |

**评分细则**：
- **6-7 分**：文档完整清晰
- **4-5 分**：基本完整
- **2-3 分**：文档简单
- **0-1 分**：缺少文档

---

## 进阶题与挑战题（额外加分）

### 进阶题（+20 分）

| 任务 | 分数 | 评分标准 |
|------|------|---------|
| 任务 4：数据迁移工具 | 10 分 | 迁移逻辑正确，异常处理完善，有统计信息 |
| 任务 5：连接池调研 | 10 分 | 调研有深度，对比测试完整，报告清晰 |

**评分细则**：
- **9-10 分**：完全实现，设计合理
- **7-8 分**：基本实现，部分功能可以改进
- **4-6 分**：实现不完整或有明显问题
- **0-3 分**：尝试了但未能完成

---

### 挑战题（+20 分）

| 任务 | 分数 | 评分标准 |
|------|------|---------|
| 任务 6：多表关联 | 10 分 | JOIN 查询正确，一对多关系设计合理 |
| 任务 7：ADR 设计文档 | 10 分 | 对比方案全面，决策理由充分，有演进路径 |

**评分细则**：
- **9-10 分**：实现/文档全面，有深度思考
- **7-8 分**：完整，有设计思考
- **4-6 分**：有内容，但深度不足
- **0-3 分**：未完成或质量不高

---

## AI 协作练习（额外加分）

如完成 AI 协作练习，根据审查质量评分：

| 评分项 | 分数 | 标准 |
|--------|------|------|
| 问题发现 | 3 分 | 发现 AI 生成 SQL 中的 3 个以上问题 |
| 修复质量 | 2 分 | 修复后的 SQL 规范，无新问题 |

---

## 总分计算

### 基础分（100 分）
- 功能完整性：30 分
- 代码质量：25 分
- 测试覆盖：25 分
- 工程规范：20 分

### 额外加分（最多 +40 分）
- 进阶题：+20 分
- 挑战题：+20 分
- AI 协作练习：+5 分

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
| 字符串拼接 SQL | -15 分 | 存在 SQL 注入风险 |
| 未使用 try-with-resources | -12 分 | 资源泄漏风险 |
| 缺少参数校验 | -10 分 | 方法入口未验证 null/空字符串 |
| SQLException 被吞掉 | -10 分 | 只打印不抛出，调用方无法感知错误 |
| ResultSet 未关闭 | -8 分 | 资源泄漏 |
| 测试覆盖不足 70% | -8 分 | 缺少关键测试 |
| 提交数据库文件到 Git | -5 分 | .db 文件不应提交 |
| 命名不规范 | -3 分 | 不符合 Java 命名规范 |

---

## 快速自查清单

提交前请检查：

### JDBC 基础
- [ ] pom.xml 中包含 sqlite-jdbc 依赖
- [ ] JDBC URL 格式正确（jdbc:sqlite:文件名.db）
- [ ] 使用 try-with-resources 管理 Connection
- [ ] schema.sql 能正确执行

### SQL 安全
- [ ] 所有带参数的查询使用 PreparedStatement
- [ ] 没有字符串拼接 SQL
- [ ] 用户输入通过 setString/setInt 等方式设置

### 资源管理
- [ ] Connection 使用 try-with-resources
- [ ] PreparedStatement 使用 try-with-resources
- [ ] ResultSet 使用 try-with-resources
- [ ] 嵌套资源关闭顺序正确

### 异常处理
- [ ] SQLException 被捕获并转换
- [ ] 异常信息包含上下文（操作类型、实体 ID）
- [ ] 保留原始异常链（cause 参数）

### 防御式编程
- [ ] 所有 public 方法检查 null 参数
- [ ] 空字符串检查（isBlank）
- [ ] 返回 Optional 而不是 null

### 测试
- [ ] 使用 H2 内存数据库
- [ ] @BeforeEach 初始化干净状态
- [ ] 测试覆盖 CRUD 全部操作
- [ ] 测试边界情况（null、空字符串、不存在记录）
- [ ] 测试异常抛出（assertThrows）

### 提交
- [ ] .gitignore 包含 *.db
- [ ] Git commit message 清晰规范
- [ ] 有 README 说明如何运行
- [ ] 代码在正确的包结构中
