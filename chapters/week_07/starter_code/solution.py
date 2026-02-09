"""
Week 07 作业参考实现

本文件是作业的参考实现，当你在作业中遇到困难时可以查看

作业要求：
1. 把单文件项目拆成多模块
2. 使用 import 导入其他模块
3. 使用 __name__ 守卫让模块既能导入也能运行

注意：
- 这是基础作业的参考实现
- 只需要实现核心功能，不需要覆盖进阶/挑战部分
- 建议先自己尝试，遇到困难时再查看
"""

# =====================
# 示例 1：拆分输入校验模块
# =====================

"""
假设你有一个单文件项目，包含以下输入校验函数：

def get_choice(min_choice=1, max_choice=5):
    while True:
        try:
            choice = int(input(f"\n请输入选择（{min_choice}-{max_choice}）："))
            if min_choice <= choice <= max_choice:
                return choice
            print(f"错误：请输入 {min_choice} 到 {max_choice} 之间的数字")
        except ValueError:
            print("错误：请输入数字，不要输入文字")

def get_date():
    while True:
        date = input("请输入日期（如 02-09）：")
        if "-" not in date or len(date) != 5:
            print("错误：日期格式不对，请输入类似 '02-09' 的格式")
            continue
        parts = date.split("-")
        if not (parts[0].isdigit() and parts[1].isdigit()):
            print("错误：日期必须是数字，请输入类似 '02-09' 的格式")
            continue
        return date

def get_content():
    while True:
        content = input("请输入今天学了什么：")
        if not content.strip():
            print("错误：学习内容不能为空")
            continue
        return content

你可以把这些函数拆分成一个独立的模块 input_handler.py：
"""

# input_handler.py
def get_choice(min_choice=1, max_choice=5):
    """获取用户选择（带异常处理）"""
    while True:
        try:
            choice = int(input(f"\n请输入选择（{min_choice}-{max_choice}）："))
            if min_choice <= choice <= max_choice:
                return choice
            print(f"错误：请输入 {min_choice} 到 {max_choice} 之间的数字")
        except ValueError:
            print("错误：请输入数字，不要输入文字")


def get_date():
    """获取日期（格式：MM-DD）"""
    while True:
        date = input("请输入日期（如 02-09）：")
        if "-" not in date or len(date) != 5:
            print("错误：日期格式不对，请输入类似 '02-09' 的格式")
            continue
        parts = date.split("-")
        if not (parts[0].isdigit() and parts[1].isdigit()):
            print("错误：日期必须是数字，请输入类似 '02-09' 的格式")
            continue
        return date


def get_content():
    """获取学习内容"""
    while True:
        content = input("请输入今天学了什么：")
        if not content.strip():
            print("错误：学习内容不能为空")
            continue
        return content


# 添加 __name__ 守卫，让模块既能导入也能运行
if __name__ == "__main__":
    print("=== 测试 input_handler 模块 ===")

    choice = get_choice(1, 5)
    print(f"你选择了：{choice}")

    date = get_date()
    print(f"日期：{date}")

    content = get_content()
    print(f"内容：{content}")

    print("\n✓ 测试完成！")


# =====================
# 示例 2：拆分文件操作模块
# =====================

"""
把文件操作相关的函数拆分成 storage.py：
"""

# storage.py
from pathlib import Path


def get_data_file():
    """获取数据文件的路径"""
    return Path.cwd() / "data.txt"


def load_data():
    """从文件加载数据"""
    data_file = get_data_file()
    data = {}

    try:
        if data_file.exists():
            content = data_file.read_text(encoding="utf-8")
            for line in content.split("\n"):
                line = line.strip()
                if line:
                    parts = line.split(": ", 1)
                    if len(parts) == 2:
                        key, value = parts
                        data[key] = value
            print(f"已加载 {len(data)} 条数据")
        else:
            print("首次运行，将创建新的数据文件")
    except Exception as e:
        print(f"加载文件时出错：{e}")

    return data


def save_data(data):
    """保存数据到文件"""
    data_file = get_data_file()

    try:
        content = ""
        for key, value in data.items():
            content += f"{key}: {value}\n"

        data_file.write_text(content, encoding="utf-8")
        print(f"已保存 {len(data)} 条数据")
    except Exception as e:
        print(f"保存文件时出错：{e}")


if __name__ == "__main__":
    print("=== 测试 storage 模块 ===")

    data = load_data()
    data["02-09"] = "测试数据"
    save_data(data)

    print("\n✓ 测试完成！")


# =====================
# 示例 3：创建主入口文件
# =====================

"""
创建 main.py 作为主入口，导入其他模块：
"""

# main.py
from input_handler import get_choice, get_date, get_content
from storage import load_data, save_data


def print_menu():
    """打印菜单"""
    print("\n请选择功能：")
    print("1. 添加记录")
    print("2. 查看记录")
    print("3. 退出")


def main():
    """主函数"""
    # 加载数据
    data = load_data()

    while True:
        print_menu()
        choice = get_choice(1, 3)

        if choice == 1:
            # 添加记录
            date = get_date()
            content = get_content()
            data[date] = content
            print(f"已添加：{date} - {content}")
        elif choice == 2:
            # 查看记录
            if not data:
                print("还没有记录哦")
            else:
                print("\n=== 记录 ===")
                for date in sorted(data.keys()):
                    print(f"{date}: {data[date]}")
        elif choice == 3:
            # 退出前保存
            save_data(data)
            print("再见！")
            break


if __name__ == "__main__":
    main()


# =====================
# 关键要点总结
# =====================

"""
1. **模块化拆分原则**
   - 按功能分组：相关功能放在同一模块
   - 单一职责：每个模块只做一件事
   - 低耦合：模块间尽量减少依赖

2. **import 的使用**
   - import module：导入整个模块
   - from module import name：导入特定函数
   - import module as alias：给模块起别名

3. **__name__ 守卫**
   - if __name__ == "__main__"：判断是否直接运行
   - 直接运行时：__name__ == "__main__"
   - 导入时：__name__ == 模块名
   - 用途：让模块既能导入也能运行测试

4. **模块搜索路径**
   - Python 只在特定路径下查找模块
   - 确保模块文件在当前目录或 PYTHONPATH 中
   - 导入前检查模块文件是否存在

5. **测试模块**
   - 可以直接运行模块文件进行测试
   - 测试代码放在 __name__ 守卫里
   - 导入时不会执行测试代码
"""

# =====================
# 常见错误和解决方法
# =====================

"""
错误 1：ModuleNotFoundError
原因：模块文件不在搜索路径中
解决：确保模块文件和主程序在同一目录

错误 2：ImportError
原因：导入的名称不存在
解决：检查模块文件是否有该函数/变量

错误 3：循环导入
原因：A 导入 B，B 又导入 A
解决：重构代码，提取共同依赖

错误 4：__name__ 守卫误用
原因：把函数定义写在守卫里面
解决：函数定义在守卫外，测试代码在守卫内
"""

if __name__ == "__main__":
    print("本文件是参考实现，查看各个示例了解如何拆分模块")
