"""
Week 04 作业参考实现

本文件是 Starter Code 的参考答案，供学生在遇到困难时查看。
实现了基础作业、进阶作业和挑战作业的所有函数。

注意：这只是一个参考实现，不是唯一正确的答案。
编程有多种正确的写法，鼓励学生探索自己的实现方式。

参考方式：
- 先自己尝试实现
- 遇到困难时，查看对应函数的参考实现
- 理解思路后，用自己的方式重新写一遍

运行方式：
    python3 -m pytest chapters/week_04/tests/test_assignment.py -q
"""

# ========================================
# 基础作业（40 分）
# ========================================

def add_score(scores, new_score):
    """
    添加一个新成绩到列表末尾
    """
    scores.append(new_score)
    return scores


def remove_failing_scores(scores):
    """
    删除所有不及格的成绩（< 60 分）

    方法：创建新列表，避免在遍历时修改原列表
    """
    # 方法1：列表推导式（Pythonic）
    result = [score for score in scores if score >= 60]
    return result

    # 方法2：传统循环（更易理解）
    # result = []
    # for score in scores:
    #     if score >= 60:
    #         result.append(score)
    # return result


def get_top_scores(scores, n=3):
    """
    获取前 n 个最高分

    方法：先排序（降序），再切片
    """
    if not scores:  # 处理空列表
        return []

    # sorted() 返回新列表，不修改原列表
    sorted_scores = sorted(scores, reverse=True)

    # 处理 n > len(scores) 的情况
    return sorted_scores[:n]


def get_student_score(scores_dict, name):
    """
    获取学生的成绩

    方法：使用 .get() 方法安全访问字典
    """
    # .get() 方法：键存在返回对应值，不存在返回 None
    return scores_dict.get(name)


def count_pass_fail(scores_dict, passing_score=60):
    """
    统计及格和不及格人数

    方法：遍历字典的值，分别计数
    """
    pass_count = 0
    fail_count = 0

    for score in scores_dict.values():
        if score >= passing_score:
            pass_count += 1
        else:
            fail_count += 1

    return {"pass": pass_count, "fail": fail_count}


def find_top_student(scores_dict):
    """
    找出最高分的学生

    方法1：手动遍历
    """
    if not scores_dict:  # 处理空字典
        return None

    max_score = None
    max_name = None

    for name, score in scores_dict.items():
        if max_score is None or score > max_score:
            max_score = score
            max_name = name

    return max_name

    # 方法2：使用 max() 函数（Pythonic）
    # return max(scores_dict, key=scores_dict.get)


def generate_report(scores_dict):
    """
    生成格式化的成绩单

    方法：遍历字典，判断等级，拼接字符串
    """
    lines = ["=== 成绩单 ==="]

    for name, score in scores_dict.items():
        # 判断等级
        if score >= 90:
            level = "优秀"
        elif score >= 80:
            level = "良好"
        elif score >= 60:
            level = "及格"
        else:
            level = "不及格"

        lines.append(f"{name}: {score} ({level})")

    # 用换行符连接所有行
    return "\n".join(lines)


# ========================================
# 进阶作业（30 分）
# ========================================

def analyze_scores(scores_list):
    """
    分析成绩列表，返回统计信息

    方法：使用内置函数 + 遍历
    """
    if not scores_list:  # 处理空列表
        return {
            "count": 0,
            "average": 0,
            "max": 0,
            "min": 0,
            "pass_rate": 0.0
        }

    count = len(scores_list)
    total = sum(scores_list)
    average = round(total / count, 1)  # 保留1位小数
    max_score = max(scores_list)
    min_score = min(scores_list)

    # 计算及格率
    pass_count = sum(1 for score in scores_list if score >= 60)
    pass_rate = pass_count / count

    return {
        "count": count,
        "average": average,
        "max": max_score,
        "min": min_score,
        "pass_rate": pass_rate
    }


def group_by_grade(scores_dict):
    """
    按等级分组学生

    方法：初始化所有等级列表，遍历填充
    """
    # 初始化四个等级的空列表
    groups = {
        "优秀": [],
        "良好": [],
        "及格": [],
        "不及格": []
    }

    # 遍历成绩字典，按等级分组
    for name, score in scores_dict.items():
        if score >= 90:
            groups["优秀"].append(name)
        elif score >= 80:
            groups["良好"].append(name)
        elif score >= 60:
            groups["及格"].append(name)
        else:
            groups["不及格"].append(name)

    return groups


# ========================================
# 挑战作业（30 分）
# ========================================

def add_class(school, class_name, scores_dict):
    """
    添加一个班级到学校数据结构中

    方法：直接给字典添加键值对
    """
    # 字典的添加/修改是同一语法
    school[class_name] = scores_dict
    return school


def _calculate_average(scores_dict):
    """
    辅助函数：计算成绩字典的平均分

    提取公共逻辑，避免重复代码
    """
    if not scores_dict:
        return 0.0

    total = sum(scores_dict.values())
    count = len(scores_dict)
    return round(total / count, 1)


def get_class_average(school, class_name):
    """
    获取某个班级的平均分

    方法：先检查班级是否存在，再计算平均分
    """
    # 检查班级是否存在
    if class_name not in school:
        return None

    # 使用辅助函数计算平均分
    return _calculate_average(school[class_name])


def compare_classes(school, class1, class2):
    """
    比较两个班级的平均分

    方法：分别计算两个班级的平均分，比较后格式化输出
    """
    # 获取两个班级的平均分
    avg1 = get_class_average(school, class1)
    avg2 = get_class_average(school, class2)

    # 处理班级不存在的情况
    if avg1 is None:
        return f"{class1} 班级不存在"
    if avg2 is None:
        return f"{class2} 班级不存在"

    # 比较平均分
    diff = avg1 - avg2

    if diff > 0:
        return f"{class1} 比 {class2} 高 {diff:.1f} 分"
    elif diff < 0:
        return f"{class2} 比 {class1} 高 {-diff:.1f} 分"
    else:
        return f"{class1} 和 {class2} 平均分相同"


def find_top_class(school):
    """
    找出平均分最高的班级

    方法：遍历所有班级，计算平均分，找最大值
    """
    if not school:  # 处理空学校
        return None

    max_avg = -1
    top_class = None

    for class_name, scores_dict in school.items():
        avg = _calculate_average(scores_dict)
        if avg > max_avg:
            max_avg = avg
            top_class = class_name

    return top_class

    # 方法2：使用 max() 函数（Pythonic）
    # return max(school, key=lambda name: _calculate_average(school[name]))


# ========================================
# 主程序：演示用法
# ========================================

if __name__ == "__main__":
    print("=== 基础作业演示 ===\n")

    # 演示 add_score
    scores = [85, 92]
    print(f"原始成绩：{scores}")
    add_score(scores, 78)
    print(f"添加后：{scores}\n")

    # 演示 remove_failing_scores
    scores = [85, 45, 92, 58, 78]
    print(f"原始成绩：{scores}")
    result = remove_failing_scores(scores)
    print(f"删除不及格后：{result}\n")

    # 演示 get_top_scores
    scores = [85, 92, 78, 90, 88]
    top = get_top_scores(scores, 3)
    print(f"成绩：{scores}")
    print(f"前三名：{top}\n")

    # 演示字典操作
    scores_dict = {"小北": 85, "阿码": 92, "老潘": 78, "小红": 58}
    print(f"成绩字典：{scores_dict}")
    print(f"小北的成绩：{get_student_score(scores_dict, '小北')}")
    print(f"统计：{count_pass_fail(scores_dict)}")
    print(f"最高分：{find_top_student(scores_dict)}\n")

    # 演示成绩单生成
    report = generate_report(scores_dict)
    print(report)
    print()

    print("=== 进阶作业演示 ===\n")

    # 演示成绩分析
    scores_list = [85, 92, 78, 45, 88]
    analysis = analyze_scores(scores_list)
    print(f"成绩：{scores_list}")
    print(f"分析：{analysis}\n")

    # 演示等级分组
    groups = group_by_grade(scores_dict)
    print(f"按等级分组：{groups}\n")

    print("=== 挑战作业演示 ===\n")

    # 演示多班级管理
    school = {
        "一班": {"小北": 85, "阿码": 92, "老潘": 78},
        "二班": {"小红": 90, "小明": 88, "小李": 95}
    }

    print(f"学校数据：{school}\n")
    print(f"一班平均分：{get_class_average(school, '一班')}")
    print(f"二班平均分：{get_class_average(school, '二班')}")
    print(f"比较：{compare_classes(school, '一班', '二班')}")
    print(f"最高班级：{find_top_class(school)}")
