"""
示例：输入校验函数集

本例演示：把输入校验逻辑封装成函数，并使用 raise
运行方式：python3 chapters/week_06/examples/16_input_validation_functions.py
预期输出：
  - 有效年龄：输出年龄
  - 无效年龄：抛出 ValueError 并被捕获
"""

def get_positive_integer(prompt):
    """
    获取一个正整数（会抛出 ValueError）

    返回：正整数
    抛出：ValueError（输入非正整数时）
    """
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


def get_age():
    """
    获取年龄（18-120 之间）

    返回：合法的年龄
    抛出：ValueError（年龄不在 18-120 之间时）
    """
    age = get_positive_integer("请输入你的年龄：")

    # 额外的业务逻辑检查
    if age < 18:
        raise ValueError("年龄必须大于等于 18")

    if age > 120:
        raise ValueError("年龄超出合理范围")

    return age


# 测试代码
if __name__ == "__main__":
    try:
        age = get_age()
        print(f"你的年龄：{age}")

        # 后续业务逻辑...
        print("注册成功！")

    except ValueError as e:
        print(f"输入错误：{e}")
        print("注册失败")

# 说明：
# 1. get_positive_integer：基础校验（必须是正整数）
# 2. get_age：业务逻辑校验（18-120 之间）
# 3. 调用者可以捕获异常，决定如何处理
#
# 测试建议：
# 输入"20" → 输出"你的年龄：20" 和 "注册成功！"
# 输入"15" → 输出"输入错误：年龄必须大于等于 18" 和 "注册失败"
# 输入"200" → 输出"输入错误：年龄超出合理范围" 和 "注册失败"
