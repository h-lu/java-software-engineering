"""
示例：input() 基础用法。

展示如何获取用户输入，以及 input() 返回的是字符串。

运行方式：python3 chapters/week_01/examples/03_input.py
交互说明：按提示输入姓名和年龄，观察输出结果。
"""

# 获取用户输入
name = input("请输入你的姓名：")
print("你好，", name, "！")

# 验证 input() 返回的类型
print("\n--- 类型测试 ---")
age = input("请输入你的年龄：")
print("你输入的是：", age)
print("它的类型是：", type(age))

# 注意：即使输入数字，input() 返回的也是字符串
# 比如输入 25，得到的是 "25"（带引号的字符串），不是数字 25
