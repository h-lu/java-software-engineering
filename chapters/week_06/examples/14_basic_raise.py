"""
示例：raise 的基本用法

本例演示：用 raise 主动抛出异常
运行方式：python3 chapters/week_06/examples/14_basic_raise.py
预期输出：
  - 正常年龄：输出年龄
  - 负数年龄：抛出 ValueError "年龄不能为负数"
  - 超大年龄：抛出 ValueError "年龄超出合理范围"
"""

print("=== 用 raise 主动抛出异常 ===\n")

age = int(input("请输入你的年龄："))

# 检查年龄的合法性
if age < 0:
    raise ValueError("年龄不能为负数")

if age > 120:
    raise ValueError("年龄超出合理范围")

print(f"你的年龄：{age}")

# 说明：
# raise 的作用是"主动抛出异常"，告诉调用者"这里有问题"
#
# 常见用法：
# 1. 输入验证：发现非法输入时抛出异常
# 2. 参数检查：函数参数不合法时抛出异常
# 3. 状态检查：对象状态不正确时抛出异常
#
# 测试建议：
# 输入"20" → 输出"你的年龄：20"
# 输入"-5" → 抛出 ValueError "年龄不能为负数"
# 输入"200" → 抛出 ValueError "年龄超出合理范围"
