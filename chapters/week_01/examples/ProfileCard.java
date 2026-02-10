import java.util.Scanner;

/**
 * 示例：个人资料卡片（进阶版）
 *
 * 这个示例展示了：
 * - 使用 printf 格式化输出
 * - 处理多种数据类型
 * - 更友好的用户界面
 */
public class ProfileCard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 个人资料卡片生成器 ===");
        System.out.println();

        System.out.print("请输入姓名：");
        String name = scanner.nextLine();

        System.out.print("请输入职业：");
        String job = scanner.nextLine();

        System.out.print("请输入所在城市：");
        String city = scanner.nextLine();

        System.out.print("请输入年龄：");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("请输入工作年限：");
        double yearsOfExp = Double.parseDouble(scanner.nextLine());

        // 使用 printf 格式化输出
        System.out.println();
        System.out.println("╔══════════════════════════════════════╗");
        System.out.printf("║%12s 的个人资料卡片          ║%n", name);
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf("║ 职业：%-30s ║%n", job);
        System.out.printf("║ 所在地：%-28s ║%n", city);
        System.out.printf("║ 年龄：%d 岁%24s ║%n", age, "");
        System.out.printf("║ 工作年限：%.1f 年%20s ║%n", yearsOfExp, "");
        System.out.println("╚══════════════════════════════════════╝");

        scanner.close();
    }
}
