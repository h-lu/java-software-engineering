"""Week 03 测试：异常处理与防御式编程

本测试文件验证 Week 03 的学习目标：
1. 受检异常（checked exception）与运行时异常（runtime exception）的处理
2. try-catch-finally 结构的正确使用
3. 防御式编程原则（输入验证、边界检查）
4. 自定义异常类的设计与使用

测试设计原则：
- 正例测试：验证正常输入下的预期行为
- 边界测试：验证临界值、空值、极值等情况
- 反例测试：验证异常输入应抛出特定异常

注意：由于这是一个 Java 教材项目，测试使用 pytest 框架来验证 Java 代码的行为。
实际运行时需要编译和执行 Java 代码。
"""

from __future__ import annotations

import subprocess
import tempfile
import os
from pathlib import Path

import pytest


# ==================== 测试辅助函数 ====================

def compile_java(java_file: Path, classpath: str = None) -> tuple[bool, str]:
    """编译 Java 文件，返回 (是否成功, 错误信息)"""
    cmd = ["javac"]
    if classpath:
        cmd.extend(["-cp", classpath])
    cmd.append(str(java_file))

    result = subprocess.run(cmd, capture_output=True, text=True)
    return result.returncode == 0, result.stderr


def run_java(class_name: str, classpath: str = None) -> tuple[bool, str, str]:
    """运行 Java 类，返回 (是否成功, 标准输出, 标准错误)"""
    cmd = ["java"]
    if classpath:
        cmd.extend(["-cp", classpath])
    cmd.append(class_name)

    result = subprocess.run(cmd, capture_output=True, text=True)
    return result.returncode == 0, result.stdout, result.stderr


# ==================== 自定义异常类测试 ====================

class TestCustomExceptions:
    """测试自定义异常类的正确继承和消息内容"""

    def test_campus_flow_exception_extends_exception(self):
        """正例：CampusFlowException 应该继承自 Exception（受检异常）"""
        # 验证自定义异常继承层次
        java_code = '''
public class TestCampusFlowException extends Exception {
    public TestCampusFlowException(String message) {
        super(message);
    }

    public static void main(String[] args) {
        try {
            throw new TestCampusFlowException("测试异常");
        } catch (Exception e) {
            System.out.println("捕获到异常: " + e.getMessage());
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestCampusFlowException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestCampusFlowException", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "捕获到异常: 测试异常" in stdout

    def test_task_not_found_exception_has_correct_message(self):
        """正例：TaskNotFoundException 应该包含任务标题信息"""
        java_code = '''
public class TestTaskNotFoundException extends Exception {
    public TestTaskNotFoundException(String taskTitle) {
        super("找不到任务: " + taskTitle);
    }

    public static void main(String[] args) {
        try {
            throw new TestTaskNotFoundException("完成Java作业");
        } catch (TestTaskNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestTaskNotFoundException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestTaskNotFoundException", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "找不到任务: 完成Java作业" in stdout

    def test_invalid_task_data_exception_has_field_and_reason(self):
        """正例：InvalidTaskDataException 应该包含字段名和原因"""
        java_code = '''
public class TestInvalidTaskDataException extends Exception {
    public TestInvalidTaskDataException(String field, String reason) {
        super("任务数据无效 [" + field + "]: " + reason);
    }

    public static void main(String[] args) {
        try {
            throw new TestInvalidTaskDataException("title", "标题不能为空");
        } catch (TestInvalidTaskDataException e) {
            System.out.println(e.getMessage());
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestInvalidTaskDataException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestInvalidTaskDataException", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "任务数据无效 [title]: 标题不能为空" in stdout


# ==================== 受检异常 vs 运行时异常测试 ====================

class TestCheckedVsRuntimeExceptions:
    """测试受检异常与运行时异常的区别和处理方式"""

    def test_checked_exception_must_be_caught_or_declared(self):
        """正例：受检异常必须被捕获或声明抛出"""
        # 这个测试验证受检异常的处理方式
        java_code = '''
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestCheckedException {
    // 方法声明抛出受检异常
    public void readFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        scanner.close();
    }

    // 方法捕获受检异常
    public void readFileWithCatch(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("捕获到文件不存在异常");
        }
    }

    public static void main(String[] args) {
        TestCheckedException test = new TestCheckedException();
        test.readFileWithCatch("不存在的文件.txt");
        System.out.println("程序正常结束");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestCheckedException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestCheckedException", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "捕获到文件不存在异常" in stdout
            assert "程序正常结束" in stdout

    def test_runtime_exception_does_not_need_declaration(self):
        """正例：运行时异常不需要强制声明或捕获"""
        java_code = '''
public class TestRuntimeException {
    // 方法抛出运行时异常，不需要声明
    public void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        System.out.println("标题有效: " + title);
    }

    public static void main(String[] args) {
        TestRuntimeException test = new TestRuntimeException();

        // 测试有效标题
        test.validateTitle("有效标题");

        // 测试无效标题（应该抛出异常）
        try {
            test.validateTitle("");
            System.out.println("不应该执行到这里");
        } catch (IllegalArgumentException e) {
            System.out.println("捕获到运行时异常: " + e.getMessage());
        }

        System.out.println("程序正常结束");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestRuntimeException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestRuntimeException", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "标题有效: 有效标题" in stdout
            assert "捕获到运行时异常: 标题不能为空" in stdout
            assert "程序正常结束" in stdout

    def test_null_pointer_exception_is_runtime(self):
        """边界：NullPointerException 是运行时异常，不需要强制处理"""
        java_code = '''
public class TestNullPointerException {
    public static void main(String[] args) {
        String nullString = null;

        try {
            // 尝试调用 null 对象的方法
            int length = nullString.length();
            System.out.println("长度: " + length);
        } catch (NullPointerException e) {
            System.out.println("捕获到 NullPointerException");
        }

        System.out.println("程序继续执行");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestNullPointerException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestNullPointerException", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "捕获到 NullPointerException" in stdout
            assert "程序继续执行" in stdout


# ==================== try-catch-finally 结构测试 ====================

class TestTryCatchFinally:
    """测试 try-catch-finally 结构的正确性"""

    def test_try_catch_catches_exception(self):
        """正例：try-catch 能正确捕获异常"""
        java_code = '''
public class TestTryCatch {
    public static void main(String[] args) {
        System.out.println("开始");

        try {
            System.out.println("在 try 块中");
            int result = 10 / 0;  // 会抛出 ArithmeticException
            System.out.println("这行不会执行");
        } catch (ArithmeticException e) {
            System.out.println("捕获到算术异常");
        }

        System.out.println("结束");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestTryCatch.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestTryCatch", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "开始" in stdout
            assert "在 try 块中" in stdout
            assert "捕获到算术异常" in stdout
            assert "结束" in stdout
            assert "这行不会执行" not in stdout

    def test_finally_always_executes_on_exception(self):
        """正例：发生异常时 finally 块仍然执行"""
        java_code = '''
public class TestFinallyWithException {
    public static void main(String[] args) {
        System.out.println("开始");

        try {
            System.out.println("在 try 块中");
            throw new RuntimeException("测试异常");
        } catch (RuntimeException e) {
            System.out.println("捕获异常");
        } finally {
            System.out.println("finally 块执行");
        }

        System.out.println("结束");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestFinallyWithException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestFinallyWithException", tmpdir)
            assert success, f"运行失败: {stderr}"
            lines = stdout.strip().split('\n')
            assert lines == ["开始", "在 try 块中", "捕获异常", "finally 块执行", "结束"]

    def test_finally_always_executes_without_exception(self):
        """正例：没有异常时 finally 块也执行"""
        java_code = '''
public class TestFinallyWithoutException {
    public static void main(String[] args) {
        System.out.println("开始");

        try {
            System.out.println("在 try 块中（无异常）");
        } catch (Exception e) {
            System.out.println("不会执行");
        } finally {
            System.out.println("finally 块执行");
        }

        System.out.println("结束");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestFinallyWithoutException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestFinallyWithoutException", tmpdir)
            assert success, f"运行失败: {stderr}"
            lines = stdout.strip().split('\n')
            assert lines == ["开始", "在 try 块中（无异常）", "finally 块执行", "结束"]

    def test_multiple_catch_blocks_match_correct_type(self):
        """正例：多个 catch 块按顺序匹配正确的异常类型"""
        java_code = '''
public class TestMultipleCatch {
    public static void main(String[] args) {
        System.out.println("测试1: 算术异常");
        testException(new ArithmeticException("除以零"));

        System.out.println("测试2: 空指针异常");
        testException(new NullPointerException("空引用"));

        System.out.println("测试3: 非法参数异常");
        testException(new IllegalArgumentException("非法参数"));
    }

    static void testException(RuntimeException e) {
        try {
            throw e;
        } catch (ArithmeticException ae) {
            System.out.println("捕获到 ArithmeticException");
        } catch (NullPointerException npe) {
            System.out.println("捕获到 NullPointerException");
        } catch (IllegalArgumentException iae) {
            System.out.println("捕获到 IllegalArgumentException");
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestMultipleCatch.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestMultipleCatch", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "捕获到 ArithmeticException" in stdout
            assert "捕获到 NullPointerException" in stdout
            assert "捕获到 IllegalArgumentException" in stdout

    def test_exception_propagation_up_call_stack(self):
        """正例：异常沿着调用链向上传播直到被捕获"""
        java_code = '''
public class TestExceptionPropagation {
    public static void main(String[] args) {
        System.out.println("main 开始");

        try {
            methodA();
        } catch (RuntimeException e) {
            System.out.println("main 捕获异常: " + e.getMessage());
        }

        System.out.println("main 结束");
    }

    static void methodA() {
        System.out.println("methodA 开始");
        methodB();
        System.out.println("methodA 结束（不会执行）");
    }

    static void methodB() {
        System.out.println("methodB 开始");
        methodC();
        System.out.println("methodB 结束（不会执行）");
    }

    static void methodC() {
        System.out.println("methodC 开始");
        throw new RuntimeException("在 methodC 中抛出");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestExceptionPropagation.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestExceptionPropagation", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "main 开始" in stdout
            assert "methodA 开始" in stdout
            assert "methodB 开始" in stdout
            assert "methodC 开始" in stdout
            assert "main 捕获异常: 在 methodC 中抛出" in stdout
            assert "main 结束" in stdout
            assert "不会执行" not in stdout


# ==================== 防御式编程测试 ====================

class TestDefensiveProgramming:
    """测试防御式编程原则的应用"""

    def test_null_input_validation(self):
        """反例：传入 null 应该抛出 IllegalArgumentException"""
        java_code = '''
public class TestNullValidation {
    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("标题不能为 null");
        }
        System.out.println("标题设置为: " + title);
    }

    public static void main(String[] args) {
        TestNullValidation test = new TestNullValidation();

        try {
            test.setTitle(null);
            System.out.println("不应该执行到这里");
        } catch (IllegalArgumentException e) {
            System.out.println("验证通过: " + e.getMessage());
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestNullValidation.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestNullValidation", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "验证通过: 标题不能为 null" in stdout
            assert "不应该执行到这里" not in stdout

    def test_empty_string_validation(self):
        """反例：传入空字符串应该抛出 IllegalArgumentException"""
        java_code = '''
public class TestEmptyStringValidation {
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        System.out.println("标题设置为: " + title);
    }

    public static void main(String[] args) {
        TestEmptyStringValidation test = new TestEmptyStringValidation();

        // 测试空字符串
        try {
            test.setTitle("");
            System.out.println("空字符串测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("空字符串验证通过");
        }

        // 测试纯空格
        try {
            test.setTitle("   ");
            System.out.println("空格测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("空格验证通过");
        }

        // 测试有效标题
        test.setTitle("有效标题");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestEmptyStringValidation.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestEmptyStringValidation", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "空字符串验证通过" in stdout
            assert "空格验证通过" in stdout
            assert "标题设置为: 有效标题" in stdout

    def test_string_length_validation(self):
        """边界：超长字符串应该被验证拒绝"""
        java_code = '''
public class TestLengthValidation {
    private static final int MAX_LENGTH = 100;

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (title.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("标题不能超过 " + MAX_LENGTH + " 字符");
        }
        System.out.println("标题长度: " + title.length());
    }

    public static void main(String[] args) {
        TestLengthValidation test = new TestLengthValidation();

        // 测试刚好 100 字符（边界值）
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("a");
        }
        test.setTitle(sb.toString());

        // 测试 101 字符（超出边界）
        try {
            test.setTitle(sb.toString() + "b");
            System.out.println("超长测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("超长验证通过: " + e.getMessage());
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestLengthValidation.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestLengthValidation", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "标题长度: 100" in stdout
            assert "超长验证通过: 标题不能超过 100 字符" in stdout

    def test_enum_value_validation(self):
        """边界：枚举值范围验证"""
        java_code = '''
public class TestEnumValidation {
    private int priority;  // 1=高, 2=中, 3=低

    public void setPriority(int priority) {
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级必须是 1（高）、2（中）或 3（低）");
        }
        this.priority = priority;
        System.out.println("优先级设置为: " + priority);
    }

    public static void main(String[] args) {
        TestEnumValidation test = new TestEnumValidation();

        // 测试有效值
        test.setPriority(1);
        test.setPriority(2);
        test.setPriority(3);

        // 测试边界值 0
        try {
            test.setPriority(0);
            System.out.println("边界值 0 测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("边界值 0 验证通过");
        }

        // 测试边界值 4
        try {
            test.setPriority(4);
            System.out.println("边界值 4 测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("边界值 4 验证通过");
        }

        // 测试负值
        try {
            test.setPriority(-1);
            System.out.println("负值测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("负值验证通过");
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestEnumValidation.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestEnumValidation", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "优先级设置为: 1" in stdout
            assert "优先级设置为: 2" in stdout
            assert "优先级设置为: 3" in stdout
            assert "边界值 0 验证通过" in stdout
            assert "边界值 4 验证通过" in stdout
            assert "负值验证通过" in stdout

    def test_date_format_validation(self):
        """边界：日期格式验证（YYYY-MM-DD）"""
        java_code = '''
public class TestDateValidation {
    public void setDueDate(String dueDate) {
        if (dueDate == null || dueDate.trim().isEmpty()) {
            throw new IllegalArgumentException("日期不能为空");
        }
        if (!dueDate.matches("\\\\d{4}-\\\\d{2}-\\\\d{2}")) {
            throw new IllegalArgumentException("日期格式无效，应为 YYYY-MM-DD");
        }
        System.out.println("日期设置为: " + dueDate);
    }

    public static void main(String[] args) {
        TestDateValidation test = new TestDateValidation();

        // 测试有效日期
        test.setDueDate("2025-12-31");

        // 测试无效格式 1：2025/12/31
        try {
            test.setDueDate("2025/12/31");
            System.out.println("斜杠格式测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("斜杠格式验证通过");
        }

        // 测试无效格式 2：01-15-2025
        try {
            test.setDueDate("01-15-2025");
            System.out.println("美式格式测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("美式格式验证通过");
        }

        // 测试无效格式 3：2025-13-45（虽然格式对，但值非法）
        // 注意：这个测试只验证格式，不验证值的有效性
        test.setDueDate("2025-13-45");  // 格式正确，可以通过
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestDateValidation.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestDateValidation", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "日期设置为: 2025-12-31" in stdout
            assert "斜杠格式验证通过" in stdout
            assert "美式格式验证通过" in stdout
            assert "日期设置为: 2025-13-45" in stdout

    def test_fail_fast_principle(self):
        """正例：尽早失败原则（在入口处验证）"""
        java_code = '''
public class TestFailFast {
    public void processTask(String title, int priority) {
        // 在入口处验证所有参数
        if (title == null) {
            throw new IllegalArgumentException("标题不能为 null");
        }
        if (priority < 1 || priority > 3) {
            throw new IllegalArgumentException("优先级无效");
        }

        // 验证通过后才执行业务逻辑
        System.out.println("处理任务: " + title + ", 优先级: " + priority);
    }

    public static void main(String[] args) {
        TestFailFast test = new TestFailFast();

        // 测试尽早失败
        try {
            test.processTask(null, 1);
            System.out.println("null 标题测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("null 标题在入口处被拦截");
        }

        try {
            test.processTask("有效标题", 5);
            System.out.println("无效优先级测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("无效优先级在入口处被拦截");
        }

        // 测试正常执行
        test.processTask("有效标题", 2);
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestFailFast.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestFailFast", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "null 标题在入口处被拦截" in stdout
            assert "无效优先级在入口处被拦截" in stdout
            assert "处理任务: 有效标题, 优先级: 2" in stdout


# ==================== 异常类型匹配测试 ====================

class TestExceptionTypeMatching:
    """测试异常类型匹配和捕获规则"""

    def test_catch_parent_catches_child(self):
        """正例：捕获父类异常也能捕获子类异常"""
        java_code = '''
public class TestParentCatch {
    public static void main(String[] args) {
        try {
            throw new IllegalArgumentException("子类异常");
        } catch (RuntimeException e) {
            System.out.println("RuntimeException 捕获: " + e.getMessage());
        }

        try {
            throw new NullPointerException("子类异常");
        } catch (Exception e) {
            System.out.println("Exception 捕获: " + e.getMessage());
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestParentCatch.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestParentCatch", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "RuntimeException 捕获: 子类异常" in stdout
            assert "Exception 捕获: 子类异常" in stdout

    def test_specific_catch_takes_precedence(self):
        """正例：具体的异常类型优先匹配"""
        java_code = '''
public class TestSpecificCatch {
    public static void main(String[] args) {
        try {
            throw new IllegalArgumentException("测试");
        } catch (IllegalArgumentException e) {
            System.out.println("具体类型捕获");
        } catch (RuntimeException e) {
            System.out.println("不应该执行到这里");
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestSpecificCatch.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestSpecificCatch", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "具体类型捕获" in stdout
            assert "不应该执行到这里" not in stdout

    def test_unmatched_exception_propagates(self):
        """反例：未匹配的异常会向上传播"""
        java_code = '''
public class TestUnmatchedException {
    public static void main(String[] args) {
        try {
            try {
                throw new NullPointerException("未匹配");
            } catch (IllegalArgumentException e) {
                System.out.println("不会捕获");
            }
        } catch (NullPointerException e) {
            System.out.println("外层捕获: " + e.getMessage());
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestUnmatchedException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestUnmatchedException", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "外层捕获: 未匹配" in stdout
            assert "不会捕获" not in stdout


# ==================== 综合场景测试 ====================

class TestIntegratedScenarios:
    """综合场景测试，模拟 CampusFlow 中的异常处理"""

    def test_task_manager_add_null_task(self):
        """反例：TaskManager 添加 null 任务应该抛出异常"""
        java_code = '''
import java.util.ArrayList;
import java.util.List;

public class TestTaskManagerDefensive {
    private List<String> tasks = new ArrayList<>();

    public void addTask(String task) {
        if (task == null) {
            throw new IllegalArgumentException("任务不能为 null");
        }
        tasks.add(task);
        System.out.println("任务已添加: " + task);
    }

    public static void main(String[] args) {
        TestTaskManagerDefensive manager = new TestTaskManagerDefensive();

        // 正常添加
        manager.addTask("完成任务");

        // 尝试添加 null
        try {
            manager.addTask(null);
            System.out.println("测试失败");
        } catch (IllegalArgumentException e) {
            System.out.println("验证通过: " + e.getMessage());
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestTaskManagerDefensive.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestTaskManagerDefensive", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "任务已添加: 完成任务" in stdout
            assert "验证通过: 任务不能为 null" in stdout

    def test_resource_cleanup_in_finally(self):
        """正例：在 finally 中清理资源"""
        java_code = '''
public class TestResourceCleanup {
    private static boolean resourceClosed = false;

    public static void main(String[] args) {
        try {
            System.out.println("使用资源");
            throw new RuntimeException("发生错误");
        } catch (RuntimeException e) {
            System.out.println("捕获异常");
        } finally {
            resourceClosed = true;
            System.out.println("资源已清理");
        }

        System.out.println("资源状态: " + (resourceClosed ? "已关闭" : "未关闭"));
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestResourceCleanup.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestResourceCleanup", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "使用资源" in stdout
            assert "捕获异常" in stdout
            assert "资源已清理" in stdout
            assert "资源状态: 已关闭" in stdout

    def test_graceful_degradation(self):
        """正例：优雅降级——文件不存在时返回空列表而不是崩溃"""
        java_code = '''
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestGracefulDegradation {
    public List<String> loadTasksFromFile(String filename) {
        List<String> tasks = new ArrayList<>();

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                tasks.add(scanner.nextLine());
            }
            scanner.close();
            System.out.println("成功加载 " + tasks.size() + " 个任务");
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在，返回空列表");
        }

        return tasks;
    }

    public static void main(String[] args) {
        TestGracefulDegradation loader = new TestGracefulDegradation();

        // 尝试加载不存在的文件
        List<String> tasks = loader.loadTasksFromFile("不存在的文件.txt");

        System.out.println("任务数量: " + tasks.size());
        System.out.println("程序正常结束");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestGracefulDegradation.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestGracefulDegradation", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "文件不存在，返回空列表" in stdout
            assert "任务数量: 0" in stdout
            assert "程序正常结束" in stdout


# ==================== 边界情况测试 ====================

class TestEdgeCases:
    """边界情况测试"""

    def test_catch_block_order_matters(self):
        """边界：catch 块顺序很重要，子类必须在父类之前"""
        # 这个测试验证编译错误，因为父类 catch 在子类之前
        java_code = '''
public class TestCatchOrder {
    public static void main(String[] args) {
        try {
            throw new IllegalArgumentException("测试");
        } catch (Exception e) {
            System.out.println("父类捕获");
        } catch (IllegalArgumentException e) {
            System.out.println("子类捕获");
        }
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestCatchOrder.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            # 应该编译失败，因为 IllegalArgumentException 已经被 Exception 捕获
            assert not success, "应该编译失败，因为 catch 块顺序错误"
            assert "已捕获" in error or "already caught" in error or "Unreachable" in error

    def test_empty_catch_block_is_bad_practice(self):
        """反例：空的 catch 块是坏习惯（吞异常）"""
        java_code = '''
public class TestEmptyCatch {
    public static void main(String[] args) {
        try {
            throw new RuntimeException("重要错误");
        } catch (RuntimeException e) {
            // 空的 catch 块 - 坏习惯！
        }

        System.out.println("程序继续执行，但错误被隐藏了");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestEmptyCatch.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestEmptyCatch", tmpdir)
            assert success, f"运行失败: {stderr}"
            # 程序正常结束，但错误被隐藏了
            assert "程序继续执行，但错误被隐藏了" in stdout
            assert "重要错误" not in stdout

    def test_rethrow_exception_preserves_stack_trace(self):
        """正例：重新抛出异常保留堆栈跟踪"""
        java_code = '''
public class TestRethrowException {
    public static void main(String[] args) {
        try {
            methodA();
        } catch (RuntimeException e) {
            System.out.println("最终捕获: " + e.getMessage());
            // 验证堆栈跟踪包含原始位置
            StackTraceElement[] stack = e.getStackTrace();
            System.out.println("异常起源于: " + stack[0].getMethodName());
        }
    }

    static void methodA() {
        try {
            methodB();
        } catch (RuntimeException e) {
            System.out.println("methodA 捕获并重新抛出");
            throw e;  // 重新抛出，保留原始堆栈
        }
    }

    static void methodB() {
        throw new RuntimeException("原始错误");
    }
}
'''
        with tempfile.TemporaryDirectory() as tmpdir:
            java_file = Path(tmpdir) / "TestRethrowException.java"
            java_file.write_text(java_code)

            success, error = compile_java(java_file)
            assert success, f"编译失败: {error}"

            success, stdout, stderr = run_java("TestRethrowException", tmpdir)
            assert success, f"运行失败: {stderr}"
            assert "methodA 捕获并重新抛出" in stdout
            assert "最终捕获: 原始错误" in stdout
            assert "异常起源于: methodB" in stdout


if __name__ == "__main__":
    pytest.main([__file__, "-v"])
