package com.ryan;

import android.app.Application;
import android.content.Context;

public final class AppUtils {
    private static Context mContext;

    static void init(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
}
