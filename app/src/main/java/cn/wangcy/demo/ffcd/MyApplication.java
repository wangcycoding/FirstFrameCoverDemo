package cn.wangcy.demo.ffcd;

import android.app.Application;
import android.content.Context;

/**
 * Created by wangchuanyu on 2018/7/13.
 */

public class MyApplication extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
    }

}
