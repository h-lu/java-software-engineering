"""
示例：pathlib 基础——现代化的路径处理库

本例演示：
- pathlib.Path 的基本用法
- Path.home()、Path.cwd() 获取常用路径
- 用 / 运算符拼接路径（跨平台兼容）
- read_text()、write_text() 快捷方法
- 检查文件是否存在、创建目录

运行方式：python3 chapters/week_05/examples/03_pathlib_basics.py
预期输出：演示 pathlib 的各种实用方法
"""

from pathlib import Path


# =====================
# 1. 获取常用路径
# =====================

def demo_common_paths():
    """演示获取常用路径"""
    print("=== 1. 获取常用路径 ===")

    # 获取用户主目录（跨平台兼容）
    home = Path.home()
    print(f"✓ 用户主目录：{home}")
    # Windows: C:\Users\用户名
    # Mac/Linux: /Users/用户名

    # 获取当前工作目录
    cwd = Path.cwd()
    print(f"✓ 当前工作目录：{cwd}")

    # 用字符串创建路径
    file_path = Path("diary.txt")
    print(f"✓ 相对路径：{file_path}")

    # 用绝对路径
    abs_path = Path("/Users/xb/Documents/diary.txt")
    print(f"✓ 绝对路径：{abs_path}")


# =====================
# 2. 路径拼接
# =====================

def demo_path_join():
    """演示路径拼接"""
    print("\n=== 2. 路径拼接（/ 运算符） ===")

    home = Path.home()

    # 用 / 运算符拼接路径（比 os.path.join() 更直观）
    diary_dir = home / "Documents" / "Diary"
    print(f"✓ 拼接后的路径：{diary_dir}")

    # 继续拼接文件名
    diary_file = diary_dir / "diary.txt"
    print(f"✓ 完整文件路径：{diary_file}")

    # 对比传统写法
    import os
    traditional_path = os.path.join(home, "Documents", "Diary", "diary.txt")
    print(f"✓ 传统写法：{traditional_path}")


# =====================
# 3. 路径的各个部分
# =====================

def demo_path_parts():
    """演示获取路径的各个部分"""
    print("\n=== 3. 路径的各个部分 ===")

    file_path = Path("/Users/xb/Documents/diary.txt")

    print(f"✓ 完整路径：{file_path}")
    print(f"✓ 文件名：{file_path.name}")           # diary.txt
    print(f"✓ 文件名（不含扩展名）：{file_path.stem}")  # diary
    print(f"✓ 扩展名：{file_path.suffix}")          # .txt
    print(f"✓ 父目录：{file_path.parent}")          # /Users/xb/Documents
    print(f"✓ 父目录的父目录：{file_path.parent.parent}")  # /Users/xb

    # 遍历所有父目录
    print("✓ 所有父目录：")
    for parent in file_path.parents:
        print(f"  - {parent}")


# =====================
# 4. 文件检查
# =====================

def demo_file_checks():
    """演示文件检查"""
    print("\n=== 4. 文件检查 ===")

    # 创建测试文件
    test_file = Path("test_pathlib.txt")
    test_file.write_text("测试内容", encoding="utf-8")

    # 检查文件是否存在
    if test_file.exists():
        print(f"✓ {test_file} 存在")

    # 检查是否是文件
    if test_file.is_file():
        print(f"✓ {test_file} 是一个文件")

    # 检查是否是目录
    if not test_file.is_dir():
        print(f"✓ {test_file} 不是一个目录")

    # 检查文件大小
    size = test_file.stat().st_size
    print(f"✓ 文件大小：{size} 字节")


# =====================
# 5. 读写文件快捷方法
# =====================

def demo_read_write_text():
    """演示 pathlib 的读写快捷方法"""
    print("\n=== 5. 读写文件快捷方法 ===")

    file_path = Path("pathlib_demo.txt")

    # 写入文件（不用 open()）
    content = "第一行\n第二行\n第三行\n"
    file_path.write_text(content, encoding="utf-8")
    print(f"✓ 已写入 {file_path}")

    # 读取文件（不用 open()）
    read_content = file_path.read_text(encoding="utf-8")
    print(f"✓ 读取内容：\n{read_content}")

    # 对比传统写法
    print("\n对比传统写法：")
    print("传统：with open(path, 'w') as f: f.write(content)")
    print("pathlib：path.write_text(content)")
    print("✓ pathlib 更简洁！")


# =====================
# 6. 创建目录
# =====================

def demo_create_dir():
    """演示创建目录"""
    print("\n=== 6. 创建目录 ===")

    # 创建多层目录（如果不存在）
    data_dir = Path("data/logs")

    # parents=True：创建所有父目录
    # exist_ok=True：如果目录已存在，不报错
    data_dir.mkdir(parents=True, exist_ok=True)
    print(f"✓ 已创建目录：{data_dir}")

    # 在目录中创建文件
    log_file = data_dir / "app.log"
    log_file.write_text("应用日志\n", encoding="utf-8")
    print(f"✓ 已创建文件：{log_file}")


# =====================
# 7. 坏例子：硬编码路径
# =====================

def bad_example_hardcoded_path():
    """❌ 坏例子：硬编码路径（不能跨平台）"""
    print("\n=== 坏例子：硬编码路径 ===")

    # Windows 路径
    windows_path = "C:\\Users\\xb\\Documents\\diary.txt"
    print(f"❌ Windows 路径：{windows_path}")
    print("   在 Mac/Linux 上不能使用")

    # Mac/Linux 路径
    unix_path = "/Users/xb/Documents/diary.txt"
    print(f"❌ Unix 路径：{unix_path}")
    print("   在 Windows 上不能使用")

    # 问题：代码不能跨平台
    print("\n问题：硬编码路径不能跨平台兼容！")


# =====================
# 8. 好例子：用 pathlib 跨平台
# =====================

def good_example_pathlib_cross_platform():
    """✅ 好例子：用 pathlib 跨平台兼容"""
    print("\n=== 好例子：用 pathlib 跨平台兼容 ===")

    # 用 pathlib 构建路径（跨平台兼容）
    diary_file = Path.home() / "Documents" / "diary.txt"
    print(f"✓ 跨平台路径：{diary_file}")
    print("   在 Windows/Mac/Linux 上都能运行")

    # 在当前目录下创建文件（相对路径）
    local_file = Path.cwd() / "data.txt"
    print(f"✓ 当前目录下的文件：{local_file}")
    print("   不依赖操作系统，自动适配路径分隔符")


# =====================
# 主函数
# =====================

def main():
    """运行所有示例"""
    demo_common_paths()
    demo_path_join()
    demo_path_parts()
    demo_file_checks()
    demo_read_write_text()
    demo_create_dir()
    bad_example_hardcoded_path()
    good_example_pathlib_cross_platform()

    print("\n" + "=" * 50)
    print("总结：")
    print("- Path.home()：获取用户主目录")
    print("- Path.cwd()：获取当前工作目录")
    print("- / 运算符：拼接路径（跨平台兼容）")
    print("- .exists()：检查文件/目录是否存在")
    print("- .read_text() / .write_text()：快捷读写方法")
    print("- .mkdir(parents=True, exist_ok=True)：创建目录")
    print("- pathlib 比字符串路径更安全、更直观")
    print("=" * 50)


if __name__ == "__main__":
    main()
