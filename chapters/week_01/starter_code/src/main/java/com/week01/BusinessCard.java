package com.week01;

import java.util.Scanner;

/**
 * 练习 5：完整的名片生成器
 *
 * <p>这是本周的主要练习。
 * 你需要完成一个能读取用户输入并生成格式化名片的程序。
 *
 * 要求：
 * 1. 读取用户的姓名、职位、邮箱、年龄、工作年限
 * 2. 使用正确的数据类型存储
 * 3. 输出格式化的名片
 * 4. 变量命名清晰，符合 Java 规范
 *
 * 挑战（可选）：
 * - 使用 printf 格式化输出
 * - 添加输入验证
 * - 支持生成多张名片
 */
public class BusinessCard {

    public static void main(String[] args) {
        // TODO: 创建 Scanner 对象
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 名片生成器 ===");
        System.out.println();

        // TODO: 读取用户输入
        // 提示：使用 scanner.nextLine() 读取字符串
        // 提示：使用 Integer.parseInt(scanner.nextLine()) 读取整数
        // 提示：使用 Double.parseDouble(scanner.nextLine()) 读取浮点数

        System.out.print("请输入姓名：");
        String name = scanner.nextLine();

        System.out.print("请输入职位：");
        String jobTitle = scanner.nextLine();

        System.out.print("请输入邮箱：");
        String email = scanner.nextLine();

        System.out.print("请输入年龄：");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("请输入工作年限：");
        double yearsOfExp = Double.parseDouble(scanner.nextLine());

        // TODO: 输出格式化的名片
        // 提示：使用 System.out.println() 输出
        // 挑战：使用 printf 格式化输出，让边框对齐

        System.out.println();
        System.out.println("================================");
        System.out.println("       " + name + " 的个人名片");
        System.out.println("================================");
        System.out.println("职位：" + jobTitle);
        System.out.println("年龄：" + age + "岁");
        System.out.println("工作经验：" + yearsOfExp + "年");
        System.out.println("邮箱：" + email);
        System.out.println("================================");

        // 记得关闭 Scanner
        scanner.close();
    }
}
