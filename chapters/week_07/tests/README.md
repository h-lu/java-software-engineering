# Week 07 测试文档

## 测试文件说明

### test_smoke.py
基础冒烟测试，验证 Python 基础功能和 Week 07 核心概念是否正常工作。

**测试覆盖：**
- Python 版本检查
- 标准库模块可用性
- import 语句基本功能
- `__name__` 变量
- sys.path 模块搜索路径
- 模块属性
- 基础 Python 数据类型和操作（作为回顾）

### test_import_basics.py
import 语句的基础测试。

**测试覆盖：**
- `import module` 语法
- `from module import name` 语法
- `import module as alias` 语法
- 模块搜索路径（sys.path）
- 标准库模块的使用
- 导入边界情况
- 导入最佳实践

**测试类别：**
- 正例：正常导入和使用模块
- 边界：重复导入、别名使用、模块缓存
- 反例：导入不存在的模块、命名冲突

### test_modules_and_name_guard.py
模块和 `__name__` 守卫测试。

**测试覆盖：**
- 模块的基本概念
- `__name__` 变量的值（直接运行 vs 被导入）
- `__name__` 守卫的作用
- 模块级代码的执行时机
- 守卫外 vs 守卫内定义函数的区别
- 模块化最佳实践
- `__name__` 守卫的常见模式

**测试类别：**
- 正例：正确的守卫使用，模块既能导入也能运行
- 边界：模块级代码、守卫内测试代码
- 反例：函数定义在守卫内（错误示例）

### test_packages_and_imports.py
包结构和导入测试。

**测试覆盖：**
- 包的基本概念（包含 `__init__.py` 的目录）
- `__init__.py` 的作用（可以是空的、可以暴露函数）
- 绝对导入（`from package.subpackage import module`）
- 相对导入（`from . import module`, `from ..package import module`）
- 相对导入的限制（只能在包内使用）
- 项目结构（代码/测试/数据分离）
- 嵌套包
- 导入错误处理
- 包 vs 模块的区别

**测试类别：**
- 正例：正确的包结构和导入方式
- 边界：嵌套包、空 `__init__.py`
- 反例：相对导入在包外使用

## 测试用例矩阵

| 核心知识点 | 正例 | 边界 | 反例 |
|----------|-----|------|-----|
| import module | ✓ | 重复导入、多个模块 | 不存在的模块 |
| from import | ✓ | 多名称导入、常量导入 | 命名冲突、不存在的名称 |
| import as | ✓ | 长模块名简化 | - |
| 模块搜索路径 | ✓ | 当前目录、sys.path | 模块不在路径中 |
| __name__ 变量 | ✓ | 直接运行 vs 导入 | - |
| __name__ 守卫 | ✓ | 测试代码、示例代码 | 函数定义在守卫内 |
| 包结构 | ✓ | 空 __init__.py、暴露函数 | - |
| 绝对导入 | ✓ | 从包导入 | - |
| 相对导入 | ✓ | 同级模块、父包 | 包外使用相对导入 |
| 项目结构 | ✓ | 代码/测试分离 | - |

## 运行测试

```bash
# 运行所有 Week 07 测试
python3 -m pytest chapters/week_07/tests -q

# 运行单个测试文件
python3 -m pytest chapters/week_07/tests/test_smoke.py -v

# 运行特定测试类
python3 -m pytest chapters/week_07/tests/test_import_basics.py::TestImportModule -v

# 运行特定测试方法
python3 -m pytest chapters/week_07/tests/test_modules_and_name_guard.py::TestNameGuard::test_name_guard_executes_when_run_as_script -v
```

## 测试统计

- 总测试数：115
- 测试文件：4
- 测试类别：正例、边界、反例
- 核心知识点：10 个

## 测试特点

1. **独立性**：每个测试独立运行，不依赖其他测试
2. **可读性**：测试名称清晰描述测试内容和预期结果
3. **全面性**：覆盖正例、边界、反例
4. **实用性**：测试实际使用场景，而不仅仅是语法
5. **教育性**：测试代码本身也是学习材料

## 注意事项

1. 某些测试使用 `subprocess` 创建临时脚本，以验证 `__name__` 的值
2. 测试中的"反例"有些是概念性的（说明错误用法），有些会实际触发异常
3. 相对导入的测试需要创建临时包结构
4. 所有临时文件和目录在测试结束后会被自动清理
