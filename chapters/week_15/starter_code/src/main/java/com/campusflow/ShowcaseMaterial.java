package com.campusflow;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示材料类 - 用于验证展示材料的完整性
 *
 * Week 15: 项目集市与展示准备
 * 本类用于验证 PPT、海报等展示材料是否符合要求
 */
public class ShowcaseMaterial {

    private String projectName;
    private List<String> pptSlides;
    private String posterContent;
    private List<String> qrCodes;

    public ShowcaseMaterial(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            throw new IllegalArgumentException("项目名称不能为空");
        }
        this.projectName = projectName;
        this.pptSlides = new ArrayList<>();
        this.qrCodes = new ArrayList<>();
    }

    /**
     * 添加 PPT 页面
     */
    public void addPptSlide(String slideContent) {
        if (slideContent == null || slideContent.trim().isEmpty()) {
            throw new IllegalArgumentException("PPT 内容不能为空");
        }
        // 检查一页信息是否过多（超过 300 字符）
        if (slideContent.length() > 300) {
            throw new IllegalArgumentException("PPT 一页信息过多，请精简至 300 字符以内");
        }
        pptSlides.add(slideContent);
    }

    /**
     * 设置海报内容
     */
    public void setPosterContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("海报内容不能为空");
        }
        // 检查海报是否包含关键要素
        if (!content.contains("问题") && !content.contains("解决方案")) {
            throw new IllegalArgumentException("海报必须包含问题与解决方案");
        }
        this.posterContent = content;
    }

    /**
     * 添加二维码
     */
    public void addQrCode(String qrCodeType, String url) {
        if (qrCodeType == null || qrCodeType.trim().isEmpty()) {
            throw new IllegalArgumentException("二维码类型不能为空");
        }
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("二维码 URL 不能为空");
        }
        // 验证 URL 格式
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("URL 必须以 http:// 或 https:// 开头");
        }
        qrCodes.add(qrCodeType + ": " + url);
    }

    /**
     * 验证 PPT 页数是否合理（12-15 页）
     */
    public boolean validatePptPageCount() {
        return pptSlides.size() >= 12 && pptSlides.size() <= 15;
    }

    /**
     * 验证海报内容是否完整
     */
    public boolean validatePosterContent() {
        return posterContent != null && !posterContent.trim().isEmpty();
    }

    /**
     * 验证二维码数量（至少 2 个：GitHub 和在线演示）
     */
    public boolean validateQrCodeCount() {
        return qrCodes.size() >= 2;
    }

    /**
     * 验证所有材料是否完整
     */
    public boolean isComplete() {
        return validatePptPageCount() && validatePosterContent() && validateQrCodeCount();
    }

    // Getters
    public String getProjectName() {
        return projectName;
    }

    public int getPptSlideCount() {
        return pptSlides.size();
    }

    public List<String> getPptSlides() {
        return new ArrayList<>(pptSlides);
    }

    public String getPosterContent() {
        return posterContent;
    }

    public int getQrCodeCount() {
        return qrCodes.size();
    }

    public List<String> getQrCodes() {
        return new ArrayList<>(qrCodes);
    }
}
