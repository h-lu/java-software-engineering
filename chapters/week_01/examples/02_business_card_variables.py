"""
示例：使用变量的名片（第 2 节）。

用变量存储名片信息，实现"可配置"的名片。
同时展示变量名大小写敏感的问题。

运行方式：python3 chapters/week_01/examples/02_business_card_variables.py
预期输出：
    ========== 个人名片 ==========
    姓名：李小明
    职业：Python 学习者
    邮箱：xiaoming@example.com
    ==============================
"""

# 用变量存储名片信息
name = "李小明"
job = "Python 学习者"
email = "xiaoming@example.com"

print("========== 个人名片 ==========")
print("姓名：", name)
print("职业：", job)
print("邮箱：", email)
print("==============================")

# --- 常见错误示例（已注释掉）---
# 错误：变量名大小写不匹配
# name = "李小明"
# print("姓名：", Name)  # NameError: name 'Name' is not defined
# 原因：Python 区分大小写，name 和 Name 是两个不同的名字

# 修复方法：统一使用小写
# name = "李小明"
# print("姓名：", name)  # 正确
