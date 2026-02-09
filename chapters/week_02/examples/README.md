# Week 02 示例代码

本目录包含 Week 02 "让程序做选择" 的所有示例代码。

## 示例列表

### 01_number_guessing_simple.py
**主题**：if/elif/else 条件判断

演示最简单的猜数字游戏：用户只能猜一次，程序告诉"大了""小了""猜对了"。

**运行方式**：
```bash
python3 chapters/week_02/examples/01_number_guessing_simple.py
```

**涉及知识点**：
- if/elif/else 语句
- 比较运算符（==, <, >）
- int() 类型转换

---

### 02_number_guessing_while.py
**主题**：while 循环 + break

让用户反复猜，直到猜中为止。使用 `break` 语句跳出循环。

**运行方式**：
```bash
python3 chapters/week_02/examples/02_number_guessing_while.py
```

**涉及知识点**：
- while True 无限循环
- break 跳出循环
- 带条件的 while 循环

---

### 03_number_guessing_for.py
**主题**：for 循环 + range()

限制用户最多猜 5 次，增加游戏紧张感。演示 for 循环和 range() 函数。

**运行方式**：
```bash
python3 chapters/week_02/examples/03_number_guessing_for.py
```

**涉及知识点**：
- for 循环
- range() 函数
- for...else 语句（循环正常结束时执行）
- 剩余次数计算

---

### 04_number_guessing_full.py
**主题**：复杂条件判断 + 逻辑运算符

完整版猜数字游戏：带难度选择（简单/中等/困难），影响数字范围。

**运行方式**：
```bash
python3 chapters/week_02/examples/04_number_guessing_full.py
```

**涉及知识点**：
- 复杂的 if/elif/else 嵌套
- 逻辑运算符（and, or, not）
- 布尔表达式的可读性优化
- random.randint() 生成随机数
- 输入验证（防止无效输入）

---

### 05_pyhelper.py
**主题**：PyHelper 超级线 Week 02 进度

在上周"能打印鼓励语"的基础上，PyHelper 学会了"根据心情推荐建议"。

**运行方式**：
```bash
python3 chapters/week_02/examples/05_pyhelper.py
```

**改进内容**：
- 用 input() 询问用户心情
- 用 if/elif/else 给出不同的学习建议
- 增强交互感和个性化

---

## 运行所有示例

如果你想依次运行所有示例：

```bash
# 示例 1：单次判断
python3 chapters/week_02/examples/01_number_guessing_simple.py

# 示例 2：while 循环
python3 chapters/week_02/examples/02_number_guessing_while.py

# 示例 3：for 循环
python3 chapters/week_02/examples/03_number_guessing_for.py

# 示例 4：完整版
python3 chapters/week_02/examples/04_number_guessing_full.py

# 示例 5：PyHelper
python3 chapters/week_02/examples/05_pyhelper.py
```

## 测试验证

每个示例代码都包含：
- ✅ 可运行的代码（可直接执行）
- ✅ 详细的注释说明
- ✅ 预期输出示例（作为注释）
- ✅ 常见错误演示（反例）
- ✅ 知识点总结

## 学习建议

1. **按顺序运行**：从 01 到 05 依次运行，感受游戏功能的逐步完善
2. **修改参数**：尝试修改 `secret` 的值、`max_attempts` 的次数，观察效果变化
3. **阅读反例**：每个示例文件末尾都有"坏例子演示"，展示常见错误及其原因
4. **对照学习**：边运行代码，边对照 CHAPTER.md 的对应章节，理解原理
