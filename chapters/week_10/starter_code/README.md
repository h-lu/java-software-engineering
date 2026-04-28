# Week 10 Starter Code：AI Frontend + CORS

这个 starter 支持 Week 10 作业：用 AI 生成 CampusFlow frontend，人工审查、修复，再通过 CORS 接入 backend。

它不是完成版 frontend，也不是完成版 API。Java server 只用来证明 Maven 和 Javalin wiring 能跑通。你的主要工作在 Markdown 报告和 `frontend/` 目录下的文件里。

## 运行命令

```bash
mvn test
mvn compile
java -cp target/classes com.campusflow.App
curl http://localhost:7070/health
```

手动测试 frontend 时，先启动你的 Week 09 backend，或扩展这个 starter，然后在浏览器中打开 `frontend/index.html`。

## 你需要编辑的文件

- `PROMPT.md`：写下生成第一版 frontend 使用的 prompt。
- `AI_TOOL.md`：记录 AI tool 和 model version。
- `frontend/ai_generated.html`：保存未经修改的 AI 原始输出。
- `REVIEW.md`：完成 checklist，至少记录三类问题。
- `frontend/index.html`：实现你审查并修复后的 frontend。
- `pom.xml`：如果使用这个 starter backend 完成 CORS 任务，添加 Javalin。
- `src/main/java/com/campusflow/App.java`：把占位 server 替换为浏览器调用需要的 CORS 配置。

## 待办清单

- [ ] 写一个包含 role、task、constraints、output format 的 prompt。
- [ ] 保存未经修改的 AI generated frontend。
- [ ] 审查 XSS、empty states、loading/error states、hallucinated APIs。
- [ ] 修复 frontend，处理用户内容时不要使用不安全的 `innerHTML`。
- [ ] 至少增加两个 UX improvements。
- [ ] 配置并验证 demo 使用的 backend CORS。
- [ ] 按提交要求保存截图和说明。

smoke test 只检查 starter wiring。完成实现后，请增加浏览器或 API 测试。
