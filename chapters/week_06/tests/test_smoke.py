"""
Week 06 基线测试（Smoke Tests）

这些是基础的"冒烟测试"——确保最核心的异常处理概念能正常工作。
如果这些测试都失败，说明基础环境有问题，其他测试也不用跑了。

测试覆盖：
- 基本的异常类型（ValueError, TypeError, ZeroDivisionError）
- 基本的 try/except 结构
- 基本的异常捕获和处理
- 基本的 LBYL vs EAFP 风格
"""

import pytest


# ============================================================================
# 测试基本的异常类型
# ============================================================================

def test_value_error_is_raised():
    """基线测试：ValueError 会在值不合法时抛出"""
    with pytest.raises(ValueError):
        int("abc")


def test_zero_division_error_is_raised():
    """基线测试：ZeroDivisionError 会在除以零时抛出"""
    with pytest.raises(ZeroDivisionError):
        result = 10 / 0


def test_type_error_is_raised():
    """基线测试：TypeError 会在类型不匹配时抛出"""
    with pytest.raises(TypeError):
        result = "2" + 3


def test_key_error_is_raised():
    """基线测试：KeyError 会在访问不存在的键时抛出"""
    d = {"key": "value"}
    with pytest.raises(KeyError):
        value = d["missing_key"]


def test_index_error_is_raised():
    """基线测试：IndexError 会在索引越界时抛出"""
    lst = [1, 2, 3]
    with pytest.raises(IndexError):
        value = lst[10]


# ============================================================================
# 测试基本的 try/except 结构
# ============================================================================

def test_try_except_catches_exception():
    """基线测试：except 块能捕获异常"""
    exception_caught = False

    try:
        raise ValueError("测试异常")
    except ValueError:
        exception_caught = True

    assert exception_caught is True


def test_try_except_prevents_crash():
    """基线测试：try/except 能防止程序崩溃"""
    try:
        result = 10 / 0
    except ZeroDivisionError:
        result = "error"

    assert result == "error"


def test_exception_can_be_caught_multiple_times():
    """基线测试：可以多次捕获同一个异常"""
    catch_count = 0

    for i in range(3):
        try:
            raise ValueError("测试")
        except ValueError:
            catch_count += 1

    assert catch_count == 3


# ============================================================================
# 测试特定的异常捕获
# ============================================================================

def test_specific_exception_caught():
    """基线测试：能捕获特定类型的异常"""
    caught_value_error = False
    caught_type_error = False

    try:
        raise ValueError("测试")
    except ValueError:
        caught_value_error = True
    except TypeError:
        caught_type_error = True

    assert caught_value_error is True
    assert caught_type_error is False


def test_multiple_exception_types_one_except():
    """基线测试：一个 except 能捕获多种异常"""
    exception_caught = False

    try:
        raise ValueError("测试")
    except (ValueError, TypeError):
        exception_caught = True

    assert exception_caught is True


def test_bare_except_catches_all():
    """基线测试：裸的 except 能捕获所有异常（不推荐，但测试其行为）"""
    exception_caught = False

    try:
        raise ValueError("测试")
    except:
        exception_caught = True

    assert exception_caught is True


def test_exception_as_variable():
    """基线测试：异常可以赋值给变量"""
    exception_message = None

    try:
        raise ValueError("测试消息")
    except ValueError as e:
        exception_message = str(e)

    assert exception_message == "测试消息"


# ============================================================================
# 测试 else 和 finally
# ============================================================================

def test_else_executes_when_no_exception():
    """基线测试：else 块在无异常时执行"""
    else_executed = False

    try:
        result = 10 + 20
    except ValueError:
        pass
    else:
        else_executed = True

    assert else_executed is True


def test_else_not_executes_when_exception_raised():
    """基线测试：有异常时 else 块不执行"""
    else_executed = False

    try:
        raise ValueError("测试")
    except ValueError:
        pass
    else:
        else_executed = True

    assert else_executed is False


def test_finally_always_executes():
    """基线测试：finally 总是执行"""
    finally_executed = False

    try:
        raise ValueError("测试")
    except ValueError:
        pass
    finally:
        finally_executed = True

    assert finally_executed is True


def test_finally_executes_on_success():
    """基线测试：成功时 finally 也执行"""
    finally_executed = False

    try:
        result = 10 + 20
    finally:
        finally_executed = True

    assert finally_executed is True


# ============================================================================
# 测试 LBYL vs EAFP 风格
# ============================================================================

def test_lbyl_style_with_if():
    """基线测试：LBYL 风格用 if 检查"""
    d = {"key": "value"}

    # LBYL: 先检查
    if "key" in d:
        result = d["key"]
    else:
        result = None

    assert result == "value"


def test_eafp_style_with_try_except():
    """基线测试：EAFP 风格用 try/except"""
    d = {"key": "value"}

    # EAFP: 直接尝试
    try:
        result = d["key"]
    except KeyError:
        result = None

    assert result == "value"


def test_both_styles_handle_missing_key():
    """基线测试：两种风格都能处理键不存在"""
    d = {}

    # LBYL
    if "key" in d:
        lbyl_result = d["key"]
    else:
        lbyl_result = "default"

    # EAFP
    try:
        eafp_result = d["key"]
    except KeyError:
        eafp_result = "default"

    assert lbyl_result == eafp_result == "default"


# ============================================================================
# 测试 raise 语句
# ============================================================================

def test_raise_raises_exception():
    """基线测试：raise 能抛出异常"""
    with pytest.raises(ValueError):
        raise ValueError("测试异常")


def test_raise_with_message():
    """基线测试：raise 可以带消息"""
    exception_message = None

    try:
        raise ValueError("自定义消息")
    except ValueError as e:
        exception_message = str(e)

    assert exception_message == "自定义消息"


def test_reraise_exception():
    """基线测试：可以重新抛出异常"""
    with pytest.raises(ValueError):
        try:
            raise ValueError("原始异常")
        except ValueError:
            raise  # 重新抛出


# ============================================================================
# 测试异常不会执行 return 后的代码
# ============================================================================

def test_exception_skips_later_code():
    """基线测试：异常会跳过后面的代码"""
    code_after_exception = False

    try:
        raise ValueError("测试")
        code_after_exception = True  # 这行不会执行
    except ValueError:
        pass

    assert code_after_exception is False


def test_return_in_try_skips_finally_return():
    """基线测试：try 中的 return 不会跳过 finally"""
    def test_func():
        try:
            return "from_try"
        finally:
            return "from_finally"

    result = test_func()
    # finally 的 return 会覆盖 try 的 return
    assert result == "from_finally"


# ============================================================================
# 测试嵌套的异常处理
# ============================================================================

def test_nested_try_except():
    """基线测试：可以嵌套 try/except"""
    inner_caught = False
    outer_caught = False

    try:
        try:
            raise ValueError("内部异常")
        except ValueError:
            inner_caught = True
            raise TypeError("外部异常")
    except TypeError:
        outer_caught = True

    assert inner_caught is True
    assert outer_caught is True


# ============================================================================
# 测试输入验证的 LBYL 风格
# ============================================================================

def test_isdigit_for_positive_integer():
    """基线测试：isdigit 能检查正整数字符串"""
    assert "10".isdigit() is True
    assert "abc".isdigit() is False
    assert "-5".isdigit() is False
    assert "0".isdigit() is True


def test_string_strip_for_whitespace():
    """基线测试：strip 能移除空格"""
    assert "  hello  ".strip() == "hello"
    assert "   ".strip() == ""
    assert "no-space".strip() == "no-space"


def test_in_operator_for_dict():
    """基线测试：in 能检查键是否存在"""
    d = {"key": "value"}
    assert ("key" in d) is True
    assert ("missing" in d) is False


def test_in_operator_for_list():
    """基线测试：in 能检查元素是否存在"""
    lst = [1, 2, 3]
    assert (2 in lst) is True
    assert (5 in lst) is False


# ============================================================================
# 测试基本的文件异常
# ============================================================================

def test_file_not_found_error_on_missing_file(tmp_path):
    """基线测试：读取不存在的文件抛出 FileNotFoundError"""
    with pytest.raises(FileNotFoundError):
        with open(tmp_path / "不存在的文件.txt", "r") as f:
            content = f.read()


def test_file_can_be_created_and_read(tmp_path):
    """基线测试：文件可以创建和读取"""
    filepath = tmp_path / "test.txt"

    # 写入
    with open(filepath, "w", encoding="utf-8") as f:
        f.write("Hello")

    # 读取
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()

    assert content == "Hello"


# ============================================================================
# 测试异常处理的最佳实践
# ============================================================================

def test_specific_exception_is_better_than_bare_except():
    """基线测试：捕获特定异常比裸 except 更好"""
    # 这个测试只是说明概念，实际代码中应该捕获特定异常

    # 好的做法
    try:
        result = int("abc")
    except ValueError:
        result = "error"

    # 不好的做法（虽然能工作）
    try:
        result = int("abc")
    except:
        result = "error"

    assert result == "error"


def test_exception_message_should_be_helpful():
    """基线测试：异常消息应该有帮助"""
    try:
        raise ValueError("年龄必须大于 0")
    except ValueError as e:
        message = str(e)

    # 好的消息应该说明问题
    assert "年龄" in message
    assert "大于 0" in message


# ============================================================================
# 测试自定义异常的基本概念
# ============================================================================

def test_custom_exception_can_be_defined():
    """基线测试：可以定义自定义异常"""
    class MyError(Exception):
        pass

    def raise_my_error():
        raise MyError("自定义错误")

    with pytest.raises(MyError):
        raise_my_error()


def test_custom_exception_can_inherit_from_exception():
    """基线测试：自定义异常可以继承 Exception"""
    class MyError(Exception):
        pass

    assert issubclass(MyError, Exception)


def test_custom_exception_can_inherit_from_value_error():
    """基线测试：自定义异常可以继承 ValueError"""
    class MyError(ValueError):
        pass

    assert issubclass(MyError, ValueError)

    # 捕获 ValueError 也能捕获 MyError
    try:
        raise MyError("测试")
    except ValueError:
        caught = True

    assert caught is True
