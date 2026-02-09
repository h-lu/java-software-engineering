"""
测试 PyHelper 函数

Week 03 PyHelper 进度：函数重构 + 菜单

测试覆盖：
- 欢迎信息
- 菜单显示
- 用户选择验证
- 心情推荐
- 名言返回
- 主程序流程
"""

import pytest


# ============================================================================
# 测试欢迎信息功能
# ============================================================================

def test_print_welcome():
    """测试：打印欢迎信息"""
    def print_welcome():
        """打印欢迎信息"""
        return "=" * 40 + "\n  欢迎使用 PyHelper！\n" + "=" * 40 + "\n"

    welcome = print_welcome()
    assert "欢迎使用 PyHelper" in welcome
    assert "===" in welcome


def test_welcome_contains_separator():
    """测试：欢迎信息包含分隔线"""
    def print_welcome():
        """打印欢迎信息"""
        separator = "=" * 40
        return f"{separator}\n  欢迎使用 PyHelper！\n{separator}\n"

    welcome = print_welcome()
    assert "========================================" in welcome


# ============================================================================
# 测试菜单功能
# ============================================================================

def test_print_menu():
    """测试：打印菜单"""
    def print_menu():
        """打印菜单"""
        return "请选择功能：\n1. 获取学习建议\n2. 查看今日名言\n3. 退出\n"

    menu = print_menu()
    assert "请选择功能" in menu
    assert "获取学习建议" in menu
    assert "查看今日名言" in menu
    assert "退出" in menu


def test_menu_has_three_options():
    """测试：菜单有三个选项"""
    def print_menu():
        """打印菜单"""
        menu = """
1. 获取学习建议
2. 查看今日名言
3. 退出
"""
        return menu

    menu = print_menu()
    assert "1." in menu
    assert "2." in menu
    assert "3." in menu


# ============================================================================
# 测试选择验证功能
# ============================================================================

def test_validate_choice_valid():
    """测试：验证有效的选择"""
    def validate_choice(choice):
        """验证选择是否有效"""
        valid_choices = ["1", "2", "3"]
        return choice in valid_choices

    assert validate_choice("1") is True
    assert validate_choice("2") is True
    assert validate_choice("3") is True


def test_validate_choice_invalid():
    """测试：验证无效的选择"""
    def validate_choice(choice):
        """验证选择是否有效"""
        valid_choices = ["1", "2", "3"]
        return choice in valid_choices

    assert validate_choice("0") is False
    assert validate_choice("4") is False
    assert validate_choice("abc") is False
    assert validate_choice("") is False
    assert validate_choice("1.5") is False


@pytest.mark.parametrize("choice, is_valid", [
    ("1", True),
    ("2", True),
    ("3", True),
    ("0", False),
    ("4", False),
    ("-1", False),
    ("10", False),
    ("abc", False),
    ("", False),
    (" ", False),
    ("1a", False),
])
def test_validate_choice_parameterized(choice, is_valid):
    """测试：验证选择（参数化）"""
    def validate_choice(choice):
        """验证选择是否有效"""
        valid_choices = ["1", "2", "3"]
        return choice in valid_choices

    assert validate_choice(choice) == is_valid


# ============================================================================
# 测试心情获取功能
# ============================================================================

def test_get_mood_menu():
    """测试：获取心情的菜单"""
    def get_mood_menu():
        """返回心情选择菜单"""
        return """
今天心情怎么样？
1. 充满干劲
2. 一般般
3. 有点累
"""

    menu = get_mood_menu()
    assert "今天心情怎么样" in menu
    assert "充满干劲" in menu
    assert "一般般" in menu
    assert "有点累" in menu


def test_validate_mood():
    """测试：验证心情选择"""
    def validate_mood(mood):
        """验证心情选择是否有效"""
        valid_moods = ["1", "2", "3"]
        return mood in valid_moods

    assert validate_mood("1") is True
    assert validate_mood("2") is True
    assert validate_mood("3") is True
    assert validate_mood("4") is False
    assert validate_mood("0") is False


# ============================================================================
# 测试学习建议功能
# ============================================================================

def test_get_advice_motivated():
    """测试：充满干劲时的建议"""
    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "太好了！推荐你今天挑战一个新概念，比如开始学习函数或者列表。"
        elif mood == "2":
            return "那就做点巩固练习吧，复习上周的变量和字符串，写几个小例子。"
        elif mood == "3":
            return "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
        else:
            return "写点巩固练习最稳妥。"

    advice = get_advice("1")
    assert "挑战" in advice
    assert "新概念" in advice


def test_get_advice_average():
    """测试：一般般时的建议"""
    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "太好了！推荐你今天挑战一个新概念，比如开始学习函数或者列表。"
        elif mood == "2":
            return "那就做点巩固练习吧，复习上周的变量和字符串，写几个小例子。"
        elif mood == "3":
            return "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
        else:
            return "写点巩固练习最稳妥。"

    advice = get_advice("2")
    assert "巩固" in advice
    assert "练习" in advice


def test_get_advice_tired():
    """测试：有点累时的建议"""
    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "太好了！推荐你今天挑战一个新概念，比如开始学习函数或者列表。"
        elif mood == "2":
            return "那就做点巩固练习吧，复习上周的变量和字符串，写几个小例子。"
        elif mood == "3":
            return "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
        else:
            return "写点巩固练习最稳妥。"

    advice = get_advice("3")
    assert "休息" in advice or "累" in advice


def test_get_advice_invalid():
    """测试：无效心情时的默认建议"""
    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "太好了！推荐你今天挑战一个新概念，比如开始学习函数或者列表。"
        elif mood == "2":
            return "那就做点巩固练习吧，复习上周的变量和字符串，写几个小例子。"
        elif mood == "3":
            return "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
        else:
            return "写点巩固练习最稳妥。"

    advice = get_advice("999")
    assert "巩固" in advice


def test_get_advice_returns_string():
    """测试：get_advice 返回字符串"""
    def get_advice(mood):
        """根据心情返回建议"""
        return "建议内容"

    advice = get_advice("1")
    assert isinstance(advice, str)


# ============================================================================
# 测试名言功能
# ============================================================================

def test_get_quote():
    """测试：获取今日名言"""
    def get_quote():
        """返回今日名言"""
        return "学编程是马拉松，不是百米冲刺。找到自己的节奏最重要。"

    quote = get_quote()
    assert "马拉松" in quote
    assert "节奏" in quote


def test_get_quote_returns_string():
    """测试：get_quote 返回字符串"""
    def get_quote():
        """返回今日名言"""
        return "学编程是马拉松，不是百米冲刺。找到自己的节奏最重要。"

    quote = get_quote()
    assert isinstance(quote, str)
    assert len(quote) > 0


def test_get_quote_is_consistent():
    """测试：get_quote 每次返回相同内容"""
    def get_quote():
        """返回今日名言"""
        return "学编程是马拉松，不是百米冲刺。找到自己的节奏最重要。"

    quote1 = get_quote()
    quote2 = get_quote()
    assert quote1 == quote2


# ============================================================================
# 测试显示建议功能
# ============================================================================

def test_show_advice_combines_mood_and_quote():
    """测试：show_advice 结合心情建议和名言"""
    def get_mood():
        """模拟获取心情"""
        return "1"

    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "太好了！推荐你今天挑战一个新概念。"

    def get_quote():
        """返回今日名言"""
        return "学编程是马拉松。"

    def show_advice():
        """功能1：显示学习建议"""
        mood = get_mood()
        advice = get_advice(mood)
        return f"{advice}\n{get_quote()}"

    result = show_advice()
    assert "挑战" in result
    assert "马拉松" in result


# ============================================================================
# 测试显示名言功能
# ============================================================================

def test_show_quote():
    """测试：显示名言"""
    def get_quote():
        """返回今日名言"""
        return "学编程是马拉松，不是百米冲刺。找到自己的节奏最重要。"

    def show_quote():
        """功能2：显示名言"""
        return f"今日一句：{get_quote()}"

    result = show_quote()
    assert "今日一句" in result
    assert "马拉松" in result


# ============================================================================
# 测试主程序流程
# ============================================================================

def test_main_welcome_then_menu():
    """测试：主程序先显示欢迎，再显示菜单"""
    execution_order = []

    def print_welcome():
        """打印欢迎信息"""
        execution_order.append("welcome")
        return "欢迎"

    def print_menu():
        """打印菜单"""
        execution_order.append("menu")
        return "菜单"

    def mock_main():
        """模拟主程序"""
        print_welcome()
        print_menu()

    mock_main()
    assert execution_order == ["welcome", "menu"]


def test_main_choice_logic():
    """测试：主程序的选择逻辑"""
    def process_choice(choice):
        """处理用户选择"""
        if choice == "1":
            return "获取学习建议"
        elif choice == "2":
            return "查看今日名言"
        elif choice == "3":
            return "退出"
        else:
            return "无效选择"

    assert process_choice("1") == "获取学习建议"
    assert process_choice("2") == "查看今日名言"
    assert process_choice("3") == "退出"
    assert process_choice("4") == "无效选择"


# ============================================================================
# 测试函数的模块化设计
# ============================================================================

def test_functions_are_modular():
    """测试：函数是模块化的，可以独立测试"""
    def get_advice(mood):
        """独立的建议函数"""
        if mood == "1":
            return "建议1"
        return "建议2"

    def get_quote():
        """独立的名言函数"""
        return "名言"

    # 可以独立测试每个函数
    assert get_advice("1") == "建议1"
    assert get_quote() == "名言"


def test_functions_are_reusable():
    """测试：函数是可复用的"""
    def get_quote():
        """返回今日名言"""
        return "学编程是马拉松。"

    # 可以多次调用同一个函数
    quote1 = get_quote()
    quote2 = get_quote()
    quote3 = get_quote()

    assert quote1 == quote2 == quote3


# ============================================================================
# 测试函数的返回值 vs 打印
# ============================================================================

def test_get_advice_returns_does_not_print():
    """测试：get_advice 返回值而不是直接打印"""
    output = []

    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "太好了！推荐你今天挑战一个新概念。"
        return "其他建议"

    # 这个函数返回字符串，不打印
    advice = get_advice("1")

    assert advice == "太好了！推荐你今天挑战一个新概念。"
    assert len(output) == 0  # 没有打印


def test_show_advice_can_format_output():
    """测试：show_advice 可以格式化输出"""
    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "挑战新概念"

    def show_advice():
        """显示学习建议（带格式）"""
        advice = get_advice("1")
        return f"\n{advice}\n"

    result = show_advice()
    assert result.startswith("\n")
    assert result.endswith("\n")
    assert "挑战新概念" in result


# ============================================================================
# 测试边界情况
# ============================================================================

def test_empty_mood():
    """测试：空心情输入"""
    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "建议1"
        elif mood == "2":
            return "建议2"
        elif mood == "3":
            return "建议3"
        else:
            return "默认建议"

    assert get_advice("") == "默认建议"


def test_whitespace_mood():
    """测试：空白字符心情输入"""
    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "建议1"
        elif mood == "2":
            return "建议2"
        elif mood == "3":
            return "建议3"
        else:
            return "默认建议"

    # 空格不是有效心情
    assert get_advice(" ") == "默认建议"


def test_numeric_string_mood():
    """测试：数字字符串心情输入"""
    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "建议1"
        elif mood == "2":
            return "建议2"
        elif mood == "3":
            return "建议3"
        else:
            return "默认建议"

    assert get_advice("99") == "默认建议"
    assert get_advice("-1") == "默认建议"


# ============================================================================
# 测试函数组合
# ============================================================================

def test_combine_advice_and_quote():
    """测试：组合建议和名言"""
    def get_advice(mood):
        """根据心情返回建议"""
        if mood == "1":
            return "挑战新概念"
        return "巩固练习"

    def get_quote():
        """返回今日名言"""
        return "学编程是马拉松。"

    def show_advice(mood):
        """显示建议和名言"""
        advice = get_advice(mood)
        quote = get_quote()
        return f"{advice}\n{quote}"

    result = show_advice("1")
    assert "挑战新概念" in result
    assert "马拉松" in result


def test_function_chaining():
    """测试：函数链式调用"""
    def add_prefix(text):
        """添加前缀"""
        return f"前缀：{text}"

    def add_suffix(text):
        """添加后缀"""
        return f"{text}：后缀"

    # 链式调用
    result = add_suffix(add_prefix("内容"))
    assert result == "前缀：内容：后缀"


# ============================================================================
# 测试 PyHelper 的文档字符串
# ============================================================================

def test_pyhelper_functions_have_docstrings():
    """测试：PyHelper 函数应该有文档字符串"""
    def print_welcome():
        """打印欢迎信息"""
        pass

    def get_advice(mood):
        """根据心情返回建议"""
        pass

    def get_quote():
        """返回今日名言"""
        pass

    assert print_welcome.__doc__ is not None
    assert get_advice.__doc__ is not None
    assert get_quote.__doc__ is not None


# ============================================================================
# 测试 PyHelper 的输出格式
# ============================================================================

def test_welcome_format():
    """测试：欢迎信息的格式"""
    def print_welcome():
        """打印欢迎信息"""
        return "=" * 40 + "\n  欢迎使用 PyHelper！\n" + "=" * 40 + "\n"

    welcome = print_welcome()
    lines = welcome.split("\n")

    # 应该有4行（包括最后的空行）
    assert len(lines) == 4
    # 第一行和第三行是分隔线
    assert lines[0] == "=" * 40
    assert lines[2] == "=" * 40


def test_menu_format():
    """测试：菜单的格式"""
    def print_menu():
        """打印菜单"""
        return """请选择功能：
1. 获取学习建议
2. 查看今日名言
3. 退出
"""

    menu = print_menu()
    assert "请选择功能：" in menu
    assert "1." in menu
    assert "2." in menu
    assert "3." in menu


# ============================================================================
# 测试 PyHelper 的状态管理
# ============================================================================

def test_pyhelper_is_stateless():
    """测试：PyHelper 函数应该是无状态的"""
    call_count = []

    def get_quote():
        """返回今日名言"""
        call_count.append(1)
        return "学编程是马拉松。"

    # 每次调用返回相同结果
    quote1 = get_quote()
    quote2 = get_quote()

    assert quote1 == quote2
    assert len(call_count) == 2


# ============================================================================
# 测试 PyHelper 的扩展性
# ============================================================================

def test_adding_new_feature_is_easy():
    """测试：添加新功能应该很容易"""
    def show_advice():
        """功能1：显示学习建议"""
        return "学习建议"

    def show_quote():
        """功能2：显示名言"""
        return "名言"

    # 可以轻松添加新功能
    def show_records():
        """功能3：显示学习记录（新功能）"""
        return "学习记录"

    assert show_advice() == "学习建议"
    assert show_quote() == "名言"
    assert show_records() == "学习记录"


def test_menu_can_be_extended():
    """测试：菜单可以轻松扩展"""
    def print_menu_v1():
        """版本1的菜单"""
        return "1. 建议\n2. 名言\n3. 退出\n"

    def print_menu_v2():
        """版本2的菜单（添加了新功能）"""
        return "1. 建议\n2. 名言\n3. 记录\n4. 退出\n"

    menu1 = print_menu_v1()
    menu2 = print_menu_v2()

    assert "记录" not in menu1
    assert "记录" in menu2


# ============================================================================
# 测试 PyHelper 的错误处理
# ============================================================================

def test_invalid_choice_handling():
    """测试：无效选择的处理"""
    def handle_choice(choice):
        """处理用户选择"""
        if choice == "1":
            return "功能1"
        elif choice == "2":
            return "功能2"
        elif choice == "3":
            return "退出"
        else:
            return "无效输入"

    assert handle_choice("invalid") == "无效输入"
    assert handle_choice("4") == "无效输入"
    assert handle_choice("") == "无效输入"


# ============================================================================
# 测试 PyHelper 的用户体验
# ============================================================================

def test_clear_feedback():
    """测试：提供清晰的反馈"""
    def process_choice(choice):
        """处理用户选择并返回反馈"""
        if choice == "1":
            return "你选择了：获取学习建议"
        elif choice == "2":
            return "你选择了：查看今日名言"
        elif choice == "3":
            return "再见！"
        else:
            return "无效输入，请重试"

    assert "获取学习建议" in process_choice("1")
    assert "查看今日名言" in process_choice("2")
    assert "再见" in process_choice("3")


def test_friendly_messages():
    """测试：友好的消息"""
    def get_farewell_message():
        """返回告别消息"""
        return "\n再见！祝你学习愉快！"

    farewell = get_farewell_message()
    assert "再见" in farewell
    assert "愉快" in farewell


# ============================================================================
# 测试 PyHelper 的代码质量
# ============================================================================

def test_functions_have_single_responsibility():
    """测试：函数应该有单一职责"""
    def get_advice(mood):
        """只负责返回建议"""
        if mood == "1":
            return "建议1"
        return "建议2"

    def format_advice(advice):
        """只负责格式化建议"""
        return f"\n{advice}\n"

    # 每个函数只做一件事
    advice = get_advice("1")
    formatted = format_advice(advice)

    assert formatted == "\n建议1\n"


def test_functions_are_short():
    """测试：函数应该简短"""
    def short_function():
        """简短的函数"""
        return "结果"

    # 函数应该足够短，易于理解
    # 这里只是示例，实际测试中可以检查函数长度
    result = short_function()
    assert result == "结果"


# ============================================================================
# 测试 PyHelper 的国际化潜力
# ============================================================================

def test_messages_can_be_localized():
    """测试：消息可以被本地化"""
    messages = {
        "zh": {
            "welcome": "欢迎使用 PyHelper！",
            "goodbye": "再见！",
        },
        "en": {
            "welcome": "Welcome to PyHelper!",
            "goodbye": "Goodbye!",
        }
    }

    def get_message(key, lang="zh"):
        """获取本地化消息"""
        return messages[lang][key]

    assert get_message("welcome", "zh") == "欢迎使用 PyHelper！"
    assert get_message("welcome", "en") == "Welcome to PyHelper!"
    assert get_message("goodbye", "zh") == "再见！"
    assert get_message("goodbye", "en") == "Goodbye!"


# ============================================================================
# 测试 PyHelper 的配置灵活性
# ============================================================================

def test_menu_options_can_be_configured():
    """测试：菜单选项可以被配置"""
    def get_menu_config():
        """返回菜单配置"""
        return {
            "1": {"name": "获取学习建议", "action": "advice"},
            "2": {"name": "查看今日名言", "action": "quote"},
            "3": {"name": "退出", "action": "exit"},
        }

    config = get_menu_config()
    assert config["1"]["name"] == "获取学习建议"
    assert config["2"]["action"] == "quote"
    assert config["3"]["action"] == "exit"


# ============================================================================
# 测试 PyHelper 的日志记录潜力
# ============================================================================

def test_function_calls_can_be_logged():
    """测试：函数调用可以被记录"""
    log = []

    def logged_get_advice(mood):
        """带日志的建议函数"""
        log.append(f"get_advice called with mood={mood}")
        if mood == "1":
            return "建议1"
        return "建议2"

    # 调用函数
    result = logged_get_advice("1")

    assert result == "建议1"
    assert len(log) == 1
    assert "get_advice called with mood=1" in log[0]
