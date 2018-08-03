package cn.wangcy.demo.ffcd.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.wangcy.demo.ffcd.R;
import cn.wangcy.demo.ffcd.adapter.VideoAdapter;
import cn.wangcy.demo.ffcd.bean.VideoDataBean;
import cn.wangcy.demo.ffcd.db.DbManager;
import cn.wangcy.demo.ffcd.utils.LogUtils;
import cn.wangcy.demo.ffcd.utils.ToastUtils;

public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private List<VideoDataBean> mVideoBeans;
    private final String OutPutFileDirPath = Environment.getExternalStorageDirectory() + "/first_frame";
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    DbManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rlv_main);
        mVideoBeans = new ArrayList<>();
        mVideoBeans.addAll(new VideoDataBean().putData(this));
        getPermissions();
        manager = new DbManager(this);
        if (!TextUtils.equals(manager.findImageUrl(mVideoBeans.get(mVideoBeans.size() - 1)), "file://null")) {
            setImageIsNull(mVideoBeans);
        }
        VideoAdapter adapter = new VideoAdapter(this, mVideoBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                JZVideoPlayer.onChildViewAttachedToWindow(view, R.id.videoView);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                JZVideoPlayer.onChildViewDetachedFromWindow(view);
            }
        });
    }

    private void setImageIsNull(List<VideoDataBean> mDatas) {
        for (int j = 0; j < mDatas.size(); j++) {
            mDatas.get(j).setmVideoCover(manager.findImageUrl(mDatas.get(j)));
            LogUtils.d("hhhhhhhh", "fffffffVideoAdapter4: " + manager.findImageUrl(mDatas.get(j))+" "+mDatas.toString());
        }
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(this, mPermissionList, 12000);
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.goOnPlayOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        deleteFile(new File(OutPutFileDirPath));//用于删除缓存下来的文件及文件夹
    }

    public static void deleteFile(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; ++i) {
                    deleteFile(files[i]);
                }
            }
        }
        f.delete();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 12000) {
            ToastUtils.show("请求权限成功");
        }
    }
}
