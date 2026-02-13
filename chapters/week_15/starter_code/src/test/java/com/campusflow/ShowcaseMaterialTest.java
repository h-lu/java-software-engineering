package com.campusflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Week 15: 展示材料测试
 *
 * 测试展示材料（PPT、海报、二维码）的创建和验证
 */
@DisplayName("展示材料测试")
class ShowcaseMaterialTest {

    private ShowcaseMaterial material;

    @BeforeEach
    void setUp() {
        material = new ShowcaseMaterial("CampusFlow");
    }

    // ==================== 正例测试 ====================

    @Nested
    @DisplayName("正例测试")
    class PositiveTests {

        @Test
        @DisplayName("创建展示材料 - 合法项目名应成功")
        void createMaterial_ValidProjectName_ShouldSucceed() {
            ShowcaseMaterial m = new ShowcaseMaterial("Test Project");
            assertEquals("Test Project", m.getProjectName());
        }

        @Test
        @DisplayName("添加 PPT 页面 - 简短内容应成功")
        void addPptSlide_ShortContent_ShouldSucceed() {
            material.addPptSlide("封面 - CampusFlow 标题和团队");
            assertEquals(1, material.getPptSlideCount());
        }

        @Test
        @DisplayName("添加 PPT 页面 - 最大长度内容应成功")
        void addPptSlide_MaxLengthContent_ShouldSucceed() {
            // 300 字符（边界）
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 300; i++) {
                sb.append("A");
            }
            material.addPptSlide(sb.toString());
            assertEquals(1, material.getPptSlideCount());
        }

        @Test
        @DisplayName("设置海报内容 - 包含问题和解决方案应成功")
        void setPosterContent_WithProblemAndSolution_ShouldSucceed() {
            material.setPosterContent("问题：现有工具太复杂 | 解决方案：极简技术栈");
            assertTrue(material.validatePosterContent());
        }

        @Test
        @DisplayName("添加二维码 - HTTPS URL 应成功")
        void addQrCode_HttpsUrl_ShouldSucceed() {
            material.addQrCode("GitHub", "https://github.com/campusflow");
            material.addQrCode("在线演示", "https://campusflow.example.com");
            assertEquals(2, material.getQrCodeCount());
        }

        @Test
        @DisplayName("添加二维码 - HTTP URL 应成功")
        void addQrCode_HttpUrl_ShouldSucceed() {
            material.addQrCode("本地演示", "http://localhost:8080");
            assertEquals(1, material.getQrCodeCount());
        }

        @Test
        @DisplayName("验证 PPT 页数 - 12 页应通过")
        void validatePptPageCount_TwelveSlides_ShouldPass() {
            for (int i = 0; i < 12; i++) {
                material.addPptSlide("Slide " + (i + 1));
            }
            assertTrue(material.validatePptPageCount());
        }

        @Test
        @DisplayName("验证 PPT 页数 - 15 页应通过")
        void validatePptPageCount_FifteenSlides_ShouldPass() {
            for (int i = 0; i < 15; i++) {
                material.addPptSlide("Slide " + (i + 1));
            }
            assertTrue(material.validatePptPageCount());
        }

        @Test
        @DisplayName("验证二维码数量 - 2 个应通过")
        void validateQrCodeCount_TwoQrCodes_ShouldPass() {
            material.addQrCode("GitHub", "https://github.com/test");
            material.addQrCode("演示", "https://demo.example.com");
            assertTrue(material.validateQrCodeCount());
        }

        @Test
        @DisplayName("完整材料验证 - 所有条件满足应通过")
        void isComplete_AllMaterialsReady_ShouldPass() {
            // 添加 12 页 PPT
            for (int i = 0; i < 12; i++) {
                material.addPptSlide("Slide " + (i + 1));
            }
            // 设置海报
            material.setPosterContent("问题：复杂 | 解决方案：极简");
            // 添加二维码
            material.addQrCode("GitHub", "https://github.com/test");
            material.addQrCode("演示", "https://demo.example.com");

            assertTrue(material.isComplete());
        }
    }

    // ==================== 边界测试 ====================

    @Nested
    @DisplayName("边界测试")
    class BoundaryTests {

        @Test
        @DisplayName("项目名边界 - 单字名称应接受")
        void projectNameBoundary_SingleCharacter_ShouldAccept() {
            ShowcaseMaterial m = new ShowcaseMaterial("A");
            assertEquals("A", m.getProjectName());
        }

        @Test
        @DisplayName("PPT 内容边界 - 单字内容应接受")
        void pptContentBoundary_SingleCharacter_ShouldAccept() {
            material.addPptSlide("A");
            assertEquals(1, material.getPptSlideCount());
        }

        @Test
        @DisplayName("PPT 内容边界 - 300 字符应接受（边界）")
        void pptContentBoundary_Exactly300Chars_ShouldAccept() {
            String content300 = "A".repeat(300);
            material.addPptSlide(content300);
            assertEquals(1, material.getPptSlideCount());
        }

        @Test
        @DisplayName("PPT 页数边界 - 11 页应失败（低于下限）")
        void pptPageCountBoundary_ElevenSlides_ShouldFail() {
            for (int i = 0; i < 11; i++) {
                material.addPptSlide("Slide " + (i + 1));
            }
            assertFalse(material.validatePptPageCount());
        }

        @Test
        @DisplayName("PPT 页数边界 - 16 页应失败（高于上限）")
        void pptPageCountBoundary_SixteenSlides_ShouldFail() {
            for (int i = 0; i < 16; i++) {
                material.addPptSlide("Slide " + (i + 1));
            }
            assertFalse(material.validatePptPageCount());
        }

        @Test
        @DisplayName("海报内容边界 - 仅包含问题应通过")
        void posterContentBoundary_OnlyProblem_ShouldPass() {
            material.setPosterContent("问题：工具太复杂");
            assertTrue(material.validatePosterContent());
        }

        @Test
        @DisplayName("海报内容边界 - 仅包含解决方案应通过")
        void posterContentBoundary_OnlySolution_ShouldPass() {
            material.setPosterContent("解决方案：极简技术栈");
            assertTrue(material.validatePosterContent());
        }

        @Test
        @DisplayName("二维码数量边界 - 1 个应失败")
        void qrCodeCountBoundary_OneQrCode_ShouldFail() {
            material.addQrCode("GitHub", "https://github.com/test");
            assertFalse(material.validateQrCodeCount());
        }

        @Test
        @DisplayName("二维码 URL 边界 - 仅协议和域名应接受")
        void qrCodeUrlBoundary_MinimalUrl_ShouldAccept() {
            material.addQrCode("Test", "https://a.bc");
            assertEquals(1, material.getQrCodeCount());
        }
    }

    // ==================== 反例测试 ====================

    @Nested
    @DisplayName("反例测试")
    class NegativeTests {

        @Test
        @DisplayName("创建展示材料 - 空项目名应抛出异常")
        void createMaterial_NullProjectName_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> new ShowcaseMaterial(null),
                    "项目名称不能为空");
        }

        @Test
        @DisplayName("创建展示材料 - 空白项目名应抛出异常")
        void createMaterial_EmptyProjectName_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> new ShowcaseMaterial("   "),
                    "项目名称不能为空");
        }

        @Test
        @DisplayName("添加 PPT 页面 - 空内容应抛出异常")
        void addPptSlide_NullContent_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> material.addPptSlide(null),
                    "PPT 内容不能为空");
        }

        @Test
        @DisplayName("添加 PPT 页面 - 空白内容应抛出异常")
        void addPptSlide_EmptyContent_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> material.addPptSlide("   "),
                    "PPT 内容不能为空");
        }

        @Test
        @DisplayName("添加 PPT 页面 - 超过 300 字符应抛出异常")
        void addPptSlide_ExceedsMaxLength_ShouldThrow() {
            String tooLong = "A".repeat(301);
            assertThrows(IllegalArgumentException.class,
                    () -> material.addPptSlide(tooLong),
                    "PPT 一页信息过多，请精简至 300 字符以内");
        }

        @Test
        @DisplayName("设置海报内容 - 空内容应抛出异常")
        void setPosterContent_NullContent_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> material.setPosterContent(null),
                    "海报内容不能为空");
        }

        @Test
        @DisplayName("设置海报内容 - 缺少关键要素应抛出异常")
        void setPosterContent_MissingKeywords_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> material.setPosterContent("这是一些普通内容"),
                    "海报必须包含问题与解决方案");
        }

        @Test
        @DisplayName("添加二维码 - 空类型应抛出异常")
        void addQrCode_NullType_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> material.addQrCode(null, "https://example.com"),
                    "二维码类型不能为空");
        }

        @Test
        @DisplayName("添加二维码 - 空 URL 应抛出异常")
        void addQrCode_NullUrl_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> material.addQrCode("GitHub", null),
                    "二维码 URL 不能为空");
        }

        @Test
        @DisplayName("添加二维码 - 无效 URL 应抛出异常")
        void addQrCode_InvalidUrl_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> material.addQrCode("GitHub", "not-a-url"),
                    "URL 必须以 http:// 或 https:// 开头");
        }

        @Test
        @DisplayName("添加二维码 - FTP 协议应抛出异常")
        void addQrCode_FtpProtocol_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> material.addQrCode("FTP", "ftp://example.com"),
                    "URL 必须以 http:// 或 https:// 开头");
        }

        @Test
        @DisplayName("完整材料验证 - PPT 不足应失败")
        void isComplete_InsufficientPptSlides_ShouldFail() {
            for (int i = 0; i < 11; i++) {
                material.addPptSlide("Slide " + (i + 1));
            }
            material.setPosterContent("问题：复杂 | 解决方案：极简");
            material.addQrCode("GitHub", "https://github.com/test");
            material.addQrCode("演示", "https://demo.example.com");

            assertFalse(material.isComplete());
        }

        @Test
        @DisplayName("完整材料验证 - 缺少海报应失败")
        void isComplete_MissingPoster_ShouldFail() {
            for (int i = 0; i < 12; i++) {
                material.addPptSlide("Slide " + (i + 1));
            }
            // 不设置海报
            material.addQrCode("GitHub", "https://github.com/test");
            material.addQrCode("演示", "https://demo.example.com");

            assertFalse(material.isComplete());
        }

        @Test
        @DisplayName("完整材料验证 - 二维码不足应失败")
        void isComplete_InsufficientQrCodes_ShouldFail() {
            for (int i = 0; i < 12; i++) {
                material.addPptSlide("Slide " + (i + 1));
            }
            material.setPosterContent("问题：复杂 | 解决方案：极简");
            material.addQrCode("GitHub", "https://github.com/test");
            // 只有一个二维码

            assertFalse(material.isComplete());
        }
    }

    // ==================== 功能测试 ====================

    @Nested
    @DisplayName("功能测试")
    class FeatureTests {

        @Test
        @DisplayName("获取 PPT 列表 - 应返回独立副本")
        void getPptSlides_ShouldReturnIndependentCopy() {
            material.addPptSlide("Slide 1");
            var slides = material.getPptSlides();
            slides.clear(); // 修改返回的列表
            assertEquals(1, material.getPptSlideCount()); // 原始列表不应被修改
        }

        @Test
        @DisplayName("获取二维码列表 - 应返回独立副本")
        void getQrCodes_ShouldReturnIndependentCopy() {
            material.addQrCode("GitHub", "https://github.com/test");
            var codes = material.getQrCodes();
            codes.clear();
            assertEquals(1, material.getQrCodeCount());
        }
    }
}
