"""
示例：健壮的用户输入处理器

本例演示：整合所有学到的知识，写一个健壮的输入处理器
运行方式：python3 chapters/week_06/examples/18_robust_input_processor.py
预期输出：
  - 演示三个输入函数：获取年龄、获取数量、获取选择
  - 无论用户输入什么，都不会崩溃
"""


def get_positive_integer(prompt):
    """获取一个正整数"""
    while True:
        value_str = input(prompt)

        if not value_str.isdigit():
            print("错误：请输入一个正整数")
            continue

        value = int(value_str)

        if value <= 0:
            print("错误：请输入大于 0 的整数")
            continue

        return value


def get_choice(prompt, valid_choices):
    """
    获取用户选择（从有效选项中选）

    valid_choices: 有效的选项列表，如 ["快递", "自提"]
    """
    while True:
        choice = input(prompt)

        if choice in valid_choices:
            return choice

        print(f"错误：请选择 {', '.join(valid_choices)} 之一")


def get_age(min_age=18, max_age=120):
    """获取年龄（在指定范围内）"""
    while True:
        age_str = input(f"请输入你的年龄（{min_age}-{max_age}）：")

        if not age_str.isdigit():
            print("错误：请输入一个整数")
            continue

        age = int(age_str)

        if age < min_age or age > max_age:
            print(f"错误：年龄必须在 {min_age}-{max_age} 之间")
            continue

        return age


# 主程序
print("=== 用户信息收集 ===\n")

name = input("请输入你的姓名：")
print(f"姓名：{name}")

age = get_age(min_age=18, max_age=120)
print(f"年龄：{age}")

count = get_positive_integer("请输入购买数量：")
print(f"购买数量：{count}")

choice = get_choice("请选择配送方式（快递/自提）：", ["快递", "自提"])
print(f"配送方式：{choice}")

print("\n=== 信息收集完成 ===")

# 说明：
# 1. 所有输入函数都用 while 循环，让用户"重试直到输入正确"
# 2. 用 LBYL 风格（先用 isdigit() 检查）
# 3. 给出清晰的错误提示
# 4. 无论用户输入什么，程序都不会崩溃
#
# 测试建议：
# 尝试输入各种非法值：
# - 姓名：任意
# - 年龄：abc、-5、200、20
# - 数量：abc、-5、0、5
# - 配送：飞机、快递
