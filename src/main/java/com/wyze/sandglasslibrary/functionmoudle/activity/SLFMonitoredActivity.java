package com.wyze.sandglasslibrary.functionmoudle.activity;

import android.os.Bundle;

import com.wyze.sandglasslibrary.base.SLFPhotoBaseActivity;

import java.util.ArrayList;

public abstract class SLFMonitoredActivity extends SLFPhotoBaseActivity {

    private final ArrayList<LifeCycleListener> listeners = new ArrayList<LifeCycleListener>();

    public static interface LifeCycleListener {
        public void onActivityCreated(SLFMonitoredActivity activity);
        public void onActivityDestroyed(SLFMonitoredActivity activity);
        public void onActivityStarted(SLFMonitoredActivity activity);
        public void onActivityStopped(SLFMonitoredActivity activity);
    }

    public static class LifeCycleAdapter implements LifeCycleListener {
        @Override
        public void onActivityCreated(SLFMonitoredActivity activity) {}
        @Override
        public void onActivityDestroyed(SLFMonitoredActivity activity) {}
        @Override
        public void onActivityStarted(SLFMonitoredActivity activity) {}
        @Override
        public void onActivityStopped(SLFMonitoredActivity activity) {}
    }

    public void addLifeCycleListener(LifeCycleListener listener) {
        if (listeners.contains(listener)) {return;}
        listeners.add(listener);
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {
        listeners.remove(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (LifeCycleListener listener : listeners) {
            listener.onActivityCreated(this);
        }
    }

    @Override
    public void initContentView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (LifeCycleListener listener : listeners) {
            listener.onActivityDestroyed(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (LifeCycleListener listener : listeners) {
            listener.onActivityStarted(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (LifeCycleListener listener : listeners) {
            listener.onActivityStopped(this);
        }
    }

}
