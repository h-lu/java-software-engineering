"""
示例：EAFP 风格文件操作

本例演示：EAFP 风格的文件操作（推荐做法）
运行方式：python3 chapters/week_06/examples/11_eafp_file.py
预期输出：
  - 文件存在：输出文件内容
  - 文件不存在：输出"文件不存在"
"""

print("=== EAFP 风格（推荐）===")

# EAFP：直接尝试打开文件，失败了再处理
try:
    with open("data.txt", "r", encoding="utf-8") as file:
        content = file.read()
        print(f"文件内容：\n{content}")

except FileNotFoundError:
    print("文件不存在")

print("\n=== 对比：LBYL 风格（不推荐）===")

import os

# LBYL：先检查文件是否存在，再打开
if os.path.exists("data.txt"):
    with open("data.txt", "r", encoding="utf-8") as file:
        content = file.read()
        print(f"文件内容：\n{content}")
else:
    print("文件不存在")

# 说明：
# EAFP 的优势：
# 1. 避免竞态条件（检查和打开之间文件可能被删除）
# 2. 代码更简洁（不需要先检查）
# 3. 文件操作的开销主要在 I/O，检查是否存在并没有节省多少时间
#
# 竞态条件示例：
# 1. LBYL：检查文件存在 → 文件被删除 → 尝试打开（失败！）
# 2. EAFP：直接尝试打开（要么成功，要么失败，不会有中间状态）

# 测试建议：
# 1. 创建 data.txt 文件 → 输出文件内容
# 2. 删除 data.txt 文件 → 输出"文件不存在"
