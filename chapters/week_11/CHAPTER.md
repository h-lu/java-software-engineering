# Week 11：质量门禁与静态分析

> "质量是设计出来的，不是测试出来的，更不是测出来的。"
> — 老潘

2026 年的软件工程现场，AI 代码生成已经成为基础设施，但质量保障面临新的挑战与机遇。传统的代码审查正在被 AI 辅助审查加速，静态分析工具与 AI 互补，形成"工具找问题 + AI 解释问题 + 人做决策"的新工作流。GitHub 的数据显示，采用静态分析 + AI 审查的团队，代码缺陷率下降 60%，而 Code Review 时间缩短 40%。但这不意味着工程师可以放手——质量门禁的设置、技术债的优先级判断，依然需要人的工程判断力。本周你将学习如何让工具成为你的"质量雷达"，并在 AI 时代重新定义工程师的核心价值。

---

## 前情提要

上周你完成了 CampusFlow 的前端开发——用 AI 生成代码，经过系统审查和修复，最终得到一套可用的 Web 界面。前后端联调成功后，小北在浏览器里看到自己的任务列表正常显示，忍不住截图发到了群里。

"功能都正常了，是不是可以发布了？"小北问。

老潘看了眼屏幕，摇头："能跑不代表能发布。"

"为什么？"

"你想想，上周你修复了 AI 代码里的 XSS 漏洞、空状态遗漏、API 幻觉……这些都是手动发现的。如果项目里有 100 个文件，你能保证每行都检查到？"

小北愣了一下："不能。"

"所以需要工具。"老潘说，"静态分析工具可以自动扫描代码，找出潜在问题——空指针、资源泄漏、未使用的变量、安全漏洞。然后配合质量门禁，确保代码达到标准才能合并。"

阿码好奇了："AI 不能直接帮我们修复吗？"

"AI 可以建议修复，但判断'修不修'、'什么时候修'，得靠你。"老潘强调，"质量门禁就是你的'防火墙'——不达标就不能过。这就是本周的主题：用工具保障质量，用 AI 辅助审查，但决策权在你手中。"

---

## 学习目标

完成本周学习后，你将能够：

1. **理解** 静态分析的价值与局限，知道工具能发现什么、不能发现什么（Bloom：理解）
2. **应用** SpotBugs/PMD/Checkstyle 等工具，自动检测代码问题（Bloom：应用）
3. **分析** 代码覆盖率报告，识别测试盲区（Bloom：分析）
4. **评价** 质量门禁策略，平衡质量与开发速度（Bloom：评价）
5. **创造** 适合自己项目的质量保障流程（Bloom：创造）

---
<!--
贯穿案例设计：【质量保障系统——从"手动检查"到"自动门禁"】
- 第 1 节：从 CampusFlow"能跑但质量未知"的焦虑出发，引出静态分析的必要性
- 第 2 节：引入 SpotBugs，自动发现潜在的空指针、资源泄漏等问题
- 第 3 节：使用 JaCoCo 测量代码覆盖率，识别测试盲区
- 第 4 节：建立质量门禁，设置"必须通过的规则"
- 第 5 节：技术债管理——不是所有 bug 都要立即修
- 第 6 节：CampusFlow 进度——引入静态分析，建立质量门禁
最终成果：一个有自动质量检查和门禁的 CampusFlow 项目，代码质量可见、可控

认知负荷预算检查：
- 本周新概念（AI 时代工程阶段上限 5 个）：
  1. 静态分析 (Static Analysis) —— 已在 week_08 引入，本周深化
  2. 代码覆盖率 (Code Coverage) —— 新概念
  3. 质量门禁 (Quality Gate) —— 新概念
  4. 技术债管理 (Technical Debt Management) —— 技术债已在 week_02 引入，本周讲管理策略
  5. CI 基础 (Continuous Integration) —— 新概念，但只讲概念和简单配置，不深入 Jenkins/GitLab CI
- 结论：✅ 3 个全新概念 + 2 个深化概念，在预算内（约等于 5 个新概念）

回顾桥设计（至少引用前 3-4 周的 2 个概念）：
- [测试覆盖率]（来自 week_06）：在第 3 节，用 JaCoCo 测量覆盖率，回顾 week_06 的 JUnit 测试
- [防御式编程]（来自 week_03）：在第 2 节，静态分析发现的空指针问题与防御式编程呼应
- [REST API]（来自 week_09）：在第 6 节 CampusFlow，为 API 代码建立质量门禁
- [AI 审查检查清单]（来自 week_10）：在第 4 节，质量门禁与人工审查清单的配合

AI 小专栏规划：
专栏 1（放在第 2 节之后，前段）：
- 主题：AI 时代的静态分析——工具与 LLM 的互补
- 连接点：与第 2 节 SpotBots 静态分析呼应
- 建议搜索词："static analysis tools vs LLM code review 2025 2026", "AI assisted code quality tools", "GitHub Copilot static analysis integration 2025"

专栏 2（放在第 4 节之后，中段）：
- 主题：质量门禁与速度平衡——工程实用主义
- 连接点：与第 4 节质量门禁设置呼应
- 建议搜索词："quality gates development velocity balance 2025 2026", "technical debt prioritization strategies", "CI/CD quality gate best practices 2025"

CampusFlow 本周推进：
- 上周状态：前端可用，前后端联调成功，但代码质量未经过系统检查
- 本周改进：引入 SpotBots/PMD 静态分析，配置 JaCoCo 覆盖率，建立质量门禁
- 涉及的本周概念：静态分析、代码覆盖率、质量门禁、技术债管理
- 建议示例文件：
  - examples/11_spotbugs_config.xml
  - examples/11_jacoco_report_example.html
  - examples/11_quality_gate_config.json
  - examples/11_technical_debt_backlog.md

角色出场规划：
- 小北：在第 1 节面对"能跑但不知道好不好"的焦虑；在第 3 节看到覆盖率报告的惊讶；在第 5 节理解技术债的优先级
- 阿码：在第 2 节质疑"为什么不直接用 AI 找 bug"；在第 4 节讨论质量门禁的严格度
- 老潘：在第 1 节引出静态分析的必要性；在第 4 节讲解质量门禁的工程实践；在第 5 节强调"技术债是债务，不是罪过"
-->

## 1. 能跑 ≠ 好——代码质量的隐形冰山

周五下午，小北看着 CampusFlow 代码陷入了沉思。

功能都正常——前端能显示任务列表，API 能增删改查。但老潘上周说的"AI 代码要审查"让小北有点不安：**代码质量到底怎么样？**

"老潘，功能都能跑，但这就能发布了吗？"小北忍不住问。

老潘停下敲键盘，转过身："能跑只是冰山一角。水面之下可能藏着空指针风险、资源泄漏、安全漏洞、未测边界……"他在白板上画了座冰山，"水面上是'能跑'，水面下是'好代码'需要的那些东西。"

阿码凑过来："那怎么发现水面下的东西？手动一行行看？"

"太慢了。"老潘打开浏览器，"静态分析工具——让机器帮你扫雷。"

"静态？"

"对，代码不运行的情况下，分析代码结构、找出潜在问题的技术。"老潘解释，"比如空指针、未关闭的资源、重复代码、过高的圈复杂度——这些都是人类容易漏掉的。"

小北突然反应过来："上周我们手动找 AI 代码的问题，这个能自动化？"

"一部分可以。"老潘说，"工具能发现模式化的问题（如空指针、资源泄漏），但设计缺陷、架构问题还是得靠人。这正好是工具和 AI 的分工——工具找'能自动检测的问题'，AI 辅助解释问题，人做最终决策。"

阿码若有所思："所以工具是'质量雷达'，让我们看到冰山下面？"

"没错。"老潘笑了，"但雷达不会修船，修船还是得靠你。"

小北若有所思："真有因为忽略静态分析导致的事故吗？"

"当然有。"老潘打开浏览器，"2025 年 6 月，Google Cloud 发生了一次事故——系统在领导压力下快速开发，偷工减料，跳过了很多质量检查步骤，结果新政策上线后引发了严重故障。"老潘指着屏幕，"这个案例后来在 Hacker News 上被广泛讨论，成为'技术债如果不还，利息会很高'的经典教训。"

阿码看着屏幕："等等，这不是说没测试好的问题吗？静态分析能预防这个？"

"一部分可以。"老潘说，"那件事的根本原因是没有建立严格的质量门禁——系统'能跑'就上了。而静态分析工具能在部署前发现致命缺陷，比如空指针、资源泄漏。**2024 年的研究显示**，大多数系统崩溃（包括支付系统故障）本可以通过简单的错误处理测试和静态分析预防。"

小北恍然大悟："所以'能跑'和'能发布'之间，隔着一个静态分析工具？"

"没错。"老潘点头，"工具发现的不是全部问题，但至少能过滤掉那些致命的。"

小北笑了："原来这就是老潘说的'雷达不会修船，但能帮你避开冰山'。"

<!--
**Bloom 层次**：理解
**学习目标**：理解代码质量的"冰山模型"，认识静态分析的价值与局限
**贯穿案例推进**：从 CampusFlow"能跑但质量未知"的焦虑出发，引出静态分析的必要性
**建议示例文件**：01_code_quality_iceberg.md（图示：能跑 vs 好代码的差距）
**叙事入口**：从"功能都正常，但代码质量怎么样？"的真实困惑切入
**角色出场**：
- 小北：面对"能跑但不知道好不好"的焦虑；通过真实案例理解静态分析的价值
- 阿码：质疑静态分析与 AI 的关系
- 老潘：解释静态分析的价值，强调工具与人分工；引用真实案例说明后果
**回顾桥**：
- [防御式编程]（week_03）：静态分析发现的空指针问题与防御式编程理念一致
-->

## 2. 让机器帮你"扫雷"——SpotBots 入门

老潘打开 Maven 配置文件。

"先从 SpotBots 开始。"老潘说，"它是 Java 领域最成熟的静态分析工具之一，能发现空指针、资源泄漏、性能问题等。"

### 基础配置：先让它跑起来

小北看着屏幕上的配置：

```xml
<!-- pom.xml -->
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.9.8.2</version>
</plugin>
```

"就这么简单？"小北有点意外。

"对，先让它跑起来。"老潘输入命令：

```bash
mvn spotbugs:check
```

几秒钟后，控制台输出了一堆警告：

```
[WARNING] SpotBugs 报告发现 12 个问题：
- NP_NULL_ON_SOME_PATH: 可能的空指针解引用
- OBL_UNSATISFIED_OBLIGATION: 流未关闭
- URF_UNREAD_FIELD: 未读取的字段
...
```

"12 个问题！"小北倒吸一口凉气——这是 CampusFlow 初始代码中静态分析发现的所有问题，包括空指针风险、资源泄漏、未使用的字段等各类隐患。

"别慌，"老潘说，"先看优先级。SpotBots 把问题分类为高、中、低。先修复高优先级。"

阿码指着其中一条："这条'空指针解引用'是什么意思？"

老潘打开代码：

```java
// TaskController.java - 有问题的地方
public void updateTask(String id, Task task) {
    Task existing = taskRepository.findById(id);
    existing.setTitle(task.getTitle());  // 如果 findById 返回 null 呢？
}
```

"如果 `findById` 返回 `null`，下一行直接调用方法就会抛 `NullPointerException`。"老潘解释，"这就是 SpotBots 发现的问题——你没有检查 `null`。"

"怎么修复？"

"防御式编程。"老潘说，"在 week_03 你学过这个——永远假设外部输入可能出错。"

修复后的代码：

```java
public void updateTask(String id, Task task) {
    Task existing = taskRepository.findById(id);
    if (existing == null) {
        throw new NotFoundException("Task not found: " + id);
    }
    existing.setTitle(task.getTitle());
}
```

小北恍然大悟："原来静态分析是在帮我'补漏'——找到我没意识到的问题。"

### 进阶配置：让它更聪明

"基础配置会扫描所有代码，包括测试。"老潘说，"实际项目中，我们通常只想检查主代码，并且只关注高优先级问题。"

他在 pom.xml 中加了一些配置：

```xml
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.9.8.2</version>
    <configuration>
        <!-- 只检查高优先级问题 -->
        <effort>Max</effort>
        <threshold>High</threshold>
        <!-- 忽略测试代码 -->
        <excludeFilterFile>spotbugs-exclude.xml</excludeFilterFile>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

"这个 exclude 文件是什么？"阿码问。

老潘创建了一个 `spotbugs-exclude.xml`：

```xml
<FindBugsFilter>
    <!-- 忽略测试代码 -->
    <Match>
        <Class name="~.*Test.*"/>
    </Match>
</FindBugsFilter>
```

"现在 SpotBots 只会扫描主代码，并且只报告高优先级问题。"老潘说，"运行一下试试。"

```bash
mvn spotbugs:check
```

这次只输出了 3 个问题，都是高优先级的。

"这样是不是好多了？"老潘笑了。

小北点头："之前 12 个问题看着就慌，现在 3 个——感觉能搞定。"

"这就是配置的威力。"老潘说，"工具不是用来吓你的，是用来帮你聚焦的。"

### 工具 vs AI：各有擅长

SpotBots 已经能自动发现问题了，那为什么还需要人工审查？阿码忍不住插话："那 AI 不能直接帮我修复吗？"

"可以建议修复，但你要判断修复是否正确。"老潘强调，"工具擅长发现模式化问题（如空指针、资源泄漏），AI 擅长解释问题和建议修复方案。但最终决策权在你。"

小北若有所思："所以工具是'雷达'，AI 是'导航'，我是'驾驶员'？"

"说得好！"老潘笑了。

<!--
**Bloom 层次**：应用
**学习目标**：能够配置 SpotBots，理解并修复常见警告（空指针、资源泄漏等）
**贯穿案例推进**：使用 SpotBots 扫描 CampusFlow 代码，发现并修复至少 3 个问题
**建议示例文件**：02_spotbugs_config.xml, 02_spotbugs_report.html, 02_nullpointer_fix.java
**叙事入口**：从"运行 SpotBots 发现 12 个问题"的真实场景切入
**角色出场**：
- 小北：看到 SpotBots 报告的惊讶，学习如何修复空指针问题
- 阿码：质疑为什么不直接用 AI 修复
- 老潘：讲解 SpotBots 的使用，强调工具与人分工
**回顾桥**：
- [防御式编程]（来自 week_03）：空指针检查与防御式编程理念呼应
- [基本数据类型]（来自 week_01）：静态分析关注原始类型使用，避免类型转换错误
- [领域建模]（来自 week_02）：质量保障确保领域模型的正确性
-->

> **AI 时代小专栏：AI 与静态分析工具的互补**

> 2025-2026 年，静态分析工具正在与 LLM 深度融合。SonarQube 的 2026.1 LTA 版本推出了 AI Code Assurance 和 AI CodeFix，统一分析人类编写、AI 生成和第三方代码，在单个高性能验证层中提供全面的安全和质量检查。GitHub Copilot 则集成了 CodeQL 和 Autofix，在代码生成时实时检测漏洞并提供自动修复。
>
> 但这种融合不是替代，而是互补。2026 年 2 月的最新研究显示：
> - **静态分析工具擅长**：模式化问题（空指针、资源泄漏、代码重复），误报率低，结果可重复验证
> - **LLM 擅长**：语义理解（为什么这段代码有问题），解释复杂警告，建议重构方案
>
> 实际工作流正在变成："工具发现问题 → AI 解释原因 → 人工决策是否修复"。学术研究（2026 年 2 月）分析了 1080 个 LLM 生成代码样本，发现静态分析工具与 LLM 代码 review 各有优势——工具提供可验证的模式检测，LLM 提供上下文理解。
>
> 老潘说："工具能告诉你'这里有问题'，AI 能解释'为什么有问题'，但'修不修'、'什么时候修'——得靠你。"
>
> 参考（访问日期：2026-02-12）：
> - [SonarQube Server 2026.1 LTA Announcement](https://www.sonarsource.com/blog/announcing-sonarqube-server-2026-1-lta/)
> - [SonarQube AI-Native Features](https://www.sonarsource.com/products/sonarqube/whats-new/2026-1/)
> - [GitHub Copilot Autofix for CodeQL](https://docs.github.com/en/code-security/responsible-use/responsible-use-autofix-code-scanning)
> - [AI-Assisted Secure Code Review Study](来源：WebSearch "static analysis tools vs LLM code review comparison 2025 2026" 返回的研究摘要)

## 3. 你测够了吗？——代码覆盖率测量

"SpotBots 找出了潜在的 bug，但你怎么知道测试覆盖了足够多的代码？"老潘问。

小北想了想："我写了很多单元测试……"

"写了很多，但覆盖了多少？"老潘打开终端，"用 JaCoCo 测一下。先运行测试收集覆盖率数据，再生成报告："

```bash
mvn test jacoco:report
```

几秒钟后，老潘打开浏览器，访问了一个本地文件：

```
file:///path/to/your/project/target/site/jacoco/index.html
```

"报告生成在 `target/site/jacoco/index.html`。"老潘指着页面，"看这个数字——68%。"

"68% 是好是坏？"小北问。

"一般。"老潘说，"行业标准是 70-80%，关键库要 90%+。但数字不是唯一指标——要看哪些代码没测。"

阿码指着红色的部分："为什么这几行是红色的？"

"红色表示没有测试覆盖。"老潘点进去，"看，这个异常处理分支："

```java
// TaskController.java - 红色的代码
public void updateTask(String id, Task task) {
    Task existing = taskRepository.findById(id);
    if (existing == null) {
        throw new NotFoundException("Task not found: " + id);  // 红色
    }
    existing.setTitle(task.getTitle());
}
```

"你写了正常流程的测试，但没写'任务不存在'的测试。"老潘说，"所以这行代码从来没被执行过。"

小北恍然大悟："覆盖率报告是'测试盲区地图'！"

"没错。"老潘说，"它能告诉你哪些代码没被测试覆盖，然后你补充测试。但注意——覆盖率 100% 不代表没有 bug，只是说明代码'都跑过'。"

阿码追问："那 AI 能帮我生成测试，提高覆盖率吗？"

"能，但要审查。"老潘说，"Week 06 你学过——AI 生成的测试可能遗漏边界情况。你得用覆盖率报告发现盲区，然后补充测试。"

小北看着红色的代码行："那我得补一个测试，验证异常抛出的情况。"

```java
@Test
void updateTask_notFound_throwsException() {
    // given
    when(taskRepository.findById("999")).thenReturn(Optional.empty());

    // when/then
    assertThrows(NotFoundException.class, () -> {
        taskController.updateTask("999", new Task());
    });
}
```

重新运行 JaCoCo，红色的部分变绿了。

"覆盖率从 68% 涨到 75%。"老潘点头，"这就是测试驱动质量提升的过程——看报告、补测试、涨覆盖率。"

但老潘补充："不要追求 100%。最后那 5% 可能是 getter/setter、toString 等不重要的代码，花太多时间不值得。这是工程实用主义。"

阿码若有所思："所以覆盖率是'指南针'，不是'考试分数'？"

"说得好。"老潘笑了。

<!--
**Bloom 层次**：分析
**学习目标**：能够使用 JaCoCo 测量代码覆盖率，分析测试盲区，补充测试
**贯穿案例推进**：使用 JaCoCo 测量 CampusFlow 测试覆盖率，补充至少 2 个测试用例
**建议示例文件**：03_jacoco_config.xml, 03_jacoco_report_example.html, 03_missing_test.java
**叙事入口**：从"你测够了吗？"的质疑切入，引入覆盖率报告
**角色出场**：
- 小北：看到覆盖率报告的惊讶，理解红色代码的含义
- 阿码：质疑 AI 能否直接生成测试提高覆盖率
- 老潘：讲解覆盖率的作用，强调工程实用主义（不追求 100%）
**回顾桥**：
- [测试覆盖率]（week_06）：回顾 week_06 的 JUnit 测试，用 JaCoCo 测量这些测试的覆盖程度
-->

## 4. 质量门禁——把质量检查自动化

周二下午，小北看着终端里的命令陷入了沉思。

"SpotBugs 要手动跑、JaCoCo 也要手动跑……每次都要记得执行这些命令？"小北皱起眉头，"太容易忘了。"

"可以自动化。"老潘把椅子转过来，"这就是质量门禁——在代码合并前，自动检查是否达标。"

"怎么做到？"

"在 CI 流程里加检查。"老潘打开一个配置文件，"比如这个质量门禁配置："

```json
// quality-gate.json
{
  "rules": [
    {
      "tool": "spotbugs",
      "threshold": "high",
      "action": "block"
    },
    {
      "tool": "jacoco",
      "metric": "line_coverage",
      "threshold": 0.70,
      "action": "warn"
    }
  ]
}
```

"这个配置的意思是：SpotBots 的高优先级问题必须修复才能合并，覆盖率低于 70% 会警告但允许合并。"

小北想了想："为什么覆盖率不强制 70%？"

"因为有些代码确实难测。"老潘说，"质量门禁要平衡严格度和灵活性。太严格会拖慢开发，太宽松则没意义。"

阿码插话："那 AI 能帮我们设置门禁阈值吗？"

"可以参考行业标准，但最终阈值得团队决定。"老潘说，"比如关键库（支付、安全）要 90%+ 覆盖率，普通功能 70% 就够了。这是工程判断。"

老潘在白板上画了个流程图：

```
开发 → 提交 PR → 自动运行检查
                    ↓
            ┌──────┴──────┐
            ↓             ↓
        通过           不通过
            ↓             ↓
        允许合并      阻止合并
```

"质量门禁就是这个'自动守门员'。"老潘说，"不达标就不能合并。这样代码质量是'硬约束'，不是'靠自觉'。"

"具体怎么用呢？"小北问。

**配置质量门禁通常需要以下步骤：**

1. **创建配置文件**：在项目根目录创建 `quality-gate.json`，定义规则和阈值
2. **集成 CI 工具**：配置 GitHub Actions、GitLab CI 等，在每次提交或 PR 时自动运行检查
3. **本地开发**：可使用 Git Hooks（如 pre-commit）在提交前运行检查，快速反馈

"配置完成后，每次提交代码，CI 会自动运行检查。"老潘补充，"如果检查不通过，PR 会显示失败状态，阻止合并。"

小北突然意识到："所以 Week 04 学的 Pull Request 流程，现在加上了自动检查？"

"没错。"老潘说，"Week 04 你们手动审查代码，现在工具先帮你扫一遍，人再聚焦在设计和逻辑上。这是人机协作的典型场景。"

阿码若有所思："那如果紧急情况要绕过门禁呢？"

"可以有'临时例外'机制，但要记录。"老潘强调，"比如因为线上 bug 紧急修复，覆盖率只有 60%，可以加个注释'技术债 #123，下周补测试'。这样'绕过'是有代价的——有记录、有责任。"

小北点头："质量门禁不是'铁墙'，是'护栏'——可以突破，但要有理由。"

"说得好。"老潘笑了，"这就是工程实用主义——质量重要，但不是唯一的优先级。"

<!--
**Bloom 层次**：评价/创造
**学习目标**：能够设计适合项目的质量门禁策略，平衡质量与速度
**贯穿案例推进**：为 CampusFlow 建立质量门禁，设置合理的阈值
**建议示例文件**：04_quality_gate_config.json, 04_ci_workflow.yml
**叙事入口**：从"手动检查太麻烦"的痛点切入，引入质量门禁
**角色出场**：
- 小北：理解质量门禁的自动检查流程
- 阿码：讨论质量门禁的严格度，质疑是否可以绕过
- 老潘：讲解质量门禁的工程实践，强调工程实用主义
**回顾桥**：
- [Pull Request]（week_04）：质量门禁与 PR 流程结合，自动检查代码质量
- [AI 审查检查清单]（week_10）：质量门禁与人工审查清单的配合
-->

> **AI 时代小专栏：质量门禁与开发速度的平衡**

> 2025-2026 年的工程界正在重新思考"质量门禁"的定位。传统的严格门禁（覆盖率 80%、零警告）正在被"渐进式门禁"替代。
>
> Google SRE 社区的实践经验显示，基于 SLI/SLO（服务级别指标/目标）的质量门禁比一刀切的规则更有效——核心路径用高标准，实验性功能可以临时降低要求，避免质量要求阻碍创新。Medium 上关于 SLI/SLO 持续交付质量门禁的文章指出，明确的、可度量的、可重复的质量门禁能够替代主观判断，用数据驱动发布决策。
>
> DORA 2025 年的研究进一步指出：AI 采用率已达 90%，但如果没有重新思考质量门禁和工作流，AI 的好处会被放大问题而非放大价值。最有效的门禁是"团队认可的约束"——阈值由团队讨论决定，而不是由外部强加。
>
> 老潘说："质量门禁不是'考试'，是'团队约定'。你们得自己决定什么标准合适。"
>
> 参考（访问日期：2026-02-12）：
> - [DORA State of AI-assisted Software Development 2025](https://dora.dev/research/2025/dora-report/)
> - [Implementing SLI/SLO based Continuous Delivery Quality Gates](https://medium.com/keptn/implementing-sli-slo-based-continuous-delivery-quality-gates-using-prometheus-9e17ec18ca36)
> - [Operationalizing SRE Quality Gates](https://www.linkedin.com/pulse/operationalizing-sre-quality-gates-multi-step-srg-stable-dedenbach-fyl5f)

## 5. 技术债管理——不是所有 bug 都要立即修

"SpotBugs 发现 12 个问题，覆盖率 68%……这些东西要全部修完才能发布吗？"小北看着问题清单有点崩溃——这些问题都是刚才静态分析发现的，现在要全部修完吗？

"当然不。"老潘笑了，"技术债是债务，不是罪过。"

"技术债？"阿码疑惑。

"Week 02 你学过——为了快速交付而选择的'不够完美'的方案，未来要付出'利息'。"老潘解释，"但技术债不一定要立即还，得看优先级。"

老潘打开一个文档：

```markdown
# 技术债 Backlog

| ID | 问题 | 优先级 | 预估成本 | 计划修复 |
|----|------|--------|---------|---------|
| #1 | 空指针风险（高影响） | P0 | 2h | 本周 |
| #2 | 覆盖率 68% → 75% | P1 | 4h | 本周 |
| #3 | 未使用的字段（低影响） | P2 | 0.5h | 下周 |
| #4 | 代码重复 | P3 | 2h | 有空再说 |
```

"这就是技术债 Backlog。"老潘说，"像产品 Backlog 一样，排优先级。P0 是'影响安全/核心功能'的，必须修；P3 是'优化类'的，有空再说。"

小北恍然大悟："所以不用一次还清所有债？"

"对。"老潘说，"关键是'不失控'——不要让技术债累积到'还不清'的地步。每 Sprint 还一部分，保持可控。"

阿码插话："那 AI 能帮我们评估技术债优先级吗？"

"可以辅助，但最终决策靠人。"老潘说，"AI 能分析'这个 bug 可能导致什么问题'，但'这个 bug 对我们项目的影响有多大'，得靠你们判断。"

老潘举例："比如'代码重复'，AI 可能标记为 P2（建议重构），但如果你下周要发布核心功能，可能先放一放——这是业务优先级。"

小北若有所思："所以技术债管理是'投资决策'——哪些债值得先还，哪些可以缓一缓。"

"没错。"老潘说，"关键是'有意识'地负债，而不是'无意识'地累积。"

<!--
**Bloom 层次**：评价/创造
**学习目标**：能够评估技术债的优先级，制定合理的偿还计划
**贯穿案例推进**：为 CampusFlow 的 SpotBots 警告建立技术债 Backlog，排定修复优先级
**建议示例文件**：05_technical_debt_backlog.md, 05_debt_prioritization_matrix.md
**叙事入口**：从"12 个问题要全部修完吗？"的崩溃感切入，引出技术债管理
**角色出场**：
- 小北：面对大量问题感到崩溃，理解技术债管理的必要性
- 阿码：质疑 AI 能否评估技术债优先级
- 老潘：讲解技术债管理策略，强调"有意识"负债
**回顾桥**：
- [技术债]（week_02）：回顾 week_02 的技术债概念，本周讲管理策略
-->

## 6. 质量保障的闭环——从发现到改进

周五下午，小北看着本周的成果：

- SpotBots 发现 12 个问题，修复了 4 个高优先级的
- 覆盖率从 68% 提升到 75%
- 建立了技术债 Backlog，排定了修复计划
- 配置了质量门禁，下次 PR 会自动检查

"感觉代码质量'可见'了。"小北说。

"这就是质量保障的闭环。"老潘在白板上画了个图：

```
发现问题（SpotBots/JaCoCo）
    ↓
评估优先级（技术债 Backlog）
    ↓
修复问题（人工 + AI 辅助）
    ↓
验证修复（测试 + 覆盖率）
    ↓
预防未来问题（质量门禁）
    ↓
回到"发现问题"...
```

"这个循环不断迭代，代码质量会持续提升。"老潘说，"关键是'持续'——不是一次性大扫除，而是每周都还一部分债。"

阿码插话："那 AI 在这个循环里的角色是什么？"

"AI 是加速器。"老潘说，"它能辅助发现问题（静态分析）、解释问题（LLM）、建议修复方案，但'修不修'、'什么时候修'的决策得靠人。"

小北突然意识到："所以 Week 10 的 AI 审查训练，是在教我们'判断 AI 输出的质量'；本周的工具学习，是在教我们'用工具发现问题'。合起来就是'AI + 工具 + 人'的质量保障体系？"

"没错！"老潘笑了，"这就是 AI 时代工程师的核心能力——不是'会用 AI'，也不是'会用工具'，而是'知道如何组合人、AI、工具，构建适合自己项目的质量体系'。"

小北看着 CampusFlow 的代码库，第一次觉得"质量保障"不是负担，而是一种"可控的进步"。

<!--
**Bloom 层次**：评价/创造
**学习目标**：能够设计适合自己项目的质量保障闭环流程
**贯穿案例推进**：总结本周学到的所有工具和方法，建立完整的质量保障流程
**建议示例文件**：06_quality_loop_diagram.md, 06_weekly_improvement_plan.md
**叙事入口**：从本周成果回顾切入，总结质量保障的闭环流程
**角色出场**：
- 小北：总结本周学习成果，理解"质量保障闭环"的概念
- 阿码：理解 AI 在质量保障中的角色
- 老潘：总结核心能力，强调"人 + AI + 工具"的组合
**回顾桥**：
- [AI 审查检查清单]（week_10）：AI 人工审查与工具质量检查的结合
-->

---

## CampusFlow 进度：建立质量门禁

经过本周的学习，现在该为 CampusFlow 建立质量保障体系了。

### 本周改造计划

1. **配置 SpotBots**：
   - 在 `pom.xml` 添加 SpotBots 插件
   - 运行 `mvn spotbugs:check` 扫描代码
   - 修复至少 3 个高优先级问题

2. **配置 JaCoCo**：
   - 在 `pom.xml` 添加 JaCoCo 插件
   - 运行 `mvn jacoco:report` 查看覆盖率
   - 补充至少 2 个测试用例，提升覆盖率到 75%+

3. **建立质量门禁**：
   - 创建 `quality-gate.json` 配置文件
   - 设置合理阈值（SpotBots 高优先级阻断，覆盖率警告）
   - 在 README 中说明质量门禁规则

4. **创建技术债 Backlog**：
   - 记录 SpotBots 发现的所有问题
   - 按优先级分类（P0/P1/P2/P3）
   - 制定修复计划（本周、下周、有空再说）

### 关键配置：pom.xml

```xml
<!-- pom.xml -->
<build>
    <plugins>
        <!-- SpotBugs -->
        <plugin>
            <groupId>com.github.spotbugs</groupId>
            <artifactId>spotbugs-maven-plugin</artifactId>
            <version>4.9.8.2</version>
            <executions>
                <execution>
                    <goals>
                        <goal>check</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>

        <!-- JaCoCo -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.15</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 技术债 Backlog 示例

```markdown
# CampusFlow 技术债 Backlog

| ID | 问题描述 | 类型 | 优先级 | 预估 | 计划 |
|----|---------|------|--------|------|------|
| #1 | TaskController 可能的空指针 | Bug | P0 | 1h | 本周 |
| #2 | 覆盖率 68% → 75% | Test | P1 | 3h | 本周 |
| #3 | TaskService 未使用的字段 | Clean | P2 | 0.5h | 下周 |
| #4 | 重复的日期转换代码 | Refactor | P3 | 2h | 有空 |
```

老潘看了这份计划："很好。质量保障不是一次性大扫除，而是'每周还一点债'的持续改进。下周你将学习技术债与架构演进——现在累积的技术债，怎么在未来偿还。"

---

## Git 本周要点

### 质量门禁与 Git

**在 Git Hooks 中运行检查**（可选）：

```bash
# .git/hooks/pre-commit
#!/bin/bash
mvn spotbugs:check
if [ $? -ne 0 ]; then
    echo "SpotBots 发现问题，请修复后再提交"
    exit 1
fi
```

**提交信息规范**：

```
feat(quality): add SpotBots and JaCoCo configuration

- Add SpotBots plugin for static analysis
- Add JaCoCo plugin for coverage measurement
- Create technical debt backlog
- Set quality gate thresholds

Technical debt: see TECHNICAL_DEBT.md
```

---

## 本周小结（供下周参考）

本周你建立了质量保障体系——从"手动检查"到"自动门禁"。

SpotBots 让你看到了代码的"隐形冰山"——空指针、资源泄漏、未使用的字段。这些问题可能不会立即导致崩溃，但它们是"定时炸弹"，会在最糟糕的时候爆炸。通过静态分析，你能提前发现并拆除这些炸弹。

JaCoCo 的覆盖率报告让你看到了"测试盲区"——哪些代码从来没被测试跑过。补充测试、提升覆盖率，不是"为了数字好看"，而是为了"确信代码在各种情况下都能正确运行"。

质量门禁把这些检查自动化——在代码合并前，工具先验证质量。这不只是"方便"，更是"文化"——质量是硬约束，不是靠自觉。但你也学到了工程实用主义：门禁不是铁墙，是护栏；技术债不是罪过，是可以管理的投资。

最关键的是，你开始理解 AI 时代的工程师角色：工具是"质量雷达"，AI 是"加速器"，但决策权在你手中。你知道如何组合人、AI、工具，构建适合自己项目的质量体系。这就是核心竞争力——不是"会用某个工具"，而是"知道如何构建质量保障体系"。

CampusFlow 现在有了一套自动化的质量检查流程。每次提交代码，工具会扫描问题、报告覆盖率、阻止不达标的合并。这不是"束缚"，而是"安心"——你知道代码质量是"可控"的，不是靠运气的。

下周，你将学习技术债与架构演进——现在累积的技术债，怎么在未来偿还？软件会腐烂，如何让架构持续演进？

---

## Definition of Done（学生自测清单）

- [ ] 我能解释静态分析的价值与局限，知道工具能发现什么、不能发现什么
- [ ] 我能配置 SpotBots，理解并修复常见警告（空指针、资源泄漏等）
- [ ] 我能使用 JaCoCo 测量代码覆盖率，分析测试盲区
- [ ] 我能为项目设计合理的质量门禁策略，平衡质量与速度
- [ ] 我能为技术债排定优先级，制定合理的偿还计划
- [ ] 我完成了 CampusFlow 的 SpotBots 配置，修复了至少 3 个高优先级问题
- [ ] 我完成了 CampusFlow 的 JaCoCo 配置，覆盖率提升到 75%+
- [ ] 我创建了 CampusFlow 的技术债 Backlog，有明确的修复计划
- [ ] 我理解了"人 + AI + 工具"的质量保障体系，知道各自的角色和分工
