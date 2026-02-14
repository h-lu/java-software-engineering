# Docusaurus 课程网站模板

通用的静态课程网站生成模板，基于 Docusaurus 3.x + Python 构建脚本。

## 特性

- 自动生成课程章节页面
- 支持多阶段课程结构（如：阶段一、阶段二...）
- 每周独立页面（讲义、作业、评分标准、代码、术语、锚点）
- Markdown + YAML 源文件
- 响应式设计 + 暗色模式
- 本地搜索
- 静态输出（可部署到 Netlify/Vercel/GitHub Pages 等）

## 文件结构

```
.
├── scripts/
│   └── build_site.py          # 主构建脚本（解析源文件生成 MDX）
├── site/                      # Docusaurus 站点
│   ├── docs/                  # 生成的文档（自动创建）
│   ├── src/
│   │   ├── pages/index.tsx    # 首页
│   │   └── css/custom.css     # 自定义样式
│   ├── static/img/            # 静态资源
│   ├── docusaurus.config.ts   # 站点配置
│   ├── sidebars.ts            # 侧边栏配置（自动生成）
│   └── package.json           # 依赖
├── chapters/                  # 课程内容源文件
│   ├── TOC.md                 # 课程目录结构
│   ├── SYLLABUS.md            # 教学大纲
│   └── week_01/               # 第1周内容
│       ├── CHAPTER.md         # 讲义
│       ├── ASSIGNMENT.md      # 作业
│       ├── RUBRIC.md          # 评分标准
│       ├── ANCHORS.yml        # 锚点定义
│       ├── TERMS.yml          # 术语定义
│       ├── examples/          # 示例代码
│       ├── starter_code/      # 起始代码
│       └── tests/             # 测试代码
├── shared/                    # 共享资源
│   ├── glossary.yml           # 术语表
│   └── style_guide.md         # 风格指南
├── Makefile                   # 便捷命令
└── .github/workflows/
    └── build.yml              # CI配置（可选）
```

## 快速开始

### 1. 复制模板

将 `templates/docusaurus-site/` 复制到你的课程仓库：

```bash
cp -r templates/docusaurus-site/* /path/to/your/course/
cd /path/to/your/course
```

### 2. 安装依赖

```bash
make install
```

### 3. 配置站点

编辑 `site/docusaurus.config.ts`：

```typescript
// 修改站点信息
const config: Config = {
  title: '你的课程名称',
  tagline: '课程副标题',
  url: 'https://your-domain.com',
  baseUrl: '/your-course/',
  organizationName: 'your-org',
  projectName: 'your-course',
  // ... 其他配置
}
```

### 4. 创建课程目录

编辑 `chapters/TOC.md`：

```markdown
# 课程目录

## 阶段一：基础篇

| 周次 | 标题 |
|------|------|
| [Week 01](week_01/CHAPTER.md) | 课程介绍 |
| [Week 02](week_02/CHAPTER.md) | 基础概念 |

## 阶段二：进阶篇

| 周次 | 标题 |
|------|------|
| [Week 03](week_03/CHAPTER.md) | 高级特性 |
```

### 5. 添加课程内容

创建周目录和内容：

```bash
mkdir -p chapters/week_01
```

在 `chapters/week_01/` 中创建：
- `CHAPTER.md` - 讲义内容
- `ASSIGNMENT.md` - 作业内容
- `RUBRIC.md` - 评分标准
- `ANCHORS.yml` - 锚点（可选）
- `TERMS.yml` - 术语（可选）
- `examples/` - 示例代码（可选）
- `starter_code/` - 起始代码（可选）
- `tests/` - 测试代码（可选）

### 6. 构建站点

开发模式（热重载）：
```bash
make dev
```

生产构建：
```bash
make build
```

静态文件输出到 `site/build/` 目录。

### 7. 部署

将 `site/build/` 目录上传到你的托管服务：

- **Netlify**: 拖拽 `site/build/` 到 Netlify Dashboard
- **Vercel**: `vercel --prod site/build/`
- **GitHub Pages**: 启用 Pages 并指向 `gh-pages` 分支
- **任何静态托管**: 直接上传 `site/build/` 内容

## 内容格式规范

### TOC.md 格式

```markdown
# 课程目录

## 阶段一：阶段名称

| 周次 | 标题 |
|------|------|
| [Week 01](week_01/CHAPTER.md) | 第一周标题 |
| [Week 02](week_02/CHAPTER.md) | 第二周标题 |

## 阶段二：另一阶段

...
```

### CHAPTER.md / ASSIGNMENT.md / RUBRIC.md

标准 Markdown 格式，支持所有 Docusaurus 扩展：

```markdown
# 章节标题

## 小节标题

正文内容...

```java
// 代码块
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello");
    }
}
```

:::tip 提示
这是提示框
:::
```

### ANCHORS.yml 格式

```yaml
- id: anchor-001
  claim: 这是一个主张
  evidence: 这是支持证据
  verification: 验证方式
  status: verified

- id: anchor-002
  claim: 另一个主张
  evidence: 更多证据
  verification: 如何验证
  status: pending
```

### TERMS.yml 格式

```yaml
- chinese: 中文术语
  english: English Term
  definition: 术语定义说明

- chinese: 另一个术语
  english: Another Term
  definition: 另一个定义
```

### glossary.yml 格式

```yaml
term_key:
  chinese: 中文名称
  english: English Name
  definition: 定义内容

another_term:
  chinese: 另一个术语
  english: Another Term
  definition: 定义内容
```

## Makefile 命令

| 命令 | 说明 |
|------|------|
| `make install` | 安装依赖 |
| `make dev` | 开发模式（热重载） |
| `make build` | 生产构建 |
| `make clean` | 清理构建产物 |
| `make help` | 显示所有命令 |

## 自定义配置

### 修改导航栏

编辑 `site/docusaurus.config.ts` 中的 `navbar.items`：

```typescript
navbar: {
  items: [
    {
      to: '/docs/syllabus',
      label: '教学大纲',
      position: 'left',
    },
    // 添加更多链接...
  ],
}
```

### 修改主题色

编辑 `site/src/css/custom.css`：

```css
:root {
  --ifm-color-primary: #2e8555;  /* 主色 */
  --ifm-color-primary-dark: #29784c;
  /* ... */
}
```

### 修改首页

编辑 `site/src/pages/index.tsx`：

```tsx
// 修改 Hero 区域
<header className={clsx('hero hero--primary', styles.heroBanner)}>
  <div className="container">
    <Heading as="h1" className="hero__title">
      你的课程名称
    </Heading>
    <p className="hero__subtitle">课程副标题</p>
  </div>
</header>
```

## 故障排除

### Node 版本问题

```bash
node --version  # 应 >= 18.0
# 使用 nvm 切换
nvm use 20
```

### 依赖安装失败

```bash
cd site
rm -rf node_modules package-lock.json
npm install
```

### 构建脚本失败

```bash
pip install pyyaml
python scripts/build_site.py --verbose
```

## 许可证

MIT
