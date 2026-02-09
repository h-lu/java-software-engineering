"""
示例：会崩溃的除法器（演示异常的抛出）

本例演示：当用户输入非法值时，程序会抛出异常并崩溃
运行方式：python3 chapters/week_06/examples/01_crashing_calculator.py
预期输出：
  - 正常输入：输出计算结果
  - 除以零：抛出 ZeroDivisionError
  - 非数字输入：抛出 ValueError
"""

print("=== 简单除法器（没有异常处理）===")

# 获取用户输入
numerator = input("请输入分子：")
denominator = input("请输入分母：")

# 转换为整数（这里可能会抛出 ValueError）
numerator = int(numerator)
denominator = int(denominator)

# 计算结果（这里可能会抛出 ZeroDivisionError）
result = numerator / denominator

# 如果上面都没有出错，才会执行这里
print(f"计算结果：{result}")
print("程序结束")

# 测试建议：
# 1. 正常输入：10, 2 → 输出 5.0
# 2. 除以零：10, 0 → ZeroDivisionError: division by zero
# 3. 非数字：abc, 2 → ValueError: invalid literal for int() with base 10: 'abc'
