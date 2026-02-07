# python-agentic-textbook

用 Claude Code 的 subagents + agent teams + hooks + /skills，把《Python 程序设计（Agentic Coding）》教材写作做成可重复的“每周章包”流水线。

定位：
- 读者：零基础/初学者
- 协作：自建 Gitea（Pull Request 流程语义等价 GitHub）
- 特色：每周都用“Plan -> Implement -> Verify -> Reflect”的 agentic 工作流交付可验证产物

## 目录结构（核心）

- `chapters/week_XX/`：每周一个章包（正文 + 示例 + 作业 + rubric + tests + QA + anchors/terms）
- `.claude/`：Claude Code 项目配置（subagents/skills/hooks/settings）
  - `agents/`：9 个专职 Agent（writer/example/test/exercise/consistency/polisher/qa/planner/error-fixer）
  - `skills/`：技能命令（/new-week、/draft-chapter、/release-week 等）
  - `hooks/`：TaskCompleted / TeammateIdle 自动校验
- `scripts/`：可复用脚本（`_common.py` 共享库 + 建周/校验/发布/恢复）
- `shared/`：全书共享（style guide / glossary / anchor schema / chapter sections / week summary 约定）
- `Makefile`：快捷命令入口

## Quickstart（本机验证脚本与测试）

```bash
cd /Users/wangxq/Documents/python-agentic-textbook

# 一键环境搭建
bash scripts/setup_env.sh
source .venv/bin/activate

# 创建新周
make new W=01 T="从零到可运行：Hello Python + 工程基线"

# 校验（task 模式用于写作中，release 模式用于发布前）
make validate W=01 MODE=task
make validate W=01              # 默认 release 模式

# 测试
make test W=01

# 查看进度
make resume W=01

# 全书检查
make book-check

# 批量创建 14 周目录
make scaffold
```

所有 `make` 命令见 `make help`。

## Claude Code 使用说明（hooks + teams）

1. 用 Claude Code 打开本项目目录（项目根目录包含 `.claude/settings.json`）。
2. Agent teams：本项目在 `.claude/settings.json` 里设置了 `CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS=1`。
3. Hooks：
   - `TaskCompleted`：任务标记完成时跑 `scripts/validate_week.py --mode task`
   - `TeammateIdle`：teammate 空闲前跑 `scripts/validate_week.py --mode idle`

注意：hooks 会优先使用项目内的 `.venv`（如果存在）运行校验脚本；因此建议在项目根目录创建 venv 并安装 `requirements-dev.txt`。

## 一周工作流（/skills）

在 Claude Code 里：

1. `/new-week 01 从零到可运行：Hello Python + 工程基线`
2. （可选）`/team-week week_01`（生成一段 kickoff 提示词，用 agent team 并行产出整周内容）
3. `/draft-chapter week_01`（已内置润色：prose-polisher）
4. （可选）`/polish-week week_01`（如果后续还想再次润色，可单独再跑一次）
5. `/make-assignment week_01`
6. `/qa-week week_01`
7. `/release-week week_01`
8. （可选）`/qa-book --mode fast`（跨周一致性检查：目录/标题/术语表）

Gitea 协作流程见：`shared/gitea_workflow.md`

## 开发约定

- 默认中文写作；关键术语可括注英文。
- 所有“应该如此”的断言必须落到 `ANCHORS.yml` 并提供可验证方式。
- 所有新增术语必须进入 `shared/glossary.yml`（由校验脚本与 hooks 强制）。
