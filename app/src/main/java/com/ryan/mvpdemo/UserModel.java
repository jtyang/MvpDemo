package com.ryan.mvpdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.ryan.AppUtils;
import com.ryan.mvp.model.BaseModel;

import java.util.Random;

public class UserModel extends BaseModel<IUserPresenter> implements IUserModel {
    private String[] names = {"小明", "老王", "张三", "李四", "陈五"};
    private String[] sexs = {"男", "女"};
    private String[] cities = {"广州", "北京", "上海", "重庆", "深圳"};
    private String[] ids = {"10000", "10001", "10002", "10003", "10004"};
    private String[] companys = {"阿里巴巴", "腾讯", "京东", "百度", "华为"};
    private int[] head= {R.drawable.test1, R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test5};

    private int mPreIndex = 0;


    UserModel(@Nullable Lifecycle lifecycle, @Nullable IUserPresenter presenter) {
        super(lifecycle, presenter);
    }

    @Override
    public void fetch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    // 模拟耗时操作
                    if (mPresenter != null) {
                        Random random = new Random();
                        int index = 0;
                        while(index == mPreIndex) {
                            index = random.nextInt(5);
                        }
                        mPreIndex = index;
                        String username = names[index];
                        String id = ids[index];
                        String city = cities[index];
                        String company = companys[index];
                        Bitmap bitmap = BitmapFactory.decodeResource(AppUtils.getContext().getResources(), head[index]);
                        index = random.nextInt(2);
                        String sex = sexs[index];
                        mPresenter.onFetchSuccess(username, sex, city, company, id, bitmap);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
