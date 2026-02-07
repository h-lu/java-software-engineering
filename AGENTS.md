# Agent Instructions (python-agentic-textbook)

本仓库用于生成《Python 程序设计（Agentic Coding）》教材的“每周章包”交付物（见 `chapters/`）。

## Superpowers / Skills

你可能会看到旧的指令让你运行：

```bash
~/.codex/superpowers/.codex/superpowers-codex bootstrap
```

在这台机器上该命令不存在（superpowers 已改为“技能目录 symlink 发现机制”）。
正确安装方式参考：`~/.codex/superpowers/.codex/INSTALL.md`，并确保存在：

```bash
~/.agents/skills/superpowers
```

## Project DoD (必须遵守)

- 对任意周：`python3 scripts/validate_week.py --week week_XX --mode release` 必须通过
- 并且：`python3 -m pytest chapters/week_XX/tests -q` 必须通过
- 任务 subject 必须以 `[week_XX]` 开头（hooks 依赖）

## Gitea PR 流程（建议）

- Week 06+ 默认走：分支 -> 多次提交 -> push -> Pull Request (PR) -> review -> merge
- 参考：`shared/gitea_workflow.md`

