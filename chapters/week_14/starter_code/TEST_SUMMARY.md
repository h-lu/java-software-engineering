# Week 14 测试用例设计摘要

## 测试覆盖概览

本次为 Week 14 "发布与部署" 设计了完整的 JUnit 5 测试用例，覆盖了核心概念：
- Semantic Versioning（语义化版本）
- Maven Shade Plugin（构建打包）
- 环境配置管理

## 测试文件结构

```
starter_code/
├── pom.xml                                          # Maven 配置
├── src/
│   ├── main/java/com/campusflow/
│   │   ├── Main.java                                # 应用入口
│   │   ├── config/Config.java                       # 环境配置管理
│   │   └── util/Version.java                        # 语义化版本
│   ├── main/resources/
│   │   ├── config-dev.properties                    # 开发环境配置
│   │   ├── config-test.properties                   # 测试环境配置
│   │   └── config-prod.properties                   # 生产环境配置
│   └── test/java/com/campusflow/
│       ├── AppTest.java                             # 基线测试（不修改）
│       ├── config/ConfigTest.java                   # 配置测试
│       └── util/VersionTest.java                    # 版本测试
```

## 测试统计

| 测试类 | 测试数量 | 正例 | 边界 | 反例 | 场景 |
|--------|---------|------|------|------|------|
| VersionTest | 40 | 12 | 6 | 8 | 4 |
| ConfigTest | 31 | 14 | 4 | 7 | 6 |
| AppTest | 4 | 4 | 0 | 0 | 0 |
| **总计** | **75** | **30** | **10** | **15** | **10** |

## VersionTest.java - 测试用例详解

### 正例测试（12 个）
1. `parse_WhenValidVersionString_ShouldCreateVersionObject` - 标准版本解析
2. `compareTo_WhenSameVersion_ShouldReturnZero` - 相同版本比较
3. `compareTo_WhenMajorDiffers_ShouldCompareMajor` - MAJOR 优先级
4. `compareTo_WhenMinorDiffers_ShouldCompareMinor` - MINOR 优先级
5. `compareTo_WhenPatchDiffers_ShouldComparePatch` - PATCH 优先级
6. `compareTo_WhenPreReleaseDiffers_ShouldComparePreRelease` - 预发布版本比较
7. `incrementMajor_WhenCalled_ShouldResetMinorAndPatch` - 升级主版本
8. `incrementMinor_WhenCalled_ShouldResetPatch` - 升级次版本
9. `incrementPatch_WhenCalled_ShouldIncrementPatchOnly` - 升级补丁版本
10. `toString_WhenCalled_ShouldReturnFormattedString` - 格式化输出
11. `equalsAndHashCode_WhenSameVersion_ShouldBeEqual` - 相等性验证
12. `equals_WhenDifferentVersion_ShouldNotBeEqual` - 不等性验证

### 边界测试（6 个）
1. `parse_WhenMinimumVersion_ShouldParseCorrectly` - 0.0.1
2. `parse_WhenLargeVersionNumbers_ShouldParseCorrectly` - 999.999.999
3. `constructor_WhenZeroVersion_ShouldCreateValidVersion` - 0.0.0
4. `compareTo_WhenBoundaryVersions_ShouldCompareCorrectly` - 参数化测试
5. `incrementMajor_WhenFromZero_ShouldBecomeOne` - 0.0.0 → 1.0.0
6. `toString_WhenEmptyPreRelease_ShouldNotIncludeDash` - 空预发布标识

### 反例测试（8 个）
1. `parse_WhenNullOrEmpty_ShouldThrowException` - null 或空字符串
2. `parse_WhenInvalidFormat_ShouldThrowException` - 参数化测试：
   - "1"（只有主版本）
   - "1.0"（只有主版本和次版本）
   - "a.b.c"（非数字）
   - "1.0.0.0"（四段）
   - "1..0"（双点号）
   - ".1.0.0"（开头点号）
   - "1.0.0."（结尾点号）
   - "-beta.1"（只有预发布标识）
3. `constructor_WhenNegativeMajor_ShouldThrowException` - 负主版本
4. `constructor_WhenNegativeMinor_ShouldThrowException` - 负次版本
5. `constructor_WhenNegativePatch_ShouldThrowException` - 负补丁版本
6. `equals_WhenComparedWithNull_ShouldReturnFalse` - 与 null 比较
7. `equals_WhenComparedWithDifferentType_ShouldReturnFalse` - 与不同类型比较

### 场景测试（4 个）
1. `semanticVersioning_WhenPatchUpgrade_ShouldBeBackwardCompatible` - PATCH 升级规则
2. `semanticVersioning_WhenMinorUpgrade_ShouldBeBackwardCompatible` - MINOR 升级规则
3. `semanticVersioning_WhenMajorUpgrade_ShouldBeIncompatible` - MAJOR 升级规则
4. `semanticVersioning_WhenPreReleaseVsStable_StableShouldBeGreater` - 预发布 vs 正式版本

## ConfigTest.java - 测试用例详解

### 正例测试（14 个）
1. `constructor_WhenDefaultEnv_ShouldLoadDevConfig` - 默认开发环境
2. `getDbPath_WhenConfigLoaded_ShouldReturnValue` - 获取数据库路径
3. `getPort_WhenConfigLoaded_ShouldReturnValue` - 获取端口
4. `getLogLevel_WhenConfigLoaded_ShouldReturnValue` - 获取日志级别
5. `getEnv_WhenEnvSet_ShouldReturnEnvName` - 获取环境名称
6. `getDbPath_WhenProdEnv_ShouldReturnProdPath` - 生产环境数据库路径
7. `getPort_WhenProdEnv_ShouldReturnProdPort` - 生产环境端口（80）
8. `getLogLevel_WhenProdEnv_ShouldReturnProdLogLevel` - 生产环境日志级别
9. `getPort_WhenTestEnv_ShouldReturnTestPort` - 测试环境端口
10. `getDbPath_WhenTestEnv_ShouldReturnTestPath` - 测试环境数据库路径
11. `validate_WhenConfigValid_ShouldReturnTrue` - 配置验证
12. `portValidation_WhenPort80_ShouldBeValid` - HTTP 标准端口
13. `portValidation_WhenPort8080_ShouldBeValid` - 开发环境端口
14. `configIsolation_WhenMultipleConfigs_ShouldNotInterfere` - 配置隔离

### 边界测试（4 个）
1. `getPort_WhenMinimumValidPort_ShouldReturnPort` - 最小有效端口
2. `getPort_WhenMaximumValidPort_ShouldReturnPort` - 最大有效端口
3. `getDbPath_WhenEnvVarSet_ShouldOverrideConfig` - 环境变量覆盖
4. `getPort_WhenEnvVarSet_ShouldOverrideConfig` - 端口环境变量覆盖

### 反例测试（7 个）
1. `constructor_WhenNullOrEmptyEnv_ShouldThrowException` - 空环境名称
2. `constructor_WhenInvalidEnvFile_ShouldThrowException` - 不存在的配置文件
3. `getPort_WhenPortInvalidString_ShouldThrowException` - 无效端口号格式
4. `portValidation_WhenPortBelow1_ShouldThrowException` - 端口 < 1
5. `portValidation_WhenPortAbove65535_ShouldThrowException` - 端口 > 65535
6. `dbPathValidation_WhenDbPathIsEmpty_ShouldThrowException` - 空数据库路径
7. `dbPathValidation_WhenDbPathIsBlank_ShouldThrowException` - 空白数据库路径

### 场景测试（6 个）
1. `environmentSwitch_WhenSwitchFromDevToProd_ShouldLoadProdConfig` - 环境切换
2. `environmentSwitch_WhenSwitchFromDevToTest_ShouldLoadTestConfig` - 环境切换
3. `defaultValues_WhenPropertyMissing_ShouldUseDefaults` - 默认值处理
4. `twelveFactorApp_WhenEnvVarOverridesConfig_ShouldPreferEnvVar` - 12-Factor 原则
5. `configIsolation_WhenMultipleConfigs_ShouldNotInterfere` - 多实例隔离
6. `dbPathValidation_WhenDbPathIsValid_ShouldPassValidation` - 数据库路径验证

## 关键测试技术

### 1. 参数化测试（@ParameterizedTest）
```java
@ParameterizedTest
@ValueSource(strings = {"1", "1.0", "a.b.c", ...})
void parse_WhenInvalidFormat_ShouldThrowException(String invalidVersion) {
    assertThrows(IllegalArgumentException.class, () -> Version.parse(invalidVersion));
}
```

### 2. 多源参数（@CsvSource）
```java
@ParameterizedTest
@CsvSource({
    "1.0.0, 1.0.1, -1",
    "1.0.1, 1.0.0, 1",
    ...
})
void compareTo_WhenBoundaryVersions_ShouldCompareCorrectly(String v1Str, String v2Str, int expectedSign)
```

### 3. 空值和空字符串测试（@NullAndEmptySource）
```java
@ParameterizedTest
@NullAndEmptySource
void parse_WhenNullOrEmpty_ShouldThrowException(String versionString)
```

### 4. DisplayName 注解（中文描述）
```java
@DisplayName("parse_WhenValidVersionString_ShouldCreateVersionObject")
void parse_WhenValidVersionString_ShouldCreateVersionObject()
```

## 测试命名规范

采用 `<method>_<condition>_<expected>` 格式：
- `parse_WhenValidVersionString_ShouldCreateVersionObject`
- `incrementMajor_WhenCalled_ShouldResetMinorAndPatch`
- `portValidation_WhenPortBelow1_ShouldThrowException`

这种命名方式使测试失败时能直接看出问题所在。

## 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=VersionTest
mvn test -Dtest=ConfigTest

# 运行特定测试方法
mvn test -Dtest=VersionTest#parse_WhenValidVersionString_ShouldCreateVersionObject

# 打包并运行测试
mvn clean package
```

## 测试覆盖的核心概念

### Semantic Versioning
- 版本号格式：MAJOR.MINOR.PATCH
- MAJOR：不兼容的 API 变更
- MINOR：向后兼容的功能新增
- PATCH：向后兼容的问题修复
- 预发布版本：1.0.0-beta.1 < 1.0.0

### 环境配置管理
- 开发环境（dev）：localhost:8080, DEBUG 日志
- 测试环境（test）：localhost:8081, INFO 日志
- 生产环境（prod）：port 80, WARN 日志
- 12-Factor App 原则：环境变量优先于配置文件

### Maven 打包
- maven-shade-plugin：打包所有依赖到一个 JAR
- 主类配置：ManifestResourceTransformer
- 签名文件排除：避免打包冲突

## 注意事项

1. **端口验证**：端口范围是 1-65535（不是 1024-65535），因为生产环境常用端口 80（HTTP）
2. **版本解析**：使用 `split("\\.", -1)` 保留尾随空字符串，用于检测 `"1.0.0."` 这种格式
3. **环境变量**：测试中部分环境变量覆盖测试需要 mock 框架支持
4. **配置文件**：配置文件位于 `src/main/resources/`，通过类加载器读取

## 交付清单

- [x] VersionTest.java（40 个测试）
- [x] ConfigTest.java（31 个测试）
- [x] AppTest.java（4 个基线测试）
- [x] Version.java 实现
- [x] Config.java 实现
- [x] 配置文件（dev/test/prod）
- [x] pom.xml（Maven Shade Plugin 配置）
- [x] 所有测试通过（75/75）
