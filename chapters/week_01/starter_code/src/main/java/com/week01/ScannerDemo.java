package com.week01;

import java.util.Scanner;

/**
 * 练习 4：Scanner 输入练习
 *
 * <p>学习如何读取用户输入。
 * Scanner 是 Java 标准库中用于读取输入的工具类。
 *
 * 常用方法：
 * - nextLine()：读取一整行字符串
 * - nextInt()：读取一个整数（注意：会遗留换行符！）
 * - nextDouble()：读取一个浮点数
 *
 * 重要提示：
 * 混用 nextInt()/nextDouble() 和 nextLine() 时要注意换行符问题。
 * 建议统一使用 nextLine() 读取，然后用 Integer.parseInt() 转换。
 */
public class ScannerDemo {

    public static void main(String[] args) {
        // 创建 Scanner 对象，从标准输入（键盘）读取
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 简单的交互程序 ===");
        System.out.println();

        // 读取字符串
        System.out.print("请输入你的名字：");
        String name = scanner.nextLine();  // 读取一整行

        // 读取职位
        System.out.print("请输入你的职位：");
        String jobTitle = scanner.nextLine();

        // 读取年龄（推荐方式）
        System.out.print("请输入你的年龄：");
        int age = Integer.parseInt(scanner.nextLine());  // 先读整行，再转整数

        // 读取工作年限（浮点数）
        System.out.print("请输入你的工作年限：");
        double yearsOfExp = Double.parseDouble(scanner.nextLine());

        // 输出结果
        System.out.println();
        System.out.println("=== 你的信息 ===");
        System.out.println("姓名：" + name);
        System.out.println("职位：" + jobTitle);
        System.out.println("年龄：" + age + "岁");
        System.out.println("工作年限：" + yearsOfExp + "年");

        // 记得关闭 Scanner（释放资源）
        scanner.close();
    }
}
