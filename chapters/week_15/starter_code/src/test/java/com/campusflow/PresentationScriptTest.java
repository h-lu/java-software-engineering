package com.campusflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Week 15: 演示脚本测试
 *
 * 测试演示脚本的创建、章节添加、时间分配验证等功能
 */
@DisplayName("演示脚本测试")
class PresentationScriptTest {

    private PresentationScript script;

    @BeforeEach
    void setUp() {
        script = new PresentationScript("CampusFlow 演示", 10);
    }

    // ==================== 正例测试 ====================

    @Nested
    @DisplayName("正例测试")
    class PositiveTests {

        @Test
        @DisplayName("创建演示脚本 - 合法时长应成功")
        void createScript_ValidDuration_ShouldSucceed() {
            // 8 分钟
            PresentationScript s8 = new PresentationScript("Test", 8);
            assertEquals(8, s8.getTotalPlannedMinutes());

            // 10 分钟
            PresentationScript s10 = new PresentationScript("Test", 10);
            assertEquals(10, s10.getTotalPlannedMinutes());

            // 9 分钟
            PresentationScript s9 = new PresentationScript("Test", 9);
            assertEquals(9, s9.getTotalPlannedMinutes());
        }

        @Test
        @DisplayName("添加章节 - 合法参数应成功")
        void addSection_ValidParameters_ShouldSucceed() {
            script.addSection("开场", 1, "项目介绍");
            assertEquals(1, script.getSectionCount());
            assertEquals(1, script.getTotalPlannedMinutes());
        }

        @Test
        @DisplayName("时间分配验证 - 总时长不超时应通过")
        void validateTimeAllocation_TotalWithinLimit_ShouldPass() {
            script.addSection("开场", 1, "项目介绍");
            script.addSection("问题背景", 1, "为什么做");
            script.addSection("技术亮点", 2, "技术选型");
            script.addSection("功能演示", 2, "演示功能");
            script.addSection("工程实践", 1, "测试部署");
            script.addSection("结尾", 1, "总结");

            assertTrue(script.validateTimeAllocation());
            assertEquals(8, script.getTotalPlannedMinutes());
        }

        @Test
        @DisplayName("完整脚本结构 - 标准演示脚本应通过验证")
        void completeScript_StandardPresentation_ShouldValidate() {
            // 按照章节建议的 10 分钟脚本
            script.addSection("开场", 1, "自我介绍和路线图");
            script.addSection("问题背景", 1, "为什么做这个项目");
            script.addSection("技术亮点", 2, "技术选型和架构");
            script.addSection("功能演示", 2, "创建、编辑、标记完成");
            script.addSection("工程实践", 1, "测试、文档、部署");
            script.addSection("踩坑与成长", 1, "Bug Bash 和 AI 协作");
            script.addSection("结尾", 2, "总结和链接");

            assertEquals(7, script.getSectionCount());
            assertEquals(10, script.getTotalPlannedMinutes());
            assertTrue(script.validateTimeAllocation());
        }
    }

    // ==================== 边界测试 ====================

    @Nested
    @DisplayName("边界测试")
    class BoundaryTests {

        @ParameterizedTest
        @ValueSource(ints = {8, 9, 10})
        @DisplayName("时长边界 - 8-10 分钟应接受")
        void durationBoundary_EdgeCases_ShouldAccept(int duration) {
            PresentationScript s = new PresentationScript("Test", duration);
            assertEquals(duration, s.getTotalPlannedMinutes());
        }

        @Test
        @DisplayName("章节时长边界 - 最小 1 分钟应接受")
        void sectionDurationBoundary_OneMinute_ShouldAccept() {
            script.addSection("短章节", 1, "最短章节");
            assertEquals(1, script.getTotalPlannedMinutes());
        }

        @Test
        @DisplayName("章节名称边界 - 单字名称应接受")
        void sectionNameBoundary_SingleCharacter_ShouldAccept() {
            script.addSection("开", 1, "测试");
            assertEquals(1, script.getSectionCount());
        }

        @Test
        @DisplayName("章节描述边界 - 最短描述应接受")
        void sectionDescriptionBoundary_ShortDescription_ShouldAccept() {
            script.addSection("测试", 1, "A");
            assertEquals(1, script.getSectionCount());
        }

        @Test
        @DisplayName("时间分配边界 - 刚好 10 分钟应通过")
        void timeAllocationBoundary_ExactlyTenMinutes_ShouldPass() {
            for (int i = 0; i < 10; i++) {
                script.addSection("Section " + i, 1, "Description " + i);
            }
            assertEquals(10, script.getTotalPlannedMinutes());
            assertTrue(script.validateTimeAllocation());
        }
    }

    // ==================== 反例测试 ====================

    @Nested
    @DisplayName("反例测试")
    class NegativeTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("标题为空或空白 - 应抛出异常")
        void createScript_NullOrEmptyTitle_ShouldThrow(String title) {
            assertThrows(IllegalArgumentException.class,
                    () -> new PresentationScript(title, 10),
                    "演示标题不能为空");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0, 7, 11, 15})
        @DisplayName("时长超出范围 - 应抛出异常")
        void createScript_DurationOutOfRange_ShouldThrow(int duration) {
            assertThrows(IllegalArgumentException.class,
                    () -> new PresentationScript("Test", duration),
                    "演示时长必须在 8-10 分钟之间");
        }

        @Test
        @DisplayName("添加章节 - 空名称应抛出异常")
        void addSection_NullName_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> script.addSection(null, 1, "Description"),
                    "章节名称不能为空");
        }

        @Test
        @DisplayName("添加章节 - 空描述应抛出异常")
        void addSection_EmptyDescription_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> script.addSection("Name", 1, "   "),
                    "章节描述不能为空");
        }

        @Test
        @DisplayName("添加章节 - 零时长应抛出异常")
        void addSection_ZeroDuration_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> script.addSection("Name", 0, "Description"),
                    "章节时长必须大于 0");
        }

        @Test
        @DisplayName("添加章节 - 负时长应抛出异常")
        void addSection_NegativeDuration_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> script.addSection("Name", -1, "Description"),
                    "章节时长必须大于 0");
        }

        @Test
        @DisplayName("时间分配验证 - 超时应失败")
        void validateTimeAllocation_ExceedsLimit_ShouldFail() {
            // 添加超过 10 分钟的章节
            for (int i = 0; i < 11; i++) {
                script.addSection("Section " + i, 1, "Description");
            }
            assertFalse(script.validateTimeAllocation());
            assertEquals(11, script.getTotalPlannedMinutes());
        }
    }

    // ==================== 功能测试 ====================

    @Nested
    @DisplayName("功能测试")
    class FeatureTests {

        @Test
        @DisplayName("获取脚本标题 - 应返回正确标题")
        void getTitle_ShouldReturnCorrectTitle() {
            assertEquals("CampusFlow 演示", script.getTitle());
        }

        @Test
        @DisplayName("获取章节列表 - 应返回独立副本")
        void getSections_ShouldReturnIndependentCopy() {
            script.addSection("Section 1", 1, "Desc 1");
            var sections = script.getSections();
            sections.clear(); // 修改返回的列表
            assertEquals(1, script.getSectionCount()); // 原始列表不应被修改
        }
    }
}
