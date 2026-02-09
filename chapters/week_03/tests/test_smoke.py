"""
Week 03 基线测试（Smoke Tests）

这些是基础的"冒烟测试"——确保最核心的功能能正常工作。
如果这些测试都失败，说明基础环境有问题，其他测试也不用跑了。

测试覆盖：
- 基本的函数定义和调用
- 基本的参数传递
- 基本的返回值
- 基本的作用域规则
"""

import pytest


# ============================================================================
# 测试函数定义和调用
# ============================================================================

def test_basic_function_definition_and_call():
    """基础测试：函数定义后能正常调用"""
    def greet():
        return "Hello"

    result = greet()
    assert result == "Hello"


def test_function_call_with_parentheses():
    """基础测试：调用函数时必须加括号"""
    def get_value():
        return 42

    # 带括号是调用
    result = get_value()
    assert result == 42


def test_function_without_parentheses_is_not_called():
    """基础测试：不带括号只是函数对象，不会执行"""
    def get_value():
        return 42

    # 不带括号只是函数本身
    func_object = get_value
    assert callable(func_object)  # 是可调用对象

    # 必须加括号才能调用
    result = func_object()
    assert result == 42


# ============================================================================
# 测试参数传递
# ============================================================================

def test_function_with_single_parameter():
    """基础测试：单参数函数能正常工作"""
    def square(x):
        return x * x

    result = square(5)
    assert result == 25


def test_function_with_multiple_parameters():
    """基础测试：多参数函数能正常工作"""
    def add(a, b):
        return a + b

    result = add(3, 5)
    assert result == 8


def test_function_parameter_names_dont_matter():
    """基础测试：参数名只是占位符，名字不影响功能"""
    def func1(x):
        return x * 2

    def func2(any_name):
        return any_name * 2

    assert func1(5) == func2(5)


# ============================================================================
# 测试返回值
# ============================================================================

def test_return_statement_returns_value():
    """基础测试：return 语句能返回值"""
    def get_number():
        return 100

    result = get_number()
    assert result == 100


def test_function_without_return_returns_none():
    """基础测试：没有 return 的函数返回 None"""
    def no_return():
        x = 42

    result = no_return()
    assert result is None


def test_return_ends_function_execution():
    """基础测试：return 后的代码不会执行"""
    def early_return():
        return "first"
        return "second"  # 永远不会执行

    result = early_return()
    assert result == "first"


def test_can_return_multiple_values_as_tuple():
    """基础测试：可以返回多个值（实际上是元组）"""
    def get_name_and_age():
        return "Alice", 25

    name, age = get_name_and_age()
    assert name == "Alice"
    assert age == 25


def test_return_different_types():
    """基础测试：函数可以返回不同类型的值"""
    def return_int():
        return 42

    def return_float():
        return 3.14

    def return_str():
        return "hello"

    def return_bool():
        return True

    def return_none():
        return None

    assert return_int() == 42
    assert return_float() == 3.14
    assert return_str() == "hello"
    assert return_bool() is True
    assert return_none() is None


# ============================================================================
# 测试作用域
# ============================================================================

def test_local_variable_not_accessible_outside():
    """基础测试：函数内的局部变量在函数外无法访问"""
    def create_local():
        local_var = 42

    create_local()

    # local_var 在这里不存在
    # 如果取消下面这行的注释，会报 NameError
    # assert local_var == 42


def test_global_variable_accessible_inside_function():
    """基础测试：函数可以读取全局变量"""
    global_var = 100

    def read_global():
        return global_var

    result = read_global()
    assert result == 100


def test_function_can_modify_local_copy():
    """基础测试：函数可以修改参数的局部副本"""
    x = 10

    def modify(param):
        param = 20  # 只修改了局部副本
        return param

    result = modify(x)
    assert result == 20
    assert x == 10  # 原变量未改变


# ============================================================================
# 测试字符串作为参数
# ============================================================================

def test_function_with_string_parameter():
    """基础测试：字符串作为参数"""
    def greet(name):
        return f"Hello, {name}"

    result = greet("Alice")
    assert result == "Hello, Alice"


def test_function_with_default_parameter():
    """基础测试：带默认值的参数"""
    def greet(name="World"):
        return f"Hello, {name}"

    assert greet("Alice") == "Hello, Alice"
    assert greet() == "Hello, World"


# ============================================================================
# 测试数学运算函数
# ============================================================================

def test_km_to_miles_conversion():
    """基础测试：公里转英里换算"""
    def km_to_miles(km):
        return km * 0.621371

    result = km_to_miles(10)
    expected = 10 * 0.621371
    assert abs(result - expected) < 0.0001


def test_kg_to_pounds_conversion():
    """基础测试：公斤转磅换算"""
    def kg_to_pounds(kg):
        return kg * 2.20462

    result = kg_to_pounds(5)
    expected = 5 * 2.20462
    assert abs(result - expected) < 0.0001


def test_celsius_to_fahrenheit_conversion():
    """基础测试：摄氏度转华氏度换算"""
    def celsius_to_fahrenheit(c):
        return c * 9/5 + 32

    # 0°C = 32°F
    assert celsius_to_fahrenheit(0) == 32
    # 100°C = 212°F
    assert celsius_to_fahrenheit(100) == 212


# ============================================================================
# 测试函数可以组合调用
# ============================================================================

def test_function_can_call_other_function():
    """基础测试：函数可以调用其他函数"""
    def add_one(x):
        return x + 1

    def add_two(x):
        return add_one(add_one(x))

    result = add_two(5)
    assert result == 7


def test_function_result_can_be_parameter():
    """基础测试：函数返回值可以作为另一个函数的参数"""
    def square(x):
        return x * x

    def add_one(x):
        return x + 1

    # square(5) 的结果作为 add_one 的参数
    result = add_one(square(5))
    assert result == 26  # 5*5 + 1 = 26


# ============================================================================
# 测试函数文档字符串
# ============================================================================

def test_function_has_docstring():
    """基础测试：函数可以有文档字符串"""
    def my_function():
        """这是一个文档字符串"""
        return 42

    assert my_function.__doc__ == "这是一个文档字符串"


# ============================================================================
# 测试布尔返回值
# ============================================================================

def test_function_returning_true():
    """基础测试：函数可以返回 True"""
    def is_positive(x):
        return x > 0

    assert is_positive(5) is True


def test_function_returning_false():
    """基础测试：函数可以返回 False"""
    def is_positive(x):
        return x > 0

    assert is_positive(-5) is False


# ============================================================================
# 测试条件返回
# ============================================================================

def test_conditional_return():
    """基础测试：函数可以根据条件返回不同值"""
    def get_grade(score):
        if score >= 90:
            return "A"
        elif score >= 80:
            return "B"
        else:
            return "C"

    assert get_grade(95) == "A"
    assert get_grade(85) == "B"
    assert get_grade(75) == "C"
