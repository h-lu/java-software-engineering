# Week 13 测试设计交付物

## 设计任务

为 week_13 "文档与知识传递" 设计 JUnit 5 测试用例矩阵，包括：
1. 创建 starter_code/ 结构（Maven 配置、示例代码、测试）
2. 设计 JUnit 5 测试用例矩阵（正例、边界、反例、契约测试）
3. 确保测试能通过 Maven 验证

## 完成情况

### ✅ 交付的文件

#### 1. Maven 配置
- `starter_code/pom.xml` - Maven 配置，包含 JUnit 5.10.2

#### 2. 主应用代码
- `starter_code/src/main/java/com/campusflow/App.java` - 主应用类，包含 API 文档端点
- `starter_code/src/main/java/com/campusflow/controller/` - REST API 控制器
- `starter_code/src/main/java/com/campusflow/service/` - 业务逻辑层
- `starter_code/src/main/java/com/campusflow/repository/` - 数据访问层
- `starter_code/src/main/java/com/campusflow/model/` - 领域模型
- `starter_code/src/main/java/com/campusflow/dto/` - 请求 DTO
- `starter_code/src/main/java/com/campusflow/exception/` - 异常类

#### 3. 测试类
- `starter_code/src/test/java/com/campusflow/AppTest.java` - 基础测试
- `starter_code/src/test/java/com/campusflow/ApiDocumentationTest.java` - API 文档测试
- `starter_code/src/test/java/com/campusflow/DocumentationQualityTest.java` - 文档质量测试

#### 4. 示例文档
- `starter_code/README.md` - 项目说明文档示例
- `starter_code/docs/ADR/INDEX.md` - ADR 索引示例
- `starter_code/docs/ADR/001-domain-model.md` - ADR 文件示例

#### 5. 测试文档
- `starter_code/TEST_MATRIX.md` - 测试用例矩阵
- `starter_code/TEST_SUMMARY.md` - 测试总结
- `starter_code/TEST_CASES.md` - 详细测试用例列表
- `DELIVERABLES.md` - 交付物清单
- `DESIGN_SUMMARY.md` - 设计总结

## 测试用例矩阵

| 测试类 | 用例数 | 正例 | 边界 | 反例 | 契约 |
|--------|--------|------|------|------|------|
| ApiDocumentationTest | 15 | 6 | 2 | 2 | 3 |
| DocumentationQualityTest | 16 | 7 | 3 | 2 | - |
| AppTest | 2 | 2 | - | - | - |
| **总计** | **33** | **15** | **5** | **4** | **3** |

## 关键测试用例

### API 文档测试
- `getApiDocs_ShouldReturnValidOpenApiSpec()` - 验证返回有效 OpenAPI JSON
- `openApiSpec_ShouldContainAllRequiredPaths()` - 验证包含所有路径
- `taskSchema_ShouldContainAllRequiredFields()` - 验证 Schema 完整
- `contractTest_PathsInDocs_ShouldBeAccessible()` - 契约测试：路径可用

### 文档质量测试
- `readme_ShouldExist()` - 验证 README 存在
- `readme_ShouldContainQuickStartSection()` - 验证快速开始章节
- `adrDirectory_ShouldContainADRFiles()` - 验证 ADR 文件存在

## Week 13 特色

### API 文档端点
- `GET /api-docs` - 返回 OpenAPI JSON 规范
- `GET /api-docs.yaml` - 返回 OpenAPI YAML 规范

### 契约测试
- 验证文档中的路径与实际 API 一致
- 验证响应格式与 Schema 一致
- 验证错误响应格式一致性

### Docs-as-Code
- 文档与代码同仓库
- 使用 Markdown 格式
- 版本控制同步

## 使用方式

```bash
# 进入项目目录
cd chapters/week_13/starter_code

# 编译
mvn compile

# 运行测试
mvn test

# 生成覆盖率报告
mvn jacoco:report
open target/site/jacoco/index.html

# 运行应用
mvn exec:java
```

## 技术栈

- **Java**: 17
- **JUnit**: 5.10.2
- **Maven**: 3.6+
- **Javalin**: 6.1.3
- **Jackson**: 2.17.0

## 设计要点

1. **测试分类清晰**：正例、边界、反例、契约
2. **命名规范**：`method_condition_expected()` 格式
3. **独立性**：每个测试独立，不依赖执行顺序
4. **可维护性**：使用 @DisplayName 描述测试意图
5. **文档验证**：验证 OpenAPI 规范和文档质量

## 文件路径

所有文件位于：
```
/Users/wangxq/Documents/java-software-engineering/chapters/week_13/
```

主代码在：
```
/Users/wangxq/Documents/java-software-engineering/chapters/week_13/starter_code/
```
