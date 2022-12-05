package com.wyze.sandglasslibrary.base;

import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.bean.Constants;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;

/**
 * Greated by yangjie 2022/12/2
 */
public class SLFBaseActivity extends FragmentActivity {
    private static final String TAG = "SLFBaseActivity";

    public static Map<Integer, WeakReference<Activity>> mActivityStack = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置activity无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //管理acitivty的集合
        mActivityStack.put(hashCode(), new WeakReference<>(this));
        //是否要一键变灰
        if(Constants.isGreyed){
            ColorGrayed();
        }
    }

    @Override
    protected void onDestroy() {
        try{
            super.onDestroy();
            //activity摧毁时根据hashcode从集合移除
            mActivityStack.remove(hashCode());
        }catch (Exception e){
            Log.e(TAG,Log.getStackTraceString(e));
        }

    }
    //一键变灰
    private void ColorGrayed(){
        Paint paint = new Paint();
        ColorMatrix colorMatrix= new ColorMatrix();
        colorMatrix.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        getWindow().getDecorView().setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    }
}