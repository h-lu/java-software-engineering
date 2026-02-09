"""
Week 05: 文件基础测试

测试文件读写、with 语句、追加模式等基础操作
"""

import pytest
import os
from pathlib import Path


@pytest.fixture
def temp_file(tmp_path):
    """创建临时测试文件"""
    return tmp_path / "test.txt"


@pytest.fixture
def cleanup_file():
    """测试后清理文件"""
    created_files = []
    yield created_files
    for file in created_files:
        if os.path.exists(file):
            os.remove(file)


class TestFileWrite:
    """测试文件写入功能"""

    def test_write_single_line(self, temp_file):
        """测试写入单行文本"""
        content = "Hello, World!\n"
        temp_file.write_text(content, encoding="utf-8")

        assert temp_file.exists()
        assert temp_file.read_text(encoding="utf-8") == content

    def test_write_multiple_lines(self, temp_file):
        """测试写入多行文本"""
        lines = ["第一行\n", "第二行\n", "第三行\n"]
        content = "".join(lines)

        temp_file.write_text(content, encoding="utf-8")

        result = temp_file.read_text(encoding="utf-8")
        assert result == content
        assert result.count("\n") == 3

    def test_write_with_newlines(self, temp_file):
        """测试换行符的正确使用"""
        with open(temp_file, "w", encoding="utf-8") as f:
            f.write("Line 1\n")
            f.write("Line 2\n")

        content = temp_file.read_text(encoding="utf-8")
        assert content == "Line 1\nLine 2\n"

    def test_write_without_newline(self, temp_file):
        """测试不换行时内容会连在一起"""
        with open(temp_file, "w", encoding="utf-8") as f:
            f.write("Hello")
            f.write("World")

        content = temp_file.read_text(encoding="utf-8")
        assert content == "HelloWorld"


class TestFileRead:
    """测试文件读取功能"""

    def test_read_all_content(self, temp_file):
        """测试读取全部内容 (read())"""
        content = "Hello\nWorld\n"
        temp_file.write_text(content, encoding="utf-8")

        with open(temp_file, "r", encoding="utf-8") as f:
            result = f.read()

        assert result == content

    def test_readlines_returns_list(self, temp_file):
        """测试 readlines() 返回列表"""
        lines = ["Line 1\n", "Line 2\n", "Line 3\n"]
        temp_file.write_text("".join(lines), encoding="utf-8")

        with open(temp_file, "r", encoding="utf-8") as f:
            result = f.readlines()

        assert result == lines
        assert isinstance(result, list)

    def test_readline_single_line(self, temp_file):
        """测试 readline() 读取单行"""
        temp_file.write_text("Line 1\nLine 2\n", encoding="utf-8")

        with open(temp_file, "r", encoding="utf-8") as f:
            line1 = f.readline()
            line2 = f.readline()

        assert line1 == "Line 1\n"
        assert line2 == "Line 2\n"

    def test_iterate_file_line_by_line(self, temp_file):
        """测试逐行遍历文件 (for line in file)"""
        lines = ["Line 1\n", "Line 2\n", "Line 3\n"]
        temp_file.write_text("".join(lines), encoding="utf-8")

        result = []
        with open(temp_file, "r", encoding="utf-8") as f:
            for line in f:
                result.append(line)

        assert result == lines

    def test_read_empty_file(self, temp_file):
        """测试读取空文件"""
        temp_file.write_text("", encoding="utf-8")

        content = temp_file.read_text(encoding="utf-8")
        assert content == ""

    def test_read_with_strip(self, temp_file):
        """测试用 strip() 去除换行符"""
        temp_file.write_text("Line 1\nLine 2\n", encoding="utf-8")

        with open(temp_file, "r", encoding="utf-8") as f:
            lines = [line.strip() for line in f]

        assert lines == ["Line 1", "Line 2"]


class TestWithStatement:
    """测试 with 语句的自动关闭功能"""

    def test_with_closes_file_automatically(self, temp_file):
        """测试 with 语句自动关闭文件"""
        with open(temp_file, "w", encoding="utf-8") as f:
            f.write("Test content\n")
            # 不需要手动 close()

        # 文件应该已经被写入
        assert temp_file.read_text(encoding="utf-8") == "Test content\n"

    def test_with_handles_write_and_close(self, temp_file):
        """测试 with 语句确保数据落盘"""
        with open(temp_file, "w", encoding="utf-8") as f:
            f.write("Data 1\n")
            f.write("Data 2\n")

        # 即使没有显式 flush/close，数据也应该写入
        content = temp_file.read_text(encoding="utf-8")
        assert "Data 1\n" in content
        assert "Data 2\n" in content


class TestFileModes:
    """测试不同文件模式"""

    def test_write_mode_overwrites(self, temp_file):
        """测试 "w" 模式会覆盖已有内容"""
        temp_file.write_text("Old content", encoding="utf-8")

        # 用 "w" 模式重新写入
        with open(temp_file, "w", encoding="utf-8") as f:
            f.write("New content")

        content = temp_file.read_text(encoding="utf-8")
        assert content == "New content"
        assert "Old" not in content

    def test_append_mode_adds_to_end(self, temp_file):
        """测试 "a" 追加模式"""
        temp_file.write_text("Line 1\n", encoding="utf-8")

        # 追加写入
        with open(temp_file, "a", encoding="utf-8") as f:
            f.write("Line 2\n")
            f.write("Line 3\n")

        content = temp_file.read_text(encoding="utf-8")
        assert content == "Line 1\nLine 2\nLine 3\n"

    def test_append_creates_file_if_not_exists(self, tmp_path):
        """测试追加模式在文件不存在时创建文件"""
        new_file = tmp_path / "new.txt"

        # 文件不存在，追加模式应该创建它
        with open(new_file, "a", encoding="utf-8") as f:
            f.write("First line\n")

        assert new_file.exists()
        assert new_file.read_text(encoding="utf-8") == "First line\n"

    def test_read_mode_fails_if_not_exists(self, tmp_path):
        """测试 "r" 模式在文件不存在时报错"""
        non_existent = tmp_path / "does_not_exist.txt"

        with pytest.raises(FileNotFoundError):
            with open(non_existent, "r", encoding="utf-8") as f:
                f.read()


class TestFileHandling:
    """测试文件处理相关操作"""

    def test_split_line_with_delimiter(self, temp_file):
        """测试用 split() 分割行内容"""
        temp_file.write_text("02-09: 今天学习了文件操作\n", encoding="utf-8")

        with open(temp_file, "r", encoding="utf-8") as f:
            line = f.readline().strip()
            parts = line.split(": ", 1)  # 最多分割1次

        assert len(parts) == 2
        assert parts[0] == "02-09"
        assert parts[1] == "今天学习了文件操作"

    def test_split_handles_multiple_colons(self, temp_file):
        """测试 split() 正确处理内容中的冒号"""
        temp_file.write_text("02-09: 今天学了: split() 的用法\n", encoding="utf-8")

        with open(temp_file, "r", encoding="utf-8") as f:
            line = f.readline().strip()
            parts = line.split(": ", 1)  # 只分割第一个 ": "

        assert len(parts) == 2
        assert parts[0] == "02-09"
        assert parts[1] == "今天学了: split() 的用法"

    def test_write_dict_to_file(self, temp_file):
        """测试将字典内容写入文件（每行一个键值对）"""
        data = {
            "02-09": "学会了列表和字典",
            "02-10": "写了一个成绩单项目"
        }

        with open(temp_file, "w", encoding="utf-8") as f:
            for key, value in data.items():
                f.write(f"{key}: {value}\n")

        content = temp_file.read_text(encoding="utf-8")
        assert "02-09: 学会了列表和字典\n" in content
        assert "02-10: 写了一个成绩单项目\n" in content

    def test_read_dict_from_file(self, temp_file):
        """测试从文件读取并重建字典"""
        # 先写入
        temp_file.write_text("02-09: 学会了列表\n02-10: 写了项目\n", encoding="utf-8")

        # 再读取
        result = {}
        with open(temp_file, "r", encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if line:
                    parts = line.split(": ", 1)
                    if len(parts) == 2:
                        result[parts[0]] = parts[1]

        assert result == {"02-09": "学会了列表", "02-10": "写了项目"}


class TestEdgeCases:
    """测试边界情况"""

    def test_write_empty_string(self, temp_file):
        """测试写入空字符串"""
        temp_file.write_text("", encoding="utf-8")
        assert temp_file.read_text(encoding="utf-8") == ""

    def test_write_special_characters(self, temp_file):
        """测试写入特殊字符"""
        special = "!@#$%^&*()_+-=[]{}|;':\",./<>?\n"
        temp_file.write_text(special, encoding="utf-8")

        assert temp_file.read_text(encoding="utf-8") == special

    def test_very_long_line(self, temp_file):
        """测试写入很长的行"""
        long_line = "A" * 10000 + "\n"
        temp_file.write_text(long_line, encoding="utf-8")

        content = temp_file.read_text(encoding="utf-8")
        assert len(content) == 10001
        assert content.startswith("AAAA")

    def test_unicode_emojis(self, temp_file):
        """测试写入 emoji 字符"""
        emoji_text = "今天很开心 😊\n庆祝一下 🎉\n"
        temp_file.write_text(emoji_text, encoding="utf-8")

        assert temp_file.read_text(encoding="utf-8") == emoji_text

    def test_blank_lines(self, temp_file):
        """测试处理空行"""
        temp_file.write_text("Line 1\n\nLine 3\n\n\n", encoding="utf-8")

        lines = []
        with open(temp_file, "r", encoding="utf-8") as f:
            for line in f:
                stripped = line.strip()
                if stripped:  # 跳过空行
                    lines.append(stripped)

        assert lines == ["Line 1", "Line 3"]
