package com.ryan.mvp.view;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.ryan.mvp.presenter.IPresenter;

public class BaseFragment<P extends IPresenter> extends Fragment implements IView {
    @Nullable
    protected P mPresenter;

    @Override
    public Lifecycle getLifeCycle() {
        return super.getLifecycle();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter = null;
        }
    }
}
