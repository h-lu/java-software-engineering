package com.campusflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Week 15: 问答策略测试
 *
 * 测试问答准备的完整性验证
 */
@DisplayName("问答策略测试")
class QaStrategyTest {

    private QaStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new QaStrategy();
    }

    // ==================== 正例测试 ====================

    @Nested
    @DisplayName("正例测试")
    class PositiveTests {

        @Test
        @DisplayName("添加问答对 - 合法参数应成功")
        void addQaPair_ValidParameters_ShouldSucceed() {
            strategy.addQaPair("为什么用 Java？", "Java 的静态类型系统更适合学习工程化开发。");
            assertEquals(1, strategy.getQaCount());
        }

        @Test
        @DisplayName("获取回答 - 存在的问题应返回答案")
        void getAnswer_ExistingQuestion_ShouldReturnAnswer() {
            String question = "为什么用 Javalin？";
            String answer = "Javalin 轻量简洁，非常适合学习项目快速上手开发。";
            strategy.addQaPair(question, answer);
            assertEquals(answer, strategy.getAnswer(question));
        }

        @Test
        @DisplayName("验证问答数量 - 10 个应通过")
        void hasEnoughQaPairs_TenQaPairs_ShouldPass() {
            for (int i = 0; i < 10; i++) {
                strategy.addQaPair("问题 " + i, "这是第 " + i + " 个问题的详细解释说明内容和更多。");
            }
            assertTrue(strategy.hasEnoughQaPairs());
        }

        @Test
        @DisplayName("验证技术问题 - 包含技术关键词应通过")
        void hasTechnicalQuestions_ContainsTechnicalKeyword_ShouldPass() {
            strategy.addQaPair("为什么用 Java？", "Java 的静态类型系统更适合学习工程化开发。");
            strategy.addQaPair("技术选型是什么？", "我们选择了极简技术栈来降低学习成本和难度。");
            assertTrue(strategy.hasTechnicalQuestions());
        }

        @Test
        @DisplayName("验证设计问题 - 包含设计关键词应通过")
        void hasDesignQuestions_ContainsDesignKeyword_ShouldPass() {
            strategy.addQaPair("架构设计是什么？", "我们采用了标准的分层架构来组织代码结构。");
            strategy.addQaPair("模型如何设计？", "我们参考了领域驱动设计 DDD 的思想来设计。");
            assertTrue(strategy.hasDesignQuestions());
        }

        @Test
        @DisplayName("验证工程问题 - 包含工程关键词应通过")
        void hasPracticeQuestions_ContainsPracticeKeyword_ShouldPass() {
            strategy.addQaPair("测试覆盖率多少？", "我们达到了百分之八十的单元测试覆盖率指标。");
            strategy.addQaPair("如何部署？", "使用 Railway 平台实现自动化的持续部署流程。");
            assertTrue(strategy.hasPracticeQuestions());
        }

        @Test
        @DisplayName("验证未来问题 - 包含未来关键词应通过")
        void hasFutureQuestions_ContainsFutureKeyword_ShouldPass() {
            strategy.addQaPair("未来计划是什么？", "我们计划增加搜索功能和更多的高级筛选功能。");
            strategy.addQaPair("下一步做什么？", "继续优化移动端体验并提升整体系统性能表现。");
            assertTrue(strategy.hasFutureQuestions());
        }

        @Test
        @DisplayName("完整问答准备 - 所有类型都应通过")
        void isComplete_AllTypesPresent_ShouldPass() {
            // 技术问题
            strategy.addQaPair("为什么用 Java？", "Java 的静态类型系统更适合学习工程化开发。");
            strategy.addQaPair("为什么不用 Spring？", "Spring 框架太重了，不适合作为学习项目使用。");
            strategy.addQaPair("为什么用 SQLite？", "SQLite 零配置，非常适合小项目快速开发使用。");

            // 设计问题
            strategy.addQaPair("架构设计是什么？", "我们采用领域模型、仓储、控制器分层架构。");
            strategy.addQaPair("模型如何设计？", "核心实体是任务，参考了领域驱动设计思想。");

            // 工程问题
            strategy.addQaPair("测试覆盖率多少？", "我们使用 JUnit 5 编写测试，覆盖率达到百分之八十。");
            strategy.addQaPair("如何部署？", "使用 Railway 自动部署，推送代码后会自动构建。");

            // 未来问题
            strategy.addQaPair("未来计划是什么？", "短期增加搜索功能，长期迁移到 PostgreSQL 数据库。");
            strategy.addQaPair("下一步做什么？", "优化移动端体验，增加数据导出和其他功能。");

            // 补充到 10 个
            strategy.addQaPair("Bug Bash 发现了什么？", "其他组发现了三个 Bug，我们全部都修复完成了。");

            assertTrue(strategy.isComplete());
        }
    }

    // ==================== 边界测试 ====================

    @Nested
    @DisplayName("边界测试")
    class BoundaryTests {

        @Test
        @DisplayName("问答数量边界 - 9 个应失败")
        void qaCountBoundary_NineQaPairs_ShouldFail() {
            for (int i = 0; i < 9; i++) {
                strategy.addQaPair("Q" + i, "这是第 " + i + " 个问题的详细解释说明内容和更多。");
            }
            assertFalse(strategy.hasEnoughQaPairs());
        }

        @Test
        @DisplayName("问答数量边界 - 10 个应通过")
        void qaCountBoundary_TenQaPairs_ShouldPass() {
            for (int i = 0; i < 10; i++) {
                strategy.addQaPair("Q" + i, "这是第 " + i + " 个问题的详细解释说明内容和更多。");
            }
            assertTrue(strategy.hasEnoughQaPairs());
        }

        @Test
        @DisplayName("回答长度边界 - 20 字符应通过")
        void answerLengthBoundary_Exactly20Chars_ShouldPass() {
            String answer20 = "A".repeat(20);
            strategy.addQaPair("问题", answer20);
            assertEquals(1, strategy.getQaCount());
        }

        @Test
        @DisplayName("回答长度边界 - 19 字符应失败")
        void answerLengthBoundary_NineteenChars_ShouldFail() {
            String answer19 = "A".repeat(19);
            assertThrows(IllegalArgumentException.class,
                    () -> strategy.addQaPair("问题", answer19),
                    "回答过于简短，请提供详细解释（至少 20 字符）");
        }

        @Test
        @DisplayName("问题长度边界 - 单字问题应接受")
        void questionLengthBoundary_SingleCharacter_ShouldAccept() {
            strategy.addQaPair("Q", "这是详细回答，至少需要二十个字符的长度。");
            assertEquals(1, strategy.getQaCount());
        }

        @Test
        @DisplayName("技术关键词 - 包含'为什么'应识别")
        void technicalKeyword_ContainsWhy_ShouldIdentify() {
            strategy.addQaPair("为什么不用 Spring", "Spring 框架太重，不适合作为学习项目使用。");
            assertTrue(strategy.hasTechnicalQuestions());
        }

        @Test
        @DisplayName("设计关键词 - 包含'模型'应识别")
        void designKeyword_ContainsModel_ShouldIdentify() {
            strategy.addQaPair("数据模型如何设计", "我们参考了领域驱动设计的核心思想方法和实践。");
            assertTrue(strategy.hasDesignQuestions());
        }

        @Test
        @DisplayName("工程关键词 - 包含'测试'应识别")
        void practiceKeyword_ContainsTest_ShouldIdentify() {
            strategy.addQaPair("测试怎么做", "我们使用 JUnit 5 来编写单元测试用例。");
            assertTrue(strategy.hasPracticeQuestions());
        }

        @Test
        @DisplayName("未来关键词 - 包含'下一步'应识别")
        void futureKeyword_ContainsNextStep_ShouldIdentify() {
            strategy.addQaPair("下一步呢", "继续优化系统性能并持续提升整体用户体验。");
            assertTrue(strategy.hasFutureQuestions());
        }
    }

    // ==================== 反例测试 ====================

    @Nested
    @DisplayName("反例测试")
    class NegativeTests {

        @Test
        @DisplayName("添加问答对 - 空问题应抛出异常")
        void addQaPair_NullQuestion_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> strategy.addQaPair(null, "This is a valid answer with enough characters"),
                    "问题不能为空");
        }

        @Test
        @DisplayName("添加问答对 - 空白问题应抛出异常")
        void addQaPair_EmptyQuestion_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> strategy.addQaPair("   ", "This is a valid answer with enough characters"),
                    "问题不能为空");
        }

        @Test
        @DisplayName("添加问答对 - 空回答应抛出异常")
        void addQaPair_NullAnswer_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> strategy.addQaPair("Question", null),
                    "回答不能为空");
        }

        @Test
        @DisplayName("添加问答对 - 空白回答应抛出异常")
        void addQaPair_EmptyAnswer_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> strategy.addQaPair("Question", "   "),
                    "回答不能为空");
        }

        @Test
        @DisplayName("添加问答对 - 回答过短应抛出异常")
        void addQaPair_ShortAnswer_ShouldThrow() {
            assertThrows(IllegalArgumentException.class,
                    () -> strategy.addQaPair("Question", "Too short"),
                    "回答过于简短，请提供详细解释（至少 20 字符）");
        }

        @Test
        @DisplayName("获取回答 - 不存在的问题应返回 null")
        void getAnswer_NonExistentQuestion_ShouldReturnNull() {
            assertNull(strategy.getAnswer("不存在的问题"));
        }

        @Test
        @DisplayName("验证问答数量 - 无问答对应失败")
        void hasEnoughQaPairs_NoQaPairs_ShouldFail() {
            assertFalse(strategy.hasEnoughQaPairs());
        }

        @Test
        @DisplayName("验证技术问题 - 无技术关键词应失败")
        void hasTechnicalQuestions_NoTechnicalKeywords_ShouldFail() {
            strategy.addQaPair("项目是什么", "CampusFlow 是一个简单的任务管理系统。");
            strategy.addQaPair("功能有哪些", "系统支持创建、编辑和管理任务的主要功能。");
            assertFalse(strategy.hasTechnicalQuestions());
        }

        @Test
        @DisplayName("验证设计问题 - 无设计关键词应失败")
        void hasDesignQuestions_NoDesignKeywords_ShouldFail() {
            strategy.addQaPair("项目是什么", "CampusFlow 是一个简单的任务管理系统。");
            strategy.addQaPair("怎么使用", "用户访问项目网址即可直接在线使用所有功能。");
            assertFalse(strategy.hasDesignQuestions());
        }

        @Test
        @DisplayName("验证工程问题 - 无工程关键词应失败")
        void hasPracticeQuestions_NoPracticeKeywords_ShouldFail() {
            strategy.addQaPair("项目是什么", "CampusFlow 是一个简单的任务管理系统。");
            strategy.addQaPair("团队成员", "我们小组一共有三名成员参与开发和维护工作。");
            assertFalse(strategy.hasPracticeQuestions());
        }

        @Test
        @DisplayName("验证未来问题 - 无未来关键词应失败")
        void hasFutureQuestions_NoFutureKeywords_ShouldFail() {
            strategy.addQaPair("项目是什么", "CampusFlow 是一个简单的任务管理系统。");
            strategy.addQaPair("怎么使用", "用户访问项目网址即可直接在线使用所有功能。");
            assertFalse(strategy.hasFutureQuestions());
        }

        @Test
        @DisplayName("完整问答准备 - 数量不足应失败")
        void isComplete_InsufficientQaPairs_ShouldFail() {
            // 各类型都有，但总数不足
            strategy.addQaPair("为什么用 Java？", "Java 静态类型系统更适合学习工程化开发。");
            strategy.addQaPair("架构设计是什么？", "我们采用标准的分层架构来组织整体代码结构。");
            strategy.addQaPair("测试覆盖率多少？", "我们使用单元测试达到了百分之八十的代码覆盖率。");
            strategy.addQaPair("未来计划是什么？", "计划增加搜索功能和更多高级筛选功能特性。");

            assertFalse(strategy.isComplete()); // 只有 4 个，不足 10 个
        }

        @Test
        @DisplayName("完整问答准备 - 缺少技术问题应失败")
        void isComplete_MissingTechnicalQuestions_ShouldFail() {
            for (int i = 0; i < 10; i++) {
                strategy.addQaPair("普通问题 " + i, "这是详细回答说明内容，至少满足二十字符要求。");
            }
            assertFalse(strategy.isComplete());
        }

        @Test
        @DisplayName("完整问答准备 - 缺少设计问题应失败")
        void isComplete_MissingDesignQuestions_ShouldFail() {
            for (int i = 0; i < 5; i++) {
                strategy.addQaPair("技术问题 " + i, "这是详细回答说明内容，至少满足二十字符要求。");
            }
            for (int i = 0; i < 5; i++) {
                strategy.addQaPair("未来计划 " + i, "这是详细回答说明内容，至少满足二十字符要求。");
            }
            assertFalse(strategy.isComplete());
        }
    }

    // ==================== 功能测试 ====================

    @Nested
    @DisplayName("功能测试")
    class FeatureTests {

        @Test
        @DisplayName("获取所有问题 - 应返回独立副本")
        void getAllQuestions_ShouldReturnIndependentCopy() {
            strategy.addQaPair("Q1", "这是第一个问题的详细回答说明内容和文本描述。");
            var questions = strategy.getAllQuestions();
            questions.clear();
            assertEquals(1, strategy.getQaCount());
        }

        @Test
        @DisplayName("重复问题 - 应覆盖旧答案")
        void duplicateQuestion_ShouldOverwriteOldAnswer() {
            strategy.addQaPair("问题", "这是旧答案的详细说明内容和更多的补充描述。");
            strategy.addQaPair("问题", "这是新答案的详细说明内容和更多的补充描述。");
            assertEquals("这是新答案的详细说明内容和更多的补充描述。", strategy.getAnswer("问题"));
            assertEquals(1, strategy.getQaCount());
        }
    }
}
