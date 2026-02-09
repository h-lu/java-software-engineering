"""
示例：最基础的 print() 函数用法。

运行方式：python3 chapters/week_01/examples/01_hello.py
预期输出：
    Hello, World!
    姓名： 张三
    年龄： 25
    加载中...完成！
"""

# 最基本的输出
print("Hello, World!")

# print() 可以一次输出多个内容，自动用空格隔开
print("姓名：", "张三")
print("年龄：", 25)

# 使用 end 参数控制结尾（默认是换行符 \n）
print("加载中", end="")
print("...", end="")
print("完成！")
