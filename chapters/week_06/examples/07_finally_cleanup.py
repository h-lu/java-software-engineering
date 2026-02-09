"""
示例：finally 用于资源清理

本例演示：finally 块无论是否出错都会执行，用于资源释放
运行方式：python3 chapters/week_06/examples/07_finally_cleanup.py
预期输出：
  - 文件存在：输出文件内容，然后"文件已关闭"
  - 文件不存在：输出"文件不存在"，然后"文件已关闭"
"""

import tempfile
import os

print("=== 示例 1：用 finally 确保文件关闭 ===")

file = None
try:
    # 尝试打开一个可能不存在的文件
    file = open("test_data.txt", "r", encoding="utf-8")
    content = file.read()
    print(f"文件内容：{content}")

except FileNotFoundError:
    print("文件不存在")

finally:
    # 无论是否出错，都会执行
    if file:
        file.close()
        print("文件已关闭")

print("\n=== 示例 2：用 finally 清理临时文件 ===")

temp_file = None
try:
    # 创建一个临时文件
    temp_file = tempfile.NamedTemporaryFile(mode="w", delete=False)
    temp_file.write("临时数据\n")
    temp_file.close()
    print(f"临时文件已创建：{temp_file.name}")

    # 模拟一个可能出错的操作
    user_input = input("输入 1 继续清理，输入 2 模拟错误：")
    if user_input == "2":
        raise ValueError("模拟一个错误")

    print("操作成功完成")

except ValueError as e:
    print(f"操作失败：{e}")

finally:
    # 无论操作是否成功，都删除临时文件
    if temp_file and os.path.exists(temp_file.name):
        os.remove(temp_file.name)
        print("临时文件已清理")

# 说明：
# finally 最常见的用途是"资源释放"：
#   - 关闭文件
#   - 释放网络连接
#   - 清理临时文件
#   - 释放锁
#
# 无论 try 块是否抛出异常，finally 都会执行
