# Week 13 交付物清单

本周 starter 是文档型起步包：它不提供完整后端实现，而是帮助学生补齐 API 文档、项目 README 和 ADR 索引。

## Maven 验证命令

```bash
cd chapters/week_13/starter_code
mvn test
```

## 当前 starter 包含

- `pom.xml`：Java 21 + JUnit 5 smoke test 配置
- `openapi.yaml`：OpenAPI 文档模板，保留可填写的 TODO/待办项
- `PROJECT_README_TODO.md`：项目 README 模板
- `docs/ADR_INDEX.md`：ADR 汇总模板
- `docs/ADR/001-domain-model.md`：单篇 ADR 模板
- `src/test/java/com/campusflow/Week13StarterSmokeTest.java`：确认起步包结构完整
- `TEST_MATRIX.md` / `TEST_SUMMARY.md` / `TEST_CASES.md`：文档自测提示

## 学生最终交付

- [ ] `openapi.yaml`：与真实 CampusFlow API 一致
- [ ] `README.md`：从 `PROJECT_README_TODO.md` 改写而来，包含 5 分钟快速开始
- [ ] `docs/ADR_INDEX.md`：汇总已有架构决策
- [ ] `docs/REVIEW_REPORT.md`：记录 AI 生成文档的人工审查

## 版本信息

- **Week**: 13
- **主题**: 技术文档与知识管理
- **Java**: 21
- **Maven**: 3.9+
