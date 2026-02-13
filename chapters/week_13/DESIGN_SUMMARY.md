# Week 13 设计总结：JUnit 5 测试用例矩阵

## 交付物完成情况

### 1. Starter Code 结构

```
chapters/week_13/starter_code/
├── pom.xml                                   # Maven 配置（JUnit 5.10.2）
├── README.md                                  # 项目说明文档示例
├── TEST_MATRIX.md                             # 测试用例矩阵
├── TEST_SUMMARY.md                            # 测试总结
├── TEST_CASES.md                             # 详细测试用例列表
├── DELIVERABLES.md                          # 交付物清单
│
├── src/main/java/com/campusflow/
│   ├── App.java                               # 主应用（含 /api-docs 端点）
│   ├── controller/TaskController.java
│   ├── service/TaskService.java
│   ├── repository/
│   ├── model/Task.java
│   ├── dto/TaskRequest.java
│   └── exception/
│
├── src/test/java/com/campusflow/
│   ├── AppTest.java                          # 基础测试
│   ├── ApiDocumentationTest.java              # API 文档测试
│   ├── DocumentationQualityTest.java          # 文档质量测试
│   └── TaskServiceTest.java                # Service 测试
│
└── docs/ADR/
    ├── INDEX.md                              # ADR 索引
    └── 001-domain-model.md                   # ADR 示例
```

### 2. 测试用例矩阵

| 测试类 | 用例数 | 正例 | 边界 | 反例 | 契约 |
|--------|--------|------|------|------|------|
| ApiDocumentationTest | 15 | 6 | 2 | 2 | 3 |
| DocumentationQualityTest | 16 | 7 | 3 | 2 | - |
| AppTest | 2 | 2 | - | - | - |
| **总计** | **33** | **15** | **5** | **4** | **3** |

### 3. 测试覆盖的场景

#### 正例测试
- API 文档端点返回有效的 OpenAPI JSON/YAML
- 文档包含所有必需的路径和 Schema 定义
- README.md 存在并包含必需章节
- ADR 目录存在且包含文件

#### 边界测试
- 文档响应时间 < 1 秒
- 文档大小在合理范围内（1KB - 100KB）
- README 大小合理（100 - 100KB）
- README 包含可运行的命令

#### 反例测试
- 不存在的文档端点应返回 404
- 文档不应暴露敏感信息（password/token）
- README 不应包含占位符（[TODO]/[Your Name]）
- ADR 文件应遵循命名规范（###-title.md）

#### 契约测试
- 文档中的路径应实际可访问
- 实际 API 响应符合文档 Schema
- 错误响应格式一致（包含 code/message/timestamp）

### 4. 关键测试用例

| ID | 测试方法 | 验证内容 |
|----|---------|----------|
| AD-01 | getApiDocs_ShouldReturnValidOpenApiSpec | 返回有效 OpenAPI JSON，包含 info/paths/components |
| AD-20 | contractTest_PathsInDocs_ShouldBeAccessible | 契约测试：文档路径与实际 API 一致 |
| AD-21 | contractTest_ActualApi_ShouldMatchDocSchema | 实际响应字段与文档一致 |
| DQ-01 | readme_ShouldExist | README.md 文件存在 |
| DQ-03 | readme_ShouldContainQuickStartSection | 包含快速开始章节 |
| DQ-06 | adrDirectory_ShouldExist | docs/ADR 目录存在 |

### 5. Week 13 特色功能

#### API 文档端点
- `GET /api-docs` - 返回 OpenAPI JSON 规范
- `GET /api-docs.yaml` - 返回 OpenAPI YAML 规范

#### 文档验证测试
- OpenAPI 规范格式验证
- Schema 定义完整性检查
- 契约一致性验证

#### Docs-as-Code 实践
- 文档与代码同仓库
- 使用 Markdown 格式
- 版本控制同步

### 6. 测试命名规范

遵循 `method_condition_expected()` 格式：
- `getApiDocs_WhenCalled_ShouldReturn200()`
- `readme_WhenMissing_ShouldFail()`
- `taskSchema_ShouldContainAllRequiredFields()`

### 7. Maven 验证

```bash
# 编译
mvn compile

# 运行测试
mvn test

# 生成覆盖率
mvn jacoco:report
open target/site/jacoco/index.html
```

### 8. 学习目标覆盖

| 学习目标 | 对应内容 |
|---------|---------|
| 理解 API 文档价值 | ApiDocumentationTest |
| 应用 OpenAPI 规范 | App.java 中的 /api-docs 端点 |
| 分析 README 结构 | DocumentationQualityTest |
| 评价文档质量 | 文档质量检查测试 |
| 创造文档体系 | README + ADR 示例 |

## 技术栈

- **Java**: 17
- **JUnit**: 5.10.2
- **Maven**: 3.6+
- **Javalin**: 6.1.3
- **Jackson**: 2.17.0

## 下一步

学生需要：
1. 完善 README.md（添加项目特定信息）
2. 补充更多 ADR 文件
3. 根据实际 API 更新 OpenAPI 规范
4. 运行测试验证文档完整性
5. 可选：添加 Swagger UI
