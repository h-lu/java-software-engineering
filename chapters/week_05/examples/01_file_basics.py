"""
示例：文件读写基础——open() 函数的基本用法

本例演示：
- 用 open() 打开文件、写入内容、读取内容
- read()、readlines()、for line in file 三种读取方式
- 写入模式 ("w") vs 追加模式 ("a")
- 关闭文件的重要性（close()）

运行方式：python3 chapters/week_05/examples/01_file_basics.py
预期输出：创建 hello.txt 并读取内容，演示三种读取方式
"""


# =====================
# 1. 最简单的文件写入
# =====================

def demo_write_basic():
    """演示基本的文件写入"""
    print("=== 1. 基本文件写入 ===")

    # 打开文件（"w" 表示写入模式，如果不存在会创建）
    # encoding="utf-8" 很重要！避免中文乱码
    file = open("hello.txt", "w", encoding="utf-8")

    # 写入内容（注意：write() 不会自动加换行符）
    file.write("Hello, World!\n")  # 手动加 \n
    file.write("这是第二行\n")

    # 关闭文件（非常重要！否则数据可能不会落盘）
    file.close()

    print("✓ 已写入 hello.txt")


# =====================
# 2. 三种读取方式
# =====================

def demo_read_all():
    """演示 read()——一次性读取全部内容"""
    print("\n=== 2.1 用 read() 读取全部内容 ===")

    file = open("hello.txt", "r", encoding="utf-8")

    # read() 返回一个大字符串（包含换行符）
    content = file.read()

    file.close()

    print("读取到的内容：")
    print(repr(content))  # repr() 显示特殊字符（如 \n）
    # 输出：'Hello, World!\n这是第二行\n'


def demo_read_lines():
    """演示 readlines()——返回一个列表"""
    print("\n=== 2.2 用 readlines() 按行读取 ===")

    file = open("hello.txt", "r", encoding="utf-8")

    # readlines() 返回一个列表，每个元素是一行（包含换行符）
    lines = file.readlines()

    file.close()

    print(f"读取到 {len(lines)} 行：")
    for i, line in enumerate(lines, 1):
        print(f"  第 {i} 行：{repr(line)}")
    # 输出：
    #   第 1 行：'Hello, World!\n'
    #   第 2 行：'这是第二行\n'


def demo_read_line_by_line():
    """演示 for line in file——逐行读取（内存友好）"""
    print("\n=== 2.3 用 for line in file 逐行读取 ===")

    file = open("hello.txt", "r", encoding="utf-8")

    # 直接遍历文件对象（不会一次性加载整个文件到内存）
    print("逐行读取：")
    for line in file:
        # strip() 去掉末尾的换行符
        line = line.strip()
        print(f"  - {line}")

    file.close()


# =====================
# 3. 写入 vs 追加
# =====================

def demo_write_vs_append():
    """演示写入模式 ("w") 和追加模式 ("a") 的区别"""
    print("\n=== 3. 写入模式 vs 追加模式 ===")

    # 先用 "w" 模式写入（会覆盖旧内容）
    with open("counter.txt", "w", encoding="utf-8") as file:
        file.write("第一次写入\n")
    print("✓ 用 'w' 模式写入：counter.txt = '第一次写入'")

    # 再用 "w" 模式写入（会覆盖！）
    with open("counter.txt", "w", encoding="utf-8") as file:
        file.write("第二次写入\n")
    print("✓ 再用 'w' 模式写入：counter.txt = '第二次写入'（旧内容被覆盖）")

    # 用 "a" 模式追加（会加到文件末尾）
    with open("counter.txt", "a", encoding="utf-8") as file:
        file.write("追加的内容\n")
    print("✓ 用 'a' 模式追加：counter.txt = '第二次写入\\n追加的内容'")

    # 读取查看结果
    with open("counter.txt", "r", encoding="utf-8") as file:
        content = file.read()
        print(f"\n最终文件内容：\n{content}")


# =====================
# 4. 坏例子：忘记关闭文件
# =====================

def bad_example_no_close():
    """❌ 坏例子：忘记 close() 的风险"""
    print("\n=== 4. 坏例子：忘记 close() ===")

    file = open("bad.txt", "w", encoding="utf-8")
    file.write("这行可能不会落盘！")
    # 忘记写 file.close() 了！

    print("❌ 文件已打开但未关闭")
    print("   数据可能在缓冲区里，不会立即写到硬盘")
    print("   如果程序在这里崩溃，数据就丢失了")

    # 修复：关闭文件
    file.close()
    print("✓ 现在关闭了文件，数据才真正落盘")


# =====================
# 5. 最佳实践：用 with 语句
# =====================

def good_example_with():
    """✅ 好例子：用 with 语句自动关闭文件"""
    print("\n=== 5. 好例子：用 with 语句 ===")

    # with 语句会自动关闭文件（即使发生错误）
    with open("good.txt", "w", encoding="utf-8") as file:
        file.write("with 语句自动关闭文件\n")
        file.write("不用担心忘记 close()\n")

    # 离开 with 块后，文件已经自动关闭
    print("✓ 已自动关闭文件")

    # 读取验证
    with open("good.txt", "r", encoding="utf-8") as file:
        content = file.read()
        print(f"✓ 文件内容：{repr(content)}")


# =====================
# 主函数
# =====================

def main():
    """运行所有示例"""
    demo_write_basic()
    demo_read_all()
    demo_read_lines()
    demo_read_line_by_line()
    demo_write_vs_append()
    bad_example_no_close()
    good_example_with()

    print("\n" + "=" * 50)
    print("总结：")
    print("- read()：一次性读取全部内容（返回字符串）")
    print("- readlines()：按行读取（返回列表）")
    print("- for line in file：逐行读取（内存友好）")
    print("- 'w' 模式：覆盖写入")
    print("- 'a' 模式：追加写入")
    print("- with 语句：自动关闭文件（推荐！）")
    print("=" * 50)


if __name__ == "__main__":
    main()
