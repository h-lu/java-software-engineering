"""
Week 07: 烟雾测试（Smoke Tests）

这些是基础的冒烟测试，用于验证 Week 07 的核心功能是否正常工作。
"""

import pytest
import sys
import os


def test_python_version():
    """测试: Python 版本是否合适"""
    # Python 3.6+ 是推荐版本
    assert sys.version_info >= (3, 6)


def test_standard_library_modules_available():
    """测试: 标准库模块是否可用"""
    import math
    import random
    import datetime
    import pathlib

    assert hasattr(math, "sqrt")
    assert hasattr(random, "choice")
    assert hasattr(datetime, "datetime")
    assert hasattr(pathlib, "Path")


def test_import_statement_works():
    """测试: import 语句是否正常工作"""
    import math

    result = math.sqrt(16)
    assert result == 4.0


def test_from_import_works():
    """测试: from import 语句是否正常工作"""
    from math import sqrt, pi

    assert sqrt(25) == 5.0
    assert pi == 3.141592653589793


def test_import_as_alias_works():
    """测试: import as 语句是否正常工作"""
    import math as m

    assert m.sqrt(16) == 4.0


def test_name_variable_exists():
    """测试: __name__ 变量是否存在"""
    assert hasattr(__builtins__, "__name__") or "__name__" in globals()


def test_sys_path_exists():
    """测试: sys.path 是否存在"""
    import sys

    assert hasattr(sys, "path")
    assert isinstance(sys.path, list)
    assert len(sys.path) > 0


def test_module_has_name_attribute():
    """测试: 模块是否有 __name__ 属性"""
    import math

    assert hasattr(math, "__name__")
    assert math.__name__ == "math"


def test_os_path_operations_work():
    """测试: 基本的路径操作是否正常"""
    from pathlib import Path

    home = Path.home()
    assert home.exists()

    cwd = Path.cwd()
    assert cwd.exists()


def test_file_operations_work(tmp_path):
    """测试: 基本的文件操作是否正常"""
    from pathlib import Path

    # 创建文件
    test_file = tmp_path / "test.txt"
    test_file.write_text("Hello, World!", encoding="utf-8")

    # 读取文件
    content = test_file.read_text(encoding="utf-8")
    assert content == "Hello, World!"

    # 检查文件存在
    assert test_file.exists()


def test_dict_operations_work():
    """测试: 字典操作是否正常（用于理解模块搜索路径）"""
    import sys

    # sys.modules 是一个字典，存储已导入的模块
    assert isinstance(sys.modules, dict)

    # 导入一个模块后，它应该在 sys.modules 中
    import math
    assert "math" in sys.modules


def test_list_operations_work():
    """测试: 列表操作是否正常（用于理解 sys.path）"""
    import sys

    # sys.path 是一个列表
    assert isinstance(sys.path, list)

    # 列表应该有元素
    assert len(sys.path) > 0

    # 可以访问第一个元素
    first_path = sys.path[0]
    assert isinstance(first_path, str)


def test_function_definition_works():
    """测试: 函数定义是否正常"""
    def add(a, b):
        return a + b

    assert add(2, 3) == 5


def test_try_except_works():
    """测试: try/except 是否正常"""
    try:
        result = 10 / 2
    except ZeroDivisionError:
        result = 0

    assert result == 5


def test_string_operations_work():
    """测试: 字符串操作是否正常"""
    text = "Hello, World!"

    # 拼接
    assert "Hello" + ", " + "World!" == text

    # 大小写
    assert text.lower() == "hello, world!"
    assert text.upper() == "HELLO, WORLD!"

    # 分割
    parts = text.split(", ")
    assert parts == ["Hello", "World!"]


def test_basic_arithmetic_works():
    """测试: 基本算术运算是否正常"""
    assert 2 + 3 == 5
    assert 10 - 4 == 6
    assert 3 * 4 == 12
    assert 10 / 2 == 5.0
    assert 10 // 3 == 3
    assert 10 % 3 == 1
    assert 2 ** 3 == 8


def test_comparison_operators_work():
    """测试: 比较运算符是否正常"""
    assert 5 > 3
    assert 3 < 5
    assert 5 >= 5
    assert 5 <= 5
    assert 5 == 5
    assert 5 != 3


def test_boolean_logic_works():
    """测试: 布尔逻辑是否正常"""
    assert True and True is True
    assert True and False is False
    assert True or False is True
    assert False or False is False
    assert not True is False
    assert not False is True


def test_if_statement_works():
    """测试: if 语句是否正常"""
    x = 10

    if x > 5:
        result = "greater"
    else:
        result = "smaller"

    assert result == "greater"


def test_for_loop_works():
    """测试: for 循环是否正常"""
    total = 0
    for i in [1, 2, 3, 4, 5]:
        total += i

    assert total == 15


def test_while_loop_works():
    """测试: while 循环是否正常"""
    count = 0
    total = 0

    while count < 5:
        total += count
        count += 1

    assert total == 10  # 0 + 1 + 2 + 3 + 4


def test_list_comprehension_works():
    """测试: 列表推导式是否正常"""
    squares = [x ** 2 for x in range(5)]

    assert squares == [0, 1, 4, 9, 16]


def test_dict_comprehension_works():
    """测试: 字典推导式是否正常"""
    squares = {x: x ** 2 for x in range(5)}

    assert squares == {0: 0, 1: 1, 2: 4, 3: 9, 4: 16}


def test_none_works():
    """测试: None 是否正常"""
    result = None

    assert result is None
    assert not (result is not None)


def test_type_checking_works():
    """测试: 类型检查是否正常"""
    assert isinstance(42, int)
    assert isinstance(3.14, float)
    assert isinstance("hello", str)
    assert isinstance([1, 2, 3], list)
    assert isinstance({"key": "value"}, dict)


def test_len_function_works():
    """测试: len 函数是否正常"""
    assert len([1, 2, 3]) == 3
    assert len("hello") == 5
    assert len({"a": 1, "b": 2}) == 2


def test_range_function_works():
    """测试: range 函数是否正常"""
    result = list(range(5))

    assert result == [0, 1, 2, 3, 4]


def test_print_function_works(capsys):
    """测试: print 函数是否正常"""
    print("Hello, World!")

    captured = capsys.readouterr()
    assert "Hello, World!" in captured.out


def test_help_function_exists():
    """测试: help 函数是否存在"""
    assert callable(help)

    # help 函数应该能接受参数
    # 注意: 不实际调用它，因为它会输出很多内容
    assert True


def test_dir_function_works():
    """测试: dir 函数是否正常"""
    import math

    # dir 应该返回一个列表
    result = dir(math)

    assert isinstance(result, list)
    assert len(result) > 0
    assert "__name__" in result


def test_type_function_works():
    """测试: type 函数是否正常"""
    assert type(42) == int
    assert type("hello") == str
    assert type([1, 2, 3]) == list


def test_hasattr_function_works():
    """测试: hasattr 函数是否正常"""
    import math

    assert hasattr(math, "sqrt") is True
    assert hasattr(math, "nonexistent_function") is False


def test_getattr_function_works():
    """测试: getattr 函数是否正常"""
    import math

    sqrt_func = getattr(math, "sqrt")
    assert callable(sqrt_func)
    assert sqrt_func(16) == 4.0


def test_isinstance_function_works():
    """测试: isinstance 函数是否正常"""
    import math

    assert isinstance(math, type(math))
    assert isinstance(42, int)
    assert isinstance("hello", str)


def test_issubclass_function_works():
    """测试: issubclass 函数是否正常"""
    class Parent:
        pass

    class Child(Parent):
        pass

    assert issubclass(Child, Parent) is True
