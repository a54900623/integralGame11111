package com.party.game.common.utils.img;

public class  CompressResult {
    private Float ratio;
    private Integer width;
    private Integer height;
    private Long size;
    private String name;
    private byte[] bytes;


    public Float getRatio() {
        return ratio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRatio(Float ratio) {
        this.ratio = ratio;

    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}