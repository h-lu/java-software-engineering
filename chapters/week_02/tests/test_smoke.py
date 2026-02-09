"""
Week 02 基线测试（Smoke Tests）

这些是基础的"冒烟测试"——确保最核心的功能能正常工作。
如果这些测试都失败，说明基础环境有问题，其他测试也不用跑了。

测试覆盖：
- 基本的 if/else 判断
- 基本的 for 循环
- 基本的 while 循环
- 基本的布尔表达式
"""

import pytest


def test_basic_if_statement():
    """基础测试：if 语句能正常工作"""
    x = 10
    if x > 5:
        result = "big"
    else:
        result = "small"

    assert result == "big"


def test_basic_for_loop():
    """基础测试：for 循环能正常工作"""
    total = 0
    for i in range(5):
        total += i

    assert total == 10  # 0+1+2+3+4 = 10


def test_basic_while_loop():
    """基础测试：while 循环能正常工作"""
    count = 0
    while count < 3:
        count += 1

    assert count == 3


def test_basic_boolean_and():
    """基础测试：布尔 and 运算能正常工作"""
    assert (True and True) is True
    assert (True and False) is False


def test_basic_boolean_or():
    """基础测试：布尔 or 运算能正常工作"""
    assert (True or False) is True
    assert (False or False) is False


def test_basic_boolean_not():
    """基础测试：布尔 not 运算能正常工作"""
    assert (not True) is False
    assert (not False) is True


def test_basic_comparison_operators():
    """基础测试：比较运算符能正常工作"""
    assert (5 == 5) is True
    assert (5 != 3) is True
    assert (5 > 3) is True
    assert (3 < 5) is True
    assert (5 >= 5) is True
    assert (3 <= 5) is True


def test_basic_range_function():
    """基础测试：range() 函数能正常工作"""
    result = list(range(5))
    assert result == [0, 1, 2, 3, 4]


def test_basic_break_statement():
    """基础测试：break 能跳出循环"""
    count = 0
    for i in range(10):
        count += 1
        if i == 3:
            break

    assert count == 4


def test_basic_continue_statement():
    """基础测试：continue 能跳过本次迭代"""
    result = []
    for i in range(5):
        if i == 2:
            continue
        result.append(i)

    assert result == [0, 1, 3, 4]


def test_basic_if_elif_else():
    """基础测试：if/elif/else 能正常工作"""
    score = 85
    if score >= 90:
        grade = "A"
    elif score >= 80:
        grade = "B"
    elif score >= 70:
        grade = "C"
    else:
        grade = "D"

    assert grade == "B"


def test_basic_in_operator():
    """基础测试：in 运算符能正常工作"""
    assert (3 in [1, 2, 3, 4, 5]) is True
    assert (6 in [1, 2, 3, 4, 5]) is False
