# Week 13 JUnit 5 测试用例矩阵

## 测试分类总览

| 测试类 | 测试数量 | 正例 | 边界 | 反例 | 契约 | 其他 |
|--------|---------|------|------|------|------|------|
| ApiDocumentationTest | 15 | 6 | 2 | 2 | 3 | 2 |
| DocumentationQualityTest | 16 | 7 | 3 | 2 | - | 4 |
| AppTest | 2 | 2 | - | - | - | - |
| **总计** | **33** | **15** | **5** | **4** | **3** | **6** |

## 详细测试矩阵

### 1. ApiDocumentationTest - API 文档测试

| ID | 测试方法 | 类型 | 验证内容 |
|----|---------|------|----------|
| AD-01 | getApiDocs_ShouldReturnValidOpenApiSpec | 正例 | 返回有效 OpenAPI JSON，包含必需字段 |
| AD-02 | getApiDocsYaml_ShouldReturnValidYamlSpec | 正例 | 返回有效 OpenAPI YAML |
| AD-03 | openApiSpec_ShouldContainAllRequiredPaths | 正例 | 包含 6+ 个核心路径 |
| AD-04 | openApiSpec_ShouldContainSchemaDefinitions | 正例 | 包含 Task/TaskRequest/Error/HealthResponse |
| AD-05 | taskSchema_ShouldContainAllRequiredFields | 正例 | Task Schema 包含 id/title/status，title 有约束 |
| AD-06 | taskRequestSchema_ShouldMarkRequiredFields | 正例 | TaskRequest 标记 title 为 required |
| AD-10 | apiDocs_ResponseTime_ShouldBeReasonable | 边界 | 响应时间 < 1 秒 |
| AD-11 | apiDocs_Size_ShouldBeReasonable | 边界 | 文档大小 1KB - 100KB |
| AD-20 | contractTest_PathsInDocs_ShouldBeAccessible | 契约 | 文档中的路径实际可用 |
| AD-21 | contractTest_ActualApi_ShouldMatchDocSchema | 契约 | 实际 API 响应符合文档 Schema |
| AD-22 | contractTest_ErrorResponse_ShouldMatchErrorSchema | 契约 | 错误响应包含 code/message/timestamp |
| AD-30 | getNonExistentDocs_ShouldReturn404 | 反例 | 不存在的文档端点返回 404 |
| AD-31 | apiDocs_ShouldNotExposeSensitiveInfo | 反例 | 不包含 password/secret/token |

### 2. DocumentationQualityTest - 文档质量测试

| ID | 测试方法 | 类型 | 验证内容 |
|----|---------|------|----------|
| DQ-01 | readme_ShouldExist | 正例 | README.md 文件存在 |
| DQ-02 | readme_ShouldContainProjectTitle | 正例 | 包含项目标题 |
| DQ-03 | readme_ShouldContainQuickStartSection | 正例 | 包含快速开始章节 |
| DQ-04 | readme_ShouldContainInstallationInstructions | 正例 | 包含安装/运行说明 |
| DQ-05 | readme_ShouldContainFAQSection | 正例 | 包含常见问题章节 |
| DQ-06 | adrDirectory_ShouldExist | 正例 | docs/ADR 目录存在 |
| DQ-07 | adrDirectory_ShouldContainADRFiles | 正例 | ADR 目录包含至少 1 个文件 |
| DQ-10 | readme_Size_ShouldBeReasonable | 边界 | README 大小 100 - 100KB |
| DQ-11 | readme_ShouldContainRunnableCommands | 边界 | 包含代码块或命令示例 |
| DQ-12 | readme_ShouldContainVersionInfo | 边界 | 包含版本信息 |
| DQ-20 | readme_ShouldNotContainPlaceholders | 反例 | 不包含 [TODO]/[Your Name] 等占位符 |
| DQ-21 | adr_Files_ShouldFollowNamingConvention | 反例 | ADR 文件命名格式：###-title.md |
| DQ-22 | adr_Files_ShouldContainRequiredSections | 反例 | ADR 包含 status/context/decision |

### 3. AppTest - 基础测试

| ID | 测试方法 | 类型 | 验证内容 |
|----|---------|------|----------|
| BA-01 | testAppExists | 正例 | 应用类可加载 |
| BA-02 | testBasicAssertion | 正例 | 基础断言测试 |

## 测试场景覆盖

### 场景 1：API 文档生成
```
用户访问 /api-docs
  ↓
返回有效的 OpenAPI JSON 规范
  ↓
包含 info、paths、components
  ↓
所有 Schema 定义完整
```

### 场景 2：文档与实现一致性
```
文档定义 API 路径
  ↓
测试每个路径是否可访问
  ↓
验证响应格式与 Schema 一致
  ↓
验证错误响应格式一致
```

### 场景 3：文档完整性检查
```
检查 README.md 存在
  ↓
验证包含快速开始、FAQ 等章节
  ↓
验证 ADR 目录存在
  ↓
验证 ADR 文件格式正确
```

## 测试命名模式

| 模式 | 示例 | 使用场景 |
|------|------|----------|
| `method_ShouldDoSomething()` | `readme_ShouldExist()` | 验证应该发生的正例 |
| `method_WhenCondition_ShouldDoSomething()` | `getApiDocs_WhenCalled_ShouldReturn200()` | 条件 + 预期结果 |
| `method_ShouldContainRequired()` | `taskSchema_ShouldContainAllRequiredFields()` | 验证必需内容 |
| `contractTest_Subject_ShouldDoSomething()` | `contractTest_PathsInDocs_ShouldBeAccessible()` | 契约测试 |

## 运行命令速查

```bash
# 所有测试
mvn test

# 特定类
mvn test -Dtest=ApiDocumentationTest
mvn test -Dtest=DocumentationQualityTest

# 特定方法
mvn test -Dtest=ApiDocumentationTest#getApiDocs_ShouldReturnValidOpenApiSpec

# 覆盖率
mvn test jacoco:report
open target/site/jacoco/index.html
```
