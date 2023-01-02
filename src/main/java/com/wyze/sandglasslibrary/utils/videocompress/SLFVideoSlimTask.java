package com.wyze.sandglasslibrary.utils.videocompress;

import android.os.AsyncTask;

import com.wyze.sandglasslibrary.utils.videocompress.listner.SLFSlimProgressListener;


/**
 * A asyncTask to convert video
**/
public class SLFVideoSlimTask extends AsyncTask<Object, Float, Boolean> {
    private SLFVideoSlimmer.SLFProgressListener mListener;


    public SLFVideoSlimTask(SLFVideoSlimmer.SLFProgressListener listener) {
        mListener = listener;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected Boolean doInBackground(Object... paths) {
        return new SLFVideoSlimEncoder().convertVideo((String)paths[0], (String)paths[1], (Float) paths[2],(Float) paths[3],(Float) paths[4], new SLFSlimProgressListener() {
            @Override
            public void onProgress(float percent) {
                publishProgress(percent);
            }
        });
    }

    @Override
    protected void onProgressUpdate(Float... percent) {
        super.onProgressUpdate(percent);
        if (mListener != null) {
            mListener.onProgress(percent[0]);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (mListener != null) {
            if (result) {
                mListener.onFinish(true);
            } else {
                mListener.onFinish(false);
            }
        }
    }
}
