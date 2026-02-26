# Create Slides

## 简介

将教材章节内容（CHAPTER.md）转换为现代化 HTML 教学幻灯片。

## 使用方式

```bash
/create-slides --week week_XX [--target-score 90]
```

**参数说明：**
- `--week`: 周次，如 `week_01` 或批量 `week_01,week_02,week_03`
- `--target-score`: 目标质量分，**默认 90**（可选，可省略）

## 工作流程

当我收到 `/create-slides` 命令时，我会：

### Phase 1: 内容提取与初始生成

1. 读取 `chapters/{week}/CHAPTER.md`
2. 提取关键信息：
   - 周数、标题、引言
   - 学习目标（SMART 化）
   - 角色出场规划（小北/阿码/老潘）
   - AI 专栏主题
3. **创建目录结构**：
   ```bash
   mkdir -p slides/{week}
   ```
4. **生成 `slides/{week}/index.html`**：
   - 使用共享样式：`../themes/modern-education.css`
   - 使用共享 JS：`../lib/slide-framework.js`
   - 提取幻灯片内容（`<section class="slide">` 块）
   - 包含导航控制、缩略图、键盘导航功能

### Phase 2: 并行审核

并行调用 5 个审核 Agent：

```yaml
agents:
  - slide-auditor:
      input: slides.html
      check: [字体, 颜色, 布局, 文字密度≤150字]
      
  - pedagogy-reviewer:
      input: slides.html
      check: [SMART目标, 叙事结构, 互动设计]
      
  - content-validator:
      input: [slides.html, CHAPTER.md]
      check: [内容覆盖, AI专栏, 贯穿案例]
      
  - character-consistency:
      input: slides.html
      check: [角色出场, 性格一致性, 视觉标识]
      
  - style-consistency:
      input: slides.html
      check: [CSS变量, 页面类型class, 玻璃拟态]
```

### Phase 3: Critic 分析

我（作为 Critic）会：
1. 汇总 5 个 Agent 的审核结果
2. 计算综合评分：
   ```
   base = slide*0.2 + pedagogy*0.25 + content*0.2 + character*0.15 + style*0.2
   penalty = blockers*15 + min(criticals*5, 20)
   final = max(0, base - penalty)
   ```
3. 生成修复清单（按优先级排序）

### Phase 4: Fixer 修复

调用 `slides-fixer` Agent：
```yaml
input:
  slides: slides.html
  chapter: CHAPTER.md
  issues: [修复清单]
  
task:
  - 从 CHAPTER.md 提取内容填充占位符
  - 优化文字密度
  - 确保样式符合规范
  - 输出修复后的 slides.html
```

### Phase 5: 循环直到达标

重复 Phase 2-4 直到：
- 综合评分 ≥ target-score（默认 90，可通过 `--target-score` 调整）
- 或达到最大迭代次数（默认 5 轮）
- 或连续 2 轮无改进（卡死检测）

## 质量门禁

| 评分 | 级别 | 处理 |
|-----|------|------|
| ≥ 90 | 🟢 优秀 | 直接发布 |
| 80-89 | 🟡 良好 | 微调后发布 |
| 60-79 | 🟠 需改进 | 修复后发布 |
| < 60 | 🔴 不合格 | 需人工重写 |

## 输出

### 新结构（集中式）
```
slides/
├── week_XX/                 # 各周幻灯片目录
│   └── index.html          # 幻灯片入口
├── themes/
│   └── modern-education.css # 共享样式（全局唯一）
├── lib/
│   └── slide-framework.js   # 共享 JS 框架
└── FINAL_REPORT.md          # 审核报告（如需要）

chapters/week_XX/            # 章节内容（不含 slides）
├── CHAPTER.md
├── ASSIGNMENT.md
└── ...
```

### 路径规范
- CSS: `../themes/modern-education.css`
- JS: `../lib/slide-framework.js`
- 每 week 独立目录: `slides/week_XX/index.html`

## 示例

```bash
# 生成并自动修复直到 90 分（默认，可省略 --target-score）
/create-slides --week week_01

# 显式指定目标分数
/create-slides --week week_01 --target-score 90

# 批量生成
/create-slides --week week_01,week_02,week_03

# 降低标准快速生成（仅用于草稿）
/create-slides --week week_01 --target-score 75
```

## Agent 提示模板

### slide-auditor

```
审核 HTML 幻灯片视觉布局: {slides_path}

检查项目：
1. 字体大小（标题≥32px，正文≥20px，代码≥14px）
2. 文字密度（每页去除HTML标签后≤150字）
3. 颜色使用（必须统一使用 CSS 变量，禁止硬编码）
4. 页面布局平衡
5. 代码块展示合理性

输出格式：
score: 0-100
issues:
  - severity: blocker|critical|suggestion
    rule: 规则名
    message: 问题描述
    location: 具体位置
    suggestion: 修复建议
summary: 审核摘要
passed: true|false
```

### pedagogy-reviewer

```
审核 HTML 幻灯片教学质量: {slides_path}

检查项目：
1. SMART 学习目标（避免弱动词：了解、知道、认识、熟悉）
2. 认知负荷（每页新概念≤3个）
3. 叙事弧线完整性（必须有 cover/learning-objectives/summary/end）
4. 互动设计（至少1个 exercise 页面）
5. 节奏控制（避免连续4页同类型）

输出格式同上。
```

### content-validator

```
对比 CHAPTER.md 和 HTML 幻灯片内容完整性:
- CHAPTER: {chapter_path}
- SLIDES: {slides_path}

检查项目：
1. 学习目标覆盖度（每个学习目标都有对应页面）
2. AI 专栏完整性（与 CHAPTER.md 中的数量一致）
3. 贯穿案例展示
4. 关键概念覆盖

输出格式同上。
```

### character-consistency

```
审核角色一致性: {slides_path}

角色设定：
- 小北：学生视角，蓝色 #3B82F6，class="dialogue xiaobei"
- 阿码：追问者，橙色 #FF6B35，class="dialogue ama"
- 老潘：导师，绿色 #0D7377，class="dialogue laopan"

检查项目：
1. 角色出场次数（总计≥2次，各角色≥1次）
2. 性格一致性（小北=困惑/尝试，阿码=质疑/追问，老潘=引导/总结）
3. 视觉标识正确性（CSS class 和颜色）

输出格式同上。
```

### style-consistency

```
审核样式一致性: {slides_path}

CSS 变量规范：
- --primary: #0D7377
- --accent-orange: #FF6B35
- --accent-blue: #3B82F6
- --glass-bg: rgba(255,255,255,0.7)

检查项目：
1. 是否使用统一 CSS 变量（禁止硬编码颜色）
2. 页面类型 class 是否正确（cover/agenda/narrative/concept/exercise/summary/end）
3. 角色样式类是否正确（dialogue xiaobei/ama/laopan）
4. 玻璃拟态效果是否一致

输出格式同上。
```

### slides-fixer

```
修复 HTML 幻灯片

文件: slides/{week}/index.html
源课件: chapters/{week}/CHAPTER.md
问题清单: {issues}

修复任务：
1. 从 CHAPTER.md 提取关键内容
2. 填充 slides/{week}/index.html 中的占位符
3. 优化文字密度（每页≤150字）
4. 确保使用统一 CSS 变量（引用 ../themes/modern-education.css）
5. 修复所有审核发现的问题

路径检查：
- CSS 路径应为：../themes/modern-education.css
- JS 路径应为：../lib/slide-framework.js

修复原则：
- 内容源于课件，而非重新创作
- 精炼表达，而非简单拆分
- 保持 PPT 与课件章节对应

输出：修复后的完整 HTML 内容
```

## 样式系统

**现代玻璃拟态设计 (Glassmorphism)**

```css
/* 共享样式: slides/themes/modern-education.css */
:root {
  --primary: #0D7377;
  --accent-orange: #FF6B35;
  --glass-bg: rgba(255, 255, 255, 0.7);
  --glass-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  
  /* 幻灯片固定尺寸 */
  --slide-width: 1280px;
  --slide-height: 720px;
}
```

**页面类型：**
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

**共享资源路径：**
- 样式：`../themes/modern-education.css`
- JS 框架：`../lib/slide-framework.js`
- 每 week 引用上一级目录的共享资源

## 参考

- [教材风格指南](../../shared/style_guide.md)
- [角色设定](../../shared/characters.yml)
- [幻灯片模板](../../templates/slides/slides-template.html)
- [Glassmorphism Design](https://glassmorphism.com/)
