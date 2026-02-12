# Week 10：AI 生成前端与审查训练

> "AI 能帮你写代码，但不能帮你背锅。"
> —— 老潘

2026 年，AI 代码生成已经从"实验性工具"变成"基础设施"。GitHub Copilot 在 2025 年 7 月突破 2000 万用户，生成代码占比达到 46%，90% 的 Fortune 100 企业已采用 AI 编程助手。但与此同时，Veracode 2025 年的研究报告揭示了一个令人警醒的事实：45% 的 AI 生成代码样本未能通过安全测试，Java 语言的安全失败率高达 72%，跨站脚本攻击（XSS）在 86% 的相关代码样本中未被有效防御。

工程界正在形成新的共识——AI 是倍增器，但判断力在人。当 AI 可以几秒钟生成一个完整的前端页面时，工程师的核心价值不再是"写代码的速度"，而是"判断代码质量的能力"。本周是课程的核心训练周。你将学习如何用 AI 生成前端代码，但更重要的是——你将掌握一套系统化的 AI 代码审查方法，学会识别 AI 常见的错误模式。这是 AI 时代工程师的核心竞争力。

---

## 前情提要

上周你完成了 CampusFlow 的 Web 化改造。你的后端 API 已经可以响应 HTTP 请求，支持完整的 CRUD 操作，返回标准的 JSON 格式数据。

但有一个尴尬的现实：你的 API 只能通过 curl 或 Postman 访问。普通用户不会用命令行工具，他们期待的是一个可以在浏览器里点击、输入、查看的界面。

"要不我们手写一个前端？"小北问。

"手写太费时间了，"阿码说，"而且我们主要是学 Java 后端，前端只是辅助。"

"那用 AI 生成？"小北犹豫了一下，"但 AI 生成的前端代码靠谱吗？"

这正是本周要解决的问题——如何让 AI 帮你快速生成前端，同时确保代码质量。答案是一套系统化的审查流程。

---

## 学习目标

完成本周学习后，你将能够：

1. **应用** Prompt 工程技巧，让 AI 生成符合需求的前端代码（Bloom：应用）
2. **分析** AI 生成代码中的常见问题（边界遗漏、安全漏洞、幻觉代码）（Bloom：分析）
3. **评价** 前端代码的质量，使用审查检查清单找出至少 3 类问题（Bloom：评价）
4. **应用** 前后端联调技术，解决 CORS 和 API 调用问题（Bloom：应用）
5. **创造** 改进后的前端实现，修复 AI 代码的缺陷（Bloom：创造）

---

<!--
贯穿案例设计：【任务管理前端——从 AI 生成到人工审查】
- 第 1 节：从"API 没有界面"的痛点出发，引出 AI 生成前端的思路
- 第 2 节：学习 Prompt 工程，用 AI 生成第一个 HTML 页面
- 第 3 节：引入 AI 审查检查清单，系统性地找出 AI 代码的问题
- 第 4 节：学习前后端联调，解决 CORS 和 API 调用
- 第 5 节：修复 AI 代码，完成可用的前端界面
- 第 6 节：CampusFlow 进度——为后端添加 AI 生成+人工审查后的前端
最终成果：一个经过人工审查和修复的、可正常使用的 CampusFlow Web 前端

认知负荷预算检查：
- 本周新概念（AI 时代工程阶段上限 5 个）：
  1. Prompt 工程 (Prompt Engineering)
  2. CORS (Cross-Origin Resource Sharing)
  3. AI 幻觉 (AI Hallucination)
  4. 前端审查检查清单 (Frontend Review Checklist)
  5. Fetch API / XMLHttpRequest
- 结论：✅ 正好 5 个，在预算内

回顾桥设计（至少引用前 3-4 周的 2 个概念）：
- [REST API]（来自 week_09）：在第 1、4 节，前端需要对接上周设计的 API
- [异常处理]（来自 week_03）：在第 4 节，前端需要处理 API 错误响应
- [防御式编程]（来自 week_03）：在第 3 节，审查 AI 代码的边界处理
- [Repository 模式]（来自 week_07）：在第 2 节，Prompt 中需要描述后端数据模型

AI 小专栏规划：
专栏 1（放在第 2 节之后，前段）：
- 主题：AI 生成前端代码的现状与陷阱——2025-2026 年趋势分析
- 连接点：与第 2 节 AI 生成前端代码呼应
- 建议搜索词："AI frontend code generation 2025 2026", "LLM HTML CSS JavaScript accuracy", "GitHub Copilot web development statistics 2025"

专栏 2（放在第 4 节之后，中段）：
- 主题：AI 代码安全漏洞案例分析——XSS、CSRF、注入攻击
- 连接点：与第 4 节前后端联调中的安全问题呼应
- 建议搜索词："AI generated code security vulnerabilities 2025 2026", "LLM XSS injection risks", "AI code security audit statistics 2025"

CampusFlow 本周推进：
- 上周状态：REST API 完成，可通过 curl/Postman 访问，但无用户界面
- 本周改进：使用 AI 生成前端 HTML/CSS/JS，经过人工审查和修复后，实现可用的 Web 界面
- 涉及的本周概念：Prompt 工程、AI 审查检查清单、CORS、Fetch API、错误处理
- 建议示例文件：
  - examples/10_ai_prompt_template.md
  - examples/10_ai_generated_frontend.html（AI 生成的初始版本，含问题）
  - examples/10_reviewed_frontend.html（审查修复后的版本）
  - examples/10_cors_config.java

角色出场规划：
- 小北：在第 1 节面对"没有前端界面"的困惑；在第 3 节发现 AI 代码的边界问题；在第 5 节修复代码后的成就感
- 阿码：在第 2 节尝试用 AI 生成前端，过度信任 AI 输出；在第 3 节被老潘提醒审查的重要性
- 老潘：在第 1 节介绍 AI 生成前端的工程实践；在第 3 节讲解审查检查清单；在第 5 节强调"AI 不能背锅"的工程原则
-->

## 1. 你的 API 需要一个"脸"

小北盯着 Postman 的界面，叹了口气。

"API 功能都正常了，但总不能让用户都用 curl 吧？"

"当然不行，"老潘走过来，"你需要一个前端界面——一个用户可以在浏览器里点击、输入、查看的网页。"

"可我们是 Java 后端课，还要学前端？"

"不用精通，但要知道怎么让前后端配合。"老潘说，"而且现在是 2026 年，有个新选择——让 AI 帮你生成前端代码。"

阿码耳朵竖了起来："AI 能写前端？"

"能，而且写得还不错。HTML、CSS、JavaScript 都能生成，甚至能直接对接你的 REST API。"老潘顿了顿，"但有个前提——你必须会审查 AI 生成的代码。"

"审查？"

"对。AI 生成的代码可能有边界遗漏、安全漏洞、过时 API，甚至'幻觉'——调用不存在的方法。如果你直接复制粘贴上线，出了问题你负责还是 AI 负责？"

小北想了想："我负责。"

"没错。所以本周的核心不是'用 AI 写前端'，而是'学会审查 AI 写的前端'。这是 AI 时代工程师的必修课。"

<!--
**Bloom 层次**：理解
**学习目标**：理解为什么需要前端界面，认识 AI 生成前端的机遇与风险
**贯穿案例推进**：从"API 没有界面"的痛点出发，引出 AI 生成前端的思路
**建议示例文件**：01_api_only.md（描述只有 API 的局限）
**叙事入口**：从"用户不会用 curl"这个真实场景切入
**角色出场**：
- 小北：面对 API 没有界面的困惑，担心需要学前端
- 阿码：对 AI 生成前端表现出兴趣
- 老潘：介绍 AI 生成前端的工程实践，强调审查的必要性
**回顾桥**：
- [REST API]（week_09）：回顾上周完成的 API，引出前端需求
-->

## 2. 如何让 AI 听懂你的话？

"要让 AI 生成好代码，你得先学会跟它说话。"老潘打开一个文本编辑器。

"这不是自然语言吗？我说'给我写个前端'，它不就写了？"

阿码试了一下，把 AI 生成的代码展示给大家：

```html
<!-- AI 生成的初始版本（示意：包含问题） -->
<!DOCTYPE html>
<html>
<head>
    <title>任务管理</title>
</head>
<body>
    <h1>任务列表</h1>
    <div id="tasks"></div>
    <script>
        fetch('/tasks')
            .then(r => r.json())
            .then(data => {
                document.getElementById('tasks').innerHTML = data.map(t =>
                    `<div>${t.title}</div>`
                ).join('');
            });
    </script>
</body>
</html>
```

"能跑吗？"小北问。

"能跑，但问题很多，"老潘指着代码，"没有错误处理、没有加载状态、有 XSS 漏洞、硬编码了 API 路径……"

"那怎么让 AI 生成更好的代码？"

"给更好的 Prompt。"老潘说，"Prompt 工程不是'技巧'，而是'沟通'——你要清楚地告诉 AI：你是谁、你要什么、约束条件是什么、输出格式是什么。"

老潘在白板上写下 Prompt 工程的四要素：

**角色**（Role）：让 AI 扮演特定角色。告诉它"你是一个专业的前端开发工程师"，比"帮我写代码"能得到更专业的输出。

**任务**（Task）：明确描述你要什么。不要说"写个前端"，要说"为任务管理应用生成完整的前端代码，包含列表展示、添加表单、删除功能"。

**约束**（Constraints）：列出限制条件和要求。比如"使用原生 HTML/CSS/JavaScript，不依赖框架"、"处理加载状态和错误情况"、"防止 XSS 攻击"。

**格式**（Format）：指定输出格式。比如"一个完整的 HTML 文件，包含内联 CSS 和 JavaScript，可以直接保存为 .html 文件运行"。

"试试这个 Prompt："

```markdown
你是一个专业的前端开发工程师。请为一个任务管理应用生成完整的前端代码。

后端 API 信息：
- 基础 URL: http://localhost:7070
- GET /tasks - 获取所有任务
- POST /tasks - 创建任务（请求体: {title, description, dueDate}）
- PUT /tasks/{id} - 更新任务
- DELETE /tasks/{id} - 删除任务

要求：
1. 使用原生 HTML/CSS/JavaScript，不依赖框架
2. 包含任务列表展示、添加任务表单、删除按钮
3. 处理加载状态和错误情况
4. 防止 XSS 攻击（不要直接插入 HTML）
5. 使用 Fetch API 调用后端

输出格式：
一个完整的 HTML 文件，包含内联 CSS 和 JavaScript，可以直接保存为 .html 文件运行。
```

阿码把这段 Prompt 输入 AI，生成的代码明显更完整了——有样式、有表单、有错误处理的框架。

"但这就够了吗？"老潘问。

"还不够，"小北说，"我得检查它是不是真的做到了 Prompt 里的所有要求。"

"没错。Prompt 工程只是第一步，审查才是重点。"

<!--
**Bloom 层次**：应用
**学习目标**：掌握 Prompt 工程四要素，能够写出清晰、完整的前端生成 Prompt
**贯穿案例推进**：对比"差 Prompt"和"好 Prompt"的生成结果，学习 Prompt 工程技巧
**建议示例文件**：02_prompt_template.md, 02_ai_generated_good.html
**叙事入口**：从"给 AI 一句话 vs 给 AI 一段结构化 Prompt"的对比切入
**角色出场**：
- 阿码：尝试简单 Prompt，展示 AI 生成的问题代码
- 老潘：讲解 Prompt 工程四要素，提供改进后的 Prompt
- 小北：理解 Prompt 工程的要点，意识到审查的必要性
**回顾桥**：
- [REST API]（week_09）：Prompt 中需要准确描述上周设计的 API 端点
-->

> **AI 时代小专栏：AI 生成前端代码的现状与陷阱**
>
> 2025-2026 年，AI 代码生成能力突飞猛进，但前端领域是个例外。研究显示，GPT-4 在 HTML 生成任务中的成功率仅约 32%，而 Python 代码生成成功率高达 76%。Web 开发（HTML/CSS/JavaScript）是 LLM 表现最弱的领域，平均成功率仅 34.5%。
>
> 为什么前端这么难？因为好的前端需要三重配合：HTML 结构、CSS 样式、JavaScript 逻辑，还要考虑浏览器兼容性、可访问性（Accessibility）、响应式设计。这比单纯的算法实现复杂得多。
>
> 模型之间也有明显差异。DeepSeek R1 优先考虑代码质量而非速度（响应时间 29 秒 vs Gemini 的 7.16 秒），Claude 3.7 Sonnet 在复杂设计模式上准确率更高。Google Angular 团队甚至专门发布了 Web Codegen Scorer 工具来评估 LLM 前端代码质量。
>
> AI 生成前端代码的常见问题包括：使用过时的 API（如用 `var` 而不是 `const`/`let`）、忽略可访问性（没有 `alt` 标签）、设计模式实现错误。这就是为什么"生成-审查-修复"流程在前端领域尤为重要。
>
> 参考（访问日期：2026-02-12）：
> - [Performance Comparison of LLMs in Code Generation](https://forum.effectivealtruism.org/posts/uELFuRsfimKcvvDTK/performance-comparison-of-large-language-models-llms-in-code)
> - [New Open Source Tool from Angular Scores Vibe Code Quality](https://thenewstack.io/new-open-source-tool-from-angular-scores-vibe-code-quality/)

## 3. 审查——AI 时代工程师的核心技能

老潘拿出一份清单："这是 AI 代码审查检查清单。不只是前端，任何 AI 生成的代码都要过一遍。"

```markdown
## AI 代码审查检查清单

### 功能正确性
- [ ] 代码是否实现了需求？
- [ ] 是否有明显的逻辑错误？

### 边界情况（AI 常遗漏）⭐
- [ ] 空输入处理了吗？
- [ ] 空数据状态（列表为空）处理了吗？
- [ ] 网络错误处理了吗？
- [ ] 加载状态处理了吗？

### 安全考虑 ⭐
- [ ] 有 XSS 风险吗？（直接插入 innerHTML）
- [ ] 有 CSRF 风险吗？
- [ ] 敏感信息泄露了吗？

### 可维护性
- [ ] 命名清晰吗？
- [ ] 代码重复了吗？
- [ ] 有必要的注释吗？

### AI 特定问题 ⭐
- [ ] 有"幻觉"（不存在的方法/类）吗？
- [ ] 有过时的 API 使用吗？
- [ ] 有不符合项目规范的地方吗？
```

"现在用这份清单检查刚才 AI 生成的代码。"老潘说。

小北逐条检查：

"边界情况……加载状态有了，但空数据状态没有处理——如果任务列表为空，页面会一片空白。"

"安全……这里用了 innerHTML 插入任务标题，如果标题里有 `<script>` 标签就会执行恶意代码。"

"AI 特定问题……这里调用了 `response.getJSON()`，但 Fetch API 的 Response 对象没有这个方法。"

"什么意思？"小北困惑了。

老潘在白板上写下：

```javascript
// AI 幻觉代码（错误）
const response = await fetch('/tasks');
const data = await response.getJSON();  // ❌ Response 没有 getJSON() 方法

// 正确写法
const response = await fetch('/tasks');
const data = await response.json();    // ✅ Response 对象的方法是 .json()
```

"`.json()` 是 Response 对象的方法，返回一个 Promise，解析结果是 JSON 对象。AI 把它记成了 `getJSON()`，这是典型的'幻觉'——方法看起来很像，但不实际存在。"

"找到了三个问题，"小北说，"那怎么修复？"

"这就是下一节的任务。"老潘说，"但记住这个感觉——审查不是'找茬'，而是'负责'。代码是你合并的，bug 是你背锅。"

阿码在旁边记录："所以 AI 是副驾驶，但机长必须是人。"

"说得好。"

<!--
**Bloom 层次**：分析/评价
**学习目标**：能够使用审查检查清单系统性地找出 AI 代码的问题，至少识别 3 类缺陷
**贯穿案例推进**：使用检查清单审查 AI 生成的前端代码，找出具体问题
**建议示例文件**：03_review_checklist.md, 03_ai_code_with_problems.html, 03_review_notes.md
**叙事入口**：从"怎么知道 AI 代码好不好"这个问题切入，引入检查清单工具
**角色出场**：
- 老潘：提供审查检查清单，讲解审查的重要性
- 小北：实际使用清单审查代码，发现具体问题
- 阿码：总结"AI 是副驾驶，人是机长"的比喻
**回顾桥**：
- [防御式编程]（week_03）：审查 AI 代码的边界处理，与防御式编程理念一致
-->

## 4. 让前后端"说上话"

修复了 AI 代码的问题，小北把前端文件保存为 `campusflow.html`，双击打开。

页面显示出来了，但任务列表是空的。浏览器控制台报错：

```
Access to fetch at 'http://localhost:7070/tasks' from origin 'null'
has been blocked by CORS policy.
```

"CORS？这是什么？"小北懵了。

老潘在白板上画了个图："想象一下，浏览器是个严格的门卫。当你的前端在 `file://` 协议下运行，而 API 在 `http://localhost:7070`，浏览器认为这是两个不同的'域'。"

"为什么两个不同就不能通信？"

"因为同源策略（Same-Origin Policy）。浏览器规定：只有当页面和 API 来自同一个'源'（协议+域名+端口都相同）时，才允许 JavaScript 发起 HTTP 请求。这是为了防止恶意网站窃取其他网站的数据。"

"那我们怎么办？总不能把前端也部署到服务器上吧？"

"有更简单的办法——后端主动告诉浏览器：'我信任这个来源，允许它访问我'。这就是 CORS 头的作用。"

老潘在 Javalin 配置中添加了 CORS 支持：

```java
// 在 Javalin 应用配置中添加
var app = Javalin.create(config -> {
    config.bundledPlugins.enableCors(cors -> {
        cors.addRule(CorsRule::anyHost);  // 开发环境：允许任何来源
        // 生产环境应该限制具体域名：
        // cors.addRule(rule -> rule.allowHost("http://localhost:8080"));
    });
}).start(7070);
```

"还有，"老潘补充，"前端调用 API 时要注意错误处理。这和 Week 03 学的异常处理是一个道理。"

小北恍然大悟："只是从 Java 的 try-catch 变成了 JavaScript 的 try-catch？"

"没错。防御式编程的思想是跨语言的——永远假设外部输入可能出错，永远准备 Plan B。"

健壮的 API 调用应该这样写：

```javascript
async function fetchTasks() {
    try {
        const response = await fetch('http://localhost:7070/tasks');

        if (!response.ok) {
            // 处理 HTTP 错误状态
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const tasks = await response.json();
        return tasks;
    } catch (error) {
        // 处理网络错误或其他异常
        console.error('Failed to fetch tasks:', error);
        showErrorMessage('无法连接到服务器，请检查网络');
        return [];  // 返回空数组避免后续代码崩溃
    }
}
```

现在前端能正常调用 API 了，但小北知道这只是开始。真正的挑战是修复 AI 代码里那些隐藏的问题。

<!--
**Bloom 层次**：应用
**学习目标**：理解 CORS 原理，能够在 Javalin 中配置 CORS，实现健壮的前端 API 调用
**贯穿案例推进**：解决前后端联调中的 CORS 问题，实现完整的 API 调用链路
**建议示例文件**：04_cors_config.java, 04_api_client.js, 04_error_handling.html
**叙事入口**：从"双击 HTML 文件打开却调不通 API"的真实问题切入
**角色出场**：
- 小北：遇到 CORS 错误，困惑不解
- 老潘：解释 CORS 原理，提供 Javalin 配置方案
**回顾桥**：
- [异常处理]（week_03）：前端错误处理与 Java 异常处理理念一致
- [REST API]（week_09）：前端调用的就是上周设计的 API 端点
-->

> **AI 时代小专栏：AI 代码中的安全陷阱**
>
> 2025-2026 年的安全研究揭示了一个令人警醒的事实：AI 生成的代码存在系统性安全风险，且问题比想象中更严重。
>
> Veracode 2025 年的研究报告测试了大量 AI 生成代码样本，发现 **45% 的代码包含已知安全缺陷**。其中 XSS（跨站脚本攻击）漏洞的失败率高达 **86%**——意味着绝大多数 AI 生成的前端代码在面对恶意输入时毫无防御能力。Java 语言的安全失败率更是高达 **72%**，在所有测试语言中风险最高。
>
> Sec-Context 的研究进一步指出：AI 生成的代码包含 XSS 漏洞的可能性是人工代码的 **2.74 倍**。Cloud Security Alliance 的报告发现，**62% 的 AI 生成代码方案包含设计缺陷或已知安全漏洞**，AI 模型会"始终复制危险的安全反模式"。
>
> 最令人担忧的是：**81% 的组织已经将含有漏洞的 AI 代码部署到生产环境**。Gartner 预测，到 2028 年，公民开发者的"prompt-to-app"方法将使软件缺陷增加 **2500%**。
>
> 为什么 AI 代码这么不安全？三个原因：
> 1. **模式重复**：AI 从训练数据中学习，而训练数据里充满了不安全的代码模式（如直接使用 `innerHTML`）
> 2. **功能优先于安全**：AI 优先让代码"能跑"，而不是"安全"
> 3. **缺乏上下文理解**：AI 不知道你的代码运行在什么环境、面对什么威胁
>
> 面对这些数据，一个结论很清楚：安全审查不是可选项，而是必选项。正如老潘所说——"AI 不能背锅。"
>
> 参考（访问日期：2026-02-12）：
> - [Veracode 2025 GenAI Code Security Report](https://www.veracode.com/resources/analyst-reports/2025-genai-code-security-report/)
> - [Cloud Security Alliance - Security Risks in AI-Generated Code](https://cloudsecurityalliance.org/blog/2025/07/09/understanding-security-risks-in-ai-generated-code)
> - [Navigating Vulnerabilities Generated by AI Coding Assistants - Secure Code Warrior](https://www.securecodewarrior.com/article/deep-dive-navigating-vulnerabilities-generated-by-ai-coding-assistants)

## 5. 修复与改进——从"能用"到"好用"

小北对着审查清单，开始一行一行地修复 AI 生成的代码。

第一个问题是 XSS。AI 用 `innerHTML` 直接插入任务标题，这在 Week 03 学的防御式编程里是大忌。

```javascript
// 修复前（AI 生成，有风险）
element.innerHTML = `<div>${task.title}</div>`;

// 修复后（安全）
const div = document.createElement('div');
div.textContent = task.title;  // textContent 自动转义 HTML
element.appendChild(div);
```

"等等，"阿码探头看了一眼，"如果用户输入 `<script>alert('hacked')</script>` 当任务标题，用 `innerHTML` 真的会执行吗？"

"会。"小北说，"我刚试过了。"

阿码倒吸一口凉气。这就是 86% 的 AI 代码都存在的 XSS 漏洞——不是理论风险，是真实可触的攻击面。

第二个问题是空状态。AI 生成的代码在任务列表为空时什么也不显示，用户会困惑"是加载中还是没数据"。

```javascript
// 修复前（AI 生成，空白）
function renderTasks(tasks) {
    container.innerHTML = tasks.map(t => ...).join('');
}

// 修复后（友好提示）
function renderTasks(tasks) {
    if (tasks.length === 0) {
        container.innerHTML = '<p class="empty">暂无任务，点击"添加"创建第一个任务</p>';
        return;
    }
    container.innerHTML = tasks.map(t => ...).join('');
}
```

第三个问题是 AI 的"幻觉"。它调用了一个不存在的方法 `response.getJSON()`，正确的应该是 `response.json()`。

```javascript
// 修复前（AI 幻觉）
const response = await fetch('/tasks');
const data = await response.getJSON();  // 不存在的方法！

// 修复后（正确）
const response = await fetch('http://localhost:7070/tasks');
const data = await response.json();  // 正确方法
```

"AI 怎么会犯这种错？"小北不解。

"它可能在某个训练样本里见过类似写法，或者把 jQuery 的 `$.getJSON()` 记混了。"老潘说，"这就是为什么要运行——不运行永远发现不了。"

修复完这些问题，页面终于可以正常工作了。但老潘摇了摇头。

"这只是'能用'，怎么让它'好用'？"

"什么意思？"

"比如，添加任务后表单还留着原来的内容，用户得手动清空；删除任务一点就删，没有确认；网络慢的时候页面一片空白，没有加载提示……这些是 AI 不会主动考虑的交互细节。"

小北恍然大悟。他继续改进：添加加载动画、清空表单、删除前确认。这些改进让页面从"功能正常"变成了"体验流畅"。

```javascript
// 添加加载指示器
async function loadTasks() {
    showLoading();
    try {
        const tasks = await fetchTasks();
        renderTasks(tasks);
    } finally {
        hideLoading();
    }
}
```

"这就是人工的价值，"老潘说，"不只是修复 AI 的错误，还要补充 AI 想不到的体验优化。AI 能帮你写代码，但不能帮你理解用户。"

<!--
**Bloom 层次**：创造
**学习目标**：能够修复 AI 代码的具体问题，并添加用户体验优化
**贯穿案例推进**：系统性地修复 AI 代码的 4 类问题，添加交互优化
**建议示例文件**：05_fixed_frontend.html, 05_improved_frontend.html
**叙事入口**：从"找到问题"过渡到"修复问题"，展示具体修复代码
**角色出场**：
- 小北：实际修复 AI 代码的问题，获得成就感
- 老潘：引导从"能用"到"好用"的改进方向
**回顾桥**：
- [异常处理]（week_03）：错误处理与 Week 03 的防御式编程一致
-->

## 6. 提交你的审查报告

周三下午，小北盯着屏幕上的代码，陷入了沉思。

"怎么了？"老潘走过来。

"我在想……这份 AI 生成的代码，经过我这么多轮修复，到底算'谁写的'？"

老潘笑了："好问题。这正好引出今天最后一个内容——审查报告。"

他打开一个新的文档："你需要把整个审查过程记录下来：原始 Prompt、AI 生成的代码、你发现的问题、你的修复。"

"这不是在凑字数吧？"

"当然不是。"老潘指着屏幕，"三个月后你再看这份报告，会发现自己当时的疏漏。这就是成长记录。而且——"

他顿了顿："未来的面试官不会问'你会不会用 AI'，而是问'你怎么确保 AI 代码的质量'。这份报告就是你能力的证明。"

阿码在旁边补了一句："我现在明白为什么课程要专门安排一周练这个了。不是'额外工作'，是核心竞争力。"

"没错。"老潘说，"未来的工程师分两种：一种是只会复制粘贴 AI 代码的，一种是能判断 AI 代码好坏的。你想做哪种？"

<!--
**Bloom 层次**：评价/创造
**学习目标**：能够撰写完整的 AI 审查报告，总结 AI 的擅长与不擅长之处
**贯穿案例推进**：完成审查报告，总结本周学习成果
**建议示例文件**：06_review_report_template.md
**叙事入口**：从"记录审查过程"的工程实践角度切入
**角色出场**：
- 老潘：强调审查报告的价值，总结核心能力
- 阿码：总结学习体会，呼应课程目标
**回顾桥**：无（本章收尾）
-->

---

## CampusFlow 进度：为后端穿上"外衣"

经过本周的学习，现在该为 CampusFlow 后端添加前端界面了。

### 本周改造计划

1. **使用 AI 生成前端代码**：
   - 编写清晰的 Prompt，描述 CampusFlow API 和需求
   - 让 AI 生成 HTML/CSS/JavaScript 前端代码

2. **使用审查检查清单评估**：
   - 检查 XSS 风险
   - 检查边界情况处理
   - 检查 API 调用正确性
   - 检查错误处理

3. **修复并改进**：
   - 修复发现的安全和功能性问题
   - 添加加载状态、空状态提示
   - 优化用户体验

4. **配置 CORS**：
   - 在 Javalin 后端启用 CORS 支持
   - 确保前端可以正常调用 API

5. **撰写审查报告**：
   - 记录原始 Prompt 和 AI 输出
   - 列出发现的问题和修复方案
   - 总结经验教训

### 关键代码：Javalin CORS 配置

```java
// CampusFlowApplication.java - 添加 CORS 支持
var app = Javalin.create(config -> {
    config.bundledPlugins.enableCors(cors -> {
        // 开发环境：允许任何来源
        cors.addRule(CorsRule::anyHost);
    });
}).start(7070);
```

### 前端文件结构

```
campusflow-web/
├── index.html          # 主页面（AI 生成 + 人工审查修复）
├── css/
│   └── style.css       # 样式文件
└── js/
    └── app.js          # JavaScript 逻辑
```

### 审查报告模板

请按照以下结构提交你的 AI 审查训练报告：

```markdown
# CampusFlow AI 前端审查报告

## 使用的 AI 工具
- 工具名称：Claude / ChatGPT / Copilot / 其他
- 模型版本：

## Prompt 设计
[粘贴你的完整 Prompt]

## AI 生成代码概览
- 代码行数：
- 功能完整性：

## 审查发现的问题（至少 3 个）
### 问题 1：[类型，如 XSS 安全]
- 位置：
- 描述：
- 风险等级：高/中/低
- 修复方案：

### 问题 2：[类型，如边界情况]
...

## 修复后的改进
- 安全性改进：
- 健壮性改进：
- 用户体验改进：

## 经验总结
- AI 擅长：
- AI 不擅长：
- 我的审查心得：
```

老潘看了这份规划："记住，前端只是 CampusFlow 的'外衣'，真正的业务逻辑在后端。但一件好的外衣能让用户愿意使用你的产品。"

"而且，"他补充道，"这份审查报告是你 AI 协作能力的证明。未来的面试官不会问'你会不会用 AI'，而是问'你怎么确保 AI 代码的质量'。"

---

## Git 本周要点

### 前端代码的 Git 管理

**忽略开发环境文件**：在 `.gitignore` 中添加：
```
# IDE
.idea/
.vscode/
*.iml

# 依赖（如果使用 npm）
node_modules/

# 构建产物
dist/
build/
```

**分离前后端代码**：
- 方案 A：同一仓库，前端代码放在 `frontend/` 目录
- 方案 B：分离仓库，前端单独管理（推荐大型项目）

CampusFlow 使用方案 A：
```
campusflow/
├── src/
│   ├── main/java/      # Java 后端
│   └── test/java/      # 测试
├── frontend/           # 前端代码
│   ├── index.html
│   ├── css/
│   └── js/
└── pom.xml
```

**提交信息规范**：
```
feat(frontend): add AI-generated task management UI

- Generate initial frontend with Claude
- Fix XSS vulnerabilities in task rendering
- Add loading states and error handling
- Configure CORS in backend

AI review report: see AI_REVIEW_REPORT.md
```

---

## 本周小结（供下周参考）

本周你完成了 AI 时代工程师的核心训练——AI 代码审查。

从 Prompt 工程开始，你学会了如何与 AI 有效沟通：明确角色、任务、约束和格式。然后你拿到了 AI 生成的前端代码，但这只是起点。真正的价值在于审查——使用系统化的检查清单，你找出了 XSS 漏洞、边界遗漏、API 幻觉等问题。

修复这些问题的过程中，你不仅掌握了前端安全的基础知识（XSS 防护、CORS 配置），更重要的是建立了"审查者思维"。你开始理解：AI 是强大的工具，但工具的输出必须经过人的判断。这种判断力——知道什么是好的代码、什么是问题代码——是 AI 时代工程师的核心竞争力。

CampusFlow 现在有了一套可用的 Web 界面。虽然它是 AI 生成的，但经过你的审查和修复，它是安全的、健壮的、用户友好的。这比单纯手写代码更有价值，因为它代表了未来的工作方式：人类工程师负责判断和决策，AI 负责生成和实现。

小北第一次用浏览器访问自己的 CampusFlow，看到任务列表正常显示、可以添加删除任务时，那种成就感是真实的。阿码在审查报告中写道："AI 是副驾驶，但机长必须是人。"老潘的提醒——"AI 不能背锅"——会成为你日后使用 AI 的准则。

下周，你将学习质量保障与静态分析——用工具自动发现代码问题，进一步提升代码质量。

---

## Definition of Done（学生自测清单）

- [ ] 我能写出清晰、完整的 Prompt，让 AI 生成符合需求的前端代码
- [ ] 我能使用审查检查清单系统性地评估 AI 代码，找出至少 3 类问题
- [ ] 我能识别并修复 AI 代码中的 XSS 安全漏洞
- [ ] 我能识别并修复 AI 代码中的边界情况遗漏（空状态、错误处理）
- [ ] 我能识别并修复 AI 代码中的"幻觉"（调用不存在的方法/类）
- [ ] 我能在 Javalin 后端配置 CORS，解决前后端联调的跨域问题
- [ ] 我能实现健壮的前端 API 调用，处理网络错误和 HTTP 错误状态
- [ ] 我完成了 CampusFlow 的前端界面，经过审查修复后可以正常使用
- [ ] 我撰写了完整的 AI 审查训练报告，记录了发现的问题和修复方案
