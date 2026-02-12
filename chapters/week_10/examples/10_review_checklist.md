<!--
 * 示例：AI 代码审查检查清单
 * 运行方式：本文件为 Markdown 文档，供阅读和实际审查使用
 * 预期输出：系统化的 AI 代码审查流程，能够发现常见 AI 代码问题
-->

# AI 代码审查检查清单

> "AI 能帮你写代码，但不能帮你背锅。" —— 老潘

这份检查清单用于系统化地审查 AI 生成的代码。无论是前端、后端还是配置文件，都建议按照以下维度进行检查。

---

## 如何使用这份清单

1. **准备阶段**：获取 AI 生成的原始代码，不要做任何修改
2. **逐条检查**：按照清单的五个维度逐一审查
3. **记录问题**：发现的问题记录在表格中，标注风险等级
4. **修复验证**：修复后再次检查，确保问题已解决

---

## 审查维度一：功能正确性

验证代码是否实现了需求，是否有明显的逻辑错误。

### 检查项

- [ ] **需求覆盖**：代码是否实现了 Prompt 中要求的所有功能？
- [ ] **逻辑正确**：业务逻辑是否正确？（如：计算、判断、循环）
- [ ] **API 正确**：调用的 API 端点、方法、参数是否正确？
- [ ] **数据流正确**：数据从输入到输出的流转是否正确？

### 常见 AI 错误

| 错误类型 | 示例 | 后果 |
|---------|------|------|
| 功能遗漏 | 要求"删除确认"，AI 没做 | 用户误操作数据丢失 |
| 逻辑错误 | 逾期判断逻辑写反 | 显示错误的逾期状态 |
| API 错误 | POST 写成 GET | 请求失败，405 错误 |
| 参数错误 | 字段名拼写错误 | 数据无法正确传递 |

### 审查问题记录

| 序号 | 问题描述 | 位置 | 风险等级 | 修复状态 |
|------|---------|------|---------|---------|
| | | | 高/中/低 | |

---

## 审查维度二：边界情况（AI 常遗漏）⭐

AI 擅长"正常路径"，但经常遗漏边界情况。这是审查的重点。

### 检查项

- [ ] **空输入处理**：表单提交时，空值是否被正确处理？
- [ ] **空数据状态**：列表为空时，是否显示友好提示？
- [ ] **网络错误**：请求失败时，是否有错误提示？
- [ ] **超时处理**：长时间无响应时，是否有超时机制？
- [ ] **加载状态**：数据加载中，是否有加载指示器？
- [ ] **大数据量**：数据量很大时，是否有分页或虚拟滚动？
- [ ] **特殊字符**：输入包含特殊字符时，是否能正确处理？

### 常见 AI 遗漏

```javascript
// AI 常见写法（有问题）
async function loadTasks() {
    const response = await fetch('/tasks');
    const data = await response.json();
    renderTasks(data);  // 如果 data 为空数组？如果网络错误？
}

// 审查后修复
async function loadTasks() {
    showLoading();
    try {
        const response = await fetch('/tasks');
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        const data = await response.json();
        if (!data || data.length === 0) {
            showEmptyState();  // 处理空数据
            return;
        }
        renderTasks(data);
    } catch (error) {
        showError(error.message);  // 处理错误
    } finally {
        hideLoading();  // 无论成功与否都隐藏加载
    }
}
```

### 审查问题记录

| 序号 | 边界情况 | 是否处理 | 风险等级 | 修复状态 |
|------|---------|---------|---------|---------|
| 1 | 空输入 | 是/否 | | |
| 2 | 空数据列表 | 是/否 | | |
| 3 | 网络错误 | 是/否 | | |
| 4 | 加载状态 | 是/否 | | |
| 5 | 请求超时 | 是/否 | | |

---

## 审查维度三：安全考虑 ⭐

AI 生成的代码经常有安全漏洞。前端特别要注意 XSS，后端要注意注入攻击。

### 检查项

- [ ] **XSS 防护**：是否使用 `innerHTML` 插入用户输入？
- [ ] **CSRF 防护**：敏感操作是否有 CSRF 防护？
- [ ] **敏感信息**：代码中是否硬编码了密钥、密码？
- [ ] **输入验证**：用户输入是否在客户端和服务器端都验证？
- [ ] **HTTPS**：生产环境是否强制使用 HTTPS？

### XSS 检查重点

```javascript
// ❌ 危险：XSS 漏洞
element.innerHTML = `<div>${userInput}</div>`;

// ✅ 安全：使用 textContent
const div = document.createElement('div');
div.textContent = userInput;
element.appendChild(div);

// ❌ 危险：HTML 字符串拼接
tasks.map(t => `<div>${t.title}</div>`).join('');

// ✅ 安全：DOM API 创建元素
tasks.forEach(t => {
    const div = document.createElement('div');
    div.textContent = t.title;
    container.appendChild(div);
});
```

### 安全审查记录

| 检查项 | 状态 | 问题描述 | 修复方案 |
|--------|------|---------|---------|
| XSS 防护 | ✅/❌ | | |
| CSRF 防护 | ✅/❌ | | |
| 敏感信息 | ✅/❌ | | |
| 输入验证 | ✅/❌ | | |

---

## 审查维度四：可维护性

代码是否易于理解和维护？是否遵循最佳实践？

### 检查项

- [ ] **命名清晰**：变量、函数、类的命名是否清晰表达意图？
- [ ] **代码重复**：是否有重复代码可以提取为函数？
- [ ] **函数长度**：函数是否过长（超过 50 行需要警惕）？
- [ ] **注释质量**：复杂逻辑是否有注释说明？
- [ ] **代码组织**：代码是否按功能合理组织？
- [ ] **魔法数字**：是否有未命名的硬编码数值？

### 命名检查示例

```javascript
// ❌ 不好的命名
function doStuff(a, b) {
    return a.map(x => x.n === b);
}

// ✅ 好的命名
function findTasksByStatus(tasks, status) {
    return tasks.filter(task => task.status === status);
}
```

### 可维护性审查记录

| 检查项 | 状态 | 问题描述 | 改进建议 |
|--------|------|---------|---------|
| 命名清晰 | ✅/❌ | | |
| 代码重复 | ✅/❌ | | |
| 函数长度 | ✅/❌ | | |
| 注释质量 | ✅/❌ | | |

---

## 审查维度五：AI 特定问题 ⭐

AI 有独特的错误模式，需要特别关注。

### 检查项

- [ ] **幻觉代码**：是否调用了不存在的方法/类/属性？
- [ ] **过时 API**：是否使用了已弃用的 API？
- [ ] **混合风格**：是否混合了不同风格的代码（如 jQuery + Fetch）？
- [ ] **假设存在**：是否假设某些库/变量已存在？
- [ ] **示例数据残留**：是否残留了示例/测试数据？

### 常见 AI 幻觉

```javascript
// ❌ 幻觉：response.getJSON() 不存在
const data = await response.getJSON();

// ✅ 正确：使用 .json() 方法
const data = await response.json();

// ❌ 幻觉：假设 $ 存在（jQuery）
$('#tasks').html(content);

// ✅ 正确：使用原生 DOM API
document.getElementById('tasks').innerHTML = content;

// ❌ 幻觉：假设有某个方法
array.remove(item);

// ✅ 正确：使用标准方法
array.splice(array.indexOf(item), 1);
```

### AI 特定问题记录

| 问题类型 | 位置 | 具体问题 | 修复方案 |
|---------|------|---------|---------|
| 幻觉代码 | | | |
| 过时 API | | | |
| 混合风格 | | | |
| 假设存在 | | | |

---

## 快速审查模板

复制以下模板，快速记录审查结果：

```markdown
## AI 代码审查报告

### 基本信息
- 审查日期：
- AI 工具：
- 代码用途：

### 发现的问题

#### 高优先级（必须修复）
1. [类型] 问题描述
   - 位置：
   - 风险：
   - 修复方案：

#### 中优先级（建议修复）
1. [类型] 问题描述
   - 位置：
   - 风险：
   - 修复方案：

#### 低优先级（可选优化）
1. [类型] 问题描述
   - 位置：
   - 风险：
   - 修复方案：

### 审查总结
- 问题总数：
- 高风险问题：
- AI 擅长：
- AI 不擅长：
- 人工价值：
```

---

## 审查实战：对比 AI 生成 vs 人工修复

### 示例 1：XSS 漏洞

**AI 生成（有问题）：**
```javascript
container.innerHTML = tasks.map(t => `
    <div>${t.title}</div>  <!-- XSS 风险！ -->
`).join('');
```

**人工修复（安全）：**
```javascript
tasks.forEach(t => {
    const div = document.createElement('div');
    div.textContent = t.title;  // 安全：自动转义
    container.appendChild(div);
});
```

### 示例 2：幻觉代码

**AI 生成（有问题）：**
```javascript
const response = await fetch('/tasks');
const data = await response.getJSON();  // 不存在的方法！
```

**人工修复（正确）：**
```javascript
const response = await fetch('/tasks');
if (!response.ok) throw new Error('Request failed');
const data = await response.json();  // 正确的方法
```

### 示例 3：边界遗漏

**AI 生成（有问题）：**
```javascript
function renderTasks(tasks) {
    container.innerHTML = tasks.map(...).join('');  // 空数组时显示空白
}
```

**人工修复（完整）：**
```javascript
function renderTasks(tasks) {
    if (!tasks || tasks.length === 0) {
        showEmptyState();  // 显示"暂无数据"提示
        return;
    }
    // 正常渲染...
}
```

---

## 审查后的行动清单

完成审查后，按以下步骤行动：

- [ ] 记录所有发现的问题
- [ ] 按优先级排序（高/中/低）
- [ ] 修复高优先级问题
- [ ] 验证修复是否有效
- [ ] 记录 AI 的擅长和不擅长之处
- [ ] 更新 Prompt 模板，避免重复问题
- [ ] 撰写审查报告，总结经验

---

## 经验法则

1. **永远假设 AI 代码有 bug** —— 不运行不验证
2. **安全问题是红线** —— XSS、注入等必须修复
3. **边界情况是试金石** —— AI 的弱点就是人的价值
4. **记录比修复更重要** —— 积累经验，提升 Prompt 质量
5. **审查是学习能力** —— 比写代码更重要的核心竞争力
