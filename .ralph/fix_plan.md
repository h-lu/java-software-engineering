# Ralph Fix Plan - Java 软件工程教材 QA 批量处理

## High Priority
- [x] week_06: 重新 QA 审核并修复 S1-S4 问题
- [ ] week_07: 重新 QA 审核并修复 S1-S4 问题
- [ ] week_08: 重新 QA 审核并修复 S1-S4 问题
- [ ] week_09: 重新 QA 审核并修复 S1-S4 问题
- [ ] week_10: 重新 QA 审核并修复 S1-S4 问题

## Medium Priority
- [ ] week_11: 重新 QA 审核并修复 S1-S4 问题
- [ ] week_12: 重新 QA 审核并修复 S1-S4 问题
- [ ] week_13: 重新 QA 审核并修复 S1-S4 问题
- [ ] week_14: 重新 QA 审核并修复 S1-S4 问题

## Low Priority
- [ ] week_15: 重新 QA 审核并修复 S1-S4 问题
- [ ] week_16: 重新 QA 审核并修复 S1-S4 问题

## Completed
- [x] Ralph 初始化与配置
- [x] 项目环境设置
- [x] week_06: 重新 QA 审核并修复 S1-S4 问题

## Notes
- 每次只处理一个 week，禁止并发
- 必须调用 /qa-week 进行全新审核
- 忽略现有 QA_REPORT.md，重新生成
- 修复所有 S1-S4 问题（不只是 S1-S2）
- 每个 week 修复后必须验证通过 validate_week.py --mode release
- 每个 week 完成后要 Git 提交
