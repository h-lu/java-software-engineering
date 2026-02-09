# Week 06 示例代码清单

本目录包含 Week 06（异常处理与防御性编程）的所有示例代码。

## 示例文件列表

### 第 1 节：程序为什么崩溃

| 文件 | 说明 | 运行方式 |
|------|------|----------|
| `01_crashing_calculator.py` | 会崩溃的除法器（演示异常的抛出） | `python3 01_crashing_calculator.py` |

**测试建议**：
- 正常输入：10, 2 → 输出 5.0
- 除以零：10, 0 → ZeroDivisionError
- 非数字输入：abc → ValueError

---

### 第 2 节：捕获异常——try/except 结构

| 文件 | 说明 | 运行方式 |
|------|------|----------|
| `02_catch_all_exceptions.py` | 用 `except:` 捕获所有异常（不推荐） | `python3 02_catch_all_exceptions.py` |
| `03_specific_exceptions.py` | 捕获特定异常（推荐） | `python3 03_specific_exceptions.py` |
| `04_multiple_exceptions.py` | 一次捕获多个异常 | `python3 04_multiple_exceptions.py` |
| `05_safe_divider.py` | 安全的除法器（函数封装版） | `python3 05_safe_divider.py` |

**关键对比**：
- `02_catch_all_exceptions.py`：用裸的 `except:`，会掩盖错误信息
- `03_specific_exceptions.py`：分别捕获 ValueError 和 ZeroDivisionError，给出清晰的提示

---

### 第 3 节：else 和 finally——完整的异常处理

| 文件 | 说明 | 运行方式 |
|------|------|----------|
| `06_else_clause.py` | else 子句的使用 | `python3 06_else_clause.py` |
| `07_finally_cleanup.py` | finally 用于资源清理 | `python3 07_finally_cleanup.py` |
| `08_with_and_finally.py` | with 和 finally 一起用 | `python3 08_with_and_finally.py` |
| `09_complete_exception_handler.py` | 完整的 try/except/else/finally | `python3 09_complete_exception_handler.py` |

**执行流程**：
- 成功时：try → else → finally
- 失败时：try → except → finally

---

### 第 4 节：LBYL vs EAFP——两种编程哲学

| 文件 | 说明 | 运行方式 |
|------|------|----------|
| `10_eafp_dict.py` | EAFP 风格访问字典 | `python3 10_eafp_dict.py` |
| `11_eafp_file.py` | EAFP 风格文件操作 | `python3 11_eafp_file.py` |
| `12_lbyl_input.py` | LBYL 风格输入验证 | `python3 12_lbyl_input.py` |
| `13_mixed_style.py` | LBYL + EAFP 混合风格 | `python3 13_mixed_style.py` |

**风格对比**：
- EAFP（先尝试）：适合字典访问、文件操作
- LBYL（先检查）：适合用户输入验证

---

### 第 5 节：raise——主动抛出异常

| 文件 | 说明 | 运行方式 |
|------|------|----------|
| `14_basic_raise.py` | raise 的基本用法 | `python3 14_basic_raise.py` |
| `15_why_raise.py` | 为什么需要 raise | `python3 15_why_raise.py` |
| `16_input_validation_functions.py` | 输入校验函数集 | `python3 16_input_validation_functions.py` |
| `17_error_messages.py` | 好的 vs 坏的错误消息 | `python3 17_error_messages.py` |
| `18_robust_input_processor.py` | 健壮的用户输入处理器 | `python3 18_robust_input_processor.py` |
| `19_retry_with_limit.py` | 限制重试次数 | `python3 19_retry_with_limit.py` |

**关键概念**：
- `raise` vs `print`：raise 会中断程序，print 不会
- 好的错误消息：告诉用户"出了什么错、为什么、怎么修复"

---

### PyHelper 进度

| 文件 | 说明 | 运行方式 |
|------|------|----------|
| `06_pyhelper.py` | PyHelper Week 06 版本（异常处理版） | `python3 06_pyhelper.py` |

**本周改进**：
- 所有输入函数都加上了异常处理
- 菜单选择：输入"abc"不会崩溃
- 日期输入：格式不对会提示
- 文件操作：文件损坏会提示错误

**测试建议**：
1. 尝试输入各种非法值（abc、-5、200 等）
2. 验证程序不会崩溃
3. 验证错误提示是否清晰

---

## 运行所有示例

```bash
# 运行单个示例
python3 chapters/week_06/examples/01_crashing_calculator.py

# 运行 PyHelper
python3 chapters/week_06/examples/06_pyhelper.py
```

---

## 本周知识点

1. **异常处理**：try/except 结构
2. **异常类型**：ValueError、TypeError、ZeroDivisionError、KeyError、FileNotFoundError
3. **else 和 finally**：完整的异常处理
4. **LBYL vs EAFP**：两种编程哲学
5. **raise**：主动抛出异常
6. **输入校验**：设计健壮的输入函数
7. **防御性编程**：假设用户一定会乱输入，让程序能容错

---

## 与上周的对比

| 维度 | Week 05 | Week 06 |
|------|---------|---------|
| 输入错误 | 程序崩溃 | 提示错误并让用户重试 |
| 文件错误 | 程序崩溃 | 提示错误并继续运行 |
| 错误处理 | 无 | try/except + 输入校验 |
| 用户体验 | 脆弱 | 健壮 |

---

## 下周预告

Week 07 将学习**模块化**（modularization）：
- 把代码拆成多个文件
- import 和模块
- PyHelper 将从"一个巨大的文件"变成"一个有组织结构的项目"
