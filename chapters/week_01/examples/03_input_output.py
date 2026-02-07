#!/usr/bin/env python3
"""
示例 03: 用户输入和输出格式化

学习目标:
- 使用 input() 获取用户输入
- 使用 f-string（格式化字符串）组合变量和文本
- 了解 input() 返回的总是字符串

运行方法:
    python3 03_input_output.py

预期交互:
    请输入你的名字: Alice
    请输入你的年龄: 20
    你好, Alice! 明年你就 21 岁了。
"""

from __future__ import annotations


# input() 函数暂停程序，等待用户输入
# 用户输入的内容会被当作字符串返回
name = input("请输入你的名字: ")

# f-string 格式化：在字符串前加 f，用 {} 包裹变量
# 这是 Python 3.6+ 推荐的方式
print(f"你好, {name}!")

# 获取数字输入时需要注意：input() 返回的是字符串
age_str = input("请输入你的年龄: ")
print(f"输入的类型是: {type(age_str)}")

# 需要用 int() 将字符串转换为整数
age = int(age_str)
next_age = age + 1

# 组合多个变量
print(f"{name}, 明年你就 {next_age} 岁了。")

# 另一种格式化方式：字符串的 format() 方法
print("{} 今年 {} 岁".format(name, age))

# 最简单的字符串拼接（不推荐复杂情况使用）
print("欢迎, " + name + "!")
