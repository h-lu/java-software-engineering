# Week 12 作业：集成测试与 Bug Bash

> "单元测试能证明各个零件能跑，但只有集成测试能证明它们在一起也能跑。"
> —— 老潘

---

## 作业概述

本周你将让 CampusFlow 从"各自能跑"走向"整体能跑"。你将编写集成测试验证前后端通信，使用测试替身（Mock）隔离外部依赖，并参与 Bug Bash 活动发现和修复系统问题。

**核心任务**：
1. 编写 API 集成测试，使用 HttpClient 发送真实 HTTP 请求
2. 使用 Mockito 编写 Mock 测试，隔离外部依赖
3. 参与团队 Bug Bash，发现至少 2 个 bug 并完成修复
4. 编写 Bug Bash 报告和根因分析

---

## 学习目标（对应章节）

完成本周作业后，你将能够：
- **理解** 集成测试的价值与局限（Bloom：理解）
- **应用** 测试替身（Mock/Stub）隔离外部依赖（Bloom：应用）
- **分析** 端到端测试与契约测试的场景（Bloom：分析）
- **评价** Bug Bash 的工程价值（Bloom：评价）
- **创造** 适合自己项目的集成测试策略（Bloom：创造）

---

## 作业背景

上周你为 CampusFlow 建立了质量门禁——SpotBugs 发现潜在 bug，JaCoCo 测量测试覆盖率。但小北在前后端联调时遇到了问题：前端调用 `/api/tasks`，后端只暴露了 `/tasks`。

这是典型的"集成问题"——单元测试测的是"方法对不对"，集成测试测的是"接口对不对"。

本周你需要：
1. 为 CampusFlow 编写集成测试，确保前后端能正常通信
2. 使用 Mock 替身隔离外部依赖，加快测试速度
3. 参与 Bug Bash，通过团队协作发现边界情况
4. 修复发现的问题，提升系统质量

---

## 基础作业（必做，100 分）

### 任务 1：编写 API 集成测试（40 分）

**目标**：为 CampusFlow 的 REST API 编写集成测试，验证前后端通信。

**背景知识**：
集成测试会启动真实的 Javalin 服务器，发送真实的 HTTP 请求，验证完整链路。这能发现单元测试发现不了的问题：
- API 契约不匹配（如前端调用 `/api/tasks`，后端暴露 `/tasks`）
- JSON 字段名错误（如返回 `taskName` 而不是 `title`）
- HTTP 状态码不对（如应该返回 201 却返回 200）

**要求**：

#### 1.1 创建集成测试类（5 分）

创建 `TaskApiIntegrationTest.java`，配置测试环境：

```java
class TaskApiIntegrationTest {

    private Javalin app;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        // 使用内存数据库
        taskRepository = new TaskRepository("jdbc:h2:mem:test");

        // 启动 Javalin 服务器
        app = Javalin.create().start(8080);
        // 配置路由...
    }

    @AfterEach
    void tearDown() {
        app.stop();
    }
}
```

#### 1.2 编写 GET /api/tasks 测试（10 分）

验证获取任务列表 API：

```java
@Test
void getTasks_returnsEmptyList_initially() throws Exception {
    // when: 发送 HTTP GET 请求
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/tasks"))
            .GET()
            .build(), BodyHandlers.ofString());

    // then: 验证 HTTP 状态码和响应体
    assertEquals(200, response.statusCode());
    assertEquals("[]", response.body());
}

@Test
void getTasks_returnsListOfTasks() throws Exception {
    // given: 准备测试数据
    taskRepository.save(new Task("任务1"));

    // when: 发送请求
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/tasks"))
            .GET()
            .build(), BodyHandlers.ofString());

    // then: 验证响应
    assertEquals(200, response.statusCode());
    assertTrue(response.body().contains("任务1"));
}
```

#### 1.3 编写 POST /api/tasks 测试（10 分）

验证创建任务 API：

```java
@Test
void createTask_returns201AndTask() throws Exception {
    // given: 准备请求体
    String jsonBody = "{\"title\":\"集成测试任务\",\"completed\":false}";

    // when: POST 请求创建任务
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/tasks"))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(jsonBody))
            .build(), BodyHandlers.ofString());

    // then: 验证 201 Created 和返回的 JSON
    assertEquals(201, response.statusCode());
    assertTrue(response.body().contains("\"title\":\"集成测试任务\""));
}
```

#### 1.4 编写错误场景测试（10 分）

测试边界和异常情况：

```java
@Test
void getTask_byId_notFound_returns404() throws Exception {
    // when: 请求不存在的任务
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/tasks/999"))
            .GET()
            .build(), BodyHandlers.ofString());

    // then: 验证 404 Not Found
    assertEquals(404, response.statusCode());
}

@Test
void createTask_invalidTitle_returns400() throws Exception {
    // given: 空标题
    String jsonBody = "{\"title\":\"\",\"completed\":false}";

    // when: POST 请求
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/tasks"))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(jsonBody))
            .build(), BodyHandlers.ofString());

    // then: 验证 400 Bad Request
    assertEquals(400, response.statusCode());
}
```

#### 1.5 编写契约测试（5 分）

验证 API 契约（JSON 字段名、类型）：

```java
@Test
void getTask_apiReturnsExpectedJsonFormat() throws Exception {
    // given: 准备测试数据
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
}
```

**提交物**：
- `TaskApiIntegrationTest.java`：集成测试类
- `integration_test_results.txt`：测试运行结果（`mvn test` 的输出）

**评分要点**：
- 创建集成测试类，配置 @BeforeEach 和 @AfterEach（5 分）
- GET /api/tasks 测试（10 分）
- POST /api/tasks 测试（10 分）
- 错误场景测试（10 分）
- 契约测试（5 分）

---

### 任务 2：使用 Mock 编写单元测试（30 分）

**目标**：使用 Mockito 编写 Mock 测试，隔离外部依赖（如数据库）。

**背景知识**：
Mock 对象是"替身演员"，可以替代真实的外部依赖（如数据库）。这样测试：
- 更快（不需要连接数据库）
- 更稳定（不会因为数据库问题失败）
- 更专注（只测试业务逻辑，不测试外部系统）

**要求**：

#### 2.1 添加 Mockito 依赖（5 分）

在 `pom.xml` 中添加：

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.12.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.12.0</version>
    <scope>test</scope>
</dependency>
```

#### 2.2 编写 Mock 测试（15 分）

创建 `TaskServiceMockTest.java`：

```java
@ExtendWith(MockitoExtension.class)
class TaskServiceMockTest {

    @Mock
    private TaskRepository mockRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getAllTasks_withMock_returnsTasks() {
        // given: Mock 返回预设数据
        when(mockRepository.findAll())
            .thenReturn(List.of(new Task("任务1"), new Task("任务2")));

        // when: 调用 Service
        List<Task> tasks = taskService.getAllTasks();

        // then: 验证结果
        assertEquals(2, tasks.size());
        assertEquals("任务1", tasks.get(0).getTitle());
    }

    @Test
    void deleteTask_withMock_verifiesCall() {
        // given: Mock 无需返回值
        doNothing().when(mockRepository).delete("1");

        // when: 删除任务
        taskService.deleteTask("1");

        // then: 验证是否调用了 delete
        verify(mockRepository).delete("1");
        verify(mockRepository, times(1)).delete("1");
    }

    @Test
    void findById_notFound_throwsException() {
        // given: Mock 返回空
        when(mockRepository.findById("999")).thenReturn(Optional.empty());

        // when/then: 验证抛出异常
        assertThrows(NotFoundException.class, () -> {
            taskService.getTaskById("999");
        });
    }
}
```

#### 2.3 编写 Spy 测试（10 分）

Spy 是"部分 Mock"，部分方法真实实现，部分方法 Mock：

```java
@Test
void markCompleted_withSpy_callsRealMethod() {
    // given: 创建 Spy 对象
    Task task = spy(new Task("测试任务"));

    // when: 调用真实方法
    task.markCompleted();

    // then: 验证状态变化
    assertTrue(task.isCompleted());
    verify(task, atLeastOnce()).markCompleted();
}
```

**提交物**：
- `pom.xml`：添加了 Mockito 依赖
- `TaskServiceMockTest.java`：Mock 测试类

**评分要点**：
- 添加 Mockito 依赖（5 分）
- 编写至少 2 个 Mock 测试（15 分）
- 编写 Spy 测试（10 分）

---

### 任务 3：Bug Bash 参与与报告（30 分）

**目标**：参与团队组织的 Bug Bash 活动，发现系统问题，完成修复和根因分析。

**背景知识**：
Bug Bash 是集体找 bug 的工程实践——所有团队暂停开发，互相测试对方的系统，记录发现的问题。新鲜视角能发现开发者盲区的问题。

**Bug Bash 流程**：

1. **准备（15 分钟）**
   - 部署 CampusFlow 到测试服务器（或本地启动）
   - 准备一个"测试指南"（核心功能列表、已知问题）

2. **交叉测试（60 分钟）**
   - A 团队测试 B 团队的系统，B 测试 C，C 测试 A
   - 测试者尝试"破坏"系统：输入奇怪数据、快速点击、并发操作
   - 记录每个 bug 的复现步骤

3. **汇总（30 分钟）**
   - 所有团队回到一起，报告发现的问题
   - 按优先级分类（P0 崩溃 / P1 功能缺陷 / P2 体验问题）
   - 评选"最奇葩 bug"奖

4. **修复（课后）**
   - 每个团队修复自己系统的 bug
   - 完成根因分析

**要求**：

#### 3.1 参与 Bug Bash（5 分）

- 参与团队 Bug Bash 活动
- 测试至少 1 个其他团队的系统
- 记录发现的问题

#### 3.2 编写 Bug Bash 报告（15 分）

创建 `BUG_BASH_REPORT.md`：

```markdown
# CampusFlow Bug Bash 报告

## 测试环境
- URL: http://localhost:8080（或测试服务器地址）
- 测试时间: 2026-02-XX 14:00-16:00
- 测试者: XX 团队

## 发现的问题

| ID | 问题描述 | 复现步骤 | 优先级 | 根因 |
|----|---------|---------|--------|------|
| #1 | 任务标题包含 emoji 时返回 500 | 1. 创建任务 2. 输入 "🔥🔥🔥" 3. 保存 | P0 | 数据库字符集未设置为 UTF-8 |
| #2 | 快速点击"完成"导致状态闪烁 | 1. 打开任务列表 2. 快速点击"完成"5 次 | P2 | 前端未防抖 |
| #3 | 任务列表超过 100 条时前端卡顿 | 1. 创建 100+ 任务 | P1 | 前端未分页 |

## 修复计划
- #1: 本周修复（修改数据库连接字符串）
- #2: 下周修复（添加前端防抖）
- #3: 加入技术债 Backlog（P3）

## 收到的反馈
- API 设计清晰，容易理解
- 缺少分页功能
- 错误提示不够友好
```

#### 3.3 根因分析（10 分）

选择至少 1 个 bug，完成根因分析：

```markdown
## Bug #1: Emoji 导致 500 错误

### 复现步骤
1. 创建任务
2. 标题输入 "🔥🔥🔥"
3. 点击保存
4. 后端返回 500 Internal Server Error

### 根因分析

**直接原因**：
数据库字符集未设置为 UTF-8，无法存储 emoji 字符

**设计缺陷**：
- 数据库初始化时未指定字符集
- 缺少输入验证（标题长度、字符类型）

**测试盲区**：
- 单元测试只测试了 ASCII 字符
- 集成测试未包含边界数据（emoji、超长字符串）

**修复方案**：
1. 修改数据库连接字符串：`?useUnicode=true&characterEncoding=utf8mb4`
2. 添加输入验证：标题长度限制，过滤非法字符
3. 补充测试：包含 emoji、超长字符串的测试用例

### 验证
- [ ] 修复后 emoji 能正常存储
- [ ] 添加了输入验证
- [ ] 补充了测试用例
```

**提交物**：
- `BUG_BASH_REPORT.md`：Bug Bash 报告
- `ROOT_CAUSE_ANALYSIS.md`：根因分析文档
- 修复后的代码（提交到 Git）

**评分要点**：
- 参与 Bug Bash 活动（5 分）
- Bug Bash 报告完整，包含至少 2 个 bug（10 分）
- 根因分析深入，不只是修复表面问题（15 分）

---

## 进阶作业（选做，+20 分）

### 进阶 1：契约测试与 OpenAPI（+10 分）

**目标**：为 API 编写 OpenAPI 规范，验证契约不被破坏。

**要求**：

1. **创建 `openapi.yaml`**，描述 API 契约：

```yaml
openapi: 3.0.0
info:
  title: CampusFlow API
  version: 1.0.0
paths:
  /tasks:
    get:
      summary: 获取所有任务
      responses:
        '200':
          description: 成功
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Task'
    post:
      summary: 创建任务
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/TaskRequest'
      responses:
        '201':
          description: 创建成功
components:
  schemas:
    Task:
      type: object
      required:
        - id
        - title
        - completed
      properties:
        id:
          type: string
        title:
          type: string
        completed:
          type: boolean
```

2. **编写契约测试**，验证 API 符合 OpenAPI 规范

**提交物**：
- `openapi.yaml`：OpenAPI 规范
- `ContractTest.java`：契约测试类

---

### 进阶 2：E2E 测试（+10 分）

**目标**：使用 Selenium 或 Playwright 编写端到端测试，模拟真实用户操作。

**要求**：

1. **添加 Playwright 依赖**（可选）

2. **编写 E2E 测试**：

```java
@Test
void createTask_e2e() {
    // given: 打开浏览器
    Page page = browser.newPage();
    page.navigate("http://localhost:8080");

    // when: 用户创建任务
    page.fill("#task-title", "E2E 测试任务");
    page.click("#create-button");

    // then: 验证任务出现在列表中
    assertTrue(page.content().contains("E2E 测试任务"));
}
```

**提交物**：
- `TaskE2ETest.java`：E2E 测试类
- `e2e_test_results.txt`：测试运行结果

---

## AI 协作练习（可选但推荐）

**目标**：练习用 AI 生成集成测试，并审查 AI 输出。

### 任务：AI 生成集成测试

**步骤**：

1. **编写 Prompt**，让 AI 生成集成测试：
   - 明确需求：为 CampusFlow 的 POST /api/tasks 生成集成测试
   - 指定技术栈：JUnit 5、HttpClient、Javalin
   - 添加约束：覆盖正常、边界、异常场景

2. **保存 AI 生成的测试代码**（不要修改）

3. **使用审查检查清单评估**：

#### AI 生成集成测试审查清单

**功能正确性**
- [ ] 测试是否启动了 Javalin 服务器？
- [ ] 是否发送了真实 HTTP 请求？
- [ ] 断言是否正确验证了预期行为？

**边界情况（AI 常遗漏）⭐**
- [ ] 空列表场景测试了吗？
- [ ] 404/500 错误场景测试了吗？
- [ ] 无效输入（空标题、超长标题）测试了吗？
- [ ] 并发请求测试了吗？

**测试质量**
- [ ] 测试之间有依赖吗？（应该独立）
- [ ] 测试数据是否隔离？（@BeforeEach 清理）
- [ ] 是否使用了 @AfterEach 关闭服务器？

**契约验证**
- [ ] 验证 JSON 字段名了吗？（如 title vs taskName）
- [ ] 验证 HTTP 状态码了吗？（200/201/404/500）

4. **记录发现的问题并修复**

**审查清单**：
- [ ] **正确性**：测试能运行吗？
- [ ] **完整性**：是否遗漏边界场景？
- [ ] **独立性**：测试之间有依赖吗？
- [ ] **契约验证**：是否验证了 API 契约？
- [ ] **AI 特定问题**：有不存在的 API（幻觉）吗？

**提交物**：
- `ai_generated_test.java`：AI 生成的原始测试代码
- `ai_review_test.md`：审查报告，包含：
  - 发现的问题列表
  - 修复后的代码
  - 经验总结（AI 擅长/不擅长生成集成测试的原因）

---

## 提交物清单

### 必交文件
- [ ] `TaskApiIntegrationTest.java`：API 集成测试
- [ ] `integration_test_results.txt`：测试运行结果
- [ ] `pom.xml`：添加了 Mockito 依赖
- [ ] `TaskServiceMockTest.java`：Mock 测试
- [ ] `BUG_BASH_REPORT.md`：Bug Bash 报告
- [ ] `ROOT_CAUSE_ANALYSIS.md`：根因分析文档
- [ ] 修复后的所有源代码文件

### 进阶作业文件（如完成）
- [ ] `openapi.yaml`：OpenAPI 规范
- [ ] `ContractTest.java`：契约测试
- [ ] `TaskE2ETest.java`：E2E 测试

### AI 协作练习文件（如完成）
- [ ] `ai_generated_test.java`：AI 生成的测试代码
- [ ] `ai_review_test.md`：审查报告

---

## 作业截止时间

- **基础作业**：本周日 23:59
- **进阶作业**：下周三 23:59

---

## 常见问题

### Q1: 集成测试和单元测试有什么区别？

**单元测试**：
- 不启动真实服务
- 直接调用方法（如 `taskService.getAllTasks()`）
- 使用 Mock 隔离外部依赖
- 关注逻辑正确性

**集成测试**：
- 启动真实服务（Javalin）
- 发送 HTTP 请求
- 连接真实数据库（或内存数据库）
- 关注系统集成（API 契约、JSON 格式）

### Q2: 端口 8080 被占用怎么办？

在测试中使用随机端口：

```java
@BeforeEach
void setUp() {
    app = Javalin.create().start(0);  // 0 = 随机端口
    port = app.port();  // 获取实际端口
}

@Test
void getTasks_returnsList() throws Exception {
    HttpResponse<String> response = HttpClient.newHttpClient()
        .send(HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:" + port + "/api/tasks"))
            .GET()
            .build(), BodyHandlers.ofString());
}
```

### Q3: Mock 测试验证了什么？

Mock 测试验证：
- 业务逻辑是否正确（如 `TaskService` 的处理逻辑）
- 方法是否被调用（`verify(mockRepository).delete("1")`）
- 调用次数是否正确（`times(1)`）

Mock 测试**不**验证：
- SQL 是否正确（没有执行真实 SQL）
- 数据库连接是否正常
- JSON 序列化是否正确

### Q4: Bug Bash 只能测试自己团队的系统吗？

**不**。Bug Bash 的价值在于"新鲜视角"：
- 测试其他团队的系统，能发现开发者盲区的问题
- 其他团队测试你的系统，能发现你"习以为常"的问题
- 建议交叉测试：A 测 B，B 测 C，C 测 A

### Q5: 根因分析要写多详细？

至少包含：
- **直接原因**：为什么会出现这个 bug？（如数据库字符集问题）
- **设计缺陷**：是设计问题还是实现问题？
- **测试盲区**：为什么测试没发现？
- **修复方案**：不只修复 bug，还要预防同类问题

参考教材 Week 12 第 4 节的根因分析示例。

---

## 参考资源

- 如果你遇到困难，可以参考 `starter_code/src/test/java/` 中的示例测试
- 教材 Week 12 章节：`chapters/week_12/CHAPTER.md`
- JUnit 5 文档：https://junit.org/junit5/docs/current/user-guide/
- Mockito 文档：https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html
- Java HttpClient 教程：https://docs.oracle.com/en/java/javase/17/docs/api/java.net.http/java/net/http/HttpClient.html

---

## 学习建议

1. **测试金字塔**：单元测试（多）→ 集成测试（适量）→ E2E 测试（少）
2. **Mock 不是万能**：过度 Mock 会导致测试跟"假对象"交互，而不是测试真实逻辑
3. **Bug Bash 是文化**：不只是找 bug，更是团队协作和互相学习
4. **根因分析是成长**：每个 bug 都是教训，理解根因才能避免同类问题

祝作业顺利！记住老潘的话："单元测试证明零件能跑，集成测试证明它们在一起也能跑。"
