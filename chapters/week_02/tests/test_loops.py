"""
测试循环（for/while）和 range() 函数

Week 02 核心知识点：
- for 循环配合 range()
- while 循环
- break 跳出循环
- continue 跳过本次迭代
- for...else 结构（循环正常结束时执行）
"""

import pytest


# ============================================================================
# 测试 range() 函数
# ============================================================================

def test_range_five_times():
    """测试：range(5) 生成 0, 1, 2, 3, 4（共 5 个数）"""
    result = []
    for i in range(5):
        result.append(i)

    assert result == [0, 1, 2, 3, 4]
    assert len(result) == 5


def test_range_start_end():
    """测试：range(1, 5) 生成 1, 2, 3, 4（左闭右开）"""
    result = []
    for i in range(1, 5):
        result.append(i)

    assert result == [1, 2, 3, 4]


def test_range_with_step():
    """测试：range(0, 10, 2) 生成 0, 2, 4, 6, 8（步长为 2）"""
    result = []
    for i in range(0, 10, 2):
        result.append(i)

    assert result == [0, 2, 4, 6, 8]


def test_range_negative_step():
    """测试：range(5, 0, -1) 生成 5, 4, 3, 2, 1（倒序）"""
    result = []
    for i in range(5, 0, -1):
        result.append(i)

    assert result == [5, 4, 3, 2, 1]


def test_range_single_element():
    """测试：range(1) 只生成 [0]"""
    result = []
    for i in range(1):
        result.append(i)

    assert result == [0]


def test_range_empty():
    """测试：range(0) 生成空列表"""
    result = []
    for i in range(0):
        result.append(i)

    assert result == []


@pytest.mark.parametrize("start, stop, step, expected", [
    (0, 5, 1, [0, 1, 2, 3, 4]),
    (1, 6, 1, [1, 2, 3, 4, 5]),
    (0, 10, 2, [0, 2, 4, 6, 8]),
    (5, 0, -1, [5, 4, 3, 2, 1]),
    (10, 0, -2, [10, 8, 6, 4, 2]),
])
def test_range_various_patterns(start, stop, step, expected):
    """测试：range() 的各种模式（参数化）"""
    result = []
    for i in range(start, stop, step):
        result.append(i)

    assert result == expected


# ============================================================================
# 测试 for 循环
# ============================================================================

def test_for_loop_sum():
    """测试：for 循环求和 1 到 10"""
    total = 0
    for i in range(1, 11):
        total += i

    assert total == 55  # 1+2+3+...+10 = 55


def test_for_loop_counter():
    """测试：for 循环计数器（从 1 开始计数）"""
    result = []
    for i in range(5):
        # 显示给用户的是 i+1，因为人类从 1 开始计数
        result.append(f"第 {i + 1} 次循环")

    expected = [
        "第 1 次循环",
        "第 2 次循环",
        "第 3 次循环",
        "第 4 次循环",
        "第 5 次循环",
    ]
    assert result == expected


def test_for_loop_with_break():
    """测试：for 循环中使用 break 提前退出"""
    result = []
    for i in range(10):
        if i == 5:
            break  # 到 5 就停止
        result.append(i)

    assert result == [0, 1, 2, 3, 4]


def test_for_loop_with_continue():
    """测试：for 循环中使用 continue 跳过某些值"""
    result = []
    for i in range(5):
        if i == 2:
            continue  # 跳过 2
        result.append(i)

    assert result == [0, 1, 3, 4]


def test_for_loop_nested():
    """测试：嵌套 for 循环（打印乘法表的一小部分）"""
    result = []
    for i in range(1, 4):
        for j in range(1, 4):
            result.append(f"{i}x{j}={i*j}")

    # 3x3 乘法表
    expected = [
        "1x1=1", "1x2=2", "1x3=3",
        "2x1=2", "2x2=4", "2x3=6",
        "3x1=3", "3x2=6", "3x3=9",
    ]
    assert result == expected


# ============================================================================
# 测试 for...else 结构
# ============================================================================

def test_for_else_normal_completion():
    """测试：for...else - 正常结束（执行 else）"""
    found = False
    for i in range(5):
        if i == 10:  # 这个条件永远不满足
            found = True
            break
    else:
        # 循环正常结束（没有被 break），执行 else
        message = "没找到"

    assert not found
    assert message == "没找到"


def test_for_else_break_exit():
    """测试：for...else - break 退出（不执行 else）"""
    found = False
    for i in range(5):
        if i == 3:
            found = True
            break  # 找到了，提前退出
    else:
        # 有 break，不会执行 else
        message = "没找到"

    assert found
    assert not hasattr(locals(), 'message')  # else 没执行


def test_for_else_guess_number_success():
    """测试：猜数字游戏 - 猜中了（不执行 else）"""
    secret = 42
    max_attempts = 5
    result = None

    for attempt in range(max_attempts):
        # 模拟第 3 次猜中
        if attempt == 3:
            result = "success"
            break
    else:
        # 如果循环正常结束（没 break），说明没猜中
        result = "failed"

    assert result == "success"


def test_for_else_guess_number_failed():
    """测试：猜数字游戏 - 没猜中（执行 else）"""
    secret = 42
    max_attempts = 5
    result = None

    for attempt in range(max_attempts):
        # 模拟永远猜不中
        if attempt == max_attempts + 10:  # 永远不满足
            result = "success"
            break
    else:
        # 循环正常结束，没猜中
        result = "failed"

    assert result == "failed"


# ============================================================================
# 测试 while 循环
# ============================================================================

def test_while_loop_countdown():
    """测试：while 循环倒计时"""
    result = []
    count = 5

    while count > 0:
        result.append(count)
        count -= 1

    assert result == [5, 4, 3, 2, 1]


def test_while_loop_with_condition():
    """测试：while 循环带条件（猜数字直到猜中）"""
    secret = 42
    guess = 0
    attempts = 0

    while guess != secret:
        # 模拟猜测过程
        if attempts == 0:
            guess = 30
        elif attempts == 1:
            guess = 50
        elif attempts == 2:
            guess = 42  # 猜中了
        attempts += 1

    assert guess == 42
    assert attempts == 3


def test_while_loop_with_break():
    """测试：while True 配合 break（无限循环直到 break）"""
    result = []
    count = 0

    while True:
        result.append(count)
        count += 1
        if count >= 5:
            break  # 达到条件后退出

    assert result == [0, 1, 2, 3, 4]


def test_while_loop_with_continue():
    """测试：while 循环中使用 continue"""
    result = []
    count = 0

    while count < 5:
        count += 1
        if count == 2:
            continue  # 跳过 2
        result.append(count)

    assert result == [1, 3, 4, 5]


def test_while_loop_prevent_infinite():
    """测试：while 循环必须有退出条件（防止无限循环）"""
    # 好的实践：总是有明确的退出条件
    count = 0
    max_iterations = 10

    while count < max_iterations:
        count += 1
        # 如果忘记更新 count，循环会永远运行
        # 但我们设置了 max_iterations 作为安全阀

    assert count == 10


# ============================================================================
# 测试猜数字游戏的循环逻辑
# ============================================================================

def test_guess_game_for_loop_max_attempts():
    """测试：猜数字游戏 - 限制 5 次"""
    max_attempts = 5
    attempts_count = 0

    for attempt in range(max_attempts):
        attempts_count += 1
        # 模拟每次都没猜中

    assert attempts_count == 5


def test_guess_game_remaining_attempts():
    """测试：猜数字游戏 - 剩余次数计算"""
    max_attempts = 5
    attempt = 2  # 第 3 次（从 0 开始）

    remaining = max_attempts - attempt - 1

    assert remaining == 2  # 还有 2 次机会


def test_guess_game_early_success():
    """测试：猜数字游戏 - 第 2 次就猜中了"""
    secret = 42
    max_attempts = 5
    used_attempts = 0

    for attempt in range(max_attempts):
        used_attempts += 1
        # 模拟第 2 次猜中
        if attempt == 1:
            success = True
            break
    else:
        success = False

    assert success
    assert used_attempts == 2


def test_guess_game_all_attempts_failed():
    """测试：猜数字游戏 - 5 次都没猜中"""
    secret = 42
    max_attempts = 5
    used_attempts = 0

    for attempt in range(max_attempts):
        used_attempts += 1
        # 模拟永远猜不中
        if attempt == max_attempts + 10:  # 永远不满足
            success = True
            break
    else:
        success = False

    assert not success
    assert used_attempts == 5


# ============================================================================
# 测试循环中的变量更新
# ============================================================================

def test_loop_variable_accumulation():
    """测试：循环中的累加器"""
    total = 0
    for i in range(1, 6):
        total += i  # 累加

    assert total == 15


def test_loop_build_list():
    """测试：循环中构建列表"""
    squares = []
    for i in range(1, 6):
        squares.append(i ** 2)

    assert squares == [1, 4, 9, 16, 25]


def test_loop_build_string():
    """测试：循环中拼接字符串"""
    result = ""
    for i in range(3):
        result += "ho"

    assert result == "hohoho"


# ============================================================================
# 测试循环边界情况
# ============================================================================

def test_for_loop_zero_iterations():
    """测试：for 循环 - 零次迭代（range(0)）"""
    count = 0
    for i in range(0):
        count += 1  # 永远不会执行

    assert count == 0


def test_for_loop_single_iteration():
    """测试：for 循环 - 单次迭代（range(1)）"""
    count = 0
    for i in range(1):
        count += 1

    assert count == 1


def test_while_loop_never_enters():
    """测试：while 循环 - 条件一开始就不满足"""
    count = 0
    while False:
        count += 1  # 永远不会执行

    assert count == 0


def test_while_loop_enters_once():
    """测试：while 循环 - 只执行一次"""
    count = 0
    condition = True

    while condition:
        count += 1
        condition = False  # 立即变为 False

    assert count == 1


# ============================================================================
# 实际应用场景测试
# ============================================================================

def test_calculate_factorial():
    """测试：计算阶乘（5! = 120）"""
    n = 5
    factorial = 1

    for i in range(1, n + 1):
        factorial *= i

    assert factorial == 120


def test_fizzbuzz_partial():
    """测试：FizzBuzz 的一部分（1-10）"""
    result = []
    for i in range(1, 11):
        if i % 3 == 0 and i % 5 == 0:
            result.append("FizzBuzz")
        elif i % 3 == 0:
            result.append("Fizz")
        elif i % 5 == 0:
            result.append("Buzz")
        else:
            result.append(i)

    expected = [1, 2, "Fizz", 4, "Buzz", "Fizz", 7, 8, "Fizz", "Buzz"]
    assert result == expected
