---
name: team-week
description: 需要用 agent team 并行产出一整周章包时，生成一段可直接粘贴给 Lead 的 kickoff 提示词（含角色、任务拆分、依赖与校验）。
argument-hint: "<week_id e.g. week_01>"
allowed-tools: Read, Grep, Glob, Write, Edit
disable-model-invocation: true
---

# /team-week

## 用法

```
/team-week week_XX
```

## 作用

输出一段“可直接粘贴给 Lead 会话”的 kickoff 提示词，用于让 agent team 在一个 sprint 内并行产出整周章包，并通过 hooks/校验。

## 输出模板（把 week_XX 替换成参数）

```text
你是 Lead（delegate mode，只拆任务与收敛，不直接写正文）。
目标：把 week_XX 产出成完整章包，并通过校验：
- python3 scripts/validate_week.py --week week_XX --mode release

强制约束：
- 所有 task subject 必须以 [week_XX] 开头（hooks 依赖）
- 交付遵循 CLAUDE.md + shared/style_guide.md
- /draft-chapter week_XX 已内置 prose-polisher（去模板感 + 可加 AI 时代小专栏）

请创建 team 角色（建议 5 人）：
- Writer（CHAPTER）
- Example（examples + 讲解段落）
- Assignment（ASSIGNMENT + RUBRIC）
- QA（student-qa，学生视角阻塞项）
- Consistency（consistency-editor，术语/格式/引用一致性）

Task list（可并行，注意依赖）：
- [week_XX] Outline + DoD（syllabus-planner）
- [week_XX] Draft CHAPTER.md（chapter-writer）
- [week_XX] Polish CHAPTER.md prose + AI sidebar refs（prose-polisher，已在 /draft-chapter 内）
- [week_XX] Produce examples（example-engineer）
- [week_XX] Design tests（test-designer）
- [week_XX] Write assignment + rubric（exercise-factory）
- [week_XX] QA sweep (blocking/suggestions)（student-qa）
- [week_XX] Consistency sweep (terms/anchors/format)（consistency-editor）
- [week_XX] Green check + release（/qa-week -> /release-week）

关键检查点（任何人完成任务前都要过）：
- python3 -m pytest chapters/week_XX/tests -q
- python3 scripts/validate_week.py --week week_XX --mode task

收敛规则：
- QA_REPORT 的“阻塞项”必须清零（不允许 - [ ]）才能 release
- 不要为了“写完”牺牲可运行/可验证：tests/anchors/terms 要能对上
```

