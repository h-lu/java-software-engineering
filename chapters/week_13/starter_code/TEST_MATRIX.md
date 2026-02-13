# Week 13 测试用例矩阵

## 概述

本文档定义了 Week 13 "文档与知识传递" 的 JUnit 5 测试用例矩阵。

## 测试分类

### 1. API 文档测试（ApiDocumentationTest）

| 测试 ID | 测试名称 | 类型 | 描述 | 预期结果 |
|---------|---------|------|------|----------|
| AD-01 | getApiDocs_ShouldReturnValidOpenApiSpec | 正例 | GET /api-docs 返回有效的 OpenAPI JSON | HTTP 200，包含 openapi/info/paths 字段 |
| AD-02 | getApiDocsYaml_ShouldReturnValidYamlSpec | 正例 | GET /api-docs.yaml 返回有效的 YAML | HTTP 200，Content-Type 为 YAML |
| AD-03 | openApiSpec_ShouldContainAllRequiredPaths | 正例 | 文档包含所有必需路径 | 包含 6+ 个核心路径 |
| AD-04 | openApiSpec_ShouldContainSchemaDefinitions | 正例 | 文档包含 Schema 定义 | 包含 Task/TaskRequest/Error/HealthResponse |
| AD-05 | taskSchema_ShouldContainAllRequiredFields | 正例 | Task Schema 包含所有必需字段 | 包含 id/title/status，title 有约束 |
| AD-06 | taskRequestSchema_ShouldMarkRequiredFields | 正例 | TaskRequest 标记必填字段 | title 标记为 required |
| AD-10 | apiDocs_ResponseTime_ShouldBeReasonable | 边界 | 文档响应时间检查 | < 1 秒 |
| AD-11 | apiDocs_Size_ShouldBeReasonable | 边界 | 文档大小检查 | 1KB - 100KB |
| AD-20 | contractTest_PathsInDocs_ShouldBeAccessible | 契约 | 文档中的路径实际可用 | 所有路径可访问 |
| AD-21 | contractTest_ActualApi_ShouldMatchDocSchema | 契约 | 实际 API 响应符合文档 Schema | 响应字段与文档一致 |
| AD-22 | contractTest_ErrorResponse_ShouldMatchErrorSchema | 契约 | 错误响应符合 Error Schema | 包含 code/message/timestamp |
| AD-30 | getNonExistentDocs_ShouldReturn404 | 反例 | 不存在的文档端点 | HTTP 404 |
| AD-31 | apiDocs_ShouldNotExposeSensitiveInfo | 反例 | 文档不暴露敏感信息 | 不包含 password/secret/token |

### 2. 文档质量测试（DocumentationQualityTest）

| 测试 ID | 测试名称 | 类型 | 描述 | 预期结果 |
|---------|---------|------|------|----------|
| DQ-01 | readme_ShouldExist | 正例 | README.md 文件存在 | 文件存在 |
| DQ-02 | readme_ShouldContainProjectTitle | 正例 | README 包含项目标题 | 包含 CampusFlow |
| DQ-03 | readme_ShouldContainQuickStartSection | 正例 | README 包含快速开始 | 包含快速开始章节 |
| DQ-04 | readme_ShouldContainInstallationInstructions | 正例 | README 包含安装说明 | 包含 mvn/运行/安装 |
| DQ-05 | readme_ShouldContainFAQSection | 正例 | README 包含常见问题 | 包含 FAQ 章节 |
| DQ-06 | adrDirectory_ShouldExist | 正例 | docs/ADR 目录存在 | 目录存在 |
| DQ-07 | adrDirectory_ShouldContainADRFiles | 正例 | ADR 目录包含文件 | 至少 1 个 .md 文件 |
| DQ-10 | readme_Size_ShouldBeReasonable | 边界 | README 大小检查 | 100 - 100KB |
| DQ-11 | readme_ShouldContainRunnableCommands | 边界 | README 包含可运行命令 | 包含代码块或命令 |
| DQ-12 | readme_ShouldContainVersionInfo | 边界 | README 包含版本信息 | 包含版本号 |
| DQ-20 | readme_ShouldNotContainPlaceholders | 反例 | README 不包含占位符 | 不包含 [TODO]/[Your Name] |
| DQ-21 | adr_Files_ShouldFollowNamingConvention | 反例 | ADR 文件命名规范 | 格式：###-title.md |
| DQ-22 | adr_Files_ShouldContainRequiredSections | 反例 | ADR 包含必需章节 | 包含 status/context/decision |

### 3. 基础功能测试（AppTest）

| 测试 ID | 测试名称 | 类型 | 描述 | 预期结果 |
|---------|---------|------|------|----------|
| BA-01 | testAppExists | 正例 | 应用类可加载 | 不抛出异常 |
| BA-02 | testBasicAssertion | 正例 | 基础断言测试 | 所有断言通过 |

## 测试覆盖范围

### 正例测试（Happy Path）
- 验证文档端点可访问
- 验证文档内容完整
- 验证文档格式正确
- 验证 Schema 定义完整

### 边界测试
- 文档响应时间
- 文档大小
- README 大小
- 特殊字符处理

### 反例测试
- 不存在的端点
- 敏感信息暴露
- 占位符未替换
- 命名规范

### 契约测试
- 文档路径与实际 API 一致性
- 响应格式与 Schema 一致性
- 错误响应格式一致性

## 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=ApiDocumentationTest
mvn test -Dtest=DocumentationQualityTest

# 运行特定测试方法
mvn test -Dtest=ApiDocumentationTest#getApiDocs_ShouldReturnValidOpenApiSpec
```

## 测试命名规范

遵循 `method_condition_expected()` 格式：
- `getApiDocs_WhenCalled_ShouldReturn200()`
- `readme_WhenMissing_ShouldFail()`
- `taskSchema_ShouldContainAllRequiredFields()`

## 测试失败恢复

1. 如果是文档缺失：补充文档
2. 如果是文档内容不完整：修改文档
3. 如果是测试本身的问题：修复测试
4. 如果是实现未完成：添加注释提醒

## 交付物

- `/Users/wangxq/Documents/java-software-engineering/chapters/week_13/starter_code/src/test/java/com/campusflow/ApiDocumentationTest.java`
- `/Users/wangxq/Documents/java-software-engineering/chapters/week_13/starter_code/src/test/java/com/campusflow/DocumentationQualityTest.java`
- `/Users/wangxq/Documents/java-software-engineering/chapters/week_13/starter_code/src/test/java/com/campusflow/AppTest.java`
