package com.sandglass.sandglasslibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

public class SLFCropCircleTransformation extends SLFBitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "jp.wasabeef.glide.transformations.CropCircleTransformation." + VERSION;

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.circleCrop(pool, toTransform, outWidth, outHeight);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SLFCropCircleTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID).getBytes(CHARSET));
    }
}
