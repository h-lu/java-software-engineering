# Week 07：让数据活下来——数据持久化与 JDBC

> "内存是易失的，磁盘是永恒的。"
> —— 数据库工程师的谚语

想象一下：你花了一下午录入的图书信息，关机后再打开，全没了。这不是 bug，而是内存的本质——它只为"此刻"服务，不负责"永恒"。但在真实世界里，数据必须 survive 程序的重启。

2025 年的数据库版图正在经历一场静默革命。SQLite，这个诞生于 2000 年的嵌入式数据库，正在边缘计算和 IoT 领域强势复兴。从 Cloudflare D1 到 Turso 的 libSQL，从智能手表到无人机，SQLite 以其"零配置、单文件"的特性成为边缘设备的首选存储方案。据 2025 年的基准测试显示，优化后的 SQLite 在边缘计算场景下可达到每秒 12 万+ 次的操作性能。与此同时，AI 正在重塑我们与数据库的交互方式——GitHub Copilot 已集成 SQL Server 安全分析器，能自动检测 SQL 注入风险。本周，你将掌握数据持久化的核心技能，让你的程序真正"记住"东西。

---

## 前情提要

上周你学会了用 JUnit 5 为代码构建安全网——从第一个简单的 `@Test`，到使用 `@BeforeEach` 消除重复，再到 `assertThrows` 验证异常。你的 CampusFlow 现在有了完整的单元测试覆盖，测试覆盖率达到了 80%+。

但还有一个致命弱点：所有数据都存储在内存中的 `ArrayList` 和 `HashMap` 里。程序一关闭，数据就烟消云散。本周我们要解决这个问题——用 JDBC 连接 SQLite，让数据真正持久化到磁盘。

---

## 学习目标

完成本周学习后，你将能够：

1. 解释内存存储与持久化存储的区别，识别需要持久化的场景
2. 使用 JDBC 连接 SQLite 数据库并执行基本的 CRUD 操作
3. 正确管理数据库连接资源，避免资源泄漏
4. 设计并执行数据库初始化脚本，实现数据迁移策略
5. 为数据库访问层编写单元测试（使用内存数据库或测试专用数据库）
6. 将现有的内存 Repository 迁移到 JDBC 实现

---

<!--
贯穿案例设计：【图书馆借阅追踪器的持久化改造】
- 第 1 节：从内存版 LibraryTracker 开始，展示程序关闭后数据丢失的问题
- 第 2 节：引入 SQLite 和 JDBC，建立数据库连接
- 第 3 节：使用 try-with-resources 管理连接，避免资源泄漏
- 第 4 节：实现完整的 CRUD 操作（增删改查）
- 第 5 节：设计 schema 和初始化脚本，数据迁移
- 第 6 节：使用 H2 内存数据库编写 Repository 测试
- 第 7 节：CampusFlow 进度——将 Repository 迁移到 JDBC
最终成果：一个完整的、数据持久化的图书馆借阅系统，有测试覆盖

认知负荷预算检查：
- 本周新概念（系统化工程阶段上限 6 个）：
  1. JDBC（Java Database Connectivity）
  2. SQLite（嵌入式数据库）
  3. 数据库连接管理（Connection）
  4. SQL 基础（DDL/DML）
  5. try-with-resources（资源自动关闭）
  6. 内存数据库测试（H2）
- 结论：✅ 正好 6 个，在预算内

回顾桥设计（至少 2 个）：
- [Repository 模式]（来自 week_05）：在第 4-7 节，通过将内存 Repository 改造为 JDBC Repository 来复现
- [防御式编程]（来自 week_03）：在第 4 节，通过 SQL 注入防护和输入验证来复现
- [单元测试]（来自 week_06）：在第 6 节，通过 Repository 单元测试来复现
- [try-catch-finally]（来自 week_03）：在第 3 节，通过对比引出 try-with-resources 的优势

AI 小专栏规划：
专栏 1（放在第 2 节之后，前段）：
- 主题：AI 辅助 SQL 生成与审查
- 连接点：与第 2 节 JDBC/SQL 基础呼应
- 建议搜索词："AI SQL generation tools 2025 2026", "GitHub Copilot SQL review"

专栏 2（放在第 5 节之后，中段）：
- 主题：AI 时代的数据库设计决策
- 连接点：与第 5 节数据建模和 schema 设计呼应
- 建议搜索词："AI database schema design 2025 2026", "LLM database migration best practices"

CampusFlow 本周推进：
- 上周状态：CLI 版功能完整，内存存储，有完整单元测试（覆盖率 80%+）
- 本周改进：将内存 Repository 迁移到 JDBC + SQLite 实现，数据持久化到磁盘
- 涉及的本周概念：JDBC、SQLite、连接管理、CRUD、初始化脚本、Repository 测试
- 建议示例文件：examples/07_campusflow_jdbc_repository.java

角色出场规划：
- 小北：在第 2 节 JDBC 配置踩坑（驱动类找不到、URL 写错）；在第 4 节犯 SQL 语法错误（忘记 WHERE 条件导致全表更新）
- 阿码：在第 3 节追问"连接池是什么？为什么示例没有？"；在第 5 节问"如果 schema 变了怎么办？"
- 老潘：在第 1 节点评内存存储的生产环境风险；在第 7 节给生产环境数据库选型建议（SQLite vs MySQL vs PostgreSQL）
-->

## 1. 程序一关，数据全没？

小北昨天加班到很晚，终于把图书馆的 100 本书信息录入了系统。今天一早打开程序，准备继续工作——界面空空如也。

"我明明昨天录入了 100 本书啊？"

他反复检查了代码，确认 `addBook` 方法没问题，又试着重新录入几本书，程序运行正常。关机，再打开——又是空的。

"这不可能……"小北盯着屏幕，一脸茫然。

老潘路过，看了一眼："你用的是 `ArrayList` 存数据吧？"

"对，Week 05 学的……"

"`ArrayList` 存在内存里。内存是什么？是 RAM，是易失的（volatile）。程序一关，操作系统就把那块内存收回了，数据当然没了。"

小北恍然大悟。他想起 Week 05 用的 `LibraryTracker`：

```java
public class LibraryTracker {
    private List<Book> books = new ArrayList<>();  // 存在内存里！
    private Map<String, BorrowRecord> records = new HashMap<>();

    // 程序关闭后，books 和 records 里的数据全部丢失
}
```

这就是**内存存储**的本质：快，但短命。对于临时计算、缓存数据，内存存储很合适；但对于需要长期保存的业务数据——图书信息、用户账户、交易记录——你必须用**持久化存储**（persistent storage）。

"在生产环境里，内存数据丢失是事故。"老潘说，"想象一下，电商网站的订单存在内存里，服务器一重启，今天的订单全没了——这公司还开不开？"

小北倒吸一口凉气。他意识到，自己写的程序虽然能跑，但离"生产级"还差得远。

问题的解决方案是**数据库**——专门用来持久化存储数据的系统。但数据库种类繁多：MySQL、PostgreSQL、MongoDB、Redis……该选哪个？

对于学习和原型开发，有一个绝佳选择：**SQLite**。

---

## 2. SQLite：一个文件就是一个数据库

"SQLite？"阿码听到这个名字，"为什么不直接用 MySQL？那个听起来更专业。"

好问题。MySQL 确实更专业——它是客户端-服务器架构的数据库，能处理高并发、大数据量。但对于学习和原型开发，MySQL 太重了：你需要安装服务器、配置用户权限、启动服务……

SQLite 的理念完全不同：**零配置、单文件、嵌入式**。整个数据库就是一个 `.db` 文件，不需要服务器进程，不需要配置，直接嵌入到你的程序里。

```
你的程序  ←→  SQLite 库  ←→  library.db（普通文件）
```

这意味着：
- 不需要安装数据库服务器
- 数据库文件可以复制、移动、备份
- 非常适合学习和单元测试

Java 通过 **JDBC**（Java Database Connectivity）连接数据库。JDBC 是 Java 的标准数据库访问接口，无论底层是 SQLite、MySQL 还是 Oracle，你的代码写法都大同小异。

让我们建立第一个连接。首先，在 `pom.xml` 中添加 SQLite JDBC 驱动依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.44.0.0</version>
    </dependency>
</dependencies>
```

然后，编写连接代码：

```java
// 文件：src/main/java/JdbcFirstConnection.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcFirstConnection {

    public static void main(String[] args) {
        // JDBC URL 格式：jdbc:sqlite:文件名.db
        String url = "jdbc:sqlite:library.db";

        try {
            // 建立连接
            Connection conn = DriverManager.getConnection(url);
            System.out.println("连接成功！");

            // 关闭连接
            conn.close();
        } catch (SQLException e) {
            System.err.println("连接失败：" + e.getMessage());
        }
    }
}
```

运行这段代码，你会在项目目录下看到一个 `library.db` 文件——这就是你的数据库。如果文件不存在，SQLite 会自动创建它。

这里要注意 `url` 变量的类型——它是 `String`， Week 01 学的**静态类型**在这里派上用场：编译器会检查你传入 `getConnection()` 的是不是字符串，如果是其他类型（比如 `int`），编译就会报错。

小北兴奋地运行代码，然后报错了：

```
java.sql.SQLException: No suitable driver found for jdbc:sqlite:library.db
```

"这是什么鬼？"小北挠头。

这是 JDBC 的经典坑：**驱动类没有被加载**。虽然 Maven 已经下载了驱动 jar 包，但 JDBC 需要显式加载驱动类。在较新的 SQLite JDBC 驱动中，这个问题通常自动解决；但如果遇到，可以显式加载：

```java
// 显式加载驱动类（现代 JDBC 通常不需要，但遇到问题时可以尝试）
Class.forName("org.sqlite.JDBC");
```

小北加上这行，再次运行——成功！他看着目录下新出现的 `library.db` 文件，有种"终于把数据落地了"的踏实感。

现在，让我们创建第一张表：

```java
// 文件：src/main/java/CreateTableExample.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTableExample {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:library.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // 创建 books 表
            String sql = """
                CREATE TABLE IF NOT EXISTS books (
                    isbn TEXT PRIMARY KEY,
                    title TEXT NOT NULL,
                    author TEXT NOT NULL,
                    available BOOLEAN DEFAULT 1
                )
                """;

            stmt.execute(sql);
            System.out.println("表创建成功！");

        } catch (SQLException e) {
            System.err.println("错误：" + e.getMessage());
        }
    }
}
```

注意这里的 `try (Connection conn = ...)` 语法——这是 Java 7 引入的 **try-with-resources**，它会自动关闭 `Connection` 和 `Statement`。下一节我们会详细讲这个。

---

> **AI 时代小专栏：AI 辅助 SQL 生成与审查**
>
> 2025 年，AI 辅助 SQL 生成工具已经相当成熟。GitHub Copilot 不仅能生成 Java 代码，也能在 SQL Server Management Studio 中辅助编写 T-SQL。更重要的是，微软在 2026 年 1 月发布的 MSSQL 扩展（v1.37+）集成了 Security Analyzer，能自动检测 SQL 注入风险、不安全的动态 SQL 和过度权限。
>
> 但这不意味着你可以无脑信任 AI 生成的 SQL。研究表明，AI 生成的 SQL 虽然语法正确率高，但在安全性和性能优化上仍需要人工审查。特别是当涉及多表连接、事务边界或敏感数据访问时，AI 可能会生成"看起来对但跑起来慢"的查询。
>
> 使用 AI 辅助 SQL 的最佳实践：
> 1. **让 AI 生成草稿，你负责审查**——特别是 WHERE 条件和 JOIN 逻辑
> 2. **用参数化查询**——永远不要直接拼接用户输入到 SQL 字符串中（后面我们会详细讲）
> 3. **测试边界情况**——AI 往往只考虑"正常路径"
>
> 回到你刚写的代码——`CREATE TABLE` 语句看似简单，但字段类型选择（TEXT vs VARCHAR）、约束设计（PRIMARY KEY、NOT NULL）都需要你理解业务需求。AI 可以帮你写语法正确的 SQL，但表结构设计反映的是你对业务的理解。
>
> 参考（访问日期：2026-02-12）：
> - [GitHub Copilot SQL Server Security Analyzer](https://techcommunity.microsoft.com/blog/coreinfrastructureandsecurityblog/github-copilot--sql-server-understanding-the-security-analyzer/4489247)

---

## 3. 连接用完了要还——资源管理

小北的连接代码能跑了，但老潘看了一眼，摇摇头："连接没关。"

"我调了 `conn.close()` 啊？"

"如果在 `close()` 之前抛异常呢？"

小北愣住了。确实，如果 `execute()` 抛出 `SQLException`，`conn.close()` 就不会执行，连接就一直开着——这叫**资源泄漏**（resource leak）。数据库连接是有限的资源，如果程序一直打开新连接而不关闭，最终会耗尽连接池（或操作系统资源），导致"too many connections"错误。

Week 03 我们学过 `try-catch-finally` 可以解决这个问题：

```java
Connection conn = null;
try {
    conn = DriverManager.getConnection(url);
    // 使用连接...
} catch (SQLException e) {
    // 处理异常...
} finally {
    // 无论是否异常，都关闭连接
    if (conn != null) {
        try {
            conn.close();
        } catch (SQLException e) {
            // 关闭时的异常...
        }
    }
}
```

但这代码太啰嗦了。Java 7 引入了 **try-with-resources**，让资源管理变得优雅：

```java
try (Connection conn = DriverManager.getConnection(url)) {
    // 使用连接...
} catch (SQLException e) {
    // 处理异常...
}
// 连接会自动关闭，即使在 try 块中抛出了异常
```

`try-with-resources` 的原理是：`Connection`、`Statement`、`ResultSet` 都实现了 `AutoCloseable` 接口，当 `try` 块结束时（无论正常结束还是异常退出），它们的 `close()` 方法会被自动调用。

多个资源也可以一起管理：

```java
try (Connection conn = DriverManager.getConnection(url);
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM books")) {

    while (rs.next()) {
        System.out.println(rs.getString("title"));
    }

} catch (SQLException e) {
    System.err.println("查询失败：" + e.getMessage());
}
// conn、stmt、rs 都会按相反顺序自动关闭
```

阿码在旁边看着，突然问："我听说生产环境都用连接池，为什么我们的示例没有？"

好问题。**连接池**（connection pool）是管理数据库连接的高级技术。它维护一组可复用的连接，避免频繁创建和关闭连接的开销。生产环境确实应该用连接池（如 HikariCP），但对于学习和单元测试，直接使用 `DriverManager` 更简单，也足够用。

"那什么时候该用连接池？"阿码追问。

老潘回答："当你的程序需要频繁访问数据库，且对性能有要求时。连接池能复用连接，减少创建开销。但对于 CLI 工具、简单的批处理程序，直接 `DriverManager` 就行。"

小北点点头，把 `try-with-resources` 的写法记在心里。比起 Week 03 学的 `try-catch-finally`，这种写法既安全又简洁——**资源管理自动化，是 Java 工程化的重要进步**。

"所以我现在能放心地操作数据库了？"小北问。

"能了，但还有一件事，"老潘说，"怎么把数据存进去、取出来、修改、删除？这才是 Repository 的核心工作。"

---

## 4. 增删改查——Repository 的 JDBC 实现

现在我们来实现完整的 CRUD（Create, Read, Update, Delete）操作。还记得 Week 05 学的 **Repository 模式**吗？它封装了对数据源的访问逻辑，为领域层提供统一的数据操作接口。

这也呼应了 Week 04 的**类定义**——当时我们用 `class Book` 定义了领域类，现在需要为它设计对应的表结构。类设计决定代码结构，表设计决定数据存储结构，两者相辅相成。

从 Week 02 的**领域模型**角度看，我们之前把 `Book` 实体存在内存的 `ArrayList` 里，现在只是换了存储介质——从内存列表换到数据库表，领域模型本身并没有变。

之前我们的 `BookRepository` 用 `ArrayList` 实现：

```java
// Week 05 的内存版 Repository
public class InMemoryBookRepository {
    private List<Book> books = new ArrayList<>();

    public void save(Book book) { books.add(book); }
    public Book findByIsbn(String isbn) { /* 遍历查找 */ }
    // ...
}
```

现在，我们要把它改造成 JDBC 版，让数据真正持久化。先看基础的保存和查询：

```java
// 文件：src/main/java/JdbcBookRepository.java（第一部分）
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBookRepository {
    private final String url;

    public JdbcBookRepository(String url) {
        this.url = url;
    }

    // CREATE - 插入新书记录
    public void save(Book book) {
        // 防御式编程：参数校验（Week 03）
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("Book 和 ISBN 不能为空");
        }

        String sql = "INSERT INTO books (isbn, title, author, available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setBoolean(4, book.isAvailable());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            // 将受检异常转换为运行时异常（Week 03 的异常处理策略）
            throw new RuntimeException("保存图书失败: " + e.getMessage(), e);
        }
    }

    // READ - 根据 ISBN 查询
    public Optional<Book> findByIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM books WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }

        } catch (SQLException e) {
            // SQLException 是受检异常（Week 03），必须处理或声明
            // 这里转换为运行时异常，简化调用方处理
            throw new RuntimeException("查询图书失败: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    // READ - 查询所有图书
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询图书列表失败: " + e.getMessage(), e);
        }

        return books;
    }

    // 辅助方法：将 ResultSet 映射为 Book 对象
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
            rs.getString("isbn"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getBoolean("available")
        );
    }
}
```

接下来是更新和删除操作：

```java
// 文件：src/main/java/JdbcBookRepository.java（第二部分）

    // UPDATE - 更新图书信息
    public void update(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("Book 和 ISBN 不能为空");
        }

        String sql = "UPDATE books SET title = ?, author = ?, available = ? WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setBoolean(3, book.isAvailable());
            pstmt.setString(4, book.getIsbn());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("图书不存在: " + book.getIsbn());
            }

        } catch (SQLException e) {
            throw new RuntimeException("更新图书失败: " + e.getMessage(), e);
        }
    }

    // DELETE - 删除图书
    public void delete(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN 不能为空");
        }

        String sql = "DELETE FROM books WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("删除图书失败: " + e.getMessage(), e);
        }
    }
```

小北运行完测试，看着数据库里真实存储的数据，松了口气。但他注意到一个细节：代码里用的是 `PreparedStatement`，而不是 `Statement`。

"这两个有什么区别？"他问。

老潘拿过他的电脑，指着一段代码说："你一开始写的是这样吧？"

```java
// ❌ 错误示范：字符串拼接 SQL
String sql = "INSERT INTO books VALUES ('" + book.getIsbn() + "', '" + book.getTitle() + "')";
stmt.execute(sql);
```

老潘脸色变了："停！这是 SQL 注入漏洞。"

**SQL 注入**是最常见的数据库安全漏洞。如果 `book.getTitle()` 包含恶意内容，比如 `"'); DROP TABLE books; --"`，拼接后的 SQL 就变成了：

```sql
INSERT INTO books VALUES ('ISBN001', ''); DROP TABLE books; --')
```

这会导致 `books` 表被删除！

`PreparedStatement` 通过**参数化查询**防止这种攻击：

```java
// ✅ 正确做法：使用 PreparedStatement
String sql = "INSERT INTO books (isbn, title) VALUES (?, ?)";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, book.getIsbn());  // 自动转义特殊字符
pstmt.setString(2, book.getTitle()); // 即使包含引号也不会破坏 SQL 结构
```

`?` 是占位符，实际的值通过 `setString()`、`setInt()` 等方法设置。JDBC 驱动会自动处理转义，确保用户输入不会被解释为 SQL 代码。

这是 Week 03 学的**防御式编程**在数据库层的应用：**永远不要信任用户输入，永远在边界处验证和清理**。

小北合上笔记本，看着屏幕上正常运行的程序，心里踏实了不少。但他也知道，这只是开始——真正的挑战是：表结构怎么设计？如果以后要加字段怎么办？

---

## 5. 表结构设计与数据迁移

阿码举手："如果 schema 变了，旧数据怎么办？"

好问题。这就是**数据迁移**（migration）问题。让我们先设计一个完整的 schema：

```sql
-- 文件：src/main/resources/schema.sql
-- 图书表
CREATE TABLE IF NOT EXISTS books (
    isbn TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    available BOOLEAN DEFAULT 1
);

-- 借阅者表
CREATE TABLE IF NOT EXISTS borrowers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT
);

-- 借阅记录表
CREATE TABLE IF NOT EXISTS loans (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    book_isbn TEXT NOT NULL,
    borrower_id INTEGER NOT NULL,
    borrow_date TEXT NOT NULL,
    return_date TEXT,
    FOREIGN KEY (book_isbn) REFERENCES books(isbn),
    FOREIGN KEY (borrower_id) REFERENCES borrowers(id)
);

-- 索引优化查询
CREATE INDEX IF NOT EXISTS idx_loans_book ON loans(book_isbn);
CREATE INDEX IF NOT EXISTS idx_loans_borrower ON loans(borrower_id);
```

SQLite 支持标准的 SQL 数据类型，但有一些特点：
- `TEXT`：字符串
- `INTEGER`：整数（包括布尔值，0/1）
- `REAL`：浮点数
- `BLOB`：二进制数据

注意我们用了 `FOREIGN KEY` 定义外键关系，建立了表之间的关联。这是关系数据库的核心特性。

初始化数据可以放在另一个文件：

```sql
-- 文件：src/main/resources/data.sql
INSERT INTO books (isbn, title, author) VALUES
    ('978-0134685991', 'Effective Java', 'Joshua Bloch'),
    ('978-0132350884', 'Clean Code', 'Robert C. Martin'),
    ('978-0321356680', 'Java Concurrency in Practice', 'Brian Goetz');
```

在程序启动时执行这些脚本：

```java
// 文件：src/main/java/DatabaseInitializer.java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    private final String url;

    public DatabaseInitializer(String url) {
        this.url = url;
    }

    public void initialize() {
        executeScript("schema.sql");
        executeScript("data.sql");
    }

    private void executeScript(String resourceName) {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // 从 classpath 读取 SQL 文件
            String sql = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(resourceName)
            )).lines().collect(Collectors.joining("\n"));

            // 分割并执行每条语句
            for (String statement : sql.split(";")) {
                if (!statement.trim().isEmpty()) {
                    stmt.execute(statement);
                }
            }

            System.out.println(resourceName + " 执行成功");

        } catch (Exception e) {
            throw new RuntimeException("执行 " + resourceName + " 失败: " + e.getMessage(), e);
        }
    }
}
```

关于 schema 变更，生产环境通常使用迁移工具（如 Flyway、Liquibase），它们会记录已执行的迁移脚本，确保每个变更只执行一次。对于学习项目，你可以手动管理：

```
V001__create_tables.sql
V002__add_book_category.sql
V003__add_user_indexes.sql
```

命名规范：`V{版本号}__{描述}.sql`。这样你可以清楚地看到数据库的演进历史。

小北看着自己的 schema，突然意识到：数据库设计不只是写 SQL，而是在为业务建模。每一张表、每一个字段、每一个约束，都反映了你对业务的理解。

阿码在旁边说："我用 AI 生成过 schema，几秒钟就搞定了。"

"那它生成的外键约束对吗？"小北问，"借阅记录表为什么要用 `borrower_id` 而不是直接存名字，AI 能解释清楚吗？"

阿码愣了一下："这个……"

"这就是**规范化**（normalization）的思想，"老潘走过来，"避免数据冗余。如果借阅者改名，你只需要改 `borrowers` 表，所有相关记录自动更新。AI 可以帮你写 SQL，但数据库设计反映的是你对业务的理解。"

---

> **AI 时代小专栏：AI 时代的数据库设计决策**
>
> AI 正在改变数据库设计的方式。截至 2025 年底，像 AI2sql、SQLAI.ai 这样的工具可以根据自然语言描述生成表结构。你只需要说"创建一个图书管理系统，包含图书、借阅者和借阅记录"，AI 就能生成相应的 CREATE TABLE 语句。
>
> 但这里有一个陷阱：**AI 生成的 schema 往往是"语法正确但设计欠考虑"的**。它可能会：
> - 忽略外键约束，导致数据不一致
> - 选择不合适的字段类型（比如用 TEXT 存日期）
> - 遗漏必要的索引，导致查询性能问题
> - 没有考虑业务规则（比如一本书不能被两个人同时借阅）
>
> 使用 AI 辅助 schema 设计的最佳实践：
> 1. **让 AI 生成初稿，你负责审查**——特别关注主键、外键、约束
> 2. **理解生成的每一行**——不要复制粘贴你不理解的 SQL
> 3. **考虑查询模式**——哪些字段会被频繁查询？需要索引吗？
> 4. **预留扩展空间**——但也不要过度设计（YAGNI 原则）
>
> 回到你设计的 schema——`loans` 表为什么要用 `borrower_id` 而不是直接存借阅者名字？这是**规范化**（normalization）的思想：避免数据冗余。如果借阅者改名，你只需要改 `borrowers` 表，所有相关记录自动更新。
>
> AI 可以帮你写 SQL，但数据库设计反映的是你对业务的理解。这是工程师的核心能力，不是 AI 能替代的。

---

## 6. 测试数据库访问层

小北写完 `JdbcBookRepository`，信心满满地准备写测试——然后愣住了。

"等等，测试要操作真实数据库吗？那测试跑完，我的数据岂不是乱了？"

他想象了一下：测试往数据库里塞了一堆"测试图书"，跑完之后这些垃圾数据还留在库里，下次启动程序时全显示在界面上——"这太可怕了。"

阿码在旁边说："而且如果测试失败了，数据库里会留下脏数据，影响后面的测试。要不……我们干脆不测了？"

"不行！"小北立刻反对，"Week 06 学的测试不能白学。肯定有办法。"

这正是数据库测试的经典难题：**测试隔离**。Week 06 我们学会了单元测试，但那些测试都是纯内存操作，互不干扰。现在 Repository 操作的是真实数据库，怎么办？

解决方案是：**使用内存数据库**。

**H2** 是一个用 Java 编写的内存数据库，兼容 JDBC 和大部分 SQL 语法。它非常适合单元测试：
- 测试运行快（内存操作，无磁盘 I/O）
- 测试隔离（每个测试用独立数据库）
- 无需额外安装

在 `pom.xml` 中添加 H2 依赖（test scope）：

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.224</version>
    <scope>test</scope>
</dependency>
```

然后编写 Repository 测试：

```java
// 文件：src/test/java/JdbcBookRepositoryTest.java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JdbcBookRepositoryTest {

    private JdbcBookRepository repository;
    private final String testUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    @BeforeEach
    void setUp() {
        // 每个测试前初始化内存数据库
        DatabaseInitializer initializer = new DatabaseInitializer(testUrl);
        initializer.initialize();

        repository = new JdbcBookRepository(testUrl);
    }

    @Test
    void shouldSaveAndFindBook() {
        // 准备
        Book book = new Book("978-TEST-001", "测试书", "测试作者", true);

        // 执行
        repository.save(book);

        // 验证
        Optional<Book> found = repository.findByIsbn("978-TEST-001");
        assertTrue(found.isPresent());
        assertEquals("测试书", found.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenBookNotFound() {
        Optional<Book> found = repository.findByIsbn("不存在的ISBN");
        assertFalse(found.isPresent());
    }

    @Test
    void shouldUpdateBook() {
        // 先保存
        Book book = new Book("978-UPDATE-001", "原书名", "作者", true);
        repository.save(book);

        // 更新
        Book updated = new Book("978-UPDATE-001", "新书名", "作者", false);
        repository.update(updated);

        // 验证
        Optional<Book> found = repository.findByIsbn("978-UPDATE-001");
        assertTrue(found.isPresent());
        assertEquals("新书名", found.get().getTitle());
        assertFalse(found.get().isAvailable());
    }

    @Test
    void shouldDeleteBook() {
        Book book = new Book("978-DELETE-001", "待删除", "作者", true);
        repository.save(book);
        assertTrue(repository.findByIsbn("978-DELETE-001").isPresent());

        repository.delete("978-DELETE-001");

        assertFalse(repository.findByIsbn("978-DELETE-001").isPresent());
    }

    @Test
    void shouldThrowExceptionWhenSavingNullBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        });
    }

    @Test
    void shouldFindAllBooks() {
        repository.save(new Book("ISBN-1", "书1", "作者", true));
        repository.save(new Book("ISBN-2", "书2", "作者", true));
        repository.save(new Book("ISBN-3", "书3", "作者", true));

        var books = repository.findAll();

        assertEquals(3, books.size());
    }
}
```

注意 `@BeforeEach` 的用法——每个测试方法前都会执行，确保测试之间的隔离性。这是 Week 06 学的**测试生命周期**在数据库测试中的应用。

H2 的 JDBC URL `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1` 表示：
- `mem`：内存模式，数据不写入磁盘
- `DB_CLOSE_DELAY=-1`：连接关闭后不立即删除数据库（这样多个连接可以访问同一个内存数据库）

这种测试策略的好处不言而喻：内存操作比磁盘快几个数量级；每个测试用独立的数据库实例，互不影响；测试数据在 `@BeforeEach` 中初始化，每次状态一致。

小北跑完测试，看着全绿的输出，松了口气。他的 Repository 现在有了一层"安全网"——任何改动都能被快速验证。这就是 Week 06 学的测试思维在数据库层的延伸。

"等等，"阿码突然问，"如果生产环境用 SQLite，测试用 H2，会不会有兼容性问题？"

好问题。H2 兼容大部分标准 SQL，但不同数据库确实有差异（比如 SQLite 的 `LIMIT` 和 MySQL 的语法就不同）。对于学习项目，这种差异可以忽略；但对于生产系统，你应该用**与生产环境相同的数据库**做集成测试。SQLite 的好处是轻量，但如果你的生产环境是 PostgreSQL，测试也应该用 PostgreSQL——至少要在 CI 流程中加一道集成测试。

老潘补充道："在公司里，我们通常用 Docker 在 CI 里启动一个 PostgreSQL 容器跑集成测试。本地单元测试用 H2 快速验证，CI 里用真实数据库保证兼容性。这叫'测试金字塔'——单元测试多而快，集成测试少而全。"

小北点点头，把这点记在心里。测试不只是为了"跑通"，更是为了建立对代码的信心。

---

## 7. CampusFlow 进度：从内存到持久化

到目前为止，CampusFlow 的 Repository 层已经用 `ArrayList` 和 `HashMap` 重构完成，也有了完整的单元测试。但数据还是"假"的——程序一关就消失。

本周我们要把 `TaskRepository` 从内存版迁移到 JDBC 版。先看原来的内存实现：

```java
// Week 05-06 的内存版
public class TaskRepository {
    private final Map<String, Task> tasks = new HashMap<>();

    public void save(Task task) { tasks.put(task.getId(), task); }
    public Optional<Task> findById(String id) { return Optional.ofNullable(tasks.get(id)); }
    public List<Task> findAll() { return new ArrayList<>(tasks.values()); }
    public void delete(String id) { tasks.remove(id); }
}
```

现在改造成 JDBC 版：

```java
// 文件：src/main/java/repository/JdbcTaskRepository.java
package repository;

import domain.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTaskRepository implements TaskRepository {
    private final String url;

    public JdbcTaskRepository(String url) {
        this.url = url;
    }

    @Override
    public void save(Task task) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task 和 ID 不能为空");
        }

        String sql = """
            INSERT INTO tasks (id, title, description, status, created_at)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
                title = excluded.title,
                description = excluded.description,
                status = excluded.status
            """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getStatus());
            pstmt.setString(5, task.getCreatedAt().toString());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("保存任务失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Task> findById(String id) {
        if (id == null || id.isBlank()) return Optional.empty();

        String sql = "SELECT * FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToTask(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询任务失败: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";
        List<Task> tasks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(mapToTask(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询任务列表失败: " + e.getMessage(), e);
        }

        return tasks;
    }

    @Override
    public void delete(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID 不能为空");
        }

        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("删除任务失败: " + e.getMessage(), e);
        }
    }

    private Task mapToTask(ResultSet rs) throws SQLException {
        return new Task(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("status")
        );
    }
}
```

对应的 schema：

```sql
-- 文件：src/main/resources/campusflow_schema.sql
CREATE TABLE IF NOT EXISTS tasks (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL CHECK (status IN ('pending', 'in_progress', 'done')),
    created_at TEXT NOT NULL
);
```

注意 `CHECK` 约束——它确保 `status` 只能是预定义的三个值之一。这是数据库层的防御式编程。

老潘看了这段代码，点点头："SQLite 适合学习和原型开发，但生产环境要考虑更多因素。"

"比如？"小北问。

"并发访问。SQLite 是单写多读，如果多个用户同时修改数据，会有锁竞争。对于高并发场景，应该考虑 PostgreSQL 或 MySQL。但对于 CampusFlow 这种学习项目，SQLite 完全够用。"

"那什么时候该从 SQLite 迁移到 PostgreSQL？"

"当以下情况出现时：
1. 需要多用户同时写入
2. 数据量超过几十 GB
3. 需要复杂的权限管理
4. 需要主从复制做高可用

但在那之前，SQLite 能帮你走很远。很多生产系统（包括一些云服务）底层也是 SQLite。"

---

## Git 本周要点

数据库文件不应该提交到版本控制：

```gitignore
# 数据库文件
*.db
*.sqlite
*.sqlite3

# 但 schema 和迁移脚本要提交
!schema.sql
!data.sql
!V*.sql
```

迁移脚本命名规范：
```
db/migration/
  V001__create_tables.sql
  V002__add_task_priority.sql
  V003__add_user_indexes.sql
```

---

## 本周小结（供下周参考）

本周你学会了让数据"活下来"的核心技能。从理解内存存储的局限性，到使用 JDBC 连接 SQLite；从 `try-with-resources` 管理资源，到用 `PreparedStatement` 防止 SQL 注入；从设计数据库 schema，到用 H2 内存数据库编写 Repository 测试——你的程序现在真正拥有了持久化能力。

小北的图书馆系统现在关机再开，数据依然在。CampusFlow 也从"玩具"变成了"能用的小工具"。

但 JDBC 代码写起来有点啰嗦，对吧？每个 CRUD 操作都要处理 `Connection`、`PreparedStatement`、`ResultSet`……下周我们将学习**设计模式**，看看如何用更优雅的方式组织数据访问代码。

---

## Definition of Done（学生自测清单）

- [ ] 我能解释内存存储和持久化存储的区别
- [ ] 我能写出 JDBC 连接 SQLite 的基本代码
- [ ] 我能正确使用 try-with-resources 管理数据库连接
- [ ] 我能使用 PreparedStatement 实现 CRUD 操作
- [ ] 我能设计简单的数据库 schema 并编写初始化脚本
- [ ] 我能为 Repository 编写使用内存数据库的单元测试
- [ ] 我的 CampusFlow 数据现在存储在 SQLite 中，程序重启后数据不丢失
- [ ] 我的代码通过了所有单元测试（包括新的 Repository 测试）
