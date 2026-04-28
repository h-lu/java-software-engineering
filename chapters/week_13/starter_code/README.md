# Week 13 Starter Code：文档与知识传递

这个目录是学生起步包，不是答案。你需要把 TODO 模板改成自己 CampusFlow 项目的文档。

## 本周任务

完成 Week 13 作业中的三类交付物：

- `openapi.yaml`：描述 CampusFlow REST API 的契约。
- `PROJECT_README_TODO.md`：改写为你的项目 README，可以完成后复制到项目根目录 `README.md`。
- `docs/ADR_INDEX.md` 与 `docs/ADR/*.md`：汇总架构决策，说明每个选择的背景、理由、后果。

## 运行检查

需要 Java 21 和 Maven 3.9+。

```bash
mvn test
```

这个 smoke test 只检查起步包结构和 TODO 标记是否存在，不会替你判断文档质量。完成作业后，你还需要按 `ASSIGNMENT.md` 和 `RUBRIC.md` 自查。

## 建议步骤

1. 先阅读 `../ASSIGNMENT.md` 和 `../examples/`。
2. 根据你自己的 CampusFlow API 修改 `openapi.yaml`。
3. 把 `PROJECT_README_TODO.md` 改成真实的 5 分钟快速开始文档。
4. 补全 `docs/ADR_INDEX.md`，并把已有 ADR 逐条连接起来。
5. 用 AI 生成内容时，逐条核对端口、路径、字段名、版本号，删除不真实的内容。

## 不要提交的内容

- 不要把示例里的完整答案原样复制为你的交付物。
- 不要编造不存在的端点、配置项、部署地址或统计数据。
- 不要把 API 密钥、数据库密码等敏感信息写进 README 或 OpenAPI 示例。
