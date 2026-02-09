"""
示例：带参数的函数

本例演示如何用参数让函数接受不同的输入，以及 parameter 和 argument 的区别

运行方式：python3 chapters/week_03/examples/03_with_params.py
预期输出：换算多个不同的值
"""


# 带参数的函数
def km_to_miles(km):
    """
    公里转英里

    参数：
        km: 要转换的公里数

    返回：
        None（这个版本直接打印结果）
    """
    miles = km * 0.621371
    print(f"{km} 公里 = {miles} 英里")


# 调用时传入具体的值
print("直接传入数字：")
km_to_miles(10)
km_to_miles(42)
km_to_miles(100)


# 用户输入一次，可以换算多个值
print("\n基于用户输入的计算：")
user_value = float(input("输入一个数值："))
print("\n用户输入的值：")
km_to_miles(user_value)
print("\n用户输入值的 2 倍：")
km_to_miles(user_value * 2)
print("\n用户输入值的 1/2：")
km_to_miles(user_value / 2)

# 注意：这个版本的函数直接打印，所以结果不能用于计算
# 如果想在表达式里使用结果，需要用 return（见下一个示例）


# 函数可以接受多个参数
def convert_length(value, from_unit, to_unit):
    """显示长度转换信息"""
    print(f"将 {value} {from_unit} 转换为 {to_unit}")


print("\n多参数函数演示：")
convert_length(100, "公里", "英里")
convert_length(50, "千克", "磅")


# --- 关键概念说明 ---

# 参数（parameter）：定义函数时写的"占位符"（这里是 km）
# 参数（argument）：调用时传入的"实际值"（这里是 10、42、100）

# 参数也是变量，但它的作用范围只限于函数内部（作用域）
