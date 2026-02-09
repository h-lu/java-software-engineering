"""
PyHelper - Week 07 模块化版本

这是 PyHelper 的 Week 07 版本，从单文件拆分成多模块项目
在上周（Week 06）的基础上，本周学会了"分身术"——把代码拆成多个文件

本周改进：
1. 把 1500+ 行的单文件拆成 5 个模块
2. 每个模块职责清晰，易于维护
3. 模块既能被导入，也能独立运行测试
4. 使用 __name__ 守卫隔离测试代码

项目结构：
├── main.py              # 主入口（协调各模块）
├── storage.py           # 文件操作
├── input_handler.py     # 输入校验
├── encouragement.py     # 鼓励语
└── records.py           # 业务逻辑

运行方式：
  cd chapters/week_07/examples/07_pyhelper_modular
  python3 main.py

预期输出：
  一个功能完整的学习助手，支持添加、查看、统计学习记录
"""

# 导入各模块的功能
from storage import load_learning_log, save_learning_log
from input_handler import get_choice
from records import add_record, show_records, show_stats
from encouragement import show_advice


def print_welcome():
    """打印欢迎信息"""
    print("=" * 40)
    print("  欢迎使用 PyHelper！")
    print("  Week 07：模块化版本")
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
    主函数

    Week 07 改进：
    1. 不再包含所有功能的实现代码
    2. 通过 import 导入其他模块
    3. 主函数只负责协调和流程控制
    4. 代码更清晰、更易维护
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


# =====================
# Week 07 改进总结
# =====================

"""
对比 Week 06 的单文件版本，本周的改进：

1. **模块化拆分（核心改进）**
   - 上周：1500+ 行代码挤在一个文件里
   - 这周：拆成 5 个模块，每个 40-60 行，职责清晰

2. **模块职责划分**
   - main.py：主入口，协调各模块
   - storage.py：文件操作（load、save）
   - input_handler.py：输入校验（choice、date、content）
   - encouragement.py：鼓励语和建议
   - records.py：业务逻辑（add、show、stats）

3. **__name__ 守卫**
   - 每个模块都有 if __name__ == "__main__": 守卫
   - 模块既能被导入，也能独立运行测试
   - 测试代码不会在导入时自动执行

4. **模块间的导入关系**
   - main.py 导入所有模块
   - records.py 导入 input_handler.py
   - 其他模块相对独立

5. **可维护性提升**
   - 想修改"输入校验"？只打开 input_handler.py
   - 想测试"文件操作"？直接运行 storage.py
   - 想复用某个功能？复制对应模块即可

6. **用到本周知识点**
   - import 语句：import、from...import
   - 模块搜索路径：所有模块在同一目录
   - __name__ 守卫：每个模块都有
   - 重构：从单文件拆成多模块

下周预告（Week 08）：
PyHelper 将学会"体检"——为每个模块编写测试
这就是"单元测试"（unit testing）
"""
