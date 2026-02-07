# week_01：从零到可运行：Hello Python + 工程基线

## 学习目标

完成本周学习后，你将能够：

1. 编写并运行最简单的 Python 程序（print、变量、基本输入输出）。
2. 理解变量是"标签"而非"盒子"，避免常见的类型错误。
3. 使用 `validate_week.py` 和 `pytest` 验证代码是否符合预期。
4. 使用 Git 进行基本的版本控制（init、status、add、commit、log）。
5. 理解 DoD（Definition of Done）概念，把"写完代码"变成"可交付"。

## 先修要求

- 无需编程基础（Week 01 面向零基础读者）。
- 已安装 Python 3.9+（运行 `python3 --version` 检查）。
- 已安装 pytest（本书用它做自动验证）：`python3 -m pip install pytest`。

## 1. 为什么学 Python

### 编程是什么

编程（Programming）就是**用精确的语言告诉计算机该做什么**。计算机非常"笨"：它只会严格按照你写的指令执行，不会猜测你的意图。但计算机也非常"快"：一旦指令正确，它可以在毫秒级完成人类需要数小时的工作。

学习编程的核心价值不在于记住语法，而在于培养**结构化思考**的能力：如何把一个大问题拆解成可执行的小步骤。

### Python 的特点

Python 是一门**高级、解释型、通用**的编程语言：

- **高级**：语法接近自然语言，不用关心内存分配等底层细节。
- **解释型**：代码不需要编译，直接运行源代码（由 Python 解释器逐行执行）。
- **通用**：可用于 Web 开发、数据分析、自动化脚本、人工智能等领域。

Python 的设计哲学强调**可读性**。一个好的 Python 程序读起来像伪代码，这使它成为初学者的理想选择。

## 2. 运行第一个程序（Hello World）

### 概念：脚本 vs 交互式

Python 代码有两种运行方式：

1. **交互式（REPL）**：在终端输入 `python3`，逐行执行代码，适合实验。
2. **脚本模式**：把代码写入 `.py` 文件，用 `python3 文件名.py` 执行，适合保存和复用。

REPL（Read-Eval-Print Loop）是"读取-求值-输出"的循环，适合快速验证想法；脚本模式是工程实践的标准方式。

### 正例：Hello, World!

创建文件 `hello.py`：

```python
print("Hello, World!")
```

运行：

```bash
python3 hello.py
```

输出：

```
Hello, World!
```

`print()` 是一个**内置函数**（built-in function），作用是把内容输出到屏幕。括号内的 `"Hello, World!"` 是一个**字符串**（string），即一串文本字符。

### 常见错误

**错误 1：引号不匹配**

```python
print("Hello, World!')
```

报错：

```
SyntaxError: unterminated string literal
```

**原因**：Python 中字符串可以用单引号 `'...'` 或双引号 `"..."` 包裹，但必须成对匹配。开头用双引号，结尾也必须用双引号。

**错误 2：括号不匹配**

```python
print("Hello, World!"
```

报错：

```
SyntaxError: '(' was never closed
```

**原因**：每个左括号 `(` 必须有对应的右括号 `)`。

**错误 3：大小写敏感**

```python
Print("Hello, World!")
```

报错：

```
NameError: name 'Print' is not defined
```

**原因**：Python 区分大小写。`print` 是小写的，首字母大写的 `Print` 会被认为是另一个名字。

### 小结

- 脚本模式是工程实践的标准，代码保存在 `.py` 文件中可重复执行。
- `print()` 用于输出，括号内是字符串，用引号包裹。
- Python 是大小写敏感的，引号和括号必须成对匹配。
- 遇到 `SyntaxError` 时，首先检查引号、括号是否配对。

## 3. 变量与数据类型

### 概念：变量是标签

变量（Variable）是用来存储数据的**名字**。关键认知：**变量不是装数据的盒子，而是贴在数据上的标签**。

```python
name = "Alice"
age = 25
```

这里发生了什么：

1. Python 在内存中创建字符串 `"Alice"` 和整数 `25`。
2. 把名字 `name` 贴在 `"Alice"` 上，`age` 贴在 `25` 上。
3. 之后通过名字就能找到对应的数据。

多个变量可以指向同一个数据：

```python
a = [1, 2, 3]  # 一个列表
b = a          # b 也指向同一个列表
b.append(4)    # 通过 b 修改
print(a)       # [1, 2, 3, 4] -- a 也变了！
```

### 正例：基本数据类型

```python
# 字符串（str）：文本
name = "Alice"
greeting = '你好'

# 整数（int）：没有小数部分的数字
age = 25
year = 2024

# 浮点数（float）：有小数部分的数字
pi = 3.14159
price = 19.99

# 布尔值（bool）：真或假
is_student = True
has_graduated = False

print(name, age, pi, is_student)
```

使用 `type()` 函数查看变量的类型：

```python
print(type(name))        # <class 'str'>
print(type(age))         # <class 'int'>
print(type(pi))          # <class 'float'>
print(type(is_student))  # <class 'bool'>
```

### 常见错误

**错误 1：变量未定义就使用**

```python
print(message)
```

报错：

```
NameError: name 'message' is not defined
```

**原因**：使用变量前必须先赋值。正确的做法是先定义：`message = "Hello"`。

**错误 2：类型错误（字符串 + 数字）**

```python
age = 25
print("我今年" + age + "岁")
```

报错：

```
TypeError: can only concatenate str (not "int") to str
```

**原因**：Python 不会自动把数字转成字符串。解决方法：

```python
# 方法 1：用 str() 转换
print("我今年" + str(age) + "岁")

# 方法 2：用逗号（自动加空格）
print("我今年", age, "岁")

# 方法 3：用 f-string（推荐）
print(f"我今年{age}岁")
```

**错误 3：变量名不符合规则**

```python
2nd_name = "Bob"      # 不能以数字开头
my-name = "Bob"       # 不能包含连字符
my name = "Bob"       # 不能包含空格
class = "Math"        # 不能用保留字
```

**变量命名规则**：

- 只能包含字母、数字、下划线
- 不能以数字开头
- 不能是 Python 的保留字（如 `if`, `for`, `class` 等）
- 惯例：用下划线分隔的小写字母（`snake_case`）

### 小结

- 变量是贴在数据上的标签，不是装数据的盒子。
- Python 的基本数据类型包括 `str`（字符串）、`int`（整数）、`float`（浮点数）、`bool`（布尔值）。
- 字符串和数字不能直接相加，需要用 `str()` 转换或使用 f-string。
- 变量名只能包含字母、数字、下划线，不能以数字开头。

## 4. 基本输入输出

### 概念：程序与用户的交互

程序通常需要与用户交互：

- **输入（Input）**：从用户获取数据
- **处理（Process）**：对数据进行计算或转换
- **输出（Output）**：把结果展示给用户

这是最基本的程序结构模式。

### 正例：input() 和 print()

```python
# 获取用户输入
name = input("请输入你的名字：")

# 处理（这里只是简单的拼接）
message = "你好，" + name + "！"

# 输出结果
print(message)
```

运行示例：

```
请输入你的名字：Alice
你好，Alice！
```

更实用的例子：简单计算器

```python
# 获取两个数字
a = input("请输入第一个数字：")
b = input("请输入第二个数字：")

# 转换为整数后相加
sum_result = int(a) + int(b)

# 输出结果
print(f"{a} + {b} = {sum_result}")
```

### 常见错误

**错误：忘记 input 返回的是字符串**

```python
age = input("请输入你的年龄：")
next_year = age + 1  # 错误！
```

报错：

```
TypeError: can only concatenate str (not "int") to str
```

**原因**：`input()` 函数**总是返回字符串**，即使用户输入的是数字。需要显式转换：

```python
age = input("请输入你的年龄：")
age = int(age)       # 转换为整数
next_year = age + 1  # 正确
print(f"明年你就{next_year}岁了")
```

或者简写为一行：

```python
age = int(input("请输入你的年龄："))
```

**注意**：如果用户输入的不是数字（比如输入 "abc"），`int()` 会报错。Week 06 会学习如何处理这种错误。

### 小结

- `input()` 用于获取用户输入，参数是提示文本。
- `input()` 始终返回字符串，数字输入需要用 `int()` 或 `float()` 转换。
- 程序的基本模式是：输入 -> 处理 -> 输出。

## 5. 本周 Agentic 训练

### DoD 概念详解

**DoD（Definition of Done，完成的定义）** 是工程化思维的核心概念。它回答一个问题：**"什么情况下可以说这件事做完了？"**

没有 DoD 的"完成"往往是主观的：

- 开发者 A："我写完了！"（意思是代码写出来了）
- 开发者 B："我写完了！"（意思是代码写出来了，测试通过了，文档更新了）

DoD 把"完成"变成可验证的清单，消除歧义。

本书的每周 DoD 包含：

1. **文件齐全**：CHAPTER.md、ASSIGNMENT.md、RUBRIC.md、QA_REPORT.md、ANCHORS.yml、TERMS.yml
2. **代码可运行**：`pytest` 测试通过
3. **术语一致**：新术语已登记到 TERMS.yml 并合入 glossary.yml
4. **锚点可验证**：重要结论有对应的 ANCHORS.yml 条目
5. **QA 阻塞项已解决**：QA_REPORT.md 中没有未勾选的阻塞项

### validate_week.py 的使用

`validate_week.py` 是本书的自动化校验工具，检查每周交付物是否符合 DoD。

基本用法：

```bash
# 任务模式：运行测试
python3 scripts/validate_week.py --week week_01 --mode task

# 空闲模式：检查 QA 阻塞项
python3 scripts/validate_week.py --week week_01 --mode idle

# 发布模式：完整检查（用于发布前）
python3 scripts/validate_week.py --week week_01 --mode release
```

### pytest 的使用

pytest 是 Python 的测试框架，本书用它来自动验证代码行为。

运行本周测试：

```bash
python3 -m pytest chapters/week_01/tests -q
```

`-q` 参数表示"安静模式"，只显示关键信息。

测试通过会显示：

```
.
1 passed in 0.01s
```

测试失败会显示详细的错误信息，帮助你定位问题。

### Agentic 工作流四步循环

你每周都要重复同一个交付循环：

1. **Plan**：用 1 句话写清本周目标 + DoD
2. **Implement**：小步实现（建议至少 2 次提交：draft + verify）
3. **Verify**：运行校验与测试
4. **Reflect**：在 `QA_REPORT.md` 写 3 行复盘（卡点/定位方式/下次改进）

## 6. Git 基础

### 为什么需要版本控制

想象这样的场景：

- 你写了一个程序，保存为 `project.py`
- 你想尝试一个新功能，于是复制一份为 `project_v2.py`
- 又复制一份 `project_v2_backup.py`
- 几天后，你忘记了哪个版本是最新的，也不敢删除任何文件

**版本控制系统（Version Control System, VCS）** 就是解决这个问题的工具。它帮你：

- 记录每次修改的内容和时间
- 随时回退到之前的版本
- 比较不同版本的差异
- 协作时合并多人的修改

Git 是目前最流行的分布式版本控制系统。

### Git 核心概念

- **仓库（Repository）**：项目的完整历史记录，包含所有文件和变更历史
- **工作区（Working Directory）**：你实际编辑文件的目录
- **暂存区（Staging Area）**：准备提交的变更列表
- **提交（Commit）**：一次变更的快照，包含作者、时间、说明信息

### 本周必会命令

**1. git init：初始化仓库**

```bash
cd my_project
git init
```

这会创建一个 `.git` 目录，开始跟踪项目变更。

**2. git status：查看状态**

```bash
git status
```

显示哪些文件被修改了、哪些是新文件、哪些已准备好提交。

**3. git add：添加到暂存区**

```bash
git add hello.py          # 添加单个文件
git add -A                # 添加所有变更
```

**4. git commit：创建提交**

```bash
git commit -m "添加 hello.py"
```

`-m` 后面是提交信息，描述这次变更做了什么。好的提交信息应该：

- 用动词开头（添加、修复、更新、删除等）
- 简洁说明变更内容
- 50 个字符以内（第一行）

**5. git log：查看历史**

```bash
git log --oneline -n 10   # 简洁格式，显示最近 10 条
```

### 本地 vs 远端（概念）

- **本地仓库**：你电脑上的 Git 仓库
- **远端仓库（Remote）**：服务器上的 Git 仓库（如 GitHub、Gitea）

本周只涉及本地操作。Week 05 开始会学习如何与远端仓库交互（push/pull）。

### 常见坑

**只改文件不提交**：下周找不到"当时怎么改的"。至少做 draft + verify 两次提交。

```bash
# 第一次提交：草稿版本
git add -A
git commit -m "draft: 实现基本功能"

# 验证通过后，第二次提交
git add -A
git commit -m "verify: 通过所有测试"
```

### Pull Request（预告）

Gitea 上也叫 Pull Request，流程等价 GitHub：

1. 创建分支并 push
2. 开 PR（Pull Request）
3. Review（代码审查）
4. Merge（合并到主分支）

本周可选了解，Week 06 开始成为必做流程。参考：`shared/gitea_workflow.md`

## 7. 小结

本周我们建立了学习 Python 的工程基线：

1. **Python 基础**：学会了编写和运行最简单的程序，理解了变量是标签而非盒子，掌握了基本的输入输出。

2. **常见错误**：引号/括号不匹配、变量未定义、类型错误（字符串与数字混用）、忘记 input 返回字符串。

3. **Agentic 工作流**：理解了 DoD 概念，学会使用 `validate_week.py` 和 `pytest` 进行自动化验证。记住四步循环：Plan -> Implement -> Verify -> Reflect。

4. **Git 基础**：版本控制让你不再害怕修改代码，因为随时可以回退。核心命令：`init`、`status`、`add`、`commit`、`log`。

**下周预告**：Week 02 将学习控制流（if/for/while），让程序能够做选择和重复执行。

---

## 本周 DoD（Definition of Done）

发布前必须满足 `CLAUDE.md` 的 DoD 条目，并通过：

```bash
python3 scripts/validate_week.py --week week_01 --mode release
```

## 建议验证命令

```bash
# 1. 静态检查（文件齐全、术语一致、锚点格式）
python3 scripts/validate_week.py --week week_01 --mode idle

# 2. 运行测试
python3 -m pytest chapters/week_01/tests -q

# 3. 完整发布检查
python3 scripts/validate_week.py --week week_01 --mode release
```

## Git 本周要点

本周必会命令：

- `git status` —— 查看当前状态
- `git diff` —— 查看修改内容
- `git add -A` —— 添加所有变更
- `git commit -m "draft: ..."` —— 创建提交
- `git log --oneline -n 10` —— 查看最近历史

常见坑：

- 只改文件不提交：下周找不到"当时怎么改的"。至少做 draft + verify 两次提交。
