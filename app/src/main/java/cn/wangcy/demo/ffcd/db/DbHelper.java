package cn.wangcy.demo.ffcd.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangchuanyu on 2018/7/13.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "first_frame_table";
    private static final String name = "first_frame";
    private static final int version_num = 1;




    public DbHelper(Context context) {
        super(context, name, null, version_num);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (id integer primary key autoincrement, image_url text, video_url text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
