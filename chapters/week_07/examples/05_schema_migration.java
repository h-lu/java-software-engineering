/*
 * 示例：数据库 Schema 设计和初始化脚本
 * 运行方式：mvn -q -f chapters/week_07/starter_code/pom.xml exec:java \
 *          -Dexec.mainClass="examples._05_schema_migration"
 * 预期输出：
 *   初始化数据库 schema...
 *   插入初始数据...
 *   数据库初始化完成
 *   图书数量: 3
 *   借阅者数量: 2
 */
package examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 演示数据库 Schema 设计和初始化脚本执行
 *
 * <p>包含：
 * <ul>
 *   <li>多表设计（图书、借阅者、借阅记录）</li>
 *   <li>外键约束和索引</li>
 *   <li>Schema 版本管理思路</li>
 * </ul>
 */
public class _05_schema_migration {

    private static final String DB_URL = "jdbc:sqlite:library_schema.db";

    public static void main(String[] args) {
        System.out.println("=== Schema 设计和数据迁移演示 ===\n");

        // 初始化数据库
        DatabaseInitializer initializer = new DatabaseInitializer(DB_URL);
        initializer.initialize();

        // 验证数据
        verifyData();

        System.out.println("\n=== Schema 版本管理建议 ===");
        System.out.println("生产环境使用 Flyway 或 Liquibase 管理迁移：");
        System.out.println("  db/migration/");
        System.out.println("    V001__create_tables.sql");
        System.out.println("    V002__add_book_category.sql");
        System.out.println("    V003__add_user_indexes.sql");
        System.out.println("\n命名规范：V{版本号}__{描述}.sql");
    }

    private static void verifyData() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // 统计图书数量
            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) as count FROM books");
            if (rs1.next()) {
                System.out.println("图书数量: " + rs1.getInt("count"));
            }

            // 统计借阅者数量
            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(*) as count FROM borrowers");
            if (rs2.next()) {
                System.out.println("借阅者数量: " + rs2.getInt("count"));
            }

            rs1.close();
            rs2.close();

        } catch (SQLException e) {
            System.err.println("验证数据失败: " + e.getMessage());
        }
    }
}

/**
 * 数据库初始化器
 *
 * <p>执行 schema.sql 和 data.sql 初始化数据库
 */
class DatabaseInitializer {
    private final String url;

    public DatabaseInitializer(String url) {
        this.url = url;
    }

    public void initialize() {
        System.out.println("初始化数据库 schema...");
        executeSchema();

        System.out.println("插入初始数据...");
        executeData();

        System.out.println("✓ 数据库初始化完成\n");
    }

    private void executeSchema() {
        // Schema 定义：表结构
        String schemaSql = """
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
            """;

        executeScript(schemaSql);
    }

    private void executeData() {
        // 初始数据
        String dataSql = """
            -- 初始图书数据
            INSERT OR IGNORE INTO books (isbn, title, author) VALUES
                ('978-0134685991', 'Effective Java', 'Joshua Bloch'),
                ('978-0132350884', 'Clean Code', 'Robert C. Martin'),
                ('978-0321356680', 'Java Concurrency in Practice', 'Brian Goetz');

            -- 初始借阅者数据
            INSERT OR IGNORE INTO borrowers (id, name, email) VALUES
                (1, '张三', 'zhangsan@example.com'),
                (2, '李四', 'lisi@example.com');
            """;

        executeScript(dataSql);
    }

    private void executeScript(String sql) {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // 分割并执行每条语句
            for (String statement : sql.split(";")) {
                String trimmed = statement.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                    stmt.execute(trimmed);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("执行 SQL 脚本失败: " + e.getMessage(), e);
        }
    }
}

/*
 * 数据库设计要点：
 *
 * 1. 主键选择
 *    - books 表使用 ISBN（业务主键）
 *    - borrowers 和 loans 使用自增 INTEGER（代理主键）
 *
 * 2. 外键约束
 *    - loans.book_isbn 引用 books.isbn
 *    - loans.borrower_id 引用 borrowers.id
 *    - 确保数据一致性，防止孤立记录
 *
 * 3. 索引设计
 *    - 外键字段自动查询频繁，加索引
 *    - 避免过度索引（写操作变慢）
 *
 * 4. 规范化（Normalization）
 *    - borrowers 独立成表，避免数据冗余
 *    - 借阅者改名只需改一处
 *
 * 5. Schema 迁移策略
 *    - 开发环境：删除重建（简单）
 *    - 生产环境：增量迁移（安全）
 *    - 使用 Flyway/Liquibase 管理版本
 */
