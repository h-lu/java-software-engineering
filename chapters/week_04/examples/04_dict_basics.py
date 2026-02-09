"""
示例：字典的创建和基本操作。

本例演示如何创建字典、通过键访问值、添加和修改键值对。

运行方式：python3 chapters/week_04/examples/04_dict_basics.py
预期输出：演示字典的创建、访问、添加、修改、删除
"""

# ========== 创建字典 ==========

print("=== 创建字典 ===")

# 用花括号 {} 创建字典，键值对用冒号分隔，对之间用逗号分隔
scores = {
    "小北": 85,
    "阿码": 92,
    "老潘": 78,
    "小红": 90,
    "小明": 88
}

print(f"成绩字典：{scores}")
print(f"字典类型：{type(scores)}")

# ========== 通过键访问值 ==========

print("\n=== 通过键访问值 ===")

# 用方括号加键名访问对应的值
print(f"小北的成绩：{scores['小北']}")   # 85
print(f"阿码的成绩：{scores['阿码']}")   # 92

# ========== 常见错误：键不存在 ==========

print("\n=== 键不存在错误 ===")

try:
    print(scores["小李"])  # 字典里没有"小李"这个键
except KeyError as e:
    print(f"错误：{e}")
    print("提示：访问不存在的键会报 KeyError")

# ========== 安全访问：get() 方法 ==========

print("\n=== 安全访问：get() 方法 ===")

# get()：键存在时返回对应的值，不存在时返回 None 或指定的默认值
print(f"get('小北')：{scores.get('小北')}")         # 85
print(f"get('小李')：{scores.get('小李')}")         # None
print(f"get('小李', 0)：{scores.get('小李', 0)}")   # 0（指定默认值）

# ========== 添加和修改键值对 ==========

print("\n=== 添加和修改键值对 ===")

scores = {"小北": 85, "阿码": 92}
print(f"原始字典：{scores}")

# 添加新键值对
scores["老潘"] = 78
print(f"添加'老潘'后：{scores}")

# 修改现有键的值
scores["小北"] = 90
print(f"修改'小北'后：{scores}")

# Python 的逻辑：键存在就修改，不存在就添加
scores["小红"] = 95  # 添加
scores["阿码"] = 88  # 修改
print(f"混合操作后：{scores}")

# ========== 删除键值对 ==========

print("\n=== 删除键值对 ===")

scores = {"小北": 85, "阿码": 92, "老潘": 78}
print(f"原始字典：{scores}")

# del：删除指定键
del scores["老潘"]
print(f"del scores['老潘'] 后：{scores}")

# pop()：删除并返回被删的值
scores = {"小北": 85, "阿码": 92, "老潘": 78}
removed = scores.pop("阿码")
print(f"pop('阿码') 返回：{removed}")
print(f"pop('阿码') 后：{scores}")

# pop() 可以指定默认值（键不存在时返回默认值）
value = scores.pop("小李", 0)
print(f"pop('小李', 0) 返回：{value}")

# ========== 检查键是否存在 ==========

print("\n=== 检查键是否存在 ===")

scores = {"小北": 85, "阿码": 92}

# 用 in 关键字检查键是否存在
print(f"'小北' 在字典中吗？{'小北' in scores}")      # True
print(f"'小李' 在字典中吗？{'小李' in scores}")      # False

# ========== 字典长度 ==========

print("\n=== 字典长度 ===")

scores = {"小北": 85, "阿码": 92, "老潘": 78}
print(f"字典：{scores}")
print(f"学生人数：{len(scores)}")

# ========== 空字典 ==========

print("\n=== 空字典 ===")

empty_dict = {}
print(f"空字典：{empty_dict}")
print(f"空字典长度：{len(empty_dict)}")
