package com.sandglass.sandglasslibrary.utils.videocompress;


import android.os.AsyncTask;

/**
 * Video Slimmer
 * a library to convert video to smaller mp4 file
 * can set new width ,height, and bitrate ,progress listner
 */
public class SLFVideoSlimmer {

    public static SLFVideoSlimTask currentTask;
    public static SLFVideoSlimTask convertVideo(String srcPath, String destPath, float outputWidth, float outputHeight, float bitrate, SLFProgressListener listener) {
        SLFVideoSlimTask task = new SLFVideoSlimTask(listener);
        currentTask = task;
        task.execute(srcPath, destPath, outputWidth, outputHeight, bitrate);
        return task;
    }


    public static void stopConvertVideo(){
        if(currentTask!=null&&currentTask.getStatus() == AsyncTask.Status.RUNNING){
            currentTask.cancel(true);
        }
    }

    public static interface SLFProgressListener {

        void onStart();
        void onFinish(boolean result);
        void onProgress(float progress);

    }

}
