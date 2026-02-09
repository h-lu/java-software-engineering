"""
Week 07: 模块与 __name__ 守卫测试

测试覆盖:
1. 模块的基本概念
2. __name__ 变量的值
3. __name__ 守卫的作用
4. 模块作为脚本运行 vs 被导入
5. 模块级别的变量和函数
"""

import pytest
import sys
import os
import tempfile
import subprocess
from pathlib import Path


class TestModuleBasics:
    """测试模块的基本概念"""

    def test_module_object_has_attributes(self):
        """测试: 模块对象具有特定属性"""
        import math

        # 模块有 __name__ 属性
        assert hasattr(math, "__name__")
        assert math.__name__ == "math"

        # 模块有 __file__ 属性（对于 .py 文件）
        # 注意: 标准库模块可能是内置的，没有 __file__
        # assert hasattr(math, "__file__")

    def test_module_contains_functions_and_constants(self):
        """测试: 模块包含函数和常量"""
        import math

        # 函数
        assert callable(math.sqrt)
        assert callable(math.sin)

        # 常量
        assert isinstance(math.pi, float)

    def test_module_can_be_reimported(self):
        """测试: 模块可以被重新导入"""
        import math

        # 第一次导入
        first_pi = math.pi

        # 重新导入（实际上使用缓存的模块）
        import math

        # 相同的模块对象
        second_pi = math.pi
        assert first_pi == second_pi

    def test_module_in_sys_modules(self):
        """测试: 导入的模块在 sys.modules 中"""
        import sys
        import math

        assert "math" in sys.modules
        assert sys.modules["math"] is math


class TestNameVariable:
    """测试 __name__ 变量"""

    def test_main_script_has_main_name(self):
        """测试: 直接运行的脚本 __name__ 是 '__main__'"""
        # 创建一个临时脚本
        script_content = """
# 打印 __name__ 的值
print(f"__name__ = {__name__}")
"""

        with tempfile.NamedTemporaryFile(mode="w", suffix=".py", delete=False) as f:
            f.write(script_content)
            script_path = f.name

        try:
            # 运行脚本
            result = subprocess.run(
                [sys.executable, script_path],
                capture_output=True,
                text=True
            )

            assert "__name__ = __main__" in result.stdout
        finally:
            os.unlink(script_path)

    def test_imported_module_has_module_name(self):
        """测试: 被导入的模块 __name__ 是模块名"""
        # 创建一个临时模块
        module_content = """
print(f"__name__ = {__name__}")
"""

        with tempfile.TemporaryDirectory() as tmpdir:
            module_path = Path(tmpdir) / "test_module.py"
            module_path.write_text(module_content, encoding="utf-8")

            # 创建一个导入该模块的脚本
            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
import test_module
"""

            script_path = Path(tmpdir) / "main.py"
            script_path.write_text(script_content, encoding="utf-8")

            # 运行脚本
            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "__name__ = test_module" in result.stdout

    def test_name_is_string(self):
        """测试: __name__ 总是字符串"""
        import math

        assert isinstance(math.__name__, str)
        assert isinstance(__name__, str)


class TestNameGuard:
    """测试 __name__ 守卫"""

    def test_name_guard_executes_when_run_as_script(self):
        """测试: 直接运行时守卫内的代码会执行"""
        # 创建一个带守卫的脚本
        script_content = """
def main():
    print("Main function executed")

if __name__ == "__main__":
    print("Guard executed")
    main()
"""

        with tempfile.NamedTemporaryFile(mode="w", suffix=".py", delete=False) as f:
            f.write(script_content)
            script_path = f.name

        try:
            result = subprocess.run(
                [sys.executable, script_path],
                capture_output=True,
                text=True
            )

            assert "Guard executed" in result.stdout
            assert "Main function executed" in result.stdout
        finally:
            os.unlink(script_path)

    def test_name_guard_skipped_when_imported(self):
        """测试: 导入时守卫内的代码不会执行"""
        # 创建一个带守卫的模块
        module_content = """
def main():
    print("Main function executed")

print("Module loaded")

if __name__ == "__main__":
    print("Guard executed")
    main()
"""

        with tempfile.TemporaryDirectory() as tmpdir:
            module_path = Path(tmpdir) / "test_module.py"
            module_path.write_text(module_content, encoding="utf-8")

            # 创建导入该模块的脚本
            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
import test_module
print("Import completed")
"""

            script_path = Path(tmpdir) / "main.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            # 模块加载时的代码应该执行
            assert "Module loaded" in result.stdout
            assert "Import completed" in result.stdout

            # 守卫内的代码不应该执行
            assert "Guard executed" not in result.stdout
            assert "Main function executed" not in result.stdout

    def test_name_guard_allows_dual_use(self):
        """测试: __name__ 守卫让模块既能导入也能运行"""
        # 创建一个可复用的工具模块
        module_content = """
def add(a, b):
    return a + b

def multiply(a, b):
    return a * b

if __name__ == "__main__":
    # 作为脚本运行时的测试代码
    print(f"add(2, 3) = {add(2, 3)}")
    print(f"multiply(4, 5) = {multiply(4, 5)}")
"""

        with tempfile.TemporaryDirectory() as tmpdir:
            module_path = Path(tmpdir) / "calculator.py"
            module_path.write_text(module_content, encoding="utf-8")

            # 测试1: 直接运行
            result1 = subprocess.run(
                [sys.executable, str(module_path)],
                capture_output=True,
                text=True
            )
            assert "add(2, 3) = 5" in result1.stdout
            assert "multiply(4, 5) = 20" in result1.stdout

            # 测试2: 导入使用
            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from calculator import add, multiply

result1 = add(10, 20)
result2 = multiply(3, 7)
print("Imported: add=" + str(result1) + ", multiply=" + str(result2))
"""

            script_path = Path(tmpdir) / "main.py"
            script_path.write_text(script_content, encoding="utf-8")

            result2 = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )
            assert "Imported: add=30, multiply=21" in result2.stdout
            # 守卫内的输出不应该出现
            assert "add(2, 3) = 5" not in result2.stdout


class TestModuleLevelCode:
    """测试模块级代码"""

    def test_module_level_code_executes_on_import(self):
        """测试: 模块级代码在导入时执行"""
        module_content = """
# 模块级代码
print("Module level code executed")

module_variable = 42

def module_function():
    return module_variable
"""

        with tempfile.TemporaryDirectory() as tmpdir:
            module_path = Path(tmpdir) / "test_module.py"
            module_path.write_text(module_content, encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
import test_module

print(f"Variable: {{test_module.module_variable}}")
print(f"Function result: {{test_module.module_function()}}")
"""

            script_path = Path(tmpdir) / "main.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "Module level code executed" in result.stdout
            assert "Variable: 42" in result.stdout
            assert "Function result: 42" in result.stdout

    def test_functions_defined_outside_guard_are_importable(self):
        """测试: 守卫外定义的函数可以被导入"""
        # 正确做法: 函数定义在守卫外面
        module_content = """
def useful_function():
    return "This is useful"

def another_function():
    return "This is also useful"

if __name__ == "__main__":
    # 测试代码在守卫里面
    print(useful_function())
"""

        with tempfile.TemporaryDirectory() as tmpdir:
            module_path = Path(tmpdir) / "utils.py"
            module_path.write_text(module_content, encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from utils import useful_function, another_function

print(useful_function())
print(another_function())
"""

            script_path = Path(tmpdir) / "main.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "This is useful" in result.stdout
            assert "This is also useful" in result.stdout

    def test_functions_defined_inside_guard_are_not_importable(self):
        """测试: 守卫内定义的函数不能被导入（错误示例）"""
        # 错误做法: 函数定义在守卫里面
        module_content = """
if __name__ == "__main__":
    def useful_function():
        return "This won't work"
    print(useful_function())
"""

        with tempfile.TemporaryDirectory() as tmpdir:
            module_path = Path(tmpdir) / "bad_module.py"
            module_path.write_text(module_content, encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
try:
    from bad_module import useful_function
    print("Import succeeded (unexpected)")
except ImportError as e:
    print(f"Import failed as expected: {{e}}")
"""

            script_path = Path(tmpdir) / "main.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            # 导入应该失败
            assert "Import failed" in result.stdout or "cannot import" in result.stderr.lower()


class TestModuleBestPractices:
    """测试模块化最佳实践"""

    def test_single_responsibility_per_module(self):
        """测试: 单一职责原则 - 每个模块只做一件事"""
        # 这个测试说明概念: 一个好的模块应该有清晰的职责
        # 例如: math.py 只做数学运算, random.py 只做随机数
        import math
        import random

        # math 模块专注于数学运算
        assert hasattr(math, "sqrt")
        assert hasattr(math, "sin")

        # random 模块专注于随机数
        assert hasattr(random, "choice")
        assert hasattr(random, "randint")

    def test_modules_promote_reusability(self):
        """测试: 模块化提高代码复用性"""
        # 创建一个工具模块
        module_content = """
def calculate_area(length, width):
    return length * width

def calculate_perimeter(length, width):
    return 2 * (length + width)
"""

        with tempfile.TemporaryDirectory() as tmpdir:
            module_path = Path(tmpdir) / "geometry.py"
            module_path.write_text(module_content, encoding="utf-8")

            # 创建多个使用该模块的脚本
            script1 = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from geometry import calculate_area

area = calculate_area(5, 3)
print(f"Area: {{area}}")
"""

            script2 = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from geometry import calculate_perimeter

perimeter = calculate_perimeter(5, 3)
print(f"Perimeter: {{perimeter}}")
"""

            script1_path = Path(tmpdir) / "script1.py"
            script1_path.write_text(script1, encoding="utf-8")

            script2_path = Path(tmpdir) / "script2.py"
            script2_path.write_text(script2, encoding="utf-8")

            # 运行两个脚本
            result1 = subprocess.run(
                [sys.executable, str(script1_path)],
                capture_output=True,
                text=True
            )

            result2 = subprocess.run(
                [sys.executable, str(script2_path)],
                capture_output=True,
                text=True
            )

            assert "Area: 15" in result1.stdout
            assert "Perimeter: 16" in result2.stdout

    def test_modules_reduce_name_conflicts(self):
        """测试: 模块化减少命名冲突"""
        # 两个模块可以有同名函数
        module1_content = """
def process(data):
    return f"Module1 processed: {data}"
"""

        module2_content = """
def process(data):
    return f"Module2 processed: {data}"
"""

        with tempfile.TemporaryDirectory() as tmpdir:
            module1_path = Path(tmpdir) / "module1.py"
            module2_path = Path(tmpdir) / "module2.py"

            module1_path.write_text(module1_content, encoding="utf-8")
            module2_path.write_text(module2_content, encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
import module1
import module2

result1 = module1.process("data")
result2 = module2.process("data")

print(result1)
print(result2)
"""

            script_path = Path(tmpdir) / "main.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "Module1 processed: data" in result.stdout
            assert "Module2 processed: data" in result.stdout


class TestNameGuardPatterns:
    """测试 __name__ 守卫的常见模式"""

    def test_test_code_in_guard(self):
        """测试: 在守卫中放置测试代码"""
        module_content = """
def add(a, b):
    return a + b

def subtract(a, b):
    return a - b

# 测试代码
if __name__ == "__main__":
    print("Running tests...")
    assert add(2, 3) == 5
    assert subtract(5, 3) == 2
    print("All tests passed!")
"""

        with tempfile.NamedTemporaryFile(mode="w", suffix=".py", delete=False) as f:
            f.write(module_content)
            script_path = f.name

        try:
            # 直接运行会执行测试
            result = subprocess.run(
                [sys.executable, script_path],
                capture_output=True,
                text=True
            )
            assert "Running tests..." in result.stdout
            assert "All tests passed!" in result.stdout
        finally:
            os.unlink(script_path)

    def test_demo_code_in_guard(self):
        """测试: 在守卫中放置示例代码"""
        module_content = """
def greet(name):
    return f"Hello, {name}!"

if __name__ == "__main__":
    # 演示用法
    print(greet("World"))
    print(greet("Python"))
"""

        with tempfile.NamedTemporaryFile(mode="w", suffix=".py", delete=False) as f:
            f.write(module_content)
            script_path = f.name

        try:
            result = subprocess.run(
                [sys.executable, script_path],
                capture_output=True,
                text=True
            )
            assert "Hello, World!" in result.stdout
            assert "Hello, Python!" in result.stdout
        finally:
            os.unlink(script_path)

    def test_command_line_interface_in_guard(self):
        """测试: 在守卫中实现简单的命令行接口"""
        module_content = """
def calculate_square(n):
    return n * n

def calculate_cube(n):
    return n * n * n

if __name__ == "__main__":
    import sys
    if len(sys.argv) > 1:
        number = int(sys.argv[1])
        print(f"Square: {calculate_square(number)}")
        print(f"Cube: {calculate_cube(number)}")
    else:
        print("Usage: python script.py <number>")
"""

        with tempfile.NamedTemporaryFile(mode="w", suffix=".py", delete=False) as f:
            f.write(module_content)
            script_path = f.name

        try:
            result = subprocess.run(
                [sys.executable, script_path, "5"],
                capture_output=True,
                text=True
            )
            assert "Square: 25" in result.stdout
            assert "Cube: 125" in result.stdout
        finally:
            os.unlink(script_path)
