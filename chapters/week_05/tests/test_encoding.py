"""
Week 05: 编码测试

测试 UTF-8 编码、中文支持、不同编码之间的差异
"""

import pytest
from pathlib import Path


class TestUTF8Encoding:
    """测试 UTF-8 编码读写"""

    def test_write_read_chinese_with_utf8(self, tmp_path):
        """测试用 UTF-8 读写中文"""
        file = tmp_path / "chinese.txt"
        chinese_content = "你好，世界！\n这是中文测试。\n"

        # 写入时指定 UTF-8
        with open(file, "w", encoding="utf-8") as f:
            f.write(chinese_content)

        # 读取时也用 UTF-8
        with open(file, "r", encoding="utf-8") as f:
            result = f.read()

        assert result == chinese_content
        assert "你好" in result
        assert "中文测试" in result

    def test_write_read_mixed_languages(self, tmp_path):
        """测试读写混合语言文本"""
        file = tmp_path / "mixed.txt"
        mixed = "English, 中文, 日本語, 한국\n"

        file.write_text(mixed, encoding="utf-8")
        content = file.read_text(encoding="utf-8")

        assert content == mixed
        assert "English" in content
        assert "中文" in content
        assert "日本語" in content

    def test_pathlib_write_read_chinese(self, tmp_path):
        """测试 pathlib 的 UTF-8 中文读写"""
        file = tmp_path / "chinese.txt"
        text = "今天学习了文件编码\nUTF-8 真的很方便\n"

        file.write_text(text, encoding="utf-8")
        content = file.read_text(encoding="utf-8")

        assert content == text
        assert "编码" in content

    def test_chinese_in_dict_format(self, tmp_path):
        """测试中文在字典格式的文本文件中"""
        file = tmp_path / "learning_log.txt"

        # 写入类似 PyHelper 的学习记录格式
        log = {
            "02-09": "学会了列表和字典的基本用法",
            "02-10": "写了一个成绩单项目",
            "02-11": "搞懂了 UTF-8 编码"
        }

        with open(file, "w", encoding="utf-8") as f:
            for date, content in log.items():
                f.write(f"{date}: {content}\n")

        # 读取并验证
        content = file.read_text(encoding="utf-8")
        assert "列表和字典" in content
        assert "成绩单项目" in content
        assert "UTF-8" in content

    def test_unicode_emojis(self, tmp_path):
        """测试 Unicode emoji 字符的读写"""
        file = tmp_path / "emoji.txt"
        emoji_text = "今天很开心 😊\n庆祝一下 🎉\n学习编程 💻\n"

        file.write_text(emoji_text, encoding="utf-8")
        content = file.read_text(encoding="utf-8")

        assert content == emoji_text
        assert "😊" in content
        assert "🎉" in content


class TestEncodingBasics:
    """测试编码的基本概念"""

    def test_encode_string_to_bytes(self):
        """测试将字符串编码为字节"""
        text = "中文"
        utf8_bytes = text.encode("utf-8")

        assert isinstance(utf8_bytes, bytes)
        assert len(utf8_bytes) == 6  # 每个中文字符在 UTF-8 中占 3 字节

    def test_decode_bytes_to_string(self):
        """测试将字节解码为字符串"""
        text = "中文"
        utf8_bytes = text.encode("utf-8")

        decoded = utf8_bytes.decode("utf-8")
        assert decoded == text

    def test_different_encodings_produce_different_bytes(self):
        """测试不同编码产生不同的字节序列"""
        text = "中文"

        utf8_bytes = text.encode("utf-8")
        gbk_bytes = text.encode("gbk")

        # UTF-8 和 GBK 的字节表示不同
        assert utf8_bytes != gbk_bytes

        # 但都能正确解码回原文本
        assert utf8_bytes.decode("utf-8") == text
        assert gbk_bytes.decode("gbk") == text

    def test_wrong_encoding_decoding_fails(self):
        """测试错误的编码解码会失败"""
        text = "中文"
        utf8_bytes = text.encode("utf-8")

        # 用 GBK 解码 UTF-8 编码的字节会报错或乱码
        with pytest.raises(UnicodeDecodeError):
            utf8_bytes.decode("gbk")


class TestEncodingInFileOperations:
    """测试文件操作中的编码处理"""

    def test_write_utf8_read_utf8(self, tmp_path):
        """测试 UTF-8 写入和读取匹配"""
        file = tmp_path / "test.txt"
        content = "这是中文内容"

        with open(file, "w", encoding="utf-8") as f:
            f.write(content)

        with open(file, "r", encoding="utf-8") as f:
            result = f.read()

        assert result == content

    def test_write_utf8_read_wrong_encoding(self, tmp_path):
        """测试用错误的编码读取 UTF-8 文件"""
        file = tmp_path / "test.txt"
        # 某些中文字符在 UTF-8 编码后无法用 GBK 正确解码
        content = "特殊字符：🎉"

        with open(file, "w", encoding="utf-8") as f:
            f.write(content)

        # 用 GBK 读取 UTF-8 编码的文件会报错
        with pytest.raises(UnicodeDecodeError):
            with open(file, "r", encoding="gbk") as f:
                f.read()

    def test_encoding_parameter_required_for_chinese(self, tmp_path):
        """测试中文必须指定正确的编码"""
        file = tmp_path / "chinese.txt"
        content = "中文测试"

        # 不指定 encoding，在某些系统上可能使用默认编码（不安全）
        # 但通常现代 Python 默认会用 UTF-8
        with open(file, "w", encoding="utf-8") as f:
            f.write(content)

        # 读取时也必须指定 encoding
        result = file.read_text(encoding="utf-8")
        assert result == content


class TestSpecialCharacters:
    """测试特殊字符的编码处理"""

    def test_newline_characters(self, tmp_path):
        """测试换行符的处理"""
        file = tmp_path / "newlines.txt"
        # 使用简单的换行符，避免平台差异
        content = "Line 1\nLine 2\nLine 3\n"

        file.write_text(content, encoding="utf-8")
        result = file.read_text(encoding="utf-8")

        assert result == content
        assert "\n" in result

    def test_tabs_and_spaces(self, tmp_path):
        """测试制表符和空格"""
        file = tmp_path / "tabs.txt"
        content = "Column1\tColumn2\tColumn3\n"

        file.write_text(content, encoding="utf-8")
        result = file.read_text(encoding="utf-8")

        assert result == content
        assert "\t" in result

    def test_quotes_and_backslashes(self, tmp_path):
        """测试引号和反斜杠"""
        file = tmp_path / "quotes.txt"
        content = '他说："这是\'测试\'字符串"\n路径：C:\\Users\\Test\n'

        file.write_text(content, encoding="utf-8")
        result = file.read_text(encoding="utf-8")

        assert result == content

    def test_punctuation_marks(self, tmp_path):
        """测试各种标点符号"""
        file = tmp_path / "punctuation.txt"
        content = "全角标点：，。！？；：「」\n半角标点:,.!?;:'\"\n"

        file.write_text(content, encoding="utf-8")
        result = file.read_text(encoding="utf-8")

        assert result == content


class TestEncodingEdgeCases:
    """测试编码的边界情况"""

    def test_empty_string_encoding(self):
        """测试空字符串的编码"""
        text = ""
        utf8_bytes = text.encode("utf-8")

        assert utf8_bytes == b""
        assert len(utf8_bytes) == 0

    def test_very_long_chinese_text(self, tmp_path):
        """测试很长的中文文本"""
        file = tmp_path / "long.txt"
        long_text = "这是一段很长的中文文本。" * 1000

        file.write_text(long_text, encoding="utf-8")
        result = file.read_text(encoding="utf-8")

        assert result == long_text
        assert len(result) > 0

    def test_mixed_ascii_and_chinese(self, tmp_path):
        """测试 ASCII 和中文混合"""
        file = tmp_path / "mixed.txt"
        mixed = "Hello 你好 World 世界 Test 测试\n"

        file.write_text(mixed, encoding="utf-8")
        result = file.read_text(encoding="utf-8")

        assert result == mixed

    def test_unicode_escape_sequences(self, tmp_path):
        """测试 Unicode 转义序列"""
        file = tmp_path / "unicode.txt"

        # 直接写入 Unicode 字符
        text = "\u4e2d\u6587"  # "中文"
        file.write_text(text, encoding="utf-8")

        result = file.read_text(encoding="utf-8")
        assert result == "中文"

    def test_bom_byte_order_mark(self, tmp_path):
        """测试带 BOM 的 UTF-8"""
        file = tmp_path / "with_bom.txt"

        # UTF-8 with BOM (utf-8-sig)
        content = "中文内容"
        with open(file, "w", encoding="utf-8-sig") as f:
            f.write(content)

        # 用 utf-8-sig 读取会自动处理 BOM
        with open(file, "r", encoding="utf-8-sig") as f:
            result = f.read()

        assert result == content

    def test_rare_chinese_characters(self, tmp_path):
        """测试生僻中文字符"""
        file = tmp_path / "rare.txt"
        rare_chars = "龘靐齉爨\n"  # 一些生僻字

        file.write_text(rare_chars, encoding="utf-8")
        result = file.read_text(encoding="utf-8")

        assert result == rare_chars


class TestPracticalEncodingScenarios:
    """测试实际编码应用场景"""

    def test_diary_with_chinese(self, tmp_path):
        """测试中文日记的存储和读取"""
        file = tmp_path / "diary.txt"

        entries = [
            "2026-02-09: 今天学会了文件操作，with 语句很方便",
            "2026-02-10: 搞懂了 UTF-8 编码，不会再有乱码问题",
            "2026-02-11: 追加模式让日记本能持续记录"
        ]

        # 写入日记
        with open(file, "w", encoding="utf-8") as f:
            for entry in entries:
                f.write(entry + "\n")

        # 读取日记
        with open(file, "r", encoding="utf-8") as f:
            lines = [line.strip() for line in f if line.strip()]

        assert len(lines) == 3
        assert "文件操作" in lines[0]
        assert "UTF-8" in lines[1]
        assert "追加模式" in lines[2]

    def test_learning_log_with_dates(self, tmp_path):
        """测试带日期的学习记录"""
        file = tmp_path / "learning.txt"

        data = {
            "02-09": "学会了列表和字典的基本用法",
            "02-10": "写了成绩单项目",
            "02-11": "学习了 pathlib 处理文件路径"
        }

        # 写入
        with open(file, "w", encoding="utf-8") as f:
            for date, content in data.items():
                f.write(f"{date}: {content}\n")

        # 读取并解析
        result = {}
        with open(file, "r", encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if line:
                    parts = line.split(": ", 1)
                    if len(parts) == 2:
                        result[parts[0]] = parts[1]

        assert result == data

    def test_search_chinese_content(self, tmp_path):
        """测试搜索中文内容"""
        file = tmp_path / "search_test.txt"

        content = """第一行：Python 编程
第二行：文件操作
第三行：字典和列表
第四行：编码问题"""

        file.write_text(content, encoding="utf-8")

        # 搜索包含"文件"的行
        lines = file.read_text(encoding="utf-8").split("\n")
        matching = [line for line in lines if "文件" in line]

        assert len(matching) == 1
        assert "文件操作" in matching[0]
