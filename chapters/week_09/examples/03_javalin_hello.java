/*
 * 示例：Javalin 入门——Hello World。
 * 本例演示：使用 Javalin 框架搭建最简单的 Web 服务。
 * 运行方式：mvn -q -f chapters/week_09/starter_code/pom.xml compile exec:java \
 *          -Dexec.mainClass="examples._03_javalin_hello"
 * 预期输出：服务启动在 http://localhost:7070，访问根路径返回 "Hello, World!"
 *
 * 注意：本示例需要 Javalin 依赖，请确保 pom.xml 包含：
 *   <dependency>
 *       <groupId>io.javalin</groupId>
 *       <artifactId>javalin</artifactId>
 *       <version>6.1.3</version>
 *   </dependency>
 */
package examples;

import io.javalin.Javalin;

/**
 * Javalin Hello World 示例。
 *
 * <p>Javalin 是一个极简的 Java Web 框架，设计哲学是"做减法"：
 * <ul>
 *   <li>无注解，纯代码配置</li>
 *   <li>启动快速，内存占用小</li>
 *   <li>学习曲线平缓</li>
 *   <li>与 Kotlin 和 Java 都兼容</li>
 * </ul>
 *
 * <p>对比 Spring Boot：
 * <ul>
 *   <li>Spring Boot：功能丰富，但配置复杂，启动慢</li>
 *   <li>Javalin：轻量极简，几行代码启动，适合学习和微服务</li>
 * </ul>
 */
public class _03_javalin_hello {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           Javalin Hello World                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // 创建 Javalin 应用实例
        // create() 方法创建一个新的 Javalin 实例
        var app = Javalin.create();

        // 配置路由
        // .get(path, handler) 定义 GET 请求的处理逻辑
        // ctx 是 Context 对象，包含请求信息和响应方法
        app.get("/", ctx -> {
            // ctx.result() 设置响应体为纯文本
            ctx.result("Hello, World!");
        });

        // 启动服务，监听 7070 端口
        // start() 是阻塞方法，服务会一直保持运行
        app.start(7070);

        // 服务启动后的输出（这行代码在 start() 之后执行，因为 start() 不会阻塞主线程返回）
        System.out.println("✅ 服务已启动！");
        System.out.println("   访问地址: http://localhost:7070");
        System.out.println();
        System.out.println("按 Ctrl+C 停止服务");
        System.out.println();

        // 注意：实际运行时，start() 会阻塞线程
        // 上面的输出会在启动前打印
        // 为了演示效果，我们调整顺序：
    }
}

/**
 * 更完整的 Hello World 版本（带注释说明）。
 */
class JavalinHelloDetailed {

    public static void main(String[] args) {
        System.out.println("=== Javalin 详细示例 ===\n");

        // 步骤 1：创建应用
        // Javalin.create() 可以传入配置 lambda
        var app = Javalin.create(config -> {
            // 启用请求日志（开发时有用）
            config.bundledPlugins.enableDevLogging();
            // 启用 CORS（跨域支持）
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(corsRule -> {
                    corsRule.allowHost("http://localhost:3000");
                });
            });
        });

        // 步骤 2：定义路由
        // 基础路由
        app.get("/", ctx -> ctx.result("Hello, Javalin!"));

        // 返回 HTML
        app.get("/html", ctx -> {
            ctx.contentType("text/html");
            ctx.result("<h1>Hello, HTML!</h1><p>Welcome to Javalin</p>");
        });

        // 返回 JSON（Javalin 自动序列化）
        app.get("/json", ctx -> {
            ctx.json(Map.of(
                "message", "Hello, JSON!",
                "timestamp", System.currentTimeMillis()
            ));
        });

        // 步骤 3：启动服务
        System.out.println("启动服务...");
        app.start(7070);

        System.out.println("服务运行在 http://localhost:7070");
        System.out.println("  GET /      -> 纯文本");
        System.out.println("  GET /html  -> HTML 页面");
        System.out.println("  GET /json  -> JSON 数据");
    }

    // 辅助类，用于编译通过
    private static class Map {
        static <K, V> java.util.Map<K, V> of(K k1, V v1, K k2, V v2) {
            java.util.Map<K, V> map = new java.util.HashMap<>();
            map.put(k1, v1);
            map.put(k2, v2);
            return map;
        }
    }
}

/**
 * 路由和 Context 详解。
 */
class JavalinRoutingExplained {

    public static void main(String[] args) {
        var app = Javalin.create();

        // ===== 路由定义 =====

        // 1. 静态路由
        app.get("/books", ctx -> ctx.result("图书列表"));

        // 2. 路径参数（使用 {name} 语法）
        app.get("/books/{id}", ctx -> {
            String bookId = ctx.pathParam("id");
            ctx.result("图书 ID: " + bookId);
        });

        // 3. 多个路径参数
        app.get("/users/{userId}/orders/{orderId}", ctx -> {
            String userId = ctx.pathParam("userId");
            String orderId = ctx.pathParam("orderId");
            ctx.result("用户: " + userId + ", 订单: " + orderId);
        });

        // 4. 查询参数
        app.get("/search", ctx -> {
            String keyword = ctx.queryParam("keyword");
            String category = ctx.queryParam("category");
            ctx.result("搜索: " + keyword + ", 分类: " + category);
        });

        // 5. 带默认值的查询参数
        app.get("/books-paged", ctx -> {
            int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(1);
            int size = ctx.queryParamAsClass("size", Integer.class).getOrDefault(20);
            ctx.result("页码: " + page + ", 每页: " + size);
        });

        // ===== Context 对象详解 =====

        app.get("/context-demo", ctx -> {
            // 请求信息
            String method = ctx.method().toString();      // GET
            String path = ctx.path();                      // /context-demo
            String fullUrl = ctx.url();                    // 完整 URL

            // 请求头
            String userAgent = ctx.header("User-Agent");

            // 响应设置
            ctx.status(200);                               // HTTP 状态码
            ctx.contentType("application/json");           // 内容类型
            ctx.header("X-Custom-Header", "value");        // 自定义响应头

            // 响应体（三种方式）
            // ctx.result("纯文本");                         // 字符串
            // ctx.json(new Object());                     // JSON 对象
            // ctx.html("<p>HTML</p>");                    // HTML

            ctx.json(java.util.Map.of(
                "method", method,
                "path", path,
                "userAgent", userAgent != null ? userAgent : "unknown"
            ));
        });

        // ===== HTTP 方法 =====

        app.post("/books", ctx -> ctx.result("创建图书"));
        app.put("/books/{id}", ctx -> ctx.result("更新图书"));
        app.patch("/books/{id}", ctx -> ctx.result("部分更新"));
        app.delete("/books/{id}", ctx -> ctx.result("删除图书"));

        // 启动
        app.start(7070);
        System.out.println("服务启动在 http://localhost:7070");
        System.out.println("尝试访问：");
        System.out.println("  curl http://localhost:7070/books");
        System.out.println("  curl http://localhost:7070/books/123");
        System.out.println("  curl 'http://localhost:7070/search?keyword=java&category=tech'");
    }
}
