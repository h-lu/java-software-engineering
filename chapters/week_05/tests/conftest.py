"""
Week 05: pytest 配置和共享 fixtures
"""

import pytest
from pathlib import Path
import os


def pytest_configure(config):
    """
    pytest 配置钩子 - 注册自定义标记
    """
    # 注册自定义的 pytest 标记，避免警告
    config.addinivalue_line("markers", "file_io: 文件 I/O 相关测试")
    config.addinivalue_line("markers", "encoding: 编码相关测试")
    config.addinivalue_line("markers", "pathlib: pathlib 相关测试")
    config.addinivalue_line("markers", "diary: 日记本工具测试")


@pytest.fixture
def temp_diary_file(tmp_path):
    """
    创建临时日记文件用于测试

    Returns:
        Path: 临时日记文件的路径
    """
    return tmp_path / "diary.txt"


@pytest.fixture
def temp_data_file(tmp_path):
    """
    创建临时数据文件用于测试（类似 PyHelper 的数据文件）

    Returns:
        Path: 临时数据文件的路径
    """
    return tmp_path / "pyhelper_data.txt"


@pytest.fixture
def sample_diary_content():
    """
    提供示例日记内容

    Returns:
        list: 示例日记条目列表
    """
    return [
        "2026-02-09: 今天学会了文件操作",
        "2026-02-10: with 语句会自动关闭文件",
        "2026-02-11: 编码问题用 UTF-8 解决"
    ]


@pytest.fixture
def sample_learning_log():
    """
    提供示例学习记录字典（类似 PyHelper）

    Returns:
        dict: 示例学习记录
    """
    return {
        "02-09": "学会了列表和字典的基本用法",
        "02-10": "写了一个成绩单项目",
        "02-11": "学习了 pathlib 处理文件路径"
    }


@pytest.fixture
def chinese_text_samples():
    """
    提供各种中文文本样本用于测试编码

    Returns:
        dict: 包含不同类型中文文本的字典
    """
    return {
        "simple": "简单中文",
        "punctuation": "全角标点：，。！？",
        "mixed": "English mixed with 中文",
        "emoji": "今天很开心 😊",
        "long": "这是一段较长的中文文本，包含多个句子，用于测试编码是否正确。",
    }


def pytest_collection_modifyitems(config, items):
    """
    修改测试收集结果（可选）

    可以用来给测试添加标记、重新排序等
    """
    for item in items:
        # 给所有测试添加标记
        if "file" in item.nodeid.lower():
            item.add_marker(pytest.mark.file_io)
        if "encoding" in item.nodeid.lower():
            item.add_marker(pytest.mark.encoding)
        if "pathlib" in item.nodeid.lower():
            item.add_marker(pytest.mark.pathlib)
        if "diary" in item.nodeid.lower():
            item.add_marker(pytest.mark.diary)
