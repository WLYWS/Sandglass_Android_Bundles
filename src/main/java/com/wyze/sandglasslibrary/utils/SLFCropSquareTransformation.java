package com.wyze.sandglasslibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

public class SLFCropSquareTransformation extends SLFBitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "jp.wasabeef.glide.transformations.CropSquareTransformation." + VERSION;

    private int size;

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        this.size = Math.max(outWidth, outHeight);
        return TransformationUtils.centerCrop(pool, toTransform, size, size);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SLFCropSquareTransformation && ((SLFCropSquareTransformation) o).size == size;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + size * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + size).getBytes(CHARSET));
    }
}