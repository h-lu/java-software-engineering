"""
示例：with 和 finally 一起使用

本例演示：with 语句和 finally 可以配合使用
运行方式：python3 chapters/week_06/examples/08_with_and_finally.py
预期输出：
  - 文件存在：输出文件内容，然后"finally 执行了"
  - 文件不存在：输出"文件不存在"，然后"finally 执行了"
"""

print("=== with 和 finally 一起使用 ===")

try:
    # with 语句会自动管理文件的打开和关闭
    with open("test_data.txt", "r", encoding="utf-8") as file:
        content = file.read()
        print(f"文件内容：\n{content}")

except FileNotFoundError:
    print("文件不存在")

finally:
    # 无论是否出错，都会执行
    # 注意：with 已经自动关闭文件了，这里只是演示 finally 的执行时机
    print("finally 执行了")

print("\n程序结束")

# 说明：
# 1. with 语句会在退出块时自动关闭文件
# 2. finally 会在这之后执行
# 3. 两者的作用不同：
#    - with：自动管理资源的生命周期（简化代码）
#    - finally：无论是否出错都要执行的清理逻辑（更灵活）
#
# 大多数情况下，用 with 就够了
# 但如果需要"读取文件 + 记录日志 + 清理临时文件"等复杂场景，finally 更灵活
