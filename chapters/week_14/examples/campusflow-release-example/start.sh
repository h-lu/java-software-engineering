#!/bin/bash
#
 # CampusFlow 启动脚本（Linux/Mac）
 #
 # 本例演示：
 # 1. 设置环境变量
 # 2. 启动可执行 JAR
 #
 # 运行方式：
 # - chmod +x start.sh
 # - ./start.sh
 #
 # 预期输出：
 # - Server started on port 8080

export CAMPUSFLOW_ENV=prod
java -jar campusflow-1.0.0.jar
