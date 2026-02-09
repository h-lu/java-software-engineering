"""
Week 05: pathlib 测试

测试 pathlib 路径操作、跨平台兼容性、read_text/write_text 等功能
"""

import pytest
from pathlib import Path
import os


class TestPathBasics:
    """测试 pathlib 基础操作"""

    def test_path_creation(self):
        """测试创建 Path 对象"""
        p = Path("test.txt")
        assert isinstance(p, Path)
        assert str(p) == "test.txt"

    def test_path_home(self):
        """测试 Path.home() 获取用户主目录"""
        home = Path.home()
        assert isinstance(home, Path)
        assert home.exists()

    def test_path_cwd(self):
        """测试 Path.cwd() 获取当前工作目录"""
        cwd = Path.cwd()
        assert isinstance(cwd, Path)
        assert cwd.exists()
        assert cwd.is_dir()

    def test_path_join_with_operator(self):
        """测试用 / 运算符拼接路径"""
        base = Path("/home/user")
        full = base / "Documents" / "file.txt"

        assert str(full).endswith("Documents/file.txt")
        # 注意：路径分隔符在不同操作系统上不同
        assert "Documents" in str(full)
        assert "file.txt" in str(full)


class TestPathProperties:
    """测试 Path 对象属性"""

    def test_path_name(self, tmp_path):
        """测试获取文件名"""
        file = tmp_path / "test.txt"
        assert file.name == "test.txt"

        file2 = tmp_path / "data" / "report.pdf"
        assert file2.name == "report.pdf"

    def test_path_suffix(self, tmp_path):
        """测试获取文件扩展名"""
        assert (tmp_path / "file.txt").suffix == ".txt"
        assert (tmp_path / "file.pdf").suffix == ".pdf"
        assert (tmp_path / "file").suffix == ""
        assert (tmp_path / "archive.tar.gz").suffix == ".gz"  # 只返回最后一个

    def test_path_parent(self, tmp_path):
        """测试获取父目录"""
        file = tmp_path / "subdir" / "file.txt"
        assert file.parent.name == "subdir"

    def test_path_stem(self, tmp_path):
        """测试获取文件名（不含扩展名）"""
        assert (tmp_path / "file.txt").stem == "file"
        assert (tmp_path / "report.pdf").stem == "report"
        assert (tmp_path / "archive.tar.gz").stem == "archive.tar"


class TestPathOperations:
    """测试 Path 操作方法"""

    def test_path_exists(self, tmp_path):
        """测试检查文件是否存在"""
        existing = tmp_path / "exists.txt"
        existing.write_text("content", encoding="utf-8")

        assert existing.exists() == True

        non_existent = tmp_path / "does_not_exist.txt"
        assert non_existent.exists() == False

    def test_path_is_file_is_dir(self, tmp_path):
        """测试判断是文件还是目录"""
        file = tmp_path / "file.txt"
        file.write_text("content", encoding="utf-8")

        assert file.is_file() == True
        assert file.is_dir() == False

        directory = tmp_path / "subdir"
        directory.mkdir()

        assert directory.is_dir() == True
        assert directory.is_file() == False

    def test_mkdir_with_parents(self, tmp_path):
        """测试创建多级目录"""
        deep_path = tmp_path / "level1" / "level2" / "level3"

        # parents=True 会自动创建父目录
        deep_path.mkdir(parents=True)

        assert deep_path.exists()
        assert deep_path.is_dir()

    def test_mkdir_exist_ok(self, tmp_path):
        """测试 exist_ok=True 避免目录已存在时报错"""
        dir_path = tmp_path / "test_dir"
        dir_path.mkdir()

        # 再次调用不会报错
        dir_path.mkdir(exist_ok=True)

        assert dir_path.exists()

    def test_mkdir_without_exist_ok_fails(self, tmp_path):
        """测试不使用 exist_ok 时，目录已存在会报错"""
        dir_path = tmp_path / "test_dir"
        dir_path.mkdir()

        with pytest.raises(FileExistsError):
            dir_path.mkdir()  # 没有 exist_ok=True


class TestPathReadWrite:
    """测试 pathlib 的 read_text/write_text 方法"""

    def test_write_text_basic(self, tmp_path):
        """测试 write_text() 基本用法"""
        file = tmp_path / "test.txt"
        file.write_text("Hello, World!", encoding="utf-8")

        assert file.exists()
        assert file.read_text(encoding="utf-8") == "Hello, World!"

    def test_read_text_basic(self, tmp_path):
        """测试 read_text() 基本用法"""
        file = tmp_path / "test.txt"
        file.write_text("Content here", encoding="utf-8")

        content = file.read_text(encoding="utf-8")
        assert content == "Content here"

    def test_write_read_chinese(self, tmp_path):
        """测试读写中文内容"""
        file = tmp_path / "chinese.txt"
        chinese_text = "今天学习了文件操作\n中文编码测试\n"

        file.write_text(chinese_text, encoding="utf-8")
        content = file.read_text(encoding="utf-8")

        assert content == chinese_text
        assert "中文" in content

    def test_write_multiple_lines(self, tmp_path):
        """测试写入多行文本"""
        file = tmp_path / "lines.txt"
        lines = ["第一行\n", "第二行\n", "第三行\n"]
        file.write_text("".join(lines), encoding="utf-8")

        content = file.read_text(encoding="utf-8")
        assert content == "".join(lines)

    def test_write_read_empty_file(self, tmp_path):
        """测试空文件的读写"""
        file = tmp_path / "empty.txt"
        file.write_text("", encoding="utf-8")

        assert file.read_text(encoding="utf-8") == ""

    def test_read_nonexistent_file(self, tmp_path):
        """测试读取不存在的文件会报错"""
        non_existent = tmp_path / "does_not_exist.txt"

        with pytest.raises(FileNotFoundError):
            non_existent.read_text(encoding="utf-8")


class TestPathCrossPlatform:
    """测试 pathlib 的跨平台兼容性"""

    def test_path_separator_is_automatic(self):
        """测试路径分隔符自动适配"""
        home = Path.home()
        documents = home / "Documents"

        # 在不同系统上，/ 运算符会使用正确的分隔符
        # Windows: \  Mac/Linux: /
        # 但我们可以验证路径确实拼接正确
        assert str(documents).endswith("Documents") or "Documents" in str(documents)

    def test_path_objects_are_os_aware(self):
        """测试 Path 对象是操作系统感知的"""
        cwd = Path.cwd()
        # 路径应该是绝对路径
        assert cwd.is_absolute()

    def test_relative_path(self, tmp_path):
        """测试相对路径的使用"""
        # 在 tmp_path 内创建相对路径
        file = tmp_path / "data" / "file.txt"
        file.parent.mkdir(parents=True, exist_ok=True)
        file.write_text("content", encoding="utf-8")

        assert file.exists()
        assert file.is_absolute()  # 即使是相对路径创建的，也会解析为绝对路径


class TestPathPractical:
    """测试 pathlib 实际应用场景"""

    def test_construct_data_file_path(self, tmp_path):
        """测试构建数据文件路径（模拟 PyHelper 场景）"""
        data_dir = tmp_path / "data"
        data_file = data_dir / "pyhelper_data.txt"

        # 创建目录（如果不存在）
        data_dir.mkdir(parents=True, exist_ok=True)

        # 写入数据
        data_file.write_text("02-09: 学会了文件操作\n", encoding="utf-8")

        assert data_file.exists()
        assert data_file.parent == data_dir

    def test_check_file_before_read(self, tmp_path):
        """测试读取前检查文件是否存在"""
        file = tmp_path / "data.txt"

        if file.exists():
            content = file.read_text(encoding="utf-8")
        else:
            content = ""

        assert content == ""  # 文件不存在，content 应为空

        # 创建文件后
        file.write_text("New content", encoding="utf-8")

        if file.exists():
            content = file.read_text(encoding="utf-8")

        assert content == "New content"

    def test_list_files_in_directory(self, tmp_path):
        """测试列出目录中的文件"""
        # 创建几个文件
        (tmp_path / "file1.txt").write_text("content1", encoding="utf-8")
        (tmp_path / "file2.txt").write_text("content2", encoding="utf-8")
        (tmp_path / "subdir").mkdir()

        # 列出所有文件
        items = list(tmp_path.iterdir())
        names = [item.name for item in items]

        assert "file1.txt" in names
        assert "file2.txt" in names
        assert "subdir" in names
        assert len(items) >= 3

    def test_get_file_size(self, tmp_path):
        """测试获取文件大小"""
        file = tmp_path / "test.txt"
        content = "Hello, World!" * 100  # 1300 字节

        file.write_text(content, encoding="utf-8")

        # stat().st_size 返回文件字节数
        size = file.stat().st_size
        assert size > 0
        assert size == len(content.encode("utf-8"))


class TestPathEdgeCases:
    """测试 pathlib 边界情况"""

    def test_path_with_spaces(self, tmp_path):
        """测试处理带空格的路径"""
        file = tmp_path / "file with spaces.txt"
        file.write_text("content", encoding="utf-8")

        assert file.exists()
        assert file.read_text(encoding="utf-8") == "content"

    def test_path_with_unicode_filename(self, tmp_path):
        """测试处理 Unicode 文件名"""
        file = tmp_path / "中文文件名.txt"
        file.write_text("内容", encoding="utf-8")

        assert file.exists()
        assert "中文文件名.txt" == file.name

    def test_empty_filename(self, tmp_path):
        """测试空文件名"""
        # 这会创建一个名为 .txt 的文件
        file = tmp_path / ".txt"
        file.write_text("content", encoding="utf-8")

        assert file.exists()
        assert file.name == ".txt"

    def test_dots_in_filename(self, tmp_path):
        """测试文件名中包含多个点"""
        file = tmp_path / "my.file.name.txt"
        file.write_text("content", encoding="utf-8")

        assert file.name == "my.file.name.txt"
        assert file.suffix == ".txt"
        assert file.stem == "my.file.name"

    def test_trailing_slash_behavior(self):
        """测试路径末尾斜杠的行为"""
        # pathlib 会自动处理末尾分隔符
        p1 = Path("/home/user/documents")
        p2 = Path("/home/user/documents/")

        # 两者指向同一个目录
        assert p1 == p2
