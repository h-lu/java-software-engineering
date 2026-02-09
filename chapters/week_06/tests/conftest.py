"""
Week 06 测试配置文件

这个文件包含 pytest 的配置和共享的 fixture。
"""

import pytest
import sys
import os
from pathlib import Path


# ============================================================================
# 路径配置
# ============================================================================

# 添加 starter_code 到 Python 路径
starter_code_path = Path(__file__).parent.parent / "starter_code"
if str(starter_code_path) not in sys.path:
    sys.path.insert(0, str(starter_code_path))


# ============================================================================
# Fixture
# ============================================================================

@pytest.fixture
def sample_scores_dict():
    """提供示例成绩字典"""
    return {
        "小北": 85,
        "阿码": 90,
        "老潘": 95,
    }


@pytest.fixture
def sample_numbers_list():
    """提供示例数字列表"""
    return [10, 20, 30, 40, 50]


@pytest.fixture
def sample_mixed_list():
    """提供包含各种类型的列表"""
    return [42, 3.14, "hello", [1, 2], {"key": "value"}, None, True]


@pytest.fixture
def sample_mixed_dict():
    """提供包含各种类型的字典"""
    return {
        "int": 42,
        "float": 3.14,
        "str": "hello",
        "list": [1, 2, 3],
        "dict": {"nested": True},
        "none": None,
    }


@pytest.fixture
def valid_date_strings():
    """提供有效的日期字符串"""
    return [
        "01-01",
        "06-15",
        "12-31",
        "02-29",
    ]


@pytest.fixture
def invalid_date_strings():
    """提供无效的日期字符串"""
    return [
        "1-1",          # 单数字
        "123-456",      # 三位数字
        "01/01",        # 斜杠分隔
        "0101",         # 无分隔符
        "",             # 空字符串
        "ab-cd",        # 非数字
        "01-01-2024",   # 过长
    ]


@pytest.fixture
def valid_age_strings():
    """提供有效的年龄字符串"""
    return [
        "18",
        "25",
        "65",
        "100",
    ]


@pytest.fixture
def invalid_age_strings():
    """提供无效的年龄字符串"""
    return [
        "abc",          # 非数字
        "-5",           # 负数
        "150",          # 超出范围
        "12.5",         # 浮点数
        "",             # 空字符串
        "20 ",          # 包含空格
    ]


@pytest.fixture
def valid_positive_integer_strings():
    """提供有效的正整数字符串"""
    return [
        "1",
        "10",
        "100",
        "9999",
    ]


@pytest.fixture
def invalid_positive_integer_strings():
    """提供无效的正整数字符串"""
    return [
        "0",            # 零
        "-1",           # 负数
        "abc",          # 非数字
        "12.5",         # 浮点数
        "",             # 空字符串
        " ",            # 只有空格
        "1a2b",         # 字母数字混合
    ]


# ============================================================================
# 测试标记
# ============================================================================

def pytest_configure(config):
    """配置 pytest 标记"""
    config.addinivalue_line(
        "markers",
        "exception: 测试异常处理相关的功能"
    )
    config.addinivalue_line(
        "markers",
        "validation: 测试输入验证相关的功能"
    )
    config.addinivalue_line(
        "markers",
        "lbyl: 测试 LBYL 风格的代码"
    )
    config.addinivalue_line(
        "markers",
        "eafp: 测试 EAFP 风格的代码"
    )
    config.addinivalue_line(
        "markers",
        "file: 测试文件操作相关的功能"
    )
    config.addinivalue_line(
        "markers",
        "edge: 测试边界情况"
    )
    config.addinivalue_line(
        "markers",
        "slow: 标记慢速测试（如性能测试）"
    )
