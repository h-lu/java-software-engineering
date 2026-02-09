"""
Week 04 测试配置文件

提供共享的 fixtures 和测试配置
"""

import pytest


# ============================================================================
# 列表测试数据 fixtures
# ============================================================================

@pytest.fixture
def empty_list():
    """空列表"""
    return []


@pytest.fixture
def sample_scores_list():
    """示例成绩列表"""
    return [85, 92, 78, 90, 88]


@pytest.fixture
def sample_scores_with_duplicates():
    """包含重复值的成绩列表"""
    return [85, 92, 78, 90, 88, 78, 85]


@pytest.fixture
def single_element_list():
    """单元素列表"""
    return [42]


@pytest.fixture
def mixed_type_list():
    """混合类型列表"""
    return [1, "two", 3.0, True, None]


@pytest.fixture
def list_for_slicing():
    """用于切片测试的列表"""
    return [85, 92, 78, 90, 88, 95, 82]


# ============================================================================
# 字典测试数据 fixtures
# ============================================================================

@pytest.fixture
def empty_dict():
    """空字典"""
    return {}


@pytest.fixture
def sample_scores_dict():
    """示例成绩字典（姓名->成绩）"""
    return {
        "小北": 85,
        "阿码": 92,
        "老潘": 78,
        "小红": 90,
        "小明": 88
    }


@pytest.fixture
def sample_config_dict():
    """示例配置字典"""
    return {
        "theme": "dark",
        "language": "zh-CN",
        "font_size": 14,
        "auto_save": True
    }


@pytest.fixture
def dict_with_none_values():
    """包含 None 值的字典"""
    return {
        "key1": "value1",
        "key2": None,
        "key3": 0
    }


@pytest.fixture
def learning_log_dict():
    """学习记录字典（日期->内容）"""
    return {
        "02-07": "学习了列表的基本用法",
        "02-08": "学习了字典的基本用法",
        "02-09": "学习了遍历模式"
    }


# ============================================================================
# 边界值测试 fixtures
# ============================================================================

@pytest.fixture
def boundary_indices():
    """边界索引值"""
    return {
        "zero": 0,
        "positive": 1,
        "negative": -1,
        "large_positive": 1000,
        "large_negative": -1000
    }


@pytest.fixture
def special_values():
    """特殊值用于测试"""
    return {
        "zero": 0,
        "empty_string": "",
        "none": None,
        "false": False,
        "negative": -1,
        "float": 3.14
    }


# ============================================================================
# 切片测试 fixtures
# ============================================================================

@pytest.fixture
def slicing_test_cases():
    """切片测试用例"""
    return [
        # (list, slice, expected_description)
        ([1, 2, 3, 4, 5], slice(0, 3), "first_three"),
        ([1, 2, 3, 4, 5], slice(-3, None), "last_three"),
        ([1, 2, 3, 4, 5], slice(None, None, 2), "every_other"),
        ([1, 2, 3, 4, 5], slice(None, None, -1), "reversed"),
    ]


# ============================================================================
# 排序测试 fixtures
# ============================================================================

@pytest.fixture
def unsorted_scores():
    """未排序的成绩列表"""
    return [85, 92, 78, 90, 88]


@pytest.fixture
def sorted_ascending():
    """升序排序的成绩列表"""
    return [78, 85, 88, 90, 92]


@pytest.fixture
def sorted_descending():
    """降序排序的成绩列表"""
    return [92, 90, 88, 85, 78]


# ============================================================================
# 遍历测试 fixtures
# ============================================================================

@pytest.fixture
def list_for_iteration():
    """用于遍历测试的列表"""
    return ["apple", "banana", "cherry", "date"]


@pytest.fixture
def dict_for_iteration():
    """用于遍历测试的字典"""
    return {
        "a": 1,
        "b": 2,
        "c": 3,
        "d": 4
    }


# ============================================================================
# 成绩统计测试 fixtures
# ============================================================================

@pytest.fixture
def grade_boundary_scores():
    """等级边界成绩"""
    return {
        "excellent": 95,  # >= 90: 优秀
        "good_high": 89,  # >= 80: 良好
        "good_low": 80,
        "pass_high": 79,  # >= 60: 及格
        "pass_low": 60,
        "fail": 59  # < 60: 不及格
    }


# ============================================================================
# PyHelper 测试 fixtures
# ============================================================================

@pytest.fixture
def valid_moods():
    """有效的心情选项"""
    return ["1", "2", "3"]


@pytest.fixture
def valid_menu_choices():
    """有效的菜单选项"""
    return ["1", "2", "3", "4", "5"]


@pytest.fixture
def mood_advice_mapping():
    """心情到建议的映射"""
    return {
        "1": "太好了！推荐你今天挑战一个新概念，比如开始学习函数或者列表。",
        "2": "那就做点巩固练习吧，复习上周的变量和字符串，写几个小例子。",
        "3": "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
    }


# ============================================================================
# 错误测试 fixtures
# ============================================================================

@pytest.fixture
def invalid_indices():
    """无效索引值"""
    return {
        "too_large": 100,  # 对于短列表来说太大
        "too_small": -100,  # 对于短列表来说太小
    }


@pytest.fixture
def missing_keys():
    """缺失的键"""
    return ["nonexistent", "missing", "unknown", "void"]
