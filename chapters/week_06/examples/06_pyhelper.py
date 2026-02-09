"""
PyHelper - 你的命令行学习助手
Week 06：异常处理与防御性编程

在上周（Week 05）的基础上，PyHelper 学会了"不怕坏输入"
这周给 PyHelper 穿上了"防弹衣"——无论用户输入什么乱七八糟的东西，都不会崩溃

本周改进：
1. 所有输入函数都加上了异常处理
2. 菜单选择：输入"abc"不会崩溃，会提示错误并让用户重试
3. 日期输入：格式不对会提示，并让用户重试
4. 文件操作：文件损坏或读写失败会提示错误
5. 用 while + try/except 实现"重试直到输入正确"

运行方式：python3 chapters/week_06/examples/06_pyhelper.py
预期输出：一个健壮的学习助手，输入错误不会崩溃
"""

from pathlib import Path


# =====================
# 文件操作函数（Week 05 + Week 06 改进）
# =====================

def get_data_file():
    """获取数据文件的路径（跨平台兼容）"""
    data_file = Path.cwd() / "pyhelper_data.txt"
    return data_file


def load_learning_log():
    """
    从文件加载学习记录（带异常处理）

    返回：字典，格式为 {"日期": "学习内容"}
    如果文件不存在或损坏，返回空字典并提示错误
    """
    data_file = get_data_file()
    learning_log = {}

    try:
        if data_file.exists():
            content = data_file.read_text(encoding="utf-8")

            # 解析文件内容（每行格式：日期: 内容）
            for line in content.split("\n"):
                line = line.strip()
                if line:
                    parts = line.split(": ", 1)
                    if len(parts) == 2:
                        date, log_content = parts
                        learning_log[date] = log_content

            print(f"✓ 已加载 {len(learning_log)} 条学习记录")
        else:
            print("✓ 首次运行，将创建新的数据文件")

    except Exception as e:
        # Week 06 新增：捕获文件操作的所有异常
        print(f"✗ 加载文件时出错：{e}")
        print("✓ 将创建新的数据文件")

    return learning_log


def save_learning_log(learning_log):
    """
    保存学习记录到文件（带异常处理）

    learning_log: 字典，格式为 {"日期": "学习内容"}
    """
    data_file = get_data_file()

    try:
        # 用 for 循环构建文件内容（Week 02 复习）
        content = ""
        for date, log_content in learning_log.items():
            content += f"{date}: {log_content}\n"

        # Week 06 改进：用 try/except 捕获写入异常
        data_file.write_text(content, encoding="utf-8")
        print(f"✓ 已保存 {len(learning_log)} 条学习记录")

    except Exception as e:
        # Week 06 新增：捕获写入失败的情况
        print(f"✗ 保存文件时出错：{e}")


# =====================
# 输入校验函数（Week 06 新增）
# =====================

def get_choice(min_choice=1, max_choice=5):
    """
    获取用户选择（带异常处理）

    Week 06 改进：
    - 用 while + try/except 实现"重试直到输入正确"
    - 捕获 ValueError（输入非数字）
    - 检查数字是否在有效范围内
    """
    while True:
        try:
            choice = int(input(f"\n请输入选择（{min_choice}-{max_choice}）："))
            if min_choice <= choice <= max_choice:
                return choice
            print(f"✗ 错误：请输入 {min_choice} 到 {max_choice} 之间的数字")

        except ValueError:
            # Week 06 新增：捕获输入非数字的错误
            print("✗ 错误：请输入数字，不要输入文字")


def get_date():
    """
    获取日期（格式：MM-DD）

    Week 06 新增：输入校验
    - 检查日期格式（必须是 XX-XX）
    - 检查是否为数字
    """
    while True:
        date = input("请输入日期（如 02-09）：")

        # 简单校验：日期格式必须是 XX-XX
        if "-" not in date or len(date) != 5:
            print("✗ 错误：日期格式不对，请输入类似 '02-09' 的格式")
            continue

        # 校验是否为数字
        parts = date.split("-")
        if not (parts[0].isdigit() and parts[1].isdigit()):
            print("✗ 错误：日期必须是数字，请输入类似 '02-09' 的格式")
            continue

        return date


def get_content():
    """
    获取学习内容

    Week 06 新增：检查空输入
    """
    while True:
        content = input("请输入今天学了什么：")

        if not content.strip():
            print("✗ 错误：学习内容不能为空")
            continue

        return content


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
# 业务逻辑函数（Week 04 复习 + Week 06 改进）
# =====================

def add_record(learning_log):
    """
    添加学习记录（带输入校验）

    Week 06 改进：
    - 用 get_date() 和 get_content() 进行输入校验
    - 用户输入错误不会崩溃，会提示并让用户重试
    """
    date = get_date()
    content = get_content()

    if date in learning_log:
        print(f"⚠️  注意：{date} 的记录已存在")
        overwrite = input("是否覆盖？(y/n)：").lower()
        if overwrite == "y":
            learning_log[date] = content
            print(f"✓ 已覆盖：{date} - {content}")
        else:
            print("✗ 取消添加")
    else:
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
# 学习建议功能（Week 02 复习 + Week 06 改进）
# =====================

def get_mood():
    """
    获取用户心情（带异常处理）

    Week 06 改进：
    - 用 try/except 捕获输入非数字的错误
    - 输入无效时给出默认值（避免崩溃）
    """
    print("\n今天心情怎么样？")
    print("1. 充满干劲")
    print("2. 一般般")
    print("3. 有点累")

    try:
        mood = int(input("请输入你的心情（1-3）："))
        if mood in [1, 2, 3]:
            return str(mood)
        print("✗ 输入无效，默认为一般般")
        return "2"
    except ValueError:
        # Week 06 新增：捕获输入非数字的错误
        print("✗ 输入无效，默认为一般般")
        return "2"


def get_advice(mood):
    """
    根据心情返回建议

    用到 Week 02 的 if/elif/else
    """
    if mood == "1":
        return "太好了！推荐你今天挑战一个新概念，比如异常处理或模块化。"
    elif mood == "2":
        return "那就做点巩固练习吧，写几个小例子，熟悉一下异常处理。"
    elif mood == "3":
        return "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
    else:
        return "写点巩固练习最稳妥。"


def show_advice():
    """
    显示学习建议

    Week 06 改进：
    - get_mood() 现在有异常处理，不会崩溃
    """
    mood = get_mood()
    advice = get_advice(mood)
    print(f"\n{advice}")


# =====================
# 主函数（Week 03 + Week 05 + Week 06 改进）
# =====================

def main():
    """
    主函数——把所有功能串联起来

    Week 06 改进：
    1. 所有输入函数都有异常处理
    2. 用户输入任何乱七八糟的东西都不会崩溃
    3. 文件操作有异常处理，文件损坏不会崩溃
    """
    # 启动时加载学习记录（Week 05 的文件操作）
    learning_log = load_learning_log()

    print_welcome()

    while True:
        print_menu()

        # Week 06 改进：get_choice() 现在有异常处理
        choice = get_choice(min_choice=1, max_choice=5)

        if choice == 1:
            add_record(learning_log)
        elif choice == 2:
            show_records(learning_log)
        elif choice == 3:
            show_stats(learning_log)
        elif choice == 4:
            show_advice()
        elif choice == 5:
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
对比 Week 05 的代码，本周的改进：

1. **异常处理（核心改进）**
   - 上周：输入"abc"会崩溃，报 ValueError
   - 这周：输入任何东西都不会崩溃，会提示错误并让用户重试

2. **新增输入校验函数**
   - get_choice()：带异常处理的菜单选择
   - get_date()：带格式校验的日期输入
   - get_content()：带空值检查的内容输入

3. **文件操作异常处理**
   - load_learning_log()：捕获文件损坏、编码错误等异常
   - save_learning_log()：捕获写入失败等异常

4. **用到本周知识点**
   - try/except：捕获特定异常（ValueError、FileNotFoundError）
   - while + try/except：实现"重试直到输入正确"
   - LBYL 风格：用 if 检查输入格式（如日期格式）
   - 友好的错误消息：告诉用户"出了什么错、怎么修复"

5. **防御性编程**
   - 不再假设"用户会正确输入"
   - 而是"假设用户一定会乱输入"，然后让程序能容错
   - 无论用户输入什么，程序都不会崩溃

本周用到的 Week 06 新知识：
- try/except 结构
- 捕获特定异常（ValueError、Exception）
- while + try/except 实现"重试直到输入正确"
- 输入校验（LBYL 风格）
- 友好的错误消息设计

下周预告（Week 07）：
PyHelper 将学会"分身"——把代码拆成多个文件，变成一个真正的项目
这就是"模块化"（modularization）
"""
