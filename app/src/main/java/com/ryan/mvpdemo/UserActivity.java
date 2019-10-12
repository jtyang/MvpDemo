package com.ryan.mvpdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ryan.mvp.view.BaseActivity;

public class UserActivity extends BaseActivity<IUserPresenter> implements IUserView, View.OnClickListener {
    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private TextView mTvResult;
    private ImageView mIvResult;
    private AlertDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new UserPresenter(this);
        mLoadingDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("正在请求中，请稍后...")
                .create();


        mTvResult = findViewById(R.id.tv_result);
        mIvResult = findViewById(R.id.iv_result);
        findViewById(R.id.btn_fetch).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fetch:
                if (mPresenter != null) {
                    mLoadingDialog.show();
                    mPresenter.fetch();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public  void onFetchSuccess(final String username, final String sex, final String city, final String company, final String id, final Bitmap bitmap) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoadingDialog.dismiss();
                String result = "用户名：" + username + "\n" +
                        "性别：" + sex + "\n" +
                        "城市：" + city + "\n" +
                        "公司：" + company + "\n" +
                        "id：" + id + "\n";
                mTvResult.setText(result);
                mIvResult.setImageBitmap(bitmap);
            }
        });
    }

}
