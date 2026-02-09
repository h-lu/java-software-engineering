"""
示例：交互式名片（第 3 节）。

使用 input() 获取用户信息，生成个性化名片。

运行方式：python3 chapters/week_01/examples/03_business_card_interactive.py
交互说明：按提示输入姓名、职业、邮箱，查看生成的名片。
"""

print("=== 名片生成器 ===")

# 获取用户输入
name = input("请输入姓名：")
job = input("请输入职业：")
email = input("请输入邮箱：")

# 输出名片（\n 表示换行）
print("\n========== 个人名片 ==========")
print("姓名：", name)
print("职业：", job)
print("邮箱：", email)
print("==============================")
