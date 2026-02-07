# 14 周大纲（零基础 Python + Agentic Coding + Gitea PR）

本大纲面向**零基础/初学者**：每周只引入少量 Python 新知识，并用 Claude Code 的工作流把“写程序”变成**可验证交付**：

- Plan：写清目标与 DoD
- Implement：小步实现（优先可运行）
- Verify：`validate_week.py` + `pytest`
- Reflect：用 QA/anchors 复盘并修订

协作平台使用自建 Gitea；Pull Request (PR) 流程语义等价 GitHub。
参考：`shared/gitea_workflow.md`

## 周计划（每周：Python + Agentic + Git）

1. Week 01： 从零到可运行：Hello Python + 工程基线
   - Python：运行脚本、print/变量/字符串、最小 I/O
   - Agentic：DoD 概念；学会跑 `validate_week.py`/pytest
   - Git：为什么版本控制；`init/status/add/commit/log`；本地 vs 远端（概念）
   - PR：预告（可选）
2. Week 02：控制流：让程序做选择
   - Python：if/for/while、基本逻辑
   - Agentic：用“输入输出例子表”约束实现；先写失败用例再修
   - Git：`diff`；提交信息最小规范（动词开头 + 说明变更）
   - PR：可选
3. Week 03：函数：把问题切小
   - Python：函数/参数/返回、作用域（基础）
   - Agentic：把需求拆成 3-5 个函数并自审
   - Git：分支入门：`switch -c`/`checkout -b`；合并概念
   - PR：可选
4. Week 04：数据结构 I：列表与字典
   - Python：list/dict、遍历、常见模式
   - Agentic：先画数据结构（输入/中间态/输出）再写代码
   - Git：撤销（安全场景）：`restore`、`reset --soft`
   - PR：可选
5. Week 05：文件与路径：把程序变成小工具
   - Python：读写文件、pathlib、简单日志（可选）
   - Agentic：错误场景清单（空文件/不存在/权限/编码）并给出处理策略
   - Git：远端 Gitea：`remote add origin`、`push -u`、`pull`
   - PR：可选（推荐开始使用）
6. Week 06：异常与输入校验：让程序不崩
   - Python：try/except、抛错 vs 返回值、输入校验
   - Agentic：失败优先（先定义错误信息/边界，再实现）
   - Git/PR：固定交付流程开始（必做）：分支 -> 多次提交 -> push -> PR -> review -> merge
7. Week 07：模块与项目结构：从脚本到小项目
   - Python：import、模块拆分、简单包结构
   - Agentic：重构计划（不改变行为的前提下整理结构）
   - Git/PR：延续 Week 06（必做）
8. Week 08：pytest：让正确变成自动化
   - Python：pytest 基础断言、少量 fixture（够用即可）
   - Agentic：TDD 小步（写一个失败测试 -> 最小实现 -> 通过）
   - Git/PR：延续 Week 06（必做）
9. Week 09：字符串处理与正则：文本小技能
   - Python：字符串 API、基础正则、常见陷阱
   - Agentic：边界用例（空输入/特殊字符/多行）驱动修复
   - Git/PR：延续 Week 06（必做）
10. Week 10：数据格式：JSON/YAML/（可选 CSV）
   - Python：序列化/反序列化、配置文件、数据契约
   - Agentic：把“输入格式”写成可验证契约（tests/anchors）
   - Git/PR：延续 Week 06（必做）
11. Week 11：轻量 OOP：dataclass 与封装
   - Python：`dataclasses`、最小封装/状态管理（不走重 OOP）
   - Agentic：先定接口再实现（把变化隔离在边界）
   - Git/PR：延续 Week 06（必做）
12. Week 12：命令行工具：argparse + logging（基础）
   - Python：子命令、参数设计、退出码、日志
   - Agentic：发布清单（可运行、可验证、可回滚）
   - Git/PR：延续 Week 06（必做）
13. Week 13：Agentic 团队工作流（Claude Code 专项）
   - 主题：subagents/skills/hooks、review checklist、失败驱动迭代
   - Git/PR：review 作为 DoD 的一部分（必做）
14. Week 14：Capstone：学习助手/作业检查器 v1（收敛发布）
   - 主题：把前 13 周技能收敛成可交付项目；补测试、补文档、写 release notes
   - Git/PR：最终 PR + 可选 tag 发布

