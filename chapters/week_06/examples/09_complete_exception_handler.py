"""
示例：完整的 try/except/else/finally 结构

本例演示：完整的异常处理结构，包含所有四个子句
运行方式：python3 chapters/week_06/examples/09_complete_exception_handler.py
预期输出：
  - 成功：try → else → finally
  - 失败：try → except → finally
"""

def safe_divide():
    """安全的除法器（完整版）"""
    try:
        print("=== 除法计算器 ===")
        numerator = int(input("请输入分子："))
        denominator = int(input("请输入分母："))

        result = numerator / denominator

    except ValueError:
        print("错误：请输入数字，不要输入文字")
        return None

    except ZeroDivisionError:
        print("错误：分母不能为零")
        return None

    else:
        # 只有没出错时才会执行
        print(f"计算成功！结果：{result}")
        return result

    finally:
        # 无论是否出错，都会执行
        print("=== 计算结束 ===\n")


# 测试代码
if __name__ == "__main__":
    print("=== 测试 1：正常输入 ===")
    safe_divide()

    print("=== 测试 2：除以零 ===")
    safe_divide()

    print("=== 测试 3：非数字输入 ===")
    safe_divide()

# 执行流程：
# 成功时：try → else → finally
# 失败时：try → except → finally
#
# 结构说明：
# - try：可能出错的代码
# - except：出错了怎么处理
# - else：没出错时做什么（可选）
# - finally：无论是否出错都做什么（可选）
