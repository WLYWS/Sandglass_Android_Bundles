package com.wyze.sandglasslibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

/**
 * Greated by yangjie
 * Descrip:给加载图片加遮罩
 * time:2022/12/7
 */
public class SLFMaskTransformation extends SLFBitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "jp.wasabeef.glide.transformations.MaskTransformation." + VERSION;

    private static Paint paint = new Paint();
    private int maskId;

    static {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    /**
     * @param maskId If you change the mask file, please also rename the mask file, or Glide will get
     *               the cache with the old mask. Because key() return the same values-en if using the
     *               same make file name. If you have a good idea please tell us, thanks.
     */
    public SLFMaskTransformation(int maskId) {
        this.maskId = maskId;
    }

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();

        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(true);

        Drawable mask = SLFMaskDrawableUtil.getMaskDrawable(context.getApplicationContext(), maskId);

        Canvas canvas = new Canvas(bitmap);
        mask.setBounds(0, 0, width, height);
        mask.draw(canvas);
        canvas.drawBitmap(toTransform, 0, 0, paint);

        return bitmap;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SLFMaskTransformation &&
                ((SLFMaskTransformation) o).maskId == maskId;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + maskId * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + maskId).getBytes(CHARSET));
    }
}
