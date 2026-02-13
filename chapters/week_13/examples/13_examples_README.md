# Week 13 示例文档说明

本目录包含 Week 13（文档与知识传递）的示例文件。

## 文件列表

### 1. 13_openapi_spec.yaml

**用途**：展示 OpenAPI 3.0.1 规范的完整示例

**内容**：
- CampusFlow 任务的 REST API 文档
- 包含所有端点（GET、POST、PUT、PATCH、DELETE）
- 包含请求/响应的 schema 定义
- 包含错误码和业务约束说明

**如何使用**：
1. 访问 https://editor.swagger.io/
2. 复制 `13_openapi_spec.yaml` 的内容到编辑器
3. 查看渲染后的交互式 API 文档

**关键点**：
- `info` 部分：描述 API 的基本信息（名称、版本、描述）
- `servers` 部分：定义服务器地址
- `paths` 部分：定义所有 API 端点
- `components/schemas` 部分：定义数据模型
- 每个端点的 `description`：说明业务约束（如标题最大 100 字符）

---

### 2. 13_README.md

**用途**：展示优秀 README 的结构和内容

**内容**：
- 快速开始（5 分钟运行指南）
- 功能特性介绍
- API 使用示例
- 开发指南（测试、覆盖率、静态分析）
- 常见问题（FAQ）

**关键点**：
- **倒金字塔原则**：最重要的内容（快速开始）在最前面
- **代码块可复制**：所有命令都是完整的，可以直接复制粘贴运行
- **用户视角**：回答用户最关心的问题（"我能 5 分钟跑起来吗？"）

**对比**：

| 好的 README | 不好的 README |
|-------------|---------------|
| 快速开始在最前面 | 从项目历史开始 |
| 给完整的命令行示例 | 只说"运行 mvn test" |
| 有常见问题章节 | 没有 FAQ |
| 用户能 5 分钟跑起来 | 用户不知道从哪开始 |

---

### 3. 13_adr_index.md

**用途**：展示架构决策索引的格式和内容

**内容**：
- ADR 列表表格（标题、日期、状态、影响）
- 每个决策的摘要（决策、理由、后果）
- 决策依赖图
- 待决策事项

**关键点**：
- **索引和摘要**：让读者不用翻每个文件，就能看全所有决策
- **依赖关系**：展示决策之间的"涟漪效应"
- **待决策事项**：列出未来可能需要决策的问题

**ADR 的价值**：
- 代码告诉你"做了什么"
- ADR 告诉你"为什么这样做"
- 人会忘，但 ADR 能保留设计意图

---

## 如何使用这些示例

### 学习顺序

1. **先看 13_README.md**：理解用户视角的文档写作
2. **再看 13_openapi_spec.yaml**：学习 API 文档规范
3. **最后看 13_adr_index.md**：理解架构决策管理

### 实践建议

1. **复制示例结构**：不要复制具体内容，而是复制结构
2. **填入你自己的内容**：把 CampusFlow 的内容替换成你自己的项目
3. **审查 AI 生成的内容**：用 AI 生成框架，人工补充业务逻辑
4. **持续更新**：文档和代码一起演进（Docs-as-Code）

---

## 常见错误

### ❌ 错误 1：README 写成技术文档

```markdown
# My Project

## 技术栈
- Java 17
- Spring Boot
- PostgreSQL

## 架构设计
（复杂的技术架构图）
```

**问题**：用户只想知道"怎么跑起来"，不关心技术栈。

**修正**：把"快速开始"放在最前面。

---

### ❌ 错误 2：OpenAPI 文档没有业务约束

```yaml
paths:
  /tasks:
    post:
      summary: 创建任务
      requestBody:
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/TaskInput'
```

**问题**：没有说明"标题最大 100 字符"这个业务约束。

**修正**：在 `description` 中添加业务规则说明。

---

### ❌ 错误 3：ADR 只有决策，没有理由

```markdown
# ADR-001: 数据存储

## 决策
使用 PostgreSQL。
```

**问题**：读者不知道为什么选择 PostgreSQL。

**修正**：添加"背景"、"理由"、"后果"章节。

---

## 验证清单

使用以下清单验证你的文档质量：

### README 验证清单

- [ ] 快速开始在前 10 行内
- [ ] 所有命令可以复制粘贴运行
- [ ] 有常见问题（FAQ）章节
- [ ] 户能在 5 分钟内跑起来

### OpenAPI 验证清单

- [ ] 所有端点都有文档
- [ ] 请求/响应 schema 定义完整
- [ ] 业务约束写在 `description` 中
- [ ] 错误码有说明

### ADR 验证清单

- [ ] 每个决策有"背景"、"理由"、"后果"
- [ ] 有决策依赖图
- [ ] 有待决策事项
- [ ] 决策理由清晰，不是"凭感觉"

---

## 参考资料

- [OpenAPI Specification 3.1](https://spec.openapis.org/oas/v3.1.0)
- [Awesome README](https://github.com/matiassingers/awesome-readme)
- [ADR 模板](https://adr.github.io/)
- [Writing READMEs - Art of Readme](https://www.artofreadme.com/)

---

**更新日期**：2026-02-12
**对应章节**：Week 13 - 文档与知识传递
