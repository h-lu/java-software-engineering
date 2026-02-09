"""
示例：安全的除法器（函数封装版）

本例演示：把除法逻辑封装成函数，用 try/except 处理异常
运行方式：python3 chapters/week_06/examples/05_safe_divider.py
预期输出：
  - 三次测试：正常输入、除以零、非数字输入，都能正确处理
"""

def safe_divide():
    """
    安全的除法器

    返回：
      - 成功：返回计算结果（float）
      - 失败：返回 None
    """
    try:
        numerator = int(input("请输入分子："))
        denominator = int(input("请输入分母："))

        # 检查除以零
        if denominator == 0:
            print("错误：分母不能为零")
            return None

        result = numerator / denominator
        print(f"计算结果：{result}")
        return result

    except ValueError:
        print("错误：请输入数字，不要输入文字")
        return None


# 测试代码
if __name__ == "__main__":
    print("=== 测试 1：正常输入 ===")
    safe_divide()

    print("\n=== 测试 2：除以零 ===")
    safe_divide()

    print("\n=== 测试 3：非数字输入 ===")
    safe_divide()

# 测试建议：
# 测试 1：输入 10, 2 → 输出 5.0
# 测试 2：输入 10, 0 → 输出"错误：分母不能为零"
# 测试 3：输入 abc, 2 → 输出"错误：请输入数字，不要输入文字"
#
# 说明：
# - 用到 Week 03 学的函数（def、return）
# - 提前返回（return None）避免继续执行
# - 调用者可以通过返回值判断是否成功
