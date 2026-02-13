# Week 14：发布与部署——从"本地能跑"到"别人能用"

> "代码写完了只是完成了 50%，剩下 50% 是让用户能真正用上。"
> — 老潘

2026 年的软件工程现场，"部署"正在经历一场深刻的变革。云原生技术已成为主流——根据 Datadog 的最新报告，超过 78% 的工程团队采用混合架构，结合 Serverless 和容器化部署来优化成本与性能。与此同时，研究显示约 70% 的线上事故源于"本地能跑、生产环境崩了"的环境差异。容器化技术（Docker）正在成为标准部署格式，保证"一次构建，到处运行"，而 Serverless 平台按需计费、自动扩容的特性，让小流量应用的部署门槛降到历史最低。AI 工具也在进入部署领域——从生成 Dockerfile 到检测环境配置问题——但"回滚策略"、"监控告警"、"灰度发布"等工程决策仍需人类经验。本周你将学习如何让 CampusFlow 从"本地开发"走向"可访问服务"——构建可部署的软件包、管理环境配置、设计部署策略、建立发布流程。

---

## 前情提要

上周你为 CampusFlow 编写了完整的文档体系——API 文档（OpenAPI 规范）、README（快速开始指南）、ADR 汇总（架构决策记录）。小北看着 `openapi.yaml`，有种成就感："现在隔壁组能看懂我们的 API 了。"

周三下午，小北把 CampusFlow 推荐给室友试用。

"我怎么访问？"室友掏出手机。

小北想了想："呃...你得自己克隆代码、配环境、运行服务器..."

室友皱眉，把手机收回去："这也太麻烦了。为什么不能像你们说的 Web 应用一样，直接打开网址就能用？"

小北愣住了——他的 CampusFlow 只能在本地运行，室友想用还得自己折腾一遍。这种尴尬让他意识到：**写完代码和让用户用上，是两回事**。

老潘路过，听到对话："这就是'本地开发'和'可访问服务'的差距。"

"有什么区别？"小北问。

老潘在白板上画了个对比图：

```
┌─────────────────────────────────────────────────────────────┐
│                  本地开发 vs 生产部署                         │
├─────────────────────────────────────────────────────────────┤
│ 【本地开发】（你能跑）                                        │
│   - 代码在你电脑上                                            │
│   - 访问地址：localhost:8080（只有你能访问）                   │
│   - 数据库：campusflow.db（在你电脑上）                        │
│   - 环境：IntelliJ + 开发配置                                  │
│                                                              │
│ 【生产部署】（别人能用）                                      │
│   - 代码在服务器上                                            │
│   - 访问地址：https://campusflow.example.com（所有人能访问）    │
│   - 数据库：云数据库或持久化存储                                │
│   - 环境：服务器 + 生产配置                                    │
└─────────────────────────────────────────────────────────────┘
```

"看到区别了吗？"老潘问，"本地开发是'你自己用'，生产部署是'给别人用'。"

小北若有所思："所以我需要把 CampusFlow 发布到服务器上？"

"对。"老潘说，"'发布'（Release）和'部署'（Deployment）是两回事：
- **发布**：给软件打上版本号，声明'这个版本可以用了'
- **部署**：把软件安装到服务器上，让用户能访问"

老潘补充："Week 11 你们学了 **CI 基础**（持续集成）——自动构建和测试。但 CI 只解决了'代码质量'问题，没解决'用户访问'问题。本周你要学的是'发布和部署'——怎么让 CampusFlow 从本地走向公网。"

阿码插话："那用 AI 自动部署不就行了？"

"AI 能生成配置，但部署决策得你来做。"老潘说，"比如：
- 出问题了怎么办？（回滚策略）
- 怎么平滑升级？（灰度发布）
- 数据库怎么迁移？（数据备份）

这些不是技术问题，是工程决策。AI 猜不到'你的用户能接受多长时间停机'、'数据丢失的代价是什么'。"

小北点头："所以我需要学习完整的发布流程？"

"对。"老潘说，"本周你将学习：
1. **版本管理**：用 Semantic Versioning 标记版本（v1.0.0、v1.1.0...）
2. **构建打包**：用 Maven 生成可执行的 JAR
3. **环境配置**：管理开发/测试/生产环境的差异
4. **部署策略**：云服务部署、容器化基础
5. **发布流程**：从'本地开发'到'线上可用'的完整路径"

老潘补充："记住，代码写完了只是完成了 50%。剩下 50% 是让用户能真正用上。"

---

## 学习目标

完成本周学习后，你将能够：

1. **理解** 版本管理的规范，知道 Semantic Versioning 的规则（Bloom：理解）
2. **应用** Maven 构建工具，生成可执行的 JAR 包（Bloom：应用）
3. **分析** 环境配置的挑战，设计多环境管理方案（Bloom：分析）
4. **评价** 不同部署策略的优劣，选择合适的方案（Bloom：评价）
5. **创造** 完整的发布流程，将 CampusFlow 部署为可访问的服务（Bloom：创造）

---

<!--
贯穿案例设计：【CampusFlow 发布系统——从"本地能跑"到"别人能用"】
- 第 1 节：从室友"怎么访问你的应用"的真实场景出发，理解"本地能跑"和"别人能用"的差距
- 第 2 节：学习版本管理（Semantic Versioning），给 CampusFlow 打标签
- 第 3 节：学习 Maven 构建打包，生成可执行 JAR
- 第 4 节：学习环境配置管理，处理多环境差异
- 第 5 节：CampusFlow 进度——打包部署，让别人能通过公网访问

最终成果：一个"可部署、可访问"的 CampusFlow，室友可以直接打开网址使用

认知负荷预算检查：
- 本周新概念（综合实战阶段上限 4 个）：
  1. Semantic Versioning（语义化版本）—— 版本管理规范，新概念
  2. Maven Assembly/Shade Plugin —— 打包工具，新概念（但 Maven 在 week_01 已引入）
  3. 环境配置管理 —— 多环境配置策略，新概念
  4. 部署策略（云服务/容器）—— 部署方案，新概念
- CI 基础在 week_11 已学习，本周深化发布流程
- Git 标签在 week_04 已提及，本周系统学习版本管理
- 结论：✅ 4 个新概念，在预算内

回顾桥设计（至少引用前 4-6 周的 3 个概念）：
- [CI 基础]（来自 week_11）：在第 3 节讨论构建时，回顾 CI 自动构建和测试
- [静态分析]（来自 week_11）：在第 3 节讨论质量门禁时，回顾构建前的检查
- [Bug Bash]（来自 week_12）：在第 5 节讨论发布策略时，回顾 Bug Bash 发现的问题是否已修复
- [REST API]（来自 week_09）：在第 4 节讨论环境配置时，回顾 API 端点配置
- [SQLite]（来自 week_07）：在第 4 节讨论环境差异时，回顾数据库路径配置
- [ADR]（来自 week_02）：在第 5 节讨论发布决策时，回顾架构决策的影响

AI 小专栏规划：
专栏 1（放在第 2 节之后，前段）：
- 主题：AI 与版本管理——自动生成 vs 人工决策
- 连接点：与第 2 节 Semantic Versioning 呼应

专栏 2（放在第 4 节之后，中段）：
- 主题：2026 年的部署趋势——云原生与 AI 辅助部署
- 连接点：与第 4 节环境配置和部署策略呼应

CampusFlow 本周推进：
- 上周状态：有完整文档（API 文档、README、ADR），但只能在本地运行
- 本周改进：版本管理（打标签）、Maven 打包（可执行 JAR）、环境配置管理、部署到云服务
- 涉及的本周概念：Semantic Versioning、Maven 打包、环境配置、部署策略

角色出场规划：
- 小北：在第 1 节遇到室友"怎么访问"的尴尬；在第 3 节打包时遇到依赖问题
- 阿码：在第 2 节质疑"为什么版本号这么复杂"；在第 4 节问"为什么不能一个配置打天下"
- 老潘：在第 1 节解释部署的价值；在第 2 节介绍 Semantic Versioning；在第 5 节强调发布流程和回滚策略
-->

## 1. "本地能跑"不等于"别人能用"

周三下午，小北自信满满地向室友展示 CampusFlow。

"这个任务管理系统很棒！"小北打开 IntelliJ，点击运行，"你看，界面出来了，能创建任务、标记完成..."

室友凑过来看，掏出手机："那我回家怎么用？发我个链接？"

小北愣住了——他的 CampusFlow 绑定在 `localhost:8080`，室友的电脑根本访问不到。

"呃...你得先装 Java 17、克隆代码、配置 Maven、运行服务器..."小北越说越没底气。

室友皱眉，把手机收回去："这也太麻烦了。我就想打开网址用一用，不想学编程。"

小北无言以对。老潘路过，听到对话，停下脚步。

"这就是'本地开发'和'可访问服务'的差距。"老潘在白板上画了两个框，"本地开发是你自己在 IDE 里点'运行'，生产部署是把代码放到服务器上，让所有人能通过网址访问。"

"有什么区别？"阿码好奇地问。

"四个字：**可访问性**。"老潘说，"本地开发只有你能访问，生产部署所有人能访问。这意味着：
- 代码要部署到公网服务器
- 需要一个域名（比如 https://campusflow.example.com）
- 需要配置 HTTPS（浏览器不会让用户访问不安全的 HTTP 网站）
- 需要考虑'如果服务崩了怎么办'（监控、回滚）

这四个问题，本地开发一个都不用想。"

小北若有所思："所以我需要学习怎么部署？"

"对。"老潘说，"'部署'（Deployment）是把软件安装到服务器上，让用户能访问。但部署之前，你得先学会'发布'（Release）——给软件打上版本号。"

"为什么要打版本号？"阿码问，"代码不是直接更新就行了吗？"

老潘摇头："想象你发布了 v1.0，用户开始使用。第二天你发现个 bug，修好之后直接覆盖 v1.0——用户不知道你改了什么，也不敢升级。如果你发布 v1.0.1（PATCH 版本），用户知道这是 bug 修复，可以放心升级。"

"版本号是给用户的承诺。"老潘补充，"它告诉用户'我改了什么'、'能不能直接升级'、'会不会搞坏我的代码'。"

小北点头："所以我需要先学习版本管理，再学习部署？"

"对。"老潘说，"本周的顺序是：
1. 版本管理：怎么给软件打版本号（Semantic Versioning）
2. 构建打包：怎么把代码变成可执行的文件（Maven JAR）
3. 环境配置：怎么处理开发/生产环境的差异（配置文件）
4. 部署策略：怎么把软件放到公网上（云服务 / Docker）

学完这四步，你就能让室友通过网址访问 CampusFlow 了。"

老潘顿了顿，补充道："Week 11 你们学了 **CI 基础**（持续集成）——自动构建和测试。CI 解决了'代码质量'问题，但没解决'用户访问'问题。本周你要学的是'发布和部署'——完整的 CI/CD 流程。"

**CD 是 Continuous Deployment（持续部署），意思是：代码通过测试之后，自动部署到生产环境。**

小北若有所思："所以我还要学习自动部署？"

"那是 Week 15 的内容。"老潘说，"本周先把 CampusFlow 手动部署上去，理解整个流程。下周再学自动化。"

## 2. 版本管理——用 Semantic Versioning 说清"变了多少"

"先来聊聊版本号。"老潘指着 CampusFlow 的 README，"你这里写的版本是 '1.0'——但'1.0'是什么意思？"

小北想了想："就是...第一个版本？"

"那如果你修了个 bug，版本号变成什么？'1.1'？'1.0.1'？'2.0'？"老潘问。

小北语塞——他确实没想过这个问题。

阿码插话："我之前看一个开源项目，版本号从 1.4.0 直接跳到 2.0.0，issue 里吵翻了。有人说'不应该破坏兼容性'，有人说'新功能太重要了值得大版本'。这就是因为版本号没遵循统一规范吗？"

"版本号是给用户的承诺。"老潘说，"想象你是用户，看到 'v1.0' → 'v2.0'，你会想：
- v2.0 有什么新功能？
- 我能直接升级，还是需要改代码？
- 会不会有破坏性变更？

如果版本号'随便标'，用户根本不敢升级。"

老潘在白板上写下了 **Semantic Versioning（语义化版本）**：

```
格式：MAJOR.MINOR.PATCH（主版本.次版本.补丁版本）
示例：1.0.0、1.1.0、2.0.0

规则：
├── MAJOR（主版本）：不兼容的 API 变更
│   示例：1.0.0 → 2.0.0（删除了某个 API 端点）
│
├── MINOR（次版本）：向后兼容的功能新增
│   示例：1.0.0 → 1.1.0（新增了搜索功能）
│
└── PATCH（补丁版本）：向后兼容的问题修复
    示例：1.0.0 → 1.0.1（修复了任务标题截断的 bug）
```

"看到规则了吗？"老潘解释，"版本号的三位数字分别告诉你：
- **改了什么**：新增功能（MINOR）、修复 bug（PATCH）、破坏性变更（MAJOR）
- **能不能直接升级**：PATCH 和 MINOR 可以直接升级，MAJOR 需要检查变更
- **风险有多高**：PATCH 最低，MAJOR 最高"

小北若有所思："所以版本号不是'随便标'，而是'变更日志'？"

"对。"老潘说，"版本号是一种沟通工具——你告诉用户'我改了什么'，用户决定'要不要升级'。根据 2026 年的行业调查，约 65% 的开源项目采用 Semantic Versioning。"

老潘展示了 CampusFlow 的版本演进示例：

```
CampusFlow 版本历史：

v1.0.0（2026-01-15）
- 初始发布
- 功能：创建任务、编辑任务、标记完成

v1.0.1（2026-01-20）← PATCH
- 修复：任务标题超过 100 字符时不再截断
- 风险：低，可直接升级

v1.1.0（2026-02-01）← MINOR
- 新增：任务搜索功能
- 新增：任务过滤（按状态、按日期）
- 风险：中，可直接升级

v2.0.0（2026-03-01）← MAJOR
- 破坏性变更：API 端点从 `/api/tasks` 改为 `/api/v2/tasks`
- 新增：用户认证
- 风险：高，需要修改前端代码
```

"看到变化了吗？"老潘问，"用户看到 'v1.0.1'，知道是 bug 修复，可以放心升级。看到 'v2.0.0'，知道有破坏性变更，需要检查代码。"

小北恍然大悟："所以版本号是'承诺'——你承诺'这个版本的变更不会搞坏我的代码'？"

"对。"老潘说，"这就是 Semantic Versioning 的核心理念：**版本号传递意义**。"

阿码举手："那 Git 的 tag 怎么用？"

"Git tag 是版本号的'锚点'。"老潘解释，"当你发布 v1.0.0 时，给当前 commit 打个标签 `v1.0.0`。这样你永远能找到'这个版本的代码是什么'。"

老潘展示了 Git tag 的使用：

```bash
# 发布 v1.0.0
git tag -a v1.0.0 -m "Release v1.0.0: Initial release"

# 推送标签到远程仓库
git push origin v1.0.0

# 查看所有标签
git tag

# 检出某个版本（查看该版本的代码）
git checkout v1.0.0

# 查看某个版本的文件
git show v1.0.0:README.md
```

"为什么要打标签？"小北问。

"因为'版本号'要和'代码'绑定。"老潘说，"假设用户报告了一个 bug：'v1.0.1 有问题'。你需要知道'v1.0.1 对应哪份代码'。如果没有标签，你只能靠记忆猜。"

老潘补充："Git tag 和 Semantic Versioning 配合使用，就是标准的版本管理流程：
1. 开发新功能或修复 bug
2. 更新版本号（遵循 SemVer 规则）
3. 打 Git tag
4. 推送标签到远程仓库
5. 构建和发布"

小北点头："那我得给 CampusFlow 重新规划版本号？"

"对。"老潘说，"你现在是 v0.x（开发中），本周要发布 v1.0.0（第一个正式版本）。记住，只有当你的软件'稳定、可用、有完整文档'时，才应该发布 v1.0.0。"

> **AI 时代小专栏：AI 与版本管理——自动生成 vs 人工决策**
>
> 2026 年的版本管理现场，一个有趣的趋势正在浮现：人类正在逐步退出"写 changelog"这件事，但"版本决策"依然牢牢握在手里。
>
> 拿 **semantic-release** 这个工具来说——它周下载量超过 230 万次，被 1200 多个包依赖。它的核心理念很激进："移除人类情感与版本号之间的直接联系，严格遵循 Semantic Versioning 规范"。它能自动分析 commit messages，判断这是 PATCH（修复）、MINOR（新功能）还是 MAJOR（破坏性变更），然后自动生成版本号、打标签、发布到 npm、生成 changelog——全程无人值守。
>
> 实践团队 Briefly 的故事很有代表性：他们之前每周花 2 小时手动写 changelog，要把技术细节翻译成产品友好的语言。后来改用 AI 工作流——抓取 git commits → AI 总结 → 分享给团队——时间从 2 小时降到 5 分钟。Medium 团队也有类似经历：从"痛苦地每周写 changelog"到"让 AI 干这事，我们只负责审查"。
>
> GitHub 和 GitLab 也跟进了。GitHub 的"自动生成发布笔记"功能会分析 PR 标签和 commit 信息，生成结构化的 release notes；GitLab 的 Changelog API 可以直接在 CI/CD 中自动生成发布内容。2025 年的调查显示，85% 的 IT 专业人士认为"更清晰的更新沟通能增加对软件发布的信心"——这解释了为什么自动化 changelog 工具在 2026 年如此火爆。
>
> 但这里有个关键：**AI 能检测"代码层变更"，但判断不了"用户层影响"**。AI 看到你删除了某个 `public` 方法，会建议 MAJOR 升级。但如果这个方法根本没有用户调用呢？实际影响是 PATCH 级别。这种"用户是否真的在用"的判断，AI 猜不到。
>
> 所以 2026 年的最佳实践是"半自动"：AI 分析代码 → 建议版本号 → 人工确认"用户影响" → 打标签发布。AI 加速了流程（版本号生成时间减少约 40%），但"这个变更会不会搞坏用户的代码"——这个责任得你背。
>
> 回到你刚学的 Semantic Versioning：AI 能帮你判断"这是 MAJOR 还是 MINOR"，但"你的用户能否接受这个 API 变更"——这个决策得靠你对项目的理解。版本号是给用户的承诺，承诺不能外包给 AI。
>
> 参考（访问日期：2026-02-12）：
> - [semantic-release npm 包（周下载量 2.3M）](https://www.npmjs.com/package/semantic-release)
> - [Semantic Versioning 自动化实践](https://oneuptime.com/blog/post/2026-01-25-semantic-versioning-automation/view)
> - [How We Stopped Writing Changelogs (Medium)](https://jmscarpa.medium.com/how-we-stopped-writing-changelogs-and-let-ai-do-it-7080275cf221)
> - [GitLab 自动化发布笔记教程](https://about.gitlab.com/blog/tutorial-automated-release-and-release-notes-with-gitlab/)
> - [GitHub 自动生成发布笔记文档](https://docs.github.com/en/repositories/releasing-projects-on-github/automatically-generated-release-notes)

## 3. 构建打包——用 Maven 生成可执行的 JAR

版本号定了，接下来是"打包"——把代码变成可执行的文件。

周三下午，小北试着把 CampusFlow 发给室友。

"你先把代码克隆下来..."小北开始指导，"然后安装 Maven...运行 `mvn compile exec:java`..."

"等等，"室友打断，"为什么不直接发我一个文件？双击就能运行那种？"

小北愣住了——他的 CampusFlow 需要 Maven、需要源代码、需要手动编译，根本不是"一个文件"。

老潘路过，听到对话："这就是为什么需要'打包'。"

"打包？"小北问。

"把你的应用和所有依赖打包成一个可执行的 JAR 文件。"老潘解释，"用户只需要 Java，双击 JAR 就能运行，不需要 Maven、不需要源代码。"

老潘在白板上画了对比：

```
┌─────────────────────────────────────────────────────────────┐
│                  源代码 vs 可执行 JAR                         │
├─────────────────────────────────────────────────────────────┤
│ 【源代码分发】（你得自己编译）                                │
│   用户需要：                                                  │
│   1. 克隆源代码                                              │
│   2. 安装 Maven                                              │
│   3. 运行 `mvn compile exec:java`                             │
│   4. 依赖网络（下载依赖库）                                    │
│                                                              │
│ 【JAR 分发】（双击运行）                                      │
│   用户需要：                                                  │
│   1. 下载 campusflow-v1.0.0.jar                              │
│   2. 安装 Java 17                                            │
│   3. 运行 `java -jar campusflow-v1.0.0.jar`                   │
│   4. 离线也能运行（所有依赖已打包）                            │
└─────────────────────────────────────────────────────────────┘
```

"看到区别了吗？"老潘问，"源代码分发是'开发者的方式'，JAR 分发是'用户的方式'。记住，**用户体验从安装开始**。如果用户连运行都不会，你的功能再强也没用。"

小北若有所思："所以我需要用 Maven 把 CampusFlow 打包成 JAR？"

"对。"老潘说，"Maven 有个插件叫 **maven-shade-plugin**，可以把所有依赖打包成一个 JAR。你之前用的 `mvn compile exec:java` 只是在开发阶段方便，正式分发得用 JAR。"

老潘打开了 `pom.xml`，展示了配置：

```xml
<project>
  <!-- ... 其他配置 ... -->

  <build>
    <plugins>
      <!-- Maven Shade Plugin：打包所有依赖到一个 JAR -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.5.1</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <!-- 主类：JAR 运行时的入口点 -->
              <transformers>
                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                  <mainClass>com.campusflow.Main</mainClass>
                </transformer>
              </transformers>
              <!-- 排除签名文件（避免打包冲突） -->
              <filters>
                <filter>
                  <artifact>*:*</artifact>
                  <excludes>
                    <exclude>META-INF/*.SF</exclude>
                    <exclude>META-INF/*.DSA</exclude>
                    <exclude>META-INF/*.RSA</exclude>
                  </excludes>
                </filter>
              </filters>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
```

"看到关键配置了吗？"老潘解释，"`maven-shade-plugin` 做了三件事：
1. **打包所有依赖**：把 Javalin、Gson、SQLite JDBC 等全部打包到一个 JAR
2. **设置主类**：告诉 Java '从这个类开始运行'
3. **排除签名文件**：不同依赖库的数字签名文件会冲突，排除它们避免打包错误"

小北看着配置，忽然发现一个问题："这个 `mainClass`——如果我写错了会怎样？"

"JAR 能打包，但运行时会报错：'no main manifest attribute'。"老潘说，"所以 Week 11 的 **测试覆盖率** 和 **质量门禁** 很重要——你需要测试主类能正常启动。"

老潘展示了打包和运行的命令：

```bash
# 清理之前的构建
mvn clean

# 打包（会运行测试）
mvn package

# 生成的 JAR 在 target/ 目录
ls -lh target/*.jar

# 运行 JAR
java -jar target/campusflow-1.0.0.jar

# 查看版本号（JAR 中的 MANIFEST.MF）
unzip -p target/campusflow-1.0.0.jar META-INF/MANIFEST.MF
```

小北试了试——几秒钟后，终端输出了 `Server started on port 8080`。

"成功了！"小北眼睛一亮，"现在室友只需要下载这个 JAR，双击就能运行？"

"对。"老潘说，"这和 Week 11 的 **CI 基础** 呼应——CI 自动构建和测试，确保代码质量。现在你在 CI 之后增加了打包步骤，产生可分发的 JAR 文件。完整的 CI/CD 流程是：自动测试 → 自动构建 → 自动部署。"

老潘展示了跨平台脚本：

**Linux/Mac（start.sh）**：
```bash
#!/bin/bash
java -jar campusflow-1.0.0.jar
```

**Windows（start.bat）**：
```batch
@echo off
java -jar campusflow-1.0.0.jar
```

"用户双击脚本就能运行。"老潘说，"这样更友好。"

小北若有所思："所以打包不只是'生成 JAR'，而是'让用户能方便地运行'？"

"对。"老潘说，"记住，**用户体验从安装开始**。如果用户连运行都不会，你的功能再强也没用。"

老潘补充："打包和 Week 11 的 **CI 基础** 呼应——CI 可以自动构建 JAR、运行测试、生成构建报告。这样每次 commit 都能产生可分发的软件包，确保'代码能跑'才能合并。"

阿码举手问："我看到有些开源项目提供 `.exe` 安装包，双击就自动安装了。Java 项目也能做吗？还是非得用命令行？"

"Java 也可以打包成 `.exe`，工具很多。"老潘说，"但那已经超出本周范围了。本周的目标是理解'可执行 JAR'——打包的核心思想。至于图形化安装、桌面启动器，等你把基础打牢了再学也不迟。下周你将学习更便捷的部署方式，连 JAR 都不用手动运行。"

小北点头："那我得给 CampusFlow 配置 maven-shade-plugin，生成可执行的 JAR。"

"对。"老潘说，"下一步是环境配置——开发环境和生产环境的差异怎么处理。"

## 4. 环境配置——让一套代码适应多个环境

周五下午，小北尝试把 CampusFlow 部署到云服务器上。

"奇怪，"小北盯着日志，"在本地运行好好的，怎么到服务器上就崩了？"

阿码凑过来看："报错是什么？"

"数据库文件找不到。"小北指着日志，"本地数据库在 `~/projects/campusflow/campusflow.db`，服务器上没有这个路径。"

老潘路过，听到对话："这就是**环境差异**的问题。"

"环境差异？"小北问。

"开发环境、测试环境、生产环境——它们的配置不一样。"老潘在白板上列了对比：

```
┌─────────────────────────────────────────────────────────────┐
│                    三种环境的差异                             │
├─────────────────────────────────────────────────────────────┤
│ 【开发环境】（你的电脑）                                      │
│   - 数据库：campusflow.db（本地文件）                          │
│   - 端口：8080                                                │
│   - 日志级别：DEBUG（详细日志）                                │
│   - API 基础路径：/api                                        │
│                                                              │
│ 【测试环境】（QA 团队使用）                                    │
│   - 数据库：test_campusflow.db（测试数据库）                   │
│   - 端口：8081                                                │
│   - 日志级别：INFO（正常日志）                                 │
│   - API 基础路径：/api/test                                  │
│                                                              │
│ 【生产环境】（真实用户使用）                                   │
│   - 数据库：云数据库或持久化存储                                │
│   - 端口：80（公网标准端口）                                   │
│   - 日志级别：WARN（只记录警告和错误）                          │
│   - API 基础路径：/api                                        │
└─────────────────────────────────────────────────────────────┘
```

"看到差异了吗？"老潘问，"数据库路径、端口、日志级别——每个环境都不一样。如果你把这些硬编码在代码里，每次部署都要改代码。"

小北恍然大悟："所以我不能硬编码这些配置？"

"对。"老潘说，"你需要**环境配置管理**——让一套代码适应多个环境。"

老潘介绍了三种常见方案：

### 方案 1：配置文件 + 环境变量

"这是最传统的做法。"老潘展示了代码：

```java
// Config.java
public class Config {
    private final Properties props;

    public Config() {
        props = new Properties();
        // 从环境变量读取环境名称（dev/test/prod）
        String env = System.getenv("CAMPUSFLOW_ENV") != null
            ? System.getenv("CAMPUSFLOW_ENV")
            : "dev";  // 默认开发环境

        // 加载对应环境的配置文件
        String configFile = "config-" + env + ".properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + configFile, e);
        }
    }

    public String getDbPath() {
        return props.getProperty("db.path");
    }

    public int getPort() {
        return Integer.parseInt(props.getProperty("server.port"));
    }
}
```

**config-dev.properties**（开发环境）：
```properties
db.path=campusflow.db
server.port=8080
log.level=DEBUG
```

**config-prod.properties**（生产环境）：
```properties
db.path=/var/data/campusflow.db
server.port=80
log.level=WARN
```

"这样你就能通过环境变量切换配置。"老潘说，"启动服务器时设置环境变量："

```bash
# 开发环境
CAMPUSFLOW_ENV=dev java -jar campusflow-1.0.0.jar

# 生产环境
CAMPUSFLOW_ENV=prod java -jar campusflow-1.0.0.jar
```

### 方案 2：12-Factor App 方法论

"更现代的做法是 **12-Factor App**。"老潘说，"核心原则是：**配置通过环境变量注入，不要写配置文件**。这和 Week 09 的 **REST API** 设计呼应——API 端点、CORS 配置都是环境相关的。"

```java
// Config.java（12-Factor 风格）
public class Config {
    public String getDbPath() {
        // 优先读取环境变量，没有则用默认值
        return System.getenv().getOrDefault("DB_PATH", "campusflow.db");
    }

    public int getPort() {
        return Integer.parseInt(
            System.getenv().getOrDefault("SERVER_PORT", "8080")
        );
    }
}
```

```bash
# 启动时注入配置
DB_PATH=/var/data/campusflow.db \
SERVER_PORT=80 \
java -jar campusflow-1.0.0.jar
```

"这样有什么好处？"阿码问。

"配置和代码完全分离。"老潘说，"你不需要修改代码就能适应不同环境。容器化部署（Docker）和云平台都推荐这种方式。"

老潘补充："这和 Week 07 的 **SQLite 持久化存储** 呼应——那时数据库路径是本地绝对路径，现在变成可配置的环境变量。还和 Week 09 的 **REST API** 呼应——API 端点、CORS 配置都是环境相关的。"

### 方案 3：配置库（Spring Cloud Config / Apollo）

"如果项目很大，可以用专门的配置中心。"老潘补充，"比如 Spring Cloud Config、Apollo——它们提供：
- 配置的统一管理
- 配置的版本控制
- 配置的动态更新

但对 CampusFlow 这样的项目，方案 1 或方案 2 就够了。"

小北若有所思："所以我应该用哪种方案？"

"从方案 1 开始。"老潘说，"它简单、够用、容易理解。等项目变大了，再考虑方案 2 或方案 3。"

小北点头："那我得重构 CampusFlow，把硬编码的配置抽出来？"

"对。"老潘说，"同时记住：
- **敏感信息不要写进配置文件**（比如数据库密码、API 密钥）
- **生产环境的配置要加密存储**（用环境变量或密钥管理服务）
- **配置文件不要提交到 Git**（加入 .gitignore）

这些不是技术问题，是安全问题。"

阿码举手："那如果我用 AI 生成配置文件，会不会泄露敏感信息？"

"好问题。"老潘说，"AI 不知道你的生产环境有哪些敏感信息。你得人工审查 AI 生成的配置，确保：
1. 生产环境的密码、密钥用占位符（如 `${DB_PASSWORD}`）
2. 配置文件加入 .gitignore
3. 敏感信息通过环境变量注入

AI 能生成配置模板，但'哪些信息敏感'得靠你判断。"

### 数据备份与恢复策略

"还有一个问题容易被忽略。"老潘说，"你把 CampusFlow 部署上去后，数据库文件存在服务器上。如果服务器崩溃，或者你误删了数据——怎么办？"

小北愣住了——他确实没考虑过这个问题。

"这就是为什么需要**数据备份策略**。"老潘在白板上画了三种备份方案：

```
┌─────────────────────────────────────────────────────────────┐
│                    数据备份策略对比                             │
├─────────────────────────────────────────────────────────────┤
│ 【方案 1：定期导出】（适合小项目）                              │
│   - 每天/每周自动导出数据库文件                                │
│   - 备份到本地或云存储（如 S3）                                │
│   - 成本：低（几个脚本就能实现）                                │
│   - 恢复时间：中（需要手动导入）                                │
│                                                              │
│ 【方案 2：增量备份】（适合中等项目）                            │
│   - 只备份变更的数据（减少存储和传输成本）                      │
│   - 需要专业工具（如 WAL 归档）                                │
│   - 成本：中（需要配置备份系统）                                │
│   - 恢复时间：短（可以快速恢复到任意时间点）                     │
│                                                              │
│ 【方案 3：实时复制】（适合大型项目）                            │
│   - 数据实时同步到备用服务器                                   │
│   - 主服务器崩溃时，备用服务器自动接管                          │
│   - 成本：高（需要多台服务器）                                  │
│   - 恢复时间：秒级（几乎无中断）                                │
└─────────────────────────────────────────────────────────────┘
```

"对 CampusFlow 这样的项目，方案 1 就够了。"老潘展示了备份脚本：

**backup.sh**（自动备份脚本）：
```bash
#!/bin/bash
# CampusFlow 数据备份脚本

# 配置
DB_PATH="/data/campusflow.db"
BACKUP_DIR="/backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/campusflow_$TIMESTAMP.db"

# 创建备份目录
mkdir -p "$BACKUP_DIR"

# 复制数据库文件
cp "$DB_PATH" "$BACKUP_FILE"

# 压缩备份（节省空间）
gzip "$BACKUP_FILE"

echo "Backup created: ${BACKUP_FILE}.gz"

# 清理 30 天前的旧备份
find "$BACKUP_DIR" -name "campusflow_*.db.gz" -mtime +30 -delete
```

**restore.sh**（恢复脚本）：
```bash
#!/bin/bash
# CampusFlow 数据恢复脚本

if [ -z "$1" ]; then
    echo "Usage: ./restore.sh <backup_file.gz>"
    exit 1
fi

BACKUP_FILE="$1"
DB_PATH="/data/campusflow.db"

# 停止服务（避免数据冲突）
# systemctl stop campusflow

# 备份当前数据库（以防万一）
cp "$DB_PATH" "$DB_PATH.before_restore"

# 解压并恢复
gunzip -c "$BACKUP_FILE" > "$DB_PATH"

# 重启服务
# systemctl start campusflow

echo "Database restored from $BACKUP_FILE"
```

"然后你可以设置 cron 定时任务，每天自动备份："老潘补充：

```bash
# 每天凌晨 2 点自动备份
0 2 * * * /path/to/backup.sh
```

"为什么要每天备份？"小北问。

"因为数据丢失的代价是你承担不起的。"老潘说，"想象一下，你部署了一个月，用户创建了上千条任务。某天服务器崩溃，数据库全没了——如果你没有备份，这些数据永远找不回来。用户会立刻流失，你的项目也就完了。"

老潘补充："这和 Week 07 的 **SQLite 持久化存储** 呼应——那时你学的是'怎么把数据存下来'，现在你学的是'怎么保证数据不丢'。备份是生产环境的必修课，不是可选项。"

阿码举手："那云平台（如 Railway）有自己的备份吗？"

"有的云平台提供自动备份，但收费。"老潘说，"而且即使平台有备份，你也应该有自己的备份——'不要把鸡蛋放在同一个篮子里'。平台可能会倒闭、备份可能会失败，自己的备份才是最可靠的。"

小北点头："所以我得给 CampusFlow 配置自动备份脚本？"

"对。"老潘说，"同时记住：
- **备份要定期测试**：确保备份文件能正常恢复
- **备份要异地存储**：不要把备份和数据库放在同一台服务器
- **备份要加密**：如果数据库包含敏感信息，备份文件也要加密

这些不是技术问题，是数据安全问题。"

> **AI 时代小专栏：2026 年的部署趋势——云原生与 AI 辅助部署**
>
> 2026 年的部署领域，正在经历一场"AI + 云原生"的双重变革——但这场变革的内核，依然是"让工程师专注于价值创造，而不是重复劳动"。
>
> 先看云原生的渗透率：根据 CNCF 2025 年的调查，**82%** 的组织在生产环境使用 Kubernetes——这个数字在 2024 年还只有 71%。更有趣的是，66% 的团队用 K8s 跑 AI 推理工作负载——Kubernetes 正成为 AI 的"基础设施骨干"。容器编排市场预计到 2030 年达到 85.3 亿美元，是 2024 年的 5 倍。这解释了为什么 Docker 成为"标准部署格式"——保证"一次构建，到处运行"。
>
> 但另一个趋势更值得注意：**78%** 的工程团队采用混合架构（Serverless + 容器）。为什么？因为不同场景需要不同方案——高流量、稳定负载用容器（成本可控），低流量、突发流量用 Serverless（按需计费）。Railway、Render、Fly.io 这些平台的崛起，正是抓住了这个"混合部署"的需求。
>
> 然后是 AI 的角色。2025 年，**76%** 的 DevOps 团队将 AI 集成到 CI/CD 流水线——不是"替代工程师"，而是"加速决策"。实践数据显示，AI 驱动的工作流自动化可以将远程 DevOps 团队输出提升 40%，事件解决时间减少 30-50%。AI 在部署领域主要做三件事：
>
> 1. **生成配置**：分析代码，自动生成 Dockerfile、Kubernetes YAML、CI 配置
> 2. **检测问题**：扫描"本地能跑、生产崩了"的环境差异（比如你硬编码了路径）
> 3. **辅助决策**：分析日志，建议"这个版本该回滚吗"
>
> 但这里有个关键边界：**AI 能生成配置，但做不了工程决策**。AI 不知道"你的用户主要在哪个地区"（影响 CDN 选择）、"你能接受多长时间停机"（影响回滚策略）、"数据丢失的代价是什么"（影响备份策略）。Gartner 预测到 2026 年，超过 80% 的企业将使用生成式 AI API——但这些 AI 是"辅助"，不是"主导"。
>
> 另一个 2026 年的趋势是 **平台工程**（Platform Engineering）：55% 的组织在 2025 年采用，Gartner 预测 2026 年将达到 80%。平台工程的核心是"给开发者提供自助式部署能力"——你不需要懂 K8s，不需要写 Dockerfile，只需要推送代码，平台自动搞定。GitOps 成为标准实践：Git 仓库是"单一真相源"，部署即代码。
>
> 回到你刚学的环境配置和部署策略：AI 能帮你生成长得专业的 Dockerfile，能检测配置文件里的硬编码路径，但"选择云平台"、"设计部署流程"、"制定回滚策略"——这些决策得靠你。就像老潘说的："AI 能减少重复劳动，但承担不了决策责任。"
>
> 所以你刚学的环境配置管理、部署策略选择，在 AI 时代不是"过时技能"，而是"判断 AI 生成质量"的基础。你得先知道"好的部署长什么样"，才能审查 AI 的输出。
>
> 参考（访问日期：2026-02-12）：
> - [CNCF 2025 云原生调查（82% K8s 生产采用率）](https://www.cncf.io/reports/cncf-survey-2025/)（基于 WebSearch 搜索结果）
> - [AI DevOps 统计（76% CI/CD 集成，事件解决时间减少 30-50%）](https://oneuptime.com/blog/post/2026-01-25-semantic-versioning-automation/view)（基于 WebSearch 搜索结果）
> - [Gartner 预测：2026 年 80% 企业使用生成式 AI](https://www.gartner.com/en/articles/80-of-enterprises-will-use-generative-ai-apis-by-2026)（基于 WebSearch 搜索结果）

## 5. 部署策略——让 CampusFlow 飞向公网

版本号有了、JAR 打包好了、环境配置准备好了——最后一步是"部署"：把 CampusFlow 放到公网上，让室友能通过网址访问。

周三下午，小北研究了半天云服务。

"好多选择..."小北看着列表，"AWS、Azure、阿里云、腾讯云、Railway、Render、Vercel...我该用哪个？"

老潘路过，听到对话："先问自己：你的需求是什么？"

"需求？"小北想了想，"让室友能访问 CampusFlow...就几个人用。"

"那就选最简单的。"老潘说，"不要过度设计。"

老潘介绍了三种部署方案，按复杂度递增：

### 方案 1：云平台一键部署（推荐新手）

"适合小项目、学生项目。"老潘说，"平台如：
- **Railway**（railway.app）：支持 Java，自动检测 Maven 项目
- **Render**（render.com）：免费层支持 Java
- **Fly.io**（fly.io）：部署 Docker 容器

它们的共同特点是：**连接 GitHub 仓库 → 自动构建 → 自动部署**。"

老潘展示了 Railway 的部署流程：

1. **连接 GitHub**：
   ```
   登录 Railway → 点击 "New Project" → 选择 "Deploy from GitHub repo"
   ```

2. **选择仓库**：
   ```
   选择 campusflow 仓库 → Railway 自动检测 Maven 项目
   ```

3. **配置环境变量**：
   ```
   在 Railway 控制台设置：
   - DB_PATH=/data/campusflow.db（Railway 的持久化存储目录）
   - SERVER_PORT=$PORT（使用 Railway 自动分配的 PORT 环境变量）
   ```

   **重要提示**：
   - Railway 提供的 `/data` 目录是持久化存储路径，重启后数据不会丢失
   - 端口必须用 `$PORT` 环境变量，Railway 会自动分配端口
   - 不要写死 8080，否则部署会失败

4. **自动部署**：
   ```
   Railway 自动运行 `mvn package` → 启动服务 → 分配公网 URL
   ```

5. **获取访问地址**：
   ```
   Railway 给你一个 URL：https://campusflow.up.railway.app
   ```

**故障排查**：

小北部署后，发现服务启动失败。老潘过来帮忙看日志："问题在这里——数据库文件找不到。"

```log
Error: Unable to access database file: /data/campusflow.db
```

"为什么？"小北问。

"Railway 的 `/data` 目录需要手动创建卷（volume）来持久化。"老潘解释，"如果不配置卷，每次部署后 `/data` 目录都会被清空，数据就丢了。"

老潘展示了 Railway 的持久化配置：

```toml
# railway.toml（在项目根目录创建）
[build]
builder = "maven"

[deploy]
startCommand = "java -jar target/campusflow-1.0.0.jar"

# 持久化存储配置
[[volumes]]
name = "data"
mount_to = "/data"
```

"这样配置后，Railway 会创建一个持久化卷，数据不会丢失。"老潘说。

"那如果我不配置呢？"阿码问。

"每次部署都是全新的容器，数据全丢。"老潘说，"所以持久化存储是必选项。"

**环境变量处理**：

小北还发现一个问题：代码里写的是 `SERVER_PORT=8080`，但 Railway 要求用 `$PORT`。

"需要在代码里兼容处理。"老潘展示了修复：

```java
public int getPort() {
    // 优先使用环境变量 PORT（云平台），其次用配置文件
    String port = System.getenv("PORT");
    if (port != null) {
        return Integer.parseInt(port);
    }
    return Integer.parseInt(props.getProperty("server.port", "8080"));
}
```

"这样本地开发用配置文件的 8080，部署到 Railway 用环境变量的 PORT。"老潘说。

"就这么简单？"小北惊讶。

"对。"老潘说，"Railway 会自动：
- 检测你的 Maven 项目
- 运行构建命令
- 启动 JAR
- 分配公网域名
- 配置 HTTPS（自动证书）

你只需要推送代码到 GitHub，Railway 自动部署。"

### 部署后的监控与告警

"但部署成功不是结束。"老潘补充，"你还需要考虑：**如果服务崩了，怎么知道？**"

小北愣住了——他又没考虑过这个问题。

"这就是为什么需要**健康检查**和**监控告警**。"老潘在白板上解释：

**健康检查（Health Check）**：

"健康检查是一个简单的 API 端点，用来检测服务是否正常运行。"老潘展示了代码：

```java
// HealthCheck.java
public class HealthCheck {
    private final Config config;

    public HealthCheck(Config config) {
        this.config = config;
    }

    public Map<String, Object> check() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", Instant.now().toString());
        status.put("version", "1.0.0");

        // 检查数据库连接
        try {
            String dbPath = config.getDbPath();
            File dbFile = new File(dbPath);
            status.put("database", dbFile.exists() ? "CONNECTED" : "DISCONNECTED");
        } catch (Exception e) {
            status.put("database", "ERROR: " + e.getMessage());
        }

        return status;
    }
}
```

```java
// 在 Main.java 中注册端点
app.get("/health", ctx -> {
    HealthCheck healthCheck = new HealthCheck(config);
    Map<String, Object> status = healthCheck.check();
    ctx.json(status);
});
```

"访问 `https://campusflow.up.railway.app/health`，你会看到："

```json
{
  "status": "UP",
  "timestamp": "2026-02-12T10:30:00Z",
  "version": "1.0.0",
  "database": "CONNECTED"
}
```

"云平台（如 Railway、Render）会定期调用这个端点。如果返回非 200 状态码，或者 `status` 是 `DOWN`，平台会自动重启服务。"

**监控告警**：

"健康检查是'服务是否活着'，监控告警是'服务是否正常'。"老潘补充，"常见的监控指标包括：

| 指标 | 说明 | 告警阈值 |
|------|------|---------|
| **可用性** | 服务是否响应 | 连续 3 次健康检查失败 |
| **响应时间** | API 响应速度 | 超过 1 秒（P95） |
| **错误率** | 5xx 错误占比 | 超过 5% |
| **CPU 使用率** | 服务器负载 | 超过 80% |
| **内存使用率** | 内存占用 | 超过 85% |

这些指标可以在云平台的控制台查看。Railway 和 Render 都提供内置监控面板。"

老潘展示了 Railway 的监控配置：

```toml
# railway.toml（增加健康检查配置）
[build]
builder = "maven"

[deploy]
startCommand = "java -jar target/campusflow-1.0.0.jar"

# 健康检查配置
[deploy.healthcheck]
path = "/health"
timeout = 30
interval = 10  # 每 10 秒检查一次

# 持久化存储配置
[[volumes]]
name = "data"
mount_to = "/data"
```

"这样 Railway 会每 10 秒调用一次 `/health`。如果连续失败，会自动重启服务。"

**告警通知**：

"如果你想在服务出问题时收到通知，可以配置告警渠道。"老潘补充，"常见方案包括：
- **邮件通知**：Railway/Render 支持配置邮箱
- **Slack/钉钉/企业微信**：通过 Webhook 接收告警
- **短信/电话**：严重故障时使用

对 CampusFlow 这样的项目，邮件通知就够了。"

阿码举手："那如果我用 AI 工具监控日志，能自动发现问题吗？"

"可以。"老潘说，"2026 年有很多 AIOps（AI运维）工具，能分析日志、检测异常、预测故障。但记住：
1. AI 能发现问题，但解决不了
2. AI 的判断可能误报（把正常当成异常）
3. 你还是得理解监控指标的含义

AI 是辅助工具，不是替代品。"

小北点头："所以我得给 CampusFlow 加上 `/health` 端点？"

"对。"老潘说，"健康检查是生产环境的标配。没有它，你不知道服务什么时候崩了。"

### 方案 2：Docker 容器部署（推荐进阶）

"如果你想学更通用的技能，用 Docker。"老潘说，"Docker 保证'一次构建，到处运行'——本地能跑，生产也能跑。"

老潘展示了 Dockerfile 的写法：

```dockerfile
# 多阶段构建（减小镜像大小）
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 运行时镜像（更小）
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/campusflow-1.0.0.jar app.jar
# 暴露端口
EXPOSE 8080
# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar"]
```

"然后推送到容器注册表，再部署到云平台。"老潘展示了命令：

```bash
# 构建 Docker 镜像
docker build -t campusflow:1.0.0 .

# 运行容器（本地测试）
docker run -p 8080:8080 campusflow:1.0.0

# 推送到 Docker Hub（或 GitHub Container Registry）
docker tag campusflow:1.0.0 your-dockerhub/campusflow:1.0.0
docker push your-dockerhub/campusflow:1.0.0

# 部署到云平台（如 Fly.io）
fly launch --image your-dockerhub/campusflow:1.0.0
```

"Docker 的好处是"老潘说，"本地测试的容器和生产运行的容器完全一样——没有环境差异。"

### 方案 3：传统 VPS 部署（不推荐新手）

"你也可以租一台云服务器（AWS EC2、阿里云 ECS），手动部署。"老潘说，"但这需要：
- 配置服务器（安装 Java、配置防火墙）
- 管理进程（用 systemd 或 supervisor）
- 配置反向代理（Nginx）
- 申请 HTTPS 证书（Let's Encrypt）

这三种方案里，它最灵活，但也最复杂。"

小北若有所思："我应该用哪种方案？"

"从方案 1 开始。"老潘说，"它最简单，够你用了。等项目变大了，再考虑 Docker。"

阿码举手问："那会不会有人觉得'一键部署'太简单了，不够专业？"

老潘笑了："专业不是'选最复杂的方案'，而是'选最合适的方案'。如果 Railway 能解决你的问题，为什么要花两周学 Docker？工程的核心是**解决问题，不是展示技能**。"

"哦..."小北恍然大悟，"所以我应该先问'我的需求是什么'，再选方案？"

"对。"老潘说，"这就是工程思维。"

老潘补充："这和 Week 12 的 **Bug Bash** 呼应——Bug Bash 发现的问题在部署前已经修复了吗？上周你们互相测试找出的 bug，如果没修好就部署，用户会立刻流失。部署前要确认：
1. Bug Bash 发现的高优先级 bug 全部修复
2. 测试覆盖率达标（Week 11 的质量门禁）
3. 端到端测试通过（Week 12 的 E2E 测试）

还和 Week 02 的 **ADR** 呼应——架构决策会影响部署策略。例如，如果你选择了 SQLite，部署时要考虑数据库文件存储；如果选择了云数据库，部署时要配置网络连接。"

---

## CampusFlow 进度：发布与部署

经过本周的学习，你理解了"代码写完了"只是完成了 50%，剩下 50% 是"让用户能用上"。现在该把 CampusFlow 从本地开发环境推向公网——让室友能通过网址访问。

### 本周改造计划

1. **版本管理**：
   - 给 CampusFlow 打上 v1.0.0 标签（遵循 Semantic Versioning）
   - 在 README 中添加版本历史（CHANGELOG）
   - 使用 Git tag 绑定版本和代码

2. **构建打包**：
   - 配置 `maven-shade-plugin`，生成可执行 JAR
   - 编写跨平台启动脚本（start.sh / start.bat）
   - 本地测试 JAR 能正常运行

3. **环境配置**：
   - 创建 `config-dev.properties` 和 `config-prod.properties`
   - 从硬编码配置迁移到配置文件
   - 通过环境变量切换环境

4. **部署到云平台**：
   - 选择 Railway / Render / Fly.io
   - 连接 GitHub 仓库，配置自动部署
   - 配置持久化存储（`/data` 目录）
   - 测试公网访问（室友能打开网址使用）

5. **数据备份与监控**：
   - 配置自动备份脚本（backup.sh）
   - 创建 `/health` 健康检查端点
   - 配置云平台监控和告警

**云平台持久化存储**：

不同云平台的持久化存储路径不同：

| 平台 | 持久化目录 | 配置方式 |
|------|-----------|---------|
| Railway | `/data` | 在 `railway.toml` 配置卷 |
| Render | `/opt/render/project/data` | 自动持久化 |
| Fly.io | `/data` | 在 `fly.toml` 配置卷 |

**环境变量最佳实践**：

- **端口**：使用云平台自动分配的 `PORT` 环境变量
- **数据库路径**：使用云平台的持久化目录
- **敏感信息**：使用云平台的环境变量或密钥管理服务（不要写进配置文件）

### Git 标签示例

```bash
# 确保代码已提交
git status
git add .
git commit -m "Release v1.0.0: Initial public release"

# 打标签
git tag -a v1.0.0 -m "Release v1.0.0

Features:
- Task management (create, edit, delete)
- Mark tasks as completed
- RESTful API with OpenAPI documentation
- SQLite persistence

Deployment:
- Packaged as executable JAR
- Deployed to Railway (https://campusflow.up.railway.app)
"

# 推送标签
git push origin main --tags
```

### pom.xml 配置示例（maven-shade-plugin）

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.5.1</version>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>shade</goal>
          </goals>
          <configuration>
            <transformers>
              <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                <mainClass>com.campusflow.Main</mainClass>
              </transformer>
            </transformers>
            <filters>
              <filter>
                <artifact>*:*</artifact>
                <excludes>
                  <exclude>META-INF/*.SF</exclude>
                  <exclude>META-INF/*.DSA</exclude>
                  <exclude>META-INF/*.RSA</exclude>
                </excludes>
              </filter>
            </filters>
          </configuration>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

**常见问题**：

1. **打包时报错 "Invalid signature file"**
   - 原因：不同依赖库包含数字签名文件（用于验证库的真实性），打包时这些签名会冲突
   - 解决：在 `<filters>` 中排除 `META-INF/*.SF`、`META-INF/*.DSA`、`META-INF/*.RSA` 文件
   - 说明：签名文件对于运行不是必需的，排除它们可以避免打包错误

2. **运行时报错 "no main manifest attribute"**
   - 原因：没有配置主类
   - 解决：在 `<transformers>` 中添加 `<mainClass>` 配置
   - 说明：主类必须包含 `public static void main(String[] args)` 方法

3. **JAR 文件过大（> 50MB）**
   - 原因：打包了所有依赖，包括不必要的库
   - 解决：检查 `pom.xml` 的依赖，移除未使用的库
   - 说明：maven-shade-plugin 会打包所有传递依赖

### Config 类示例

```java
package com.campusflow.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties props;
    private final String env;

    public Config() {
        // 从环境变量读取环境名称
        this.env = System.getenv().getOrDefault("CAMPUSFLOW_ENV", "dev");
        this.props = loadConfig();
    }

    private Properties loadConfig() {
        Properties props = new Properties();
        String configFile = "config-" + env + ".properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new RuntimeException("Config file not found: " + configFile);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + configFile, e);
        }
        return props;
    }

    public String getDbPath() {
        // 优先使用环境变量（云平台），其次用配置文件
        String dbPath = System.getenv("DB_PATH");
        if (dbPath != null) {
            return dbPath;
        }
        return props.getProperty("db.path", "campusflow.db");
    }

    public int getPort() {
        // 优先使用环境变量 PORT（云平台），其次用配置文件
        String port = System.getenv("PORT");
        if (port != null) {
            return Integer.parseInt(port);
        }
        return Integer.parseInt(props.getProperty("server.port", "8080"));
    }

    public String getEnv() {
        return env;
    }
}
```

### HealthCheck 端点示例

```java
package com.campusflow.health;

import com.campusflow.config.Config;
import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class HealthCheck {
    private final Config config;

    public HealthCheck(Config config) {
        this.config = config;
    }

    public Map<String, Object> check() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", Instant.now().toString());
        status.put("version", "1.0.0");

        // 检查数据库连接
        try {
            String dbPath = config.getDbPath();
            File dbFile = new File(dbPath);
            status.put("database", dbFile.exists() ? "CONNECTED" : "DISCONNECTED");
        } catch (Exception e) {
            status.put("database", "ERROR: " + e.getMessage());
        }

        return status;
    }
}
```

**在 Main.java 中注册端点**：

```java
import com.campusflow.health.HealthCheck;

// 在 Javalin 配置中添加健康检查端点
HealthCheck healthCheck = new HealthCheck(config);
app.get("/health", ctx -> {
    ctx.json(healthCheck.check());
});
```

### 数据备份脚本示例

**backup.sh**（自动备份脚本）：

```bash
#!/bin/bash
# CampusFlow 数据备份脚本

DB_PATH="${DB_PATH:-/data/campusflow.db}"
BACKUP_DIR="${BACKUP_DIR:-/backups}"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/campusflow_$TIMESTAMP.db"

# 创建备份目录
mkdir -p "$BACKUP_DIR"

# 复制数据库文件
cp "$DB_PATH" "$BACKUP_FILE"

# 压缩备份（节省空间）
gzip "$BACKUP_FILE"

echo "Backup created: ${BACKUP_FILE}.gz"

# 清理 30 天前的旧备份
find "$BACKUP_DIR" -name "campusflow_*.db.gz" -mtime +30 -delete
```

**restore.sh**（恢复脚本）：

```bash
#!/bin/bash
# CampusFlow 数据恢复脚本

if [ -z "$1" ]; then
    echo "Usage: ./restore.sh <backup_file.gz>"
    exit 1
fi

BACKUP_FILE="$1"
DB_PATH="${DB_PATH:-/data/campusflow.db}"

# 备份当前数据库（以防万一）
cp "$DB_PATH" "$DB_PATH.before_restore"

# 解压并恢复
gunzip -c "$BACKUP_FILE" > "$DB_PATH"

echo "Database restored from $BACKUP_FILE"
echo "Previous database saved to $DB_PATH.before_restore"
```

老潘看了这份计划，点头："很好。现在 CampusFlow 是一个'可部署、可访问'的 Web 应用——室友可以直接打开网址使用，你也能在简历上写'已上线'。"

他停顿了一下："但发布不是终点。Week 11 你们学了 **CI 基础**，本周学了发布和部署——完整的 **CI/CD 流水线**应该是：
1. 开发者推送代码到 GitHub
2. CI 自动运行测试（Week 11）
3. 测试通过后，自动构建 JAR（本周）
4. 自动部署到云平台（本周）

这样你只要写代码、推送到 GitHub，剩下的全自动化。"

小北若有所思："所以我还需要学习 CI/CD 流水线设计？"

"那是 Week 15 的内容。"老潘说，"本周先把 CampusFlow 部署上去，让别人能用。下周你将学习发布准备与版本管理——怎么规划发布流程、怎么准备回滚策略、怎么为项目集市展示做准备。"

---

## Git 本周要点

### Git Tag 与版本管理

**Git Tag（标签）**：给重要的 commit 打上标记，通常用于版本发布。

```bash
# 查看所有标签
git tag

# 创建轻量标签（快速标记）
git tag v1.0.0

# 创建附注标签（推荐，包含信息）
git tag -a v1.0.0 -m "Release v1.0.0: Initial release"

# 给过去的 commit 打标签
git tag -a v0.9.0 <commit-hash> -m "Beta release"

# 推送标签到远程仓库
git push origin v1.0.0
git push origin --tags  # 推送所有标签

# 删除标签
git tag -d v1.0.0          # 删除本地标签
git push origin --delete v1.0.0  # 删除远程标签

# 检出某个标签（查看该版本的代码）
git checkout v1.0.0

# 查看标签信息
git show v1.0.0
```

**Commit Message 规范**：
```bash
# 推荐格式：<type>(<scope>): <subject>

feat: add task search feature        # 新功能
fix: resolve task title truncation    # Bug 修复
docs: update API documentation        # 文档变更
refactor: simplify config management  # 重构
test: add integration tests           # 测试
chore: upgrade Maven to 3.9.5         # 构建/工具变更

# 发布版本
release: v1.0.0
```

---

## 本周小结（供下周参考）

本周你让 CampusFlow 从"本地能跑"走向"别人能用"——这是一个里程碑式的转变。

版本管理让你理解了"版本号不是随便标"的道理。Semantic Versioning 用三段数字（MAJOR.MINOR.PATCH）传递变更信息——主版本变更表示不兼容的 API 修改，次版本表示向后兼容的新功能，补丁版本表示 bug 修复。你学会了用 Git tag 绑定版本和代码，确保"v1.0.0 的 bug"能精确对应到某份代码。根据 2026 年的行业数据，约 65% 的开源项目采用 Semantic Versioning。AI 能建议版本号，但"这个变更对用户的影响"得靠你判断。

构建打包让你理解了"用户体验从安装开始"。maven-shade-plugin 把所有依赖打包成一个可执行 JAR，用户只需要 Java 就能运行，不需要 Maven、不需要源代码。你学会了配置主类、排除签名文件、编写跨平台启动脚本。这和 Week 11 的 CI 基础呼应——CI 可以自动构建 JAR、运行测试，确保每次 commit 都能产生可分发的软件包。

环境配置让你理解了"一套代码适应多个环境"的重要性。开发环境、测试环境、生产环境——它们的数据库路径、端口、日志级别都不一样。你学会了用配置文件 + 环境变量管理差异，从硬编码配置迁移到外部配置。这和 Week 07 的持久化存储、Week 09 的 REST API 呼应——数据库路径、API 端点都是环境相关的配置。敏感信息（密码、密钥）不能写进配置文件，要用环境变量或密钥管理服务。

部署策略让你理解了"不要过度设计"的原则。Railway、Render、Fly.io 这些云平台提供了"一键部署"能力——连接 GitHub 仓库 → 自动构建 → 自动部署 → 分配公网 URL。你不需要管理服务器、不需要配置 Nginx、不需要申请 HTTPS 证书。Docker 容器化是更通用的技能，保证"一次构建，到处运行"，但增加了学习成本。老潘的建议是"从最简单的开始"，等需求变复杂了再升级方案。根据 2026 年的数据，约 78% 的工程团队采用混合架构，结合 Serverless 和容器化部署。

**数据持久化与备份**：SQLite 在云平台的部署需要特别注意数据持久化。Railway、Fly.io 等平台提供持久化卷（如 `/data` 目录），但需要手动配置。如果不配置卷，每次部署后数据都会丢失。更重要的，你需要配置自动备份——每天凌晨导出数据库文件到备份目录，压缩后存储 30 天。如果服务器崩溃或数据损坏，可以用备份脚本快速恢复。这和 Week 07 的持久化存储呼应——那时你学的是"怎么把数据存下来"，现在你学的是"怎么保证数据不丢"。

**监控与告警**：部署成功不是结束，你还需要知道"服务是否正常运行"。健康检查端点（`/health`）是一个简单的 API，返回服务的状态、版本、数据库连接等信息。云平台会定期调用这个端点，如果连续失败会自动重启服务。监控指标包括可用性、响应时间、错误率、CPU 和内存使用率。对 CampusFlow 这样的项目，云平台自带的监控面板就够了，告警通知用邮件即可。记住，AI 工具能辅助分析日志，但理解监控指标、判断故障原因——这些得靠你。

CampusFlow 现在是一个"可部署、可访问"的 Web 应用。室友可以直接打开网址使用，你也能在简历上写"已上线"。但发布不是终点，而是新阶段的起点——你需要考虑监控、告警、回滚、灰度发布。Week 11 的 CI 基础和本周的发布部署结合起来，就是完整的 CI/CD 流水线：自动测试 → 自动构建 → 自动部署。

下周，你将学习完整的 CI/CD 流水线设计——怎么把发布和部署自动化、怎么规划发布流程、怎么准备回滚策略、怎么做工程复盘。

---

## Definition of Done（学生自测清单）

- [ ] 我能解释 Semantic Versioning 的规则，知道 MAJOR/MINOR/PATCH 的含义
- [ ] 我能使用 Git tag 给项目打版本标签
- [ ] 我能配置 maven-shade-plugin，生成可执行 JAR
- [ ] 我能设计环境配置管理方案，处理多环境差异
- [ ] 我能选择合适的部署策略（云平台 / Docker / VPS）
- [ ] 我完成了 CampusFlow 的版本管理（v1.0.0 tag）
- [ ] 我完成了 CampusFlow 的打包（可执行 JAR）
- [ ] 我完成了 CampusFlow 的环境配置（config-dev.properties / config-prod.properties）
- [ ] 我完成了 CampusFlow 的部署（公网可访问）
- [ ] 我为 CampusFlow 配置了数据备份策略（自动备份脚本）
- [ ] 我为 CampusFlow 添加了健康检查端点（/health）
- [ ] 我理解了"代码写完了只是 50%，剩下 50% 是让用户能用上"
