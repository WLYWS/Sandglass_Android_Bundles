package com.wyze.sandglasslibrary.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseApplication;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SLFViewUtil {

    private SLFViewUtil() {
    }


    /**
     * Adapter使用
     *
     * @param view converView
     * @param id   控件的id
     * @return 返回<T extends View>
     */
    @SuppressWarnings("unchecked")
    public static <E extends View> E get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (null == viewHolder) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (null == childView) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);

        }
        return (E) childView;
    }


    //回收view占用的资源
    private static void clearViewResource(View view) {
        if (view == null) {
            return;
        }

        if (view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(null);
        } else if (view instanceof WebView) {
            WebView webview = (WebView) view;
            webview.setTag(null);
            webview.stopLoading();
            webview.clearHistory();
            try {
                webview.removeAllViews();
            } catch (Exception e) {
                //empty code
            }

            webview.loadUrl("about:blank");

            try {
                //android 5.0 之后webview需要先detach, 再destroy, 防止内存泄露
                ((ViewGroup) webview.getParent()).removeView(webview);
            } catch (Exception e) {
                //empty code
            }
            webview.destroy();
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int childCount = group.getChildCount();

            for (int i = 0; i < childCount; i++) {
                View child = group.getChildAt(i);
                clearViewResource(child);
            }

            try {
                group.removeAllViews();
            } catch (Exception e) {
                //empty code
            }

        }
    }

    public static void clearAllActivityViews(Activity activity) {
        try {
            View view = activity.getWindow().getDecorView()
                    .findViewById(android.R.id.content);

            clearViewResource(view);
        } catch (Exception e) {
            Log.e("ViewUtils", "" + e);

        }
    }

    //设置软键盘
    @SuppressLint("BlockedPrivateApi")
    public static void fixInputMethodManager(Activity destContext) {
        if (destContext == null) {
            return;
        }

        final InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        final SLFReflectorUtil.TypedObject windowToken = new SLFReflectorUtil.TypedObject(destContext.getWindow()
                .getDecorView().getWindowToken(), IBinder.class);

        SLFReflectorUtil.invokeMethodExceptionSafe(imm, "windowDismissed", windowToken);

        final SLFReflectorUtil.TypedObject view = new SLFReflectorUtil.TypedObject(null, View.class);

        SLFReflectorUtil.invokeMethodExceptionSafe(imm, "startGettingWindowFocus", view);

        String[] arr = new String[]{
                "mCurRootView", "mServedView", "mNextServedView"
        };
        Field f;
        Object obj_get;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Exception t) {
                //
            }
        }
    }


    //获取view的所有子view
    public static List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }

    /*
     * @param bitmap 源bitmap
     * @param vw     缩放后指定的宽度
     * @param vh     缩放后指定的高度
     * @return 缩放后的中间部分图片 Bitmap
     */
    @SuppressWarnings("java:S3776")
    public static Bitmap zoomBitmap(Bitmap bitmap, float vw, float vh) {
        float width = bitmap.getWidth();//获得图片宽高
        float height = bitmap.getHeight();

        float scaleWidht;
        float scaleHeight;
        float x;
        float y;//图片缩放倍数以及x，y轴平移位置
        Bitmap newbmp = null; //新的图片
        Matrix matrix = new Matrix();//变换矩阵
        if ((width / height) <= vw / vh) {//当宽高比大于所需要尺寸的宽高比时以宽的倍数为缩放倍数
            scaleWidht = vw / width;
            scaleHeight = scaleWidht;
            y = height - vh / scaleHeight;// 获取bitmap源文件中y做表需要偏移的像数大小
            x = 0;
        } else {
            scaleHeight = vh / height;
            scaleWidht = scaleHeight;
            x = width - vw / scaleWidht;// 获取bitmap源文件中x做表需要偏移的像数大小
            y = 0;
        }
        matrix.postScale(scaleWidht / 1f, scaleHeight / 1f);
        try {
            //获得新的图片 （原图，x轴起始位置，y轴起始位置，x轴结束位置，Y轴结束位置，缩放矩阵，是否过滤原图）为防止报错取绝对值
            if ((vw >= width || vh >= height) || (vw < 100 || vh < 100)) {
                float scale = vw / vh;
                if (width > height) {
                    if (scale > 1) {
                        newbmp = Bitmap.createBitmap(bitmap, 0, (int) Math.abs((height - width / scale) / 2), (int) (width),
                                (int) (width / scale), null, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()

                    } else {
                        newbmp = Bitmap.createBitmap(bitmap, (int) Math.abs((width - height * scale) / 2), 0, (int) (height * scale),
                                (int) (height), null, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()

                    }

                } else {
                    if (scale > 1) {
                        newbmp = Bitmap.createBitmap(bitmap, 0, (int) Math.abs((height - width / scale) / 2), (int) (width),
                                (int) (width / scale), null, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()

                    } else {
                        if (width / scale > height) {
                            newbmp = Bitmap.createBitmap(bitmap, (int) ((width - height * scale) / 2), 0, (int) (height * scale),
                                    (int) height, null, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
                        } else {
                            newbmp = Bitmap.createBitmap(bitmap, 0, (int) Math.abs((height - width / scale) / 2), (int) (width),
                                    (int) (width / scale), null, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()

                        }
                    }

                }
            } else if (height - y > 0 && width - x > 0) {

//					newbmp = Bitmap.createBitmap(bitmap, (int) Math.abs(x/2), (int) Math.abs(y/2), (int) Math.abs(width-x),
//							(int)(height-y), matrix, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
                newbmp = Bitmap.createBitmap(bitmap, (int) Math.abs(x / 2), (int) Math.abs(y / 2), (int) Math.ceil(vw / scaleWidht),
                        (int) Math.ceil(vh / scaleHeight), matrix, false);// createBitmap()方法中定义的参数x+width要小于或等于bitmap.getWidth()，y+height要小于或等于bitmap.getHeight()
            }
        } catch (Exception e) {//如果报错则返回原图，不至于为空白
            return bitmap;
        }
        return newbmp;
    }

    public static Bitmap getBitmapFromPath(String path) {
        try {
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;// optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
            BitmapFactory.decodeFile(path, onlyBoundsOptions);
            int originalWidth = onlyBoundsOptions.outWidth;
            int originalHeight = onlyBoundsOptions.outHeight;
            if ((originalWidth == -1) || (originalHeight == -1)) {
                return null;
            }
            // 图片分辨率以1080fx1920f为标准
            float hh = 1920f;// 这里设置高度为1920f
            float ww = 1080f;// 这里设置宽度为1080f
            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be;// be=1表示不缩放
            if (originalWidth > originalHeight) {// 如果宽度大的话根据宽度固定大小缩放
                be = (int) (originalWidth / ww);
            } else {// 如果高度高的话根据宽度固定大小缩放
                be = (int) (originalHeight / hh);
            }
            if (be <= 0) {
                be = 1;
            }
            // 比例压缩
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = be;// 设置缩放比例
            bitmapOptions.inDither = true;// optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
            return BitmapFactory.decodeFile(path, bitmapOptions);// 再进行质量压缩
        } catch (Exception e) {
            //
        }
        return null;
    }

    public static Bitmap snapShotWithoutStatusBar(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, view.getMeasuredWidth(),
                view.getMeasuredHeight());
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenSize()[0];
        int height = getScreenSize()[1];
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    public static int[] getScreenSize() {
        int[] screens;
        DisplayMetrics dm = SLFBaseApplication.getAppContext().getResources().getDisplayMetrics();
        screens = new int[]{dm.widthPixels, dm.heightPixels};
        return screens;
    }

    public static void savePicture(Bitmap bm, String fileName)
            throws IOException {

        String subForder = SLFConstants.CROP_IMAGE_PATH;
        File foder = new File(subForder);
        if (!foder.exists()) {
            boolean isMkdirs = foder.mkdirs();
            Log.e("savePicture", isMkdirs + "");
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 85, bos);
        bos.flush();
        bos.close();
    }


    public static void savePicture(Bitmap bm, String folderName, String fileName)
            throws IOException {

        File foder = new File(folderName);
        if (!foder.exists()) {
            boolean isMkdirs = foder.mkdirs();
            Log.e("savePicture", isMkdirs + "");
        }
        File myCaptureFile = new File(folderName, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 85, bos);
        bos.flush();
        bos.close();
    }

    public static void savePNGPicture(Bitmap bm, String folderName, String fileName)
            throws IOException {

        File foder = new File(folderName);
        if (!foder.exists()) {
            boolean isMkdirs = foder.mkdirs();
            Log.e("isMkdirs", isMkdirs + "");
        }
        File myCaptureFile = new File(folderName, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 85, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 图片质量压缩方法
     * @param image
     * @return
     */
    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static File compressImage(Bitmap image,String folderName,String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.size()/1024 > 300) { // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options = options - 10;// 每次都减少10
        }
        SLFLogUtil.e("compressImage",options + " " + baos.size()/1024);
        File file = null;
        try {
            File dirFile = new File(folderName);
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }
            file = new File(folderName, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            baos.writeTo(fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SLFLogUtil.e("compressImage"," options123-->" + file.length()/1024);
        return file;
    }

    /**
     * FFmpeg压缩视频
     * -threads： 执行线程数，传入1 单线程压缩
     * -i：input路径，传入视频文件的路径
     * -c:v：编码格式，一般都是指定libx264
     * -crf： 编码质量，取值范围是0-51，默认值为23，数字越小输出视频的质量越高。这里的30是我们经过测试得到的经验值
     * -preset：转码速度，ultrafast，superfast，veryfast，faster，fast，medium，slow，slower，veryslow和placebo。ultrafast编码速度最快，但压缩率低，生成的文件更大，placebo则正好相反。x264所取的默认值为medium。需要说明的是，preset主要是影响编码的速度，并不会很大的影响编码出来的结果的质量。
     * -acodec：音频编码，一般采用libmp3lame
     * arg.thumbVideoPath：最后传入的是视频压缩后保存的路径
     * -y：输出时覆盖输出目录已存在的同名文件（如果不加此参数，就不会覆盖）
     *
     ffmpeg  -i  Desktop/input.mp4  -fs 10MB  Desktop/output.mp4  压缩到指定大小的视频
     */
    public static void compressVideo(String inputPath,String outputPath){

    }

    /**
     * 生成视图的预览
     */
    public static String getScreenShotByView(Activity activity, View v) {
        Bitmap bitmap;
        File foder = new File(SLFConstants.CROP_IMAGE_PATH);
        if (!foder.exists()) {
            boolean isMkdirs = foder.mkdirs();
            Log.e("isMkdirs", isMkdirs + "");
        }
        String path = SLFConstants.CROP_IMAGE_PATH + System.currentTimeMillis() + ".png";
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        v.setVisibility(View.GONE);
        bitmap = view.getDrawingCache();
        v.setVisibility(View.VISIBLE);
        try {
            bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], v.getWidth(), v.getHeight());
            try (FileOutputStream fout = new FileOutputStream(path)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            }
            return path;
        } catch (Exception e) {
            //
        } finally {
            // 清理缓存
            view.destroyDrawingCache();
        }
        return null;

    }

    /**
     * 生成视图的预览(按照指定大小)
     * 视图生成成功返回视图的绝对路径
     */
    public static String getScreenShotByView(Activity activity, View v, int aspectX, int aspectY) {
        Bitmap bitmap;
        File foder = new File(SLFConstants.CROP_IMAGE_PATH);
        if (!foder.exists()) {
            boolean isMkdirs = foder.mkdirs();
            Log.e("getScreenShotByView", isMkdirs + "");
        }
        String path = SLFConstants.CROP_IMAGE_PATH + System.currentTimeMillis() + ".png";
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        v.setVisibility(View.GONE);
        bitmap = view.getDrawingCache();
        v.setVisibility(View.VISIBLE);
        try {
            bitmap = Bitmap.createBitmap(bitmap, location[0], location[1], v.getWidth(), v.getHeight());

            //计算压缩的比率
            float scaleWidth = ((float) aspectX) / v.getWidth();
            float scaleHeight = ((float) aspectY) / v.getHeight();
            //获取想要缩放的matrix
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            //获取新的bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, v.getWidth(), v.getHeight(), matrix, true);

            try (FileOutputStream fout = new FileOutputStream(path)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            }
            return path;
        } catch (Exception e) {
            //
        } finally {
            // 清理缓存
            view.destroyDrawingCache();
        }
        return null;

    }

    /**
     * 获取图片缩略图
     **/
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        // Options中有个属性inJustDecodeBounds。我们可以充分利用它，来避免大图片的溢出问题
        options.inJustDecodeBounds = true;// 设置为true可以不加载到内存，直接获取Bitmap宽高
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        if (bitmap == null) {
            // 计算缩放比
            int h = options.outHeight;// 获取Bitmap的实际高度
            int w = options.outWidth;// 获取Bitmap的实际宽度

            int beWidth = w / width;
            int beHeight = h / height;
            int rate = Math.min(beWidth, beHeight);
            if (rate <= 0) {// 图片实际大小小于缩略图,不缩放
                rate = 1;
            }
            options.inSampleSize = rate;// rate就是压缩的比例
            options.inJustDecodeBounds = false;
            // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
            bitmap = BitmapFactory.decodeFile(imagePath, options);// 获取压缩后的图片
        }
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressWarnings({"WeakerAccess"})
    public static Bitmap decodeBitmap(byte[] source) {
        int orientation;
        boolean flip;
        try {
            ExifInterface exif;
            try (InputStream stream = new ByteArrayInputStream(source)) {
                exif = new ExifInterface(stream);
            }
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (exifOrientation) {

                case ExifInterface.ORIENTATION_ROTATE_180:
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    orientation = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_90:
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    orientation = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    orientation = 270;
                    break;

                default:
                    orientation = 0;
            }

            flip = exifOrientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL ||
                    exifOrientation == ExifInterface.ORIENTATION_FLIP_VERTICAL ||
                    exifOrientation == ExifInterface.ORIENTATION_TRANSPOSE ||
                    exifOrientation == ExifInterface.ORIENTATION_TRANSVERSE;

        } catch (IOException e) {
            orientation = 0;
            flip = false;
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(source, 0, source.length);

        if (orientation != 0 || flip) {
            Matrix matrix = new Matrix();
            matrix.setRotate(orientation);
            // matrix.postScale(1, -1) Flip... needs testing.
            Bitmap temp = bitmap;
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            temp.recycle();
        }
        return bitmap;
    }

    /*
     * 获取控件宽
     */
    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }

    /*
     * 获取控件高
     */
    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());
    }
    /**设置TextView 左上右下各draable的图标*/
    public static void setTextViewDrawableLTRD(TextView view, int drawableId, String ltrd) {
        Drawable drawable = SLFResourceUtils.getDrawable(drawableId);

/// 这一步必须要做,否则不会显示.

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (ltrd) {
            case "left":
                view.setCompoundDrawables(drawable, null, null, null);
                break;
            case "top" :
                view.setCompoundDrawables(null, drawable, null, null);
                break;
            case "right":
                view.setCompoundDrawables(null, null, drawable, null);
                break;
            case "bottom":
                view.setCompoundDrawables(null, null, null, drawable);
                break;
            default:
                view.setCompoundDrawables(null, null, drawable, null);
                break;
        }
    }
    /**imageview progress showing*/
    public static void slfImageViewStartAnim(ImageView view) {
        view.clearAnimation();
        view.startAnimation(getLoadingAnim());
    }
    /**imageview hideAnim*/
    public static void  slfImageViewCancelAnim(ImageView view){
        view.clearAnimation();
    }
    /**初始化一个rotate动画*/
    private static Animation getLoadingAnim() {
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(450);
        animation.setRepeatCount(-1);
        animation.setFillAfter(true);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

}