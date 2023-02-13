package com.sandglass.sandglasslibrary.utils.videocompress;


/**
 * Video Slimmer
 * a library to convert video to smaller mp4 file
 * can set new width ,height, and bitrate ,progress listner
 */
public class SLFVideoSlimmer {


    public static SLFVideoSlimTask convertVideo(String srcPath, String destPath, float outputWidth, float outputHeight, float bitrate, SLFProgressListener listener) {
        SLFVideoSlimTask task = new SLFVideoSlimTask(listener);
        task.execute(srcPath, destPath, outputWidth, outputHeight, bitrate);
        return task;
    }


    public static interface SLFProgressListener {

        void onStart();
        void onFinish(boolean result);
        void onProgress(float progress);

    }

}
