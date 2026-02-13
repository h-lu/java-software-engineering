@echo off
REM CampusFlow 启动脚本（Windows）
REM
REM 本例演示：
REM 1. 设置环境变量
REM 2. 启动可执行 JAR
REM
REM 运行方式：
REM - 双击 start.bat
REM - 或在命令行运行：start.bat
REM
REM 预期输出：
REM - Server started on port 8080

set CAMPUSFLOW_ENV=prod
java -jar campusflow-1.0.0.jar

pause
