package com.wyze.sandglasslibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

/**
 * Created by yangjie on 2022/12/7.
 */
public class SLFRotateTransformation extends SLFBitmapTransformation {
    private static final int VERSION = 1;
    private static final String ID = "jp.wasabeef.glide.transformations.RotateTransformation." + VERSION;

    private float rotateRotationAngle;

    public SLFRotateTransformation(float rotateRotationAngle) {
        this.rotateRotationAngle = rotateRotationAngle;
    }

    public String getId() {
        return "rotate" + rotateRotationAngle;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + rotateRotationAngle).getBytes(CHARSET));
    }

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return SLFImageUtil.rotateImage(rotateRotationAngle,toTransform);
    }
}
