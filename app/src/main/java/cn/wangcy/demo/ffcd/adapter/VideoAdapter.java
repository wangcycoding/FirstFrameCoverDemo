package cn.wangcy.demo.ffcd.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import cn.wangcy.demo.ffcd.R;
import cn.wangcy.demo.ffcd.bean.VideoDataBean;
import cn.wangcy.demo.ffcd.db.DbManager;
import cn.wangcy.demo.ffcd.thread.CreateFirstFrameBitmapThread;
import cn.wangcy.demo.ffcd.utils.LogUtils;

/**
 * Created by wangchuanyu on 2018/7/12.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context context;
    private List<VideoDataBean> mDatas;
    private CreateFirstFrameBitmapThread thread;
    private DbManager manager;

    public VideoAdapter(Context context, List<VideoDataBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        manager = new DbManager(context);
        for (int i = 0; i < mDatas.size(); i++) {
            LogUtils.d("gggggggg", "从数据库查数：" + manager.findImageUrl(mDatas.get(i)));
            if (TextUtils.equals(manager.findImageUrl(mDatas.get(i)), "file://null")) {
                LogUtils.d("gggggggg", "从数据库查数1 : " + manager.findImageUrl(mDatas.get(i)));
                thread = new CreateFirstFrameBitmapThread(context, mDatas, handler, i);
                thread.start();
                break;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(
                context).inflate(R.layout.adapter_video_recyclerview, parent,
                false));
//        View view = LayoutInflater.from(context).inflate(R.layout.adapter_video_recyclerview, parent, false);
        return holder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.videoView.setUp(mDatas.get(position).getmVideoUrl(), JZVideoPlayerStandard.SCREEN_WINDOW_LIST, mDatas.get(position).getmTitle());
        LogUtils.d("ffffffffff", "fffffffVideoAdapter1: " + manager.findImageUrl(mDatas.get(position)));
        if (!TextUtils.isEmpty(manager.findImageUrl(mDatas.get(position)))) {
            LogUtils.d("ffffffffff", "fffffffVideoAdapter1: " + manager.findImageUrl(mDatas.get(position)));
            Glide.with(context).asBitmap().load(manager.findImageUrl(mDatas.get(position))).into(holder.videoView.thumbImageView);
        } else{
            if (!TextUtils.isEmpty(mDatas.get(position).getmVideoCover())){
                LogUtils.d("ffffffffff", "fffffffVideoAdapter2: " + manager.findImageUrl(mDatas.get(position)));
                Glide.with(context).asBitmap().load(mDatas.get(position).getmVideoCover()).into(holder.videoView.thumbImageView);
            }
        }
        LogUtils.d(mDatas.get(position).getmTitle() + " " + mDatas.get(position).getmVideoUrl() + " position: " + position);
    }


    /**
     * 在更新UI的方法
     */
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:  //接到从线程内传来的图片bitmap和imageView.
                    //这里只是将bitmap传到imageView中就行了。只所以不在线程中做是考虑到线程的安全性
                    mDatas.get(msg.arg1).setmVideoCover("file://" + msg.obj);
                try {
                    if (!TextUtils.equals(mDatas.get(msg.arg1).getmVideoCover(), "file://null"))
                        manager.addImage(mDatas.get(msg.arg1));

                }catch (Exception ex){
                }
                    notifyItemChanged(msg.arg1);
//                    notifyDataSetChanged();


                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }

        ;
    };


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        JZVideoPlayerStandard videoView;

        public ViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
//            videoView.thumbImageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    public Bitmap createBitmapFromVideoPath(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 12) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
            ex.printStackTrace();
            LogUtils.d(ex.getMessage());
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
            ex.printStackTrace();
            LogUtils.d(ex.getMessage());
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
                ex.printStackTrace();
                LogUtils.d(ex.getMessage());
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

}
