package com.sandglass.sandglasslibrary.utils;

import android.graphics.Bitmap;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.sandglass.sandglasslibrary.R;

/**
 * created by yangjie
 * describe:根据枚举类型生成不同的形状
 * time: 2022/12/7
 * Copyright© 2019 WYZE.
 */
public class SLFImageTransformation {

    private int resId; //用于图形遮罩的图片资源id

    @SuppressWarnings("rawtypes")
    private Transformation<Bitmap> mTransformation = new SLFCropCircleTransformation();
    public SLFImageTransformation(SLFImageShapes shape){
        initShape(shape);
    }
    public SLFImageTransformation(SLFImageShapes shape, int resId){
        this.resId = resId;
        initShape(shape);
    }

    private void initShape(SLFImageShapes shape) {
        switch (shape) {
            case CENTERCROP:
                mTransformation = new CenterCrop();
                break;
            case CIRCLE:
                mTransformation = new SLFCropCircleTransformation();
                break;
            case ROUND:
                mTransformation = new SLFRoundedCornersTransformation(shape.getRadius(),0,shape.getCornerType());
                break;
            case SQUARE:
                mTransformation = new SLFCropSquareTransformation();
                break;
            case BLUR:
                mTransformation = new SLFBlurTransformation(shape.getRadius());
                break;
            case GRAYSCALE:
                mTransformation = new SLFGrayscaleTransformation();
                break;
            case MASK:
                if(resId==0){
                    resId = R.drawable.ic_launcher_background;
                }
                mTransformation = new SLFMaskTransformation(resId);
                break;
            case ROTATE:
                mTransformation = new SLFRotateTransformation(shape.getRotationAngle());
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("rawtypes")
    public Transformation<Bitmap> getTransformation() {
        return mTransformation;
    }
    @SuppressWarnings("rawtypes")
    public void setTransformation(Transformation<Bitmap> transformation) {
        this.mTransformation = transformation;
    }

}
