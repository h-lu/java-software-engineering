/*
 * 示例：RESTful API 设计原则演示。
 * 本例演示：资源识别、URI 设计、HTTP 方法选择、状态码使用。
 * 运行方式：javac 02_restful_design.java && java RestfulDesignDemo
 * 预期输出：展示好的和坏的 RESTful 设计示例
 */

import java.util.*;

// 文件：RestfulDesignDemo.java（RESTful 设计示例入口）
class RestfulDesignDemo {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           RESTful API 设计原则演示                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // 原则 1：资源识别
        System.out.println("【原则 1：一切皆资源】");
        System.out.println();
        System.out.println("在 REST 中，所有数据都是资源：");
        System.out.println("  • 图书（Book）");
        System.out.println("  • 用户（User）");
        System.out.println("  • 任务（Task）");
        System.out.println("  • 借阅记录（BorrowRecord）");
        System.out.println();
        System.out.println("每个资源都有唯一的标识符：URI");
        System.out.println();

        // 原则 2：URI 设计
        System.out.println("【原则 2：URI 设计 - 使用名词，不用动词】");
        System.out.println();
        System.out.println("❌ 不好的设计（动词）：");
        System.out.println("  GET /getBooks          → 获取图书列表");
        System.out.println("  GET /getBook?id=1      → 获取指定图书");
        System.out.println("  POST /createBook       → 创建图书");
        System.out.println("  POST /updateBook       → 更新图书");
        System.out.println("  POST /deleteBook       → 删除图书");
        System.out.println();

        System.out.println("✅ 好的设计（名词 + HTTP 方法）：");
        System.out.println("  GET    /books          → 获取图书列表");
        System.out.println("  GET    /books/1        → 获取 ID 为 1 的图书");
        System.out.println("  POST   /books          → 创建新图书");
        System.out.println("  PUT    /books/1        → 更新 ID 为 1 的图书");
        System.out.println("  DELETE /books/1        → 删除 ID 为 1 的图书");
        System.out.println();

        // 原则 3：资源层级
        System.out.println("【原则 3：资源层级关系】");
        System.out.println();
        System.out.println("使用路径表示资源间的层级：");
        System.out.println("  GET /users/42/tasks    → 用户 42 的所有任务");
        System.out.println("  GET /books/123/reviews → 图书 123 的所有评论");
        System.out.println("  POST /users/42/tasks   → 为用户 42 创建新任务");
        System.out.println();

        System.out.println("❌ 避免扁平化设计：");
        System.out.println("  GET /tasksOfUser?userId=42   → 不够 RESTful");
        System.out.println("  GET /userTasks?user=42       → 不够直观");
        System.out.println();

        // 原则 4：查询参数
        System.out.println("【原则 4：查询参数用于过滤、排序、分页】");
        System.out.println();
        System.out.println("过滤：");
        System.out.println("  GET /books?author=刘慈欣     → 按作者过滤");
        System.out.println("  GET /books?category=科幻     → 按分类过滤");
        System.out.println("  GET /books?year=2020         → 按年份过滤");
        System.out.println();

        System.out.println("组合过滤：");
        System.out.println("  GET /books?author=刘慈欣&category=科幻&year=2020");
        System.out.println();

        System.out.println("排序和分页：");
        System.out.println("  GET /books?sort=year&order=desc     → 按年份降序");
        System.out.println("  GET /books?page=1&size=20           → 分页");
        System.out.println("  GET /books?offset=0&limit=20        → 偏移量分页");
        System.out.println();

        // 原则 5：HTTP 方法
        System.out.println("【原则 5：HTTP 方法表示操作】");
        System.out.println();
        System.out.println("┌──────────┬─────────────────┬──────────┐");
        System.out.println("│ 方法     │ 操作            │ 幂等性   │");
        System.out.println("├──────────┼─────────────────┼──────────┤");
        System.out.println("│ GET      │ 获取资源        │ ✅ 是    │");
        System.out.println("│ POST     │ 创建资源        │ ❌ 否    │");
        System.out.println("│ PUT      │ 全量更新        │ ✅ 是    │");
        System.out.println("│ PATCH    │ 部分更新        │ ❌ 否*   │");
        System.out.println("│ DELETE   │ 删除资源        │ ✅ 是    │");
        System.out.println("└──────────┴─────────────────┴──────────┘");
        System.out.println("* PATCH 的幂等性取决于实现");
        System.out.println();

        System.out.println("什么是幂等性？");
        System.out.println("  执行一次和执行多次，效果相同。");
        System.out.println("  例：GET /books/1 执行 100 次，结果不变。");
        System.out.println("  例：POST /books 执行 100 次，创建 100 本书。");
        System.out.println();

        // 原则 6：状态码
        System.out.println("【原则 6：HTTP 状态码】");
        System.out.println();
        System.out.println("2xx 成功：");
        System.out.println("  200 OK              → 请求成功（GET、PUT、DELETE）");
        System.out.println("  201 Created         → 创建成功（POST）");
        System.out.println("  204 No Content      → 成功但无返回内容（DELETE）");
        System.out.println();

        System.out.println("4xx 客户端错误：");
        System.out.println("  400 Bad Request     → 请求格式错误");
        System.out.println("  401 Unauthorized    → 未认证");
        System.out.println("  403 Forbidden       → 无权限");
        System.out.println("  404 Not Found       → 资源不存在");
        System.out.println("  409 Conflict        → 资源冲突（如重复创建）");
        System.out.println("  422 Unprocessable   → 语义错误（如验证失败）");
        System.out.println();

        System.out.println("5xx 服务器错误：");
        System.out.println("  500 Internal Error  → 服务器内部错误");
        System.out.println("  502 Bad Gateway     → 网关错误");
        System.out.println("  503 Service Unavail → 服务不可用");
        System.out.println();

        // 完整示例：图书管理 API
        System.out.println("【完整示例：图书管理 API 设计】");
        System.out.println();

        List<ApiEndpoint> endpoints = Arrays.asList(
            new ApiEndpoint("GET", "/books", "获取所有图书", "200 OK", "[{id:1,title:'三体'},...]"),
            new ApiEndpoint("GET", "/books/{id}", "获取指定图书", "200 OK / 404", "{id:1,title:'三体'}"),
            new ApiEndpoint("POST", "/books", "创建新图书", "201 Created", "{id:2,title:'流浪地球'}"),
            new ApiEndpoint("PUT", "/books/{id}", "全量更新图书", "200 OK / 404", "{id:1,title:'三体全集'}"),
            new ApiEndpoint("PATCH", "/books/{id}", "部分更新图书", "200 OK", "{year:2024}"),
            new ApiEndpoint("DELETE", "/books/{id}", "删除图书", "204 No Content", ""),
            new ApiEndpoint("GET", "/books?author=刘慈欣", "搜索图书", "200 OK", "过滤结果")
        );

        System.out.println("┌────────┬─────────────────┬────────────────────┬──────────────┐");
        System.out.println("│ 方法   │ URI             │ 描述               │ 响应         │");
        System.out.println("├────────┼─────────────────┼────────────────────┼──────────────┤");
        for (ApiEndpoint ep : endpoints) {
            System.out.printf("│ %-6s │ %-15s │ %-18s │ %-12s │%n",
                ep.method, ep.uri, ep.description, ep.response);
        }
        System.out.println("└────────┴─────────────────┴────────────────────┴──────────────┘");
        System.out.println();

        // 常见错误
        System.out.println("【常见设计错误】");
        System.out.println();

        List<DesignError> errors = Arrays.asList(
            new DesignError("使用动词", "/getBooks", "/books", "REST 用 HTTP 方法表示动作"),
            new DesignError("单数名词", "/book", "/books", "集合资源用复数"),
            new DesignError("错误方法", "POST /books/1/delete", "DELETE /books/1", "用 HTTP 方法而非 URL 动词"),
            new DesignError("忽略状态码", "总是返回 200", "201/204/404", "状态码传达结果信息"),
            new DesignError("深层嵌套", "/users/1/books/2/reviews/3", "/reviews/3", "层级不超过 3 层")
        );

        for (int i = 0; i < errors.size(); i++) {
            DesignError err = errors.get(i);
            System.out.println((i + 1) + ". " + err.name);
            System.out.println("   ❌ 错误: " + err.badExample);
            System.out.println("   ✅ 正确: " + err.goodExample);
            System.out.println("   💡 原因: " + err.reason);
            System.out.println();
        }

        // 设计检查清单
        System.out.println("【RESTful API 设计检查清单】");
        System.out.println();
        System.out.println("设计新端点时，问自己：");
        System.out.println("  □ URI 是否使用名词复数？（/books 而非 /book）");
        System.out.println("  □ URI 是否不包含动词？（用 HTTP 方法表示动作）");
        System.out.println("  □ HTTP 方法是否恰当？（GET/POST/PUT/PATCH/DELETE）");
        System.out.println("  □ 状态码是否正确？（200/201/204/400/404/500）");
        System.out.println("  □ 资源层级是否合理？（不超过 3 层）");
        System.out.println("  □ 查询参数是否用于过滤/排序？");
        System.out.println();

        System.out.println("通过本示例，你应该理解：");
        System.out.println("  1. REST 是设计风格，不是技术标准");
        System.out.println("  2. 资源是核心概念，URI 是资源的标识");
        System.out.println("  3. HTTP 方法表示对资源的操作");
        System.out.println("  4. 状态码传达操作结果");
    }
}

// 辅助类：API 端点
class ApiEndpoint {
    String method;
    String uri;
    String description;
    String response;
    String example;

    ApiEndpoint(String method, String uri, String description, String response, String example) {
        this.method = method;
        this.uri = uri;
        this.description = description;
        this.response = response;
        this.example = example;
    }
}

// 辅助类：设计错误
class DesignError {
    String name;
    String badExample;
    String goodExample;
    String reason;

    DesignError(String name, String badExample, String goodExample, String reason) {
        this.name = name;
        this.badExample = badExample;
        this.goodExample = goodExample;
        this.reason = reason;
    }
}
