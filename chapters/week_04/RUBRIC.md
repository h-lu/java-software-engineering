# Week 04 评分标准

## 总分：100 分（基础 40 + 进阶 30 + 挑战 30）

---

## 基础作业（40 分）

### 1. 成绩列表操作 (15 分)

| 评分项 | 分值 | 评分标准 |
|--------|------|----------|
| `add_score` 功能正确 | 5 分 | 正确添加成绩到列表末尾，使用 `append()` 方法 |
| `remove_failing_scores` 功能正确 | 6 分 | 正确删除所有 < 60 分的元素，处理边界情况（空列表、全及格、全不及格） |
| 不在遍历时修改列表 | 2 分 | 没有在 `for` 循环中直接 `remove()`，避免跳过元素 |
| `get_top_scores` 功能正确 | 5 分 | 返回前 n 个最高分（降序），处理 n > len(scores) 的情况 |
| 代码可读性 | 2 分 | 变量命名清晰，有必要的注释 |

**常见扣分点**：
- 在 `for score in scores:` 循环里 `scores.remove(score)` → -2 分
- `result = scores.sort()` → -1 分（`sort()` 返回 `None`）
- 没有处理空列表边界情况 → -1 分
- 直接修改输入列表而非返回新列表（如 `get_top_scores`）→ -1 分

**自动化测试覆盖**：
```python
def test_add_score():
    scores = [85, 92]
    result = add_score(scores, 78)
    assert result == [85, 92, 78]

def test_remove_failing_scores():
    scores = [85, 45, 92, 58, 78]
    result = remove_failing_scores(scores)
    assert result == [85, 92, 78]

def test_remove_failing_empty():
    result = remove_failing_scores([])
    assert result == []

def test_get_top_scores():
    scores = [85, 92, 78, 90, 88]
    result = get_top_scores(scores, 3)
    assert result == [92, 90, 88]
```

---

### 2. 字典查询与统计 (15 分)

| 评分项 | 分值 | 评分标准 |
|--------|------|----------|
| `get_student_score` 功能正确 | 4 分 | 存在的键返回对应值，不存在的键返回 `None`（不报错） |
| 使用 `.get()` 方法或 `in` 判断 | 2 分 | 安全访问字典，避免 `KeyError` |
| `count_pass_fail` 功能正确 | 5 分 | 正确统计及格/不及格人数，返回格式正确的字典 |
| `find_top_student` 功能正确 | 4 分 | 正确找出最高分学生，处理空字典返回 `None` |
| 代码可读性 | 2 分 | 变量命名清晰，有必要的注释 |

**常见扣分点**：
- 用 `scores_dict[name]` 访问不存在的键，导致 `KeyError` → -2 分
- `count_pass_fail` 返回格式错误（如返回元组而非字典）→ -2 分
- `find_top_student` 没有处理空字典 → -2 分
- 遍历字典时误以为遍历的是"值"（实际是"键"）→ -1 分

**自动化测试覆盖**：
```python
def test_get_student_score_exists():
    scores = {"小北": 85, "阿码": 92}
    result = get_student_score(scores, "小北")
    assert result == 85

def test_get_student_score_not_exists():
    scores = {"小北": 85, "阿码": 92}
    result = get_student_score(scores, "小红")
    assert result is None

def test_count_pass_fail():
    scores = {"小北": 85, "阿码": 92, "老潘": 45}
    result = count_pass_fail(scores, 60)
    assert result == {"pass": 2, "fail": 1}

def test_find_top_student():
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    result = find_top_student(scores)
    assert result == "阿码"
```

---

### 3. 成绩单生成 (10 分)

| 评分项 | 分值 | 评分标准 |
|--------|------|----------|
| 输出格式正确 | 5 分 | 包含标题、"名字: 分数 (等级)" 格式、换行符正确 |
| 等级判断逻辑正确 | 3 分 | 90+ 优秀、80-89 良好、60-79 及格、<60 不及格 |
| 使用 `.items()` 遍历字典 | 1 分 | 正确使用 `for name, score in scores_dict.items()` |
| 代码可读性 | 1 分 | 变量命名清晰，字符串拼接逻辑易懂 |

**常见扣分点**：
- 缺少"=== 成绩单 ==="标题 → -1 分
- 等级判断边界错误（如 90 分判为"良好"）→ -2 分
- 缺少换行符或多余换行 → -1 分
- 用 `for name in scores_dict:` 然后手动 `score = scores_dict[name]`（不扣分但不够优雅）

**自动化测试覆盖**：
```python
def test_generate_report():
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    result = generate_report(scores)
    assert "=== 成绩单 ===" in result
    assert "小北: 85 (良好)" in result
    assert "阿码: 92 (优秀)" in result
    assert "老潘: 78 (及格)" in result
```

---

## 进阶作业（30 分）

### 4. 成绩分析器 (30 分)

| 评分项 | 分值 | 评分标准 |
|--------|------|----------|
| `analyze_scores` 功能正确 | 15 分 | 正确计算所有统计项，平均分保留 1 位小数 |
| 处理空列表边界情况 | 3 分 | 空列表返回 `{"count": 0, "average": 0, ...}` 或类似合理值 |
| `group_by_grade` 功能正确 | 10 分 | 正确按等级分组，返回嵌套字典结构 |
| 代码质量 | 2 分 | 逻辑清晰，无重复代码，适当使用内置函数 |

**常见扣分点**：
- `analyze_scores` 的 `pass_rate` 计算错误（如除零、整数除法）→ -3 分
- 平均分没有保留小数（如 77.6 变成 78）→ -1 分
- `group_by_grade` 没有初始化所有等级的列表 → -3 分
- 直接修改输入字典而非返回新字典 → -2 分

**自动化测试覆盖**：
```python
def test_analyze_scores():
    scores = [85, 92, 78, 45, 88]
    result = analyze_scores(scores)
    assert result["count"] == 5
    assert abs(result["average"] - 77.6) < 0.1
    assert result["max"] == 92
    assert result["min"] == 45
    assert result["pass_rate"] == 0.8

def test_analyze_empty():
    result = analyze_scores([])
    assert result["count"] == 0

def test_group_by_grade():
    scores = {"小北": 85, "阿码": 92, "老潘": 78, "小红": 58}
    result = group_by_grade(scores)
    assert result["优秀"] == ["阿码"]
    assert result["良好"] == ["小北"]
    assert result["及格"] == ["老潘"]
    assert result["不及格"] == ["小红"]
```

---

## 挑战作业（30 分）

### 5. 多班级成绩管理系统 (30 分)

| 评分项 | 分值 | 评分标准 |
|--------|------|----------|
| 数据结构设计合理 | 5 分 | 使用嵌套字典结构（学校 → 班级 → 学生 → 成绩） |
| `add_class` 功能正确 | 5 分 | 正确添加班级，处理班级已存在的情况（覆盖或报错均可） |
| `get_class_average` 功能正确 | 5 分 | 正确计算班级平均分，班级不存在返回 `None` |
| `compare_classes` 功能正确 | 8 分 | 格式化输出正确，处理相等、班级不存在等情况 |
| `find_top_class` 功能正确 | 5 分 | 正确找出平均分最高的班级，处理空 `school` |
| 代码质量与模块化 | 2 分 | 提取公共逻辑（如计算平均分的辅助函数），避免重复代码 |

**常见扣分点**：
- 没有检查"班级是否存在"就访问，导致 `KeyError` → -3 分
- `compare_classes` 输出格式不符合要求 → -2 分
- 重复计算平均分（如多次遍历同一个字典）→ -1 分
- 没有处理空 `school` 或班级不存在的情况 → -2 分/处

**自动化测试覆盖**：
```python
def test_add_class():
    school = {}
    add_class(school, "一班", {"小北": 85, "阿码": 92})
    assert "一班" in school
    assert school["一班"]["小北"] == 85

def test_get_class_average():
    school = {"一班": {"小北": 85, "阿码": 92}, "二班": {"老潘": 78}}
    result = get_class_average(school, "一班")
    assert abs(result - 88.5) < 0.1

def test_compare_classes():
    school = {"一班": {"小北": 85}, "二班": {"老潘": 78}}
    result = compare_classes(school, "一班", "二班")
    assert "一班 比 二班 高 7.0 分" == result

def test_find_top_class():
    school = {"一班": {"小北": 85, "阿码": 70}, "二班": {"老潘": 78}}
    result = find_top_class(school)
    assert result in ["一班", "二班"]  # 一班平均 77.5，二班 78，应返回"二班"
```

---

## AI 协作练习（可选，+10 分）

| 评分项 | 分值 | 评分标准 |
|--------|------|----------|
| 发现问题的数量和质量 | 4 分 | 找出至少 2 个有意义的问题（命名、边界、性能等） |
| 问题分析准确 | 3 分 | 每个问题有 1-2 句话准确说明为什么是问题 |
| 修复方案合理 | 2 分 | 修复后的代码解决了发现的问题 |
| 测试用例有效 | 1 分 | 测试用例能证明修复有效或暴露原代码的问题 |

**AI 代码参考问题**（供评分者参考）：

1. **命名问题**：函数名叫 `remove_duplicates` 但参数叫 `scores`，暗示只能处理成绩，不够通用
2. **边界情况**：如果输入是 `None` 或非列表类型，会报错
3. **性能问题**：`if score not in result` 的时间复杂度是 O(n²)，对于大列表很慢
4. **类型安全**：没有检查输入类型，`for score in scores` 如果 scores 不是可迭代对象会崩溃

**优秀回答示例**：
```
问题1：时间复杂度 O(n²)。每次 `not in result` 都要遍历 result 列表，
     10000 个元素需要 5000 万次比较。优化：用集合存储已见过的元素。

问题2：输入类型不安全。如果 scores 是 None 或整数，会报 TypeError。
     修复：添加类型检查或 try/except。

修复后：
def remove_duplicates(items):
    if not isinstance(items, list):
        return []
    seen = set()
    result = []
    for item in items:
        if item not in seen:
            seen.add(item)
            result.append(item)
    return result

测试：
remove_duplicates(None)  # 返回 []，不崩溃
remove_duplicates([1,2,1,2,3] * 10000)  # 速度快，O(n)
```

---

## 总体评分原则

### 代码质量（-2 分/严重问题）

- 变量命名混乱（如 `a`, `tmp`, `x1`）
- 缺少必要的注释（复杂逻辑需要注释说明）
- 重复代码（相同逻辑出现 3 次以上）
- 没有遵循 PEP 8 基本规范（如缩进、空格）

### 测试通过情况（优先）

- 所有自动化测试通过 → 按上述标准给分
- 部分测试失败 → 每个失败的测试扣 2-5 分（视重要性）
- 完全无法运行 → 最多给 30%（手动评分代码逻辑）

### 学习目标达成度

- **基础作业**：掌握列表和字典的基本操作（CRUD、遍历）
- **进阶作业**：能用列表和字典解决实际问题，考虑边界情况
- **挑战作业**：理解嵌套数据结构，能用数据驱动设计简化代码

---

## 加分项

- **代码优雅**：使用 Pythonic 写法（如列表推导、内置函数）→ +2 分
- **完整测试**：自己写的测试用例覆盖全面 → +2 分
- **AI 练习**：AI 协作练习质量高 → +10 分
- **提前完成**：在截止日期前提交且有质量 → +2 分

---

## 最终等级转换

| 分数范围 | 等级 | 说明 |
|----------|------|------|
| 90-100 | A | 优秀，完全掌握本周内容 |
| 80-89 | B | 良好，理解主要概念 |
| 70-79 | C | 及格，基本掌握但需巩固 |
| 60-69 | D | 勉强及格，有明显薄弱点 |
| <60 | F | 不及格，需要重新学习 |

注：AI 协作练习的 +10 分是额外奖励，不计入总分上限（即可以超过 100 分）。
