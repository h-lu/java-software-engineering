"""
示例：定义你的第一个函数

本例演示如何定义和调用函数，以及"定义"和"调用"的区别

运行方式：python3 chapters/week_03/examples/02_first_function.py
预期输出：运行一次换算
"""

# 定义一个函数——给代码起个名字
def km_to_miles():
    """公里转英里的函数"""
    km = float(input("输入公里数："))
    miles = km * 0.621371
    print(f"{km} 公里 = {miles} 英里")


# 调用这个函数——用名字来执行它
print("第一次调用：")
km_to_miles()

print("\n第二次调用：")
km_to_miles()

# 关键点：
# 1. def 是"定义"（define）的缩写
# 2. km_to_miles 是函数名
# 3. () 是函数的标志，告诉 Python"这是一个函数"
# 4. 定义函数不会执行代码，必须调用才会执行
# 5. 调用时函数名后必须加括号：km_to_miles()


# --- 常见错误演示 ---

# 错误 1：只定义不调用——什么都不会发生
# def km_to_miles():
#     km = float(input("输入公里数："))
#     miles = km * 0.621371
#     print(f"{km} 公里 = {miles} 英里")
# （上面这段代码运行后不会有任何输出，因为没有调用）

# 错误 2：调用时忘记括号——不会执行
# km_to_miles  # 这只是函数本身，不会执行
# km_to_miles()  # 正确！加上括号才是调用
