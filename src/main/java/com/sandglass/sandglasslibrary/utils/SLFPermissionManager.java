package com.sandglass.sandglasslibrary.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.commonui.SLFCancelOrOkDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * created by yangjie
 * describe:权限请求工具类
 * time: 2022/12/5
 */

public class SLFPermissionManager {
    /**
     * 权限请求码
     */
    private final int mRequestCode = 100;
    public static boolean showSystemSetting = true;
    List<String> mPermissionList;

    private SLFPermissionManager() {
    }

    private static SLFPermissionManager slfPermissionManger;
    private IPermissionsResult mPermissionsResult;

    public static SLFPermissionManager getInstance() {
        if (slfPermissionManger == null) {
            slfPermissionManger = new SLFPermissionManager();
        }
        return slfPermissionManger;
    }

    public void chekPermissions(Activity context, String[] permissions, @NonNull IPermissionsResult permissionsResult) {
        mPermissionsResult = permissionsResult;
        /**6.0才用动态权限*/
        if (Build.VERSION.SDK_INT < 23) {
            permissionsResult.passPermissons();
            return;
        }

        /**创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中*/
        List<String> mPermissionList = new ArrayList<>();
        /**逐个判断你要的权限是否已经通过*/
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                /**添加还未授予的权限*/
                mPermissionList.add(permissions[i]);
            }
        }

        /**申请权限*/
        /**有权限没有通过，需要申请*/
        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(context, permissions, mRequestCode);
        } else {
            /**说明权限都已经通过，可以做你想做的事情去*/
            permissionsResult.passPermissons();
            return;
        }


    }

    /**
     * 请求权限后回调的方法
     * 参数： requestCode  是我们自己定义的权限请求码
     * 参数： permissions  是我们请求的权限名称数组
     * 参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
     */

    public void onRequestPermissionsResult(Activity context, int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        /**有权限没有通过*/
        boolean hasPermissionDismiss = false;
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            /**如果有权限没有被允许*/
            if (hasPermissionDismiss) {
//                if (mPermissionList.size() > 0) {
//                    ActivityCompat.requestPermissions(context, permissions, mRequestCode);
//                }
                showSystemPermissionsSettingDialog(context);
//                if (showSystemSetting) {
//                    /**跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问*/
//                    showSystemPermissionsSettingDialog(context);
//                } else {
//                    mPermissionsResult.forbitPermissons();
//                }
            } else {
                /**全部权限通过，可以进行下一步操作。。。*/
                mPermissionsResult.passPermissons();
            }
        }

    }


    /**
     * 不再提示权限时的展示对话框
     */
    AlertDialog mPermissionDialog;

    private void showSystemPermissionsSettingDialog(final Activity context) {
//        final String mPackName = context.getPackageName();
//        if (mPermissionDialog == null) {
//            mPermissionDialog = new AlertDialog.Builder(context)
//                    .setMessage(SLFResourceUtils.getString(R.string.slf_permission_text))
//                    .setPositiveButton(SLFResourceUtils.getString(R.string.slf_permission_text_setting), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            cancelPermissionDialog();
//
//                            Uri packageURI = Uri.parse("package:" + mPackName);
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                            context.startActivity(intent);
//                            context.finish();
//                        }
//                    })
//                    .setNegativeButton(SLFResourceUtils.getString(R.string.slf_title_cancel), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            /**关闭页面或者做其他操作*/
//                            cancelPermissionDialog();
//                            /**mContext.finish();*/
//                            mPermissionsResult.forbitPermissons();
//                        }
//                    })
//                    .create();
//        }
//        mPermissionDialog.show();
        final String mPackName = context.getPackageName();
        SLFCancelOrOkDialog slfHintDialog = new SLFCancelOrOkDialog(context, SLFCancelOrOkDialog.STYLE_CANCEL_OK);
        slfHintDialog.setTitle(SLFResourceUtils.getString(R.string.slf_permission_text_title));
        slfHintDialog.setContentText(SLFResourceUtils.getString(R.string.slf_permission_text_content));
        slfHintDialog.setLeftBtnText(SLFResourceUtils.getString(R.string.slf_title_cancel));
        slfHintDialog.setRightBtnText(SLFResourceUtils.getString(R.string.slf_permission_text_go_to_set));
        slfHintDialog.setOnListener(new SLFCancelOrOkDialog.OnHintDialogListener() {

            @Override
            public void onClickOk() {
                cancelPermissionDialog();

                Uri packageURI = Uri.parse("package:" + mPackName);
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                context.startActivity(intent);
                context.finish();
            }

            @Override
            public void onClickCancel() {
                /**关闭页面或者做其他操作*/
                cancelPermissionDialog();
                /**mContext.finish();*/
                mPermissionsResult.forbitPermissons();
            }

            @Override
            public void onClickOther() {

            }
        });

        slfHintDialog.show();
    }
//    private void showSystemPermissionsSettingDialog(final Activity context) {
//        final String mPackName = context.getPackageName();
//        if (mPermissionDialog == null) {
//            mPermissionDialog = new AlertDialog.Builder(context)
//                    .setMessage(SLFResourceUtils.getString(R.string.slf_permission_text))
//                    .setPositiveButton(SLFResourceUtils.getString(R.string.slf_permission_text_setting), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            cancelPermissionDialog();
//
//                            Uri packageURI = Uri.parse("package:" + mPackName);
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                            context.startActivity(intent);
//                            context.finish();
//                        }
//                    })
//                    .setNegativeButton(SLFResourceUtils.getString(R.string.slf_title_cancel), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            /**关闭页面或者做其他操作*/
//                            cancelPermissionDialog();
//                            /**mContext.finish();*/
//                            mPermissionsResult.forbitPermissons();
//                        }
//                    })
//                    .create();
//        }
//        mPermissionDialog.show();
//    }

    /**
     * 关闭对话框
     */
    private void cancelPermissionDialog() {
        if (mPermissionDialog != null) {
            mPermissionDialog.cancel();
            mPermissionDialog = null;
        }

    }

public interface IPermissionsResult {
    void passPermissons();

    void forbitPermissons();
}

}
