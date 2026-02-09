"""
示例：捕获特定异常（推荐做法）

本例演示：分别捕获 ValueError 和 ZeroDivisionError，给出清晰的错误提示
运行方式：python3 chapters/week_06/examples/03_specific_exceptions.py
预期输出：
  - 正常输入：输出计算结果
  - 除以零：输出"错误：分母不能为零"
  - 非数字输入：输出"错误：请输入数字，不要输入文字"
"""

print("=== 除法器（捕获特定异常，推荐）===")

try:
    # 获取用户输入
    numerator = input("请输入分子：")
    denominator = input("请输入分母：")

    # 转换为整数（可能抛出 ValueError）
    numerator = int(numerator)
    denominator = int(denominator)

    # 计算结果（可能抛出 ZeroDivisionError）
    result = numerator / denominator

    # 如果上面都没有出错，才会执行这里
    print(f"计算结果：{result}")

except ValueError:
    # 捕获 ValueError（输入非数字）
    print("错误：请输入数字，不要输入文字")

except ZeroDivisionError:
    # 捕获 ZeroDivisionError（除以零）
    print("错误：分母不能为零")

print("程序继续运行...")

# 测试建议：
# 1. 正常输入：10, 2 → 输出 5.0
# 2. 除以零：10, 0 → 输出"错误：分母不能为零"
# 3. 非数字：abc, 2 → 输出"错误：请输入数字，不要输入文字"
#
# 改进：现在用户知道具体是什么错误，可以针对性地修复
