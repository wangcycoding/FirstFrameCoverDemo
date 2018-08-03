package cn.wangcy.demo.ffcd.listenter;

import java.util.List;

/**
 * Created by wangchuanyu on 2018/7/18.
 */

public interface PermissionListener {
    void granted();
    void denied(List<String> deniedList);
}
