"""
示例：用单个变量存储多个成绩的问题。

本例演示为什么需要列表——当有多个成绩时，用单个变量会导致代码重复、难以维护。

运行方式：python3 chapters/week_04/examples/01_scores_before.py
预期输出：显示 5 个学生的成绩和平均分
"""

# ========== 问题：用单独的变量存储多个成绩 ==========

print("=== 用单独变量存储成绩 ===")

# 每个学生一个变量
score1 = 85
score2 = 92
score3 = 78
score4 = 90
score5 = 88

print(f"学生1: {score1}")
print(f"学生2: {score2}")
print(f"学生3: {score3}")
print(f"学生4: {score4}")
print(f"学生5: {score5}")

# 计算平均分——需要写所有的变量名
average = (score1 + score2 + score3 + score4 + score5) / 5
print(f"\n平均分：{average}")

# 问题1：如果学生变成 50 个怎么办？
# 问题2：如果要打印所有成绩，得写 50 行 print
# 问题3：如果想找出最高分，得写 score1 > score2 and score1 > score3 ...

print("\n=== 这段代码的问题 ===")
print("1. 重复代码太多：每个学生需要一行变量定义")
print("2. 难以维护：改一个输出格式要改 N 处")
print("3. 无法批量处理：无法用循环遍历所有成绩")
print("4. 扩展性差：学生数量改变时需要大量修改")

print("\n=== 更好的方式 ===")
print("下周我们将学习'列表'，用一个变量存储所有成绩！")
