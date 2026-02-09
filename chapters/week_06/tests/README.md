# Week 06 测试说明

## 概述

本目录包含 Week 06（异常处理与防御性编程）的所有测试用例。

## 测试文件

### 1. `test_smoke.py` - 基线测试

基础的"冒烟测试"，确保最核心的异常处理概念能正常工作。

**测试覆盖**：
- 基本的异常类型（ValueError, TypeError, ZeroDivisionError, KeyError, IndexError）
- 基本的 try/except 结构
- 特定异常的捕获
- else 和 finally 子句
- LBYL vs EAFP 两种编程风格
- raise 语句
- 嵌套的异常处理
- 输入验证的 LBYL 风格
- 基本的文件异常
- 自定义异常的基本概念

**运行方式**：
```bash
python3 -m pytest chapters/week_06/tests/test_smoke.py -v
```

### 2. `test_week06.py` - 综合测试

完整的测试套件，覆盖本周所有知识点。

**测试覆盖**：
1. **异常类型识别**（7 个测试）
   - ValueError、TypeError、ZeroDivisionError
   - KeyError、IndexError、FileNotFoundError

2. **safe_divide 函数**（13 个测试）
   - 正例：正常除法、负数、浮点数
   - 边界：除以 -1、0 除以非零、极大/极小数
   - 反例：除以零、字符串输入、None 输入

3. **字典访问（EAFP）**（5 个测试）
   - 正例：获取存在的键
   - 边界：空字典、None 默认值、各种类型

4. **列表访问（EAFP）**（7 个测试）
   - 正例：有效索引、首/尾元素
   - 边界：负数索引、空列表
   - 反例：索引越界（正/负）

5. **输入校验（LBYL）**（20+ 个测试）
   - is_positive_integer：有效/无效数字、非字符串
   - is_valid_age：有效/无效年龄、边界值、自定义范围
   - is_valid_date_format：有效/无效日期格式

6. **try/except/else/finally**（9 个测试）
   - except 捕获异常
   - else 执行条件
   - finally 总是执行
   - 多个 except 块
   - 多种异常类型
   - 异常作为变量

7. **自定义异常**（9 个测试）
   - InvalidInputError 和 OutOfRangeError
   - validate_age 函数的各种情况

8. **文件操作**（8 个测试）
   - safe_write_file 和 safe_read_file
   - Unicode 内容
   - 空文件
   - 多行内容

9. **LBYL vs EAFP 对比**（6 个测试）
   - 字典访问的两种风格
   - 输入验证的 LBYL 风格

10. **带重试机制的函数**（6 个测试）
    - get_positive_integer_with_retry
    - get_choice_with_retry
    - 超过最大尝试次数

11. **边界情况**（10+ 个测试）
    - 浮点数精度
    - 非常小的除数
    - None 值
    - 前导零
    - 空字符串
    - Unicode 字符串

12. **异常传播**（3 个测试）
    - 异常向上传播
    - 捕获后重新抛出
    - 异常链

13. **性能测试**（2 个测试）
    - LBYL 检查两次 vs EAFP 一次操作
    - 异常的开销

**运行方式**：
```bash
python3 -m pytest chapters/week_06/tests/test_week06.py -v
```

## 测试分类

### 按测试类型

| 类型 | 说明 | 示例 |
|------|------|------|
| 正例 | 正常输入，期望正确输出 | 输入 10 和 2，期望结果 5.0 |
| 边界 | 边界值 | 输入 0、-1、极大/极小数 |
| 反例 | 错误输入，期望异常或默认值 | 输入 "abc"，期望 ValueError |

### 按知识点

| 知识点 | 测试文件 | 测试数量 |
|--------|---------|---------|
| 异常类型识别 | test_smoke.py, test_week06.py | 13 |
| try/except 结构 | test_smoke.py, test_week06.py | 22 |
| EAFP 风格 | test_week06.py | 12 |
| LBYL 风格 | test_smoke.py, test_week06.py | 26 |
| 输入校验函数 | test_week06.py | 20+ |
| 自定义异常 | test_smoke.py, test_week06.py | 12 |
| 文件操作 | test_week06.py | 8 |
| 重试机制 | test_week06.py | 6 |
| 边界情况 | test_week06.py | 10+ |
| 异常传播 | test_week06.py | 3 |
| 性能考虑 | test_week06.py | 2 |

## 运行测试

### 运行所有测试

```bash
python3 -m pytest chapters/week_06/tests/ -v
```

### 运行特定文件

```bash
# 只运行基线测试
python3 -m pytest chapters/week_06/tests/test_smoke.py -v

# 只运行综合测试
python3 -m pytest chapters/week_06/tests/test_week06.py -v
```

### 运行特定测试类

```bash
# 只测试 safe_divide 相关的测试
python3 -m pytest chapters/week_06/tests/test_week06.py::TestSafeDivide -v

# 只测试输入校验相关的测试
python3 -m pytest chapters/week_06/tests/test_week06.py::TestInputValidationLBYL -v
```

### 运行特定测试方法

```bash
# 只测试除以零的情况
python3 -m pytest chapters/week_06/tests/test_week06.py::TestSafeDivide::test_safe_divide_by_zero_returns_none -v
```

### 按标记运行

```bash
# 运行所有标记为 exception 的测试
python3 -m pytest chapters/week_06/tests/ -m exception -v

# 运行所有标记为 validation 的测试
python3 -m pytest chapters/week_06/tests/ -m validation -v

# 运行所有标记为 lbyl 的测试
python3 -m pytest chapters/week_06/tests/ -m lbyl -v

# 运行所有标记为 eafp 的测试
python3 -m pytest chapters/week_06/tests/ -m eafp -v

# 运行所有标记为 edge 的测试
python3 -m pytest chapters/week_06/tests/ -m edge -v

# 排除慢速测试
python3 -m pytest chapters/week_06/tests/ -m "not slow" -v
```

### 查看测试覆盖率

```bash
# 安装 pytest-cov（如果未安装）
pip install pytest-cov

# 运行测试并生成覆盖率报告
python3 -m pytest chapters/week_06/tests/ --cov=chapters/week_06/starter_code --cov-report=term-missing
```

## 测试命名规范

测试命名遵循清晰、描述性的原则：

```
test_<被测试函数>_<场景>_<预期结果>
```

示例：
- `test_safe_divide_normal_case` - 测试正常情况
- `test_safe_divide_by_zero_returns_none` - 测试除以零返回 None
- `test_safe_divide_string_input_raises_value_error` - 测试字符串输入抛出 ValueError

## Fixture 说明

`conftest.py` 中定义了以下共享 fixture：

| Fixture | 返回值 | 说明 |
|---------|-------|------|
| `sample_scores_dict` | `dict` | 示例成绩字典 |
| `sample_numbers_list` | `list` | 示例数字列表 |
| `sample_mixed_list` | `list` | 包含各种类型的列表 |
| `sample_mixed_dict` | `dict` | 包含各种类型的字典 |
| `valid_date_strings` | `list` | 有效的日期字符串列表 |
| `invalid_date_strings` | `list` | 无效的日期字符串列表 |
| `valid_age_strings` | `list` | 有效的年龄字符串列表 |
| `invalid_age_strings` | `list` | 无效的年龄字符串列表 |
| `valid_positive_integer_strings` | `list` | 有效的正整数字符串列表 |
| `invalid_positive_integer_strings` | `list` | 无效的正整数字符串列表 |

## 测试最佳实践

1. **独立性**：每个测试应该独立运行，不依赖其他测试的结果
2. **清晰性**：测试名称和断言应该清晰表达测试意图
3. **完整性**：覆盖正例、边界、反例
4. **可维护性**：使用 fixture 减少重复代码

## 常见问题

### Q: 测试失败了怎么办？

A: 首先区分是"测试本身写错了"还是"solution.py 实现有问题"：
- 如果是测试写错了，修复测试
- 如果是 solution.py 没实现，在测试文件中加注释说明预期行为

### Q: 如何跳过某些测试？

A: 使用 `@pytest.mark.skip` 或 `@pytest.mark.skipif` 装饰器：

```python
@pytest.mark.skip("尚未实现")
def test_something():
    pass

@pytest.mark.skipif(sys.version_info < (3, 8), reason="需要 Python 3.8+")
def test_python38_feature():
    pass
```

### Q: 如何预期测试会失败？

A: 使用 `@pytest.mark.xfail` 装饰器：

```python
@pytest.mark.xfail(reason="已知问题")
def test_known_issue():
    assert 1 == 2
```

## 贡献指南

添加新测试时：
1. 遵循测试命名规范
2. 包含正例、边界、反例（如适用）
3. 使用现有的 fixture（如果合适）
4. 添加适当的测试标记
5. 更新本 README 文档

## 参考资源

- [pytest 官方文档](https://docs.pytest.org/)
- [Python 异常处理文档](https://docs.python.org/3/tutorial/errors.html)
- Week 06 CHAPTER.md - 理论知识
- Week 06 starter_code/solution.py - 参考实现
