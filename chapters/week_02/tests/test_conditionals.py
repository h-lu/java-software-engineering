"""
测试条件判断逻辑（if/elif/else）

Week 02 核心知识点：
- if 语句的基本用法
- elif 多分支判断
- else 分支
- 条件的顺序性（从上到下检查）
"""

import pytest


# ============================================================================
# 测试单分支 if 语句
# ============================================================================

def test_check_age_adult():
    """测试：成年人判断（>= 18）"""
    age = 20
    if age >= 18:
        result = "adult"
    else:
        result = "minor"

    assert result == "adult"


def test_check_age_minor():
    """测试：未成年人判断（< 18）"""
    age = 15
    if age >= 18:
        result = "adult"
    else:
        result = "minor"

    assert result == "minor"


def test_check_age_boundary():
    """测试：边界值（正好 18 岁）"""
    age = 18
    if age >= 18:
        result = "adult"
    else:
        result = "minor"

    assert result == "adult"


# ============================================================================
# 测试多分支 if/elif/else 语句
# ============================================================================

def test_score_grade_excellent():
    """测试：分数评级 - 优秀（>= 90）"""
    score = 95

    if score >= 90:
        grade = "优秀"
    elif score >= 80:
        grade = "良好"
    elif score >= 60:
        grade = "及格"
    else:
        grade = "不及格"

    assert grade == "优秀"


def test_score_grade_good():
    """测试：分数评级 - 良好（80-89）"""
    score = 85

    if score >= 90:
        grade = "优秀"
    elif score >= 80:
        grade = "良好"
    elif score >= 60:
        grade = "及格"
    else:
        grade = "不及格"

    assert grade == "良好"


def test_score_grade_pass():
    """测试：分数评级 - 及格（60-79）"""
    score = 65

    if score >= 90:
        grade = "优秀"
    elif score >= 80:
        grade = "良好"
    elif score >= 60:
        grade = "及格"
    else:
        grade = "不及格"

    assert grade == "及格"


def test_score_grade_fail():
    """测试：分数评级 - 不及格（< 60）"""
    score = 45

    if score >= 90:
        grade = "优秀"
    elif score >= 80:
        grade = "良好"
    elif score >= 60:
        grade = "及格"
    else:
        grade = "不及格"

    assert grade == "不及格"


@pytest.mark.parametrize("score, expected", [
    (90, "优秀"),   # 边界：正好 90
    (89, "良好"),   # 边界：89 是良好
    (80, "良好"),   # 边界：正好 80
    (79, "及格"),   # 边界：79 是及格
    (60, "及格"),   # 边界：正好 60
    (59, "不及格"), # 边界：59 是不及格
    (0, "不及格"),  # 边界：最低分
    (100, "优秀"),  # 边界：最高分
])
def test_score_grade_boundaries(score, expected):
    """测试：分数评级的所有边界值"""
    if score >= 90:
        grade = "优秀"
    elif score >= 80:
        grade = "良好"
    elif score >= 60:
        grade = "及格"
    else:
        grade = "不及格"

    assert grade == expected


# ============================================================================
# 测试条件判断的顺序性
# ============================================================================

def test_condition_order_matters():
    """测试：条件顺序的重要性（错误的顺序会导致错误结果）"""
    score = 85

    # 正确的顺序：从高到低
    if score >= 90:
        grade_correct = "优秀"
    elif score >= 80:
        grade_correct = "良好"
    elif score >= 60:
        grade_correct = "及格"
    else:
        grade_correct = "不及格"

    # 错误的顺序：从低到高（会导致 85 分被判定为及格）
    if score >= 60:
        grade_wrong = "及格"
    elif score >= 80:
        grade_wrong = "良好"
    elif score >= 90:
        grade_wrong = "优秀"
    else:
        grade_wrong = "不及格"

    assert grade_correct == "良好"
    assert grade_wrong == "及格"  # 错误顺序导致错误结果


# ============================================================================
# 测试猜数字游戏的判断逻辑
# ============================================================================

def test_guess_number_correct():
    """测试：猜数字 - 猜对了"""
    secret = 42
    guess = 42

    if guess == secret:
        result = "correct"
    elif guess < secret:
        result = "too_low"
    else:
        result = "too_high"

    assert result == "correct"


def test_guess_number_too_low():
    """测试：猜数字 - 太小"""
    secret = 42
    guess = 30

    if guess == secret:
        result = "correct"
    elif guess < secret:
        result = "too_low"
    else:
        result = "too_high"

    assert result == "too_low"


def test_guess_number_too_high():
    """测试：猜数字 - 太大"""
    secret = 42
    guess = 50

    if guess == secret:
        result = "correct"
    elif guess < secret:
        result = "too_low"
    else:
        result = "too_high"

    assert result == "too_high"


# ============================================================================
# 测试难度选择逻辑
# ============================================================================

def test_difficulty_easy():
    """测试：难度选择 - 简单模式"""
    difficulty = 1

    if difficulty == 1:
        max_num = 50
    elif difficulty == 2:
        max_num = 100
    elif difficulty == 3:
        max_num = 200
    else:
        max_num = 100  # 默认中等

    assert max_num == 50


def test_difficulty_medium():
    """测试：难度选择 - 中等模式"""
    difficulty = 2

    if difficulty == 1:
        max_num = 50
    elif difficulty == 2:
        max_num = 100
    elif difficulty == 3:
        max_num = 200
    else:
        max_num = 100  # 默认中等

    assert max_num == 100


def test_difficulty_hard():
    """测试：难度选择 - 困难模式"""
    difficulty = 3

    if difficulty == 1:
        max_num = 50
    elif difficulty == 2:
        max_num = 100
    elif difficulty == 3:
        max_num = 200
    else:
        max_num = 100  # 默认中等

    assert max_num == 200


def test_difficulty_invalid():
    """测试：难度选择 - 无效输入（使用默认值）"""
    difficulty = 5

    if difficulty == 1:
        max_num = 50
    elif difficulty == 2:
        max_num = 100
    elif difficulty == 3:
        max_num = 200
    else:
        max_num = 100  # 默认中等

    assert max_num == 100


@pytest.mark.parametrize("difficulty, expected", [
    (1, 50),
    (2, 100),
    (3, 200),
    (0, 100),   # 无效：太小
    (-1, 100),  # 无效：负数
    (4, 100),   # 无效：太大
    (99, 100),  # 无效：超大数
])
def test_difficulty_all_cases(difficulty, expected):
    """测试：所有难度选择情况（参数化）"""
    if difficulty == 1:
        max_num = 50
    elif difficulty == 2:
        max_num = 100
    elif difficulty == 3:
        max_num = 200
    else:
        max_num = 100  # 默认中等

    assert max_num == expected


# ============================================================================
# 测试嵌套条件
# ============================================================================

def test_nested_condition_age_and_student():
    """测试：嵌套条件 - 年龄和学生身份"""
    age = 20
    is_student = True

    if age >= 18:
        if is_student:
            discount = "学生优惠"
        else:
            discount = "成人票价"
    else:
        discount = "儿童优惠"

    assert discount == "学生优惠"


def test_nested_condition_age_not_student():
    """测试：嵌套条件 - 成人但非学生"""
    age = 25
    is_student = False

    if age >= 18:
        if is_student:
            discount = "学生优惠"
        else:
            discount = "成人票价"
    else:
        discount = "儿童优惠"

    assert discount == "成人票价"


def test_nested_condition_child():
    """测试：嵌套条件 - 儿童"""
    age = 12
    is_student = True  # 儿童的学生身份不影响结果

    if age >= 18:
        if is_student:
            discount = "学生优惠"
        else:
            discount = "成人票价"
    else:
        discount = "儿童优惠"

    assert discount == "儿童优惠"


# ============================================================================
# 测试 PyHelper 心情推荐逻辑
# ============================================================================

def test_pyhelper_mood_motivated():
    """测试：PyHelper - 充满干劲"""
    mood = "1"

    if mood == "1":
        advice = "挑战新概念"
    elif mood == "2":
        advice = "巩固练习"
    elif mood == "3":
        advice = "休息一下"
    else:
        advice = "巩固练习"  # 默认

    assert advice == "挑战新概念"


def test_pyhelper_mood_average():
    """测试：PyHelper - 一般般"""
    mood = "2"

    if mood == "1":
        advice = "挑战新概念"
    elif mood == "2":
        advice = "巩固练习"
    elif mood == "3":
        advice = "休息一下"
    else:
        advice = "巩固练习"  # 默认

    assert advice == "巩固练习"


def test_pyhelper_mood_tired():
    """测试：PyHelper - 有点累"""
    mood = "3"

    if mood == "1":
        advice = "挑战新概念"
    elif mood == "2":
        advice = "巩固练习"
    elif mood == "3":
        advice = "休息一下"
    else:
        advice = "巩固练习"  # 默认

    assert advice == "休息一下"


def test_pyhelper_mood_invalid():
    """测试：PyHelper - 无效输入（使用默认）"""
    mood = "999"

    if mood == "1":
        advice = "挑战新概念"
    elif mood == "2":
        advice = "巩固练习"
    elif mood == "3":
        advice = "休息一下"
    else:
        advice = "巩固练习"  # 默认

    assert advice == "巩固练习"
