"""
示例：限制重试次数

本例演示：用 for 循环限制重试次数，超过次数后抛出异常
运行方式：python3 chapters/week_06/examples/19_retry_with_limit.py
预期输出：
  - 前两次错误：显示剩余尝试次数
  - 第三次错误：抛出异常 "输入错误次数过多，超过 3 次"
"""


def get_positive_integer(prompt, max_attempts=3):
    """
    获取一个正整数（最多重试 max_attempts 次）

    max_attempts: 最大尝试次数（默认 3 次）

    返回：正整数
    抛出：ValueError（超过最大尝试次数时）
    """
    for attempt in range(max_attempts):
        value_str = input(prompt)

        if not value_str.isdigit():
            remaining_attempts = max_attempts - attempt - 1
            print(f"错误：请输入一个正整数（剩余尝试次数：{remaining_attempts}）")
            continue

        value = int(value_str)

        if value <= 0:
            remaining_attempts = max_attempts - attempt - 1
            print(f"错误：请输入大于 0 的整数（剩余尝试次数：{remaining_attempts}）")
            continue

        return value

    # 重试次数用完，抛出异常
    raise ValueError(f"输入错误次数过多，超过 {max_attempts} 次")


# 测试代码
if __name__ == "__main__":
    print("=== 测试：限制重试次数 ===\n")

    try:
        count = get_positive_integer("请输入购买数量：", max_attempts=3)
        print(f"购买数量：{count}")

    except ValueError as e:
        print(f"输入失败：{e}")

# 说明：
# 1. 用 for 循环限制尝试次数（range(max_attempts)）
# 2. 每次失败后显示剩余次数（max_attempts - attempt - 1）
# 3. 超过次数后抛出异常，让调用者知道"真的失败了"
#
# 测试建议：
# 输入三次错误值（如 abc），第四次输入正确值
# 观察剩余尝试次数的变化
#
# 场景：
# - 如果是用户手动输入，通常给无限重试（while True）
# - 如果是 API 调用或自动化场景，通常限制重试次数（避免死循环）
