# Week 13：文档与知识传递

> "代码是给机器看的，文档是给人的。好的工程师不只是写能跑的代码，还要让其他人能理解你的设计。"
> — 老潘

2026 年的软件工程现场，代码生成 AI 正在改变"写代码"的方式，但"文档与知识传递"反而变得更加重要。研究显示，约 40% 的开发时间花在"理解别人写的代码"上，而 AI 生成的代码往往缺少上下文和设计意图。与此同时，AI 辅助文档生成工具正在快速发展——从 OpenAPI 的自动文档到 AI 生成的 README，但这些工具生成的文档需要人类审查，确保准确性和可维护性。传统的"事后补文档"正在被"文档即代码"（Docs-as-Code）的工程实践替代——文档与代码同步演进、版本控制、自动更新。本周你将学习如何让 CampusFlow 从"能跑"走向"可交付"，把 API 设计、架构决策、项目经验沉淀为可传递的知识。

---

## 前情提要

上周你为 CampusFlow 建立了完整的集成测试体系，还组织了一场 Bug Bash——三个团队互相测试对方的系统，发现了 emoji 导致的 500 错误、快速点击导致的状态闪烁、大量数据导致的前端卡顿。小北在 Bug Bash 报告上写满了笔记，老潘点头："系统级质量保障体系建起来了。"

周三下午，隔壁组的同学跑来问小北："你们的 API 返回的 JSON 格式是什么？为什么我的前端调不通？"

小北打开代码，指着 `Task` 类："你看，字段是 `title`、`completed`、`dueDate`..."

"但你怎么不写个文档？"对方皱眉，"我总不能每次都来看你的代码吧？"

小北愣住了——他的代码能跑，测试也通过了，但确实没有正式的 API 文档。

老潘路过，听到对话："这就是本周的主题。代码只是'答案'，文档才是'解释'。好的工程师不只写能跑的代码，还要让其他人能理解你的设计。"

阿码插话："那用 AI 自动生成文档不就行了？"

"AI 能生成框架，但设计意图、架构决策、踩过的坑——这些知识只有你知道。"老潘说，"文档不只是'是什么'，更是'为什么'。"

小北若有所思："所以我需要把 CampusFlow 的 API 设计、架构决策、使用经验都写下来？"

"对。"老潘说，"本周你将学习 API 文档、README、ADR 汇总，还有怎么审查 AI 生成的文档。"

---

## 学习目标

完成本周学习后，你将能够：

1. **理解** API 文档的价值与要素，知道什么样的文档是好文档（Bloom：理解）
2. **应用** OpenAPI/Swagger 规范，为自己的 REST API 生成文档（Bloom：应用）
3. **分析** README 的结构，写出清晰的项目说明文档（Bloom：分析）
4. **评价** AI 生成的文档质量，使用审查清单找出问题（Bloom：评价）
5. **创造** 完整的文档体系，为项目交付做准备（Bloom：创造）

---

<!--
贯穿案例设计：【CampusFlow 文档系统——从"无文档"到"可交付"】
- 第 1 节：从 Bug Bash 中"别人看不懂我的 API"的真实场景出发，理解文档的价值
- 第 2 节：学习 OpenAPI/Swagger 规范，为 CampusFlow API 编写文档
- 第 3 节：写 README，让新用户 5 分钟内能跑起来
- 第 4 节：汇总 ADR，把架构决策整理成知识库
- 第 5 节：CampusFlow 进度——完成 API 文档、README、ADR 汇总，为交付做准备
最终成果：一个"有文档、可交付"的 CampusFlow，别人能看懂 API、能跑起来、能理解设计决策

认知负荷预算检查：
- 本周新概念（综合实战阶段上限 4 个）：
  1. API 文档规范 (OpenAPI/Swagger) —— API 文档标准，新概念
  2. 文档即代码 (Docs-as-Code) —— 文档与代码同步管理的工程实践，新概念
  3. README 结构 —— 项目说明文档的标准结构，新概念（但简单）
  4. 文档审查清单 —— 审查 AI 生成文档的工具，新概念
- 文档在 week_09（REST API）时已提及，本周深化实践
- ADR 在 week_02 已引入，本周汇总整理
- 结论：✅ 4 个新概念，在预算内

回顾桥设计（至少引用前 4-6 周的 3 个概念）：
- [REST API]（来自 week_09）：在第 2 节为 API 编写文档，回顾 RESTful 设计原则
- [ADR]（来自 week_02）：在第 4 节汇总 ADR，回顾架构决策记录的价值
- [契约测试]（来自 week_12）：在第 2 节讨论"文档即契约"的理念
- [Bug Bash]（来自 week_12）：在第 1 节回顾 Bug Bash 中发现的文档缺失问题
- [集成测试]（来自 week_12）：在第 2 节讨论文档如何帮助集成测试

AI 小专栏规划：
专栏 1（放在第 2 节之后，前段）：
- 主题：AI 时代的 API 文档——自动生成 vs 人工审查
- 连接点：与第 2 节编写 OpenAPI 文档呼应
- 使用搜索词："API documentation automation trends 2026", "OpenAPI tools AI integration 2026"

专栏 2（放在第 4 节之后，中段）：
- 主题：Docs-as-Code 与 AI——文档工程新范式
- 连接点：与第 4 节 ADR 汇总和文档管理呼应
- 使用搜索词："Docs-as-Code AI tools 2026", "technical documentation best practices 2026"

CampusFlow 本周推进：
- 上周状态：有集成测试、经过 Bug Bash 验证、前后端能正常通信
- 本周改进：编写 API 文档（OpenAPI）、写 README、汇总 ADR、审查 AI 生成的文档
- 涉及的本周概念：API 文档规范、Docs-as-Code、README 结构、文档审查清单

角色出场规划：
- 小北：在第 1 节遇到"别人看不懂我的 API"的困惑；在第 3 节写 README 时不知道写什么
- 阿码：在第 2 节质疑"为什么不用 AI 自动生成文档"；在第 4 节问"ADR 汇总有什么用"
- 老潘：在第 1 节解释文档的价值；在第 2 节介绍 OpenAPI 规范；在第 4 节强调知识管理的重要性
-->

## 1. 为什么需要文档——代码不是全部

周五下午，小北盯着 Bug Bash 报告，眉头越锁越紧。

隔壁组的反馈一条比一条直接："API 没有文档，只能看代码猜格式""字段名和类型都不清楚""错误码含义不明白""试了五次才调通"。

小北忍不住抱怨："我的代码能跑，测试也全绿，Bug Bash 时也没人发现功能问题，为什么他们还是说'看不懂'？"

老潘停下敲键盘，转过身："问题不在代码，在地图。"

"地图？"

"你代码写得很好，但你没有给别人'导航'。"老潘走到白板前，"代码告诉机器'做什么'——这个字段叫 `title`，类型是 `String`，最大 100 字符。但文档告诉人'为什么'——为什么要有这个字段？它的约束是什么？使用时要注意什么？"

阿码插话："那注释不就够了吗？我每个方法都写了注释。"

"注释是在代码'旁边'，你得先找到代码。"老潘在白板上画了个对比图，"文档是代码的'地图'。想象你到一个陌生城市：
- **代码**：城市的每一条街道、每一栋建筑——详细但庞大
- **文档**：城市的地铁图、景区导览、'从机场怎么到市中心'的指南

你想快速找到目的地，是愿意拿着整张城市的地图册在大街小巷里乱转，还是看一眼地铁图就直奔目标？"

小北若有所思："所以文档是'导航'？"

"对。"老潘说，"文档的核心价值是**降低理解成本**。新同学加入团队、新用户使用你的 API、新成员维护你的代码——文档让他们不用'读代码就能理解'。"

老潘展示了文档的三个层次：

```
┌─────────────────────────────────────────────────────────┐
│                    文档金字塔                        │
├─────────────────────────────────────────────────────────┤
│ 顶层：使用指南（User Guide）                        │
│   - README（怎么跑起来）                              │
│   - 快速开始（5 分钟上手）                            │
├─────────────────────────────────────────────────────────┤
│ 中层：API 文档 / 设计文档（Technical Docs）            │
│   - API 端点、请求/响应格式                            │
│   - 架构决策记录（为什么这样设计）                      │
├─────────────────────────────────────────────────────────┤
│ 底层：代码注释 / Javadoc（Code Comments）              │
│   - 类和方法的功能说明                                 │
│   - 参数、返回值、异常说明                             │
└─────────────────────────────────────────────────────────┘
```

"不同层次的文档面向不同的读者。"老潘解释：
- **用户/外部团队**：只需要顶层的 README 和 API 文档
- **开发者/团队成员**：需要中层的 ADR 和 API 文档
- **维护者**：需要底层的注释和 Javadoc

小北恍然大悟："所以我之前只写了注释（底层），没有写 README 和 API 文档（顶层、中层）？"

"对。"老潘说，"Bug Bash 中隔壁组的问题就是因为缺少中层文档——他们不知道 API 的格式，只能猜。"

阿码举手："那用 AI 自动生成文档不就行了？"

"AI 能生成框架，但设计意图、架构决策、踩过的坑——这些知识只有你知道。"老潘说，"比如 CampusFlow 的 API 为什么用 `/api/tasks` 而不是 `/tasks`？这是设计决策，AI 猜不到。"

老潘想起了 Week 09 你们设计 REST API 时讨论过 **RESTful 风格**——API 端点应该用名词表示资源，用 HTTP 方法表示操作。`/api/tasks` 遵循了这个约定，但这个"为什么"只有你们知道。

小北点头："所以我需要把 CampusFlow 的 API 设计、架构决策、使用经验都写下来？"

"对。"老潘说，"本周你将学习：
1. **API 文档**：用 OpenAPI/Swagger 规范描述你的 REST API
2. **README**：让新用户 5 分钟内能跑起来
3. **ADR 汇总**：把架构决策整理成知识库
4. **文档审查**：怎么判断 AI 生成的文档质量"

老潘补充："文档不是'事后补'，而是和代码一起演进。这叫 **Docs-as-Code（文档即代码）**——文档和代码用同一套 Git 工作流、同一套版本控制。"

小北若有所思："所以文档也是代码的一部分？"

"对。"老潘说，"代码是'答案'，文档是'解释'。好的工程师不只写能跑的代码，还要让其他人能理解你的设计。"

## 2. API 文档——用 OpenAPI 描述你的服务

"现在来写 CampusFlow 的 API 文档。"老潘打开编辑器，创建了一个新文件 `openapi.yaml`。

小北凑过来看："API 文档写什么？把所有接口列出来？"

"不只是列表。"老潘的手指在键盘上悬停，"API 文档的核心是'契约'（Contract）——你承诺给调用者的保证。"

小北想起上周 Bug Bash 时隔壁组的问题——他们不知道请求格式是什么，也不知道错误码代表什么。如果有一份文档，这些问题就不会出现。

"那这个'契约'长什么样？"小北问。

> 💡 **渐进式学习**：我们不直接看完整示例，而是从最小开始，逐步增加复杂度。先理解"核心结构"，再看"完整契约"。这样更容易消化。
>

老潘敲了几行键盘，屏幕上出现了一个 YAML 文件。

```yaml
# 只有两个核心部分：info 和 paths
openapi: 3.0.3
info:
  title: CampusFlow API
  version: 1.0.0

servers:
  - url: http://localhost:8080

paths:
  /api/tasks:
    get:
      summary: 获取所有任务
      responses:
        '200':
          description: 成功返回任务列表
```
"看到没？`$ref`、`schema`、`requestBody` 这些复杂概念都没出现——这个最简示例只让你看到'API 有哪些端点'，不关心'数据格式是什么'。"老潘说，"现在往里加，逐步添加复杂度。"

> 💡 **第二步：添加请求体（RequestBody）**
> `requestBody` 告诉前端'发送请求时需要带什么数据'。比如创建任务时，前端需要传 `title`、`description`——这就是 Request Body。
>
> 下面这个 POST 示例加上了 `requestBody`。

```yaml
# openapi.yaml
openapi: 3.0.3
info:
  title: CampusFlow API
  description: |
    CampusFlow 是校园任务管理系统，支持创建任务、设置截止日期、标记完成。
  version: 1.0.0

servers:
  - url: http://localhost:8080
    description: 本地开发服务器

paths:
  /api/tasks:
    get:
      summary: 获取所有任务
      description: 返回当前用户的所有任务，按创建时间倒序排列
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
      requestBody:        # 🆕 新概念：请求体（Request Body）
        required: true
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

小北盯着这份文档看了一会儿，忽然发现一个问题："这个 `description` 字段——AI 能自动生成吗？"

"部分能。"老潘说，"AI 能从代码中提取'有什么'——路径是 `/api/tasks`、方法有 GET 和 POST、参数是什么。但'为什么'和'注意什么'，AI 猜不到。"

老潘指着 `description` 字段："比如'标题超过 100 字符返回 400'——这是业务规则，不是技术限制。AI 看到的是 `title.length() <= 100`，但它不知道这是'业务约束'还是'技术限制'。"

"有区别吗？"阿码问。

"区别大了。"老潘说，"如果是技术限制（比如数据库字段最大 100 字符），未来可能通过技术方案解决。如果是业务约束（比如'标题太长影响用户体验'），那永远都不会改。这个'为什么'只有你知道。"

老潘补充："OpenAPI 文档不只是给人看的，它还是机器可读的契约：

1. **生成客户端代码**：前端用 OpenAPI Generator 自动生成调用 API 的代码，不需要手写 fetch/axios
2. **生成服务器桩代码**：根据文档自动生成接口定义，确保实现符合契约
3. **自动测试**：用工具验证你的实现是否符合文档（这叫契约测试，Week 12 学过）

小北恍然大悟："所以 OpenAPI 文档不只是'说明'，而是'合同'？"

"对，而且是可以自动执行的合同。"老潘说，"这和 Week 12 的**契约测试**呼应——文档定义契约，测试验证契约。如果文档说'标题最大 100 字符'，你的实现必须遵守。否则测试会失败。"

老潘打开浏览器，访问 `http://localhost:8080/swagger-ui`——一个漂亮的交互式文档页面出现了。

"这是 Swagger UI，它从 `openapi.yaml` 自动生成。"老潘指着页面，"看到那个'Try it out'按钮了吗？点击后可以直接发送测试请求。"

小北眼睛一亮："所以文档不只是'看'的，还是'用'的？"

"对，这就是'可执行文档'——文档和测试合一。"老潘说，"Swagger UI 会把你的 OpenAPI 文档渲染成交互式网页，还能直接发送请求、查看响应。"

小北试了试——点击'Try it out'、点击'Execute'，几秒钟就完成了一次 API 调用。

"**哦！**小北眼睛一亮，"这居然比 curl 命令快多了——不用记参数、不用拼 JSON、不用猜返回格式，点击就能测！"

"对，这就是'可执行文档'的威力。"老潘说，"文档和测试合一，用户'看文档'就能'用 API'。"

"工具是辅助，核心是'把设计说清楚'。"老潘提醒，"OpenAPI 文档的价值不在工具，而在你认真思考'API 契约是什么'。如果文档写得不清楚，再好的工具也没用。"

> **AI 时代小专栏：AI 能生成 API 文档吗？**
>
> 2025-2026 年，AI 工具在 API 文档生成领域取得了显著进展。研究显示，AI 生成的 API 文档能准确覆盖约 80-90% 的基础信息（端点路径、方法、参数类型、响应格式），但在"为什么"层面的说明上仍有不足。
>
> 实践反馈指出：AI 擅长从代码中提取"是什么"（字段名、类型、默认值），但不擅长生成"为什么"（设计理由、业务约束、使用场景）。例如，AI 能识别 `title` 字段是 `String` 类型，但不知道"最大 100 字符"是业务规则而非技术限制。
>
> 2026 年的工程实践显示，最佳工作流是："AI 生成骨架 → 人工补充业务逻辑 → 自动同步代码变更"。例如，当你修改 API 时，AI 工具可以自动更新 OpenAPI 文档，但业务约束的 `description` 仍需人工维护。
>
> 行业数据显示，超过 80% 的知识库文档存在过时或不准确的问题。这意味着 AI 自动生成虽能提升效率，但人工审查和补充仍然不可或缺。
>
> 老潘说："AI 能帮你写文档的'框架'，但'灵魂'（设计意图、业务约束）得靠你。"
>
> 参考资料（访问日期：2026-02-12）：
> - [OpenAPI Specification 3.1](https://spec.openapis.org/oas/v3.1.0)
> - [13 Best OpenAPI Documentation Tools for 2026](https://treblle.com/blog/best-openapi-documentation-tools)
> - [API Trends in 2026: Integration Endpoints to AI Control](https://neosalpha.com/top-api-trends-to-watch/)

## 3. README——让新用户 5 分钟跑起来

API 文档解决了"怎么调用"的问题，但还有一个更基础的问题——"怎么跑起来"。

周三下午，阿码试着跑了隔壁组的 CampusFlow，结果卡了半小时。

"他们没有 README，我不知道怎么启动服务器。"阿码抱怨，"最后去问他们，才知道要先配置数据库、再运行 `mvn jetty:run`。"

老潘路过，听到对话："这就是 README 的价值——API 文档告诉开发者'怎么用接口'，README 告诉所有人'怎么跑起来'。"

"README 写什么？"小北问，"就写'怎么启动'？"

老潘展示了一个优秀的 README 结构：

```markdown
# CampusFlow

> 一个简洁的校园任务管理系统，支持创建任务、设置截止日期、标记完成。

## 快速开始

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

## 功能特性

- ✅ 创建任务、编辑任务、删除任务
- ✅ 设置截止日期、标记完成
- ✅ 任务搜索和过滤
- ✅ RESTful API + OpenAPI 文档

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

## 使用指南

### 创建任务
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"学习集成测试","completed":false}'
```

### 标记完成
```bash
curl -X PUT http://localhost:8080/api/tasks/{id} \
  -H "Content-Type: application/json" \
  -d '{"completed":true}'
```

## 开发指南

### 运行测试
```bash
mvn test
```

### 代码覆盖率
```bash
mvn jacoco:report
# 报告在 target/site/jacoco/index.html
```

## 常见问题

**Q: 数据库在哪里？**
A: SQLite 数据库文件在项目根目录（`campusflow.db`），首次运行自动创建。

**Q: 如何修改端口号？**
A: 编辑 `src/main/resources/config.properties`，修改 `server.port` 配置。

**Q: 任务标题有长度限制吗？**
A: 是的，标题最大 100 字符。

## 架构设计

本项目采用三层架构：
- **Controller 层**：处理 HTTP 请求（Javalin 路由）
- **Service 层**：业务逻辑（任务管理、验证）
- **Repository 层**：数据访问（JDBC + SQLite）

详细的架构决策请参考 [docs/ADR](docs/ADR)。

## 贡献指南

欢迎贡献！请：
1. Fork 本仓库
2. 创建特性分支（`git checkout -b feature/amazing-feature`）
3. 提交更改（`git commit -m 'Add amazing feature'`）
4. 推送到分支（`git push origin feature/amazing-feature`）
5. 创建 Pull Request

## 许可证

MIT License

## 联系方式

- 项目主页：https://github.com/your-org/campusflow
- 问题反馈：https://github.com/your-org/campusflow/issues
```

"看出来结构了吗？"老潘问。

小北想了想："首先快速开始（5 分钟跑起来），然后功能特性、使用指南、常见问题..."

"对。"老潘说，"好的 README 遵循**倒金字塔原则**：
1. **最重要**：快速开始（新用户最关心）
2. **次重要**：功能特性、使用指南
3. **可选**：项目结构、开发指南
4. **参考**：常见问题、许可证"

老潘补充："README 的核心目标是**降低上手门槛**。用户在 GitHub 上看到你的项目，第一个问题不是'这个项目用了什么技术'，而是'我能 5 分钟内跑起来吗'。如果能，他才会继续看；如果不能，他直接关掉页面。"

阿码若有所思："所以 README 不是'项目介绍'，而是'使用手册'？"

"对。"老潘说，"README 的第一个词应该是'Read Me'——读我，就能用。"

小北看着上面的 README："但我写过很多 README，都是堆信息，没有结构..."

"这很正常。"老潘说，"写 README 的技巧是**站在用户角度**——他打开你的页面时，最想知道什么？
- 能用吗？（快速开始）
- 能做什么？（功能特性）
- 怎么用？（使用指南）
- 出问题了怎么办？（常见问题）

用户不关心你的项目结构、开发细节——那是给开发者看的。"

老潘补充："记住，README 是你的'门面'。用户在 GitHub 上看到你的项目，第一眼就是 README。如果 README 写得好，用户才愿意继续往下看。"

小北点头："那我得给 CampusFlow 重写 README，让隔壁组能 5 分钟跑起来。"

## 4. ADR 汇总——把架构决策整理成知识库

README 和 API 文档解决了"怎么用"的问题，但还有一个更深层次的问题——"为什么这么做"。

周五下午，小北看着文件夹里的 4 篇 ADR，有些发愁。

"ADR-001 是 Week 02 写的，ADR-004 是 Week 12 写的..."小北翻着文档，"时间久了，我自己都快忘了当时为什么这么选。更别说新加入的队友了——他们根本不知道我们为什么用 SQLite 而不是 PostgreSQL。"

老潘走过来看了看："这就是需要 **ADR 汇总**的原因。单个 ADR 是'决策碎片'，汇总才是'决策地图'。"

"ADR 汇总？就是把它们复制粘贴到一个文件？"

"不只是复制。"老潘打开一个例子，"汇总的核心是**索引和摘要**——让读者不用翻每个文件，就能看全所有决策、理解决策之间的依赖关系。"

```markdown
# CampusFlow 架构决策索引

本文档汇总了 CampusFlow 项目的所有架构决策记录（ADR）。

## ADR 列表

| ADR | 标题 | 日期 | 状态 | 影响 |
|-----|------|------|------|
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

"这是 ADR 索引。"老潘说，"它把所有决策汇总成一页，方便回顾。"

小北看着这份索引，忽然发现一个有趣的细节："决策依赖图——原来 ADR-001 的选择影响了后面所有决策？"

"对，这就是架构决策的'涟漪效应'。"老潘指着依赖图，"你 Week 02 选择贫血模型（ADR-001），影响了 Week 07 的数据存储方案（ADR-002），又影响了 Week 09 的 API 设计（ADR-003）。每个决策都是后一个决策的'前置条件'。"

小北恍然大悟："所以新队友加入时，不是直接看代码，而是先看 ADR 汇总——就像看电影前先看人物关系图？"

"这个比喻很好。"老潘笑了，"ADR 汇总就是项目的'人物关系图'——让你快速理解'为什么代码长这样'。"

阿码插话："但 ADR 写了就不用改了吗？"

"不是。"老潘说，"ADR 可以'撤销'（Superseded）。比如 ADR-001 选择了贫血模型，但如果项目变大，可以写 ADR-005 说'改用充血模型'，并标记 ADR-001 为'已撤销'。"

老潘指着"待决策事项"："这部分同样重要。它列出未来可能需要决策的问题——比如 CampusFlow 如果上线，可能需要缓存、消息队列。这些不是现在的问题，但提前记录下来，未来遇到时就不会'临时抱佛脚'。"

阿码眼睛一亮："所以 ADR 不只是'回顾'，还是'规划'？"

"对。"老潘说，"ADR 汇总是架构管理的工具——过去（已决策）、现在（正在决策）、未来（待决策）都在一页纸上。"

小北若有所思："我之前觉得 ADR 是'形式主义'，现在才发现它其实是'团队记忆'。人会忘、文档会丢，但 ADR 能保留设计意图。这比代码更重要——代码告诉你'做了什么'，ADR 告诉你'为什么这么做'。"

> **AI 时代小专栏：Docs-as-Code 与 AI——文档工程新范式**
>
> 2025-2026 年，"文档即代码"（Docs-as-Code）的工程实践正在被 AI 工具加速。传统的文档流程（写 Word → 导出 PDF → 上传 Wiki）正在被"Markdown + Git + CI/CD"替代——文档和代码用同一套工作流、同一套版本控制。
>
> 研究显示，采用 Docs-as-Code 的团队文档更新频率提高了 2-3 倍，因为文档不再是"事后补"，而是和代码一起提交。AI 辅助工具（如 Claude Code、Cursor）能自动从代码变更同步更新文档，减少手动维护成本。2026 年 1 月的工具评估显示，最佳实践包括：
> - **文档用 Markdown 写**：与代码同仓库、同 Git 流程
> - **CI 自动渲染**：提交后自动生成 HTML/PDF
> - **AI 审查一致性**：检测代码与文档的差异
> - **版本同步**：文档标签和代码标签一致（v1.0.0 的文档对应 v1.0.0 的代码）
>
> 但 AI 工具也有局限——它能检测"字段名变了"，但不知道"设计意图变了"。比如你把 `title` 改成 `name`，AI 能更新文档，但不知道为什么改——这个理由只有你知道。
>
> 老潘说："AI 能帮你保持文档和代码的一致，但'为什么这样设计'得靠你写下来。"
>
> 参考资料（访问日期：2026-02-12）：
> - [Best docs-as-code solutions for API teams in January 2026](https://buildwithfern.com/post/docs-as-code-solutions-api-teams)
> - [Best Software Documentation Tools in 2026](https://ferndesk.com/blog/best-software-documentation-tools)
> - [Documentation Generation with Claude Code](https://developertoolkit.ai/en/claude-code/lessons/documentation/)

## 5. 文档审查——怎么判断 AI 生成的文档质量

有了 OpenAPI 文档、README、ADR 汇总，文档体系就完整了——但还有一个关键问题：质量保证。

周三下午，阿码试着用 AI 生成了 CampusFlow 的 README。

"你看，AI 生成得很快！"阿码展示结果，"只要 10 秒，比我手写快多了..."

小北看了看，皱眉："但这里写错了——数据库文件不在 `data/` 目录，而是在项目根目录。"

"还有这里，"老潘指着一段文字，"AI 说'支持 PostgreSQL、MySQL、SQLite'，但我们只支持 SQLite。这是'幻觉'——AI 编造了不存在的功能。"

阿码尴尬："那怎么办？AI 不是说能帮我写文档吗？"

"AI 能生成文档，但不能保证质量。"老潘说，"文档审查和代码审查一样重要——你需要用检查清单验证 AI 的输出。"

老潘展示了文档审查清单：

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

### AI 特定问题（幻觉检测）
> ⚠️ **注意**：此项是专门检测 AI 幻觉的补充检查——AI 可能编造不存在的功能、使用过时命令、伪造链接。这些是 AI 工具特有的问题，需要在"准确性"检查之外额外验证。
>
- [ ] 有虚构的功能吗？（AI 可能写你没实现的功能，如"支持 PostgreSQL、MySQL"）
- [ ] 有过时的命令吗？（AI 可能用旧版本语法）
- [ ] 有不存在的链接吗？（AI 可能编造 URL）
```

小北看着这份清单，忽然发现一个关键点："'AI 特定问题'这一项——AI 会编造不存在的东西？"

"对，这叫'幻觉'（Hallucination）。"老潘说，"AI 不是'查数据库'，而是'预测下一个词'。它可能根据训练数据（见过很多支持 PostgreSQL 的项目）推断你的项目也支持，但这是错的。"

阿码想起之前 AI 生成的 README："那怎么判断 AI 是在'编造'还是'推断'？"

"验证。"老潘说，"最简单的方法是：**AI 生成什么，你就验证什么**。
- AI 说命令是 `mvn install`？你复制粘贴跑一遍
- AI 说支持 PostgreSQL？你检查代码里有 JDBC URL 吗
- AI 说链接是 `https://...`？你点开看看"

老潘补充："这个清单和 Week 10 的**AI 代码审查清单**类似——重点是验证。代码错了编译器会报错，文档错了用户会直接放弃使用。"

阿码点头："所以最好的工作流是什么？完全不用 AI？"

"不是'不用'，是'不盲信'。"老潘总结了混合工作流：
1. **AI 生成框架**：让 AI 生成 README/OpenAPI 的骨架（省时间）
2. **人工验证**：逐条运行命令、检查链接、验证示例（保质量）
3. **补充业务逻辑**：AI 不知道的约束、注意事项、常见问题（加价值）
4. **持续同步**：代码变更时，用 AI 检测文档是否需要更新（保持一致）

老潘补充："记住，AI 是倍增器，不是替代者。AI 能加速文档生成，但'文档质量'得靠你把控。就像自动驾驶汽车——它能辅助驾驶，但你还是得盯着路况。"

---

## CampusFlow 进度：文档与知识传递

经过本周的学习，你理解了文档不是"事后补"的负担，而是"可交付"的前提。现在该为 CampusFlow 建立完整的文档体系了——让隔壁组能看懂 API，让新用户能 5 分钟跑起来，让未来的队友能理解决策意图。

### 本周改造计划

1. **编写 API 文档**：
   - 创建 `openapi.yaml`，描述所有 REST API 端点
   - 为每个端点添加 `description`（业务约束、注意事项）
   - 集成 Swagger UI，提供交互式文档

2. **写 README**：
   - 添加"快速开始"（5 分钟运行指南）
   - 添加"功能特性"（让用户知道能做什么）
   - 添加"常见问题"（解决用户的困惑）

3. **汇总 ADR**：
   - 创建 `docs/ADR_INDEX.md`，汇总所有架构决策
   - 为每个 ADR 添加"决策摘要"和"后果"
   - 标记决策之间的依赖关系

4. **审查 AI 生成的文档**：
   - 用 AI 生成 README 骨架
   - 使用审查清单验证准确性、完整性、清晰度
   - 修正 AI 的幻觉和错误

### API 文档示例（openapi.yaml）

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
```

### README 示例

```markdown
# CampusFlow

> 一个简洁的校园任务管理系统，支持创建任务、设置截止日期、标记完成。

## 快速开始（5 分钟）

```bash
git clone https://github.com/your-org/campusflow.git
cd campusflow
mvn compile exec:java -Dexec.mainClass="com.campusflow.Main"
```

访问 http://localhost:8080，API 文档：http://localhost:8080/swagger-ui

## 功能特性

- ✅ 创建任务、编辑任务、删除任务
- ✅ 设置截止日期、标记完成
- ✅ 任务搜索和过滤
- ✅ RESTful API + OpenAPI 文档

## 常见问题

**Q: 数据库在哪里？**
A: SQLite 数据库文件在项目根目录（`campusflow.db`），首次运行自动创建。

**Q: 如何修改端口号？**
A: 编辑 `src/main/resources/config.properties`，修改 `server.port` 配置。
```

### ADR 汇总示例（docs/ADR_INDEX.md）

```markdown
# CampusFlow 架构决策索引

## 决策摘要

| ADR | 决策 | 理由 | 状态 |
|-----|------|------|------|
| ADR-001 | 贫血模型（Anemic Domain Model） | 小团队简单优先 | 已采纳 |
| ADR-002 | SQLite + JDBC | 零配置、学习成本低 | 已采纳 |
| ADR-003 | RESTful API | 前后端分离、标准契约 | 已采纳 |
| ADR-004 | 不引入 Spring Boot | 学习 Java 核心技术 | 已采纳 |

## 待决策

- [ ] 是否引入缓存层（Redis）？
- [ ] 是否需要消息队列（异步任务）？
```

老潘看了这份计划，点头："很好。现在 CampusFlow 是一个'有文档、可交付'的 Web 应用——API 文档让别人能调用，README 让别人能跑起来，ADR 汇总让别人能理解。"

他停顿了一下："文档是交付的最后一步。下周你将学习发布准备与版本管理——怎么给 CampusFlow 打标签、发版本、准备项目集市展示。"

---

## Git 本周要点

### 文档与 Git 工作流

**Docs-as-Code**：文档和代码用同一套 Git 流程。

```bash
# 为文档创建分支
git checkout -b docs/api-documentation

# 添加文档文件
git add openapi.yaml README.md docs/ADR_INDEX.md

# 提交时遵循规范
git commit -m "docs: add OpenAPI specification and README

- Add openapi.yaml for all REST API endpoints
- Write README with quick start guide
- Create ADR index with decision summaries

Reviewed: AI-generated content verified manually
"

# 文档和代码一起合并
git push origin docs/api-documentation
# 创建 PR，合并到主分支
```

---

## 本周小结（供下周参考）

本周你让 CampusFlow 从"能跑"走向"可交付"——这是一个重要的里程碑。

API 文档让你理解了"契约"的价值。OpenAPI 文档不只是"API 列表"，而是前后端的合同——定义了端点、请求、响应、错误码。你学会了用 YAML 格式描述 API，还学会了用 Swagger UI 渲染成交互式文档。这和 Week 09 的 REST API 设计、Week 12 的契约测试形成了完整的闭环：设计 API → 编写文档 → 验证契约。AI 能生成文档骨架，但业务约束、设计理由、注意事项——这些只有你知道，需要人工补充。

README 让你理解了"用户视角"的重要性。好的 README 不是堆信息，而是遵循倒金字塔原则——最重要（快速开始）放在最前面，用户 5 分钟就能跑起来。你学会了站在用户角度写文档，而不是站在开发者角度炫耀技术细节。这和 Bug Bash 中隔壁组的反馈呼应——如果没有 README，新用户根本不知道怎么启动服务器。

ADR 汇总让你理解了"知识管理"的价值。架构决策不只是"记录"更是"索引"——决策摘要、依赖关系、待决策事项，让团队能快速回顾设计意图、规划未来演进。这和 Week 02 开始写 ADR 时的初衷呼应——人会忘，但 ADR 能保留"为什么这么做"的理由。决策依赖图让你看到了架构的"涟漪效应"：Week 02 的贫血模型选择，影响了后面所有技术决策。

文档审查让你理解了 AI 的局限。AI 能生成文档框架，但可能编造不存在的功能（"幻觉"）、用过时的命令、错误的路径。你学会了用审查清单验证 AI 生成的文档——准确性（命令能跑吗？）、完整性（常见问题覆盖了吗？）、清晰度（非专家能看懂吗？）。这和 Week 10 的 AI 代码审查形成呼应，都是"AI 生成 + 人工验证"的工作流。

Docs-as-Code（文档即代码）让你理解了文档和代码是同一套工作流——文档不是"事后补"，而是和代码一起演进、一起版本控制、一起提交。AI 能帮助检测代码与文档的差异，但"为什么这样设计"的知识还得靠你写下来。

CampusFlow 现在是一个"有文档、可交付"的 Web 应用。API 文档让别人能看懂你的设计、能调用接口，README 让别人能 5 分钟跑起来，ADR 汇总让别人能理解你的决策。下周，你将学习发布准备与版本管理——怎么给 CampusFlow 打标签、发版本、准备项目集市展示。

---

## Definition of Done（学生自测清单）

- [ ] 我能解释 API 文档的价值，知道 OpenAPI 规范的要素
- [ ] 我能编写 OpenAPI 文档，描述 REST API 的端点、请求、响应
- [ ] 我能写出清晰的 README，让新用户 5 分钟内跑起来
- [ ] 我能汇总 ADR，建立架构决策索引
- [ ] 我能使用审查清单评估 AI 生成的文档质量
- [ ] 我完成了 CampusFlow 的 API 文档（openapi.yaml）
- [ ] 我完成了 CampusFlow 的 README（快速开始 + 常见问题）
- [ ] 我完成了 CampusFlow 的 ADR 汇总（docs/ADR_INDEX.md）
- [ ] 我理解了"Docs-as-Code"的工程实践
- [ ] 我理解了文档是交付的必要组成部分
