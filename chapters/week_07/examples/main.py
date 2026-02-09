"""
main.py - 主入口

本模块是 PyHelper 的主入口，负责：
1. 打印欢迎信息和菜单
2. 获取用户选择并调用相应功能
3. 协调其他模块完成功能

运行方式：
  cd chapters/week_07/examples
  python3 main.py

预期输出：
  已加载 N 条学习记录
  ======================================
    欢迎使用 PyHelper！
  ======================================
  [菜单和交互界面]

依赖：
  - from storage import load_learning_log, save_learning_log
  - from input_handler import get_choice
  - from records import add_record, show_records, show_stats
  - from encouragement import show_advice
"""

from storage import load_learning_log, save_learning_log
from input_handler import get_choice
from records import add_record, show_records, show_stats
from encouragement import show_advice


def print_welcome():
    """打印欢迎信息"""
    print("=" * 40)
    print("  欢迎使用 PyHelper！")
    print("=" * 40)
    print()


def print_menu():
    """打印菜单"""
    print("\n请选择功能：")
    print("1. 添加学习记录")
    print("2. 查看所有记录")
    print("3. 统计学习天数")
    print("4. 获取学习建议")
    print("5. 退出并保存")


def main():
    """
    主函数——把所有功能串联起来

    Week 07 改进：
    1. 不再包含所有功能的实现代码
    2. 通过 import 导入其他模块
    3. 主函数只负责协调和流程控制
    """
    # 启动时加载学习记录（从 storage 模块）
    learning_log = load_learning_log()

    print_welcome()

    while True:
        print_menu()

        # 获取用户选择（从 input_handler 模块）
        choice = get_choice(min_choice=1, max_choice=5)

        if choice == 1:
            # 添加记录（调用 records 模块）
            add_record(learning_log)
        elif choice == 2:
            # 查看记录（调用 records 模块）
            show_records(learning_log)
        elif choice == 3:
            # 统计天数（调用 records 模块）
            show_stats(learning_log)
        elif choice == 4:
            # 获取建议（调用 encouragement 模块）
            show_advice()
        elif choice == 5:
            # 退出前保存（调用 storage 模块）
            save_learning_log(learning_log)
            print("\n再见！祝你学习愉快！")
            break

        print("\n" + "-" * 40)


# =====================
# 启动程序
# =====================

if __name__ == "__main__":
    main()
