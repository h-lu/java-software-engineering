"""
示例：列表的创建和基本访问。

本例演示如何创建列表、通过索引访问元素、计算列表长度和总和。

运行方式：python3 chapters/week_04/examples/02_list_basics.py
预期输出：显示列表的创建、索引访问、sum/len 函数的使用
"""

# ========== 创建列表 ==========

print("=== 创建列表 ===")

# 用方括号 [] 创建列表，元素用逗号分隔
scores = [85, 92, 78, 90, 88]

print(f"成绩列表：{scores}")
print(f"列表类型：{type(scores)}")

# ========== 索引访问 ==========

print("\n=== 索引访问 ===")

# 索引从 0 开始
print(f"第一个成绩（索引0）：{scores[0]}")   # 85
print(f"第二个成绩（索引1）：{scores[1]}")   # 92
print(f"第三个成绩（索引2）：{scores[2]}")   # 78

# 负数索引：从末尾开始计数
print(f"最后一个成绩（索引-1）：{scores[-1]}")   # 88
print(f"倒数第二个（索引-2）：{scores[-2]}")      # 90

# ========== 常见错误：索引越界 ==========

print("\n=== 索引越界错误 ===")
try:
    print(scores[5])  # 只有 5 个元素（索引 0-4），访问索引 5 会报错
except IndexError as e:
    print(f"错误：{e}")
    print("提示：5 个元素的索引是 0, 1, 2, 3, 4，不存在索引 5")

# ========== 列表操作 ==========

print("\n=== 列表操作 ===")

# len()：获取列表长度（元素个数）
print(f"学生人数：{len(scores)}")

# sum()：计算列表所有元素的总和
total = sum(scores)
print(f"成绩总和：{total}")

# 计算平均分
average = sum(scores) / len(scores)
print(f"平均分：{average}")

# ========== 检查元素是否存在 ==========

print("\n=== 检查元素是否存在 ===")

# 用 in 关键字检查某个值是否在列表中
print(f"92 在列表中吗？{92 in scores}")      # True
print(f"100 在列表中吗？{100 in scores}")    # False

# ========== 空列表 ==========

print("\n=== 空列表 ===")

empty_list = []
print(f"空列表：{empty_list}")
print(f"空列表长度：{len(empty_list)}")
