"""
records.py - 业务逻辑模块

职责：负责学习记录的业务逻辑

功能：
- add_record()：添加学习记录
- show_records()：查看所有学习记录
- show_stats()：统计学习天数

运行方式（测试）：
  cd chapters/week_07/examples/07_pyhelper_modular
  python3 records.py

导入方式：
  from records import add_record, show_records, show_stats
"""

# 从同目录的 input_handler 模块导入函数
from input_handler import get_date, get_content


def add_record(learning_log):
    """添加学习记录（带输入校验）"""
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
    """查看所有学习记录"""
    if not learning_log:
        print("还没有学习记录哦，去添加一些吧！")
        return

    print("\n=== 学习记录 ===")

    for date in sorted(learning_log.keys()):
        print(f"{date}: {learning_log[date]}")


def show_stats(learning_log):
    """统计学习天数"""
    count = len(learning_log)
    print(f"\n你已经学习了 {count} 天")

    if count >= 5:
        print("太棒了！坚持就是胜利！")
    elif count >= 2:
        print("不错的开始，继续加油！")
    else:
        print("万事开头难，加油！")


# =====================
# 测试代码
# =====================

if __name__ == "__main__":
    print("=== 测试 records 模块 ===")

    learning_log = {}
    add_record(learning_log)
    show_records(learning_log)
    show_stats(learning_log)

    print("\n✓ 测试完成！")
