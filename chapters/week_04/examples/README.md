# Week 04 示例代码

本目录包含 Week 04（用容器装数据）的所有示例代码。

## 文件列表

### 01_scores_before.py
**演示问题：用单独变量存储多个成绩的痛点**

- 展示为什么需要列表
- 代码重复、难以维护
- 无法批量处理数据

**关键点**：
- 单个变量无法高效管理多数据
- 扩展性差

---

### 02_list_basics.py
**演示列表的创建和基本访问**

- 创建列表：`scores = [85, 92, 78]`
- 索引访问：`scores[0]`、`scores[-1]`
- 列表操作：`len()`、`sum()`
- 检查元素：`92 in scores`

**关键点**：
- 索引从 0 开始
- 负数索引从末尾计数
- 访问不存在的索引会报 `IndexError`

---

### 03_list_operations.py
**演示列表的增删改查操作**

- 添加：`append()`、`insert()`
- 删除：`remove()`、`pop()`、`del`
- 修改：`scores[0] = 90`
- 查找：`index()`、`in`
- 排序：`sort()`、`sorted()`
- 切片：`scores[:3]`、`scores[-3:]`

**关键点**：
- `sort()` 修改原列表，返回 `None`
- `sorted()` 返回新列表，不修改原列表
- `pop()` 既删除又返回被删的值

---

### 04_dict_basics.py
**演示字典的创建和基本操作**

- 创建字典：`scores = {"小北": 85, "阿码": 92}`
- 访问值：`scores["小北"]`、`scores.get("小北")`
- 添加/修改：`scores["老潘"] = 78`
- 删除：`del scores["老潘"]`、`scores.pop("阿码")`
- 检查键：`"小北" in scores`

**关键点**：
- 访问不存在的键会报 `KeyError`
- 用 `get()` 方法安全访问
- 键存在就修改，不存在就添加

---

### 05_grade_statistics.py
**演示遍历模式和数据驱动设计**

- 遍历列表：`for score in scores:`
- 遍历字典：`for name, score in scores.items():`
- `enumerate()` 同时获取索引和元素
- 找出最高分、统计及格人数
- 生成成绩单

**关键点**：
- 数据驱动设计：数据和逻辑分离
- 遍历代码是通用的，不管有多少数据
- `.items()` 遍历键值对，`.values()` 遍历值

---

### 06_pyhelper.py
**PyHelper 超级线：用字典存储学习记录**

在上周（Week 03）的基础上，添加学习记录管理功能：

**新增功能**：
- 添加学习记录（`add_record()`）
- 查看所有记录（`show_records()`）
- 统计学习天数（`show_stats()`）

**本周知识点应用**：
- 字典存储：`learning_log = {}`
- 字典操作：`learning_log[date] = content`
- 字典遍历：`for date in sorted(learning_log.keys())`
- 字典长度：`len(learning_log)`

**运行方式**：
```bash
python3 chapters/week_04/examples/06_pyhelper.py
```

---

## 运行所有示例

```bash
# 运行单个示例
python3 chapters/week_04/examples/01_scores_before.py
python3 chapters/week_04/examples/02_list_basics.py
python3 chapters/week_04/examples/03_list_operations.py
python3 chapters/week_04/examples/04_dict_basics.py
python3 chapters/week_04/examples/05_grade_statistics.py
python3 chapters/week_04/examples/06_pyhelper.py

# 或运行所有测试
python3 -m pytest chapters/week_04/tests -q
```

---

## 学习建议

1. **按顺序运行**：从 01 到 06，每个文件建立在前一个的基础上
2. **动手修改**：尝试修改代码，比如添加更多元素、修改数据
3. **预测输出**：先猜输出结果，再运行验证
4. **制造错误**：故意访问不存在的索引/键，观察报错信息
5. **对比学习**：对比列表和字典的区别，理解各自的使用场景
