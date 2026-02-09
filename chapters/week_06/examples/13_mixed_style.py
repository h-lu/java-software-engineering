"""
示例：LBYL + EAFP 混合风格

本例演示：不同场景使用不同的风格
运行方式：python3 chapters/week_06/examples/13_mixed_style.py
预期输出：
  - 演示两个函数：LBYL 风格的用户输入、EAFP 风格的字典访问
"""

def get_positive_integer(prompt):
    """
    获取一个正整数（LBYL 风格）

    适合场景：用户输入验证（失败频繁，检查开销小）
    """
    while True:  # 用 Week 02 学的 while 循环
        value_str = input(prompt)

        # 先检查输入是否合法（LBYL）
        if not value_str.isdigit():
            print("错误：请输入一个正整数")
            continue

        value = int(value_str)

        if value <= 0:
            print("错误：请输入大于 0 的整数")
            continue

        return value


def get_dictionary_value(dictionary, key, default=None):
    """
    获取字典的值（EAFP 风格）

    适合场景：字典访问（检查开销大，失败开销小）
    """
    try:
        return dictionary[key]
    except KeyError:
        return default


# 测试代码
if __name__ == "__main__":
    print("=== 测试 get_positive_integer（LBYL）===")
    age = get_positive_integer("请输入你的年龄：")
    print(f"你的年龄：{age}")

    print("\n=== 测试 get_dictionary_value（EAFP）===")
    scores = {"小北": 85, "阿码": 90}

    score = get_dictionary_value(scores, "小北", 0)
    print(f"小北的分数：{score}")

    missing_score = get_dictionary_value(scores, "老潘", 0)
    print(f"老潘的分数：{missing_score}")

# 说明：
# 没有绝对的好坏，只有"适不适合"：
#
# | 场景          | 推荐风格 | 原因                            |
# |---------------|----------|---------------------------------|
# | 访问字典/列表 | EAFP     | 检查的开销大，失败的开销小      |
# | 文件操作      | EAFP     | 避免竞态条件                    |
# | 用户输入验证  | LBYL     | 失败频繁，异常开销大            |
# | 数组索引      | EAFP     | IndexError 很轻量               |
# | 网络请求      | LBYL     | 网络失败的开销很大（超时）      |
