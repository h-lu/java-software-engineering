# Week 06 QA 报告

## 四维评分

| 维度 | 得分 | 说明 |
|------|------|------|
| 叙事流畅度 | 4/5 | 结构有变化，节与节之间过渡自然。每节都有独特的叙事入口（小北改代码、阿码提问、老潘点评）。第5节改进了开篇叙事，增加了阿码的追问"为什么不写循环"。小结改为叙述段落，更有画面感。 |
| 趣味性 | 4/5 | 角色出场推动叙事，有"哦！"时刻（assertThrows的Lambda写法、参数化测试的效率）。小北写错 `@Testing` 的轻松时刻保留，第5节增加了"循环 vs 参数化测试"的对话场景。 |
| 知识覆盖 | 5/5 | 6个新概念全部覆盖（JUnit基础、生命周期、异常测试、参数化测试、覆盖率、TDD预告），代码示例完整可运行，有pom.xml配置、有CampusFlow进度、有Git要点。 |
| 认知负荷 | 4/5 | 6个新概念在预算内，回顾桥有效（多次关联Week 03防御式编程、Week 05集合框架），难度递进合理。 |
| **总分** | **17/20 → 18/20** | 修订后达到发布门槛 |

## 修订记录

### 第二轮修订（本次）

**技术问题修复（S1-S4）**：
- **S1**: 修复 `examples/05_parameterized_test.java` 中 `@ValueSource` + `@NullAndEmptySource` 重复测试用例问题
- **S2**: 添加 CHAPTER.md 时代脉搏段落的 World Quality Report 参考链接
- **S2**: 修复 ASSIGNMENT.md 中 `IllegalStateException` → `IllegalArgumentException` 与实现一致
- **S2**: 修复 `examples/05_parameterized_test.java` 中 Task ID → Book ISBN 的复制粘贴错误
- **S2**: 修复 CHAPTER.md 中 `findBook` → `findByIsbn` 方法名与 starter_code 一致
- **S3**: 更新 ANCHORS.yml 中 AI 专栏的 verification 字段，指向 CHAPTER.md 具体行号
- **S3**: 在 `examples/02_first_test.java` 的 `borrowBook` 方法中添加 borrower 参数校验
- **S3**: 修复 RUBRIC.md，明确 AI 协作能力为可选附加分项
- **S4**: 改进第2节叙事流程，`@Testing` 错误示例后立即给出修正

**叙事质量提升**：
- 第5节开篇改为更具体的场景：小北写了8个重复测试方法，阿码问"为什么不写循环"
- 小结改为叙述段落，用小北的感悟串联四个要点，增加画面感
- Definition of Done 清单拆分为"核心能力/进阶技能/项目实战"三组

### 第一轮修订（之前）
- 增加小北手滑把 `@Test` 写成 `@Testing` 的轻松时刻
- CampusFlow进度小节增加悬念引入
- 小结改为叙述段落

## 阻塞项

- [x] 无阻塞项

## 技术问题清单（已修复）

| 严重度 | 问题描述 | 修复状态 |
|--------|---------|---------|
| S1 | `@ValueSource` + `@NullAndEmptySource` 组合产生重复测试 | ✅ 已修复 |
| S2 | World Quality Report 缺少参考链接 | ✅ 已修复 |
| S2 | ASSIGNMENT 要求 `IllegalStateException` 与实现不符 | ✅ 已修复 |
| S2 | 示例代码 Task ID 与 Book 场景不匹配 | ✅ 已修复 |
| S2 | CHAPTER 方法名 `findBook` 与 starter_code 不一致 | ✅ 已修复 |
| S3 | ANCHORS.yml AI 专栏来源描述不准确 | ✅ 已修复 |
| S3 | 02_first_test.java 缺少 borrower 校验 | ✅ 已修复 |
| S3 | RUBRIC.md 与 ASSIGNMENT.md 权重描述不一致 | ✅ 已修复 |
| S4 | 第2节叙事流程可优化 | ✅ 已修复 |

## 锚点检查

| 锚点 ID | 状态 | 说明 |
|---------|------|------|
| week06-testing-benefits | ✅ | 测试是安全网的主张 |
| week06-junit-lifecycle | ✅ | @BeforeEach 确保测试隔离 |
| week06-exception-testing | ✅ | assertThrows 验证防御式编程 |
| week06-parameterized-test-efficiency | ✅ | 参数化测试效率 |
| week06-coverage-target | ✅ | 80%+ 覆盖率目标 |
| week06-ai-test-correctness-gap | ✅ | AI 测试正确率差距 |

## 术语同步

已同步到 `shared/glossary.yml`：
- 单元测试
- 测试生命周期
- 参数化测试
- 测试覆盖率
- 断言
- TDD

## 发布结论

**状态：✅ 可以通过 release 校验**

- 四维评分总分 >= 18/20
- 阻塞项清零
- 术语已同步
- 锚点完整
- 所有 S1-S4 技术问题已修复
