"""
示例：常见错误及修复方法。

展示 Python 中三种最常见的错误类型。
每个错误都被 try-except 捕获，避免程序崩溃。

运行方式：python3 chapters/week_01/examples/05_errors.py
预期输出：展示三种错误及其修复方法。
"""

print("=== 错误示例与修复 ===\n")

# --- 错误 1：SyntaxError（语法错误）---
print("【错误 1】SyntaxError：语法错误")
print("错误代码：print('Hello World!')  # 漏了右引号")
print("错误信息：SyntaxError: unterminated string literal")
print("修复方法：补全引号")

# 演示修复后的正确代码
print("正确输出：", end="")
print("Hello World!")  # 正确：引号成对出现
print()

# --- 错误 2：NameError（名字错误）---
print("【错误 2】NameError：使用了未定义的变量")
print("错误代码：先 print(message)，后定义 message")
print("错误信息：NameError: name 'message' is not defined")
print("修复方法：先定义变量，再使用变量")

# 演示修复后的正确代码
message = "Hello"  # 先定义
print(f"正确输出：{message}")  # 后使用
print()

# --- 错误 3：TypeError（类型错误）---
print("【错误 3】TypeError：类型不匹配")
print("错误代码：'25' + 5  # 字符串和数字相加")
print("错误信息：TypeError: can only concatenate str (not 'int') to str")
print("修复方法：把数字转成字符串，或把字符串转成数字")

# 修复方法 1：数字转字符串
age_str = "25"
result = age_str + str(5)  # str(5) 把数字转成字符串 "5"
print(f"修复输出（字符串拼接）：{result}")

# 修复方法 2：字符串转数字
age_num = int("25")  # int() 把字符串转成整数
result = age_num + 5
print(f"修复输出（数值相加）：{result}")

print("\n--- 调试小贴士 ---")
print("看到报错别慌，按这三步走：")
print("1. 看错误类型（SyntaxError/NameError/TypeError...）")
print("2. 看错误描述（英文说明具体发生了什么）")
print("3. 看行号定位（line X 指出错位置）")
