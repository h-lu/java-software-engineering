# Week 10 作业：AI 生成前端与审查训练

> "AI 能帮你写代码，但不能帮你背锅。"
> —— 老潘

---

## 作业概述

本周是课程的核心训练周。你将学习如何让 AI 生成前端代码，但更重要的是——掌握一套系统化的 AI 代码审查方法。

**核心任务**：
1. 使用 Prompt 工程让 AI 生成 CampusFlow 前端代码
2. 使用审查检查清单系统性地评估 AI 输出
3. 修复发现的问题并改进用户体验
4. 配置 CORS 实现前后端联调

---

## 基础作业（必做）

### 任务 1：编写 Prompt 让 AI 生成前端代码（15 分）

**目标**：编写一个清晰、完整的 Prompt，让 AI 生成符合 CampusFlow API 的前端代码。

**要求**：
- Prompt 必须包含四要素：角色、任务、约束、格式
- 准确描述 CampusFlow API 端点（基于 Week 09 实现）
- 明确前端功能需求：任务列表展示、添加任务、删除任务、标记完成

**CampusFlow API 参考**：
```
基础 URL: http://localhost:7070

GET    /tasks               - 获取所有任务（返回 {data: [...], total: N}）
GET    /tasks/{id}          - 获取指定任务
POST   /tasks               - 创建任务（请求体: {title, description, dueDate}）
PUT    /tasks/{id}          - 全量更新任务
PATCH  /tasks/{id}          - 部分更新任务（如只更新状态字段）
DELETE /tasks/{id}          - 删除任务
GET    /stats               - 获取统计信息
```

> **注意**：标记任务完成可以用两种方式：
> - `PATCH /tasks/{id}` + 请求体 `{"status": "completed"}`
> - 或使用专门的 `POST /tasks/{id}/complete` 端点（后端已实现）
> 两种方式都有效，选择一种即可。

**Task 模型结构**：
```json
{
  "id": "uuid-string",
  "title": "任务标题",
  "description": "任务描述",
  "dueDate": "2026-02-20",
  "completed": false,
  "overdueDays": 0,
  "createdAt": "2026-02-12T10:30:00"
}
```

**提交物**：
- `PROMPT.md`：你编写的完整 Prompt

**评分要点**：
- 角色定义清晰（3 分）
- 任务描述完整（4 分）
- 约束条件明确（4 分）
- 格式要求具体（4 分）

---

### 任务 2：保存 AI 生成的初始代码（5 分）

**目标**：将 AI 生成的原始代码保存下来，作为审查的基准。

**要求**：
- 不要修改 AI 生成的代码（即使有 obvious 问题）
- 保存为 `frontend/ai_generated.html`
- 记录使用的 AI 工具名称和模型版本

**提交物**：
- `frontend/ai_generated.html`：AI 生成的原始前端代码
- `AI_TOOL.md`：使用的 AI 工具信息

---

### 任务 3：使用检查清单审查 AI 代码（25 分）

**目标**：使用审查检查清单系统性地评估 AI 代码，找出至少 3 类问题。

**审查检查清单**：

```markdown
## AI 代码审查检查清单

### 功能正确性
- [ ] 代码是否实现了需求？
- [ ] 是否有明显的逻辑错误？

### 边界情况（AI 常遗漏）
- [ ] 空输入处理了吗？
- [ ] 空数据状态（列表为空）处理了吗？
- [ ] 网络错误处理了吗？
- [ ] 加载状态处理了吗？

### 安全考虑
- [ ] 有 XSS 风险吗？（直接插入 innerHTML）
- [ ] 有 CSRF 风险吗？
- [ ] 敏感信息泄露了吗？

### 可维护性
- [ ] 命名清晰吗？
- [ ] 代码重复了吗？
- [ ] 有必要的注释吗？

### AI 特定问题
- [ ] 有"幻觉"（不存在的方法/类）吗？
- [ ] 有过时的 API 使用吗？
- [ ] 有不符合项目规范的地方吗？
```

**常见问题类型参考**：

1. **安全问题（XSS）**：
   ```javascript
   // 问题代码：直接使用 innerHTML 插入用户输入
   element.innerHTML = `<div>${task.title}</div>`;

   // 攻击示例：用户输入 <script>alert('hacked')</script> 作为标题
   ```

2. **边界情况遗漏**：
   - 任务列表为空时页面空白
   - 网络错误时没有任何提示
   - 表单提交时没有验证空输入

3. **API 错误/幻觉**：
   - 调用不存在的方法如 `response.getJSON()`
   - 错误的 API 路径（如 `/api/tasks` 而不是 `/tasks`）
   - 未处理 HTTP 错误状态

4. **健壮性问题**：
   - 没有加载状态指示
   - 没有错误边界处理
   - 硬编码配置（如 API URL）

**提交物**：
- `REVIEW.md`：审查报告，包含：
  - 检查清单的勾选结果
  - 发现的至少 3 类问题，每类包含：
    - 问题类型（安全/边界/API 错误/健壮性）
    - 问题位置（代码行号或函数名）
    - 问题描述
    - 风险等级（高/中/低）

**评分要点**：
- 使用了完整的检查清单（5 分）
- 发现至少 3 类不同问题（15 分）
- 问题描述准确、有证据（5 分）

---

### 任务 4：修复与改进 AI 代码（25 分）

**目标**：修复审查发现的问题，并添加用户体验优化。

**必须修复的问题**：

1. **XSS 安全防护**：
   ```javascript
   // 修复前（有风险）
   element.innerHTML = `<div>${task.title}</div>`;

   // 修复后（安全）
   const div = document.createElement('div');
   div.textContent = task.title;  // textContent 自动转义 HTML
   element.appendChild(div);
   ```

2. **空状态处理**：
   ```javascript
   // 修复前：列表为空时页面空白
   function renderTasks(tasks) {
       container.innerHTML = tasks.map(t => ...).join('');
   }

   // 修复后：友好的空状态提示
   function renderTasks(tasks) {
       if (tasks.length === 0) {
           container.innerHTML = '<p class="empty">暂无任务，点击"添加"创建第一个任务</p>';
           return;
       }
       // ... 正常渲染
   }
   ```

3. **错误处理**：
   ```javascript
   // 健壮的 API 调用
   async function fetchTasks() {
       try {
           const response = await fetch('http://localhost:7070/tasks');
           if (!response.ok) {
               throw new Error(`HTTP error! status: ${response.status}`);
           }
           return await response.json();
       } catch (error) {
           console.error('Failed to fetch tasks:', error);
           showErrorMessage('无法连接到服务器，请检查网络');
           return { data: [] };
       }
   }
   ```

**用户体验优化（至少 2 项）**：
- 添加加载指示器（Loading spinner）
- 添加操作成功提示（Toast 通知）
- 删除前确认对话框
- 表单提交后清空输入
- 任务完成状态视觉反馈

**提交物**：
- `frontend/index.html`：修复后的前端代码
- `REVIEW.md` 中补充修复说明

**评分要点**：
- XSS 漏洞已修复（8 分）
- 边界情况已处理（8 分）
- 错误处理已完善（5 分）
- 用户体验有优化（4 分）

---

### 任务 5：配置 CORS（15 分）

**目标**：在 Javalin 后端启用 CORS，确保前端可以正常调用 API。

**背景知识**：
当你直接用浏览器打开 HTML 文件（`file://` 协议）时，调用 `http://localhost:7070` 的 API 会被浏览器阻止（CORS 策略）。需要在后端明确允许跨域请求。

**实现要求**：

1. 修改 `App.java`，添加 CORS 配置：
   ```java
   import io.javalin.plugin.bundled.CorsPlugin;

   var app = Javalin.create(config -> {
       config.bundledPlugins.enableCors(cors -> {
           // 开发环境：允许任何来源
           cors.addRule(CorsRule::anyHost);

           // 生产环境应该限制具体域名：
           // cors.addRule(rule -> rule.allowHost("http://localhost:8080"));
       });
   }).start(7070);
   ```

2. 确保前端代码使用完整的 API URL：
   ```javascript
   // 不要省略协议和主机
   const API_BASE = 'http://localhost:7070';
   fetch(`${API_BASE}/tasks`)
   ```

**验证方法**：
1. 启动后端：`mvn -q compile exec:java`
2. 用浏览器打开 `frontend/index.html`
3. 打开浏览器开发者工具（F12）
4. 确认：
   - 任务列表正常加载
   - 控制台没有 CORS 错误
   - Network 标签能看到成功的 API 请求

**提交物**：
- `src/main/java/com/campusflow/App.java`：添加了 CORS 配置的后端代码
- `cors_screenshot.png`：浏览器控制台截图，证明前后端通信正常

**评分要点**：
- CORS 配置正确（8 分）
- 前后端能正常通信（7 分）

---

## 进阶作业（选做，+20 分）

### 进阶 1：添加更多功能（+8 分）

实现以下功能中的至少 2 个：
- **任务筛选**：按完成状态筛选（全部/未完成/已完成）
- **任务搜索**：按标题关键词搜索
- **任务排序**：按截止日期排序
- **逾期提醒**：视觉标识逾期任务
- **统计面板**：显示总任务数、完成率等

### 进阶 2：改进 UI 设计（+6 分）

- 使用 CSS 框架（如 Tailwind CDN）美化界面
- 添加响应式设计（适配移动端）
- 添加动画过渡效果

### 进阶 3：添加前端测试（+6 分）

- 使用 Jest 或 Vitest 编写至少 3 个单元测试
- 测试覆盖：任务渲染、表单验证、API 调用

---

## AI 协作练习（可选但推荐）

**目标**：练习与 AI 协作生成和审查代码，建立"AI 是副驾驶，人是机长"的工作模式。

**步骤**：

1. **使用 AI 生成前端代码**
   - 使用你编写的 Prompt 让 AI 生成代码
   - 保存 AI 的原始输出（不要修改）

2. **使用审查检查清单评估代码**
   - 逐条检查功能正确性、边界情况、安全问题、可维护性
   - 特别关注 AI 常犯的错误：XSS、空状态、幻觉 API

3. **记录发现的问题**
   - 至少找出 3 类问题
   - 对每类问题给出具体代码位置和修复建议

4. **修复问题并解释修复思路**
   - 不要只是修改代码，要解释为什么这样改
   - 思考：AI 为什么会犯这个错误？如何避免？

**审查清单**：
- [ ] 功能正确性：代码是否实现了需求？
- [ ] 边界情况：空输入、空列表、网络错误处理了吗？
- [ ] 安全问题：有 XSS 风险吗？敏感信息泄露了吗？
- [ ] 可维护性：命名清晰吗？有重复代码吗？
- [ ] AI 特定问题：有幻觉代码吗？有过时 API 吗？

**提交要求**：
- AI 生成的原始代码
- 审查报告（列出发现的问题）
- 修复后的代码
- 经验总结（AI 擅长/不擅长什么，你的审查心得）

---

## 提交物清单

### 必交文件
- [ ] `PROMPT.md`：你编写的完整 Prompt
- [ ] `frontend/ai_generated.html`：AI 生成的原始前端代码
- [ ] `frontend/index.html`：修复后的前端代码
- [ ] `REVIEW.md`：AI 审查报告
- [ ] `AI_TOOL.md`：使用的 AI 工具信息
- [ ] `src/main/java/com/campusflow/App.java`：添加了 CORS 配置的后端代码
- [ ] `cors_screenshot.png`：前后端通信正常的截图

### 进阶作业文件（如完成）
- [ ] `frontend/advanced.html`：包含进阶功能的版本
- [ ] `frontend/__tests__/app.test.js`：前端测试文件

### 目录结构参考
```
week_10_submission/
├── PROMPT.md
├── REVIEW.md
├── AI_TOOL.md
├── cors_screenshot.png
├── frontend/
│   ├── ai_generated.html    # AI 原始输出
│   └── index.html           # 修复后版本
└── src/
    └── main/
        └── java/
            └── com/
                └── campusflow/
                    └── App.java   # 添加 CORS 配置
```

---

## 作业截止时间

- **基础作业**：本周日 23:59
- **进阶作业**：下周三 23:59

---

## 常见问题

### Q1: AI 生成的前端代码运行不了怎么办？

首先检查浏览器控制台（F12）的错误信息：
- **CORS 错误**：参考任务 5 配置后端 CORS
- **404 错误**：检查 API URL 是否正确（应该是 `http://localhost:7070/tasks`）
- **语法错误**：可能是 AI 生成了不兼容的 JavaScript 语法，尝试修复或重新生成

### Q2: 怎么判断有没有 XSS 漏洞？

测试方法：
1. 在添加任务时，输入 `<script>alert('xss')</script>` 作为标题
2. 如果弹出了 alert 框，说明有 XSS 漏洞
3. 修复后应该显示纯文本 `<script>alert('xss')</script>`，而不会执行

### Q3: 审查报告要写多长？

没有字数要求，但要包含：
- 检查清单的勾选状态（可以用表格或列表）
- 至少 3 类问题的详细描述（每类 3-5 句话）
- 修复说明（简述如何修复）

### Q4: 可以用 AI 辅助写审查报告吗？

可以，但注意：
- 报告内容必须基于你实际发现的问题
- 不要编造不存在的问题
- 最终判断和修复方案必须是你自己的

---

## 参考资源

- 如果你遇到困难，可以参考 `starter_code/src/main/java/com/campusflow/App.java` 中的 CORS 配置示例
- Week 09 的 API 文档在 `chapters/week_09/CHAPTER.md` 中
- AI 审查检查清单模板：`shared/ai_progression.md`

---

## 学习建议

1. **Prompt 工程是关键**：好的 Prompt 能让 AI 生成更好的代码，花时间优化你的 Prompt
2. **审查比生成更重要**：AI 生成代码只是开始，审查和修复才是价值所在
3. **记录你的发现**：审查报告不只是作业，更是你 AI 协作能力的证明
4. **测试边界情况**：不要只测"正常流程"，要故意输入奇怪的数据看代码如何处理

祝作业顺利！记住老潘的话："AI 不能背锅。"
