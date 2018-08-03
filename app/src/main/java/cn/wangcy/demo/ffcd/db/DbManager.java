package cn.wangcy.demo.ffcd.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

import cn.wangcy.demo.ffcd.bean.VideoDataBean;
import cn.wangcy.demo.ffcd.utils.LogUtils;

/**
 * Created by wangchuanyu on 2018/7/13.
 */

public class DbManager {
    private DbHelper dbHelper;
    private SQLiteDatabase db;


    public DbManager(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    public void addImage(VideoDataBean bean){
        if (null == bean)
            return;
        //添加判断，如果存在就更新
        if (!TextUtils.isEmpty(findImageUrl(bean))&&!TextUtils.isEmpty(findImageUrl(bean))&&!findImageUrl(bean).equals("file://null")){
            updateImageUrl(bean);
            return;
        }


        ContentValues cv = new ContentValues();
        LogUtils.d("rrrrrrr",bean.getmVideoCover());
        LogUtils.d("rrrrrrr",bean.getmVideoUrl());
        cv.put("image_url",bean.getmVideoCover());
        cv.put("video_url",bean.getmVideoUrl());
        long id = db.insert(DbHelper.TABLE_NAME,null, cv);




        Cursor cursor = db.query(DbHelper.TABLE_NAME, null, "video_url=?" + " and " + "image_url=?", new String[]{bean.getmVideoUrl(), bean.getmVideoCover()}, null, null, null);
        ArrayList<HashMap<String, String>> AL = new ArrayList<HashMap<String, String>>();
        if (cursor.moveToNext()) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("video_url", cursor.getString(cursor.getColumnIndex("video_url")));
                hm.put("image_url", cursor.getString(cursor.getColumnIndex("image_url")));
                AL.add(hm);
            }
            cursor.close();
        }
        if (id != -1) LogUtils.d("zzzzzzzzz", "插入成功  " + AL.toString());
        else LogUtils.d("zzzzzzzzz", "插入失败");


    }
    public String findVideoUrl(VideoDataBean bean){
        if (bean==null){
            return null;
        }
        Cursor cursor = db.query(DbHelper.TABLE_NAME,null, "video_url=?" + " and " + "image_url=?",new String[]{bean.getmVideoUrl(),bean.getmVideoUrl()},null,null,null);
        while (cursor.moveToNext()){
            return cursor.getString(0);
        }
        cursor.close();
        return null;
    }
    public String findImageUrl(VideoDataBean bean){
        if (bean==null){
            return null;
        }
        try{
            Cursor cursor = db.query(DbHelper.TABLE_NAME,null, "video_url=?" + " and " + "image_url=?",new String[]{bean.getmVideoUrl(),bean.getmVideoCover()},null,null,null);
            while (cursor.moveToNext()){
                LogUtils.d("yyyyyyyyy","image_url:"+cursor.getString(cursor.getColumnIndex("image_url")));
                return cursor.getString(cursor.getColumnIndex("image_url"));
            }
        }catch (Exception e){
            LogUtils.d("yyyyyyyyy","ex:"+e.toString());
            return "file://null";
        }

        return "file://null";
    }
    public void updateImageUrl(VideoDataBean bean){
        ContentValues cv = new ContentValues();
        cv.put("video_url",bean.getmVideoUrl());
        cv.put("image_url",bean.getmVideoCover());
        db.update(DbHelper.TABLE_NAME,cv,"image_url=?",new String[]{bean.getmVideoUrl()});
    }

}
