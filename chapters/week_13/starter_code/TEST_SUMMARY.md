# Week 13 测试用例矩阵总结

## 项目结构

```
chapters/week_13/starter_code/
├── pom.xml                                   # Maven 配置
├── README.md                                  # 项目说明（示例文档）
├── TEST_MATRIX.md                             # 测试用例矩阵
├── src/
│   ├── main/
│   │   └── java/com/campusflow/
│   │       ├── App.java                       # 主应用类，包含 API 文档端点
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── model/
│   │       ├── dto/
│   │       └── exception/
│   └── test/
│       └── java/com/campusflow/
│           ├── AppTest.java                  # 基础测试
│           ├── ApiDocumentationTest.java      # API 文档测试
│           ├── DocumentationQualityTest.java  # 文档质量测试
│           └── TaskApiTest.java              # API 功能测试（从 week_12 复制）
└── docs/
    └── ADR/
        ├── INDEX.md                          # ADR 索引（示例）
        └── 001-domain-model.md              # ADR 示例
```

## 测试类概览

### 1. ApiDocumentationTest（API 文档测试）

**测试目标**：验证 OpenAPI 规范的正确性

| 测试数量 | 正例 | 边界 | 反例 | 契约 |
|---------|------|------|------|------|
| 15 | 6 | 2 | 2 | 3 |

**关键测试**：
- `getApiDocs_ShouldReturnValidOpenApiSpec()` - 验证文档端点返回有效的 OpenAPI JSON
- `openApiSpec_ShouldContainAllRequiredPaths()` - 验证文档包含所有必需路径
- `taskSchema_ShouldContainAllRequiredFields()` - 验证 Task Schema 字段完整性
- `contractTest_PathsInDocs_ShouldBeAccessible()` - 契约测试：文档路径与实际 API 一致

### 2. DocumentationQualityTest（文档质量测试）

**测试目标**：验证项目文档的完整性和质量

| 测试数量 | 正例 | 边界 | 反例 | Docs-as-Code |
|---------|------|------|------|--------------|
| 16 | 7 | 3 | 2 | 4 |

**关键测试**：
- `readme_ShouldExist()` - 验证 README.md 存在
- `readme_ShouldContainQuickStartSection()` - 验证快速开始章节
- `adrDirectory_ShouldContainADRFiles()` - 验证 ADR 目录存在且包含文件
- `readme_ShouldNotContainPlaceholders()` - 验证没有未替换的占位符

### 3. AppTest（基础测试）

**测试目标**：验证应用可以正常启动

| 测试数量 | 正例 |
|---------|------|
| 2 | 2 |

## 测试覆盖范围

### 正例测试
- API 文档端点可访问性
- 文档内容完整性（info、paths、schemas）
- README 存在性和必需章节
- ADR 文件存在性和格式

### 边界测试
- 文档响应时间（< 1 秒）
- 文档大小（1KB - 100KB）
- README 大小（100 - 100KB）
- README 包含可运行命令

### 反例测试
- 不存在的文档端点应返回 404
- 文档不应暴露敏感信息
- README 不应包含占位符
- ADR 文件命名规范

### 契约测试
- 文档中的路径应实际可用
- 实际 API 响应符合文档 Schema
- 错误响应格式一致性

## 运行测试

```bash
# 进入项目目录
cd chapters/week_13/starter_code

# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ApiDocumentationTest
mvn test -Dtest=DocumentationQualityTest
mvn test -Dtest=AppTest

# 运行测试并生成覆盖率报告
mvn test jacoco:report

# 查看覆盖率报告
open target/site/jacoco/index.html
```

## 测试命名规范

遵循 `method_condition_expected()` 格式：
- `getApiDocs_WhenCalled_ShouldReturn200()`
- `readme_WhenMissing_ShouldFail()`
- `taskSchema_ShouldContainAllRequiredFields()`

## 示例文档

本 starter_code 包含以下示例文档供参考：

1. **README.md** - 项目说明文档
   - 快速开始（5 分钟）
   - 功能特性
   - 使用指南
   - 常见问题

2. **docs/ADR/INDEX.md** - ADR 索引
   - ADR 列表
   - 决策摘要
   - 决策依赖图

3. **docs/ADR/001-domain-model.md** - ADR 示例
   - 状态、背景、决策、理由、后果

## Week 13 特色

1. **API 文档端点**
   - `GET /api-docs` - 返回 OpenAPI JSON
   - `GET /api-docs.yaml` - 返回 OpenAPI YAML

2. **文档验证测试**
   - 验证文档格式正确性
   - 验证文档与实现一致性（契约测试）

3. **Docs-as-Code**
   - 文档与代码同仓库
   - 使用 Markdown 格式
   - 版本控制同步

## 下一步

- 学生需要补充实际的 ADR 文件
- 学生需要完善 README（添加项目特定信息）
- 学生需要根据实际 API 更新 OpenAPI 规范
- 学生可以添加更多文档质量检查测试
