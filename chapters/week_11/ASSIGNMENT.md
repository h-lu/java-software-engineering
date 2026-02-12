# Week 11 作业：质量门禁与静态分析

> "工具能告诉你'这里有问题'，AI 能解释'为什么有问题'，但'修不修'、'什么时候修'——得靠你。"
> —— 老潘

---

## 作业概述

本周你将建立 CampusFlow 的质量保障体系——从"手动检查"到"自动门禁"。你将配置静态分析工具（SpotBugs）和代码覆盖率工具（JaCoCo），发现并修复代码问题，建立技术债管理策略，并设计适合项目的质量门禁。

**核心任务**：
1. 配置 SpotBugs 进行静态分析，修复至少 3 个高优先级问题
2. 配置 JaCoCo 测量代码覆盖率，补充测试提升覆盖率到 75%+
3. 建立技术债 Backlog，制定优先级和修复计划
4. 设计质量门禁配置，平衡质量与开发速度

---

## 基础作业（必做）

### 任务 1：配置 SpotBugs 静态分析（20 分）

**目标**：在 CampusFlow 项目中配置 SpotBugs，自动扫描代码潜在问题。

**背景知识**：
SpotBugs 是 Java 静态分析工具，能在不运行代码的情况下发现：
- 空指针解引用（NP_NULL_ON_SOME_PATH）
- 资源未关闭（OBL_UNSATISFIED_OBLIGATION）
- 未使用的字段（URF_UNREAD_FIELD）
- 性能问题（如低效的字符串拼接）

**要求**：

1. **修改 pom.xml，添加 SpotBugs 插件**：
   ```xml
   <plugin>
       <groupId>com.github.spotbugs</groupId>
       <artifactId>spotbugs-maven-plugin</artifactId>
       <version>4.9.8.2</version>
       <executions>
           <execution>
               <goals>
                   <goal>check</goal>
               </goals>
           </execution>
       </executions>
   </plugin>
   ```

2. **运行静态分析**：
   ```bash
   mvn spotbugs:check
   ```

3. **查看报告**：
   报告生成在 `target/spotbugsXml.xml`，或使用 `mvn spotbugs:gui` 打开图形界面。

**提交物**：
- `pom.xml`：添加了 SpotBugs 插件的配置
- `spotbugs_report.txt`：SpotBugs 扫描结果的文本输出（运行 `mvn spotbugs:check` 的输出）

**评分要点**：
- pom.xml 配置正确（8 分）
- 成功运行 SpotBugs 并生成报告（6 分）
- 报告内容完整（6 分）

---

### 任务 2：修复至少 3 个高优先级问题（25 分）

**目标**：分析 SpotBugs 报告，修复至少 3 个高优先级问题。

**常见问题类型及修复示例**：

#### 1. 空指针解引用（NP_NULL_ON_SOME_PATH）

**问题代码**：
```java
public void updateTask(String id, Task task) {
    Task existing = taskRepository.findById(id);
    existing.setTitle(task.getTitle());  // 如果 findById 返回 null 呢？
}
```

**修复后**：
```java
public void updateTask(String id, Task task) {
    Task existing = taskRepository.findById(id);
    if (existing == null) {
        throw new NotFoundException("Task not found: " + id);
    }
    existing.setTitle(task.getTitle());
}
```

#### 2. 资源未关闭（OBL_UNSATISFIED_OBLIGATION）

**问题代码**：
```java
public void exportTasks(String filename) {
    FileWriter writer = new FileWriter(filename);
    writer.write(tasks.toString());
    // 忘记关闭 writer
}
```

**修复后**：
```java
public void exportTasks(String filename) {
    try (FileWriter writer = new FileWriter(filename)) {
        writer.write(tasks.toString());
    }
}
```

#### 3. 未使用的字段（URF_UNREAD_FIELD）

**问题代码**：
```java
public class Task {
    private String title;
    private int tempCounter;  // 从未读取
}
```

**修复后**：
删除未使用的字段或补充缺失的逻辑。

**要求**：
- 修复至少 3 个 SpotBugs 报告的高/中优先级问题
- 每个修复必须添加注释说明修复理由
- 修复后重新运行 SpotBugs，确认问题已解决

**提交物**：
- 修复后的代码文件（提交整个项目）
- `fixes.md`：修复说明文档，每个问题包含：
  - 问题类型（如 NP_NULL_ON_SOME_PATH）
  - 问题位置（类名、方法名、行号）
  - 问题描述
  - 修复方案（修复前/修复后代码对比）
  - 验证结果（SpotBugs 是否不再报此问题）

**评分要点**：
- 修复至少 3 个高/中优先级问题（15 分，每个 5 分）
- 修复方案正确有效（6 分）
- fixes.md 文档清晰完整（4 分）

---

### 任务 3：配置 JaCoCo 测量覆盖率（15 分）

**目标**：配置 JaCoCo 插件，测量代码覆盖率，识别测试盲区。

**背景知识**：
JaCoCo（Java Code Coverage）能测量测试执行时覆盖了哪些代码：
- **行覆盖率**（Line Coverage）：多少行代码被执行
- **分支覆盖率**（Branch Coverage）：多少 if/switch 分支被执行
- **方法覆盖率**（Method Coverage）：多少方法被调用

**要求**：

1. **修改 pom.xml，添加 JaCoCo 插件**：
   ```xml
   <plugin>
       <groupId>org.jacoco</groupId>
       <artifactId>jacoco-maven-plugin</artifactId>
       <version>0.8.15</version>
       <executions>
           <execution>
               <goals>
                   <goal>prepare-agent</goal>
               </goals>
           </execution>
           <execution>
               <id>report</id>
               <phase>test</phase>
               <goals>
                   <goal>report</goal>
               </goals>
           </execution>
       </executions>
   </plugin>
   ```

2. **运行测试并生成报告**：
   ```bash
   mvn test
   mvn jacoco:report
   ```

3. **查看报告**：
   打开 `target/site/jacoco/index.html`，查看覆盖率。

**提交物**：
- `pom.xml`：添加了 JaCoCo 插件的配置
- `jacoco_screenshot.png`：覆盖率报告的截图（需显示整体覆盖率数字）
- `coverage_analysis.md`：覆盖率分析，包含：
  - 当前行覆盖率百分比
  - 当前分支覆盖率百分比
  - 覆盖率最低的 3 个类及其覆盖率
  - 未覆盖代码的典型示例（从报告中复制红色代码）

**评分要点**：
- pom.xml 配置正确（5 分）
- 成功生成覆盖率报告（5 分）
- coverage_analysis.md 分析详细（5 分）

---

### 任务 4：补充测试提升覆盖率到 75%+（20 分）

**目标**：根据 JaCoCo 报告，补充测试用例，提升行覆盖率到 75% 以上。

**常见未覆盖场景及测试示例**：

#### 1. 异常处理分支

**未覆盖代码**：
```java
public void updateTask(String id, Task task) {
    Task existing = taskRepository.findById(id);
    if (existing == null) {
        throw new NotFoundException("Task not found: " + id);  // 红色
    }
    existing.setTitle(task.getTitle());
}
```

**补充测试**：
```java
@Test
void updateTask_notFound_throwsException() {
    // given
    when(taskRepository.findById("999")).thenReturn(Optional.empty());

    // when/then
    assertThrows(NotFoundException.class, () -> {
        taskController.updateTask("999", new Task());
    });
}
```

#### 2. 边界条件

**未覆盖代码**：
```java
public boolean isOverdue() {
    if (dueDate == null) return false;  // 红色：null 从未测试
    return dueDate.isBefore(LocalDate.now());
}
```

**补充测试**：
```java
@Test
void isOverdue_nullDueDate_returnsFalse() {
    // given
    Task task = new Task("Test");
    task.setDueDate(null);

    // when/then
    assertFalse(task.isOverdue());
}
```

#### 3. 枚举所有分支

**未覆盖代码**：
```java
public String getStatus() {
    if (completed) {
        return "completed";  // 红色
    }
    if (overdueDays > 0) {
        return "overdue";
    }
    return "pending";
}
```

**补充测试**：
```java
@Test
void getStatus_completed_returnsCompleted() {
    task.markCompleted();
    assertEquals("completed", task.getStatus());
}
```

**要求**：
- 根据 JaCoCo 报告，至少补充 3 个测试用例
- 每个测试用例必须对应一个红色（未覆盖）代码块
- 测试类放在对应包下（如 `src/test/java/com/campusflow/controller/TaskControllerTest.java`）
- 修复后行覆盖率必须达到 75% 以上
- 运行 `mvn test jacoco:report` 验证

**提交物**：
- 补充的测试类文件（`src/test/java/...`）
- `new_tests.md`：新测试说明，每个测试包含：
  - 测试方法名
  - 对应的未覆盖代码位置
  - 测试目的（测什么边界或异常情况）

**评分要点**：
- 补充至少 3 个测试用例（12 分，每个 4 分）
- 覆盖率达到 75%+（5 分）
- new_tests.md 文档清晰（3 分）

---

### 任务 5：创建技术债 Backlog（20 分）

**目标**：为 SpotBugs 发现的所有问题建立技术债 Backlog，排定优先级和修复计划。

**背景知识**：
技术债是为了快速交付而选择的"不够完美"的方案。不是所有问题都要立即修复，需要根据影响程度排优先级。

**优先级定义**：

| 优先级 | 定义 | 示例 | 修复时限 |
|--------|------|------|---------|
| P0 | 影响安全/核心功能 | 空指针风险、SQL 注入 | 本周必须 |
| P1 | 影响质量基线 | 覆盖率低于标准、高优先级静态分析警告 | 本周完成 |
| P2 | 优化类、低影响 | 未使用的字段、代码风格问题 | 下周修复 |
| P3 | 改进建议 | 代码重复、低优先级警告 | 有空再说 |

**要求**：

1. **创建 TECHNICAL_DEBT.md**，包含至少 10 个技术债项
2. **每个技术债包含**：
   - ID（#1, #2, ...）
   - 问题描述
   - 类型（Bug / Test / Clean / Refactor / Security）
   - 优先级（P0/P1/P2/P3）
   - 预估修复时间
   - 计划修复时间

**示例**：

```markdown
# CampusFlow 技术债 Backlog

## P0 - 本周必须修复

| ID | 问题描述 | 类型 | 预估 | 计划 |
|----|---------|------|------|------|
| #1 | TaskController 可能的空指针 | Bug | 1h | 本周三 |
| #2 | TaskService 资源未关闭 | Bug | 0.5h | 本周四 |

## P1 - 本周完成

| ID | 问题描述 | 类型 | 预估 | 计划 |
|----|---------|------|------|------|
| #3 | 覆盖率 68% → 75% | Test | 3h | 本周五 |
| #4 | TaskRepository 未测试的异常分支 | Test | 1h | 本周五 |

## P2 - 下周修复

| ID | 问题描述 | 类型 | 预估 | 计划 |
|----|---------|------|------|------|
| #5 | TaskService 未使用的字段 tempCounter | Clean | 0.5h | 下周二 |
| #6 | 重复的日期转换代码 | Refactor | 2h | 下周四 |

## P3 - 有空再说

| ID | 问题描述 | 类型 | 预估 | 计划 |
|----|---------|------|------|------|
| #7 | toString() 方法覆盖率低 | Test | 1h | Sprint 结束 |
| #8 | getter/setter 圈复杂度 | Clean | 0.5h | 有空 |

## 修复策略

- P0：影响核心功能，本周必须修复
- P1：质量基线问题，本周完成
- P2：优化类问题，下周修复
- P3：改进建议，累积到 Sprint 结束或有空时处理
```

**提交物**：
- `TECHNICAL_DEBT.md`：技术债 Backlog

**评分要点**：
- 包含至少 10 个技术债项（8 分）
- 优先级分类合理（6 分）
- 预估时间和计划时间合理（6 分）

---

## 进阶作业（选做，+20 分）

### 进阶 1：设计质量门禁配置（+10 分）

**目标**：创建 `quality-gate.json` 配置文件，定义合理的质量门禁规则。

**背景知识**：
质量门禁是在代码合并前自动检查质量标准的机制。太严格会拖慢开发，太宽松则没意义。

**要求**：

1. **创建 quality-gate.json**：
   ```json
   {
     "rules": [
       {
         "tool": "spotbugs",
         "category": "high",
         "threshold": 0,
         "action": "block"
       },
       {
         "tool": "spotbugs",
         "category": "medium",
         "threshold": 5,
         "action": "warn"
       },
       {
         "tool": "jacoco",
         "metric": "line_coverage",
         "threshold": 0.70,
         "action": "warn"
       },
       {
         "tool": "jacoco",
         "metric": "line_coverage",
         "threshold": 0.60,
         "action": "block"
       }
     ],
     "exceptions": [
       {
         "reason": "紧急热修复",
         "allowed_by": ["老潘"],
         "max_override_coverage": 0.50
       }
     ]
   }
   ```

2. **编写 quality-gate-explanation.md**，解释每个规则的设计理由：
   - 为什么 SpotBugs 高优先级要阻断？
   - 为什么覆盖率设 75% 警告、60% 阻断？
   - 什么情况下允许例外？

**提交物**：
- `quality-gate.json`：质量门禁配置
- `quality-gate-explanation.md`：设计理由说明

---

### 进阶 2：配置 CI 质量门禁（+10 分）

**目标**：在 GitHub Actions 或 GitLab CI 中配置自动质量检查。

**要求**：

1. **创建 .github/workflows/quality.yml**（GitHub Actions）或 `.gitlab-ci.yml`（GitLab CI）

**GitHub Actions 示例**：
```yaml
name: Quality Gate

on: [pull_request]

jobs:
  quality-check:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run SpotBugs
        run: mvn spotbugs:check
      - name: Run tests with coverage
        run: mvn test jacoco:report
      - name: Check coverage threshold
        run: |
          COVERAGE=$(mvn exec:exec -Dexec.executable='echo' -Dexec.args='%jacoco.line.coverage%' | grep -oP '\d+')
          if [ $COVERAGE -lt 75 ]; then
            echo "Coverage $COVERAGE% is below 75% threshold"
            exit 1
          fi
```

2. **提交一个 PR，验证 CI 跑通**

**提交物**：
- `.github/workflows/quality.yml` 或 `.gitlab-ci.yml`
- `ci_screenshot.png`：CI 运行成功的截图

---

## AI 协作练习（可选但推荐）

**目标**：练习用 AI 辅助配置静态分析工具，并审查 AI 生成的配置。

### 任务：用 AI 生成 SpotBugs 配置

**步骤**：

1. **编写 Prompt**，让 AI 生成 SpotBugs 配置：
   - 明确需求：为 CampusFlow 项目配置 SpotBugs
   - 指定版本：Maven 插件，版本 4.9.8.2
   - 添加约束：只检查高优先级问题，忽略测试代码

2. **保存 AI 生成的配置**（不要修改）

3. **使用审查检查清单评估**：
   - [ ] 配置版本正确吗？
   - [ ] 是否包含了必要的 goal（check）？
   - [ ] 是否配置了 effort/maxRank 来过滤问题？
   - [ ] 是否忽略了测试代码（如 `<excludeFilterFile>`）？
   - [ ] 配置格式正确吗（XML 语法）？

4. **记录发现的问题并修复**

**审查清单**：
- [ ] **正确性**：配置能运行吗？`mvn spotbugs:check` 是否报错？
- [ ] **完整性**：是否缺少必要配置（如 executions/goals）？
- [ ] **合理性**：过滤规则合理吗？（如忽略低优先级问题是否恰当）
- [ ] **规范性**：是否遵循 Maven 插件配置规范？
- [ ] **AI 特定问题**：有幻觉配置（不存在的参数）吗？版本号正确吗？

**提交物**：
- `ai_generated_spotbugs.xml`：AI 生成的原始配置
- `ai_review_spotbugs.md`：审查报告，包含：
  - 发现的问题列表
  - 修复后的配置
  - 经验总结（AI 擅长/不擅长配置工具的原因）

---

## 提交物清单

### 必交文件
- [ ] `pom.xml`：添加了 SpotBugs 和 JaCoCo 插件
- [ ] `spotbugs_report.txt`：SpotBugs 扫描结果
- [ ] `fixes.md`：修复说明文档（至少 3 个问题）
- [ ] `jacoco_screenshot.png`：覆盖率报告截图
- [ ] `coverage_analysis.md`：覆盖率分析
- [ ] `TECHNICAL_DEBT.md`：技术债 Backlog（至少 10 项）
- [ ] 修复后的所有源代码文件

### 进阶作业文件（如完成）
- [ ] `quality-gate.json`：质量门禁配置
- [ ] `quality-gate-explanation.md`：设计理由说明
- [ ] `.github/workflows/quality.yml` 或 `.gitlab-ci.yml`
- [ ] `ci_screenshot.png`：CI 运行截图

### AI 协作练习文件（如完成）
- [ ] `ai_generated_spotbugs.xml`：AI 生成的配置
- [ ] `ai_review_spotbugs.md`：审查报告

---

## 作业截止时间

- **基础作业**：本周日 23:59
- **进阶作业**：下周三 23:59

---

## 常见问题

### Q1: SpotBugs 报错 "Failed to analyze plugin descriptor" 怎么办？

检查 Maven 依赖是否正确：
- 确保 JDK 版本兼容（建议 Java 17）
- 尝试 `mvn clean` 后重新运行
- 检查网络连接（Maven 需要下载插件）

### Q2: 覆盖率提升不上去怎么办？

1. 查看 JaCoCo 报告中的红色代码（未覆盖）
2. 针对每个红色代码块写测试
3. 常见未覆盖场景：
   - 异常处理分支
   - null 检查分支
   - 枚举的所有状态
   - getter/setter（如果追求高覆盖率）

4. 目标覆盖率：基础作业要求达到 75%，不要为了 100% 而写无意义的测试

### Q3: 技术债应该多少个？

建议至少 10 个，来源包括：
- SpotBugs 报告的所有高/中优先级问题（至少 5 个）
- 覆盖率未达到 80% 的类（至少 3 个）
- 代码质量问题（如重复代码、命名不当）

### Q4: 质量门禁应该设多严格？

根据项目阶段调整：
- 早期（MVP）：覆盖率 60-70% 即可，快速迭代
- 成长期：覆盖率 75-80%，高优先级 SpotBugs 问题阻断
- 成熟期：覆盖率 80%+，所有 SpotBugs 问题阻断

工程实用主义：不要一开始就追求完美，质量要"持续改进"，而不是"一次性达标"。

### Q5: AI 生成的配置可以直接用吗？

必须审查！常见问题：
- 版本号过时或不存在
- 参数拼写错误（如 `<effort>` 写成 `<effortLevel>`）
- 缺少必要配置（如 `<executions>`）
- 不符合项目规范

---

## 参考资源

- 如果你遇到困难，可以参考 `starter_code/src/main/java/com/campusflow/App.java` 中的示例代码
- SpotBugs 官方文档：https://spotbugs.github.io/
- JaCoCo 官方文档：https://www.jacoco.org/jacoco/trunk/doc/
- Week 11 章节正文：`chapters/week_11/CHAPTER.md`

---

## 学习建议

1. **工具是雷达，不是救世主**：静态分析工具只能发现模式化问题，设计缺陷仍需人工审查
2. **覆盖率是指标，不是目标**：不要为了 100% 覆盖率而写无意义的测试
3. **技术债要管理，不要恐惧**：有意识负债，定期还债，保持可控
4. **质量门禁要平衡**：太严格拖慢速度，太宽松失去意义

祝作业顺利！记住老潘的话："工具发现问题，AI 解释原因，人做决策。"
