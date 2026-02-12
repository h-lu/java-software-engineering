# 示例：Pull Request 描述模板

本示例展示一个完整的 Pull Request 描述模板，用于 CampusFlow 项目。

---

## 模板

```markdown
## 改动内容
<!-- 描述这个 PR 做了什么 -->
添加任务筛选功能，支持按优先级和完成状态筛选任务。

### 新增方法
- `filterByPriority(String priority)`：返回指定优先级的任务列表
- `filterByStatus(boolean completed)`：返回指定状态的任务列表

### 代码示例
```java
TaskManager manager = new TaskManager();
// 添加一些任务...

List<Task> highPriorityTasks = manager.filterByPriority("高");
List<Task> incompleteTasks = manager.filterByStatus(false);
```

## 为什么做
<!-- 业务背景或问题背景 -->
用户反馈任务列表太长，需要筛选功能来快速找到重要任务。

## 实现思路
<!-- 关键设计决策 -->
1. 在 TaskManager 中添加筛选方法，保持简单
2. 返回新列表，不修改原始数据（防御式编程）
3. 后续考虑拆分为独立的 TaskFilter 类（见 ADR-002）

## 测试方式
<!-- 如何验证这个改动 -->
1. 运行 `TaskManagerTest` 中的测试用例：
   ```bash
   mvn test -Dtest=TaskManagerTest
   ```
2. 手动测试：在 CLI 中创建任务后使用筛选功能
3. 边界情况测试：空列表、null 参数

## 审查重点
<!-- 希望审查者特别关注的地方 -->
- [ ] 异常处理是否完善（null 参数检查）
- [ ] 是否符合单一职责原则
- [ ] 测试覆盖是否充分

## 审查清单（自查）
- [x] 代码符合 Java 命名规范
- [x] 新增方法有适当的注释
- [x] 异常处理完善（考虑空输入情况）
- [x] 单元测试覆盖新增功能
- [x] 本地测试通过

## 相关文档
- ADR-001：领域模型设计
- 相关 PR：#12（任务排序功能）
```

---

## 不好的 PR 描述示例

```markdown
## 改动内容
修复 bug

## 测试方式
测过了
```

**问题**：
1. 没有说明修复了什么 bug
2. 没有测试步骤
3. 没有上下文信息
4. 审查者无法快速理解改动

---

## 好的 PR 描述要点

1. **做了什么**：清晰描述改动范围
2. **为什么做**：业务背景或问题背景
3. **怎么做**：关键设计决策
4. **如何测试**：验证步骤
5. **审查重点**：引导审查者关注关键部分
