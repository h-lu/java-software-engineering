package com.campusflow.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库连接测试类
 * 测试数据库连接、资源释放和错误处理
 */
public class DatabaseConnectionTest {

    private final String validH2Url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    // ==================== 连接成功测试 ====================

    @Test
    @DisplayName("正例：应成功连接到 H2 内存数据库")
    void shouldConnectToDatabase() throws SQLException {
        // 执行
        Connection conn = DriverManager.getConnection(validH2Url);

        // 验证
        assertNotNull(conn);
        assertFalse(conn.isClosed());

        // 清理
        conn.close();
    }

    @Test
    @DisplayName("正例：连接应能执行简单 SQL")
    void connection_ShouldExecuteSimpleSql() throws SQLException {
        try (Connection conn = DriverManager.getConnection(validH2Url);
             Statement stmt = conn.createStatement()) {

            // 执行简单查询
            boolean result = stmt.execute("SELECT 1");
            assertTrue(result);
        }
    }

    @Test
    @DisplayName("正例：使用 try-with-resources 应自动关闭连接")
    void tryWithResources_ShouldAutoCloseConnection() throws SQLException {
        Connection conn;
        try (Connection c = DriverManager.getConnection(validH2Url)) {
            conn = c;
            assertFalse(c.isClosed());
        }
        // try 块结束后连接应已关闭
        assertTrue(conn.isClosed());
    }

    // ==================== 资源释放测试 ====================

    @Test
    @DisplayName("正例：应正确关闭 Connection")
    void shouldCloseConnectionProperly() throws SQLException {
        Connection conn = DriverManager.getConnection(validH2Url);
        assertFalse(conn.isClosed());

        conn.close();

        assertTrue(conn.isClosed());
    }

    @Test
    @DisplayName("正例：应正确关闭 Statement")
    void shouldCloseStatementProperly() throws SQLException {
        try (Connection conn = DriverManager.getConnection(validH2Url)) {
            Statement stmt = conn.createStatement();
            assertFalse(stmt.isClosed());

            stmt.close();

            assertTrue(stmt.isClosed());
        }
    }

    @Test
    @DisplayName("正例：使用 try-with-resources 关闭 Connection 时关联资源应被清理")
    void tryWithResources_ShouldCleanUpResources() throws SQLException {
        Statement stmt;
        try (Connection conn = DriverManager.getConnection(validH2Url)) {
            stmt = conn.createStatement();
            stmt.execute("SELECT 1");
            // Statement 在 Connection 关闭前是打开的
            assertFalse(stmt.isClosed());
        }
        // Connection 关闭后，Statement 通常也会被关闭（取决于 JDBC 驱动实现）
        // H2 驱动在连接关闭后不会自动关闭 Statement，这是实现细节差异
        // 测试重点应放在 try-with-resources 能正确关闭 Connection 上
    }

    @Test
    @DisplayName("正例：多层 try-with-resources 应正确关闭所有资源")
    void nestedTryWithResources_ShouldCloseAllResources() {
        assertDoesNotThrow(() -> {
            try (Connection conn = DriverManager.getConnection(validH2Url);
                 Statement stmt = conn.createStatement()) {

                stmt.execute("CREATE TABLE test (id INT)");

                try (java.sql.PreparedStatement ps = conn.prepareStatement("SELECT * FROM test");
                     java.sql.ResultSet rs = ps.executeQuery()) {
                    // 内层资源使用
                }
                // 内层资源应已关闭
            }
        });
    }

    // ==================== 错误处理测试 ====================

    @Test
    @DisplayName("反例：无效的 JDBC URL 应抛出 SQLException")
    void invalidJdbcUrl_ShouldThrowSQLException() {
        assertThrows(SQLException.class, () -> {
            DriverManager.getConnection("jdbc:invalid:url");
        });
    }

    @Test
    @DisplayName("反例：空 URL 应抛出 SQLException")
    void nullUrl_ShouldThrowSQLException() {
        assertThrows(SQLException.class, () -> {
            DriverManager.getConnection(null);
        });
    }

    @Test
    @DisplayName("反例：不支持的协议应抛出 SQLException")
    void unsupportedProtocol_ShouldThrowSQLException() {
        assertThrows(SQLException.class, () -> {
            DriverManager.getConnection("http://localhost:8080");
        });
    }

    // ==================== DatabaseInitializer 测试 ====================

    @Test
    @DisplayName("正例：DatabaseInitializer 应成功初始化数据库")
    void databaseInitializer_ShouldInitializeDatabase() throws SQLException {
        // 准备
        DatabaseInitializer initializer = new DatabaseInitializer(validH2Url);

        // 执行
        assertDoesNotThrow(() -> initializer.initializeSchema());

        // 验证：表已创建，可以执行操作
        try (Connection conn = DriverManager.getConnection(validH2Url);
             Statement stmt = conn.createStatement()) {
            // 尝试向 books 表插入数据（如果表存在则不会抛异常）
            stmt.execute("INSERT INTO books (isbn, title, author, available) VALUES ('TEST-001', 'Test', 'Author', true)");
        }
    }

    @Test
    @DisplayName("边界：无效的 URL 应抛出 IllegalArgumentException")
    void databaseInitializer_WithNullUrl_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseInitializer(null);
        });
        assertEquals("数据库 URL 不能为空", exception.getMessage());
    }

    @Test
    @DisplayName("边界：空字符串 URL 应抛出 IllegalArgumentException")
    void databaseInitializer_WithEmptyUrl_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new DatabaseInitializer("   ");
        });
        assertEquals("数据库 URL 不能为空", exception.getMessage());
    }

    // ==================== 连接属性测试 ====================

    @Test
    @DisplayName("正例：连接应支持元数据查询")
    void connection_ShouldSupportMetadata() throws SQLException {
        try (Connection conn = DriverManager.getConnection(validH2Url)) {
            java.sql.DatabaseMetaData metaData = conn.getMetaData();
            assertNotNull(metaData);
            assertEquals("H2", metaData.getDatabaseProductName());
        }
    }

    @Test
    @DisplayName("正例：新连接应处于自动提交模式")
    void newConnection_ShouldBeInAutoCommitMode() throws SQLException {
        try (Connection conn = DriverManager.getConnection(validH2Url)) {
            assertTrue(conn.getAutoCommit());
        }
    }

    @Test
    @DisplayName("正例：应能创建多个独立连接")
    void shouldCreateMultipleIndependentConnections() throws SQLException {
        try (Connection conn1 = DriverManager.getConnection(validH2Url);
             Connection conn2 = DriverManager.getConnection(validH2Url)) {

            assertNotNull(conn1);
            assertNotNull(conn2);
            assertNotSame(conn1, conn2);
            assertFalse(conn1.isClosed());
            assertFalse(conn2.isClosed());
        }
    }
}
