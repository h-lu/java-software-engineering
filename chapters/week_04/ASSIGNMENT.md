# Week 04 作业：用容器装数据

> **本周目标**：掌握列表和字典的基础操作，能用数据结构简化代码逻辑。

**作业截止时间**：下次上课前
**提交方式**：在 `chapters/week_04/` 目录下完成 `starter_code/solution.py`，运行 `pytest` 验证通过

---

## 基础作业（必做，40 分）

### 1. 成绩列表操作 (15 分)

实现以下函数，完成成绩列表的常见操作：

```python
def add_score(scores, new_score):
    """
    添加一个新成绩到列表末尾

    输入：scores 是成绩列表，new_score 是要添加的成绩
    输出：添加后的列表（原地修改，返回列表本身）
    示例：
        add_score([85, 92], 78) -> [85, 92, 78]
    """
    pass

def remove_failing_scores(scores):
    """
    删除所有不及格的成绩（< 60 分）

    注意：不要在遍历列表的同时修改它！
    提示：可以创建一个新列表，或者用 while 循环

    输入：scores 是成绩列表
    输出：删除不及格成绩后的列表
    示例：
        remove_failing_scores([85, 45, 92, 58, 78]) -> [85, 92, 78]
    """
    pass

def get_top_scores(scores, n=3):
    """
    获取前 n 个最高分

    输入：scores 是成绩列表，n 是要返回的个数
    输出：包含前 n 个最高分的新列表（降序）
    示例：
        get_top_scores([85, 92, 78, 90, 88], 3) -> [92, 90, 88]
    边界情况：
        - 如果 n > len(scores)，返回所有成绩降序排列
        - 如果 scores 为空，返回空列表
    """
    pass
```

**常见错误提示**：
- `remove_failing_scores` 中如果在 `for` 循环里 `remove()`，会导致跳过元素
- `sort()` 返回 `None`，不要直接 `return scores.sort()`
- 记得处理空列表的边界情况

---

### 2. 字典查询与统计 (15 分)

实现学生成绩字典的查询和统计功能：

```python
def get_student_score(scores_dict, name):
    """
    获取学生的成绩

    输入：scores_dict 是 {"名字": 分数} 的字典，name 是学生名字
    输出：如果学生存在，返回成绩；否则返回 None
    示例：
        get_student_score({"小北": 85, "阿码": 92}, "小北") -> 85
        get_student_score({"小北": 85, "阿码": 92}, "小红") -> None
    """
    pass

def count_pass_fail(scores_dict, passing_score=60):
    """
    统计及格和不及格人数

    输入：scores_dict 是成绩字典，passing_score 是及格线
    输出：一个字典 {"pass": 及格人数, "fail": 不及格人数}
    示例：
        count_pass_fail({"小北": 85, "阿码": 92, "老潘": 45}, 60)
        -> {"pass": 2, "fail": 1}
    """
    pass

def find_top_student(scores_dict):
    """
    找出最高分的学生

    输入：scores_dict 是成绩字典
    输出：最高分学生的名字
    示例：
        find_top_student({"小北": 85, "阿码": 92, "老潘": 78}) -> "阿码"
    边界情况：
        - 如果字典为空，返回 None
        - 如果有多人并列第一，返回其中任意一个即可
    """
    pass
```

**常见错误提示**：
- 用 `scores_dict[name]` 访问不存在的键会报 `KeyError`，改用 `.get()` 方法
- 遍历字典时，默认遍历的是"键"，不是"值"
- `find_top_student` 可以用 `max(scores_dict, key=scores_dict.get)` 的思路

---

### 3. 成绩单生成 (10 分)

生成格式化的成绩单：

```python
def generate_report(scores_dict):
    """
    生成格式化的成绩单

    输入：scores_dict 是成绩字典
    输出：一个字符串，包含每个学生的成绩和等级

    等级规则：
        - 90+ : 优秀
        - 80-89: 良好
        - 60-79: 及格
        - <60  : 不及格

    输出格式（注意换行符）：
        === 成绩单 ===
        小北: 85 (良好)
        阿码: 92 (优秀)
        老潘: 78 (及格)

    示例：
        report = generate_report({"小北": 85, "阿码": 92, "老潘": 78})
        print(report)  # 输出上面的格式化文本
    """
    pass
```

**提示**：
- 用字符串拼接或 f-string 构建每一行
- 可以用 `for name, score in scores_dict.items():` 遍历
- 用 `if/elif/else` 判断等级

---

## 进阶作业（选做，30 分）

### 4. 成绩分析器 (30 分)

实现一个完整的成绩分析工具：

```python
def analyze_scores(scores_list):
    """
    分析成绩列表，返回统计信息

    输入：scores_list 是成绩列表
    输出：一个字典，包含以下统计信息：
        {
            "count": 总人数,
            "average": 平均分（保留1位小数）,
            "max": 最高分,
            "min": 最低分,
            "pass_rate": 及格率（小数，如 0.8 表示 80%）
        }

    示例：
        analyze_scores([85, 92, 78, 45, 88])
        -> {
            "count": 5,
            "average": 77.6,
            "max": 92,
            "min": 45,
            "pass_rate": 0.8  # 4/5 = 0.8
        }

    边界情况：
        - 如果列表为空，返回所有统计值为 0 或 None
    """
    pass

def group_by_grade(scores_dict):
    """
    按等级分组学生

    输入：scores_dict 是成绩字典
    输出：一个嵌套字典，按等级存储学生列表

    示例：
        group_by_grade({"小北": 85, "阿码": 92, "老潘": 78, "小红": 58})
        -> {
            "优秀": ["阿码"],
            "良好": ["小北"],
            "及格": ["老潘"],
            "不及格": ["小红"]
        }
    """
    pass
```

**提示**：
- `analyze_scores` 可以用 `sum()`、`len()`、`max()`、`min()` 等内置函数
- `group_by_grade` 需要先初始化四个等级的空列表，再遍历填充
- 字典的值可以是列表，如 `groups["优秀"].append(name)`

---

## 挑战作业（选做，30 分）

### 5. 多班级成绩管理系统 (30 分)

实现一个能管理多个班级成绩的系统：

```python
def add_class(school, class_name, scores_dict):
    """
    添加一个班级到学校数据结构中

    输入：
        - school: 学校数据结构（嵌套字典）
        - class_name: 班级名称（如 "一班"）
        - scores_dict: 该班级的成绩字典

    输出：更新后的 school 字典

    数据结构设计：
        school = {
            "一班": {"小北": 85, "阿码": 92},
            "二班": {"老潘": 78, "小红": 90}
        }
    """
    pass

def get_class_average(school, class_name):
    """
    获取某个班级的平均分

    输入：
        - school: 学校数据结构
        - class_name: 班级名称

    输出：该班级的平均分（保留1位小数）
    边界情况：
        - 如果班级不存在，返回 None
    """
    pass

def compare_classes(school, class1, class2):
    """
    比较两个班级的平均分

    输入：
        - school: 学校数据结构
        - class1, class2: 要比较的班级名称

    输出：一个字符串，格式为 "X班 比 Y班 高 Z 分" 或 "X班 和 Y班 平均分相同"

    示例：
        compare_classes(
            {"一班": {"小北": 85}, "二班": {"老潘": 78}},
            "一班", "二班"
        )
        -> "一班 比 二班 高 7.0 分"
    """
    pass

def find_top_class(school):
    """
    找出平均分最高的班级

    输入：school 是学校数据结构
    输出：平均分最高的班级名称
    边界情况：
        - 如果 school 为空，返回 None
        - 如果有多个班级并列第一，返回任意一个
    """
    pass
```

**提示**：
- 这里的数据结构是"嵌套字典"：`学校 -> 班级 -> 学生 -> 成绩`
- 每个函数都要先检查"键是否存在"，用 `.get()` 或 `in`
- 可以用上周学的"函数封装"思想，把重复逻辑（如计算平均分）提取出来

---

## AI 协作练习（可选，不扣分）

下面这段代码是某个 AI 工具生成的"删除列表中重复元素"的功能。请审查它：

```python
# AI 生成的代码（可能有 2-3 个问题）
def remove_duplicates(scores):
    """删除列表中的重复元素"""
    result = []
    for score in scores:
        if score not in result:
            result.append(score)
    return result
```

### 审查清单

请检查以下问题：

- [ ] **代码能运行吗？** 试着运行一下，有没有报错
- [ ] **变量命名清晰吗？** `scores` 这个名字准确吗？如果输入不是成绩呢？
- [ ] **有没有缺少错误处理的地方？** 如果输入 `None` 会怎样？如果输入不是列表呢？
- [ ] **边界情况处理了吗？** 空列表会怎样？只有一个元素会怎样？
- [ ] **你能写一个让它失败的测试吗？** 构造一个输入，让代码产生错误结果
- [ ] **时间复杂度合理吗？** 如果列表有 10000 个元素，这段代码会慢吗？为什么？

### 你的任务

1. 找出至少 2 个问题（bug、命名、边界情况、性能等）
2. 修复这些问题，写出改进后的代码
3. 写一个测试用例，证明修复是有效的

**提交物**（和基础作业分开，作为附加材料）：
- 你发现的问题列表（每问题 1-2 句话说明）
- 修复后的代码
- 一个测试用例

**注意**：这个练习是"审查 AI"，不是"用 AI 写作业"。你必须理解代码才能发现问题。

---

## 提交与验证

### 验证步骤

1. 完成函数实现后，在项目根目录运行：
   ```bash
   python3 -m pytest chapters/week_04/tests -q
   ```

2. 如果测试失败，查看报错信息，修改代码后重新运行

3. 所有测试通过后，提交你的代码：
   ```bash
   git add chapters/week_04/starter_code/solution.py
   git commit -m "week 04: 完成列表和字典练习"
   ```

### 评分标准

- **基础作业**（40 分）：功能正确性为主，通过测试用例
- **进阶作业**（30 分）：考虑边界情况，代码简洁
- **挑战作业**（30 分）：数据结构设计合理，逻辑清晰
- **AI 协作练习**（可选）：+10 分额外奖励

### 遇到困难？

如果你遇到困难，可以参考 `starter_code/solution.py` 中的参考实现。但请先自己尝试，再看答案。直接复制答案无法帮助你真正理解。

---

## 学习目标检查

完成本周作业后，你应该能：

- [ ] 创建、访问、修改列表元素
- [ ] 使用列表的 `append()`、`remove()`、`sort()` 等方法
- [ ] 理解切片操作，如 `scores[:3]`、`scores[-3:]`
- [ ] 创建、访问、修改字典的键值对
- [ ] 使用 `.get()` 方法安全访问字典
- [ ] 用 `for` 循环遍历列表和字典
- [ ] 用 `enumerate()` 同时获取索引和元素
- [ ] 判断"什么时候用列表，什么时候用字典"
- [ ] 理解"数据驱动设计"的基本思想

如果以上某项你不确定，建议重新阅读对应章节的示例代码。
