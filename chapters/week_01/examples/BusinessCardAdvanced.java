import java.util.Scanner;

/**
 * 示例：高级名片生成器
 *
 * 这个示例展示了更完整的实现，包括：
 * - 方法抽取
 * - 输入验证（基础版）
 * - 循环生成多张名片
 *
 * 注：这是挑战练习的参考实现，学生可以先尝试自己实现。
 */
public class BusinessCardAdvanced {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueGenerating = true;

        System.out.println("=== 高级名片生成器 ===");
        System.out.println();

        while (continueGenerating) {
            // 读取用户输入
            System.out.print("请输入姓名：");
            String name = readNotEmpty(scanner, "姓名不能为空，请重新输入：");

            System.out.print("请输入职位：");
            String jobTitle = readNotEmpty(scanner, "职位不能为空，请重新输入：");

            System.out.print("请输入邮箱：");
            String email = readValidEmail(scanner, "邮箱格式不正确，请重新输入：");

            System.out.print("请输入年龄：");
            int age = readPositiveInt(scanner, "年龄必须是正整数，请重新输入：");

            System.out.print("请输入工作年限：");
            double yearsOfExp = readPositiveDouble(scanner, "工作年限不能为负数，请重新输入：");

            // 生成名片
            printBusinessCard(name, jobTitle, email, age, yearsOfExp);

            // 询问是否继续
            System.out.println();
            System.out.print("是否继续生成名片？(y/n)：");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                continueGenerating = false;
            }
            System.out.println();
        }

        System.out.println("谢谢使用！");
        scanner.close();
    }

    /**
     * 读取非空字符串
     */
    private static String readNotEmpty(Scanner scanner, String prompt) {
        String input = scanner.nextLine();
        while (input.trim().isEmpty()) {
            System.out.print(prompt);
            input = scanner.nextLine();
        }
        return input;
    }

    /**
     * 读取有效的邮箱地址
     */
    private static String readValidEmail(Scanner scanner, String prompt) {
        String email = scanner.nextLine();
        while (!email.contains("@") || !email.contains(".")) {
            System.out.print(prompt);
            email = scanner.nextLine();
        }
        return email;
    }

    /**
     * 读取正整数
     */
    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0 && value <= 120) {
                    return value;
                }
                System.out.print(prompt);
            } catch (NumberFormatException e) {
                System.out.print("请输入有效的整数，请重新输入：");
            }
        }
    }

    /**
     * 读取正浮点数
     */
    private static double readPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= 0) {
                    return value;
                }
                System.out.print(prompt);
            } catch (NumberFormatException e) {
                System.out.print("请输入有效的数字，请重新输入：");
            }
        }
    }

    /**
     * 打印名片
     */
    private static void printBusinessCard(String name, String jobTitle,
                                          String email, int age, double yearsOfExp) {
        System.out.println();
        System.out.println("================================");
        System.out.println("       " + name + " 的个人名片");
        System.out.println("================================");
        System.out.println("职位：" + jobTitle);
        System.out.println("年龄：" + age + "岁");
        System.out.println("工作经验：" + yearsOfExp + "年");
        System.out.println("邮箱：" + email);
        System.out.println("================================");
    }
}
