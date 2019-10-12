package com.ryan.mvpdemo;

import android.graphics.Bitmap;

import com.ryan.mvp.presenter.IPresenter;

public interface IUserPresenter extends IPresenter {
    void fetch();

    void onFetchSuccess(String username, String sex, String city, String company, String id, Bitmap bitmap);
}
