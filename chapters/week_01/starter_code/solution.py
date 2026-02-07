"""
Week 01 作业起始代码: 从零到可运行

本作业练习目标:
- 定义和调用函数
- 使用 input() 获取用户输入
- 使用 f-string 格式化输出
- 理解函数返回值

待实现函数:
- greet(name): 根据名字返回问候语
- get_user_info(): 获取用户输入并返回用户信息字符串
- main(): 程序入口，协调其他函数

测试命令:
    python3 -m pytest chapters/week_01/tests -q
"""

from __future__ import annotations


def greet(name: str) -> str:
    """根据用户名字返回问候语。

    Args:
        name: 用户的名字

    Returns:
        格式化的问候字符串，例如 "你好, Alice!"
    """
    return f"你好, {name}!"


def get_user_info() -> str:
    """获取用户输入并返回信息摘要。

    提示用户输入名字和年龄，返回格式化的信息字符串。

    Returns:
        包含用户信息的格式化字符串
    """
    name = input("请输入你的名字: ")
    age = int(input("请输入你的年龄: "))
    next_age = age + 1
    return f"{name} 今年 {age} 岁，明年 {next_age} 岁"


def solve(text: str) -> str:
    """处理输入文本并返回结果。

    这是测试使用的核心函数。当前实现只是返回输入文本，
    你可以在完成 greet 和 get_user_info 后，根据需要修改它。

    Args:
        text: 输入的文本

    Returns:
        处理后的文本
    """
    # TODO: 可选 - 根据需要修改此函数
    # 当前实现只是返回输入，确保测试通过
    return text


def main() -> None:
    """程序入口函数。

    协调程序流程，打印欢迎信息，获取并展示用户信息。
    """
    print("=== 欢迎 ===")
    user_info = get_user_info()
    print(user_info)
    # Extract name from user_info (format: "name 今年 age 岁，明年 next_age 岁")
    name = user_info.split(" ")[0]
    greeting = greet(name)
    print(greeting)


# 当直接运行此文件时，执行 main() 函数
# 当作为模块导入时，不自动执行
if __name__ == "__main__":
    main()
