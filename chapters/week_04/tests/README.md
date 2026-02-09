# Week 04 测试文档

本目录包含 Week 04（用容器装数据）的测试用例。

## 测试文件结构

```
tests/
├── __init__.py           # 测试包初始化
├── conftest.py           # 共享 fixtures 和测试配置
├── test_smoke.py         # 冒烟测试（基础功能验证）
├── test_lists.py         # 列表操作测试
├── test_dicts.py         # 字典操作测试
└── test_iteration.py     # 遍历模式测试
```

## 测试覆盖范围

### test_smoke.py - 基线测试
测试最核心的基础功能，如果这些测试失败，说明基础环境有问题。

**测试内容：**
- 列表创建和访问
- 字典创建和访问
- 基本遍历操作
- 基本容器操作（append, get, in 等）
- 内置函数（sum, max, min）
- 列表切片
- 列表排序
- 字符串和列表的可变性对比

### test_lists.py - 列表操作测试
全面测试列表（list）的各种操作。

**测试类别：**

1. **列表创建**
   - 字面量创建
   - 包含不同类型的列表
   - 列表推导式
   - 从 range 创建

2. **索引访问**
   - 正数索引
   - 负数索引
   - 索引越界错误
   - 空列表访问错误

3. **列表切片**
   - 获取前 n 个元素
   - 获取后 n 个元素
   - 获取中间范围
   - 带步长的切片
   - 反转列表
   - 切片超出范围
   - 切片返回新列表

4. **列表修改**
   - 通过索引修改元素
   - 负数索引修改

5. **列表方法**
   - `append()` - 末尾添加
   - `insert()` - 指定位置插入
   - `remove()` - 按值删除
   - `pop()` - 弹出元素
   - `index()` - 查找索引
   - `count()` - 统计出现次数
   - `sort()` - 就地排序
   - `sorted()` - 返回新列表
   - `extend()` - 扩展列表
   - `clear()` - 清空列表
   - `copy()` - 创建副本
   - `reverse()` - 反转列表

6. **列表遍历**
   - 直接遍历元素
   - 索引遍历
   - `enumerate()` 遍历

7. **列表特性**
   - 可变性
   - 作为函数参数
   - 拼接和重复
   - 布尔值
   - 比较
   - 嵌套列表

### test_dicts.py - 字典操作测试
全面测试字典（dict）的各种操作。

**测试类别：**

1. **字典创建**
   - 字面量创建
   - 构造函数创建
   - 从元组列表创建
   - 不同类型的值
   - 键的可哈希性要求
   - 空字典

2. **字典访问**
   - 通过键访问
   - 访问不存在的键（KeyError）
   - `get()` 方法
   - `get()` 的默认值
   - 区分"键不存在"和"值为 None"

3. **字典修改和添加**
   - 添加新键值对
   - 更新现有键
   - 添加和更新使用相同语法

4. **字典删除**
   - `del` 语句
   - `pop()` 方法
   - `pop()` 的默认值
   - `popitem()` 方法
   - `clear()` 方法

5. **字典查询方法**
   - `keys()` - 获取所有键
   - `values()` - 获取所有值
   - `items()` - 获取所有键值对
   - `in` 操作符检查键

6. **字典遍历**
   - 遍历键（默认）
   - 遍历值
   - 遍历键值对

7. **字典的其他方法**
   - `update()` - 更新字典
   - `setdefault()` - 设置默认值
   - `fromkeys()` - 从键创建
   - `copy()` - 创建副本

8. **字典特性**
   - 可变性
   - 键的唯一性
   - 值的可重复性
   - Python 3.7+ 的插入顺序保持
   - 布尔值
   - 比较
   - 嵌套字典
   - 字典与列表的组合

9. **字典高级操作**
   - 字典推导式
   - 作为函数参数
   - 作为函数返回值
   - 解包字典
   - 不同类型的键（元组、数字、混合）

### test_iteration.py - 遍历模式测试
测试各种遍历模式和常见的数据处理算法。

**测试类别：**

1. **列表遍历模式**
   - 直接遍历元素
   - 索引遍历（range + len）
   - `enumerate()` 遍历
   - 自定义起始值

2. **字典遍历模式**
   - 遍历键
   - 遍历值
   - 遍历键值对

3. **遍历中的条件判断**
   - 用 if 过滤
   - 计数统计
   - 字典过滤
   - 嵌套条件（elif/else）

4. **累加模式**
   - 求和
   - 计数出现次数
   - 累加字典值
   - 条件计数

5. **查找极值**
   - 查找最大值
   - 查找最小值
   - 查找带索引的极值
   - 字典中的极值

6. **搜索模式**
   - 线性搜索列表
   - 搜索字典
   - 带默认值的搜索

7. **转换模式**
   - 转换列表元素
   - 转换字典值
   - 从字典列表提取字段

8. **遍历嵌套结构**
   - 嵌套列表
   - 字典包含列表值

9. **数据驱动设计模式**
   - 查表模式
   - 配置驱动行为

10. **遍历时的副作用**
    - 遍历时修改列表的问题
    - 构建新列表（推荐做法）
    - 遍历时修改字典的问题

11. **空容器遍历**
    - 空列表遍历
    - 空字典遍历

12. **break/continue 与遍历**
    - break 提前终止
    - continue 跳过迭代
    - 用 break 实现查找

13. **for-else 子句**
    - 正常结束时执行 else
    - break 跳出不执行 else

14. **列表/字典推导式**
    - 列表推导式与 for 循环
    - 带条件的推导式
    - 字典推导式

15. **特殊遍历方式**
    - 反向遍历（reversed）
    - 排序后遍历（sorted）
    - 同时遍历多个序列（zip）
    - range 遍历

## 运行测试

### 运行所有测试
```bash
python3 -m pytest chapters/week_04/tests -q
```

### 运行特定测试文件
```bash
python3 -m pytest chapters/week_04/tests/test_lists.py -q
python3 -m pytest chapters/week_04/tests/test_dicts.py -q
python3 -m pytest chapters/week_04/tests/test_iteration.py -q
```

### 运行特定测试用例
```bash
python3 -m pytest chapters/week_04/tests/test_lists.py::test_list_creation_with_values -q
```

### 查看详细输出
```bash
python3 -m pytest chapters/week_04/tests -v
```

### 显示打印输出
```bash
python3 -m pytest chapters/week_04/tests -s
```

### 运行并显示覆盖率（需要安装 pytest-cov）
```bash
python3 -m pytest chapters/week_04/tests --cov=chapters/week_04/starter_code --cov-report=term-missing
```

## 测试统计

| 测试文件 | 测试用例数 | 覆盖范围 |
|---------|----------|---------|
| test_smoke.py | 40+ | 基础操作 |
| test_lists.py | 90+ | 列表全面操作 |
| test_dicts.py | 80+ | 字典全面操作 |
| test_iteration.py | 70+ | 遍历模式 |
| **总计** | **280+** | **Week 04 全部内容** |

## Fixtures 说明

`conftest.py` 提供了以下共享 fixtures：

### 列表相关
- `empty_list` - 空列表
- `sample_scores_list` - 示例成绩列表
- `sample_scores_with_duplicates` - 包含重复值的列表
- `single_element_list` - 单元素列表
- `mixed_type_list` - 混合类型列表
- `list_for_slicing` - 用于切片测试的列表

### 字典相关
- `empty_dict` - 空字典
- `sample_scores_dict` - 示例成绩字典
- `sample_config_dict` - 示例配置字典
- `dict_with_none_values` - 包含 None 值的字典
- `learning_log_dict` - 学习记录字典

### 边界值
- `boundary_indices` - 边界索引值
- `special_values` - 特殊值（0, "", None, False, -1, 3.14）

### 切片测试
- `slicing_test_cases` - 切片测试用例列表

### 排序测试
- `unsorted_scores` - 未排序的成绩
- `sorted_ascending` - 升序排序结果
- `sorted_descending` - 降序排序结果

### 遍历测试
- `list_for_iteration` - 用于遍历的列表
- `dict_for_iteration` - 用于遍历的字典

### 成绩统计
- `grade_boundary_scores` - 等级边界成绩

### PyHelper 测试
- `valid_moods` - 有效心情选项
- `valid_menu_choices` - 有效菜单选项
- `mood_advice_mapping` - 心情建议映射

### 错误测试
- `invalid_indices` - 无效索引
- `missing_keys` - 缺失的键

## 测试设计原则

1. **正例测试（Happy Path）**
   - 测试正常的、预期的使用场景
   - 验证功能按预期工作

2. **边界测试（Edge Cases）**
   - 空列表/空字典
   - 单元素
   - 极端值（很大/很小的数）
   - 边界索引（0, -1, len-1）

3. **反例测试（Negative Cases）**
   - 错误输入
   - 异常情况
   - 不应该通过的场景

4. **清晰的测试命名**
   - 格式：`test_<功能>_<场景>_<预期结果>`
   - 示例：`test_remove_nonexistent_raises_error`

5. **独立性**
   - 每个测试独立运行
   - 不依赖其他测试的结果
   - 使用 fixtures 提供测试数据

## 测试与章节内容的对应关系

### 章节 1：从 10 个变量到 1 个列表
- ✓ 列表创建
- ✓ 索引访问
- ✓ 列表修改
- ✓ 可变性 vs 不可变性
- ✓ `append()` 和 `remove()`

### 章节 2：让列表动起来
- ✓ `insert()` 插入元素
- ✓ `pop()` 弹出元素
- ✓ `del` 语句
- ✓ `in` 和 `index()`
- ✓ 列表切片
- ✓ `sort()` 和 `sorted()`

### 章节 3：从"按位置找"到"按名字找"
- ✓ 字典创建
- ✓ 通过键访问
- ✓ `get()` 方法
- ✓ 添加和更新
- ✓ `del` 和 `pop()`
- ✓ `keys()`, `values()`, `items()`

### 章节 4：让数据自己说话
- ✓ 遍历列表
- ✓ 遍历字典
- ✓ `enumerate()`
- ✓ 遍历中的条件判断
- ✓ 查找极值
- ✓ 统计计数
- ✓ 数据驱动设计

## 常见问题

### Q: 测试失败怎么办？
1. 查看失败的测试用例名称，了解具体哪个功能有问题
2. 运行 `pytest -v` 查看详细错误信息
3. 检查是测试本身的问题还是实现有问题
4. 如果是 starter_code/solution.py 未实现，需要在测试中加注释说明

### Q: 如何添加新测试？
1. 确定要测试的功能点
2. 选择合适的测试文件（test_lists.py, test_dicts.py, 或 test_iteration.py）
3. 按照命名规范编写测试函数
4. 运行测试确保通过

### Q: fixtures 如何使用？
Fixtures 定义在 `conftest.py` 中，在测试函数中直接作为参数使用：

```python
def test_example(sample_scores_list):
    assert len(sample_scores_list) == 5
```

### Q: 如何测试异常情况？
使用 `pytest.raises()`：

```python
def test_index_error():
    with pytest.raises(IndexError):
        _ = [1, 2, 3][10]
```

## 贡献指南

添加新测试时请遵循：
1. 清晰的测试命名
2. 充分的测试覆盖（正例、边界、反例）
3. 使用 fixtures 避免重复代码
4. 添加必要的注释说明测试目的
5. 确保测试独立运行

## 参考资源

- [Pytest 官方文档](https://docs.pytest.org/)
- Week 03 测试（参考模式）
- CHAPTER.md（了解本周教学内容）
- ASSIGNMENT.md（了解作业要求）
