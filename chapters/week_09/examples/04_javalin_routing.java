/*
 * 示例：Javalin 路由详解——路径参数、查询参数、请求处理。
 * 本例演示：Javalin 的路由系统和各种参数处理方式。
 * 运行方式：mvn -q -f chapters/week_09/starter_code/pom.xml compile exec:java \
 *          -Dexec.mainClass="examples._04_javalin_routing"
 * 预期输出：服务启动，演示各种路由和参数处理
 *
 * 测试命令：
 *   curl http://localhost:7070/books
 *   curl http://localhost:7070/books/123
 *   curl "http://localhost:7070/books?author=刘慈欣&year=2020"
 *   curl -X POST http://localhost:7070/books -H "Content-Type: application/json" \
 *        -d '{"title":"三体","author":"刘慈欣"}'
 */
package examples;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.*;

/**
 * Javalin 路由和参数处理完整示例。
 */
public class _04_javalin_routing {

    // 模拟数据存储
    private static final Map<String, Book> books = new HashMap<>();
    private static int nextId = 1;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           Javalin 路由和参数处理示例                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // 预置一些数据
        seedData();

        var app = Javalin.create();

        // ===== 1. 基本路由 =====
        System.out.println("【1. 基本路由】");

        app.get("/", ctx -> ctx.result("图书管理 API 服务"));

        // 健康检查端点（常用）
        app.get("/health", ctx -> {
            ctx.json(Map.of(
                "status", "UP",
                "timestamp", System.currentTimeMillis()
            ));
        });

        // ===== 2. 路径参数（Path Parameters） =====
        System.out.println("【2. 路径参数】");
        System.out.println("   语法: /resource/{id}");
        System.out.println("   示例: /books/123 中的 123");

        // 获取单个资源
        app.get("/books/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Book book = books.get(id);

            if (book == null) {
                ctx.status(404).json(Map.of(
                    "error", "Book not found",
                    "id", id
                ));
                return;
            }

            ctx.json(book);
        });

        // 多个路径参数
        app.get("/authors/{authorId}/books/{bookId}", ctx -> {
            String authorId = ctx.pathParam("authorId");
            String bookId = ctx.pathParam("bookId");

            ctx.json(Map.of(
                "message", "嵌套资源示例",
                "authorId", authorId,
                "bookId", bookId
            ));
        });

        // ===== 3. 查询参数（Query Parameters） =====
        System.out.println("【3. 查询参数】");
        System.out.println("   语法: ?key=value&key2=value2");
        System.out.println("   示例: /books?author=刘慈欣&year=2020");

        // 列表 + 过滤
        app.get("/books", ctx -> {
            // 获取查询参数（可能为 null）
            String author = ctx.queryParam("author");
            String yearStr = ctx.queryParam("year");
            String sort = ctx.queryParam("sort");

            // 过滤逻辑
            List<Book> result = new ArrayList<>(books.values());

            if (author != null) {
                result.removeIf(b -> !b.getAuthor().equals(author));
            }

            if (yearStr != null) {
                try {
                    int year = Integer.parseInt(yearStr);
                    result.removeIf(b -> b.getYear() != year);
                } catch (NumberFormatException e) {
                    ctx.status(400).json(Map.of(
                        "error", "Invalid year parameter",
                        "value", yearStr
                    ));
                    return;
                }
            }

            // 排序
            if ("year".equals(sort)) {
                result.sort(Comparator.comparingInt(Book::getYear));
            } else if ("title".equals(sort)) {
                result.sort(Comparator.comparing(Book::getTitle));
            }

            ctx.json(result);
        });

        // 分页示例
        app.get("/books-paged", ctx -> {
            // 带默认值的查询参数
            int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
            int size = ctx.queryParamAsClass("size", Integer.class).getOrDefault(10);

            // 参数校验
            if (page < 1) page = 1;
            if (size < 1 || size > 100) size = 10;

            List<Book> allBooks = new ArrayList<>(books.values());
            int total = allBooks.size();
            int totalPages = (int) Math.ceil((double) total / size);

            // 分页计算
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, total);

            List<Book> pageData = fromIndex < total
                ? allBooks.subList(fromIndex, toIndex)
                : List.of();

            // 返回分页结果
            ctx.json(Map.of(
                "data", pageData,
                "page", page,
                "size", size,
                "total", total,
                "totalPages", totalPages
            ));
        });

        // ===== 4. 请求体处理 =====
        System.out.println("【4. 请求体处理】");

        // POST 创建资源
        app.post("/books", ctx -> {
            // ctx.bodyAsClass() 自动将 JSON 转换为 Java 对象
            // 需要：1) Jackson 依赖 2) 类有无参构造 3) 字段名匹配
            Book newBook = ctx.bodyAsClass(Book.class);

            // 生成 ID
            String id = String.valueOf(nextId++);
            newBook.setId(id);

            // 保存
            books.put(id, newBook);

            // 返回 201 Created
            ctx.status(201).json(newBook);
        });

        // PUT 全量更新
        app.put("/books/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Book existing = books.get(id);

            if (existing == null) {
                ctx.status(404).json(Map.of("error", "Book not found"));
                return;
            }

            // 全量更新：替换整个对象
            Book updates = ctx.bodyAsClass(Book.class);
            updates.setId(id); // 确保 ID 不变
            books.put(id, updates);

            ctx.json(updates);
        });

        // ===== 5. 请求头处理 =====
        System.out.println("【5. 请求头处理】");

        app.get("/headers-demo", ctx -> {
            // 获取请求头
            String userAgent = ctx.header("User-Agent");
            String accept = ctx.header("Accept");
            String auth = ctx.header("Authorization");

            // 获取所有请求头
            Map<String, String> allHeaders = new HashMap<>();
            ctx.headerMap().forEach((k, v) -> allHeaders.put(k, v.toString()));

            ctx.json(Map.of(
                "userAgent", userAgent != null ? userAgent : "unknown",
                "accept", accept != null ? accept : "unknown",
                "hasAuth", auth != null,
                "allHeaders", allHeaders
            ));
        });

        // ===== 6. 响应控制 =====
        System.out.println("【6. 响应控制】");

        // 不同状态码
        app.get("/status-demo", ctx -> {
            String type = ctx.queryParam("type");

            switch (type != null ? type : "") {
                case "200" -> ctx.status(200).result("OK");
                case "201" -> ctx.status(201).result("Created");
                case "204" -> ctx.status(204); // No Content
                case "400" -> ctx.status(400).json(Map.of("error", "Bad Request"));
                case "401" -> ctx.status(401).json(Map.of("error", "Unauthorized"));
                case "403" -> ctx.status(403).json(Map.of("error", "Forbidden"));
                case "404" -> ctx.status(404).json(Map.of("error", "Not Found"));
                case "500" -> ctx.status(500).json(Map.of("error", "Internal Error"));
                default -> ctx.status(200).json(Map.of(
                    "message", "Try ?type=404",
                    "validTypes", List.of("200", "201", "204", "400", "401", "403", "404", "500")
                ));
            }
        });

        // 内容协商
        app.get("/content-demo", ctx -> {
            String accept = ctx.header("Accept");

            if (accept != null && accept.contains("application/json")) {
                ctx.contentType("application/json");
                ctx.json(Map.of("message", "JSON response"));
            } else if (accept != null && accept.contains("text/html")) {
                ctx.contentType("text/html");
                ctx.result("<h1>HTML Response</h1>");
            } else {
                ctx.contentType("text/plain");
                ctx.result("Plain text response");
            }
        });

        // ===== 7. 路由组（使用 path） =====
        System.out.println("【7. 路由组】");

        // API 版本控制
        app.path("/api/v1", v1 -> {
            v1.get("/status", ctx -> ctx.json(Map.of("version", "v1", "status", "stable")));
            v1.get("/books", ctx -> ctx.result("v1 图书列表"));
        });

        app.path("/api/v2", v2 -> {
            v2.get("/status", ctx -> ctx.json(Map.of("version", "v2", "status", "beta")));
            v2.get("/books", ctx -> ctx.result("v2 图书列表（更多字段）"));
        });

        // 启动服务
        app.start(7070);

        System.out.println();
        System.out.println("✅ 服务已启动在 http://localhost:7070");
        System.out.println();
        System.out.println("测试命令：");
        System.out.println("  # 基础路由");
        System.out.println("  curl http://localhost:7070/");
        System.out.println("  curl http://localhost:7070/health");
        System.out.println();
        System.out.println("  # 路径参数");
        System.out.println("  curl http://localhost:7070/books/1");
        System.out.println("  curl http://localhost:7070/authors/101/books/1");
        System.out.println();
        System.out.println("  # 查询参数");
        System.out.println("  curl 'http://localhost:7070/books?author=刘慈欣'");
        System.out.println("  curl 'http://localhost:7070/books?year=2006&sort=title'");
        System.out.println("  curl 'http://localhost:7070/books-paged?page=1&size=5'");
        System.out.println();
        System.out.println("  # 请求体");
        System.out.println("  curl -X POST http://localhost:7070/books \\");
        System.out.println("    -H 'Content-Type: application/json' \\");
        System.out.println("    -d '{\"title\":\"球状闪电\",\"author\":\"刘慈欣\",\"year\":2004}'");
        System.out.println();
        System.out.println("  # 状态码演示");
        System.out.println("  curl 'http://localhost:7070/status-demo?type=404'");
        System.out.println();
        System.out.println("按 Ctrl+C 停止服务");
    }

    // 预置数据
    private static void seedData() {
        Book book1 = new Book(String.valueOf(nextId++), "三体", "刘慈欣", 2006);
        Book book2 = new Book(String.valueOf(nextId++), "流浪地球", "刘慈欣", 2000);
        Book book3 = new Book(String.valueOf(nextId++), "球状闪电", "刘慈欣", 2004);
        Book book4 = new Book(String.valueOf(nextId++), "Java核心技术", "Cay Horstmann", 2020);
        Book book5 = new Book(String.valueOf(nextId++), "Effective Java", "Joshua Bloch", 2018);

        books.put(book1.getId(), book1);
        books.put(book2.getId(), book2);
        books.put(book3.getId(), book3);
        books.put(book4.getId(), book4);
        books.put(book5.getId(), book5);
    }
}

/**
 * 图书实体类（用于 JSON 序列化/反序列化）。
 */
class Book {
    private String id;
    private String title;
    private String author;
    private int year;

    // 必须有无参构造（Jackson 需要）
    public Book() {}

    public Book(String id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Getters 和 Setters（Jackson 需要）
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    @Override
    public String toString() {
        return "Book{id='" + id + "', title='" + title + "', author='" + author + "', year=" + year + "}";
    }
}
