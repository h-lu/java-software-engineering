# Ralph Agent Configuration - Java 教材项目

## 环境设置

```bash
# 初始化 SDKman 环境（必须）
export SDKMAN_DIR="$HOME/.sdkman"
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"

# 验证环境
java -version
mvn -version
```

## Build Instructions

```bash
# 初始化 SDKman 环境
export SDKMAN_DIR="$HOME/.sdkman"
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"

# 构建指定周的代码（示例：week_06）
cd chapters/week_06/starter_code && mvn clean compile
```

## Test Instructions

```bash
# 初始化 SDKman 环境
export SDKMAN_DIR="$HOME/.sdkman"
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"

# 运行指定周的测试（示例：week_06）
cd /home/ubuntu/java-software-engineering && mvn -q -f chapters/week_06/starter_code/pom.xml test

# 或者使用项目脚本验证
python3 scripts/validate_week.py --week week_06 --mode release
```

## QA 批量处理流程（重新审核版）

```bash
# 1. 初始化环境
export SDKMAN_DIR="$HOME/.sdkman"
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"

# 2. 调用 /qa-week 进行全新审核（忽略现有 QA_REPORT）
/qa-week week_XX

# 3. 读取新生成的 QA_REPORT.md，识别所有 S1-S4 问题
cat chapters/week_XX/QA_REPORT.md

# 4. 修复所有 S1-S4 问题

# 5. 验证修复
python3 scripts/validate_week.py --week week_XX --mode release

# 6. Git 提交
git add chapters/week_XX/
git commit -m "fix(week_XX): 修复 QA S1-S4 问题"
```

## Run Instructions

```bash
# 初始化 SDKman 环境
export SDKMAN_DIR="$HOME/.sdkman"
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"

# 运行指定周的示例（示例：week_06）
cd chapters/week_06/starter_code && mvn exec:java -Dexec.mainClass="LibraryTracker"
```

## Notes

- **必须**在每次 Bash 命令前初始化 SDKman 环境
- Java 版本：17.0.9-tem (Eclipse Temurin)
- Maven 版本：3.9.12
- 项目结构：chapters/week_XX/starter_code/pom.xml
- QA 脚本：scripts/validate_week.py
- **当前任务**：重新 QA 审核 week_06 到 week_16
- **处理原则**：一周一周进行，必须调用 /qa-week，修复所有 S1-S4 问题

## 周进度跟踪

| Week | 状态 | 备注 |
|------|------|------|
| week_06 | ⏳ 待重新审核 | |
| week_07 | ⏳ 待重新审核 | |
| week_08 | ⏳ 待重新审核 | |
| week_09 | ⏳ 待重新审核 | |
| week_10 | ⏳ 待重新审核 | |
| week_11 | ⏳ 待重新审核 | |
| week_12 | ⏳ 待重新审核 | |
| week_13 | ⏳ 待重新审核 | |
| week_14 | ⏳ 待重新审核 | |
| week_15 | ⏳ 待重新审核 | |
| week_16 | ⏳ 待重新审核 | |
