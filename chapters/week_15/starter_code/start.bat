@echo off
REM CampusFlow 启动脚本（演示用）
REM Week 15: 项目集市与展示准备

echo =========================================
echo CampusFlow - 演示启动脚本
echo =========================================
echo.

REM 检查 Java 版本
echo 检查 Java 版本...
java -version
echo.

REM 编译项目
echo 编译项目...
call mvn clean package -DskipTests
echo.

REM 检查编译是否成功
if not exist "target\campusflow-1.0.0.jar" (
    echo 错误：编译失败，JAR 文件不存在
    pause
    exit /b 1
)

echo =========================================
echo 启动 CampusFlow...
echo =========================================
echo.
echo 演示提示：
echo 1. 确保 8080 端口未被占用
echo 2. 准备好测试数据（5-10 个任务）
echo 3. 打开浏览器访问 http://localhost:8080
echo 4. 演示流程：创建 ^> 标记完成 ^> 过滤 ^> 删除
echo.
echo 按 Ctrl+C 停止服务器
echo.

REM 启动应用
java -jar target\campusflow-1.0.0.jar

pause
