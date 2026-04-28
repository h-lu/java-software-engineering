package com.week01;

import java.util.Scanner;

/**
 * 练习 4：Scanner 输入练习。
 */
public class ScannerDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // TODO: 用 nextLine 读取姓名、职位和年龄。
        System.out.print("请输入你的名字：");
        String name = scanner.nextLine();

        System.out.print("请输入你的职位：");
        String job = scanner.nextLine();

        System.out.print("请输入你的年龄：");
        String ageText = scanner.nextLine();

        // TODO: 把 ageText 转成 int，并按作业示例输出。
        System.out.println("姓名：" + name);
        System.out.println("职位：" + job);
        System.out.println("年龄输入：" + ageText);

        scanner.close();
    }
}
