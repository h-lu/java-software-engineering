# Java 软件工程与 Agentic 开发（教材项目）

> 一本面向有 Python 基础学生的 Java 进阶教材，采用 Agentic Coding 方式自动生成。

## 核心理念

**"AI 是倍增器，不是替代者。架构决策在人，质量把控在人，工程思维必须掌握在自己手中。"**

---

## 课程定位

- **前置要求**：已学过 Python 基础（完成《Python 程序设计（Agentic Coding）》）
- **课程时长**：16 周
- **班级规模**：70 人，2-3 人团队（约 25-35 个项目）
- **技术栈**：Java 17 + Javalin（极简）+ SQLite + JUnit 5
- **项目形式**：团队项目（CampusFlow）+ 项目集市展示

---

## 16 周课程结构

| 阶段 | 周次 | 能力目标 |
|------|------|---------|
| 思维奠基 | 01–04 | 从 Python 迁移到 Java，建立工程思维 |
| 系统化工程 | 05–08 | 构建可靠的后端系统，ADR 训练 |
| AI 时代工程 | 09–12 | 与 AI 协作，但保持判断力 |
| 综合实战 | 13–16 | 交付与工程复盘 |

### 关键机制

1. **ADR（架构决策记录）**：每两周一篇，记录设计决策
2. **首席架构师轮换**：每两周轮换，负责设计决策
3. **AI 审查训练**：Week 10 核心，学会批判性审查 AI 输出

---

## 快速开始

```bash
# 1. 克隆并进入项目
git clone <repo-url> && cd java-software-engineering

# 2. 一键环境搭建
make setup            # 创建环境

# 3. 批量创建 16 周目录
make scaffold         # 从 TOC.md 读取标题，生成所有周的模板

# 4. 校验
make validate W=01    # 校验第 1 周
make test W=01        # 跑第 1 周测试
make book-check       # 全书一致性检查
```

---

## 一周写作流程

在 Claude Code 中打开本项目，使用 skill 命令：

```
/new-week 01 软件工程史与类型思维     # 1. 创建新周
/team-week week_01                     # 2. Agent Team 并行产出（推荐）
                                       #    规划 → 写正文 → 润色 → QA
/qa-week week_01                       # 3. 质量检查
/release-week week_01                  # 4. 发布
```

---

## 目录结构

```
chapters/
  SYLLABUS.md              # 16 周教学大纲
  TOC.md                   # 目录
  week_XX/                 # 每周一个章包
    CHAPTER.md             #   正文
    ASSIGNMENT.md          #   作业
    RUBRIC.md              #   评分标准
    QA_REPORT.md           #   质量报告
    ANCHORS.yml            #   可验证断言
    TERMS.yml              #   本周新术语
    examples/              #   示例代码（.java）
    starter_code/          #   作业起始代码
    tests/                 #   JUnit 测试用例

shared/
  style_guide.md           # Java 代码风格规范
  writing_exemplars.md     # 写作范例库
  characters.yml           # 角色设定（小北/阿码/老潘）
  book_project.md          # CampusFlow 项目设计
  ai_progression.md        # AI 倍增器理念
  concept_map.yml          # Java 概念图谱
  glossary.yml             # 全书术语表
  anchor_schema.md         # 锚点格式说明

.claude/
  agents/                  # Agent 配置（带 MCP 工具）
  skills/                  # Skill 命令
  hooks/                   # 自动校验
  settings.json            # Claude Code 项目配置

scripts/                   # 校验/构建脚本
Makefile                   # 快捷命令入口
```

---

## 写作质量体系

- **场景驱动**：先让读者感受到需求，再引出概念
- **贯穿案例**：CampusFlow 项目逐周演进
- **角色叙事**：小北（困惑）、阿码（追问）、老潘（工程建议）
- **ADR 强制**：每两周一篇架构决策记录
- **AI 审查训练**：Week 10 专门训练批判性思维

---

## 开发约定

- 默认中文写作，关键术语括注英文
- 重要断言必须落到 `ANCHORS.yml`
- 新术语必须进入 `shared/glossary.yml`
- ADR 必须由人写，不能用 AI 代劳
- 所有 task subject 以 `[week_XX]` 开头
