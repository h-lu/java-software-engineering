"""
示例：猜数字游戏（完整版 - 带难度选择）

本例演示：
1. 复杂的条件判断（if/elif/else 嵌套）
2. 逻辑运算符（and, or, not）
3. 布尔表达式的可读性优化

运行方式：python3 chapters/week_02/examples/04_number_guessing_full.py

预期输出示例：
    === 猜数字游戏 ===
    选择难度：
    1. 简单（1-50）
    2. 中等（1-100）
    3. 困难（1-200）
    请输入难度（1-3）：2

    我想好了一个 1 到 100 之间的数字，你有 5 次机会猜中它！

    第 1 次猜测：50
    太小了
    还有 4 次机会
    ...
"""

import random

print("=== 猜数字游戏 ===")
print("选择难度：")
print("1. 简单（1-50）")
print("2. 中等（1-100）")
print("3. 困难（1-200）")

# 获取难度选择
difficulty = input("请输入难度（1-3）：")

# 方法 1：用 if/elif/else 处理不同的难度
if difficulty == "1":
    max_num = 50
elif difficulty == "2":
    max_num = 100
elif difficulty == "3":
    max_num = 200
else:
    print("无效选择，使用中等模式")
    max_num = 100

# 随机生成答案
secret = random.randint(1, max_num)
max_attempts = 5

print(f"\n我想好了一个 1 到 {max_num} 之间的数字，你有 {max_attempts} 次机会猜中它！")

# 限制次数的猜测循环
for attempt in range(max_attempts):
    guess = int(input(f"\n第 {attempt + 1} 次猜测："))

    if guess == secret:
        print(f"恭喜！你用了 {attempt + 1} 次猜中了答案 {secret}！")
        break
    elif guess < secret:
        print("太小了")
    else:
        print("太大了")

    remaining = max_attempts - attempt - 1
    if remaining > 0:
        print(f"还有 {remaining} 次机会")
else:
    print(f"\n很遗憾，{max_attempts} 次都用完了。答案是 {secret}")


# --- 逻辑运算符演示 ---
"""
# and：两个条件都为真，整个表达式才为真
age = 20
is_student = True
if age < 25 and is_student:
    print("你可以享受学生优惠")

# or：只要有一个条件为真，整个表达式就为真
if age < 18 or age > 65:
    print("你可以享受优惠票价")

# not：取反
difficulty = 2
if not (difficulty == 1 or difficulty == 2 or difficulty == 3):
    print("无效的难度选择")
"""


# --- 布尔表达式优化示例（老潘的建议） ---
"""
# 坏写法：太复杂，难以理解
if (difficulty == 1 and max_num == 50) or (difficulty == 2 and max_num == 100) or (difficulty == 3 and max_num == 200):
    print("有效配置")

# 好写法：用变量给布尔表达式起名字
is_valid_difficulty = difficulty in [1, 2, 3]
is_valid_range = max_num in [50, 100, 200]

if is_valid_difficulty and is_valid_range:
    print("有效配置")

# 更 Python 的写法：用 in 简化
if difficulty not in [1, 2, 3]:
    print("无效的难度选择")
"""


# --- 输入验证演示 ---
"""
# 更严谨的输入验证（防止用户输入非数字）
difficulty = input("请输入难度（1-3）：")

# 检查是否为数字且在范围内
if difficulty.isdigit() and 1 <= int(difficulty) <= 3:
    difficulty = int(difficulty)
else:
    print("无效输入，请输入 1、2 或 3")
    difficulty = 2  # 默认中等
"""
