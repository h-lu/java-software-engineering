"""
示例：LBYL 风格输入验证

本例演示：LBYL（Look Before You Leap）风格适合用户输入验证
运行方式：python3 chapters/week_06/examples/12_lbyl_input.py
预期输出：
  - 有效输入：输出年龄
  - 无效输入：输出错误提示，并让用户重试
"""

print("=== LBYL 风格（推荐用于用户输入）===")

age_str = input("请输入你的年龄：")

# LBYL：先检查输入是否合法，再转换
if age_str.isdigit() and 0 < int(age_str) < 120:
    age = int(age_str)
    print(f"你的年龄：{age}")
else:
    print("输入无效：年龄必须是 1-119 之间的整数")

print("\n=== 对比：EAFP 风格（不推荐用于用户输入）===")

# EAFP：直接尝试转换，失败了再处理
try:
    age = int(input("请输入你的年龄："))
    if not (0 < age < 120):
        raise ValueError("年龄超出范围")
    print(f"你的年龄：{age}")
except ValueError:
    print("输入无效：年龄必须是 1-119 之间的整数")

# 说明：
# LBYL 更适合用户输入，因为：
# 1. 用户输入经常失败（异常开销大）
# 2. isdigit() 等检查的开销很小
# 3. 可以给出更友好的错误提示
#
# EAFP 不适合用户输入，因为：
# 1. 抛出异常的开销比 if 判断大
# 2. 用户输入"abc"会抛出异常（这是预期行为，不应该用异常处理）

# 测试建议：
# 输入"20" → 输出年龄
# 输入"abc" → 输出错误提示
# 输入"-5" → 输出错误提示
# 输入"200" → 输出错误提示
