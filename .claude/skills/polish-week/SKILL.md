---
name: polish-week
description: 当正文读起来过于模板化/AI味时，对指定 week 的 CHAPTER.md 做教材化润色，并补充近两年 AI/agentic 编程侧栏。
argument-hint: "<week_id e.g. week_01>"
allowed-tools: Bash, Read, Write, Edit, Grep, Glob
disable-model-invocation: true
---

# /polish-week

## 用法

```
/polish-week week_XX
```

## 目标

- `chapters/week_XX/CHAPTER.md` 更像教材：自然、轻松、有趣，但仍然清晰、可教。
- 追加 1-2 个“AI 时代小专栏”（200-500 字/个），讨论近两年（约 24 个月）AI 对编程/软件工程的影响，并附参考链接 + 访问日期。
- 不改代码/测试/YAML，不引入新的 `validate_week.py` 失败。

## 步骤

1. 调用 subagent `prose-polisher`：只润色 `chapters/week_XX/CHAPTER.md`。
2. （可选）调用 subagent `consistency-editor`：做格式/术语/引用一致性检查（避免重写内容）。
3. 验证（不要求 QA 阻塞清零时用 task 模式）：
   ```bash
   python3 scripts/validate_week.py --week week_XX --mode task
   ```

如果你准备发布（并且已清空 QA 阻塞项），再跑：

```bash
python3 scripts/validate_week.py --week week_XX --mode release
```

