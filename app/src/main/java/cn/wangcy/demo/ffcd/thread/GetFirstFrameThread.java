package cn.wangcy.demo.ffcd.thread;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.util.List;
import java.util.logging.Handler;

import cn.wangcy.demo.ffcd.bean.VideoDataBean;
import cn.wangcy.demo.ffcd.db.DbManager;
import cn.wangcy.demo.ffcd.utils.ToastUtils;

/**
 * Created by wangchuanyu on 2018/7/13.
 */

public class GetFirstFrameThread extends Thread {

    private Handler mUIHander;
    private List<VideoDataBean> mDatas;
    private final String outPutFileDirPath = Environment.getExternalStorageDirectory()+"ExFirstFrame";
    private Context mContext;
    private DbManager dbManager;
    private int index = 0;


    public GetFirstFrameThread(Context mContext,Handler mUIHander, List<VideoDataBean> mDatas,int index) {
        this.mContext = mContext;
        this.mUIHander = mUIHander;
        this.mDatas = mDatas;
        this.index=index;
    }

    @Override
    public void run() {
        super.run();
        long fileLength = 0;
        File file = null;
        try{
            file = new File(outPutFileDirPath);
            if (!file.exists()){
                file.mkdir();
            }
        }catch (Exception e){
            ToastUtils.show(e.toString());
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        synchronized (this){
            for (int i = index;i<mDatas.size();i++){

            }
        }




    }
    private void extractFrame(MediaMetadataRetriever retriever){



    }
    private String saveSDImage(){

        return "";
    }
}
