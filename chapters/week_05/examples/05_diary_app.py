"""
示例：完整的日记本工具

本例演示：
- 写日记、读日记、搜日记
- 用 with 语句确保文件正确关闭
- 用 pathlib 管理文件路径
- 用 UTF-8 编码避免中文乱码
- 用追加模式 ("a") 持续记录

运行方式：python3 chapters/week_05/examples/05_diary_app.py
预期输出：一个交互式日记本工具
"""

from datetime import datetime
from pathlib import Path


# =====================
# 日记本核心功能
# =====================

class DiaryApp:
    """日记本应用类"""

    def __init__(self, filename="diary.txt"):
        """初始化日记本"""
        self.filename = Path(filename)
        print(f"✓ 日记本已初始化：{self.filename}")

    def add_entry(self, content):
        """
        添加一条日记（追加模式）

        content: 日记内容（字符串）
        """
        # 获取当前日期
        today = datetime.now().strftime("%Y-%m-%d")
        timestamp = datetime.now().strftime("%H:%M")

        # 构建日记条目
        entry = f"[{today} {timestamp}] {content}\n"

        # 用追加模式写入（用 with 和 UTF-8）
        with open(self.filename, "a", encoding="utf-8") as file:
            file.write(entry)

        print(f"✓ 日记已添加：{entry.strip()}")

    def read_all(self):
        """
        读取所有日记

        返回：日记列表（每个元素是一行）
        """
        # 检查文件是否存在
        if not self.filename.exists():
            print("✗ 日记本还是空的，快写第一篇吧！")
            return []

        # 读取所有内容（用 pathlib 的快捷方法）
        content = self.filename.read_text(encoding="utf-8")

        # 按行分割，去掉空行
        lines = [line.strip() for line in content.split("\n") if line.strip()]

        return lines

    def search(self, keyword):
        """
        按关键词搜索日记

        keyword: 关键词（字符串）
        返回：匹配的日记列表
        """
        all_entries = self.read_all()

        # 过滤包含关键词的日记
        matching = [entry for entry in all_entries if keyword in entry]

        return matching

    def show_all(self):
        """显示所有日记"""
        entries = self.read_all()

        if not entries:
            return

        print(f"\n=== 所有日记（共 {len(entries)} 篇）===")
        for entry in entries:
            print(f"  {entry}")

    def show_search_results(self, keyword):
        """显示搜索结果"""
        results = self.search(keyword)

        if not results:
            print(f"\n✗ 没有找到包含'{keyword}'的日记")
            return

        print(f"\n=== 搜索'{keyword}'（共 {len(results)} 条）===")
        for result in results:
            print(f"  {result}")

    def clear_all(self):
        """清空所有日记（危险操作！）"""
        confirm = input("⚠️  确定要清空所有日记吗？（yes/no）：")
        if confirm.lower() == "yes":
            self.filename.write_text("", encoding="utf-8")
            print("✓ 日记本已清空")
        else:
            print("✓ 已取消")


# =====================
# 交互式菜单
# =====================

def print_menu():
    """打印菜单"""
    print("\n" + "=" * 40)
    print("  📔 日记本工具")
    print("=" * 40)
    print("1. 写日记")
    print("2. 查看所有日记")
    print("3. 搜索日记")
    print("4. 清空日记（危险）")
    print("5. 退出")
    print("-" * 40)


def get_choice():
    """获取用户选择"""
    while True:
        choice = input("\n请选择（1-5）：")
        if choice in ["1", "2", "3", "4", "5"]:
            return choice
        print("✗ 无效输入，请输入 1-5")


# =====================
# 主程序
# =====================

def main():
    """主函数"""
    # 创建日记本实例
    diary = DiaryApp("my_diary.txt")

    print("欢迎使用日记本工具！")
    print("在这里记录你的学习心得和日常生活")

    while True:
        print_menu()
        choice = get_choice()

        if choice == "1":
            # 写日记
            print("\n--- 写日记 ---")
            content = input("请输入日记内容：")
            if content.strip():
                diary.add_entry(content)
            else:
                print("✗ 日记内容不能为空")

        elif choice == "2":
            # 查看所有日记
            print("\n--- 查看日记 ---")
            diary.show_all()

        elif choice == "3":
            # 搜索日记
            print("\n--- 搜索日记 ---")
            keyword = input("请输入关键词：")
            if keyword.strip():
                diary.show_search_results(keyword)
            else:
                print("✗ 关键词不能为空")

        elif choice == "4":
            # 清空日记
            print("\n--- 清空日记 ---")
            diary.clear_all()

        elif choice == "5":
            # 退出
            print("\n再见！祝你每天开心！🎉")
            break

        print()


# =====================
# 测试代码
# =====================

def test_diary_app():
    """测试日记本功能"""
    print("=== 测试日记本 ===")

    # 创建测试日记本
    diary = DiaryApp("test_diary.txt")

    # 清空旧数据
    diary.filename.write_text("", encoding="utf-8")

    # 添加几篇日记
    diary.add_entry("今天学会了文件操作")
    diary.add_entry("with 语句很方便")
    diary.add_entry("UTF-8 编码避免乱码")

    # 查看所有日记
    print("\n--- 所有日记 ---")
    diary.show_all()

    # 搜索日记
    print("\n--- 搜索'文件' ---")
    diary.show_search_results("文件")

    print("\n✓ 测试完成！")


# =====================
# 启动程序
# =====================

if __name__ == "__main__":
    import sys

    # 如果命令行参数是 --test，运行测试
    if len(sys.argv) > 1 and sys.argv[1] == "--test":
        test_diary_app()
    else:
        # 否则运行交互式程序
        main()


# =====================
# 本周知识点总结
# =====================

"""
本周用到的知识：

1. 文件读写（Week 05 新学）
   - with open() as file: 自动关闭文件
   - "a" 模式：追加写入
   - encoding="utf-8"：避免中文乱码

2. pathlib（Week 05 新学）
   - Path(filename)：创建路径对象
   - path.exists()：检查文件是否存在
   - path.read_text()：快捷读取方法

3. 字符串方法（Week 01 复习）
   - line.strip()：去掉首尾空白字符
   - keyword in entry：判断关键词是否存在

4. 列表推导式（Week 04 复习）
   - [entry for entry in all_entries if keyword in entry]
   - 过滤包含关键词的日记

5. 类和方法（Week 03 复习）
   - class DiaryApp: 定义类
   - self.filename：实例属性
   - self.add_entry()：实例方法

6. 条件判断（Week 02 复习）
   - if choice == "1": 菜单选择
   - if content.strip(): 检查空输入

本周改进：
- 从内存存储（字典）升级到文件存储
- 数据持久化：程序关闭后数据不丢失
- 用追加模式持续记录
- 用 with 语句确保文件正确关闭
- 用 pathlib 管理路径（跨平台兼容）
- 用 UTF-8 编码支持中文
"""
