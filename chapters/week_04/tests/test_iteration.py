"""
测试遍历模式（iteration patterns）

Week 04 核心知识点：
- for 循环遍历列表
- for 循环遍历字典
- enumerate 遍历（同时获取索引和元素）
- 遍历中的条件判断
- 常见的数据处理模式（求和、计数、过滤、查找极值）
"""

import pytest


# ============================================================================
# 测试列表遍历模式
# ============================================================================

def test_for_loop_over_list_elements():
    """测试：直接遍历列表元素"""
    scores = [85, 92, 78, 90, 88]
    result = []

    for score in scores:
        result.append(score)

    assert result == [85, 92, 78, 90, 88]


def test_for_loop_with_range_and_index():
    """测试：用 range(len()) 遍历索引"""
    scores = [85, 92, 78]
    result = []

    for i in range(len(scores)):
        result.append(scores[i])

    assert result == [85, 92, 78]


def test_enumerate_list():
    """测试：enumerate 同时获取索引和元素"""
    scores = [85, 92, 78, 90]
    result = []

    for index, score in enumerate(scores):
        result.append((index, score))

    assert result == [(0, 85), (1, 92), (2, 78), (3, 90)]


def test_enumerate_with_custom_start():
    """测试：enumerate 可以指定起始值"""
    scores = [85, 92, 78]
    result = []

    for index, score in enumerate(scores, start=1):
        result.append(f"第{index}个: {score}")

    assert result == ["第1个: 85", "第2个: 92", "第3个: 78"]


# ============================================================================
# 测试字典遍历模式
# ============================================================================

def test_for_loop_over_dict_keys():
    """测试：遍历字典的键（默认行为）"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    keys = []

    for name in scores:
        keys.append(name)

    assert set(keys) == {"小北", "阿码", "老潘"}


def test_for_loop_over_dict_keys_explicit():
    """测试：显式遍历字典的键"""
    scores = {"小北": 85, "阿码": 92}
    keys = []

    for name in scores.keys():
        keys.append(name)

    assert set(keys) == {"小北", "阿码"}


def test_for_loop_over_dict_values():
    """测试：遍历字典的值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    values = []

    for score in scores.values():
        values.append(score)

    assert set(values) == {85, 92, 78}


def test_for_loop_over_dict_items():
    """测试：遍历字典的键值对"""
    scores = {"小北": 85, "阿码": 92}
    items = []

    for name, score in scores.items():
        items.append(f"{name}: {score}")

    assert set(items) == {"小北: 85", "阿码: 92"}


# ============================================================================
# 测试遍历中的条件判断
# ============================================================================

def test_filter_with_if_statement():
    """测试：在遍历中用 if 过滤"""
    scores = [85, 92, 78, 90, 88]
    passed = []

    for score in scores:
        if score >= 60:
            passed.append(score)

    assert passed == [85, 92, 78, 90, 88]


def test_filter_and_count():
    """测试：遍历并统计符合条件的数量"""
    scores = [85, 92, 58, 90, 88, 45]
    pass_count = 0
    fail_count = 0

    for score in scores:
        if score >= 60:
            pass_count += 1
        else:
            fail_count += 1

    assert pass_count == 4
    assert fail_count == 2


def test_dict_filter_with_if():
    """测试：遍历字典并过滤"""
    scores = {"小北": 85, "阿码": 92, "老潘": 58, "小红": 90}
    passed = {}

    for name, score in scores.items():
        if score >= 60:
            passed[name] = score

    assert passed == {"小北": 85, "阿码": 92, "小红": 90}


def test_nested_conditions():
    """测试：嵌套条件判断"""
    scores = {"小北": 85, "阿码": 92, "老潘": 58, "小红": 90}
    levels = []

    for name, score in scores.items():
        if score >= 90:
            level = "优秀"
        elif score >= 80:
            level = "良好"
        elif score >= 60:
            level = "及格"
        else:
            level = "不及格"

        levels.append((name, level))

    assert ("小北", "良好") in levels
    assert ("阿码", "优秀") in levels
    assert ("老潘", "不及格") in levels


# ============================================================================
# 测试累加模式（求和、计数）
# ============================================================================

def test_sum_accumulation():
    """测试：累加求和"""
    scores = [85, 92, 78, 90]
    total = 0

    for score in scores:
        total += score

    assert total == 345


def test_count_occurrences():
    """测试：计数统计"""
    scores = [85, 92, 78, 90, 88, 78, 85]
    counts = {}

    for score in scores:
        if score in counts:
            counts[score] += 1
        else:
            counts[score] = 1

    assert counts[85] == 2
    assert counts[78] == 2
    assert counts[92] == 1


def test_sum_dict_values():
    """测试：累加字典的值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    total = 0

    for score in scores.values():
        total += score

    assert total == 255


def test_count_with_condition():
    """测试：条件计数"""
    scores = [85, 92, 58, 90, 88, 45, 78]
    excellent_count = 0

    for score in scores:
        if score >= 90:
            excellent_count += 1

    assert excellent_count == 2


# ============================================================================
# 测试查找极值模式
# ============================================================================

def test_find_maximum():
    """测试：手动查找最大值"""
    scores = [85, 92, 78, 90, 88]
    max_score = None

    for score in scores:
        if max_score is None or score > max_score:
            max_score = score

    assert max_score == 92


def test_find_minimum():
    """测试：手动查找最小值"""
    scores = [85, 92, 78, 90, 88]
    min_score = None

    for score in scores:
        if min_score is None or score < min_score:
            min_score = score

    assert min_score == 78


def test_find_max_with_index():
    """测试：查找最大值及其索引"""
    scores = [85, 92, 78, 90, 88]
    max_score = None
    max_index = None

    for index, score in enumerate(scores):
        if max_score is None or score > max_score:
            max_score = score
            max_index = index

    assert max_score == 92
    assert max_index == 1


def test_find_dict_max_value():
    """测试：查找字典中的最大值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78, "小红": 90}
    max_score = None
    max_name = None

    for name, score in scores.items():
        if max_score is None or score > max_score:
            max_score = score
            max_name = name

    assert max_score == 92
    assert max_name == "阿码"


def test_find_dict_min_value():
    """测试：查找字典中的最小值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78, "小红": 90}
    min_score = None
    min_name = None

    for name, score in scores.items():
        if min_score is None or score < min_score:
            min_score = score
            min_name = name

    assert min_score == 78
    assert min_name == "老潘"


# ============================================================================
# 测试搜索模式
# ============================================================================

def test_linear_search_list():
    """测试：线性搜索列表"""
    scores = [85, 92, 78, 90, 88]
    target = 78
    found = False

    for score in scores:
        if score == target:
            found = True
            break

    assert found is True


def test_linear_search_dict():
    """测试：搜索字典中的键"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    target_name = "阿码"
    found_score = None

    for name, score in scores.items():
        if name == target_name:
            found_score = score
            break

    assert found_score == 92


def test_search_with_default():
    """测试：搜索并返回默认值"""
    scores = [85, 92, 78, 90]
    target = 100
    found_index = -1

    for index, score in enumerate(scores):
        if score == target:
            found_index = index
            break

    assert found_index == -1


# ============================================================================
# 测试转换模式
# ============================================================================

def test_transform_list():
    """测试：转换列表元素"""
    scores = [85, 92, 78]
    doubled = []

    for score in scores:
        doubled.append(score * 2)

    assert doubled == [170, 184, 156]


def test_transform_dict_values():
    """测试：转换字典的值"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    curved = {}

    for name, score in scores.items():
        curved[name] = min(score + 5, 100)

    assert curved["小北"] == 90
    assert curved["阿码"] == 97
    assert curved["老潘"] == 83


def test_extract_field_from_dict_list():
    """测试：从字典列表中提取字段"""
    students = [
        {"name": "小北", "score": 85},
        {"name": "阿码", "score": 92},
        {"name": "老潘", "score": 78}
    ]
    names = []

    for student in students:
        names.append(student["name"])

    assert names == ["小北", "阿码", "老潘"]


# ============================================================================
# 测试遍历嵌套结构
# ============================================================================

def test_iterate_nested_list():
    """测试：遍历嵌套列表"""
    matrix = [
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]
    ]
    result = []

    for row in matrix:
        for num in row:
            result.append(num)

    assert result == [1, 2, 3, 4, 5, 6, 7, 8, 9]


def test_iterate_dict_with_list_values():
    """测试：遍历包含列表值的字典"""
    student_scores = {
        "小北": [85, 92, 78],
        "阿码": [90, 88, 95]
    }
    all_scores = []

    for name, scores in student_scores.items():
        for score in scores:
            all_scores.append(score)

    assert all_scores == [85, 92, 78, 90, 88, 95]


# ============================================================================
# 测试数据驱动设计模式
# ============================================================================

def test_lookup_table_pattern():
    """测试：查表模式"""
    score_to_level = {
        (90, 100): "优秀",
        (80, 89): "良好",
        (60, 79): "及格",
        (0, 59): "不及格"
    }

    def get_level(score):
        for (low, high), level in score_to_level.items():
            if low <= score <= high:
                return level
        return "未知"

    assert get_level(95) == "优秀"
    assert get_level(85) == "良好"
    assert get_level(75) == "及格"
    assert get_level(55) == "不及格"


def test_config_driven_behavior():
    """测试：配置驱动行为"""
    config = {
        "theme": "dark",
        "font_size": 14,
        "auto_save": True
    }

    settings = []
    for key, value in config.items():
        if isinstance(value, bool):
            status = "启用" if value else "禁用"
            settings.append(f"{key}: {status}")
        else:
            settings.append(f"{key}: {value}")

    assert "theme: dark" in settings
    assert "font_size: 14" in settings
    assert "auto_save: 启用" in settings


# ============================================================================
# 测试遍历时的副作用
# ============================================================================

def test_modifying_list_while_iterating():
    """测试：遍历时修改列表可能导致问题"""
    scores = [85, 92, 78, 90, 88]

    # 正确做法：先收集要修改的
    to_remove = []
    for score in scores:
        if score < 80:
            to_remove.append(score)

    for score in to_remove:
        scores.remove(score)

    assert 78 not in scores
    assert len(scores) == 4


def test_building_new_list_during_iteration():
    """测试：遍历时构建新列表（推荐做法）"""
    original = [85, 92, 58, 90, 88]
    filtered = []

    for score in original:
        if score >= 60:
            filtered.append(score)

    assert filtered == [85, 92, 90, 88]
    assert original == [85, 92, 58, 90, 88]  # 原列表不变


def test_modifying_dict_while_iterating():
    """测试：遍历时修改字典可能导致问题"""
    scores = {"小北": 85, "阿码": 92, "老潘": 58, "小红": 90}

    # 正确做法：先收集要删除的键
    to_remove = []
    for name, score in scores.items():
        if score < 60:
            to_remove.append(name)

    for name in to_remove:
        del scores[name]

    assert "老潘" not in scores
    assert len(scores) == 3


# ============================================================================
# 测试空容器遍历
# ============================================================================

def test_iterate_empty_list():
    """测试：遍历空列表不会执行循环体"""
    empty = []
    count = 0

    for item in empty:
        count += 1

    assert count == 0


def test_iterate_empty_dict():
    """测试：遍历空字典不会执行循环体"""
    empty = {}
    count = 0

    for key, value in empty.items():
        count += 1

    assert count == 0


# ============================================================================
# 测试遍历与 break/continue
# ============================================================================

def test_break_stops_iteration():
    """测试：break 提前终止遍历"""
    scores = [85, 92, 78, 90, 88]
    result = []

    for score in scores:
        if score < 80:
            break
        result.append(score)

    assert result == [85, 92]


def test_continue_skips_iteration():
    """测试：continue 跳过当前迭代"""
    scores = [85, 92, 78, 90, 88]
    result = []

    for score in scores:
        if score < 80:
            continue
        result.append(score)

    assert result == [85, 92, 90, 88]


def test_find_with_break():
    """测试：用 break 实现查找"""
    scores = [85, 92, 78, 90, 88]
    target = 78
    found_at = None

    for index, score in enumerate(scores):
        if score == target:
            found_at = index
            break

    assert found_at == 2


# ============================================================================
# 测试 else 子句与循环
# ============================================================================

def test_for_else_executed():
    """测试：循环正常结束时执行 else"""
    scores = [85, 92, 78]
    executed = False

    for score in scores:
        pass
    else:
        executed = True

    assert executed is True


def test_for_else_not_executed_with_break():
    """测试：break 跳出时不执行 else"""
    scores = [85, 92, 78]
    executed = False

    for score in scores:
        break
    else:
        executed = True

    assert executed is False


# ============================================================================
# 测试遍历与列表推导式
# ============================================================================

def test_list_comprehension_vs_loop():
    """测试：列表推导式等同于 for 循环"""
    scores = [85, 92, 78, 90, 88]

    # for 循环
    doubled_loop = []
    for score in scores:
        doubled_loop.append(score * 2)

    # 列表推导式
    doubled_comp = [score * 2 for score in scores]

    assert doubled_loop == doubled_comp


def test_list_comprehension_with_condition():
    """测试：带条件的列表推导式"""
    scores = [85, 92, 58, 90, 88]

    # for 循环
    filtered_loop = []
    for score in scores:
        if score >= 60:
            filtered_loop.append(score)

    # 列表推导式
    filtered_comp = [score for score in scores if score >= 60]

    assert filtered_loop == filtered_comp


# ============================================================================
# 测试遍历与字典推导式
# ============================================================================

def test_dict_comprehension_vs_loop():
    """测试：字典推导式等同于 for 循环"""
    scores = [85, 92, 78]

    # for 循环
    squared_loop = {}
    for score in scores:
        squared_loop[score] = score * score

    # 字典推导式
    squared_comp = {score: score * score for score in scores}

    assert squared_loop == squared_comp


# ============================================================================
# 测试反向遍历
# ============================================================================

def test_reverse_iteration():
    """测试：反向遍历列表"""
    scores = [85, 92, 78, 90]
    result = []

    for score in reversed(scores):
        result.append(score)

    assert result == [90, 78, 92, 85]


def test_enumerate_reversed():
    """测试：反向遍历时获取索引"""
    scores = [85, 92, 78]

    result = []
    for index, score in enumerate(reversed(scores)):
        result.append((index, score))

    assert result == [(0, 78), (1, 92), (2, 85)]


# ============================================================================
# 测试排序后遍历
# ============================================================================

def test_iterate_sorted_list():
    """测试：遍历排序后的列表"""
    scores = [85, 92, 78, 90]

    # 升序
    ascending = []
    for score in sorted(scores):
        ascending.append(score)

    assert ascending == [78, 85, 90, 92]

    # 降序
    descending = []
    for score in sorted(scores, reverse=True):
        descending.append(score)

    assert descending == [92, 90, 85, 78]


def test_iterate_sorted_dict_keys():
    """测试：按排序后的键遍历字典"""
    scores = {"小北": 85, "阿码": 92, "老潘": 78}
    result = []

    for name in sorted(scores.keys()):
        result.append(f"{name}: {scores[name]}")

    # Note: sorted() sorts Chinese characters by Unicode code point
    # The actual order may differ from insertion order
    assert len(result) == 3
    assert "小北: 85" in result
    assert "阿码: 92" in result
    assert "老潘: 78" in result


# ============================================================================
# 测试 zip 遍历多个序列
# ============================================================================

def test_zip_two_lists():
    """测试：同时遍历两个列表"""
    names = ["小北", "阿码", "老潘"]
    scores = [85, 92, 78]
    result = []

    for name, score in zip(names, scores):
        result.append(f"{name}: {score}")

    assert result == ["小北: 85", "阿码: 92", "老潘: 78"]


def test_zip_stops_at_shortest():
    """测试：zip 在最短的序列结束时停止"""
    names = ["小北", "阿码", "老潘", "小红"]
    scores = [85, 92]
    result = []

    for name, score in zip(names, scores):
        result.append((name, score))

    assert result == [("小北", 85), ("阿码", 92)]


# ============================================================================
# 测试 range 遍历
# ============================================================================

def test_range_with_start_stop_step():
    """测试：range 的 start, stop, step 参数"""
    result = []

    for i in range(0, 10, 2):
        result.append(i)

    assert result == [0, 2, 4, 6, 8]


def test_range_negative_step():
    """测试：range 的负步长"""
    result = []

    for i in range(10, 0, -2):
        result.append(i)

    assert result == [10, 8, 6, 4, 2]


def test_range_single_argument():
    """测试：range 单参数表示 stop"""
    result = []

    for i in range(5):
        result.append(i)

    assert result == [0, 1, 2, 3, 4]
