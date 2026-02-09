"""
测试字典（dict）操作

Week 04 核心知识点：
- 创建字典
- 访问字典值（通过键、get 方法）
- 字典修改（添加、更新、删除）
- 字典方法（keys, values, items, get, pop）
- 字典遍历
- 字典的特性（可变、无序但 Python 3.7+ 保持插入顺序、键唯一）
"""

import pytest


# ============================================================================
# 测试字典创建
# ============================================================================

def test_dict_creation_with_literals():
    """测试：用字面量创建字典"""
    scores = {
        "小北": 85,
        "阿码": 92,
        "老潘": 78
    }

    assert len(scores) == 3
    assert scores["小北"] == 85
    assert scores["阿码"] == 92


def test_dict_creation_with_constructor():
    """测试：用 dict() 构造函数创建字典"""
    scores = dict(小北=85, 阿码=92, 老潘=78)

    assert scores["小北"] == 85
    assert scores["阿码"] == 92


def test_dict_from_list_of_tuples():
    """测试：从元组列表创建字典"""
    data = [("小北", 85), ("阿码", 92), ("老潘", 78)]
    scores = dict(data)

    assert scores["小北"] == 85
    assert scores["阿码"] == 92


def test_dict_with_different_value_types():
    """测试：字典值可以是不同类型"""
    mixed = {
        "name": "Alice",
        "age": 25,
        "height": 1.68,
        "student": True,
        "address": None
    }

    assert mixed["name"] == "Alice"
    assert mixed["age"] == 25
    assert mixed["height"] == 1.68
    assert mixed["student"] is True
    assert mixed["address"] is None


def test_dict_keys_must_be_hashable():
    """测试：字典键必须是可哈希的（不可变类型）"""
    # 有效键：字符串、数字、元组
    valid = {
        "string": 1,
        42: 2,
        (1, 2): 3
    }

    assert valid["string"] == 1
    assert valid[42] == 2
    assert valid[(1, 2)] == 3


def test_empty_dict():
    """测试：创建空字典"""
    empty = {}

    assert empty == {}
    assert len(empty) == 0
    assert bool(empty) is False


# ============================================================================
# 测试字典访问
# ============================================================================

def test_access_by_key():
    """测试：通过键访问值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}

    assert scores["小北"] == 85
    assert scores["阿码"] == 92
    assert scores["老潘"] == 78


def test_access_nonexistent_key_raises_error():
    """测试：访问不存在的键会引发 KeyError"""
    scores = {"小北": 85, "阿码": 92}

    with pytest.raises(KeyError):
        _ = scores["小红"]


def test_get_method_returns_value():
    """测试：get() 方法返回值"""
    scores = {"小北": 85, "阿码": 92}

    assert scores.get("小北") == 85
    assert scores.get("阿码") == 92


def test_get_method_returns_default_for_missing_key():
    """测试：get() 对不存在的键返回默认值"""
    scores = {"小北": 85, "阿码": 92}

    # 默认返回 None
    assert scores.get("小红") is None

    # 指定默认值
    assert scores.get("小红", 0) == 0
    assert scores.get("小红", "N/A") == "N/A"


def test_get_method_with_none_value():
    """测试：get() 区分"键不存在"和"值为 None" """
    scores = {"小北": 85, "阿码": None}

    # 键存在，值为 None
    assert scores.get("阿码") is None

    # 键不存在，返回默认值
    assert scores.get("小红", "default") == "default"


# ============================================================================
# 测试字典修改和添加
# ============================================================================

def test_add_new_key_value_pair():
    """测试：添加新的键值对"""
    scores = {"小北": 85, "阿码": 92}

    scores["老潘"] = 78
    assert scores["老潘"] == 78
    assert len(scores) == 3


def test_update_existing_key():
    """测试：更新现有键的值"""
    scores = {"小北": 85, "阿码": 92}

    scores["小北"] = 90
    assert scores["小北"] == 90
    assert len(scores) == 2  # 数量不变


def test_add_and_update_use_same_syntax():
    """测试：添加和更新用相同的语法"""
    scores = {}

    # 添加
    scores["小北"] = 85
    assert scores["小北"] == 85

    # 更新
    scores["小北"] = 90
    assert scores["小北"] == 90


# ============================================================================
# 测试字典删除
# ============================================================================

def test_del_by_key():
    """测试：del 删除指定键"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}

    del scores["阿码"]
    assert "阿码" not in scores
    assert len(scores) == 2
    assert scores == {"小北": 85, "老潘": 78}


def test_del_nonexistent_key_raises_error():
    """测试：del 不存在的键会引发 KeyError"""
    scores = {"小北": 85}

    with pytest.raises(KeyError):
        del scores["小红"]


def test_pop_removes_and_returns_value():
    """测试：pop 删除键并返回对应的值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}

    value = scores.pop("阿码")
    assert value == 92
    assert "阿码" not in scores
    assert len(scores) == 2


def test_pop_with_default():
    """测试：pop 可以指定默认值"""
    scores = {"小北": 85}

    # 键存在时返回值
    assert scores.pop("小北", 0) == 85

    # 键不存在时返回默认值
    assert scores.pop("小红", 0) == 0


def test_pop_nonexistent_key_without_default_raises_error():
    """测试：pop 不存在的键（无默认值）会引发 KeyError"""
    scores = {"小北": 85}

    with pytest.raises(KeyError):
        scores.pop("小红")


def test_popitem_removes_last_item():
    """测试：popitem 删除并返回最后一个键值对"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}

    key, value = scores.popitem()

    # 返回的是键值对元组
    assert key in ["小北", "阿码", "老潘"]
    assert value in [85, 92, 78]
    assert len(scores) == 2


def test_clear_dict():
    """测试：clear 清空字典"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}

    scores.clear()
    assert scores == {}
    assert len(scores) == 0


# ============================================================================
# 测试字典查询方法
# ============================================================================

def test_keys_method():
    """测试：keys() 返回所有键"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}

    keys = scores.keys()
    assert set(keys) == {"小北", "阿码", "老潘"}


def test_values_method():
    """测试：values() 返回所有值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}

    values = scores.values()
    assert set(values) == {85, 92, 78}


def test_items_method():
    """测试：items() 返回所有键值对"""
    scores = {"小北": 85, "阿码": 92}

    items = scores.items()
    assert set(items) == {("小北", 85), ("阿码", 92)}


def test_in_operator_for_keys():
    """测试：in 检查键是否存在"""
    scores = {"小北": 85, "阿码": 92}

    assert "小北" in scores
    assert "阿码" in scores
    assert "小红" not in scores


# ============================================================================
# 测试字典遍历
# ============================================================================

def test_iterate_over_keys():
    """测试：遍历字典的键"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    keys = []

    for name in scores:
        keys.append(name)

    assert set(keys) == {"小北", "阿码", "老潘"}


def test_iterate_over_keys_explicitly():
    """测试：显式遍历键"""
    scores = {"小北": 85, "阿码": 92}
    keys = []

    for name in scores.keys():
        keys.append(name)

    assert set(keys) == {"小北", "阿码"}


def test_iterate_over_values():
    """测试：遍历字典的值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    values = []

    for score in scores.values():
        values.append(score)

    assert set(values) == {85, 92, 78}


def test_iterate_over_items():
    """测试：遍历字典的键值对"""
    scores = {"小北": 85, "阿码": 92}
    items = []

    for name, score in scores.items():
        items.append((name, score))

    assert set(items) == {("小北", 85), ("阿码", 92)}


# ============================================================================
# 测试字典的其他方法
# ============================================================================

def test_update_method():
    """测试：update 用另一个字典更新"""
    scores = {"小北": 85, "阿码": 92}
    new_data = {"老潘": 78, "小红": 90}

    scores.update(new_data)

    assert scores == {"小北": 85, "阿码": 92, "老潘": 78, "小红": 90}


def test_update_overwrites_existing_keys():
    """测试：update 会覆盖已存在的键"""
    scores = {"小北": 85, "阿码": 92}
    updates = {"小北": 90, "老潘": 78}

    scores.update(updates)

    assert scores == {"小北": 90, "阿码": 92, "老潘": 78}


def test_setdefault_method():
    """测试：setdefault 在键不存在时设置默认值"""
    scores = {"小北": 85, "阿码": 92}

    # 键存在时返回现有值
    value1 = scores.setdefault("小北", 0)
    assert value1 == 85
    assert scores["小北"] == 85

    # 键不存在时设置并返回默认值
    value2 = scores.setdefault("老潘", 78)
    assert value2 == 78
    assert scores["老潘"] == 78


def test_fromkeys_method():
    """测试：fromkeys 创建键相同的新字典"""
    keys = ["小北", "阿码", "老潘"]
    scores = dict.fromkeys(keys, 0)

    assert scores == {"小北": 0, "阿码": 0, "老潘": 0}


def test_copy_method():
    """测试：copy 创建字典副本"""
    original = {"小北": 85, "阿码": 92}
    copied = original.copy()

    assert copied == original
    assert copied is not original

    # 修改副本不影响原字典
    copied["小北"] = 90
    assert original["小北"] == 85
    assert copied["小北"] == 90


# ============================================================================
# 测试字典的特性
# ============================================================================

def test_dict_is_mutable():
    """测试：字典是可变的"""
    scores = {"小北": 85}
    scores["小北"] = 90

    assert scores["小北"] == 90


def test_dict_keys_are_unique():
    """测试：字典键必须唯一"""
    # 创建时重复的键会覆盖
    scores = {"小北": 85, "阿码": 92, "小北": 90}

    assert scores["小北"] == 90  # 最后的值
    assert len(scores) == 2  # 只有两个键


def test_dict_values_can_be_duplicate():
    """测试：字典值可以重复"""
    scores = {"小北": 85, "阿码": 85, "老潘": 85}

    assert scores["小北"] == 85
    assert scores["阿码"] == 85
    assert scores["老潘"] == 85
    assert len(scores) == 3


def test_dict_preserves_insertion_order():
    """测试：Python 3.7+ 字典保持插入顺序"""
    scores = {}
    scores["小北"] = 85
    scores["阿码"] = 92
    scores["老潘"] = 78

    keys = list(scores.keys())
    assert keys == ["小北", "阿码", "老潘"]


# ============================================================================
# 测试字典的布尔值
# ============================================================================

def test_empty_dict_is_falsy():
    """测试：空字典是 False"""
    empty = {}

    assert not empty
    assert bool(empty) is False


def test_nonempty_dict_is_truthy():
    """测试：非空字典是 True"""
    nonempty = {"key": "value"}

    assert nonempty
    assert bool(nonempty) is True


def test_dict_with_false_values_is_truthy():
    """测试：包含假值的字典仍然是 True"""
    false_values = {
        "zero": 0,
        "empty": "",
        "none": None,
        "false": False
    }

    assert false_values
    assert bool(false_values) is True


# ============================================================================
# 测试字典比较
# ============================================================================

def test_dict_equality():
    """测试：字典相等性比较"""
    dict1 = {"a": 1, "b": 2}
    dict2 = {"a": 1, "b": 2}
    dict3 = {"a": 1, "b": 3}

    assert dict1 == dict2
    assert dict1 != dict3


def test_dict_equality_order_independent():
    """测试：字典相等性与键顺序无关"""
    dict1 = {"a": 1, "b": 2, "c": 3}
    dict2 = {"c": 3, "a": 1, "b": 2}

    assert dict1 == dict2


# ============================================================================
# 测试嵌套字典
# ============================================================================

def test_nested_dict():
    """测试：嵌套字典"""
    student = {
        "name": "小北",
        "scores": {
            "math": 85,
            "english": 92,
            "python": 78
        }
    }

    assert student["name"] == "小北"
    assert student["scores"]["math"] == 85
    assert student["scores"]["python"] == 78


def test_nested_dict_modification():
    """测试：修改嵌套字典"""
    student = {
        "name": "小北",
        "scores": {"math": 85}
    }

    student["scores"]["math"] = 90
    student["scores"]["english"] = 92

    assert student["scores"] == {"math": 90, "english": 92}


# ============================================================================
# 测试字典与列表的组合
# ============================================================================

def test_dict_with_list_values():
    """测试：字典的值可以是列表"""
    student = {
        "name": "小北",
        "scores": [85, 92, 78, 90],
        "hobbies": ["reading", "gaming"]
    }

    assert student["scores"][0] == 85
    assert len(student["hobbies"]) == 2


def test_list_of_dicts():
    """测试：列表可以包含字典"""
    students = [
        {"name": "小北", "score": 85},
        {"name": "阿码", "score": 92},
        {"name": "老潘", "score": 78}
    ]

    assert students[0]["name"] == "小北"
    assert students[1]["score"] == 92


# ============================================================================
# 测试字典的字典推导式
# ============================================================================

def test_dict_comprehension():
    """测试：字典推导式"""
    scores = [85, 92, 78, 90, 88]
    squared = {x: x * x for x in scores}

    assert squared[85] == 7225
    assert squared[92] == 8464


def test_dict_comprehension_with_condition():
    """测试：带条件的字典推导式"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78, "小红": 90}

    # 只保留及格的
    passed = {name: score for name, score in scores.items() if score >= 60}

    assert "老潘" in passed
    assert len(passed) == 4


# ============================================================================
# 测试字典作为函数参数
# ============================================================================

def test_dict_as_function_argument():
    """测试：字典可以作为函数参数"""
    def get_score(data, name):
        return data.get(name, 0)

    scores = {"小北": 85, "阿码": 92}

    assert get_score(scores, "小北") == 85
    assert get_score(scores, "小红") == 0


def test_function_can_modify_dict():
    """测试：函数可以修改传入的字典"""
    def add_score(data, name, score):
        data[name] = score

    scores = {"小北": 85}
    add_score(scores, "阿码", 92)

    assert scores == {"小北": 85, "阿码": 92}


# ============================================================================
# 测试字典作为函数返回值
# ============================================================================

def test_function_returning_dict():
    """测试：函数可以返回字典"""
    def create_student(name, score):
        return {"name": name, "score": score}

    student = create_student("小北", 85)

    assert student == {"name": "小北", "score": 85}


# ============================================================================
# 测试解包字典
# ============================================================================

def test_dict_unpacking_in_creation():
    """测试：创建字典时解包"""
    base = {"a": 1, "b": 2}
    extended = {**base, "c": 3}

    assert extended == {"a": 1, "b": 2, "c": 3}


def test_dict_unpacking_overwrites():
    """测试：解包时后面的覆盖前面的"""
    base = {"a": 1, "b": 2}
    override = {"b": 20, "c": 3}
    combined = {**base, **override}

    assert combined == {"a": 1, "b": 20, "c": 3}


# ============================================================================
# 测试字典的键类型
# ============================================================================

def test_tuple_keys():
    """测试：元组可以作为字典键"""
    coordinates = {
        (0, 0): "origin",
        (1, 2): "point A",
        (3, 4): "point B"
    }

    assert coordinates[(0, 0)] == "origin"
    assert coordinates[(1, 2)] == "point A"


def test_numeric_keys():
    """测试：数字可以作为字典键"""
    data = {
        1: "one",
        2: "two",
        3.14: "pi"
    }

    assert data[1] == "one"
    assert data[2] == "two"
    assert data[3.14] == "pi"


def test_mixed_key_types():
    """测试：可以混合不同类型的键"""
    mixed = {
        "string": 1,
        42: 2,
        (1, 2): 3,
        3.14: 4
    }

    assert mixed["string"] == 1
    assert mixed[42] == 2
    assert mixed[(1, 2)] == 3
    assert mixed[3.14] == 4
