"""
测试列表（list）操作

Week 04 核心知识点：
- 创建列表
- 访问列表元素（索引、负数索引）
- 列表切片
- 列表修改（通过索引赋值）
- 列表方法（append, insert, remove, pop, index, count）
- 列表排序（sort, sorted）
- 列表遍历
- 列表的特性（可变、有序）
"""

import pytest


# ============================================================================
# 测试列表创建
# ============================================================================

def test_list_creation_with_values():
    """测试：创建包含多个值的列表"""
    scores = [85, 92, 78, 90, 88]

    assert len(scores) == 5
    assert scores[0] == 85
    assert scores[-1] == 88


def test_list_creation_with_different_types():
    """测试：列表可以包含不同类型的元素"""
    mixed = [1, "two", 3.0, True, None]

    assert mixed[0] == 1
    assert mixed[1] == "two"
    assert mixed[2] == 3.0
    assert mixed[3] is True
    assert mixed[4] is None


def test_list_creation_with_list_comprehension():
    """测试：用列表推导式创建列表"""
    squares = [x * x for x in range(5)]

    assert squares == [0, 1, 4, 9, 16]


def test_list_from_range():
    """测试：从 range 创建列表"""
    numbers = list(range(5))

    assert numbers == [0, 1, 2, 3, 4]


# ============================================================================
# 测试列表索引访问
# ============================================================================

def test_positive_index():
    """测试：正数索引访问"""
    scores = [85, 92, 78, 90, 88]

    assert scores[0] == 85
    assert scores[1] == 92
    assert scores[2] == 78
    assert scores[3] == 90
    assert scores[4] == 88


def test_negative_index():
    """测试：负数索引访问（从末尾开始）"""
    scores = [85, 92, 78, 90, 88]

    assert scores[-1] == 88
    assert scores[-2] == 90
    assert scores[-3] == 78
    assert scores[-4] == 92
    assert scores[-5] == 85


def test_index_out_of_range_raises_error():
    """测试：索引越界会引发 IndexError"""
    scores = [85, 92, 78]

    with pytest.raises(IndexError):
        _ = scores[3]

    with pytest.raises(IndexError):
        _ = scores[-4]


def test_index_on_empty_list_raises_error():
    """测试：访问空列表的索引会引发错误"""
    empty = []

    with pytest.raises(IndexError):
        _ = empty[0]


# ============================================================================
# 测试列表切片
# ============================================================================

def test_slice_first_n():
    """测试：切片获取前 n 个元素"""
    scores = [85, 92, 78, 90, 88, 95, 82]

    assert scores[:3] == [85, 92, 78]
    assert scores[:5] == [85, 92, 78, 90, 88]


def test_slice_last_n():
    """测试：切片获取后 n 个元素"""
    scores = [85, 92, 78, 90, 88, 95, 82]

    assert scores[-3:] == [88, 95, 82]
    assert scores[-2:] == [95, 82]


def test_slice_middle_range():
    """测试：切片获取中间范围"""
    scores = [85, 92, 78, 90, 88, 95, 82]

    assert scores[2:5] == [78, 90, 88]
    assert scores[1:4] == [92, 78, 90]


def test_slice_with_step():
    """测试：带步长的切片"""
    scores = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

    # 每隔一个元素
    assert scores[::2] == [0, 2, 4, 6, 8]

    # 反转列表
    assert scores[::-1] == [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]


def test_slice_returns_new_list():
    """测试：切片返回新列表，不影响原列表"""
    original = [1, 2, 3, 4, 5]
    sliced = original[1:4]

    assert sliced == [2, 3, 4]
    assert original == [1, 2, 3, 4, 5]

    # 修改切片不影响原列表
    sliced[0] = 99
    assert sliced == [99, 3, 4]
    assert original == [1, 2, 3, 4, 5]


def test_slice_out_of_range():
    """测试：切片超出范围不会报错"""
    scores = [85, 92, 78]

    # 超出范围的切片会自动调整
    assert scores[:10] == [85, 92, 78]
    assert scores[1:10] == [92, 78]


def test_slice_empty_result():
    """测试：某些切片会返回空列表"""
    scores = [85, 92, 78]

    assert scores[3:3] == []
    assert scores[2:1] == []


# ============================================================================
# 测试列表修改
# ============================================================================

def test_modify_element_by_index():
    """测试：通过索引修改元素"""
    scores = [85, 92, 78, 90, 88]

    scores[0] = 90
    assert scores[0] == 90
    assert scores == [90, 92, 78, 90, 88]

    scores[-1] = 95
    assert scores[-1] == 95
    assert scores == [90, 92, 78, 90, 95]


def test_modify_with_negative_index():
    """测试：用负数索引修改元素"""
    scores = [85, 92, 78]

    scores[-1] = 90
    assert scores == [85, 92, 90]


# ============================================================================
# 测试列表方法 - append
# ============================================================================

def test_append_adds_to_end():
    """测试：append 在列表末尾添加元素"""
    scores = [85, 92, 78]

    scores.append(90)
    assert scores == [85, 92, 78, 90]
    assert len(scores) == 4

    scores.append(88)
    assert scores == [85, 92, 78, 90, 88]


def test_append_none():
    """测试：可以 append None"""
    scores = [85, 92]
    scores.append(None)

    assert scores == [85, 92, None]


def test_append_returns_none():
    """测试：append 返回 None"""
    scores = [85, 92]

    result = scores.append(78)
    assert result is None
    assert scores == [85, 92, 78]


def test_append_to_empty_list():
    """测试：向空列表 append"""
    empty = []
    empty.append(1)

    assert empty == [1]


# ============================================================================
# 测试列表方法 - insert
# ============================================================================

def test_insert_at_position():
    """测试：insert 在指定位置插入元素"""
    scores = [85, 92, 78, 90, 88]

    scores.insert(2, 95)
    assert scores == [85, 92, 95, 78, 90, 88]


def test_insert_at_beginning():
    """测试：在开头插入元素"""
    scores = [85, 92, 78]

    scores.insert(0, 100)
    assert scores == [100, 85, 92, 78]


def test_insert_at_end():
    """测试：在末尾插入元素（相当于 append）"""
    scores = [85, 92, 78]

    scores.insert(3, 90)
    assert scores == [85, 92, 78, 90]


def test_insert_with_negative_index():
    """测试：用负数索引插入"""
    scores = [85, 92, 78, 90, 88]

    # 在倒数第二个位置之前插入
    scores.insert(-1, 100)
    assert scores == [85, 92, 78, 90, 100, 88]


def test_insert_beyond_length():
    """测试：索引超出长度时会在末尾插入"""
    scores = [85, 92, 78]

    scores.insert(100, 90)
    assert scores == [85, 92, 78, 90]


# ============================================================================
# 测试列表方法 - remove
# ============================================================================

def test_remove_first_occurrence():
    """测试：remove 删除第一个匹配的元素"""
    scores = [85, 92, 78, 90, 88, 78]

    scores.remove(78)
    assert scores == [85, 92, 90, 88, 78]
    # 只删除了第一个 78


def test_remove_only_element():
    """测试：删除单元素列表的元素"""
    single = [42]

    single.remove(42)
    assert single == []


def test_remove_nonexistent_raises_error():
    """测试：删除不存在的元素会引发 ValueError"""
    scores = [85, 92, 78]

    with pytest.raises(ValueError):
        scores.remove(100)


def test_remove_from_empty_list_raises_error():
    """测试：从空列表删除会引发错误"""
    empty = []

    with pytest.raises(ValueError):
        empty.remove(1)


# ============================================================================
# 测试列表方法 - pop
# ============================================================================

def test_pop_last_element():
    """测试：pop 删除并返回最后一个元素"""
    scores = [85, 92, 78, 90]

    last = scores.pop()
    assert last == 90
    assert scores == [85, 92, 78]


def test_pop_at_index():
    """测试：pop 删除并返回指定索引的元素"""
    scores = [85, 92, 78, 90]

    first = scores.pop(0)
    assert first == 85
    assert scores == [92, 78, 90]

    third = scores.pop(1)
    assert third == 78
    assert scores == [92, 90]


def test_pop_with_negative_index():
    """测试：用负数索引 pop"""
    scores = [85, 92, 78, 90]

    last = scores.pop(-1)
    assert last == 90
    assert scores == [85, 92, 78]


def test_pop_from_empty_list_raises_error():
    """测试：从空列表 pop 会引发错误"""
    empty = []

    with pytest.raises(IndexError):
        empty.pop()


def test_pop_index_out_of_range_raises_error():
    """测试：pop 越界索引会引发错误"""
    scores = [85, 92]

    with pytest.raises(IndexError):
        scores.pop(2)


# ============================================================================
# 测试列表方法 - index
# ============================================================================

def test_index_find_element():
    """测试：index 找到元素的索引"""
    scores = [85, 92, 78, 90, 88]

    assert scores.index(85) == 0
    assert scores.index(92) == 1
    assert scores.index(88) == 4


def test_index_first_occurrence():
    """测试：index 返回第一个匹配项的索引"""
    scores = [85, 92, 78, 90, 88, 78]

    assert scores.index(78) == 2  # 第一个 78


def test_index_not_found_raises_error():
    """测试：找不到元素会引发 ValueError"""
    scores = [85, 92, 78]

    with pytest.raises(ValueError):
        scores.index(100)


# ============================================================================
# 测试列表方法 - count
# ============================================================================

def test_count_occurrences():
    """测试：count 统计元素出现次数"""
    scores = [85, 92, 78, 90, 88, 78, 85]

    assert scores.count(85) == 2
    assert scores.count(78) == 2
    assert scores.count(92) == 1
    assert scores.count(100) == 0


def test_count_empty_list():
    """测试：空列表的 count 总是 0"""
    empty = []

    assert empty.count(1) == 0


# ============================================================================
# 测试 del 语句
# ============================================================================

def test_del_by_index():
    """测试：del 删除指定索引的元素"""
    scores = [85, 92, 78, 90]

    del scores[1]
    assert scores == [85, 78, 90]


def test_del_slice():
    """测试：del 删除切片"""
    scores = [85, 92, 78, 90, 88]

    del scores[1:4]
    assert scores == [85, 88]


def test_del_last_element():
    """测试：del 删除最后一个元素"""
    scores = [85, 92, 78]

    del scores[-1]
    assert scores == [85, 92]


# ============================================================================
# 测试列表排序
# ============================================================================

def test_sort_ascending():
    """测试：sort 升序排序（修改原列表）"""
    scores = [85, 92, 78, 90, 88]

    scores.sort()
    assert scores == [78, 85, 88, 90, 92]


def test_sort_descending():
    """测试：sort 降序排序"""
    scores = [85, 92, 78, 90, 88]

    scores.sort(reverse=True)
    assert scores == [92, 90, 88, 85, 78]


def test_sort_returns_none():
    """测试：sort 返回 None"""
    scores = [85, 92, 78]

    result = scores.sort()
    assert result is None
    assert scores == [78, 85, 92]


def test_sorted_returns_new_list():
    """测试：sorted 返回新列表，不修改原列表"""
    original = [85, 92, 78, 90]

    sorted_list = sorted(original)

    assert sorted_list == [78, 85, 90, 92]
    assert original == [85, 92, 78, 90]  # 原列表不变


def test_sorted_descending():
    """测试：sorted 可以降序排序"""
    scores = [85, 92, 78, 90]

    sorted_desc = sorted(scores, reverse=True)
    assert sorted_desc == [92, 90, 85, 78]


def test_sort_empty_list():
    """测试：排序空列表不会报错"""
    empty = []
    empty.sort()
    assert empty == []


def test_sort_single_element():
    """测试：排序单元素列表"""
    single = [42]
    single.sort()
    assert single == [42]


# ============================================================================
# 测试列表遍历
# ============================================================================

def test_for_loop_direct_iteration():
    """测试：直接遍历列表元素"""
    scores = [85, 92, 78, 90, 88]
    result = []

    for score in scores:
        result.append(score)

    assert result == [85, 92, 78, 90, 88]


def test_for_loop_with_index():
    """测试：用索引遍历列表"""
    scores = [85, 92, 78]
    result = []

    for i in range(len(scores)):
        result.append(scores[i])

    assert result == [85, 92, 78]


def test_enumerate_list():
    """测试：enumerate 同时获取索引和元素"""
    scores = [85, 92, 78]
    result = []

    for index, score in enumerate(scores):
        result.append((index, score))

    assert result == [(0, 85), (1, 92), (2, 78)]


def test_enumerate_with_start():
    """测试：enumerate 可以指定起始值"""
    scores = [85, 92, 78]
    result = []

    for index, score in enumerate(scores, start=1):
        result.append((index, score))

    assert result == [(1, 85), (2, 92), (3, 78)]


# ============================================================================
# 测试列表的可变性
# ============================================================================

def test_list_is_mutable():
    """测试：列表是可变的"""
    original = [1, 2, 3]
    original[0] = 99

    assert original == [99, 2, 3]


def test_list_passed_to_function_can_be_modified():
    """测试：列表传给函数后可以被修改"""
    def modify_list(lst):
        lst.append(4)
        lst[0] = 99

    my_list = [1, 2, 3]
    modify_list(my_list)

    assert my_list == [99, 2, 3, 4]


# ============================================================================
# 测试列表的其他操作
# ============================================================================

def test_list_concatenation():
    """测试：列表拼接"""
    list1 = [1, 2, 3]
    list2 = [4, 5, 6]

    combined = list1 + list2
    assert combined == [1, 2, 3, 4, 5, 6]

    # 原列表不变
    assert list1 == [1, 2, 3]
    assert list2 == [4, 5, 6]


def test_list_multiplication():
    """测试：列表重复"""
    original = [1, 2]

    repeated = original * 3
    assert repeated == [1, 2, 1, 2, 1, 2]


def test_list_extension():
    """测试：extend 扩展列表"""
    list1 = [1, 2, 3]
    list2 = [4, 5, 6]

    list1.extend(list2)
    assert list1 == [1, 2, 3, 4, 5, 6]


def test_list_clear():
    """测试：clear 清空列表"""
    scores = [85, 92, 78]

    scores.clear()
    assert scores == []
    assert len(scores) == 0


def test_list_copy():
    """测试：copy 创建列表副本"""
    original = [1, 2, 3]
    copied = original.copy()

    assert copied == original
    assert copied is not original  # 不是同一个对象

    # 修改副本不影响原列表
    copied[0] = 99
    assert original == [1, 2, 3]
    assert copied == [99, 2, 3]


def test_list_reversal():
    """测试：reverse 反转列表"""
    scores = [85, 92, 78, 90]

    scores.reverse()
    assert scores == [90, 78, 92, 85]


# ============================================================================
# 测试列表的布尔值
# ============================================================================

def test_empty_list_is_falsy():
    """测试：空列表是 False"""
    empty = []

    assert not empty
    assert bool(empty) is False


def test_nonempty_list_is_truthy():
    """测试：非空列表是 True"""
    nonempty = [1]

    assert nonempty
    assert bool(nonempty) is True


def test_list_with_zeros_is_truthy():
    """测试：包含 0 的列表仍然是 True"""
    zeros = [0, 0, 0]

    assert zeros
    assert bool(zeros) is True


# ============================================================================
# 测试列表比较
# ============================================================================

def test_list_equality():
    """测试：列表相等性比较"""
    list1 = [1, 2, 3]
    list2 = [1, 2, 3]
    list3 = [1, 2, 4]

    assert list1 == list2
    assert list1 != list3


def test_list_ordering():
    """测试：列表顺序比较"""
    list1 = [1, 2, 3]
    list2 = [1, 2, 4]

    assert list1 < list2
    assert list2 > list1


# ============================================================================
# 测试嵌套列表
# ============================================================================

def test_nested_list():
    """测试：嵌套列表"""
    matrix = [
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]
    ]

    assert matrix[0] == [1, 2, 3]
    assert matrix[1][1] == 5
    assert matrix[2][2] == 9


def test_nested_list_modification():
    """测试：修改嵌套列表"""
    matrix = [
        [1, 2],
        [3, 4]
    ]

    matrix[0][0] = 99
    assert matrix == [[99, 2], [3, 4]]
