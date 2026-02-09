"""
PyHelper - 你的命令行学习助手
Week 03：函数重构 + 菜单

在上周（Week 02）的基础上，PyHelper 学会了"把功能打包成函数"
这周我们用函数重构代码，加一个文字菜单让用户选择功能

运行方式：python3 chapters/week_03/examples/06_pyhelper.py
预期输出：一个有菜单的、模块化的学习助手
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
    print("1. 获取学习建议")
    print("2. 查看今日名言")
    print("3. 退出")


# =====================
# 输入相关函数
# =====================

def get_choice():
    """
    获取用户选择，带输入验证
    用到 Week 02 的 while 循环和 if 判断
    """
    while True:
        choice = input("\n请输入选择（1-3）：")
        if choice in ["1", "2", "3"]:
            return choice
        print("无效输入，请输入 1-3")


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
        return "太好了！推荐你今天挑战一个新概念，比如开始学习函数或者列表。"
    elif mood == "2":
        return "那就做点巩固练习吧，复习上周的变量和字符串，写几个小例子。"
    elif mood == "3":
        return "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
    else:
        return "写点巩固练习最稳妥。"


def get_quote():
    """返回今日名言"""
    return "学编程是马拉松，不是百米冲刺。找到自己的节奏最重要。"


# =====================
# 功能函数
# =====================

def show_advice():
    """功能1：显示学习建议"""
    mood = get_mood()
    advice = get_advice(mood)
    print(f"\n{advice}")
    print(get_quote())


def show_quote():
    """功能2：显示名言"""
    print(f"\n今日一句：{get_quote()}")


# =====================
# 主函数
# =====================

def main():
    """
    主函数——把所有功能串联起来
    用到 Week 02 的 while 循环和 if/elif/else
    """
    print_welcome()

    while True:
        print_menu()
        choice = get_choice()

        if choice == "1":
            show_advice()
        elif choice == "2":
            show_quote()
        elif choice == "3":
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
对比 Week 02 的代码，本周的改进：

1. **功能被拆成小函数**
   - print_welcome()：打印欢迎信息
   - get_mood()：获取用户心情
   - get_advice()：根据心情返回建议
   - 每个函数各司其职

2. **函数有返回值**
   - get_advice() 返回建议字符串，而不是直接打印
   - get_quote() 返回名言字符串
   - 这样调用者可以决定如何处理结果

3. **有菜单循环**
   - 用 while True 让用户可以持续使用不同的功能
   - 这是 Week 02 学的循环，这周用在菜单上

4. **输入验证**
   - get_choice() 会检查输入是否有效
   - 无效就让你重输

5. **代码可维护性提升**
   - 如果想加新功能（如"查看学习记录"），只需：
     1. 写一个新的函数（如 show_records()）
     2. 在 print_menu() 里加一个选项
     3. 在 main() 的 if/elif 里加一个分支

   - 改动是局部的，不会影响其他功能

本周用到的 Week 03 新知识：
- 函数定义（def 语句）
- 参数（parameter）
- 返回值（return）
- 作用域（scope）——所有变量都是局部变量

下周预告（Week 04）：
PyHelper 将学会"用字典存学习记录"
支持添加、查看、统计学习笔记
"""
