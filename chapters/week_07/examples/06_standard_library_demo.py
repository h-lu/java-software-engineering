"""
示例：Python 标准库常用模块演示

本例演示 Python 标准库中几个最常用的模块
标准库是 Python 自带的"工具箱"，不需要额外安装

运行方式：python3 chapters/week_07/examples/06_standard_library_demo.py
预期输出：演示 math、random、datetime、pathlib 的常用功能
"""

import math
import random
from datetime import datetime, timedelta
from pathlib import Path


def demo_math():
    """演示 math 模块——数学函数"""
    print("=== math 模块（数学函数）===")

    print(f"√2 = {math.sqrt(2):.4f}")
    print(f"π = {math.pi:.4f}")
    print(f"e = {math.e:.4f}")
    print(f"sin(π/2) = {math.sin(math.pi / 2):.4f}")
    print(f"向上取整 ceil(4.2) = {math.ceil(4.2)}")
    print(f"向下取整 floor(4.8) = {math.floor(4.8)}")


def demo_random_module():
    """演示 random 模块——随机数"""
    print("\n=== random 模块（随机数）===")

    print(f"随机整数 (1-10)：{random.randint(1, 10)}")
    print(f"随机浮点数 (0-1)：{random.random():.4f}")
    print(f"从列表中随机选择：{random.choice(['苹果', '香蕉', '橙子'])}")

    # 生成随机密码
    import string

    chars = string.ascii_letters + string.digits
    password = "".join(random.choice(chars) for _ in range(8))
    print(f"随机密码：{password}")


def demo_datetime_module():
    """演示 datetime 模块——日期时间"""
    print("\n=== datetime 模块（日期时间）===")

    now = datetime.now()
    print(f"当前时间：{now}")
    print(f"格式化：{now.strftime('%Y-%m-%d %H:%M:%S')}")
    print(f"明天：{now + timedelta(days=1)}")
    print(f"一周后：{now + timedelta(weeks=1)}")

    # 计算时间差
    start = datetime(2026, 2, 1)
    end = datetime(2026, 2, 9)
    delta = end - start
    print(f"2月1日到2月9日：{delta.days} 天")


def demo_pathlib():
    """演示 pathlib 模块——路径处理（Week 05 复习）"""
    print("\n=== pathlib 模块（路径处理）===")

    # 获取当前工作目录
    cwd = Path.cwd()
    print(f"当前目录：{cwd}")

    # 拼接路径
    data_file = cwd / "data" / "test.txt"
    print(f"拼接路径：{data_file}")

    # 检查路径是否存在
    print(f"路径是否存在：{data_file.exists()}")

    # 获取文件名、扩展名、父目录
    print(f"文件名：{data_file.name}")
    print(f"扩展名：{data_file.suffix}")
    print(f"父目录：{data_file.parent}")


def demo_json_module():
    """演示 json 模块——JSON 数据（Week 10 预告）"""
    print("\n=== json 模块（JSON 数据）===")

    import json

    # Python 字典转 JSON 字符串
    data = {"name": "小北", "age": 18, "skills": ["Python", "Git"]}
    json_str = json.dumps(data, ensure_ascii=False)
    print(f"Python → JSON：\n{json_str}")

    # JSON 字符串转 Python 字典
    parsed = json.loads(json_str)
    print(f"JSON → Python：{parsed}")


def demo_string_module():
    """演示 string 模块——字符串常量"""
    print("\n=== string 模块（字符串常量）===")

    import string

    print(f"小写字母：{string.ascii_lowercase}")
    print(f"大写字母：{string.ascii_uppercase}")
    print(f"所有字母：{string.ascii_letters}")
    print(f"数字：{string.digits}")
    print(f"标点符号：{string.punctuation}")


if __name__ == "__main__":
    demo_math()
    demo_random_module()
    demo_datetime_module()
    demo_pathlib()
    demo_json_module()
    demo_string_module()

    print("\n=== 关键要点 ===")
    print("1. Python 标准库包含数百个模块，无需安装")
    print("2. 常用模块：math、random、datetime、pathlib、json")
    print("3. 记住最常用的，其他的用到再查")
    print("4. 查阅文档：https://docs.python.org/3/library/")
