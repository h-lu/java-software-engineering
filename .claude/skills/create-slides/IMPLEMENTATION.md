# Create Slides Implementation

## 架构

```
┌─────────────────────────────────────────────────────────────┐
│  User: /create-slides --week week_01  [--target-score 90]   │
└─────────────────────┬───────────────────────────────────────┘
                      │
                      ▼
┌─────────────────────────────────────────────────────────────┐
│  create_slides.py (Orchestrator)                            │
│  - 解析参数                                                 │
│  - 协调 Agent 调用                                          │
│  - 管理 Critic-Fixer 循环                                   │
└─────────────────────┬───────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        ▼             ▼             ▼
   ┌─────────┐  ┌──────────┐  ┌──────────┐
   │ Extract │  │ Parallel │  │ Critic   │
   │ Content │  │ Audit    │  │ - Fixer  │
   └─────────┘  └──────────┘  └──────────┘
                      │
        ┌─────────────┼─────────────┐
        ▼             ▼             ▼
   ┌─────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐
   │ slide   │  │ pedagogy │  │ content  │  │ character│
   │ auditor │  │ reviewer │  │ validator│  │ consistency
   └─────────┘  └──────────┘  └──────────┘  └──────────┘
                      │
                      ▼
                ┌──────────┐
                │slides-   │
                │fixer     │
                └──────────┘
```

## 实现步骤

### Step 1: 内容提取

```python
# 直接读取 CHAPTER.md，提取关键内容
structure = extract_structure(chapter_path)
# 生成初始 slides.md（基于课件内容精炼）
generate_slides(structure, slides_path)
```

### Step 2: 并行审核

```python
# 使用 Task 工具并行调用 4 个审核 Agent
audit_tasks = [
    Task(
        subagent_name="slide-auditor",
        prompt=f"审核 {slides_path} 的视觉布局",
        context={"slides_path": str(slides_path)}
    ),
    Task(
        subagent_name="pedagogy-reviewer",
        prompt=f"审核 {slides_path} 的教学质量",
        context={"slides_path": str(slides_path)}
    ),
    Task(
        subagent_name="content-validator",
        prompt=f"对比 {slides_path} 和 {chapter_path} 的内容完整性",
        context={
            "slides_path": str(slides_path),
            "chapter_path": str(chapter_path)
        }
    ),
    Task(
        subagent_name="character-consistency",
        prompt=f"审核 {slides_path} 的角色一致性",
        context={
            "slides_path": str(slides_path),
            "characters_path": "shared/characters.yml"
        }
    )
]

results = await asyncio.gather(*audit_tasks)
```

### Step 3: Critic 分析

```python
# 汇总审核结果，生成修复清单
issues = collect_issues(results)
critical_issues = [i for i in issues if i.severity == "critical"]

if len(critical_issues) <= 1:  # 批准标准
    approved = True
else:
    approved = False
```

### Step 4: Fixer 修复

```python
# 调用 slides-fixer Agent
fixed_content = await Task(
    subagent_name="slides-fixer",
    prompt="根据审核报告修复 PPT",
    context={
        "slides_path": str(slides_path),
        "chapter_path": str(chapter_path),
        "issues": issues
    }
)
```

### Step 5: 循环直到达标

```python
for iteration in range(max_iterations):
    # 审核
    results = await parallel_audit(...)
    score = calculate_score(results)
    
    if score >= target_score:
        break
    
    # 修复
    await fixer_fix(...)
```

## Agent 输出格式

所有审核 Agent 输出统一格式：

```yaml
score: 85
issues:
  - severity: critical
    rule: 规则名
    message: 问题描述
    location: 第3页-标题
    suggestion: 修复建议
summary: 审核摘要
passed: false
```

## 评分计算

```python
def calculate_score(results):
    weights = {
        'slide-auditor': 0.25,
        'pedagogy-reviewer': 0.30,
        'content-validator': 0.25,
        'character-consistency': 0.20
    }
    
    base_score = sum(
        r.score * weights.get(r.agent, 0.25)
        for r in results
    )
    
    # 惩罚项
    critical_count = sum(
        1 for r in results for i in r.issues
        if i.severity == 'critical'
    )
    penalty = min(critical_count * 5, 20)
    
    return max(0, base_score - penalty)
```

## 关键原则

1. **Agent 只做审核，不做修复**
   - 审核 Agent 输出问题和建议
   - 修复由专门的 slides-fixer 执行

2. **内容源于课件**
   - slides-fixer 从 CHAPTER.md 提取内容
   - 精炼而非重新创作

3. **并行审核**
   - 4 个审核 Agent 同时运行
   - 提高效率

4. **循环收敛**
   - 最多 5 轮 Critic-Fixer
   - 卡死检测防止无限循环
