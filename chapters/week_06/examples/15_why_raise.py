"""
示例：为什么需要 raise

本例演示：raise 比 print 更适合上报错误
运行方式：python3 chapters/week_06/examples/15_why_raise.py
预期输出：
  - 余额不足：转账失败，不发送通知
  - 余额充足：转账成功，发送通知
"""

# 模拟银行账户
class Account:
    def __init__(self, name, balance):
        self.name = name
        self.balance = balance


# ❌ 坏的做法：用 print 上报错误
def transfer_bad(from_account, to_account, amount):
    """转账函数（坏做法：用 print 上报错误）"""
    if from_account.balance < amount:
        print("余额不足，转账失败")
        return

    from_account.balance -= amount
    to_account.balance += amount
    print("转账成功")


print("=== 坏做法：用 print 上报错误 ===")
alice = Account("Alice", 100)
bob = Account("Bob", 0)

transfer_bad(alice, bob, 200)
print("给 Bob 发送通知...")  # 转账失败也会执行（不对！）
print()


# ✅ 好的做法：用 raise 上报错误
def transfer_good(from_account, to_account, amount):
    """转账函数（好做法：用 raise 上报错误）"""
    if from_account.balance < amount:
        raise ValueError("余额不足，转账失败")

    from_account.balance -= amount
    to_account.balance += amount


print("=== 好做法：用 raise 上报错误 ===")
alice = Account("Alice", 100)
bob = Account("Bob", 0)

try:
    transfer_good(alice, bob, 200)
    print("给 Bob 发送通知...")  # 只有转账成功才会执行
except ValueError as e:
    print(f"转账失败：{e}")
    print("不发送通知")

# 说明：
# print 的问题：
# 1. 不会让程序停下来，后续代码会继续执行
# 2. 调用者无法判断操作是否成功
# 3. 错误信息只能打印到控制台，无法编程处理
#
# raise 的优势：
# 1. 会中断程序，让调用者知道"出错了"
# 2. 调用者可以用 try/except 捕获并处理错误
# 3. 错误信息可以编程处理（记录日志、发送告警等）
