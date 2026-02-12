/*
 * 示例：使用 H2 内存数据库进行单元测试
 * 运行方式：mvn -q -f chapters/week_07/starter_code/pom.xml test \
 *          -Dtest=examples._06_h2_testing
 * 预期输出：
 *   测试通过（所有断言成功）
 */
package examples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 演示使用 H2 内存数据库进行 Repository 单元测试
 *
 * <p>H2 内存数据库特点：
 * <ul>
 *   <li>测试运行快（无磁盘 I/O）</li>
 *   <li>测试隔离（每个测试独立数据库）</li>
 *   <li>兼容 JDBC 和标准 SQL</li>
 *   <li>无需额外安装</li>
 * </ul>
 *
 * <p>需要添加依赖（test scope）：
 * <pre>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;com.h2database&lt;/groupId&gt;
 *     &lt;artifactId&gt;h2&lt;/artifactId&gt;
 *     &lt;version&gt;2.2.224&lt;/version&gt;
 *     &lt;scope&gt;test&lt;/scope&gt;
 * &lt;/dependency&gt;
 * &lt;/pre>
 */
public class _06_h2_testing {

    // H2 内存数据库 URL
    // DB_CLOSE_DELAY=-1 表示连接关闭后不立即删除数据库
    private static final String TEST_DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    private ProductRepository repository;

    /**
     * 每个测试前初始化数据库
     *
     * <p>@BeforeEach 确保测试隔离性
     */
    @BeforeEach
    void setUp() {
        // 创建表结构
        createTestSchema();

        // 初始化 Repository
        repository = new ProductRepository(TEST_DB_URL);
    }

    @Test
    void shouldSaveAndFindProduct() {
        // 准备
        Product product = new Product("P001", "测试产品", 99.99);

        // 执行
        repository.save(product);

        // 验证
        Optional<Product> found = repository.findById("P001");
        assertTrue(found.isPresent(), "应该能找到产品");
        assertEquals("测试产品", found.get().name(), "产品名称应该匹配");
        assertEquals(99.99, found.get().price(), 0.001, "产品价格应该匹配");
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        Optional<Product> found = repository.findById("不存在的ID");
        assertFalse(found.isPresent(), "不应该找到不存在的产品");
    }

    @Test
    void shouldUpdateProduct() {
        // 先保存
        Product product = new Product("P002", "原名称", 50.0);
        repository.save(product);

        // 更新
        Product updated = new Product("P002", "新名称", 100.0);
        repository.update(updated);

        // 验证
        Optional<Product> found = repository.findById("P002");
        assertTrue(found.isPresent());
        assertEquals("新名称", found.get().name());
        assertEquals(100.0, found.get().price(), 0.001);
    }

    @Test
    void shouldDeleteProduct() {
        // 先保存
        Product product = new Product("P003", "待删除", 10.0);
        repository.save(product);
        assertTrue(repository.findById("P003").isPresent());

        // 删除
        repository.delete("P003");

        // 验证
        assertFalse(repository.findById("P003").isPresent());
    }

    @Test
    void shouldFindAllProducts() {
        // 保存多个产品
        repository.save(new Product("A001", "产品1", 10.0));
        repository.save(new Product("A002", "产品2", 20.0));
        repository.save(new Product("A003", "产品3", 30.0));

        // 查询所有
        List<Product> products = repository.findAll();

        // 验证
        assertEquals(3, products.size(), "应该返回 3 个产品");
    }

    @Test
    void shouldThrowExceptionWhenSavingNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        }, "保存 null 应该抛出异常");
    }

    @Test
    void shouldThrowExceptionWhenSavingProductWithNullId() {
        Product product = new Product(null, "测试", 10.0);
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(product);
        }, "保存 ID 为 null 的产品应该抛出异常");
    }

    // ============ 辅助方法 ============

    private void createTestSchema() {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id VARCHAR(50) PRIMARY KEY,
                    name VARCHAR(200) NOT NULL,
                    price DECIMAL(10, 2) NOT NULL
                )
                """);

        } catch (SQLException e) {
            throw new RuntimeException("创建测试表失败", e);
        }
    }
}

/**
 * 产品实体（Record 类型，Java 16+）
 */
record Product(String id, String name, double price) {}

/**
 * 产品 Repository（H2/SQLite 兼容）
 */
class ProductRepository {
    private final String url;

    public ProductRepository(String url) {
        this.url = url;
    }

    public void save(Product product) {
        if (product == null || product.id() == null) {
            throw new IllegalArgumentException("Product 和 ID 不能为空");
        }

        String sql = "INSERT INTO products (id, name, price) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.id());
            pstmt.setString(2, product.name());
            pstmt.setDouble(3, product.price());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("保存产品失败: " + e.getMessage(), e);
        }
    }

    public Optional<Product> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询产品失败: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("查询产品列表失败: " + e.getMessage(), e);
        }

        return products;
    }

    public void update(Product product) {
        if (product == null || product.id() == null) {
            throw new IllegalArgumentException("Product 和 ID 不能为空");
        }

        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.name());
            pstmt.setDouble(2, product.price());
            pstmt.setString(3, product.id());

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                throw new IllegalArgumentException("产品不存在: " + product.id());
            }

        } catch (SQLException e) {
            throw new RuntimeException("更新产品失败: " + e.getMessage(), e);
        }
    }

    public void delete(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID 不能为空");
        }

        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("删除产品失败: " + e.getMessage(), e);
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
            rs.getString("id"),
            rs.getString("name"),
            rs.getDouble("price")
        );
    }
}

/*
 * 测试策略说明：
 *
 * 1. 测试隔离
 *    - @BeforeEach 创建新的内存数据库
 *    - 每个测试独立，互不影响
 *
 * 2. 测试数据准备
 *    - 在 @BeforeEach 中创建表结构
 *    - 每个测试自己准备所需数据
 *
 * 3. 测试执行速度
 *    - 内存操作比磁盘快几个数量级
 *    - 适合频繁运行的单元测试
 *
 * 4. 生产环境兼容性
 *    - H2 兼容大部分标准 SQL
 *    - 但不同数据库有差异（如数据类型、函数）
 *    - CI 阶段应补充真实数据库的集成测试
 *
 * 5. 测试金字塔
 *    - 单元测试（H2）：多而快，验证业务逻辑
 *    - 集成测试（SQLite/PostgreSQL）：少而全，验证兼容性
 */
