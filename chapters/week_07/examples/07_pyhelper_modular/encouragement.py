"""
encouragement.py - 鼓励语模块

职责：负责生成鼓励语和学习建议

功能：
- get_mood()：获取用户心情
- get_advice()：根据心情返回建议
- show_advice()：显示学习建议

运行方式（测试）：
  cd chapters/week_07/examples/07_pyhelper_modular
  python3 encouragement.py

导入方式：
  from encouragement import show_advice, get_mood, get_advice
"""


def get_mood():
    """获取用户心情"""
    print("\n今天心情怎么样？")
    print("1. 充满干劲")
    print("2. 一般般")
    print("3. 有点累")

    try:
        mood = int(input("请输入你的心情（1-3）："))
        if mood in [1, 2, 3]:
            return str(mood)
        print("输入无效，默认为一般般")
        return "2"
    except ValueError:
        print("输入无效，默认为一般般")
        return "2"


def get_advice(mood):
    """根据心情返回建议"""
    if mood == "1":
        return "太好了！推荐你今天挑战一个新概念，比如异常处理或模块化。"
    elif mood == "2":
        return "那就做点巩固练习吧，写几个小例子，熟悉一下异常处理。"
    elif mood == "3":
        return "累了就休息一下吧，今天可以只看视频不动手，或者写 10 分钟代码就停。"
    else:
        return "写点巩固练习最稳妥。"


def show_advice():
    """显示学习建议"""
    mood = get_mood()
    advice = get_advice(mood)
    print(f"\n建议：{advice}")


# =====================
# 测试代码
# =====================

if __name__ == "__main__":
    print("=== 测试 encouragement 模块 ===")

    mood = get_mood()
    advice = get_advice(mood)
    print(f"\n建议：{advice}")

    print("\n✓ 测试完成！")
