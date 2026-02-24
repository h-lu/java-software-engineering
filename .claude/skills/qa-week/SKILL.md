---
name: qa-week
description: 质量审查：术语/锚点/格式/角色一致性 + 双视角 QA（技术正确性 + 四维叙事评分）；输出并收敛 QA_REPORT。
argument-hint: "<week_id e.g. week_01>"
allowed-tools: Bash, Read, Write, Edit, Grep, Glob
disable-model-invocation: true
---

# /qa-week

## 用法

```
/qa-week week_XX
```

## 步骤

1. 调用 `consistency-editor`：
   - 对齐 `shared/style_guide.md`
   - 同步 `TERMS.yml` -> `shared/glossary.yml`
   - 检查循环角色使用一致性（对照 `shared/characters.yml`）
   - 修复 `ANCHORS.yml` 问题（依赖 `validate_week.py` 报错定位，不手动逐条检查）
   - consistency-editor 内部会跑 `validate_week.py --mode drafting`，基础检查已覆盖
2. 调用 `technical-reviewer`：技术正确性审读（专家视角）。
   - 检查概念定义准确性、代码可编译/可运行、Java 最佳实践
   - 检查教学法对齐（目标对齐、递进合理、示例-练习对应）
   - 检查练习题质量（题意清晰、可评分性、难度梯度）
   - 输出 S1-S4 四级严重度问题清单
   - **S1-S4 所有问题必须修复后才能 release**
3. 调用 `student-qa`：四维评分审读 + 阻塞项/建议项（学生视角）。
   - 必须输出四维评分（叙事流畅度/趣味性/知识覆盖/认知负荷）
   - 总分 >= 18/20 才能 release
4. 把 QA 结果收敛到 `QA_REPORT.md`：
   - student-qa 四维评分写在顶部
   - technical-reviewer 的 S1-S4 所有问题放到 `## 技术阻塞项` 下（checkbox，必须全部勾选）
   - student-qa 的阻塞项放到 `## 叙事阻塞项` 下（checkbox）
   - 阻塞项清零（不允许存在 `- [ ]`）
5. 最终 release 级验证（含 ANCHORS.yml、CampusFlow、角色、概念预算检查）：
   ```bash
   python3 scripts/validate_week.py --week week_XX --mode release
   ```
