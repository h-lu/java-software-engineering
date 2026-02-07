"""测试 week_01 starter_code/solution.py。

测试目标:
- 测试 solve(text) 函数（核心测试）
- 测试 greet(name) 函数（如果已实现）
- 测试边界情况（空字符串、特殊字符等）

注意: greet/get_user_info/main 是学生需要实现的函数，
如果未实现（返回 None），相关测试会被跳过。
"""

from __future__ import annotations

import importlib.util
import sys
from pathlib import Path

import pytest


def load_solution_module():
    """动态加载 solution.py 模块。"""
    root = Path(__file__).resolve().parents[1]
    p = root / "starter_code" / "solution.py"
    spec = importlib.util.spec_from_file_location("week_solution", p)
    assert spec and spec.loader, f"无法加载 solution.py: {p}"
    m = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(m)
    return m


# 加载 solution 模块
solution = load_solution_module()


def is_implemented(func, test_input, expected_not_none: bool = True) -> bool:
    """检查函数是否已实现（不是返回 None）。"""
    try:
        result = func(test_input)
        return result is not None if expected_not_none else True
    except Exception:
        return False


class TestSolve:
    """测试 solve(text) 函数 - 核心函数，必须实现。"""

    def test_solve_exists(self) -> None:
        """测试 solve 函数存在。"""
        assert hasattr(solution, "solve"), "solution 模块缺少 solve 函数"

    def test_solve_returns_string(self) -> None:
        """测试 solve 函数返回字符串。"""
        result = solution.solve("hello")
        assert isinstance(result, str), f"solve 应返回字符串，实际返回 {type(result)}"

    def test_solve_returns_input(self) -> None:
        """测试 solve 函数返回输入文本（当前实现）。"""
        test_cases = [
            "hello",
            "world",
            "你好，Python！",
            "",
        ]
        for text in test_cases:
            result = solution.solve(text)
            assert result == text, f"solve('{text}') 应返回 '{text}'，实际: {result}"

    def test_solve_with_empty_string(self) -> None:
        """测试 solve 函数使用空字符串。"""
        result = solution.solve("")
        assert result == "", "solve('') 应返回空字符串"

    def test_solve_with_unicode(self) -> None:
        """测试 solve 函数使用 Unicode 字符。"""
        text = "你好，世界！"
        result = solution.solve(text)
        assert result == text, f"solve 应正确处理 Unicode，实际: {result}"

    def test_solve_with_special_chars(self) -> None:
        """测试 solve 函数使用特殊字符。"""
        text = "Hello\nWorld\t!"
        result = solution.solve(text)
        assert result == text, "solve 应正确处理特殊字符"

    def test_solve_with_long_text(self) -> None:
        """测试 solve 函数使用超长文本。"""
        text = "A" * 10000
        result = solution.solve(text)
        assert result == text, "solve 应能处理超长文本"


class TestGreet:
    """测试 greet(name) 函数 - 学生作业，可选测试。"""

    def test_greet_exists(self) -> None:
        """测试 greet 函数存在。"""
        assert hasattr(solution, "greet"), "solution 模块缺少 greet 函数"

    def test_greet_returns_string(self) -> None:
        """测试 greet 函数返回字符串。"""
        if not is_implemented(solution.greet, "Alice"):
            pytest.skip("greet 函数尚未实现（返回 None）")
        result = solution.greet("Alice")
        assert isinstance(result, str), f"greet 应返回字符串，实际返回 {type(result)}"

    def test_greet_with_normal_name(self) -> None:
        """测试 greet 函数使用普通名字。"""
        if not is_implemented(solution.greet, "Alice"):
            pytest.skip("greet 函数尚未实现（返回 None）")
        result = solution.greet("Alice")
        # 接受 "你好, Alice!" 或 "你好，Alice！"（中英文标点）
        assert "Alice" in result, f"问候语应包含名字，实际: {result}"
        assert "你好" in result, f"问候语应包含'你好'，实际: {result}"

    def test_greet_with_chinese_name(self) -> None:
        """测试 greet 函数使用中文名字。"""
        if not is_implemented(solution.greet, "张三"):
            pytest.skip("greet 函数尚未实现（返回 None）")
        result = solution.greet("张三")
        assert "张三" in result, f"问候语应包含中文名字，实际: {result}"
        assert "你好" in result, f"问候语应包含'你好'，实际: {result}"

    def test_greet_with_empty_string(self) -> None:
        """测试 greet 函数使用空字符串。"""
        if not is_implemented(solution.greet, ""):
            pytest.skip("greet 函数尚未实现（返回 None）")
        result = solution.greet("")
        assert isinstance(result, str), "greet('') 应返回字符串"

    def test_greet_with_special_characters(self) -> None:
        """测试 greet 函数使用特殊字符。"""
        if not is_implemented(solution.greet, "<script>"):
            pytest.skip("greet 函数尚未实现（返回 None）")
        result = solution.greet("<script>alert('xss')</script>")
        assert "<script>" in result, "greet 应保留特殊字符（不转义）"

    def test_greet_with_long_name(self) -> None:
        """测试 greet 函数使用超长名字。"""
        long_name = "A" * 1000
        if not is_implemented(solution.greet, long_name):
            pytest.skip("greet 函数尚未实现（返回 None）")
        result = solution.greet(long_name)
        assert long_name in result, "greet 应能处理超长名字"

    def test_greet_with_whitespace(self) -> None:
        """测试 greet 函数使用带空格的名字。"""
        if not is_implemented(solution.greet, "Alice Wang"):
            pytest.skip("greet 函数尚未实现（返回 None）")
        result = solution.greet("Alice Wang")
        assert "Alice Wang" in result, "greet 应保留名字中的空格"


class TestGetUserInfo:
    """测试 get_user_info() 函数 - 学生作业，可选测试。"""

    def test_get_user_info_exists(self) -> None:
        """测试 get_user_info 函数存在。"""
        assert hasattr(solution, "get_user_info"), "solution 模块缺少 get_user_info 函数"


class TestMain:
    """测试 main() 函数 - 学生作业，可选测试。"""

    def test_main_exists(self) -> None:
        """测试 main 函数存在。"""
        assert hasattr(solution, "main"), "solution 模块缺少 main 函数"


class TestModuleStructure:
    """测试模块结构完整性。"""

    def test_module_has_all_required_functions(self) -> None:
        """测试模块包含所有必需的函数。"""
        required_functions = ["greet", "get_user_info", "solve", "main"]
        for func_name in required_functions:
            assert hasattr(solution, func_name), f"solution 模块缺少 {func_name} 函数"

    def test_module_can_be_imported(self) -> None:
        """测试模块可以被成功导入。"""
        # 如果执行到这里，说明模块已经加载成功
        assert solution is not None
