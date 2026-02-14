#!/bin/bash
# =============================================================================
# Course Site Build Script
# =============================================================================
# 从项目根目录构建 Docusaurus 课程站点
#
# 用法:
#   ./scripts/site.sh install   - 安装依赖
#   ./scripts/site.sh dev       - 启动开发服务器
#   ./scripts/site.sh build     - 构建生产版本
#   ./scripts/site.sh clean     - 清理构建产物
# =============================================================================

set -e

# 颜色输出
BLUE='\033[36m'
GREEN='\033[32m'
YELLOW='\033[33m'
NC='\033[0m'

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
TEMPLATE_DIR="$PROJECT_ROOT/templates/docusaurus-site"
SITE_DIR="$TEMPLATE_DIR/site"
CHAPTERS_DIR="$PROJECT_ROOT/chapters"
SHARED_DIR="$PROJECT_ROOT/shared"

# 检查目录是否存在
check_dirs() {
    if [ ! -d "$CHAPTERS_DIR" ]; then
        echo -e "${YELLOW}警告: chapters 目录不存在: $CHAPTERS_DIR${NC}"
    fi
    if [ ! -d "$SHARED_DIR" ]; then
        echo -e "${YELLOW}警告: shared 目录不存在: $SHARED_DIR${NC}"
    fi
}

# 安装依赖
install() {
    echo -e "${BLUE}安装站点依赖...${NC}"
    cd "$SITE_DIR" && npm install
    echo -e "${GREEN}依赖安装完成!${NC}"
}

# 生成文档内容
generate() {
    echo -e "${BLUE}生成文档内容...${NC}"
    python3 "$TEMPLATE_DIR/scripts/build_site.py" \
        --site-dir "$SITE_DIR" \
        --chapters-dir "$CHAPTERS_DIR" \
        --shared-dir "$SHARED_DIR" \
        --verbose
    echo -e "${GREEN}文档生成完成!${NC}"
}

# 启动开发服务器
dev() {
    check_dirs
    generate
    echo -e "${BLUE}启动开发服务器...${NC}"
    cd "$SITE_DIR" && npm run start
}

# 构建生产版本
build() {
    check_dirs
    generate
    echo -e "${BLUE}构建生产版本...${NC}"
    cd "$SITE_DIR" && npm run build
    echo -e "${GREEN}构建完成! 输出目录: $SITE_DIR/build/${NC}"
}

# 清理构建产物
clean() {
    echo -e "${YELLOW}清理构建产物...${NC}"
    cd "$SITE_DIR" && rm -rf build .docusaurus
    find "$SITE_DIR/docs" -name "*.mdx" -delete 2>/dev/null || true
    find "$SITE_DIR/docs" -type d -empty -delete 2>/dev/null || true
    echo -e "${GREEN}清理完成!${NC}"
}

# 显示帮助
help() {
    echo ""
    echo -e "${BLUE}课程站点构建脚本${NC}"
    echo ""
    echo "用法: ./scripts/site.sh <命令>"
    echo ""
    echo "命令:"
    echo "  install   安装 npm 依赖"
    echo "  dev       生成文档并启动开发服务器"
    echo "  build     生成文档并构建生产版本"
    echo "  clean     清理构建产物"
    echo "  help      显示此帮助信息"
    echo ""
    echo "目录配置:"
    echo "  课程内容: $CHAPTERS_DIR"
    echo "  共享文件: $SHARED_DIR"
    echo "  站点目录: $SITE_DIR"
    echo ""
}

# 主入口
case "${1:-help}" in
    install)
        install
        ;;
    dev)
        dev
        ;;
    build)
        build
        ;;
    clean)
        clean
        ;;
    help|--help|-h)
        help
        ;;
    *)
        echo -e "${YELLOW}未知命令: $1${NC}"
        help
        exit 1
        ;;
esac
