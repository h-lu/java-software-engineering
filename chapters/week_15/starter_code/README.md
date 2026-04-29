# Week 15 Starter Code：项目集市与展示准备

Week 15 主要交付展示材料，不要求新增 CampusFlow 功能代码。这个 starter_code 提供模板和一个小检查器，帮助你整理展示脚本、PPT/海报大纲、问答准备和演练反馈。

## 运行命令

需要 Java 21 和 Maven 3.9+。

```bash
cd chapters/week_15/starter_code
mvn test
mvn -q compile
java -cp target/classes com.campusflow.ShowcaseReadinessCheck
```

## 需要编辑的文件

- `presentation_script.md`：TODO 写 8-10 分钟演示脚本关键点。
- `slides_outline.md`：TODO 规划 12-15 页 PPT；它是准备材料，不替代最终提交的 `slides.pptx` 或 `slides.html`。
- `poster_checklist.md`：TODO 检查 A1 海报信息层级和二维码；它是检查清单，不替代最终提交的 `poster.pdf` 或 `poster.html`。
- `qa_prep.md`：TODO 准备 10-15 个问答。
- `showcase_practice.md`：可选加分，记录跨组演练反馈；未参加时保留文件并说明未参加即可。
- `ai_ppt_review.md`：可选加分，如果使用 AI 生成 PPT 草稿，记录人工审查。
- `prepare_demo_data.sh`：可选演示辅助脚本，不作为评分提交物；如果使用，请按你真实 API 修改。

## 最终提交物映射

- `presentation_script.md` -> 作业提交的演示脚本。
- `slides_outline.md` -> 生成 `slides.pptx` 或 `slides.html`。
- `poster_checklist.md` -> 生成 `poster.pdf` 或 `poster.html`。
- `qa_prep.md` -> 作业提交的问答准备。
- `showcase_practice.md`、`ai_ppt_review.md` -> 只在选择进阶/AI 协作练习时提交。

## 使用建议

1. 先完成脚本，再做 PPT。PPT 只承载脚本中的关键证据。
2. 演示数据要真实、可复现，避免现场临时造数据。
3. Q&A 回答遵循：直接回答、解释原因、补充边界。
4. 所有二维码和部署链接在展示前一天、展示当天各验证一次。
