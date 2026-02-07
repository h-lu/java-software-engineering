# 《Python 程序设计（Agentic Coding）》写作宪法

本文件是全书的最高约束。所有 subagents、skills、team mate 的输出都必须遵守。

## 目标与读者

- 读者：零基础（第一次学编程）或只会一点点 Python 的初学者。
- 目标：用“工程化 + agentic 工作流”掌握 Python 程序设计的基本功（从脚本到可维护的小工具/小项目）。
- 写作默认语言：中文；允许关键术语括注英文。

## 每周章包 Definition of Done (DoD)

对任意 `chapters/week_XX/`，发布前必须满足：

1. 文件齐全：
   - `CHAPTER.md` / `ASSIGNMENT.md` / `RUBRIC.md` / `QA_REPORT.md`
   - `ANCHORS.yml` / `TERMS.yml`
   - `examples/` / `starter_code/solution.py` / `tests/`
2. `python3 -m pytest chapters/week_XX/tests -q` 通过。
3. `QA_REPORT.md` 的“阻塞项”下不存在未勾选 `- [ ]`。
4. `TERMS.yml` 中出现的 `term_zh` 必须已进入 `shared/glossary.yml`。
5. `ANCHORS.yml`：
   - `id` 周内唯一
   - `claim/evidence/verification` 字段齐全
   - `verification` 可执行（推荐 `pytest:...` 或 `cmd:...`）

上述规则由 `scripts/validate_week.py` 与 Claude Code hooks 强制。

### Git/Gitea 建议项（不作为硬闸门）

为便于复盘、回滚与协作，本书建议每周都使用 Gitea 的 Pull Request (PR) 流程（语义等价 GitHub）：

- `/release-week` 前，建议先确认工作区干净：
  - `git status --porcelain` 输出为空
- 建议至少 2 次提交（draft + verify），并能在 `git log -n 5 --oneline` 里看到。
- PR 描述建议引用本周 DoD，并粘贴验证命令（pytest/validate）的通过信息。
  - 参考：`shared/gitea_workflow.md`

## 行文风格（摘要）

详见 `shared/style_guide.md`。核心原则：

- 概念先行：先给读者“为什么”，再给“怎么做”。
- 每节应覆盖：动机/概念、最小正例、常见错误/反例、小结。
  - 不要求每节都用固定四个小标题；优先自然叙述，避免“模板感/AI味”。
- 代码块必须可运行（或明确注明“伪代码/节选”）。
- 重要结论必须可验证：落到 `ANCHORS.yml`。
  - 若正文读起来过于模板化，可在 Claude Code 中运行：`/polish-week week_XX`（只润色 `CHAPTER.md`）。

## 术语与一致性

- 新术语先在当周 `TERMS.yml` 登记，再合入 `shared/glossary.yml`。
- 术语中文为主，英文可选；定义必须短、清晰、可被新手复述。

## Team 协作约定（强制）

- 所有 task subject 必须以 `[week_XX]` 开头，供 hooks 精确定位。
  - 示例：`[week_01] Draft chapter outline`
- Lead 建议开启 delegate mode：只拆任务与收敛，不写正文。
- 任意 teammate 完成任务前，必须确保本周校验通过（hooks 会拦截）。
