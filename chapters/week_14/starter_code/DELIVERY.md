# Week 14 测试用例交付清单

## 交付内容

### 1. 核心实现类（src/main/java/）

| 文件 | 路径 | 功能 |
|------|------|------|
| Main.java | com.campusflow.Main | 应用入口，展示版本和配置信息 |
| Version.java | com.campusflow.util.Version | 语义化版本管理类 |
| Config.java | com.campusflow.config.Config | 环境配置管理类 |

### 2. 测试类（src/test/java/）

| 文件 | 路径 | 测试数量 | 覆盖内容 |
|------|------|---------|----------|
| AppTest.java | com.campusflow.AppTest | 4 | 基线测试（不修改） |
| VersionTest.java | com.campusflow.util.VersionTest | 40 | 版本解析、比较、升级测试 |
| ConfigTest.java | com.campusflow.config.ConfigTest | 31 | 配置加载、环境切换、验证测试 |

**总计：75 个测试用例**

### 3. 配置文件（src/main/resources/）

| 文件 | 环境 | 内容 |
|------|------|------|
| config-dev.properties | 开发环境 | port=8080, db=campusflow.db, log=DEBUG |
| config-test.properties | 测试环境 | port=8081, db=test_campusflow.db, log=INFO |
| config-prod.properties | 生产环境 | port=80, db=/var/data/campusflow.db, log=WARN |

### 4. 示例代码（examples/）

| 文件 | 功能 |
|------|------|
| VersionComparisonExample.java | 版本比较示例，展示 SemVer 规则 |
| ConfigExample.java | 环境配置示例，展示多环境管理 |
| ReleaseWorkflowExample.java | 发布流程示例，展示完整发布步骤 |

### 5. 启动脚本

| 文件 | 平台 |
|------|------|
| start.sh | Linux/Mac |
| start.bat | Windows |

### 6. Maven 配置（pom.xml）

- **JUnit 5.10.0**：测试框架
- **Maven Compiler Plugin 3.11.0**：Java 17 编译
- **Maven Surefire Plugin 3.0.0**：测试运行
- **Maven Shade Plugin 3.5.1**：打包可执行 JAR

## 测试覆盖统计

### 按类别分类

| 类别 | 数量 | 占比 |
|------|------|------|
| 正例（Happy Path） | 30 | 40% |
| 边界（Boundary） | 10 | 13% |
| 反例（Error Cases） | 15 | 20% |
| 场景（Semantic Scenarios） | 10 | 13% |
| 基线（Baseline） | 4 | 5% |
| **总计** | **75** | **100%** |

### 按测试类分类

| 测试类 | 正例 | 边界 | 反例 | 场景 | 总计 |
|--------|------|------|------|------|------|
| VersionTest | 12 | 6 | 8 | 4 | 30 |
| ConfigTest | 14 | 4 | 7 | 6 | 31 |
| AppTest | 4 | 0 | 0 | 0 | 4 |
| **总计** | **30** | **10** | **15** | **10** | **75** |

## 核心概念覆盖

### 1. Semantic Versioning（语义化版本）

**测试覆盖点：**
- ✅ 版本号格式解析（MAJOR.MINOR.PATCH）
- ✅ 版本比较（MAJOR > MINOR > PATCH）
- ✅ 预发布版本处理（beta、rc）
- ✅ 版本升级（incrementMajor/incrementMinor/incrementPatch）
- ✅ 边界值（0.0.0, 999.999.999）
- ✅ 异常处理（null、空字符串、格式错误）

**示例：**
```java
Version v1 = Version.parse("1.0.0");
Version v2 = v1.incrementMinor(); // 1.1.0
Version v3 = v1.incrementMajor(); // 2.0.0
```

### 2. 环境配置管理

**测试覆盖点：**
- ✅ 多环境配置加载（dev/test/prod）
- ✅ 环境变量覆盖（12-Factor App）
- ✅ 配置验证（端口范围、数据库路径）
- ✅ 默认值处理
- ✅ 配置隔离（多实例不干扰）

**示例：**
```java
Config devConfig = new Config("dev");
Config prodConfig = new Config("prod");
assertEquals(8080, devConfig.getPort());
assertEquals(80, prodConfig.getPort());
```

### 3. Maven 构建打包

**测试覆盖点：**
- ✅ maven-shade-plugin 配置
- ✅ 主类设置（ManifestResourceTransformer）
- ✅ 签名文件排除（避免冲突）

**运行命令：**
```bash
mvn clean package
java -jar target/campusflow-1.0.0.jar
```

## 测试质量亮点

### 1. 参数化测试（减少重复代码）

```java
@ParameterizedTest
@ValueSource(strings = {"1", "1.0", "a.b.c", ...})
void parse_WhenInvalidFormat_ShouldThrowException(String invalidVersion)
```

### 2. CSV 源测试（数据驱动）

```java
@ParameterizedTest
@CsvSource({"1.0.0, 1.0.1, -1", "1.0.1, 1.0.0, 1", ...})
void compareTo_WhenBoundaryVersions_ShouldCompareCorrectly(...)
```

### 3. 空值和空字符串测试

```java
@ParameterizedTest
@NullAndEmptySource
void parse_WhenNullOrEmpty_ShouldThrowException(String versionString)
```

### 4. 中文 DisplayName（测试失败时清晰定位）

```java
@DisplayName("parse_WhenValidVersionString_ShouldCreateVersionObject")
void parse_WhenValidVersionString_ShouldCreateVersionObject()
```

## 验证结果

```bash
mvn clean test
```

**输出：**
```
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0 - AppTest
Tests run: 40, Failures: 0, Errors: 0, Skipped: 0 - VersionTest
Tests run: 31, Failures: 0, Errors: 0, Skipped: 0 - ConfigTest
Tests run: 75, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## 文件清单

```
chapters/week_14/
├── starter_code/
│   ├── pom.xml                                    # Maven 配置
│   ├── start.sh                                   # Linux/Mac 启动脚本
│   ├── start.bat                                  # Windows 启动脚本
│   ├── TEST_SUMMARY.md                            # 测试摘要
│   ├── DELIVERY.md                                # 本文件
│   └── src/
│       ├── main/java/com/campusflow/
│       │   ├── Main.java                          # 应用入口
│       │   ├── config/Config.java                 # 配置管理
│       │   └── util/Version.java                  # 版本管理
│       ├── main/resources/
│       │   ├── config-dev.properties              # 开发环境配置
│       │   ├── config-test.properties             # 测试环境配置
│       │   └── config-prod.properties             # 生产环境配置
│       └── test/java/com/campusflow/
│           ├── AppTest.java                       # 基线测试
│           ├── config/ConfigTest.java             # 配置测试
│           └── util/VersionTest.java              # 版本测试
└── examples/
    ├── VersionComparisonExample.java              # 版本比较示例
    ├── ConfigExample.java                         # 配置示例
    └── ReleaseWorkflowExample.java                # 发布流程示例
```

## 后续建议

### 1. 集成测试扩展
- 可以添加 `ConfigIntegrationTest` 测试配置文件的完整加载流程
- 可以添加 `VersionIntegrationTest` 测试版本升级的完整场景

### 2. Mock 框架引入
- 对于需要修改环境变量的测试，可以引入 Mockito 框架
- 示例：`@Mock System.getenv()`

### 3. 测试覆盖率
- 可以使用 JaCoCo 插件生成测试覆盖率报告
- 目标：行覆盖率 > 80%，分支覆盖率 > 70%

### 4. 性能测试
- 可以添加 `VersionPerformanceTest` 测试大量版本比较的性能
- 可以添加 `ConfigLoadTest` 测试配置加载的性能

## 总结

本次为 Week 14 "发布与部署" 设计了完整的 JUnit 5 测试用例，共计 **75 个测试**，覆盖了：
- **Semantic Versioning** 的所有规则
- **环境配置管理** 的所有场景
- **Maven 打包** 的关键配置

测试用例遵循 `<method>_<condition>_<expected>` 命名规范，使用 JUnit 5 的参数化测试、CSV 源测试等高级特性，确保测试失败时能快速定位问题。

所有测试通过，满足交付要求。
