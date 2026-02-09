"""
示例：import module as alias 用法

本例演示如何给模块起别名
常用于模块名字很长的情况

运行方式：python3 chapters/week_07/examples/04_import_alias.py
预期输出：演示 import as 的用法
"""

# 示例 1：给 math 起个简短的名字
import math as m


def demo_math_alias():
    """演示 math 模块的别名"""
    print("=== 使用 m 作为 math 的别名 ===")

    # 用 m. 代替 math.
    result = m.sqrt(16)
    print(f"√16 = {result}")

    print(f"π = {m.pi:.4f}")


def demo_common_aliases():
    """演示常见的模块别名约定"""
    print("\n=== 常见的模块别名约定 ===")

    # Python 社区有一些约定俗成的别名
    # 虽然我们没有安装这些库，但可以看到命名约定

    aliases = [
        ("numpy", "np", "数值计算库"),
        ("pandas", "pd", "数据分析库"),
        ("matplotlib.pyplot", "plt", "绘图库"),
        ("seaborn", "sns", "统计可视化库"),
    ]

    for module, alias, description in aliases:
        print(f"import {module} as {alias}  # {description}")


def demo_why_use_alias():
    """演示为什么需要别名"""
    print("\n=== 为什么需要别名？===")

    reasons = [
        "1. 模块名字很长：import matplotlib.pyplot as plt",
        "2. 避免命名冲突：如果本地也有一个 math 变量",
        "3. 符合社区约定：import numpy as np（大家都这么写）",
        "4. 提高代码可读性：np.array 比 numpy.array 更简洁",
    ]

    for reason in reasons:
        print(reason)


def demo_custom_module_alias():
    """演示自定义模块的别名"""
    print("\n=== 自定义模块的别名 ===")

    # 假设你有一个很长的模块名
    # import my_very_long_module_name as mvl

    print("如果模块名很长：")
    print("  import my_very_long_module_name as mvl")
    print("  使用：mvl.function()  # 比 my_very_long_module_name.function() 简洁")


if __name__ == "__main__":
    demo_math_alias()
    demo_common_aliases()
    demo_why_use_alias()
    demo_custom_module_alias()

    print("\n=== 关键要点 ===")
    print("1. import math as m：给 math 起别名 m")
    print("2. 使用：m.sqrt() 而不是 math.sqrt()")
    print("3. 常见约定：numpy as np、pandas as pd、pyplot as plt")
    print("4. 优点：简化代码、避免冲突、符合社区规范")
