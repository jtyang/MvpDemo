package com.ryan.mvpdemo;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.ryan.mvp.presenter.BasePresenter;

public class UserPresenter extends BasePresenter<IUserView, IUserModel> implements IUserPresenter {
    UserPresenter(@NonNull IUserView view) {
        super(view);

        mModel = new UserModel(view.getLifeCycle(), this);
    }

    @Override
    public void fetch() {
        if (mModel != null) {
            mModel.fetch();
        }
    }

    @Override
    public void onFetchSuccess(String username, String sex, String city, String company, String id, Bitmap bitmap) {
        if (mView != null) {
            mView.onFetchSuccess(username, sex, city, company, id, bitmap);
        }
    }

}
