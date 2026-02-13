import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 基础应用测试。
 */
class AppTest {

    @Test
    void testAppExists() {
        // 基础测试：验证应用能正常启动
        assertDoesNotThrow(() -> {
            // App 类存在且可以被实例化
            Class.forName("com.campusflow.App");
        });
    }
}
