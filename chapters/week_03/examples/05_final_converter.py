"""
示例：完整的模块化换算器

本例演示如何把一个大问题分解为多个小函数，设计清晰的调用关系
这是 Week 03 贯穿案例的最终版本

运行方式：python3 chapters/week_03/examples/05_final_converter.py
预期输出：一个有菜单的、模块化的单位换算器
"""


# =====================
# 核心换算函数（只做计算）
# =====================

def km_to_miles(km):
    """公里转英里"""
    return km * 0.621371


def kg_to_pounds(kg):
    """公斤转磅"""
    return kg * 2.20462


def celsius_to_fahrenheit(c):
    """摄氏度转华氏度"""
    return c * 9/5 + 32


# =====================
# 菜单和输入函数
# =====================

def show_menu():
    """显示菜单"""
    print("\n=== 单位换算器 ===")
    print("1. 公里 → 英里")
    print("2. 公斤 → 磅")
    print("3. 摄氏度 → 华氏度")
    print("4. 退出")


def get_choice():
    """获取用户选择，带输入验证（用到 Week 02 的 while 循环）"""
    while True:
        choice = input("请选择（1-4）：")
        if choice in ["1", "2", "3", "4"]:
            return choice
        print("无效输入，请输入 1-4")


# =====================
# 执行换算的函数
# =====================

def do_conversion(choice):
    """根据用户选择执行换算"""
    if choice == "1":
        km = float(input("输入公里数："))
        result = km_to_miles(km)
        print(f"{km} 公里 = {result:.2f} 英里")
    elif choice == "2":
        kg = float(input("输入公斤数："))
        result = kg_to_pounds(kg)
        print(f"{kg} 公斤 = {result:.2f} 磅")
    elif choice == "3":
        c = float(input("输入摄氏度："))
        result = celsius_to_fahrenheit(c)
        print(f"{c}°C = {result:.2f}°F")


# =====================
# 主函数
# =====================

def main():
    """主函数——把所有函数串联起来"""
    while True:
        show_menu()
        choice = get_choice()

        if choice == "4":
            print("再见！")
            break

        do_conversion(choice)


# =====================
# 启动程序
# =====================

if __name__ == "__main__":
    main()


# =====================
# 设计说明
# =====================

"""
这个程序把 Week 02 学的 while 循环和 Week 03 学的函数结合起来了。

设计原则：
1. 每个函数只做一件事（Single Responsibility Principle）
2. 函数名清晰表达意图
3. 核心换算函数只做计算，不涉及输入输出
4. 菜单函数负责显示，输入函数负责获取和验证
5. main() 函数是入口，负责整体流程控制

优势：
- 如果想加新的换算类型，只需：
  1. 写一个新的换算函数（如 miles_to_km）
  2. 在 show_menu() 里加一个选项
  3. 在 do_conversion() 里加一个 elif 分支

- 改动是局部的，不会影响其他部分

- 每个函数都可以独立测试

关于作用域：
- 所有变量都是局部变量，不依赖全局变量
- 函数通过参数获取输入，通过返回值输出结果
- 这是推荐的函数设计模式
"""
