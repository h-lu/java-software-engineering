# Week 03 QA 报告

生成日期：2026-02-24

---

## 四维评分

| 维度 | 分数 | 说明 |
|------|------|------|
| **叙事流畅度** | 4/5 | 结构清晰、过渡自然，AI 小专栏有轻微模板感 |
| **趣味性** | 5/5 | 多个"哦！"时刻，角色互动推动叙事，开头悬念强 |
| **知识覆盖** | 5/5 | 知识点全面；AI 小专栏链接已通过 `.research_cache.md` 验证 |
| **认知负荷** | 4/5 | 概念递进合理，回顾桥有效，第 3 节略有堆叠 |
| **总分** | **18/20** | ✅ 通过 |

---

## 阻塞项

无。

---

## 技术阻塞项（S1）

无 S1 致命问题。

---

## 叙事阻塞项

无。

- [x] AI 小专栏链接真实性：已通过 `.research_cache.md` 验证记录确认

---

## 已修复问题（S2-S3）

### S2-1：setDueDate 未 trim 输入 ✅ 已修复
- **文件**：`examples/04_defensive_programming.java`
- **修复**：添加 `trim()` 与其他 setter 保持一致

### S2-2：NumberFormatException 泄漏 ✅ 已修复
- **文件**：`starter_code/solution.java`
- **修复**：用 try-catch 包装 `Integer.parseInt` 并转换为 `InvalidTaskDataException`

### S2-3：CHAPTER.md 代码与 examples 不一致 ✅ 已修复
- **文件**：`CHAPTER.md`
- **修复**：添加注释说明"完整可运行代码见 examples/01_crash_demo.java"

### S2-4：DuplicateTaskException 设计说明不清 ✅ 已修复
- **文件**：`ASSIGNMENT.md`
- **修复**：添加设计建议，明确与 `InvalidTaskDataException` 的区别

### S3-2：createTempFile 捕获过宽 ✅ 已修复
- **文件**：`examples/02_try_catch_basic.java`
- **修复**：改为捕获 `IOException` 并添加说明注释

### S3-3：ANCHORS.yml W03-006 引用文件不存在 ✅ 已修复
- **文件**：`ANCHORS.yml`
- **修复**：更新 verification 指向 ASSIGNMENT.md 任务 4

---

## 未修复项（S4 润色建议）

### S4：NumberFormatException 段落位置
- **建议**：将"一个反直觉的事实"段落移至第 4 节
- **决策**：当前段落位于第 3 节结尾（529-544 行），已自然过渡到第 4 节防御式编程，无需移动
- **理由**：当前位置作为"为什么需要防御式编程"的论据，承接流畅

---

## 角色使用检查

| 角色 | 出场次数 | 人设符合度 | 推动叙事 |
|------|---------|-----------|---------|
| 小北 | 4次 | ✅ 符合 | ✅ 犯错/困惑引出概念 |
| 阿码 | 3次 | ✅ 符合 | ✅ 追问边界和哲学 |
| 老潘 | 5次 | ✅ 符合 | ✅ 工程视角、真实故事 |

---

## 知识锚点验证

| 锚点ID | 验证状态 |
|--------|---------|
| W03-001 | ✅ |
| W03-002 | ✅ |
| W03-003 | ✅ |
| W03-004 | ✅ |
| W03-005 | ✅ |
| W03-006 | ✅ 已更新 |
| W03-007 | ✅ |
| W03-008 | ✅ |
| W03-009 | ✅ |

---

## 术语同步检查

本周新概念（TERMS.yml）：
1. 受检异常 (checked exception)
2. 运行时异常 (runtime exception)
3. 异常传播 (exception propagation)
4. 防御式编程 (defensive programming)
5. FMEA (Failure Mode and Effects Analysis)
6. 尽早失败 (fail fast)
7. 优雅降级 (graceful degradation)

**同步状态**：已合入 shared/glossary.yml

---

## 验证结果

```
[validate-week] OK: week_03 (mode=drafting)
```

---

## 最终结论

**状态**：✅ **通过**

**总分**：18/20（达到 18/20 通过线）

**S1 阻塞项**：0 个

**叙事阻塞项**：0 个

**已修复**：S2-1, S2-2, S2-3, S2-4, S3-2, S3-3

**可发布**：是

---

*报告生成时间：2026-02-24*
*审读执行：consistency-editor, technical-reviewer, student-qa*
*修复执行：Lead agent*
