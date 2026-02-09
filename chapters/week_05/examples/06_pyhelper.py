"""
PyHelper - 你的命令行学习助手
Week 05：用文件存储学习记录（持久化）

在上周（Week 04）的基础上，PyHelper 学会了"把学习记录存到文件"
这样程序关闭后，数据不会丢失

本周改进：
1. 启动时自动加载学习记录
2. 退出时自动保存学习记录
3. 用 pathlib 管理文件路径
4. 用 with 语句确保文件正确关闭
5. 用 UTF-8 编码避免中文乱码

运行方式：python3 chapters/week_05/examples/06_pyhelper.py
预期输出：一个有持久化存储的学习助手
"""

from pathlib import Path


# =====================
# 文件操作函数（Week 05 新增）
# =====================

def get_data_file():
    """获取数据文件的路径（跨平台兼容）"""
    # 在当前目录下创建 pyhelper_data.txt
    data_file = Path.cwd() / "pyhelper_data.txt"
    return data_file


def load_learning_log():
    """
    从文件加载学习记录

    返回：字典，格式为 {"日期": "学习内容"}
    如果文件不存在，返回空字典
    """
    data_file = get_data_file()
    learning_log = {}

    # 检查文件是否存在
    if data_file.exists():
        # 用 pathlib 的 read_text() 方法读取文件
        content = data_file.read_text(encoding="utf-8")

        # 解析文件内容（每行格式：日期: 内容）
        for line in content.split("\n"):
            line = line.strip()
            if line:
                # 用 split() 分割日期和内容（最多分割 1 次）
                parts = line.split(": ", 1)
                if len(parts) == 2:
                    date, log_content = parts
                    learning_log[date] = log_content

        print(f"✓ 已加载 {len(learning_log)} 条学习记录")
    else:
        print("✓ 首次运行，将创建新的数据文件")

    return learning_log


def save_learning_log(learning_log):
    """
    保存学习记录到文件

    learning_log: 字典，格式为 {"日期": "学习内容"}
    """
    data_file = get_data_file()

    # 用 for 循环构建文件内容（Week 02 复习）
    content = ""
    for date, log_content in learning_log.items():
        content += f"{date}: {log_content}\n"

    # 用 pathlib 的 write_text() 方法写入文件
    data_file.write_text(content, encoding="utf-8")

    print(f"✓ 已保存 {len(learning_log)} 条学习记录")


# =====================
# 显示相关函数（Week 03 复习）
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
    print("5. 退出并保存")


# =====================
# 输入相关函数（Week 02 + Week 03 复习）
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
        print("✗ 无效输入，请输入 1-5")


def get_mood():
    """获取用户心情"""
    print("\n今天心情怎么样？")
    print("1. 充满干劲")
    print("2. 一般般")
    print("3. 有点累")
    mood = input("请输入你的心情（1-3）：")
    return mood


# =====================
# 业务逻辑函数（Week 02 + Week 04 复习）
# =====================

def get_advice(mood):
    """
    根据心情返回建议

    用到 Week 02 的 if/elif/else
    """
    if mood == "1":
        return "太好了！推荐你今天挑战一个新概念，比如文件操作或 pathlib。"
    elif mood == "2":
        return "那就做点巩固练习吧，写几个小例子，熟悉一下文件读写。"
    elif mood == "3":
        return "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
    else:
        return "写点巩固练习最稳妥。"


# =====================
# 学习记录管理（Week 04 复习）
# =====================

def add_record(learning_log):
    """
    添加学习记录

    用到 Week 04 的字典操作
    """
    date = input("请输入日期（如 02-09）：")
    content = input("请输入今天学了什么：")

    if date in learning_log:
        print(f"⚠️  注意：{date} 的记录会被覆盖")

    # 字典添加/修改键值对
    learning_log[date] = content
    print(f"✓ 已添加：{date} - {content}")


def show_records(learning_log):
    """
    查看所有学习记录

    用到 Week 04 的字典遍历和 sorted()
    """
    if not learning_log:
        print("✗ 还没有学习记录哦，去添加一些吧！")
        return

    print("\n=== 学习记录 ===")

    # 按日期排序后遍历
    for date in sorted(learning_log.keys()):
        print(f"{date}: {learning_log[date]}")


def show_stats(learning_log):
    """
    统计学习天数

    用到 Week 04 的 len()
    """
    count = len(learning_log)
    print(f"\n你已经学习了 {count} 天")

    # 用到 Week 02 的 if/elif/else
    if count >= 5:
        print("✓ 太棒了！坚持就是胜利！")
    elif count >= 2:
        print("✓ 不错的开始，继续加油！")
    else:
        print("✓ 万事开头难，加油！")


# =====================
# 功能函数（Week 03 复习）
# =====================

def show_advice():
    """功能1：显示学习建议"""
    mood = get_mood()
    advice = get_advice(mood)
    print(f"\n{advice}")


# =====================
# 主函数（Week 03 + Week 05 改进）
# =====================

def main():
    """
    主函数——把所有功能串联起来

    Week 05 改进：
    1. 启动时加载学习记录（从文件）
    2. 退出时保存学习记录（到文件）
    """
    # 启动时加载学习记录（Week 05 的文件操作）
    learning_log = load_learning_log()

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
            # 退出前保存学习记录（Week 05 的文件操作）
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
# 本周改进说明
# =====================

"""
对比 Week 04 的代码，本周的改进：

1. **持久化存储（核心改进）**
   - 上周：数据存在内存里（字典），程序关闭后丢失
   - 这周：数据存到文件里，程序关闭后还在

2. **新增文件操作函数**
   - get_data_file()：获取数据文件路径（用 pathlib）
   - load_learning_log()：启动时加载学习记录
   - save_learning_log()：退出时保存学习记录

3. **用到本周知识点**
   - pathlib：Path.cwd() 获取当前目录
   - pathlib：path.exists() 检查文件是否存在
   - pathlib：path.read_text() 快捷读取
   - pathlib：path.write_text() 快捷写入
   - encoding="utf-8"：避免中文乱码

4. **数据格式**
   - 文件格式：每行 "日期: 内容"
   - 示例：
     02-09: 学会了列表和字典的基本用法
     02-10: 写了一个成绩单项目
     02-11: 学会了文件读写和 pathlib

5. **回顾旧知识**
   - Week 02：while 循环、if/elif/else、input()
   - Week 03：函数定义、参数传递、返回值
   - Week 04：字典操作、len()、sorted()

本周用到的 Week 05 新知识：
- 文件读写（open/read/write）
- with 语句（pathlib 内部已使用）
- pathlib（Path、路径操作、快捷方法）
- 编码（UTF-8）

下周预告（Week 06）：
PyHelper 将学会"异常处理"
不管用户输入什么乱七八糟的东西，都不会崩溃
比如：输入 "abc" 而不是数字、文件损坏、日期格式错误等
"""
