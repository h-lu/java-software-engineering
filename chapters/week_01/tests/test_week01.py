"""Week 01 测试用例

测试范围：
1. print() 函数输出
2. 变量赋值与复用
3. f-string 字符串格式化
4. 错误类型识别（SyntaxError, NameError, TypeError）
"""

import pytest


# ============================================================================
# 正例测试（Happy Path）
# ============================================================================

class TestPrintOutput:
    """测试 print() 函数输出"""

    def test_print_hello_world(self, capsys):
        """测试基本的 print 输出"""
        print("Hello, World!")
        captured = capsys.readouterr()
        assert captured.out.strip() == "Hello, World!"

    def test_print_multiple_args(self, capsys):
        """测试 print 多参数输出，默认用空格分隔"""
        print("姓名：", "张三")
        captured = capsys.readouterr()
        assert captured.out.strip() == "姓名： 张三"

    def test_print_number(self, capsys):
        """测试 print 输出数字"""
        print("年龄：", 25)
        captured = capsys.readouterr()
        assert captured.out.strip() == "年龄： 25"

    def test_print_end_parameter(self, capsys):
        """测试 print 的 end 参数控制换行"""
        print("加载中", end="")
        print("...", end="")
        print("完成！")
        captured = capsys.readouterr()
        assert captured.out == "加载中...完成！\n"


class TestVariableAssignment:
    """测试变量赋值和复用"""

    def test_basic_variable_assignment(self, capsys):
        """测试基本变量赋值和输出"""
        name = "李小明"
        job = "Python 学习者"
        print(name, job)
        captured = capsys.readouterr()
        assert captured.out.strip() == "李小明 Python 学习者"

    def test_variable_reuse(self, capsys):
        """测试变量复用 - 同一变量多次使用"""
        name = "张三"
        print(f"姓名：{name}")
        print(f"欢迎，{name}！")
        captured = capsys.readouterr()
        lines = captured.out.strip().split('\n')
        assert lines[0] == "姓名：张三"
        assert lines[1] == "欢迎，张三！"

    def test_variable_update(self, capsys):
        """测试变量更新值"""
        count = 1
        print(count)
        count = 2
        print(count)
        captured = capsys.readouterr()
        lines = captured.out.strip().split('\n')
        assert lines[0] == "1"
        assert lines[1] == "2"


class TestFstringBasic:
    """测试 f-string 基本用法"""

    def test_fstring_simple_variable(self, capsys):
        """测试 f-string 插入单个变量"""
        name = "李小明"
        print(f"你好，{name}！")
        captured = capsys.readouterr()
        assert captured.out.strip() == "你好，李小明！"

    def test_fstring_multiple_variables(self, capsys):
        """测试 f-string 插入多个变量"""
        name = "李小明"
        job = "Python 学习者"
        print(f"姓名：{name}，职业：{job}")
        captured = capsys.readouterr()
        assert captured.out.strip() == "姓名：李小明，职业：Python 学习者"

    def test_fstring_expression(self, capsys):
        """测试 f-string 中嵌入表达式"""
        x = 10
        y = 20
        print(f"{x} + {y} = {x + y}")
        captured = capsys.readouterr()
        assert captured.out.strip() == "10 + 20 = 30"


class TestFstringFormatting:
    """测试 f-string 格式说明符"""

    def test_fstring_float_precision(self, capsys):
        """测试 f-string 控制小数位数"""
        pi = 3.1415926
        print(f"圆周率约等于 {pi:.2f}")
        captured = capsys.readouterr()
        assert captured.out.strip() == "圆周率约等于 3.14"

    def test_fstring_center_align(self, capsys):
        """测试 f-string 居中对齐"""
        title = "名片"
        result = f"{title:^10}"
        # Python 的 f-string 对齐是基于字符数，不是显示宽度
        # "名片"是 2 个字符，居中对齐在 10 字符宽度中，左右各填充 4 个空格
        assert result == "    名片    "
        assert len(result) == 10  # 4 空格 + 2 字符 + 4 空格 = 10 字符

    def test_fstring_left_align(self, capsys):
        """测试 f-string 左对齐"""
        name = "张三"
        result = f"{name:<10}"
        assert result == "张三        "

    def test_fstring_repeat_character(self, capsys):
        """测试 f-string 中字符重复"""
        width = 20
        border = f"{'=' * width}"
        assert border == "===================="


# ============================================================================
# 边界测试（Edge Cases）
# ============================================================================

class TestVariableCaseSensitivity:
    """测试变量名大小写敏感"""

    def test_variable_case_sensitive(self, capsys):
        """测试 name 和 Name 是不同的变量"""
        name = "小写"
        Name = "大写"
        print(name, Name)
        captured = capsys.readouterr()
        assert captured.out.strip() == "小写 大写"

    def test_different_case_variables_independent(self):
        """测试不同大小写的变量相互独立"""
        user_name = "张三"
        User_Name = "李四"
        USER_NAME = "王五"
        assert user_name == "张三"
        assert User_Name == "李四"
        assert USER_NAME == "王五"


class TestFstringEmptyString:
    """测试 f-string 空字符串处理"""

    def test_fstring_empty_variable(self, capsys):
        """测试 f-string 处理空字符串变量"""
        empty = ""
        print(f"[{empty}]")
        captured = capsys.readouterr()
        assert captured.out.strip() == "[]"

    def test_fstring_whitespace_only(self, capsys):
        """测试 f-string 处理仅包含空白的字符串"""
        spaces = "   "
        print(f"[{spaces}]")
        captured = capsys.readouterr()
        assert captured.out.strip() == "[   ]"


class TestStringConcatenation:
    """测试字符串拼接"""

    def test_string_concatenation_plus(self, capsys):
        """测试使用 + 拼接字符串"""
        greeting = "你好，" + "世界"
        print(greeting)
        captured = capsys.readouterr()
        assert captured.out.strip() == "你好，世界"

    def test_string_concatenation_multiple(self, capsys):
        """测试多个字符串拼接"""
        result = "Py" + "thon" + " " + "3"
        print(result)
        captured = capsys.readouterr()
        assert captured.out.strip() == "Python 3"


# ============================================================================
# 反例测试（Error Cases）
# ============================================================================

class TestNameErrorRaised:
    """测试 NameError 异常"""

    def test_name_error_undefined_variable(self):
        """测试使用未定义变量会报 NameError"""
        with pytest.raises(NameError) as exc_info:
            print(undefined_variable_xyz)  # noqa: F821
        assert "undefined_variable_xyz" in str(exc_info.value)

    def test_name_error_wrong_case(self):
        """测试变量名大小写错误会报 NameError"""
        name = "李小明"
        with pytest.raises(NameError) as exc_info:
            print(Name)  # noqa: F821
        assert "Name" in str(exc_info.value)

    def test_name_error_use_before_define(self):
        """测试先使用后定义会报 NameError"""
        with pytest.raises(NameError) as exc_info:
            print(message)  # noqa: F821
            message = "Hello"
        assert "message" in str(exc_info.value)


class TestSyntaxErrorRaised:
    """测试 SyntaxError 异常"""

    def test_syntax_error_unterminated_string(self):
        """测试未闭合的字符串会报 SyntaxError"""
        with pytest.raises(SyntaxError):
            # 使用 exec 执行有语法错误的代码
            exec('print("Hello World!)')

    def test_syntax_error_missing_parenthesis(self):
        """测试缺少括号会报 SyntaxError"""
        with pytest.raises(SyntaxError):
            exec('print("Hello"')


class TestTypeErrorRaised:
    """测试 TypeError 异常"""

    def test_type_error_string_plus_int(self):
        """测试字符串与整数相加会报 TypeError"""
        age = "25"
        with pytest.raises(TypeError) as exc_info:
            result = age + 5  # noqa: F841
        assert "str" in str(exc_info.value) and "int" in str(exc_info.value)

    def test_type_error_string_concat_with_number(self):
        """测试字符串拼接时混入数字会报 TypeError"""
        with pytest.raises(TypeError):
            result = "年龄：" + 25  # noqa: F841


# ============================================================================
# 名片生成器综合测试（贯穿案例）
# ============================================================================

class TestBusinessCardGenerator:
    """测试名片生成器功能（贯穿案例）"""

    def test_business_card_static(self, capsys):
        """测试静态名片输出"""
        print("========== 个人名片 ==========")
        print("姓名：李小明")
        print("职业：Python 学习者")
        print("邮箱：xiaoming@example.com")
        print("==============================")
        captured = capsys.readouterr()
        lines = captured.out.strip().split('\n')
        assert lines[0] == "========== 个人名片 =========="
        assert "李小明" in lines[1]
        assert "Python 学习者" in lines[2]
        assert "xiaoming@example.com" in lines[3]

    def test_business_card_with_variables(self, capsys):
        """测试使用变量的名片生成"""
        name = "李小明"
        job = "Python 学习者"
        email = "xiaoming@example.com"

        print("========== 个人名片 ==========")
        print("姓名：", name)
        print("职业：", job)
        print("邮箱：", email)
        print("==============================")
        captured = capsys.readouterr()
        assert name in captured.out
        assert job in captured.out
        assert email in captured.out

    def test_business_card_with_fstring(self, capsys):
        """测试使用 f-string 的名片生成"""
        name = "李小明"
        job = "Python 学习者"
        email = "xiaoming@example.com"

        print(f"========== 个人名片 ==========")
        print(f"姓名：{name}")
        print(f"职业：{job}")
        print(f"邮箱：{email}")
        print(f"==============================")
        captured = capsys.readouterr()
        assert name in captured.out
        assert job in captured.out
        assert email in captured.out

    def test_business_card_formatted_border(self, capsys):
        """测试带边框的格式化名片"""
        name = "李小明"
        job = "Python 学习者"

        print(f"╔{'═' * 30}╗")
        print(f"║{'个人名片':^30}║")
        print(f"╠{'═' * 30}╣")
        print(f"║  姓名：{name:<20}║")
        print(f"║  职业：{job:<20}║")
        print(f"╚{'═' * 30}╝")
        captured = capsys.readouterr()
        lines = captured.out.strip().split('\n')
        assert "╔" in lines[0] and "═" in lines[0] and "╗" in lines[0]
        assert "个人名片" in lines[1]
        assert name in lines[3]
        assert job in lines[4]
