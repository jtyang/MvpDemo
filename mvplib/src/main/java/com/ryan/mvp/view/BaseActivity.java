package com.ryan.mvp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;

import com.ryan.mvp.presenter.IPresenter;

public class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView {
    @Nullable
    protected  P mPresenter;

    @Override
    public Lifecycle getLifeCycle() {
        return super.getLifecycle();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter = null;
        }
    }
}
