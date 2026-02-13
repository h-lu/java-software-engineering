# 示例 4：16 周课程知识地图

## 问题场景

Week 16 结束时，小北翻开 Week 01 的笔记——那时候连 `ArrayList` 都不会用，现在能写 REST API 了。

但问题是："16 周到底学了什么？"

如果只是罗列"Java、Git、测试、API"，那是一堆碎片。**知识地图**（Knowledge Map）把这些碎片连成一张网——看见概念之间的关系，看见两条主线（ADR + CampusFlow）如何推进，看见自己从"写代码"到"做工程"的演进。

## 核心概念

**四阶段课程结构**：从思维奠基到综合实战，层层递进。

```
Week 01-04: 思维奠基
    ↓
Week 05-08: 系统化工程
    ↓
Week 09-12: AI 时代工程
    ↓
Week 13-16: 综合实战
```

**两条主线**：
1. **ADR 主线**：架构决策记录，贯穿 4 篇，记录技术选型的思考过程
2. **CampusFlow 主线**：从立项到发布，体验完整的软件生命周期

## 使用示例

下面是完整的 16 周知识地图：

```markdown
# 16 周课程知识地图

## 阶段一：思维奠基（Week 01-04）

### Week 01: 类型思维与 Java 入门
**核心概念**：
- 静态类型 vs 动态类型
- 变量声明、基本数据类型
- Scanner 输入

**CampusFlow 进度**：
- 项目启动：选题、组队
- 环境：Java 17 + Maven + Git 仓库

**为什么重要**：
> "从 Python 到 Java，最大的挑战不是语法，而是'类型思维'——在写代码前先想清楚'这是什么类型的数据'。"

---

### Week 02: 类设计与领域建模
**核心概念**：
- 类定义、封装、getter/setter
- SOLID 原则（单一职责、开闭原则）
- 名词提取法（从需求到领域模型）
- **ADR-001**：领域模型设计决策

**CampusFlow 进度**：
- 核心类：User、Room、Booking
- 首席架构师第 1 轮（小北）

**为什么重要**：
> "好的领域模型是项目的地基。Week 02 设计的 User/Room/Booking 三层结构，到 Week 16 还在用。"

---

### Week 03: 异常处理与防御式编程
**核心概念**：
- 受检异常 vs 运行时异常
- try-catch-finally、异常传播
- 防御式编程、FMEA（故障模式分析）
- 尽早失败、优雅降级

**CampusFlow 进度**：
- 输入验证（年龄不能为负数）
- 菜单选择异常处理（输入"abc"不崩溃）

**为什么重要**：
> "Week 03 之前，CampusFlow 遇到非法输入就崩溃。Week 03 之后，它学会了'防御'——在数据进入系统前先检查。"

---

### Week 04: Git 协作与代码审查
**核心概念**：
- Feature Branch 工作流
- Pull Request、Code Review
- 审查检查清单、建设性反馈
- 冲突解决

**CampusFlow 进度**：
- 团队协作规范建立
- 第一次合并冲突（花了 2 小时）

**为什么重要**：
> "代码不是写完就结束了，而是要'被别人审查'。Week 04 的第一次 PR 被老潘批了一顿，但那是我成长的起点。"

---

## 阶段二：系统化工程（Week 05-08）

### Week 05: 集合框架与 Repository 模式
**核心概念**：
- ArrayList、HashMap、泛型
- 增强 for 循环、迭代器
- Repository 模式（数据访问抽象）

**CampusFlow 进度**：
- 用 `ArrayList<Room>` 存储会议室列表
- `RoomRepository` 封装数据访问逻辑
- 首席架构师第 2 轮（阿码）

**为什么重要**：
> "Week 05 之前，数据到处都是。Week 05 之后，数据访问被 Repository 统一管理——这就是'抽象'的价值。"

---

### Week 06: JUnit 5 与测试驱动开发
**核心概念**：
- 单元测试、测试生命周期（@BeforeEach）
- 断言（assertEquals、assertThrows）
- 参数化测试
- TDD（红-绿-重构）

**CampusFlow 进度**：
- 第一个测试：`BookingServiceTest`
- 覆盖率目标：70%

**为什么重要**：
> "Week 06 之前，我不知道代码有没有 bug。Week 06 之后，测试网告诉我'代码是安全的'。"

---

### Week 07: JDBC 与 SQLite 持久化
**核心概念**：
- JDBC、PreparedStatement
- try-with-resources
- SQL 注入防护
- SQLite、内存数据库（H2）

**CampusFlow 进度**：
- **ADR-002**：数据存储方案决策（选择 SQLite）
- 数据持久化：程序关闭后数据不丢失
- 首席架构师第 2 轮完成

**为什么重要**：
> "Week 07 之前，CampusFlow 关闭后所有数据消失。Week 07 之后，它终于能'记住'东西了。"

---

### Week 08: 设计模式与代码重构
**核心概念**：
- 代码坏味道（上帝类、重复代码）
- 重构（提取方法、提取类）
- 策略模式、工厂模式
- 静态代码分析（SpotBugs、PMD）
- 圈复杂度

**CampusFlow 进度**：
- 用策略模式重构"预约规则"
- 代码坏味道清理：拆分 `BookingService` 的长方法
- 首席架构师第 3 轮（老潘）

**为什么重要**：
> "Week 08 之前，'预约规则'是 50 行的 if-else。Week 08 之后，它变成了 3 个策略类——每个规则一个类。"

---

## 阶段三：AI 时代工程（Week 09-12）

### Week 09: REST API 与 Javalin 入门
**核心概念**：
- REST 架构风格
- HTTP 方法（GET、POST、PUT、DELETE）
- JSON 序列化、路由
- API 端点设计

**CampusFlow 进度**：
- **ADR-003**：API 设计决策（RESTful 风格）
- 第一个 API：`GET /api/rooms`
- Javalin 框架集成

**为什么重要**：
> "Week 09 之前，CampusFlow 是命令行工具。Week 09 之后，它变成了 Web 服务——可以被浏览器、移动端调用。"

---

### Week 10: AI 辅助与前端审查
**核心概念**：
- Prompt 工程
- AI 幻觉
- 前端审查检查清单（5 维度）
- Fetch API、CORS、XSS 防护

**CampusFlow 进度**：
- 用 AI 生成前端代码（HTML + JS）
- 用检查清单审查，发现 3 个安全漏洞
- 前后端联调

**为什么重要**：
> "Week 10 是课程最重要的一周——不只是用 AI，而是学会'审视 AI 代码'。AI 能干活，但不能背锅。"

---

### Week 11: 静态分析与覆盖率
**核心概念**：
- 静态分析（SpotBugs、PMD、Checkstyle）
- 代码覆盖率（行覆盖、分支覆盖）
- 质量门禁
- 技术债管理

**CampusFlow 进度**：
- 覆盖率目标提升到 85%
- 本地质量门禁：不达标不能提交
- 技术债清单：10 项，已修复 6 项

**为什么重要**：
> "Week 11 之前，我们靠'自觉'保证质量。Week 11 之后，质量门禁强制我们'必须达标'。"

---

### Week 12: 测试替身与 Bug Bash
**核心概念**：
- 测试替身（Dummy、Stub、Mock、Spy）
- Bug Bash（集体找 bug）
- 根因分析
- 端到端测试、契约测试

**CampusFlow 进度**：
- **ADR-004**：架构演进决策（引入测试策略）
- Bug Bash：发现 15 个 bug，全部修复
- 首席架构师第 4 轮（小北）

**为什么重要**：
> "Week 12 的 Bug Bash 让我们意识到：自己测永远有盲区。新鲜视角能发现你忽略的问题。"

---

## 阶段四：综合实战（Week 13-16）

### Week 13: API 文档与 Docs-as-Code
**核心概念**：
- OpenAPI 规范（Swagger）
- 文档即代码（Git 工作流）
- 文档审查检查清单
- Swagger UI（交互式文档）

**CampusFlow 进度**：
- API 文档迁移到 OpenAPI
- README 完善：快速开始、功能特性、常见问题

**为什么重要**：
> "Week 13 之前，文档是'事后补的'。Week 13 之后，文档和代码同步演进。"

---

### Week 14: 版本管理与部署
**核心概念**：
- Semantic Versioning（SemVer）
- Maven Shade Plugin（打包）
- 环境配置管理
- 部署（云平台、VPS）

**CampusFlow 进度**：
- 发布 v1.0.0
- 打包成可执行 JAR
- 部署到演示环境

**为什么重要**：
> "Week 14 是第一次'真实发布'——用户不需要看代码，只需要 `java -jar campusflow.jar`。"

---

### Week 15: 项目集市与展示准备
**核心概念**：
- 技术演示原则（传递价值、展示能力、分享成长）
- 演示脚本设计（10 分钟：开场 1 分钟 + 主体 7 分钟 + 结尾 2 分钟）
- 展示材料设计（PPT、海报）
- 问答应对策略

**CampusFlow 进度**：
- 准备演示脚本（8 分钟精简版）
- 设计 PPT（13 页）和海报
- 准备 Q&A 应答清单

**为什么重要**：
> "Week 15 之前，我们以为'功能做好了自然会讲'。Week 15 之后，我们明白'怎么讲'和'做了什么'一样重要。"

---

### Week 16: 工程复盘与职业规划
**核心概念**：
- KPT 复盘法（Keep、Problem、Try）
- 反馈闭环（收集 → 分类 → 分析 → 行动 → 验证）
- 能力雷达图（六维能力模型）
- 工程师职业发展路径

**CampusFlow 进度**：
- 项目集市展示（收集 23 份反馈）
- 工程复盘报告（4 篇 ADR 回顾）
- 能力评估（平均 3.5/5 分）

**为什么重要**：
> "Week 16 不是终点，而是下一段旅程的起点。复盘让我们把'经历'变成'经验'。"

---

## 两条主线

### 主线一：ADR（架构决策记录）

| ADR | 周次 | 标题 | 决策 | 影响 |
|-----|------|------|------|------|
| ADR-001 | Week 02 | 领域模型设计 | User + Room + Booking 三层模型 | Week 16 还在用，证明设计成功 |
| ADR-002 | Week 04 | 数据存储方案 | SQLite（放弃 JSON 文件） | 轻量，但 Week 12 发现并发性能有限 |
| ADR-003 | Week 06 | API 设计风格 | RESTful + OpenAPI | 规范，但初期学习成本高 |
| ADR-004 | Week 08 | 架构演进 | 引入策略模式处理预约规则 | 代码更灵活，但类数量翻倍 |

**ADR 的价值**：
- Week 16 回顾时，能找到"为什么这么做"的理由
- 团队协作时，避免重复讨论已决策的事项
- 项目集市上，用 ADR 回答评委的"为什么"问题

---

### 主线二：CampusFlow（项目演进）

```
Week 01: 项目启动
   ├─ 选题：会议室预约系统
   ├─ 组队：小北、阿码、老潘
   └─ 环境：Java 17 + Maven + Git

Week 02-04: 领域模型与异常处理
   ├─ 核心类设计（ADR-001）
   ├─ 输入验证与异常处理
   └─ Git 协作规范建立

Week 05-08: 数据持久化与测试
   ├─ Repository 模式
   ├─ JUnit 5 测试（70% 覆盖率）
   ├─ SQLite 持久化（ADR-002）
   └─ 设计模式应用（策略模式）

Week 09-12: Web 化与 AI 协作
   ├─ REST API 设计（ADR-003）
   ├─ Javalin 后端
   ├─ AI 生成前端（人工审查）
   ├─ 质量门禁（85% 覆盖率）
   └─ 架构演进（ADR-004）

Week 13-16: 交付与展示
   ├─ API 文档完善
   ├─ 版本管理与部署（v1.0.0）
   ├─ 项目集市展示
   └─ 工程复盘报告
```

**CampusFlow 的成长**：
- Week 01：一个想法
- Week 04：一个命令行工具
- Week 08：一个带测试和数据库的完整应用
- Week 12：一个 Web 服务
- Week 16：一个可交付的产品

---

## 概念关联网络

### 类型系统主线
```
Week 01: 静态类型
    ↓
Week 02: 封装（private 字段 + getter/setter）
    ↓
Week 05: 泛型（ArrayList<String>）
    ↓
Week 09: JSON 序列化（对象 ↔ 字符串）
```

### 错误处理主线
```
Week 03: 异常处理（try-catch）
    ↓
Week 06: 测试（assertThrows）
    ↓
Week 07: SQL 注入防护（PreparedStatement）
    ↓
Week 12: 根因分析（为什么出 bug？）
```

### 协作主线
```
Week 04: Feature Branch + Code Review
    ↓
Week 08: 首席架构师轮换
    ↓
Week 10: AI 审查检查清单
    ↓
Week 15: 技术演示 + Q&A
```

---

## 课程回顾桥

| 起点周 | 概念 | 复现周 | 连接方式 |
|--------|------|--------|----------|
| Week 01 | 静态类型 | Week 05 | 泛型是类型系统的延伸 |
| Week 02 | 领域模型 | Week 07 | Repository 是领域模型的持久化层 |
| Week 03 | 异常处理 | Week 09 | REST API 用异常码表示错误 |
| Week 04 | Git 协作 | Week 13 | 文档也用 Git 工作流（Docs-as-Code） |
| Week 06 | 单元测试 | Week 12 | Bug Bash 是单元测试的补充 |
| Week 09 | REST API | Week 13 | OpenAPI 是 REST API 的文档规范 |

---

## 下一步学习建议

**如果你想在 3 个月后达到"中级工程师"水平**：

1. **巩固基础**（Week 17-20）：
   - 学习 Spring Boot（对比 Javalin 的差异）
   - 研究 MySQL（对比 SQLite 的限制）
   - 练习 TDD（先写测试再写代码）

2. **深入工程**（Week 21-24）：
   - 学习 Docker（容器化部署）
   - 配置 GitHub Actions（CI/CD）
   - 参与开源项目（GitHub 上找小项目）

3. **扩展视野**（Week 25-28）：
   - 学习微服务架构（对比单体应用）
   - 研究分布式系统（CAP 定理、最终一致性）
   - 阅读经典书籍：《设计模式》《重构》《代码整洁之道》

**推荐阅读**：
- 《代码整洁之道》—— Week 02 的 SOLID 原则深入
- 《重构》—— Week 08 的重构方法论
- 《持续交付》—— Week 14 的部署实践
- 《五十年后》—— 软件工程的本质思考

---

## 相关术语

- **回顾桥**：每个阶段结束时，回看上一阶段的概念
- **锚点**：Week 02 的 ADR-001 → Week 16 的架构回顾
- **CampusFlow 进度**：从"想法"到"产品"的完整旅程
