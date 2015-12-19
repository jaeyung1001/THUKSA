package com.community.tsinghua.app;

import android.app.Application;

import com.community.tsinghua.helper.ParseUtils;

/**
 * Created by LG on 2015-11-25.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // register with parse
        ParseUtils.registerParse(this);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
