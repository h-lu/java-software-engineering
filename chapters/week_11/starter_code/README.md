# Week 11: 质量门禁与静态分析

本示例项目展示了如何使用静态分析工具和质量门禁来保障代码质量。

## 项目概述

CampusFlow 质量保障系统，包含以下功能：

1. **静态分析示例**：展示常见代码问题及修复方案
2. **质量门禁**：自定义质量规则和阈值
3. **测试覆盖率**：通过测试提高代码覆盖率

## 代码结构

```
src/main/java/com/campusflow/
├── quality/
│   ├── CodeWithBugs.java      # 包含常见静态分析问题的示例代码
│   ├── CodeFixed.java         # 修复后的版本
│   └── QualityGate.java       # 质量门禁配置
```

## 运行静态分析

### 运行 SpotBugs

```bash
mvn spotbugs:check
```

报告位置：`target/spotbugs.xml`

### 运行 JaCoCo 覆盖率

```bash
mvn jacoco:report
```

报告位置：`target/site/jacoco/index.html`

### 运行所有检查

```bash
mvn clean test spotbugs:check jacoco:report
```

## 质量门禁配置

### 默认门禁

- SpotBugs 高优先级问题：**阻断**（必须修复）
- JaCoCo 行覆盖率：**警告**（≥70%）
- JaCoCo 分支覆盖率：**警告**（≥50%）

### 严格门禁（核心库）

- SpotBugs 高/中优先级问题：**阻断**
- JaCoCo 行覆盖率：**阻断**（≥90%）
- JaCoCo 分支覆盖率：**警告**（≥80%）

### 宽松门禁（实验性功能）

- SpotBugs 高优先级问题：**警告**
- JaCoCo 行覆盖率：**信息**（≥50%）

## 常见 SpotBugs 警告

| 警告代码 | 描述 | 修复方法 |
|---------|------|---------|
| NP_NULL_ON_SOME_PATH | 可能的空指针解引用 | 添加 null 检查 |
| OBL_UNSATISFIED_OBLIGATION | 流未关闭 | 使用 try-with-resources |
| URF_UNREAD_FIELD | 未读取的字段 | 删除或添加 getter |
| ES_COMPARING_STRINGS_WITH_EQ | 字符串使用 == 比较 | 使用 equals() |
| EQ_COMPARETO_USE_OBJECT_EQUALS | compareTo 与 equals 不一致 | 保持一致逻辑 |
| REC_CATCH_EXCEPTION | 直接捕获 Exception | 捕获具体异常 |
| DM_EXIT | 调用 System.exit() | 抛出异常 |
| DM_STRING_CTOR | 不必要的 String 构造方法 | 直接返回字符串 |

## 技术债管理

创建技术债 Backlog：

```markdown
# CampusFlow 技术债 Backlog

| ID | 问题描述 | 类型 | 优先级 | 预估 | 计划 |
|----|---------|------|--------|------|------|
| #1 | TaskController 可能的空指针 | Bug | P0 | 1h | 本周 |
| #2 | 覆盖率 68% → 75% | Test | P1 | 3h | 本周 |
| #3 | 未使用的字段 | Clean | P2 | 0.5h | 下周 |
| #4 | 重复的日期转换代码 | Refactor | P3 | 2h | 有空 |
```

## 测试矩阵

### 静态分析测试（StaticAnalysisTest）

| 场景 | 测试数量 | 覆盖目标 |
|------|---------|---------|
| 正例 | 5 | 主分支 |
| 边界 | 8 | 边界分支 |
| 反例 | 6 | 异常分支 |

### 覆盖率提升测试（CoverageImprovementTest）

| 场景 | 测试数量 | 覆盖目标 |
|------|---------|---------|
| 正常流程 | 5 | 主分支 |
| 边界条件 | 8 | 边界分支 |
| 异常情况 | 6 | 异常分支 |

### 质量门禁测试（QualityGateTest）

| 场景 | 测试数量 | 覆盖目标 |
|------|---------|---------|
| 默认门禁 | 4 | 规则配置 |
| 严格门禁 | 4 | 严格阈值 |
| 宽松门禁 | 4 | 宽松阈值 |
| 门禁评估 | 8 | 评估逻辑 |

## 学习目标

完成本示例学习后，你将能够：

1. 理解静态分析的价值与局限
2. 配置和使用 SpotBugs
3. 测量和分析代码覆盖率
4. 设计和实现质量门禁
5. 管理技术债

## 参考资料

- [SpotBugs 官方文档](https://spotbugs.github.io/)
- [JaCoCo 官方文档](https://www.jacoco.org/jacoco/trunk/doc/)
- [Maven Surefire 插件](https://maven.apache.org/surefire/maven-surefire-plugin/)
