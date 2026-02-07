#!/usr/bin/env python3
"""
示例 05: 常见错误示例（带注释说明）

学习目标:
- 认识初学者常犯的错误
- 学会阅读错误信息（Traceback）
- 了解如何避免这些错误

运行方法:
    取消注释你想测试的错误示例，然后运行
    python3 05_common_errors.py

重要提示:
    默认所有错误都被注释掉了，以免程序崩溃。
    学习时请一次取消注释一个示例，观察错误信息。
"""

from __future__ import annotations


# ========== 错误 1: 缩进错误 (IndentationError) ==========
# Python 使用缩进来表示代码块，必须保持一致

# 错误示例（取消下面的注释来测试）:
# def say_hello():
# print("Hello")  # 错误：没有缩进

# 正确写法:
def say_hello():
    print("Hello")  # 使用 4 个空格缩进


# ========== 错误 2: 名称错误 (NameError) ==========
# 使用了未定义的变量

# 错误示例:
# print(undefined_variable)  # 错误：变量未定义

# 正确写法:
greeting = "Hello"
print(greeting)


# ========== 错误 3: 类型错误 (TypeError) ==========
# 对不兼容的类型进行操作

# 错误示例:
# age = 20
# print("年龄是: " + age)  # 错误：不能直接将字符串和数字相加

# 正确写法 1: 使用 str() 转换
age = 20
print("年龄是: " + str(age))

# 正确写法 2: 使用 f-string（推荐）
print(f"年龄是: {age}")


# ========== 错误 4: 语法错误 (SyntaxError) ==========
# 违反了 Python 的语法规则

# 错误示例:
# if age > 18  # 错误：缺少冒号
#     print("成年")

# 正确写法:
if age > 18:
    print("成年")


# ========== 错误 5: 索引越界 (IndexError) ==========
# 访问了不存在的索引位置

name = "Python"
# 错误示例:
# print(name[10])  # 错误：字符串只有 6 个字符，索引 10 不存在

# 正确写法: 使用 len() 检查长度，或使用安全的索引
print(f"字符串长度: {len(name)}")
print(f"最后一个字符: {name[-1]}")  # 负数索引从末尾开始


# ========== 错误 6: 字符串不可变 (TypeError) ==========
# 试图修改字符串中的某个字符

# 错误示例:
# name = "Python"
# name[0] = "C"  # 错误：字符串是不可变的

# 正确写法: 创建新字符串
name = "Python"
new_name = "C" + name[1:]
print(f"原字符串: {name}")
print(f"新字符串: {new_name}")


# ========== 错误 7: 输入转换错误 (ValueError) ==========
# 将不合适的字符串转换为数字

# 错误示例:
# user_input = "abc"
# number = int(user_input)  # 错误："abc" 不能转换为整数

# 正确写法: 确保输入可以转换
user_input = "123"
if user_input.isdigit():
    number = int(user_input)
    print(f"转换成功: {number}")
else:
    print(f"错误: '{user_input}' 不是有效的数字")


# 总结：遇到错误时的排查步骤
print("\n=== 错误排查小贴士 ===")
print("1. 从下往上阅读错误信息")
print("2. 找到 File 和 Line 定位错误位置")
print("3. 查看错误类型和描述")
print("4. 检查该行代码及其附近代码")
