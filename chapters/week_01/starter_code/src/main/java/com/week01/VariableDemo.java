package com.week01;

/**
 * 练习 3：变量声明练习
 *
 * <p>理解静态类型的变量声明。
 * 与 Python 不同，Java 要求在声明变量时必须指定类型。
 *
 * 静态类型的好处：
 * - 编译期就能发现类型错误
 * - 代码更易读（变量类型一目了然）
 * - IDE 可以提供更智能的提示
 */
public class VariableDemo {

    public static void main(String[] args) {
        // String：字符串类型（注意 S 大写）
        String name = "张三";
        String job = "软件工程师";
        String email = "zhangsan@example.com";
        String phone = "138-0000-0000";

        // int：整数类型（没有小数）
        int age = 28;

        // double：浮点数类型（有小数）
        double yearsOfExp = 5.5;

        // boolean：布尔类型（只有 true 或 false）
        boolean isEmployed = true;

        // char：单个字符（用单引号）
        char grade = 'A';

        // 输出变量：使用 + 进行字符串拼接
        System.out.println("================================");
        System.out.println("       " + name + "的个人名片");
        System.out.println("================================");
        System.out.println("职位：" + job);
        System.out.println("年龄：" + age + "岁");
        System.out.println("工作经验：" + yearsOfExp + "年");
        System.out.println("邮箱：" + email);
        System.out.println("电话：" + phone);
        System.out.println("在职状态：" + isEmployed);
        System.out.println("绩效等级：" + grade);
        System.out.println("================================");
    }
}
