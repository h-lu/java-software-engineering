/**
 * Slide Framework - 共享幻灯片导航和交互逻辑
 * 用于 Java 软件工程课程幻灯片
 */

// 幻灯片导航
let currentSlide = 0;
let slides = [];
let totalSlides = 0;

/**
 * 初始化幻灯片框架
 */
function initSlideFramework() {
    slides = document.querySelectorAll('.slide');
    totalSlides = slides.length;
    
    // 更新总页数显示
    const totalPagesEl = document.getElementById('totalPages');
    if (totalPagesEl) {
        totalPagesEl.textContent = totalSlides;
    }
    
    // 生成缩略图
    generateThumbnails();
    
    // 初始化代码高亮
    if (typeof hljs !== 'undefined') {
        hljs.highlightAll();
    }
    
    // 设置滚动监听
    setupScrollObserver();
    
    // 设置键盘导航
    setupKeyboardNavigation();
}

/**
 * 显示指定幻灯片
 */
function showSlide(index) {
    if (index < 0 || index >= totalSlides) return;
    currentSlide = index;
    slides[index].scrollIntoView({ behavior: 'smooth', block: 'center' });
    updateThumbnails();
    
    const currentPageEl = document.getElementById('currentPage');
    if (currentPageEl) {
        currentPageEl.textContent = index + 1;
    }
}

/**
 * 下一页
 */
function nextSlide() {
    showSlide(currentSlide + 1);
}

/**
 * 上一页
 */
function prevSlide() {
    showSlide(currentSlide - 1);
}

/**
 * 切换全屏
 */
function toggleFullscreen() {
    const slide = slides[currentSlide];
    if (slide.requestFullscreen) {
        slide.requestFullscreen();
    } else if (slide.webkitRequestFullscreen) {
        slide.webkitRequestFullscreen();
    } else if (slide.msRequestFullscreen) {
        slide.msRequestFullscreen();
    }
}

/**
 * 生成缩略图导航
 */
function generateThumbnails() {
    const container = document.getElementById('thumbnails');
    if (!container) return;
    
    slides.forEach((slide, index) => {
        const thumb = document.createElement('div');
        thumb.className = 'thumbnail' + (index === 0 ? ' active' : '');
        thumb.onclick = () => showSlide(index);
        thumb.title = `Slide ${index + 1}`;
        
        // 使用 canvas 生成缩略图
        const canvas = document.createElement('canvas');
        canvas.width = 240;
        canvas.height = 135;
        const ctx = canvas.getContext('2d');
        
        // 获取幻灯片背景色
        const computedStyle = getComputedStyle(slide);
        const bgColor = computedStyle.backgroundColor || computedStyle.background;
        ctx.fillStyle = bgColor !== 'rgba(0, 0, 0, 0)' ? bgColor : '#ffffff';
        ctx.fillRect(0, 0, 240, 135);
        
        // 添加页码标签
        ctx.fillStyle = '#0D7377';
        ctx.font = 'bold 24px Inter, sans-serif';
        ctx.textAlign = 'center';
        ctx.fillText(String(index + 1), 120, 75);
        
        const img = document.createElement('img');
        img.src = canvas.toDataURL();
        img.alt = `Slide ${index + 1}`;
        thumb.appendChild(img);
        container.appendChild(thumb);
    });
}

/**
 * 更新缩略图激活状态
 */
function updateThumbnails() {
    document.querySelectorAll('.thumbnail').forEach((thumb, index) => {
        thumb.classList.toggle('active', index === currentSlide);
    });
}

/**
 * 设置滚动监听
 */
function setupScrollObserver() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const index = Array.from(slides).indexOf(entry.target);
                currentSlide = index;
                
                const currentPageEl = document.getElementById('currentPage');
                if (currentPageEl) {
                    currentPageEl.textContent = index + 1;
                }
                updateThumbnails();
            }
        });
    }, { threshold: 0.5 });
    
    slides.forEach(slide => observer.observe(slide));
}

/**
 * 设置键盘导航
 */
function setupKeyboardNavigation() {
    document.addEventListener('keydown', (e) => {
        // 防止在输入框中触发
        if (e.target.tagName === 'INPUT' || e.target.tagName === 'TEXTAREA') {
            return;
        }
        
        switch(e.key) {
            case 'ArrowRight':
            case ' ':
            case 'PageDown':
                e.preventDefault();
                nextSlide();
                break;
            case 'ArrowLeft':
            case 'PageUp':
                e.preventDefault();
                prevSlide();
                break;
            case 'Home':
                e.preventDefault();
                showSlide(0);
                break;
            case 'End':
                e.preventDefault();
                showSlide(totalSlides - 1);
                break;
            case 'f':
            case 'F':
                if (!e.ctrlKey && !e.metaKey) {
                    e.preventDefault();
                    toggleFullscreen();
                }
                break;
        }
    });
}

/**
 * 跳转到指定幻灯片（URL hash 支持）
 */
function jumpToSlideFromHash() {
    const hash = window.location.hash;
    if (hash) {
        const match = hash.match(/slide-(\d+)/);
        if (match) {
            const slideNum = parseInt(match[1], 10) - 1;
            showSlide(slideNum);
        }
    }
}

/**
 * 更新 URL hash（可选）
 */
function updateHash() {
    window.history.replaceState(null, null, `#slide-${currentSlide + 1}`);
}

// DOM 加载完成后初始化
document.addEventListener('DOMContentLoaded', () => {
    initSlideFramework();
    jumpToSlideFromHash();
});

// 导出公共 API（供外部使用）
window.SlideFramework = {
    showSlide,
    nextSlide,
    prevSlide,
    toggleFullscreen,
    getCurrentSlide: () => currentSlide,
    getTotalSlides: () => totalSlides
};
