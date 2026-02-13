# Week 13 作业：文档与知识传递

> "代码是给机器看的，文档是给人的。好的工程师不只写能跑的代码，还要让其他人能理解你的设计。"
> —— 老潘

---

## 作业概述

本周你将让 CampusFlow 从"能跑"走向"可交付"。你将编写 API 文档（OpenAPI 规范）、完善 README、汇总 ADR，并学习如何审查 AI 生成的文档质量。

**核心任务**：
1. 为 CampusFlow 的 REST API 编写 OpenAPI 规范文档
2. 编写/改进 README，让新用户 5 分钟内能跑起来
3. 创建 ADR 索引，汇总所有架构决策
4. 审查 AI 生成的文档，找出错误并修复

---

## 学习目标（对应章节）

完成本周作业后，你将能够：
- **理解** API 文档的价值与要素（Bloom：理解）
- **应用** OpenAPI/Swagger 规范描述 REST API（Bloom：应用）
- **分析** README 的结构，写出清晰的项目说明（Bloom：分析）
- **评价** AI 生成的文档质量（Bloom：评价）
- **创造** 完整的文档体系（Bloom：创造）

---

## 作业背景

上周 Bug Bash 中，隔壁组反馈："API 没有文档，只能看代码猜格式""字段名和类型都不清楚""怎么跑起来不清楚"。

小北的代码能跑，测试也全绿，但确实缺少正式文档——别人看不懂他的 API，也不知道怎么启动服务。

老潘说："代码只是'答案'，文档才是'解释'。好的工程师不只写能跑的代码，还要让其他人能理解你的设计。"

本周你需要：
1. 编写 API 文档（OpenAPI），让别人能调用你的接口
2. 写 README，让新用户 5 分钟内跑起来
3. 汇总 ADR，让别人理解你的设计决策
4. 审查 AI 生成的文档，确保质量

---

## 基础作业（必做，100 分）

### 任务 1：编写 API 文档（40 分）

**目标**：为 CampusFlow 的 REST API 编写 OpenAPI 规范文档。

**背景知识**：
OpenAPI（前身是 Swagger）是一种 API 文档规范，用 YAML 或 JSON 格式描述 API 的端点、请求、响应、错误码。它不只是给人看的，还是机器可读的"契约"——可以生成客户端代码、服务器桩代码、自动测试。

**要求**：

#### 1.1 创建 openapi.yaml（10 分）

在项目根目录创建 `openapi.yaml`：

```yaml
openapi: 3.0.3
info:
  title: CampusFlow API
  description: |
    CampusFlow 是校园任务管理系统。

    **重要约束**：
    - 任务标题最大 100 字符
    - 任务 ID 是 UUID 格式
    - 所有日期时间使用 ISO 8601 格式
  version: 1.0.0

servers:
  - url: http://localhost:8080
    description: 本地开发服务器
```

**评分要点**：
- [ ] openapi 版本正确（3.0.3）（2 分）
- [ ] 包含 title、description、version（4 分）
- [ ] description 包含业务约束说明（4 分）

#### 1.2 描述 API 端点（15 分）

为所有 REST API 端点编写文档：

```yaml
paths:
  /api/tasks:
    get:
      summary: 获取所有任务
      description: |
        返回当前用户的所有任务，按创建时间倒序排列。

        **分页**：支持 `?page=1&size=20` 参数（可选）
        **搜索**：支持 `?keyword=xxx` 参数（可选）
      responses:
        '200':
          description: 成功返回任务列表
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Task'
        '500':
          description: 服务器内部错误
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'

    post:
      summary: 创建新任务
      description: |
        创建一个新任务。标题必填，描述和截止日期可选。

        如果标题超过 100 字符，返回 400 错误。
        如果标题为空，返回 400 错误。
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/TaskInput'
      responses:
        '201':
          description: 任务创建成功
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Task'
        '400':
          description: 请求参数错误（如标题为空、超过长度限制）
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'
```

**评分要点**：
- [ ] GET /api/tasks 文档完整（5 分）
- [ ] POST /api/tasks 文档完整（5 分）
- [ ] PUT /api/tasks/{id} 文档完整（2 分）
- [ ] DELETE /api/tasks/{id} 文档完整（2 分）
- [ ] 每个端点包含 summary、description、responses（1 分）

#### 1.3 定义数据模型（10 分）

在 `components/schemas` 中定义数据模型：

```yaml
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
          description: 任务的唯一标识符（UUID）
        title:
          type: string
          description: 任务标题（1-100 字符）
          maxLength: 100
        description:
          type: string
          description: 任务详细描述（可选）
        completed:
          type: boolean
          description: 任务是否已完成
        createdAt:
          type: string
          format: date-time
          description: 任务创建时间

    TaskInput:
      type: object
      required:
        - title
      properties:
        title:
          type: string
          description: 任务标题（1-100 字符）
          minLength: 1
          maxLength: 100
        description:
          type: string
          description: 任务详细描述（可选）

    Error:
      type: object
      required:
        - message
      properties:
        message:
          type: string
          description: 错误信息
```

**评分要点**：
- [ ] Task 模型定义完整（4 分）
- [ ] TaskInput 模型定义完整（3 分）
- [ ] Error 模型定义完整（2 分）
- [ ] 字段描述清晰，包含约束说明（1 分）

#### 1.4 错误码文档（5 分）

为所有可能的错误码编写文档：

```yaml
paths:
  /api/tasks/{id}:
    get:
      # ...
      responses:
        '200':
          description: 成功返回任务
        '404':
          description: 任务不存在
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Error'
              example:
                message: "任务不存在: 999"
```

**评分要点**：
- [ ] 404 Not Found 文档（2 分）
- [ ] 400 Bad Request 文档（2 分）
- [ ] 500 Internal Server Error 文档（1 分）

**提交物**：
- `openapi.yaml`：完整的 API 文档

**验证方式**：
- 使用 [OpenAPI Editor](https://editor.swagger.io/) 验证格式正确性
- 确保所有端点都有文档

---

### 任务 2：编写/改进 README（30 分）

**目标**：编写清晰的 README，让新用户 5 分钟内跑起来。

**背景知识**：
README 是项目的"门面"。用户在 GitHub 上看到你的项目，第一眼就是 README。好的 README 遵循"倒金字塔原则"——最重要（快速开始）放在最前面。

**要求**：

#### 2.1 快速开始（10 分）

```markdown
## 快速开始（5 分钟）

### 前置要求
- Java 17+
- Maven 3.6+

### 5 分钟运行

1. **克隆仓库**
   ```bash
   git clone https://github.com/your-org/campusflow.git
   cd campusflow
   ```

2. **配置数据库**
   ```bash
   # 数据库会自动创建在项目根目录（campusflow.db）
   # 无需手动配置
   ```

3. **启动服务器**
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.campusflow.Main"
   ```

4. **访问应用**
   - 打开浏览器访问 http://localhost:8080
   - API 文档：http://localhost:8080/swagger-ui

### 验证安装

发送测试请求：
```bash
curl http://localhost:8080/api/tasks
# 预期输出：[]
```
```

**评分要点**：
- [ ] 前置要求清晰（2 分）
- [ ] 5 分钟运行步骤完整（5 分）
- [ ] 每个命令都能运行（复制粘贴验证）（2 分）
- [ ] 包含验证步骤（1 分）

#### 2.2 功能特性（5 分）

```markdown
## 功能特性

- ✅ 创建任务、编辑任务、删除任务
- ✅ 设置截止日期、标记完成
- ✅ 任务搜索和过滤
- ✅ RESTful API + OpenAPI 文档
```

**评分要点**：
- [ ] 功能列表清晰（3 分）
- [ ] 使用 emoji 或图标增强可读性（2 分）

#### 2.3 使用指南（5 分）

```markdown
## 使用指南

### 创建任务
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"学习文档编写","completed":false}'
```

### 标记完成
```bash
curl -X PUT http://localhost:8080/api/tasks/{id} \
  -H "Content-Type: application/json" \
  -d '{"completed":true}'
```
```

**评分要点**：
- [ ] 包含至少 2 个使用示例（3 分）
- [ ] 命令能运行（2 分）

#### 2.4 常见问题（5 分）

```markdown
## 常见问题

**Q: 数据库在哪里？**
A: SQLite 数据库文件在项目根目录（`campusflow.db`），首次运行自动创建。

**Q: 如何修改端口号？**
A: 编辑 `src/main/resources/config.properties`，修改 `server.port` 配置。

**Q: 任务标题有长度限制吗？**
A: 是的，标题最大 100 字符。
```

**评分要点**：
- [ ] 至少 3 个常见问题（3 分）
- [ ] 问题来自真实用户反馈（1 分）
- [ ] 回答清晰准确（1 分）

#### 2.5 项目结构（5 分）

```markdown
## 项目结构

```
campusflow/
├── src/main/java/        # Java 源代码
├── src/main/resources/   # 配置文件
├── src/test/java/       # 测试代码
├── docs/               # 架构决策记录（ADR）
├── openapi.yaml        # API 文档
└── README.md           # 本文件
```
```

**评分要点**：
- [ ] 目录树清晰（3 分）
- [ ] 关键目录有说明（2 分）

**提交物**：
- `README.md`：完整的项目说明文档

**验证方式**：
- 按照 README 的步骤，从头跑一遍项目
- 确保每个命令都能执行成功

---

### 任务 3：创建 ADR 索引（20 分）

**目标**：汇总所有架构决策，创建 ADR 索引。

**背景知识**：
ADR（Architecture Decision Record）是架构决策记录。单个 ADR 是"决策碎片"，索引才是"决策地图"。索引让读者不用翻每个文件，就能看全所有决策、理解决策之间的依赖关系。

**要求**：

#### 3.1 创建 ADR 索引（15 分）

在 `docs/ADR_INDEX.md` 创建索引：

```markdown
# CampusFlow 架构决策索引

本文档汇总了 CampusFlow 项目的所有架构决策记录（ADR）。

## ADR 列表

| ADR | 标题 | 日期 | 状态 | 影响 |
|-----|------|------|------|------|
| [ADR-001](adr/001-domain-model.md) | 领域模型设计 | 2026-01-15 | 已采纳 | 高 |
| [ADR-002](adr/002-data-storage.md) | 数据存储方案 | 2026-02-01 | 已采纳 | 高 |
| [ADR-003](adr/003-api-design.md) | REST API 设计 | 2026-02-15 | 已采纳 | 中 |
| [ADR-004](adr/004-architecture-evolution.md) | 架构演进 | 2026-02-28 | 已采纳 | 高 |

## 决策摘要

### ADR-001：领域模型设计

**决策**：采用贫血模型（Anemic Domain Model）
- Task 作为数据实体，业务逻辑在 Service 层
- 优点：简单、易理解、适合小团队
- 缺点：业务逻辑分散，项目大时难以维护

**背景**：Week 02 时团队规模小（2 人），项目复杂度低。

**后果**：如果项目演变成大型系统，需考虑演进为充血模型（Rich Domain Model）。

### ADR-002：数据存储方案

**决策**：使用 SQLite + JDBC
- SQLite：零配置、单文件数据库
- JDBC：手动管理连接、使用 try-with-resources
- Repository 模式：封装数据访问逻辑

**背景**：Week 07 时选择，优先考虑简单性而非性能。

**后果**：如果并发量超过 100 QPS，需迁移到 PostgreSQL/MySQL。

### ADR-003：REST API 设计

**决策**：采用标准 RESTful 风格
- 资源导向：`/api/tasks`、`/api/tasks/{id}`
- HTTP 方法语义：GET（读取）、POST（创建）、PUT（更新）、DELETE（删除）
- JSON 格式：使用 Gson 序列化

**背景**：Week 09 时设计，与前端团队约定契约。

**后果**：如果需要 GraphQL 或 gRPC，需重新设计 API。

### ADR-004：架构演进

**决策**：不引入 Spring Boot
- 继续使用 Javalin（极简框架）
- 理由：学习 Java 核心技术，不依赖框架魔法
- 代价：需要手动配置依赖注入、事务管理

**背景**：Week 12 时讨论，团队认为"理解原理"比"快速开发"更重要。

**后果**：如果项目需要进入生产环境，需重新评估 Spring Boot。

## 决策依赖图

```
ADR-001（领域模型）
    ↓ 影响
ADR-002（数据存储）
    ↓ 影响
ADR-003（API 设计）
    ↓ 影响
ADR-004（架构演进）
```

## 待决策事项

- [ ] 是否引入缓存层（Redis）？
- [ ] 是否需要消息队列（异步任务）？
- [ ] 是否迁移到 Spring Boot？

## 参考资源

- [ADR 模板](https://adr.github.io/)
- [架构决策记录最佳实践](https://www.thoughtworks.com/radar/techniques/architecture-decision-records)
```

**评分要点**：
- [ ] ADR 列表完整（3 分）
- [ ] 每个 ADR 包含决策摘要（6 分）
- [ ] 包含决策依赖图（3 分）
- [ ] 包含待决策事项（3 分）

#### 3.2 链接完整性（5 分）

**评分要点**：
- [ ] 所有 ADR 链接有效（3 分）
- [ ] 参考资源链接有效（2 分）

**提交物**：
- `docs/ADR_INDEX.md`：ADR 索引文件

---

### 任务 4：文档审查（10 分）

**目标**：审查 AI 生成的文档，使用检查清单找出问题。

**背景知识**：
AI 能生成文档框架，但可能编造不存在的功能（"幻觉"）、用过时的命令、错误的路径。你需要用审查清单验证 AI 生成的文档质量。

**要求**：

> 📋 **AI 生成文档示例（供参考）**
>
> 以下是一个 AI 生成的 README 片段示例（**包含典型错误**）：
> ```markdown
> ## 快速开始
>
> ### 安装
>
> 1. 克隆项目：
>    ```bash
>    git clone https://github.com/user/campusflow.git
>    cd campusflow
>    mvn install  # ❌ 错误：应该是 mvn compile exec:java
>    ```
>
> 2. 配置数据库：
>    ```bash
>    export DB_PATH=data/campusflow.db  # ❌ 错误：数据库在根目录，不是 data/ 下
>    mkdir -p data
>    ```
>
> 3. 启动服务：
>    ```bash
>    java -jar target/campusflow.jar  # ❌ 错误：没有构建步骤，且 jar 文件路径不对
>    ```
>
> ## 功能特性
>
> - ✅ 支持多种数据库：PostgreSQL、MySQL、SQLite  # ❌ 幻觉：只支持 SQLite
> - ✅ 任务管理、用户认证、团队协作  # ❌ 幻觉：当前版本没有用户认证和团队协作功能
> - ✅ RESTful API 设计
> - ✅ 响应式前端支持
>
> ## API 文档
>
> 访问在线文档：https://api.campusflow.io/docs  # ❌ 编造的 URL
> ```
>
> **这份 AI 生成的内容包含了多个典型错误**：
> - `mvn install` 是旧命令（过时问题）
> - `data/campusflow.db` 路径错误（准确性问题）
> - "支持 PostgreSQL、MySQL" 是虚构功能（幻觉问题）
> - `https://api.campusflow.io/docs` 是编造的 URL（幻觉问题）
>
> 你的任务是：**用审查清单找出这些问题，并生成修复后的正确文档**。
>

#### 4.1 使用 AI 生成文档（2 分）

尝试用 AI 生成 README 或 OpenAPI 文档，保存原始输出。

#### 4.2 使用审查清单评估（5 分）

```markdown
## AI 生成文档审查清单

### 准确性（AI 常错）
- [ ] 所有命令都能运行吗？（复制粘贴验证）
- [ ] 所有链接都有效吗？（点击验证）
- [ ] 代码示例和实际代码一致吗？
- [ ] 版本号、端口号、路径都正确吗？

### 完整性
- [ ] 快速开始能让新用户 5 分钟跑起来吗？
- [ ] 常见问题覆盖了真实用户的问题吗？
- [ ] API 文档包含所有端点吗？
- [ ] 错误码和异常情况说明了吗？

### 清晰度
- [ ] 技术术语有解释吗？（非专家能看懂）
- [ ] 有冗余信息吗？（删除重复内容）
- [ ] 结构符合倒金字塔原则吗？（重要信息在前）

### 维护性
- [ ] 文档和代码在同一仓库吗？（Docs-as-Code）
- [ ] 有版本标签吗？（文档随代码版本变化）
- [ ] 有最后更新日期吗？（读者知道文档是否过时）

### AI 特定问题（幻觉）
- [ ] 有虚构的功能吗？（AI 可能写你实现的功能）
- [ ] 有过时的命令吗？（AI 可能用旧版本语法）
- [ ] 有不存在的链接吗？（AI 可能编造 URL）
```

**评分要点**：
- [ ] 使用清单逐项检查（3 分）
- [ ] 记录发现的问题（2 分）

#### 4.3 编写审查报告（3 分）

创建 `docs/REVIEW_REPORT.md`：

```markdown
# AI 生成文档审查报告

## 审查的文档
- 文档类型：README / OpenAPI
- AI 工具：Claude / Cursor / Copilot
- 审查日期：2026-02-XX

## 发现的问题

### 准确性问题
1. **命令错误**：AI 生成的启动命令是 `mvn install`，实际应该是 `mvn compile exec:java`
2. **路径错误**：AI 说数据库在 `data/` 目录，实际在项目根目录

### 完整性问题
1. **缺少验证步骤**：快速开始没有说明如何验证安装成功
2. **常见问题不足**：只列了 1 个问题，实际用户常问 3 个问题

### AI 幻觉
1. **虚构功能**：AI 说支持 PostgreSQL、MySQL，但实际只支持 SQLite

## 修复后的文档
（附上修复后的完整文档）

## 经验总结
- AI 擅长：生成文档框架、格式化 Markdown/YAML
- AI 不擅长：准确描述项目细节、理解业务约束
- 我的改进：人工验证所有命令、补充真实使用场景
```

**评分要点**：
- [ ] 报告包含发现的问题（1 分）
- [ ] 报告包含修复后的文档（1 分）
- [ ] 报告包含经验总结（1 分）

**提交物**：
- `docs/REVIEW_REPORT.md`：审查报告

---

## 进阶作业（选做，+20 分）

### 进阶 1：集成 Swagger UI（+10 分）

**目标**：集成 Swagger UI，提供交互式 API 文档。

**要求**：

1. **添加 Swagger UI 依赖**（可选，或使用在线工具）

2. **配置 Javalin 提供 Swagger UI**

访问 `http://localhost:8080/swagger-ui` 可以看到交互式文档。

**评分要点**：
- [ ] Swagger UI 能正常访问（5 分）
- [ ] 可以在 Swagger UI 中测试 API（5 分）

---

### 进阶 2：文档自动化（+10 分）

**目标**：实现文档自动更新（Docs-as-Code）。

**要求**：

1. **CI 自动渲染文档**

在 GitHub Actions 或类似 CI 系统中配置：

```yaml
name: Docs Build

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Validate OpenAPI
        run: |
          npx @apidevtools/swagger-cli validate openapi.yaml
      - name: Build Docs
        run: |
          # 将 Markdown 渲染为 HTML
```

2. **版本同步**

文档标签和代码标签一致（v1.0.0 的文档对应 v1.0.0 的代码）。

**评分要点**：
- [ ] CI 配置文件存在（5 分）
- [ ] OpenAPI 验证通过（3 分）
- [ ] 文档和代码版本标签一致（2 分）

---

## AI 协作练习（可选但推荐）

**目标**：练习用 AI 生成文档，并学会审查 AI 输出。

### 任务：AI 生成 README

**步骤**：

1. **编写 Prompt**，让 AI 生成 README：
   - 明确需求：为 CampusFlow 项目生成 README
   - 指定结构：快速开始、功能特性、使用指南、常见问题
   - 添加约束：5 分钟内跑起来、命令能执行

2. **保存 AI 生成的 README**（不要修改）

3. **使用审查检查清单评估**：

#### AI 生成 README 审查清单

**准确性（AI 常错）**
- [ ] 所有命令都能运行吗？（复制粘贴验证）
- [ ] 所有链接都有效吗？（点击验证）
- [ ] 代码示例和实际代码一致吗？

**完整性**
- [ ] 快速开始能让新用户 5 分钟跑起来吗？
- [ ] 常见问题覆盖了真实用户的问题吗？
- [ ] 使用指南包含核心功能吗？

**清晰度**
- [ ] 结构符合倒金字塔原则吗？（重要信息在前）
- [ ] 技术术语有解释吗？
- [ ] 有冗余信息吗？

**AI 特定问题（幻觉）**
- [ ] 有虚构的功能吗？
- [ ] 有过时的命令吗？
- [ ] 有不存在的链接吗？

4. **记录发现的问题并修复**

**提交物**：
- `ai_generated_README.md`：AI 生成的原始 README
- `ai_review_README.md`：审查报告，包含：
  - 发现的问题列表
  - 修复后的 README
  - 经验总结（AI 擅长/不擅长生成文档的原因）

---

## 提交物清单

### 必交文件
- [ ] `openapi.yaml`：API 文档（OpenAPI 规范）
- [ ] `README.md`：项目说明文档（快速开始 + 功能特性 + 常见问题）
- [ ] `docs/ADR_INDEX.md`：ADR 索引
- [ ] `docs/REVIEW_REPORT.md`：文档审查报告

### 进阶作业文件（如完成）
- [ ] `swagger-ui-config.xml`：Swagger UI 配置（进阶 1）
- [ ] `.github/workflows/docs.yml`：CI 配置（进阶 2）

### AI 协作练习文件（如完成）
- [ ] `ai_generated_README.md`：AI 生成的原始 README
- [ ] `ai_review_README.md`：审查报告

---

## 作业截止时间

- **基础作业**：本周日 23:59
- **进阶作业**：下周三 23:59

---

## 常见问题

### Q1: OpenAPI 和 Swagger 有什么区别？

**OpenAPI**：是 API 规范（标准），描述 API 的格式。

**Swagger**：是实现 OpenAPI 规范的工具集（包括 Swagger Editor、Swagger UI）。

简单说：OpenAPI 是"标准"，Swagger 是"工具"。

### Q2: README 应该写多长？

README 不宜过长，建议：
- **核心**：快速开始（最重要）
- **补充**：功能特性、使用指南
- **可选**：项目结构、开发指南、贡献指南

如果文档很长，考虑拆分成多个文件（如 `docs/INSTALL.md`、`docs/API.md`），README 只放链接。

### Q3: ADR 索引需要多详细？

ADR 索引的目标是"快速回顾"，不需要复制完整 ADR。建议：
- **必填**：决策是什么、理由、后果
- **可选**：详细讨论过程、备选方案

完整内容在 ADR 文件中，索引只放摘要。

### Q4: AI 生成的文档可以直接用吗？

**不推荐直接使用**。AI 常见问题：
- **幻觉**：编造不存在的功能
- **过时**：使用旧版本命令
- **错误**：路径、端口号不准确

正确做法：
1. AI 生成框架
2. 人工逐条验证
3. 修正错误、补充细节

### Q5: Docs-as-Code 是什么？

**Docs-as-Code**：文档和代码用同一套工作流。
- 文档用 Markdown/YAML 写（与代码同仓库）
- 文档用 Git 版本控制
- 文档和代码一起 CI/CD

好处：文档不会过时（和代码同步演进）。

---

## 参考资源

- 如果你遇到困难，可以参考 `starter_code/src/main/java/` 中的示例代码
- 教材 Week 13 章节：`chapters/week_13/CHAPTER.md`
- OpenAPI 规范：https://spec.openapis.org/oas/v3.1.0
- OpenAPI Editor：https://editor.swagger.io/
- ADR 模板：https://adr.github.io/

---

## 学习建议

1. **文档是交付的前提**：没有文档的项目不能称为"可交付"
2. **用户视角**：写 README 时站在用户角度，不是开发者角度
3. **Docs-as-Code**：文档和代码同步演进，不是"事后补"
4. **AI 是辅助**：AI 能生成框架，但质量得靠你把控

祝作业顺利！记住老潘的话："代码是给机器看的，文档是给人的。好的工程师不只写能跑的代码，还要让其他人能理解你的设计。"
