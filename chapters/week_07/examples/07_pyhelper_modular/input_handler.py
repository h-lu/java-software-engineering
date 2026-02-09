"""
input_handler.py - 输入校验模块

职责：负责用户输入的校验和处理

功能：
- get_choice()：获取菜单选择（带异常处理）
- get_date()：获取日期（格式校验）
- get_content()：获取学习内容（非空检查）

运行方式（测试）：
  cd chapters/week_07/examples/07_pyhelper_modular
  python3 input_handler.py

导入方式：
  from input_handler import get_choice, get_date, get_content
"""


def get_choice(min_choice=1, max_choice=5):
    """获取用户选择（带异常处理）"""
    while True:
        try:
            choice = int(input(f"\n请输入选择（{min_choice}-{max_choice}）："))
            if min_choice <= choice <= max_choice:
                return choice
            print(f"错误：请输入 {min_choice} 到 {max_choice} 之间的数字")
        except ValueError:
            print("错误：请输入数字，不要输入文字")


def get_date():
    """获取日期（格式：MM-DD）"""
    while True:
        date = input("请输入日期（如 02-09）：")

        if "-" not in date or len(date) != 5:
            print("错误：日期格式不对，请输入类似 '02-09' 的格式")
            continue

        parts = date.split("-")
        if not (parts[0].isdigit() and parts[1].isdigit()):
            print("错误：日期必须是数字，请输入类似 '02-09' 的格式")
            continue

        return date


def get_content():
    """获取学习内容"""
    while True:
        content = input("请输入今天学了什么：")

        if not content.strip():
            print("错误：学习内容不能为空")
            continue

        return content


# =====================
# 测试代码
# =====================

if __name__ == "__main__":
    print("=== 测试 input_handler 模块 ===")

    choice = get_choice(1, 5)
    print(f"你选择了：{choice}")

    date = get_date()
    print(f"日期：{date}")

    content = get_content()
    print(f"内容：{content}")

    print("\n✓ 测试完成！")
