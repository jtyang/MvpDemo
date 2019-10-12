package com.ryan.mvp.model;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.ryan.lifecycle.LifeCycleComponent;
import com.ryan.mvp.presenter.IPresenter;

public class BaseModel<P extends IPresenter> extends LifeCycleComponent implements IModel{
    @Nullable
    protected P mPresenter;

    public BaseModel(@Nullable Lifecycle mLifeCycle, @Nullable P presenter) {
        super(mLifeCycle);
        this.mPresenter = presenter;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPresenter != null) {
            mPresenter = null;
        }

    }
}
