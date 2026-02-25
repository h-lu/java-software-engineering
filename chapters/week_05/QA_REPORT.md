# Week 05 QA 报告

生成日期：2026-02-25

---

## 四维评分 (Student-QA)

| 维度 | 得分 | 说明 |
|------|------|------|
| 叙事流畅度 | 4/5 | 场景驱动叙事清晰，从数组困境→ArrayList→HashMap→泛型→迭代的递进自然。第5节(Iterator)衔接稍技术性扣1分 |
| 趣味性 | 5/5 | 角色互动出色：阿码踩坑原始类型、小北顿悟泛型价值、老潘工程点评，金句收尾有记忆点 |
| 知识覆盖 | 5/5 | 6个核心概念全覆盖，代码示例完整（正例+边界+反例），AI小专栏贴合时代 |
| 认知负荷 | 5/5 | 6个新概念正好在预算内(上限6)，回顾桥4个超过最低要求(2个) |
| **总分** | **19/20** | ✅ 达到release标准 (>= 18/20) |

---

## 阻塞项

### 技术阻塞项 (Technical Reviewer - S1-S2)

**状态：✅ 无阻塞项**

#### S1 (Critical) - 必须修复
- [x] 无关键问题

#### S2 (Major) - 应该修复
- [x] 无重大问题

### 叙事阻塞项 (Student-QA)

**状态：✅ 无阻塞项**

- [x] 无阻塞项 - 达到release标准

---

## 建议项 (非阻塞性改进)

### 技术建议 (S3-S4) - 已修复
- [x] S3: `ASSIGNMENT.md` AI Cache示例缺少 `import java.util.List` 和 `import java.util.ArrayList` ✅ 已添加
- [x] S3: `ASSIGNMENT.md` AI Cache示例使用原始类型 `new HashMap()` ✅ 已改为 `new HashMap<>()`
- [x] S4: `CHAPTER.md` 第1节初始 `addBook` 示例故意无边界检查 ✅ 已添加注释说明

### 叙事建议
- [ ] 第5节Iterator可增加更具体场景引出必要性
- [ ] 长代码块可添加更详细注释说明数据同步逻辑
- [ ] Git小节位置与前后文关联性可优化

### 其他修复
- [x] S4: `CampusFlowRepository.java` 中新建 repo 实例可能让读者困惑 ✅ 已添加注释说明这是用于演示的独立实例

---

## 一致性检查 (Consistency-Editor)

### 代码风格对齐
| 检查项 | 状态 | 备注 |
|--------|------|------|
| 类名 PascalCase | ✅ | `Book`, `LibraryWithArray`, `LibraryTracker` 等 |
| 方法名 camelCase | ✅ | `addBook()`, `findByIsbn()`, `getTitle()` 等 |
| 常量 UPPER_SNAKE_CASE | ✅ | 本周无常量定义 |
| K&R 大括号风格 | ✅ | 左大括号不换行 |
| 4空格缩进 | ✅ | 代码示例格式正确 |

### 术语同步 (TERMS.yml → glossary.yml)
**状态：✅ 已同步，无需更新**

| 术语 (term_zh) | 状态 | glossary.yml 位置 |
|----------------|------|-------------------|
| ArrayList | ✅ 已存在 | line 153-156 |
| HashMap | ✅ 已存在 | line 158-162 |
| 泛型 | ✅ 已存在 | line 163-167 |
| 原始类型 | ✅ 已存在 | line 168-172 |
| 迭代器 | ✅ 已存在 | line 173-177 |
| 增强 for 循环 | ✅ 已存在 | line 178-182 |
| 菱形操作符 | ✅ 已存在 | line 183-187 |
| Repository 模式 | ✅ 已存在 | line 188-192 |

### 角色一致性检查
| 角色 | 出场次数 | 一致性检查 |
|------|----------|------------|
| **小北** (Python转Java学生) | 4次 | ✅ 性格一致：对类型困惑、问基础问题、踩坑后领悟 |
| **阿码** (好奇追问者) | 3次 | ✅ 性格一致：问边界问题、尝试偷懒、踩坑后认错 |
| **老潘** (工程前辈) | 3次 | ✅ 性格一致：务实简洁、给工程建议、强调生产环境 |

**符合约束**：每章至少出场 2 次 ✅ (实际每角色均 ≥3 次)

### ANCHORS.yml 有效性
| 检查项 | 状态 |
|--------|------|
| YAML 语法 | ✅ 格式正确 |
| ID 唯一性 | ✅ 8个锚点ID均唯一 |
| claim 完整性 | ✅ 每个锚点有明确claim |
| evidence 存在性 | ✅ 每个claim有对应证据文件 |
| verification 可执行 | ✅ 验证方法描述清晰 |

---

## 认知负荷检查

### 新概念数
- **本周新概念**: 6个 (预算上限6个) ✅
  1. ArrayList（动态数组）
  2. HashMap（键值对映射）
  3. 泛型（类型参数）
  4. 原始类型与类型安全
  5. 迭代器（Iterator）
  6. 增强 for 循环（for-each）

### 回顾桥
- **已引用概念**: 4个 (要求≥2个) ✅
  - Week 01: Scanner、静态类型
  - Week 02: 封装/类定义
  - Week 03: 异常处理/防御式编程

---

## CampusFlow 进度检查

**状态：✅ 完整**

- 上周状态：团队协作流程建立，ADR-002（数据存储方案决策）完成
- 本周改进：使用集合重构数据存储层，从数组升级到 ArrayList/HashMap 组合
- 具体落盘：
  1. ✅ 创建 Repository 层类（TaskRepository）
  2. ✅ 使用 ArrayList 存储实体对象
  3. ✅ 使用 HashMap 建立 ID 到对象的快速查找索引
  4. ✅ 添加基本的增删改查方法

---

## 校验状态汇总

| 检查项 | 状态 |
|--------|------|
| 文件齐全（CHAPTER.md、ASSIGNMENT.md、RUBRIC.md、QA_REPORT.md、TERMS.yml、ANCHORS.yml） | ✅ |
| 术语已同步到 shared/glossary.yml | ✅ |
| 代码可运行 | ✅ (静态审查通过) |
| 认知负荷预算达标（6个概念 ≤ 6个预算） | ✅ |
| 角色出场达标（每角色 ≥2次） | ✅ |
| CampusFlow 进度完整 | ✅ |
| 四维评分 >= 18/20 | ✅ (19/20) |
| 无技术阻塞项（S1-S2） | ✅ |
| 无叙事阻塞项 | ✅ |
| validate_week.py (idle模式) | ✅ 通过 |
| validate_week.py (release模式) | ✅ 通过 |

### 测试验证详情
```bash
$ mvn -q -f chapters/week_05/starter_code/pom.xml test
# 所有测试通过
```

---

## QA 审读结论

**Week 05 通过质量审查，达到 Release 标准。**

建议：可进入 release-week 流程。
