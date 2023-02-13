package com.sandglass.sandglasslibrary.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.commonapi.SLFApi;

import java.io.File;
import java.io.FileDescriptor;
import java.lang.reflect.Array;

public class SLFImageUtil {

    private static int placeholderImgResId;
    private static int errorImgResId;

    private SLFImageUtil(){}

    public static void setPlaceholderRes(int resId){
        placeholderImgResId = resId;
    }

    public static int getPlaceholderRes(){
        if(placeholderImgResId==0){
            return R.drawable.ic_launcher_background;
        }
        return placeholderImgResId;
    }

    public static void setErrorImgRes(int resId){
        errorImgResId = resId;
    }

    public static int getErrorImgRes(){
        if(errorImgResId==0){
            return R.drawable.ic_launcher_background;
        }
        return errorImgResId;
    }

    public static RequestManager with(Context context){
        return Glide.with(context);
    }

    /**
     * 默认方式加载图片
     */
    public static void loadDefaultImage(Context context, Object url, ImageView img) {
        Glide.with(context)
                .load(url)
                .into(img);
    }

    /**
     * 默认方式加载本地资源图片
     */
    @SuppressWarnings("unchecked")
    public static void loadDefaultImage(Context context, Object url, ImageView img, SLFImageShapes... shape) {

        Transformation<Bitmap>[] transformations = (Transformation<Bitmap>[]) Array.newInstance(Transformation.class, shape.length);
        for (int i = 0; i < shape.length; i++) {
            transformations[i] = new SLFImageTransformation(shape[i]).getTransformation();
        }

        Glide.with(context)
                .load(url)
                .centerCrop()
                .transform(transformations)
                .into(img);
    }

    /**
     * 默认方式加载图片
     */
    public static void loadDefaultImage(Context context, int resId, ImageView img) {
        Glide.with(context)
                .load(resId)
                .into(img);
    }

    /**
     * 默认方式加载图片
     */
//    public static void loadSvg(Context context,Object url, ImageView img) {
//        Glide.with(context)
//                .as(PictureDrawable.class)
//                .load(url)
//                .listener(new SvgSoftwareLayerSetter())
//                .into(img);
//    }


    /**
     * 默认方式加载图片
     */
    public static void loadImage(Context context, Object url, ImageView img) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(getPlaceholderRes())
                .error(getErrorImgRes())
                .into(img);
    }


    /**
     * 默认方式加载本地资源图片
     */
    @SuppressWarnings("unchecked")
    public static void loadImage(Context context, int resId, ImageView img, SLFImageShapes... shape) {

        Transformation<Bitmap>[] transformations = (Transformation<Bitmap>[]) Array.newInstance(Transformation.class, shape.length);
        for (int i = 0; i < shape.length; i++) {
            transformations[i] = new SLFImageTransformation(shape[i]).getTransformation();
        }


        Glide.with(context)
                .load(resId)
                .centerCrop()
                .placeholder(getPlaceholderRes())
                .error(getErrorImgRes())
                .transform(transformations)
                .into(img);
    }

    /**
     * 默认方式加载二进制流图片
     */
    @SuppressWarnings("unchecked")
    public static void loadImage(Context context, byte[] imgBytes, ImageView img, SLFImageShapes... shape) {

        Transformation<Bitmap>[] transformations = (Transformation<Bitmap>[]) Array.newInstance(Transformation.class, shape.length);
        for (int i = 0; i < shape.length; i++) {
            transformations[i] = new SLFImageTransformation(shape[i]).getTransformation();
        }

        Glide.with(context)
                .load(imgBytes)
                .centerCrop()
                .placeholder(getPlaceholderRes())
                .error(getErrorImgRes())
                .transform(transformations)
                .into(img);
    }

    /**
     * 自定义形状加载图片
     */
    public static void loadImage(Context context, Object url, ImageView img, @SuppressWarnings("rawtypes") Transformation<Bitmap> transformation) {
        Glide.with(context)
                .load(url)
                .placeholder(getPlaceholderRes())
                .error(getErrorImgRes())
                .transform(transformation)
                .into(img);
    }


    /**
     * 用已定义的特殊形状加载图片
     */
    public static void loadImage(Context context, Object url, ImageView img, SLFImageShapes shape) {
        Glide.with(context)
                .load(url)
                .placeholder(getPlaceholderRes())
                .error(getErrorImgRes())
                .transform(new SLFImageTransformation(shape).getTransformation())
                .into(img);
    }


    /**
     * 用已定义的多种特殊形状叠加加载图片
     */
    @SuppressWarnings("unchecked")
    public static void loadImage(Context context, Object url, ImageView img, SLFImageShapes... shape) {
        Transformation<Bitmap>[] transformations = (Transformation<Bitmap>[]) Array.newInstance(Transformation.class, shape.length);
        for (int i = 0; i < shape.length; i++) {
            transformations[i] = new SLFImageTransformation(shape[i]).getTransformation();
        }

        Glide.with(context)
                .load(url)
                .placeholder(getPlaceholderRes())
                .error(getErrorImgRes())
                .transform(transformations)
                .into(img);
    }


    /**
     * 用自定义的特殊图形遮罩加载图片
     */
    @SuppressWarnings("unchecked")
    public static void loadMaskImage(Context context, Object url, ImageView img, int resId) {
        Glide.with(context)
                .load(url)
                .placeholder(getPlaceholderRes())
                .error(getErrorImgRes())
                .transform(new SLFImageTransformation(SLFImageShapes.SQUARE).getTransformation(), new SLFImageTransformation(SLFImageShapes.MASK, resId).getTransformation())
                .into(img);
    }


    /**
     * 用自定义的特殊图形遮罩加载图片
     */
    @SuppressWarnings("unchecked")
    public static void loadMaskImage(Context context, Object url, ImageView img, int resId,SLFImageShapes shape) {
        Glide.with(context)
                .load(url)
                .placeholder(getPlaceholderRes())
                .error(getErrorImgRes())
                .transform(new SLFImageTransformation(SLFImageShapes.SQUARE).getTransformation(), new SLFImageTransformation(SLFImageShapes.MASK, resId).getTransformation(),new SLFImageTransformation(shape).getTransformation())
                .into(img);
    }


    /**
     * 用自定义的占位图和缺省图加载图片
     */
    @SuppressWarnings("unchecked")
    public static void loadImage(Context context, Object url, ImageView img,int placeholderImgResId,int errorImgResId,SLFImageShapes... shape) {
        Transformation<Bitmap>[] transformations = (Transformation<Bitmap>[]) Array.newInstance(Transformation.class, shape.length);
        for (int i = 0; i < shape.length; i++) {
            transformations[i] = new SLFImageTransformation(shape[i]).getTransformation();
        }

        Glide.with(context)
                .load(url)
                .placeholder(placeholderImgResId)
                .error(errorImgResId)
//              .thumbnail(0.2f)
//		        .apply(RequestOptions.bitmapTransform(new ImageTransformation(context, shape).getTransformation()))
                .transform(transformations)
                //注:是否跳过内存缓存，设置为false，如为true的话每次闪烁也正常~
                .skipMemoryCache(false)
                //取消Glide自带的动画
                .dontAnimate()
                .into(img);
    }

    /**
     * 历史留言展示图片
     */
    @SuppressWarnings("unchecked")
    public static void loadImageShow(Context context, Object url, ImageView img,View view, int placeholderImgResId,int errorImgResId, SLFImageShapes... shape) {
        Transformation<Bitmap>[] transformations = (Transformation<Bitmap>[]) Array.newInstance(Transformation.class, shape.length);
        for (int i = 0; i < shape.length; i++) {
            transformations[i] = new SLFImageTransformation(shape[i]).getTransformation();
        }

        Glide.with(context)
                .load(url)
                .placeholder(placeholderImgResId)
                .thumbnail(0.1f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        view.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_feedback_add_photo_bg));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        view.setBackground(null);
                        return false;
                    }
                })
                .error(errorImgResId)
                .transform(transformations)
                //注:是否跳过内存缓存，设置为false，如为true的话每次闪烁也正常~
                .skipMemoryCache(false)
                //取消Glide自带的动画
                .dontAnimate()
                .into(img);
    }


    //清理内存缓存
    public static void clearMemory(){
        Glide.get(SLFApi.getSLFContext()).clearMemory();
    }

    //清理硬盘缓存
    public static void clearDiskCache(){
        Glide.get(SLFApi.getSLFContext()).clearMemory();
        Glide.get(SLFApi.getSLFContext()).clearDiskCache();
    }

    /**
     * 按一定角度旋转图片
     * @param rotateRotationAngle 图片旋转的角度
     * @param toTransform  要旋转的图片
     * @return 旋转后的图片
     */
    public static Bitmap rotateImage(float rotateRotationAngle, @NonNull Bitmap toTransform){
        Matrix matrix = new Matrix();
        matrix.setRotate(rotateRotationAngle);
        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
    }

    public static Uri getImageUriByPath(Context context, String path) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { path }, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range")
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            // 如果图片不在手机的共享图片数据库，就先把它插入。
            if (new File(path).exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, path);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Uri getImageUriById(long id) {
        Uri baseUri = Uri.parse("content://media/external/images/media");
        return Uri.withAppendedPath(baseUri, "" + id);
    }

    public static Uri getVideoUriById(long id) {
        Uri baseUri = Uri.parse("content://media/external/video/media");
        return Uri.withAppendedPath(baseUri, "" + id);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri){
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            //
        }
        return null;
    }

}
