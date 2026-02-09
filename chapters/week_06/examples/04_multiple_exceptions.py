"""
示例：一次捕获多个异常

本例演示：用元组捕获多个异常类型，当它们有相同的处理方式时
运行方式：python3 chapters/week_06/examples/04_multiple_exceptions.py
预期输出：
  - 正常输入：输出计算结果
  - ValueError 或 TypeError：输出"错误：输入不合法，请检查"
  - ZeroDivisionError：输出"错误：分母不能为零"
"""

print("=== 除法器（一次捕获多个异常）===")

try:
    # 获取用户输入
    numerator = input("请输入分子：")
    denominator = input("请输入分母：")

    # 转换为整数（可能抛出 ValueError 或 TypeError）
    numerator = int(numerator)
    denominator = int(denominator)

    # 计算结果（可能抛出 ZeroDivisionError）
    result = numerator / denominator

    # 如果上面都没有出错，才会执行这里
    print(f"计算结果：{result}")

except (ValueError, TypeError):
    # 一次捕获两种异常（当它们的处理方式相同时）
    print("错误：输入不合法，请检查")

except ZeroDivisionError:
    # 单独捕获 ZeroDivisionError（需要不同的处理方式）
    print("错误：分母不能为零")

print("程序继续运行...")

# 测试建议：
# 1. 正常输入：10, 2 → 输出 5.0
# 2. 除以零：10, 0 → 输出"错误：分母不能为零"
# 3. 非数字：abc, 2 → 输出"错误：输入不合法，请检查"
#
# 说明：ValueError 和 TypeError 都是"输入不合法"，可以用同样的错误提示
#       ZeroDivisionError 需要更具体的提示，所以单独处理
