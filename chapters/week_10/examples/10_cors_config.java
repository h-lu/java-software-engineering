/*
 * 示例：Javalin CORS 配置
 * 运行方式：mvn -q -f chapters/week_10/starter_code/pom.xml compile exec:java \
 *          -Dexec.mainClass="examples._10_cors_config"
 * 预期输出：服务启动，允许跨域请求，前端可以正常调用 API
 *
 * 本例演示：
 * 1. 开发环境的宽松 CORS 配置（允许任何来源）
 * 2. 生产环境的安全 CORS 配置（限制特定来源）
 * 3. 自定义 CORS 规则（允许特定方法和头信息）
 * 4. 与 CampusFlow 集成
 */
package examples;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;

import java.util.Map;

/**
 * Javalin CORS 配置示例。
 *
 * <p>CORS（Cross-Origin Resource Sharing，跨域资源共享）是一种浏览器安全机制，
 * 用于控制一个域的网页如何请求另一个域的资源。当前端和后端运行在不同域名/端口时，
 * 必须正确配置 CORS 才能正常通信。
 *
 * <p>常见场景：
 * <ul>
 *   <li>前端直接打开 HTML 文件（file:// 协议）调用 localhost API</li>
 *   <li>前端运行在 localhost:8080，后端运行在 localhost:7070</li>
 *   <li>前端部署在 example.com，后端部署在 api.example.com</li>
 * </ul>
 */
public class _10_cors_config {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║     Javalin CORS 配置示例                                ║");
        System.out.println("║     Week 10：前后端联调必备                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // 选择要演示的配置方式
        Javalin app = createDevCorsApp();
        // Javalin app = createProductionCorsApp();
        // Javalin app = createCustomCorsApp();

        app.start(7070);

        System.out.println("✅ 服务已启动，CORS 已配置！");
        System.out.println();
        System.out.println("测试方法：");
        System.out.println("1. 创建一个 HTML 文件，使用 Fetch API 调用 http://localhost:7070/api/data");
        System.out.println("2. 直接双击打开 HTML 文件（file:// 协议）");
        System.out.println("3. 观察浏览器控制台，应该没有 CORS 错误");
        System.out.println();
        System.out.println("服务地址: http://localhost:7070");
        System.out.println("按 Ctrl+C 停止服务");
    }

    /**
     * 方式 1：开发环境配置 - 允许任何来源（最宽松）。
     *
     * <p>适用场景：本地开发、快速原型。
     * <p>⚠️ 警告：不要在生产环境使用！
     */
    static Javalin createDevCorsApp() {
        System.out.println("使用配置：开发环境（允许任何来源）");
        System.out.println();

        return Javalin.create(config -> {
            // 启用 CORS 插件，允许任何来源访问
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        })
        .get("/api/data", ctx -> {
            // 模拟 API 端点
            ctx.json(Map.of(
                "message", "CORS 配置成功！",
                "timestamp", System.currentTimeMillis(),
                "origin", ctx.header("Origin")
            ));
        })
        .post("/api/data", ctx -> {
            // 测试 POST 请求
            ctx.json(Map.of(
                "message", "POST 请求成功！",
                "received", ctx.body()
            ));
        });
    }

    /**
     * 方式 2：生产环境配置 - 限制特定来源（推荐）。
     *
     * <p>适用场景：生产环境、有明确的前端域名。
     * <p>只允许特定域名访问，提高安全性。
     */
    static Javalin createProductionCorsApp() {
        System.out.println("使用配置：生产环境（限制特定来源）");
        System.out.println();

        return Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    // 只允许特定域名
                    rule.allowHost("http://localhost:8080");
                    rule.allowHost("https://campusflow.example.com");

                    // 允许的 HTTP 方法
                    rule.allowMethod("GET");
                    rule.allowMethod("POST");
                    rule.allowMethod("PUT");
                    rule.allowMethod("DELETE");
                    rule.allowMethod("PATCH");

                    // 允许的请求头
                    rule.allowHeader("Content-Type");
                    rule.allowHeader("Authorization");

                    // 允许携带凭证（cookies）
                    rule.allowCredentials();
                });
            });
        })
        .get("/api/data", ctx -> {
            ctx.json(Map.of("message", "生产环境 CORS 配置成功！"));
        });
    }

    /**
     * 方式 3：自定义 CORS 规则。
     *
     * <p>适用场景：需要精细控制 CORS 行为。
     */
    static Javalin createCustomCorsApp() {
        System.out.println("使用配置：自定义规则");
        System.out.println();

        return Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                // 规则 1：API 端点 - 允许特定来源
                cors.addRule(rule -> {
                    rule.path("/api/*");
                    rule.allowHost("http://localhost:8080");
                    rule.allowHost("http://localhost:3000");
                    rule.allowMethod("GET");
                    rule.allowMethod("POST");
                    rule.allowHeader("Content-Type");
                });

                // 规则 2：公开端点 - 允许任何来源
                cors.addRule(rule -> {
                    rule.path("/public/*");
                    rule.anyHost();
                    rule.allowMethod("GET");
                });

                // 规则 3：健康检查 - 允许任何来源，仅 GET
                cors.addRule(rule -> {
                    rule.path("/health");
                    rule.anyHost();
                    rule.allowMethod("GET");
                });
            });
        })
        .get("/api/data", ctx -> {
            ctx.json(Map.of("message", "API 数据"));
        })
        .get("/public/info", ctx -> {
            ctx.json(Map.of("message", "公开信息"));
        })
        .get("/health", ctx -> {
            ctx.json(Map.of("status", "UP"));
        });
    }

    /**
     * CampusFlow 集成示例：为 Week 09 的 CampusFlow 添加 CORS 支持。
     *
     * <p>使用方法：将 CORS 配置添加到 App.java 的 Javalin 创建代码中。
     */
    static Javalin createCampusFlowApp() {
        return Javalin.create(config -> {
            // 启用开发日志
            config.bundledPlugins.enableDevLogging();

            // 启用 CORS
            config.bundledPlugins.enableCors(cors -> {
                // 开发环境：允许任何来源
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);

                // 生产环境请使用以下配置：
                // cors.addRule(rule -> {
                //     rule.allowHost("http://localhost:8080");
                //     rule.allowHost("https://yourdomain.com");
                //     rule.allowMethod("GET");
                //     rule.allowMethod("POST");
                //     rule.allowMethod("PUT");
                //     rule.allowMethod("DELETE");
                //     rule.allowMethod("PATCH");
                //     rule.allowHeader("Content-Type");
                // });
            });
        });
    }
}

/*
 * CORS 常见问题排查指南：
 *
 * 问题 1：浏览器报错 "No 'Access-Control-Allow-Origin' header"
 * 原因：后端没有配置 CORS，或配置不正确
 * 解决：确保 config.bundledPlugins.enableCors() 正确调用
 *
 * 问题 2：浏览器报错 "CORS policy: Method DELETE is not allowed"
 * 原因：CORS 配置中没有允许 DELETE 方法
 * 解决：在规则中添加 rule.allowMethod("DELETE")
 *
 * 问题 3：预检请求（OPTIONS）失败
 * 原因：复杂请求（如 POST with JSON）会发送预检请求，需要正确配置
 * 解决：确保 allowHeader("Content-Type") 已设置
 *
 * 问题 4：携带 Cookie 时 CORS 失败
 * 原因：credentials 配置不匹配
 * 解决：后端设置 rule.allowCredentials()，前端设置 credentials: 'include'
 *
 * 问题 5：配置了 CORS 但仍然报错
 * 排查步骤：
 * 1. 确认后端服务已重启（配置更改需要重启）
 * 2. 检查浏览器控制台，确认错误信息
 * 3. 使用 curl 测试后端响应头：
 *    curl -I -H "Origin: http://localhost:8080" http://localhost:7070/api/data
 * 4. 确认响应头中包含 Access-Control-Allow-Origin
 */
