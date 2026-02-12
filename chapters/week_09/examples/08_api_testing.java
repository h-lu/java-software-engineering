/*
 * 示例：API 测试——使用 JUnit 和 Java HttpClient 测试 REST API。
 * 本例演示：如何编写 API 集成测试。
 * 运行方式：mvn -q -f chapters/week_09/starter_code/pom.xml test \
 *          -Dtest=examples._08_api_testing
 * 预期输出：所有测试通过
 *
 * 注意：本测试会启动真实的 HTTP 服务（端口 7071，避免与开发服务冲突）
 */
package examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * API 集成测试示例。
 *
 * <p>测试策略：
 * <ul>
 *   <li>单元测试：测试单个类/方法（Week 06）</li>
 *   <li>集成测试：测试多个组件协作（本示例）</li>
 *   <li>E2E 测试：测试完整用户场景（浏览器自动化）</li>
 * </ul>
 *
 * <p>集成测试特点：
 * <ul>
 *   <li>启动真实服务</li>
 *   <li>发送真实 HTTP 请求</li>
 *   <li>验证完整请求-响应链路</li>
 *   <li>比单元测试慢，但覆盖更全面</li>
 * </ul>
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class _08_api_testing {

    private static Javalin app;
    private static HttpClient client;
    private static final String BASE_URL = "http://localhost:7071";
    private static final ObjectMapper mapper = new ObjectMapper();

    // 测试数据
    private static String createdBookId;

    @BeforeAll
    static void setUp() {
        // 创建 HttpClient
        client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

        // 创建并启动测试服务
        BookRepository repository = new TestBookRepository();
        BookController controller = new BookController(repository);

        app = Javalin.create(config -> {
            // 测试环境禁用请求日志，减少输出噪音
        });

        // 路由
        app.get("/health", ctx -> ctx.json(Map.of("status", "UP")));
        app.get("/books", controller::getAllBooks);
        app.get("/books/{id}", controller::getBook);
        app.post("/books", controller::createBook);
        app.put("/books/{id}", controller::updateBook);
        app.delete("/books/{id}", controller::deleteBook);

        // 异常处理
        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getStatusCode()).json(Map.of(
                "code", e.getStatusCode(),
                "message", e.getMessage()
            ));
        });

        app.start(7071);

        // 等待服务启动
        waitForServer();
    }

    @AfterAll
    static void tearDown() {
        if (app != null) {
            app.stop();
        }
    }

    private static void waitForServer() {
        // 简单等待，确保服务启动
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    @Order(1)
    @DisplayName("健康检查端点应该返回 200")
    void healthCheckShouldReturn200() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/health"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("UP"));
    }

    @Test
    @Order(2)
    @DisplayName("获取图书列表应该返回空数组")
    void getAllBooksShouldReturnEmptyList() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("data"));
        assertTrue(response.body().contains("total"));
    }

    @Test
    @Order(3)
    @DisplayName("创建图书应该返回 201 和创建的图书")
    void createBookShouldReturn201() throws Exception {
        String json = """
            {
                "title": "三体",
                "author": "刘慈欣",
                "year": 2006
            }
            """;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("三体"));
        assertTrue(response.body().contains("刘慈欣"));

        // 保存创建的图书 ID 供后续测试使用
        Map<String, Object> created = mapper.readValue(response.body(), Map.class);
        createdBookId = (String) created.get("id");
        assertNotNull(createdBookId);
    }

    @Test
    @Order(4)
    @DisplayName("获取指定图书应该返回图书详情")
    void getBookShouldReturnBookDetails() throws Exception {
        assertNotNull(createdBookId, "前置测试应该已创建图书");

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books/" + createdBookId))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("三体"));
    }

    @Test
    @Order(5)
    @DisplayName("获取不存在的图书应该返回 404")
    void getNonExistentBookShouldReturn404() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books/99999"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("not found") || response.body().contains("Not Found"));
    }

    @Test
    @Order(6)
    @DisplayName("更新图书应该返回更新后的图书")
    void updateBookShouldReturnUpdatedBook() throws Exception {
        assertNotNull(createdBookId);

        String json = """
            {
                "id": "%s",
                "title": "三体（全集）",
                "author": "刘慈欣",
                "year": 2006
            }
            """.formatted(createdBookId);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books/" + createdBookId))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("三体（全集）"));
    }

    @Test
    @Order(7)
    @DisplayName("删除图书应该返回 204")
    void deleteBookShouldReturn204() throws Exception {
        assertNotNull(createdBookId);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books/" + createdBookId))
            .DELETE()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(204, response.statusCode());
    }

    @Test
    @Order(8)
    @DisplayName("删除后再次获取应该返回 404")
    void getDeletedBookShouldReturn404() throws Exception {
        assertNotNull(createdBookId);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books/" + createdBookId))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @Order(9)
    @DisplayName("无效的 JSON 应该返回 400")
    void invalidJsonShouldReturn400() throws Exception {
        String invalidJson = "{invalid json}";

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    @Test
    @Order(10)
    @DisplayName("验证失败的请求应该返回 400")
    void validationFailureShouldReturn400() throws Exception {
        String json = """
            {
                "title": "",
                "author": "刘慈欣",
                "year": 2006
            }
            """;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    @Test
    @Order(11)
    @DisplayName("请求头应该正确传递")
    void requestHeadersShouldBeProcessed() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/health"))
            .header("Accept", "application/json")
            .header("X-Custom-Header", "test-value")
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        // 验证响应头
        assertNotNull(response.headers().firstValue("Content-Type"));
    }

    // ===== 测试辅助类 =====

    static class TestBookRepository implements BookRepository {
        private final Map<String, Book> books = new ConcurrentHashMap<>();
        private int nextId = 1;

        @Override
        public Book save(Book book) {
            if (book.getId() == null) {
                book.setId(String.valueOf(nextId++));
            }
            books.put(book.getId(), book);
            return book;
        }

        @Override
        public Optional<Book> findById(String id) {
            return Optional.ofNullable(books.get(id));
        }

        @Override
        public List<Book> findAll() {
            return List.copyOf(books.values());
        }

        @Override
        public void delete(String id) {
            books.remove(id);
        }
    }
}

// ===== 共享类（简化版） =====

interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(String id);
    List<Book> findAll();
    void delete(String id);
}

class Book {
    private String id;
    private String title;
    private String author;
    private int year;

    public Book() {}

    public Book(String id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}

class ApiException extends RuntimeException {
    private final int statusCode;

    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() { return statusCode; }
}

class BookController {
    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    public void getAllBooks(io.javalin.http.Context ctx) {
        List<Book> books = repository.findAll();
        ctx.json(Map.of("data", books, "total", books.size()));
    }

    public void getBook(io.javalin.http.Context ctx) {
        String id = ctx.pathParam("id");
        Book book = repository.findById(id)
            .orElseThrow(() -> new ApiException(404, "Book not found: " + id));
        ctx.json(book);
    }

    public void createBook(io.javalin.http.Context ctx) {
        Book book = ctx.bodyAsClass(Book.class);

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new ApiException(400, "Title is required");
        }

        Book saved = repository.save(book);
        ctx.status(201).json(saved);
    }

    public void updateBook(io.javalin.http.Context ctx) {
        String id = ctx.pathParam("id");
        Book existing = repository.findById(id)
            .orElseThrow(() -> new ApiException(404, "Book not found: " + id));

        Book updates = ctx.bodyAsClass(Book.class);
        updates.setId(id);
        repository.save(updates);
        ctx.json(updates);
    }

    public void deleteBook(io.javalin.http.Context ctx) {
        String id = ctx.pathParam("id");
        Book existing = repository.findById(id)
            .orElseThrow(() -> new ApiException(404, "Book not found: " + id));
        repository.delete(id);
        ctx.status(204);
    }
}

/*
 * API 测试最佳实践：
 *
 * 1. 测试隔离
 *    - 每个测试使用独立的数据
 *    - 测试顺序控制（@Order）避免相互影响
 *    - 或使用 @BeforeEach 重置数据
 *
 * 2. 端口管理
 *    - 测试使用不同端口（如 7071），避免与开发服务冲突
 *    - 或使用随机端口（Javalin 支持 port(0) 自动分配）
 *
 * 3. 断言策略
 *    - 验证状态码
 *    - 验证响应体关键字段
 *    - 验证响应头（Content-Type 等）
 *
 * 4. 性能考虑
 *    - 集成测试比单元测试慢，不要过多
 *    - 关键路径（CRUD）必须有集成测试
 *    - 复杂业务逻辑优先单元测试
 *
 * 5. 持续集成
 *    - 集成测试需要启动服务，确保 CI 环境支持
 *    - 设置合理的超时时间
 */
