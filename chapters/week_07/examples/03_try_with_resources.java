/*
 * 示例：try-with-resources 资源管理
 * 运行方式：mvn -q -f chapters/week_07/starter_code/pom.xml exec:java \
 *          -Dexec.mainClass="examples._03_try_with_resources"
 * 预期输出：
 *   旧方式（繁琐）...
 *   新方式（简洁）...
 *   多资源管理...
 *   所有资源已正确关闭
 */
package examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 对比 try-catch-finally 和 try-with-resources 两种资源管理方式
 *
 * <p>数据库连接、语句和结果集都是昂贵的资源，必须确保关闭。
 * Java 7 引入的 try-with-resources 让资源管理变得简洁安全。
 */
public class _03_try_with_resources {

    private static final String DB_URL = "jdbc:sqlite:demo.db";

    public static void main(String[] args) throws SQLException {
        System.out.println("=== 资源管理方式对比 ===\n");

        // 先创建测试表
        createTestTable();

        System.out.println("--- 方式 1：try-catch-finally（旧方式，繁琐）---");
        oldStyleQuery();

        System.out.println("\n--- 方式 2：try-with-resources（新方式，简洁）---");
        newStyleQuery();

        System.out.println("\n--- 方式 3：多资源管理 ---");
        multiResourceQuery();

        System.out.println("\n=== 总结 ===");
        System.out.println("try-with-resources 优势：");
        System.out.println("  1. 代码更简洁，无需手动调用 close()");
        System.out.println("  2. 资源按相反顺序自动关闭（后开的先关）");
        System.out.println("  3. 即使发生异常，资源也会被关闭");
        System.out.println("  4. 编译器检查，避免忘记关闭资源");
    }

    /**
     * ❌ 旧方式：try-catch-finally
     *
     * <p>缺点：
     * <ul>
     *   <li>代码冗长，容易出错</li>
     *   <li>finally 块中还要处理 close() 的异常</li>
     *   <li>多个资源时代码更复杂</li>
     * </ul>
     */
    private static void oldStyleQuery() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM demo");

            if (rs.next()) {
                System.out.println("记录数: " + rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("查询失败: " + e.getMessage());
            throw e;
        } finally {
            // 必须在 finally 中关闭资源，而且顺序很重要（后开的先关）
            // 每个 close() 还可能抛出异常，需要额外处理！
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("关闭 ResultSet 失败: " + e.getMessage());
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("关闭 Statement 失败: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("关闭 Connection 失败: " + e.getMessage());
                }
            }
        }
    }

    /**
     * ✅ 新方式：try-with-resources
     *
     * <p>优点：
     * <ul>
     *   <li>自动关闭实现了 AutoCloseable 接口的资源</li>
     *   <li>即使发生异常也会关闭资源</li>
     *   <li>代码清晰易读</li>
     * </ul>
     */
    private static void newStyleQuery() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM demo")) {

            if (rs.next()) {
                System.out.println("记录数: " + rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("查询失败: " + e.getMessage());
            throw e;
        }
        // 不需要 finally 块！conn、stmt、rs 会自动关闭
    }

    /**
     * ✅ 多资源管理示例
     *
     * <p>多个资源可以在 try 语句中用分号分隔，
     * 它们会按声明的相反顺序自动关闭。
     */
    private static void multiResourceQuery() throws SQLException {
        // 模拟两个数据库连接（实际场景可能是不同数据库）
        String url1 = DB_URL;
        String url2 = DB_URL;

        try (Connection conn1 = DriverManager.getConnection(url1);
             Connection conn2 = DriverManager.getConnection(url2);
             Statement stmt1 = conn1.createStatement();
             Statement stmt2 = conn2.createStatement()) {

            // 执行查询...
            ResultSet rs1 = stmt1.executeQuery("SELECT '连接1' as source");
            ResultSet rs2 = stmt2.executeQuery("SELECT '连接2' as source");

            if (rs1.next() && rs2.next()) {
                System.out.println("同时管理多个连接和语句");
                System.out.println("  " + rs1.getString("source"));
                System.out.println("  " + rs2.getString("source"));
            }

            // rs1 和 rs2 也应该用 try-with-resources
            // 这里为了演示多资源语法，简化了代码
            rs1.close();
            rs2.close();

        } // 关闭顺序：rs2, rs1, stmt2, stmt1, conn2, conn1
    }

    private static void createTestTable() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS demo (id INTEGER PRIMARY KEY, name TEXT)");
            stmt.execute("INSERT OR IGNORE INTO demo VALUES (1, '测试数据')");
        }
    }
}

/*
 * 关键概念：
 *
 * 1. AutoCloseable 接口
 *    - Connection、Statement、ResultSet 都实现了这个接口
 *    - 自定义资源也可以实现这个接口使用 try-with-resources
 *
 * 2. 资源关闭顺序
 *    - 按声明的相反顺序关闭
 *    - 对于数据库操作：先关 ResultSet，再关 Statement，最后关 Connection
 *
 * 3. 异常抑制
 *    - 如果 try 块和 close() 都抛出异常，close() 的异常会被抑制
 *    - 可以用 e.getSuppressed() 获取被抑制的异常
 */
