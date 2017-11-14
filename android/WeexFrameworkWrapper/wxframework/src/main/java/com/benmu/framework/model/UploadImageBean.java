package com.benmu.framework.model;

import java.io.Serializable;

/**
 * 选择图片，数据，解析Bean
 */
public class UploadImageBean implements Serializable {
    public int maxCount; //最大可选择几张
    public double imageWidth;// 压缩后，图片最大宽度
    public boolean allowCrop;//是否需要剪切为圆形
}
