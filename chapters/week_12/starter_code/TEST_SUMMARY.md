# Week 11 测试用例矩阵总结

## 测试概览

| 测试类 | 测试数量 | 覆盖主题 |
|--------|---------|---------|
| StaticAnalysisTest | 33 | 静态分析问题修复 |
| CoverageImprovementTest | 37 | 代码覆盖率提升 |
| QualityGateTest | 25 | 质量门禁配置和评估 |
| **总计** | **95** | **Week 11 质量保障** |

> 注意：项目总测试数为 156，其中包含 Week 10 的测试（61 个）和本周新增测试（95 个）。

---

## 1. StaticAnalysisTest - 静态分析问题测试

### 测试目标
验证常见静态分析问题的行为和修复方案，每个测试对应一个 SpotBugs 警告类型。

### 测试矩阵

#### 1.1 空指针解引用 (NP_NULL_ON_SOME_PATH)

| 测试方法 | 场景 | 预期结果 |
|---------|------|---------|
| `getFullName_WhenValidInput_ShouldReturnFullName` | 正常输入 | 返回全名 |
| `getFullName_WhenFirstNameNull_ShouldThrowException` | firstName 为 null | 抛出 IllegalArgumentException |
| `getFullName_WhenLastNameNull_ShouldThrowException` | lastName 为 null | 抛出 IllegalArgumentException |
| `getFullName_WhenBothNull_ShouldThrowException` | 两者都为 null | 抛出 IllegalArgumentException |
| `getFullName_WhenEmptyString_ShouldHandleCorrectly` | 空字符串输入 | 正常处理 |
| `getFullName_WhenInputHasSpaces_ShouldTrim` | 带空格的输入 | trim 后返回 |

#### 1.2 字符串比较 (ES_COMPARING_STRINGS_WITH_EQ)

| 测试方法 | 场景 | 预期结果 |
|---------|------|---------|
| `isExitCommand_WhenEqual_ShouldReturnTrue` | 相等字符串 | 返回 true |
| `isExitCommand_WhenNotEqual_ShouldReturnFalse` | 不相等字符串 | 返回 false |
| `isExitCommand_WhenNull_ShouldReturnFalse` | null 输入 | 返回 false |
| `isExitCommand_WhenDifferentCase_ShouldReturnFalse` | 大小写不同 | 返回 false |
| `isExitCommand_WhenWithSpaces_ShouldReturnFalse` | 带空格 | 返回 false |

#### 1.3 数字解析 (REC_CATCH_EXCEPTION)

| 测试方法 | 场景 | 预期结果 |
|---------|------|---------|
| `parseNumber_WhenValidNumber_ShouldReturnNumber` | 有效数字 | 返回数字 |
| `parseNumber_WhenNegativeNumber_ShouldReturnNumber` | 负数 | 返回负数 |
| `parseNumber_WhenZero_ShouldReturnZero` | 零 | 返回 0 |
| `parseNumber_WhenInvalidInput_ShouldReturnZero` | 无效输入 | 返回 0 |
| `parseNumber_WhenNull_ShouldReturnZero` | null 输入 | 返回 0 |
| `parseNumber_WhenEmptyString_ShouldReturnZero` | 空字符串 | 返回 0 |
| `parseNumber_WhenWithSpaces_ShouldReturnZero` | 带空格的数字 | 返回 0 |
| `parseNumber_WhenLargeNumber_ShouldReturnNumber` | 超大数字 | 返回 MAX_VALUE |

#### 1.4 compareTo 与 equals 一致性 (EQ_COMPARETO_USE_OBJECT_EQUALS)

| 测试方法 | 场景 | 预期结果 |
|---------|------|---------|
| `compareTo_WhenSameObject_ShouldReturnZero` | 相同对象 | 返回 0 |
| `compareTo_WhenSameName_ShouldComparePriority` | name 相同 | 比较 priority |
| `compareTo_WhenDifferentName_ShouldCompareName` | name 不同 | 比较 name |
| `compareTo_WhenNull_ShouldReturnPositive` | null 输入 | 返回正数 |
| `compareTo_ShouldBeConsistentWithEquals` | 一致性检查 | compareTo 与 equals 一致 |

#### 1.5 小写转换 (DM_STRING_CTOR)

| 测试方法 | 场景 | 预期结果 |
|---------|------|---------|
| `toLowerCase_WhenUpperCase_ShouldConvertToLowerCase` | 大写字母 | 转换为小写 |
| `toLowerCase_WhenMixedCase_ShouldConvertToLowerCase` | 混合大小写 | 转换为小写 |
| `toLowerCase_WhenNull_ShouldReturnNull` | null 输入 | 返回 null |
| `toLowerCase_WhenEmptyString_ShouldReturnEmptyString` | 空字符串 | 返回空字符串 |
| `toLowerCase_WhenSpecialChars_ShouldKeepUnchanged` | 特殊字符 | 保持不变 |

#### 1.6 文件读取 (OBL_UNSATISFIED_OBLIGATION)

| 测试方法 | 场景 | 预期结果 |
|---------|------|---------|
| `readFile_WhenNormalFile_ShouldReadContent` | 正常文件 | 正确读取内容 |
| `readFile_WhenEmptyFile_ShouldReturnEmpty` | 空文件 | 返回空内容 |
| `readFile_WhenFileNotExists_ShouldThrowException` | 文件不存在 | 抛出 IOException |
| `readFile_WhenFileHasSpecialChars_ShouldReadCorrectly` | 特殊字符 | 正确读取 |

---

## 2. CoverageImprovementTest - 覆盖率提升测试

### 测试目标
提高代码覆盖率，确保各种代码路径（正常流程、边界条件、异常情况）都被测试到。

### 测试矩阵

#### 2.1 正常流程 (5 个测试)

| 测试方法 | 测试内容 |
|---------|---------|
| `constructor_WhenValidInput_ShouldCreateObject` | 构造函数创建对象 |
| `setName_WhenValidName_ShouldUpdateName` | 设置 name |
| `setPriority_WhenValidPriority_ShouldUpdatePriority` | 设置 priority |
| `getName_WhenCalledMultipleTimes_ShouldReturnSameValue` | 多次调用 getter |
| `getPriority_WhenCalledMultipleTimes_ShouldReturnSameValue` | 多次调用 getter |

#### 2.2 边界条件 (8 个测试)

| 测试方法 | 测试内容 |
|---------|---------|
| `constructor_WhenEmptyName_ShouldHandleCorrectly` | 空字符串 name |
| `constructor_WhenMinPriority_ShouldHandleCorrectly` | 最小优先级 |
| `constructor_WhenMaxPriority_ShouldHandleCorrectly` | 最大优先级 |
| `constructor_WhenZeroPriority_ShouldHandleCorrectly` | 零优先级 |
| `setName_WhenNull_ShouldSetNull` | 设置 null name |
| `compareTo_WhenSameNameAndPriority_ShouldReturnZero` | 相同对象比较 |
| `compareTo_WhenSameNameButLargerPriority_ShouldReturnPositive` | name 相同，priority 较大 |
| `compareTo_WhenSameNameButSmallerPriority_ShouldReturnNegative` | name 相同，priority 较小 |

#### 2.3 equals 和 hashCode (6 个测试)

| 测试方法 | 测试内容 |
|---------|---------|
| `equals_WhenSameObject_ShouldReturnTrue` | 相同对象 |
| `equals_WhenNull_ShouldReturnFalse` | null 比较 |
| `equals_WhenDifferentType_ShouldReturnFalse` | 不同类型 |
| `equals_WhenSameNameAndPriority_ShouldReturnTrue` | 相同 name 和 priority |
| `equals_WhenDifferentName_ShouldReturnFalse` | 不同 name |
| `equals_WhenDifferentPriority_ShouldReturnFalse` | 不同 priority |

#### 2.4 compareTo 边界 (4 个测试)

| 测试方法 | 测试内容 |
|---------|---------|
| `compareTo_WhenOtherNameIsNull_ShouldHandleCorrectly` | 其他对象 name 为 null |
| `compareTo_WhenBothNamesAreNull_ShouldComparePriority` | 两者 name 都为 null |
| `compareTo_WhenBothNamesAreNullAndSamePriority_ShouldReturnZero` | 两者 name 都为 null 且 priority 相同 |
| `compareTo_WhenEmptyName_ShouldHandleCorrectly` | 空字符串 name |

#### 2.5 其他边界测试 (8 个测试)

| 测试方法 | 测试内容 |
|---------|---------|
| `hashCode_WhenEqual_ShouldReturnSameHashCode` | 相等对象 hashCode 相同 |
| `hashCode_WhenDifferent_ShouldReturnDifferentHashCode` | 不同对象 hashCode 不同 |
| `equals_WhenOneNameIsNull_ShouldReturnFalse` | 一个 name 为 null |
| `isExitCommand_WhenMultipleWords_ShouldReturnFalse` | 多个单词输入 |
| `isExitCommand_WhenWithLeadingTrailingSpaces_ShouldReturnFalse` | 带前后空格 |
| `isExitCommand_WhenOnlySpaces_ShouldReturnFalse` | 只有空格 |
| `toLowerCase_WhenAlreadyLowerCase_ShouldReturnUnchanged` | 已经是小写 |
| `toLowerCase_WhenAllUpperCase_ShouldConvert` | 全大写转换 |

#### 2.6 parseNumber 边界 (6 个测试)

| 测试方法 | 测试内容 |
|---------|---------|
| `parseNumber_WhenPositiveInteger_ShouldParse` | 正整数解析 |
| `parseNumber_WhenNegative_ShouldParse` | 负数解析 |
| `parseNumber_WhenWithPlusSign_ShouldReturnZero` | 带加号返回 0 |
| `parseNumber_WhenDecimal_ShouldReturnZero` | 小数返回 0 |
| `parseNumber_WhenScientificNotation_ShouldReturnZero` | 科学计数法返回 0 |

---

## 3. QualityGateTest - 质量门禁测试

### 测试目标
验证质量门禁的各种配置和评估逻辑。

### 测试矩阵

#### 3.1 默认门禁 (4 个测试)

| 测试方法 | 验证内容 |
|---------|---------|
| `defaultGate_ShouldHaveCorrectRuleCount` | 规则数量 = 3 |
| `defaultGate_ShouldContainSpotBugsHighPriorityRule` | SpotBugs 高优先级规则存在 |
| `defaultGate_ShouldContainJacocoLineCoverageRule` | JaCoCo 行覆盖率 70% |
| `defaultGate_ShouldContainJacocoBranchCoverageRule` | JaCoCo 分支覆盖率 50% |

#### 3.2 严格门禁 (4 个测试)

| 测试方法 | 验证内容 |
|---------|---------|
| `strictGate_ShouldHaveCorrectRuleCount` | 规则数量 = 4 |
| `strictGate_ShouldContainSpotBugsMediumPriorityRule` | SpotBugs 中优先级规则存在 |
| `strictGate_ShouldHave90PercentLineCoverage` | 行覆盖率 90% |
| `strictGate_ShouldHave80PercentBranchCoverage` | 分支覆盖率 80% |

#### 3.3 宽松门禁 (4 个测试)

| 测试方法 | 验证内容 |
|---------|---------|
| `lenientGate_ShouldHaveCorrectRuleCount` | 规则数量 = 2 |
| `lenientGate_SpotBugsShouldWarnNotBlock` | SpotBugs 仅警告 |
| `lenientGate_ShouldHave50PercentLineCoverage` | 行覆盖率 50% |
| `lenientGate_BranchCoverageShouldBeInfo` | 分支覆盖率 INFO 级别 |

#### 3.4 门禁评估 (7 个测试)

| 测试方法 | 场景 | 预期状态 |
|---------|------|---------|
| `evaluate_WhenAllChecksPass_ShouldReturnPassed` | 所有检查通过 | PASSED |
| `evaluate_WhenBlockRuleFails_ShouldReturnFailed` | BLOCK 规则失败 | FAILED |
| `evaluate_WhenWarnRuleFails_ShouldReturnWarning` | WARN 规则失败 | WARNING |
| `evaluate_WhenBothBlockAndWarnFail_ShouldReturnFailed` | 两者都失败 | FAILED（阻断优先） |
| `evaluate_WhenEmptyResults_ShouldReturnPassed` | 空结果列表 | PASSED |
| `evaluate_WhenInfoRuleFails_ShouldReturnPassed` | INFO 规则失败 | PASSED（不影响） |

#### 3.5 自定义门禁 (3 个测试)

| 测试方法 | 验证内容 |
|---------|---------|
| `customGate_ShouldAllowAddingCustomRule` | 添加自定义规则 |
| `customGate_ShouldSupportFluentApi` | 链式调用 |
| `customGate_ShouldKeepSetName` | 保持设置的名称 |

#### 3.6 边界条件 (4 个测试)

| 测试方法 | 场景 | 预期结果 |
|---------|------|---------|
| `boundary_WhenCoverageEqualsThreshold_ShouldPass` | 覆盖率 = 阈值 | 通过 |
| `boundary_WhenCoverageSlightlyBelowThreshold_ShouldFail` | 覆盖率 < 阈值 | 失败 |
| `boundary_WhenCoverageIsZero_ShouldFail` | 覆盖率 = 0 | 失败 |
| `boundary_WhenCoverageIsFull_ShouldPass` | 覆盖率 = 1 | 通过 |

---

## 运行测试

```bash
# 运行所有测试
mvn test

# 只运行本周的测试（使用 JUnit 5 标签）
mvn test -Dgroups="week11"

# 运行特定测试类
mvn test -Dtest=StaticAnalysisTest
mvn test -Dtest=CoverageImprovementTest
mvn test -Dtest=QualityGateTest

# 生成覆盖率报告
mvn jacoco:report
# 报告位置: target/site/jacoco/index.html

# 运行 SpotBugs 静态分析
mvn spotbugs:check
# 报告位置: target/spotbugs.xml
```

---

## 测试覆盖率目标

| 指标 | 当前 | 目标 |
|------|------|------|
| 行覆盖率 | 75%+ | 70% |
| 分支覆盖率 | 65%+ | 50% |
| 测试数量 | 95 | 80+ |

---

## 与章节内容的对应关系

| 章节 | 概念 | 测试覆盖 |
|------|------|---------|
| 第 2 节 | SpotBugs 静态分析 | StaticAnalysisTest |
| 第 3 节 | JaCoCo 代码覆盖率 | CoverageImprovementTest |
| 第 4 节 | 质量门禁配置 | QualityGateTest |
| 第 5 节 | 技术债管理 | TECHNICAL_DEBT_BACKLOG.md |

---

## 学习要点

1. **静态分析不是银弹**：工具能发现模式化问题，但不能发现所有问题
2. **覆盖率是指标不是目标**：100% 覆盖率不代表没有 bug
3. **质量门禁要平衡**：太严格阻碍开发，太宽松失去意义
4. **技术债要管理**：不是所有问题都要立即修，优先级排序是关键

---

**测试设计日期**: 2026-02-12
**JUnit 版本**: 5.10.2
**Maven 版本**: 3.x
