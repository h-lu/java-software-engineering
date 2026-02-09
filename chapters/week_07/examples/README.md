# Week 07 示例代码

本目录包含 Week 07（模块化与项目结构）的所有示例代码。

## 文件列表

### 基础示例

| 文件 | 说明 | 运行方式 |
|------|------|---------|
| `01_single_file_mess.py` | 单文件项目的困境（反例） | `python3 01_single_file_mess.py` |
| `02_import_module.py` | import module 用法 | `python3 02_import_module.py` |
| `03_from_import.py` | from module import 用法 | `python3 03_from_import.py` |
| `04_import_alias.py` | import module as alias 用法 | `python3 04_import_alias.py` |
| `05_random_dict.py` | random 操作字典 | `python3 05_random_dict.py` |
| `06_standard_library_demo.py` | 标准库演示 | `python3 06_standard_library_demo.py` |

### 模块化示例

| 文件 | 说明 | 运行方式 |
|------|------|---------|
| `storage.py` | 文件操作模块 | `python3 storage.py` |
| `input_handler.py` | 输入校验模块 | `python3 input_handler.py` |
| `encouragement.py` | 鼓励语模块 | `python3 encouragement.py` |
| `records.py` | 业务逻辑模块 | `python3 records.py` |
| `main.py` | 主入口 | `python3 main.py` |

### PyHelper 模块化版本

目录：`07_pyhelper_modular/`

这是 PyHelper 的 Week 07 模块化版本，从单文件拆分成多模块项目。

```
07_pyhelper_modular/
├── main.py              # 主入口
├── storage.py           # 文件操作
├── input_handler.py     # 输入校验
├── encouragement.py     # 鼓励语
└── records.py           # 业务逻辑
```

运行方式：
```bash
cd 07_pyhelper_modular
python3 main.py
```

单独测试模块：
```bash
python3 storage.py
python3 input_handler.py
python3 encouragement.py
python3 records.py
```

## 学习要点

### 1. import 语句

- `import module`：导入整个模块
- `from module import name`：导入特定函数/常量
- `import module as alias`：给模块起别名

### 2. __name__ 守卫

- `if __name__ == "__main__":` 让模块既能导入也能运行
- 直接运行时：`__name__ == "__main__"`
- 导入时：`__name__ == "模块名"`

### 3. 模块化拆分原则

- 按功能分组：相关功能放在同一模块
- 单一职责：每个模块只做一件事
- 低耦合：模块间尽量减少依赖

### 4. 模块搜索路径

Python 按以下顺序查找模块：
1. 当前目录
2. PYTHONPATH 环境变量
3. 标准库目录

确保自定义模块文件在当前目录或 PYTHONPATH 中。

## 与上周的对比

| Week 06 | Week 07 |
|---------|---------|
| 单文件 1500+ 行 | 多模块，每个 40-60 行 |
| 所有功能挤在一个文件 | 按功能分组，职责清晰 |
| 难以维护和复用 | 易于维护和复用 |
| 无法单独测试某个功能 | 每个模块都能独立测试 |

## 下周预告

Week 08 将学习测试（testing）：
- 给每个模块编写单元测试
- 使用 pytest 测试框架
- 自动验证代码功能是否正确
