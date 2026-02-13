# Week 12：集成测试与 Bug Bash

> "单元测试能证明各个零件能跑，但只有集成测试能证明它们在一起也能跑。"
> — 老潘

2026 年的软件工程现场，前后端分离架构已经成为标配，但集成测试的复杂度也随之上升。研究数据显示，约 60% 的生产事故发生在"各个组件单独测试通过，但集成后失败"的场景。与此同时，AI 代码生成正在加速这一挑战——AI 生成的前端代码可能调用不存在的 API，AI 生成的后端代码可能返回意外的数据格式。传统的"手动联调"正在被"自动化集成测试 + Bug Bash"工程实践替代——前者保证系统的可验证性，后者通过团队协作发现边界情况。本周你将学习如何让系统从"各自能跑"走向"整体能跑"，并在 Bug Bash 中体验团队找 bug 的工程文化。

---

## 前情提要

上周你为 CampusFlow 建立了质量门禁——SpotBugs 扫描潜在 bug，JaCoCo 测量测试覆盖率，技术债 Backlog 跟踪修复计划。小北看着红色的覆盖率报告一点点变绿，老潘点头："质量保障体系建起来了。"

周二下午，小北决定把前后端联调一下。

"前端用 AI 生成的代码，后端 API 也测过了，应该没问题吧？"小北一边想一边启动了 Javalin 服务器，打开浏览器。

前端加载了，任务列表却一直是空的。小北打开浏览器控制台，看到一行红色的错误：

```
GET http://localhost:8080/api/tasks 404 (Not Found)
```

"奇怪？"小北检查后端日志，发现 API 端点是 `/tasks` 而不是 `/api/tasks`。

"前端调用了 `/api/tasks`，但后端只暴露了 `/tasks`。"老潘看了眼代码，"这是典型的集成问题——前后端契约对不上。"

阿码凑过来："单元测试都通过了啊？"

"单元测试测的是后端自己的逻辑，没有测前端调用的 URL。"老潘说，"你需要集成测试——启动真实服务，发送 HTTP 请求，验证完整链路。"

小北若有所思："所以各自能跑，不代表在一起也能跑？"

"没错。"老潘说，"这就是本周的主题：从'各自能跑'到'整体能跑'。"

---

## 学习目标

完成本周学习后，你将能够：

1. **理解** 集成测试的价值与局限，知道它与单元测试的区别（Bloom：理解）
2. **应用** 测试替身（Mock/Stub），隔离外部依赖（Bloom：应用）
3. **分析** 端到端测试与契约测试的场景，选择合适的测试策略（Bloom：分析）
4. **评价** Bug Bash 的工程价值，理解团队协作找 bug 的文化（Bloom：评价）
5. **创造** 适合自己项目的集成测试策略和 Bug Bash 流程（Bloom：创造）

---

<!--
贯穿案例设计：【集成测试系统——从"各自能跑"到"整体能跑"】
- 第 1 节：从 CampusFlow 前后端联调失败的真实场景出发，理解"各自能跑不代表整体能跑"
- 第 2 节：编写集成测试，启动真实服务，验证前后端通信
- 第 3 节：使用测试替身（Mock）隔离外部依赖（如数据库）
- 第 4 节：学习 Bug Bash 工程实践，团队协作找 bug
- 第 5 节：CampusFlow 进度——编写集成测试，组织 Bug Bash，修复发现的问题
最终成果：一个有完整集成测试、经过 Bug Bash 验证的 CampusFlow Web 版

认知负荷预算检查：
- 本周新概念（AI 时代工程阶段上限 5 个）：
  1. 测试替身 (Test Double) —— Mock/Stub 的概念，新概念
  2. Bug Bash —— 集体找 bug 的工程实践，新概念
  3. 端到端测试 (E2E Test) —— 用户视角的测试，新概念
  4. 契约测试 (Contract Test) —— API 契约验证，新概念
  5. 根因分析 (Root Cause Analysis) —— 问题定位方法论，新概念（但简单引入）
- 集成测试在 week_09 已引入，本周深化实践
- 结论：✅ 4-5 个新概念，在预算内

回顾桥设计（至少引用前 3-4 周的 2 个概念）：
- [单元测试]（来自 week_06）：在第 1 节对比单元测试与集成测试的区别
- [REST API]（来自 week_09）：在第 2 节为 API 编写集成测试
- [AI 审查检查清单]（来自 week_10）：在第 4 节 Bug Bash 中使用审查清单记录问题
- [静态分析]（来自 week_11）：在第 5 节 CampusFlow，结合静态分析和集成测试

AI 小专栏规划：
专栏 1（放在第 2 节之后，前段）：
- 主题：AI 时代的集成测试——LLM 能生成集成测试吗？
- 连接点：与第 2 节编写集成测试呼应

专栏 2（放在第 4 节之后，中段）：
- 主题：Bug Bash 与 AI 辅助——团队协作新范式
- 连接点：与第 4 节 Bug Bash 工程实践呼应

CampusFlow 本周推进：
- 上周状态：静态分析通过，质量门禁建立，但前后端未联调
- 本周改进：编写集成测试，组织 Bug Bash，发现并修复 API 契约、数据格式、边界情况等问题
- 涉及的本周概念：集成测试、测试替身、Bug Bash、契约测试

角色出场规划：
- 小北：在第 1 节遇到前后端联调失败的困惑；在第 3 节学习 Mock 的概念；在 Bug Bash 中发现 bug
- 阿码：在第 2 节质疑"为什么不直接用 E2E 测试"；在 Bug Bash 中与其他团队竞争
- 老潘：在第 1 节解释集成测试的价值；在第 4 节组织 Bug Bash；强调团队协作文化
-->

## 1. 各自能跑 ≠ 整体能跑——为什么需要集成测试

周五下午，小北看着屏幕陷入了沉思。

前端的单元测试通过了，后端的单元测试也通过了，但把两者放在一起——浏览器控制台报错 404。

"为什么单独测都行，一起跑就挂了？"小北忍不住问。

老潘停下敲键盘，转过身："因为单元测试只测'零件'，不测'组装'。"

"零件？"

"对。"老潘在白板上画了两个方框，"前端是一个零件，后端是另一个零件。单元测试确保前端自己的逻辑没问题，后端自己的逻辑也没问题。但两个零件要'拼起来'——前端发 HTTP 请求，后端返回 JSON——这个'拼装'的过程，单元测试不覆盖。"

阿码凑过来："那之前 Week 06 学的单元测试不够吗？"

"不够。"老潘说，"单元测试是在'隔离环境'下测试——不启动真实服务，不依赖外部系统。这保证了测试速度快、问题定位准。但集成问题（API 契约、数据格式、网络超时、数据库连接）只有真实环境下才能暴露。"

小北若有所思："所以集成测试是'真刀真枪'地跑？"

"对。"老潘说，"集成测试会启动真实的服务（如 Javalin），连接真实的数据库（或内存数据库），发送真实的 HTTP 请求，验证完整链路。这样能发现单元测试发现不了的问题。"

老潘打开一个集成测试的例子：

```java
// IntegrationTest.java
@Test
void getTasks_returnsEmptyList_initially() {
    // given: 启动 Javalin 服务器
    app = Javalin.create().start(8080);
    taskRepository = new TaskRepository();

    // when: 发送 HTTP GET 请求
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/tasks"))
            .GET()
            .build(), BodyHandlers.ofString());

    // then: 验证响应
    assertEquals(200, response.statusCode());
    assertEquals("[]", response.body());
}
```

"这个测试启动了真实的 Javalin 服务器，发送 HTTP 请求，验证响应。"老潘解释，"如果后端没有注册 `/api/tasks` 路由，测试会失败——这就是集成测试发现的问题。"

小北恍然大悟："所以前端调用 `/api/tasks`，但后端只暴露了 `/tasks`，这种契约问题只有集成测试能发现？"

"没错。"老潘说，"单元测试测的是'方法对不对'，集成测试测的是'接口对不对'。"

阿码插话："那为什么不直接用 E2E 测试（启动真实浏览器，模拟用户操作）？"

"好问题。"老潘说，"E2E 测试更接近真实用户场景，但也更慢、更脆弱。集成测试在'速度'和'真实性'之间取平衡——启动服务但不启动浏览器，用 HTTP 客户端模拟请求。"

小北若有所思："所以单元测试 → 集成测试 → E2E 测试，是一个金字塔？"

"对，这就是测试金字塔。"老潘在白板上画了个三角形，"底层是单元测试（多且快），中层是集成测试（适量），顶层是 E2E 测试（少且慢）。"

老潘指着金字塔："集成测试承上启下——发现单元测试发现不了的集成问题，又比 E2E 测试快。"

小北看着屏幕上的集成测试代码："那我得给 CampusFlow 写一些集成测试，确保前后端能正常通信。"

"没错。"老潘说，"先让'零件'能跑（单元测试），再让'整体'能跑（集成测试）。"

## 2. 编写集成测试——从 HTTP 请求到 JSON 响应

"现在来写第一个集成测试。"老潘打开测试文件。

"集成测试和单元测试有什么区别？"小北问。

老潘在屏幕上并排展示了两个测试：

```java
// 单元测试（不启动服务）
@Test
void getTasks_returnsEmptyList() {
    // given
    TaskRepository repository = new TaskRepository();
    TaskService service = new TaskService(repository);

    // when
    List<Task> tasks = service.getAllTasks();

    // then
    assertEquals(0, tasks.size());
}

// 集成测试（启动真实服务）
@Test
void getTasks_apiReturnsEmptyList() {
    // given: 启动 Javalin 服务器
    app = Javalin.create().start(8080);
    // 配置路由...

    // when: 发送真实 HTTP 请求
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/tasks"))
            .GET()
            .build(), BodyHandlers.ofString());

    // then: 验证 HTTP 状态码和响应体
    assertEquals(200, response.statusCode());
    assertEquals("[]", response.body());
}
```

"看出来区别了吗？"老潘问。

小北想了想："单元测试直接调用 `service.getAllTasks()`，集成测试启动了 Javalin 服务器，发送 HTTP 请求？"

"对。"老潘说，"集成测试走的是'完整链路'：HTTP 请求 → Javalin 路由 → Controller → Service → Repository → 响应序列化 → JSON 返回。"

"单元测试只测了 Service 层，集成测试测了所有层。"小北若有所思。

"没错。"老潘说，"集成测试能发现路由没注册、JSON 序列化错误、HTTP 状态码不对等单元测试发现不了的问题。"

老潘继续写第二个测试：

```java
@Test
void createTask_apiReturnsCreatedTask() throws Exception {
    // given: 启动服务器
    app = Javalin.create().start(8080);

    // when: POST 请求创建任务
    String jsonBody = "{\"title\":\"学习集成测试\",\"completed\":false}";
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/tasks"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build(), BodyHandlers.ofString());

    // then: 验证 201 Created 和返回的 JSON
    assertEquals(201, response.statusCode());
    assertTrue(response.body().contains("\"title\":\"学习集成测试\""));
}
```

"这个测试验证了 POST 请求。"老潘说，"如果前端发送的 JSON 格式不对（如缺少 `title` 字段），或者后端没有设置 `Content-Type` 头，集成测试会失败。"

阿码插话："那每次测试都要启动服务器？会不会很慢？"

"会。"老潘说，"所以集成测试要精选场景——测试'关键路径'，不要什么都测。比如创建任务、获取任务列表、更新任务状态，这些是核心功能，必须有集成测试。"

小北点头："所以集成测试是'抽样检查'，单元测试是'全面检查'？"

"说得好。"老潘笑了。

老潘补充："还有个技巧——用 `@BeforeEach` 启动服务器，`@AfterEach` 关闭服务器，确保每个测试都是干净的环境。"

```java
@BeforeEach
void setUp() {
    app = Javalin.create().start(8080);
    // 配置路由、数据库...
}

@AfterEach
void tearDown() {
    app.stop();
}
```

小北看着代码："现在我的测试有'单元测试'和'集成测试'两层了。"

"对。"老潘说，"单元测试保证'零件正确'，集成测试保证'组装正确'。"

> **AI 时代小专栏：AI 能生成集成测试吗？**
>
> 2025-2026 年，AI 工具在测试生成领域取得了显著进展。GitHub 已发布专门用于 .NET 的 AI 测试生成功能（Copilot Testing），可自动创建、运行和测试整个项目的代码。Copilot 还能生成 API 测试用例并简化测试工作流，支持微服务架构测试。学术研究显示，这类系统提供与代码库更新同步的 bug 检测、基于上下文检索的自动测试用例生成、以及检测问题的修复建议。
>
> 但实践反馈也指出：Copilot 在测试生成上是"有帮助但不是很好"——能节省可衡量的时间，但并不完美。AI 生成的集成测试能覆盖约 70-80% 的常规场景（CRUD 操作、正常流程），但在边界情况（网络超时、并发请求、数据损坏）方面仍有不足。
>
> 2026 年的工程实践显示，AI 擅长根据 OpenAPI/Swagger 文档生成基础集成测试，自动设置 HTTP 请求头、解析 JSON 响应；但不擅长设计复杂的测试场景（如模拟数据库故障、验证事务回滚），处理异步操作时序问题。
>
> 实际工作流正在变成："AI 生成测试骨架 → 人工补充边界情况 → 运行并修复"。AI 是倍增器，不是替代者——它能加速"写测试"的过程，但"测什么、怎么测"的决策得靠你。
>
> 老潘说："AI 能帮你写出'能跑'的集成测试，但只有你知道哪些场景值得测。"
>
> 参考资料（访问日期：2026-02-12）：
> - [Microsoft .NET Copilot Testing](https://devblogs.microsoft.com/dotnet/github-copilot-testing-for-dotnet/)
> - [Official GitHub Docs: Writing tests with Copilot](https://docs.github.com/en/copilot/tutorials/write-tests)
> - [Academic Paper on Copilot for Testing](https://arxiv.org/html/2504.01866v2)

## 3. 测试替身——当外部依赖太重时

"集成测试需要真实的数据库吗？"小北问。

老潘想了想："有两种选择——用真实数据库，或者用测试替身（Test Double）。"

"测试替身？"

"就像电影里的替身演员。"老潘说，"当你不想依赖真实的外部系统（如数据库、第三方 API）时，可以用'假'的对象替代。"

老潘打开一个例子：

```java
// 真实数据库集成测试
@Test
void getTasks_withRealDatabase() throws SQLException {
    // given: 创建真实 SQLite 连接
    Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
    TaskRepository repository = new TaskRepository(conn);

    // when: 从数据库查询
    List<Task> tasks = repository.findAll();

    // then: 验证结果
    assertEquals(0, tasks.size());
}

// Mock 测试（用测试替身）
@Test
void getTasks_withMockRepository() {
    // given: 创建 Mock 对象
    TaskRepository mockRepo = mock(TaskRepository.class);
    when(mockRepo.findAll()).thenReturn(List.of(new Task("测试任务")));

    TaskService service = new TaskService(mockRepo);

    // when: 调用 Service
    List<Task> tasks = service.getAllTasks();

    // then: 验证结果
    assertEquals(1, tasks.size());
}
```

"看出来区别了吗？"老潘问。

小北想了想："第一个测试连接了真实数据库，第二个用了'假'的 Repository？"

"对。"老潘说，"Mock 对象会'假装'返回某些数据，不需要真实数据库。这样测试更快、更稳定（不会因为数据库问题失败）。"

"那为什么不用 Mock？"阿码问。

"Mock 的缺点是'不够真实'。"老潘说，"如果 SQL 写错了，Mock 测试不会发现，因为根本没有执行 SQL。所以：
- **单元测试**：用 Mock，关注逻辑正确性
- **集成测试**：用真实数据库，关注系统集成"

小北若有所思："所以单元测试用 Mock，集成测试用真实数据库？"

"对。"老潘说，"这就是测试策略的平衡——速度 vs 真实性。"

老潘补充："Mock 有多种类型，不只是 `mock()` 一种。"

| 测试替身类型 | 用途 |
|------------|------|
| **Dummy** | 占位符，不使用，只是满足参数要求 |
| **Stub** | 预设返回值，不验证调用 |
| **Mock** | 预设返回值，验证是否被调用、调用几次、参数是什么 |
| **Spy** | 部分方法真实实现，部分方法 Mock |
| **Fake** | 简化实现（如内存数据库），但功能真实 |

"最常用的是 Stub 和 Mock。"老潘说，"Stub 只管'返回什么'，Mock 还管'有没有被调用'。"

老潘举了个例子：

```java
// Stub：只预设返回值
TaskRepository stubRepo = mock(TaskRepository.class);
when(stubRepo.findById("1")).thenReturn(Optional.of(new Task("任务1")));

// Mock：还验证调用
TaskService service = new TaskService(stubRepo);
service.deleteTask("1");

verify(stubRepo).delete("1");  // 验证是否调用了 delete
```

小北恍然大悟："所以 Mock 不只是'替身'，还能'检查演员有没有按剧本演'？"

"说得好！"老潘笑了。

但老潘提醒："不要过度 Mock。如果 Mock 太多，测试可能在跟'假对象'交互，而不是测试真实逻辑。经验法则——集成测试尽量少用 Mock，单元测试可以用。"

阿码若有所思："所以测试替身是'工具'，不是'目的'？"

"对。"老潘说，"目标是'发现问题'，Mock 只是手段之一。"

## 4. Bug Bash——团队协作找 bug

周五下午，老潘在白板上写了四个大字：**Bug Bash**。

"什么是 Bug Bash？"小北好奇。

"集体找 bug 的工程实践。"老潘说，"所有团队暂停开发，花 1-2 小时互相测试对方的系统，记录发现的问题。"

"为什么要互相测试？"阿码问。

"因为开发者对自己的系统有'盲区'。"老潘说，"你觉得'正常'的操作，别人可能觉得'奇怪'。新鲜视角能发现你没想到的问题。"

老潘展示了 Bug Bash 的流程：

```
1. 准备（15 分钟）
   - 每个团队准备好演示环境（部署在测试服务器）
   - 准备一个"测试指南"（核心功能列表、已知问题）

2. 交叉测试（60 分钟）
   - A 团队测试 B 团队的系统，B 测试 C，C 测试 A
   - 测试者尝试"破坏"系统：输入奇怪数据、快速点击、并发操作
   - 记录每个 bug 的复现步骤

3. 汇总（30 分钟）
   - 所有团队回到一起，报告发现的问题
   - 按优先级分类（P0 崩溃 / P1 功能缺陷 / P2 体验问题）
   - 评选"最奇葩 bug"奖

4. 修复（课后）
   - 每个团队修复自己系统的 bug
   - 下周分享修复经验
```

"听起来像'黑客松'的反向版？"小北笑了。

"有点像。"老潘说，"但目标是建设性的——发现问题而不是嘲笑。Bug Bash 的文化是'帮对方找 bug，就是帮自己提升质量'。"

老潘展示了 Bug Bash 报告模板：

```markdown
# Bug Bash 报告

## 测试者
团队：TaskForce
测试对象：CampusFlow（BookHub 团队）

## 发现的问题

| ID | 问题描述 | 复现步骤 | 优先级 |
|----|---------|---------|--------|
| #1 | 创建任务时输入超长标题（1000 字符），前端卡死 | 1. 打开创建任务对话框 2. 粘贴 1000 字符 3. 点击保存 | P1 |
| #2 | 快速连续点击"完成"按钮，任务状态闪烁 | 1. 打开任务列表 2. 快速点击"完成"按钮 5 次 | P2 |
| #3 | 任务标题包含 emoji 时，后端返回 500 错误 | 1. 创建任务，标题输入 "🔥🔥🔥" 2. 保存 | P0 |

## 反馈
- 界面设计很清晰
- 缺少错误提示（P1 和 P3 没有提示用户）
- 建议添加输入长度限制
```

"这个报告不只是问题列表，还有反馈和建议。"老潘说，"Bug Bash 的另一个价值是'互相学习'——看看别的团队怎么设计界面、怎么处理错误。"

阿码若有所思："所以 Bug Bash 不只是找 bug，还是团队交流？"

"对。"老潘说，"工程不只是代码，还有文化。Bug Bash 建立'质量是每个人的责任'的文化。"

小北举手："那 AI 能辅助 Bug Bash 吗？"

"可以。"老潘说，"AI 可以：
- 自动记录测试步骤（截图、录屏）
- 分析错误日志，生成 bug 报告
- 建议测试场景（边界情况、并发操作）

但 AI 不能替代人的创造力——人类能想出'奇怪'的测试场景，AI 只能基于历史数据。"

老潘举例："比如 A 团队发现'快速点击导致状态闪烁'，这是人的直觉测试，AI 不一定能想到。"

小北若有所思："所以 Bug Bash 是'人类智慧'的展示场？"

"说得好。"老潘笑了。

老潘补充："Bug Bash 完成后，要做根因分析（Root Cause Analysis）。不是只修复表面问题，而是问'为什么会出现这个 bug'——是设计缺陷？代码质量问题？还是测试盲区？"

"比如 P3 的 emoji 问题。"老潘说，"根因可能是数据库字符集设置不对，或者前端输入验证缺失。找到根因，才能避免同类问题再次发生。"

小北想起 Week 03 学的**运行时异常**（runtime exception）："emoji 导致的 500 错误，可能就是字符编码异常被抛成了运行时异常，但没有被捕获？"

"对。"老潘说，"当时我们学过——运行时异常通常表示编程错误，应该在开发阶段发现。Bug Bash 就是在'模拟用户场景'时，把这些隐藏的运行时异常找出来。"

小北点头："所以 Bug Bash 不只是'找 bug'，还是'学习机会'？"

"对。"老潘说，"每个 bug 都是'教训'——理解根因，改进流程，下次不犯。"

> **AI 时代小专栏：Bug Bash 与 AI 辅助——团队协作新范式**
>
> 2025-2026 年，AI 工具正在改变 bug bounty 和调试领域。AI 渗透测试工具在 2026 年专注于业务逻辑缺陷检测和可扩展的持续安全测试。同时，AI 辅助开发时代最值得追捕的 bug 是那些 AI 生成代码中常见的 web 应用问题。AI 静态应用安全测试（SAST）工具也在快速发展，为 Bug Bash 提供了新的武器。
>
> 2026 年的工程实践显示：
> - **AI 擅长**：自动记录测试步骤、分析错误日志、建议相似问题的历史解决方案
> - **人类擅长**：创造性的测试场景（"如果这么操作会怎样"）、跨系统的复杂交互、用户体验判断
>
> 真正有效的 Bug Bash 是"人类直觉 + AI 工具"的组合——人类想出测试场景，AI 帮忙记录和分析。行业观察显示，这种组合比纯人工 Bug Bash 效率高 2-3 倍，因为 AI 能处理重复性工作，让人类专注于创造性测试。
>
> 老潘说："AI 能帮你记录 bug，但'怎么测'得靠你的创造力。"
>
> 参考资料（访问日期：2026-02-12）：
> - [Top 5 AI Tools Every Bug Bounty Hunter Should Know](https://medium.com/data-and-beyond/top-5-ai-tools-every-bug-bounty-hunter-should-know-1d7638598f74)
> - [Best AI Pentesting Tools in 2026](https://escape.tech/blog/best-ai-pentesting-tools/)
> - [The Most Valuable Bugs to Hunt in the AI-Assisted Development Era](https://blogs.jsmon.sh/the-most-valuable-bugs-to-hunt-in-the-ai-assisted-development-era/)
> - [Top 10 AI-powered SAST Tools in 2026](https://www.aikido.dev/blog/top-10-ai-powered-sast-tools-in-2025)

## 5. 契约测试——API 契约的硬约束

周三下午，小北在 Bug Bash 中发现了一个奇怪的问题。

"BookHub 的 API 返回的字段名是 `taskName`，但前端调用的是 `title`。"小北指着代码，"这种契约问题，集成测试能发现吗？"

老潘看了看："集成测试如果'前后端一起写'，能发现。但如果是'两个团队分别开发'（一个写后端，一个写前端），契约问题会很常见。"

"那怎么预防？"

"契约测试（Contract Test）。"老潘说，"把 API 契约写成测试，确保前后端遵守约定。"

老潘打开一个例子：

```java
// 契约测试：验证 API 返回的 JSON 格式
@Test
void getTask_apiReturnsExpectedJsonFormat() throws Exception {
    // given: 启动服务器，准备测试数据
    app = Javalin.create().start(8080);
    Task task = new Task("测试任务");
    taskRepository.save(task);

    // when: 请求任务详情
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/tasks/" + task.getId()))
            .GET()
            .build(), BodyHandlers.ofString());

    // then: 验证 JSON 契约（字段名、类型）
    String json = response.body();
    assertTrue(json.contains("\"id\":"));
    assertTrue(json.contains("\"title\":"));  // 不是 taskName
    assertTrue(json.contains("\"completed\":"));
    assertFalse(json.contains("\"taskName\":"));  // 前端不认这个字段
}
```

"这个测试不只是验证'有没有数据'，还验证'字段名对不对'。"老潘说，"如果后端把 `title` 改成 `taskName`，测试会失败——契约被破坏了。"

阿码插话："所以契约测试是'前后端的合同'？"

"对。"老潘说，"契约测试的核心思想是：
1. **定义契约**：用 OpenAPI/Swagger 文档定义 API 规范（路径、方法、请求体、响应体）
2. **测试契约**：后端测试确保'实现符合契约'，前端测试确保'调用符合契约'
3. **持续验证**：每次代码变更都运行契约测试，确保不被破坏"

老潘展示了契约测试的工作流：

```
┌─────────┐    OpenAPI 文档    ┌─────────┐
│ 前端团队 │ ─────────────────→ │ 后端团队 │
└─────────┘                    └─────────┘
     │                              │
     │ 生成前端契约测试              │ 生成后端契约测试
     ↓                              ↓
┌─────────┐                    ┌─────────┐
│ 前端测试 │                    │ 后端测试 │
└─────────┘                    └─────────┘
     │                              │
     └──────── 验证通过，契约不被破坏 ────────┘
```

小北若有所思："所以契约测试是'预防式'的——不等联调失败，在开发阶段就发现问题？"

"对。"老潘说，"契约测试的哲学是'测试文档，而不是猜测文档'。API 文档（OpenAPI）是'活的契约'，契约测试确保代码和文档一致。"

老潘补充："如果你的项目有多个团队（前端、后端、移动端），契约测试特别有价值——它是'团队间的硬约束'。"

小北点头："所以 CampusFlow 虽然是单人/双人项目，但契约测试的思维可以应用到'API 设计'上——先定义契约，再实现功能。"

"没错。"老潘说，"契约测试不只是测试方法，更是一种设计思维——API 先于实现。"

---

## CampusFlow 进度：集成测试与 Bug Bash

经过本周的学习，现在该为 CampusFlow 建立完整的集成测试和 Bug Bash 流程了。

### 本周改造计划

1. **编写集成测试**：
   - 创建 `IntegrationTest.java`，测试关键 API（GET /api/tasks、POST /api/tasks、PUT /api/tasks/:id）
   - 使用 `HttpClient` 发送真实 HTTP 请求
   - 验证 HTTP 状态码、JSON 响应体

2. **使用测试替身**：
   - 为单元测试添加 Mock（Mockito），隔离数据库依赖
   - 为集成测试使用内存数据库（H2），加快测试速度

3. **编写契约测试**：
   - 为 API 定义 OpenAPI 规范（可选）
   - 编写契约测试，验证 JSON 字段名、类型

4. **组织 Bug Bash**：
   - 准备测试环境（部署到测试服务器）
   - 邀请其他团队测试 CampusFlow
   - 记录发现的问题，修复并总结

### 集成测试示例

```java
// IntegrationTest.java
class TaskApiIntegrationTest {

    private Javalin app;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        // 使用内存数据库
        taskRepository = new TaskRepository("jdbc:h2:mem:test");

        // 启动 Javalin 服务器
        app = Javalin.create()
            .start(8080)
            .get("/api/tasks", ctx -> {
                List<Task> tasks = taskRepository.findAll();
                ctx.json(tasks);
            })
            .post("/api/tasks", ctx -> {
                Task task = ctx.bodyAsClass(Task.class);
                taskRepository.save(task);
                ctx.status(201).json(task);
            });
    }

    @AfterEach
    void tearDown() {
        app.stop();
    }

    @Test
    void getTasks_returnsEmptyList_initially() throws Exception {
        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/tasks"))
                .GET()
                .build(), BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("[]", response.body());
    }

    @Test
    void createTask_returns201AndTask() throws Exception {
        String jsonBody = "{\"title\":\"集成测试任务\",\"completed\":false}";

        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/tasks"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonBody))
                .build(), BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("\"title\":\"集成测试任务\""));
    }
}
```

### Bug Bash 报告示例

```markdown
# CampusFlow Bug Bash 报告

## 测试环境
- URL: http://test-server.campusflow.com
- 测试时间: 2026-02-14 14:00-16:00
- 测试者: 3 个团队（BookHub、RoomReserve、SpendWise）

## 发现的问题

| ID | 问题描述 | 复现步骤 | 优先级 | 根因 |
|----|---------|---------|--------|------|
| #1 | 任务标题包含 emoji 时返回 500 | 创建任务，输入 "🔥🔥🔥" | P0 | 数据库字符集未设置为 UTF-8 |
| #2 | 快速点击"完成"导致状态闪烁 | 连续点击 5 次 | P2 | 前端未防抖 |
| #3 | 任务列表超过 100 条时前端卡顿 | 创建 100+ 任务 | P1 | 前端未分页 |

## 修复计划
- #1: 本周修复（修改数据库连接字符串）
- #2: 下周修复（添加前端防抖）
- #3: 加入技术债 Backlog（P3）

## 反馈
- API 设计清晰，容易理解
- 缺少分页功能
- 错误提示不够友好
```

老潘看了这份计划："很好。集成测试确保'前后端能通信'，Bug Bash 发现'真实用户场景的问题'。下周你将学习文档与知识传递——怎么把你的 API 设计、测试经验沉淀下来，传递给其他人。"

---

## Git 本周要点

### 集成测试与 Git

**分支策略**：

```bash
# 为集成测试创建分支
git checkout -b feature/integration-tests

# 编写测试、修复问题...

# 提交时运行完整测试套件（单元 + 集成）
git commit -m "test(integration): add API integration tests

- Add integration tests for GET/POST/PUT endpoints
- Use H2 in-memory database for faster tests
- Fix API contract issue: title instead of taskName

Test results:
- Unit tests: 42/42 passed
- Integration tests: 5/5 passed
"
```

---

## 本周小结（供下周参考）

本周你让 CampusFlow 从"各自能跑"走向"整体能跑"。

集成测试让你发现了单元测试发现不了的问题——API 契约不匹配、JSON 字段名错误、HTTP 状态码不对。这些问题在真实环境下才会暴露，而集成测试就是"预演"真实环境。你学会了用 `HttpClient` 发送真实 HTTP 请求，验证完整链路。这不是"替代"单元测试，而是"补充"——单元测试保证逻辑正确，集成测试保证系统组装正确。

测试替身（Mock/Stub）让你理解了"隔离依赖"的价值。单元测试用 Mock，关注逻辑而不依赖外部系统；集成测试用真实数据库，关注系统集成。关键是"知道测什么"——单元测试测"方法对不对"，集成测试测"接口对不对"，E2E 测试测"用户体验对不对"。

Bug Bash 让你体验了团队协作找 bug 的工程文化。开发者对自己的系统有盲区，新鲜视角能发现你没想到的问题。Bug Bash 不只是"找 bug"，更是"互相学习"——看看别的团队怎么设计、怎么处理错误。你学会了记录 bug、分析根因，不只是修复表面问题，而是理解"为什么会出现"。

契约测试让你理解了 API 契约的重要性。契约是"前后端的合同"，契约测试确保"实现符合文档"。你的 CampusFlow 现在有了集成测试、契约测试，经过 Bug Bash 的验证——前后端能正常通信，API 契约不会被破坏，真实用户场景的问题被发现并修复。

CampusFlow 现在是一个"能联调、能验证、能交付"的 Web 应用。但还有一个关键问题：怎么把这些知识传递给其他人？下周，你将学习文档与知识传递——API 文档、README、ADR 汇总，还有 AI 辅助文档生成（当然，需要审查）。

---

## Definition of Done（学生自测清单）

- [ ] 我能解释集成测试的价值与局限，知道它与单元测试的区别
- [ ] 我能编写集成测试，使用 HttpClient 发送 HTTP 请求
- [ ] 我能使用测试替身（Mock/Stub）隔离外部依赖
- [ ] 我能分析契约测试的场景，选择合适的测试策略
- [ ] 我能评价 Bug Bash 的工程价值，理解团队协作文化
- [ ] 我完成了 CampusFlow 的集成测试，至少 3 个测试用例
- [ ] 我参与了 Bug Bash，发现了至少 2 个 bug 并修复
- [ ] 我理解了"单元测试 → 集成测试 → E2E 测试"的测试金字塔
- [ ] 我理解了契约测试的价值，API 契约是"前后端的合同"
