# Week 12 示例文件说明

本目录包含 Week 12 "集成测试与 Bug Bash" 的所有示例代码。

---

## 文件列表

### 1. `12_integration_test.java`
**用途**：完整的 JUnit 5 集成测试示例

**演示内容**：
- 使用 HttpClient 发送 HTTP 请求
- @BeforeEach/@AfterEach 生命周期管理
- 测试 REST API 端点
- 对比单元测试和集成测试的区别

**运行方式**：
```bash
# 编译示例
javac -cp .:junit-jupiter-api-5.10.2.jar 12_integration_test.java

# 阅读理解即可，实际运行参考 starter_code 中的测试
```

---

### 2. `12_mock_demo.java`
**用途**：Mockito Mock 示例

**演示内容**：
- 展示 Mock 对象的创建
- when().thenReturn() 语法
- verify() 验证方法调用
- 对比真实依赖和 Mock 依赖

**运行方式**：
```bash
# 阅读理解 Mock 概念
# 实际运行参考 starter_code 中的单元测试
```

---

### 3. `12_stub_demo.java`
**用途**：Stub 示例

**演示内容**：
- 展示 Stub 与 Mock 的区别
- Stub 返回预定义值
- 没有验证功能

**运行方式**：阅读理解 Stub 概念

---

### 4. `12_bug_bash_report.md`
**用途**：Bug Bash 报告模板

**内容**：
- 问题发现者
- 问题描述
- 复现步骤
- 严重程度
- 根因分析

**使用方式**：参考模板，为 Bug Bash 活动准备报告

---

### 5. `12_contract_test.java`
**用途**：API 契约测试示例

**演示内容**：
- 验证请求/响应格式
- OpenAPI/Swagger 契约文档
- 前后端契约同步

**运行方式**：
```bash
# 参考示例编写契约测试
# 实际运行参考 starter_code
```

---

### 6. `12_test_pyramid.java`
**用途**：测试金字塔概念演示

**演示内容**：
- 单元测试（底层，多且快）
- 集成测试（中层，适量）
- E2E 测试（顶层，少且慢）

**运行方式**：阅读理解测试策略

---

## 快速开始

### 1. 理解集成测试
阅读 `12_integration_test.java`，理解集成测试与单元测试的区别。

### 2. 学习测试替身
阅读 `12_mock_demo.java` 和 `12_stub_demo.java`，理解 Mock 和 Stub 的区别。

### 3. 理解测试金字塔
阅读 `12_test_pyramid.java`，理解测试策略的平衡。

### 4. 学习契约测试
阅读 `12_contract_test.java`，理解 API 契约验证。

### 5. 准备 Bug Bash
使用 `12_bug_bash_report.md` 模板，准备 Bug Bash 报告。

---

## 关键学习点

### 集成测试 vs 单元测试
| 维度 | 单元测试 | 集成测试 |
|------|---------|---------|
| **依赖** | Mock/隔离 | 真实服务 |
| **速度** | 快 | 慢 |
| **覆盖** | 代码逻辑 | 系统组装 |
| **发现问题** | 逻辑错误 | 契约问题 |

### 测试替身类型
| 类型 | 用途 | 验证 |
|------|------|------|
| **Dummy** | 占位符 | 无 |
| **Stub** | 预设返回值 | 无 |
| **Mock** | 预设返回值 + 验证调用 | 有 |
| **Spy** | 部分真实实现 | 有 |
| **Fake** | 简化实现 | 无 |

### Bug Bash 优先级
- **P0**：崩溃/安全漏洞，立即修复
- **P1**：功能缺陷，本周修复
- **P2**：体验问题，下周修复
- **P3**：优化建议，技术债 Backlog

### 契约测试价值
1. **预防式**：不等联调失败，开发阶段发现问题
2. **文档驱动**：OpenAPI 文档是"活的契约"
3. **团队约束**：前后端遵循同一规范

---

## CampusFlow 集成步骤

### 1. 编写集成测试
参考 `12_integration_test.java`，为 CampusFlow 编写 API 集成测试。

### 2. 添加 Mock 支持
在 pom.xml 中添加 Mockito 依赖。

### 3. 运行测试套件
```bash
# 单元测试
mvn test

# 集成测试
mvn verify

# 完整测试
mvn clean verify
```

### 4. 组织 Bug Bash
使用 `12_bug_bash_report.md` 模板，记录发现的问题。

### 5. 修复并总结
分析 Bug Bash 发现的问题，找出根因，修复并总结经验。

---

## 常见问题

### Q1: 集成测试太慢怎么办？
A: 精选测试场景——只测试"关键路径"，不做全面覆盖。使用内存数据库加快速度。

### Q2: Mock 和 Stub 怎么选？
A: 需要验证"是否被调用"用 Mock，只需要"返回什么"用 Stub。

### Q3: Bug Bash 会不会浪费时间？
A: 不会。Bug Bash 发现的问题往往是开发者看不到的盲区，ROI 很高。建议每次 Sprint 前安排 1-2 小时。

### Q4: 契约测试和集成测试区别？
A: 契约测试关注"接口规范"，集成测试关注"功能实现"。两者互补。

### Q5: 测试金字塔比例应该是多少？
A: 经验法则：70% 单元测试，20% 集成测试，10% E2E 测试。但根据项目调整。

---

## 参考资料

- [JUnit 5 用户指南](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito 文档](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [测试金字塔](https://martinfowler.com/articles/practical-test-pyramid.html)
- [契约测试模式](https://martinfowler.com/bliki/ContractTest.html)
- [OpenAPI 规范](https://swagger.io/specification/)
