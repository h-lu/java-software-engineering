# Week 13 交付物清单

## Starter Code 结构

```
chapters/week_13/starter_code/
├── pom.xml                                   # Maven 配置
├── README.md                                  # 项目说明示例文档
├── TEST_MATRIX.md                             # 测试用例矩阵文档
├── TEST_SUMMARY.md                            # 测试总结文档
├── TEST_CASES.md                             # 详细测试用例列表
│
├── src/main/java/com/campusflow/
│   ├── App.java                               # 主应用（含 API 文档端点）
│   ├── controller/
│   │   └── TaskController.java                # REST API 控制器
│   ├── service/
│   │   └── TaskService.java                   # 业务逻辑层
│   ├── repository/
│   │   ├── TaskRepository.java                # 仓储接口
│   │   └── InMemoryTaskRepository.java        # 内存实现
│   ├── model/
│   │   └── Task.java                         # 领域模型
│   ├── dto/
│   │   └── TaskRequest.java                  # 请求 DTO
│   └── exception/
│       ├── NotFoundException.java              # 未找到异常
│       └── ValidationException.java           # 验证异常
│
├── src/test/java/com/campusflow/
│   ├── AppTest.java                          # 基础测试（2 个用例）
│   ├── ApiDocumentationTest.java              # API 文档测试（15 个用例）
│   ├── DocumentationQualityTest.java          # 文档质量测试（16 个用例）
│   └── TaskApiTest.java                      # API 功能测试
│
└── docs/ADR/
    ├── INDEX.md                              # ADR 索引示例
    └── 001-domain-model.md                   # ADR 文件示例
```

## 文件统计

| 类型 | 数量 | 说明 |
|------|------|------|
| Java 源文件 | 9 | App.java + 各层组件 |
| 测试类 | 4 | 33 个测试用例 |
| 文档文件 | 4 | README + 测试文档 + ADR |
| 总代码行数 | ~693 | 测试 + 文档代码 |

## 测试用例统计

| 测试类 | 用例数 | 正例 | 边界 | 反例 | 契约 |
|--------|--------|------|------|------|------|
| ApiDocumentationTest | 15 | 6 | 2 | 2 | 3 |
| DocumentationQualityTest | 16 | 7 | 3 | 2 | - |
| AppTest | 2 | 2 | - | - | - |
| **总计** | **33** | **15** | **5** | **4** | **3** |

## Week 13 特色功能

### 1. API 文档端点
- `GET /api-docs` - 返回 OpenAPI JSON 规范
- `GET /api-docs.yaml` - 返回 OpenAPI YAML 规范

### 2. 文档验证测试
- 验证 OpenAPI 规范格式正确性
- 验证文档与实现一致性（契约测试）
- 验证 README 和 ADR 完整性

### 3. 示例文档
- README.md（快速开始、使用指南、FAQ）
- ADR 索引和示例文件
- Docs-as-Code 实践

## Maven 验证命令

```bash
cd chapters/week_13/starter_code

# 编译
mvn compile

# 运行测试
mvn test

# 生成覆盖率报告
mvn jacoco:report

# 运行应用
mvn exec:java
```

## 学习目标覆盖

| 学习目标 | 对应测试 | 代码 |
|---------|---------|------|
| 理解 API 文档价值 | ApiDocumentationTest | AD-01 ~ AD-06 |
| 应用 OpenAPI 规范 | App.java | /api-docs 端点 |
| 分析 README 结构 | DocumentationQualityTest | DQ-01 ~ DQ-05 |
| 评价文档质量 | DocumentationQualityTest | DQ-20 ~ DQ-22 |
| 创造文档体系 | docs/ADR/* | README + ADR |

## 交付文件清单

### 必交文件
- [x] `pom.xml` - Maven 配置
- [x] `src/main/java/com/campusflow/App.java` - 主应用类
- [x] `src/test/java/com/campusflow/ApiDocumentationTest.java` - API 文档测试
- [x] `src/test/java/com/campusflow/DocumentationQualityTest.java` - 文档质量测试
- [x] `src/test/java/com/campusflow/AppTest.java` - 基础测试

### 文档示例
- [x] `README.md` - 项目说明文档
- [x] `docs/ADR/INDEX.md` - ADR 索引
- [x] `docs/ADR/001-domain-model.md` - ADR 示例

### 测试文档
- [x] `TEST_MATRIX.md` - 测试用例矩阵
- [x] `TEST_SUMMARY.md` - 测试总结
- [x] `TEST_CASES.md` - 详细测试用例列表

## 版本信息

- **版本**: 2.4.0
- **Week**: 13
- **主题**: 文档与知识传递
- **Java**: 17
- **JUnit**: 5.10.2
- **Maven**: 3.6+

## 下一步建议

1. 学生需要根据实际项目完善 README
2. 学生需要补充更多 ADR 文件
3. 学生可以扩展文档质量检查测试
4. 学生可以添加 Swagger UI 集成
