# Week 13 示例文件索引

本目录包含 Week 13（文档与知识传递）的所有示例文件。

## 文件列表

| 文件名 | 类型 | 说明 |
|--------|------|------|
| `13_openapi_spec.yaml` | OpenAPI 规范 | CampusFlow 任务的完整 API 文档示例 |
| `13_README.md` | README 文档 | 优秀 README 的结构和内容示例 |
| `13_adr_index.md` | ADR 索引 | 架构决策汇总和索引示例 |
| `13_examples_README.md` | 说明文档 | 本目录的使用说明和验证清单 |
| `13_documentation_comparison.java` | 对比示例 | 坏/好文档对比（教学用途） |

## 学习路径

### 第 1 步：理解 README 的倒金字塔原则

阅读 `13_README.md`，注意：
- 快速开始在最前面（用户最关心）
- 所有命令可以复制粘贴运行
- 有常见问题（FAQ）章节

### 第 2 步：学习 OpenAPI 规范

阅读 `13_openapi_spec.yaml`，注意：
- `info` 部分：API 基本信息
- `paths` 部分：所有端点定义
- `components/schemas` 部分：数据模型
- `description` 字段：业务约束说明

### 第 3 步：理解 ADR 汇总

阅读 `13_adr_index.md`，注意：
- ADR 列表表格
- 决策摘要（决策、理由、后果）
- 决策依赖图
- 待决策事项

### 第 4 步：对比好坏例子

阅读 `13_documentation_comparison.java`，对比：
- 坏 README vs 好 README
- 坏 OpenAPI vs 好 OpenAPI
- 坏 ADR vs 好 ADR

## 如何使用这些示例

### 方式 1：复制结构

```bash
# 1. 复制 README 结构
cp 13_README.md ~/my-project/README.md

# 2. 编辑内容，替换成你自己的项目
vim ~/my-project/README.md
```

### 方式 2：参考写作

参考示例的写作风格：
- README：用户视角，倒金字塔原则
- OpenAPI：业务约束写在 description 中
- ADR：有背景、理由、后果

### 方式 3：审查 AI 生成的内容

使用 `13_examples_README.md` 中的验证清单：
1. 用 AI 生成文档骨架
2. 使用验证清单检查质量
3. 修正 AI 的"幻觉"和错误

## 验证清单

### README 验证

- [ ] 快速开始在前 10 行内
- [ ] 所有命令可以复制粘贴运行
- [ ] 有常见问题（FAQ）章节
- [ ] 用户能在 5 分钟内跑起来

### OpenAPI 验证

- [ ] 所有端点都有文档
- [ ] 请求/响应 schema 定义完整
- [ ] 业务约束写在 description 中
- [ ] 错误码有说明

### ADR 验证

- [ ] 每个决策有"背景"、"理由"、"后果"
- [ ] 有决策依赖图
- [ ] 有待决策事项
- [ ] 决策理由清晰，不是"凭感觉"

## 相关资源

- [OpenAPI Specification 3.1](https://spec.openapis.org/oas/v3.1.0)
- [Awesome README](https://github.com/matiassingers/awesome-readme)
- [ADR 模板](https://adr.github.io/)
- [Writing READMEs - Art of Readme](https://www.artofreadme.com/)

---

**对应章节**：Week 13 - 文档与知识传递
**更新日期**：2026-02-12
