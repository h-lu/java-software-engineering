/**
 * 示例：Python vs Java 类型对比
 *
 * 这个示例展示了 Python 和 Java 在类型处理上的差异。
 * 帮助学生理解"静态类型"的价值。
 */
public class TypeComparison {
    public static void main(String[] args) {
        System.out.println("=== Python vs Java 类型对比 ===\n");

        // 场景 1：字符串拼接 vs 类型错误
        System.out.println("场景 1：年龄 + 10");
        String ageStr = "25";
        System.out.println("Python: '25' + 10 = '2510' (字符串拼接)");

        // Java 会在编译期就报错
        // int result = ageStr + 10;  // ❌ 编译错误：bad operand types for binary operator '+'
        System.out.println("Java: 编译期报错，不允许 String + int\n");

        // 场景 2：正确的做法
        int ageInt = Integer.parseInt(ageStr);
        int result = ageInt + 10;
        System.out.println("正确的做法：");
        System.out.println("int age = Integer.parseInt(\"25\");");
        System.out.println("int result = age + 10;");
        System.out.println("结果：" + result + "\n");

        // 场景 3：类型安全的收益
        System.out.println("场景 3：电商价格计算");
        double price = 100.0;
        double discount = 0.8;  // 20% off
        double finalPrice = price * discount;
        System.out.println("原价：" + price);
        System.out.println("折扣：" + discount);
        System.out.println("最终价格：" + finalPrice);

        // 如果有人错误地把 discount 写成字符串
        // String discountStr = "8折";
        // double wrong = price * discountStr;  // ❌ 编译错误：bad operand types for binary operator '*'
        System.out.println("\n如果 discount 是字符串 '8折'，Java 会在编译期报错");
        System.out.println("而 Python 会在运行时产生错误或错误结果");
    }
}
