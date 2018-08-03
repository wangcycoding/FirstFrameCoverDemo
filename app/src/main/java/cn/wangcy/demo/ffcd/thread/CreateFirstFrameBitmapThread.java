package cn.wangcy.demo.ffcd.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.wangcy.demo.ffcd.bean.VideoDataBean;
import cn.wangcy.demo.ffcd.db.DbManager;
import cn.wangcy.demo.ffcd.utils.PictureUtils;

/**
 * Created by wangchuanyu on 2018/7/16.
 */

public class CreateFirstFrameBitmapThread extends Thread {

    private List<VideoDataBean> mDatas;
    private int width = 0;
    private int height = 0;
    private ImageView videoCover = null;
    private final String OutPutFileDirPath = Environment.getExternalStorageDirectory() + "/first_frame";
    private Handler handler;
    private DbManager manager;
    private long fileLength;
    private int posIndex = 0;


    public CreateFirstFrameBitmapThread(List<VideoDataBean> mDatas, int width, int height, ImageView videoCover, Handler handler) {
        this.mDatas = mDatas;
        this.width = width;
        this.height = height;
        this.videoCover = videoCover;
        this.handler = handler;
    }

    public CreateFirstFrameBitmapThread(Context context, List<VideoDataBean> mDatas, Handler handler) {
        this.mDatas = mDatas;
        this.handler = handler;
        manager = new DbManager(context);
    }

    public CreateFirstFrameBitmapThread(Context context, List<VideoDataBean> mDatas, Handler handler, int i) {
        this.mDatas = mDatas;
        this.handler = handler;
        this.posIndex = i;
        manager = new DbManager(context);
    }

    public CreateFirstFrameBitmapThread() {

    }

    @Override
    public void run() {
        super.run();
        for (int i = posIndex; i < mDatas.size(); i++) {
            createBitmapFromVideoPath(i, mDatas.get(i).getmVideoUrl());
        }
    }

    /**
     * 获取第一帧的方法，通过上方for循环进行获取第一帧
     * @param positon
     * @param url
     */
    public void createBitmapFromVideoPath(int positon, String url) {
        synchronized (this) {
            Bitmap bitmap = null;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                if (Build.VERSION.SDK_INT >= 12) {
                    retriever.setDataSource(url, new HashMap<String, String>());
                } else {
                    retriever.setDataSource(url);
                }
                bitmap = retriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                String len = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                fileLength = TextUtils.isEmpty(len) ? 0 : Long.valueOf(len);

                Bitmap bitmapNew = /*scaleImage(bitmap)*/bitmap;
//                Bitmap bitmapNew = /*scaleImage(bitmap)*/zoomImage(bitmap,100,100);
                String path = PictureUtils.saveImageToSDForEdit(bitmapNew, OutPutFileDirPath, positon + "_FirstFrame_ffcd"  + PictureUtils.POSTFIX);
//            sendPic(bitmapNew, positon);
                sendPic(path, bitmapNew, positon);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {

            } finally {
                try {
                    retriever.release();
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    /**
     * 压缩图片
     * @param bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomImage(Bitmap bitmap, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * 发送图片到handler，更新UI
     * @param path
     * @param bitmap
     * @param positon
     */
    public void sendPic(String path, Bitmap bitmap, int positon) {
        Message msg = new Message();
        msg.what = 0;
        msg.arg1 = positon;
        msg.obj = path;
        handler.sendMessage(msg);
    }

    /**
     * 发送图片到handler，更新UI(以list形式传递数据，主要用于更新bitmap类型的数据)
     * @param bitmap
     * @param positon
     */
    public void sendPic(Bitmap bitmap, int positon) {
        Message msg = new Message();
        msg.what = 0;
        List list = new ArrayList();
        list.add(bitmap);
        msg.obj = list;  //handler传递对象
        msg.arg1 = positon;
        handler.sendMessage(msg);
    }
}
