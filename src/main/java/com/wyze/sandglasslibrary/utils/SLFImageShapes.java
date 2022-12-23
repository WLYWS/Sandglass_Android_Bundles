package com.wyze.sandglasslibrary.utils;
/**
 * created by yangjie
 * describe:图片形状类型枚举
 * time: 2022/12/7
 */
public enum SLFImageShapes {
    CENTERCROP,             //居中裁剪
    CIRCLE,                 //圆形
    ROUND(15),        //圆角矩形
    SQUARE,                 //正方形
    BLUR(12),         //高斯模糊
    GRAYSCALE,              //去色
    MASK,                   //图形遮罩
    ROTATE(90f);                 //旋转图片

    private int radius;
    private SLFRoundedCornersTransformation.CornerType cornerType = SLFRoundedCornersTransformation.CornerType.ALL;
    private float rotationAngle;
    SLFImageShapes(){}
    SLFImageShapes(int radius){
        this.radius = radius;
    }
    SLFImageShapes(float rotationAngle){
        this.rotationAngle = rotationAngle;
    }

    public static SLFImageShapes ROUND(int radius) {
        return SLFImageShapes.ROUND.setRadius(SLFConvertUtil.dp2px(radius));
    }

    public static SLFImageShapes ROUND(int radius, SLFRoundedCornersTransformation.CornerType cornerType) {
        return SLFImageShapes.ROUND.setRadius(SLFConvertUtil.dp2px(radius)).setCornerType(cornerType);
    }

    public static SLFImageShapes BLUR(int radius) {
        return SLFImageShapes.BLUR.setRadius(SLFConvertUtil.dp2px(radius));
    }

    public static SLFImageShapes ROTATE(float rotationAngle) {
        return SLFImageShapes.ROTATE.setRotationAngle(rotationAngle);
    }

    public int getRadius(){
        return radius;
    }
    private SLFImageShapes setRadius(int radius){
        this.radius = radius;
        return this;
    }
    private SLFImageShapes setCornerType(SLFRoundedCornersTransformation.CornerType cornerType){
        this.cornerType = cornerType;
        return this;
    }
    public SLFRoundedCornersTransformation.CornerType getCornerType(){
        return cornerType;
    }

    public float getRotationAngle() {
        return rotationAngle;
    }
    private SLFImageShapes setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
        return this;
    }
}
