"""
示例：猜数字游戏（while 循环版）

本例演示 while 循环：让用户反复猜，直到猜中为止
使用 break 语句跳出循环

运行方式：python3 chapters/week_02/examples/02_number_guessing_while.py

预期输出示例：
    猜一个数字（1-100）：50
    太小了，再试一次
    猜一个数字（1-100）：80
    太大了，再试一次
    猜一个数字（1-100）：42
    恭喜你，猜对了！
"""

# 设定答案
secret = 42

# while True 表示"永远循环"
# 直到遇到 break 才会跳出
while True:
    guess = int(input("猜一个数字（1-100）："))

    if guess == secret:
        print("恭喜你，猜对了！")
        break  # 跳出循环，结束游戏
    elif guess < secret:
        print("太小了，再试一次")
    else:
        print("太大了，再试一次")


# --- 坏例子演示（反例） ---
# 下面是一个常见的错误：忘记写 break，导致无限循环

# 错误示例：
# while True:
#     guess = int(input("猜一个数字（1-100）："))
#     if guess == secret:
#         print("恭喜你，猜对了！")
#         # 忘记写 break！！！
#         # 程序会一直循环，猜对了也不会停止
#     elif guess < secret:
#         print("太小了")
#     else:
#         print("太大了")


# --- 另一种写法：带条件的 while ---
"""
# 这种写法不需要 break，更简洁
secret = 42
guess = 0  # 初始化为一个不等于 secret 的值

while guess != secret:  # 当 guess 不等于 secret 时继续循环
    guess = int(input("猜一个数字（1-100）："))

    if guess < secret:
        print("太小了")
    elif guess > secret:
        print("太大了")

print("恭喜你，猜对了！")
"""
