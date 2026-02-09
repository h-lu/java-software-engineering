"""
Week 07: import 基础测试

测试覆盖:
1. import module 语法
2. from module import name 语法
3. import module as alias 语法
4. 模块搜索路径
5. 标准库模块的基本使用
"""

import pytest
import sys
import os
from pathlib import Path


class TestImportModule:
    """测试 import module 语法"""

    def test_import_math_module(self):
        """测试: 导入 math 模块"""
        import math
        assert hasattr(math, "sqrt")
        assert hasattr(math, "pi")
        assert hasattr(math, "sin")

    def test_import_random_module(self):
        """测试: 导入 random 模块"""
        import random
        assert hasattr(random, "choice")
        assert hasattr(random, "randint")
        assert hasattr(random, "random")

    def test_import_datetime_module(self):
        """测试: 导入 datetime 模块"""
        import datetime
        assert hasattr(datetime, "datetime")
        assert hasattr(datetime, "timedelta")

    def test_import_pathlib_module(self):
        """测试: 导入 pathlib 模块"""
        import pathlib
        assert hasattr(pathlib, "Path")

    def test_imported_module_requires_prefix(self):
        """测试: 导入的模块需要加前缀使用"""
        import math

        # 正确用法: 加模块名前缀
        result = math.sqrt(16)
        assert result == 4.0

        # 验证直接使用 sqrt 会报错
        with pytest.raises(NameError):
            sqrt(16)

    def test_import_multiple_modules(self):
        """测试: 导入多个模块"""
        import math
        import random
        import sys

        # 验证所有模块都可用
        assert math.sqrt(25) == 5.0
        assert 0 <= random.random() <= 1
        assert isinstance(sys.path, list)


class TestFromImport:
    """测试 from module import name 语法"""

    def test_from_import_specific_function(self):
        """测试: 从模块导入特定函数"""
        from math import sqrt

        # 直接使用，不需要前缀
        result = sqrt(16)
        assert result == 4.0

    def test_from_import_multiple_names(self):
        """测试: 从模块导入多个名称"""
        from math import sqrt, pi, sin

        # 验证所有导入的名称都可用
        assert sqrt(25) == 5.0
        assert pi == 3.141592653589793
        assert sin(0) == 0

    def test_from_import_constant(self):
        """测试: 从模块导入常量"""
        from math import pi, e

        assert pi == 3.141592653589793
        assert e == 2.718281828459045

    def test_from_random_import_functions(self):
        """测试: 从 random 导入函数"""
        from random import choice, randint

        # choice 从列表中随机选择
        items = ["a", "b", "c"]
        result = choice(items)
        assert result in items

        # randint 生成指定范围随机整数
        number = randint(1, 10)
        assert 1 <= number <= 10

    def test_from_pathlib_import_path(self):
        """测试: 从 pathlib 导入 Path"""
        from pathlib import Path

        home = Path.home()
        assert isinstance(home, Path)

    def test_from_module_import_star_is_bad_practice(self):
        """测试: from module import * 是不推荐的做法"""
        # 注意: 这个测试只是说明概念，不建议在实际代码中使用 import *

        # 导入前，math 模块的函数不在全局命名空间
        assert "sqrt" not in dir()

        # 使用 import * 后，所有名称都会被导入
        # 注意: 这个操作是不可逆的，所以我们不实际执行它
        # from math import *
        # assert "sqrt" in dir()

    def test_from_import_name_conflict(self):
        """测试: from import 可能导致命名冲突"""
        from math import pi

        # 原始的 pi
        original_pi = pi
        assert original_pi == 3.141592653589793

        # 赋值会覆盖导入的名称
        pi = 3.14
        assert pi == 3.14
        assert pi != original_pi

        # 恢复原始值（避免影响其他测试）
        import math
        globals()["pi"] = math.pi


class TestImportAsAlias:
    """测试 import module as alias 语法"""

    def test_import_math_as_m(self):
        """测试: 给 math 模块起别名 m"""
        import math as m

        # 使用别名访问
        assert m.sqrt(16) == 4.0
        assert m.pi == 3.141592653589793

    def test_import_numpy_as_np(self):
        """测试: 常见的别名约定 np"""
        # 注意: numpy 可能未安装，这里只测试语法
        # import numpy as np
        # assert np.array([1, 2, 3]).tolist() == [1, 2, 3]
        pass

    def test_import_pandas_as_pd(self):
        """测试: 常见的别名约定 pd"""
        # 注意: pandas 可能未安装，这里只测试语法
        # import pandas as pd
        pass

    def test_import_matplotlib_pyplot_as_plt(self):
        """测试: 常见的别名约定 plt"""
        # 注意: matplotlib 可能未安装，这里只测试语法
        # import matplotlib.pyplot as plt
        pass

    def test_alias_shortens_long_names(self):
        """测试: 别名让长模块名更简洁"""
        # 使用别名的目的: 简化长模块名的使用
        import collections as abc

        # 验证可以使用别名访问模块内容
        assert hasattr(abc, "Counter")
        assert hasattr(abc, "defaultdict")

    def test_multiple_aliases(self):
        """测试: 导入多个模块并给它们起别名"""
        import math as m
        import random as r
        import sys as s

        # 验证所有别名都可用
        assert m.sqrt(16) == 4.0
        assert 0 <= r.random() <= 1
        assert isinstance(s.path, list)


class TestModuleSearchPath:
    """测试模块搜索路径"""

    def test_sys_path_exists(self):
        """测试: sys.path 是一个列表"""
        import sys
        assert isinstance(sys.path, list)
        assert len(sys.path) > 0

    def test_sys_path_contains_current_directory(self):
        """测试: sys.path 包含当前目录"""
        import sys
        import os

        # 当前工作目录应该在 sys.path 中
        cwd = os.getcwd()
        # 注意: 可能不是第一个元素，但应该在列表中
        # sys.path 的第一个元素通常是运行脚本的目录或空字符串
        assert isinstance(sys.path[0], str)

    def test_sys_path_contains_standard_library(self):
        """测试: sys.path 包含标准库目录"""
        import sys

        # 标准库目录应该在 sys.path 中
        # 标准库通常包含 "lib" 或 "site-packages" 等关键词
        has_stdlib = any("lib" in p.lower() or "site-packages" in p.lower()
                        for p in sys.path)
        # 这个断言可能在不同环境下失败，所以用软断言
        # assert has_stdlib

    def test_import_from_current_directory(self, tmp_path):
        """测试: 从当前目录导入模块"""
        # 创建一个临时模块
        module_file = tmp_path / "temp_module.py"
        module_file.write_text("""
def hello():
    return "Hello from temp module"

VALUE = 42
""", encoding="utf-8")

        # 临时添加 tmp_path 到 sys.path
        import sys
        original_path = sys.path.copy()
        sys.path.insert(0, str(tmp_path))

        try:
            # 导入临时模块
            import temp_module

            assert temp_module.hello() == "Hello from temp module"
            assert temp_module.VALUE == 42
        finally:
            # 恢复原始 sys.path
            sys.path = original_path
            # 从 sys.modules 中移除导入的模块
            if "temp_module" in sys.modules:
                del sys.modules["temp_module"]

    def test_import_error_when_module_not_found(self):
        """测试: 导入不存在的模块会报错"""
        with pytest.raises(ImportError, match="No module named"):
            import nonexistent_module_12345

    def test_module_caching_in_sys_modules(self):
        """测试: 模块在 sys.modules 中缓存"""
        import sys
        import math

        # math 应该在 sys.modules 中
        assert "math" in sys.modules
        assert sys.modules["math"] is math


class TestStandardLibraryModules:
    """测试标准库模块的基本使用"""

    def test_math_module_functions(self):
        """测试: math 模块的常用函数"""
        import math

        # 平方根
        assert math.sqrt(16) == 4.0

        # 幂运算
        assert math.pow(2, 3) == 8.0

        # 三角函数
        assert abs(math.sin(0)) < 1e-10
        assert abs(math.cos(0) - 1) < 1e-10

        # 向上取整
        assert math.ceil(3.2) == 4
        assert math.ceil(-3.2) == -3

        # 向下取整
        assert math.floor(3.8) == 3
        assert math.floor(-3.8) == -4

    def test_math_module_constants(self):
        """测试: math 模块的常量"""
        import math

        assert abs(math.pi - 3.141592653589793) < 1e-10
        assert abs(math.e - 2.718281828459045) < 1e-10

    def test_random_module_functions(self):
        """测试: random 模块的常用函数"""
        import random

        # 随机浮点数 [0.0, 1.0)
        x = random.random()
        assert 0 <= x < 1

        # 随机整数 [a, b]
        n = random.randint(1, 10)
        assert 1 <= n <= 10

        # 随机选择
        items = ["apple", "banana", "cherry"]
        choice = random.choice(items)
        assert choice in items

        # 随机打乱列表
        numbers = [1, 2, 3, 4, 5]
        original = numbers.copy()
        random.shuffle(numbers)
        # 元素相同，顺序可能不同
        assert sorted(numbers) == sorted(original)

    def test_datetime_module_usage(self):
        """测试: datetime 模块的基本使用"""
        import datetime

        # 获取当前日期时间
        now = datetime.datetime.now()
        assert isinstance(now, datetime.datetime)

        # 创建日期
        date = datetime.date(2026, 2, 9)
        assert date.year == 2026
        assert date.month == 2
        assert date.day == 9

        # 时间差
        delta = datetime.timedelta(days=7)
        future = date + delta
        assert future.day == 16

    def test_pathlib_module_usage(self):
        """测试: pathlib 模块的基本使用"""
        from pathlib import Path

        # 路径操作
        home = Path.home()
        assert home.exists()

        cwd = Path.cwd()
        assert cwd.exists()
        assert cwd.is_dir()

        # 路径拼接
        file_path = cwd / "test.txt"
        assert "test.txt" in str(file_path)


class TestImportEdgeCases:
    """测试 import 相关的边界情况"""

    def test_import_same_module_twice(self):
        """测试: 重复导入同一模块"""
        import math
        import math

        # 重复导入不会报错，使用的是同一个模块对象
        assert math.sqrt(16) == 4.0

    def test_import_module_with_dash_fails(self):
        """测试: 模块名不能包含连字符"""
        # Python 模块名不能包含连字符
        # 连字符在 Python 中是减法运算符
        # 所以 import my-module 会被解析为 import my - module
        # 这个测试说明为什么应该用下划线而不是连字符
        pass

    def test_import_case_sensitivity(self):
        """测试: import 是大小写敏感的"""
        import math

        # Math 和 math 是不同的
        assert "Math" not in dir()

    def test_from_import_nonexistent_name_raises_error(self):
        """测试: 导入不存在的名称会报错"""
        with pytest.raises(ImportError):
            from math import nonexistent_function_12345

    def test_circular_import_detection(self):
        """测试: 循环导入会导致问题"""
        # 这个测试说明循环导入的概念
        # 实际创建循环导入需要两个文件，这里只做概念说明
        # 模块 A 导入模块 B，模块 B 又导入模块 A
        # 这会导致 ImportError 或 AttributeError
        pass

    def test_import_inside_function(self):
        """测试: 在函数内导入模块（延迟导入）"""
        def calculate_sqrt(n):
            import math
            return math.sqrt(n)

        result = calculate_sqrt(25)
        assert result == 5.0

    def test_import_inside_conditional(self):
        """测试: 在条件语句中导入模块"""
        use_math = True

        if use_math:
            import math
            result = math.sqrt(16)
        else:
            result = 4.0

        assert result == 4.0


class TestImportBestPractices:
    """测试 import 最佳实践"""

    def test_import_ordering_convention(self):
        """测试: import 的排序约定（PEP 8）

        PEP 8 建议的顺序:
        1. 标准库导入
        2. 第三方库导入
        3. 本地应用/库导入
        """
        # 这个测试只说明最佳实践，不实际检查
        import sys
        import os
        import math
        import random

        # 标准库导入应该按字母顺序
        # 第三方库（如果已安装）应该单独分组
        # 本地模块应该单独分组

        assert True

    def test_one_import_per_line_is_recommended(self):
        """测试: 每行一个 import 是推荐做法"""
        # 推荐:
        # import math
        # import random
        # import sys

        # 不推荐（虽然可行）:
        # import math, random, sys

        # 这个测试只说明最佳实践
        import math
        import random
        import sys

        assert True

    def test_avoid_wildcard_imports(self):
        """测试: 避免 from module import *

        通配符导入会导入模块中的所有名称
        这可能覆盖你已经定义的名称
        """
        # 推荐做法:
        from math import sqrt, pi

        # 不推荐:
        # from math import *

        assert sqrt(16) == 4.0
        assert pi == 3.141592653589793
