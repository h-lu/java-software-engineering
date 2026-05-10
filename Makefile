# ============================================================================
# 《Java 软件工程与 Agentic 开发》教材工厂 — 快捷命令
#
# 常用：
#   make help                                 # 查看所有命令
#   make setup                                # 一键环境搭建
#   make scaffold                             # 批量创建 16 周目录
#   make new W=01 T="你的第一个程序"           # 创建新周
#   make draft W=01                           # 写正文初稿（完整流水线）
#   make polish W=01                          # 深度润色
#   make validate W=01                        # 校验（默认 release 模式）
#   make test W=01                            # 跑该周 Maven/JUnit 或 pytest 测试
#   make release W=01                         # 发布
#   make book-check                           # 全书一致性检查
# ============================================================================

PYTHON ?= python3
SCRIPTS = scripts

.PHONY: help setup scaffold new draft polish validate test release resume status book-check

# ---------------------------------------------------------------------------
# 环境与脚手架
# ---------------------------------------------------------------------------

help: ## 显示所有可用命令
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-14s\033[0m %s\n", $$1, $$2}'

setup: ## 一键创建 venv 并安装依赖
	bash $(SCRIPTS)/setup_env.sh

scaffold: ## 批量创建 week_01..week_16 目录（从 TOC.md 读标题）
	$(PYTHON) $(SCRIPTS)/scaffold_book.py

# ---------------------------------------------------------------------------
# 单周操作
# ---------------------------------------------------------------------------

new: ## 创建新周 (W=01 T="标题")
	@test -n "$(W)" || { echo "error: W is required (e.g. make new W=01 T=\"标题\")"; exit 1; }
	@test -n "$(T)" || { echo "error: T is required (e.g. make new W=01 T=\"标题\")"; exit 1; }
	$(PYTHON) $(SCRIPTS)/new_week.py --week $(W) --title "$(T)"

draft: ## 写正文初稿 — 规划→写→润色→QA→修订 (W=01)
	@test -n "$(W)" || { echo "error: W is required (e.g. make draft W=01)"; exit 1; }
	@echo "请在 Claude Code 中运行: /draft-chapter week_$$(printf '%02d' $(W))"

polish: ## 深度润色已有章节 (W=01)
	@test -n "$(W)" || { echo "error: W is required (e.g. make polish W=01)"; exit 1; }
	@echo "请在 Claude Code 中运行: /polish-week week_$$(printf '%02d' $(W))"

validate: ## 校验某周 (W=01, MODE=release|task|idle, V=1 开启详细输出)
	@test -n "$(W)" || { echo "error: W is required (e.g. make validate W=01)"; exit 1; }
	$(PYTHON) $(SCRIPTS)/validate_week.py --week $(W) --mode $(or $(MODE),release) $(if $(V),--verbose)

test: ## 跑某周测试 (W=01)
	@test -n "$(W)" || { echo "error: W is required (e.g. make test W=01)"; exit 1; }
	@week_dir=chapters/week_$$(printf '%02d' $(W)); \
	if [ -f "$$week_dir/starter_code/pom.xml" ]; then \
		mvn -q -f "$$week_dir/starter_code/pom.xml" test; \
	elif [ -d "$$week_dir/tests" ]; then \
		$(PYTHON) -m pytest "$$week_dir/tests" -q; \
	else \
		echo "error: no supported test target found under $$week_dir"; \
		exit 1; \
	fi

release: ## 发布某周 (W=01)
	@test -n "$(W)" || { echo "error: W is required (e.g. make release W=01)"; exit 1; }
	$(PYTHON) $(SCRIPTS)/release_week.py --week $(W)

resume: ## 显示某周完成状态 (W=01)
	@test -n "$(W)" || { echo "error: W is required (e.g. make resume W=01)"; exit 1; }
	$(PYTHON) $(SCRIPTS)/resume_week.py --week $(W)

# ---------------------------------------------------------------------------
# 全书操作
# ---------------------------------------------------------------------------

book-check: ## 全书一致性检查 (MODE=fast|release, STRICT=1, V=1)
	$(PYTHON) $(SCRIPTS)/validate_book.py --mode $(or $(MODE),fast) $(if $(STRICT),--strict) $(if $(V),--verbose)

status: ## 显示当前周和 git 状态
	@echo "current_week: $$(cat chapters/current_week.txt 2>/dev/null || echo '(未设置)')"
	@echo ""
	@git status --short
