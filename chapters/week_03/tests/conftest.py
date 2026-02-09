"""
Week 03 测试配置文件

提供共享的 fixtures 和测试配置
"""

import pytest


# ============================================================================
# 单位换算常量
# ============================================================================

@pytest.fixture
def km_to_miles_constant():
    """公里转英里的换算常量"""
    return 0.621371


@pytest.fixture
def kg_to_pounds_constant():
    """公斤转磅的换算常量"""
    return 2.20462


@pytest.fixture
def fahrenheit_offset():
    """华氏度偏移量"""
    return 32


@pytest.fixture
def fahrenheit_scale():
    """华氏度缩放因子"""
    return 9/5


# ============================================================================
# PyHelper fixtures
# ============================================================================

@pytest.fixture
def valid_moods():
    """有效的心情选项"""
    return ["1", "2", "3"]


@pytest.fixture
def valid_choices():
    """有效的菜单选项"""
    return ["1", "2", "3"]


@pytest.fixture
def sample_quote():
    """示例名言"""
    return "学编程是马拉松，不是百米冲刺。找到自己的节奏最重要。"


@pytest.fixture
def mood_advice_map():
    """心情到建议的映射"""
    return {
        "1": "太好了！推荐你今天挑战一个新概念，比如开始学习函数或者列表。",
        "2": "那就做点巩固练习吧，复习上周的变量和字符串，写几个小例子。",
        "3": "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。",
    }


# ============================================================================
# 单位换算函数 fixtures
# ============================================================================

@pytest.fixture
def km_to_miles_func():
    """公里转英里函数"""
    def km_to_miles(km):
        return km * 0.621371
    return km_to_miles


@pytest.fixture
def kg_to_pounds_func():
    """公斤转磅函数"""
    def kg_to_pounds(kg):
        return kg * 2.20462
    return kg_to_pounds


@pytest.fixture
def celsius_to_fahrenheit_func():
    """摄氏度转华氏度函数"""
    def celsius_to_fahrenheit(c):
        return c * 9/5 + 32
    return celsius_to_fahrenheit


# ============================================================================
# PyHelper 函数 fixtures
# ============================================================================

@pytest.fixture
def get_advice_func():
    """获取建议函数"""
    def get_advice(mood):
        if mood == "1":
            return "太好了！推荐你今天挑战一个新概念。"
        elif mood == "2":
            return "那就做点巩固练习吧。"
        elif mood == "3":
            return "累了就休息一下吧。"
        else:
            return "写点巩固练习最稳妥。"
    return get_advice


@pytest.fixture
def get_quote_func():
    """获取名言函数"""
    def get_quote():
        return "学编程是马拉松，不是百米冲刺。找到自己的节奏最重要。"
    return get_quote


# ============================================================================
# 测试数据 fixtures
# ============================================================================

@pytest.fixture
def sample_km_values():
    """示例公里值"""
    return [0, 1, 5, 10, 42, 100, 1000]


@pytest.fixture
def sample_kg_values():
    """示例公斤值"""
    return [0, 1, 5, 10, 50, 100]


@pytest.fixture
def sample_celsius_values():
    """示例摄氏度值"""
    return [-40, -20, 0, 20, 25, 37, 100]


# ============================================================================
# 边界值 fixtures
# ============================================================================

@pytest.fixture
def boundary_values():
    """边界值测试数据"""
    return {
        "zero": 0,
        "negative": -1,
        "small_positive": 0.001,
        "large": 1000000,
        "very_small": -0.001,
    }
