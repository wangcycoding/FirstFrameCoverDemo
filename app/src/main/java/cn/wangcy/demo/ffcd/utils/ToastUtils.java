package cn.wangcy.demo.ffcd.utils;

import android.widget.Toast;

import cn.wangcy.demo.ffcd.MyApplication;

/**
 * Created by wangchuanyu on 2018/7/13.
 */

public class ToastUtils {

    public static void show(String toastStr){
        Toast.makeText(MyApplication.applicationContext,toastStr,Toast.LENGTH_SHORT).show();
    }



}
