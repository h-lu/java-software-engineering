"""
示例：带返回值的函数

本例演示 return 和 print 的区别，以及为什么函数应该返回结果而不是直接打印

运行方式：python3 chapters/week_03/examples/04_with_return.py
预期输出：演示返回值的可复用性
"""


# --- 问题演示：直接打印的函数 ---

def km_to_miles_with_print(km):
    """直接打印结果的函数——结果只能看，不能复用"""
    miles = km * 0.621371
    print(f"{km} 公里 = {miles} 英里")


print("=== print 版本 ===")
km_to_miles_with_print(10)

# 问题：想把结果存起来？做不到
result = km_to_miles_with_print(10)
print(f"result 的值：{result}")  # None！
# print(f"result * 2 = {result * 2}")  # 会报错！不能对 None 做运算


# --- 正确做法：用 return 返回结果 ---

def km_to_miles(km):
    """
    公里转英里

    参数：
        km: 要转换的公里数

    返回：
        转换后的英里数
    """
    miles = km * 0.621371
    return miles  # 返回计算结果


print("\n=== return 版本 ===")

# 现在可以把结果存进变量
result = km_to_miles(10)
print(f"10 公里 = {result} 英里")

# 可以用结果做运算
double_result = result * 2
print(f"两倍距离：{double_result} 英里")

# 可以传给另一个函数
print(f"10 公里是 {km_to_miles(10)} 英里")

# 可以在表达式中使用
total = km_to_miles(5) + km_to_miles(10)
print(f"5 公里 + 10 公里 = {total} 英里")


# --- return 和 print 的区别 ---

def add_with_print(a, b):
    """print 版本——只能看，不能复用"""
    print(a + b)


def add_with_return(a, b):
    """return 版本——可以复用"""
    return a + b


print("\n=== 区别演示 ===")
print("print 版本：")
add_with_print(3, 5)  # 屏幕显示 8，但程序拿不到这个值

# result = add_with_print(3, 5) * 2  # 会报错！因为返回的是 None

print("\nreturn 版本：")
result = add_with_return(3, 5) * 2
print(f"result = {result}")  # result = 16


# --- 常见错误 ---

def km_to_miles_buggy(km):
    """忘记写 return 的函数"""
    miles = km * 0.621371
    # 忘记写 return！Python 会默认返回 None


print("\n=== 常见错误演示 ===")
buggy_result = km_to_miles_buggy(10)
print(f"buggy_result = {buggy_result}")  # None！


# --- return 的特性 ---

def demo_return(km):
    """演示 return 会结束函数"""
    miles = km * 0.621371
    return miles
    print("这行永远不会执行")  # 死代码（unreachable code）


print("\n=== return 的特性 ===")
demo_return(10)  # 后面的 print 不会执行


# 总结：
# - print 是"给人看"——把信息输出到屏幕，但程序拿不到这个值
# - return 是"给程序用"——把值返回给调用者，可以在代码里继续用
# - 函数执行到 return 就结束了，后面的代码不会再运行
# - 如果忘了写 return，Python 会默认返回 None
