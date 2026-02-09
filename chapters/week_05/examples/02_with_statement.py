"""
示例：with 语句——确保文件正确关闭的安全网

本例演示：
- with 语句的基本用法
- with 语句如何自动关闭文件
- 对比普通写法（忘记 close 的陷阱）
- with 语句在异常发生时的优势

运行方式：python3 chapters/week_05/examples/02_with_statement.py
预期输出：演示 with 语句的安全性和自动关闭文件的特性
"""


# =====================
# 1. 坏例子：忘记关闭文件
# =====================

def bad_example_forget_close():
    """❌ 坏例子：忘记 close()"""
    print("=== 坏例子：忘记 close() ===")

    # 打开文件
    file = open("unsafe.txt", "w", encoding="utf-8")
    file.write("这行可能不会落盘\n")

    # 忘记写 file.close() 了！
    # 数据还在缓冲区里，不会立即写到硬盘

    print("❌ 文件未关闭，数据可能丢失")
    print("   原因：Python 的文件写入有缓冲机制")
    print("   只有缓冲区满了或调用 close() 时，数据才会真正落盘")

    # 手动关闭（修复）
    file.close()
    print("✓ 手动关闭后，数据才落盘")


# =====================
# 2. 坏例子：出错时无法关闭文件
# =====================

def bad_example_error_no_close():
    """❌ 坏例子：出错时 close() 不会执行"""
    print("\n=== 坏例子：出错时 close() 不会执行 ===")

    file = open("unsafe_error.txt", "w", encoding="utf-8")
    file.write("这行应该写入\n")

    try:
        # 故意触发一个错误
        int("abc")  # ValueError
    except ValueError:
        print("❌ 发生错误，close() 不会执行")
        print("   文件可能没有正确关闭，数据可能丢失")

    # 如果这里不写 file.close()，数据就丢失了
    file.close()
    print("✓ 手动关闭后，数据才落盘")


# =====================
# 3. 好例子：用 with 语句
# =====================

def good_example_with():
    """✅ 好例子：用 with 语句自动关闭"""
    print("\n=== 好例子：用 with 语句 ===")

    # with 语句会自动关闭文件
    with open("safe.txt", "w", encoding="utf-8") as file:
        file.write("with 语句自动关闭文件\n")
        file.write("不用担心忘记 close()\n")

    # 离开 with 块后，文件已经自动关闭
    print("✓ 文件已自动关闭（离开 with 块）")

    # 验证文件内容
    with open("safe.txt", "r", encoding="utf-8") as file:
        content = file.read()
        print(f"✓ 文件内容：{repr(content)}")


# =====================
# 4. with 语句在异常时的优势
# =====================

def demo_with_error_handling():
    """演示 with 语句在异常发生时也会关闭文件"""
    print("\n=== with 语句在异常时的优势 ===")

    # 用 with 语句，即使发生错误，文件也会自动关闭
    with open("safe_error.txt", "w", encoding="utf-8") as file:
        file.write("这行会落盘\n")
        # 故意触发错误
        # int("abc")  # 取消注释这行会看到错误

    # 即使上面的代码出错，文件也会自动关闭
    print("✓ 文件已自动关闭（即使发生错误）")

    # 验证文件内容
    with open("safe_error.txt", "r", encoding="utf-8") as file:
        content = file.read()
        print(f"✓ 文件内容：{repr(content)}")


# =====================
# 5. 对比实验
# =====================

def compare_write_methods():
    """对比普通写法和 with 语句"""
    print("\n=== 对比实验 ===")

    # 实验 1：普通写法（不安全）
    print("\n实验 1：普通写法")
    file1 = open("compare1.txt", "w", encoding="utf-8")
    file1.write("普通写法\n")
    # 忘记 close() 了！
    print("❌ 文件未关闭，数据可能在缓冲区")
    file1.close()  # 手动关闭
    print("✓ 手动关闭后，数据落盘")

    # 实验 2：with 语句（安全）
    print("\n实验 2：with 语句")
    with open("compare2.txt", "w", encoding="utf-8") as file2:
        file2.write("with 语句\n")
    print("✓ 文件已自动关闭，数据已落盘")

    # 验证两个文件内容
    with open("compare1.txt", "r", encoding="utf-8") as f:
        print(f"✓ compare1.txt：{f.read().strip()}")

    with open("compare2.txt", "r", encoding="utf-8") as f:
        print(f"✓ compare2.txt：{f.read().strip()}")


# =====================
# 6. 实用示例：同时读写多个文件
# =====================

def demo_multiple_files():
    """演示 with 语句同时打开多个文件"""
    print("\n=== 实用示例：同时读写多个文件 ===")

    # 先创建源文件
    with open("source.txt", "w", encoding="utf-8") as f:
        f.write("第一行\n")
        f.write("第二行\n")
        f.write("第三行\n")

    # 同时打开两个文件（复制）
    with open("source.txt", "r", encoding="utf-8") as src, \
         open("dest.txt", "w", encoding="utf-8") as dst:
        for line in src:
            dst.write(line)

    print("✓ 已复制 source.txt 到 dest.txt")

    # 验证
    with open("dest.txt", "r", encoding="utf-8") as f:
        print(f"✓ dest.txt 内容：\n{f.read()}")


# =====================
# 主函数
# =====================

def main():
    """运行所有示例"""
    bad_example_forget_close()
    bad_example_error_no_close()
    good_example_with()
    demo_with_error_handling()
    compare_write_methods()
    demo_multiple_files()

    print("\n" + "=" * 50)
    print("总结：")
    print("- 普通写法：必须手动 close()，容易忘记")
    print("- with 语句：自动关闭文件（推荐！）")
    print("- with 语句在异常发生时也会关闭文件")
    print("- 可以同时打开多个文件（用逗号分隔）")
    print("=" * 50)


if __name__ == "__main__":
    main()
