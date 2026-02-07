#!/usr/bin/env python3
"""
示例 04: 字符串操作基础

学习目标:
- 字符串索引和切片
- 常用字符串方法（upper, lower, strip, replace, split）
- 字符串是不可变的（immutable）

运行方法:
    python3 04_string_basics.py

预期输出:
    原始字符串: Hello Python
    第一个字符: H
    最后一个字符: n
    前5个字符: Hello
    大写: HELLO PYTHON
    小写: hello python
    去空格: Hello Python
    替换后: Hi Python
    分割结果: ['Hello', 'Python']
    字符串长度: 12
"""

from __future__ import annotations


message = "Hello Python"
print("原始字符串:", message)

# 索引：通过位置获取字符，从 0 开始计数
print("第一个字符:", message[0])    # H
print("最后一个字符:", message[-1])  # n（负数表示从末尾倒数）

# 切片：获取子字符串 [起始:结束]（结束位置不包含）
print("前5个字符:", message[0:5])  # Hello
print("从第6个开始:", message[6:])   # Python

# 常用方法（注意：字符串方法返回新字符串，原字符串不变）
print("大写:", message.upper())
print("小写:", message.lower())

# strip() 去除首尾空白字符
spaced = "  Hello Python  "
print("去空格:", spaced.strip())

# replace() 替换子串
print("替换后:", message.replace("Hello", "Hi"))

# split() 分割字符串，返回列表
print("分割结果:", message.split(" "))

# len() 获取字符串长度
print("字符串长度:", len(message))

# 验证原字符串未被修改
print("原字符串仍然是:", message)
