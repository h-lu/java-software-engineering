"""
示例：变量基础用法。

展示如何定义变量、赋值和复用。

运行方式：python3 chapters/week_01/examples/02_variables.py
预期输出：
    姓名：李小明
    年龄：25
    明年年龄：26
    用户信息：李小明 - 25岁
"""

# 定义变量（把数据存到名字里）
name = "李小明"
age = 25

# 使用变量
print("姓名：", name)
print("年龄：", age)

# 变量可以参与运算
next_age = age + 1
print("明年年龄：", next_age)

# 同一个变量可以多次使用
print("用户信息：", name, "-", age, "岁")

# 好的变量名 vs 糟糕的变量名
user_name = "李小明"  # 好：一眼看懂
user_age = 25         # 好：含义清晰
# x = "李小明"        # 糟糕：看不懂存的是什么
# y = 25              # 糟糕：含义不明
