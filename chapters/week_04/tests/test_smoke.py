"""
Week 04 基线测试（Smoke Tests）

这些是基础的"冒烟测试"——确保最核心的功能能正常工作。
如果这些测试都失败，说明基础环境有问题，其他测试也不用跑了。

测试覆盖：
- 基本的列表创建和访问
- 基本的字典创建和访问
- 基本的遍历操作
- 基本的容器操作
"""

import pytest


# ============================================================================
# 测试列表基础操作
# ============================================================================

def test_list_creation():
    """基础测试：能创建列表"""
    my_list = [1, 2, 3]
    assert my_list == [1, 2, 3]
    assert isinstance(my_list, list)


def test_empty_list_creation():
    """基础测试：能创建空列表"""
    empty = []
    assert empty == []
    assert len(empty) == 0


def test_list_access_by_index():
    """基础测试：能通过索引访问列表元素"""
    scores = [85, 92, 78]
    assert scores[0] == 85
    assert scores[1] == 92
    assert scores[2] == 78


def test_list_negative_index():
    """基础测试：能用负数索引访问列表元素"""
    scores = [85, 92, 78]
    assert scores[-1] == 78
    assert scores[-2] == 92
    assert scores[-3] == 85


def test_list_length():
    """基础测试：能获取列表长度"""
    empty = []
    single = [1]
    multiple = [1, 2, 3, 4, 5]

    assert len(empty) == 0
    assert len(single) == 1
    assert len(multiple) == 5


def test_list_contains():
    """基础测试：能用 in 检查元素是否在列表中"""
    scores = [85, 92, 78]

    assert 85 in scores
    assert 92 in scores
    assert 100 not in scores


def test_list_append():
    """基础测试：能向列表末尾添加元素"""
    scores = [85, 92]
    scores.append(78)

    assert scores == [85, 92, 78]
    assert len(scores) == 3


# ============================================================================
# 测试字典基础操作
# ============================================================================

def test_dict_creation():
    """基础测试：能创建字典"""
    my_dict = {"name": "Alice", "age": 25}
    assert my_dict == {"name": "Alice", "age": 25}
    assert isinstance(my_dict, dict)


def test_empty_dict_creation():
    """基础测试：能创建空字典"""
    empty = {}
    assert empty == {}
    assert len(empty) == 0


def test_dict_access_by_key():
    """基础测试：能通过键访问字典值"""
    scores = {"小北": 85, "阿码": 92}
    assert scores["小北"] == 85
    assert scores["阿码"] == 92


def test_dict_get_method():
    """基础测试：能用 get() 方法安全访问字典"""
    scores = {"小北": 85, "阿码": 92}

    # 键存在时返回值
    assert scores.get("小北") == 85

    # 键不存在时返回 None
    assert scores.get("小红") is None

    # 键不存在时返回默认值
    assert scores.get("小红", 0) == 0


def test_dict_length():
    """基础测试：能获取字典长度（键值对数量）"""
    empty = {}
    single = {"key": "value"}
    multiple = {"a": 1, "b": 2, "c": 3}

    assert len(empty) == 0
    assert len(single) == 1
    assert len(multiple) == 3


def test_dict_contains_key():
    """基础测试：能用 in 检查键是否在字典中"""
    scores = {"小北": 85, "阿码": 92}

    assert "小北" in scores
    assert "阿码" in scores
    assert "小红" not in scores


def test_dict_add_update():
    """基础测试：能添加和更新字典元素"""
    scores = {"小北": 85}

    # 添加新键值对
    scores["阿码"] = 92
    assert scores["阿码"] == 92
    assert len(scores) == 2

    # 更新现有键的值
    scores["小北"] = 90
    assert scores["小北"] == 90


# ============================================================================
# 测试遍历基础操作
# ============================================================================

def test_for_loop_over_list():
    """基础测试：能用 for 循环遍历列表"""
    scores = [85, 92, 78]
    result = []

    for score in scores:
        result.append(score)

    assert result == [85, 92, 78]


def test_for_loop_over_dict_keys():
    """基础测试：能用 for 循环遍历字典的键"""
    scores = {"小北": 85, "阿码": 92}
    keys = []

    for name in scores:
        keys.append(name)

    assert set(keys) == {"小北", "阿码"}


def test_for_loop_over_dict_values():
    """基础测试：能用 for 循环遍历字典的值"""
    scores = {"小北": 85, "阿码": 92}
    values = []

    for score in scores.values():
        values.append(score)

    assert set(values) == {85, 92}


def test_for_loop_over_dict_items():
    """基础测试：能用 for 循环遍历字典的键值对"""
    scores = {"小北": 85, "阿码": 92}
    items = []

    for name, score in scores.items():
        items.append((name, score))

    assert set(items) == {("小北", 85), ("阿码", 92)}


# ============================================================================
# 测试 enumerate
# ============================================================================

def test_enumerate_list():
    """基础测试：能用 enumerate 同时获取索引和元素"""
    scores = [85, 92, 78]
    result = []

    for index, score in enumerate(scores):
        result.append((index, score))

    assert result == [(0, 85), (1, 92), (2, 78)]


# ============================================================================
# 测试内置函数与容器
# ============================================================================

def test_sum_function():
    """基础测试：sum() 函数能计算列表元素总和"""
    numbers = [1, 2, 3, 4, 5]
    assert sum(numbers) == 15


def test_max_function():
    """基础测试：max() 函数能找出列表最大值"""
    scores = [85, 92, 78, 90]
    assert max(scores) == 92


def test_min_function():
    """基础测试：min() 函数能找出列表最小值"""
    scores = [85, 92, 78, 90]
    assert min(scores) == 78


def test_max_on_dict_values():
    """基础测试：max() 能找出字典值的最大值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    assert max(scores.values()) == 92


def test_min_on_dict_values():
    """基础测试：min() 能找出字典值的最小值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    assert min(scores.values()) == 78


# ============================================================================
# 测试列表切片
# ============================================================================

def test_list_slice_first_n():
    """基础测试：能切片获取列表前 n 个元素"""
    scores = [85, 92, 78, 90, 88]
    first_three = scores[:3]

    assert first_three == [85, 92, 78]


def test_list_slice_last_n():
    """基础测试：能切片获取列表后 n 个元素"""
    scores = [85, 92, 78, 90, 88]
    last_two = scores[-2:]

    assert last_two == [90, 88]


def test_list_slice_range():
    """基础测试：能切片获取列表中间范围"""
    scores = [85, 92, 78, 90, 88]
    middle = scores[1:4]

    assert middle == [92, 78, 90]


# ============================================================================
# 测试列表修改
# ============================================================================

def test_list_modify_by_index():
    """基础测试：能通过索引修改列表元素"""
    scores = [85, 92, 78]
    scores[0] = 90

    assert scores == [90, 92, 78]


def test_list_remove_by_value():
    """基础测试：能通过值删除列表元素"""
    scores = [85, 92, 78, 90]
    scores.remove(78)

    assert scores == [85, 92, 90]
    assert 78 not in scores


# ============================================================================
# 测试列表排序
# ============================================================================

def test_sorted_function():
    """基础测试：sorted() 函数能返回排序后的新列表"""
    scores = [85, 92, 78, 90]
    sorted_scores = sorted(scores)

    assert sorted_scores == [78, 85, 90, 92]
    # 原列表不变
    assert scores == [85, 92, 78, 90]


# ============================================================================
# 测试字符串和列表的区别
# ============================================================================

def test_list_is_mutable():
    """基础测试：列表是可变的"""
    scores = [85, 92, 78]
    scores[0] = 90

    assert scores == [90, 92, 78]


def test_string_is_immutable():
    """基础测试：字符串是不可变的"""
    name = "Python"

    # 字符串不能修改
    with pytest.raises(TypeError):
        name[0] = "J"


# ============================================================================
# 测试容器作为函数参数
# ============================================================================

def test_list_as_function_parameter():
    """基础测试：列表能作为函数参数传递"""
    def get_first(lst):
        return lst[0]

    scores = [85, 92, 78]
    assert get_first(scores) == 85


def test_dict_as_function_parameter():
    """基础测试：字典能作为函数参数传递"""
    def get_value(d, key):
        return d.get(key)

    scores = {"小北": 85, "阿码": 92}
    assert get_value(scores, "小北") == 85
    assert get_value(scores, "小红") is None


# ============================================================================
# 测试容器作为函数返回值
# ============================================================================

def test_function_returning_list():
    """基础测试：函数能返回列表"""
    def make_list():
        return [1, 2, 3]

    result = make_list()
    assert result == [1, 2, 3]
    assert isinstance(result, list)


def test_function_returning_dict():
    """基础测试：函数能返回字典"""
    def make_dict():
        return {"key": "value"}

    result = make_dict()
    assert result == {"key": "value"}
    assert isinstance(result, dict)


# ============================================================================
# 测试容器的布尔值
# ============================================================================

def test_empty_list_is_falsy():
    """基础测试：空列表是 False"""
    assert not []
    assert bool([]) is False


def test_nonempty_list_is_truthy():
    """基础测试：非空列表是 True"""
    assert [1]
    assert bool([1]) is True


def test_empty_dict_is_falsy():
    """基础测试：空字典是 False"""
    assert not {}
    assert bool({}) is False


def test_nonempty_dict_is_truthy():
    """基础测试：非空字典是 True"""
    assert {"key": "value"}
    assert bool({"key": "value"}) is True
