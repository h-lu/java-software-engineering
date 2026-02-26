# Create Slides - 纯 Skill + Agent 架构

## 🎯 架构

**无 Python 脚本！纯 Claude Code Skill + Agent 协作。**

```
用户: /create-slides --week week_01 --target-score 90
    │
    ▼
┌─────────────────────────────────────────────┐
│  Claude Code (Orchestrator)                 │
│                                             │
│  Phase 1: 生成初始 HTML                     │
│  - 读取 CHAPTER.md                          │
│  - 提取内容                                 │
│  - 生成 slides.html（占位符）                │
│                                             │
│  Phase 2: 并行调用 5 个审核 Agent           │
│  ├─ Task(subagent="slide-auditor")         │
│  ├─ Task(subagent="pedagogy-reviewer")     │
│  ├─ Task(subagent="content-validator")     │
│  ├─ Task(subagent="character-consistency") │
│  └─ Task(subagent="style-consistency")     │
│                                             │
│  Phase 3: Critic 分析（Claude 自己）         │
│  - 汇总审核结果                             │
│  - 计算综合评分                             │
│  - 生成修复清单                             │
│                                             │
│  Phase 4: 调用 slides-fixer                 │
│  - Task(subagent="slides-fixer")           │
│  - 从 CHAPTER.md 提取内容                   │
│  - 填充占位符                               │
│                                             │
│  循环直到 ≥90 分                            │
└─────────────────────────────────────────────┘
    │
    ▼
  输出: slides.html
```

## 📁 文件结构

```
.claude/
├── agents/                              # 6 个专门 Agent
│   ├── slide-auditor.md                 # 视觉布局审核
│   ├── pedagogy-reviewer.md             # 教学质量审核
│   ├── content-validator.md             # 内容完整性审核
│   ├── character-consistency.md         # 角色一致性审核
│   ├── style-consistency.md             # 样式一致性审核
│   └── slides-fixer.md                  # 内容修复
│
└── skills/
    └── create-slides/
        ├── SKILL.md                     # ⭐ 主要文件（定义工作流程）
        ├── IMPLEMENTATION.md            # 实现细节参考
        └── README.md                    # 本文件
```

**无 Python 脚本！无 skill.py！**

## 🚀 使用方式

### 在 Claude Code 中

```bash
# 生成并自动修复（默认目标 90 分）
/create-slides --week week_01

# 显式指定目标分数
/create-slides --week week_01 --target-score 90

# 批量生成
/create-slides --week week_01,week_02,week_03

# 降低标准快速生成草稿
/create-slides --week week_01 --target-score 75
```

Claude Code 会：
1. 读取 `SKILL.md` 了解工作流程
2. 按 Phase 1-4 执行
3. 使用 `Task` 工具调用各个 Agent
4. 自动循环直到达标

## 🎨 工作流程详情

### Phase 1: 生成初始 HTML

Claude 直接读取 `CHAPTER.md`，提取：
- 周数、标题、引言
- 学习目标（自动 SMART 化：了解→解释，知道→掌握）
- 角色出场规划
- AI 专栏主题

生成初始 `slides.html`（含占位符）

### Phase 2: 并行审核

```python
# Claude Code 内部执行
results = await asyncio.gather(
    Task(subagent_name="slide-auditor", prompt="审核 slides.html..."),
    Task(subagent_name="pedagogy-reviewer", prompt="审核 slides.html..."),
    Task(subagent_name="content-validator", prompt="对比 CHAPTER.md..."),
    Task(subagent_name="character-consistency", prompt="审核角色..."),
    Task(subagent_name="style-consistency", prompt="审核样式...")
)
```

每个 Agent 返回：
```yaml
score: 85
issues:
  - severity: critical
    rule: 文字密度
    message: 第3页文字过多
    location: 第3页-核心概念
    suggestion: 精简至150字以内
summary: 审核摘要
passed: false
```

### Phase 3: Critic 分析

Claude 作为 Critic：
```python
# 计算综合评分
weights = {
    'slide-auditor': 0.20,
    'pedagogy-reviewer': 0.25,
    'content-validator': 0.20,
    'character-consistency': 0.15,
    'style-consistency': 0.20
}

base_score = sum(r.score * weights[r.agent] for r in results)
penalty = blockers * 15 + min(criticals * 5, 20)
final_score = max(0, base_score - penalty)

# 生成修复清单
issues = collect_issues(results)
```

### Phase 4: Fixer 修复

```python
fixed_html = await Task(
    subagent_name="slides-fixer",
    prompt="修复问题...",
    context={
        "slides": slides_html,
        "chapter": chapter_md,
        "issues": issues
    }
)

# 保存修复后的内容
slides_path.write_text(fixed_html)
```

### 循环直到达标

重复 Phase 2-4：
- 直到评分 ≥ target-score（默认 90）
- 或最多 5 轮
- 或连续 2 轮无改进

## 📊 质量门禁

| 评分 | 级别 | 处理 |
|-----|------|------|
| ≥ 90 | 🟢 优秀 | 直接发布 |
| 80-89 | 🟡 良好 | 微调后发布 |
| 60-79 | 🟠 需改进 | 修复后发布 |
| < 60 | 🔴 不合格 | 需人工重写 |

## 📦 输出

```
chapters/week_XX/slides/
├── slides.html              # HTML 幻灯片（浏览器打开）
├── themes/
│   └── modern-education.css # 统一样式
└── FINAL_REPORT.md          # 审核报告
```

## 🖥️ 浏览器打开

```bash
# 方式1: 直接打开
open chapters/week_01/slides/slides.html

# 方式2: 本地服务器
cd chapters/week_01/slides
python3 -m http.server 8080
```

## 🎨 样式系统

**现代玻璃拟态设计**

```css
:root {
  --primary: #0D7377;
  --accent-orange: #FF6B35;
  --glass-bg: rgba(255, 255, 255, 0.7);
  --glass-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}
```

页面类型：
- cover - 深色渐变封面
- agenda - 时间轴议程
- learning-objectives - 勾选列表
- narrative - 角色对话卡片
- concept - 概念卡片
- code - 深色代码页
- ai-column - 紫色 AI 专栏
- exercise - 练习问题框
- summary - 四宫格小结
- end - 深色结尾页

## 📚 Agent 说明

### slide-auditor

审核视觉布局：字体、颜色、文字密度≤150字、代码展示。

### pedagogy-reviewer

审核教学质量：SMART目标、叙事结构、互动设计、节奏控制。

### content-validator

审核内容完整性：对比 CHAPTER.md 检查学习目标、AI专栏、贯穿案例。

### character-consistency

审核角色一致性：出场次数、性格、视觉标识（蓝色小北、橙色阿码、绿色老潘）。

### style-consistency

审核样式一致性：CSS变量使用、页面类型class、玻璃拟态效果。

### slides-fixer

执行修复：从 CHAPTER.md 提取内容、填充占位符、优化文字密度。

---

## ✅ 纯 Skill + Agent，无 Python 脚本！

Claude Code 读取 `SKILL.md`，按定义的工作流程，使用 `Task` 工具调用 Agent 完成全部工作。
