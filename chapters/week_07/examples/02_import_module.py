"""
示例：import module 用法

本例演示最基本的 import 写法——导入整个模块
使用时需要加模块名前缀（如 math.sqrt）

优点：
- 避免命名冲突
- 代码清晰，一眼看出函数来自哪个模块

运行方式：python3 chapters/week_07/examples/02_import_module.py
预期输出：演示 math 模块的平方根、圆周率、三角函数
"""

# 导入整个 math 模块
import math


def demo_sqrt():
    """演示平方根计算"""
    print("=== 平方根计算 ===")

    # 使用时加 math. 前缀
    result = math.sqrt(16)
    print(f"√16 = {result}")

    result = math.sqrt(2)
    print(f"√2 = {result:.4f}")


def demo_pi():
    """演示圆周率"""
    print("\n=== 圆周率 ===")

    # math.pi 是一个常量
    print(f"圆周率 π = {math.pi}")
    print(f"保留 2 位小数：{math.pi:.2f}")


def demo_trig():
    """演示三角函数"""
    print("\n=== 三角函数 ===")

    # 计算正弦、余弦
    angle = math.pi / 4  # 45 度
    sin_val = math.sin(angle)
    cos_val = math.cos(angle)

    print(f"sin(π/4) = {sin_val:.4f}")
    print(f"cos(π/4) = {cos_val:.4f}")


def demo_max_min():
    """演示最大值、最小值"""
    print("\n=== 最大值、最小值 ===")

    numbers = [3, 1, 4, 1, 5, 9, 2, 6]
    maximum = max(numbers)  # max 是内置函数，不需要 import
    minimum = min(numbers)

    print(f"列表：{numbers}")
    print(f"最大值：{maximum}")
    print(f"最小值：{minimum}")


if __name__ == "__main__":
    demo_sqrt()
    demo_pi()
    demo_trig()
    demo_max_min()

    print("\n=== 关键要点 ===")
    print("1. import math：导入整个 math 模块")
    print("2. 使用时需要加前缀：math.sqrt()、math.pi")
    print("3. 优点：避免命名冲突，代码清晰")
