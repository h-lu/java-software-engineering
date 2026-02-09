"""
storage.py - 文件操作模块

职责：负责学习记录的文件读写操作

功能：
- get_data_file()：获取数据文件路径
- load_learning_log()：从文件加载学习记录
- save_learning_log()：保存学习记录到文件

运行方式（测试）：
  cd chapters/week_07/examples/07_pyhelper_modular
  python3 storage.py

导入方式：
  from storage import load_learning_log, save_learning_log
"""

from pathlib import Path


def get_data_file():
    """获取数据文件的路径"""
    return Path.cwd() / "pyhelper_data.txt"


def load_learning_log():
    """
    从文件加载学习记录（返回字典）

    文件格式：
    02-09: 学习了模块化
    02-08: 学习了异常处理
    """
    data_file = get_data_file()
    learning_log = {}

    try:
        if data_file.exists():
            content = data_file.read_text(encoding="utf-8")

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
        print(f"加载文件时出错：{e}")
        print("将创建新的数据文件")

    return learning_log


def save_learning_log(learning_log):
    """保存学习记录到文件"""
    data_file = get_data_file()

    try:
        content = ""
        for date, log_content in learning_log.items():
            content += f"{date}: {log_content}\n"

        data_file.write_text(content, encoding="utf-8")
        print(f"已保存 {len(learning_log)} 条学习记录")

    except Exception as e:
        print(f"保存文件时出错：{e}")


# =====================
# 测试代码
# =====================

if __name__ == "__main__":
    print("=== 测试 storage 模块 ===")

    # 测试加载
    learning_log = load_learning_log()

    # 测试保存
    learning_log["02-09"] = "测试：学会了模块化"
    save_learning_log(learning_log)

    print("\n✓ 测试完成！")
