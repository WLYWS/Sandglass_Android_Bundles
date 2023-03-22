package com.sandglass.sandglasslibrary.utils;

import android.app.Activity;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.sandglass.sandglasslibrary.base.SLFFileData;
import com.sandglass.sandglasslibrary.bean.SLFConstants;
import com.sandglass.sandglasslibrary.moudle.SLFMediaData;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFLeaveMsgRecord;
import com.sandglass.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Greated by yangjie
 * describe:上传File的工具类
 * time:2022/12/21
 */
public class SLFSubmitFile {

    private static final String TAG = "SLFSubmitFile";

    private static OnUploadSuccessListener uploadSuccessListener;

    private static Activity mActivity;

    private static SLFFileData iotFileData;
    private static SLFFileData bluetoothFileData;

    private static String type; // 1.iot设备，2.蓝牙设备, 3.同时是iot和蓝牙

    /**
     * 上传log
     */
    public static void submitLogissue(Activity activity,String url) {
        mActivity = activity;
        new Thread(() -> {

            final List<SLFFileData> fileList = new ArrayList<>();


            SLFFileData appLogFileData = new SLFFileData();
            appLogFileData.setFile_path(SLFConstants.feedbacklogPath + "appLog.zip");
            appLogFileData.setFile_name(SLFConstants.appLogName);
            appLogFileData.setSize(new File(SLFConstants.feedbacklogPath + "appLog.zip").length());
            appLogFileData.setType("app_log");
            appLogFileData.setContent_type("application/zip");
            fileList.add(appLogFileData);

            /**运行在主线程上传文件列表*/
            activity.runOnUiThread(() -> reqUploadUrl(fileList,url));

        }).start();
    }

    /**
     * 上传多媒体文件
     */
    public static void submitMediaDataissue(Activity activity, final List<SLFMediaData> list,String url) {
        mActivity = activity;
        new Thread(() -> {
            final List<SLFFileData> fileList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                SLFFileData fileData = new SLFFileData();
                fileData.setFile_path(list.get(i).getOriginalPath());
                if (list.get(i).getMimeType().contains("video")) {
                    fileData.setFile_name(list.get(i).getId() + ".mp4");
                } else if (list.get(i).getMimeType().contains("gif")) {
                    fileData.setFile_name(list.get(i).getId() + ".gif");
                } else if (list.get(i).getMimeType().contains("png")) {
                    fileData.setFile_name(list.get(i).getId() + ".png");
                } else {
                    fileData.setFile_name(list.get(i).getId() + ".jpeg");
                }
                fileData.setContent_type(list.get(i).getMimeType());
                fileData.setDuration(list.get(i).getDuration());
                fileData.setSize(list.get(i).getLength());
                if (list.get(i).getMimeType().contains("image")) {
                    fileData.setType("image");
                } else if (list.get(i).getMimeType().contains("video")) {
                    fileData.setType("video");
                }
                fileList.add(fileData);
            }
            /**运行在主线程上传文件列表*/
            activity.runOnUiThread(() -> reqUploadUrl(fileList,url));
        }).start();
    }

    /**
     * 若有固件log
     */
    public static void sumbmitFirmwareLog(Activity activity,String url) {
        new Thread(() -> {

                final List<SLFFileData> fileList = new ArrayList<>();
                if ("1".equals(type)) {
                    iotFileData = new SLFFileData();
                    iotFileData.setFile_name(SLFConstants.firmwareLogName);
                    iotFileData.setType("device_log");
                    iotFileData.setContent_type("application/octet-stream");
                    fileList.add(iotFileData);
                } else if ("2".equals(type)) {
                    bluetoothFileData = new SLFFileData();
                    bluetoothFileData.setFile_name(SLFConstants.firmwareLogName);
                    bluetoothFileData.setType("device_log");
                    bluetoothFileData.setContent_type("application/octet-stream");
                    fileList.add(bluetoothFileData);
                } else if ("3".equals(type)) {
                    iotFileData = new SLFFileData();
                    iotFileData.setFile_name(SLFConstants.firmwareIOTLogName);
                    iotFileData.setType("device_log");
                    iotFileData.setContent_type("application/octet-stream");
                    fileList.add(iotFileData);
                    bluetoothFileData = new SLFFileData();
                    bluetoothFileData.setFile_name(SLFConstants.firmwareBluetoothLogName);
                    bluetoothFileData.setType("device_log");
                    bluetoothFileData.setContent_type("application/octet-stream");
                    fileList.add(bluetoothFileData);
                }
                /**运行在主线程上传文件列表*/
                activity.runOnUiThread(() -> reqUploadUrl(fileList,url));
        }).start();
    }

    /**
     * 请求上传的url
     */
    private static void reqUploadUrl(final List<SLFFileData> fileList,String url) {

        //showloading;
        if (fileList == null || fileList.isEmpty()) {
            uploadFiles(fileList);
            return;
        }
        List<JSONObject> list = new ArrayList<>();

        try {
            for (int i = 0; i < fileList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type", fileList.get(i).getType());
                jsonObject.put("content_type", fileList.get(i).getContent_type());
                jsonObject.put("origin_name", fileList.get(i).getFile_name());
                jsonObject.put("source_type", 1);
                jsonObject.put("size", fileList.get(i).getSize());
                jsonObject.put("duration", fileList.get(i).getDuration());
                list.add(jsonObject);
            }
        } catch (JSONException e) {
            SLFLogUtil.sdke(TAG, e.getMessage());
        }

        //TODO REQUST URL

    }

    /**
     * upload
     */
    private static void uploadFiles(final List<SLFFileData> fileList) {

        //showLoading;


        uploadSuccessListener = new OnUploadSuccessListener() {
            @Override
            public void onSuccess() {
                //TODO EventBus.getDefault().post(new SLFUploadSuccess())
                //EventBus.getDefault().post(new SLFUploadSuccess());
            }

            @Override
            public void onFailure() {
                //hideLoading();
                //TODO EventBus.getDefault().post(new SLFUploadSuccess())
                Toast.makeText(mActivity.getApplicationContext(), "upload file error", Toast.LENGTH_SHORT).show();
                SLFLogUtil.sdke(TAG, "upload faid");
            }
        };

        for (int i = 0; i < fileList.size(); i++) {
            //TODO 遍历filelist 请求网络上传文件
        }


    }

    protected interface OnUploadSuccessListener {
        void onSuccess();

        void onFailure();
    }
}
