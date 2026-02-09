"""
示例：单位换算器 v1——没有函数，一团混乱

本例演示没有函数时代码的样子：所有逻辑混在一起，难以维护

运行方式：python3 chapters/week_03/examples/01_converter_before.py
预期输出：根据用户选择执行不同的单位换算
"""

# 单位换算器 v1——没有函数，一团混乱
print("单位换算器")
print("1. 公里 → 英里")
print("2. 公斤 → 磅")
print("3. 摄氏度 → 华氏度")

choice = input("选择换算类型（1-3）：")

if choice == "1":
    km = float(input("输入公里数："))
    miles = km * 0.621371
    print(f"{km} 公里 = {miles} 英里")
elif choice == "2":
    kg = float(input("输入公斤数："))
    pounds = kg * 2.20462
    print(f"{kg} 公斤 = {pounds} 磅")
elif choice == "3":
    celsius = float(input("输入摄氏度："))
    fahrenheit = celsius * 9/5 + 32
    print(f"{celsius}°C = {fahrenheit}°F")

# 问题：
# 1. 换算公式、输入提示、输出格式全都混在一起
# 2. 如果想加一个新的换算类型，得在 if/elif 里再加一长段代码
# 3. 改一个地方（比如输出格式），得改三个地方
# 4. 代码无法复用——换算公式被"锁"在各自的 if 分支里
