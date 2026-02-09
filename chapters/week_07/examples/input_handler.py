"""
input_handler.py - 输入校验模块

本模块负责用户输入的校验和处理
包含以下功能：
1. get_choice()：获取菜单选择（带异常处理）
2. get_date()：获取日期（格式校验）
3. get_content()：获取学习内容（非空检查）

运行方式（测试）：
  cd chapters/week_07/examples
  python3 input_handler.py

预期输出：
  === 测试 input_handler 模块 ===
  请输入选择（1-5）：[用户输入]
  你选择了：[选择结果]

导入方式：
  from input_handler import get_choice, get_date, get_content
"""


def get_choice(min_choice=1, max_choice=5):
    """
    获取用户选择（带异常处理）

    Week 06 的异常处理：
    - 用 while + try/except 实现"重试直到输入正确"
    - 捕获 ValueError（输入非数字）
    - 检查数字是否在有效范围内

    参数：
        min_choice (int)：最小选项值，默认 1
        max_choice (int)：最大选项值，默认 5

    返回：
        int：用户输入的有效选择
    """
    while True:
        try:
            choice = int(input(f"\n请输入选择（{min_choice}-{max_choice}）："))
            if min_choice <= choice <= max_choice:
                return choice
            print(f"错误：请输入 {min_choice} 到 {max_choice} 之间的数字")

        except ValueError:
            # 捕获输入非数字的错误
            print("错误：请输入数字，不要输入文字")


def get_date():
    """
    获取日期（格式：MM-DD）

    Week 06 的输入校验：
    - 检查日期格式（必须是 XX-XX）
    - 检查是否为数字

    返回：
        str：格式化的日期字符串，如 "02-09"
    """
    while True:
        date = input("请输入日期（如 02-09）：")

        # 简单校验：日期格式必须是 XX-XX
        if "-" not in date or len(date) != 5:
            print("错误：日期格式不对，请输入类似 '02-09' 的格式")
            continue

        # 校验是否为数字
        parts = date.split("-")
        if not (parts[0].isdigit() and parts[1].isdigit()):
            print("错误：日期必须是数字，请输入类似 '02-09' 的格式")
            continue

        return date


def get_content():
    """
    获取学习内容

    Week 06 的空值检查：
    - 确保用户输入了内容（不允许空字符串）

    返回：
        str：用户输入的学习内容
    """
    while True:
        content = input("请输入今天学了什么：")

        if not content.strip():
            print("错误：学习内容不能为空")
            continue

        return content


# =====================
# 测试代码（__name__ 守卫）
# =====================

if __name__ == "__main__":
    """
    测试 input_handler 模块

    当直接运行此文件时，会执行测试代码
    当导入此模块时，不会执行测试代码
    """
    print("=== 测试 input_handler 模块 ===")

    # 测试 get_choice
    choice = get_choice(1, 5)
    print(f"你选择了：{choice}")

    # 测试 get_date
    date = get_date()
    print(f"日期：{date}")

    # 测试 get_content
    content = get_content()
    print(f"内容：{content}")

    print("\n✓ 测试完成！")
