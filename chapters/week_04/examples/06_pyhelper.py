"""
PyHelper - 你的命令行学习助手
Week 04：用字典存储学习记录

在上周（Week 03）的基础上，PyHelper 学会了"用字典存储学习记录"
这周我们添加学习记录管理功能，支持添加、查看、统计

运行方式：python3 chapters/week_04/examples/06_pyhelper.py
预期输出：一个有菜单的学习助手，能管理学习记录
"""


# =====================
# 显示相关函数
# =====================

def print_welcome():
    """打印欢迎信息"""
    print("=" * 40)
    print("  欢迎使用 PyHelper！")
    print("=" * 40)
    print()


def print_menu():
    """打印菜单"""
    print("请选择功能：")
    print("1. 添加学习记录")
    print("2. 查看所有记录")
    print("3. 统计学习天数")
    print("4. 获取学习建议")
    print("5. 退出")


# =====================
# 输入相关函数
# =====================

def get_choice():
    """
    获取用户选择，带输入验证
    用到 Week 02 的 while 循环和 if 判断
    """
    while True:
        choice = input("\n请输入选择（1-5）：")
        if choice in ["1", "2", "3", "4", "5"]:
            return choice
        print("无效输入，请输入 1-5")


def get_mood():
    """获取用户心情"""
    print("\n今天心情怎么样？")
    print("1. 充满干劲")
    print("2. 一般般")
    print("3. 有点累")
    mood = input("请输入你的心情（1-3）：")
    return mood


# =====================
# 业务逻辑函数
# =====================

def get_advice(mood):
    """
    根据心情返回建议
    注意：这个函数返回字符串，而不是直接打印
    这样调用者可以决定如何处理结果
    """
    if mood == "1":
        return "太好了！推荐你今天挑战一个新概念，比如列表或字典。"
    elif mood == "2":
        return "那就做点巩固练习吧，写几个小例子，熟悉一下遍历模式。"
    elif mood == "3":
        return "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
    else:
        return "写点巩固练习最稳妥。"


# =====================
# 学习记录管理（Week 04 新增）
# =====================

def add_record(learning_log):
    """
    添加学习记录
    learning_log: 字典，格式为 {"日期": "学习内容"}

    用到本周知识点：字典的添加和修改
    """
    date = input("请输入日期（如 02-09）：")
    content = input("请输入今天学了什么：")

    if date in learning_log:
        print(f"注意：{date} 的记录会被覆盖")

    # 字典添加/修改键值对
    learning_log[date] = content
    print(f"已添加：{date} - {content}")


def show_records(learning_log):
    """
    查看所有学习记录
    learning_log: 字典，格式为 {"日期": "学习内容"}

    用到本周知识点：遍历字典、len()、sorted()
    """
    if not learning_log:
        print("还没有学习记录哦，去添加一些吧！")
        return

    print("\n=== 学习记录 ===")

    # 按日期排序后遍历
    # 用到本周知识点：for 循环遍历字典
    for date in sorted(learning_log.keys()):
        print(f"{date}: {learning_log[date]}")


def show_stats(learning_log):
    """
    统计学习天数
    learning_log: 字典

    用到本周知识点：len() 获取字典长度
    """
    count = len(learning_log)
    print(f"\n你已经学习了 {count} 天")

    # 用到 Week 02 的 if/elif/else
    if count >= 5:
        print("太棒了！坚持就是胜利！")
    elif count >= 2:
        print("不错的开始，继续加油！")
    else:
        print("万事开头难，加油！")


# =====================
# 功能函数
# =====================

def show_advice():
    """功能1：显示学习建议"""
    mood = get_mood()
    advice = get_advice(mood)
    print(f"\n{advice}")


# =====================
# 主函数
# =====================

def main():
    """
    主函数——把所有功能串联起来

    Week 04 改进：用字典存储学习记录
    """
    # 用字典存储学习记录（Week 04 新增）
    # 键是日期，值是学习内容
    learning_log = {}

    print_welcome()

    while True:
        print_menu()
        choice = get_choice()

        if choice == "1":
            add_record(learning_log)
        elif choice == "2":
            show_records(learning_log)
        elif choice == "3":
            show_stats(learning_log)
        elif choice == "4":
            show_advice()
        elif choice == "5":
            print("\n再见！祝你学习愉快！")
            break

        print("\n" + "-" * 40)


# =====================
# 启动程序
# =====================

if __name__ == "__main__":
    main()


# =====================
# 本周改进说明
# =====================

"""
对比 Week 03 的代码，本周的改进：

1. **用字典存储学习记录**
   - learning_log = {} 是一个空字典
   - 格式：{"日期": "学习内容"}
   - 示例：{"02-09": "学会了列表和字典"}

2. **新增函数**
   - add_record(learning_log)：添加学习记录
   - show_records(learning_log)：查看所有记录
   - show_stats(learning_log)：统计学习天数

3. **字典作为参数传递**
   - 函数接收字典作为参数
   - 函数内部修改字典，外部可见
   - 这是 Python 的"可变对象"特性

4. **用到本周知识点**
   - 字典的添加：learning_log[date] = content
   - 字典的遍历：for date in learning_log.keys()
   - 字典的长度：len(learning_log)
   - 字典的排序：sorted(learning_log.keys())

5. **数据驱动设计**
   - 学习记录存储在字典中
   - 遍历代码是通用的，不管有多少条记录
   - 数据和逻辑分离

本周用到的 Week 04 新知识：
- 字典（dict）
- 字典的键值对操作
- 遍历字典
- len() 获取字典长度

下周预告（Week 05）：
PyHelper 将学会"把数据存到文件"
这样程序关闭后，学习记录不会丢失
"""
