package com.ryan.mvp.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ryan.lifecycle.LifeCycleComponent;
import com.ryan.mvp.model.IModel;
import com.ryan.mvp.view.IView;

public abstract class BasePresenter<V extends IView, M extends IModel> extends LifeCycleComponent implements IPresenter {

    @Nullable
    protected V mView;
    @Nullable
    protected M mModel;

    public BasePresenter(@NonNull V view) {
        super(view.getLifeCycle());
        this.mView = view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mView != null) {
            mView = null;
        }
        if (mModel != null) {
            mModel = null;
        }
    }
}
