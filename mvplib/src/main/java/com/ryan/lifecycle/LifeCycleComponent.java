package com.ryan.lifecycle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.ref.WeakReference;

public class LifeCycleComponent implements LifecycleObserver {
    @Nullable
    private WeakReference<Lifecycle> mLifeCycle;

    public LifeCycleComponent(@Nullable Lifecycle mLifeCycle) {
        this.mLifeCycle = new WeakReference<>(mLifeCycle);
        addLifeCycle();
    }

    private void addLifeCycle() {
        Lifecycle lifecycle = null;
        if (mLifeCycle != null) {
            lifecycle = mLifeCycle.get();
        }
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {}


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {}


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {}


    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onAny() {}

}
