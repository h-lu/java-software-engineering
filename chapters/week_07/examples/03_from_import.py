"""
示例：from module import name 用法

本例演示如何只导入模块中的特定函数/常量
使用时不需要加模块名前缀

优点：
- 代码更简洁
- 只导入需要的内容

缺点：
- 可能造成命名冲突（如果本地也有同名变量）

运行方式：python3 chapters/week_07/examples/03_from_import.py
预期输出：演示 from import 的用法和命名冲突问题
"""

# 示例 1：导入特定函数
from math import sqrt, pi


def demo_basic_usage():
    """演示基本用法"""
    print("=== 基本用法 ===")

    # 直接使用 sqrt，不需要加 math. 前缀
    result = sqrt(25)
    print(f"√25 = {result}")

    # 直接使用 pi
    print(f"π = {pi:.4f}")


def demo_naming_conflict():
    """演示命名冲突问题"""
    print("\n=== 命名冲突演示 ===")

    # 先导入 pi
    from math import pi
    print(f"导入的 pi：{pi}")

    # 然后定义一个同名的本地变量
    pi = 3.14
    print(f"本地 pi：{pi}")

    # 问题：导入的 pi 被覆盖了！
    print("⚠️  注意：本地变量 pi 覆盖了导入的 math.pi")


def demo_import_multiple():
    """演示导入多个名称"""
    print("\n=== 导入多个名称 ===")

    from math import sin, cos, tan, radians

    # 将角度转换为弧度
    angle_deg = 60
    angle_rad = radians(angle_deg)

    print(f"角度：{angle_deg}°")
    print(f"弧度：{angle_rad:.4f}")
    print(f"sin({angle_deg}°) = {sin(angle_rad):.4f}")
    print(f"cos({angle_deg}°) = {cos(angle_rad):.4f}")


def demo_star_import():
    """演示星号导入（不推荐）"""
    print("\n=== 星号导入（不推荐）===")

    # from math import *  # 导入 math 模块的所有内容
    # 问题：
    # 1. 不知道导入了什么
    # 2. 可能覆盖本地变量
    # 3. 代码不清晰（不知道函数来自哪个模块）

    print("不推荐使用：from math import *")
    print("建议明确导入需要的名称：from math import sqrt, pi")


if __name__ == "__main__":
    demo_basic_usage()
    demo_naming_conflict()
    demo_import_multiple()
    demo_star_import()

    print("\n=== 关键要点 ===")
    print("1. from math import sqrt：只导入 sqrt 函数")
    print("2. 使用时不需要前缀：直接写 sqrt(25)")
    print("3. 注意命名冲突：本地变量会覆盖导入的名称")
    print("4. 推荐明确导入需要的名称，避免用 *")
