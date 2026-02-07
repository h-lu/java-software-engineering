---
name: draft-chapter
description: 生成本周正文初稿：定结构->写正文->润色去模板感->学生视角 QA->落盘 QA_REPORT。
argument-hint: "<week_id e.g. week_01>"
allowed-tools: Read, Write, Edit, Grep, Glob
disable-model-invocation: true
---

# /draft-chapter

## 用法

```
/draft-chapter week_XX
```

## 目标

- `CHAPTER.md`：结构完整 + 初稿可读 + 风格更像教材（减少模板感/AI味）
- `QA_REPORT.md`：写入 StudentQA 的阻塞项/建议项

## 步骤（按顺序）

1. 调用 subagent `syllabus-planner`：让它先把章节结构与 DoD 写进 `chapters/week_XX/CHAPTER.md`。
2. 调用 subagent `chapter-writer`：按结构补齐正文（每节：概念->正例->常见错->小结）。
3. 调用 subagent `prose-polisher`：润色 `CHAPTER.md`，让口吻更自然、更像教材，并可加入 1-2 个“AI 时代小专栏”（带参考链接 + 访问日期）。
4. 调用 subagent `student-qa`：只读审读并输出问题清单。
5. 把 StudentQA 输出落盘到 `chapters/week_XX/QA_REPORT.md`：
   - 阻塞项放到“## 阻塞项”下（checkbox）
   - 建议项放到“## 建议项”下（checkbox）
