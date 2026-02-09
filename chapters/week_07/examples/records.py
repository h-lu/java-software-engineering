"""
records.py - 业务逻辑模块

本模块负责学习记录的业务逻辑
包含以下功能：
1. add_record()：添加学习记录
2. show_records()：查看所有学习记录
3. show_stats()：统计学习天数

运行方式（测试）：
  cd chapters/week_07/examples
  python3 records.py

预期输出：
  === 测试 records 模块 ===
  [交互式添加和查看记录]

导入方式：
  from records import add_record, show_records, show_stats
"""

# 从同目录的 input_handler 模块导入函数
from input_handler import get_date, get_content


def add_record(learning_log):
    """
    添加学习记录（带输入校验）

    Week 06 改进：
    - 用 get_date() 和 get_content() 进行输入校验
    - 用户输入错误不会崩溃，会提示并让用户重试

    参数：
        learning_log (dict)：学习记录字典
    """
    date = get_date()
    content = get_content()

    if date in learning_log:
        print(f"注意：{date} 的记录已存在")
        overwrite = input("是否覆盖？(y/n)：").lower()
        if overwrite == "y":
            learning_log[date] = content
            print(f"已覆盖：{date} - {content}")
        else:
            print("取消添加")
    else:
        learning_log[date] = content
        print(f"已添加：{date} - {content}")


def show_records(learning_log):
    """
    查看所有学习记录

    Week 04 的字典遍历和 sorted()

    参数：
        learning_log (dict)：学习记录字典
    """
    if not learning_log:
        print("还没有学习记录哦，去添加一些吧！")
        return

    print("\n=== 学习记录 ===")

    # 按日期排序后遍历
    for date in sorted(learning_log.keys()):
        print(f"{date}: {learning_log[date]}")


def show_stats(learning_log):
    """
    统计学习天数

    Week 04 的 len() 和 Week 02 的 if/elif/else

    参数：
        learning_log (dict)：学习记录字典
    """
    count = len(learning_log)
    print(f"\n你已经学习了 {count} 天")

    if count >= 5:
        print("太棒了！坚持就是胜利！")
    elif count >= 2:
        print("不错的开始，继续加油！")
    else:
        print("万事开头难，加油！")


# =====================
# 测试代码（__name__ 守卫）
# =====================

if __name__ == "__main__":
    """
    测试 records 模块

    当直接运行此文件时，会执行测试代码
    当导入此模块时，不会执行测试代码
    """
    print("=== 测试 records 模块 ===")

    # 创建一个测试用的学习记录
    learning_log = {}

    # 测试添加记录
    add_record(learning_log)

    # 测试查看记录
    show_records(learning_log)

    # 测试统计
    show_stats(learning_log)

    print("\n✓ 测试完成！")
