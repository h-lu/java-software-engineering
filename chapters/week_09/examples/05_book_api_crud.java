/*
 * 示例：完整的图书管理 REST API（CRUD）。
 * 本例演示：使用 Javalin 实现完整的图书管理 API，包括所有 CRUD 操作。
 * 运行方式：mvn -q -f chapters/week_09/starter_code/pom.xml compile exec:java \
 *          -Dexec.mainClass="examples._05_book_api_crud"
 * 预期输出：服务启动，提供完整的图书管理 REST API
 *
 * 测试命令：
 *   # 获取所有图书
 *   curl http://localhost:7070/books
 *
 *   # 创建图书
 *   curl -X POST http://localhost:7070/books \
 *     -H "Content-Type: application/json" \
 *     -d '{"title":"三体","author":"刘慈欣","year":2006}'
 *
 *   # 获取指定图书
 *   curl http://localhost:7070/books/1
 *
 *   # 更新图书
 *   curl -X PUT http://localhost:7070/books/1 \
 *     -H "Content-Type: application/json" \
 *     -d '{"id":"1","title":"三体（全集）","author":"刘慈欣","year":2006}'
 *
 *   # 删除图书
 *   curl -X DELETE http://localhost:7070/books/1
 */
package examples;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 完整的图书管理 REST API 示例。
 *
 * <p>架构分层：
 * <ul>
 *   <li>Controller: 处理 HTTP 请求/响应</li>
 *   <li>Service: 业务逻辑（本示例简化，Controller 直接调用 Repository）</li>
 *   <li>Repository: 数据访问</li>
 *   <li>Entity: 领域模型</li>
 * </ul>
 */
public class _05_book_api_crud {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           图书管理 REST API（完整 CRUD）                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // 初始化组件
        BookRepository repository = new InMemoryBookRepository();
        BookController controller = new BookController(repository);

        // 预置数据
        seedData(repository);

        // 创建 Javalin 应用
        var app = Javalin.create(config -> {
            // 启用请求日志
            config.bundledPlugins.enableDevLogging();
        });

        // ===== 路由定义 =====

        // 健康检查
        app.get("/health", ctx -> ctx.json(Map.of(
            "status", "UP",
            "service", "book-api",
            "version", "1.0.0"
        )));

        // CRUD 路由
        app.get("/books", controller::getAllBooks);           // 列表（支持过滤）
        app.get("/books/{id}", controller::getBook);          // 详情
        app.post("/books", controller::createBook);           // 创建
        app.put("/books/{id}", controller::updateBook);       // 全量更新
        app.patch("/books/{id}", controller::patchBook);      // 部分更新
        app.delete("/books/{id}", controller::deleteBook);    // 删除

        // 搜索端点
        app.get("/books/search", controller::searchBooks);

        // 启动服务
        app.start(7070);

        System.out.println("✅ 图书管理 API 已启动！");
        System.out.println();
        System.out.println("API 端点：");
        System.out.println("  GET    /health         - 健康检查");
        System.out.println("  GET    /books          - 获取所有图书（支持 ?author= &year=）");
        System.out.println("  GET    /books/{id}     - 获取指定图书");
        System.out.println("  POST   /books          - 创建图书");
        System.out.println("  PUT    /books/{id}     - 全量更新");
        System.out.println("  PATCH  /books/{id}     - 部分更新");
        System.out.println("  DELETE /books/{id}     - 删除图书");
        System.out.println("  GET    /books/search   - 搜索（?keyword=）");
        System.out.println();
        System.out.println("测试命令示例：");
        System.out.println("  curl http://localhost:7070/books");
        System.out.println("  curl -X POST http://localhost:7070/books \\");
        System.out.println("    -H 'Content-Type: application/json' \\");
        System.out.println("    -d '{\"title\":\"三体\",\"author\":\"刘慈欣\",\"year\":2006}'");
        System.out.println();
        System.out.println("服务地址: http://localhost:7070");
        System.out.println("按 Ctrl+C 停止服务");
    }

    private static void seedData(BookRepository repository) {
        repository.save(new Book(null, "三体", "刘慈欣", 2006));
        repository.save(new Book(null, "流浪地球", "刘慈欣", 2000));
        repository.save(new Book(null, "球状闪电", "刘慈欣", 2004));
        repository.save(new Book(null, "Java核心技术", "Cay Horstmann", 2020));
        repository.save(new Book(null, "Effective Java", "Joshua Bloch", 2018));
    }
}

/**
 * Book 实体类。
 */
class Book {
    private String id;
    private String title;
    private String author;
    private int year;

    // 无参构造（Jackson 反序列化需要）
    public Book() {}

    public Book(String id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Getters 和 Setters
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
        return String.format("Book[id=%s, title=%s, author=%s, year=%d]", id, title, author, year);
    }
}

/**
 * Repository 接口（Week 07 模式复用）。
 */
interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(String id);
    List<Book> findAll();
    List<Book> findByAuthor(String author);
    List<Book> findByYear(int year);
    void delete(String id);
    long count();
}

/**
 * 内存版 Repository 实现（使用 ConcurrentHashMap 保证线程安全）。
 */
class InMemoryBookRepository implements BookRepository {
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
        return new ArrayList<>(books.values());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return books.values().stream()
            .filter(b -> b.getAuthor().equals(author))
            .toList();
    }

    @Override
    public List<Book> findByYear(int year) {
        return books.values().stream()
            .filter(b -> b.getYear() == year)
            .toList();
    }

    @Override
    public void delete(String id) {
        books.remove(id);
    }

    @Override
    public long count() {
        return books.size();
    }
}

/**
 * Controller 层：处理 HTTP 请求/响应。
 */
class BookController {
    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    /**
     * GET /books - 获取所有图书（支持过滤）。
     */
    public void getAllBooks(Context ctx) {
        String author = ctx.queryParam("author");
        String yearStr = ctx.queryParam("year");

        List<Book> books;

        if (author != null) {
            // 按作者过滤
            books = repository.findByAuthor(author);
        } else if (yearStr != null) {
            // 按年份过滤
            try {
                int year = Integer.parseInt(yearStr);
                books = repository.findByYear(year);
            } catch (NumberFormatException e) {
                ctx.status(400).json(Map.of(
                    "error", "Invalid year parameter",
                    "message", "Year must be a number"
                ));
                return;
            }
        } else {
            // 获取全部
            books = repository.findAll();
        }

        ctx.json(Map.of(
            "data", books,
            "total", books.size()
        ));
    }

    /**
     * GET /books/{id} - 获取指定图书。
     */
    public void getBook(Context ctx) {
        String id = ctx.pathParam("id");

        Optional<Book> book = repository.findById(id);

        if (book.isPresent()) {
            ctx.json(book.get());
        } else {
            ctx.status(404).json(Map.of(
                "error", "Book not found",
                "id", id
            ));
        }
    }

    /**
     * POST /books - 创建新图书。
     */
    public void createBook(Context ctx) {
        try {
            Book book = ctx.bodyAsClass(Book.class);

            // 验证必填字段
            if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "error", "Validation failed",
                    "message", "Title is required"
                ));
                return;
            }

            if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
                ctx.status(400).json(Map.of(
                    "error", "Validation failed",
                    "message", "Author is required"
                ));
                return;
            }

            Book saved = repository.save(book);

            // 201 Created
            ctx.status(201)
               .header("Location", "/books/" + saved.getId())
               .json(saved);

        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                "error", "Invalid request body",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * PUT /books/{id} - 全量更新图书。
     */
    public void updateBook(Context ctx) {
        String id = ctx.pathParam("id");

        Optional<Book> existing = repository.findById(id);
        if (existing.isEmpty()) {
            ctx.status(404).json(Map.of(
                "error", "Book not found",
                "id", id
            ));
            return;
        }

        try {
            Book updates = ctx.bodyAsClass(Book.class);

            // 全量更新：替换所有字段
            updates.setId(id); // 确保 ID 不变
            Book saved = repository.save(updates);

            ctx.json(saved);

        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                "error", "Invalid request body",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * PATCH /books/{id} - 部分更新图书。
     */
    public void patchBook(Context ctx) {
        String id = ctx.pathParam("id");

        Optional<Book> existingOpt = repository.findById(id);
        if (existingOpt.isEmpty()) {
            ctx.status(404).json(Map.of(
                "error", "Book not found",
                "id", id
            ));
            return;
        }

        try {
            // 使用 Map 接收部分字段
            Map<String, Object> updates = ctx.bodyAsClass(Map.class);
            Book existing = existingOpt.get();

            // 只更新提供的字段
            if (updates.containsKey("title")) {
                existing.setTitle((String) updates.get("title"));
            }
            if (updates.containsKey("author")) {
                existing.setAuthor((String) updates.get("author"));
            }
            if (updates.containsKey("year")) {
                // year 可能是 Integer 或 Long
                Object yearObj = updates.get("year");
                if (yearObj instanceof Number) {
                    existing.setYear(((Number) yearObj).intValue());
                }
            }

            Book saved = repository.save(existing);
            ctx.json(saved);

        } catch (Exception e) {
            ctx.status(400).json(Map.of(
                "error", "Invalid request body",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * DELETE /books/{id} - 删除图书。
     */
    public void deleteBook(Context ctx) {
        String id = ctx.pathParam("id");

        Optional<Book> existing = repository.findById(id);
        if (existing.isEmpty()) {
            ctx.status(404).json(Map.of(
                "error", "Book not found",
                "id", id
            ));
            return;
        }

        repository.delete(id);

        // 204 No Content（删除成功，无需返回内容）
        ctx.status(204);
    }

    /**
     * GET /books/search?keyword=xxx - 搜索图书。
     */
    public void searchBooks(Context ctx) {
        String keyword = ctx.queryParam("keyword");

        if (keyword == null || keyword.trim().isEmpty()) {
            ctx.status(400).json(Map.of(
                "error", "Missing parameter",
                "message", "keyword is required"
            ));
            return;
        }

        String lowerKeyword = keyword.toLowerCase();
        List<Book> results = repository.findAll().stream()
            .filter(b ->
                b.getTitle().toLowerCase().contains(lowerKeyword) ||
                b.getAuthor().toLowerCase().contains(lowerKeyword)
            )
            .toList();

        ctx.json(Map.of(
            "keyword", keyword,
            "results", results,
            "total", results.size()
        ));
    }
}
