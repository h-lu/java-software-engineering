# Week 10 示例文件说明

本目录包含 Week 10 "AI 生成前端与审查训练" 的所有示例代码。

---

## 文件列表

### 1. `10_ai_prompt_template.md`
**用途**：AI 前端生成 Prompt 工程模板

**内容**：
- Prompt 工程四要素（角色、任务、约束、格式）
- CampusFlow API 完整 Prompt 示例
- Prompt 优化技巧
- 常见 Prompt 陷阱

**使用方式**：阅读参考，复制 Prompt 模板到 AI 工具使用

---

### 2. `10_ai_generated_frontend.html`
**用途**：AI 生成的初始前端代码（包含典型问题）

**故意包含的问题**：
1. XSS 风险：使用 `innerHTML` 直接插入用户输入
2. 没有错误处理：API 调用失败时页面会崩溃
3. 没有加载状态：用户不知道数据正在加载
4. 没有空状态处理：列表为空时页面一片空白
5. 硬编码 API 路径：可能导致跨域问题
6. 幻觉代码：使用了不存在的 `response.getJSON()` 方法

**使用方式**：在浏览器中打开，观察问题，用于审查训练

**警告**：仅供教学演示，不要直接用于生产环境！

---

### 3. `10_reviewed_frontend.html`
**用途**：审查修复后的前端代码（安全、健壮的版本）

**修复的问题**：
1. 使用 `textContent` 替代 `innerHTML`（XSS 防护）
2. 添加 `try-catch` 错误处理
3. 添加加载状态指示器
4. 添加空状态友好提示
5. 完整的 API 路径配置
6. 正确的 Fetch API 使用方法
7. 添加删除确认对话框
8. 表单提交后清空
9. 按钮禁用防止重复提交

**使用方式**：在浏览器中打开（需要配合后端服务）

**运行步骤**：
1. 启动后端：`mvn -q -f ../starter_code/pom.xml compile exec:java`
2. 双击打开 `10_reviewed_frontend.html`
3. 测试所有功能

---

### 4. `10_cors_config.java`
**用途**：Javalin CORS 配置示例

**演示内容**：
1. 开发环境的宽松 CORS 配置（允许任何来源）
2. 生产环境的安全 CORS 配置（限制特定来源）
3. 自定义 CORS 规则（不同端点不同策略）
4. CampusFlow 集成示例

**使用方式**：参考配置方式，集成到自己的项目

**运行方式**：
```bash
mvn -q -f ../starter_code/pom.xml compile exec:java \
  -Dexec.mainClass="examples._10_cors_config"
```

---

### 5. `10_review_checklist.md`
**用途**：AI 代码审查检查清单

**审查维度**：
1. 功能正确性
2. 边界情况（AI 常遗漏）
3. 安全考虑（XSS、CSRF、注入攻击）
4. 可维护性（命名、代码组织）
5. AI 特定问题（幻觉、过时 API）

**使用方式**：打印或打开参考，逐条审查 AI 生成的代码

---

### 6. `10_api_client.js`
**用途**：健壮的 API 调用示例

**演示内容**：
1. `async/await` 模式
2. 错误处理（HTTP 错误、网络错误）
3. 超时处理
4. 返回值处理
5. 重试机制
6. CampusFlow 专用客户端封装

**使用方式**：
- 在浏览器控制台中运行
- 或引入到 HTML 文件中使用

---

## 快速开始

### 1. 学习 Prompt 工程
阅读 `10_ai_prompt_template.md`，理解如何写好 Prompt。

### 2. 观察 AI 代码问题
打开 `10_ai_generated_frontend.html`，使用 `10_review_checklist.md` 找出问题。

### 3. 学习修复方案
对比 `10_reviewed_frontend.html`，了解如何修复问题。

### 4. 运行完整示例
```bash
# 终端 1：启动后端
mvn -q -f ../starter_code/pom.xml compile exec:java

# 终端 2：测试 API
curl http://localhost:7070/health
curl http://localhost:7070/tasks
```

### 5. 在浏览器中打开前端
双击 `10_reviewed_frontend.html`，测试所有功能。

---

## 关键学习点

### Prompt 工程四要素
- **角色**：让 AI 扮演专业工程师
- **任务**：明确、具体地描述需求
- **约束**：列出安全、功能、技术栈限制
- **格式**：指定输出格式（如"完整 HTML 文件"）

### AI 代码常见问题
1. **边界遗漏**：空状态、错误处理、加载状态
2. **安全漏洞**：XSS（innerHTML）、CSRF、注入
3. **幻觉代码**：不存在的方法/类、错误的 API
4. **过时 API**：使用已弃用的方法

### CORS 配置
```java
config.bundledPlugins.enableCors(cors -> {
    cors.addRule(CorsPluginConfig.CorsRule::anyHost);
});
```

### XSS 防护
```javascript
// 危险
element.innerHTML = userInput;

// 安全
const div = document.createElement('div');
div.textContent = userInput;
element.appendChild(div);
```

---

## 参考资料

- [Javalin CORS 文档](https://javalin.io/documentation.html#cors)
- [Fetch API 文档](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)
- [OWASP XSS 防护速查表](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)
