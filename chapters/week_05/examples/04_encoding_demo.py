"""
示例：编码问题——UTF-8 vs GBK

本例演示：
- UTF-8 和 GBK 编码的区别
- 中文乱码问题演示
- 如何正确处理编码
- encode() 和 decode() 方法

运行方式：python3 chapters/week_05/examples/04_encoding_demo.py
预期输出：演示编码问题及其解决方案
"""


# =====================
# 1. 编码基础
# =====================

def demo_encoding_basics():
    """演示编码的基本概念"""
    print("=== 1. 编码基础 ===")

    text = "中文测试"

    # UTF-8 编码（全球通用）
    utf8_bytes = text.encode("utf-8")
    print(f"✓ 文本：{text}")
    print(f"✓ UTF-8 编码：{utf8_bytes}")
    print(f"✓ 字节数：{len(utf8_bytes)}")

    # GBK 编码（中国国家标准）
    gbk_bytes = text.encode("gbk")
    print(f"✓ GBK 编码：{gbk_bytes}")
    print(f"✓ 字节数：{len(gbk_bytes)}")

    print("\n说明：")
    print("- UTF-8：全球通用，支持所有语言")
    print("- GBK：中国标准，只支持中文")
    print("- 同一个文字，不同编码会变成不同的字节")


# =====================
# 2. 编码不匹配导致乱码
# =====================

def demo_mismatched_encoding():
    """演示编码不匹配导致的乱码"""
    print("\n=== 2. 编码不匹配导致乱码 ===")

    # 用 UTF-8 写入文件
    with open("test_utf8.txt", "w", encoding="utf-8") as f:
        f.write("中文测试")
    print("✓ 已用 UTF-8 编码写入文件")

    # 用 GBK 读取（会乱码或报错）
    print("\n❌ 用 GBK 读取 UTF-8 文件：")
    try:
        with open("test_utf8.txt", "r", encoding="gbk") as f:
            content = f.read()
            print(f"读取内容：{content}")
            print("（可能会显示乱码）")
    except UnicodeDecodeError as e:
        print(f"✗ 解码错误：{e}")
        print("   GBK 无法正确解码 UTF-8 的字节")


# =====================
# 3. 正确做法：统一用 UTF-8
# =====================

def demo_correct_encoding():
    """演示正确的编码做法"""
    print("\n=== 3. 正确做法：统一用 UTF-8 ===")

    # 用 UTF-8 写入
    with open("test_correct.txt", "w", encoding="utf-8") as f:
        f.write("中文测试")
    print("✓ 已用 UTF-8 编码写入文件")

    # 用 UTF-8 读取
    with open("test_correct.txt", "r", encoding="utf-8") as f:
        content = f.read()
        print(f"✓ 用 UTF-8 读取：{content}")
        print("✓ 编码一致，不会乱码！")


# =====================
# 4. 检测文件编码
# =====================

def demo_detect_encoding():
    """演示检测文件编码（进阶）"""
    print("\n=== 4. 检测文件编码（进阶） ===")

    # 创建测试文件
    with open("unknown_encoding.txt", "wb") as f:
        f.write("中文测试".encode("utf-8"))

    print("提示：实际项目中可以用 chardet 库检测编码")
    print("安装：pip install chardet")
    print("""
    # 使用示例：
    import chardet

    with open("unknown.txt", "rb") as f:
        raw_data = f.read()
        result = chardet.detect(raw_data)
        encoding = result['encoding']
        print(f"检测到编码：{encoding}")

    with open("unknown.txt", "r", encoding=encoding) as f:
        content = f.read()
    """)


# =====================
# 5. 常见编码问题
# =====================

def demo_common_issues():
    """演示常见的编码问题"""
    print("\n=== 5. 常见编码问题 ===")

    # 问题 1：忘记指定 encoding
    print("\n问题 1：忘记指定 encoding")
    print("❌ open(file, 'w')  # 使用系统默认编码")
    print("   Windows 可能是 GBK，Mac 可能是 UTF-8")
    print("✓ open(file, 'w', encoding='utf-8')  # 明确指定 UTF-8")

    # 问题 2：写入和读取编码不一致
    print("\n问题 2：写入和读取编码不一致")
    print("❌ 写入用 UTF-8，读取用 GBK → 乱码")
    print("✓ 写入和读取都用 UTF-8")

    # 问题 3：Windows 记事本默认用 GBK
    print("\n问题 3：Windows 记事本默认用 GBK")
    print("❌ 用记事本打开 UTF-8 文件可能乱码")
    print("✓ 用 VS Code / Notepad++ 等编辑器，选择 UTF-8")


# =====================
# 6. BOM（字节序标记）
# =====================

def demo_bom():
    """演示 BOM（字节序标记）"""
    print("\n=== 6. BOM（字节序标记） ===")

    print("说明：")
    print("BOM（Byte Order Mark）是文件开头的一个特殊标记")
    print("UTF-8 with BOM：文件开头有 3 个字节（\\xef\\xbb\\xbf）")
    print("\n推荐：")
    print("- 用 UTF-8（without BOM）")
    print("- BOM 是 UTF-16 的遗留问题，UTF-8 不需要")
    print("\nPython 处理 BOM：")
    print("✓ open(file, 'r', encoding='utf-8-sig')  # 自动处理 BOM")


# =====================
# 7. 实用函数
# =====================

def demo_utility_functions():
    """演示实用的编码处理函数"""
    print("\n=== 7. 实用函数 ===")

    def safe_write(filename, content, encoding="utf-8"):
        """安全写入文件（统一用 UTF-8）"""
        with open(filename, "w", encoding=encoding) as f:
            f.write(content)
        print(f"✓ 已写入 {filename}（编码：{encoding}）")

    def safe_read(filename, encoding="utf-8"):
        """安全读取文件（统一用 UTF-8）"""
        with open(filename, "r", encoding=encoding) as f:
            return f.read()

    # 测试
    safe_write("safe_test.txt", "中文测试")
    content = safe_read("safe_test.txt")
    print(f"✓ 读取内容：{content}")


# =====================
# 8. 多语言支持
# =====================

def demo_multilingual():
    """演示 UTF-8 的多语言支持"""
    print("\n=== 8. UTF-8 多语言支持 ===")

    # UTF-8 支持所有语言
    multilingual_text = """
    中文：你好世界
    英文：Hello World
    日文：こんにちは
    韩文：안녕하세요
    俄文：Привет мир
    阿拉伯文：مرحبا بالعالم
    表情符号：😀🎉🚀
    """

    print("✓ UTF-8 支持多语言：")
    print(multilingual_text)

    # 写入文件
    with open("multilingual.txt", "w", encoding="utf-8") as f:
        f.write(multilingual_text)
    print("✓ 已写入 multilingual.txt")


# =====================
# 主函数
# =====================

def main():
    """运行所有示例"""
    demo_encoding_basics()
    demo_mismatched_encoding()
    demo_correct_encoding()
    demo_detect_encoding()
    demo_common_issues()
    demo_bom()
    demo_utility_functions()
    demo_multilingual()

    print("\n" + "=" * 50)
    print("总结：")
    print("- 编码：文字 → 字节的规则")
    print("- UTF-8：全球通用，推荐使用")
    print("- GBK：中国标准，但不兼容其他语言")
    print("- 统一用 UTF-8：写入和读取都用 UTF-8")
    print("- open() 时明确指定 encoding='utf-8'")
    print("- 避免 Windows 默认编码问题")
    print("=" * 50)


if __name__ == "__main__":
    main()
