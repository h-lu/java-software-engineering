# Week 16 示例代码与文档

本目录包含 Week 16（项目展示与工程复盘）的示例代码和文档模板。

## 示例清单

### 文档模板

1. **16_showcase_feedback.md** - 项目集市反馈收集与分析
   - 反馈闭环（Keep / Problem / Try）
   - CampusFlow 团队的反馈整理示例
   - 最佳实践：收集、整理、回应反馈的方法

2. **16_retrospective_report.md** - 工程复盘报告（KPT 复盘法）
   - Keep（做得好的）
   - Problem（遇到的问题）
   - Try（下次尝试）
   - 16 周 ADR 回顾

3. **16_skill_radar.md** - 能力雷达图与成长轨迹
   - 六维能力模型
   - Week 01 vs Week 16 对比
   - 简历写作建议

4. **16_knowledge_map.md** - 16 周课程知识地图
   - 四阶段结构
   - 两条主线（ADR + CampusFlow）
   - 概念关联网络
   - 下一步学习建议

5. **16_career_roadmap.md** - 工程师职业发展路径
   - 初级 → 中级 → 高级 → 专家
   - 各阶段能力要求
   - 转型方向（PM、销售、创业）
   - 薪资参考

6. **16_campusflow_roadmap.md** - CampusFlow 未来发展路线图
   - v1.0.1（Bug 修复）
   - v1.1.0（功能增强）
   - v1.2.0（体验优化）
   - v2.0.0（架构重构）
   - v3.0.0（SaaS 化）

### 代码示例

1. **CampusFlowFeedbackCollector.java** - 反馈收集工具
   - 交互式收集反馈（分类、内容、来源、优先级）
   - 自动统计（KEEP / PROBLEM / TRY 数量）
   - 导出 Markdown 报告
   - 生成行动清单

2. **SkillRadarGenerator.java** - 能力雷达图生成器
   - 六维能力评分（1-5 分）
   - 生成 ASCII 雷达图
   - 证据记录（CampusFlow 项目经历）
   - 导出详细评估报告

## 运行方式

### 反馈收集工具

```bash
# 编译
javac examples/CampusFlowFeedbackCollector.java

# 运行
java CampusFlowFeedbackCollector

# 按提示输入反馈，完成后会生成 feedback_report.md
```

### 能力雷达图生成器

```bash
# 编译
javac examples/SkillRadarGenerator.java

# 运行
java SkillRadarGenerator

# 会生成 skill_radar.md，包含雷达图和详细评估
```

## 文档用途

### 学生使用

1. **项目集市后**：使用 `16_showcase_feedback.md` 模板整理反馈
2. **撰写复盘报告**：参考 `16_retrospective_report.md` 的 KPT 方法
3. **自我评估**：用 `16_skill_radar.md` 评估 16 周成长
4. **职业规划**：参考 `16_career_roadmap.md` 规划下一步学习

### 教师使用

1. **课程回顾**：用 `16_knowledge_map.md` 帮助学生回顾 16 周内容
2. **评分参考**：用 `16_skill_radar.md` 的六维模型评估学生
3. **CampusFlow 演进**：参考 `16_campusflow_roadmap.md` 规划课程版本

## 核心概念

### KPT 复盘法

| 维度 | 含义 | 示例 |
|------|------|------|
| Keep | 做得好的，继续保持 | Feature Branch 工作流效果很好 |
| Problem | 遇到的，需要改进 | 需求蔓延导致功能过多 |
| Try | 下次尝试的新方法 | 引入 CI/CD 工具 |

### 六维能力模型

| 维度 | Week 01 | Week 16 | 提升 |
|------|---------|---------|------|
| 编程语言 | 2 | 4 | +2 |
| 工程工具 | 2 | 4 | +2 |
| 测试能力 | 1 | 4 | +3 |
| 架构设计 | 1 | 3 | +2 |
| 文档能力 | 1 | 3 | +2 |
| 团队协作 | 1 | 3 | +2 |

### 反馈闭环

```
收集 → 分类 → 分析 → 行动 → 验证
  ↑                              ↓
  └──────────────────────────────┘
```

## CampusFlow 进度

Week 16 是 CampusFlow 的终点，也是新起点：

```
Week 01: 项目启动
Week 04: 领域模型 + 异常处理
Week 08: 数据持久化 + 测试
Week 12: Web 化 + AI 协作
Week 16: 交付 + 展示 + 复盘

未来：v1.0.1 → v1.1 → v1.2 → v2.0（微服务）
```

## 延伸阅读

### 复盘方法
- 《敏捷回顾：让团队从优秀到卓越》
- Google 的 "Project Aristotle"（团队效能研究）

### 职业发展
- 《程序员修炼之道》
- 《软件工程师的职业发展路径》

### 产品规划
- 《启示录：打造用户喜爱的产品》
- 《精益创业》

## 与前几周的联系

| 周次 | 概念 | Week 16 的应用 |
|------|------|----------------|
| Week 02 | ADR | 复盘时回顾 4 篇 ADR |
| Week 04 | Git 协作 | Feature Branch 效果评估 |
| Week 06 | 单元测试 | 测试能力 4 分的证据 |
| Week 10 | AI 审查 | AI 幻觉导致返工的反思 |
| Week 11 | 质量门禁 | 本地质量门禁的效果 |
| Week 15 | 技术演示 | 演示准备的反思 |

## 下一步

完成 Week 16 后，建议：

1. **整理作品集**：
   - CampusFlow 代码（GitHub）
   - 项目集市反馈
   - 能力评估报告
   - 技术博客

2. **规划学习路径**：
   - 参考 `16_career_roadmap.md`
   - 选择方向（深度技术 / 广度工程 / 产品转型）
   - 制定 3 个月行动计划

3. **保持实践**：
   - 继续 CampusFlow 开发（v1.1/v1.2）
   - 参与开源项目
   - 写技术博客

---

**作者**: ExampleEngineer
**日期**: 2026-01-13
**课程**: Java 软件工程与 Agentic 开发
