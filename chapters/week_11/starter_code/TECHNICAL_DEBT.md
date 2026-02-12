# CampusFlow 技术债 Backlog

**项目**：CampusFlow
**更新日期**：2026-02-12
**维护者**：开发团队

---

## 使用说明

技术债 Backlog 用于记录已知但暂时不修复的问题。与产品 Backlog 不同，技术债 Backlog 关注代码质量而非功能。

**优先级定义**：
- **P0**：影响安全或核心功能，本周必须修复
- **P1**：质量基线问题，本周完成
- **P2**：优化类问题，下周修复
- **P3**：累积到 Sprint 结束或有空时处理

**修复策略**：
- 每个 Sprint 分配 20% 时间处理技术债
- P0/P1 优先处理，P2/P3 排期处理
- 修复前创建 Issue，修复后更新 Backlog

---

## 当前债务（Week 11）

### P0（本周必须修复）

| ID | 问题描述 | 类型 | 影响 | 预估 | 状态 |
|----|---------|------|------|------|------|
| #1 | TaskService.updateTask 可能的空指针 | Bug | 可能 NPE 崩溃 | 1h | 🔴 待修复 |
| #2 | InMemoryTaskRepository 未验证输入 | Bug | 非法输入导致异常 | 1h | 🔴 待修复 |
| #3 | 覆盖率低于 70% 目标 | Test | 测试盲区 | 2h | 🔴 待修复 |

**修复计划**：
- [ ] #1：添加 null 检查，抛出 NotFoundException
- [ ] #2：添加参数验证
- [ ] #3：补充异常处理测试，提升覆盖率到 75%+

---

### P1（本周完成）

| ID | 问题描述 | 类型 | 影响 | 预估 | 状态 |
|----|---------|------|------|------|------|
| #4 | Task 类缺少 equals/hashCode | Code | 可能的 Set/Map 问题 | 1h | 🔴 待修复 |
| #5 | Magic Number: 逾期费 0.5 元/天 | Code | 可维护性 | 0.5h | 🟡 进行中 |

**修复计划**：
- [ ] #4：添加 equals() 和 hashCode() 方法
- [ ] #5：提取为常量 OVERDUE_FEE_PER_DAY

---

### P2（下周修复）

| ID | 问题描述 | 类型 | 影响 | 预估 | 状态 |
|----|---------|------|------|------|------|
| #6 | 日志使用 System.out 而非 slf4j | Code | 生产环境问题 | 1h | ⚪ 待排期 |
| #7 | 缺少 API 文档 | Docs | 可维护性 | 2h | ⚪ 待排期 |

---

### P3（累积处理）

| ID | 问题描述 | 类型 | 影响 | 预估 | 状态 |
|----|---------|------|------|------|------|
| #8 | REST API 缺少 Rate Limiting | Feature | 稳定性 | 4h | ⚪ 待排期 |
| #9 | 单元测试缺少 Mock 数据 | Test | 测试可靠性 | 2h | ⚪ 待排期 |

---

## 统计信息

**总债务数**：9 个
- P0：3 个（🔴 紧急）
- P1：2 个（🟡 本周）
- P2：2 个（🟢 下周）
- P3：2 个（⚪ 累积）

**预估修复时间**：14.5 小时
- P0：4 小时（本周必须）
- P1：1.5 小时（本周完成）
- P2：3 小时（下周）
- P3：6 小时（Sprint 结束）

---

## 技术债趋势

| Week | P0 | P1 | P2 | P3 | 总计 | 新增 | 修复 |
|------|----|----|----|----|------|------|------|
| Week 11 | 3 | 2 | 2 | 2 | 9 | - | - |
| Week 12 | - | - | - | - | - | - | - |

---

## 与质量门禁集成

### SpotBugs 集成

运行静态分析后，将发现的问题记录到技术债 Backlog：

```bash
# 运行 SpotBugs
mvn spotbugs:check

# 查看报告
open target/site/spotbugs.html

# 将高优先级问题添加到 P0，中优先级添加到 P1/P2
```

### JaCoCo 集成

生成覆盖率报告后，识别未覆盖的代码分支：

```bash
# 运行测试并生成覆盖率报告
mvn test jacoco:report

# 查看报告
open target/site/jacoco/index.html

# 点击红色代码，补充测试用例
```

---

## 质量门禁配置

参见 `pom.xml` 中的配置：

- **SpotBugs**：高优先级问题阻断（failOnError=false，先警告）
- **JaCoCo**：行覆盖率 70%+（haltOnFailure=false，先警告）

未来可以调整为严格模式（haltOnFailure=true），建立真正的"门禁"。

---

## 参考资料

- 示例：`chapters/week_11/examples/11_technical_debt_backlog.md`
- [Martin Fowler - TechnicalDebt](https://martinfowler.com/bliki/TechnicalDebt.html)
- [SonarQube 技术债管理](https://www.sonarqube.org/)

---

**记住**：技术债是债务，不是罪过。关键是"有意识"地负债，"有计划"地还债。
