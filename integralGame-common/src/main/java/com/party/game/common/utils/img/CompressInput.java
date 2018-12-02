package com.party.game.common.utils.img;

import java.io.InputStream;

public class CompressInput {
    private String formatName; // 文件格式
    private String fileName; // 文件名
    private InputStream imgInput; // 图片文件流
    private String strUrl;// 本地或远程路径
    private Integer maxWidth;// 最大宽度
    private Integer maxHeight;// 最大高度
    private Long maxSize;// 限制大小

    public String getStrUrl() {
        return strUrl;
    }

    public void setStrUrl(String strUrl) {
        this.strUrl = strUrl;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public InputStream getImgInput() {
        return imgInput;
    }

    public void setImgInput(InputStream imgInput) {
        this.imgInput = imgInput;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }
}
