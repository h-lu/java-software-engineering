/**
 * 示例：简单计算器
 *
 * 这个示例展示了如何使用不同的基本数据类型和简单的算术运算。
 */
public class SimpleCalculator {
    public static void main(String[] args) {
        // 整数运算
        int a = 10;
        int b = 3;
        System.out.println("整数运算：");
        System.out.println(a + " + " + b + " = " + (a + b));
        System.out.println(a + " - " + b + " = " + (a - b));
        System.out.println(a + " * " + b + " = " + (a * b));
        System.out.println(a + " / " + b + " = " + (a / b));  // 整数除法，结果是 3
        System.out.println(a + " % " + b + " = " + (a % b));  // 取模，结果是 1

        System.out.println();

        // 浮点数运算
        double x = 10.0;
        double y = 3.0;
        System.out.println("浮点数运算：");
        System.out.println(x + " / " + y + " = " + (x / y));  // 浮点除法，结果是 3.333...
    }
}
