"""
示例：列表的增删改查操作。

本例演示列表的常见操作：添加、删除、修改、排序。

运行方式：python3 chapters/week_04/examples/03_list_operations.py
预期输出：演示 append、insert、remove、pop、sort 等方法
"""

# ========== 添加元素 ==========

print("=== 添加元素 ===")

scores = [85, 92, 78]
print(f"原始列表：{scores}")

# append()：在末尾添加一个元素
scores.append(90)
print(f"append(90) 后：{scores}")

# insert()：在指定位置插入元素
scores.insert(1, 88)  # 在索引 1 的位置插入 88
print(f"insert(1, 88) 后：{scores}")

# ========== 删除元素 ==========

print("\n=== 删除元素 ===")

scores = [85, 92, 78, 90, 88]
print(f"原始列表：{scores}")

# remove()：删除第一个匹配的值
scores.remove(78)
print(f"remove(78) 后：{scores}")

# pop()：弹出并返回最后一个元素
scores = [85, 92, 78, 90, 88]
last = scores.pop()
print(f"pop() 返回：{last}")
print(f"pop() 后列表：{scores}")

# pop(index)：弹出指定位置的元素
first = scores.pop(0)
print(f"pop(0) 返回：{first}")
print(f"pop(0) 后列表：{scores}")

# del：删除指定位置的元素（不返回）
scores = [85, 92, 78]
del scores[1]
print(f"del scores[1] 后：{scores}")

# ========== 修改元素 ==========

print("\n=== 修改元素 ===")

scores = [85, 92, 78]
print(f"原始列表：{scores}")

# 通过索引修改元素
scores[0] = 90
print(f"scores[0] = 90 后：{scores}")

scores[2] = 95
print(f"scores[2] = 95 后：{scores}")

# ========== 查找元素 ==========

print("\n=== 查找元素 ===")

scores = [85, 92, 78, 90, 88]
print(f"列表：{scores}")

# index()：返回元素的索引
index = scores.index(92)
print(f"92 的索引：{index}")

# 检查元素是否存在
if 100 in scores:
    print(f"100 的索引：{scores.index(100)}")
else:
    print("100 不在列表中")

# ========== 排序 ==========

print("\n=== 排序 ===")

scores = [85, 92, 78, 90, 88]
print(f"原始列表：{scores}")

# sort()：升序排序（修改原列表）
scores.sort()
print(f"sort() 升序后：{scores}")

# sort(reverse=True)：降序排序
scores.sort(reverse=True)
print(f"sort(reverse=True) 降序后：{scores}")

# sorted()：返回新列表，不修改原列表
scores = [85, 92, 78, 90, 88]
sorted_scores = sorted(scores)
print(f"\n原始列表：{scores}")
print(f"sorted() 返回：{sorted_scores}")
print(f"sorted() 不修改原列表：{scores}")

# ========== 切片 ==========

print("\n=== 切片 ===")

scores = [85, 92, 78, 90, 88, 95, 82]
print(f"完整列表：{scores}")

# 获取前 3 个元素
first_three = scores[:3]
print(f"前 3 个 [0:3]：{first_three}")

# 获取后 3 个元素
last_three = scores[-3:]
print(f"后 3 个 [-3:]：{last_three}")

# 获取中间 3 个元素（索引 2 到 4）
middle = scores[2:5]
print(f"中间 3 个 [2:5]：{middle}")
