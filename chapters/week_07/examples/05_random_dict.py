"""
示例：用 random 操作字典

本例演示如何结合 random 模块和字典（Week 04 的知识）
实现一个"随机抽取器"

运行方式：python3 chapters/week_07/examples/05_random_dict.py
预期输出：随机抽取学生、随机分配任务、随机出题
"""

import random


def demo_lucky_student():
    """演示：随机抽取幸运同学"""
    print("=== 随机抽取幸运同学 ===")

    students = ["小北", "阿码", "老潘", "小红", "小明"]
    scores = {
        "小北": 85,
        "阿码": 90,
        "老潘": 88,
        "小红": 92,
        "小明": 87,
    }

    # 从列表中随机选一个
    lucky_student = random.choice(students)
    lucky_score = scores[lucky_student]

    print(f"🎉 幸运同学：{lucky_student}")
    print(f"📊 他的分数：{lucky_score}")


def demo_task_assignment():
    """演示：随机分配任务"""
    print("\n=== 随机分配任务 ===")

    tasks = ["写测试", "写文档", "Code Review", "修复 Bug", "优化性能"]
    people = ["小北", "阿码", "老潘"]

    print("任务分配结果：")
    for person in people:
        task = random.choice(tasks)
        print(f"  {person} → {task}")


def demo_random_question():
    """演示：随机出题（从题库中抽取）"""
    print("\n=== 随机出题 ===")

    question_bank = {
        "Q1": "Python 中如何定义函数？",
        "Q2": "列表和字典的区别是什么？",
        "Q3": "try/except 的作用是什么？",
        "Q4": "__name__ 守卫的作用是什么？",
        "Q5": "相对导入和绝对导入的区别？",
    }

    # 随机抽取 3 道题
    question_ids = random.sample(list(question_bank.keys()), 3)

    print("今天的练习题：")
    for qid in question_ids:
        print(f"  {qid}. {question_bank[qid]}")


def demo_random_groups():
    """演示：随机分组"""
    print("\n=== 随机分组 ===")

    students = ["小北", "阿码", "老潘", "小红", "小明", "小李", "小王", "小张"]

    # 先打乱顺序
    random.shuffle(students)

    # 分成 2 组，每组 4 人
    group_size = 4
    groups = [students[i : i + group_size] for i in range(0, len(students), group_size)]

    print("分组结果：")
    for i, group in enumerate(groups, 1):
        print(f"  第 {i} 组：{', '.join(group)}")


def demo_random_stats():
    """演示：用 random 模拟统计数据"""
    print("\n=== 模拟统计数据 ===")

    # 模拟 100 次掷骰子
    outcomes = {1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0}

    for _ in range(100):
        result = random.randint(1, 6)
        outcomes[result] += 1

    print("掷骰子 100 次的结果：")
    for face, count in sorted(outcomes.items()):
        bar = "█" * (count // 2)
        print(f"  {face} 点：{count:2d} 次 {bar}")


if __name__ == "__main__":
    demo_lucky_student()
    demo_task_assignment()
    demo_random_question()
    demo_random_groups()
    demo_random_stats()

    print("\n=== 本用到的 random 函数 ===")
    print("random.choice(list)：从列表中随机选一个元素")
    print("random.sample(list, n)：从列表中随机选 n 个不重复的元素")
    print("random.shuffle(list)：打乱列表顺序")
    print("random.randint(a, b)：生成 a 到 b 之间的随机整数")
