"""
示例：EAFP 风格访问字典

本例演示：EAFP（Easier to Ask Forgiveness than Permission）风格
运行方式：python3 chapters/week_06/examples/10_eafp_dict.py
预期输出：
  - 键存在：输出分数
  - 键不存在：输出"找不到这个学生"
"""

scores = {"小北": 85, "阿码": 90, "老潘": 95}

print("=== EAFP 风格（推荐）===")

# EAFP：先尝试，失败了再处理
try:
    name = input("请输入学生姓名：")
    score = scores[name]
    print(f"{name} 的分数：{score}")

except KeyError:
    print("找不到这个学生")

print("\n=== 对比：LBYL 风格（不推荐）===")

# LBYL：先检查，确保没问题再执行
if "小北" in scores:
    print(f"小北的分数：{scores['小北']}")
else:
    print("找不到这个学生")

# 说明：
# EAFP 更 Pythonic，因为：
# 1. 代码更简洁（不需要先检查）
# 2. 避免竞态条件（检查和访问之间不会有时间差）
# 3. 字典的访问操作很快，失败的开销也不大
#
# LBYL 的问题：
# 1. 需要两次操作（检查 + 访问）
# 2. "小北" in scores 需要遍历字典，scores["小北"] 又要遍历一次

# 测试建议：
# 输入"小北" → 输出分数
# 输入"小明" → 输出"找不到这个学生"
