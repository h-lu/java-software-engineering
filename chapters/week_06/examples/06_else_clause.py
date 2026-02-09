"""
示例：else 子句的使用

本例演示：else 在 try/except 中的作用——只有没出错时才会执行
运行方式：python3 chapters/week_06/examples/06_else_clause.py
预期输出：
  - 成功时：输出"计算成功！结果：X"
  - 失败时：输出错误信息，不执行 else 块
"""

print("=== 除法器（使用 else 子句）===")

try:
    numerator = int(input("请输入分子："))
    denominator = int(input("请输入分母："))

    result = numerator / denominator

except ValueError:
    print("错误：请输入数字")

except ZeroDivisionError:
    print("错误：分母不能为零")

else:
    # 只有没出错时才会执行
    print(f"计算成功！结果：{result}")

# 说明：
# else 的作用是"把成功时的处理逻辑和 try 块分开"
# 这样代码结构更清晰：
#   - try：可能出错的代码
#   - except：出错了怎么处理
#   - else：没出错时做什么

# 测试建议：
# 1. 正常输入：10, 2 → 输出"计算成功！结果：5.0"
# 2. 除以零：10, 0 → 输出"错误：分母不能为零"（不执行 else）
# 3. 非数字：abc → 输出"错误：请输入数字"（不执行 else）
