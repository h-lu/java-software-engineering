"""
示例：猜数字游戏（单次判断版）

本例演示最简单的条件判断：if/elif/else
用户只能猜一次，程序会告诉"大了""小了""猜对了"

运行方式：python3 chapters/week_02/examples/01_number_guessing_simple.py

预期输出示例：
    猜一个数字（1-100）：50
    太小了
"""

# 设定答案（固定为 42，方便测试）
secret = 42

# 获取用户输入
# input() 返回的是字符串，需要用 int() 转成整数
guess = int(input("猜一个数字（1-100）："))

# 用 if/elif/else 做判断
if guess == secret:
    print("恭喜你，猜对了！")
elif guess < secret:
    print("太小了")
else:
    print("太大了")


# --- 坏例子演示（反例） ---
# 下面是一些常见的错误写法，请避免：

# 错误 1：忘记冒号
# if guess == secret
#     print("猜对了")  # SyntaxError: invalid syntax

# 错误 2：缩进不一致
# if guess == secret:
#     print("猜对了")
#   print("缩进错了")  # IndentationError

# 错误 3：用赋值代替比较
# if guess = secret:  # SyntaxError: invalid syntax
#     print("猜对了")

# 错误 4：逻辑顺序错误（把范围大的放在前面）
# score = 85
# if score >= 60:
#     print("及格")  # 85 分会被判定为"及格"而不是"优秀"
# elif score >= 90:
#     print("优秀")
