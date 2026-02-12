# Week 09：从命令行到 Web——REST API 设计与 Javalin 入门

> "API 是程序员之间的契约。好的契约让协作顺畅，坏的契约让所有人痛苦。"
> —— 改编自 Joshua Bloch

2025-2026 年，API 设计正在经历一场静默的革命。AI 辅助编程工具不仅能生成端点代码，还能根据自然语言描述生成 OpenAPI 规范、自动补全 JSON Schema、甚至模拟 API 行为。GitHub Copilot 和 Claude Code 已经能根据注释生成完整的 REST 控制器代码。但这也带来了新问题：当 AI 生成的 API 设计不符合 RESTful 原则时，你如何识别？当 AI 建议的端点命名与团队规范冲突时，你如何决策？

API 设计是软件工程的核心技能——它定义了你的系统如何与外界对话。本周，你将学习 RESTful 设计原则，使用 Javalin 框架将 CampusFlow 从 CLI 版改造为 Web 服务。这是 CampusFlow 从"本地工具"走向"网络应用"的关键一步。

---

## 前情提要

上周你完成了 CampusFlow 的代码重构——上帝类被拆解，策略模式引入了费用计算，静态分析工具确认了代码质量的提升。你的 CLI 版 CampusFlow 已经是一个结构良好、测试覆盖充分的控制台应用。

但 CLI 有天然的局限：它只能在本地运行，用户必须安装 Java 环境，界面是纯文字的。如果你想让室友也能用 CampusFlow 管理任务，或者想在手机上查看任务列表，CLI 就无能为力了。

这时候你需要 Web 服务——一个运行在服务器的程序，通过 HTTP 协议与客户端通信。而 REST API 就是这场对话的"语法规则"。本周，你将把 CampusFlow 改造成一个真正的 Web 后端，学会设计优雅的 API，为下周的 AI 生成前端打下基础。

---

## 学习目标

完成本周学习后，你将能够：

1. 解释 REST 架构风格的核心原则（资源、URI、HTTP 方法、状态码）
2. 设计符合 RESTful 规范的 API 端点（端点命名、请求/响应格式）
3. 使用 Javalin 框架创建 Web 服务，实现路由和请求处理
4. 处理 JSON 数据的序列化与反序列化（使用 Jackson/Gson）
5. 实现统一的 API 异常处理机制
6. 使用 HTTP 客户端（curl/Postman/IDE 工具）测试 REST API

---

<!--
贯穿案例设计：【图书管理 API——从本地到网络】
- 第 1 节：从 CLI 图书管理器的痛点出发，引出 Web 服务的需求
- 第 2 节：设计图书管理 API 的 RESTful 端点（资源识别、URI 设计）
- 第 3 节：用 Javalin 搭建基础服务，实现第一个 GET /books 端点
- 第 4 节：实现完整的 CRUD（POST/PUT/DELETE），处理 JSON 数据
- 第 5 节：统一异常处理，返回标准错误响应格式
- 第 6 节：CampusFlow 进度——将 CLI 版改造为 Web 服务，编写 ADR-003
最终成果：一个可运行的图书管理 REST API 服务，支持完整的 CRUD 操作

认知负荷预算检查：
- 本周新概念（AI 时代工程阶段上限 5 个）：
  1. REST (Representational State Transfer)
  2. API 端点 (API Endpoint)
  3. HTTP 方法/动词 (GET/POST/PUT/DELETE)
  4. JSON (JavaScript Object Notation)
  5. 路由 (Routing)
- 结论：✅ 正好 5 个，在预算内

回顾桥设计（至少引用前 4 周的 2 个概念）：
- [Repository 模式]（来自 week_07）：在第 4 节，Service 层复用 Repository 进行数据操作
- [异常处理]（来自 week_03）：在第 5 节，API 层统一处理异常，返回标准错误格式
- [策略模式]（来自 week_08）：在第 5 节，异常处理策略可扩展
- [单元测试]（来自 week_06）：在第 5 节，API 测试也是单元测试的一种

AI 小专栏规划：
专栏 1（放在第 2 节之后，前段）：
- 主题：AI 辅助 API 设计——如何让 AI 帮助设计 RESTful API，以及需要警惕的陷阱
- 连接点：与第 2 节 RESTful 设计原则呼应
- 建议搜索词："AI REST API design 2025 2026", "LLM API endpoint naming quality", "GitHub Copilot API design patterns"

专栏 2（放在第 4 节之后，中段）：
- 主题：AI 生成 API 文档——OpenAPI/Swagger 与 AI 的结合
- 连接点：与第 4 节完整 CRUD 实现呼应
- 建议搜索词："OpenAPI AI generation 2025 2026", "Swagger AI documentation tools", "LLM OpenAPI spec generation accuracy"

CampusFlow 本周推进：
- 上周状态：CLI 版代码重构完成，代码质量显著提升（圈复杂度从 47 降至 8），通过静态分析检查
- 本周改进：将 CLI 版改造为 Web 服务，设计并实现 REST API，编写 ADR-003（API 设计决策）
- 涉及的本周概念：REST 设计原则、Javalin 框架、JSON 处理、路由、HTTP 方法
- 建议示例文件：examples/09_javalin_hello.java, examples/09_book_api.java, examples/09_campusflow_api.java

角色出场规划：
- 小北：在第 1 节面对"什么是 HTTP 请求"的困惑；在第 3 节第一次运行 Javalin 服务时的兴奋与疑问
- 阿码：在第 2 节尝试用 AI 设计 API 端点，讨论 AI 建议的合理性；在第 4 节追问"PUT 和 PATCH 有什么区别"
- 老潘：在第 1 节讲解生产环境 API 设计的重要性；在第 5 节介绍 API 版本控制和向后兼容的工程实践
-->

## 1. 为什么你的程序需要"上网"？

小北看着自己的 CampusFlow CLI 版，陷入了沉思。

"功能都实现了，代码也重构得挺漂亮，"他自言自语，"但只能在我电脑上跑。如果我想在手机上查看任务怎么办？"

阿码凑过来："你可以把数据库文件拷到手机上？"

"那也太麻烦了。而且室友也想用，我总不能每次他添加任务都发我微信吧？"

老潘正好路过："你们需要把程序变成一个服务——一个 24 小时运行、随时响应请求的服务。"

"就像网站那样？"

"对。你的 CLI 程序是'拉'式的——用户必须主动运行它。而 Web 服务是'推'式的——它一直等着，随时响应来自任何地方的请求。"

小北想了想："那岂不是很复杂？要学什么 Tomcat、Spring 什么的……"

"不用那么重。"老潘说，"有个叫 Javalin 的框架，专门做极简 Web 开发。几行代码就能跑起来。"

这就是本周的起点——把你的 Java 程序从"单机应用"变成"网络服务"。但在此之前，你需要理解 Web 服务的基本原理：HTTP 协议、请求/响应模型、以及 REST 架构风格。

### 从 CLI 到 Web 服务的思维转变

CLI 应用和 Web 服务有几个本质区别：

**运行方式**：CLI 程序启动一次、执行一次、然后退出；Web 服务启动后一直运行，等待请求。

**交互模式**：CLI 是"同步阻塞"的——程序问、用户答、程序继续；Web 服务是"异步非阻塞"的——同时处理多个客户端的请求。

**访问范围**：CLI 只能在本地运行；Web 服务可以被任何能联网的设备访问。

小北突然想到一个问题："那 Web 服务怎么知道用户想做什么？"

"通过 HTTP 请求。"老潘说，"请求里包含 URL（你要操作什么）、方法（你要做什么）、以及可选的请求体（操作需要的数据）。"

<!--
**Bloom 层次**：理解
**学习目标**：理解 CLI 应用的局限性，认识 Web 服务的价值，了解 HTTP 请求/响应模型
**贯穿案例推进**：从"本地图书管理器"的痛点出发，引出 Web 服务的需求
**建议示例文件**：01_cli_limitation.java（展示 CLI 的局限）
**叙事入口**：从"想用手机查看任务"这个真实场景切入
**角色出场**：
- 小北：面对 CLI 局限性的困惑，引出 Web 服务需求
- 阿码：提出"拷数据库文件"的笨拙方案，反衬 Web 服务的必要性
- 老潘：介绍 Web 服务的概念和 Javalin 框架
**回顾桥**：无（本章开篇，建立新上下文）
-->

## 2. REST 到底是什么？

"REST 不是某种技术，而是一种设计风格。"老潘在白板上画了几个框。

"想象你在设计一个图书馆的 API。REST 说：把一切都看作**资源**（Resource）。图书是资源，用户是资源，借阅记录也是资源。"

阿码举手："那怎么操作这些资源？"

"用 HTTP 的**方法**（Method）。GET 是获取，POST 是创建，PUT 是更新，DELETE 是删除。"

小北有点晕："等等，这和我在浏览器里输入网址有什么区别？"

"浏览器只是做 GET 请求。但 REST API 可以用所有 HTTP 方法，而且返回的是数据（通常是 JSON），不是 HTML 页面。"

### 资源与 URI

REST 的第一原则是：**一切皆资源**。每个资源都有一个唯一的标识符——URI（统一资源标识符）。

```
/books          # 所有图书（集合资源）
/books/123      # ID 为 123 的图书（单个资源）
/users          # 所有用户
/users/42/tasks # 用户 42 的所有任务
```

注意这里用**名词**而不是动词。`/getBooks` 或 `/book` 都不是好的设计。

阿码追问："那如果我要搜索图书呢？用 `/searchBooks` 吗？"

"不，搜索只是对图书集合的过滤，用查询参数："

```
/books?author=刘慈欣&category=科幻
```

### HTTP 方法与状态码

REST 使用 HTTP 方法来表达对资源的操作：

| 方法 | 操作 | 幂等性 |
|------|------|--------|
| GET | 获取资源 | 是 |
| POST | 创建资源 | 否 |
| PUT | 全量更新 | 是 |
| PATCH | 部分更新 | 否（通常）|
| DELETE | 删除资源 | 是 |

"幂等性是什么意思？"小北问。

"就是执行一次和执行多次效果相同。GET 和 PUT 是幂等的——你多次获取同一本书，结果一样；多次用同样的数据更新一本书，结果也一样。但 POST 不是幂等的——每次调用都会创建新资源。"

REST 还用 HTTP 状态码告诉客户端操作结果：

- **2xx 成功**：200 OK（成功）、201 Created（创建成功）、204 No Content（成功但无返回内容）
- **4xx 客户端错误**：400 Bad Request（请求格式错误）、404 Not Found（资源不存在）、409 Conflict（资源冲突）
- **5xx 服务器错误**：500 Internal Server Error（服务器内部错误）

"状态码是 API 的'肢体语言'。"老潘说，"用对了，调用方立刻知道发生了什么；用错了，大家都要猜。"

<!--
**Bloom 层次**：理解
**学习目标**：理解 REST 的核心概念（资源、URI、HTTP 方法、状态码），能够识别 RESTful 设计
**贯穿案例推进**：设计图书管理 API 的 RESTful 端点，讨论资源识别和 URI 设计
**建议示例文件**：02_rest_principles.md, 02_uri_design_examples.md
**叙事入口**：从"如何操作资源"这个问题切入，通过对话展开概念
**角色出场**：
- 老潘：讲解 REST 核心概念
- 阿码：追问搜索场景的 URI 设计
- 小北：对幂等性概念提出疑问
**回顾桥**：无（新概念建立）
-->

> **AI 时代小专栏：AI 辅助 API 设计**
>
> 2025-2026 年，AI 工具已经深度介入 API 设计的各个环节。GitHub Copilot 可以根据注释生成完整的 REST 控制器代码；ChatGPT 和 Claude 能将自然语言需求转换为 OpenAPI 规范；一些专门的 API 设计工具甚至能根据历史请求自动生成测试用例。
>
> 但 AI 的 API 设计建议并非总是可靠。研究表明，LLM 在代码坏味道检测上的精确度只有 0.79，召回率仅 0.41。常见的问题包括使用动词而非名词作为 URI（如 `/getBooks` 而非 `/books`）、混淆 PUT 和 PATCH 的语义、忽略 HTTP 状态码的恰当使用等。
>
> 企业级实践强调"原子化转换"——每次重构改动控制在 200 行以内，以及语义清晰性：使用自描述的字段名（如 `temperature_celsius` 而非 `temp`）、丰富元数据、上下文感知设计。
>
> 面对 AI 生成的 API 设计，建议你按这个清单审查：
> 1. **资源命名是否使用名词复数？** 如 `/books` 而非 `/book` 或 `/getBooks`
> 2. **HTTP 方法是否恰当？** GET 用于读取，POST 用于创建，PUT 用于全量更新，PATCH 用于部分更新，DELETE 用于删除
> 3. **状态码是否正确？** 200/201 成功，400 客户端错误，404 资源不存在，500 服务器错误
> 4. **错误响应格式是否统一？** 是否包含 code、message、details 等字段
>
> 企业级实践中，API 设计评审是强制环节——无论代码是人写的还是 AI 生成的。AI 可以加速实现，但设计决策必须由人做出。
>
> 参考（访问日期：2026-02-12）：
> - https://www.gravitee.io/blog/designing-apis-for-llm-apps
> - https://restfulapi.net/resource-naming/

小北合上笔记本，跃跃欲试："那我们现在就开始写吧？"

"别急，"老潘笑了，"先检查一遍你的设计。URI 都用名词了吗？状态码都考虑到了吗？"

阿码在旁边插话："我都检查过了，应该没问题。"

"好，那我们从最简单的 Hello World 开始。"老潘打开 IDE，"理论够了，写点代码吧。"

## 3. Hello, Javalin！

Javalin 是一个极简的 Java Web 框架——它不像 Spring Boot 那样庞大复杂，几行代码就能启动一个 HTTP 服务。

首先，在 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>io.javalin</groupId>
    <artifactId>javalin</artifactId>
    <version>6.1.3</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

然后，创建你的第一个 Web 服务：

```java
// 文件：HelloJavalin.java
import io.javalin.Javalin;

public class HelloJavalin {
    public static void main(String[] args) {
        var app = Javalin.create()
            .get("/", ctx -> ctx.result("Hello, World!"))
            .start(7070);

        System.out.println("Server started on http://localhost:7070");
    }
}
```

小北运行代码，然后在浏览器里访问 `http://localhost:7070/`——屏幕上出现了 "Hello, World!"。

"就这么简单？"他有点不敢相信。

"就这么简单。"老潘笑了，"Javalin 的设计哲学是'做减法'。你不需要配置一大堆东西，专注于写处理逻辑就行。"

### 理解路由和 Context

代码里的 `.get("/", ctx -> ...)` 就是**路由**（Routing）——它告诉 Javalin：当收到 GET 请求且路径是 `/` 时，执行后面的代码。

`ctx` 是 **Context** 对象，代表当前请求的上下文。它包含了请求的所有信息（路径、参数、请求体），也提供了响应的方法（`result()`、`json()`、`status()` 等）。

```java
// 获取路径参数
app.get("/books/{id}", ctx -> {
    String bookId = ctx.pathParam("id");
    ctx.result("Book ID: " + bookId);
});

// 获取查询参数
app.get("/books", ctx -> {
    String author = ctx.queryParam("author");
    ctx.result("Searching for author: " + author);
});
```

小北试着访问 `http://localhost:7070/books/123`，屏幕上显示 "Book ID: 123"。

"那如果我想返回 JSON 呢？"

"用 `ctx.json()`："

```java
app.get("/books/{id}", ctx -> {
    Book book = findBookById(ctx.pathParam("id"));
    ctx.json(book);
});
```

Javalin 会自动用 Jackson 把 Java 对象序列化成 JSON。

<!--
**Bloom 层次**：应用
**学习目标**：能够搭建 Javalin 项目，创建基本路由，理解 Context 对象
**贯穿案例推进**：搭建图书管理 API 的基础框架，实现 GET /health 健康检查端点
**建议示例文件**：03_hello_javalin.java, 03_javalin_setup.md（Maven 依赖配置）
**叙事入口**：从"Hello, World!"的成就感切入，展示 Javalin 的极简特性
**角色出场**：
- 小北：第一次运行成功时的兴奋，追问"7070 是什么"
- 老潘：解释端口、路由、Context 的概念
**回顾桥**：无（全新技术栈）
-->

## 4. 实现你的第一个 REST API

有了基础，现在来实现真正的功能。

你要构建一个图书管理 API，支持以下操作：
- `GET /books` —— 获取所有图书
- `GET /books/{id}` —— 获取指定 ID 的图书
- `POST /books` —— 创建新图书
- `PUT /books/{id}` —— 更新图书信息
- `DELETE /books/{id}` —— 删除图书

阿码看着这个列表："这和我们在 Week 07 写的 Repository 方法好像啊！"

"没错。"老潘点头，"REST API 就是给 Repository 穿上一层 HTTP 外衣。Service 层和 Repository 层可以复用，你只需要写'翻译层'——把 HTTP 请求翻译成方法调用，把方法返回值翻译成 HTTP 响应。"

### 图书实体类

```java
// 文件：Book.java
public class Book {
    private String id;
    private String title;
    private String author;
    private int year;

    // 无参构造（Jackson 需要）
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
}
```

### Repository 层（复用 Week 07 的模式与类定义）

```java
// 文件：BookRepository.java
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookRepository {
    private final Map<String, Book> books = new ConcurrentHashMap<>();
    private int nextId = 1;

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(String.valueOf(nextId++));
        }
        books.put(book.getId(), book);
        return book;
    }

    public void delete(String id) {
        books.remove(id);
    }
}
```

### Controller 层（HTTP 翻译层）

```java
// 文件：BookController.java
import io.javalin.http.Context;
import java.util.List;

public class BookController {
    private final BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    // GET /books
    public void getAllBooks(Context ctx) {
        List<Book> books = repository.findAll();
        ctx.json(books);
    }

    // GET /books/{id}
    public void getBook(Context ctx) {
        String id = ctx.pathParam("id");
        Book book = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book not found: " + id));
        ctx.json(book);
    }

    // POST /books
    public void createBook(Context ctx) {
        Book book = ctx.bodyAsClass(Book.class);
        Book saved = repository.save(book);
        ctx.status(201).json(saved);
    }

    // PUT /books/{id}
    public void updateBook(Context ctx) {
        String id = ctx.pathParam("id");
        Book existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Book not found: " + id));

        Book updates = ctx.bodyAsClass(Book.class);
        existing.setTitle(updates.getTitle());
        existing.setAuthor(updates.getAuthor());
        existing.setYear(updates.getYear());

        repository.save(existing);
        ctx.json(existing);
    }

    // DELETE /books/{id}
    public void deleteBook(Context ctx) {
        String id = ctx.pathParam("id");
        repository.delete(id);
        ctx.status(204);
    }
}
```

### 组装应用

```java
// 文件：BookApiApplication.java
import io.javalin.Javalin;

public class BookApiApplication {
    public static void main(String[] args) {
        BookRepository repository = new BookRepository();
        BookController controller = new BookController(repository);

        // 预置一些数据
        repository.save(new Book(null, "三体", "刘慈欣", 2006));
        repository.save(new Book(null, "流浪地球", "刘慈欣", 2000));

        var app = Javalin.create()
            .get("/books", controller::getAllBooks)
            .get("/books/{id}", controller::getBook)
            .post("/books", controller::createBook)
            .put("/books/{id}", controller::updateBook)
            .delete("/books/{id}", controller::deleteBook)
            .start(7070);

        System.out.println("Book API started on http://localhost:7070");
    }
}
```

小北运行这段代码，然后用 curl 测试：

```bash
$ curl http://localhost:7070/books
[{"id":"1","title":"三体","author":"刘慈欣","year":2006},{"id":"2","title":"流浪地球","author":"刘慈欣","year":2000}]
```

"成功了！"小北兴奋地喊道。

### 关于 JSON 处理的注意事项

`ctx.bodyAsClass(Book.class)` 会自动把请求体的 JSON 转换成 Java 对象，但有几个坑要注意：

1. **必须有默认构造方法**：Jackson 需要无参构造来创建对象
2. **字段名要匹配**：JSON 的 `bookTitle` 不会自动映射到 Java 的 `title`
3. **类型要兼容**：JSON 的数字不能自动转换成 String

阿码尝试 POST 一个图书：

```bash
curl -X POST http://localhost:7070/books \
  -H "Content-Type: application/json" \
  -d '{"title":"球状闪电","author":"刘慈欣","year":"2004"}'
```

结果报了 500 错误。

"year 应该是数字，你传了字符串。"老潘看了一眼，"API 要处理这种边界情况——要么在接收时做类型转换，要么返回 400 告诉客户端请求格式错误。"

<!--
**Bloom 层次**：应用/分析
**学习目标**：能够实现完整的 RESTful CRUD 端点，处理 JSON 数据，区分参数类型
**贯穿案例推进**：图书管理 API 从框架到完整功能，实现所有 CRUD 端点
**建议示例文件**：04_book_controller.java, 04_book_service.java, 04_json_handling.java
**叙事入口**：从"Week 07 的 Repository 方法"自然过渡到 API 端点设计
**角色出场**：
- 阿码：发现 API 与 Repository 的对应关系，追问复用策略
- 小北：在实现 POST 端点时遇到 JSON 解析问题
- 老潘：讲解分层架构，强调复用已有代码
**回顾桥**：
- [Repository 模式]（week_07）：Service 层直接复用已有的 Repository 实现
- [单元测试]（week_06）：强调 API 也需要测试覆盖
-->

> **AI 时代小专栏：AI 生成 API 文档**
>
> OpenAPI（原 Swagger）是 REST API 的事实标准文档格式。2025-2026 年，AI 工具已经能够根据代码自动生成 OpenAPI 规范、根据自然语言描述生成 API 文档、根据 API 文档生成客户端 SDK。
>
> 一些顶级 AI 驱动 API 文档工具正在改变开发者的工作方式：
>
> **APItoolkit** - 从实时生产流量自动生成 OpenAPI/Swagger 文档，分析真实 API 请求以检测结构、字段和格式，自动识别新/更新的字段并提示文档更新。
>
> **Mintlify** - 专为 AI 智能体消费 API 而设计，专注于 AI 智能体可有效解析的结构化内容，帮助防止 AI 智能体进行未授权或过度的 API 调用。
>
> **Apidog** - 支持 MCP（模型上下文协议），可与 Cursor 等 AI 编码助手集成，提供设计优先方法和零配置模拟服务器。
>
> 2025-2026 年的关键趋势包括：AI 智能体消费（51% 的开发者担心 AI 智能体进行未授权 API 调用）、实时流量分析、MCP（模型上下文协议）与 AI 编码助手集成、设计优先方法防止文档漂移、AI 自动生成测试。
>
> 好的 API 文档应该包含端点的完整 URL 和 HTTP 方法、请求参数和请求体的 Schema、响应体的 Schema 和示例、可能的错误码和错误响应、使用示例（curl 或代码）。
>
> AI 可以帮你写初稿，但你需要验证每个字段、每个示例是否正确。毕竟，API 文档是契约，错误的契约比没有契约更糟糕。
>
> 参考（访问日期：2026-02-12）：
> - https://document360.com/blog/api-documentation-tools/
> - https://www.mintlify.com/blog/top-7-api-documentation-tools-of-2025
> - https://ferndesk.com/blog/best-api-documentation-tools

## 5. 当 API 出错时怎么办？

阿码兴冲冲地跑过来："我试了一下，故意传了个错的日期格式，你猜怎么着？"

"500 Internal Server Error，"他亮出手机屏幕，"而且这错误页面也太丑了吧，还暴露了一堆堆栈信息！"

小北凑过来看："这要是被黑客看到，不就知道我们用的什么框架了吗？"

"好问题。"老潘说，"REST API 需要用**状态码**（Status Code）传达结果。404 表示资源不存在，500 表示服务器内部错误，400 表示请求参数有问题……"

"但默认的错误页面太丑了，而且暴露太多信息。"

"所以你需要统一的异常处理。把所有异常转换成标准的错误响应格式，既友好又安全。"

### 自定义异常

```java
// 文件：ApiException.java
public class ApiException extends RuntimeException {
    private final int statusCode;

    public ApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

// 文件：NotFoundException.java
public class NotFoundException extends ApiException {
    public NotFoundException(String message) {
        super(404, message);
    }
}

// 文件：BadRequestException.java
public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(400, message);
    }
}
```

### 标准错误响应

```java
// 文件：ErrorResponse.java
public class ErrorResponse {
    private int code;
    private String message;
    private String details;
    private long timestamp;

    public ErrorResponse(int code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters...
}
```

### 全局异常处理器

```java
// 在 BookApiApplication.java 中添加
var app = Javalin.create()
    // ... 路由定义 ...
    .exception(ApiException.class, (e, ctx) -> {
        ErrorResponse error = new ErrorResponse(
            e.getStatusCode(),
            e.getMessage(),
            null  // 生产环境不要暴露内部细节
        );
        ctx.status(e.getStatusCode()).json(error);
    })
    .exception(Exception.class, (e, ctx) -> {
        // 未知异常，记录日志但返回通用错误
        e.printStackTrace();  // 实际应用应该用日志框架
        ErrorResponse error = new ErrorResponse(
            500,
            "Internal server error",
            null
        );
        ctx.status(500).json(error);
    })
    .start(7070);
```

现在当小北访问 `GET /books/999` 时，会收到：

```json
{
  "code": 404,
  "message": "Book not found: 999",
  "details": null,
  "timestamp": 1707753600000
}
```

"这样好多了。"小北说，"客户端可以根据 code 做不同的处理。"

"而且不要把堆栈信息返回给用户。"老潘强调，"那是给开发人员看的，暴露给客户端是安全隐患。"

阿码问："那如果我想区分'图书不存在'和'用户没有权限查看'呢？"

"用 403 Forbidden。"老潘说，"404 是'我没找到'，403 是'我找到了但你不许看'。"

### 回顾 Week 03 的异常处理与受检异常

还记得 Week 03 学的异常处理吗？那时候我们处理的是 CLI 程序的**受检异常**（Checked Exception）——捕获、记录、给用户友好的提示。

API 层的异常处理是同样的思路，但输出格式不同：
- CLI：打印到控制台
- API：返回 JSON 错误响应

这种分层处理的思想是一致的：底层抛出具体异常，上层决定如何呈现。Week 03 里你写的 `try-catch` 块和自定义异常类，在 Web 服务里依然适用——只是现在异常处理器变成了 Javalin 的全局处理器，而不是 `main` 方法里的 `try-catch`。

<!--
**Bloom 层次**：应用/评价
**学习目标**：能够正确使用 HTTP 状态码，实现统一异常处理，设计标准错误响应
**贯穿案例推进**：为图书管理 API 添加异常处理和标准错误响应
**建议示例文件**：05_exception_handler.java, 05_error_response.java, 05_api_tests.java
**叙事入口**：从"GET 不存在的 ID 返回什么"这个具体问题切入
**角色出场**：
- 小北：面对错误处理不一致的困惑
- 老潘：讲解状态码语义和 API 安全最佳实践
- 阿码：追问"要不要返回堆栈信息给用户"
**回顾桥**：
- [异常处理]（week_03）：在 API 层统一处理异常，回顾 try-catch 和自定义异常
- [策略模式]（week_08）：异常处理也可以使用策略模式进行扩展
-->

## 6. 测试你的 API

代码写完了，怎么验证它真的工作？

"你可以用 curl，"老潘说，"或者用 IDE 内置的 HTTP 客户端。Postman 也是个好选择。"

阿码看着一长串 curl 命令，皱起眉头："这么多命令，我每次都要手动敲吗？这也太麻烦了。"

"当然不用，"老潘笑了，"手动测试只是开始。你可以写自动化测试——让代码帮你发请求、验证响应。"

```bash
# 获取所有图书
curl http://localhost:7070/books

# 创建新图书
curl -X POST http://localhost:7070/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Java核心技术","author":"Cay Horstmann","year":2020}'

# 获取指定图书
curl http://localhost:7070/books/1

# 更新图书
curl -X PUT http://localhost:7070/books/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Java核心技术 卷I","author":"Cay Horstmann","year":2021}'

# 删除图书
curl -X DELETE http://localhost:7070/books/1
```

阿码看着这些命令："这比在 Java 里写测试简单多了！"

"手动测试适合开发调试，但自动化测试还是少不了。"老潘说，"你可以用 JUnit 配合 HTTP 客户端库（如 Java 11 的 HttpClient）写集成测试。"

### 集成测试示例

```java
// 文件：BookApiTest.java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.net.http.*;
import java.net.URI;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookApiTest {
    private static HttpClient client;
    private static final String BASE_URL = "http://localhost:7070";

    @BeforeAll
    static void setUp() {
        client = HttpClient.newHttpClient();
        // 启动服务器（实际项目中应该提取为可复用的启动逻辑）
        BookApiApplication.main(new String[]{});
    }

    @Test
    @Order(1)
    void testCreateBook() throws Exception {
        String json = "{\"title\":\"测试图书\",\"author\":\"测试作者\",\"year\":2024}";

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("测试图书"));
    }

    @Test
    @Order(2)
    void testGetAllBooks() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/books"))
            .GET()
            .build();

        HttpResponse<String> response = client.send(request,
            HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("测试图书"));
    }
}
```

### 单元测试 vs 集成测试

Week 06 我们学的单元测试是针对单个方法的——给定输入，验证输出。但 API 测试是**集成测试**——它测试整个请求处理链路：路由解析、参数绑定、业务逻辑、JSON 序列化。

| 测试类型 | 测试对象 | 速度 | 依赖 |
|----------|----------|------|------|
| 单元测试 | 单个类/方法 | 快 | 无外部依赖 |
| 集成测试 | 多个组件协作 | 慢 | 需要数据库、服务器等 |

"两种测试都要写。"老潘说，"单元测试保证每个零件正常，集成测试保证装在一起能跑。"

<!--
**Bloom 层次**：应用
**学习目标**：能够使用 curl/IDE 工具测试 REST API，理解集成测试的概念
**贯穿案例推进**：完整测试图书管理 API 的所有端点，验证功能正确性
**建议示例文件**：06_api_testing.md（curl 命令示例）, 06_api_integration_test.java
**叙事入口**：从"怎么验证 API 工作正常"这个实际问题切入
**角色出场**：
- 阿码：发现手动测试的便捷性，但被提醒自动化测试的重要性
- 老潘：介绍 curl、IDE 工具、集成测试的不同使用场景
**回顾桥**：
- [单元测试]（week_06）：对比单元测试与集成测试的区别和适用场景
-->

---

## CampusFlow 进度：从 CLI 到 Web 服务

经过本周的学习，现在该把 CampusFlow 从 CLI 版改造成 Web 服务了。

### 本周改造计划

1. **添加 Javalin 依赖**到 `pom.xml`
2. **设计 CampusFlow API**：
   - `GET /tasks` —— 获取所有任务
   - `GET /tasks/{id}` —— 获取指定任务
   - `POST /tasks` —— 创建任务
   - `PUT /tasks/{id}` —— 更新任务
   - `DELETE /tasks/{id}` —— 删除任务
   - `GET /tasks/{id}/overdue-fee` —— 计算逾期费用（复用策略模式）
3. **复用现有代码**：Service 层和 Repository 层基本不变，只需添加 Controller 层
4. **统一异常处理**：将 Week 03 的异常处理转换为 API 错误响应
5. **编写 ADR-003**：记录 API 设计决策

### 关键代码结构

```java
// CampusFlowController.java - 新增
public class CampusFlowController {
    private final TaskService taskService;  // Week 08 重构后的 Service

    public CampusFlowController(TaskService taskService) {
        this.taskService = taskService;
    }

    public void getAllTasks(Context ctx) {
        List<Task> tasks = taskService.findAll();
        ctx.json(tasks);
    }

    public void getTask(Context ctx) {
        String id = ctx.pathParam("id");
        Task task = taskService.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found: " + id));
        ctx.json(task);
    }

    public void createTask(Context ctx) {
        Task task = ctx.bodyAsClass(Task.class);
        Task saved = taskService.create(task);
        ctx.status(201).json(saved);
    }

    // PUT、DELETE 类似...

    public void calculateOverdueFee(Context ctx) {
        String id = ctx.pathParam("id");
        Task task = taskService.findById(id)
            .orElseThrow(() -> new NotFoundException("Task not found: " + id));

        // 复用 Week 08 的策略模式
        FeeCalculationStrategy strategy = new StandardFeeStrategy();
        double fee = strategy.calculate(task.getDueDate(), LocalDate.now());

        ctx.json(Map.of("taskId", id, "overdueFee", fee));
    }
}
```

### ADR-003 模板

```markdown
# ADR-003: 引入 REST API 架构

## 状态
已接受（2026-02-12）

## 背景
CampusFlow 目前是一个 CLI 应用，只能在本地运行。为了支持多用户访问和跨设备使用，需要将其改造为 Web 服务。

## 决策
我们决定：
1. 使用 Javalin 框架构建 REST API（轻量、学习曲线平缓）
2. 复用现有的 Service 和 Repository 层（不重复造轮子）
3. 采用标准 RESTful 设计（资源命名、HTTP 方法、状态码）
4. 引入 Jackson 进行 JSON 序列化
5. 实现统一异常处理机制

## 后果

### 正面
- 用户可以通过任何 HTTP 客户端访问 CampusFlow
- 为下周的 AI 生成前端打下基础
- 现有业务逻辑代码高度复用

### 负面
- 需要学习新的框架和概念（Javalin、REST、JSON）
- 增加了部署复杂度（需要持续运行服务）
- 需要考虑并发访问和数据一致性

## 替代方案

### 替代方案 1：使用 Spring Boot
- 优点：功能丰富、生态成熟
- 缺点：过于复杂、启动慢、与课程极简理念不符
- 结论：拒绝

### 替代方案 2：保持 CLI 版，添加导出功能
- 优点：简单、无需学习新技术
- 缺点：无法支持实时协作、移动端访问
- 结论：拒绝

## 参考
- Week 09: REST API 设计与 Javalin 入门
- Javalin 官方文档: https://javalin.io/documentation
```

老潘看了这份规划："记住，API 设计要面向未来。现在只有任务管理，但以后可能要加用户系统、权限控制。你的 URI 设计要预留扩展空间。"

小北点点头。他意识到，从 CLI 到 Web 不只是技术的迁移，更是思维方式的转变——从"单机程序"到"网络服务"，从"我知道用户是谁"到"任何人都能访问"。

---

## Git 本周要点

Web 项目有一些新的 Git 注意事项：

**忽略构建产物**：在 `.gitignore` 中添加：
```
target/
*.class
*.jar
*.db
```

**依赖管理**：`pom.xml` 修改后需要提交，但 `target/` 目录不需要。

**配置文件分离**：数据库 URL、端口号等配置应该外部化，不要硬编码在代码里。可以使用环境变量或配置文件。

**API 契约变更**：如果修改了 API 的 URL、参数或响应格式，这是破坏性变更。需要在提交信息中明确标注：
```
feat(api)!: change POST /tasks response format

BREAKING CHANGE: response now includes 'createdAt' field
```

---

## 本周小结（供下周参考）

本周你完成了 CampusFlow 的 Web 化改造。

从理解 REST 架构风格开始，你学会了识别资源、设计 URI、选择 HTTP 方法。然后你用 Javalin 框架搭建了一个真正的 Web 服务，实现了完整的 CRUD API。JSON 数据的处理、统一的异常处理、恰当的 HTTP 状态码——这些细节让你的 API 既功能完整又专业可靠。

更重要的是，你理解了分层架构的价值：Service 层和 Repository 层从 CLI 版几乎原封不动地复用，只有最外层的"交互层"从命令行变成了 HTTP。这种设计让代码可以适应不同的使用场景。

小北第一次用浏览器访问自己写的 API 时，那种成就感是无与伦比的。阿码尝试用 AI 生成 API 端点代码，也学会了审查 AI 的设计建议。老潘的"API 是契约"的提醒，会成为你日后设计接口的准则。

但一个只有后端的 Web 服务是不完整的。下周，你将进入 AI 协作期——学习如何让 AI 生成前端代码，并学会审查它的输出质量。这是 AI 时代工程师的核心技能：不只是用 AI，而是学会判断 AI 输出的质量。

---

## Definition of Done（学生自测清单）

- [ ] 我能解释 REST 的六个核心原则（资源、URI、HTTP 方法、状态码、无状态、统一接口）
- [ ] 我能设计符合 RESTful 规范的 API 端点（正确的资源命名、HTTP 方法选择）
- [ ] 我能使用 Javalin 框架创建 Web 服务并实现路由
- [ ] 我能处理 JSON 数据的序列化和反序列化（使用 Jackson 或 Gson）
- [ ] 我能实现统一的 API 异常处理，返回标准的错误响应格式
- [ ] 我能使用 curl 或 IDE 工具测试 REST API
- [ ] 我完成了 CampusFlow 的 Web 化改造，API 可以正常运行
- [ ] 我编写了 ADR-003，记录了 API 设计决策
