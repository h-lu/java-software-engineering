"""
示例：猜数字游戏（for 循环版 - 限制次数）

本例演示 for 循环和 range() 函数
限制用户最多猜 5 次，增加游戏紧张感

运行方式：python3 chapters/week_02/examples/03_number_guessing_for.py

预期输出示例：
    第 1 次猜测（1-100）：50
    太小了
    还有 4 次机会
    第 2 次猜测（1-100）：80
    太大了
    还有 3 次机会
    ...
"""

# 设定答案和最大尝试次数
secret = 42
max_attempts = 5

# range(max_attempts) 生成 0, 1, 2, 3, 4
# 每次循环，attempt 会依次取这些值
for attempt in range(max_attempts):
    guess = int(input(f"第 {attempt + 1} 次猜测（1-100）："))

    if guess == secret:
        print(f"恭喜！你用了 {attempt + 1} 次猜中了答案 {secret}！")
        break  # 猜对了，提前跳出循环
    elif guess < secret:
        print("太小了")
    else:
        print("太大了")

    # 显示剩余次数
    remaining = max_attempts - attempt - 1
    if remaining > 0:
        print(f"还有 {remaining} 次机会")
else:
    # 注意：这个 else 和 for 对齐，不是和 if 对齐！
    # for...else 的含义：循环正常结束时执行（没有被 break 打断）
    print(f"\n很遗憾，{max_attempts} 次都用完了。答案是 {secret}")


# --- 坏例子演示（反例） ---
# 下面是一些常见的错误：

# 错误 1：误解 range(5) 的范围
# for i in range(5):
#     print(i)  # 输出：0, 1, 2, 3, 4（不是 1, 2, 3, 4, 5）

# 错误 2：for 后面的 else 缩进错误（和 if 对齐了）
# for attempt in range(max_attempts):
#     guess = int(input(...))
#     if guess == secret:
#         print("猜对了")
#         break
#     else:
#         print("猜错了")  # 这是 if 的 else，不是 for 的 else！


# --- range() 函数详解 ---
"""
range(5)      -> 0, 1, 2, 3, 4           （从 0 开始，到 5 结束，不包括 5）
range(1, 6)   -> 1, 2, 3, 4, 5           （从 1 开始，到 6 结束，不包括 6）
range(0, 10, 2) -> 0, 2, 4, 6, 8         （从 0 开始，每次加 2，到 10 结束）

常见用法：
for i in range(5):           # 循环 5 次
for i in range(1, 6):        # 循环 5 次，从 1 开始计数
for i in range(0, 10, 2):    # 遍历偶数
"""
