"""测试 week_01 示例代码。

测试目标:
- 确保所有示例代码可以正常运行
- 验证示例代码的输出符合预期
"""

from __future__ import annotations

import importlib.util
import subprocess
import sys
from pathlib import Path

import pytest


EXAMPLES_DIR = Path(__file__).resolve().parents[1] / "examples"


def run_example(filename: str, input_data: str | None = None) -> tuple[str, str, int]:
    """运行示例文件并返回 stdout, stderr, returncode.

    Args:
        filename: 示例文件名
        input_data: 可选的输入数据（用于模拟用户输入）

    Returns:
        (stdout, stderr, returncode) 元组
    """
    filepath = EXAMPLES_DIR / filename
    result = subprocess.run(
        [sys.executable, str(filepath)],
        input=input_data,
        capture_output=True,
        text=True,
    )
    return result.stdout, result.stderr, result.returncode


class Test01Hello:
    """测试 01_hello.py 示例."""

    def test_hello_runs_without_error(self) -> None:
        """测试 01_hello.py 可以正常运行且不报错."""
        stdout, stderr, rc = run_example("01_hello.py")
        assert rc == 0, f"程序退出码非零，stderr: {stderr}"

    def test_hello_outputs_expected_text(self) -> None:
        """测试 01_hello.py 输出包含预期的文本."""
        stdout, _, _ = run_example("01_hello.py")
        assert "Hello, World!" in stdout
        assert "你好，Python！" in stdout
        assert "第一行" in stdout
        assert "第二行" in stdout


class Test02Variables:
    """测试 02_variables.py 示例."""

    def test_variables_runs_without_error(self) -> None:
        """测试 02_variables.py 可以正常运行且不报错."""
        stdout, stderr, rc = run_example("02_variables.py")
        assert rc == 0, f"程序退出码非零，stderr: {stderr}"

    def test_variables_outputs_expected_content(self) -> None:
        """测试 02_variables.py 输出包含预期的变量信息."""
        stdout, _, _ = run_example("02_variables.py")
        assert "用户名: Alice" in stdout
        assert "年龄: 20" in stdout
        assert "身高: 1.65 米" in stdout
        assert "<class 'str'>" in stdout
        assert "<class 'int'>" in stdout
        assert "<class 'float'>" in stdout
        assert "明年年龄: 21" in stdout


class Test03InputOutput:
    """测试 03_input_output.py 示例（需要模拟输入）."""

    def test_input_output_runs_with_simulated_input(self) -> None:
        """测试 03_input_output.py 使用模拟输入可以正常运行."""
        # 模拟用户输入：名字和年龄
        input_data = "Alice\n20\n"
        stdout, stderr, rc = run_example("03_input_output.py", input_data)
        assert rc == 0, f"程序退出码非零，stderr: {stderr}"

    def test_input_output_formats_correctly(self) -> None:
        """测试 03_input_output.py 正确格式化输出."""
        input_data = "Alice\n20\n"
        stdout, _, _ = run_example("03_input_output.py", input_data)
        assert "你好, Alice!" in stdout
        assert "输入的类型是:" in stdout
        assert "<class 'str'>" in stdout
        assert "Alice, 明年你就 21 岁了。" in stdout
        assert "Alice 今年 20 岁" in stdout
        assert "欢迎, Alice!" in stdout

    def test_input_output_handles_different_names(self) -> None:
        """测试 03_input_output.py 能处理不同的名字输入."""
        input_data = "张三\n25\n"
        stdout, _, _ = run_example("03_input_output.py", input_data)
        assert "你好, 张三!" in stdout
        assert "张三, 明年你就 26 岁了。" in stdout


class Test04StringBasics:
    """测试 04_string_basics.py 示例."""

    def test_string_basics_runs_without_error(self) -> None:
        """测试 04_string_basics.py 可以正常运行且不报错."""
        stdout, stderr, rc = run_example("04_string_basics.py")
        assert rc == 0, f"程序退出码非零，stderr: {stderr}"

    def test_string_basics_outputs_expected_operations(self) -> None:
        """测试 04_string_basics.py 输出包含所有字符串操作结果."""
        stdout, _, _ = run_example("04_string_basics.py")
        assert "原始字符串: Hello Python" in stdout
        assert "第一个字符: H" in stdout
        assert "最后一个字符: n" in stdout
        assert "前5个字符: Hello" in stdout
        assert "大写: HELLO PYTHON" in stdout
        assert "小写: hello python" in stdout
        assert "去空格: Hello Python" in stdout
        assert "替换后: Hi Python" in stdout
        assert "分割结果: ['Hello', 'Python']" in stdout
        assert "字符串长度: 12" in stdout
        assert "原字符串仍然是: Hello Python" in stdout


class Test05CommonErrors:
    """测试 05_common_errors.py 示例（所有错误都被注释掉了）."""

    def test_common_errors_runs_without_error(self) -> None:
        """测试 05_common_errors.py 默认情况下可以正常运行（错误都被注释）."""
        stdout, stderr, rc = run_example("05_common_errors.py")
        assert rc == 0, f"程序退出码非零，stderr: {stderr}"

    def test_common_errors_outputs_hello(self) -> None:
        """测试 05_common_errors.py 输出 Hello（来自正确示例）."""
        stdout, _, _ = run_example("05_common_errors.py")
        assert "Hello" in stdout

    def test_common_errors_shows_error_tips(self) -> None:
        """测试 05_common_errors.py 显示错误排查小贴士."""
        stdout, _, _ = run_example("05_common_errors.py")
        assert "错误排查小贴士" in stdout
        assert "从下往上阅读错误信息" in stdout


class TestAllExamplesExist:
    """测试所有示例文件都存在."""

    @pytest.mark.parametrize(
        "filename",
        [
            "01_hello.py",
            "02_variables.py",
            "03_input_output.py",
            "04_string_basics.py",
            "05_common_errors.py",
        ],
    )
    def test_example_file_exists(self, filename: str) -> None:
        """测试示例文件存在."""
        filepath = EXAMPLES_DIR / filename
        assert filepath.exists(), f"示例文件不存在: {filepath}"
