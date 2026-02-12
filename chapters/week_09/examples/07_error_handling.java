/*
 * 示例：API 异常处理——统一错误响应格式。
 * 本例演示：如何实现统一的 API 异常处理机制。
 * 运行方式：mvn -q -f chapters/week_09/starter_code/pom.xml compile exec:java \
 *          -Dexec.mainClass="examples._07_error_handling"
 * 预期输出：服务启动，演示各种异常情况的处理
 *
 * 测试命令：
 *   # 正常请求
 *   curl http://localhost:7070/books/1
 *
 *   # 404 错误
 *   curl http://localhost:7070/books/999
 *
 *   # 400 错误（验证失败）
 *   curl -X POST http://localhost:7070/books \
 *     -H "Content-Type: application/json" \
 *     -d '{"title":"","author":"刘慈欣"}'
 *
 *   # 400 错误（JSON 格式错误）
 *   curl -X POST http://localhost:7070/books \
 *     -H "Content-Type: application/json" \
 *     -d '{invalid json}'
 */
package examples;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * API 异常处理完整示例。
 *
 * <p>为什么需要统一异常处理？
 * <ul>
 *   <li>给用户友好的错误信息（而非堆栈跟踪）</li>
 *   <li>统一错误格式，方便客户端处理</li>
 *   <li>隐藏内部实现细节（安全）</li>
 *   <li>记录日志，便于排查问题</li>
 * </ul>
 */
public class _07_error_handling {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           API 异常处理与统一错误响应                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        BookRepository repository = new InMemoryBookRepository();
        BookController controller = new BookController(repository);

        // 预置数据
        repository.save(new Book(null, "三体", "刘慈欣", 2006));

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        // ===== 路由定义 =====
        app.get("/health", ctx -> ctx.json(Map.of("status", "UP")));
        app.get("/books", controller::getAllBooks);
        app.get("/books/{id}", controller::getBook);
        app.post("/books", controller::createBook);
        app.put("/books/{id}", controller::updateBook);
        app.delete("/books/{id}", controller::deleteBook);

        // ===== 异常处理配置 =====

        // 1. 处理自定义 API 异常
        app.exception(ApiException.class, (e, ctx) -> {
            ErrorResponse error = new ErrorResponse(
                e.getStatusCode(),
                e.getMessage(),
                e.getDetails(),
                Instant.now().toEpochMilli()
            );
            ctx.status(e.getStatusCode()).json(error);
        });

        // 2. 处理验证异常
        app.exception(ValidationException.class, (e, ctx) -> {
            ErrorResponse error = new ErrorResponse(
                400,
                "Validation failed",
                e.getMessage(),
                Instant.now().toEpochMilli()
            );
            ctx.status(400).json(error);
        });

        // 3. 处理资源未找到
        app.exception(NotFoundException.class, (e, ctx) -> {
            ErrorResponse error = new ErrorResponse(
                404,
                "Resource not found",
                e.getMessage(),
                Instant.now().toEpochMilli()
            );
            ctx.status(404).json(error);
        });

        // 4. 处理 JSON 解析错误
        app.exception(io.javalin.http.BadRequestResponse.class, (e, ctx) -> {
            ErrorResponse error = new ErrorResponse(
                400,
                "Invalid request format",
                "Request body is not valid JSON",
                Instant.now().toEpochMilli()
            );
            ctx.status(400).json(error);
        });

        // 5. 兜底异常处理器（所有未处理的异常）
        app.exception(Exception.class, (e, ctx) -> {
            // 记录日志（实际应用使用日志框架）
            System.err.println("[ERROR] Unhandled exception: " + e.getMessage());
            e.printStackTrace();

            // 返回通用错误（不要暴露内部细节）
            ErrorResponse error = new ErrorResponse(
                500,
                "Internal server error",
                null, // 生产环境不暴露细节
                Instant.now().toEpochMilli()
            );
            ctx.status(500).json(error);
        });

        // 启动服务
        app.start(7070);

        System.out.println("✅ 服务已启动！");
        System.out.println();
        System.out.println("异常处理演示：");
        System.out.println("  # 正常请求");
        System.out.println("  curl http://localhost:7070/books/1");
        System.out.println();
        System.out.println("  # 404 - 资源不存在");
        System.out.println("  curl http://localhost:7070/books/999");
        System.out.println();
        System.out.println("  # 400 - 验证失败");
        System.out.println("  curl -X POST http://localhost:7070/books \\");
        System.out.println("    -H 'Content-Type: application/json' \\");
        System.out.println("    -d '{\"title\":\"\",\"author\":\"刘慈欣\"}'");
        System.out.println();
        System.out.println("  # 400 - JSON 格式错误");
        System.out.println("  curl -X POST http://localhost:7070/books \\");
        System.out.println("    -H 'Content-Type: application/json' \\");
        System.out.println("    -d '{invalid}'");
        System.out.println();
        System.out.println("服务地址: http://localhost:7070");
    }
}

// ===== 自定义异常类 =====

/**
 * 基础 API 异常。
 */
class ApiException extends RuntimeException {
    private final int statusCode;
    private final String details;

    public ApiException(int statusCode, String message) {
        this(statusCode, message, null);
    }

    public ApiException(int statusCode, String message, String details) {
        super(message);
        this.statusCode = statusCode;
        this.details = details;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDetails() {
        return details;
    }
}

/**
 * 404 资源未找到。
 */
class NotFoundException extends ApiException {
    public NotFoundException(String resource, String id) {
        super(404, resource + " not found: " + id);
    }
}

/**
 * 400 验证失败。
 */
class ValidationException extends ApiException {
    public ValidationException(String message) {
        super(400, message);
    }

    public ValidationException(String field, String message) {
        super(400, field + ": " + message);
    }
}

/**
 * 409 资源冲突。
 */
class ConflictException extends ApiException {
    public ConflictException(String message) {
        super(409, message);
    }
}

/**
 * 403 无权限。
 */
class ForbiddenException extends ApiException {
    public ForbiddenException(String message) {
        super(403, message);
    }
}

// ===== 错误响应类 =====

/**
 * 标准错误响应格式。
 */
class ErrorResponse {
    private final int code;
    private final String message;
    private final String details;
    private final long timestamp;
    private final String path;

    public ErrorResponse(int code, String message, String details, long timestamp) {
        this(code, message, details, timestamp, null);
    }

    public ErrorResponse(int code, String message, String details, long timestamp, String path) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
        this.path = path;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public String getDetails() { return details; }
    public long getTimestamp() { return timestamp; }
    public String getPath() { return path; }
}

// ===== Controller 层 =====

class BookController {
    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    public void getAllBooks(Context ctx) {
        List<Book> books = repository.findAll();
        ctx.json(Map.of("data", books, "total", books.size()));
    }

    public void getBook(Context ctx) {
        String id = ctx.pathParam("id");

        Book book = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book", id));

        ctx.json(book);
    }

    public void createBook(Context ctx) {
        Book book;
        try {
            book = ctx.bodyAsClass(Book.class);
        } catch (Exception e) {
            throw new ValidationException("Invalid JSON format: " + e.getMessage());
        }

        // 验证
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new ValidationException("title", "Title is required");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new ValidationException("author", "Author is required");
        }
        if (book.getYear() < 1000 || book.getYear() > 2100) {
            throw new ValidationException("year", "Year must be between 1000 and 2100");
        }

        // 检查重复
        boolean exists = repository.findAll().stream()
            .anyMatch(b -> b.getTitle().equals(book.getTitle())
                        && b.getAuthor().equals(book.getAuthor()));
        if (exists) {
            throw new ConflictException("Book already exists: " + book.getTitle());
        }

        Book saved = repository.save(book);
        ctx.status(201).json(saved);
    }

    public void updateBook(Context ctx) {
        String id = ctx.pathParam("id");

        Book existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book", id));

        Book updates;
        try {
            updates = ctx.bodyAsClass(Book.class);
        } catch (Exception e) {
            throw new ValidationException("Invalid JSON format");
        }

        // 验证
        if (updates.getTitle() == null || updates.getTitle().trim().isEmpty()) {
            throw new ValidationException("title", "Title is required");
        }

        updates.setId(id);
        Book saved = repository.save(updates);
        ctx.json(saved);
    }

    public void deleteBook(Context ctx) {
        String id = ctx.pathParam("id");

        Book existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book", id));

        repository.delete(id);
        ctx.status(204);
    }
}

// ===== Repository 和 Entity =====

interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(String id);
    List<Book> findAll();
    void delete(String id);
}

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
        return List.copyOf(books.values());
    }

    @Override
    public void delete(String id) {
        books.remove(id);
    }
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

/*
 * 异常处理最佳实践：
 *
 * 1. 分层异常
 *    - 底层（Repository）：抛出具体异常（如 SQLException）
 *    - 中层（Service）：转换为业务异常（如 NotFoundException）
 *    - 上层（Controller）：捕获并转换为 HTTP 响应
 *
 * 2. 错误响应格式统一
 *    - code: HTTP 状态码
 *    - message: 用户友好的错误信息
 *    - details: 详细说明（可选，开发环境可用）
 *    - timestamp: 错误发生时间
 *    - path: 请求路径（可选）
 *
 * 3. 安全考虑
 *    - 生产环境不要返回堆栈跟踪
 *    - 不要暴露内部实现细节（如 SQL 错误）
 *    - 敏感操作记录日志
 *
 * 4. 常见 HTTP 状态码使用
 *    - 400: 请求格式错误或验证失败
 *    - 401: 未认证（需要登录）
 *    - 403: 无权限（已登录但无权访问）
 *    - 404: 资源不存在
 *    - 409: 资源冲突（如重复创建）
 *    - 422: 语义错误（如业务规则验证失败）
 *    - 500: 服务器内部错误
 */
