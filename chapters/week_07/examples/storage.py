"""
storage.py - 文件操作模块

本模块负责学习记录的文件读写操作
包含以下功能：
1. get_data_file()：获取数据文件路径
2. load_learning_log()：从文件加载学习记录
3. save_learning_log()：保存学习记录到文件

运行方式（测试）：
  cd chapters/week_07/examples
  python3 storage.py

预期输出：
  === 测试 storage 模块 ===
  已加载 N 条学习记录
  已保存 N+1 条学习记录

导入方式：
  from storage import load_learning_log, save_learning_log
"""

from pathlib import Path


def get_data_file():
    """
    获取数据文件的路径

    返回：
        Path：数据文件的路径对象
    """
    return Path.cwd() / "pyhelper_data.txt"


def load_learning_log():
    """
    从文件加载学习记录（返回字典）

    文件格式：
    02-09: 学习了模块化
    02-08: 学习了异常处理

    返回：
        dict：学习记录字典，格式为 {"日期": "学习内容"}
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

            print(f"已加载 {len(learning_log)} 条学习记录")
        else:
            print("首次运行，将创建新的数据文件")

    except Exception as e:
        # Week 06 的异常处理
        print(f"加载文件时出错：{e}")
        print("将创建新的数据文件")

    return learning_log


def save_learning_log(learning_log):
    """
    保存学习记录到文件

    参数：
        learning_log (dict)：学习记录字典，格式为 {"日期": "学习内容"}
    """
    data_file = get_data_file()

    try:
        # 用 for 循环构建文件内容
        content = ""
        for date, log_content in learning_log.items():
            content += f"{date}: {log_content}\n"

        # Week 05 的 pathlib 写入
        data_file.write_text(content, encoding="utf-8")
        print(f"已保存 {len(learning_log)} 条学习记录")

    except Exception as e:
        # Week 06 的异常处理
        print(f"保存文件时出错：{e}")


# =====================
# 测试代码（__name__ 守卫）
# =====================

if __name__ == "__main__":
    """
    测试 storage 模块

    当直接运行此文件时，会执行测试代码
    当导入此模块时，不会执行测试代码
    """
    print("=== 测试 storage 模块 ===")

    # 测试加载
    learning_log = load_learning_log()

    # 测试保存
    learning_log["02-09"] = "测试：学会了模块化"
    save_learning_log(learning_log)

    print("\n✓ 测试完成！")
    print("提示：查看 pyhelper_data.txt 文件内容")
