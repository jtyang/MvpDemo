package com.ryan.mvpdemo;

import android.graphics.Bitmap;

import com.ryan.mvp.view.IView;

public interface IUserView extends IView {
    void onFetchSuccess(String username, String sex, String city, String company, String id, Bitmap bitmap);
}
