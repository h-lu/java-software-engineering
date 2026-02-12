# Week 11 示例文件说明

本目录包含 Week 11 "质量门禁与静态分析" 的所有示例代码。

---

## 文件列表

### 1. `11_spotbugs_config.xml`
**用途**：SpotBugs Maven 插件配置示例

**内容**：
- 完整的 pom.xml 配置片段
- SpotBugs 过滤器配置（排除误报）
- 高优先级问题阻断设置

**使用方式**：复制到 pom.xml 的 `<build><plugins>` 部分

**运行方式**：
```bash
mvn spotbugs:check
```

---

### 2. `11_jacoco_config.xml`
**用途**：JaCoCo 代码覆盖率配置示例

**内容**：
- 完整的 pom.xml 配置片段
- 覆盖率报告生成设置
- 覆盖率阈值配置

**使用方式**：复制到 pom.xml 的 `<build><plugins>` 部分

**运行方式**：
```bash
mvn jacoco:report
# 报告生成在 target/site/jacoco/index.html
```

---

### 3. `11_nullpointer_fix.java`
**用途**：SpotBugs 发现的空指针问题与修复示例

**演示内容**：
- 有问题的代码（未检查 null）
- SpotBugs 报告的解释
- 修复后的代码（防御式编程）

**运行方式**：阅读参考，理解问题类型和修复方法

---

### 4. `11_missing_test.java`
**用途**：JaCoCo 覆盖率盲区示例

**演示内容**：
- 未被测试覆盖的代码分支
- 如何补充测试用例
- 修复前后的覆盖率对比

**运行方式**：
```bash
# 运行测试
mvn test

# 生成覆盖率报告
mvn jacoco:report
```

---

### 5. `11_quality_gate_config.json`
**用途**：质量门禁配置示例

**内容**：
- 质量门禁规则定义
- SpotBugs + JaCoCo 组合门禁
- 阻断（block）与警告（warn）策略

**使用方式**：参考配置，设置项目的质量门禁

---

### 6. `11_technical_debt_backlog.md`
**用途**：技术债 Backlog 管理示例

**内容**：
- 技术债优先级分类（P0-P3）
- 修复策略和时间规划
- CampusFlow 技术债示例

**使用方式**：参考格式，为项目创建技术债追踪

---

### 7. `11_campusflow_pom.xml`
**用途**：CampusFlow 完整质量配置示例

**内容**：
- SpotBugs 插件配置
- JaCoCo 插件配置
- 两个工具的集成

**使用方式**：复制到 CampusFlow 的 pom.xml

---

## 快速开始

### 1. 学习 SpotBugs 配置
阅读 `11_spotbugs_config.xml`，理解如何配置静态分析。

### 2. 学习空指针修复
阅读 `11_nullpointer_fix.java`，理解 SpotBugs 如何发现和报告问题。

### 3. 学习 JaCoCo 配置
阅读 `11_jacoco_config.xml`，理解如何配置代码覆盖率。

### 4. 学习测试盲区识别
阅读 `11_missing_test.java`，理解如何发现和补充测试。

### 5. 配置质量门禁
参考 `11_quality_gate_config.json`，为项目设置质量门禁。

### 6. 管理技术债
参考 `11_technical_debt_backlog.md`，创建技术债 Backlog。

---

## CampusFlow 集成步骤

### 1. 更新 pom.xml
将 `11_campusflow_pom.xml` 中的配置复制到 CampusFlow 的 pom.xml。

### 2. 运行静态分析
```bash
mvn spotbugs:check
```

### 3. 查看覆盖率报告
```bash
mvn test jacoco:report
open target/site/jacoco/index.html
```

### 4. 修复高优先级问题
根据 SpotBugs 报告，修复 P0 问题。

### 5. 补充测试
根据 JaCoCo 报告，补充测试用例，提升覆盖率到 75%+。

### 6. 建立技术债 Backlog
创建 `TECHNICAL_DEBT.md`，记录未修复的问题。

---

## 关键学习点

### SpotBugs 高优先级问题类型
1. **NP_NULL_ON_SOME_PATH**：可能空指针解引用
2. **OBL_UNSATISFIED_OBLIGATION**：资源未关闭
3. **RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE**：冗余 null 检查但实际会 NPE

### JaCoCo 覆盖率指标
- **行覆盖率**（Line Coverage）：代码行执行比例
- **分支覆盖率**（Branch Coverage）：if/switch 分支执行比例
- **方法覆盖率**（Method Coverage）：方法调用比例
- **类覆盖率**（Class Coverage）：类加载比例

### 质量门禁设置原则
1. **核心路径严格**：关键业务逻辑需要高标准
2. **实验功能宽松**：临时特性可以降低要求
3. **团队约定优先**：阈值由团队讨论决定

### 技术债优先级
- **P0**：影响安全/核心功能，本周必须修复
- **P1**：质量基线问题，本周完成
- **P2**：优化类，下周修复
- **P3**：累积到 Sprint 结束处理

---

## 参考资料

- [SpotBugs Maven Plugin 文档](https://spotbugs.github.io/)
- [JaCoCo Maven Plugin 文档](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [SonarQube 质量门禁](https://www.sonarqube.org/)
- [技术债管理最佳实践](https://martinfowler.com/bliki/TechnicalDebt.html)

---

## 常见问题

### Q1: SpotBugs 报告太多问题，怎么看不过来？
A: 优先关注高优先级（High）问题，特别是空指针和资源泄漏。低优先级问题可以放入技术债 Backlog。

### Q2: 覆盖率 100% 就没有 bug 了吗？
A: 不是。覆盖率只说明代码"都跑过"，不保证逻辑正确。需要结合测试用例设计。

### Q3: 质量门禁太严格，影响开发速度怎么办？
A: 采用渐进式门禁——核心路径高标准，实验功能临时降低要求。定期 review 和调整阈值。

### Q4: 技术债什么时候还？
A: 每个 Sprint 固定时间还一部分债（如 20% 时间）。P0/P1 立即修复，P2/P3 排期处理。

### Q5: CI 中如何配置这些检查？
A: 在 CI 脚本中添加 `mvn spotbugs:check jacoco:report`，设置质量门禁规则不通过则失败。
