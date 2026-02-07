#!/usr/bin/env python3
"""
示例 02: 变量和基本数据类型

学习目标:
- 学会创建变量（给数据起名字）
- 了解三种基本数据类型：字符串(str)、整数(int)、浮点数(float)
- 学会使用 type() 查看数据类型

运行方法:
    python3 02_variables.py

预期输出:
    用户名: Alice
    年龄: 20
    身高: 1.65 米
    <class 'str'>
    <class 'int'>
    <class 'float'>
"""

from __future__ import annotations


# 变量 = 数据的"标签"
# 格式：变量名 = 值

# 字符串(str)：文本数据，用引号包裹
username = "Alice"
print("用户名:", username)

# 整数(int)：没有小数部分的数字
age = 20
print("年龄:", age)

# 浮点数(float)：带小数点的数字
height = 1.65
print("身高:", height, "米")

# 使用 type() 查看变量的类型
print(type(username))  # <class 'str'>
print(type(age))       # <class 'int'>
print(type(height))    # <class 'float'>

# 变量可以重新赋值（更换标签指向的数据）
age = 21
print("明年年龄:", age)

# 变量名区分大小写
Age = 30  # 这是另一个不同的变量
print("Age:", Age, "age:", age)
