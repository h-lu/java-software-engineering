"""
示例：美化版名片（第 4 节）。

使用 f-string 和格式说明符创建美观的名片。

运行方式：python3 chapters/week_01/examples/04_business_card_formatted.py
交互说明：按提示输入信息，查看带边框的精美名片。
"""

print("=== 名片生成器 ===\n")

# 获取用户输入
name = input("请输入姓名：")
job = input("请输入职业：")
email = input("请输入邮箱：")

# 使用 f-string 和特殊字符创建美观的边框
# ^30 表示居中对齐，占30个字符宽度
# <20 表示左对齐，占20个字符宽度
# {'═' * 30} 把字符重复30次

print(f"\n╔{'═' * 30}╗")
print(f"║{'个人名片':^30}║")
print(f"╠{'═' * 30}╣")
print(f"║  姓名：{name:<20}║")
print(f"║  职业：{job:<20}║")
print(f"║  邮箱：{email:<20}║")
print(f"╚{'═' * 30}╝")
