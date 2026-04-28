package com.week01;

import java.util.Scanner;

/**
 * Week 01 练习 6 骨架：对比字符串输入与类型转换。
 */
public class TypeComparison {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入年龄：");
        String ageText = scanner.nextLine();

        // 待办：观察字符串拼接和整数加法的区别。
        String textResult = ageText + "10";
        int age = Integer.parseInt(ageText);
        int ageAfter10Years = age + 10;

        System.out.println("字符串拼接结果：" + textResult);
        System.out.println("10 年后的年龄：" + ageAfter10Years);

        scanner.close();
    }
}
