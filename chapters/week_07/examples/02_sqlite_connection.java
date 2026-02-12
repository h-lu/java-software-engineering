/*
 * 示例：第一个 JDBC 连接 - 连接 SQLite 数据库
 * 运行方式：mvn -q -f chapters/week_07/starter_code/pom.xml exec:java \
 *          -Dexec.mainClass="examples._02_sqlite_connection"
 * 预期输出：
 *   连接成功！
 *   表创建成功！
 *   数据库文件：library.db
 */
package examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 演示 JDBC 连接 SQLite 数据库的基本操作
 *
 * <p>需要添加以下依赖到 pom.xml：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;org.xerial&lt;/groupId&gt;
 *     &lt;artifactId&gt;sqlite-jdbc&lt;/artifactId&gt;
 *     &lt;version&gt;3.44.0.0&lt;/version&gt;
 * &lt;/dependency&gt;
 * &lt;/pre>
 *
 * <p>SQLite 特点：
 * <ul>
 *   <li>零配置：无需安装服务器</li>
 *   <li>单文件：整个数据库就是一个 .db 文件</li>
 *   <li>嵌入式：直接嵌入到应用程序中</li>
 * </ul>
 */
public class _02_sqlite_connection {

    // JDBC URL 格式：jdbc:sqlite:文件名.db
    // 如果文件不存在，SQLite 会自动创建
    private static final String DB_URL = "jdbc:sqlite:library.db";

    public static void main(String[] args) {
        System.out.println("=== JDBC 连接 SQLite 演示 ===\n");

        // 步骤 1：建立连接
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            System.out.println("✓ 连接成功！");
            System.out.println("  数据库 URL: " + DB_URL);

            // 步骤 2：创建表
            createBooksTable(conn);

            System.out.println("\n✓ 数据库文件已创建/打开：library.db");
            System.out.println("  你可以用 SQLite 浏览器工具查看这个文件");

        } catch (SQLException e) {
            System.err.println("✗ 数据库操作失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 创建 books 表
     *
     * <p>使用 CREATE TABLE IF NOT EXISTS 避免重复创建时报错
     */
    private static void createBooksTable(Connection conn) throws SQLException {
        // SQL 语句使用 Java 15+ 的多行文本块（Text Blocks）
        String sql = """
            CREATE TABLE IF NOT EXISTS books (
                isbn TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                available BOOLEAN DEFAULT 1
            )
            """;

        // Statement 用于执行静态 SQL 语句
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✓ 表创建成功（或已存在）");
        }
    }
}

/*
 * ⚠️ 常见问题排查：
 *
 * 问题 1：No suitable driver found for jdbc:sqlite:library.db
 * 解决：检查 pom.xml 中是否添加了 sqlite-jdbc 依赖
 *
 * 问题 2：数据库文件找不到
 * 解决：相对路径是基于项目根目录的，检查文件是否在正确位置
 *
 * 问题 3：权限错误
 * 解决：确保程序有写入当前目录的权限
 *
 * 问题 4：表已存在错误
 * 解决：使用 CREATE TABLE IF NOT EXISTS 而不是 CREATE TABLE
 */
