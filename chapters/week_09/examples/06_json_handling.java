/*
 * 示例：JSON 处理——序列化与反序列化。
 * 本例演示：使用 Jackson 进行 Java 对象与 JSON 的相互转换。
 * 运行方式：mvn -q -f chapters/week_09/starter_code/pom.xml test \
 *          -Dtest=examples._06_json_handling
 * 预期输出：所有测试通过，展示 JSON 处理的各种场景
 *
 * 注意：本示例需要以下依赖：
 *   - com.fasterxml.jackson.core:jackson-databind:2.17.0
 *   - com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0 (Java 8 日期时间)
 */
package examples;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSON 序列化与反序列化完整示例。
 *
 * <p>什么是序列化？
 * <ul>
 *   <li>序列化：Java 对象 → JSON 字符串</li>
 *   <li>反序列化：JSON 字符串 → Java 对象</li>
 * </ul>
 *
 * <p>Jackson 是 Java 世界最流行的 JSON 库，Spring Boot 和 Javalin 都默认使用它。
 */
public class _06_json_handling {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        // 创建 ObjectMapper 实例
        mapper = new ObjectMapper();

        // 配置：美化输出（开发调试时有用）
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // 配置：禁用日期时间戳格式，使用 ISO-8601 字符串
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 注册 Java 8 日期时间模块
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeSimpleObject() throws JsonProcessingException {
        // 准备
        Book book = new Book("1", "三体", "刘慈欣", 2006);

        // 执行：序列化
        String json = mapper.writeValueAsString(book);

        // 验证
        System.out.println("序列化后的 JSON：");
        System.out.println(json);

        assertTrue(json.contains("\"id\" : \"1\""));
        assertTrue(json.contains("\"title\" : \"三体\""));
        assertTrue(json.contains("\"author\" : \"刘慈欣\""));
        assertTrue(json.contains("\"year\" : 2006"));
    }

    @Test
    void shouldDeserializeSimpleObject() throws JsonProcessingException {
        // 准备：JSON 字符串
        String json = """
            {
                "id": "1",
                "title": "流浪地球",
                "author": "刘慈欣",
                "year": 2000
            }
            """;

        // 执行：反序列化
        Book book = mapper.readValue(json, Book.class);

        // 验证
        assertEquals("1", book.getId());
        assertEquals("流浪地球", book.getTitle());
        assertEquals("刘慈欣", book.getAuthor());
        assertEquals(2000, book.getYear());
    }

    @Test
    void shouldHandleNestedObject() throws JsonProcessingException {
        // 准备：嵌套对象
        Author author = new Author("刘慈欣", "科幻作家", 1963);
        BookWithAuthor book = new BookWithAuthor("1", "三体", author, 2006);

        // 序列化
        String json = mapper.writeValueAsString(book);
        System.out.println("嵌套对象序列化：");
        System.out.println(json);

        // 反序列化
        BookWithAuthor parsed = mapper.readValue(json, BookWithAuthor.class);

        // 验证
        assertEquals("刘慈欣", parsed.getAuthor().getName());
        assertEquals("科幻作家", parsed.getAuthor().getBio());
    }

    @Test
    void shouldHandleCollections() throws JsonProcessingException {
        // 准备：列表
        List<Book> books = List.of(
            new Book("1", "三体", "刘慈欣", 2006),
            new Book("2", "流浪地球", "刘慈欣", 2000),
            new Book("3", "Java核心技术", "Cay Horstmann", 2020)
        );

        // 序列化列表
        String json = mapper.writeValueAsString(books);
        System.out.println("列表序列化：");
        System.out.println(json);

        // 反序列化列表（需要 TypeReference）
        List<Book> parsed = mapper.readValue(json,
            mapper.getTypeFactory().constructCollectionType(List.class, Book.class));

        // 验证
        assertEquals(3, parsed.size());
        assertEquals("三体", parsed.get(0).getTitle());
    }

    @Test
    void shouldHandleLocalDate() throws JsonProcessingException {
        // 准备：带日期的对象
        Task task = new Task("task-001", "完成 Week 09 作业", LocalDate.of(2026, 2, 20));

        // 序列化
        String json = mapper.writeValueAsString(task);
        System.out.println("日期序列化：");
        System.out.println(json);

        // 验证：日期格式为 ISO-8601（2026-02-20）
        assertTrue(json.contains("2026-02-20"));

        // 反序列化
        Task parsed = mapper.readValue(json, Task.class);
        assertEquals(LocalDate.of(2026, 2, 20), parsed.getDueDate());
    }

    @Test
    void shouldHandleMap() throws JsonProcessingException {
        // 准备：Map
        Map<String, Object> data = Map.of(
            "total", 100,
            "page", 1,
            "books", List.of(
                Map.of("id", "1", "title", "三体"),
                Map.of("id", "2", "title", "流浪地球")
            )
        );

        // 序列化
        String json = mapper.writeValueAsString(data);
        System.out.println("Map 序列化：");
        System.out.println(json);

        // 反序列化（Map 类型）
        Map<String, Object> parsed = mapper.readValue(json, Map.class);

        // 验证
        assertEquals(100, parsed.get("total"));
        assertEquals(1, parsed.get("page"));
        assertTrue(parsed.get("books") instanceof List);
    }

    @Test
    void shouldHandlePartialData() throws JsonProcessingException {
        // 场景：JSON 只包含部分字段（如 PATCH 请求）
        String partialJson = """
            {
                "title": "三体（修订版）"
            }
            """;

        // 反序列化到现有对象
        Book existing = new Book("1", "三体", "刘慈欣", 2006);

        // 使用 readerForUpdating 合并数据
        Book updated = mapper.readerForUpdating(existing).readValue(partialJson);

        // 验证：只有 title 被更新
        assertEquals("三体（修订版）", updated.getTitle());
        assertEquals("刘慈欣", updated.getAuthor()); // 未改变
        assertEquals(2006, updated.getYear());       // 未改变
    }

    @Test
    void shouldHandleUnknownProperties() throws JsonProcessingException {
        // 场景：JSON 包含 Java 对象中没有的字段
        String jsonWithExtra = """
            {
                "id": "1",
                "title": "三体",
                "author": "刘慈欣",
                "year": 2006,
                "publisher": "重庆出版社",
                "isbn": "9787536692930"
            }
            """;

        // 默认行为：抛出异常
        // 配置：忽略未知字段
        mapper.configure(
            com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        );

        // 反序列化成功
        Book book = mapper.readValue(jsonWithExtra, Book.class);
        assertEquals("三体", book.getTitle());
        // publisher 和 isbn 被忽略
    }

    @Test
    void shouldHandleNullValues() throws JsonProcessingException {
        // 场景：null 值处理
        Book book = new Book("1", null, "刘慈欣", 2006);

        // 默认：包含 null 字段
        String json = mapper.writeValueAsString(book);
        System.out.println("包含 null 的 JSON：" + json);
        assertTrue(json.contains("\"title\" : null"));

        // 配置：忽略 null 字段
        mapper.setSerializationInclusion(
            com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
        );
        String jsonWithoutNull = mapper.writeValueAsString(book);
        System.out.println("忽略 null 的 JSON：" + jsonWithoutNull);
        assertFalse(jsonWithoutNull.contains("title"));
    }

    @Test
    void shouldFailOnInvalidJson() {
        // 场景：无效的 JSON
        String invalidJson = """
            {
                "id": "1",
                "title": "三体",
                "year": "不是数字"
            }
            """;

        // 反序列化应该失败
        assertThrows(JsonProcessingException.class, () -> {
            mapper.readValue(invalidJson, Book.class);
        });
    }

    @Test
    void shouldFailOnMissingRequiredField() {
        // 场景：缺少必填字段（需要 @JsonProperty(required = true)）
        String incompleteJson = """
            {
                "id": "1",
                "author": "刘慈欣"
            }
            """;

        // 默认情况下，Jackson 不会验证必填字段
        // 反序列化成功，title 为 null
        assertDoesNotThrow(() -> {
            Book book = mapper.readValue(incompleteJson, Book.class);
            assertNull(book.getTitle());
        });
    }
}

// ===== 实体类 =====

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

class Author {
    private String name;
    private String bio;
    private int birthYear;

    public Author() {}

    public Author(String name, String bio, int birthYear) {
        this.name = name;
        this.bio = bio;
        this.birthYear = birthYear;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public int getBirthYear() { return birthYear; }
    public void setBirthYear(int birthYear) { this.birthYear = birthYear; }
}

class BookWithAuthor {
    private String id;
    private String title;
    private Author author;
    private int year;

    public BookWithAuthor() {}

    public BookWithAuthor(String id, String title, Author author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}

class Task {
    private String id;
    private String title;
    private LocalDate dueDate;
    private LocalDateTime createdAt;

    public Task() {
        this.createdAt = LocalDateTime.now();
    }

    public Task(String id, String title, LocalDate dueDate) {
        this();
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

/*
 * JSON 处理最佳实践：
 *
 * 1. 实体类设计
 *    - 提供无参构造方法（Jackson 需要）
 *    - 提供 Getter 和 Setter（或使用 public 字段）
 *    - 字段名与 JSON 键名保持一致（或使用 @JsonProperty）
 *
 * 2. ObjectMapper 配置
 *    - 重用 ObjectMapper 实例（线程安全）
 *    - 配置日期格式（ISO-8601 推荐）
 *    - 生产环境禁用 INDENT_OUTPUT（节省带宽）
 *
 * 3. 异常处理
 *    - 捕获 JsonProcessingException
 *    - 给用户友好的错误提示（不要暴露堆栈）
 *
 * 4. 安全性
 *    - 忽略未知字段（防止新字段导致失败）
 *    - 验证必填字段（在业务层，而非反序列化层）
 */
