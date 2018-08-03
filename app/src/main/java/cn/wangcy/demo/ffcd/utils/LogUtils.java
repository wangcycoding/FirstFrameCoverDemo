package cn.wangcy.demo.ffcd.utils;

import android.util.Log;

/**
 * Created by wangchuanyu on 2018/7/13.
 */

public class LogUtils {
    private static boolean isPrint = true;
    public static final String TAG = "LogUtils";

    public static void v(String content) {
        if (isPrint) {
            Log.v(TAG, content);
        }
    }

    public static void v(String tag, String content) {
        if (isPrint)
            Log.v(tag, content);
    }

    public static void d(String content) {
        if (isPrint)
            Log.d(TAG, content);
    }

    public static void d(String tag, String content) {
        if (isPrint)
            Log.d(tag, content);
    }
    public static void i(String content) {
        if (isPrint)
            Log.i(TAG, content);
    }

    public static void i(String tag, String content) {
        if (isPrint)
            Log.i(tag, content);
    }
    public static void w(String content) {
        if (isPrint)
            Log.w(TAG, content);
    }

    public static void w(String tag, String content) {
        if (isPrint)
            Log.w(tag, content);
    }
    public static void e(String content) {
        if (isPrint)
            Log.e(TAG, content);
    }

    public static void e(String tag, String content) {
        if (isPrint)
            Log.e(tag, content);
    }
}
