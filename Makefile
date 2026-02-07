# ============================================================================
# 教材工厂 — 快捷命令
#
# 使用方式：
#   make new W=01 T="从零到可运行：Hello Python + 工程基线"
#   make validate W=01
#   make test W=01
#   make release W=01
#   make book-check
#   make scaffold
#   make setup
#   make resume W=01
# ============================================================================

PYTHON ?= python3
SCRIPTS = scripts

.PHONY: help new validate test release book-check scaffold setup resume status

help: ## 显示所有可用命令
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-14s\033[0m %s\n", $$1, $$2}'

new: ## 创建新周 (W=01 T="标题")
	@test -n "$(W)" || { echo "error: W is required (e.g. make new W=01 T=\"标题\")"; exit 1; }
	@test -n "$(T)" || { echo "error: T is required (e.g. make new W=01 T=\"标题\")"; exit 1; }
	$(PYTHON) $(SCRIPTS)/new_week.py --week $(W) --title "$(T)"

validate: ## 校验某周 (W=01, MODE=release|task|idle)
	@test -n "$(W)" || { echo "error: W is required (e.g. make validate W=01)"; exit 1; }
	$(PYTHON) $(SCRIPTS)/validate_week.py --week $(W) --mode $(or $(MODE),release) $(if $(V),--verbose)

test: ## 跑某周测试 (W=01)
	@test -n "$(W)" || { echo "error: W is required (e.g. make test W=01)"; exit 1; }
	$(PYTHON) -m pytest chapters/week_$(shell printf '%02d' $(W))/tests -q

release: ## 发布某周 (W=01)
	@test -n "$(W)" || { echo "error: W is required (e.g. make release W=01)"; exit 1; }
	$(PYTHON) $(SCRIPTS)/release_week.py --week $(W)

book-check: ## 全书一致性检查 (MODE=fast|release, STRICT=1)
	$(PYTHON) $(SCRIPTS)/validate_book.py --mode $(or $(MODE),fast) $(if $(STRICT),--strict) $(if $(V),--verbose)

scaffold: ## 批量创建 week_01..week_14 目录
	$(PYTHON) $(SCRIPTS)/scaffold_book.py

setup: ## 一键创建 venv 并安装依赖
	bash $(SCRIPTS)/setup_env.sh

resume: ## 显示某周的完成状态 (W=01)
	@test -n "$(W)" || { echo "error: W is required (e.g. make resume W=01)"; exit 1; }
	$(PYTHON) $(SCRIPTS)/resume_week.py --week $(W)

status: ## 显示当前周和 git 状态
	@echo "current_week: $$(cat chapters/current_week.txt 2>/dev/null || echo '(empty)')"
	@echo ""
	@git status --short
