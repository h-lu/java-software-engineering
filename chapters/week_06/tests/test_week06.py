"""
Week 06 综合测试：异常处理与防御性编程

测试覆盖：
1. 异常类型识别（ValueError, TypeError, ZeroDivisionError, KeyError, IndexError, FileNotFoundError）
2. try/except/else/finally 结构
3. LBYL vs EAFP 两种编程风格
4. 输入校验函数
5. 自定义异常
6. 带重试机制的输入函数
"""

import pytest
import sys
import os

# 添加 starter_code 到路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'starter_code'))

from solution import (
    safe_divide,
    get_dictionary_value,
    get_list_item,
    is_positive_integer,
    is_valid_age,
    is_valid_date_format,
    get_positive_integer_with_retry,
    get_choice_with_retry,
    safe_read_file,
    safe_write_file,
    InvalidInputError,
    OutOfRangeError,
    validate_age,
)


# ============================================================================
# 测试异常类型识别
# ============================================================================

class TestExceptionTypes:
    """测试各种异常类型的识别和处理"""

    def test_value_error_on_invalid_conversion(self):
        """测试：ValueError 在类型转换失败时抛出"""
        with pytest.raises(ValueError):
            int("abc")

    def test_type_error_on_mismatched_operation(self):
        """测试：TypeError 在类型不匹配时抛出"""
        with pytest.raises(TypeError):
            result = "2" + 3  # 字符串不能直接加数字

    def test_zero_division_error(self):
        """测试：ZeroDivisionError 在除以零时抛出"""
        with pytest.raises(ZeroDivisionError):
            result = 10 / 0

    def test_key_error_on_missing_dict_key(self):
        """测试：KeyError 在访问不存在的键时抛出"""
        scores = {"小北": 85, "阿码": 90}
        with pytest.raises(KeyError):
            value = scores["老潘"]

    def test_index_error_on_out_of_range(self):
        """测试：IndexError 在索引越界时抛出"""
        numbers = [1, 2, 3]
        with pytest.raises(IndexError):
            value = numbers[10]

    def test_file_not_found_error(self):
        """测试：FileNotFoundError 在文件不存在时抛出"""
        with pytest.raises(FileNotFoundError):
            with open("不存在的文件.txt", "r") as f:
                content = f.read()


# ============================================================================
# 测试 safe_divide 函数
# ============================================================================

class TestSafeDivide:
    """测试安全除法函数"""

    # 正例
    def test_safe_divide_normal_case(self):
        """测试：正常除法运算"""
        result = safe_divide(10, 2)
        assert result == 5.0

    def test_safe_divide_negative_numbers(self):
        """测试：负数除法"""
        result = safe_divide(-10, 2)
        assert result == -5.0

        result = safe_divide(10, -2)
        assert result == -5.0

    def test_safe_divide_float_numbers(self):
        """测试：浮点数除法"""
        result = safe_divide(7.5, 2.5)
        assert result == 3.0

    def test_safe_divide_result_is_float(self):
        """测试：除法结果总是浮点数"""
        result = safe_divide(10, 5)
        assert isinstance(result, float)
        assert result == 2.0

    # 边界
    def test_safe_divide_by_negative_one(self):
        """测试：除以 -1"""
        result = safe_divide(10, -1)
        assert result == -10.0

    def test_safe_divide_zero_divided_by_nonzero(self):
        """测试：0 除以非零数"""
        result = safe_divide(0, 5)
        assert result == 0.0

    def test_safe_divide_very_large_numbers(self):
        """测试：非常大的数字"""
        result = safe_divide(1e10, 10)
        assert result == 1e9

    def test_safe_divide_very_small_numbers(self):
        """测试：非常小的数字"""
        result = safe_divide(1e-10, 10)
        assert abs(result - 1e-11) < 1e-15  # 浮点数精度容忍

    # 反例
    def test_safe_divide_by_zero_returns_none(self):
        """测试：除以零返回 None"""
        result = safe_divide(10, 0)
        assert result is None

    def test_safe_divide_zero_by_zero_returns_none(self):
        """测试：0 除以 0 返回 None"""
        result = safe_divide(0, 0)
        assert result is None

    def test_safe_divide_string_input_raises_value_error(self):
        """测试：字符串输入抛出 ValueError"""
        with pytest.raises(ValueError, match="输入必须是数字"):
            safe_divide("abc", 2)

    def test_safe_divide_none_input_raises_value_error(self):
        """测试：None 输入抛出 ValueError"""
        with pytest.raises(ValueError, match="输入必须是数字"):
            safe_divide(None, 2)


# ============================================================================
# 测试字典访问（EAFP 风格）
# ============================================================================

class TestDictionaryAccessEAFP:
    """测试字典访问的 EAFP 风格"""

    # 正例
    def test_get_dictionary_value_existing_key(self):
        """测试：获取存在的键"""
        scores = {"小北": 85, "阿码": 90}
        result = get_dictionary_value(scores, "小北")
        assert result == 85

    def test_get_dictionary_value_with_default(self):
        """测试：键不存在时返回默认值"""
        scores = {"小北": 85, "阿码": 90}
        result = get_dictionary_value(scores, "老潘", default=0)
        assert result == 0

    # 边界
    def test_get_dictionary_value_empty_dict(self):
        """测试：空字典"""
        empty_dict = {}
        result = get_dictionary_value(empty_dict, "key", default="default")
        assert result == "default"

    def test_get_dictionary_value_none_default(self):
        """测试：默认值为 None"""
        scores = {"小北": 85}
        result = get_dictionary_value(scores, "老潘")
        assert result is None

    def test_get_dictionary_value_various_types(self):
        """测试：字典包含各种类型的值"""
        mixed_dict = {
            "int": 42,
            "float": 3.14,
            "str": "hello",
            "list": [1, 2, 3],
            "dict": {"nested": True},
            "none": None,
        }

        assert get_dictionary_value(mixed_dict, "int") == 42
        assert get_dictionary_value(mixed_dict, "float") == 3.14
        assert get_dictionary_value(mixed_dict, "str") == "hello"
        assert get_dictionary_value(mixed_dict, "list") == [1, 2, 3]
        assert get_dictionary_value(mixed_dict, "dict") == {"nested": True}
        assert get_dictionary_value(mixed_dict, "none") is None


# ============================================================================
# 测试列表访问（EAFP 风格）
# ============================================================================

class TestListAccessEAFP:
    """测试列表访问的 EAFP 风格"""

    # 正例
    def test_get_list_item_valid_index(self):
        """测试：获取有效索引的元素"""
        numbers = [10, 20, 30, 40, 50]
        result = get_list_item(numbers, 2)
        assert result == 30

    def test_get_list_item_first_element(self):
        """测试：获取第一个元素"""
        numbers = [10, 20, 30]
        result = get_list_item(numbers, 0)
        assert result == 10

    def test_get_list_item_last_element(self):
        """测试：获取最后一个元素"""
        numbers = [10, 20, 30]
        result = get_list_item(numbers, 2)
        assert result == 30

    # 边界
    def test_get_list_item_negative_index(self):
        """测试：负数索引"""
        numbers = [10, 20, 30]
        result = get_list_item(numbers, -1)
        assert result == 30

        result = get_list_item(numbers, -2)
        assert result == 20

    def test_get_list_item_empty_list(self):
        """测试：空列表"""
        empty_list = []
        result = get_list_item(empty_list, 0, default="empty")
        assert result == "empty"

    # 反例
    def test_get_list_item_out_of_range_positive(self):
        """测试：正数索引越界"""
        numbers = [10, 20, 30]
        result = get_list_item(numbers, 10, default=None)
        assert result is None

    def test_get_list_item_out_of_range_negative(self):
        """测试：负数索引越界"""
        numbers = [10, 20, 30]
        result = get_list_item(numbers, -10, default=None)
        assert result is None

    def test_get_list_item_various_types(self):
        """测试：列表包含各种类型的元素"""
        mixed_list = [42, 3.14, "hello", [1, 2], {"key": "value"}, None, True]

        assert get_list_item(mixed_list, 0) == 42
        assert get_list_item(mixed_list, 1) == 3.14
        assert get_list_item(mixed_list, 2) == "hello"
        assert get_list_item(mixed_list, 3) == [1, 2]
        assert get_list_item(mixed_list, 4) == {"key": "value"}
        assert get_list_item(mixed_list, 5) is None
        assert get_list_item(mixed_list, 6) is True


# ============================================================================
# 测试输入校验函数（LBYL 风格）
# ============================================================================

class TestInputValidationLBYL:
    """测试输入校验的 LBYL 风格"""

    # 正例 - is_positive_integer
    def test_is_positive_integer_valid_numbers(self):
        """测试：有效的正整数字符串"""
        assert is_positive_integer("1") is True
        assert is_positive_integer("10") is True
        assert is_positive_integer("100") is True
        assert is_positive_integer("99999") is True

    # 反例 - is_positive_integer
    def test_is_positive_integer_negative_numbers(self):
        """测试：负数字符串"""
        assert is_positive_integer("-1") is False
        assert is_positive_integer("-100") is False

    def test_is_positive_integer_zero(self):
        """测试：零"""
        assert is_positive_integer("0") is False

    def test_is_positive_integer_non_digit_strings(self):
        """测试：非数字字符串"""
        assert is_positive_integer("abc") is False
        assert is_positive_integer("12.5") is False
        assert is_positive_integer("1a2b") is False
        assert is_positive_integer("") is False
        assert is_positive_integer(" ") is False

    def test_is_positive_integer_non_string_input(self):
        """测试：非字符串输入"""
        assert is_positive_integer(123) is False
        assert is_positive_integer(12.5) is False
        assert is_positive_integer(None) is False
        assert is_positive_integer([]) is False
        assert is_positive_integer({}) is False

    # 正例 - is_valid_age
    def test_is_valid_age_valid_ages(self):
        """测试：有效年龄"""
        assert is_valid_age("18") is True
        assert is_valid_age("25") is True
        assert is_valid_age("65") is True
        assert is_valid_age("100") is True

    def test_is_valid_age_with_custom_range(self):
        """测试：自定义年龄范围"""
        assert is_valid_age("10", min_age=5, max_age=15) is True
        assert is_valid_age("5", min_age=5, max_age=15) is True
        assert is_valid_age("15", min_age=5, max_age=15) is True

    # 边界 - is_valid_age
    def test_is_valid_age_boundary_values(self):
        """测试：边界值"""
        assert is_valid_age("0", min_age=0, max_age=120) is True
        assert is_valid_age("120", min_age=0, max_age=120) is True
        assert is_valid_age("18", min_age=18, max_age=120) is True
        assert is_valid_age("120", min_age=18, max_age=120) is True

    # 反例 - is_valid_age
    def test_is_valid_age_out_of_range(self):
        """测试：超出范围"""
        assert is_valid_age("17", min_age=18, max_age=120) is False
        assert is_valid_age("121", min_age=18, max_age=120) is False
        assert is_valid_age("-1", min_age=0, max_age=120) is False
        assert is_valid_age("150", min_age=0, max_age=120) is False

    def test_is_valid_age_invalid_format(self):
        """测试：无效格式"""
        assert is_valid_age("abc") is False
        assert is_valid_age("12.5") is False
        assert is_valid_age("") is False
        assert is_valid_age("20 ") is False

    # 正例 - is_valid_date_format
    def test_is_valid_date_format_valid_dates(self):
        """测试：有效的日期格式"""
        assert is_valid_date_format("01-01") is True
        assert is_valid_date_format("12-31") is True
        assert is_valid_date_format("02-09") is True
        assert is_valid_date_format("06-15") is True

    # 反例 - is_valid_date_format
    def test_is_valid_date_format_invalid_formats(self):
        """测试：无效的日期格式"""
        assert is_valid_date_format("1-1") is False  # 单数字
        assert is_valid_date_format("123-456") is False  # 三位数字
        assert is_valid_date_format("01/01") is False  # 用斜杠
        assert is_valid_date_format("0101") is False  # 无分隔符
        assert is_valid_date_format("") is False  # 空字符串
        assert is_valid_date_format("ab-cd") is False  # 非数字

    def test_is_valid_date_format_non_string(self):
        """测试：非字符串输入"""
        assert is_valid_date_format(123) is False
        assert is_valid_date_format(None) is False
        assert is_valid_date_format([]) is False


# ============================================================================
# 测试 try/except/else/finally 结构
# ============================================================================

class TestTryExceptElseFinally:
    """测试完整的异常处理结构"""

    def test_try_except_catches_exception(self):
        """测试：except 块能捕获异常"""
        exception_caught = False

        try:
            raise ValueError("测试异常")
        except ValueError:
            exception_caught = True

        assert exception_caught is True

    def test_else_executes_when_no_exception(self):
        """测试：else 块在无异常时执行"""
        else_executed = False

        try:
            result = 10 + 20
        except ValueError:
            pass
        else:
            else_executed = True

        assert else_executed is True

    def test_else_not_executed_when_exception_raised(self):
        """测试：有异常时 else 块不执行"""
        else_executed = False

        try:
            raise ValueError("测试异常")
        except ValueError:
            pass
        else:
            else_executed = True

        assert else_executed is False

    def test_finally_always_executes_on_success(self):
        """测试：成功时 finally 总是执行"""
        finally_executed = False

        try:
            result = 10 + 20
        finally:
            finally_executed = True

        assert finally_executed is True

    def test_finally_always_executes_on_exception(self):
        """测试：异常时 finally 总是执行"""
        finally_executed = False

        try:
            raise ValueError("测试异常")
        except ValueError:
            pass
        finally:
            finally_executed = True

        assert finally_executed is True

    def test_finally_executes_even_with_return_in_try(self):
        """测试：即使 try 中有 return，finally 也会执行"""
        finally_executed = False

        def test_func():
            try:
                return "try_return"
            finally:
                nonlocal finally_executed
                finally_executed = True

        result = test_func()
        assert result == "try_return"
        assert finally_executed is True

    def test_multiple_except_blocks(self):
        """测试：多个 except 块"""
        caught_value_error = False
        caught_type_error = False

        try:
            # 可以改变这里来测试不同的异常
            raise ValueError("测试")
        except ValueError:
            caught_value_error = True
        except TypeError:
            caught_type_error = True

        assert caught_value_error is True
        assert caught_type_error is False

    def test_catching_multiple_exception_types(self):
        """测试：捕获多种异常类型"""
        exception_caught = False

        try:
            raise ValueError("测试异常")
        except (ValueError, TypeError):
            exception_caught = True

        assert exception_caught is True

    def test_exception_as_variable(self):
        """测试：将异常赋值给变量"""
        exception_message = None

        try:
            raise ValueError("自定义错误消息")
        except ValueError as e:
            exception_message = str(e)

        assert exception_message == "自定义错误消息"


# ============================================================================
# 测试自定义异常
# ============================================================================

class TestCustomExceptions:
    """测试自定义异常类"""

    def test_invalid_input_error_is_value_error_subclass(self):
        """测试：InvalidInputError 是 ValueError 的子类"""
        assert issubclass(InvalidInputError, ValueError)

    def test_invalid_input_error_can_be_raised(self):
        """测试：可以抛出 InvalidInputError"""
        with pytest.raises(InvalidInputError):
            raise InvalidInputError("无效输入")

    def test_out_of_range_error_is_value_error_subclass(self):
        """测试：OutOfRangeError 是 ValueError 的子类"""
        assert issubclass(OutOfRangeError, ValueError)

    def test_out_of_range_error_can_be_raised(self):
        """测试：可以抛出 OutOfRangeError"""
        with pytest.raises(OutOfRangeError):
            raise OutOfRangeError("超出范围")

    def test_validate_age_valid_age(self):
        """测试：验证有效年龄"""
        result = validate_age(25, min_age=18, max_age=120)
        assert result == 25

    def test_validate_age_boundary_values(self):
        """测试：边界值年龄"""
        result = validate_age(18, min_age=18, max_age=120)
        assert result == 18

        result = validate_age(120, min_age=18, max_age=120)
        assert result == 120

    def test_validate_age_too_young_raises_out_of_range_error(self):
        """测试：年龄太小抛出 OutOfRangeError"""
        with pytest.raises(OutOfRangeError, match="年龄必须大于等于 18"):
            validate_age(17, min_age=18, max_age=120)

    def test_validate_age_too_old_raises_out_of_range_error(self):
        """测试：年龄太大抛出 OutOfRangeError"""
        with pytest.raises(OutOfRangeError, match="年龄必须小于等于 120"):
            validate_age(121, min_age=18, max_age=120)

    def test_validate_age_non_number_raises_invalid_input_error(self):
        """测试：非数字年龄抛出 InvalidInputError"""
        with pytest.raises(InvalidInputError, match="年龄必须是数字"):
            validate_age("twenty-five", min_age=18, max_age=120)

    def test_validate_age_float_gets_converted_to_int(self):
        """测试：浮点数年龄被转换为整数"""
        result = validate_age(25.7, min_age=18, max_age=120)
        assert result == 25
        assert isinstance(result, int)


# ============================================================================
# 测试文件操作
# ============================================================================

class TestFileOperations:
    """测试文件操作和异常处理"""

    def test_safe_write_file_creates_file(self, tmp_path):
        """测试：safe_write_file 能创建文件"""
        filepath = tmp_path / "test.txt"
        content = "Hello, World!"

        result = safe_write_file(str(filepath), content)
        assert result is True

        # 验证文件已创建
        assert filepath.exists()
        assert filepath.read_text(encoding="utf-8") == content

    def test_safe_read_file_reads_content(self, tmp_path):
        """测试：safe_read_file 能读取文件"""
        filepath = tmp_path / "test.txt"
        content = "Hello, World!"
        filepath.write_text(content, encoding="utf-8")

        result = safe_read_file(str(filepath))
        assert result == content

    def test_safe_read_file_not_found_raises_exception(self, tmp_path):
        """测试：读取不存在的文件抛出 FileNotFoundError"""
        filepath = tmp_path / "不存在的文件.txt"

        with pytest.raises(FileNotFoundError):
            safe_read_file(str(filepath))

    def test_safe_write_and_read_cycle(self, tmp_path):
        """测试：写入后读取的一致性"""
        filepath = tmp_path / "cycle.txt"
        original_content = "这是测试内容\n第二行"

        # 写入
        assert safe_write_file(str(filepath), original_content) is True

        # 读取
        read_content = safe_read_file(str(filepath))
        assert read_content == original_content

    def test_safe_write_file_with_unicode(self, tmp_path):
        """测试：写入 Unicode 内容"""
        filepath = tmp_path / "unicode.txt"
        content = "Hello 世界! 🌍 测试中文"

        assert safe_write_file(str(filepath), content) is True
        assert filepath.read_text(encoding="utf-8") == content

    def test_safe_write_empty_file(self, tmp_path):
        """测试：写入空文件"""
        filepath = tmp_path / "empty.txt"

        assert safe_write_file(str(filepath), "") is True
        assert safe_read_file(str(filepath)) == ""

    def test_safe_read_file_with_multiline_content(self, tmp_path):
        """测试：读取多行内容"""
        filepath = tmp_path / "multiline.txt"
        content = "第一行\n第二行\n第三行"

        filepath.write_text(content, encoding="utf-8")
        result = safe_read_file(str(filepath))

        assert result == content


# ============================================================================
# 测试 LBYL vs EAFP 风格对比
# ============================================================================

class TestLBYLvsEAFP:
    """测试 LBYL 和 EAFP 两种风格的差异"""

    def test_lbyl_style_for_dict_access(self):
        """测试：LBYL 风格的字典访问"""
        scores = {"小北": 85, "阿码": 90}

        # LBYL: 先检查，再访问
        if "小北" in scores:
            result = scores["小北"]
        else:
            result = None

        assert result == 85

    def test_eafp_style_for_dict_access(self):
        """测试：EAFP 风格的字典访问"""
        scores = {"小北": 85, "阿码": 90}

        # EAFP: 直接尝试，失败时处理
        try:
            result = scores["小北"]
        except KeyError:
            result = None

        assert result == 85

    def test_both_styles_handle_missing_key(self):
        """测试：两种风格都能处理键不存在"""
        scores = {"小北": 85}

        # LBYL
        if "老潘" in scores:
            lbyl_result = scores["老潘"]
        else:
            lbyl_result = "default"

        # EAFP
        try:
            eafp_result = scores["老潘"]
        except KeyError:
            eafp_result = "default"

        assert lbyl_result == eafp_result == "default"

    def test_lbyl_style_for_input_validation(self):
        """测试：LBYL 风格的输入验证"""
        age_str = "25"

        # LBYL: 先检查是否合法
        if age_str.isdigit() and 0 < int(age_str) < 120:
            is_valid = True
        else:
            is_valid = False

        assert is_valid is True

    def test_lbyl_rejects_invalid_input(self):
        """测试：LBYL 拒绝无效输入"""
        age_str = "abc"

        # LBYL: 先检查
        if age_str.isdigit() and 0 < int(age_str) < 120:
            is_valid = True
        else:
            is_valid = False

        assert is_valid is False


# ============================================================================
# 测试带重试机制的函数
# ============================================================================

class TestRetryMechanisms:
    """测试带重试机制的输入函数"""

    def test_get_positive_integer_with_retry_valid_input(self, monkeypatch):
        """测试：第一次输入就正确"""
        # 模拟用户输入
        monkeypatch.setattr("builtins.input", lambda x: "10")

        result = get_positive_integer_with_retry("请输入数字：", max_attempts=3)
        assert result == 10

    def test_get_positive_integer_with_retry_invalid_then_valid(self, monkeypatch):
        """测试：先输入错误，再输入正确"""
        inputs = iter(["abc", "xyz", "10"])
        monkeypatch.setattr("builtins.input", lambda x: next(inputs))

        result = get_positive_integer_with_retry("请输入数字：", max_attempts=3)
        assert result == 10

    def test_get_positive_integer_with_retry_exceeds_attempts(self, monkeypatch):
        """测试：超过最大尝试次数"""
        inputs = iter(["abc", "xyz", "invalid"])
        monkeypatch.setattr("builtins.input", lambda x: next(inputs))

        with pytest.raises(ValueError, match="输入错误次数过多"):
            get_positive_integer_with_retry("请输入数字：", max_attempts=3)

    def test_get_choice_with_retry_valid_choice(self, monkeypatch):
        """测试：第一次选择就正确"""
        monkeypatch.setattr("builtins.input", lambda x: "A")

        result = get_choice_with_retry("请选择：", ["A", "B", "C"], max_attempts=3)
        assert result == "A"

    def test_get_choice_with_retry_invalid_then_valid(self, monkeypatch):
        """测试：先选择错误，再选择正确"""
        inputs = iter(["X", "Y", "B"])
        monkeypatch.setattr("builtins.input", lambda x: next(inputs))

        result = get_choice_with_retry("请选择：", ["A", "B", "C"], max_attempts=3)
        assert result == "B"

    def test_get_choice_with_retry_exceeds_attempts(self, monkeypatch):
        """测试：超过最大尝试次数"""
        inputs = iter(["X", "Y", "Z"])
        monkeypatch.setattr("builtins.input", lambda x: next(inputs))

        with pytest.raises(ValueError, match="输入错误次数过多"):
            get_choice_with_retry("请选择：", ["A", "B", "C"], max_attempts=3)


# ============================================================================
# 测试边界情况和特殊情况
# ============================================================================

class TestEdgeCases:
    """测试边界情况和特殊情况"""

    def test_safe_divide_with_float_division(self):
        """测试：浮点数除法的精度"""
        result = safe_divide(1, 3)
        assert abs(result - 0.3333333333333333) < 1e-10

    def test_safe_divide_very_small_divisor(self):
        """测试：非常小的除数"""
        result = safe_divide(1, 0.0001)
        assert abs(result - 10000) < 0.1

    def test_dictionary_with_none_value(self):
        """测试：字典值为 None"""
        d = {"key": None}
        result = get_dictionary_value(d, "key", default="default")
        assert result is None

    def test_list_with_none_element(self):
        """测试：列表元素为 None"""
        lst = [1, None, 3]
        result = get_list_item(lst, 1, default="default")
        assert result is None

    def test_is_positive_integer_with_whitespace(self):
        """测试：包含空格的字符串"""
        assert is_positive_integer(" 10") is False
        assert is_positive_integer("10 ") is False
        assert is_positive_integer(" 10 ") is False

    def test_is_positive_integer_with_leading_zeros(self):
        """测试：前导零"""
        assert is_positive_integer("01") is True
        assert is_positive_integer("001") is True
        assert is_positive_integer("000") is False  # 转换为 0，不大于 0

    def test_is_valid_age_with_leading_zeros(self):
        """测试：年龄的前导零"""
        assert is_valid_age("01", min_age=0, max_age=120) is True
        assert is_valid_age("001", min_age=0, max_age=120) is True

    def test_empty_string_input_validation(self):
        """测试：空字符串的输入验证"""
        assert is_positive_integer("") is False
        assert is_valid_age("") is False
        assert is_valid_date_format("") is False

    def test_unicode_string_input(self):
        """测试：Unicode 字符串输入"""
        assert is_positive_integer("一二三") is False
        assert is_valid_age("二十五") is False


# ============================================================================
# 测试异常的传播和重新抛出
# ============================================================================

class TestExceptionPropagation:
    """测试异常的传播和重新抛出"""

    def test_exception_propagates_up(self):
        """测试：异常向上传播"""
        def inner_function():
            raise ValueError("内部错误")

        def outer_function():
            inner_function()

        with pytest.raises(ValueError, match="内部错误"):
            outer_function()

    def test_catch_and_reraise(self):
        """测试：捕获后重新抛出"""
        def function():
            try:
                raise ValueError("原始错误")
            except ValueError as e:
                # 可以添加额外处理
                raise  # 重新抛出

        with pytest.raises(ValueError, match="原始错误"):
            function()

    def test_exception_chaining(self):
        """测试：异常链"""
        def function():
            try:
                raise ValueError("第一个错误")
            except ValueError:
                raise TypeError("第二个错误") from None

        with pytest.raises(TypeError, match="第二个错误"):
            function()


# ============================================================================
# 性能和效率测试
# ============================================================================

class TestPerformance:
    """测试性能相关的考虑"""

    def test_lbyl_checks_twice(self):
        """测试：LBYL 可能需要检查两次"""
        import time

        d = {"key": "value"}

        # LBYL: 检查 + 访问 = 两次操作
        start = time.perf_counter()
        for _ in range(10000):
            if "key" in d:
                _ = d["key"]
        lbyl_time = time.perf_counter() - start

        # EAFP: 直接尝试 = 一次操作（成功时）
        start = time.perf_counter()
        for _ in range(10000):
            try:
                _ = d["key"]
            except KeyError:
                pass
        eafp_time = time.perf_counter() - start

        # EAFP 在键存在时应该更快（但不强求，因为差异可能很小）
        # 这个测试主要是说明概念，不作为严格断言
        print(f"\nLBYL 时间: {lbyl_time:.6f}s")
        print(f"EAFP 时间: {eafp_time:.6f}s")

    def test_exception_overhead(self):
        """测试：异常的开销"""
        import time

        # 无异常的情况
        start = time.perf_counter()
        for _ in range(1000):
            try:
                result = 10 / 2
            except ZeroDivisionError:
                pass
        no_exception_time = time.perf_counter() - start

        # 有异常的情况
        start = time.perf_counter()
        for _ in range(1000):
            try:
                result = 10 / 0
            except ZeroDivisionError:
                pass
        with_exception_time = time.perf_counter() - start

        # 异常的开销应该更大
        assert with_exception_time > no_exception_time
        print(f"\n无异常时间: {no_exception_time:.6f}s")
        print(f"有异常时间: {with_exception_time:.6f}s")
