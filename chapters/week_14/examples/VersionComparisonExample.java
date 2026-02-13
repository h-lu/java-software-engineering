package examples;

import com.campusflow.util.Version;

/**
 * VersionComparisonExample - 版本比较示例
 *
 * 展示 Semantic Versioning 的比较规则
 */
public class VersionComparisonExample {

    public static void main(String[] args) {
        System.out.println("=== Semantic Versioning 比较示例 ===\n");

        // 示例 1: PATCH 版本比较
        Version v1_0_0 = new Version(1, 0, 0);
        Version v1_0_1 = new Version(1, 0, 1);
        System.out.println("PATCH 版本比较:");
        System.out.println(v1_0_0 + " vs " + v1_0_1 + " = " + v1_0_0.compareTo(v1_0_1));
        System.out.println("说明: 1.0.1 是 1.0.0 的 bug 修复版本\n");

        // 示例 2: MINOR 版本比较
        Version v1_1_0 = new Version(1, 1, 0);
        System.out.println("MINOR 版本比较:");
        System.out.println(v1_0_0 + " vs " + v1_1_0 + " = " + v1_0_0.compareTo(v1_1_0));
        System.out.println("说明: 1.1.0 是 1.0.0 的新功能版本\n");

        // 示例 3: MAJOR 版本比较
        Version v2_0_0 = new Version(2, 0, 0);
        System.out.println("MAJOR 版本比较:");
        System.out.println(v1_0_0 + " vs " + v2_0_0 + " = " + v1_0_0.compareTo(v2_0_0));
        System.out.println("说明: 2.0.0 有破坏性变更，需要检查兼容性\n");

        // 示例 4: 预发布版本比较
        Version beta = new Version(1, 0, 0, "beta.1");
        Version rc = new Version(1, 0, 0, "rc.1");
        Version stable = new Version(1, 0, 0);
        System.out.println("预发布版本比较:");
        System.out.println(beta + " vs " + rc + " = " + beta.compareTo(rc));
        System.out.println(beta + " vs " + stable + " = " + beta.compareTo(stable));
        System.out.println("说明: beta < rc < stable（预发布版本 < 正式版本）\n");

        // 示例 5: 版本升级
        System.out.println("版本升级示例:");
        System.out.println("当前版本: " + v1_0_0);
        System.out.println("PATCH 升级: " + v1_0_0.incrementPatch());
        System.out.println("MINOR 升级: " + v1_0_0.incrementMinor());
        System.out.println("MAJOR 升级: " + v1_0_0.incrementMajor());
        System.out.println("说明: PATCH 和 MINOR 升级向后兼容，MAJOR 升级需要检查变更\n");

        // 示例 6: 解析版本字符串
        System.out.println("版本字符串解析:");
        String[] versionStrings = {
            "1.0.0",
            "2.3.4-beta.1",
            "1.0.0-rc.1",
            "0.0.1"
        };
        for (String vs : versionStrings) {
            Version v = Version.parse(vs);
            System.out.println("解析 \"" + vs + "\" -> " + v);
        }
    }
}
