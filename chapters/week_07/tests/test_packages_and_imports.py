"""
Week 07: 包与导入测试

测试覆盖:
1. 包的基本概念
2. __init__.py 的作用
3. 绝对导入
4. 相对导入
5. 项目结构
"""

import pytest
import sys
import os
import tempfile
import subprocess
from pathlib import Path


class TestPackageBasics:
    """测试包的基本概念"""

    def test_directory_with_init_py_is_package(self):
        """测试: 包含 __init__.py 的目录是包"""
        with tempfile.TemporaryDirectory() as tmpdir:
            # 创建包结构
            pkg_dir = Path(tmpdir) / "mypackage"
            pkg_dir.mkdir()

            # 创建 __init__.py
            init_file = pkg_dir / "__init__.py"
            init_file.write_text("# Package initialization\n", encoding="utf-8")

            # 创建一个模块
            module_file = pkg_dir / "module.py"
            module_file.write_text("VALUE = 42\n", encoding="utf-8")

            # 创建测试脚本
            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
import mypackage.module

print(f"Module value: {{mypackage.module.VALUE}}")
"""

            script_path = Path(tmpdir) / "test_script.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "Module value: 42" in result.stdout

    def test_init_py_can_be_empty(self):
        """测试: __init__.py 可以是空文件"""
        with tempfile.TemporaryDirectory() as tmpdir:
            pkg_dir = Path(tmpdir) / "emptypackage"
            pkg_dir.mkdir()

            # 创建空的 __init__.py
            init_file = pkg_dir / "__init__.py"
            init_file.write_text("", encoding="utf-8")

            # 创建一个模块
            module_file = pkg_dir / "module.py"
            module_file.write_text("def hello(): return 'Hello'\n", encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from emptypackage.module import hello

print(hello())
"""

            script_path = Path(tmpdir) / "test_script.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "Hello" in result.stdout

    def test_init_py_can_expose_functions(self):
        """测试: __init__.py 可以暴露常用函数"""
        with tempfile.TemporaryDirectory() as tmpdir:
            pkg_dir = Path(tmpdir) / "smartpackage"
            pkg_dir.mkdir()

            # 创建模块1
            module1 = pkg_dir / "module1.py"
            module1.write_text("def func1(): return 'Function 1'\n", encoding="utf-8")

            # 创建模块2
            module2 = pkg_dir / "module2.py"
            module2.write_text("def func2(): return 'Function 2'\n", encoding="utf-8")

            # 在 __init__.py 中暴露常用函数
            init_file = pkg_dir / "__init__.py"
            init_file.write_text("""
from .module1 import func1
from .module2 import func2
""", encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
# 可以直接从包导入函数
from smartpackage import func1, func2

print(func1())
print(func2())
"""

            script_path = Path(tmpdir) / "test_script.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "Function 1" in result.stdout
            assert "Function 2" in result.stdout


class TestAbsoluteImports:
    """测试绝对导入"""

    def test_absolute_import_from_package(self):
        """测试: 使用绝对导入从包中导入"""
        with tempfile.TemporaryDirectory() as tmpdir:
            # 创建包结构
            pkg_dir = Path(tmpdir) / "mypackage"
            pkg_dir.mkdir()

            (pkg_dir / "__init__.py").write_text("", encoding="utf-8")

            # 创建子模块
            utils_module = pkg_dir / "utils.py"
            utils_module.write_text("""
def helper():
    return "Helper function"
""", encoding="utf-8")

            # 创建主模块，使用绝对导入
            main_module = pkg_dir / "main.py"
            main_module.write_text("""
from mypackage.utils import helper

def run():
    return helper()
""", encoding="utf-8")

            # 创建测试脚本
            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from mypackage.main import run

print(run())
"""

            script_path = Path(tmpdir) / "test_script.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "Helper function" in result.stdout

    def test_absolute_import_clarity(self):
        """测试: 绝对导入更清晰，明确指出完整路径"""
        # 绝对导入: from package.subpackage import module
        # 这个测试说明概念: 绝对导入总是从包的根目录开始
        import collections
        import collections.abc

        # 标准库使用绝对导入
        assert hasattr(collections.abc, "Mapping")


class TestRelativeImports:
    """测试相对导入"""

    def test_relative_import_sibling_module(self):
        """测试: 使用相对导入导入同级模块"""
        with tempfile.TemporaryDirectory() as tmpdir:
            pkg_dir = Path(tmpdir) / "mypackage"
            pkg_dir.mkdir()

            (pkg_dir / "__init__.py").write_text("", encoding="utf-8")

            # 创建同级模块1
            module1 = pkg_dir / "utils.py"
            module1.write_text("""
def util_function():
    return "Utility function"
""", encoding="utf-8")

            # 创建同级模块2，使用相对导入
            module2 = pkg_dir / "main.py"
            module2.write_text("""
from .utils import util_function

def run():
    return util_function()
""", encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from mypackage.main import run

print(run())
"""

            script_path = Path(tmpdir) / "test_script.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "Utility function" in result.stdout

    def test_relative_import_parent_package(self):
        """测试: 使用相对导入从子包导入父包"""
        with tempfile.TemporaryDirectory() as tmpdir:
            pkg_dir = Path(tmpdir) / "mypackage"
            pkg_dir.mkdir()

            (pkg_dir / "__init__.py").write_text("", encoding="utf-8")

            # 创建父包模块
            parent_module = pkg_dir / "parent.py"
            parent_module.write_text("""
PARENT_VALUE = "from parent"
""", encoding="utf-8")

            # 创建子包
            subpkg_dir = pkg_dir / "subpkg"
            subpkg_dir.mkdir()

            (subpkg_dir / "__init__.py").write_text("", encoding="utf-8")

            # 子包模块使用相对导入导入父包
            child_module = subpkg_dir / "child.py"
            child_module.write_text("""
from ..parent import PARENT_VALUE

def get_parent_value():
    return PARENT_VALUE
""", encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from mypackage.subpkg.child import get_parent_value

print(get_parent_value())
"""

            script_path = Path(tmpdir) / "test_script.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "from parent" in result.stdout

    def test_relative_import_cannot_use_outside_package(self):
        """测试: 相对导入只能在包内使用"""
        # 创建一个尝试在包外使用相对导入的脚本
        script_content = """
# 尝试在顶层脚本中使用相对导入（会失败）
try:
    from .math import sqrt
    print("Relative import worked")
except ImportError as e:
    print(f"Relative import failed as expected: {e}")
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

            # 相对导入在顶层脚本中应该失败
            assert "failed" in result.stdout.lower() or "attempted relative import" in result.stderr.lower()
        finally:
            os.unlink(script_path)


class TestProjectStructure:
    """测试项目结构"""

    def test_typical_project_structure(self):
        """测试: 典型的项目结构"""
        with tempfile.TemporaryDirectory() as tmpdir:
            # 创建项目结构
            project_dir = Path(tmpdir) / "myproject"
            project_dir.mkdir()

            # 创建包目录
            pkg_dir = project_dir / "myproject"
            pkg_dir.mkdir()

            (pkg_dir / "__init__.py").write_text("", encoding="utf-8")
            (pkg_dir / "main.py").write_text("VERSION = '1.0'\n", encoding="utf-8")

            # 创建测试目录
            tests_dir = project_dir / "tests"
            tests_dir.mkdir()

            (tests_dir / "__init__.py").write_text("", encoding="utf-8")

            # 创建数据目录
            data_dir = project_dir / "data"
            data_dir.mkdir()

            # 验证目录结构
            assert pkg_dir.exists()
            assert (pkg_dir / "__init__.py").exists()
            assert (pkg_dir / "main.py").exists()
            assert tests_dir.exists()
            assert data_dir.exists()

    def test_separating_code_from_tests(self):
        """测试: 代码和测试分离"""
        with tempfile.TemporaryDirectory() as tmpdir:
            # 创建包
            pkg_dir = Path(tmpdir) / "calculator"
            pkg_dir.mkdir()

            (pkg_dir / "__init__.py").write_text("", encoding="utf-8")

            # 创建计算模块
            calc_module = pkg_dir / "calc.py"
            calc_module.write_text("""
def add(a, b):
    return a + b

def subtract(a, b):
    return a - b
""", encoding="utf-8")

            # 创建测试目录
            tests_dir = Path(tmpdir) / "tests"
            tests_dir.mkdir()

            (tests_dir / "__init__.py").write_text("", encoding="utf-8")

            # 创建测试模块
            test_module = tests_dir / "test_calc.py"
            test_module.write_text(f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from calculator.calc import add, subtract

def test_add():
    assert add(2, 3) == 5
    print("test_add passed")

def test_subtract():
    assert subtract(5, 3) == 2
    print("test_subtract passed")

if __name__ == "__main__":
    test_add()
    test_subtract()
    print("All tests passed!")
""", encoding="utf-8")

            # 运行测试
            result = subprocess.run(
                [sys.executable, str(test_module)],
                capture_output=True,
                text=True
            )

            assert "test_add passed" in result.stdout
            assert "test_subtract passed" in result.stdout
            assert "All tests passed!" in result.stdout


class TestImportErrorHandling:
    """测试导入错误处理"""

    def test_import_nonexistent_module_raises_error(self):
        """测试: 导入不存在的模块会报错"""
        with pytest.raises(ImportError):
            import nonexistent_package_12345

    def test_import_nonexistent_function_from_module_raises_error(self):
        """测试: 从模块导入不存在的函数会报错"""
        import math

        with pytest.raises(ImportError):
            from math import nonexistent_function

    def test_circular_import_causes_problem(self):
        """测试: 循环导入会导致问题（概念性测试）"""
        # 这个测试说明循环导入的概念
        # 实际创建循环导入需要多个文件，这里只做概念说明
        pass


class TestPackageVsModule:
    """测试包和模块的区别"""

    def test_module_is_single_file(self):
        """测试: 模块是单个文件"""
        import math

        # 模块有 __name__ 属性
        assert hasattr(math, "__name__")

    def test_package_is_directory_with_init(self):
        """测试: 包是包含 __init__.py 的目录"""
        import collections

        # 包通常有 __path__ 属性
        # 注意: 不是所有包都有 __path__，但大多数有
        # collections 在某些实现中是模块
        # 这里只测试概念

    def test_importing_package_executes_init_py(self):
        """测试: 导入包会执行 __init__.py"""
        with tempfile.TemporaryDirectory() as tmpdir:
            pkg_dir = Path(tmpdir) / "testpkg"
            pkg_dir.mkdir()

            # 创建 __init__.py
            init_file = pkg_dir / "__init__.py"
            init_file.write_text("""
print("__init__.py is being executed")
PACKAGE_VALUE = 42
""", encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
import testpkg

print(f"Package value: {{testpkg.PACKAGE_VALUE}}")
"""

            script_path = Path(tmpdir) / "test_script.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "__init__.py is being executed" in result.stdout
            assert "Package value: 42" in result.stdout


class TestNestedPackages:
    """测试嵌套包"""

    def test_nested_package_structure(self):
        """测试: 嵌套包结构"""
        with tempfile.TemporaryDirectory() as tmpdir:
            # 创建顶级包
            top_pkg = Path(tmpdir) / "top"
            top_pkg.mkdir()
            (top_pkg / "__init__.py").write_text("# Top package\n", encoding="utf-8")

            # 创建子包
            sub_pkg = top_pkg / "sub"
            sub_pkg.mkdir()
            (sub_pkg / "__init__.py").write_text("# Sub package\n", encoding="utf-8")

            # 创建子子包
            subsub_pkg = sub_pkg / "subsub"
            subsub_pkg.mkdir()
            (subsub_pkg / "__init__.py").write_text("# Sub-sub package\n", encoding="utf-8")

            # 创建模块
            module = subsub_pkg / "module.py"
            module.write_text("VALUE = 'deep value'\n", encoding="utf-8")

            script_content = f"""
import sys
sys.path.insert(0, {repr(tmpdir)})
from top.sub.subsub.module import VALUE

print(f"Value: {{VALUE}}")
"""

            script_path = Path(tmpdir) / "test_script.py"
            script_path.write_text(script_content, encoding="utf-8")

            result = subprocess.run(
                [sys.executable, str(script_path)],
                capture_output=True,
                text=True
            )

            assert "Value: deep value" in result.stdout


class TestImportBestPractices:
    """测试导入最佳实践"""

    def test_absolute_import_preferred_over_relative(self):
        """测试: 绝对导入优先于相对导入（Python 官方推荐）"""
        # 这个测试说明概念: Python 3 默认使用绝对导入
        # 相对导入只在包内部使用
        import sys
        import os

        # 这些是绝对导入
        assert isinstance(sys.path, list)
        assert isinstance(os.path, type(os))

    def test_avoid_import_cycles(self):
        """测试: 避免循环导入"""
        # 这个测试说明概念
        # 循环导入: A 导入 B，B 导入 A
        # 解决方法:
        # 1. 重构代码，提取共同依赖到第三个模块
        # 2. 延迟导入（在函数内导入）
        # 3. 重新设计模块结构
        pass

    def test_keep_init_py_simple(self):
        """测试: 保持 __init__.py 简单"""
        # 这个测试说明概念: __init__.py 应该尽量简单
        # 只做必要的初始化和导入暴露
        # 不应该包含大量业务逻辑
        pass

    def test_use_descriptive_package_names(self):
        """测试: 使用描述性的包名"""
        # 好的包名: lowercase, underscore_separated
        # 避免使用连字符（因为 Python 标识符不能包含连字符）
        good_names = ["mypackage", "my_package", "calculator"]
        bad_names = ["my-package", "MyPackage", "my.package"]

        # 这个测试只说明概念
        assert "my" in good_names[0]
        assert "-" not in good_names[0]
