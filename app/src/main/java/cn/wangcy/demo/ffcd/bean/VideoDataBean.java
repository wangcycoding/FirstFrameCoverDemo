package cn.wangcy.demo.ffcd.bean;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import cn.wangcy.demo.ffcd.contants.ContentsUrl;
import cn.wangcy.demo.ffcd.db.DbManager;

/**
 * Created by wangchuanyu on 2018/7/12.
 */

public class VideoDataBean {

    private String mTitle;
    private String mVideoUrl;
    private String mVideoCover;
    private Bitmap mBitmap;
    private DbManager manager;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmVideoUrl() {
        return mVideoUrl;
    }

    public void setmVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }

    public String getmVideoCover() {
        return mVideoCover;
    }

    public void setmVideoCover(String mVideoCover) {
        this.mVideoCover = mVideoCover;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public List<VideoDataBean> putData(Context context){
        List<VideoDataBean> mdatas = new ArrayList<>();
        VideoDataBean bean;
        manager = new DbManager(context);
        for (int i = 0;i<3;i++){
            bean = new VideoDataBean();
            bean.setmTitle("TVB倚天屠龙记第"+(i+1)+"集");
            bean.setmVideoUrl(ContentsUrl.videoUrls[i]);
            bean.setmVideoCover("file://null");
            mdatas.add(bean);
        }
        return mdatas;
    }

    @Override
    public String toString() {
        return "VideoDataBean{" +
                "mTitle='" + mTitle + '\'' +
                ", mVideoUrl='" + mVideoUrl + '\'' +
                ", mVideoCover='" + mVideoCover + '\'' +
                '}';
    }
}
