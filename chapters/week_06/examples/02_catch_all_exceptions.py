"""
示例：用 except: 捕获所有异常（不推荐）

本例演示：用裸的 except: 可以捕获所有异常，但会掩盖错误信息
运行方式：python3 chapters/week_06/examples/02_catch_all_exceptions.py
预期输出：无论输入什么，都输出"出错了！"，但不知道具体是什么错误
"""

print("=== 除法器（捕获所有异常，但不推荐）===")

try:
    # 获取用户输入
    numerator = input("请输入分子：")
    denominator = input("请输入分母：")

    # 转换为整数
    numerator = int(numerator)
    denominator = int(denominator)

    # 计算结果
    result = numerator / denominator

    # 如果上面都没有出错，才会执行这里
    print(f"计算结果：{result}")

except:
    # 捕获所有异常（不推荐！）
    print("出错了！")

print("程序继续运行...")

# 测试建议：
# 1. 正常输入：10, 2 → 输出 5.0
# 2. 除以零：10, 0 → 输出"出错了！"（但不知道是因为除以零）
# 3. 非数字：abc, 2 → 输出"出错了！"（但不知道是因为输入非数字）
#
# 问题：用户不知道具体是什么错误，无法针对性地修复
