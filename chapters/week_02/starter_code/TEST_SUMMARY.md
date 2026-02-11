# Week 02 测试用例总结

## 测试覆盖概览

本项目包含 **48 个测试用例**，全面覆盖了 Week 02 的核心学习目标：

### 测试文件列表

| 测试文件 | 测试数量 | 覆盖内容 |
|---------|---------|---------|
| `TaskTest.java` | 14 | Task 类的封装、对象状态、边界情况 |
| `TaskManagerTest.java` | 16 | 任务管理功能、单一职责原则 |
| `EncapsulationTest.java` | 10 | 封装原则验证、数据保护 |
| `SolidPrinciplesTest.java` | 8 | SOLID 原则、上帝类反模式 |
| **总计** | **48** | **完整的类设计与封装测试** |

---

## 1. TaskTest.java (14 个测试)

### 正例测试（Happy Path）
- `test_create_task_with_valid_title_should_success` - 验证基本对象创建
- `test_new_task_should_have_default_completed_false` - 验证默认状态
- `test_mark_completed_should_change_state_to_true` - 验证状态变化
- `test_multiple_tasks_should_have_independent_state` - 验证对象独立性

### 边界测试（Edge Cases）
- `test_task_with_empty_title_should_still_create_object` - 空标题边界
- `test_task_with_special_characters_in_title` - 特殊字符处理
- `test_task_with_very_long_title` - 长文本处理（1000 字符）
- `test_mark_completed_twice_should_still_be_completed` - 重复操作边界

### 反例测试（Negative Cases）
- `test_title_setter_with_null_should_throw_exception` - null 输入验证
- `test_title_setter_with_empty_string_should_throw_exception` - 空字符串验证
- `test_title_setter_with_whitespace_only_should_throw_exception` - 纯空格验证
- `test_priority_setter_with_invalid_value_should_throw_exception` - 超出范围验证

### Getter/Setter 封装测试
- `test_getter_should_return_correct_value` - 验证 getter 功能
- `test_setter_should_update_value_with_validation` - 验证 setter 验证逻辑

---

## 2. TaskManagerTest.java (16 个测试)

### 正例测试
- `test_add_task_should_increase_task_count` - 验证添加任务
- `test_add_task_and_retrieve_it` - 验证添加和检索
- `test_mark_completed_by_title_should_change_state` - 验证标记完成
- `test_get_incomplete_tasks_should_return_only_active` - 验证过滤功能
- `test_get_all_tasks_should_return_defensive_copy` - 验证防御性拷贝

### 边界测试
- `test_get_all_tasks_from_empty_manager_should_return_empty_list` - 空管理器
- `test_get_incomplete_tasks_from_empty_manager_should_return_empty_list` - 空过滤
- `test_add_multiple_tasks_with_same_title` - 重复标题处理
- `test_mark_completed_non_existent_task_should_do_nothing` - 不存在任务处理
- `test_mark_completed_empty_string_should_do_nothing` - 空字符串标题处理
- `test_all_tasks_completed_should_return_empty_incomplete_list` - 全部完成场景

### 反例测试
- `test_add_null_task_should_throw_exception` - null 任务验证
- `test_mark_completed_null_title_should_throw_exception` - null 标题验证

### 单一职责原则测试
- `test_task_manager_should_not_store_task_data_itself` - 验证职责分离
- `test_task_manager_should_manage_multiple_tasks_independently` - 验证独立性
- `test_task_manager_should_filter_by_priority` - 验证过滤功能

---

## 3. EncapsulationTest.java (10 个测试)

### 封装验证测试
- `test_task_fields_should_be_private` - 使用反射验证 private 字段
- `test_setter_should_validate_input` - 验证 setter 数据验证
- `test_priority_setter_should_validate_range` - 验证范围检查

### 不变性测试
- `test_task_title_should_be_immutable_if_designed_so` - 概念演示
- `test_immutable_object_should_not_allow_state_change` - 不可变对象概念

### 内部实现变化测试
- `test_implementation_can_change_without_breaking_external_code` - 封装的核心价值

### 数据保护测试
- `test_public_field_would_allow_invalid_state` - 演示 public 字段的问题
- `test_getter_should_return_defensive_copy_for_mutable_fields` - 防御性拷贝概念

### 封装的好处测试
- `test_encapsulation_allows_controlled_change` - 控制变化的能力
- `test_encapsulation_allows_validation_logic` - 验证逻辑的价值

---

## 4. SolidPrinciplesTest.java (8 个测试)

### 单一职责原则（SRP）测试
- `test_task_should_only_store_data` - Task 的单一职责
- `test_task_manager_should_only_manage_tasks` - TaskManager 的单一职责
- `test_classes_should_have_clear_single_responsibility` - 清晰的职责划分

### 开闭原则（OCP）测试
- `test_task_manager_should_be_open_for_extension` - 对扩展开放
- `test_polymorphism_allows_extension_without_modification` - 多态与扩展

### "上帝类"反模式测试
- `test_god_class_anti_pattern_should_be_avoided` - 演示上帝类问题
- `test_responsibility_separation_makes_code_easier_to_maintain` - 职责分离的好处

### 判断 SRP 违反的测试
- `test_how_to_detect_srp_violation` - 如何识别 SRP 违反

---

## 测试矩阵总览

### 按测试类型分类

| 测试类型 | 数量 | 示例 |
|---------|-----|------|
| 正例（Happy Path） | 18 | 基本功能正常工作 |
| 边界（Edge Cases） | 16 | 空值、长文本、特殊字符 |
| 反例（Negative Cases） | 8 | 异常输入应被拒绝 |
| 设计原则（Principles） | 6 | 封装、SRP、OCP |

### 按学习目标分类

| 学习目标 | 对应测试 | 验证内容 |
|---------|---------|---------|
| 类与对象的关系 | TaskTest | 每个对象有独立状态 |
| 封装原则 | EncapsulationTest | private 字段 + public 方法 |
| 单一职责原则 | SolidPrinciplesTest | 职责分离、避免上帝类 |
| 数据验证 | TaskTest | setter 拒绝非法输入 |
| 防御式编程 | TaskManagerTest | 处理 null、空列表 |
| 领域模型 | 所有测试 | 实体类 vs 服务类 |

---

## 如何使用这些测试

### 学生学习路径

1. **运行测试，了解现状**
   ```bash
   mvn test
   ```
   所有测试应该通过（因为提供了完整实现）

2. **修改代码，观察测试失败**
   - 修改 Task 类，去掉 setter 的验证逻辑
   - 修改 TaskManager 类，直接返回内部列表（不防御性拷贝）
   - 观察哪些测试失败，理解为什么

3. **添加新功能，添加新测试**
   - 添加 `dueDate` 字段到 Task
   - 添加 `getOverdueTasks()` 方法到 TaskManager
   - 为新功能编写测试

4. **重构设计，改进代码**
   - 将验证逻辑抽取到 `TaskValidator` 类
   - 将显示逻辑抽取到 `TaskPrinter` 类
   - 验证所有测试仍然通过

### 教师使用建议

1. **课堂教学演示**
   - 展示单个测试的运行结果
   - 解释测试命名（`test_功能_场景_预期结果`）
   - 讲解如何通过测试理解设计意图

2. **作业验收**
   - 检查测试覆盖率（48 个测试都通过）
   - 检查代码质量（private 字段、验证逻辑）
   - 检查理解程度（能解释"为什么这样设计"）

3. **扩展练习**
   - 添加 `TaskValidator` 类和相关测试
   - 添加 `TaskPrinter` 类和相关测试
   - 实现开闭原则（通过接口扩展格式化功能）

---

## 测试命名规范

所有测试遵循统一的命名规范：

```
test_<功能>_<场景>_<预期结果>
```

示例：
- `test_create_task_with_valid_title_should_success` - 创建任务、合法标题、应该成功
- `test_title_setter_with_null_should_throw_exception` - setter、null 输入、应该抛异常
- `test_multiple_tasks_should_have_independent_state` - 多个对象、应该有独立状态

---

## 下一步

完成本周作业后，你应该能够：

1. ✅ 理解什么是"实体类"和"服务类"
2. ✅ 理解为什么要封装（private 字段 + public 方法）
3. ✅ 理解单一职责原则（SRP）- 避免"上帝类"
4. ✅ 能设计一个封装良好的类
5. ✅ 能判断一个类是否违反 SRP
6. ✅ 能为类编写单元测试

下周我们将学习 **Week 03：异常处理与防御式编程**，你会发现：
- 好的代码不是"不出错"，而是"出错时有策略"
- 封装 + 异常处理 = 强大的数据保护
- 如何用 try-catch 替代繁琐的 if-else 验证
