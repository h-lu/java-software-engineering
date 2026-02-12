"""Week 03 冒烟测试

验证测试环境是否正常，以及 Java 编译器是否可用。
"""

from __future__ import annotations

import subprocess
import tempfile
from pathlib import Path

import pytest


def test_pytest_is_working():
    """验证 pytest 本身正常工作"""
    assert True


def test_java_compiler_is_available():
    """验证 Java 编译器可用"""
    result = subprocess.run(["javac", "-version"], capture_output=True, text=True)
    assert result.returncode == 0, "Java 编译器应该可用"


def test_java_runtime_is_available():
    """验证 Java 运行时可用"""
    result = subprocess.run(["java", "-version"], capture_output=True, text=True)
    assert result.returncode == 0, "Java 运行时应该可用"


def test_can_compile_and_run_simple_java():
    """验证可以编译和运行简单的 Java 程序"""
    java_code = '''
public class SmokeTest {
    public static void main(String[] args) {
        System.out.println("Hello from Java");
    }
}
'''
    with tempfile.TemporaryDirectory() as tmpdir:
        java_file = Path(tmpdir) / "SmokeTest.java"
        java_file.write_text(java_code)

        # 编译
        result = subprocess.run(
            ["javac", str(java_file)],
            capture_output=True, text=True
        )
        assert result.returncode == 0, f"编译失败: {result.stderr}"

        # 运行
        result = subprocess.run(
            ["java", "-cp", tmpdir, "SmokeTest"],
            capture_output=True, text=True
        )
        assert result.returncode == 0, f"运行失败: {result.stderr}"
        assert "Hello from Java" in result.stdout


def test_exception_handling_basics():
    """验证基本的异常处理机制可用"""
    java_code = '''
public class ExceptionSmokeTest {
    public static void main(String[] args) {
        try {
            throw new RuntimeException("测试异常");
        } catch (RuntimeException e) {
            System.out.println("捕获成功: " + e.getMessage());
        }
    }
}
'''
    with tempfile.TemporaryDirectory() as tmpdir:
        java_file = Path(tmpdir) / "ExceptionSmokeTest.java"
        java_file.write_text(java_code)

        result = subprocess.run(
            ["javac", str(java_file)],
            capture_output=True, text=True
        )
        assert result.returncode == 0, f"编译失败: {result.stderr}"

        result = subprocess.run(
            ["java", "-cp", tmpdir, "ExceptionSmokeTest"],
            capture_output=True, text=True
        )
        assert result.returncode == 0
        assert "捕获成功: 测试异常" in result.stdout
